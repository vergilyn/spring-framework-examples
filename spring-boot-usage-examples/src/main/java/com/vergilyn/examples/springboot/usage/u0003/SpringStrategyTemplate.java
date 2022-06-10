package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.u0003.core.SpringStrategyBeanLookupUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.Function;

/**
 * 参考{@link RestTemplate}：不使用spring泛型依赖注入，而是调用方法时传递需要的类型参数。
 *
 * <p> 2022-06-10，目前更推荐这种写法。（使用时相对更友好）
 */
@Component
public class SpringStrategyTemplate implements SpringStrategyOperations, BeanFactoryAware {

	private ListableBeanFactory listableBeanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		if (beanFactory instanceof ListableBeanFactory){
			this.listableBeanFactory = (ListableBeanFactory) beanFactory;
		}
	}

	@Override
	public <K, V> List<V> lookupBeans(K key, Class<V> clazz, Function<V, K> keyFunction) {
		return SpringStrategyBeanLookupUtils.lookup(listableBeanFactory, key, clazz, keyFunction);
	}

	@Override
	public <K, V> List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference, Function<V, K> keyFunction) {
		return SpringStrategyBeanLookupUtils.lookup(listableBeanFactory, key, typeReference, keyFunction);
	}


}
