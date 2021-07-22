package com.vergilyn.examples.springframework.bind;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.vergilyn.examples.springframework.AbstractSpringBootFeatureTests;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.mock.env.MockEnvironment;

/**
 * e.g.
 * <pre>
 *   {@linkplain org.springframework.boot.SpringApplication#run(String...)}
 *   --> {@linkplain SpringApplication#bindToSpringApplication(ConfigurableEnvironment)}
 * </pre>
 * @author vergilyn
 * @since 2021-06-18
 *
 * @see <a href="https://github.com/spring-projects/spring-boot/tree/v2.2.11.RELEASE/spring-boot-project/spring-boot/src/test/java/org/springframework/boot/context/properties/bind"> Properties Bind Tests</a>
 */
@SuppressWarnings("JavadocReference")
public class BinderTests extends AbstractSpringBootFeatureTests {

	MockEnvironment environment;

	@BeforeEach
	public void beforeEach(){
		environment = new MockEnvironment();
	}

	@Test
	void list(){
		Integer[] excepted = {1, 2, 3, 4, 5};
		environment.setProperty("mock.list", StringUtils.join(excepted, ","));

		List<Integer> actual = Binder.get(environment)
				.bind("mock.list", Bindable.listOf(Integer.class)).get();

		printJson(actual, false);
	}

	@Test
	void map(){
		environment.setProperty("mock.map[a]", "1");
		environment.setProperty("mock.map[b]", "2");

		Map<String, Integer> actual = Binder.get(environment)
				.bind("mock.map", Bindable.mapOf(String.class, Integer.class)).get();

		printJson(actual);
	}

	@Test
	void enums(){
		// 不区分大小写
		environment.setProperty("mock.enum", "foo_bar");

		JavaEnum actual = Binder.get(environment)
				.bind("mock.enum", Bindable.of(JavaEnum.class)).get();

		printJson(actual);
	}

	@Test
	void javabean(){
		environment.setProperty("mock.javabean.value", "hello");
		environment.setProperty("mock.javabean.items", "bar,baz");

		JavaBean actual = Binder.get(environment)
				.bind( "mock.javabean", Bindable.of(JavaBean.class)).get();

		printJson(actual);
	}

	@Test
	void genericBean(){
		environment.setProperty("mock.generic.integer.value", "1");
		environment.setProperty("mock.generic.string.value", "string");

		GenericBean<Integer> integer = Binder.get(environment)
				.bind("mock.generic.integer", Bindable.of(GenericBean.class)).get();

		GenericBean<String> string = Binder.get(environment)
				.bind("mock.generic.string", Bindable.of(GenericBean.class)).get();

		System.out.println("mock.generic.integer: " + integer.value);
		System.out.println("mock.generic.string: " + string.value);
	}

	@Test
	void cycle(){

	}


	@Data
	static class JavaBean {

		private String value;

		private List<String> items = Collections.emptyList();
	}

	static enum JavaEnum{
		FOO_BAR("foo", 1), BAR_BAZ("bar", 2);

		public final String name;
		public final Integer code;

		JavaEnum(String name, Integer code) {
			this.name = name;
			this.code = code;
		}
	}

	@Data
	static class GenericBean<T> {
		private T value;
	}
}
