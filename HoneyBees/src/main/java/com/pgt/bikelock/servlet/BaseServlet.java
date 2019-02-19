/**
 * FileName:     BaseServlet.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:24:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeReserveVo;
import com.pgt.bikelock.vo.RequestClientVo;

 /**
 * @ClassName:     BaseServlet
 * @Description:对外业务接口基类/outside business interface base class
 * @author:    Albert
 * @date:        2017-3-24 上午10:24:57
 *
 */
public class BaseServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**状态码 status code**/ 
	public static final int HTTP_RESULT_OK = 200;//正常/normal
	public static final int HTTP_RESULT_PARAMETER_MISS = 201; // 缺少参数/lack of parameter
     public static final int HTTP_RESULT_NO_GPS = 211;
	public static final int HTTP_RESULT_DATA_NULL_ERROR = 202; // 数据不存在/data not exist
	public static final int HTTP_RESULT_DATA_EXIST_ERROR = 203; // 数据已存在/data alread exist
	public static final int HTTP_RESULT_SMS_CODE_ERROR = 204; // 短信验证码无效或已过期/short message invalid or expired
	public static final int HTTP_RESULT_COUNT_MAX_IN_DAY = 205;//单日请求最大数
	public static final int HTTP_RESULT_QUICK_ERROR = 206;//请求频繁异常
	public static final int HTTP_RESULT_DATA_OTHERS_USED_ERROR = 207;//数据已被使用/data was be used by others
	public static final int ERROR_EXCEPTION = 500; // 服务器异常/server error

	/**
	 * 获取请求类型编号/obtain request type number
	 * @param req
	 * @return
	 */
	protected int getRequestType(HttpServletRequest req) {
		System.out.println("requestType:"+req.getParameter("requestType")+";client:"+req.getRemoteAddr()
				+";agent:"+req.getHeader("user-agent"));
		if(null != req.getParameter("requestType") && !"".equals(req.getParameter("requestType").trim())){
			return Integer.parseInt(req.getParameter("requestType"));
		}
		
		return 0;
	}
	
	/**
	 * 返回数据对象/return data object
	 * @param resp
	 * @param json
	 */
	protected void setData(HttpServletResponse resp, Object json) {
		PrintWriter out;
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", json);
			dataMap.put("code", HTTP_RESULT_OK);
			out = resp.getWriter();
			out.write(dataMap.toJSONString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	protected void writeJSON (HttpServletResponse res, Object json) {
		setData(res, json);
	}
	

	/**
	 * 
	 * @Title:        setData 
	 * @Description:  返回数据对象/return data object
	 * @param:        @param resp
	 * @param:        @param json
	 * @param:        @param type    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月1日 下午2:44:47
	 */
	protected void setData(HttpServletResponse resp,Object json,int type,int position) {
		PrintWriter out;
		System.out.println("return date:"+JSON.toJSON(json)+";"+position);
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", json);
			dataMap.put("type", type);
			dataMap.put("code", HTTP_RESULT_OK);
			out = resp.getWriter();
			out.write(dataMap.toJSONString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 返回状态码/return status code
	 * @param resp
	 * @param code
	 */
	protected void setCode(HttpServletResponse resp,int code) {
		PrintWriter out;
		System.out.println("code:"+code);
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", "");
			dataMap.put("code", code);
			out = resp.getWriter();
			out.write(dataMap.toJSONString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void writeJSONCodeWithMessage (HttpServletResponse resp, int code, String message) {
		PrintWriter out;
		System.out.println("code:"+code);
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", "");
			dataMap.put("code", code);
			dataMap.put("message", message);
			out = resp.getWriter();
			out.write(dataMap.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	protected void writeJSONCode (HttpServletResponse res, int code) {
		setCode(res, code);
	}
	
	/**
	 * 
	 * @Title:        setErrorCode 
	 * @Description:  返回错误代码加异常信息/return error code plue abnormal information
	 * @param:        @param resp
	 * @param:        @param code
	 * @param:        @param message    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 下午12:11:38
	 */
	protected void setErrorCode(HttpServletResponse resp,int code,String message) {
		PrintWriter out;
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", "");
			dataMap.put("code", code);
			dataMap.put("error", message);
			out = resp.getWriter();
			out.write(dataMap.toJSONString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @Title:        setObjectData 
	 * @Description:  TODO
	 * @param:        @param dataMap
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月9日 下午6:43:07
	 */
	protected void setObjectData(HttpServletResponse resp,JSONObject dataMap) {
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.write(dataMap.toJSONString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Title:        setFlagData 
	 * @Description:  返回boolean结果/return boolen result
	 * @param:        @param resp
	 * @param:        @param flag    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 上午10:27:49
	 */
	protected void setFlagData(HttpServletResponse resp,boolean flag) {
		setData(resp, flag?"1":"0");
	}
	
	/**
	 * 返回结果/return result
	 * @param resp
	 * @param code
	 */
	protected void setResult(HttpServletResponse resp,int result) {
		setData(resp, result+"");
	}
	
	/**
	 * 返回字符串/return string
	 * @param resp
	 * @param str
	 */
	protected void setResponseStr(HttpServletResponse resp,String str) {
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.write(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @Title:        setMapWithCodeOk 
	 * @Description:  设置返回字典（扩展）,正常状态/set up return dictionary(extension), normal status
	 * @param:        @param resp
	 * @param:        @param dataMap    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月10日 下午6:16:55
	 */
	protected void setMapWithCodeOk(HttpServletResponse resp,JSONObject dataMap) {
		PrintWriter out;
		try {
			dataMap.put("code", HTTP_RESULT_OK);
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
	 * @Title:        setMap 
	 * @Description:  设置返回字典（扩展）/set up return dictionary(extension)
	 * @param:        @param resp
	 * @param:        @param dataMap    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月13日 上午10:48:13
	 */
	protected void setMap(HttpServletResponse resp,JSONObject dataMap) {
		PrintWriter out;
		try {
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
	 * 校验必填参数/varify parameter
	 * @param req
	 * @param resp
	 * @param params
	 * @return
	 */
	protected boolean checkRequstParams(HttpServletRequest req, HttpServletResponse resp,String[] params) {
		for (int i = 0; i < params.length; i++) {
			if(req.getParameter(params[i]) == null || "".equals(req.getParameter(params[i]).trim())){
				setCode(resp, HTTP_RESULT_PARAMETER_MISS);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @Title:        getUserId 
	 * @Description:  获取传入的用户ID（已在拦截器进行过滤，可直接获取）/obtain enter user id(already filter in the interceptor, can get directly
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017-3-25 下午5:44:00
	 */
	protected String getUserId(HttpServletRequest req) {
		if(StringUtils.isEmpty((String) req.getAttribute("userId"))){
			return "0";
		}
		return (String) req.getAttribute("userId");
	}
	
	/**
	 * 
	 * @Title:        getIndustryId 
	 * @Description:  获取用户当前所属产业/obtain user current industry
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月10日 上午11:25:35
	 */
	protected String getIndustryId(HttpServletRequest req) {
		return (String) req.getAttribute("industryId");
	}
	
	/**
	 * 
	 * @Title:        getCityId 
	 * @Description:  获取用户当前所在城市/obtain user current located city
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年6月28日 下午4:45:54
	 */
	protected int getCityId(HttpServletRequest req) {
		return ValueUtil.getInt(req.getAttribute("cityId"));
	}
	/**
	 * 
	 * @Title:        getStartPage 
	 * @Description:  获取起始页码/obtain start page 
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年6月7日 上午11:18:27
	 */
	protected int getStartPage(HttpServletRequest req) {
		int pageNo =  ValueUtil.getInt(req.getParameter("pageNo"));
		if(pageNo == 0){
			return 0;
		}
		return (pageNo-1)*BaseDao.pageSize;
	}

}
