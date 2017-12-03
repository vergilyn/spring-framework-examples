package com.vergilyn.demo.springboot.distributed.lock.redis;

import java.util.concurrent.TimeUnit;

import com.vergilyn.demo.springboot.distributed.lock.redis.exception.RedisLockException;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
public class OptRedisLock {
    private String key;
    private boolean lock = false;

    private final StringRedisTemplate redisClient;
    private final RedisConnection redisConnection;

    /**
     * @param purpose 锁前缀
     * @param key     锁定的ID等东西
     */
    public OptRedisLock(String purpose, String key, StringRedisTemplate redisClient) {
        if (redisClient == null) {
            throw new IllegalArgumentException("redisClient 不能为null!");
        }
        this.key = purpose + "_" + key + "_redis_lock";
        this.redisClient = redisClient;
        this.redisConnection = redisClient.getConnectionFactory().getConnection();
    }

    public boolean lockAc(long timeout, long expire, final TimeUnit unit) {
        long beginTime = System.nanoTime();
        timeout = unit.toNanos(timeout);
        try {
            while (System.nanoTime() - beginTime < timeout) {
                if (this.redisConnection.setNX(this.key.getBytes(), "1".getBytes())) {
                    this.redisConnection.expire(key.getBytes(), unit.toSeconds(expire));
                    this.lock = true;
                    return true;
                }

                System.out.println("lockAc get lock waiting...");
                Thread.sleep(30);
            }
        } catch (Exception e) {
            throw new RedisLockException("locking error", e);
        } finally { // 此处关闭连接比较保险。
            if(!this.redisConnection.isClosed()){
                this.redisConnection.close();
            }
        }
        return false;
    }


    /**
     * @return current redis-server time in milliseconds.
     */
    private long getRedisTime() {
        return this.redisConnection.time();
    }

    /**
     * 释放锁: 用新的连接执行delete, 不然redisConnection可能不好关闭.
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