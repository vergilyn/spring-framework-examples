package com.vergilyn.demo.springboot.jms.activemq.topic;


import com.vergilyn.demo.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Topic;
import java.util.HashMap;
import java.util.Map;

@Component
public class TopicProducer implements CommandLineRunner {
	private final static String clazz = TopicProducer.class.getSimpleName();

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Topic topic;

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Map map = new HashMap(Constant.map);
			map.put("id",i);
			System.out.println(clazz + ", " + i + ": Message was sent to the Topic.");
			sendObject(map);
			Thread.sleep(1000); // 为了输出结果好理解.
			System.out.println("===============================================================");
		}
	}


	public void sendObject(Map<String,Object> map){
		this.jmsMessagingTemplate.convertAndSend(this.topic, map);
	}
}