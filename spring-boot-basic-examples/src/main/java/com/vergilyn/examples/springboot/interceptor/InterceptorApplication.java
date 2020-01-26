package com.vergilyn.examples.springboot.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <pre>
 * FIXME 2020-01-26
 * 	1. spring-boot-2.x的拦截器会影响静态资源的访问
 * 	2. {@linkplain CustomInterceptor#postHandle(HttpServletRequest, HttpServletResponse, Object, ModelAndView)}
 * 	  拦截后`forward`有问题！！！
 * </pre>
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
@Controller
@RequestMapping("/interceptor")
public class InterceptorApplication {

	public static final Map<String, Object> map = Maps.newHashMap();

	static {
		map.put("name", "vergilyn");
		map.put("email", "vergilyn@vip.qq.com");
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer(){
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {

				registry.addInterceptor(new CustomInterceptor())
						.addPathPatterns("/interceptor/**");
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/static/**")
						.addResourceLocations("classpath:/static/");;
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(InterceptorApplication.class);
		application.setAdditionalProfiles("thymeleaf", "mvc");
		application.run(args);
	}

	/*
	 * 返回视图用此写法的好处是：在interceptor中`postHandle`可以通过ModelAndView获取到参数。
	 */
	@GetMapping("/normal")
	public ModelAndView index(HttpServletRequest request) {
		Map<String, Object> result = fillMap(request);

		return new ModelAndView("interceptors/interceptor", result);
	}

	@GetMapping({"/need","/*"})
	@ResponseBody
	public Map<String, Object> test(HttpServletRequest request) {
		return fillMap(request);
	}

	private Map<String, Object> fillMap(HttpServletRequest request){
		map.put("request-uri", request.getRequestURI());
		map.put("query-string", request.getQueryString());

		return map;
	}
}
