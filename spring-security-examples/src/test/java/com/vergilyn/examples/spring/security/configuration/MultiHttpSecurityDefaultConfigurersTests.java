package com.vergilyn.examples.spring.security.configuration;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.vergilyn.examples.spring.security.configuration.factories.CustomHttpSecurityConfigure.HEADER_CUSTOM_NOW_DATE;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * 由于存在2个 `SecurityFilterChain` 对应会实例话2个 `HttpSecurity`。
 * <p> 期望：同时添加一个`header: HEADER_TYPE`，并且都禁用`X-Frame-Options`。
 *
 * <p> 实现方式：
 * <pre> 目前(2023-04-18)，通过源码理解，个人认为最佳的实现方式是
 *     1) 实现并重写 {@linkplain AbstractConfiguredSecurityBuilder#configure()} ，
 *     - {@linkplain org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration#httpSecurity() HttpSecurityConfiguration#httpSecurity()}
 *       - {@linkplain org.springframework.security.config.annotation.web.configuration.HttpSecurityConfiguration#applyDefaultConfigurers(HttpSecurity) HttpSecurityConfiguration#applyDefaultConfigurers(HttpSecurity)}
 *     - {@linkplain AbstractConfiguredSecurityBuilder#doBuild()}
 * </pre>
 *
 * @author vergilyn
 * @since 2023-04-18
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MultiHttpSecurityDefaultConfigurersTests.TestSecurityConfiguration.class)
@WebAppConfiguration
@TestPropertySource(properties = {"logging.level.root=debug"})
@SuppressWarnings("JavadocReference")
class MultiHttpSecurityDefaultConfigurersTests {
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
				.andExpect(MockMvcResultMatchers.header().exists(HEADER_CUSTOM_NOW_DATE))
				.andExpect(MockMvcResultMatchers.header().doesNotExist("X-Frame-Options"));
	}

	@SneakyThrows
	@Test
	void formLoginFilterChain() {
		// 匹配到`formLoginFilterChain`。则`apiFilterChain`中的相关设置忽略。
		mvc.perform(post("/api/xxx"))
				.andExpect(MockMvcResultMatchers.header().exists(HEADER_CUSTOM_NOW_DATE))
				.andExpect(MockMvcResultMatchers.header().doesNotExist("X-Frame-Options"));
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
			http.antMatcher("/api/**")
					.authorizeHttpRequests(authorize -> authorize.anyRequest().hasRole("ADMIN"))
					.httpBasic(withDefaults());

			return http.build();
		}

		@Bean
		public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {

			http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
					.formLogin(withDefaults());

			return http.build();
		}
	}

}