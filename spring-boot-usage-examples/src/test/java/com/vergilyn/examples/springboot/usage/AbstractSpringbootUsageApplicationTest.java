package com.vergilyn.examples.springboot.usage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest(classes = SpringbootUsageApplication.class)
public abstract class AbstractSpringbootUsageApplicationTest {
	@Autowired
	protected AnnotationConfigApplicationContext applicationContext;


}