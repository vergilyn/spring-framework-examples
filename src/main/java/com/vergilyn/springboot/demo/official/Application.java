package com.vergilyn.springboot.demo.official;

import groovy.transform.BaseScript;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/1/8
 */
//@SpringBootApplication //等价于 @Configuration + @EnableAutoConfiguration + @ComponentScan

//通常建议你使用 @Configuration 类作为主要源。一般定义 main 方法的类也是主要 @Configuration 的一个很好候选。
@Configuration	

//@ComponentScan 注解自动收集所有的Spring组件，包括@Component、@Service、@Repository、@Controller、@Configuration 等类。
@ComponentScan	
//被 @EnableAutoConfiguration 注解的类所在包将被用来搜索 @Entity 项。（建议将@EnableAutoConfiguration添加到主 @Configuration 类上。）
@EnableAutoConfiguration(
//		exclude={DataSourceAutoConfiguration.class}	// 禁用：特定自动配置类
) 

//导入xml配置
//@ImportResource("classpath:/com/acme/database-config.xml")	
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
