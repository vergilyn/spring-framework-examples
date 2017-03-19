package com.vergilyn.demo.spring.scheduling;

import com.vergilyn.demo.annotation.Injector.LogInjector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //通过@EnableScheduling注解开启对计划任务的支持
//@ImportResource(locations = "ConfigXML/annotation-scan.xml" )
@Import(value = LogInjector.class)
public class TimmerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimmerApplication.class, args);
	}
}
