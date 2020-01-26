package com.vergilyn.examples.springboot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @date 2017/3/19
 */
@Aspect
@Component
public class AopAspect {

    @AfterReturning("execution(* com.vergilyn.examples.springboot.aop.*Service.*(..))")
    public void afterReturning(JoinPoint joinPoint) {
        System.out.println("aop @AfterReturning >>>> print, " + joinPoint);
    }

}