package com.vergilyn.demo.spring.value.property;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:config/value/value-base.properties")
@Component("base")
public class BaseProperty {
	@Value("${base.name}")
	private String baseName;
	@Value("${base.song}")
	private String baseSong;
	/* 嵌套(内往外) 
	 * 解析：先取内部${base.nest.song}=base.song -> ${base.song} = Safe&Sound
	 */
	@Value(value = "${${base.nest.song}}") 
	private String nestSong;
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() +":{baseName: "+this.baseName+", baseSong: "+this.baseSong+"}";
	}
	
	public String getBaseName() {
		return baseName;
	}
	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}
	public String getBaseSong() {
		return baseSong;
	}
	public void setBaseSong(String baseSong) {
		this.baseSong = baseSong;
	}

	public String getNestSong() {
		return nestSong;
	}

	public void setNestSong(String nestSong) {
		this.nestSong = nestSong;
	}
	
	public Date nowDate(){
		return Calendar.getInstance().getTime();
	}
}
