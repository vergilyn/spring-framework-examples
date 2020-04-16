package com.vergilyn.examples.springboot.official;

//@SpringBootApplication //等价于 @Configuration + @EnableAutoConfiguration + @ComponentScan

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 等价于`{@linkplain org.springframework.boot.autoconfigure.SpringBootApplication}`
 * @author vergilyn
 * @date 2020-01-26
 */
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
@Controller
public class FirstApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstApplication.class, args);
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(required=false, defaultValue="vergilyn") String name
            , Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
}
