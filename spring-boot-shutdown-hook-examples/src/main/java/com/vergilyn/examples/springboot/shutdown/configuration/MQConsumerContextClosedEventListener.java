package com.vergilyn.examples.springboot.shutdown.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService;
import org.apache.rocketmq.client.impl.consumer.ConsumeMessageOrderlyService;
import org.apache.rocketmq.common.utils.ThreadUtils;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@SuppressWarnings("JavadocReference")
public class MQConsumerContextClosedEventListener implements ApplicationListener<ContextClosedEvent>, Ordered {
	private static final String KEY_CONSUMER_SHUTDOWN_FORCE_AWAIT_MILLIS = "mq.consumer.shutdown-force-await-millis";

	/**
	 * 消费者shutdown，强制等待 20s。（根据业务代码决定）
	 */
	private static final long DEFAULT_CONSUMER_SHUTDOWN_FORCE_AWAIT_MILLIS = 20_000;

	public MQConsumerContextClosedEventListener() {
	}

	/**
	 * 强制 mq-consumer 最先close，避免其余资源被提前closed
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		gracefulShutdownMQConsumer(event.getApplicationContext());
	}

	/**
	 * <p> 1. 对于`rocketMQ: v4.8.0+`，可以设置{@link DefaultMQPushConsumer#awaitTerminationMillisWhenShutdown}来更优雅的达到以下目的。
	 *   <br/> <b>但是</b>，同样都需要调整 shutdown顺序。主要是避免 其它资源被提前closed。
	 *
	 * <p> 2. 对于低版本，简单粗暴的方式如下，强制等待多久后，继续后续的 closed。
	 *   <br/> 如果想达到高版本的graceful-shutdown，需要获取 {@link ConsumeMessageOrderlyService#consumeExecutor}（或 {@link ConsumeMessageConcurrentlyService#consumeExecutor}）。
	 *   然后进行 {@link ThreadUtils#shutdownGracefully(ExecutorService, long, TimeUnit)} 类似操作。
	 *   （由于源码并未暴露consumerExecutor's 暂时只想到通过反射逐个获取。）
	 *
	 * @see AbstractApplicationContext#doClose()
	 * @see org.apache.rocketmq.spring.autoconfigure.ListenerContainerConfiguration#registerContainer(String, Object)
	 */
	private void gracefulShutdownMQConsumer(ApplicationContext applicationContext) {
		Collection<DefaultRocketMQListenerContainer> listenerContainers = applicationContext.getBeansOfType(
				DefaultRocketMQListenerContainer.class).values();

		if (listenerContainers == null || listenerContainers.isEmpty()){
			return;
		}

		String logPrefix = "[vergilyn][mq-consumer-shutdown]";
		// 1. 关闭consumer-listener，不再消费新的消息。正在执行or已经提交的任务 会正常消费
		for (DefaultRocketMQListenerContainer listenerContainer : listenerContainers) {
			listenerContainer.destroy();
		}

		// 2. 强制等待 xx ms。然后在继续后续的 closed。
		//   更优雅的方式是，拿到所有的`ConsumeMessageOrderlyService.consumeExecutor`，然后判断线程池执行状态。
		long shutdownForceAwaitMillis = getShutdownForceAwaitMillis(applicationContext.getEnvironment());

		log.warn("{} 强制等待 {} ms 开始....", logPrefix, shutdownForceAwaitMillis);
		try {
			new CountDownLatch(1).await(shutdownForceAwaitMillis, TimeUnit.MILLISECONDS);
		} catch (Exception ignored) {

		}finally {
			log.warn("{} 强制等待 {} ms 结束....", logPrefix, shutdownForceAwaitMillis);
		}
	}

	private long getShutdownForceAwaitMillis(Environment environment){
		try {
			return environment.getRequiredProperty(KEY_CONSUMER_SHUTDOWN_FORCE_AWAIT_MILLIS, Long.class);
		}catch (Exception e){
			return DEFAULT_CONSUMER_SHUTDOWN_FORCE_AWAIT_MILLIS;
		}
	}
}
