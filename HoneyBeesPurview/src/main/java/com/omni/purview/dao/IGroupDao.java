/**
 * FileName:     IGroupDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月2日 上午10:53:14
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月2日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.dao;

import java.util.List;

import com.omni.purview.vo.GroupVo;

 /**
 * @ClassName:     IGroupDao
 * @Description:权限组业务接口定义
 * @author:    Albert
 * @date:        2017年6月2日 上午10:53:14
 *
 */
public interface IGroupDao {
	/**
	 * 权限组列表
	 * @Title:        getGroupList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<Group>    
	 * @author        Albert
	 * @Date          2017年6月2日 下午1:04:41
	 */
	List<GroupVo> getGroupList(List<String> existId,int cityId);
	
	
	/**
	 * 添加权限组
	 * @Title:        addGroup 
	 * @Description:  TODO
	 * @param:        @param groupVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月3日 下午9:46:58
	 */
	int addGroup(GroupVo groupVo);
	
	/**
	 * 修改权限组
	 * @Title:        updateGroup 
	 * @Description:  TODO
	 * @param:        @param groupVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月3日 下午9:47:46
	 */
	boolean updateGroup(GroupVo groupVo);
	
	/**
	 * 删除权限组
	 * @Title:        deleteGroup 
	 * @Description:  TODO
	 * @param:        @param groupId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月3日 下午9:48:11
	 */
	boolean deleteGroup(int groupId);
	
	/**
	 * 用户组详情
	 * @Title:        getGroupInfo 
	 * @Description:  TODO
	 * @param:        @param groupId
	 * @param:        @return    
	 * @return:       GroupVo    
	 * @author        Albert
	 * @Date          2017年6月3日 下午10:42:02
	 */
	GroupVo getGroupInfo(int groupId);
}
