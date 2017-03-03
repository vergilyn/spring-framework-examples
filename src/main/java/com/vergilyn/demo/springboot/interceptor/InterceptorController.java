package com.vergilyn.demo.springboot.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InterceptorController {

	@GetMapping("/interceptor")
	public String index() {
		return "interceptor/interceptor";
	}

	@GetMapping({"/needInterceptor","/notInterceptor"})
	@ResponseBody
	public Map<String, Object> setString(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		return map;
	}
}
