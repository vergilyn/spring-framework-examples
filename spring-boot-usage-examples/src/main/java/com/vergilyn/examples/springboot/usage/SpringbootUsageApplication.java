package com.vergilyn.examples.springboot.usage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author vergilyn
 * @since 2022-05-07
 */
@SpringBootApplication(exclude = RedisAutoConfiguration.class)
public class SpringbootUsageApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SpringbootUsageApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}
}
