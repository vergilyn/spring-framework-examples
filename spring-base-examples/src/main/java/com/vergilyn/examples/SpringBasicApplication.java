package com.vergilyn.examples;

import com.vergilyn.examples.annotation.Log;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
public class SpringBasicApplication{
    @Log
    private Logger logger;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBasicApplication.class);

        application.run(args);

    }
}
