package com.vergilyn.examples.feature.lifecycle;

import com.vergilyn.examples.feature.AbstractSpringFeatureTests;
import com.vergilyn.examples.feature.lifecycle.bean.BeanLifecycleComponent;
import com.vergilyn.examples.feature.lifecycle.bean.CustomBeanPostProcessor;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author vergilyn
 * @date 2020-07-01
 */
public class BeanLifecycleTests extends AbstractSpringFeatureTests {

    @Test
    public void destroyBean() {
        AnnotationConfigApplicationContext context = initApplicationContext();
        context.register(BeanLifecycleComponent.class, CustomBeanPostProcessor.class);
        context.refresh();

        BeanLifecycleComponent bean = context.getBean(BeanLifecycleComponent.class);
        bean.print("manual invoke!");

        context.getBeanFactory().destroyBean(bean);
    }
}
