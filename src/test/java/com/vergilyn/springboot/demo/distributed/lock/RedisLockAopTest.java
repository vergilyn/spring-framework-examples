package com.vergilyn.springboot.demo.distributed.lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.vergilyn.demo.springboot.distributed.lock.DistributedLockApplication;
import com.vergilyn.demo.springboot.distributed.lock.service.LockService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/12/1
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=DistributedLockApplication.class)
public class RedisLockAopTest {

    @Autowired
    LockService lockService;

    @Test
    public void exex(){
        lockService.lockMethod("arg1", 10000001L);
    }

    @Test
    public void mutil(){
        ThreadPoolExecutor taskExecutor =  new ThreadPoolExecutor(
                3, //运行线程大小
                6, //线程池的最大运行线程数
                1000, //空闲线程清楚时间
                TimeUnit.SECONDS, //空闲线程清楚时间的单位
                new ArrayBlockingQueue<Runnable>(1000), //运行线程满时，等待队列的大小
                new ThreadPoolExecutor.CallerRunsPolicy());//池和队列满的策略

        for (int i = 0; i < 2; i++) {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());

                    lockService.lockMethod("arg1", 10000001L);
                }
            });
        }
    }


}
