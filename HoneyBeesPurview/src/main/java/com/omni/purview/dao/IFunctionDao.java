/**
 * FileName:     IFunctionDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月18日 下午5:27:14
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月18日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.dao;

import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

import com.omni.purview.vo.FunctionVo;

 /**
 * @ClassName:     IFunctionDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月18日 下午5:27:14
 *
 */
public interface IFunctionDao {

	/**
	 * 
	 * @Title:        addFuncation 
	 * @Description:  增加功能
	 * @param:        @param function
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月24日 下午5:40:50
	 */
	String addFuncation(FunctionVo function);
	
	/**
	 * 
	 * @Title:        getFunctionList 
	 * @Description:  获取所有功能列表
	 * @param:        @param rb
	 * @param:        @return    
	 * @return:       List<FunctionVo>    
	 * @author        Albert
	 * @Date          2017年6月15日 下午3:33:50
	 */
	List<FunctionVo> getFunctionList(ResourceBundle rb,List<String> existFunc);
	
	/**
	 * 
	 * @Title:        getFunctionList 
	 * @Description:  功能列表获取
	 * @param:        @return    
	 * @return:       List<FunctionVo>    
	 * @author        Albert
	 * @Date          2017年5月24日 下午5:41:24
	 */
	List<FunctionVo> getFunctionList(boolean thirdFun,List<String> existFunc,ResourceBundle rb);
	
	/**
	 * 
	 * @Title:        getFunctionList 
	 * @Description:  TODO
	 * @param:        @param thirdFun 
	 * @param:        @param existFunc
	 * @param:        @param rb
	 * @param:        @param currentGroup
	 * @param:        @return    
	 * @return:       List<FunctionVo>    
	 * @author        Albert
	 * @Date          2018年1月26日 上午11:43:50
	 */
	List<FunctionVo> getFunctionList(boolean thirdFun,List<String> existFunc,ResourceBundle rb,String currentGroup);
	
	/**
	 * 
	 * @Title:        getSubFunctionList 
	 * @Description:  子菜单获取
	 * @param:        @param parentId
	 * @param:        @return    
	 * @return:       List<FunctionVo>    
	 * @author        Albert
	 * @Date          2017年5月24日 下午5:47:12
	 */
	List<FunctionVo> getSubFunctionList(int parentId,Connection conn,boolean thirdFun,List<String> existFunc,ResourceBundle rb);
	
	/**
	 * 
	 * @Title:        getSubFunctionList 
	 * @Description:  子菜单获取
	 * @param:        @param parentId
	 * @param:        @return    
	 * @return:       List<FunctionVo>    
	 * @author        Albert
	 * @Date          2017年5月27日 下午4:54:04
	 */
	List<FunctionVo> getSubFunctionList(int parentId,ResourceBundle rb);
	
	/**
	 * 
	 * @Title:        checkParentFunction 
	 * @Description:  检查父级功能，防止父级功能遗漏
	 * @param:        @param funcIds
	 * @param:        @return    
	 * @return:       List<String>    
	 * @author        Albert
	 * @Date          2018年1月26日 下午3:21:23
	 */
	List<String> checkParentFunction(String funcIds,List<String> funcList);
}
