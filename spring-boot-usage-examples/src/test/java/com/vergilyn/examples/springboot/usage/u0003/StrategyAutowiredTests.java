package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.AbstractSpringbootUsageApplicationTest;
import com.vergilyn.examples.springboot.usage.u0001.StrategyBasicTests;
import com.vergilyn.examples.springboot.usage.u0002.StrategyExtendsTests;
import com.vergilyn.examples.springboot.usage.u0003.handler.Handler;
import com.vergilyn.examples.springboot.usage.u0003.processor.Processor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.vergilyn.examples.springboot.usage.u0003.StrategyKey.KEY_ECHO;
import static com.vergilyn.examples.springboot.usage.u0003.StrategyKey.KEY_METRIC;

/**
 * 参考的是`RedisTemplate`的方式，相比 `{@link StrategyBasicTests} & {@link StrategyExtendsTests}`
 * 这种 组合的方式 更易在spring项目中通用。
 *
 * @author vergilyn
 * @since 2022-06-08
 */
@SuppressWarnings("JavadocReference")
class StrategyAutowiredTests extends AbstractSpringbootUsageApplicationTest {

	@Autowired
	private ChainInvokerSpringStrategyTemplate<String, Processor> processorStrategy;
	@Autowired
	private ChainInvokerSpringStrategyTemplate<String, Handler> handlerStrategy;

	@Test
	public void lookupBean(){
		System.out.println("processors >>>> ");
		List<Processor> processors = processorStrategy.all(KEY_ECHO, Processor.class);
		processors.forEach(Processor::process);

		System.out.println("handlers >>>> ");
		List<Handler> handlers = handlerStrategy.all(KEY_ECHO, Handler.class);
		handlers.forEach(Handler::handle);
	}


	@Test
	public void invoke(){
		processorStrategy.invoke(KEY_ECHO, Processor.class, Processor::process);
		handlerStrategy.invoke(KEY_METRIC, Handler.class, Handler::handle);

	}

	/**
	 * 参考{@linkplain org.springframework.data.redis.core.RedisTemplate}，泛型并没有破坏单例。
	 */
	@Test
	public void equals(){
		System.out.println("processorStrategy >>>> " + processorStrategy);
		System.out.println("handlerStrategy   >>>> " + handlerStrategy);

		Assertions.assertEquals(processorStrategy, handlerStrategy);
	}
}
