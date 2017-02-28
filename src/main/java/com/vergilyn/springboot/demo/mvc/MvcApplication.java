package com.vergilyn.springboot.demo.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

/* Spring Boot为Spring MVC提供适用于多数应用的自动配置功能。在Spring默认基础上，自动配置添加了以下特性：
 * 1. 引入ContentNegotiatingViewResolver和BeanNameViewResolver beans。
 * 2. 对静态资源的支持，包括对WebJars的支持。
 * 3. 自动注册Converter，GenericConverter，Formatter beans。
 * 4. 对HttpMessageConverters的支持。
 * 5. 自动注册MessageCodeResolver。
 * 6. 对静态index.html的支持。
 * 7. 对自定义Favicon的支持。
 */
@SpringBootApplication
@Controller
public class MvcApplication {
	public static void main(String[] args) {
		SpringApplication.run(MvcApplication.class, args);
	}
}
