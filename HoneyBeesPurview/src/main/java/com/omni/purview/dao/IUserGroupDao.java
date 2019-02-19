/**
 * FileName:     IUserGroupDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月2日 上午10:54:31
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月2日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.dao;

import java.util.List;

import com.omni.purview.vo.UserGroupVo;

 /**
 * @ClassName:     IUserGroupDao
 * @Description:权限组业务接口定义
 * @author:    Albert
 * @date:        2017年6月2日 上午10:54:31
 *
 */
public interface IUserGroupDao {

	/**
	 * 用户
	 * @Title:        getUserGroupList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<UserGroupVo>    
	 * @author        Albert
	 * @Date          2017年6月2日 下午6:42:04
	 */
	List<UserGroupVo> getUserGroupList();
	
	
	/**
	 * 获取用户权限组ID
	 * @Title:        getGroupFunctionIdList 
	 * @Description:  TODO
	 * @param:        @param groupId
	 * @param:        @return    
	 * @return:       List<String>    
	 * @author        Albert
	 * @Date          2017年6月3日 下午10:53:26
	 */
	List<String> getUserGroupIdList(int userId);
	
	
	
	/**
	 * 
	 * @Title:        addUserGroup 
	 * @Description:  为用户添加组
	 * @param:        @param userId
	 * @param:        @param groupIds
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月5日 下午1:59:33
	 */
	String addUserGroup(int userId,String[] groupIds);
	
	
	/**
	 * 
	 * @Title:        deleteUserGroup 
	 * @Description:  删除用户组
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月3日 下午10:06:46
	 */
	boolean deleteUserGroup(int userId);
}
