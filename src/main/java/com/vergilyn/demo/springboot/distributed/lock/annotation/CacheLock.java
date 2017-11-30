package com.vergilyn.demo.springboot.distributed.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheLock {

    /**
     * 锁key的前缀
     */
    String lockedPrefix() default "";

    /**
     * 轮询锁的时间
     */
    long timeOut() default 2000;

    /**
     * key在redis里存在的时间，1000S
     */
    int expireTime() default 1000;

    TimeUnit expireUnit() default TimeUnit.SECONDS;
}
