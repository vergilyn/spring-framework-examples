package com.vergilyn.spring.demo.value;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.vergilyn.demo.spring.value.SpelValueApplication;
import com.vergilyn.demo.spring.value.property.BaseProperty;
import com.vergilyn.demo.spring.value.property.SpelBasisProperty;
import com.vergilyn.demo.spring.value.property.SpelProperty;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpelValueApplication.class)
public class SpelValueApplicationTest {
	@ClassRule
	public static OutputCapture out = new OutputCapture();
	@Autowired
	private BaseProperty base;
	@Autowired
	private SpelProperty spel;
	@Autowired
	private SpelBasisProperty spelBasis;

	@Test
	public void test() throws IOException {
		System.out.println(base.getClass().getSimpleName()+": "+JSON.toJSONString(base));
		System.out.println(spel.getClass().getSimpleName()+": "+JSON.toJSONString(spel));
		System.out.println(spelBasis.getClass().getSimpleName()+": "+JSON.toJSONString(spelBasis));
	}

}
