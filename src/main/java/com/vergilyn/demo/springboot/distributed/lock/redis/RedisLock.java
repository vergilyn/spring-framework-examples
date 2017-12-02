package com.vergilyn.demo.springboot.distributed.lock.redis;

import java.util.concurrent.TimeUnit;

import com.vergilyn.demo.springboot.distributed.lock.redis.exception.RedisLockException;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
public class RedisLock {
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
    public boolean lockA(long timeout, long expire, final TimeUnit unit) {
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
                Thread.sleep(30);
            }
        } catch (Exception e) {
            throw new RedisLockException("locking error", e);
        } finally {
            closeConnection();
        }
        return false;
    }

    /**
     * 特别注意: 如果多服务器之间存在时间差, 并不建议用System.nanoTime()、System.currentTimeMillis().
     * 更好的是统一用redis-server的时间, 但只能获取到milliseconds.
     * 锁的策略参考: <a href="http://www.jeffkit.info/2011/07/1000/?spm=5176.100239.blogcont60663.7.9f4d4a8h4IOxe">用Redis实现分布式锁</a>
     *
     * @param timeout 获取锁超时, 单位: 毫秒
     * @param expire 锁失效时常, 单位: 毫秒
     * @return true, 获取锁成功; false, 获取锁失败.
     */
    public boolean lockB(long timeout, long expire) {
        long bt = System.currentTimeMillis();
        long lockVal;
        String lockExpireTime;
        try {
            while (!this.lock) {
                if(System.currentTimeMillis() - bt > timeout){
                    throw new RedisLockException("get lock timeout!");
                }

                // 锁的键值: {当前时间} + {失效时常} = {锁失效时间}
                lockVal = getRedisTime() + expire;

                // 1. 尝试获取锁
                boolean ifAbsent = this.redisClient.opsForValue().setIfAbsent(this.key, lockVal + "");
                if (ifAbsent) { // 设置成功, 表示获得锁
                    // 这种策略下, 是否设置key失效不太重要. 因为, 正常流程中最后会释放锁(del-key); 如果是异常情况下未释放锁, 后面的代码也会判断锁是否失效.
                    // 设置的好处: 能减少redis的内存消耗, 及时清理无效的key(暂时只想到这)
                    // this.redisClient.expire(key, timeout, TimeUnit.SECONDS);
                    this.lock = true;
                    return true;
                }

                lockExpireTime = this.redisClient.opsForValue().get(this.key);
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
                if (curTime > NumberUtils.toLong(lockExpireTime, 0)) {
                    // getset必须在{curTime > expireVal} 判断之后; 否则, 可能出现死循环
                    lockExpireTime = this.redisClient.opsForValue().getAndSet(this.key, lockVal + "");
                    if (curTime > NumberUtils.toLong(lockExpireTime, 0)) {
                        // this.redisClient.expire(key, timeout, TimeUnit.SECONDS); // 是否设置失效不重要, 理由同上.
                        System.out.println(this + ": getAndSet");
                        this.lock = true;
                        return true;
                    }
                }

                // 锁被占用, 短暂休眠等待轮询
                System.out.println(this + ": get lock waiting...");
                Thread.sleep(40);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RedisLockException("locking error", e);
        } finally {
            closeConnection();
        }
        System.out.println(this + ": get lock error.");
        return false;
    }

    /**
     * @return current redis-server time in milliseconds.
     */
    private long getRedisTime() {
//        return this.redisConnection.time();
        return this.redisConnection.time();
//      return System.nanoTime();
//      return  System.currentTimeMillis();
    }

    private void closeConnection(){
        if(!this.redisConnection.isClosed()){
            this.redisConnection.close();
        }
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