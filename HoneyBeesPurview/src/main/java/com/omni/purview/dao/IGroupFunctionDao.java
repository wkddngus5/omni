/**
 * FileName:     IGroupPurviewDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月2日 上午10:53:53
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月2日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.dao;

import java.util.List;
import java.util.ResourceBundle;

import com.omni.purview.vo.FunctionVo;
import com.omni.purview.vo.GroupFunctionVo;

 /**
 * @ClassName:     IGroupPurviewDao
 * @Description:权限组功能映射业务接口定义
 * @author:    Albert
 * @date:        2017年6月2日 上午10:53:53
 *
 */
public interface IGroupFunctionDao {

	/**
	 * 获取权限组功能列表(默认一级)
	 * @Title:        getGroupFunctionList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<GroupPurviewVo>    
	 * @author        Albert
	 * @Date          2017年6月2日 下午1:50:06
	 */
	List<GroupFunctionVo> getGroupFunctionList(int groupId,ResourceBundle rb);
	
	/**
	 * 获取权限组功能列表(一、二级)
	 * @Title:        getGroupsFunctionList 
	 * @Description:  TODO
	 * @param:        @param groupIds
	 * @param:        @return    
	 * @return:       List<GroupFunctionVo>    
	 * @author        Albert
	 * @Date          2017年6月5日 下午2:48:34
	 */
	List<FunctionVo> getGroupsFunctionList(String groupIds,ResourceBundle rb);
	
	/**
	 * 获取子权限组功能（三级）
	 * @Title:        getThirdGroupsFunctionList 
	 * @Description:  TODO
	 * @param:        @param parentId
	 * @param:        @param groupIds
	 * @param:        @return    
	 * @return:       List<GroupFunctionVo>    
	 * @author        Albert
	 * @Date          2017年6月5日 下午3:02:06
	 */
	List<FunctionVo> getThirdGroupsFunctionList(int parentId,String groupIds,ResourceBundle rb);
	
	/**
	 * 验证权限组权限
	 * @Title:        checkGroupsFunction 
	 * @Description:  TODO
	 * @param:        @param groupIds
	 * @param:        @param actionId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月5日 下午2:39:05
	 */
	boolean checkGroupsFunction(String groupIds,int actionId,String dataId,String adminId);
	
	/**
	 * 获取权限组功能ID
	 * @Title:        getGroupFunctionIdList 
	 * @Description:  TODO
	 * @param:        @param groupId
	 * @param:        @return    
	 * @return:       List<String>    
	 * @author        Albert
	 * @Date          2017年6月3日 下午10:53:26
	 */
	List<String> getGroupFunctionIdList(int groupId);
	
	/**
	 * 添加权限组功能
	 * @Title:        addGroupFunction 
	 * @Description:  TODO
	 * @param:        @param groupId
	 * @param:        @param funcIds
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月3日 下午9:59:35
	 */
	String addGroupFunction(int groupId,List<String> funcIdList);

	/**
	 * 
	 * @Title:        deleteGroupFunction 
	 * @Description:  删除权限组功能
	 * @param:        @param groupId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月3日 下午10:06:46
	 */
	boolean deleteGroupFunction(int groupId);
	
}
