package com.vergilyn.demo.constant;

import java.util.HashMap;
import java.util.Map;

public class Constant {
	public static final String name = "vergilyn";
	public static final Map<String,Object> map;
	static{
		map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("song", "Safe&Sound");
	}
}
