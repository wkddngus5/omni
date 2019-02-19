/**
 * FileName:     CityManage.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月28日 下午3:19:39
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月28日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.pgt.bikelock.dao.impl.BikeTypeDaoImpl;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.CityVo;

 /**
  * 接口定义起点: 80000/interface definition start point
 * @ClassName:     CityManage
 * @Description:城市管理/city manage
 * @author:    Albert
 * @date:        2017年6月28日 下午3:19:39
 *
 */
public class CityManage extends BaseManage {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		switch (getRequestType(req)) {
		case 80001:
			changeCurrentCity(req, resp);
			break;
		case 80002:
			getCityPriceList(req, resp);
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		
	}
	
	/**
	 * 80001
	 * @Title:        changeCurrentCity 
	 * @Description:  改变当前城市/modify current city
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月28日 下午3:22:36
	 */
	protected void changeCurrentCity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"cityId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		HttpSession session = req.getSession();
		session.setAttribute("cityId", req.getParameter(parms[0]));
		req.setAttribute("functionList", session.getAttribute("functionList"));
		req.setAttribute("cityList",session.getAttribute("cityList"));
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
	
	/**
	 * 80002
	 * @Title:        getCityPriceList 
	 * @Description:  获取城市价格列表/get city price list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年6月1日 下午2:45:51
	 */
	protected void getCityPriceList(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String[] parms = new String[]{"cityId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		List<BikeTypeVo> typeList = new BikeTypeDaoImpl().getTypeList(getIndustryId(req, resp), ValueUtil.getInt(req.getParameter(parms[0])));
		returnAjaxData(resp, typeList);
	}
}
