package com.vergilyn.demo.springboot.redis.dao;

import java.util.Date;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.vergilyn.demo.springboot.redis.bean.RedisCacheBean;

@Repository
public class RedisCacheDao {

	/**
	 * 模拟从数据库获取的数据
	 */
	@Cacheable(value = "redisCacheBean", key = "'.id.'+#id")
	public RedisCacheBean get(String id) {
		RedisCacheBean redisCacheBean = new RedisCacheBean(id, "name_"+id, new Date(), id.length() * 10);
		return redisCacheBean;
	}
}
