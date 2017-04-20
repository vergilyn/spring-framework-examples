package com.vergilyn.demo.springboot.filter.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

public class FilterRule {
	/**
	 * 自定义：过滤器逻辑方法
	 * @param req
	 * @return
	 */
	public static Map<String, String[]> filterRule(ServletRequest req){
		Map<String, String[]> map = new HashMap<String, String[]>(req.getParameterMap());
		Set<Entry<String, String[]>> entrySet = map.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				if(NumberUtils.isNumber(values[i])){
					values[i] = NumberUtils.toInt(values[i]) * 10 + "";
				}
				else values[i] += "_filter";
			}
			entry.setValue(values);
		}
		return map;
	}
}
