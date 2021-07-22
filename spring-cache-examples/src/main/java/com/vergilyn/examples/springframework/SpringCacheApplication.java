package com.vergilyn.examples.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author vergilyn
 * @since 2021-06-29
 */
// 扫描一个不存在的package，目的：junit时按需registry-bean。
@SpringBootApplication(scanBasePackages = "com.vergilyn.examples.unknown")
public class SpringCacheApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SpringCacheApplication.class);

		ConfigurableApplicationContext context = application.run(args);
	}
}
