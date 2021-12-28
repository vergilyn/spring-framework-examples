package com.vergilyn.examples.springframework.bind;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * <pre>
 *   {@linkplain org.springframework.util.PropertyPlaceholderHelper}
 *
 * 示例：
 *   spring-boot, {@linkplain PropertySourcesPlaceholdersResolver#PropertySourcesPlaceholdersResolver(java.lang.Iterable, org.springframework.util.PropertyPlaceholderHelper)}
 *   {@linkplain org.springframework.util.SystemPropertyUtils}
 * </pre>
 *
 * @author vergilyn
 * @since 2021-12-28
 */
public class PropertyPlaceholderHelperTests {
	/** Prefix for system property placeholders: "${". */
	public static final String PLACEHOLDER_PREFIX = "${";

	/** Suffix for system property placeholders: "}". */
	public static final String PLACEHOLDER_SUFFIX = "}";

	/** Value separator for system property placeholders: ":". */
	public static final String VALUE_SEPARATOR = ":";

	/**
	 * {@linkplain PropertyPlaceholderHelper}: <br/>
	 * 1. 使用的是 `indexOf` 和 `substring`，没有用 regexp方式实现。 <br/>
	 *
	 * 2. 默认分隔符是`:`，不能写成`: `。否则，空格会被当作是 默认值的一部分！ <br/>
	 *    FIXME 2021-12-28 spring-boot如何处理的此空格？
	 * 	  debug可知，`PropertyPlaceholderHelper`中无法扩展`String.trim()`。 <br/>
	 *
	 * 3. 只是最基本的替换，无法执行表达式/方法。例如`${num: random()}` <br/>
	 *
	 * 4. 无法提取出 text 中的 origin-placeholder-name
	 */
	@Test
	public void resolver(){

		PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX,
		                                                                            PLACEHOLDER_SUFFIX,
		                                                                            VALUE_SEPARATOR,
		                                                                            true);

		String text = "默认值`name: ${name:vergilyn}`，待替换的变量`param: ${param}，未知的变量`unknown: ${unknown}`";
		Properties properties = new Properties();
		properties.put("param", "param-01");

		String result = placeholderHelper.replacePlaceholders(text, properties);

		System.out.println(result);
	}
}
