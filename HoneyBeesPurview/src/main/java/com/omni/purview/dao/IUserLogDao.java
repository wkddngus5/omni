/**
 * FileName:     IUserLogDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月14日 上午10:32:00
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月14日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.dao;

import java.sql.Connection;

import com.omni.purview.vo.UserLogVo;

 /**
 * @ClassName:     IUserLogDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月14日 上午10:32:00
 *
 */
public interface IUserLogDao {
	/**
	 * 增加日志
	 * @Title:        addUserLog 
	 * @Description:  TODO
	 * @param:        @param logVo
	 * @param:        @param conn
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月14日 上午10:32:40
	 */
	void addUserLog(UserLogVo logVo,Connection conn);
	
	/**
	 * 增加日志
	 * @Title:        addUserLog 
	 * @Description:  TODO
	 * @param:        @param logVo    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月14日 上午11:35:11
	 */
	void addUserLog(UserLogVo logVo);
	
	
}
