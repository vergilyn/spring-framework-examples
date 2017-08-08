package com.vergilyn.demo.springboot.jms.activemq.queue;

import com.alibaba.fastjson.JSON;
import com.vergilyn.demo.springboot.jms.activemq.ActivemqApplication;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface QueueConsumer {

	@JmsListener(destination = ActivemqApplication.ACTIVEMQ_QUEUE)
	default void printQueue(Map<String,Object> map){
		System.out.println(this.getClass().getSimpleName() + " : " + JSON.toJSONString(map));
		System.out.println("===============================================================");
	}
}

@Component
class QueueConsumer01 implements QueueConsumer { }

@Component
class QueueConsumer02 implements QueueConsumer { }

@Component
class QueueConsumer03 implements QueueConsumer { }

@Component
class QueueConsumer04 implements QueueConsumer { }