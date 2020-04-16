package com.vergilyn.examples.springboot.mybatis.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Hotel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long city;
	private String name;
	private String address;
	private String zip;


}