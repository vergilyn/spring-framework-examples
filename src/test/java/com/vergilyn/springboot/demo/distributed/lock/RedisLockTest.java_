package com.vergilyn.springboot.demo.distributed.lock;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

import com.vergilyn.demo.springboot.distributed.lock.DistributedLockApplication;
import com.vergilyn.demo.springboot.distributed.lock.annotation.interceptor.CacheLockInterceptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=DistributedLockApplication.class)
public class RedisLockTest {
    private static Long commidityId1 = 10000001L;
    private static Long commidityId2 = 10000002L;

    @Test
    public void testSecKill(){
        int threadCount = 1000;
        int splitPoint = 500;
        CountDownLatch endCount = new CountDownLatch(threadCount);
        CountDownLatch beginCount = new CountDownLatch(1);
        LockServiceImpl testClass = new LockServiceImpl();

        Thread[] threads = new Thread[threadCount];
        //起500个线程，秒杀第一个商品
        for(int i= 0;i < splitPoint;i++){
            threads[i] = new Thread(new  Runnable() {
                public void run() {
                    try {
                        //等待在一个信号量上，挂起
                        beginCount.await();
                        //用动态代理的方式调用secKill方法
                        LockService proxy = (LockService) Proxy.newProxyInstance(LockService.class.getClassLoader(),
                                new Class[]{LockService.class}, new CacheLockInterceptor(testClass));
                        proxy.lockMethod("test", commidityId1);
                        endCount.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();

        }

        for(int i= splitPoint;i < threadCount;i++){
            threads[i] = new Thread(new  Runnable() {
                public void run() {
                    try {
                        //等待在一个信号量上，挂起
                        beginCount.await();
                        //用动态代理的方式调用secKill方法
                        beginCount.await();
                        LockService proxy = (LockService) Proxy.newProxyInstance(LockService.class.getClassLoader(),
                                new Class[]{LockService.class}, new CacheLockInterceptor(testClass));
                        proxy.lockMethod("test", commidityId2);
                        endCount.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();

        }


        long startTime = System.currentTimeMillis();
        //主线程释放开始信号量，并等待结束信号量
        beginCount.countDown();

        try {
            //主线程等待结束信号量
            endCount.await();
            //观察秒杀结果是否正确
            System.out.println(LockServiceImpl.inventory.get(commidityId1));
            System.out.println(LockServiceImpl.inventory.get(commidityId2));
            System.out.println("error count" + CacheLockInterceptor.ERROR_COUNT);
            System.out.println("total cost " + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
