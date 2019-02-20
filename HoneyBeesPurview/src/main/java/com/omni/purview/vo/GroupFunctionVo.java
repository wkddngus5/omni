/**
 * FileName:     GroupPurviewVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月1日 下午8:04:51
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.omni.purview.dao.impl.BaseDao;

 /**
 * @ClassName:     GroupPurviewVo
 * @Description:权限组功能映射实体
 * @author:    Albert
 * @date:        2017年6月1日 下午8:04:51
 *
 */
public class GroupFunctionVo {

	int id;
	int group_id;
	int function_id;
	FunctionVo functionVo;
	String date;
	
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}



	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}



	/**
	 * @return the group_id
	 */
	public int getGroup_id() {
		return group_id;
	}



	/**
	 * @param group_id the group_id to set
	 */
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}





	/**
	 * @return the function_id
	 */
	public int getFunction_id() {
		return function_id;
	}



	/**
	 * @param function_id the function_id to set
	 */
	public void setFunction_id(int function_id) {
		this.function_id = function_id;
	}



	/**
	 * @return the functionVo
	 */
	public FunctionVo getFunctionVo() {
		return functionVo;
	}



	/**
	 * @param functionVo the functionVo to set
	 */
	public void setFunctionVo(FunctionVo functionVo) {
		this.functionVo = functionVo;
	}



	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}



	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}



	/**
	 * 
	 */
	public GroupFunctionVo(ResultSet rst,ResourceBundle rb) throws SQLException{
		// TODO Auto-generated constructor stub
		this.id = BaseDao.getInt(rst, "id");
		this.group_id = rst.getInt("group_id");
		this.function_id = rst.getInt("function_id");
		this.functionVo = new FunctionVo(rst,null,rb);
	}

}
