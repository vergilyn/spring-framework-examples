package com.vergilyn.demo.springboot.jersey;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class JerseyApplication {
	/* 代码注入：
	 *  此种方式需注意：ServletRegistrationBean的配置，及最终的请求路径。
	 * 注解注入：
	 *  JerseyConfig.java中用@Configuration
	 */
//	@Bean
	public ServletRegistrationBean jerseyServlet() {
		/* 特别注意此路径，与JerseyController中的@Path。可能让最终路径变成：localhost:8080/jersey/jersey/get 
		 * 第一个jersey是此ServletRegistrationBean定义的，第二jersey是Controller中类注解@Path定义的
		 */
		ServletRegistrationBean registration = new ServletRegistrationBean(
				new ServletContainer(), "/jersey/*");
		// our rest resources will be available in the path /rest/*
		registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS,
				JerseyConfig.class.getName());
		return registration;
	}

	public static void main(String[] args) {
		SpringApplication.run(JerseyApplication.class, args);
	}
}
