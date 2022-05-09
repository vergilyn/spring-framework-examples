package com.vergilyn.examples.springboot.usage.u0001;

import org.springframework.stereotype.Component;

@Component
public class MetricFilter extends AbstractFilter {
	@Override
	public StrategyEnum getType() {
		return StrategyEnum.A;
	}
}
