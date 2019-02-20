/**
 * FileName:     FunctionTest.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月18日 下午5:39:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月18日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.test;

import java.util.List;

import com.omni.purview.dao.IUserGroupDao;
import com.omni.purview.dao.impl.FunctionDaoImpl;
import com.omni.purview.dao.impl.GroupDaoImpl;
import com.omni.purview.dao.impl.GroupFunctionDaoImpl;
import com.omni.purview.dao.impl.UserGroupDaoImpl;
import com.omni.purview.vo.FunctionVo;
import com.omni.purview.vo.GroupFunctionVo;
import com.omni.purview.vo.GroupVo;
import com.omni.purview.vo.UserGroupVo;

 /**
 * @ClassName:     FunctionTest
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月18日 下午5:39:55
 *
 */
public class FunctionTest {

	/** 
	 * @Title:        main 
	 * @Description:  TODO
	 * @param:        @param args    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月18日 下午5:39:55 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		System.out.println(addFunction());
		
//		List<FunctionVo> list =  getFunctionList();
//		List<GroupVo> list = getGroupList();
//		List<UserGroupVo> list =  getUserGroupList();
//		List<GroupFunctionVo> list = getFunctionGroupList();;
//		System.out.println(checkGroupsFunction());
//		getGroupsFunctionList();
		
		String[] t = "a,b,c".split(",");
	
		
		System.out.println(t[0]);
	}
	
	public static String addFunction(){
		return new FunctionDaoImpl().addFuncation(new FunctionVo(0,"test"));
	}

	/**
	 * 功能列表测试
	 * @Title:        getFunctionList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<FunctionVo>    
	 * @author        Albert
	 * @Date          2017年6月3日 上午10:45:29
	 */
	public static List<FunctionVo> getFunctionList(){
		List<FunctionVo> list = new FunctionDaoImpl().getFunctionList(false,null,null);
		return list; 
	}
	
	/**
	 * 子功能列表测试
	 * @Title:        getSubFunctionList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<FunctionVo>    
	 * @author        Albert
	 * @Date          2017年6月3日 上午10:45:42
	 */
	public static List<FunctionVo> getSubFunctionList(){
		List<FunctionVo> list = new FunctionDaoImpl().getSubFunctionList(11,null);
		return list;
	}
	
	/**
	 * 功能组列表测试
	 * @Title:        getFunctionGroupList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<GroupFunctionVo>    
	 * @author        Albert
	 * @Date          2017年6月3日 上午11:27:06
	 */
	public static List<GroupFunctionVo> getFunctionGroupList(){
		List<GroupFunctionVo> list = new GroupFunctionDaoImpl().getGroupFunctionList(1,null);
		return list;
	}
	
	/**
	 * 用户组列表测试
	 * @Title:        getGroupList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<GroupVo>    
	 * @author        Albert
	 * @Date          2017年6月3日 下午4:22:35
	 */
	public static List<GroupVo> getGroupList(){
		List<GroupVo> list = new GroupDaoImpl().getGroupList(null,0);
		return list;
	}
	
	/**
	 * 用户组关系列表测试
	 * @Title:        getUserGroupList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<UserGroupVo>    
	 * @author        Albert
	 * @Date          2017年6月3日 上午10:47:58
	 */
	public static List<UserGroupVo> getUserGroupList(){
		List<UserGroupVo> list = new UserGroupDaoImpl().getUserGroupList();
		return list;
	}
	
	/**
	 * 用户功能权限列表获取
	 * @Title:        getGroupsFunctionList 
	 * @Description:  TODO
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月5日 下午2:50:58
	 */
	public static void getGroupsFunctionList(){
		List<String> groupIds = new UserGroupDaoImpl().getUserGroupIdList(1);
		System.out.println(groupIds);
		List<FunctionVo> list = new GroupFunctionDaoImpl().getGroupsFunctionList(groupIds.toString().replace("[", "").replace("]", ""),null);
		System.out.println(list);
	}
	
	
	/**
	 * 用户权限验证
	 * @Title:        checkGroupsFunction 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月5日 下午2:37:09
	 */
	public static boolean checkGroupsFunction(){
		List<String> groupIds = new UserGroupDaoImpl().getUserGroupIdList(1);
		System.out.println(groupIds);
		return new GroupFunctionDaoImpl().checkGroupsFunction(groupIds.toString().replace("[", "").replace("]", ""),30001,"0",0+"");
	}
}
