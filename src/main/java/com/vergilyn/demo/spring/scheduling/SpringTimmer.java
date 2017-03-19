package com.vergilyn.demo.spring.scheduling;

import com.vergilyn.demo.annotation.Log;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SpringTimmer {
	@Log
	private Logger log;

//	@Transactional(readOnly=false,rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Scheduled(cron = "0/5 * * * * ?")
	public void timmer(){
		log.info("spring timmer : 1");
	}
}
