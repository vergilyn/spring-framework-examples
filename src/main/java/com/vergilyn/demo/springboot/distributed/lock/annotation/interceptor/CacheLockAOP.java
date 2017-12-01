package com.vergilyn.demo.springboot.distributed.lock.annotation.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.vergilyn.demo.springboot.distributed.lock.annotation.CacheLock;
import com.vergilyn.demo.springboot.distributed.lock.annotation.LockedComplexObject;
import com.vergilyn.demo.springboot.distributed.lock.annotation.LockedObject;
import com.vergilyn.demo.springboot.distributed.lock.redis.RedisLock;

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
public class CacheLockAOP {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 定义缓存逻辑
     */
    @Around("@annotation(com.vergilyn.demo.springboot.distributed.lock.annotation.CacheLock)")
    public void cache(ProceedingJoinPoint pjp) {
        Method method = getMethod(pjp);

        CacheLock cacheLock = method.getAnnotation(CacheLock.class);
        String key = getRedisKey(method.getParameterAnnotations(), pjp.getArgs());

        RedisLock redisLock = new RedisLock(cacheLock.lockedPrefix(), key, redisTemplate);
        System.out.println(Thread.currentThread().getName() + ": " + redisLock);

        boolean isLock = redisLock.isLock(5);
//        boolean isLock = redisLock.lock(5, 5, TimeUnit.SECONDS);
        if (isLock) {
            try {
                pjp.proceed();
                return;
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                System.out.println(redisLock + ", unlock: " + redisLock.isLock());
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
            throw new RuntimeException("方法参数为空，没有被锁定的对象");
        }
        if (null == annotations || annotations.length == 0) {
            throw new RuntimeException("没有被注解的参数");
        }
        //不支持多个参数加锁，只支持第一个注解为lockedObject或者lockedComplexObject的参数
        int index = -1;//标记参数的位置指针
        for (int i = 0; i < annotations.length; i++) {
            for (int j = 0; j < annotations[i].length; j++) {
                if (annotations[i][j] instanceof LockedComplexObject) {//注解为LockedComplexObject
                    index = i;
                    try {
                        return args[i].getClass().getField(((LockedComplexObject) annotations[i][j]).field()).toString();
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new RuntimeException("注解对象中没有该属性" + ((LockedComplexObject) annotations[i][j]).field());
                    }
                }

                if (annotations[i][j] instanceof LockedObject) {
                    index = i;
                    break;
                }
            }
            //找到第一个后直接break，不支持多参数加锁
            if (index != -1) {
                break;
            }
        }

        if (index == -1) {
            throw new RuntimeException("请指定被锁定参数");
        }

        return args[index].toString();
    }
}
