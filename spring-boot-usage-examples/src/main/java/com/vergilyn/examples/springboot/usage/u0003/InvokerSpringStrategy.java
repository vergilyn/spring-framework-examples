package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;

import java.util.List;
import java.util.function.Consumer;

/**
 * 利用spring构造函数注入实现策略模式。
 *
 * @param <K> 区分策略的key，即`map.key`。
 * @param <V> spring-bean。需要实现{@linkplain SpringStrategy}
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see org.springframework.core.Ordered
 * @see org.springframework.core.annotation.Order
 */
public interface InvokerSpringStrategy<K, V extends SpringStrategy<K>> {

	List<V> all(K key, Class<V> clazz);

	V getHighestPriority(K key, Class<V> clazz);

	V getLowerPriority(K key, Class<V> clazz);

	void invoke(K key, Class<V> clazz, Consumer<V> invoker);
}
