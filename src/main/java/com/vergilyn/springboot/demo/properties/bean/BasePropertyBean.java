package com.vergilyn.springboot.demo.properties.bean;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties()
@PropertySource("classpath:config/properties/BaseProperty.properties")
@Component
public class BasePropertyBean {
	private byte byte_ ;
	private short short_ ;
	private int int_ ;
	private long long_ ;
	private float float_ ;
	private double double_ ;
	private char char_ ;
	private boolean boolean_ ;
	@NotNull	//加JSR-303 javax.validation约束注解
	private String string_;
	/* 不知道怎么直接注入Date类型*/
//	private Date date_;
	
	public byte getByte_() {
		return byte_;
	}
	public void setByte_(byte byte_) {
		this.byte_ = byte_;
	}
	public short getShort_() {
		return short_;
	}
	public void setShort_(short short_) {
		this.short_ = short_;
	}
	public int getInt_() {
		return int_;
	}
	public void setInt_(int int_) {
		this.int_ = int_;
	}
	public long getLong_() {
		return long_;
	}
	public void setLong_(long long_) {
		this.long_ = long_;
	}
	public float getFloat_() {
		return float_;
	}
	public void setFloat_(float float_) {
		this.float_ = float_;
	}
	public double getDouble_() {
		return double_;
	}
	public void setDouble_(double double_) {
		this.double_ = double_;
	}
	public char getChar_() {
		return char_;
	}
	public void setChar_(char char_) {
		this.char_ = char_;
	}
	public boolean isBoolean_() {
		return boolean_;
	}
	public void setBoolean_(boolean boolean_) {
		this.boolean_ = boolean_;
	}
	public String getString_() {
		return string_;
	}
	public void setString_(String string_) {
		this.string_ = string_;
	}
	
	
}
