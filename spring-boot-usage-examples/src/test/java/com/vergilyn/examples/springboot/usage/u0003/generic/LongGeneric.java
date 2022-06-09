package com.vergilyn.examples.springboot.usage.u0003.generic;

import com.vergilyn.examples.springboot.usage.u0003.StrategyKey;
import org.springframework.stereotype.Component;

@Component
public class LongGeneric implements Generic<Long> {
	@Override
	public String getStrategyKey() {
		return StrategyKey.KEY_INTEGER;
	}

	@Override
	public Class<Long> getActualClass() {
		return Long.class;
	}
}
