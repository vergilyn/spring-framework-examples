package com.vergilyn.demo.servlet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ServletController {

	@GetMapping("/filter")
	public String index() {
		return "servlet/filter";
	}

	@PostMapping("/appendFilter")
	@ResponseBody
	public Map<String, Object> setString(String name,int age) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("age", age);
		return map;
	}
}
