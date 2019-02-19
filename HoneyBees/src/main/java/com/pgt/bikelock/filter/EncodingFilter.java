package com.pgt.bikelock.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter extends BaseFilter {
	
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		super.doFilter(request, response, chain);
		
		chain.doFilter(request, response);

	}

	
	public void init(FilterConfig config) throws ServletException {
		super.init(config);

	}
	
	
	
	
	public void destroy() {

	}

}
