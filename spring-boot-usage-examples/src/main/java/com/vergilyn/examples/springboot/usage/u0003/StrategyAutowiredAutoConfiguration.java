package com.vergilyn.examples.springboot.usage.u0003;

import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SuppressWarnings("JavadocReference")
public class StrategyAutowiredAutoConfiguration {

	/**
	 * @see RedisAutoConfiguration#redisTemplate(org.springframework.data.redis.connection.RedisConnectionFactory)
	 */
	@Bean
	public ChainInvokerSpringStrategyTemplate<Object, ?> chainAutowiredInvokerSpringStrategy(){
		return new ChainInvokerSpringStrategyTemplate<>();
	}
}
