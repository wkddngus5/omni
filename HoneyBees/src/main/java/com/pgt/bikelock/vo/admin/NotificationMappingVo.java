/**
 * FileName:     NotificationMappingVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月25日 下午4:39:52
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月25日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     NotificationMappingVo
 * @Description:Notification mapping empty
 * @author:    Albert
 * @date:        2017年11月25日 下午4:39:52
 *
 */
public class NotificationMappingVo {
	String id;
	String group_id;
	String notify_config_type;
	String date;
	/***/
	boolean checked;
	String name;
	int type;
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
	 * @return the group_id
	 */
	public String getGroup_id() {
		return group_id;
	}
	/**
	 * @param group_id the group_id to set
	 */
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	/**
	 * @return the notify_config_type
	 */
	public String getNotify_config_type() {
		return notify_config_type;
	}
	/**
	 * @param notify_config_type the notify_config_type to set
	 */
	public void setNotify_config_type(String notify_config_type) {
		this.notify_config_type = notify_config_type;
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
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}
	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	public NotificationMappingVo(){
		
	}
	
	public NotificationMappingVo(String name,int type){
		this.name = name;
		this.type = type;
	}
	
	public NotificationMappingVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.group_id =BaseDao.getString(rst, "group_id");
		this.notify_config_type = BaseDao.getString(rst, "notify_config_type");
		this.date = BaseDao.getString(rst, "date");
	}
}
