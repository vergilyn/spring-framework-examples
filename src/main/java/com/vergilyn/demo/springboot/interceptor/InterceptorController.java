package com.vergilyn.demo.springboot.interceptor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class InterceptorController {
	/* 返回视图用此写法的好处是：在interceptor中postHandle可以通过ModelAndView获取到参数。
	 */
	@GetMapping("/interceptor")
	public ModelAndView index() {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("name", "vergilyn");
		param.put("blog", "cnblogs.com/vergilyn");
		
		return new ModelAndView("interceptor/interceptor", param);
	}
	
/*	@GetMapping("/interceptor")
	public String index() {
		return "interceptor/interceptor";
	}
*/

	@GetMapping({"/needInterceptor","/notInterceptor"})
	@ResponseBody
	public Map<String, Object> setString(String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		return map;
	}
}
