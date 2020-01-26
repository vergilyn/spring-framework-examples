package com.vergilyn.examples.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author vergilyn
 * @date 2020-01-26
 */
@Component
public class ScopeTest {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * prototype: 每次都会创建新对象，所以都打印`1`；<br/>
     * singleton: 单例模式，所以先打印`1`，再打印`2`；
     */
    public void test(){
        PrototypeService p1 = applicationContext.getBean(PrototypeService.class);
        p1.incrNumber();
        System.out.printf("PrototypeService >>>> %s \r\n", p1.getNumber()).println();

        PrototypeService p2 = applicationContext.getBean(PrototypeService.class);
        p2.incrNumber();
        System.out.printf("PrototypeService >>>> %s \r\n", p1.getNumber()).println();

        SingletonService s1 = applicationContext.getBean(SingletonService.class);
        s1.incrNumber();
        System.out.printf("SingletonService >>>> %s \r\n", s1.getNumber()).println();

        SingletonService s2 = applicationContext.getBean(SingletonService.class);
        s2.incrNumber();
        System.out.printf("SingletonService >>>> %s \r\n", s2.getNumber()).println();
    }
}
