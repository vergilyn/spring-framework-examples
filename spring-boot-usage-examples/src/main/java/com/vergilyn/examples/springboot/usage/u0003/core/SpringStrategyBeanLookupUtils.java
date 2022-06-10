package com.vergilyn.examples.springboot.usage.u0003.core;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p> 1. XXX 2022-06-08，可以再做一层 cache-map。
 * <p> 2. XXX 2022-06-10，由于spring lazy-load，所以某个class类型的首次获取速度较慢。
 * <p> 3. 可以考虑 {@linkplain ApplicationContext#getBeansWithAnnotation(Class)}
 * <p> 4. {@link ApplicationContext#getBeansOfType(Class)} 虽然是 {@link java.util.LinkedHashMap}，
 *   但是貌似不支持{@link Order}（支持 {@link Ordered}），所以用{@link AnnotationAwareOrderComparator}重新排序。
 *
 * @author vergilyn
 * @since 2022-06-10
 *
 * @see org.springframework.beans.factory.BeanFactoryUtils
 */
public class SpringStrategyBeanLookupUtils {

	public static <K, V> List<V> lookup(ListableBeanFactory listableBeanFactory, K key, Class<V> clazz, Function<V, K> keyFunction) {
		Assert.notNull(clazz, "`clazz` must not be null.");

		return lookup(listableBeanFactory, key, ResolvableType.forType(clazz), keyFunction);
	}

	public static <K, V> List<V> lookup(ListableBeanFactory listableBeanFactory, K key, ParameterizedTypeReference<V> typeReference, Function<V, K> keyFunction) {
		Assert.notNull(typeReference, "`typeReference` must not be null.");

		return lookup(listableBeanFactory, key, ResolvableType.forType(typeReference), keyFunction);
	}

	/**
	 * @see ListableBeanFactory#getBeanNamesForType(ResolvableType)
	 * @see ListableBeanFactory#getBean(String, Class)
	 * @see BeanFactoryUtils#beanNamesForTypeIncludingAncestors(ListableBeanFactory, ResolvableType)
	 */
	public static <K, V> List<V> lookup(ListableBeanFactory listableBeanFactory, K key, ResolvableType type, Function<V, K> keyFunction){
		Assert.notNull(listableBeanFactory, "`listableBeanFactory` must not be null.");
		Assert.notNull(key, "`key` must not be null.");
		Assert.notNull(type, "`type` must not be null.");
		Assert.notNull(keyFunction, "`keyFunction` must not be null.");

		// XXX 2022-06-10，有什么区别？
		//   看`BeanFactoryUtils`多了一层递归逻辑。但`BeanFactoryUtils`又没提供 `BeanFactoryUtils#getBean(beanName, requiredClass)`
		String[] candidateBeanNames = listableBeanFactory.getBeanNamesForType(type);
		// String[] candidateBeanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(listableBeanFactory, type);

		List<V> expectedBeans = new ArrayList<>(16);

		if (candidateBeanNames == null || candidateBeanNames.length == 0){
			return expectedBeans;
		}

		Class<V> requiredType = (Class<V>) type.resolve();
		V candidateBean;
		for (String candidateBeanName : candidateBeanNames) {
			candidateBean = listableBeanFactory.getBean(candidateBeanName, requiredType);

			if (candidateBean != null && key.equals(keyFunction.apply(candidateBean))){
				expectedBeans.add(candidateBean);
			}
		}

		AnnotationAwareOrderComparator.sort(expectedBeans);

		return expectedBeans;
	}

	/**
	 * 会导致 {@code `Generic<?>`} 和 {@code `Generic<Number>`} 得到的结果一样。<br/>
	 * 根据 spring泛型注入的特性，
	 * <pre>
	 *     {@code `Generic<?>`}：得到所有 Generic的beans。
	 *     {@code `Generic<Number>`}：无法得到任何 bean。
	 * </pre>
	 *
	 * @deprecated 2022-06-10， {@link #lookup(ListableBeanFactory, Object, Class, Function)}
	 */
	public static <K, V> List<V> lookupError(ListableBeanFactory listableBeanFactory, K key, ResolvableType type, Function<V, K> keyFunction){
		Assert.notNull(listableBeanFactory, "`listableBeanFactory` must not be null.");
		Assert.notNull(key, "`key` must not be null.");
		Assert.notNull(type, "`type` must not be null.");
		Assert.notNull(keyFunction, "`keyFunction` must not be null.");

		// 会导致 `Generic<?>` 和 `Generic<Number>` 得到的结果一样。
		Class<V> requiredType = (Class<V>) type.resolve();
		Map<String, V> candidateBeanMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(listableBeanFactory, requiredType);

		List<V> expectedBeans = candidateBeanMap.values().stream()
				.filter(candidateBean -> key.equals(keyFunction.apply(candidateBean)))
				.collect(Collectors.toList());

		AnnotationAwareOrderComparator.sort(expectedBeans);

		return expectedBeans;
	}

	/**
	 * 例如
	 * <pre>
	 *     {@code new ParameterizedTypeReference<Generic<BigInteger>>() {}}
	 * </pre>
	 * 最后返回的`class = Generic.class`，之后如果再通过{@link ApplicationContext#getBeansOfType(Class)}
	 * 会得到所有的`Generic-beans`，而不是 精确匹配的`BigIntegerGeneric`。
	 *
	 * @deprecated {@link ResolvableType#resolve()}
	 */
	public static <V> Class<V> acquireRawClass(ParameterizedTypeReference<V> typeReference){
		Type type = typeReference.getType();
		if (type instanceof ParameterizedType){
			return (Class<V>) ((ParameterizedType) type).getRawType();
		}else if (type instanceof Class){
			return (Class<V>) type;
		}

		throw new UnsupportedOperationException();
	}

}
