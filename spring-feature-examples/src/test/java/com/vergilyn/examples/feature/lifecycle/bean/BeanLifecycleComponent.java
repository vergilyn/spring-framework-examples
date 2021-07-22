package com.vergilyn.examples.feature.lifecycle.bean;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author vergilyn
 * @date 2020-07-01
 */
@Component
public class BeanLifecycleComponent implements InitializingBean, DisposableBean, ApplicationContextAware, BeanPostProcessor {
    private static final AtomicInteger INDEX = new AtomicInteger(0);
    private ApplicationContext applicationContext;

    public BeanLifecycleComponent(){
        print("BeanLifecycleComponent()");
    }

    @PostConstruct
    public void postConstruct(){
        print("@PostConstruct");
    }

    @PreDestroy
    public void preDestroy(){
        print("@PreDestroy");
    }

    @Override
    public void destroy() throws Exception {
        print("DisposableBean.destroy()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        print("InitializingBean.afterPropertiesSet()");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        print("ApplicationContextAware.setApplicationContext(ApplicationContext applicationContext)");
    }

    public void print(String methodName){
        System.out.printf("[%02d][BeanLifecycleComponent] >>>> %s \r\n", INDEX.incrementAndGet(), methodName);
    }
}
