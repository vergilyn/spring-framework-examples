package com.vergilyn.examples.springboot.filter;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
// 扫描注入被@WebServlet、@WebFilter、@WebListener注解的类。（也可以代码注入）
@ServletComponentScan 
public class FilterApplication {
	private final static String PROFILE_MVC = "mvc";
	private final static String PROFILE_SERVLET = "servlet";
	public static final Map<String,Object> map = Maps.newHashMap();
	static{
		map.put("name", "vergilyn");
		map.put("email", "vergilyn@vip.qq.com");
	}

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
		app.setAdditionalProfiles(PROFILE_MVC);
		app.run(args);
	}

	@GetMapping("/filter")
	public String index() {
		return "filter/filter";
	}

	@PostMapping({"/needFilter","/notFilter"})
	@ResponseBody
	public Map<String, Object> setString(String name, int age) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("age", age);
		return map;
	}

	@GetMapping("/xssFilter")
	public String xssFilter(Model model) {
		model.addAttribute("html_value","<p>alert(123)</p>");
		return "filter/xss_filter";
	}

	@PostMapping({"/xssRequired","/xssRequest"})
	@ResponseBody
	public Map<String, Object> setString(String transform) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("transform", transform);
		map.putAll(map);
		return map;
	}
}