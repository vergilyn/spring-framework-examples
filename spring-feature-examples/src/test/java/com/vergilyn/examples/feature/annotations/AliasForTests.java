package com.vergilyn.examples.feature.annotations;

import com.vergilyn.examples.feature.AbstractSpringFeatureTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.*;
import java.lang.reflect.Method;

public class AliasForTests extends AbstractSpringFeatureTests {

	@Test
	@Owner("123")
	public void owner(){
		Method method = ReflectionUtils.findMethod(AliasForTests.class, "owner");

		Owner annotation = AnnotatedElementUtils.getMergedAnnotation(method, Owner.class);

		System.out.printf("value: %s, alias: %s, name: %s \n", annotation.value(), annotation.alias(), annotation.name());
		Assertions.assertEquals(annotation.alias(), annotation.value());
	}

	@Test
	@OwnerChild(name = "child")
	public void other(){
		Method method = ReflectionUtils.findMethod(AliasForTests.class, "other");

		// `@OwnerChild` 理解成 `@Owner` 的子类。
		Owner annotation = AnnotatedElementUtils.getMergedAnnotation(method, Owner.class);

		System.out.printf("value: %s, alias: %s, name: %s \n", annotation.value(), annotation.alias(), annotation.name());

		Assertions.assertEquals("child", annotation.name());
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
	@Documented
	private static @interface Owner {
		@AliasFor("alias")
		String value() default "owner";

		@AliasFor("value")
		String alias() default "owner";

		String name() default "owner";
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD})
	@Documented
	// 跨class的 AliasFor。
	// 这里我们将@Owner，@OwnerChild 看做一种特殊的继承关系，
	// @Owner是父注解，@OwnerChild是子注解，`@OwnerChild#name` 覆盖 `@Owner#name`）
	@Owner
	private static @interface OwnerChild {

		@AliasFor(value = "name", annotation = Owner.class)
		String name() default "owner-child";
	}
}
