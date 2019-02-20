/**
 * FileName:     UserGroupVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月2日 上午10:49:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月2日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

 /**
 * @ClassName:     UserGroupVo
 * @Description:用户组实体
 * @author:    Albert
 * @date:        2017年6月2日 上午10:49:57
 *
 */
public class UserGroupVo {
	int id;
	String user_id;
	int group_id;
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
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	
	public UserGroupVo(ResultSet rst) throws SQLException{
		// TODO Auto-generated constructor stub
		this.id = rst.getInt("id");
		this.group_id = rst.getInt("group_id");
		this.user_id = rst.getString("user_id");
		this.date = rst.getString("date");
	}

}
