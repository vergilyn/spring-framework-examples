package com.vergilyn.examples.springboot.properties;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vergilyn.base")
@Data
@ToString
public class BaseProperties {
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

}
