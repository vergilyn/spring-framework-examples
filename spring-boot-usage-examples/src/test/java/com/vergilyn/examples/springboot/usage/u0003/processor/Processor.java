package com.vergilyn.examples.springboot.usage.u0003.processor;

import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;

public interface Processor extends SpringStrategy<String> {
	default void process() {
		String className = this.getClass().getSimpleName();
		System.out.printf("[processor]class: %s, type: %s \n", className, getStrategyKey());
	}
}
