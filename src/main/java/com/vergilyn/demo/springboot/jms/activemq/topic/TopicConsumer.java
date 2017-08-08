package com.vergilyn.demo.springboot.jms.activemq.topic;

import com.alibaba.fastjson.JSON;
import com.vergilyn.demo.springboot.jms.activemq.ActivemqApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface TopicConsumer {

	@JmsListener(destination = ActivemqApplication.ACTIVEMQ_TOPIC)
	default void printQueue(Map<String, Object> map){
		System.out.println(this.getClass().getSimpleName() + " : " + JSON.toJSONString(map));
	}
}

@Component
class TopicConsumer01 implements TopicConsumer { }

@Component
class TopicConsumer02 implements TopicConsumer { }

@Component
class TopicConsumer03 implements TopicConsumer { }

@Component
class TopicConsumer04 implements TopicConsumer { }