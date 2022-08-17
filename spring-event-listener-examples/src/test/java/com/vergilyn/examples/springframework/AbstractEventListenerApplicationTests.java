package com.vergilyn.examples.springframework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest(classes = EventListenerApplication.class)
public abstract class AbstractEventListenerApplicationTests {
	protected static final AtomicInteger _INDEX = new AtomicInteger(0);

	@Autowired
	protected ApplicationContext applicationContext;

	protected static void printf(String format, Object... args){
		String prefix = String.format("[%d][vergilyn][%s][thread-%s] >>>> ", _INDEX.getAndIncrement(), LocalTime.now(), Thread.currentThread().getName());
		System.out.printf(prefix + format + "", args).println();
	}
}