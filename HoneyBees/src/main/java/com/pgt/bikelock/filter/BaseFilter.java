/**
 * FileName:     BaseFilter.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月14日 下午3:57:26
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月14日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

 /**
 * @ClassName:     BaseFilter
 * @Description:拦截器基类/Interceptor base class
 * @author:    Albert
 * @date:        2017年4月14日 下午3:57:26
 *
 */
public class BaseFilter implements Filter{
	
	private String charSet;
	private String contentType;

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding(charSet);
		response.setCharacterEncoding(charSet);
		response.setContentType(contentType);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		charSet=config.getInitParameter("charset");
        contentType=config.getInitParameter("contentType");
	}

}
