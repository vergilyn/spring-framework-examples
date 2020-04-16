package com.vergilyn.examples.annotation;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * @author VergiLyn
 * @date 2020-01-26
 */
@Component
@Aspect
public class LogInjector implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean,String beanName) throws BeansException {
        ReflectionUtils.doWithFields(bean.getClass(), field -> {
            // System.out.println("Logger Inject into :" + bean.getClass());
            // make the field accessible if defined private
            ReflectionUtils.makeAccessible(field);
            if (field.getAnnotation(Log.class) != null) {
                Logger log = LoggerFactory.getLogger(bean.getClass());
                field.set(bean, log);
            }
        });
        return bean;
    }
}