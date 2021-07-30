package com.vergilyn.examples.springframework.async;

import java.time.LocalTime;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.vergilyn.examples.springframework.AbstractSpringBootFeatureTests;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.ContextConfiguration;

/**
 * 1. 受限于spring-aop机制，同一个类的内部调用，`@Async`不会生效。 <br/>
 * 2. 在`@Async`标注的方法，同时也适用了`@Transactional`进行了标注；
 *  在其调用数据库操作之时，将无法产生事务管理的控制，原因就在于其是基于异步处理的操作。 <br/>
 *  那该如何给这些操作添加事务管理呢？可以将需要事务管理操作的方法放置到异步方法内部，在内部被调用的方法上添加@Transactional.
 *  <pre>
 *     例如： 方法A，使用了@Async/@Transactional来标注，但是无法产生事务控制的目的。
 *           方法B，使用了@Async来标注，  B中调用了C、D，C/D分别使用@Transactional做了标注，则可实现事务控制的目的。
 *  </pre>
 *
 * @author vergilyn
 * @since 2021-07-22
 *
 * @see org.springframework.scheduling.annotation.EnableAsync
 * @see <a href="https://www.cnblogs.com/jpfss/p/10273129.html">Spring中@Async用法总结</a>
 */
@ContextConfiguration(classes = AsyncContextConfiguration.class)
@Import(AsyncAnnotationTests.AsyncService.class)
public class AsyncAnnotationTests extends AbstractSpringBootFeatureTests {
	// @Autowired  // 不知道为什么 IDEA Inspection Error “Could not autowire. No beans of 'AsyncService' type found. ”
	@Resource
	AsyncService asyncService;

	@AfterEach
	public void afterEach(){
		awaitExit(10, TimeUnit.SECONDS);
	}

	@Test
	public void asyncMethod(){
		LocalTime result = asyncService.asyncMethod(5);
		System.out.println("method result >>>> " + result);

		Assertions.assertThat(result).isNull();
	}

	@Test
	@SneakyThrows
	public void asyncWithReturn(){
		Future<LocalTime> future = asyncService.asyncWithReturn(5);
		LocalTime result = future.get();
		System.out.println("method result >>>> " + result);

		Assertions.assertThat(result).isNotNull();
	}

	public static class AsyncService{

		// Inspection-Warning: Method annotated with @Async should return ''void'' or "Future-like" type
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

		// 无法获得返回值
		@Async
		public Future<LocalTime> asyncWithReturn(long timeout){
			LocalTime now = LocalTime.now();
			System.out.println("AsyncService >>>> asyncWithReturn begin: " + now);
			try {
				TimeUnit.SECONDS.sleep(timeout);
			} catch (InterruptedException e) {

			}

			LocalTime end = LocalTime.now();

			System.out.println("AsyncService >>>> asyncWithReturn end: " + end);
			return new AsyncResult<LocalTime>(end);
		}
	}
}
