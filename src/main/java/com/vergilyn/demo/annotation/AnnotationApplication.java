package com.vergilyn.demo.annotation;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/19
 */
@SpringBootApplication
public class AnnotationApplication implements CommandLineRunner{
    @Log
    private Logger log;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AnnotationApplication.class);
        app.setAdditionalProfiles("log");
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("log : " + log);
        log.info("log 注解注入成功. log：" + log);
    }
}
