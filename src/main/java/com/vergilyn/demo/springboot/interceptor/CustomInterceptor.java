package com.vergilyn.demo.springboot.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.vergilyn.demo.springboot.filter.common.FilterRule;
import com.vergilyn.demo.springboot.filter.common.ParameterRequestWrapper;

public class CustomInterceptor implements HandlerInterceptor{
	private final static String clazz = CustomInterceptor.class.getSimpleName();
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println(clazz + " >>>> preHandle(...), 在请求处理之前进行调用（Controller方法调用之前）.");
		
		String requestURI = request.getRequestURI();
		//true表示继续,false表示拦截掉
		boolean rs = requestURI.contains("need") ? false : true;
		if(!rs){
			// 根据此路径写法可知，拦截掉后，application-thymeleaf.properties的设置都没用。
			request.getRequestDispatcher("/templates/interceptor/interceptor_forward.html").forward(request, response);
		}
		return rs;
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
