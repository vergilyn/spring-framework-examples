package com.vergilyn.demo.springboot.properties;

import com.vergilyn.demo.springboot.properties.bean.BasePropertyBean;
import com.vergilyn.demo.springboot.properties.bean.ComplexPropertyBean;
import com.vergilyn.demo.springboot.properties.bean.RandomPropertyBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author VergiLyn
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@PropertySource(value={"classpath:config/application.properties"}
        , ignoreResourceNotFound = true)
@Controller
public class PropertyApplication {
	
	@Autowired
    BasePropertyBean base;
	@Autowired
    ComplexPropertyBean complex;
    @Autowired
    RandomPropertyBean random;
//    @Autowired
//    RelaxedBindPropertyBean relaxed;

    @Value("${CONSTANT_PASSWORD}")
    private String password;


    public static void main(String[] args) {
        SpringApplication.run(PropertyApplication.class, args);
    }

    @RequestMapping("/property")
    public String property(@RequestParam(value="name", required=false, defaultValue="${CONSTANT_USER}") String name
            , Model model) {
        model.addAttribute("name", name);
        model.addAttribute("password", password);
        System.out.println(base);
        System.out.println(complex);
        System.out.println(random);
//        System.out.println(relaxed);
        return "custom";
    }
}
