package com.vergilyn.examples.spel;

import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:config/spel/value-spel.properties")
@Component
@Data
@ToString
public class SpelProperty {
	
	@Value("${spel.name}")
	private String spelName;

	/* base：指BaseProperty.class(默认为baseProperty)，因为定义了@Component("base")
	 * baseSong：并不是*.properties中的key
	 */
	@Value("#{base.baseSong}")
	private String spelSong;

	/*
	 * // @Value("${ '#{base.baseSong}' }") //这个不支持。因为#开头的才是spel。
	 * 解析：由内往外，${spel.mix} = baseSong。然后在spel表达式中，('')表示定义字符串。
	 * 所以 #{'baseSong'} = baseSong
	 */
	@Value("#{ '${spel.mix}' }")
	private String mixSong;

	//组合，特别.后面跟的是对象属性。所以要是class中的属性，而不是properties中的key
	@Value("#{base. ${spel.mix} }")
	private String mixSong2;
	
}
