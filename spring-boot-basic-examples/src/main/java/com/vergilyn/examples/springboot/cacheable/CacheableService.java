package com.vergilyn.examples.springboot.cacheable;

import java.util.Map;

import com.google.common.collect.Maps;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class CacheableService {

	/**
	 * 模拟从数据库获取的数据
	 */
	@Cacheable(value = "redisCacheBean", key = "'.id.'+#id")
	public Map<String, Object> get(String id) {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", id);
		map.put("timestamp", System.currentTimeMillis());
		return map;
	}
}
