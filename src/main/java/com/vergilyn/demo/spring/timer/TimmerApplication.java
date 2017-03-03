package com.vergilyn.demo.spring.timer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //通过@EnableScheduling注解开启对计划任务的支持
public class TimmerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimmerApplication.class, args);
	}
}
