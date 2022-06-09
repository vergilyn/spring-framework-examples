package com.vergilyn.examples.springboot.usage.u0003;

import com.vergilyn.examples.springboot.usage.AbstractSpringbootUsageApplicationTest;

import java.util.List;

public abstract class AbstractSpringStrategyTemplateTests extends AbstractSpringbootUsageApplicationTest {

	public void print(String prefix, List list){
		System.out.printf("%s, size: %d\n", prefix, list == null ? 0 : list.size());

		if (list == null || list.isEmpty()){
			System.out.printf("\t- NULL \n");
		}else {

			for (Object o : list) {
				System.out.printf("\t- %s \n", o.getClass().getSimpleName());
			}
		}

		System.out.println();
	}
}
