package com.vergilyn.examples.springframework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest(classes = EventListenerApplication.class)
public abstract class AbstractEventListenerApplicationTests {

	@Autowired
	protected ApplicationContext applicationContext;
}