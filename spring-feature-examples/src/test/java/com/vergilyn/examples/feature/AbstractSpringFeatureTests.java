package com.vergilyn.examples.feature;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author vergilyn
 * @since 2021-07-22
 */
public abstract class AbstractSpringFeatureTests {

    protected AnnotationConfigApplicationContext initApplicationContext(){
        return new AnnotationConfigApplicationContext();
    }

    protected <T> T registerAndGetBean(Class<T> clazz) {
        AnnotationConfigApplicationContext context = initApplicationContext();
        context.refresh();

        T bean;
        try {
            bean = context.getBean(clazz);
        }catch (NoSuchBeanDefinitionException e){
            context.registerBean(clazz);
            bean = context.getBean(clazz);
        }

        return bean;
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

}
