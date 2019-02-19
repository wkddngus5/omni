/**
 * FileName:     IAdminLogDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月15日 下午4:44:05
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月15日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.AdminLogVo;

 /**
 * @ClassName:     IAdminLogDao
 * @Description:管理员日志接口定义类/The administrator log interface defines the class
 * @author:    Albert
 * @date:        2017年4月15日 下午4:44:05
 *
 */
public interface IAdminLogDao {
	
	static String VIEW_NAME  = "view_admin_log_detail";
	static String COLOUM_USER_NAME  = "user_name";
	static String COLOUM_NICK_NAME  = "nickname";
	static String COLOUM_FUNC_NAME  = "name";
	static String COLOUM_FUNC_PARENT_NAME  = "parent_name";
	
	/**
	 * 
	 * @Title:        getLogList 
	 * @Description:  获取管理员日志列表/get administrator log sheet
	 * @param:        @param request
	 * @param:        @return    
	 * @return:       List<AdminLogVo>    
	 * @author        Albert
	 * @Date          2017年4月15日 下午4:45:32
	 */
	List<AdminLogVo> getLogList(RequestListVo request,String funcIds);
	
	/**
	 * 
	 * @Title:        getCount 
	 * @Description:  获取数量/get quantity
	 * @param:        @param request
	 * @param:        @param funcIds
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年6月15日 下午4:41:07
	 */
	int getCount(RequestListVo request,String funcIds);
}
