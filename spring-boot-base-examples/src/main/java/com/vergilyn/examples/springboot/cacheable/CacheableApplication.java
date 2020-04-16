package com.vergilyn.examples.springboot.cacheable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * TODO 2020-01-26 缓存有效时间怎么设置？
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
@EnableCaching    //允许缓存
public class CacheableApplication implements CommandLineRunner {

    @Autowired
    private CacheableService cacheableService;

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(CacheableApplication.class);
        application.setAdditionalProfiles("cacheable");
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {

        String id1 = "1", id2 = "2";

        System.out.printf("id: %s, result:%s \r\n", id1, cacheableService.get(id1));
        Thread.sleep(1000);

        System.out.printf("id: %s, result:%s \r\n", id1, cacheableService.get(id1));
        Thread.sleep(1000);

        System.out.printf("id: %s, result:%s \r\n", id1, cacheableService.get(id1));
        Thread.sleep(1000);

        System.out.printf("id: %s, result:%s \r\n", id2, cacheableService.get(id2));

    }
}
