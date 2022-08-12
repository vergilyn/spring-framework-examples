# RocketMQ consumer graceful-shutdown

## 问题描述
服务器重启时，执行中的mq-listener-consumer未正确执行完成。

例如，consumer中会请求 redis/database/dubbo/mq-sender 等资源，但是这些资源的connection被提前关闭了，导致consumer消费失败。

但在实际业务系统中，**不一定可以简单的重复消费**。
例如发送短信，请求短信平台接口已成功（用户收到了短信），但最后消息记录写入到MQ/database失败，因为connection被提前关闭。
此场景下，不能重复消费（会导致用户收到重复的短信）。但如果标记MQ消费成功，会导致消息记录丢失。

### 相关问题参考
- issue#2085: [Support graceful shutdown for push consumer](https://github.com/apache/rocketmq/issues/2085)
- pull#2084: [[ISSUE#2085]support graceful shutdown push consumer](https://github.com/apache/rocketmq/pull/2084)
- <https://github.com/apache/rocketmq/issues/4602>

社区版RocketMQ，貌似在`rocketMQ: 4.8.0`引入 graceful-shutdown(优雅停机)。
即consumer增加了一个可配置参数`awaitTerminationMillisWhenShutdown`（[DefaultMQPushConsumer.java
](https://github.com/apache/rocketmq/pull/2084/files/c77e123106b98fa798606db3485e1dd2c62f0cfe)）：多少毫秒后，强制中断consumer-thread。
可以根据实际情况调整此参数，保证consumer执行完成。

## springboot shutdown-hook 流程分析
springboot接收到shutdown信号时，会调用 `org.springframework.context.support.AbstractApplicationContext#close()`

```JAVA
public abstract class AbstractApplicationContext{
    protected void doClose() {
        // Check whether an actual close attempt is necessary...
        if (this.active.get() && this.closed.compareAndSet(false, true)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Closing " + this);
            }
    
            LiveBeansView.unregisterApplicationContext(this);
    
            try {
                // Publish shutdown event.
                publishEvent(new ContextClosedEvent(this));
            }
            catch (Throwable ex) {
                logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
            }
    
            // Stop all Lifecycle beans, to avoid delays during individual destruction.
            if (this.lifecycleProcessor != null) {
                try {
                    this.lifecycleProcessor.onClose();
                }
                catch (Throwable ex) {
                    logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
                }
            }
    
            // Destroy all cached singletons in the context's BeanFactory.
            destroyBeans();
    
            // Close the state of this context itself.
            closeBeanFactory();
    
            // Let subclasses do some final clean-up if they wish...
            onClose();
    
            // Reset local application listeners to pre-refresh state.
            if (this.earlyApplicationListeners != null) {
                this.applicationListeners.clear();
                this.applicationListeners.addAll(this.earlyApplicationListeners);
            }
    
            // Switch to inactive.
            this.active.set(false);
        }
    }
}
```

主要需要关心 `ContextClosedEvent & destroyBeans()`。

### shutdown
#### mq-consumer未按期望执行完成分析
1. `destroyBeans()`
```text
org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory,
defaultMQProducer,
rocketMQTemplate,
lettuceClientResources,
redisConnectionFactory,
org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer_1       // 假设，MQ-consumer 在此位置触发shutdown
jvmGcMetrics,
logbackMetrics,
simpleMeterRegistry,
redisKeyValueAdapter,
redisKeyValueTemplate,      // 假设 会shutdown redis-connection
defaultValidator,
```

根据以上**倒序销毁**。并且，例如 假设`redisKeyValueTemplate` 依赖 redis-connection，会先销毁 redis-connection，再销毁 redisKeyValueTemplate。
参考代码：
```
org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#destroySingletons()
org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#destroySingleton()
org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#destroyBean()  // 其中会优先调用依赖bean的 destroy-method。
```

2. `DefaultMQPushConsumerImpl#shutdown`

社区版/商业版，最后都类似
```java
public class DefaultMQPushConsumerImpl implements MQConsumerInner {
	public synchronized void shutdown() {
		switch (this.serviceState) {
			case CREATE_JUST:
				break;
			case RUNNING:
				this.consumeMessageService.shutdown();  // consumer主要关心此代码
				this.persistConsumerOffset();
				this.mQClientFactory.unregisterConsumer(this.defaultMQPushConsumer.getConsumerGroup());
				this.mQClientFactory.shutdown();
				log.info("the consumer [{}] shutdown OK", this.defaultMQPushConsumer.getConsumerGroup());
				this.rebalanceImpl.destroy();
				this.serviceState = ServiceState.SHUTDOWN_ALREADY;
				break;
			case SHUTDOWN_ALREADY:
				break;
			default:
				break;
		}
	}
}

// `consumeMessageService`区分：ConsumeMessageConcurrentlyService、ConsumeMessageOrderlyService
//   主要需要关注的代码都一样
public class ConsumeMessageOrderlyService implements ConsumeMessageService {
	private final ThreadPoolExecutor consumeExecutor;
	private final ScheduledExecutorService scheduledExecutorService;

	public void shutdown() {
		this.stopped = true;
		this.scheduledExecutorService.shutdown();
		this.consumeExecutor.shutdown();
		if (MessageModel.CLUSTERING.equals(this.defaultMQPushConsumerImpl.messageModel())) {
			this.unlockAllMQ();
		}
	}
}

// rocketMQ: 4.9.1 提供了 graceful-shutdown机制
//   比较粗暴，超过设置的timeout会强制关闭消费者线程。
public class ConsumeMessageOrderlyService implements ConsumeMessageService {
	public void shutdown(long awaitTerminateMillis) {
		this.stopped = true;
		this.scheduledExecutorService.shutdown();
		
		// 核心
		ThreadUtils.shutdownGracefully(this.consumeExecutor, awaitTerminateMillis, TimeUnit.MILLISECONDS);
		
		if (MessageModel.CLUSTERING.equals(this.defaultMQPushConsumerImpl.messageModel())) {
			this.unlockAllMQ();
		}
	}
}
public final class ThreadUtils {

	public static void shutdownGracefully(ExecutorService executor, long timeout, TimeUnit timeUnit) {
		// Disable new tasks from being submitted.
		executor.shutdown();
		try {
			// Wait a while for existing tasks to terminate.
			if (!executor.awaitTermination(timeout, timeUnit)) {
				executor.shutdownNow();
				// Wait a while for tasks to respond to being cancelled.
				if (!executor.awaitTermination(timeout, timeUnit)) {
					log.warn(String.format("%s didn't terminate!", executor));
				}
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted.
			executor.shutdownNow();
			// Preserve interrupt status.
			Thread.currentThread().interrupt();
		}
	}
}
```

由`ConsumeMessageOrderlyService#shutdown()`可知，当触发shutdown，MQ实际上调用的是消费线程shutdown `ThreadPoolExecutor#shutdown()`。

关于`ThreadPoolExecutor#shutdown()`
> [浅谈Java关闭线程池shutdown和shutdownNow的区别](https://www.jb51.net/article/223973.htm)
> 
> 调用 `shutdown()`方法之后线程池
> 1. 并不是立刻就被关闭，因为这时线程池中可能还有很多任务正在被执行，或是任务队列中有大量正在等待被执行的任务，
> 线程池会在执行完正在执行的任务和队列中等待的任务后才彻底关闭。
> 2. 如果还有新的任务被提交，线程池则会根据拒绝策略直接拒绝后续新提交的任务。
> 3. （方法后）笤俑`isShutdown()`返回true，但`isTerminated()`返回false。

由上可知，并未保证正在执行的consumer-thread 正确执行完成。例如 调用了shutdown，然后又触发`mq-sender#shutdown()`，会导致无法发送消息。

#### 解决方式
通过以上分析可知，现在想到的方案：

mq-consumer 优先处理shutdown信号，避免 redis/dubbo connection被提前close。方式通过spring监听 `ContextClosedEvent`，在其中：
1）触发`consumer#shutdown`，不再消费后续消息。
2）参考`RocketMQ: 4.8.0`的方式，增加一个`awaitTerminationMillisWhenShutdown`。

**备注**  
1. 考虑是否通过`isTerminated()`强制等待 consumer消费完成。
相对麻烦，由于都未提供`ConsumeMessageOrderlyService#consumeExecutor`的获取，可能需要通过反射获取。

## 2022-07-21，`consumeExecutor & scheduledExecutorService`?

### `scheduledExecutorService` 用途
例如消费者抛出异常时，会返回` ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT`
```java
// DefaultRocketMQListenerContainer$DefaultMessageListenerOrderly
public class DefaultMessageListenerOrderly implements MessageListenerOrderly {

	@SuppressWarnings("unchecked")
	@Override
	public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
		for (MessageExt messageExt : msgs) {
			log.debug("received msg: {}", messageExt);
			try {
				long now = System.currentTimeMillis();
				
				// 调用 `RocketMQListener#onMessage(...)`
				handleMessage(messageExt);
				
				long costTime = System.currentTimeMillis() - now;
				log.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
			} catch (Exception e) {
				log.warn("consume message failed. messageId:{}, topic:{}, reconsumeTimes:{}", messageExt.getMsgId(), messageExt.getTopic(), messageExt.getReconsumeTimes(), e);
				
				// suspendCurrentQueueTimeMillis 默认 1s。
				context.setSuspendCurrentQueueTimeMillis(suspendCurrentQueueTimeMillis);
				return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
			}
		}

		return ConsumeOrderlyStatus.SUCCESS;
	}
}

// - ConsumeMessageOrderlyService.ConsumeRequest#run(), LINE: 542
// - ConsumeMessageOrderlyService#processConsumeResult(List, ConsumeOrderlyStatus, ConsumeOrderlyContext, ConsumeMessageOrderlyService.ConsumeRequest)}
//    这里面会将消息失败的message，等待 1s后 重新消费。
// - ConsumeMessageOrderlyService#scheduledExecutorService
public class ConsumeMessageOrderlyService {
	private void submitConsumeRequestLater(
			final ProcessQueue processQueue,
			final MessageQueue messageQueue,
			final long suspendTimeMillis
	) {
		long timeMillis = suspendTimeMillis;
		if (timeMillis == -1) {
			timeMillis = this.defaultMQPushConsumer.getSuspendCurrentQueueTimeMillis();
		}

		if (timeMillis < 10) {
			timeMillis = 10;
		} else if (timeMillis > 30000) {
			timeMillis = 30000;
		}

		this.scheduledExecutorService.schedule(new Runnable() {

			@Override
			public void run() {
				ConsumeMessageOrderlyService.this.submitConsumeRequest(null, processQueue, messageQueue, true);
			}
		}, timeMillis, TimeUnit.MILLISECONDS);
	}
}
```

由上可知，`scheduledExecutorService` 其实也是 consumerExecutor。

### 问题
#### Q1.**那么为什么新版本中，scheduledExecutorService 没有进行 graceful-shutdown处理？**
假设消息顺序依次是 task-1..N.
例如 consumerExecutor 正在消费执行任务 1个(task-3)，已提交等待执行任务 2个(task-9,task-10)。
scheduledExecutorService 正在消费执行任务 4个(task-1,task-2,task-4,task-5)，已提交等待执行任务 3个(task-6,task-7,task-8)。

`awaitTerminateMillis`设置的刚好让 consumerExecutor 能保证 3个任务执行完成。
如果所有任务执行耗时一样，这样不是会导致  scheduledExecutorService 中 1+3个任务（task-5..8） 执行失败？

如果执行任务耗时差异较大，例如 task-4、task-5 重试执行成功，task-1&task-2 后执行成功

#### Q2. 那么 MQ 记录的 offset 是？
如果更新成`offset: 5`, 那么当task-1/task-2 实际是执行失败（或未消费）

怎么避免？ 特别默认consumer是多线程消费，怎么提交修改的这个 offset？

