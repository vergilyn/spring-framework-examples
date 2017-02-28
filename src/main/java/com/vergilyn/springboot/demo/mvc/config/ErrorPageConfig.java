package com.vergilyn.springboot.demo.mvc.config;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/* Spring Boot默认提供一个/error映射用来以合适的方式处理所有的错误，并且它在servlet容器中注册了一个全局的 错误页面.
 * 1、为了完全替换默认的行为，你可以实现ErrorController，并注册一个该类型的bean定义，
 * 		或简单地添加一个ErrorAttributes类型的bean以使用现存的机制，只是替换显示的内容。
 */
@Configuration
public class ErrorPageConfig {
	@Bean
	public EmbeddedServletContainerCustomizer errorPage() {
		return new CustomErrorPage();
	}

	/*
	 * 手动注册
	 */
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setPort(9000);
		factory.setSessionTimeout(10, TimeUnit.MINUTES);
		factory.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST,"/templates/error/400.html"));
		factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/templates/error/404.html"));
		return factory;
	}

	/* 重写不同错误码的显示页面。 */
	private static class CustomErrorPage implements
			EmbeddedServletContainerCustomizer {
		@Override
		public void customize(ConfigurableEmbeddedServletContainer container) {
			container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST,"/templates/error/400.html"));
			container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/templates/error/404.html"));
		}
	}
}
