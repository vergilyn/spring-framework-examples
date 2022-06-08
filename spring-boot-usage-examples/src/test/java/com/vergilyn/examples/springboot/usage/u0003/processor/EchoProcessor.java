package com.vergilyn.examples.springboot.usage.u0003.processor;

import com.vergilyn.examples.springboot.usage.u0003.StrategyKey;
import org.springframework.stereotype.Component;

@Component
public class EchoProcessor implements Processor {

	@Override
	public String getStrategyKey() {
		return StrategyKey.KEY_ECHO;
	}
}
