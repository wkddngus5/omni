/**
 * FileName:     ParamsFilter.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 下午4:50:44
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialzization
 */
package com.pgt.bikelock.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.IpWarnDaoImpl;
import com.pgt.bikelock.resource.EnvironmentConfig;
import com.pgt.bikelock.servlet.BaseServlet;
import com.pgt.bikelock.servlet.UserServlet;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.RequestClientVo;
import com.pgt.bikelock.vo.RequestTokenVo;
/**
 * @ClassName:     ParamsFilter
 * @Description:参数拦截器/parameter interceptor
 * @author:    Albert
 * @date:        2017-3-24 下午4:50:44
 *
 */
public class ParamsFilter extends BaseFilter{

	/**安全拦截/Firewall**/
	public static Map<String, RequestClientVo> clientMap = new HashMap<String, RequestClientVo>();

	public static Map<String, RequestTokenVo> tokenMap = new HashMap<String, RequestTokenVo>();

	/**状态码**/
	public static final int HTTP_RESULT_TOKEN_ERROR = 101; // TOKEN错误/token error
	public static final int HTTP_RESULT_TOKEN_EXPIRED = 102; // TOKEN已过期/token expired
	public static final int HTTP_RESULT_DEVICE_ERROR = 103; //设备类型错误 /Device type error
	public static final int HTTP_RESULT_ACCOUNT_OTHER_DEVICE_LOGIN = 104; //账号异地登陆/Account Others Device login
	public static final int HTTP_RESULT_TOKEN_NEED_REFRESH = 105; // TOKEN待刷新/token need to refresh

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

		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {

			HttpServletRequest httpRequest = (HttpServletRequest) request;

			if (!checkTokenParam(httpRequest, httpResponse)) {
				return;
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			// TODO: handle exception
			setCode(httpResponse, BaseServlet.ERROR_EXCEPTION);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title:        checkUserAgent 
	 * @Description:  TODO
	 * @param:        @param userAgent
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年12月23日 下午2:12:49
	 */
	public static boolean checkUserAgent(String userAgent){
		if(userAgent.indexOf("Android") < 0 && userAgent.indexOf("okhttp") < 0
				&& userAgent.indexOf("iPhone") < 0 && userAgent.indexOf("iPad") < 0
				&& userAgent.indexOf("wechatdevtools") < 0){
			return false;
		}
		return true;
	}

	public boolean checkTokenParam(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		int requestType = ValueUtil.getInt(req.getParameter("requestType"));

		if(requestType == 0){
			setCode(resp,BaseServlet.HTTP_RESULT_PARAMETER_MISS);
			return false;
		}
		if(!EnvironmentConfig.DEBUG){
			String userAgent = req.getHeader("user-agent");
			System.out.println("requestType:"+requestType+";"+userAgent);
			if(!checkUserAgent(userAgent)){
				System.out.println("error request,client:"+req.getRemoteAddr());
				setCode(resp, HTTP_RESULT_DEVICE_ERROR);
				return false;
			}
		}

		//登录注册等接口直接返回/log in register interface direct return
		if(requestType < 20000 || requestType == 30001){
			return checkClient(req, resp, requestType,"0");
		}

		String token = req.getParameter("token");
		if(token == null){
			setCode(resp,BaseServlet.HTTP_RESULT_PARAMETER_MISS);
			return false;
		}

		UserBo loginBo = new UserBo();

		RequestTokenVo tokenVo;

		if(tokenMap.containsKey(token)){//exist request
			tokenVo = tokenMap.get(token);
		}else{
			tokenVo = loginBo.analyTokenMap(token,req);
		}

		int tokenStatus = tokenVo.getTokenStatus();
		switch (tokenStatus) {
		case 1:
			//token success"
			req.setAttribute("userId", tokenVo.getUserId());
			req.setAttribute("currency", tokenVo.getCurrency());
			req.setAttribute("industryId", tokenVo.getIndustryId());
			req.setAttribute("cityId", tokenVo.getCityId());

			if(!tokenMap.containsKey(token)){
				//save token info
				tokenMap.put(token, tokenVo);
			}

			return checkClient(req, resp, requestType,tokenVo.getUserId());
		case 0:
			//token expired
			setCode(resp,HTTP_RESULT_TOKEN_EXPIRED);
			break;
		case 2:
			setCode(resp, HTTP_RESULT_ACCOUNT_OTHER_DEVICE_LOGIN);
			//others login
			break;
		case 3:
			//stop
			setCode(resp, UserServlet.HTTP_RESULT_USER_BE_FROST);
			break;
		case 4:
			if(requestType == 20026){
				req.setAttribute("userId", tokenVo.getUserId());
				return true;
			}else{
				setCode(resp, HTTP_RESULT_TOKEN_NEED_REFRESH);
			}			
			break;
		default:
			//token invalid
			setCode(resp,HTTP_RESULT_TOKEN_ERROR);
			break;
		}


		return false;
	}



	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);

	}


	/**
	 * 
	 * @Title:        checkClient 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param phone
	 * @param:        @return
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月7日 下午6:07:28
	 */
	protected boolean checkClient(HttpServletRequest req, HttpServletResponse resp,int requestType,String userId)
			throws ServletException, IOException {

		if(EnvironmentConfig.DEBUG){
			return true;
		}

		String clientIp = req.getRemoteAddr();
		long nowTime = TimeUtil.getCurrentLongTime();
		if(!clientMap.containsKey(clientIp)){
			clientMap.put(clientIp, new RequestClientVo(clientIp, nowTime,requestType));
			return true;
		}
		RequestClientVo clientVo = clientMap.get(clientIp);
		if(clientVo.isStopClient()){
			System.out.println("request warn,ip:"+clientIp);
			return false;
		}
		clientVo.setCount(clientVo.getCount()+1);
		long intervals = nowTime-clientVo.getLastTime();
		System.out.println("intervals:"+intervals);
		boolean flag = false;
		if(clientVo.getRequestType() == requestType && intervals < RequestClientVo.REQUEST_INTERVAL){
			setCode(resp, BaseServlet.HTTP_RESULT_QUICK_ERROR);
		}else{
			if(nowTime-clientVo.getFirstTime() < Long.parseLong(86400+"")){//in day	
				if(clientVo.getCount() > RequestClientVo.REQUEST_MAX_REQUEST_COUNT){
					clientVo.setStopClient(true);
					//					new IpWarnDaoImpl().addWarn(clientIp,userId);
				}else{
					flag = true;
					clientVo.setLastTime(nowTime);
					clientVo.setCount(clientVo.getCount()+1);
				}

			}else{//after day
				flag = true;
				clientVo.setLastTime(nowTime);
				clientVo.setCount(0);
			}
		}


		//update client map
		clientMap.put(clientIp, clientVo);
		return flag;
	}

	/**
	 * 返回状态码
	 * @param resp
	 * @param code
	 */
	protected void setCode(HttpServletResponse resp,int code) {
		PrintWriter out;
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", "");
			dataMap.put("code", code);
			out = resp.getWriter();
			out.write(dataMap.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @Title:        setTokenStatus 
	 * @Description:  设置请求令牌状态/set token status
	 * @param:        @param token
	 * @param:        @param status  2：others login 3:stop 4:wait refresh
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月27日 上午10:54:49
	 */
	public static void setTokenStatus(String token,int status){
		if(!tokenMap.containsKey(token)){
			return;
		}
		RequestTokenVo tokenVo = tokenMap.get(token);
		tokenVo.setTokenStatus(status);
		tokenMap.put(token, tokenVo);
	}
	
	/**
	 * 
	 * @Title:        removeToken 
	 * @Description:  TODO
	 * @param:        @param token    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年2月25日 下午6:05:40
	 */
	public static void removeToken(String token){
		tokenMap.remove(token);
	}
}
