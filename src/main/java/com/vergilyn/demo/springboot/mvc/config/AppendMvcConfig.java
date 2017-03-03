package com.vergilyn.demo.springboot.mvc.config;

import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/* 如果想保留SpringBoot MVC的特性，并只是添加其他的MVC配置(拦截器，formatters，视图控制器等)，
 * 你可以添加自己的WebMvcConfigurerAdapter类型的@Bean（不使用@EnableWebMvc注解）。
 */

@Configuration
public class AppendMvcConfig {
	
	/*
	 * 任何在上下文中出现的HttpMessageConverter bean将会添加到converters列表，你可以通过这种方式覆盖默认的转换器（converters）
	 */
	@Bean
	public HttpMessageConverters customConverters() {
		HttpMessageConverter<?>[] converters = {};
		return new HttpMessageConverters(converters);
	}
}
