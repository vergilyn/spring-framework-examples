package com.vergilyn.spring.demo.value.property;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:config/value/value-spel.properties")
@Component
public class SpelProperty {
	
	@Value("${spel.name}")
	private String spelName;
	/* base：指BaseProperty.class(默认为baseProperty)，因为定义了@Component("base")
	 * baseSong：并不是*.properties中的key
	 */
	@Value("#{base.baseSong}")
	private String spelSong;
	/* // @Value("${ '#{anotherObj.media}' }") //这个不支持。
	 * 解析：由内往外，${spel.mix} = base.song。然后在spel表达式中，（''）表示定义字符串。
	 * 所以 #{'base.song'} = base.song
	 */
	@Value("#{ '${spel.mix}' }")
	private String mixSong;
	
	public String getSpelName() {
		return spelName;
	}
	public void setSpelName(String spelName) {
		this.spelName = spelName;
	}
	public String getSpelSong() {
		return spelSong;
	}
	public void setSpelSong(String spelSong) {
		this.spelSong = spelSong;
	}
	
	public String getMixSong() {
		return mixSong;
	}
	public void setMixSong(String mixSong) {
		this.mixSong = mixSong;
	}
}
