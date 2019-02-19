/**
 * FileName:     ICreditScoreDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月12日 下午4:50:40
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;
import com.pgt.bikelock.vo.CreditScoreVo;

 /**
 * @ClassName:     ICreditScoreDao
 * @Description:信用积分接口定义类/credit interface definition
 * @author:    Albert
 * @date:        2017年5月12日 下午4:50:40
 *
 */
public interface ICreditScoreDao {

	/**
	 * 
	 * @Title:        addUserCreditRecord 
	 * @Description:  增加用户信用记录/add user credit record
	 * @param:        @param scoreVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月12日 下午4:54:16
	 */
	boolean addUserCreditRecord(CreditScoreVo scoreVo);
	
	/**
	 * 
	 * @Title:        getUserCreditList 
	 * @Description:  获取用户信用积分列表/obtain user credit list
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       List<UserCreditScoreVo>    
	 * @author        Albert
	 * @Date          2017年5月12日 下午4:54:47
	 */
	List<CreditScoreVo> getUserCreditList(String userId,int startPage);
}
