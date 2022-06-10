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
 * @see GenericClassSpringStrategy
 */
public interface SpringStrategyOperations {

	<K, V> List<V> lookupBeans(K key, Class<V> clazz, Function<V, K> keyFunction);

	<K, V> List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference, Function<V, K> keyFunction);

	/**
	 * 获取匹配的<b>高优先级的bean</b>
	 *
	 * @see #getLowerBean(Object, Class, Function)
	 */
	default <K, V> V getHighestBean(K key, Class<V> clazz, Function<V, K> keyFunction) {
		List<V> beans = lookupBeans(key, clazz, keyFunction);

		if (beans.isEmpty()) {
			return null;
		}

		return beans.get(0);
	}

	/**
	 * 获取匹配的<b>低优先级的bean</b>
	 *
	 * @see #getHighestBean(Object, Class, Function)
	 */
	default <K, V> V getLowerBean(K key, Class<V> clazz, Function<V, K> keyFunction) {
		List<V> beans = lookupBeans(key, clazz, keyFunction);

		if (beans.isEmpty()) {
			return null;
		}

		return beans.get(beans.size() - 1);
	}

	/**
	 * 根据 {@code 高优先级 > 低优先级} 的顺序调用。
	 */
	default <K, V> void invokeChainBeans(K key, Class<V> clazz, Function<V, K> keyFunction, Consumer<V> invoker) {
		List<V> beans = lookupBeans(key, clazz, keyFunction);
		if (beans == null){
			return;
		}

		for (V bean : beans) {
			invoker.accept(bean);
		}
	}

	/**
	 * 根据 {@code 高优先级 > 低优先级} 的顺序调用。
	 */
	default <K, V> void invokeChainBeans(K key, ParameterizedTypeReference<V> typeReference, Function<V, K> keyFunction, Consumer<V> invoker) {
		List<V> beans = lookupBeans(key, typeReference, keyFunction);
		if (beans == null){
			return;
		}

		for (V bean : beans) {
			invoker.accept(bean);
		}
	}
}
