package com.vergilyn.demo.springboot.jsm.activemq;

import javax.jms.Queue;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
// 开启JMS
@EnableJms
public class ActivemqApplication {
	
	// 点对点（point  to point ,queue）
	@Bean
	public javax.jms.Queue queue() {
		return new ActiveMQQueue("jms.activemq.queue.map");
	}
	// 发布/订阅（publish/subscribe,topic）
	public javax.jms.Topic topic(){
		return new ActiveMQTopic("jms.activemq.topic.msg");
	}
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ActivemqApplication.class);
		app.setAdditionalProfiles("activemq");
		app.run(args);
	}
	
}
