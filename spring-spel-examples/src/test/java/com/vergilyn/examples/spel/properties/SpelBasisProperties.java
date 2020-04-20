package com.vergilyn.examples.spel.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SpelBasisProperties extends DefaultToString {

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

	@Override
	public String toString() {
		return super.toString();
	}
}
