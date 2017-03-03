package com.vergilyn.demo.springboot.profile;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@Profile注解可以实现不同环境下配置参数的切换，任何@Component或@Configuration注解的类都可以使用@Profile注解。
@Profile("dev")	
public class DevController {
	@Value("${app.name}")
	private String appName;
	@Value("${app.description}")
	private String appDesc;
	
	@RequestMapping("/profile")
	public String greeting(
			@RequestParam(value = "name", required = false, defaultValue = "VergiLyn") String name,
			Model model) {
		model.addAttribute("appDesc", appDesc);
		model.addAttribute("name", appName);
		return "greeting";
	}
}
