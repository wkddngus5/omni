/**
 * FileName:     ICouponDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 下午3:28:54
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     ICouponDao
 * @Description:优惠券接口定义/coupon interface definition
 * @author:    Albert
 * @date:        2017年4月6日 下午3:28:54
 *
 */
public interface ICouponDao {
	
	String TABLE_NAME="t_coupon";
	String CLOUM_NAME = "name";
	String VIEW_USER_COUPON_ALL = "view_user_coupon_all";
	
	/**
	 * 
	 * @Title:        getCouponList 
	 * @Description:  优惠券列表获取/coupon list obtain
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<CouponVo>    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:02:21
	 */
	List<CouponVo> getCouponList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getCount 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年10月18日 下午2:15:24
	 */
	int getCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        addCoupon 
	 * @Description:  添加优惠券/add coupon
	 * @param:        @param couponVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:31:12
	 */
	String addCoupon(CouponVo couponVo);
	
	/**
	 * 
	 * @Title:        getCouponInfo 
	 * @Description:  获取详情/obtain details
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       CouponVo    
	 * @author        Albert
	 * @Date          2017年4月8日 下午4:15:13
	 */
	CouponVo getCouponInfo(String id,boolean formateDate);
	
	
	/**
	 * 
	 * @Title:        getRegisterCouponInfo 
	 * @Description:  获取注册优惠券/obatin register coupon
	 * @param:        @return    
	 * @return:       CouponVo    
	 * @author        Albert
	 * @Date          2017年5月17日 下午3:59:12
	 */
	CouponVo getFreeCouponInfo(int type,int cityId);
	

	/**
	 * 
	 * @Title:        updateCoupon 
	 * @Description:  修改优惠券信息/modify coupoon information
	 * @param:        @param couponVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:35:26
	 */
	boolean updateCoupon(CouponVo couponVo);
	
	/**
	 * 
	 * @Title:        deleteCoupon 
	 * @Description:  删除优惠券/delete coupon
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月8日 下午3:35:41
	 */
	boolean deleteCoupon(String id);
	
	
}
