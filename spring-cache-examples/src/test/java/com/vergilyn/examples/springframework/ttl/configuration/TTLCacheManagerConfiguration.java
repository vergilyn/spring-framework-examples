package com.vergilyn.examples.springframework.ttl.configuration;

import java.time.Duration;
import java.util.Map;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;


/**
 * VFIXME 2021-06-30 以下代码其实就是 {@linkplain org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration}
 *
 * @author vergilyn
 * @since 2021-06-30
 *
 * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 */
@Configuration
@Slf4j
@SuppressWarnings("JavadocReference")
public class TTLCacheManagerConfiguration {

	private static final String KEY_SEPARATOR = ":";
	private static final String GLOBAL_KEY_PREFIX = "ttl" + KEY_SEPARATOR;
	private final static Map<String, CacheProperties.Redis> DEFAULT_CACHE_CONFIG_MAP;

	static {
		DEFAULT_CACHE_CONFIG_MAP = Maps.newHashMap();

		DEFAULT_CACHE_CONFIG_MAP.put("redis10s", create(Duration.ofSeconds(10)));
		DEFAULT_CACHE_CONFIG_MAP.put("redis30s", create(Duration.ofSeconds(30)));
		DEFAULT_CACHE_CONFIG_MAP.put("redis1d", create(Duration.ofDays(1)));
	}

	@Bean
	// @ConditionalOnBean(RedisConnectionFactory.class)
	public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
		log.info("#cache# enabled");

		RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder(redisConnectionFactory);

		Map<String, RedisCacheConfiguration> maps = Maps.newHashMap();

		DEFAULT_CACHE_CONFIG_MAP.forEach((key, value) -> {
			if (StringUtils.isEmpty(value.getKeyPrefix())) {
				// 前缀格式
				value.setKeyPrefix(GLOBAL_KEY_PREFIX + key + KEY_SEPARATOR);
			}

			RedisCacheConfiguration config = buildRedisCacheConfiguration(value);
			maps.put(key, config);
			log.info("#cache#redis key: {}, userPrefix: {}, keyPrefix: {}, ttl: {}", key, config.usePrefix(), value.getKeyPrefix(), config.getTtl());
		});

		builder.withInitialCacheConfigurations(maps);

		// builder.disableCreateOnMissingCache();

		RedisCacheManager redisCacheManager = builder.build();
		redisCacheManager.initializeCaches();

		return new CompositeCacheManager(redisCacheManager);
	}

	private static CacheProperties.Redis create(Duration duration) {
		CacheProperties.Redis result = new CacheProperties.Redis();
		result.setTimeToLive(duration);
		// result.setCacheNullValues();
		// result.setKeyPrefix();
		// result.setUseKeyPrefix();
		return result;
	}

	private RedisCacheConfiguration buildRedisCacheConfiguration(CacheProperties.Redis redisProperties) {

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

		config = config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getFastJsonRedisSerializer()));

		// SEE: org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#createConfiguration(...)
		if (redisProperties.getTimeToLive() != null) {
			config = config.entryTtl(redisProperties.getTimeToLive());
		}

		if (redisProperties.getKeyPrefix() != null) {
			config = config.prefixKeysWith(redisProperties.getKeyPrefix());
		}

		if (!redisProperties.isCacheNullValues()) {
			config = config.disableCachingNullValues();
		}

		if (!redisProperties.isUseKeyPrefix()) {
			config = config.disableKeyPrefix();
		}

		return config;
	}

	private FastJsonRedisSerializer getFastJsonRedisSerializer() {
		FastJsonRedisSerializer serializer = new FastJsonRedisSerializer(Object.class);

		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteClassName);
		fastJsonConfig.setFeatures(Feature.SupportAutoType);

		serializer.setFastJsonConfig(fastJsonConfig);

		return serializer;
	}
}
