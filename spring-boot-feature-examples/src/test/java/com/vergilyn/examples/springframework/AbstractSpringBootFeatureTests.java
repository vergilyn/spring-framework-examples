package com.vergilyn.examples.springframework;

import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author vergilyn
 * @since 2021-06-18
 */
@SpringBootTest(classes = SpringBootFeatureApplication.class)
@Slf4j
public abstract class AbstractSpringBootFeatureTests {

	@Autowired
	protected ApplicationContext applicationContext;

	protected <T> T registerAndGetBean(Class<T> clazz) {
		AnnotationConfigApplicationContext context = annotationConfigApplicationContext();

		T bean;
		try {
			bean = context.getBean(clazz);
		}catch (NoSuchBeanDefinitionException e){
			log.warn("no such bean-definition, prepare register-bean[{}]. exception: {}",
					clazz.getName(), e.getMessage());
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
		printJson(obj, true);
	}

	protected void printJson(Object obj, boolean prettyFormat){
		if (obj instanceof Enum){
			SerializeConfig config = new SerializeConfig();
			config.configEnumAsJavaBean((Class<? extends Enum>) obj.getClass()); // 配置enum转换

			List<SerializerFeature> features = Lists.newArrayList();
			if (prettyFormat){
				features.add(SerializerFeature.PrettyFormat);
			}

			System.out.println(JSON.toJSONString(obj, config, features.toArray(new SerializerFeature[0])));
			return;
		}

		System.out.println(JSON.toJSONString(obj, prettyFormat));
	}


}
