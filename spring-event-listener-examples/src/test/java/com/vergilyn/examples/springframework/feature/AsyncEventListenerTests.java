package com.vergilyn.examples.springframework.feature;

import com.vergilyn.examples.springframework.AbstractEventListenerApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * 默认情况下，spring是通过 BIO的形式进行广播。
 *   参考：{@link SimpleApplicationEventMulticaster#multicastEvent(ApplicationEvent, ResolvableType)}
 *
 * <p><h3>特别</h3>
 * <b>spring-boot 启动后，存在 2个 {@link SimpleApplicationEventMulticaster} 实例！！！</b>
 * <p> 1. {@link SpringApplication#run(String...)} 中，当应用启动时，并不存在 ApplicationContext，
 *   <br/> 此时会通过{@link SpringApplication#getRunListeners(String[])} 构建 push-event。
 *   <br/> 默认只有1个，即 {@link EventPublishingRunListener#initialMulticaster}，
 *   <br/> SEE: {@link EventPublishingRunListener#EventPublishingRunListener(SpringApplication, String[])}
 *
 * <p> 2. {@link AbstractApplicationContext#applicationEventMulticaster} 又会构建 push-event。
 *
 * <p> 区别：根据具体的{@link ApplicationEvent}分析到底是 `SpringApplication.runListener` 还是 `ApplicationContext.eventMulticaster`。
 *  <br/> <s>例如 ApplicationXxxEvent，ContextXxxEvent。根据前缀 Application/Context，可以大概猜测</s>。（不能这么判断！）
 *  <br/> 具体看 {@link EventPublishingRunListener}，例如`ApplicationContextInitializedEvent`是 `SpringApplication.runListener`；
 *  <br/> `ApplicationStartedEvent`是 `context.publishEvent(...)`。
 *
 * @author vergilyn
 * @since 2022-08-17
 *
 * @see SimpleApplicationEventMulticaster#multicastEvent(ApplicationEvent, ResolvableType)
 */
@SuppressWarnings("JavadocReference")
class AsyncEventListenerTests {

	/**
	 * <b>方式1：</b>
	 * listener内部处理时用BIO，或者结合{@link Async}。
	 * <br/> 这种方式更灵活，可以由 event-listener自身去控制选择 BIO/NIO。
	 */
	@Import({FirstAsyncTest.FirstAsyncEventListener.class, FirstAsyncTest.FirstSyncEventListener.class})
	public static class FirstAsyncTest extends AbstractEventListenerApplicationTests {
		@SneakyThrows
		@Test
		public void listenerAsync(){
			printf("first async-event-listener test.");
			TimeUnit.SECONDS.sleep(4);
		}

		@Async
		@Component
		public static class FirstAsyncEventListener implements ApplicationListener<ApplicationReadyEvent> {

			@Override
			public void onApplicationEvent(ApplicationReadyEvent event) {
				printf("listener: %s, event: %s",
				       this.getClass().getSimpleName(),
				       event.getClass().getSimpleName());
			}

		}

		@Component
		public static class FirstSyncEventListener implements ApplicationListener<ApplicationReadyEvent> {

			@Override
			public void onApplicationEvent(ApplicationReadyEvent event) {
				printf("listener: %s, event: %s",
				       this.getClass().getSimpleName(),
				       event.getClass().getSimpleName());
			}

		}
	}

	/**
	 * <b>方式2：</b>
	 * 设置 {@link SimpleApplicationEventMulticaster#setTaskExecutor(Executor)}。
	 * <br/> 会导致全部 event都是 NIO
	 * <br/> <s>（包括 spring中的event-listener 都会变成NIO，可能导致出现一些未知的问题。）</s> 此说法存疑，应该不会出现spring相关的问题。
	 */
	@Import({SecondAsyncTest.SecondEventListener_001.class, SecondAsyncTest.SecondEventListener_002.class})
	@ImportAutoConfiguration(SecondAsyncTest.TaskExecutorAutoConfiguration.class)
	public static class SecondAsyncTest extends AbstractEventListenerApplicationTests {

		@SneakyThrows
		@Test
		public void listenerAsync(){
			printf("second async-event-listener test.");
			TimeUnit.SECONDS.sleep(4);
		}

		@Configuration
		public static class TaskExecutorAutoConfiguration {

			/**
			 * @see AbstractApplicationContext#initApplicationEventMulticaster()
			 * @see SimpleApplicationEventMulticaster#setTaskExecutor(Executor)
			 *
			 */
			@Bean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
			public ApplicationEventMulticaster applicationEventMulticaster(BeanFactory beanFactory){
				SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);

				// ExecutorService taskExecutor = Executors.newFixedThreadPool(4);
				ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
				taskExecutor.setThreadNamePrefix("async-event-listener-");
				taskExecutor.setCorePoolSize(4);
				taskExecutor.initialize();

				eventMulticaster.setTaskExecutor(taskExecutor);

				return eventMulticaster;
			}
		}

		@Component
		public static class SecondEventListener_001 implements ApplicationListener<ApplicationReadyEvent> {

			@Override
			public void onApplicationEvent(ApplicationReadyEvent event) {
				printf("listener: %s, event: %s",
				       this.getClass().getSimpleName(),
				       event.getClass().getSimpleName());
			}

		}

		@Component
		public static class SecondEventListener_002 implements ApplicationListener<ApplicationReadyEvent> {

			@Override
			public void onApplicationEvent(ApplicationReadyEvent event) {
				printf("listener: %s, event: %s",
				       this.getClass().getSimpleName(),
				       event.getClass().getSimpleName());
			}

		}
	}


}
