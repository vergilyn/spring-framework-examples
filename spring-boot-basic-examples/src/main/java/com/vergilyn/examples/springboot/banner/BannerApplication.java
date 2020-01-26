package com.vergilyn.examples.springboot.banner;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
public class BannerApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(BannerApplication.class);
		application.setAdditionalProfiles("banner");

		application.setBannerMode(Banner.Mode.CONSOLE);
		application.run(args);
	}
}
