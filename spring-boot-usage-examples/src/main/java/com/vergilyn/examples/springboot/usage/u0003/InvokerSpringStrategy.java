package com.vergilyn.examples.springboot.usage.u0003;

import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.function.Consumer;

/**
 * 利用spring构造函数注入实现策略模式。
 *
 *
 * @param <K> 区分策略的key，即`map.key`。
 * @param <V> spring-bean。具体的实现类中，再决定是否需要{@code `V extends SpringStrategy<K>`}（根据实现类决定）
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see InvokerSpringStrategyOperations
 */
public interface InvokerSpringStrategy<K, V> {

	List<V> lookupBeans(K key, Class<V> clazz);

	List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference);

	default V getHighestPriority(K key, Class<V> clazz) {
		List<V> beans = lookupBeans(key, clazz);

		if (beans.isEmpty()) {
			return null;
		}

		return beans.get(0);
	}

	default V getLowerPriority(K key, Class<V> clazz) {
		List<V> beans = lookupBeans(key, clazz);

		if (beans.isEmpty()) {
			return null;
		}

		return beans.get(beans.size() - 1);
	}

	default void invoke(K key, Class<V> clazz, Consumer<V> invoker) {
		List<V> beans = lookupBeans(key, clazz);
		if (beans == null){
			return;
		}

		for (V bean : beans) {
			invoker.accept(bean);
		}
	}
}
