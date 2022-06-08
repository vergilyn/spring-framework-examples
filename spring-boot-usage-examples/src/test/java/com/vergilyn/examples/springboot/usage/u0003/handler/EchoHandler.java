package com.vergilyn.examples.springboot.usage.u0003.handler;

import com.vergilyn.examples.springboot.usage.u0003.StrategyKey;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EchoHandler implements Handler {

	@Override
	public String getStrategyKey() {
		return StrategyKey.KEY_ECHO;
	}
}
