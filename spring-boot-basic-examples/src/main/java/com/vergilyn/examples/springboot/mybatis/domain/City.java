package com.vergilyn.examples.springboot.mybatis.domain;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class City implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private String state;
	private String country;

}
