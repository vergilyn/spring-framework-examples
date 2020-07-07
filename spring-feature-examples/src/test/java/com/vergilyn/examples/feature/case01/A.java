package com.vergilyn.examples.feature.case01;

/**
 * @author vergilyn
 * @date 2020-07-07
 */
public class A {
    public A(B b) {
        System.out.println("A >>>> " + Case01Configuration.INDEX.incrementAndGet());
    }
}
