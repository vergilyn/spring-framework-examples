package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.u0001.StrategyBasicTests;
import com.vergilyn.examples.springboot.usage.u0002.StrategyExtendsTests;
import com.vergilyn.examples.springboot.usage.u0003.generic.BigIntegerExt;
import com.vergilyn.examples.springboot.usage.u0003.generic.BigIntegerExtGeneric;
import com.vergilyn.examples.springboot.usage.u0003.generic.BigIntegerGeneric;
import com.vergilyn.examples.springboot.usage.u0003.generic.Generic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.QualifierAnnotationAutowireCandidateResolver;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericTypeAwareAutowireCandidateResolver;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.ResolvableType;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * 参考的是`RedisTemplate`的方式，相比 `{@link StrategyBasicTests} & {@link StrategyExtendsTests}`
 * 这种 组合的方式 更易在spring项目中通用。
 *
 * @author vergilyn
 * @since 2022-06-08
 */
@SuppressWarnings("JavadocReference")
class GenericStrategyAutowiredTests extends AbstractSpringStrategyTemplateTests {

	@Autowired
	private List<Generic<?>> _generics;
	@Autowired(required = false)
	private List<Generic<Number>> _numberGenerics;
	@Autowired
	private List<Generic<BigInteger>> _bigIntegerGenerics;
	@Autowired
	private List<Generic<BigIntegerExt>> _bigIntegerExtGenerics;

	/**
	 * <p> 1. spring通过{@link Autowired}注入<b>泛型对象</b>时，只支持精确匹配泛型。
	 * 所以{@code List<Generic<BigInteger>>} 只会注入 {@link BigIntegerGeneric}，<b>并不会注入 {@link BigIntegerExtGeneric}</b>。
	 * <pre>核心参考：
	 *     {@link ResolvableType#isAssignableFrom(ResolvableType)}, line: 348
	 *     // We need an exact type match for generics
	 * 	   // List<CharSequence> is not assignable from List<String>
	 * </pre>
	 *
	 * <p> 2. 为什么 {@code List<Generic<?>>} <b>可以注入全部的 泛型对象</b>？
	 * 其实`?`等价于 Object.class，spring.ResolvableType进行了特殊处理！
	 * <pre>核心参考：
	 *     {@link ResolvableType#isAssignableFrom(ResolvableType)}, line: 308
	 *     // In the form <? extends Number> is assignable to X...
	 * </pre>
	 *
	 *
	 * <p>
	 * <h3>spring-beans: 5.2.12.RELEASE</h3>
	 * <pre>
	 * 	  at org.springframework.beans.factory.support.DefaultListableBeanFactory.addCandidateEntry(DefaultListableBeanFactory.java:1524)
	 * 	  at org.springframework.beans.factory.support.DefaultListableBeanFactory.findAutowireCandidates(DefaultListableBeanFactory.java:1489)
	 * 	  at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveMultipleBeans(DefaultListableBeanFactory.java:1378)
	 * 	  at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1265)
	 * 	  at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1227)
	 * 	  at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:640)
	 * 	  at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:119)
	 * 	  at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:399)
	 * 	  at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1420)
	 * 	  at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireBeanProperties(AbstractAutowireCapableBeanFactory.java:392)
	 * </pre>
	 * <pre>
	 *     核心是在 {@link DefaultListableBeanFactory#findAutowireCandidates(String, Class, DependencyDescriptor)}
	 *     中会调用
	 *     - {@link DefaultListableBeanFactory#isAutowireCandidate(String, DependencyDescriptor)}
	 *     - {@link ContextAnnotationAutowireCandidateResolver#isAutowireCandidate(BeanDefinitionHolder, DependencyDescriptor)}
	 *     - {@link QualifierAnnotationAutowireCandidateResolver#isAutowireCandidate(BeanDefinitionHolder, DependencyDescriptor)}
	 *     - {@link GenericTypeAwareAutowireCandidateResolver#isAutowireCandidate(BeanDefinitionHolder, DependencyDescriptor)}
	 *       - {@link GenericTypeAwareAutowireCandidateResolver#checkGenericTypeMatch(BeanDefinitionHolder, DependencyDescriptor)}
	 *     - {@link ResolvableType#isAssignableFrom(ResolvableType)}
	 *
	 *     如果`isAutowireCandidate = true`，那么就会将找到 dependency-bean 注入到相关的 bean 中。
	 *     {@link DefaultListableBeanFactory#addCandidateEntry(Map, String, DependencyDescriptor, Class)}
	 * </pre>
	 *
	 *
	 * @see DefaultListableBeanFactory#findAutowireCandidates(String, Class, DependencyDescriptor)
	 */
	@Test
	public void springAutowired(){
		// expected: 4
		// actual: 4
		print("List<Generic<?>>", _generics);

		// expected: 4, `BigInteger & BigIntegerExt & Float & Long`
		// actual: 0,
		// 期望获取 Number 以及 Number-subclass 的所有 generic-bean，
		// 但，实际spring只会去查找 完全配置`Generic<Number>`的 bean，所以`actual: 0`.
		print("List<Generic<Number>>", _numberGenerics);

		// expected: 2, `BigInteger & BigIntegerExt`
		// actual: 1, `BigInteger`
		// 同上，
		print("List<Generic<BigInteger>>", _bigIntegerGenerics);

		// actual: 1
		print("List<Generic<BigIntegerExt>>", _bigIntegerExtGenerics);

	}


}
