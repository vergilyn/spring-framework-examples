package com.vergilyn.examples.springboot.shutdown.usage001;

import com.vergilyn.examples.springboot.shutdown.AbstractSpringbootShutdownApplicationTests;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.autoconfigure.ListenerContainerConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractRocketMQTests extends AbstractSpringbootShutdownApplicationTests {

	@Autowired
	protected AnnotationConfigApplicationContext annotationConfigApplicationContext;
	@Autowired
	protected RocketMQTemplate rocketMQTemplate;

	/**
	 * @see ListenerContainerConfiguration#afterSingletonsInstantiated()
	 * @see org.springframework.beans.factory.support.DefaultListableBeanFactory
	 */
	protected <T> T registryAnnoRocketMQListener(Class<T> componentClass)
			throws InvocationTargetException, IllegalAccessException {

		// 1. 手动registry-class，并由spring生成bean
		annotationConfigApplicationContext.register(componentClass);
		ListenerContainerConfiguration listenerContainerConfiguration = annotationConfigApplicationContext.getBean(ListenerContainerConfiguration.class);

		// 2. 2022-07-06，没有找到别的方式通过代码注册`@RocketMQMessageListener`，
		//   所以通过反射调用。
		Method registerContainerMethod = ReflectionUtils.findMethod(ListenerContainerConfiguration.class, "registerContainer",
		                                                            String.class, Object.class);

		ReflectionUtils.makeAccessible(registerContainerMethod);

		T bean = annotationConfigApplicationContext.getBean(componentClass);
		String[] beanNames = annotationConfigApplicationContext.getBeanNamesForType(bean.getClass());

		registerContainerMethod.invoke(listenerContainerConfiguration, beanNames[0], bean);

		return bean;
	}

	protected SendResult syncSendMessage(String topic, String tag, String message){
		String dest = topic + ":" + tag;
		return rocketMQTemplate.syncSend(dest, message);
	}
}
