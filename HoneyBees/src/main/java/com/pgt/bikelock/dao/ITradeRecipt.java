/**
 * FileName:     ITradeRecipt.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午6:05:45
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.TradeReceiptVo;

 /**
 * @ClassName:     ITradeRecipt
 * @Description:订单收据业务接口定义/order bill business interface definition
 * @author:    Albert
 * @date:        2017年4月10日 下午6:05:45
 *
 */
public interface ITradeRecipt {
	
	String TABLE_NAME = "t_trade_receipt";
	String CLOUM_FIRSTNAME = "firstname";
	String CLOUM_LASTNAME = "lastname";
	String CLOUM_PHONE = "phone";
	String CLOUM_ADDRESS = "address";
	String CLOUM_COUNTRY = "country";
	
	/**
	 * 
	 * @Title:        addTradeReceipt 
	 * @Description:  添加收据/add receipt
	 * @param:        @param receipt
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月5日 下午5:22:54
	 */
	boolean addTradeReceipt(TradeReceiptVo receipt);
	
	/**
	 * 
	 * @Title:        getTradeReciptList 
	 * @Description:  获取收据申请列表/obtain receipt apply list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<TradeReceiptVo>    
	 * @author        Albert
	 * @Date          2017年4月10日 下午6:15:36
	 */
	List<TradeReceiptVo> getTradeReciptList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        updateReciptStatus 
	 * @Description:  修改收据申请状态/modify receipt apply status
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月10日 下午6:22:33
	 */
	boolean updateReciptStatus(String id,int status);
	
	/**
	 * 
	 * @Title:        deleteRecipt 
	 * @Description:  删除收据申请/delete receipt apply
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月10日 下午6:22:53
	 */
	boolean deleteRecipt(String id);
}
