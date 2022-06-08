package com.vergilyn.examples.springboot.usage.u0002.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vergilyn.examples.springboot.usage.u0002.InvokerSpringStrategy;
import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 期望：如果同一个{@linkplain SpringStrategy} 找到多个处理类，根据其<b>优先级从高到低</b>依次进行处理。
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see SingleInvokerSpringStrategy
 */
@Slf4j
public class ChainInvokerSpringStrategy<K, V extends SpringStrategy<K>>
		implements InvokerSpringStrategy<K, V> {

	protected final Map<K, List<V>> cacheMap = Maps.newConcurrentMap();

	public ChainInvokerSpringStrategy(List<V> beans) {
		registry(beans);
	}

	private void registry(List<V> beans) {
		for (V bean : beans) {
			cacheMap.computeIfAbsent(bean.getStrategyKey(), k -> Lists.newArrayList())
					.add(bean);
		}
	}

	@Override
	public List<V> all(K key) {
		return Optional.ofNullable(cacheMap.get(key)).orElse(Lists.newArrayList());
	}

	@Override
	public V getHighestPriority(K key) {
		List<V> all = all(key);

		if (all.isEmpty()){
			return null;
		}

		return all.get(0);
	}

	@Override
	public V getLowerPriority(K key) {
		List<V> all = all(key);

		if (all.isEmpty()){
			return null;
		}

		return all.get(all.size() - 1);
	}

	@Override
	public void invoke(K key, Consumer<V> invoker) {
		for (V bean : all(key)) {
			invoker.accept(bean);
		}
	}
}
