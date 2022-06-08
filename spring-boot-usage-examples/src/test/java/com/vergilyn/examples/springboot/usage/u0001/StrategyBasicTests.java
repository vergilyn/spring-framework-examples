package com.vergilyn.examples.springboot.usage.u0001;

import com.vergilyn.examples.springboot.usage.AbstractSpringbootUsageApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 2022-06-08，代码结构可以参考，但是如果项目中需要大量策略模式，此代码结构没有通用性。
 * 更优的参考：
 *
 * @author vergilyn
 * @since 2022-06-08
 */
public class StrategyBasicTests extends AbstractSpringbootUsageApplicationTest {
	@Autowired
	protected FilterFactory filterFactory;

	/**
	 * 此代码结构的目的：（其实也可以不依赖spring，但既然是spring项目，利用spring来实现更简单） <br/>
	 * <p>1. 当需要增加 {@linkplain AbstractFilter} 时，不需要维护 {@linkplain FilterFactory}。
	 *   如果更进一步，可以不维护 {@linkplain StrategyEnum}，直接增加 {@linkplain AbstractFilter}。
	 *   （因为增加`AbstractFilter`时已经知道 存在`StrategyEnum`，所以一定程度规避了 代码的隐蔽性。 ）
	 *
	 * <p>2. {@linkplain ApplicationStartedEventListener} 或 {@linkplain FilterFactory#afterPropertiesSet()} 根据实际情况抉择。
	 */
	@Test
	public void method(){
		AbstractFilter filter = filterFactory.get(StrategyEnum.A);
		filter.handler();
	}


}
