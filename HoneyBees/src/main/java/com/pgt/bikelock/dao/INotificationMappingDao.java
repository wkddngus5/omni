/**
 * FileName:     INotificationMappingDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月25日 下午5:03:05
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月25日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.admin.NotificationConfigVo;
import com.pgt.bikelock.vo.admin.NotificationMappingVo;

 /**
 * @ClassName:     INotificationMappingDao
 * @Description:Notification with admin mapping interface
 * @author:    Albert
 * @date:        2017年11月25日 下午5:03:05
 *
 */
public interface INotificationMappingDao {
	
	
	
	/**
	 * 
	 * @Title:        getMapping 
	 * @Description:  获取关系映射详情/get mapping detail
	 * @param:        @param groupId
	 * @param:        @return    
	 * @return:       NotificationMappingVo    
	 * @author        Albert
	 * @Date          2017年11月25日 下午5:04:49
	 */
	List<NotificationMappingVo> getMappingList(String groupId);
	
	/**
	 * 
	 * @Title:        getMappingList 
	 * @Description:  获取指定类型
	 * @param:        @param notifyType
	 * @param:        @return    
	 * @return:       List<NotificationConfigVo>    
	 * @author        Albert
	 * @Date          2017年11月29日 上午10:37:06
	 */
	List<NotificationConfigVo> getMappingList(int notifyType);

	/**
	 * 
	 * @Title:        getMappingTypeList 
	 * @Description:  TODO
	 * @param:        @param groupId
	 * @param:        @return    
	 * @return:       List<String>    
	 * @author        Albert
	 * @Date          2017年11月27日 下午5:14:08
	 */
	List<String> getMappingTypeList(String groupId);

	
	/**
	 * 
	 * @Title:        addMapping 
	 * @Description:  添加映射/add mapping
	 * @param:        @param adminId
	 * @param:        @param notifyIds
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月25日 下午5:11:14
	 */
	boolean addMapping(String groupId,String[] notifyIds);
	
	/**
	 * 
	 * @Title:        deleteMapping 
	 * @Description:  删除映射/delete mapping
	 * @param:        @param adminId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月25日 下午5:12:01
	 */
	boolean deleteMapping(String groupId);
}
