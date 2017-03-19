package com.vergilyn.demo.springboot.aop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/19
 */
@Component
public class AopService {

    @Value("${aop.name:Dante}")
    private String name;

    public String getMsg() {
        return "Hello, " + this.name;
    }

}
