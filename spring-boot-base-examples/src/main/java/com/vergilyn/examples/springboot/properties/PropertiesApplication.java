package com.vergilyn.examples.springboot.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
@EnableConfigurationProperties({BaseProperties.class, ComplexProperties.class})
public class PropertiesApplication implements CommandLineRunner {
    @Autowired
    private BaseProperties baseProperties;
    @Autowired
    private ComplexProperties complexProperties;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PropertiesApplication.class);
        application.setAdditionalProfiles("properties");
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.printf("BaseProperties >>>> %s \r\n", baseProperties).println();
        System.out.printf("ComplexProperties >>>> %s \r\n", complexProperties).println();
    }
}
