package com.vergilyn.examples.springboot.usage.u0002.core;

import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.Consumer;

/**
 * 期望：如果同一个{@linkplain SpringStrategy} 找到多个处理类，只用其中<b>优先级最高的处理类</b>进行处理。
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see ChainInvokerSpringStrategy
 */
@Slf4j
public class SingleInvokerSpringStrategy<K, V extends SpringStrategy<K>> extends ChainInvokerSpringStrategy<K, V> {

	public SingleInvokerSpringStrategy(List<V> beans) {
		super(beans);
	}

	@Override
	public void invoke(K key, Consumer<V> invoker) {
		invoker.accept(getHighestPriority(key));
	}
}
