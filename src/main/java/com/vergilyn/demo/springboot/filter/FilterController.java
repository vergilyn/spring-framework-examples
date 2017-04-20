package com.vergilyn.demo.springboot.filter;

import java.util.HashMap;
import java.util.Map;

import com.vergilyn.demo.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@GetMapping("/xssFilter")
	public String xssFilter(Model model) {
	    model.addAttribute("html_value","<p>alert(123)</p>");
		return "filter/xss_filter";
	}

	@PostMapping({"/xssRequired","/xssRequest"})
	@ResponseBody
	public Map<String, Object> setString(String transform) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("transform", transform);
		map.putAll(Constant.map);
		return map;
	}
}
