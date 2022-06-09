package com.vergilyn.examples.springboot.usage.u0003.generic;

import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;

public interface Generic<T extends Number> extends SpringStrategy<String> {

	Class<T> getActualClass();

	default void generic() {
		String className = this.getClass().getSimpleName();

		Class<T> actualClass = getActualClass();

		System.out.printf("[generic] class: %s, type: %s, actual-class: %s \n",
		                  className, getStrategyKey(), actualClass);
	}
}
