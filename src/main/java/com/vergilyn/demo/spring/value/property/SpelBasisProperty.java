package com.vergilyn.demo.spring.value.property;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpelBasisProperty {

	@Value("#{124}")
	public int spelInt;
	@Value("#{124.24}")
	public float spelFloat;
	@Value("#{1e4}")
	public double spelSpec;
	@Value("#{true}")
	public boolean spelBoolean = false;
	@Value("#{'string'}") // 或 @Value('#{"string"}')
	public String spelString;
	/* 调用方法。
	 * 在SpEL中避免抛出空指针异常（NullPointException）的方法是使用null-safe存取器：
	 * ?. 运算符代替点（.） #{object?.method()},如果object=null，则不会调用method()
	 */
	@Value("#{base.getNestSong()}")
	public String spelNestSong;
	@Value("#{base.nowDate()}")	//可以是任何类型
	public Date spelNowDate;
	@Value("#{null?.toString()}")	//当?.左边为null时，不再执行右边的方法。
	public String spelNull;
	
	/**
	 * 在SpEL中，使用T()运算符会调用类作用域的方法和常量。
	 */
	@Value("#{T(java.lang.Math).PI}")
	public double T_PI;
	@Value("#{T(java.lang.Math).random()}")
	public double T_andom;
	
	/**
	 * SpEL运算>>>>
	 *  四则运算：+ - * /
	 *  比较：eq(==),lt(<),le(<=),gt(>),ge(>=)。
	 *  逻辑表达式：and,or,not或!。
	 *  三元表达式：
	 *  	i. #{base.name == 'vergilyn' ? true : false}
	 *  	ii. #{base.name == 'vergilyn' ? base.name : 'dante'}
	 *  	  spel可以简化为：#{base.name  ?: 'dante'} 当base.name !=null则取base.name，否则取'dante'。
	 *  	(?:)通常被称为elvis运算符。
	 *  正则：#{base.song matches '[a-zA-Z0-9._%+_]+@[a-zA-Z0-9.-]+\\.com'}
	 */
	@Value("#{100 / 20}")	//四则运算： + - * /
	public int spelArithmetic;
}
