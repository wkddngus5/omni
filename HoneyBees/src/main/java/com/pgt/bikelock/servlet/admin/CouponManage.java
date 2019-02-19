/**
 * FileName:     CouponManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月8日 下午2:59:47
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月8日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.dao.ICouponDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.CouponDaoImpl;
import com.pgt.bikelock.dao.impl.UserCouponDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserCouponVo;


/**
 * 接口定义起点 50000/interface definition starpoint
 * @ClassName:     CouponManage
 * @Description:优惠券管理相关业务接口/coupon manage relate business interface
 * @author:    Albert
 * @date:        2017年4月8日 下午2:59:47
 *
 */
public class CouponManage extends BaseManage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);

		setModelParams(getCurrentLangValue("coupon_title"), "coupon_list");
		switch (getRequestType(req)) {
		case 50001:
			getCouponList(req, resp);
			break;
		case 50009:
			getCouponListLookup(req, resp);
			break;
		case 50002:
			toAddCoupon(req, resp);
			break;
		case 50003:
			toEditCoupon(req, resp);
			break;
		case 50005:
			setModelParams(getCurrentLangValue("coupon_manage_user_coupon_list"), "user_coupon_list");
			getUserCouponList(req, resp);
			break;
		case 50006:
			toAddCouponForUser(req, resp);
			break;
		case 50007:
			toUpdateUserCoupon(req, resp);
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
		setModelParams(getCurrentLangValue("coupon_title"), "coupon_list");
		switch (getRequestType(req)) {
		case 50001:
			getCouponList(req, resp);
			break;
		case 50009:
			getCouponListLookup(req, resp);
			break;
		case 50002:
			addCoupon(req, resp);
			break;
		case 50003:
			editCoupon(req, resp);
			break;
		case 50004:
			deleteCoupon(req, resp);
			break;
		case 50005:
			getUserCouponList(req, resp);
			break;
		case 50006:
			setModelParams(getCurrentLangValue("coupon_manage_user_coupon_list"), "user_coupon_list");
			addCouponForUsers(req, resp);
			break;
		case 50007:
			setModelParams(getCurrentLangValue("coupon_manage_user_coupon_list"), "user_coupon_list");
			updateUserCoupon(req, resp);
			break;
		case 50008:
			setModelParams(getCurrentLangValue("coupon_manage_user_coupon_list"), "user_coupon_list");
			deleteUserCoupon(req, resp);
			break;
		}
	}

	/**
	 * 50001
	 * @Title:        getCouponList 
	 * @Description:  获取优惠券列表/obtain coupon list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:08:55
	 */
	protected void getCouponList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		RequestListVo requestVo = doGetCouponList(req, resp);

		returnDataList(req, resp, requestVo,"coupon/couponList.jsp");

	}
	/**
	 * 50009
	 * @Title:        getCouponListLookup 
	 * @Description:  查询优惠券并返回/search coupon and return
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午3:43:03
	 */
	protected void getCouponListLookup(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = doGetCouponList(req, resp);
		boolean only = false;
		if(req.getParameter("only") != null){
			only = true;
		}
		req.setAttribute("only", only);
		setModelParams(getCurrentLangValue("coupon_title"), "coupon_list_look_up");
		returnDataList(req, resp, requestVo,"coupon/couponListLookup.jsp");
	}
	
	protected RequestListVo doGetCouponList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		ICouponDao couponDao = new CouponDaoImpl();
		if(req.getParameter("only") != null){
			requestVo.setType(1);
		}
		List<CouponVo> couponList = couponDao.getCouponList(requestVo);
		req.setAttribute("couponList",couponList);
		requestVo.setTotalCount(couponDao.getCount(requestVo));
		
		return requestVo;
	}
	
	/**
	 * 50002
	 * get
	 * @Title:        toAddCoupon 
	 * @Description:  跳转添加优惠券/jump add coupon
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:26:13
	 */
	protected void toAddCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("nowTime", TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm));
		req.getRequestDispatcher("coupon/addDialog.jsp").forward(req, resp);
	}

	/**
	 * 50002
	 * post
	 * @Title:        addCoupon 
	 * @Description:  执行添加优惠券/carry ut add coupon
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:27:10
	 */
	protected void addCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		
		String[] parms = new String[]{"name","typeId","start","end","cityId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String couponId = new CouponDaoImpl().addCoupon(new CouponVo(parms, req));
		returnData(resp, DelType.DelType_Add,couponId == null?false:true);
		addLogForAddData(req, resp, couponId);

	}

	/**
	 * 50003
	 * get
	 * @Title:        toEditCoupon 
	 * @Description:  跳转修改优惠券/jump modify coupon
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:40:14
	 */
	protected void toEditCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		ICouponDao couponDao = new CouponDaoImpl();
		CouponVo couponVo = couponDao.getCouponInfo(req.getParameter(parms[0]),true);
		if(couponVo == null){
			return;
		}
		if(couponVo.getType() == 3 && couponVo.getDay() == 0){
			returnParamsError(resp);
			return;
		}
		req.setAttribute("tagCityId", couponVo.getCityId());
		req.setAttribute("couponVo", couponVo);
		req.getRequestDispatcher("coupon/editDialog.jsp").forward(req, resp);
	}

	/**
	 * 50003
	 * post
	 * @Title:        editCoupon 
	 * @Description:  修改优惠券/modify coupon
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:40:43
	 */
	protected void editCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		
		String[] parms = new String[]{"name","typeId","start","end","cityId","id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		CouponVo coupon = new CouponVo(parms, req);
		if(coupon.getType() == 3 && coupon.getDay() == 0){
			returnParamsError(resp);
			return;
		}
		coupon.setId(req.getParameter(parms[5]));
		boolean flag = new CouponDaoImpl().updateCoupon(coupon);
		returnData(resp, DelType.DelType_Update,flag);
	}

	/**
	 * 50004
	 * @Title:        deleteCoupon 
	 * @Description:  删除优惠券/delete coupon
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:50:49
	 */
	protected void deleteCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new CouponDaoImpl().deleteCoupon(req.getParameter(parms[0]));
		returnData(resp, DelType.DelType_Delete,flag);
	}

	/**
	 * 50005
	 * @Title:        getUserCouponList 
	 * @Description:  用户优惠券列表/user coupon item
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午5:06:31
	 */
	protected void getUserCouponList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		List<UserCouponVo> couponList = new UserCouponDaoImpl().getUserCouponList(requestVo);
		req.setAttribute("couponList",couponList);
		requestVo.setTotalCount(BaseDao.getCount(ICouponDao.VIEW_USER_COUPON_ALL, requestVo, new String[]{ICouponDao.CLOUM_NAME,IUserDao.CLOUM_PHONE}));

		returnDataList(req, resp, requestVo,"coupon/userCouponList.jsp");

	}

	/**
	 * 50006
	 * get
	 * @Title:        toAddCouponForUser 
	 * @Description:  跳转至 为用户添加优惠券页/jump add coupon for user
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 上午10:32:59
	 */
	protected void toAddCouponForUser(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("type", req.getParameter("type"));
		req.setAttribute("nowTime", TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm));
		req.getRequestDispatcher("coupon/addUserCouponDialog.jsp").forward(req, resp);
		
	}

	/**
	 * 50006
	 * post
	 * @Title:        addCouponForUsers 
	 * @Description:  为用户添加优惠券/add coupon for user
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 上午10:31:57
	 */
	protected void addCouponForUsers(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		
		String[] parms = new String[]{"couponVo.id","couponVo.start","couponVo.end","couponVo.value","type","couponVo.type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		String couponId = req.getParameter(parms[0]);
		String couponStart = req.getParameter(parms[1]);
		String couponEnd = req.getParameter(parms[2]);
		String couponValue = req.getParameter(parms[3]);
		
		int type = ValueUtil.getInt(req.getParameter(parms[4]));
		List<UserCouponVo> list = new ArrayList<UserCouponVo>();
		String cId = null;
		if(type == 0){//add coupon for user
			String userIds[] = req.getParameter("userVo.id").split(",");
			for (int i = 0; i < userIds.length; i++) {
				UserCouponVo coupon = new UserCouponVo( couponId,couponStart,couponEnd,couponValue);
				coupon.setUid(userIds[i]);
				list.add(coupon);
			}
			
			cId = new UserCouponDaoImpl().addCouponForUsers(list,
					"1".equals(OthersSource.getSourceString("add_user_coupon_not_active"))?false:true);
			if(cId != null){
				for (int i = 0; i < userIds.length; i++) {
					CouponBo.couponNotify(1, couponId, userIds[i], req.getParameter("couponVo.name"));
				}
			}
			

		}else if(type == 1){//add random coupon
			int addCount = ValueUtil.getInt(req.getParameter("count"));
			for (int i = 0; i < addCount; i++) {
				UserCouponVo coupon = new UserCouponVo( couponId,couponStart,couponEnd,couponValue);
				list.add(coupon);
			}
			String code = null;
			if(ValueUtil.getInt(req.getParameter("sameCode")) == 1){
				code = ValueUtil.getStringRandom(7);
			}
			cId = new UserCouponDaoImpl().addCouponsNotActive(list,code);
		}
		returnData(resp, DelType.DelType_Add,cId!=null?true:false);
		addLogForAddData(req, resp, cId);
	}

	/**
	 * 50007
	 * @Title:        toUpdateUserCoupon 
	 * @Description:  跳转至修改优惠券页面/jump coupon page to modify
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午4:27:41
	 */
	protected void toUpdateUserCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		UserCouponVo couponVo = new UserCouponDaoImpl().getUserCoupon(req.getParameter(parms[0]));
		if(couponVo == null){
			return;
		}
		req.setAttribute("couponVo", couponVo);
		req.getRequestDispatcher("coupon/editUserCouponDialog.jsp").forward(req, resp);
	}
	
	/**
	 * 50007
	 * @Title:        updateUserCoupon 
	 * @Description:  更新用户优惠券信息/update user coupon information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 上午10:32:13
	 */
	protected void updateUserCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		
		String[] parms = new String[]{"id","userVo.id","couponVo.id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		boolean flag = new UserCouponDaoImpl().updateUserCoupon(new UserCouponVo(parms,req));
		returnData(resp, DelType.DelType_Update,flag?true:false);
	}

	/**
	 * 50008
	 * @Title:        deleteUserCoupon 
	 * @Description:  删除用户优惠券信息/delete user coupon information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 上午10:32:26
	 */
	protected void deleteUserCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new UserCouponDaoImpl().deleteUserCoupon(req.getParameter(parms[0]));
		returnData(resp, DelType.DelType_Delete,flag?true:false);
	}
	
	//下一接口号50010/next protocol

}
