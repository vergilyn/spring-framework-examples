package com.vergilyn.demo.springboot.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
// 扫描注入被@WebServlet、@WebFilter、@WebListener注解的类。（也可以代码注入）
@ServletComponentScan 
public class FilterApplication {
	private final static String PROFILE_MVC = "mvc";
	private final static String PROFILE_SERVLET = "servlet";
	/*
	 * 代码注入，servlet、filter、listener可能不一样。
	 * 
	 * @Bean 
	 * public ServletRegistrationBean servlet(){ 
	 * 	return new ServletRegistrationBean(servlet, urlMappings); 
	 * }
	 */

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(FilterApplication.class);
		// mvc或servlet，servlet需要@ServletComponentScan。
		app.setAdditionalProfiles(PROFILE_MVC);
		app.run(args);
	}
}