package com.vergilyn.examples.retry.feature;

import com.vergilyn.examples.retry.AbstractSpringRetryTests;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.retry.*;
import org.springframework.retry.backoff.BackOffContext;
import org.springframework.retry.backoff.BackOffInterruptedException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("JavadocReference")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CodeHelloworldTests extends AbstractSpringRetryTests {
	private static final AtomicInteger index = new AtomicInteger();

	private RetryTemplate retryTemplate = null;

	@BeforeAll
	public void init(){
		retryTemplate = new RetryTemplate();
		retryTemplate.setThrowLastExceptionOnExhausted(false);
		// retryTemplate.setRetryContextCache(new MapRetryContextCache());

		retryTemplate.setListeners(new RetryListener[]{new HelloworldRetryListener()});

		// back-off 回退策略
		// retryTemplate.setBackOffPolicy(new HelloworldBackOffPolicy());

		// 默认重试 3次 （包括 初始的第1次）
		// 重要 RetryContext 其实实在 `org.springframework.retry.RetryPolicy#open()`创建
		retryTemplate.setRetryPolicy(new SimpleRetryPolicy());
	}

	/**
	 * @see RetryTemplate#doExecute(RetryCallback, RecoveryCallback, RetryState)
	 */
	@SneakyThrows
	@Test
	public void test(){

		Integer rs = retryTemplate.execute((RetryCallback<Integer, Throwable>) context -> {

			return method(1, 0);
		});

		System.out.println("rs >>>> " + rs);
	}

	public int method(int a, int b) throws Exception{

		try {
			int rs = a / b;
			System.out.println("[vergilyn] >>>> index: " + index.getAndIncrement());

			return rs;
		}catch (Exception e){
			throw e;
		}

	}


	@Slf4j
	public static class HelloworldBackOffPolicy implements BackOffPolicy {
		private static final String LOG_PREFIX = "[spring][BackOffPolicy]";

		@Override
		public BackOffContext start(RetryContext context) {
			log.warn("{} start >>>> ", LOG_PREFIX);

			return null;
		}

		@Override
		public void backOff(BackOffContext backOffContext) throws BackOffInterruptedException {
			log.warn("{} backOff >>>> ", LOG_PREFIX);

		}
	}

	@Slf4j
	public static class HelloworldRetryListener implements RetryListener{
		private static final String LOG_PREFIX = "[spring][RetryListener]";

		@Override
		public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
			log.warn("{} open >>>> ", LOG_PREFIX);

			// javadoc:
			//   > The whole retry can be vetoed by returning false from this method,
			//   > in which case a TerminatedRetryException will be thrown.
			return true;
		}

		@Override
		public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback,
				Throwable throwable) {
			log.warn("{} close >>>> ", LOG_PREFIX);

		}

		@Override
		public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback,
				Throwable throwable) {
			log.warn("{} onError >>>> ", LOG_PREFIX);

		}
	}
}
