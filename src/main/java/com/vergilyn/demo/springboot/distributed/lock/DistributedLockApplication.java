package com.vergilyn.demo.springboot.distributed.lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.vergilyn.demo.springboot.distributed.lock.service.LockService;

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
    @Autowired
    LockService lockService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DistributedLockApplication.class);
        application.setAdditionalProfiles("redis");
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(redisTemplate.getConnectionFactory().getConnection().time());
        ThreadPoolExecutor taskExecutor =  new ThreadPoolExecutor(
                8, //运行线程大小
                10, //线程池的最大运行线程数
                1000, //空闲线程清楚时间
                TimeUnit.SECONDS, //空闲线程清楚时间的单位
                new ArrayBlockingQueue<Runnable>(1000), //运行线程满时，等待队列的大小
                new ThreadPoolExecutor.CallerRunsPolicy());//池和队列满的策略

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    lockService.lockMethod("arg1", 10000001L);
                }
            });
        }
        System.out.println(taskExecutor.getTaskCount());
    }
}
