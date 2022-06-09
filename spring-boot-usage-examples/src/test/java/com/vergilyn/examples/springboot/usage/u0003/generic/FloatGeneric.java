package com.vergilyn.examples.springboot.usage.u0003.generic;

import com.vergilyn.examples.springboot.usage.u0003.StrategyKey;
import org.springframework.stereotype.Component;

@Component
public class FloatGeneric implements Generic<Float> {
	@Override
	public String getStrategyKey() {
		return StrategyKey.KEY_FLOAT;
	}

	@Override
	public Class<Float> getActualClass() {
		return Float.class;
	}
}
