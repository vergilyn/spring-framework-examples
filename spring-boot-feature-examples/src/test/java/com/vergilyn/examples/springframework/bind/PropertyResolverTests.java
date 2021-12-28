package com.vergilyn.examples.springframework.bind;

import java.util.Properties;

import com.vergilyn.examples.springframework.AbstractSpringBootFeatureTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.core.env.AbstractPropertyResolver;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;

@SuppressWarnings("JavadocReference")
public class PropertyResolverTests extends AbstractSpringBootFeatureTests {
	private final String _key = "vergilyn.application.name";
	private final String _value = "vergilyn-${spring.application.name}-${server.port:unknown}";

	@Autowired
	private Environment environment;

	/**
	 * {@linkplain Environment}中保存的还是原始值（包含占位符表达式），
	 * <b>每次调用</b>{@linkplain Environment#getProperty(String)}时，才会替换占位符后返回真实的值。
	 *
	 * <pre>
	 *   {@linkplain PropertyResolver#getProperty(java.lang.String)}
	 *   {@linkplain PropertySourcesPropertyResolver#getProperty(java.lang.String)}
	 *   {@linkplain PropertySourcesPropertyResolver#getProperty(java.lang.String, java.lang.Class, boolean)}
	 *   {@linkplain AbstractPropertyResolver#resolveNestedPlaceholders(java.lang.String)}
	 *
	 *   {@linkplain PropertySourcesPlaceholdersResolver#PropertySourcesPlaceholdersResolver(java.lang.Iterable, org.springframework.util.PropertyPlaceholderHelper)}
	 *   {@linkplain org.springframework.util.PropertyPlaceholderHelper}  最基本的核心！
	 * </pre>
	 *
	 */
	@Test
	public void resolver(){
		MutablePropertySources sources = ((ConfigurableEnvironment) environment).getPropertySources();

		Properties properties = new Properties();
		properties.put(_key, _value);

		PropertiesPropertySource propertySource = new PropertiesPropertySource("vergilyn.tests.property-resolver", properties);
		sources.addLast(propertySource);

		final String property = environment.getProperty(_key);
		System.out.println("environment.getProperty(key) >>>> " + property);

		final String result = environment.resolvePlaceholders(_value);
		System.out.println("environment.resolvePlaceholders(text) >>>> " + result);
	}

	/**
	 * 类似字符串格式化
	 *
	 * @see String#format(String, Object...)
	 * @see java.text.MessageFormat#format(String, Object...)
	 * @see <a href="https://github.com/apache/commons-text/blob/master/src/main/java/org/apache/commons/text/StrSubstitutor.java">
	 *     commons-text, `StringSubstitutor.class`（相对jdk自带的，扩展性更高，相应的性能应该相对低。也支持 默认值，表达式计算等）</a>
	 */
	@Test
	public void replace(){

	}
}
