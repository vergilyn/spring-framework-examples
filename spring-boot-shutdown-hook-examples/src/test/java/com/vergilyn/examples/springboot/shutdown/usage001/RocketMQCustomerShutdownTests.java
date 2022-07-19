package com.vergilyn.examples.springboot.shutdown.usage001;

import com.vergilyn.examples.springboot.shutdown.builder.RocketDefinedGenerator;
import com.vergilyn.examples.springboot.shutdown.configuration.MQConsumerContextClosedEventListener;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

@Import({RocketMQCustomerShutdownTests.ConsumerShutdownListener.class
		, MQConsumerContextClosedEventListener.class
})
public class RocketMQCustomerShutdownTests extends AbstractRocketMQTests {
	private static final String POSTFIX = "consumerShutdown";
	public static final String CONSUMER_GROUP = RocketDefinedGenerator.PREFIX_GROUP_CONSUMER + POSTFIX;
	public static final String TOPIC = RocketDefinedGenerator.PREFIX_TOPIC + POSTFIX;
	public static final String TAG = RocketDefinedGenerator.PREFIX_TAG + POSTFIX;

	/**
	 * {@link DefaultSingletonBeanRegistry#destroySingletons()}，注意是倒序调用 destroy/shutdown
	 * <pre>
	 *   org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory,
	 *   defaultMQProducer,
	 *   rocketMQTemplate,
	 *   lettuceClientResources,
	 *   redisConnectionFactory,
	 *   jvmGcMetrics,
	 *   logbackMetrics,
	 *   simpleMeterRegistry,
	 *   redisKeyValueAdapter,
	 *   redisKeyValueTemplate,
	 *   defaultValidator,
	 *   org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer_1
	 * </pre>
	 */
	@SneakyThrows
	@Test
	public void test(){
		syncSendMessage(TOPIC, TAG, LocalTime.now() + ": 测试 MQ-consumer shutdown.");

		// registryAnnoRocketMQListener(ConsumerShutdownListener.class);

		asyncDelayClose(1000);

		TimeUnit.MINUTES.sleep(2);
	}


	@RocketMQMessageListener(
			consumerGroup = CONSUMER_GROUP,
			topic = TOPIC,
			selectorType = SelectorType.TAG,
			selectorExpression = TAG,
			consumeMode = ConsumeMode.ORDERLY,
			consumeThreadMax = 10
	)
	@Lazy(false)
	public static class ConsumerShutdownListener implements RocketMQListener<MessageExt> {
		private static final String LOG_PREFIX = "[vergilyn][RocketMQ-consumer] >>>> ";

		@Resource
		private StringRedisTemplate stringRedisTemplate;

		@Override
		public final void onMessage(MessageExt messageExt) {
			String logPrefix = LOG_PREFIX + String.format("topic: %s, tags: %s.", messageExt.getTopic(), messageExt.getTags());
			System.out.printf("%s consumer listener begin... \n", logPrefix);

			String msgBody = new String(messageExt.getBody());
			System.out.printf("%s message-body: %s \n", logPrefix, msgBody);

			// 执行耗时，大约 0.5 ~ 1s
			String msg = convertMessageBody(msgBody);

			// 执行耗时，大约 1.5 ~ 2.5s
			String redisValue = getByRedis("any-redis-key");

			// 执行耗时，大约 0.0 ~ 0.5s
			consumePostProcessor();
		}

		protected String convertMessageBody(String messageBody){
			sleepSafeByMillis(RandomUtils.nextInt(500, 1000));

			return messageBody;
		}

		protected String getByRedis(String redisKey){
			sleepSafeByMillis(RandomUtils.nextInt(1500, 2500));

			return stringRedisTemplate.opsForValue().get(redisKey);
		}

		protected void consumePostProcessor(){
			sleepSafeByMillis(RandomUtils.nextInt(0, 500));
		}

	}

}
