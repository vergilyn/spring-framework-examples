package com.vergilyn.demo.springboot.db_multi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/26
 */
@SpringBootApplication
public class DBmultiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DBmultiApplication.class);
        app.run(args);
    }

}
