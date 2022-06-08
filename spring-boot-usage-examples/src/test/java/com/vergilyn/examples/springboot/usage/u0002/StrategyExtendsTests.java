package com.vergilyn.examples.springboot.usage.u0002;

import com.vergilyn.examples.springboot.usage.AbstractSpringbootUsageApplicationTest;
import com.vergilyn.examples.springboot.usage.u0001.StrategyBasicTests;
import com.vergilyn.examples.springboot.usage.u0002.core.ChainInvokerSpringStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 通过extends或者创建strategy-class的方式，相比{@linkplain StrategyBasicTests} 有一定的通用性，但是：
 * <br/> 因为是 {@code extends}，由于java-class只能<b>单一继承</b>，使用上有局限性。
 * <br/> 导致，如果不能`extends`时，需要创建类似{@linkplain FilterFactory} 的代码，<b>有点繁琐!</b>。
 *
 * （继承有个好处：1.内部的cache-map不会过大。
 *   2. cache-map 更容易理解，不容易混乱。 例如全局只有 1个cache-map，需要进一步定义key来区分）
 *
 * <p> 所以，<b>更推荐：通过`组合方式注入`需要的 strategy-bean。</b>
 *
 * @author vergilyn
 * @since 2022-06-08
 */
public class StrategyExtendsTests extends AbstractSpringbootUsageApplicationTest {

	@Test
	public void strategy() {
		applicationContext.register(FilterFactory.class, EchoFilter.class, EchoAnotherFilter.class, MetricFilter.class);

		FilterFactory filterFactory = applicationContext.getBean(FilterFactory.class);

		filterFactory.invoke("echo", Filter::handler);

		// filterFactory.getHighestPriority("echo").handler();
	}

	public static class FilterFactory extends ChainInvokerSpringStrategy<String, Filter> {

		public FilterFactory(List<Filter> beans) {
			super(beans);
		}
	}

	public static interface Filter extends SpringStrategy<String> {
		default void handler() {
			String className = this.getClass().getSimpleName();
			System.out.printf("class: %s, type: %s \n", className, getStrategyKey());
		}
	}

	public static class EchoFilter implements Filter {
		@Override
		public String getStrategyKey() {
			return "echo";
		}
	}

	public static class EchoAnotherFilter implements Filter {
		@Override
		public String getStrategyKey() {
			return "echo";
		}
	}

	public static class MetricFilter implements Filter {
		@Override
		public String getStrategyKey() {
			return "metric";
		}
	}
}
