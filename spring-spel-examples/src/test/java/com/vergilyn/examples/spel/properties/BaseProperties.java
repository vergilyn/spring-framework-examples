package com.vergilyn.examples.spel.properties;

import java.time.LocalTime;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:spel/base.properties")
@Component("base")
@Data
public class BaseProperties extends DefaultToString {
	@Value("${vergilyn.spel.name: vergilyn}")
	private String name;
	@Value("${vergilyn.spel.email}")
	private String email;

	/**
	 * question >>>> Failed to convert value of type 'java.lang.String' to required type 'java.time.LocalDate';
	 */
	@Value("${vergilyn.spel.date}")
	private String date;

	/**
	 * 嵌套(由内往外)。
	 * 解析：先取内部${vergilyn.spel.ref} -> ${vergilyn.spel.name} -> "vergilyn"
	 */
	@Value(value = "${${vergilyn.spel.ref}}")
	private String ref;

	@Value("${vergilyn.spel.qq}")
	private Integer qq;

	public LocalTime getLocalTime(){
		return LocalTime.now();
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
