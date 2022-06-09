package com.vergilyn.examples.springboot.usage.u0003;

import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 如果此类的方法签名定义成泛型通用的{@code `<K, V>`}，需要额外传递`KeyFunction`，否则不需要。
 * <pre>例如：
 * {@code
 *   default <K, V extends SpringStrategy<K>> V getHighestPriority(K key, Class<V> clazz) {
 *      return getHighestPriority(key, clazz, SpringStrategy::getStrategyKey);
 *   }
 * }
 * </pre>
 *
 * @author vergilyn
 * @since 2022-06-09
 *
 * @see InvokerSpringStrategy
 */
public interface InvokerSpringStrategyOperations {

	<K, V> List<V> lookupBeans(K key, Class<V> clazz, Function<V, K> keyFunction);

	<K, V> List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference, Function<V, K> keyFunction);

	default <K, V> V getHighestPriority(K key, Class<V> clazz, Function<V, K> keyFunction) {
		List<V> beans = lookupBeans(key, clazz, keyFunction);

		if (beans.isEmpty()) {
			return null;
		}

		return beans.get(0);
	}

	default <K, V> V getLowerPriority(K key, Class<V> clazz, Function<V, K> keyFunction) {
		List<V> beans = lookupBeans(key, clazz, keyFunction);

		if (beans.isEmpty()) {
			return null;
		}

		return beans.get(beans.size() - 1);
	}

	default <K, V> void invoke(K key, Class<V> clazz, Function<V, K> keyFunction, Consumer<V> invoker) {
		List<V> beans = lookupBeans(key, clazz, keyFunction);
		if (beans == null){
			return;
		}

		for (V bean : beans) {
			invoker.accept(bean);
		}
	}
}
