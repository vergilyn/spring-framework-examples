package com.vergilyn.demo.springboot.distributed.lock.annotation.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import com.vergilyn.demo.springboot.distributed.lock.annotation.RedisDistributedLock;
import com.vergilyn.demo.springboot.distributed.lock.annotation.RedisLockedKey;
import com.vergilyn.demo.springboot.distributed.lock.redis.RedisLock;
import com.vergilyn.demo.springboot.distributed.lock.redis.exception.RedisLockException;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/12/1
 */
@Component
@Aspect
public class RedisDistributedLockAop {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 定义缓存逻辑
     */
    @Around("@annotation(com.vergilyn.demo.springboot.distributed.lock.annotation.RedisDistributedLock)")
    public void cache(ProceedingJoinPoint pjp) {
        Method method = getMethod(pjp);

        RedisDistributedLock cacheLock = method.getAnnotation(RedisDistributedLock.class);
        String key = getRedisKey(method.getParameterAnnotations(), pjp.getArgs());

        RedisLock redisLock = new RedisLock(cacheLock.lockedPrefix(), key, redisTemplate);

        //       boolean isLock = redisLock.lockB(cacheLock.timeout(), cacheLock.expireTime());
        boolean isLock = redisLock.lockA(cacheLock.timeout(), cacheLock.expireTime(), TimeUnit.MILLISECONDS);
        if (isLock) {
            try {
                pjp.proceed();
                return;
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                redisLock.unlock();
            }
        }
        System.out.println("执行方法失败");
    }

    /**
     * 获取被拦截方法对象
     * <p>
     * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象
     * 而缓存的注解在实现类的方法上
     * 所以应该使用反射获取当前对象的方法对象
     */
    private Method getMethod(ProceedingJoinPoint pjp) {
        //获取参数的类型
        Object[] args = pjp.getArgs();
        Class[] argTypes = new Class[pjp.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = null;
        try {
            method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        return method;

    }

    private String getRedisKey(Annotation[][] annotations, Object[] args){
        if (null == args || args.length == 0) {
            throw new RedisLockException("方法参数为空，没有被锁定的对象");
        }
        if (null == annotations || annotations.length == 0) {
            throw new RedisLockException("没有被注解的参数");
        }
        // 不支持多个参数加锁，只支持第一个注解为RedisLockedKey的参数
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof RedisLockedKey) { //注解为LockedComplexObject
                    RedisLockedKey redisLockedKey = (RedisLockedKey) annotations[i][j];
                    String field = redisLockedKey.field();
                    try {
                        return StringUtils.isBlank(field) ?
                                    args[i].toString() : args[i].getClass().getField(redisLockedKey.field()).toString();
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new RedisLockException("注解对象中不存在属性: " + redisLockedKey.field());
                    }
                }
            }
        }

        throw new RedisLockException("未找到注解对象!");
    }
}
