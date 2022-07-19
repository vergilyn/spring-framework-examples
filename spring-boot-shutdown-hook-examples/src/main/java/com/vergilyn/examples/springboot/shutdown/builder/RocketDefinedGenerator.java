package com.vergilyn.examples.springboot.shutdown.builder;

/**
 *
 * @author vergilyn
 * @since 2021-09-30
 */
public class RocketDefinedGenerator {
	public static final String PREFIX_TOPIC = "vergilyn_topic_";
	public static final String PREFIX_TAG = "vergilyn_tag_";
	public static final String PREFIX_GROUP_PRODUCER = "vergilyn_producer_group_";
	public static final String PREFIX_GROUP_CONSUMER = "vergilyn_consumer_group_";

	private final String topic;
	private final String tag;

	private final String producerGroup;
	private final String consumerGroup;

	public RocketDefinedGenerator(String flag) {
		this.topic = PREFIX_TOPIC + flag;
		this.tag = PREFIX_TAG + flag;
		this.producerGroup = PREFIX_GROUP_PRODUCER + flag;
		this.consumerGroup = PREFIX_GROUP_CONSUMER + flag;
	}

	public String topic() {
		return topic;
	}

	public String tag() {
		return tag;
	}

	public String producerGroup() {
		return producerGroup;
	}

	public String consumerGroup() {
		return consumerGroup;
	}
}
