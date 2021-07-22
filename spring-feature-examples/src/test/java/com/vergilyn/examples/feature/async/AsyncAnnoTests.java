package com.vergilyn.examples.feature.async;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import com.vergilyn.examples.feature.AbstractSpringFeatureTests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 *
 * @author vergilyn
 * @since 2021-07-22
 *
 * @see org.springframework.scheduling.annotation.EnableAsync
 */
public class AsyncAnnoTests extends AbstractSpringFeatureTests {


	@Test
	public void asyncMethod(){
		final AsyncService asyncService = registerAndGetBean(AsyncService.class);

		LocalTime localTime = asyncService.asyncMethod(5);
		System.out.println("junit resp >>>> " + localTime);

		awaitExit(10, TimeUnit.SECONDS);
	}


	@Component
	@Slf4j
	public static class AsyncService{

		@Async
		public LocalTime asyncMethod(long timeout){
			LocalTime now = LocalTime.now();
			System.out.println("AsyncService >>>> asyncMethod begin: " + now);
			try {
				TimeUnit.SECONDS.sleep(timeout);
			} catch (InterruptedException e) {

			}

			LocalTime end = LocalTime.now();

			System.out.println("AsyncService >>>> asyncMethod end: " + end);
			return end;
		}
	}
}
