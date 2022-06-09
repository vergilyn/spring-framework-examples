package com.vergilyn.examples.springboot.usage.u0003.generic;

import com.vergilyn.examples.springboot.usage.u0003.StrategyKey;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class BigIntegerGeneric implements Generic<BigInteger> {
	@Override
	public String getStrategyKey() {
		return StrategyKey.KEY_INTEGER;
	}

	@Override
	public Class<BigInteger> getActualClass() {
		return BigInteger.class;
	}
}
