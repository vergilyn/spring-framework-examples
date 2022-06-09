package com.vergilyn.examples.springboot.usage.u0003;

import com.google.common.collect.Lists;
import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import com.vergilyn.examples.springboot.usage.u0003.core.SpringStrategyBeanLookupUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * 由于泛型擦除，必须传递 class/ParameterizedTypeReference。并且，就算带有泛型注入的其实也是同一个单例对象。
 * 那么感觉与其这么写（类似 {@link RedisTemplate}），<b>更推荐用 {@link RestTemplate}</b> 的写法，
 * 参考：{@link SpringStrategyTemplate}
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see RestTemplate#exchange(RequestEntity, Class)
 * @see RestTemplate#exchange(RequestEntity, ParameterizedTypeReference)
 * @see RedisTemplate
 * @see RedisAutoConfiguration
 */
@SuppressWarnings("JavadocReference")
public class ChainInvokerSpringStrategyTemplate<K, V extends SpringStrategy<K>>
		implements InvokerSpringStrategy<K, V>, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public List<V> lookupBeans(K key, Class<V> clazz) {
		List<V> beans = SpringStrategyBeanLookupUtils.lookup(applicationContext, key, clazz, SpringStrategy::getStrategyKey);

		return Optional.ofNullable(beans).orElse(Lists.newArrayList());
	}

	@Override
	public List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference) {
		return SpringStrategyBeanLookupUtils.lookup(applicationContext, key, typeReference, SpringStrategy::getStrategyKey);
	}


}
