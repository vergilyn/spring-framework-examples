package com.vergilyn.demo.springboot.distributed.lock;

import java.util.concurrent.ThreadPoolExecutor;

import com.vergilyn.demo.springboot.distributed.lock.service.LockService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
    @Autowired
    ThreadPoolTaskExecutor executor;

    @Bean
    public ThreadPoolTaskExecutor myExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(8);
        // 最大线程数
        executor.setMaxPoolSize(12);
        // 运行线程满时，等待队列的大小
        executor.setQueueCapacity(1000);
        executor.setThreadNamePrefix("vl-thread-");
        // 池和队列满的策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 空闲线程清除时间
        executor.setKeepAliveSeconds(60);
        // 是否允许释放核心线程
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DistributedLockApplication.class);
        application.setAdditionalProfiles("redis");
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("run....");
        for (int i = 0; i < 9; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                   lockService.lockMethod("arg1", 1L);
//                     System.out.println(redisTemplate.getConnectionFactory().getConnection());
                }
            });
        }
        System.out.println(executor.getThreadPoolExecutor().getTaskCount());
    }
}
