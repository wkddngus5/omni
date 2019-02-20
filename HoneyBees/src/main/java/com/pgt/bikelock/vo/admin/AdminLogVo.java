/**
 * FileName:     AdminLogVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月15日 下午3:12:46/3:12:46 pm, April 15, 2017  
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月15日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<Initializing>
 */
package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     AdminLogVo
 * @Description:管理员日志实体/Administrator log entity
 * @author:    Albert
 * @date:        2017年4月15日 下午3:12:46/3:12:46 pm, April 15, 2017
 *
 */
public class AdminLogVo {
	
	String id;
	String dataId;
	String date;
	AdminVo adminVo;
	String funcName;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the dataId
	 */
	public String getDataId() {
		return dataId;
	}
	/**
	 * @param dataId the dataId to set
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
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
	 * @return the adminVo
	 */
	public AdminVo getAdminVo() {
		return adminVo;
	}
	/**
	 * @param adminVo the adminVo to set
	 */
	public void setAdminVo(AdminVo adminVo) {
		this.adminVo = adminVo;
	}

	
	
	/**
	 * @return the funcName
	 */
	public String getFuncName() {
		return funcName;
	}
	/**
	 * @param funcName the funcName to set
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	public AdminLogVo(ResultSet rst,ResourceBundle rb) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.date =BaseDao.getString(rst, "date");
		this.dataId = BaseDao.getString(rst, "data_id");
		if(!StringUtils.isEmpty(BaseDao.getString(rst, "parent_name"))){
			this.funcName = rb.getString(BaseDao.getString(rst, "parent_name")) +"-"+ rb.getString(BaseDao.getString(rst, "name"));
		}else{
			this.funcName = rb.getString(BaseDao.getString(rst, "name"));
		}
		
		this.adminVo = new AdminVo(rst);
	}
}
