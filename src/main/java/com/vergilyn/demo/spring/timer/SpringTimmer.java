package com.vergilyn.demo.spring.timer;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
// @EnableScheduling
public class SpringTimmer {
	
//	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Scheduled(cron = "0 0/1 * * * ?")	
	public void timmer(){
		System.out.println("执行定时器： ");
		for (int i = 0; i < 2; i++) {
			System.out.println(i);
		}
	}
}
