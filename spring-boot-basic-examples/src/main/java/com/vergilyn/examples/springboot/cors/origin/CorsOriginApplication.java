package com.vergilyn.examples.springboot.cors.origin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author VergiLyn
 * @date 2017/4/16
 */
@SpringBootApplication
public class CorsOriginApplication {
    public final static String CORS_ADDRESS = "127.0.0.1";

    public static void main(String[] args) {
        SpringApplication.run(CorsOriginApplication.class,args);
    }
}
