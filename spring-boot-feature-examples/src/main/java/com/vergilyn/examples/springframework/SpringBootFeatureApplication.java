package com.vergilyn.examples.springframework;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author vergilyn
 * @since 2021-06-18
 */
@SpringBootApplication
public class SpringBootFeatureApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SpringBootFeatureApplication.class);

		ConfigurableApplicationContext context = application.run(args);

		System.out.println(context.getEnvironment().getProperty("vergilyn.application.name"));
		System.out.println(context.getEnvironment().getProperty("vergilyn.application.name"));
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
