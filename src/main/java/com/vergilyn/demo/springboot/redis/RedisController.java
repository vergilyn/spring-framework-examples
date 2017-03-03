package com.vergilyn.demo.springboot.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vergilyn.demo.springboot.redis.bean.RedisCacheBean;
import com.vergilyn.demo.springboot.redis.dao.RedisCacheDao;

@Controller
public class RedisController {
	private static final String STR_REDIS_KEY = "vergil";

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private RedisCacheDao cacheDao;

	@GetMapping("/redis")
	public String index() {
		return "redis/redis_index";
	}

	@PostMapping("/setString")
	@ResponseBody
	public Map<String, Object> setString(String value) {
		redisTemplate.opsForValue().set(STR_REDIS_KEY, value);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "ok");
		return map;
	}

	@PostMapping("/getString")
	@ResponseBody
	public Map<String, Object> getString() {
		String value = redisTemplate.opsForValue().get(STR_REDIS_KEY);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", value);
		map.put("msg", "ok");
		return map;
	}
	
	@PostMapping("/getCache")
	@ResponseBody
	public RedisCacheBean get(@RequestParam String id) {
		return cacheDao.get(id);
	}
}
