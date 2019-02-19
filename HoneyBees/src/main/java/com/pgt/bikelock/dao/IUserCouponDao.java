/**
 * FileName:     IUserCouponDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午4:50:49
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.math.BigDecimal;
import java.util.List;

import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserCouponVo;

 /**
 * @ClassName:     IUserCouponDao
 * @Description:用户优惠券接口定义/user coupon interface definition
 * @author:    Albert
 * @date:        2017年4月10日 下午4:50:49
 *
 */
public interface IUserCouponDao {
	/**
	 * 
	 * @Title:        getUserValueCoupon 
	 * @Description:  匹配用户金额优惠券/match user amount coupon
	 * @param:        @param userId
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年4月6日 下午3:37:20
	 */
	UserCouponVo getUserValueCoupon(String userId,BigDecimal amount);
	
	/**
	 * 
	 * @Title:        getUserPercentCoupon 
	 * @Description:  获取用户折扣优惠券/obtain user dicount coupon
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年4月6日 下午3:48:32
	 */
	UserCouponVo getUserPercentCoupon(String userId);
	
	/**
	 * 
	 * @Title:        getCouponInfo 
	 * @Description:  获取优惠券详情/obtain coupon details
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       CouponVo    
	 * @author        Albert
	 * @Date          2017年4月6日 下午4:07:09
	 */
	CouponVo getCouponInfoForUser(String id);
	
	/**
	 * 
	 * @Title:        getUserCouponList 
	 * @Description:  获取用户优惠券列表/obtain user coupon list
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       List<UserCouponVo>    
	 * @author        Albert
	 * @Date          2017年4月6日 下午3:38:12
	 */
	List<UserCouponVo> getUserCouponList(String userId,int startPage,int showAll);
	
	
	/**
	 * 
	 * @Title:        getTypeCouponCount 
	 * @Description:  获取指定类型优惠券总数/ get Type Coupon Total Count
	 * @param:        @param userId
	 * @param:        @param giftType
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年10月16日 下午2:28:12
	 */
	int getTypeCouponCount(String userId,int giftType);
	
	/**********************后台业务*****************************/
	/**
	 * 
	 * @Title:        getUserCouponList 
	 * @Description:  获取用户优惠券列表/obtain user coupon list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<CouponVo>    
	 * @author        Albert
	 * @Date          2017年4月8日 下午4:50:10
	 */
	List<UserCouponVo> getUserCouponList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        addCouponForUser 
	 * @Description:  为用户添加优惠券(未激活)/add coupon for user
	 * @param:        @param coupon
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月10日 上午10:22:46
	 */
	boolean addCouponForUserNotActive(UserCouponVo coupon);
	
	/**
	 * 
	 * @Title:        addCouponForUserActive 
	 * @Description:  为用户添加优惠券(激活)/add coupon for user(activate)
	 * @param:        @param coupon
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月19日 下午2:51:08
	 */
	boolean addCouponForUserActive(UserCouponVo coupon);
	
	/**
	 * 
	 * @Title:        addCouponForUsers 
	 * @Description:  为多用户添加优惠券(自动激活)/add coupon for user(auto activate)
	 * @param:        @param coupons
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月10日 下午4:09:14
	 */
	String addCouponForUsers(List<UserCouponVo> coupons,boolean active);
	
	/**
	 * 
	 * @Title:        addCoupons 
	 * @Description:  添加优惠券(未激活)/add coupon(not activate)
	 * @param:        @param coupons
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月29日 上午9:19:40
	 */
	String addCouponsNotActive(List<UserCouponVo> coupons,String code);
	
	/**
	 * 
	 * @Title:        getUserCoupon 
	 * @Description:  获取用户优惠券详情/obtain user coupon details
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年4月10日 下午4:30:23
	 */
	UserCouponVo getUserCoupon(String id);
	
	/**
	 * 
	 * @Title:        getUserCouponDetail 
	 * @Description:  TODO
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年12月15日 下午6:46:14
	 */
	UserCouponVo getUserCouponDetail(String id);
	
	/**
	 * 
	 * @Title:        getNotActiveCoupon 
	 * @Description:  获取未激活优惠券(id)/obtain unactivated coupon(id)
	 * @param:        @param code
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年4月29日 上午10:43:57
	 */
	UserCouponVo getNotActiveCoupon(String id);
	
	/**
	 * 
	 * @Title:        getNotActiveCouponWithCode 
	 * @Description:  获取未激活优惠券(code)/obtain unactivated coupoen(code)
	 * @param:        @param code
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年9月12日 下午3:24:22
	 */
	UserCouponVo getNotActiveCouponWithCode(String code);
	
	/**
	 * 
	 * @Title:        checkCouponHaveBeenGet 
	 * @Description:  Same code coupon,user only can get one
	 * @param:        @param code
	 * @param:        @param uid
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年10月27日 下午3:58:35
	 */
	boolean checkCouponHaveBeenGet(String code,String uid);
	
	/**
	 * 
	 * @Title:        getRegisterCoupon 
	 * @Description:  获取注册已激活优惠券/obtain register activated coupon
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年5月17日 下午4:23:18
	 */
	UserCouponVo getRegisterActivedCoupon(String userId);
	
	/**
	 * 
	 * @Title:        getRegisterNotActiveCoupon 
	 * @Description:  获取待激活注册优惠券/obtain activate register coupon
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年5月19日 上午10:43:05
	 */
	UserCouponVo getRegisterNotActiveCoupon(String userId);
	
	
	/**
	 * 
	 * @Title:        getFreeCoupon 
	 * @Description:  免费优惠券获取/free obtain coupon
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       UserCouponVo    
	 * @author        Albert
	 * @Date          2017年5月17日 下午4:37:30
	 */
	UserCouponVo getFreeCoupon(String userId);
	
	/**
	 * 
	 * @Title:        updateUserCoupon 
	 * @Description:  更新用户优惠券信息(重新激活)/update user coupon information(renew activate)
	 * @param:        @param coupon
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月10日 上午10:24:00
	 */
	boolean updateUserCoupon(UserCouponVo coupon);
	
	/**
	 * 
	 * @Title:        deleteUserCoupon 
	 * @Description:  删除用户优惠券信息/delete user coupon information
	 * @param:        @param recordId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月10日 上午10:24:18
	 */
	boolean deleteUserCoupon(String recordId);

	
	
	/**
	 * 
	 * @Title:        activeCoupon 
	 * @Description:  激活优惠券/activate coupon
	 * @param:        @param userId
	 * @param:        @param userCouponId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月29日 上午10:31:37
	 */
	boolean activeCoupon(String userId,String userCouponId,boolean activeNow);
	
	
	/**
	 * 
	 * @Title:        activeCoupon 
	 * @Description:  激活优惠券/activate coupon
	 * @param:        @param couponId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月19日 上午10:38:43
	 */
	boolean activeCoupon(String couponId);
	
	/**
	 * 
	 * @Title:        useCoupon 
	 * @Description:  使用优惠券/use coupon
	 * @param:        @param couponId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月17日 下午2:08:49
	 */
	boolean useCoupon(String couponId);
	
	/**
	 * 
	 * @Title:        updateCouponAmount 
	 * @Description:  修改优惠券余额/modify coupon balance amount
	 * @param:        @param couponId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月19日 下午3:20:41
	 */
	boolean updateCouponAmount(UserCouponVo couponVo);
}
