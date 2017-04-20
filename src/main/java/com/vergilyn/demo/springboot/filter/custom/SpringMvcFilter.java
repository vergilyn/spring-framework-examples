package com.vergilyn.demo.springboot.filter.custom;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vergilyn.demo.springboot.filter.common.FilterRule;
import com.vergilyn.demo.springboot.filter.common.ParameterRequestWrapper;

/**
 * 针对本demo，更好的可能是{@link org.springframework.web.filter.HttpPutFormContentFilter}。<br>
 * 更多mvc定义filter参考：http://blog.csdn.net/ochangwen/article/details/52727743
 * @author VergiLyn
 * 2017年3月3日
 */
@WebFilter(urlPatterns="/needFilter") //加入此注解则application必须加入@ServletComponentScan
@Profile("mvc")
public class SpringMvcFilter extends OncePerRequestFilter {
	private final static String clazz = SpringMvcFilter.class.getSimpleName();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println(clazz + " >>>> doFilterInternal(...)");
		request = new ParameterRequestWrapper((HttpServletRequest)request, FilterRule.filterRule(request));
		filterChain.doFilter(request, response);
		
	}

}
