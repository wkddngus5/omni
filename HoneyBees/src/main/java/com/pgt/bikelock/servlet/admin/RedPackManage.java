/**
 * FileName:     RedPackageManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 上午10:17:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.dao.IBikeDao;
import com.pgt.bikelock.dao.IRedPackRuleDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.impl.AreaDaoImpl;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.CouponDaoImpl;
import com.pgt.bikelock.dao.impl.RedPackBikeDaoImpl;
import com.pgt.bikelock.dao.impl.RedPackRuleDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.servlet.admin.BaseManage.DelType;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.AreaVo;
import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.RedPackBikeVo;
import com.pgt.bikelock.vo.admin.RedPackRuleVo;

/**
 * 接口定义起点70000/interface definition startpoint 70000
 * @ClassName:     RedPackageManage
 * @Description:红包相关业务接口/red envelope related business interface
 * @author:    Albert
 * @date:        2017年4月26日 上午10:17:55
 *
 */
public class RedPackManage extends BaseManage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);


		setModelParams(getCurrentLangValue("redpack_rule_title"), "redpack_rule_list");
		switch (getRequestType(req)) {
		case 70001:
			getRuleList(req, resp);
			break;
		case 70002:
			getRuleListLookup(req, resp);
			break;
		case 70003:
			toAddRule(req, resp);
			break;
		case 70004:
			toUpdateRule(req, resp);
			break;
		case 70006:
			getBikeList(req, resp);
			break;
		case 70008:
			toAddBike(req, resp);
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
		setModelParams(getCurrentLangValue("redpack_rule_title"), "redpack_rule_list");
		switch (getRequestType(req)) {
		case 70001:
			getRuleList(req, resp);
			break;
		case 70002:
			getRuleListLookup(req, resp);
			break;
		case 70003:
			addRule(req, resp);
			break;
		case 70004:
			updateRule(req, resp);
			break;
		case 70005:
			deleteRule(req, resp);
			break;
		case 70006:
			getBikeList(req, resp);
			break;
		case 70007:
			deleteBike(req, resp);
			break;
		case 70008:
			addBike(req, resp);
			break;
		}
	}

	/**
	 * 70001
	 * @Title:        getRuleList 
	 * @Description:  获取红包规则列表/get red envelope rules item
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 上午10:21:04
	 */
	protected void getRuleList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = doGetRuleList(req, resp);
		returnDataList(req, resp, requestVo,"redpack/ruleList.jsp");
	}

	/**
	 * 70002
	 * @Title:        getRuleListLookup 
	 * @Description:  获取红包规则列表并返回/get red envelop rule list and return
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 下午6:27:55
	 */
	protected void getRuleListLookup(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = doGetRuleList(req, resp);
		req.setAttribute("only", true);
		returnDataList(req, resp, requestVo,"redpack/ruleListLookup.jsp");
	}

	protected RequestListVo doGetRuleList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		List<RedPackRuleVo> ruleList = new RedPackRuleDaoImpl().getRuleList(requestVo);
		req.setAttribute("ruleList",ruleList);
		requestVo.setTotalCount(BaseDao.getCount(IRedPackRuleDao.VIEW_RULE_NAME, requestVo,new String[]{}));

		return requestVo;
	}

	/**
	 * 70003
	 * get
	 * @Title:        toAddRule 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 上午10:53:46
	 */
	protected void toAddRule(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RedPackRuleVo ruleVo = new RedPackRuleVo();
		//默认信息加载/default information loading
		
		if(OthersSource.getSourceString("default_redpack_rule_type") != null){
			ruleVo.setType(ValueUtil.getInt(OthersSource.getSourceString("default_redpack_rule_type")));
		}

		CouponVo systemCouponVo = CouponBo.getSystemCoupon();
		if(systemCouponVo != null){
			ruleVo.setCoupon_id(systemCouponVo.getId());
			ruleVo.setCouponName(systemCouponVo.getName());
		}
		
		if(OthersSource.getSourceString("redpack_bike_coupon_num") != null){
			ruleVo.setCoupon_num(ValueUtil.getInt(OthersSource.getSourceString("redpack_bike_coupon_num")));
		}
		
		req.setAttribute("ruleVo", ruleVo);
		req.getRequestDispatcher("redpack/addRule.jsp").forward(req, resp);
	}

	/**
	 * 70003
	 * post
	 * @Title:        addRule 
	 * @Description:  添加规则/add rule
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 上午10:17:18
	 */
	protected void addRule(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"start_time","end_time"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		RedPackRuleVo ruleVo = new RedPackRuleVo(req);
		if(ruleVo.getType() == 1){
			//现金红包规则/in cash red envolope rule
			if(StringUtils.isEmpty(ruleVo.getFree_use_time()) || StringUtils.isEmpty(ruleVo.getLeast_use_time())
					||ruleVo.getMax_amount().compareTo(new BigDecimal(0)) <= 0){
				returnParamsError(resp);
				return;
			}
		}else if(ruleVo.getType() == 2){
			//优惠券红包规则/coupon red envelopes rule
			if(StringUtils.isEmpty(ruleVo.getCoupon_id())||ruleVo.getCoupon_num() <= 0){
				returnParamsError(resp);
				return;
			}
		}
		String ruleId = new RedPackRuleDaoImpl().addRule(ruleVo);
		returnData(resp, DelType.DelType_Add,ruleId == null?false:true);
		
		addLogForAddData(req, resp, ruleId);
	}

	/**
	 * 70004
	 * get
	 * @Title:        toUpdateRule 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 上午11:01:42
	 */
	protected void toUpdateRule(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		RedPackRuleVo ruleVo = new RedPackRuleDaoImpl().getRule(req.getParameter(parms[0]));
		if(!StringUtils.isEmpty(ruleVo.getArea_ids())){
			List<AreaVo> list = new AreaDaoImpl().getAreaList(ruleVo.getArea_ids(),0,0,0);
			String areaName = "";
			for (int i = 0; i < list.size(); i++) {
				if(i != list.size()-1){
					areaName += list.get(i).getName()+",";
				}else{
					areaName += list.get(i).getName();
				}
			}

			ruleVo.setAreaName(areaName);
		}
		req.setAttribute("ruleVo", ruleVo);
		req.getRequestDispatcher("redpack/addRule.jsp").forward(req, resp);
	}

	/**
	 * 70004
	 * post
	 * @Title:        updateRule 
	 * @Description:  修改规则/modify rule
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 上午10:17:48
	 */
	protected void updateRule(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"start_time","end_time"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		RedPackRuleVo ruleVo = new RedPackRuleVo(req);
		if(ruleVo.getType() == 1){
			//现金红包规则/cash red envelope rule
			if(StringUtils.isEmpty(ruleVo.getFree_use_time()) || StringUtils.isEmpty(ruleVo.getLeast_use_time())
					||ruleVo.getMax_amount().compareTo(new BigDecimal(0)) <= 0){
				returnParamsError(resp);
				return;
			}
		}else if(ruleVo.getType() == 2){
			//优惠券红包规则/discount red envelope rule
			if(StringUtils.isEmpty(ruleVo.getCoupon_id())||ruleVo.getCoupon_num() <= 0){
				returnParamsError(resp);
				return;
			}
		}
		boolean flag = new RedPackRuleDaoImpl().updateRule(ruleVo);
		returnData(resp, DelType.DelType_Update,flag);
	}

	/**
	 * 70005
	 * @Title:        deleteRule 
	 * @Description:  删除规则/delete rule
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 上午10:19:34
	 */
	protected void deleteRule(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"rule_id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new RedPackRuleDaoImpl().deleteRule(req.getParameter(parms[0]));
		returnData(resp, DelType.DelType_Delete,flag);
	}

	/**
	 * 70006
	 * @Title:        getBikeList 
	 * @Description:  红包单车列表/red envelope bike list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 上午11:56:27
	 */
	protected void getBikeList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		List<RedPackBikeVo> ruleList =  new RedPackBikeDaoImpl().getRedPackBikeList(requestVo);
				req.setAttribute("bikeList",ruleList);
		requestVo.setTotalCount(BaseDao.getCount(IRedPackRuleDao.VIEW_NAME, requestVo,
				new String[]{IUserDao.CLOUM_PHONE,IBikeDao.COLUMN_NUMBER}));
		setModelParams(getCurrentLangValue("redpack_bike_title"), "redpack_bike_title");
		returnDataList(req, resp, requestVo,"redpack/bikeList.jsp");
	}
	
	/**
	 * 70007
	 * @Title:        deleteBike 
	 * @Description:  删除红包单车/delete red envelope bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 下午12:07:14
	 */
	protected void deleteBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new RedPackBikeDaoImpl().deleteRedPackBike(req.getParameter(parms[0]));
		setModelParams(getCurrentLangValue("redpack_bike_title"), "redpack_bike_list");
		returnData(resp, DelType.DelType_Delete,flag);
	}
	
	/**
	 * 70008
	 * get
	 * @Title:        toAddBike 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 下午3:59:00
	 */
	protected void toAddBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(req.getParameter("fromExternal") != null){
			req.setAttribute("jumpUrl", "redpackManage?requestType=70008&isNavTab=1&funcId=82&id="
					+req.getParameter("id")+"&number="+req.getParameter("number"));
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		}else{
			if(ValueUtil.getInt(req.getParameter("isNavTab")) == 1){
				req.setAttribute("isNavTab", 1);
			}
			req.getRequestDispatcher("redpack/addBike.jsp").forward(req, resp);
		}
		
	}
	
	/**
	 * 70008
	 * post
	 * @Title:        addBike 
	 * @Description:  添加红包单车/add red envelope bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 下午3:59:31
	 */
	protected void addBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"bikeVo.id","ruleVo.id","ruleVo.start","ruleVo.end"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String bikeIds[] = req.getParameter(parms[0]).split(",");
		String ids = new RedPackBikeDaoImpl().addRedPackBike(bikeIds,new RedPackBikeVo(parms, req));
		setModelParams(getCurrentLangValue("redpack_bike_title"), "redpack_bike_list");
		boolean flag = ids == null?false:true;
		if(ValueUtil.getInt(req.getParameter("isNavTab")) == 1){
			returnDataJustTip(resp, flag, getReturnMessage(DelType.DelType_Add, flag));
		}else{
			returnData(resp, DelType.DelType_Add, flag);
		}
		if(ids != null){
			addLogForAddData(req, resp, ids);
		}
	}
}
