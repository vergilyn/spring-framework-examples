package com.vergilyn.examples.spring.aop.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.AbstractLazyCreationTargetSource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactoryTests {

	/**
	 * 可以参考`dubbo: 3.1.0, ReferenceBean`。
	 *
	 * <p> 主要参考 {@linkplain  org.springframework.aop.framework.JdkDynamicAopProxy#getProxy() `JdkDynamicAopProxy#getProxy()`}。
	 * <br/> 核心还是 {@linkplain Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}
	 * JdkDynamicAopProxy 同时实现了 InvocationHandler，所以最终会先调用
	 * {@linkplain org.springframework.aop.framework.JdkDynamicAopProxy#invoke(Object, Method, Object[]) `JdkDynamicAopProxy#invoke(Object, Method, Object[])`}
	 * <br/>（注意其中 {@link TargetSource} 的使用）
	 *
	 * <p>
	 * <p> 根据dubbo思路，并不一定会 `#addAdvice`，而是把此AopProxy 作为一种创建 lazy-proxy-object 的方式。
	 * 目的是
	 *
	 * @see org.springframework.aop.framework.JdkDynamicAopProxy
	 */
	@Test
	public void helloworld(){
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setTargetSource(new VergilynTargetSource());
		proxyFactory.addInterface(DemoService.class);

		proxyFactory.addAdvice(new MethodBeforeAdvice() {
			@Override
			public void before(Method method, Object[] args, Object target) throws Throwable {
				System.out.println("MethodBeforeAdvice >>>> " + method.getName());
			}
		});

		proxyFactory.addAdvice(new AfterReturningAdvice() {
			@Override
			public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
					throws Throwable {
				System.out.printf("AfterReturningAdvice >>>> method-name: %s, returnValue: %s \n",
				                    method.getName(), returnValue);
			}
		});

		Object proxy = proxyFactory.getProxy();

		DemoService proxyService = (DemoService) proxy;

		String sayHello = proxyService.sayHello("world.");

		System.out.println(sayHello);
	}


	public static interface DemoService {
		String sayHello(String name);
	}

	public static class VergilynTargetSource extends AbstractLazyCreationTargetSource {

		@Override
		protected Object createObject() throws Exception {
			return new DemoService() {
				@Override
				public String sayHello(String name) {
					System.out.println(this.getClass().getSimpleName() + "#sayHello(name)");
					return "hello, " + name;
				}
			};
		}
	}
}
