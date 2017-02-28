package com.vergilyn.springboot.demo.redis.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RedisCacheBean implements Serializable{
	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public String date;
	public int quantity;
	
	
	public RedisCacheBean(String id, String name, Date date, int quantity) {
		super();
		this.id = id;
		this.name = name;
		this.date = new SimpleDateFormat("yyyyMMddHHmmsss").format(date);
		this.quantity = quantity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	
}
