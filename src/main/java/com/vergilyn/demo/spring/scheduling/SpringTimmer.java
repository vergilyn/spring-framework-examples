package com.vergilyn.demo.spring.scheduling;

import com.vergilyn.demo.annotation.Log;
import org.apache.log4j.Logger;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class SpringTimmer {
}

@Component
class Timmer01{
	@Log
	private Logger log;
	private static int num = 0;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "5,10,15,20 * * * * ?")
	public void timmer() throws InterruptedException{
		log.info("spring timmer 01 : " + ++num + " , " + dateFormat.format(new Date()));
		Thread.sleep(3*1000);
	}
}
@Component
class Timmer02{
	@Log
	private Logger log;
	private static int num = 0;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "5,10,15,20 * * * * ?")
	public void timmer() throws InterruptedException{
		log.info("spring timmer 02 : " + ++num + " , " + dateFormat.format(new Date()));
		Thread.sleep(3*1000);
	}
}
@Component
class Timmer03{
	@Log
	private Logger log;
	private static int num = 0;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "5,10,15,20 * * * * ?")
	public void timmer() throws InterruptedException{
		log.info("spring timmer 03 : " + ++num + " , " + dateFormat.format(new Date()));
		Thread.sleep(3*1000);
	}
}

@Component
class TimmerNew{
	@Log
	private Logger log;
	private static int num = 0;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	@Scheduled(cron = "8,16,24 * * * * ?")
	public void timmer() throws InterruptedException{
		log.info("new task : " + ++num + " , " + dateFormat.format(new Date()));
		Thread.sleep(1*1000);
	}
}