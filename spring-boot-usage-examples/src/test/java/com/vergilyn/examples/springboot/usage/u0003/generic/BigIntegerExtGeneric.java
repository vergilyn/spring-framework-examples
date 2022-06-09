package com.vergilyn.examples.springboot.usage.u0003.generic;

import com.vergilyn.examples.springboot.usage.u0003.StrategyKey;
import org.springframework.stereotype.Component;

@Component
public class BigIntegerExtGeneric implements Generic<BigIntegerExt> {
	@Override
	public String getStrategyKey() {
		return StrategyKey.KEY_INTEGER;
	}

	@Override
	public Class<BigIntegerExt> getActualClass() {
		return BigIntegerExt.class;
	}
}
