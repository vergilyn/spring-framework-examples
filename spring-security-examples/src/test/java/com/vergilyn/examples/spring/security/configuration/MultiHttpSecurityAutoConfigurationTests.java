package com.vergilyn.examples.spring.security.configuration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * 通过 {@linkplain EnableWebSecurity} 自动装配 {@linkplain org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration#httpSecurity()} 可知：
 * <pre>
 *     1) `HttpSecurity` 是 prototype，每次都会是一个新的实例去调用 apiFilterChain/formLoginFilterChain
 *     2) 如果多个HttpSecurity都匹配当前请求，那么只有第一个匹配的 HttpSecurity会被使用，后面的HttpSecurity不会再被执行。
 *     因此，<b>在配置多个HttpSecurity时，需要注意它们的顺序。</b>
 * </pre>
 *
 * @author vergilyn
 * @since 2023-04-18
 *
 * @see <a href="https://docs.spring.io/spring-security/reference/5.7.7/servlet/test/mockmvc/setup.html">
 *     Setting Up MockMvc and Spring Security</a>
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MultiHttpSecurityAutoConfigurationTests.TestSecurityConfiguration.class)
@WebAppConfiguration
@TestPropertySource(properties = {"logging.level.root=debug"})
@SuppressWarnings("JavadocReference")
class MultiHttpSecurityAutoConfigurationTests {
	private static final String HEADER_TYPE = "HEADER_TYPE";
	private static final String HEADER_TYPE_API_FILTER_CHAIN = "api_filter_chain";
	private static final String HEADER_TYPE_FORM_LOGIN_FILTER_CHAIN = "form_login_filter_chain";

	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@SneakyThrows
	@Test
	void apiFilterChain() {
		// 匹配到`apiFilterChain`。则`formLoginFilterChain`中的相关设置忽略。
		mvc.perform(post("/api/xxx"))
				.andExpect(MockMvcResultMatchers.header().string(HEADER_TYPE, HEADER_TYPE_API_FILTER_CHAIN))
				.andExpect(MockMvcResultMatchers.header().doesNotExist("X-Frame-Options"));
	}

	@SneakyThrows
	@Test
	void formLoginFilterChain() {
		// 匹配到`formLoginFilterChain`。则`apiFilterChain`中的相关设置忽略。
		mvc.perform(post("/form_login/xxx"))
				.andExpect(MockMvcResultMatchers.header().string(HEADER_TYPE, HEADER_TYPE_FORM_LOGIN_FILTER_CHAIN))
				.andExpect(MockMvcResultMatchers.header().string("X-Frame-Options", "DENY"));
	}

	/**
	 *
	 * @see <a href="https://docs.spring.io/spring-security/reference/5.7/servlet/configuration/java.html#_multiple_httpsecurity">
	 *      Multiple HttpSecurity</a>
	 * @see org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration
	 */
	@Configuration
	@EnableWebSecurity
	@SuppressWarnings("JavadocReference")
	static class TestSecurityConfiguration {

		@Bean
		@Order(1)
		public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
			http.headers().frameOptions().disable().addHeaderWriter(new StaticHeadersWriter(HEADER_TYPE, HEADER_TYPE_API_FILTER_CHAIN));

			http.antMatcher("/api/**")
					.authorizeHttpRequests(authorize -> authorize.anyRequest().hasRole("ADMIN"))
					.httpBasic(withDefaults());

			return http.build();
		}

		@Bean
		public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
			http.cors().disable();

			http.headers().frameOptions().deny().addHeaderWriter(new StaticHeadersWriter(HEADER_TYPE, HEADER_TYPE_FORM_LOGIN_FILTER_CHAIN));

			http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
					.formLogin(withDefaults());

			return http.build();
		}
	}
}