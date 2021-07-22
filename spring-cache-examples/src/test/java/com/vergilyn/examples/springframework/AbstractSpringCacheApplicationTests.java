package com.vergilyn.examples.springframework;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author vergilyn
 * @since 2021-06-29
 */
@SpringBootTest(classes = SpringCacheApplication.class)
public abstract class AbstractSpringCacheApplicationTests {

	@Autowired
	protected ApplicationContext applicationContext;

	protected <T> T registerAndGetBean(Class<T> clazz) {
		AnnotationConfigApplicationContext context = annotationConfigApplicationContext();

		T bean;
		try {
			bean = context.getBean(clazz);
		}catch (NoSuchBeanDefinitionException e){
			context.registerBean(clazz);
			bean = context.getBean(clazz);
		}

		return bean;
	}

	protected AnnotationConfigApplicationContext annotationConfigApplicationContext() {
		return (AnnotationConfigApplicationContext) applicationContext;
	}

	/**
	 * @param timeout "<= 0" prevent exit.
	 * @param unit    timeout unit
	 */
	protected void awaitExit(long timeout, TimeUnit unit) {
		try {
			final Semaphore semaphore = new Semaphore(0);
			if (timeout > 0) {
				semaphore.tryAcquire(timeout, unit);
			} else {
				semaphore.acquire();
			}
		} catch (InterruptedException e) {
		}
	}

	protected void printJson(Object obj){
		System.out.println(">>>> " + JSON.toJSONString(obj, true));
	}
}
