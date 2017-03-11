package com.vergilyn.demo.springboot.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

import com.vergilyn.demo.springboot.filter.FilterApplication;



@SpringBootApplication
public class ProfileApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ProfileApplication.class);
		app.setAdditionalProfiles("dev");	//代码指定
		app.run(args);
	}

}
