package com.vergilyn.examples;

import com.vergilyn.examples.annotation.Log;
import com.vergilyn.examples.scope.ScopeTest;
import com.vergilyn.examples.spel.SpelTest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
public class SpringBasicApplication implements CommandLineRunner {
    @Log
    private Logger logger;
    @Autowired
    private SpelTest spelTest;
    @Autowired
    private ScopeTest scopeTest;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SpringBasicApplication.class);

        application.run(args);

    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("custom Logger annotation >>>> print");

        spelTest.test();

        scopeTest.test();
    }
}
