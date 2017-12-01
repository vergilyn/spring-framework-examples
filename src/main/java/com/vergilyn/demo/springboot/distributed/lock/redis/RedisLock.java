package com.vergilyn.demo.springboot.distributed.lock.redis;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
public class RedisLock {
    private static final Random RANDOM = new Random();
    private String key;
    private boolean lock = false;

    private final StringRedisTemplate redisClient;
    private final RedisConnection redisConnection;

    /**
     * @param purpose 锁前缀
     * @param key     锁定的ID等东西
     */
    public RedisLock(String purpose, String key, StringRedisTemplate redisClient) {
        if (redisClient == null) {
            throw new IllegalArgumentException("redisClient 不能为null!");
        }
        this.key = purpose + "_" + key + "_lock";
        this.redisClient = redisClient;
        this.redisConnection = redisClient.getConnectionFactory().getConnection();
    }

    /**
     * 锁的策略参考: <a href="http://blog.csdn.net/u010359884/article/details/50310387">基于redis分布式锁实现“秒杀”</a>
     * FIXME 此方式加锁策略存在一定缺陷: 在setIfAbsent()之后expire()执行之前程序异常 锁不会被释放. 虽然出现几率极低
     *
     * @param timeout timeout的时间范围内轮询锁, 单位: 秒
     * @param expire  设置锁超时时间
     * @return true, 获取锁成功; false, 获取锁失败.
     */
    public boolean lock(long timeout, long expire, final TimeUnit unit) {
        // 相比isLock(), 此策略中的time只是用于得到超时, 所以不需要用getRedisTime();
        long beginTime = System.nanoTime();  // 用nanos、mills具体看需求.
        timeout = TimeUnit.SECONDS.toNanos(timeout);
        try {
            // 在timeout的时间范围内不断轮询锁
            while (System.nanoTime() - beginTime < timeout) {
                // 锁不存在的话，设置锁并设置锁过期时间，即加锁
                if (this.redisClient.opsForValue().setIfAbsent(this.key, "1")) {
                    this.redisClient.expire(key, expire, unit);//设置锁失效时间, 防止永久阻塞
                    this.lock = true;
                    return true;
                }

                // 短暂休眠后轮询，避免可能的活锁
                System.out.println("get lock waiting...");
                Thread.sleep(30, RANDOM.nextInt(30));
            }
        } catch (Exception e) {
            throw new RuntimeException("locking error", e);
        }
        return false;
    }

    /**
     * 特别注意: 如果多服务器之间存在时间差, 并不建议用System.nanoTime()、System.currentTimeMillis().
     * 更好的是统一用redis-server的时间, 但只能获取到milliseconds.
     * 锁的策略参考: <a href="http://www.jeffkit.info/2011/07/1000/?spm=5176.100239.blogcont60663.7.9f4d4a8h4IOxe">用Redis实现分布式锁</a>
     * FIXME: 有bug, 多线程有的执行不完
     *
     * @param timeout 锁失效时常
     * @return true, 获取锁成功; false, 获取锁失败.
     */
    public boolean isLock(long timeout) {
        // 更好的是用redis.TIME, 防止多服务器之间时间误差, 但只能返回milliseconds
        long lockVal;
        String expireVal;
        try {
            while (!this.lock) {  // FIXME: 会一直等到获取锁, 感觉可以引入获取锁超时机制, 如lock()的逻辑
                System.out.println(this + ": 1 " + this.redisClient);
                // 锁的键值: {当前时间} + {失效时常} = {锁失效时间}
                lockVal = getRedisTime() + TimeUnit.SECONDS.toMillis(timeout) + 1;
                System.out.println(this + ": 2 : " + lockVal);

                // 1. 尝试获取锁
                boolean ifAbsent = this.redisClient.opsForValue().setIfAbsent(this.key, lockVal + "");
                System.out.println(this + ": 3 : " + ifAbsent + " : " + lockVal);

                if (ifAbsent) { // 设置成功, 表示获得锁
                    // 这种策略下, 是否设置key失效不太重要. 因为, 正常流程中最后会释放锁(del-key); 如果是异常情况下未释放锁, 后面的代码也会判断锁是否失效.
                    // 设置的好处: 能减少redis的内存消耗, 及时清理无效的key(暂时只想到这)
                    // this.redisClient.expire(key, timeout, TimeUnit.SECONDS);
                    System.out.println(this + ": ifAbsent : " + lockVal);

                    this.lock = true;
                    return true;
                }

                expireVal = this.redisClient.opsForValue().get(this.key);
                long curTime = getRedisTime();
                // curTime > expireVal: 表示此锁已无效
                /* 在锁无效的前提下, 尝试获取锁: (一定要用)getAndSet()
                 *
                 * 假设锁已失效, 且未正常expire. 此时C1、C2同时执行到此, C2先执行getAndSet(key, time-02), C2获取到锁
                 * 此时C1.getAndSet(key, time-01)返回的是time-02, 显然curTime > time-02: false.
                 * 所以, C1并未获取到锁. 但C1修改了key的值为: time-01.
                 * 但因为C1、C2是同时执行到此, 所以time-01、time-02的值近视相等.
                 * (若多服务器存在时间差, 那这个差值有问题, 所以服务器时间如果不同步则不能用System.nanoTime()、System.currentTimeMillis(), 该用redis-server time.)
                 */
                System.out.println(this + ": expireVal : " + expireVal);
                if (curTime > NumberUtils.toLong(expireVal, 0)) {
                    // getSet必须在{curTime > expireVal} 判断之后; 否则, 可能出现死循环
                    String ss = this.redisClient.opsForValue().getAndSet(this.key, lockVal + "");
                    if (curTime > NumberUtils.toLong(ss, 0)) {
                        // this.redisClient.expire(key, timeout, TimeUnit.SECONDS); // 是否设置失效不重要, 理由同上.
                        System.out.println(this + ": getAndSet");
                        this.lock = true;
                        return true;
                    }
                }

                // 锁被占用, 短暂休眠等待轮询
                System.out.println(this + ": get lock waiting...");
                Thread.sleep(400, RANDOM.nextInt(30));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("locking error", e);
        }
        System.out.println(this + ": get lock error.");
        return false;
    }

    /**
     * @return current redis-server time in milliseconds.
     */
    private long getRedisTime() {
//        return this.redisConnection.time();
        return this.redisClient.getConnectionFactory().getConnection().time();
//      return System.nanoTime();
//      return  System.currentTimeMillis();
    }

    /**
     * 释放锁
     */
    public void unlock() {
        if (this.lock) {
            redisClient.delete(key);
        }
    }

    public boolean isLock() {
        return lock;
    }

}