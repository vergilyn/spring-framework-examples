package com.vergilyn.examples.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author vergilyn
 * @since 2021-06-18
 */
@SpringBootApplication
public class SpringBootFeatureApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SpringBootFeatureApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}
}
