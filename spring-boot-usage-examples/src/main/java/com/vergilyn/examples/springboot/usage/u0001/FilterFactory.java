package com.vergilyn.examples.springboot.usage.u0001;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FilterFactory implements InitializingBean, ApplicationContextAware {
	private ConcurrentHashMap<StrategyEnum, AbstractFilter> filterMap = new ConcurrentHashMap<>(16);
	private ApplicationContext applicationContext;

	public AbstractFilter get(StrategyEnum strategyEnum){
		return filterMap.get(strategyEnum);
	}

	public void putAll(Map<StrategyEnum, AbstractFilter> filterMap){
		if (filterMap == null || filterMap.isEmpty()){
			return;
		}

		this.filterMap.putAll(filterMap);
	}

	// 无法做到 禁止spring-boot启动
	@Override
	public void afterPropertiesSet() throws Exception {
		// initFilterMap();
	}

	private void initFilterMap(){
		applicationContext.getBeansOfType(AbstractFilter.class).values()
				.forEach(abstractFilter -> {
					// 期望：重复的`type-handler` 禁止spring-boot启动。
					filterMap.put(abstractFilter.getType(), abstractFilter);
				});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
