package com.vergilyn.examples.springboot.db_multi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/26
 */
@SpringBootApplication
public class DBMultiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DBMultiApplication.class);
        app.run(args);
    }

}
