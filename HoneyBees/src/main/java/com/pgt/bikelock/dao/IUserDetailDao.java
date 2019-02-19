/**
 * FileName:     IUserDetailDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月12日 下午4:41:29
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialziation
 */
package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.UserDetailVo;

 /**
 * @ClassName:     IUserDetailDao
 * @Description:用户详情业务接口/user details business interface
 * @author:    Albert
 * @date:        2017年7月12日 下午4:41:29
 *
 */
public interface IUserDetailDao {
	/**
	 * 
	 * @Title:        getUserDetail 
	 * @Description:  用户详情资料获取/user details date obtain
	 * @param:        @param uid
	 * @param:        @return    
	 * @return:       UserDetailVo    
	 * @author        Albert
	 * @Date          2017年7月12日 下午4:43:47
	 */
	UserDetailVo getUserDetail(String uid);
	
	/**
	 * 
	 * @Title:        getUserDetailWithEmail 
	 * @Description:  TODO
	 * @param:        @param email
	 * @param:        @return    
	 * @return:       UserDetailVo    
	 * @author        Albert
	 * @Date          2018年4月20日 上午11:33:03
	 */
	UserDetailVo getUserDetailWithEmail(String email);
	
	/**
	 * 
	 * @Title:        updateUserDetail 
	 * @Description:  更新用户详情资料/update user details information
	 * @param:        @param detailVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月12日 下午4:44:37
	 */
	boolean updateUserDetail(UserDetailVo detailVo);
	
	/**
	 * 
	 * @Title:        emailAuth 
	 * @Description:  TODO
	 * @param:        @param userId
	 * @param:        @param email
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年4月13日 下午6:25:07
	 */
	boolean emailAuth(String userId,String email,int authType);
	
}
