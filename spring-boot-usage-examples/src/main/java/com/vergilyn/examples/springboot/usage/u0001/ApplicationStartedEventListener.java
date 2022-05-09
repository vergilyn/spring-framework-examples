package com.vergilyn.examples.springboot.usage.u0001;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

import java.util.List;
import java.util.Map;

public class ApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent>, ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * 与 {@linkplain FilterFactory#afterPropertiesSet()} 方式相比，此方式可以 <b>阻止spring-boot启动</b>
	 *
	 * <p>2022-05-09，感觉不友好的地方，此方法需要调用{@linkplain FilterFactory#putAll(Map)} （或者类似的方法。）
	 */
	@Override
	public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
		Map<String, AbstractFilter> beans = applicationContext.getBeansOfType(AbstractFilter.class);

		Map<StrategyEnum, AbstractFilter> typeMap = Maps.newHashMap();
		Map<StrategyEnum, List<AbstractFilter>> repeatMap = Maps.newHashMap();

		for (AbstractFilter filter : beans.values()) {
			StrategyEnum strategyEnum = filter.getType();
			if (typeMap.containsKey(strategyEnum)){
				repeatMap.computeIfAbsent(strategyEnum, value -> Lists.newArrayList()).add(filter);
			}
			typeMap.put(strategyEnum, filter);
		}

		// 存在重复的`type-handler`，阻止 spring-boot 启动。
		if (!repeatMap.isEmpty()){
			StringBuilder msg = new StringBuilder();
			// TODO 2022-05-09 打印明细
			throw new IllegalArgumentException("存在重复的type-handler。重复项：" + msg.toString());
		}

		FilterFactory filterFactory = applicationContext.getBean(FilterFactory.class);
		filterFactory.putAll(typeMap);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
