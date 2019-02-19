/**
 * FileName:     IUserDao.java
 * @Description: 用户相关业务接口类/user related business interface type
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:08:04
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.math.BigDecimal;
import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;

/**
 * 
 * @ClassName:     IUserDao
 * @Description:用户相关接口定义/user related interface definition
 * @author:    Albert
 * @date:        2017-3-24 上午10:19:41
 *
 */
public interface IUserDao {
	
	String TABLE_NAME = "t_user";
	String CLOUM_PHONE = "phone";

	/**
	 * @Title:        addUser 
	 * @Description:  增加用户信息/add user information
	 * @param:        @param user
	 * @param:        @return   用户ID 
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-24 下午3:24:31
	 */
	String addUser(UserVo user);

	/**
	 * 
	 * @Title:        getUser 
	 * @Description:  获取用户信息/obtain user information
	 * @param:        @param phone 手机号/phone number
	 * @param:        @return    
	 * @return:       UserVo    
	 * @author        Albert
	 * @Date          2017-3-24 下午3:51:20
	 */
	UserVo getUser(String phone,int industryId);
	
	/**
	 * 
	 * @Title:        getUserByEmail 
	 * @Description:  TODO
	 * @param:        @param email
	 * @param:        @param industryId
	 * @param:        @return    
	 * @return:       UserVo    
	 * @author        Albert
	 * @Date          2018年9月1日 下午2:54:05
	 */
	UserVo getUserByEmail(String email,int industryId);
	
	/**
	 * 
	 * @Title:        gerUser 
	 * @Description:  获取用户信息/obtain user information
	 * @param:        @param phone 手机号/phone number
	 * @param:        @param password 密码/password
	 * @param:        @return    
	 * @return:       UserVo    
	 * @author        Albert
	 * @Date          2017-3-24 下午4:14:30
	 */
	UserVo getUser(String phone,String password,int industryId,int countryId);
	
	
	/**
	 * 
	 * @Title:        getUser 
	 * @Description:  获取用户信息/obtain user information
	 * @param:        @param userId 用户ID
	 * @param:        @return    
	 * @return:       UserVo    
	 * @author        Albert
	 * @Date          2017-3-24 下午3:51:20
	 */
	UserVo getUserWithId(String userId,boolean formateName);
	
	/**
	 * 
	 * @Title:        getUserWithId 
	 * @Description:  获取用户信息,ID加密码/obtain user information, id add password
	 * @param:        @param userId
	 * @param:        @param password
	 * @param:        @return    
	 * @return:       UserVo    
	 * @author        Albert
	 * @Date          2017-3-28 下午2:18:42
	 */
	UserVo getUserWithId(String userId,String password,int industryId);
	
	/**
	 * 
	 * @Title:        getUserWithIndustryInfo 
	 * @Description:  获取用户信息关联产业信息/obtain user information related industry information
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       UserVo    
	 * @author        Albert
	 * @Date          2017-3-28 上午10:18:59
	 */
	UserVo getUserWithIndustryInfo(String userId);
	
	/**
	 * 
	 * @Title:        getUserDetailInfo 
	 * @Description:  获取用户详细信息/obtain user detail information
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       UserDetailVo    
	 * @author        Albert
	 * @Date          2017年4月5日 下午2:03:32
	 */
	UserDetailVo getUserDetailInfo(String userId);
	
	/**
	 * 
	 * @Title:        getUserId 
	 * @Description:  获取用户ID/obtain user id
	 * @param:        @param inviteCode
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月5日 下午1:42:47
	 */
	String getUserId(String inviteCode);

	/**
	 * @Title:        thirdUserLogin 
	 * @Description:  第三方用户登录/third party user register
	 * @param:        @param uuid
	 * @param:        @param type 1:微信 2:QQ
	 * @param:        @return    
	 * @return:       UserVo    
	 * @author        Albert
	 * @Date          2017-3-24 下午4:27:43
	 */
	UserVo thirdUserLogin(String uuid,int type);
	
	/**
	 * 
	 * @Title:        getThirdId 
	 * @Description:  获取第三方授权ID/obtain third party authority
	 * @param:        @param uuid
	 * @param:        @param type
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年6月2日 上午11:00:31
	 */
	String getThirdId(String uuid,int type);
	
	/**
	 * 
	 * @Title:        getThirdUUID 
	 * @Description:  TODO
	 * @param:        @param uid
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2018年2月8日 上午11:28:35
	 */
	String getThirdUUID(String uid);
	 
	/**
	 * 
	 * @Title:        addThirdInfo 
	 * @Description:  增加第三方登录信息/add third party log in information
	 * @param:        @param uuid 第三方平台返回的用户唯一ID/third party back user only id
	 * @param:        @param type
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017-3-24 下午4:31:06
	 */
	String addThirdInfo(String uuid,int type,String userId);
	
	
	/**
	 * 
	 * @Title:        updateThirdInfo 
	 * @Description:  更新第三方授权信息/update third party authority information
	 * @param:        @param userId
	 * @param:        @param thirdId
	 * @param:        @param type
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月2日 上午10:09:30
	 */
	boolean updateThirdInfo(String userId,String thirdId);
	
	/**
	 * 
	 * @Title:        updateLoginDate 
	 * @Description:  更新登录时间/update log in time
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-28 上午10:48:18
	 */
	boolean updateLoginDate(String userId);
	
	/**
	 * 
	 * @Title:        updateMoney 
	 * @Description:  修改用户余额/modify user balance
	 * @param:        @param money
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-28 上午11:23:05
	 */
	boolean updateMoney(BigDecimal money,String userId);
	
	/**
	 * 
	 * @Title:        updateGiftMoney 
	 * @Description:  修改赠送余额/update gift amount
	 * @param:        @param money
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年5月31日 下午5:48:24
	 */
	boolean updateGiftMoney(BigDecimal money,String userId);
	

	/**
	 * 
	 * @Title:        updateUserMoney 
	 * @Description:  所有用户所有余额（实际和赠送）/update user's all money,include balance and gift
	 * @param:        @param balanceMoney
	 * @param:        @param giftMoney
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年5月31日 下午7:13:54
	 */
	boolean updateUserMoney(BigDecimal balanceMoney,BigDecimal giftMoney,String userId);
	
	/**
	 * 
	 * @Title:        updateUserInfo 
	 * @Description:  更新用户资料/update user information
	 * @param:        @param userVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-28 下午2:03:54
	 */
	boolean updateUserInfo(UserVo userVo);
	
	/**
	 * 
	 * @Title:        updateUserAllInfo 
	 * @Description:  更新用户所有资料（后台）/update user all information
	 * @param:        @param userVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月31日 下午3:13:55
	 */
	boolean updateUserAllInfo(UserVo userVo);
	
	/**
	 * 
	 * @Title:        updatePassword 
	 * @Description:  更新密码/update password
	 * @param:        @param userId
	 * @param:        @param password
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-28 下午2:10:12
	 */
	boolean updatePassword(String userId,String password);
	

	/**
	 * 
	 * @Title:        updateAuthStatus 
	 * @Description:  更新认证进度  0：未认证 1：手机 2：实名 3：押金 4：银行卡(验证前置状态)/update certification schedule 0:not certification 1:phone 2:real name 3:depo
	 * @param:        @param userId
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年3月30日 上午11:31:34
	 */
	boolean updateAuthStatusCheckFront(String userId,int status);
	
	/**
	 * 
	 * @Title:        updateAuthStatus 
	 * @Description:  修改用户认证状态/modify user certification status
	 * @param:        @param userId
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月28日 下午6:07:48
	 */
	boolean updateAuthStatus(String userId,int status);
	
	/**
	 * 
	 * @Title:        updateAuthDeposit 
	 * @Description:  更新押金认证通过（重新缴付，直接到最后状态,目前最终为4，即银行卡通过，如有改动，需更改/update deposit certification(renew pay, direct to final status, at present is 4, that is bank card passed, if modify, need change
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月15日 下午6:01:34
	 */
	boolean updateAuthDeposit(String userId,int authNum);
	
	/**
	 * 
	 * @Title:        updateAuthStatus 
	 * @Description:  手机认证/phone certification
	 * @param:        @param userId
	 * @param:        @param phone
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月1日 下午7:14:41
	 */
	boolean updateAuthStatus(String userId,String phone);
	
	
	/**
	 * 
	 * @Title:        updatePhone 
	 * @Description:  修改用户手机号/modify user phone number
	 * @param:        @param userId
	 * @param:        @param phone
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月7日 下午6:09:37
	 */
	boolean updatePhone(String userId,String phone,int countryId);
	
	
	/**
	 * 
	 * @Title:        updateCity 
	 * @Description:  修改用户城市/update user city
	 * @param:        @param userId
	 * @param:        @param cityId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年2月7日 上午11:49:07
	 */
	boolean updateCity(String userId,int cityId);
	
	/**********************后台业务*****************************/
	
	/**
	 * 
	 * @Title:        getUserList 
	 * @Description:  获取用户列表/obtain user list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<UserVo>    
	 * @author        Albert
	 * @Date          2017年4月8日 上午10:56:14
	 */
 	List<UserDetailVo> getUserList(RequestListVo requestVo);
 	
 	/**
 	 * 
 	 * @Title:        getCount 
 	 * @Description:  获取列表总数/obtain list total amount
 	 * @param:        @param requestVo
 	 * @param:        @return    
 	 * @return:       int    
 	 * @author        Albert
 	 * @Date          2017年4月8日 上午11:18:00
 	 */
 	int getCount(RequestListVo requestVo);
 	
 	/**
 	 * 
 	 * @Title:        getRedPackAmount 
 	 * @Description:  获取用户红包金额/obtain user red envelopes amount
 	 * @param:        @param userId
 	 * @param:        @return    
 	 * @return:       BigDecimal    
 	 * @author        Albert
 	 * @Date          2017年5月11日 下午2:33:32
 	 */
 	BigDecimal getRedPackAmount(String userId);
 	
 	/**
 	 * 
 	 * @Title:        getMoneyAmount 
 	 * @Description:  获取用户钱包金额/obtain user red envelopes amount of money
 	 * @param:        @param userId
 	 * @param:        @return    
 	 * @return:       BigDecimal    
 	 * @author        Albert
 	 * @Date          2017年5月16日 上午10:02:06
 	 */
 	BigDecimal getMoneyAmount(String userId);
 	
 	/**
 	 * 
 	 * @Title:        getGifgMoneyAmount 
 	 * @Description:  获取赠送余额/get gift amount
 	 * @param:        @param userId
 	 * @param:        @return    
 	 * @return:       BigDecimal    
 	 * @author        Albert
 	 * @Date          2018年5月31日 下午5:49:28
 	 */
 	BigDecimal getGifgMoneyAmount(String userId);
 	
 	/**
 	 * 
 	 * @Title:        cashRedPack 
 	 * @Description:  提现红包/red envelopes in cash
 	 * @param:        @param userId
 	 * @param:        @return    
 	 * @return:       boolean    
 	 * @author        Albert
 	 * @Date          2017年5月11日 下午2:34:54
 	 */
 	boolean cashRedPack(String userId,BigDecimal amount);
 	
 	/**
 	 * 
 	 * @Title:        getStatusStatisticsList 
 	 * @Description:  用户状态统计/user status caculate
 	 * @param:        @return    
 	 * @return:       List<StatisticsVo>    
 	 * @author        Albert
 	 * @Date          2017年7月6日 上午11:01:16
 	 */
 	public List<StatisticsVo> getStatusStatisticsList();
}
