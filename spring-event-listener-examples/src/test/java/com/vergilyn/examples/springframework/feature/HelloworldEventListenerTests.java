package com.vergilyn.examples.springframework.feature;

import com.vergilyn.examples.springframework.AbstractEventListenerApplicationTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("JavadocReference")
@Import(HelloworldEventListenerTests.ApplicationReadyEventListener.class)
public class HelloworldEventListenerTests extends AbstractEventListenerApplicationTests {

	/**
	 * <pre>
	 *   {@link AbstractApplicationContext#publishEvent(ApplicationEvent)}
	 *   {@link AbstractApplicationContext#publishEvent(Object, ResolvableType)}
	 *   {@link SimpleApplicationEventMulticaster#multicastEvent(ApplicationEvent, ResolvableType)}
	 * </pre>
	 *
	 * <p><h3>注意</h3>
	 * <p> 1. 并非所有的 spring-event都可以通过 bean-registry-listener 来触发。例如 {@link ContextStartedEvent}等。
	 *   需要通过`spring.factories`来 registry-listener。
	 */
	@SneakyThrows
	@Test
	public void test(){
		printf("hello spring-event-listener test.");
		TimeUnit.SECONDS.sleep(4);
	}

	@Component
	public static class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

		@Override
		public void onApplicationEvent(ApplicationReadyEvent event) {
			printf("listener: %s, event: %s",
			                  this.getClass().getSimpleName(),
			                  event.getClass().getSimpleName());
		}

	}
}
