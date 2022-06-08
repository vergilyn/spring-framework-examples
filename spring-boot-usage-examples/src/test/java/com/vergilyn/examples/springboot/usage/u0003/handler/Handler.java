package com.vergilyn.examples.springboot.usage.u0003.handler;

import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;

public interface Handler extends SpringStrategy<String> {

	default void handle() {
		String className = this.getClass().getSimpleName();
		System.out.printf("[handler] class: %s, type: %s \n", className, getStrategyKey());
	}
}
