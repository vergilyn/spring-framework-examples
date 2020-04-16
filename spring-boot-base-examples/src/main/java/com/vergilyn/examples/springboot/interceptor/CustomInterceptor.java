package com.vergilyn.examples.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author vergilyn
 * @date 2020-01-26
 */
public class CustomInterceptor implements HandlerInterceptor{
	private final static String clazz = CustomInterceptor.class.getSimpleName();

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println(clazz + " >>>> preHandle(...), 在请求处理之前进行调用（Controller方法调用之前）.");
		
		// FIXME 2020-01-26 spring-boot-2.x 可能不一样！
		if(StringUtils.containsIgnoreCase(request.getRequestURI(), "need")){
			request.getRequestDispatcher("/templates/interceptors/interceptor_forward.html")
					.forward(request, response);
			return false;
		}
		return true;  //true - 继续, false - 拦截
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println(clazz + " >>>> postHandle(...), 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）.");

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println(clazz + " >>>> afterCompletion(...), 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）.");
		
	}

}
