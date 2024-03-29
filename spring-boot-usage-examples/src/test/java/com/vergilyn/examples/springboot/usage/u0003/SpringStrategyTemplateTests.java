package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.u0001.StrategyBasicTests;
import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import com.vergilyn.examples.springboot.usage.u0002.StrategyExtendsTests;
import com.vergilyn.examples.springboot.usage.u0003.generic.BigIntegerExt;
import com.vergilyn.examples.springboot.usage.u0003.generic.Generic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.RequestEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

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
 *   - {@link RestTemplate#exchange(RequestEntity, Class)}
 *   - {@link RestTemplate#exchange(RequestEntity, ParameterizedTypeReference)}
 * </pre>
 *
 * <p>
 * <h3>{@link SpringStrategyTemplate} VS {@link ChainGenericClassSpringStrategy}</h3>
 * <p> 1. {@link SpringStrategyTemplate} 参考 {@link RestTemplate}，不使用spring的泛型依赖注入。
 * <pre>
 * // 调用者在注入时相对更简单（不再需要指明泛型），例如
 * {@code
 *    @Autowired
 *    private SpringStrategyTemplate strategyTemplate;
 * }
 *
 * // 实际使用时，最后的`KeyFunction`根据接口抽象程度，也可以不用 显式(explicit)传递。
 * {@code
 *    strategyTemplate.lookupBeans(key, typeReference, generic -> generic.getStrategyKey())
 * }
 * </pre>
 *
 * <pre> <b>特别注意，例如：</b>
 * {@code
 *   // IService<T>,  mybatis-plus
 *   public interface SendRecordIService<K, T extends AbstractSendRecordEntity>
 *       extends SpringStrategy<K>, IService<T>{
 *
 *   }
 *
 *   @Service
 *   public class SendRecordEmailServiceImpl
 * 		extends SendRecordIServiceImpl<MessageTypeEnum, EmailMapper, EmailEntity>
 * 		implements SendRecordEmailService {
 *
 *   }
 *
 *   @Service
 *   public class SendRecordSmsServiceImpl
 * 		extends SendRecordIServiceImpl<MessageTypeEnum, SmsMapper, SmsEntity>
 * 		implements SendRecordSmsService {
 *
 *   }
 * }
 *
 * 根据spring特性，通过 {@code `new ParameterizedTypeReference<SendRecordIService<MessageTypeEnum, AbstractSendRecordEntity>>()`}
 * （类似 {@code @Autowired(required = false) List<Generic<Number>> _numberGenerics}）
 * <b>无法找到期望的spring-bean</b>，参考：{@link ListableBeanFactory#getBeanNamesForType(ResolvableType)}
 *
 * 可以写成 {@code `new ParameterizedTypeReference<SendRecordIService>()`} 用隐式强转换。
 *
 * </pre>
 *
 * @author vergilyn
 * @since 2022-06-08
 */
@SuppressWarnings({"JavadocReference", "unchecked"})
class SpringStrategyTemplateTests extends AbstractSpringStrategyTemplateTests {

	@Autowired
	private ChainGenericClassSpringStrategy<String, Generic<?>> genericStrategy;
	@Autowired
	private ChainGenericClassSpringStrategy<String, Generic<Number>> numberGenericStrategy;
	@Autowired
	private ChainGenericClassSpringStrategy<String, Generic<BigInteger>> bigIntegerGenericStrategy;
	@Autowired
	private ChainGenericClassSpringStrategy<String, Generic<BigIntegerExt>> bigIntegerExtGenericStrategy;

	@Autowired
	private List<Generic<?>> _generics;
	@Autowired(required = false)
	private List<Generic<Number>> _numberGenerics;
	@Autowired
	private List<Generic<BigInteger>> _bigIntegerGenerics;
	@Autowired
	private List<Generic<BigIntegerExt>> _bigIntegerExtGenerics;

	@Autowired
	private SpringStrategyTemplate strategyTemplate;

	/**
	 * <p> 2022-06-10，目前更推荐 {@code SpringStrategyTemplate} 的写法。（使用时相对更友好）
	 */
	@Test
	public void compare(){
		String key = StrategyKey.KEY_INTEGER;

		print("List<Generic<?>> Origin-Spring-Generic-Autowired", _generics);

		List<Generic<?>> expectedGenerics = filter(_generics, key);
		print("List<Generic<?>> Origin-Spring-Generic-Autowired-Filter", expectedGenerics);


		ParameterizedTypeReference<Generic<?>> typeReference = new ParameterizedTypeReference<Generic<?>>() {};

		List<Generic<?>> generics = genericStrategy.lookupBeans(key, typeReference);
		print("List<Generic<?>> Spring-Generic-Autowired", generics);

		List<Generic<?>> notGenericAutowired = strategyTemplate.lookupBeans(key, typeReference, SpringStrategy::getStrategyKey);
		print("List<Generic<?>> Not-Spring-Generic-Autowired", notGenericAutowired);

		Assertions.assertIterableEquals(expectedGenerics, generics);
		Assertions.assertIterableEquals(generics, notGenericAutowired);
	}

	/**
	 * 保持与 {@link GenericStrategyAutowiredTests#springAutowired()} 的注入一致。
	 */
	@Test
	public void expected(){
		String key = StrategyKey.KEY_INTEGER;

		// 保留 key 对应的 Generic.
		List<Generic<?>> expectedGenerics = filter(_generics, key);

		List<Generic<?>> generics = genericStrategy.lookupBeans(key, new ParameterizedTypeReference<Generic<?>>() {});
		print("List<Generic<?>>", generics);
		Assertions.assertIterableEquals(generics, expectedGenerics);

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

		ChainGenericClassSpringStrategy<String, Generic<Number>> spyNumberGenericStrategy = Mockito.spy(numberGenericStrategy);
		ParameterizedTypeReference<Generic<Number>> numberRef = new ParameterizedTypeReference<Generic<Number>>() {};
		Mockito.when(spyNumberGenericStrategy.lookupBeans(key, numberRef))
				.then(invocation -> {
					return lookupBeansByClass(key, numberRef);
				});
		List<Generic<Number>> numberGenerics = spyNumberGenericStrategy.lookupBeans(key, numberRef);
		print("List<Generic<Number>>", numberGenerics);


		ChainGenericClassSpringStrategy<String, Generic<BigInteger>> spyBigIntegerGenerics= Mockito.spy(bigIntegerGenericStrategy);
		ParameterizedTypeReference<Generic<BigInteger>> bigIntegerRef = new ParameterizedTypeReference<Generic<BigInteger>>() {};
		Mockito.when(spyBigIntegerGenerics.lookupBeans(key, bigIntegerRef))
				.then(invocation -> {
					return lookupBeansByClass(key, bigIntegerRef);
				});
		List<Generic<BigInteger>> bigIntegerGenerics = spyBigIntegerGenerics.lookupBeans(key, bigIntegerRef);
		print("List<Generic<BigInteger>>", bigIntegerGenerics);

	}

	private List<Generic<?>> filter(List<Generic<?>> origin, String key){
		// 保留 key 对应的 Generic.
		return  origin.stream()
				.filter(generic -> key.equals(generic.getStrategyKey()))
				.collect(Collectors.toList());
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
