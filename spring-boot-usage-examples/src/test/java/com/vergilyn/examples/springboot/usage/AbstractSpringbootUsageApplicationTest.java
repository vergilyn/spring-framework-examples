package com.vergilyn.examples.springboot.usage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = SpringbootUsageApplication.class)
public abstract class AbstractSpringbootUsageApplicationTest {
	@Autowired
	protected ApplicationContext applicationContext;

}