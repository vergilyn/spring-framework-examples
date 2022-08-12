package com.vergilyn.examples.springboot.usage.u0004;

import com.alibaba.fastjson.JSON;
import com.vergilyn.examples.springboot.usage.AbstractSpringbootUsageApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.testng.collections.Maps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SpringTests extends AbstractSpringbootUsageApplicationTest {

	@Autowired
	List<StatsChain> chains;

	@Autowired
	List<AbstractSmsChain> smsChains;

	/**
	 * 测试 spring-beans 的获取。
	 *
	 * <p>1. 实现类需要加 {@link Component} 等注解。
	 */
	@Test
	public void test(){

		Map<String, StatsChain> beansOfType = applicationContext.getBeansOfType(StatsChain.class);
		print("StatsChain >>>> ", beansOfType);

		Map<String, AbstractSmsChain> beans = applicationContext.getBeansOfType(AbstractSmsChain.class);
		print("AbstractSmsChain >>>> ", beans);

	}

	private <T extends StatsChain> void print(String prefix, Map<String, T> beans){
		Map<String, Object> result = Maps.newHashMap();
		result.put("size", beans.size());

		List<String> beanClasses = beans.values().stream().map(o -> o.getClass().getSimpleName()).collect(
				Collectors.toList());
		result.put("beans", beanClasses);

		System.out.println(JSON.toJSONString(result, true));
	}
}
