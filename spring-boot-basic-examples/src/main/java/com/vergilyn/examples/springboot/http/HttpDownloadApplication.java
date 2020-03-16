package com.vergilyn.examples.springboot.http;

import com.vergilyn.examples.springboot.http.task.CreateDownloadTask;
import com.vergilyn.examples.springboot.http.task.DownloadScheduler;
import com.vergilyn.examples.springboot.http.task.MergeScheduler;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    DownloadScheduler downloadScheduler;
    @Autowired
    MergeScheduler mergeScheduler;

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
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        // 32.6mb = 33,441kb
        String music = "https://d1.music.126.net/dmusic/cloudmusicsetup_2_2_2_195462.exe";


        this.task.createTask(music);
        this.downloadScheduler.execDownloadTask();

        // FIXME 因为是多线程下载, 这里测试自己调整时间
        Thread.sleep(20 * 1000);

        // mergeScheduler.mergeBlock();
    }
}
