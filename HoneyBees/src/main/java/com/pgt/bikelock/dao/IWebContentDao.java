/**
 * FileName:     IAgremmentDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午7:18:58
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.WebContentVo;

 /**
 * @ClassName:     IAgremmentDao
 * @Description:协议接口定义类
 * @author:    Albert
 * @date:        2017年4月10日 下午7:18:58
 *
 */
public interface IWebContentDao {
	/**
	 * 
	 * @Title:        getContentList 
	 * @Description:  获取协议列表
	 * @param:        @param industryId
	 * @param:        @return    
	 * @return:       List<AgreementVo>    
	 * @author        Albert
	 * @Date          2017年4月10日 下午7:23:05
	 */
	List<WebContentVo> getContentList(String industryId);
	
	/**
	 * 
	 * @Title:        getContentCount 
	 * @Description:  获取协议数量
	 * @param:        @param industryId
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月10日 下午7:43:00
	 */
	int getContentCount(String industryId);
	
	/**
	 * 
	 * @Title:        getAgreement 
	 * @Description:  获取协议详情
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       AgreementVo    
	 * @author        Albert
	 * @Date          2017年4月10日 下午7:54:42
	 */
	WebContentVo getContent(String id);
	
	/**
	 * 
	 * @Title:        getContent 
	 * @Description:  获取协议详情
	 * @param:        @param industryId 产业ID
	 * @param:        @param type 协议类型 1 用户协议 2：充值协议
	 * @param:        @return    
	 * @return:       AgreementVo    
	 * @author        Albert
	 * @Date          2017年4月11日 上午10:27:50
	 */
	WebContentVo getContent(String industryId,int type);
	
	/**
	 * 
	 * @Title:        updateContent 
	 * @Description:  更新协议/update protocol
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月10日 下午7:32:02
	 */
	boolean updateContent(String id,String content);
}
