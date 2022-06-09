package com.vergilyn.examples.springboot.usage.u0003;

import com.google.common.collect.Lists;
import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see org.springframework.data.redis.core.RedisTemplate
 * @see org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
 */
@SuppressWarnings("JavadocReference")
public class ChainInvokerSpringStrategyTemplate<K, V extends SpringStrategy<K>>
		implements InvokerSpringStrategy<K, V>, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public List<V> lookupBeans(K key, Class<V> clazz) {
		List<V> beans = lookupBeansCore(key, clazz);

		return Optional.ofNullable(beans).orElse(Lists.newArrayList());
	}

	@Override
	public List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference) {
		Type expectedType = typeReference.getType();
		ResolvableType type = ResolvableType.forType(expectedType);

		List<V> beans = Lists.newArrayList();
		String[] candidateBeanNames = applicationContext.getBeanNamesForType(type);
		if (candidateBeanNames == null || candidateBeanNames.length == 0){
			return beans;
		}

		Class<V> requiredType = acquireRawClass(typeReference);
		for (String candidateBeanName : candidateBeanNames) {
			V bean = applicationContext.getBean(candidateBeanName, requiredType);
			beans.add(bean);
		}

		return beans;
	}

	@Override
	public V getHighestPriority(K key, Class<V> clazz) {
		List<V> all = lookupBeans(key, clazz);

		if (all.isEmpty()){
			return null;
		}

		return all.get(0);
	}

	@Override
	public V getLowerPriority(K key, Class<V> clazz) {
		List<V> all = lookupBeans(key, clazz);

		if (all.isEmpty()){
			return null;
		}

		return all.get(all.size() - 1);
	}

	@Override
	public void invoke(K key, Class<V> clazz, Consumer<V> invoker) {
		for (V bean : lookupBeans(key, clazz)) {
			invoker.accept(bean);
		}
	}

	/**
	 * 例如
	 * <pre>
	 *     {@code new ParameterizedTypeReference<Generic<BigInteger>>() {}}
	 * </pre>
	 * 最后返回的`class = Generic.class`，之后如果再通过{@link ApplicationContext#getBeansOfType(Class)}
	 * 会得到所有的`Generic-beans`，而不是 精确匹配的`BigIntegerGeneric`。
	 *
	 */
	public Class<V> acquireRawClass(ParameterizedTypeReference<V> typeReference){
		Type type = typeReference.getType();
		if (type instanceof ParameterizedType){
			return (Class<V>) ((ParameterizedType) type).getRawType();
		}

		throw new UnsupportedOperationException();
	}

	/**
	 * <p> 1. TODO 2022-06-08，可以做一层 cache-map。
	 * <p> 2. 可以考虑 {@linkplain ApplicationContext#getBeansWithAnnotation(Class)}
	 * <p> 3. {@link ApplicationContext#getBeansOfType(Class)} 虽然是 {@link java.util.LinkedHashMap}，
	 *   但是貌似不支持{@link Order}（支持 {@link Ordered}），所以用{@link AnnotationAwareOrderComparator}重新排序。
	 */
	public List<V> lookupBeansCore(K key, Class<V> clazz){
		Assert.notNull(key, "`key` must not be null.");

		// 实际得到的是 `LinkedHashMap`
		Map<String, V> beans = applicationContext.getBeansOfType(clazz);
		if (beans == null || beans.isEmpty()){
			return null;
		}

		List<V> list = beans.values().stream()
				.filter(v -> key.equals(v.getStrategyKey()))
				.collect(Collectors.toList());

		// OrderComparator.sort(list);  // 不支持`@Order`注解

		AnnotationAwareOrderComparator.sort(list);

		return list;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
