package com.vergilyn.demo.springboot.jsm.activemq.queue;


import java.util.Map;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import com.vergilyn.demo.constant.Constant;

@Component
public class ActivemqQueueProducer implements CommandLineRunner {
	private final static String clazz = ActivemqQueueProducer.class.getSimpleName();

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private Queue mapQueue;

	@Override
	public void run(String... args) throws Exception {
		sendObject(Constant.map);
		System.out.println(clazz + ": Message was sent to the Queue.");
	}


	public void sendObject(Map<String,Object> map){
		this.jmsMessagingTemplate.convertAndSend(this.mapQueue, map);
	}
}