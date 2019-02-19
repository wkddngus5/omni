/**
 * FileName:     INotificationConfigDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月25日 下午4:33:38
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

 /**
 * @ClassName:     INotificationConfigDao
 * @Description:Notification configuration interface
 * @author:    Albert
 * @date:        2017年11月25日 下午4:33:38
 *
 */
public interface INotificationConfigDao {
	
	public static  final String table_name = "t_notification_config";
	
	/**
	 * 
	 * @Title:        getConfigList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<NotificationConfigVo>    
	 * @author        Albert
	 * @Date          2017年11月25日 下午4:44:33
	 */
	List<NotificationConfigVo> getConfigList();
	
	/**
	 * 
	 * @Title:        getConfigInfo 
	 * @Description:  TODO
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       NotificationConfigVo    
	 * @author        Albert
	 * @Date          2017年11月25日 下午4:45:15
	 */
	NotificationConfigVo getConfigInfo(int id);
	
	/**
	 * 
	 * @Title:        getConfigInfoByType 
	 * @Description:  TODO
	 * @param:        @param type
	 * @param:        @return    
	 * @return:       NotificationConfigVo    
	 * @author        Albert
	 * @Date          2017年11月27日 下午3:40:50
	 */
	NotificationConfigVo getConfigInfoByType(int type);
	
	/**
	 * 
	 * @Title:        updateConfig 
	 * @Description:  TODO
	 * @param:        @param configVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月25日 下午5:01:11
	 */
	boolean updateConfig(NotificationConfigVo configVo);
	
	/**
	 * 
	 * @Title:        addConfig 
	 * @Description:  TODO
	 * @param:        @param configVo
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年11月27日 下午2:14:06
	 */
	String addConfig(NotificationConfigVo configVo);
}
