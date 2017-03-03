package com.vergilyn.demo.servlet.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.NumberUtils;


/**
 * @WebFilter：将一个实现了javax.servlet.Filter接口的类定义为过滤器
 * SpingBootApplication加入@ServletComponentScan扫描注入webFilter。
 */
@WebFilter(filterName="customFilter",urlPatterns="/*")
public class CustomFilter implements Filter {

	@Override
	public void destroy() {
		System.out.println("CustomFilter >>>> destroy().");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
		System.out.println("CustomFilter >>>> doFilter(...).");
		req = new ParameterRequestWrapper((HttpServletRequest)req, filterRule(req));
		chain.doFilter(req, resp);
	}
	/**
	 * 自定义：过滤器逻辑方法
	 * @param req
	 * @return
	 */
	private Map<String, String[]> filterRule(ServletRequest req){
		Map<String, String[]> map = new HashMap<String, String[]>(req.getParameterMap());
		Set<Entry<String, String[]>> entrySet = map.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				if(NumberUtils.isNumber(values[i])){
					values[i] = NumberUtils.toInt(values[i]) * 10 + "";
				}
				else values[i] += "_filter";
			}
			entry.setValue(values);
		}
		return map;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("CustomFilter >>>> init(...).");
	}

}
