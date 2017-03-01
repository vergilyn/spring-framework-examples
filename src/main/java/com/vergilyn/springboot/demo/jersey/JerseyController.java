package com.vergilyn.springboot.demo.jersey;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/*
 * 所有注册的端点都应该被@Components和HTTP资源annotations（比如@GET）注解。
 * 1、因为是@Component，所以其生命周期受Spring管理。
 * 		并且你可以使用@Autowired添加依赖及使用@Value注入外部配置。
 */
//@Component
@RestController
@Path("/jersey")
public class JerseyController {
	@GET
	@Path("/msg")
	public String message() {
		return "jersey message2!";
	}
}