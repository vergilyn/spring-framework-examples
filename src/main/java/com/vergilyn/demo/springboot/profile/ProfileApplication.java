package com.vergilyn.demo.springboot.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;



@SpringBootApplication
@Controller
public class ProfileApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProfileApplication.class, args);
	}

}
