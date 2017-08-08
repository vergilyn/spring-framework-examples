package com.vergilyn.demo.springboot.jms.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

import java.util.HashMap;

@SpringBootApplication
// 开启JMS
@EnableJms
public class ActivemqApplication {
	public final static String ACTIVEMQ_QUEUE = "jms.activemq.queue";
	public final static String ACTIVEMQ_TOPIC = "jms.activemq.topic";

	// 点对点（point  to point ,queue）
	@Bean
	public javax.jms.Queue queue() {
		return new ActiveMQQueue(ACTIVEMQ_QUEUE);
	}
	// 发布/订阅（publish/subscribe,topic）
	@Bean
	public javax.jms.Topic topic(){
		return new ActiveMQTopic(ACTIVEMQ_TOPIC);
	}
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ActivemqApplication.class);
		app.setAdditionalProfiles("activemq");
		HashMap properties = new HashMap();
		// 如果为true，则是Topic；如果是false或者默认，则是queue。
		properties.put("spring.jms.pub-sub-domain",true);
		app.setDefaultProperties(properties);
		app.run(args);
	}
	
}
