package com.vergilyn.demo.springboot.distributed.lock.redis.exception;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
public class RedisLockException extends RuntimeException{
    public RedisLockException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public RedisLockException(String msg) {
        super(msg);
    }
}