package com.vergilyn.examples.springboot.usage.u0003.core;

import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;
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
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpringStrategyBeanLookupUtils {

	/**
	 * <p> 1. TODO 2022-06-08，可以再做一层 cache-map。
	 * <p> 2. 可以考虑 {@linkplain ApplicationContext#getBeansWithAnnotation(Class)}
	 * <p> 3. {@link ApplicationContext#getBeansOfType(Class)} 虽然是 {@link java.util.LinkedHashMap}，
	 * 但是貌似不支持{@link Order}（支持 {@link Ordered}），所以用{@link AnnotationAwareOrderComparator}重新排序。
	 */
	public static <K, V> List<V> lookup(ApplicationContext context, K key, Class<V> clazz, Function<V, K> keyFunction) {
		Assert.notNull(key, "`key` must not be null.");
		Assert.notNull(clazz, "`clazz` must not be null.");
		Assert.notNull(context, "`context` must not be null.");

		// 实际得到的是 `LinkedHashMap`
		Map<String, V> beans = context.getBeansOfType(clazz);
		if (beans == null || beans.isEmpty()) {
			return Lists.newArrayList();
		}

		List<V> list = beans.values().stream().filter(bean -> key.equals(keyFunction.apply(bean))).collect(Collectors.toList());

		// OrderComparator.sort(list);  // 不支持`@Order`注解

		AnnotationAwareOrderComparator.sort(list);

		return list;
	}

	public static <K, V> List<V> lookup(ApplicationContext context, K key, ParameterizedTypeReference<V> typeReference, Function<V, K> keyFunction) {
		Type expectedType = typeReference.getType();
		ResolvableType type = ResolvableType.forType(expectedType);

		List<V> beans = Lists.newArrayList();
		String[] candidateBeanNames = context.getBeanNamesForType(type);
		if (candidateBeanNames == null || candidateBeanNames.length == 0){
			return beans;
		}

		Class<V> requiredType = acquireRawClass(typeReference);
		for (String candidateBeanName : candidateBeanNames) {
			V bean = context.getBean(candidateBeanName, requiredType);
			if (key.equals(keyFunction.apply(bean))){
				beans.add(bean);
			}
		}

		return beans;
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
	public static <V> Class<V> acquireRawClass(ParameterizedTypeReference<V> typeReference){
		Type type = typeReference.getType();
		if (type instanceof ParameterizedType){
			return (Class<V>) ((ParameterizedType) type).getRawType();
		}

		throw new UnsupportedOperationException();
	}

}
