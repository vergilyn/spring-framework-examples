package com.vergilyn.demo.springboot.http;

import com.vergilyn.demo.springboot.http.task.CreateDownloadTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
@SpringBootApplication
@EnableCaching    //允许缓存 （此demo允许redis缓存）
public class HttpDownloadApplication implements CommandLineRunner{
    @Autowired
    CreateDownloadTask task;

    @Qualifier("stringRedisTemplate")
    @Autowired
    StringRedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(HttpDownloadApplication.class);
        application.setAdditionalProfiles("redis");
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("run..............");
        String music = "https://d1.music.126.net/dmusic/cloudmusicsetup_2_2_2_195462.exe";

        String taskKey = this.task.createTask(music);
        if(redisTemplate.hasKey(taskKey)){
            System.out.println("begin download..............");
            String path = task.execDownloadTask(taskKey);
            System.out.println("end download..............");

            System.out.println("begin merge..............");
            task.mergeBlock(path);
            System.out.println("end merge..............");

        }
    }
}
