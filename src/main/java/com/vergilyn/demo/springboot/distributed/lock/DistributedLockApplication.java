package com.vergilyn.demo.springboot.distributed.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
@SpringBootApplication
@EnableCaching
public class DistributedLockApplication implements CommandLineRunner{
    @Autowired
    StringRedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DistributedLockApplication.class);
        application.setAdditionalProfiles("redis");
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(redisTemplate == null);
    }
}
