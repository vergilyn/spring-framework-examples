package com.vergilyn.examples.springboot.aop;

import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/19
 */
@Component
public class AopService {

    public void getMsg() {
        System.out.println("AopService.getMsg() >>>> print");
    }

}
