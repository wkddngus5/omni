/**
 * FileName:     IBandCardDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 上午9:30:34
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月5日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;



import java.util.List;

import com.pgt.bikelock.vo.BankCardVo;

 /**
 * @ClassName:     IBandCardDao
 * @Description:银行卡接口定义类/bank card protocol definition type
 * @author:    Albert
 * @date:        2017年4月5日 上午9:30:34
 *
 */
public interface IBankCardDao {
	/**
	 * 
	 * @Title:        addCard 
	 * @Description:  增加银行卡信息/add bank card information
	 * @param:        @param card
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月5日 上午9:31:38
	 */
	boolean addCard(BankCardVo card);
	
	/**
	 * 
	 * @Title:        getCardList 
	 * @Description:  获取银行卡列表/obtain bank card list
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       List<BankCardVo>    
	 * @author        Albert
	 * @Date          2017年4月5日 上午9:39:16
	 */
	List<BankCardVo> getCardList(String userId);
	
	/**
	 * 
	 * @Title:        getCardInfo 
	 * @Description:  获取银行卡信息/obtain bank card information
	 * @param:        @param cardNumber
	 * @param:        @return    
	 * @return:       BankCardVo    
	 * @author        Albert
	 * @Date          2017年4月5日 上午9:59:48
	 */
	BankCardVo getCardInfo(String cardNumber,String userId);
	
	/**
	 * 
	 * @Title:        deleteCard 
	 * @Description:  删除银行卡/delete bank card
	 * @param:        @param cardId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月15日 上午11:42:37
	 */
	boolean deleteCard(String cardId);
}
