/**
 * FileName:     IDepositReturnDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月15日 下午3:07:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月15日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;
import com.pgt.bikelock.vo.DepositReturnVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     IDepositReturnDao
 * @Description:押金退还接口定义/deposit refund interface definition
 * @author:    Albert
 * @date:        2017年4月15日 下午3:07:38
 *
 */
public interface IDepositDao {
	
	static String TABLE_DEPOSIT_RETURN = "t_deposit_return";
	
	/**
	 * 
	 * @Title:        returnDeposit 
	 * @Description:  增加退还押金申请/add refund deposit apply
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月15日 下午3:09:13
	 */
	String returnDeposit(String userId,String tradeId);
	
	/**
	 * 
	 * @Title:        getReturnList 
	 * @Description:  获取退还列表/obtain refund list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<DepositReturnVo>    
	 * @author        Albert
	 * @Date          2017年4月15日 下午4:13:15
	 */
	public List<DepositReturnVo> getReturnList(RequestListVo requestVo);
	/**
	 * 
	 * @Title:        getCount 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月15日 下午4:13:26
	 */
	public int getCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        updateDepositReturn 
	 * @Description:  处理押金退还申请/tackle deposit refund apply
	 * @param:        @param recordId
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月15日 下午3:52:27
	 */
	boolean updateDepositReturn(String recordId,int status,String out_refund_no);
	
	/**
	 * 
	 * @Title:        refundDeposit 
	 * @Description:  批量处理已退还押金/tackle deposit has refund
	 * @param:        @param recordIds
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年9月21日 上午11:20:04
	 */
	int refundDeposit(String recordIds);
	
	/**
	 * 
	 * @Title:        getDepositReturnInfo 
	 * @Description:  押金退还申请详情/deposit refund apply details
	 * @param:        @param recordId
	 * @param:        @return    
	 * @return:       DepositReturnVo    
	 * @author        Albert
	 * @Date          2017年8月1日 下午4:34:05
	 */
	DepositReturnVo getDepositReturnInfo(String recordId);
	
}
