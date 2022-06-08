package com.vergilyn.examples.springboot.usage.u0003.handler;

import com.vergilyn.examples.springboot.usage.u0003.StrategyKey;
import org.springframework.stereotype.Component;

@Component
public class MetricHandler implements Handler {

	@Override
	public String getStrategyKey() {
		return StrategyKey.KEY_METRIC;
	}
}
