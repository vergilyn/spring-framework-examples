package com.vergilyn.demo.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//扫描注入被@WebServlet、@WebFilter、@WebListener注解的类。（也可以代码注入）
@ServletComponentScan
public class ServletApplication {
  /*  代码注入，servlet、filter、listener可能不一样。
    @Bean
	public ServletRegistrationBean servlet(){
		return new ServletRegistrationBean(servlet, urlMappings);
	}
   */
	
	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}
}
