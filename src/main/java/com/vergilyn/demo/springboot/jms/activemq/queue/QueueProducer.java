package com.vergilyn.demo.springboot.jms.activemq.queue;


import com.vergilyn.demo.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.HashMap;
import java.util.Map;

@Component
public class QueueProducer implements CommandLineRunner {
	private final static String clazz = QueueProducer.class.getSimpleName();

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue mapQueue;

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Map map = new HashMap(Constant.map);
			map.put("id",i);
			System.out.println(clazz + ", " + i + ": Message was sent to the Queue.");
			sendObject(map);
			Thread.sleep(1000); // 为了输出结果好理解.
		}
	}


	public void sendObject(Map<String,Object> map){
		this.jmsMessagingTemplate.convertAndSend(this.mapQueue, map);
	}
}