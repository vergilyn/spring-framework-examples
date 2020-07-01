package com.vergilyn.examples.feature;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author vergilyn
 * @date 2020-07-01
 */
@Component
public class CustomBeanPostProcessor implements BeanPostProcessor, Ordered {
    private static final AtomicInteger INDEX = new AtomicInteger(0);

    public CustomBeanPostProcessor() {
        print("CustomBeanPostProcessor()");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        print("BeanPostProcessor.postProcessBeforeInitialization(Object bean, String beanName), beanName = " + beanName);

        return bean;
    }

    @Override
    public int getOrder() {
        print("Ordered.getOrder()");
        return 0;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        print("BeanPostProcessor.postProcessAfterInitialization(Object bean, String beanName), beanName = " + beanName);

        return bean;
    }

    public void print(String marks){
        System.out.printf("[%02d][CustomBeanPostProcessor] >>>> %s \r\n", INDEX.incrementAndGet(), marks);
    }
}
