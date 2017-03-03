package com.vergilyn.demo.springboot.filter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FilterController {

	@GetMapping("/filter")
	public String index() {
		return "filter/filter";
	}

	@PostMapping({"/needFilter","/notFilter"})
	@ResponseBody
	public Map<String, Object> setString(String name,int age) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("age", age);
		return map;
	}
}
