/**
 * FileName:     IBikeMaintainDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月27日 下午4:46:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月27日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.BikeMaintainVo;

 /**
 * @ClassName:     IBikeMaintainDao
 * @Description:单车维护业务接口定义/bike maintain business protocol definition
 * @author:    Albert
 * @date:        2017年7月27日 下午4:46:55
 *
 */
public interface IBikeMaintainDao {
	
	final String TABLE_NAME = "t_bike_maintain";
	final String VIEW_NAME = "view_bike_maintain";

	
	/**
	 * 
	 * @Title:        getMaintainList 
	 * @Description:  获取维护列表/obtain maintain list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<BikeMaintain>    
	 * @author        Albert
	 * @Date          2017年7月27日 下午4:48:42
	 */
	List<BikeMaintainVo> getMaintainList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getMaintainCount 
	 * @Description:  获取维护列表总数/obtain maintain list total number
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年7月28日 下午2:22:27
	 */
	int getMaintainCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        addMaintain 
	 * @Description:  添加维护信息/add maintain information
	 * @param:        @param maintainVo
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月27日 下午4:48:55
	 */
	String addMaintain(BikeMaintainVo maintainVo);
	
	/**
	 * 
	 * @Title:        updateMaintain 
	 * @Description:  修改维护信息/modify maintain information
	 * @param:        @param maintainVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月28日 上午11:52:17
	 */
	boolean updateMaintain(BikeMaintainVo maintainVo);
	
	/**
	 * 
	 * @Title:        getMaintainInfo 
	 * @Description:  获取维护信息详情/obtain repair information details
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       BikeMaintainVo    
	 * @author        Albert
	 * @Date          2017年7月27日 下午6:46:17
	 */
	BikeMaintainVo getMaintainInfo(String id);
}
