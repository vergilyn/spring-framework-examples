package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.u0001.StrategyBasicTests;
import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import com.vergilyn.examples.springboot.usage.u0002.StrategyExtendsTests;
import com.vergilyn.examples.springboot.usage.u0003.generic.BigIntegerExt;
import com.vergilyn.examples.springboot.usage.u0003.generic.Generic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.RequestEntity;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 参考的是`RedisTemplate`的方式，相比 `{@link StrategyBasicTests} & {@link StrategyExtendsTests}`
 * 这种 组合的方式 更易在spring项目中通用。
 *
 * <p> <b>不友好：</b> SpringStrategyTemplate 提供的所有方法还需要多传 Class/ParameterizedTypeReference。
 * <br/> <b>貌似java的泛型擦除后，只能这么写。参考：</b>
 * <pre>
 *   - {@link org.springframework.web.client.RestTemplate#exchange(RequestEntity, Class)}
 *   - {@link org.springframework.web.client.RestTemplate#exchange(RequestEntity, ParameterizedTypeReference)}
 * </pre>
 *
 * @author vergilyn
 * @since 2022-06-08
 */
@SuppressWarnings({"JavadocReference", "unchecked"})
class SpringStrategyTemplateTests extends AbstractSpringStrategyTemplateTests {

	@Autowired
	private ChainInvokerSpringStrategyTemplate<String, Generic<?>> genericStrategy;
	@Autowired
	private ChainInvokerSpringStrategyTemplate<String, Generic<Number>> numberGenericStrategy;
	@Autowired
	private ChainInvokerSpringStrategyTemplate<String, Generic<BigInteger>> bigIntegerGenericStrategy;
	@Autowired
	private ChainInvokerSpringStrategyTemplate<String, Generic<BigIntegerExt>> bigIntegerExtGenericStrategy;

	@Autowired
	private List<Generic<?>> _generics;
	@Autowired(required = false)
	private List<Generic<Number>> _numberGenerics;
	@Autowired
	private List<Generic<BigInteger>> _bigIntegerGenerics;
	@Autowired
	private List<Generic<BigIntegerExt>> _bigIntegerExtGenerics;

	/**
	 * 保持与 {@link GenericStrategyAutowiredTests#springAutowired()} 的注入一致。
	 */
	@Test
	public void expected(){
		String key = StrategyKey.KEY_INTEGER;

		List<Generic<?>> generics = genericStrategy.lookupBeans(key, new ParameterizedTypeReference<Generic<?>>() {});
		print("List<Generic<?>>", generics);
		Assertions.assertIterableEquals(generics, _generics);

		List<Generic<Number>> numberGenerics = numberGenericStrategy.lookupBeans(key, new ParameterizedTypeReference<Generic<Number>>() {});
		print("List<Generic<Number>>", numberGenerics);
		Assertions.assertTrue(() -> {
			boolean isExpectedNull = numberGenerics == null || numberGenerics.isEmpty();
			boolean isActualNull = _numberGenerics == null || _numberGenerics.isEmpty();
			if (isExpectedNull && isActualNull){
				return true;
			}

			Assertions.assertIterableEquals(numberGenerics, _numberGenerics);
			return true;
		});

		List<Generic<BigInteger>> bigIntegerGenerics = bigIntegerGenericStrategy.lookupBeans(key, new ParameterizedTypeReference<Generic<BigInteger>>() {});
		print("List<Generic<BigInteger>>", bigIntegerGenerics);
		Assertions.assertIterableEquals(bigIntegerGenerics, _bigIntegerGenerics);

		List<Generic<BigIntegerExt>> bigIntegerExtGenerics = bigIntegerExtGenericStrategy.lookupBeans(key, new ParameterizedTypeReference<Generic<BigIntegerExt>>() {});
		print("List<Generic<BigIntegerExt>>", bigIntegerExtGenerics);
		Assertions.assertIterableEquals(bigIntegerExtGenerics, _bigIntegerExtGenerics);
	}

	/**
	 *
	 * {@link #lookupBeansByClass(Object, ParameterizedTypeReference)}方法逻辑 <b>与 spring-autowired不一样</b>，最终得到的类似 `class = Generic.class`，
	 * 所以 {@code List<Generic<Number>>} 与 {@code List<Generic<BigInteger>>} <b>完全相同，与spring的泛型注入不一样</b>。
	 *
	 * <p> 如果是spring-autowired，参考{@link GenericStrategyAutowiredTests#springAutowired()}
	 * <pre>
	 *   {@code List<Generic<Number>>},     size: 0
	 *   {@code List<Generic<?>>},          size: 4(all)
	 *   {@code List<Generic<BigInteger>>}, size: 1（spring泛型是 精确匹配exact-match，只会注入`BigIntegerGeneric`）
	 * </pre>
	 *
	 */
	@Test
	public void error(){
		// error: Cannot resolve method 'lookupBeans(String, Class<Generic>)'
		// List<Generic<Number>> generics = genericStrategy.lookupBeans(key, Generic.class);

		String key = StrategyKey.KEY_INTEGER;

		ChainInvokerSpringStrategyTemplate<String, Generic<Number>> spyNumberGenericStrategy = Mockito.spy(numberGenericStrategy);
		ParameterizedTypeReference<Generic<Number>> numberRef = new ParameterizedTypeReference<Generic<Number>>() {};
		Mockito.when(spyNumberGenericStrategy.lookupBeans(key, numberRef))
				.then(invocation -> {
					return lookupBeansByClass(key, numberRef);
				});
		List<Generic<Number>> numberGenerics = spyNumberGenericStrategy.lookupBeans(key, numberRef);
		print("List<Generic<Number>>", numberGenerics);


		ChainInvokerSpringStrategyTemplate<String, Generic<BigInteger>> spyBigIntegerGenerics= Mockito.spy(bigIntegerGenericStrategy);
		ParameterizedTypeReference<Generic<BigInteger>> bigIntegerRef = new ParameterizedTypeReference<Generic<BigInteger>>() {};
		Mockito.when(spyBigIntegerGenerics.lookupBeans(key, bigIntegerRef))
				.then(invocation -> {
					return lookupBeansByClass(key, bigIntegerRef);
				});
		List<Generic<BigInteger>> bigIntegerGenerics = spyBigIntegerGenerics.lookupBeans(key, bigIntegerRef);
		print("List<Generic<BigInteger>>", bigIntegerGenerics);

	}

	private <K, V extends SpringStrategy<K>> List<V> lookupBeansByClass(K key, ParameterizedTypeReference<V> reference){
		Assert.notNull(key, "`key` must not be null.");

		Type type = reference.getType();
		Class<V> clazz = null;
		if (type instanceof ParameterizedType){
			clazz = (Class<V>) ((ParameterizedType) type).getRawType().getClass();
		}

		// 实际得到的是 `LinkedHashMap`
		// 通过`getBeansOfType(Class<T> type)`，无法精确找到 带泛型的type，例如`Generic<BigInteger>`
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


	/**
	 * 参考{@linkplain org.springframework.data.redis.core.RedisTemplate}，泛型并没有破坏单例。
	 */
	@Test
	public void equals(){
		System.out.println("genericStrategy                 >>>> " + genericStrategy);
		System.out.println("numberGenericStrategy           >>>> " + numberGenericStrategy);
		System.out.println("bigIntegerGenericStrategy       >>>> " + bigIntegerGenericStrategy);
		System.out.println("bigIntegerExtGenericStrategy    >>>> " + bigIntegerExtGenericStrategy);

		Assertions.assertEquals(genericStrategy, numberGenericStrategy);
	}
}
