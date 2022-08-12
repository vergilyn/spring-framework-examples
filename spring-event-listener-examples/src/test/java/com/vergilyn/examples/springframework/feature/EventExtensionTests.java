package com.vergilyn.examples.springframework.feature;

import com.vergilyn.examples.springframework.AbstractEventListenerApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.context.event.GenericApplicationListenerAdapter;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("JavadocReference")
@Import({ EventExtensionTests.ApplicationStartedEventListener.class, EventExtensionTests.CustomApplicationStartedEventListener.class})
public class EventExtensionTests extends AbstractEventListenerApplicationTests {

	/**
	 * <h3>结论</h3>
	 * <p> 1. `push(parent-event)` 不会触发 child-listener；
	 * <p> 2. `push(child-event)` 会同时触发 parent-event-listener & child-event-listener。
	 *
	 * <p><h3>源码</h3>
	 * <pre>
	 *  - {@link AbstractApplicationEventMulticaster#retrieveApplicationListeners(ResolvableType, Class, AbstractApplicationEventMulticaster.CachedListenerRetriever)}, line: 231
	 *  - {@link AbstractApplicationEventMulticaster#supportsEvent(ApplicationListener, ResolvableType, Class)}, line: 361
	 *  - {@link GenericApplicationListenerAdapter#supportsEventType(ResolvableType)}, line: 75
	 *  - <b>{@link ResolvableType#isAssignableFrom(ResolvableType)}</b>
	 * </pre>
	 */
	@SneakyThrows
	@Test
	public void test(){
		// child-event 会触发 parent-listener
		applicationContext.publishEvent(new CustomApplicationStartedEvent(new SpringApplication(), null, null));
		TimeUnit.SECONDS.sleep(2);
	}


	@Component
	public static class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

		@Override
		public void onApplicationEvent(ApplicationStartedEvent event) {
			System.out.printf("[vergilyn][%s] >>>> listener: %s \n",
			                  event.getClass().getSimpleName(),
			                  this.getClass().getSimpleName());
		}
	}

	@Component
	public static class CustomApplicationStartedEventListener implements ApplicationListener<CustomApplicationStartedEvent> {

		@Override
		public void onApplicationEvent(CustomApplicationStartedEvent event) {
			System.out.printf("[vergilyn][%s] >>>> listener: %s \n",
			                  event.getClass().getSimpleName(),
			                  this.getClass().getSimpleName());
		}
	}

	public static class CustomApplicationStartedEvent extends ApplicationStartedEvent {

		public CustomApplicationStartedEvent(SpringApplication application, String[] args,
				ConfigurableApplicationContext context) {
			super(application, args, context);
		}
	}
}
