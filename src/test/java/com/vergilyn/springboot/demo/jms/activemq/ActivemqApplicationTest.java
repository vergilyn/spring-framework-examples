package com.vergilyn.springboot.demo.jms.activemq;

import javax.jms.JMSException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import com.vergilyn.demo.constant.Constant;
import com.vergilyn.demo.springboot.jsm.activemq.ActivemqApplication;
import com.vergilyn.demo.springboot.jsm.activemq.queue.ActivemqQueueProducer;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=ActivemqApplication.class)
public class ActivemqApplicationTest {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Autowired
	private ActivemqQueueProducer producer;

	@Test
	public void sendObject() throws InterruptedException, JMSException {
		this.producer.sendObject(Constant.map);;
		Thread.sleep(1000L);
		assertThat(this.outputCapture.toString().contains("key=name, value=vergilyn")).isTrue();
	}
}