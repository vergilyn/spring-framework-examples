package com.vergilyn.examples.springframework;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;

/**
 * @author vergilyn
 * @since 2021-06-18
 */
public abstract class AbstractSpringBootFeatureTests {

	protected void printJson(Object obj){
		printJson(obj, true);
	}

	protected void printJson(Object obj, boolean prettyFormat){
		if (obj instanceof Enum){
			SerializeConfig config = new SerializeConfig();
			config.configEnumAsJavaBean((Class<? extends Enum>) obj.getClass()); // 配置enum转换

			List<SerializerFeature> features = Lists.newArrayList();
			if (prettyFormat){
				features.add(SerializerFeature.PrettyFormat);
			}

			System.out.println(JSON.toJSONString(obj, config, features.toArray(new SerializerFeature[0])));
			return;
		}

		System.out.println(JSON.toJSONString(obj, prettyFormat));
	}
}
