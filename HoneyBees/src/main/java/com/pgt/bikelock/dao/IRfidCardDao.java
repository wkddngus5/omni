/**
 * FileName:     IRridCardDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月16日 上午11:01:30
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.RfidCardVo;

 /**
 * @ClassName:     IRridCardDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2018年8月16日 上午11:01:30
 *
 */
public interface IRfidCardDao {
	
	String TABLE_NAME = "t_rfid_card";

	/**
	 * 
	 * @Title:        getCardList 
	 * @Description:  get rfid card list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<RfidCardVo>    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:02:59
	 */
	List<RfidCardVo> getCardList(RequestListVo requestVo);
	

	
	/**
	 * 
	 * @Title:        getCardCount 
	 * @Description:  get rfid card count
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:04:34
	 */
	int getCardCount(RequestListVo requestVo);
	
	
	/**
	 * 
	 * @Title:        getUserCardList 
	 * @Description:  TODO
	 * @param:        @param uid
	 * @param:        @return    
	 * @return:       List<RfidCardVo>    
	 * @author        Albert
	 * @Date          2018年8月16日 下午4:29:47
	 */
	List<RfidCardVo> getUserCardList(String uid);
	
	/**
	 * 
	 * @Title:        getCardInfo 
	 * @Description:  TODO
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       RfidCardVo    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:50:14
	 */
	RfidCardVo getCardInfo(String id);
	
	/**
	 * 
	 * @Title:        getCardInfoWithCardNo 
	 * @Description:  get rfid card with no
	 * @param:        @param cardNo
	 * @param:        @return    
	 * @return:       RfidCardVo    
	 * @author        Albert
	 * @Date          2018年8月16日 下午4:35:41
	 */
	RfidCardVo getCardInfoWithCardNo(String cardNo);
	
	/**
	 * 
	 * @Title:        addCard 
	 * @Description:  add rfid card
	 * @param:        @param cardVo
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:02:19
	 */
	String addCard(RfidCardVo cardVo);
	
	/**
	 * 
	 * @Title:        updateCard 
	 * @Description:  update rfid card info
	 * @param:        @param cardVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:03:24
	 */
	boolean updateCard(RfidCardVo cardVo);

	/**
	 * 
	 * @Title:        bindCard 
	 * @Description:  TODO
	 * @param:        @param uid
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月16日 下午4:45:12
	 */
	boolean bindCard(int uid,String id);
}
