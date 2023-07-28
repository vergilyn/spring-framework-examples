package com.vergilyn.examples.spring.security.configuration.factories;

import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.time.LocalDate;


public class CustomHttpSecurityConfigure<B extends HttpSecurityBuilder<B>>
		extends AbstractHttpConfigurer<CustomHttpSecurityConfigure<B>, HttpSecurity>
		implements Ordered {

	public static final String HEADER_CUSTOM_NOW_DATE = "CUSTOM_NOW_DATE";

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions()
				.and().addHeaderWriter(new StaticHeadersWriter(HEADER_CUSTOM_NOW_DATE, LocalDate.now().toString()));

	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
