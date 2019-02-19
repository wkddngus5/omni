/**
 * FileName:     LoginFilter.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:29:27
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alipay.api.internal.util.StringUtils;
import com.omni.purview.dao.impl.FunctionDaoImpl;
import com.omni.purview.dao.impl.GroupFunctionDaoImpl;
import com.omni.purview.dao.impl.UserLogDaoImpl;
import com.omni.purview.vo.FunctionVo;
import com.omni.purview.vo.UserLogVo;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

/**
 * @ClassName:     LoginFilter
 * @Description:对外接口登录信息拦截/outside interface log in information interception
 * @author:    Albert
 * @date:        2017-3-24 上午10:29:27
 *
 */
public class AdminLoginFilter extends BaseFilter {

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
		super.doFilter(request, response, chain);
		HttpServletRequest httpRequest=(HttpServletRequest)request;
		HttpServletResponse httpResponse=(HttpServletResponse)response;
		HttpSession session=httpRequest.getSession(true);
		setLanguage(request, session);//语言设置/language set up
		if(httpRequest.getServletPath().contains("/admin/login") || httpRequest.getServletPath().contains("admin/themes")){
			chain.doFilter(request, response);
			return;
		}

		String islogin = (String) session.getAttribute("isLogin");
		
		if("true".equals(islogin)){
			
			if(purviewControl(request, response)){
				if(StringUtils.isEmpty(request.getParameter("cityId"))){
					request.setAttribute("cityId", session.getAttribute("cityId"));
				}else{
					request.setAttribute("cityId", request.getParameter("cityId"));
				}
				
				chain.doFilter(request, response);
			}else{
				PrintWriter out = response.getWriter();
				out.write("对不起，您无权限使用该功能；若有需要，请联系管理员。");
				out.flush();
				out.close();
			}
			
		}else{
			//				httpResponse.sendRedirect("login.html");
			httpResponse.sendRedirect(httpRequest.getContextPath()+"/admin/login.jsp");
		}

	}
	
	
	
	/**
	 * 
	 * @Title:        setLanguage 
	 * @Description:  语言设置/language set up
	 * @param:        @param httpRequest
	 * @param:        @param session    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月17日 上午11:58:02
	 */
	public static void setLanguage(ServletRequest httpRequest,HttpSession session){
		//设置默认语言包/set up default language pack
		Locale locale = Locale.getDefault();  
		String lang = httpRequest.getParameter("lang");
		if(lang != null && !lang.equals(session.getAttribute("lang"))){//重设语言/renew set up language
			locale = LanguageUtil.getLocale(lang);
			session.setAttribute("lang", lang);
			//语言包切换/language pack switch
			ResourceBundle rb = ResourceBundle.getBundle(LanguageUtil.Resource_Package, locale);
			session.setAttribute("languageRD", rb);
			session.setAttribute("currentLang", httpRequest.getParameter("currentLang"));
			
			//功能语言切换/function language switch
			List<FunctionVo> functionList = null;
			String adminName = (String) session.getAttribute("userName");
			if("admin".equals(adminName)||"omni".equals(adminName)){
				//系统账号，获取所有权限功能/system account, obtain all right funtion
				functionList = new FunctionDaoImpl().getFunctionList(false, null,rb);
				session.setAttribute("admin", 1);
			}else{
				String adminGroup = (String) session.getAttribute("userGroup");
				functionList = new GroupFunctionDaoImpl().getGroupsFunctionList(adminGroup,rb);
			}
			httpRequest.setAttribute("functionList", functionList);
			session.setAttribute("functionList",functionList);
		}else if(session.getAttribute("languageRD") == null){//默认语言/default language
			lang = OthersSource.getSourceString("default_language");
			locale = LanguageUtil.getLocale(lang);
			session.setAttribute("lang", lang);
			ResourceBundle rb = ResourceBundle.getBundle(LanguageUtil.Resource_Package, locale);
			session.setAttribute("languageRD", rb);
			session.setAttribute("currentLang", 
					ValueUtil.getInt(OthersSource.getSourceString("default_language_index")));
		}
		
		
	}

	
	/**
	 * 
	 * @Title:        purviewControl 
	 * @Description:  权限控制/right control
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @throws IOException 
	 * @Date          2017年5月31日 下午5:53:27
	 */
	public boolean  purviewControl(ServletRequest request,ServletResponse response) throws IOException{
		
		HttpServletRequest httpRequest=(HttpServletRequest)request;
		HttpSession session=httpRequest.getSession(false);
		String adminName = (String) session.getAttribute("userName");
		if(session!=null){
			//详情功能/detail funtion
			if(request.getParameter("funcId") != null){
				int funcId =ValueUtil.getInt(request.getParameter("funcId"));
				if(funcId > 0){
					String adminId = (String)session.getAttribute("adminId");
					String dataId = request.getParameter("id");
					if(StringUtils.isEmpty(dataId) || "{id_item}".equals(dataId)){
						dataId = request.getParameter("ids");
					}
					List<FunctionVo> subFunList = null;
					
					ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
					
					if("admin".equals(adminName)||"omni".equals(adminName)){
						//系统账号，获取所有权限功能/system account, obtain all right function
						subFunList = new FunctionDaoImpl().getSubFunctionList(funcId,rb);
						if(!StringUtils.isEmpty(dataId)){
							//增加操作日志(操作数据时)/add operate log(when operate data)
							new UserLogDaoImpl().addUserLog(new UserLogVo(adminId, funcId,dataId ));
						}
					}else{
						String adminGroup = (String) session.getAttribute("userGroup");
						//验证权限，同时增加操作日志(操作数据时)/verify right, add operate log meanwhile(when operate data)
						if(!new GroupFunctionDaoImpl().checkGroupsFunction(adminGroup, funcId,
								request.getParameter("id"),adminId)){
							return false;
						}						
						subFunList = new GroupFunctionDaoImpl().getThirdGroupsFunctionList(funcId, adminGroup,rb);
						if(!StringUtils.isEmpty(dataId)){
							//增加操作日志(操作数据时)/add operate log(operate data)
							new UserLogDaoImpl().addUserLog(new UserLogVo(adminId, funcId,dataId ));
						}
					}
					request.setAttribute("funcId", request.getParameter("funcId"));
					request.setAttribute("subFunList", subFunList);
				}
			}
		}
		
		
		return true;
	}

	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}



}
