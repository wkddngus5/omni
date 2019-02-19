/**
 * FileName:     ICashDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月11日 上午9:41:22
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;


import java.math.BigDecimal;
import java.util.List;

import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     ICashDao
 * @Description:提现业务接口定义类/cash business protocol definition
 * @author:    Albert
 * @date:        2017年5月11日 上午9:41:22
 *
 */
public interface ICashRecordDao {
	/**
	 * 
	 * @Title:        addCash 
	 * @Description:  TODO
	 * @param:        @param cashVo
	 * @param:        @return    
	 * @return:       cashId    
	 * @author        Albert
	 * @Date          2017年5月11日 上午9:59:31
	 */
	String addCash(CashRecordVo cashVo);
	
	/**
	 * 
	 * @Title:        addAccountCash 
	 * @Description:  新增余额提现记录/add balance cash record
	 * @param:        @param cashVo
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年5月23日 下午2:48:41
	 */
	String addAccountCash(CashRecordVo cashVo);
	
	/**
	 * 
	 * @Title:        updateCashOrderId 
	 * @Description:  更新转账订单ID/update transfer order id
	 * @param:        @param orderId
	 * @param:        @param cashId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月11日 下午3:07:03
	 */
	boolean updateCashOrderId(String orderId,String cashId);
	
	
	/**
	 * 
	 * @Title:        updateCashStatus 
	 * @Description:  修改提现状态/modify cash status
	 * @param:        @param status
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月11日 上午10:02:50
	 */
	boolean updateCashStatus(int status,String recordId,BigDecimal refundAmount);
	
	
	/**
	 * 
	 * @Title:        getCashList 
	 * @Description:  获取提现记录/Get Cash List
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<CashRecordVo>    
	 * @author        Albert
	 * @Date          2017年11月3日 下午5:28:31
	 */
	List<CashRecordVo> getCashList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getCashCount 
	 * @Description:   获取提现记录总数/Get Cash Count
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年11月3日 下午5:29:22
	 */
	int getCashCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getCashDetail 
	 * @Description:  获取提现记录详情/Get Cash Detail Info
	 * @param:        @param cashId
	 * @param:        @return    
	 * @return:       CashRecordVo    
	 * @author        Albert
	 * @Date          2017年11月3日 下午8:35:54
	 */
	CashRecordVo getCashDetail(String cashId);
	
	/**
	 * 
	 * @Title:        getUserBalanceCash 
	 * @Description:  获取用户当前余额提现记录
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       CashRecordVo    
	 * @author        Albert
	 * @Date          2017年11月6日 下午8:36:06
	 */
	CashRecordVo getUserBalanceCash(String userId);
}
