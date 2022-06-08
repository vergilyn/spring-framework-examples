package com.vergilyn.examples.feature.properties;

import com.vergilyn.examples.feature.AbstractSpringFeatureTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 利用spring 替换某个文件中的占位符，占位符满足 SpEL
 *
 * @author vergilyn
 * @since 2021-10-14
 */
public class PropertyResolverTests extends AbstractSpringFeatureTests {

	/**
	 * spring: {@linkplain PropertySourcesPropertyResolver} <br/>
	 * spring-boot: {@linkplain org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver} <br/>
	 *
	 * 其核心逻辑都是: {@linkplain org.springframework.util.PropertyPlaceholderHelper#replacePlaceholders(String, Properties)}
	 */
	@Test
	public void spring(){
		String propertiesTemplate="maven_home=${MAVEN_HOME:unknown}";

		final AnnotationConfigApplicationContext context = initApplicationContext();
		context.refresh();

		final ConfigurableEnvironment environment = context.getEnvironment();

		PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(environment.getPropertySources());

		final String o = resolver.resolvePlaceholders(propertiesTemplate);
		System.out.println(o);

		String s = environment.resolvePlaceholders(propertiesTemplate);
		System.out.println(s);
	}

	@Test
	public void springboot(){
		Properties properties = new Properties();
		try(InputStream inputStream = this.getClass().getResourceAsStream("ext-properties.cfg")) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final AnnotationConfigApplicationContext context = initApplicationContext();
		context.refresh();

		final ConfigurableEnvironment environment = context.getEnvironment();
		PropertySourcesPlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(environment.getPropertySources());

		// FIXME 2021-10-14 有没有简单的写法？ spring没有直接提供改方法？
		properties.replaceAll((k, v) -> resolver.resolvePlaceholders(v));

		System.out.println(properties);
	}
}
