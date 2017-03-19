package com.vergilyn.demo.springboot.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/19
 */
@SpringBootApplication
//开启aop支持; 可properties配置proxyTargetClass、exposeProxy
//@EnableAspectJAutoProxy //(proxyTargetClass = true,exposeProxy = false)
public class AopApplication implements CommandLineRunner {
    @Autowired
    private AopService aopService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AopApplication.class);
        app.setAdditionalProfiles("aop");
        app.run(args);
    }

    @Override
    public void run(String... args) {
        System.out.println(this.aopService.getMsg());
    }
}
