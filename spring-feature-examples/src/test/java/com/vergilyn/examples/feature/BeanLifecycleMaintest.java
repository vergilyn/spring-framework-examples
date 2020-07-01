package com.vergilyn.examples.feature;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author vergilyn
 * @date 2020-07-01
 */
public class BeanLifecycleMaintest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                BeanLifecycleComponent.class, CustomBeanPostProcessor.class);

        BeanLifecycleComponent bean = context.getBean(BeanLifecycleComponent.class);
        bean.print("manual invoke!");

        context.getBeanFactory().destroyBean(bean);
    }
}
