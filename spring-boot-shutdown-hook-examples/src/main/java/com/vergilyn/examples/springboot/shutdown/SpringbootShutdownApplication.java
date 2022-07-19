package com.vergilyn.examples.springboot.shutdown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author vergilyn
 * @since 2022-07-12
 */
@SpringBootApplication
public class SpringbootShutdownApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SpringbootShutdownApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}
}
