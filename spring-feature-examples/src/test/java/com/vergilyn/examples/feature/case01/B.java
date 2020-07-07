package com.vergilyn.examples.feature.case01;

import org.springframework.stereotype.Component;

/**
 * @author vergilyn
 * @date 2020-07-07
 */
@Component
public class B {
    public B() {
        System.out.println("B >>>> " + Case01Configuration.INDEX.incrementAndGet());
    }
}
