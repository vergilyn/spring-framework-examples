package com.vergilyn.springboot.demo.mvc.config;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/* 想要开始使用Jersey 2.x只需要加入spring-boot-starter-jersey依赖，
 * 然后你需要一个ResourceConfig类型的@Bean，用于注册所有的端点（endpoints,demo为JerseyController）。
 * 
 */
@Component
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig() {
		register(JerseyController.class);
	}
	
	/*
	 * 所有注册的端点都应该被@Components和HTTP资源annotations（比如@GET）注解。
	 * 1、因为是@Component，所以其生命周期受Spring管理。
	 * 		并且你可以使用@Autowired添加依赖及使用@Value注入外部配置。
	 * 2、
	 */
	@Component
	@Path("/jersey")
	public class JerseyController {
		@GET
		@Path("/msg")
		public String message() {
			return "jersey message!";
		}
	}
}
