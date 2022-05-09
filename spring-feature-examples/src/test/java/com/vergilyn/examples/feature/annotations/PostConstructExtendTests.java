package com.vergilyn.examples.feature.annotations;

import com.vergilyn.examples.feature.AbstractSpringFeatureTests;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.annotation.PostConstruct;

public class PostConstructExtendTests extends AbstractSpringFeatureTests {

	@Test
	public void test(){
		AnnotationConfigApplicationContext context = initApplicationContext();
		context.register(Child_Default_Extend.class, Child_Override_Invoke.class, Child_Override_DoNothing.class);
		context.refresh();

		// 继承父类方法，支持`@PostConstruct`
		Child_Default_Extend child_default_extend = context.getBean(Child_Default_Extend.class);
		System.out.println("child_default_extend parent-field: " + child_default_extend.getParentField());

		// 重写父类方法，但默认调用`super`，支持`@PostConstruct`
		Child_Override_Invoke child_override_invoke = context.getBean(Child_Override_Invoke.class);
		System.out.println("child_override_invoke parent-field: " + child_override_invoke.getParentField());

		// 重写父类方法，不支持`@PostConstruct`
		Child_Override_DoNothing child_override_doNothing = context.getBean(Child_Override_DoNothing.class);
		System.out.println("child_override_doNothing parent-field: " + child_override_doNothing.getParentField());
	}

	private static abstract class Parent{
		private String parentField;

		@PostConstruct
		public void init(){
			this.parentField = "parent-set";
		}

		public String getParentField() {
			return parentField;
		}
	}

	private static class Child_Default_Extend extends Parent{
	}

	private static class Child_Override_Invoke extends Parent{

		@Override
		public void init() {
			super.init();
		}
	}

	private static class Child_Override_DoNothing extends Parent{
		@Override
		public void init() {
		}
	}
}
