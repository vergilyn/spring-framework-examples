package com.vergilyn.examples.springboot.usage.u0001;

/**
 * @author vergilyn
 * @since 2022-05-07
 */
public abstract class AbstractFilter {
    public abstract StrategyEnum getType();

    public void handler() {
        String className = this.getClass().getSimpleName();
        System.out.printf("class: %s, type: %s \n", className, getType());
    }
}
