package com.vergilyn.springboot.demo.jms.activemq;

import com.vergilyn.demo.constant.Constant;
import com.vergilyn.demo.springboot.jms.activemq.ActivemqApplication;
import com.vergilyn.demo.springboot.jms.activemq.queue.QueueProducer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.JMSException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=ActivemqApplication.class)
public class ActivemqApplicationTest {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Autowired
	private QueueProducer producer;

	@Test
	public void sendObject() throws InterruptedException, JMSException {
		this.producer.sendObject(Constant.map);;
		Thread.sleep(1000L);
		assertThat(this.outputCapture.toString().contains("key=name, value=vergilyn")).isTrue();
	}
}