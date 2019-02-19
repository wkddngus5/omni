/**
 * FileName:     IRechargeDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 下午3:05:56
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RechargeAmountVo;

 /**
 * @ClassName:     IRechargeDao
 * @Description:充值金额业务接口类/recharge amount business interface type
 * @author:    Albert
 * @date:        2017年5月9日 下午3:05:56
 *
 */
public interface IRechargeAmountDao {
	
	static String TABLE_RECHARGE_AMOUNT = "t_recharge_amount";
	
	/**
	 * 
	 * @Title:        getAmountList 
	 * @Description:  获取金额列表/obtain amount list
	 * @param:        @return    
	 * @return:       List<RechargeAmountVo>    
	 * @author        Albert
	 * @Date          2017年5月9日 下午3:11:13
	 */
	List<RechargeAmountVo> getAmountList(int cityId);
	
	/**
	 * 
	 * @Title:        addAmount 
	 * @Description:  增加金额/add amount of money
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月9日 下午3:11:51
	 */
	String addAmount(RechargeAmountVo amount);
	
	/**
	 * 
	 * @Title:        updateAmount 
	 * @Description:  修改金额/modify amount of money
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月9日 下午3:12:21
	 */
	boolean updateAmount(RechargeAmountVo amount);
	
	/**
	 * 
	 * @Title:        getAmount 
	 * @Description:  获取金额详情/obtain amount of money details
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       RechargeAmountVo    
	 * @author        Albert
	 * @Date          2017年5月9日 下午3:13:38
	 */
	RechargeAmountVo getAmount(String id);
}
