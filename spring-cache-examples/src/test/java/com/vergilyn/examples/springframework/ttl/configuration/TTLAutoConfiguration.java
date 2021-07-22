package com.vergilyn.examples.springframework.ttl.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ConditionalOnProperty(name = TTLAutoConfiguration.ENV_TTL_ENABLE, havingValue = "true", matchIfMissing = true)
@EnableCaching
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Import(TTLCacheManagerConfiguration.class)
@SuppressWarnings("JavadocReference")
public class TTLAutoConfiguration {
	public static final String ENV_TTL_ENABLE = "vergilyn.cache.ttl.enable";

	/**
	 * @see CacheAutoConfiguration#cacheManagerCustomizers(org.springframework.beans.factory.ObjectProvider)
	 */
	@Bean
	public CacheManagerCustomizer<CacheManager> customizers(){
		return null;
	}

	/**
	 * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration#cacheManager(org.springframework.boot.autoconfigure.cache.CacheProperties, org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers, org.springframework.beans.factory.ObjectProvider, org.springframework.beans.factory.ObjectProvider, org.springframework.data.redis.connection.RedisConnectionFactory, org.springframework.core.io.ResourceLoader)
	 */
	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizers(){
		return null;
	}
}

