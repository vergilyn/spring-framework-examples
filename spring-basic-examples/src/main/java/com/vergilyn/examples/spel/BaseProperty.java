package com.vergilyn.examples.spel;

import java.util.Calendar;
import java.util.Date;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:config/spel/value-base.properties")
@Component("base")
@Data
@ToString
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

	public Date nowDate(){
		return Calendar.getInstance().getTime();
	}
}
