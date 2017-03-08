package com.vergilyn.demo.springboot.jsm.activemq.queue;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ActivemqQueueConsumer {
	private final static String clazz = ActivemqQueueConsumer.class.getSimpleName();

	@JmsListener(destination = "jms.activemq.queue.map")
	public void printQueue(Map<String,Object> map){
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			System.out.println(clazz + " : key=" + entry.getKey() + ", value="+entry.getValue());
		}
	}
}