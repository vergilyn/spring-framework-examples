package com.vergilyn.demo.springboot.distributed.lock;

import com.vergilyn.demo.springboot.http.HttpDownloadApplication;

import org.springframework.boot.SpringApplication;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
public class DistributedLockApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HttpDownloadApplication.class);
        application.setAdditionalProfiles("redis");
        application.run(args);
    }
}
