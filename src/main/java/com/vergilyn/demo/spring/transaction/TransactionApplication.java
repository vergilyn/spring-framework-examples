package com.vergilyn.demo.spring.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/p/6956917.html
 * @date 2017/5/21
 */
@SpringBootApplication
public class TransactionApplication {
    public final static String ID = "1";

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TransactionApplication.class);
        app.run(args);
    }
}
