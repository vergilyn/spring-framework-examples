package com.vergilyn.demo.springboot.filter.custom;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vergilyn.demo.springboot.filter.common.FilterRule;
import com.vergilyn.demo.springboot.filter.common.ParameterRequestWrapper;

@Component
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
