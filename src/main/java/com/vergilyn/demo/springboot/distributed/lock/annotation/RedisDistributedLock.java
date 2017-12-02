package com.vergilyn.demo.springboot.distributed.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisDistributedLock {

    /**
     * 锁key的前缀
     */
    String lockedPrefix() default "";

    /**
     * 轮询锁的时间超时时常, 单位: ms
     */
    long timeout() default 2000;

    /**
     * redis-key失效时常, 单位: ms
     */
    int expireTime() default 1000;

}
