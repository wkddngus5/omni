/**
 * FileName:     SystemConfigVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月30日 下午6:41:42
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.ValueUtil;

/**
 * @ClassName:     SystemConfigVo
 * @Description:系统设置实体/system configuration entity
 * @author:    Albert
 * @date:        2017年11月30日 下午6:41:42
 *
 */
public class SystemConfigVo {
	int id;
	String name;//配置名称/name
	String config;//配置详情（JSON格式）/json detail
	String date;
	String key;//配置键
	String note;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the config
	 */
	public String getConfig() {
		return config;
	}
	/**
	 * @param config the config to set
	 */
	public void setConfig(String config) {
		this.config = config;
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
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	public SystemConfigVo(){

	}
	
	public SystemConfigVo(HttpServletRequest request){
		this.id = ValueUtil.getInt(request.getParameter("id"));
		this.name = request.getParameter("name");
		this.config = request.getParameter("config");
		this.key = request.getParameter("key");
		this.note = request.getParameter("note");
	}

	public SystemConfigVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst, "id");
		this.config =BaseDao.getString(rst, "config");
		this.name= BaseDao.getString(rst, "name");
		this.date = BaseDao.getString(rst, "date");
		this.key = BaseDao.getString(rst, "cf_key");
		this.note = BaseDao.getString(rst, "note");
	}
}
