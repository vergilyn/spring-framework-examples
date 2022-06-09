package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.u0003.core.SpringStrategyBeanLookupUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Function;

/**
 * 参考{@link RestTemplate}：不使用spring泛型依赖注入，而是调用方法时传递需要的类型参数。
 */
@Component
public class SpringStrategyTemplate implements InvokerSpringStrategyOperations, ApplicationContextAware {

	private ApplicationContext applicationContext;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public <K, V> List<V> lookupBeans(K key, Class<V> clazz, Function<V, K> keyFunction) {
		return SpringStrategyBeanLookupUtils.lookup(applicationContext, key, clazz, keyFunction);
	}

	@Override
	public <K, V> List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference, Function<V, K> keyFunction) {
		return SpringStrategyBeanLookupUtils.lookup(applicationContext, key, typeReference, keyFunction);
	}
}
