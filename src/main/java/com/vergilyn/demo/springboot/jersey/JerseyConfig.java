package com.vergilyn.demo.springboot.jersey;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/* 想要开始使用Jersey 2.x只需要加入spring-boot-starter-jersey依赖，
 * 然后你需要一个ResourceConfig类型的@Bean，用于注册所有的端点（endpoints,demo为JerseyController）。
 * 
 */
//@Component
@Configuration
//Jersey servlet将被注册，并默认映射到/*。可将@ApplicationPath添加到ResourceConfig来改变该映射。
@ApplicationPath("/rest")
public class JerseyConfig extends ResourceConfig {
	
	public JerseyConfig() {
		register(JerseyController.class);
//		packages("com.vergilyn.demo.springboot.jersey"); // 通过packages注册。
	}
}
