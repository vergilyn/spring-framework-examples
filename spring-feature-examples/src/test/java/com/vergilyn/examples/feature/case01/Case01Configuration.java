package com.vergilyn.examples.feature.case01;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@linkplain Configuration}:
 *
 * @author vergilyn
 * @date 2020-07-07
 */
@org.springframework.context.annotation.ComponentScan("com.vergilyn.examples.feature.case01")
// @org.springframework.context.annotation.Configuration
public class Case01Configuration {
    protected static final AtomicInteger INDEX = new AtomicInteger(0);

    @Bean
    public A a() {
        // Method annotated with @Bean is called directly. Use dependency injection instead.
        return new A(b());
    }

    @Bean
    public B b() {
        return new B();
    }
}
