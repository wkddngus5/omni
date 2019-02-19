/**
 * FileName:     IBikeLongLeaseDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 上午11:23:46
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月5日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.BikeLongLeaseVo;

 /**
 * @ClassName:     IBikeLongLeaseDao
 * @Description:单车长租接口定义/bike long time rent protocol definition
 * @author:    Albert
 * @date:        2017年4月5日 上午11:23:46
 *
 */
public interface IBikeLongLeaseDao {
	/**
	 * 
	 * @Title:        addLease 
	 * @Description:  添加长租信息/add long time rent information
	 * @param:        @param leaseVo
	 * @param:        @return    
	 * @return:       String 记录ID
	 * @author        Albert
	 * @Date          2017年4月5日 上午11:26:12
	 */
	String addLease(BikeLongLeaseVo leaseVo);
	
	/**
	 * 
	 * @Title:        updatePaySuccess 
	 * @Description:  修改订单状态为支付成功/modify order status as payment success
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月5日 上午11:41:48
	 */
	boolean updatePaySuccess(String id);
	
	/**
	 * 
	 * @Title:        getLeaseInfo 
	 * @Description:  获取用户租赁信息/obtain user rent information
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       BikeLongLeaseVo    
	 * @author        Albert
	 * @Date          2017年4月5日 上午11:27:03
	 */
	BikeLongLeaseVo getUserLeaseInfo(String userId);

	/**
	 * 
	 * @Title:        getLeaseInfo 
	 * @Description:  获取长租信息/obtain long time rent infomation
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       BikeLongLeaseVo    
	 * @author        Albert
	 * @Date          2017年4月6日 下午3:10:02
	 */
	BikeLongLeaseVo getLeaseInfo(String id);
}
