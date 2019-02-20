/**
 * FileName:     UserAddressVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 下午4:56:00/4:56:00 pm, April 5, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月5日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

 /**
 * @ClassName:     UserAddressVo
 * @Description:用户常用地址实体/User address entity 
 * @author:    Albert
 * @date:        2017年4月5日 下午4:56:01/4:56:00 pm, April 5, 2017
 *
 */
public class UserAddressVo {
	String id;
	String uid;
	String title;
	String detail;
	double lan;
	double lng;
	int is_default;
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
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the lan
	 */
	public double getLan() {
		return lan;
	}
	/**
	 * @param lan the lan to set
	 */
	public void setLan(double lan) {
		this.lan = lan;
	}
	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	/**
	 * @return the is_default
	 */
	public int getIs_default() {
		return is_default;
	}
	/**
	 * @param is_default the is_default to set
	 */
	public void setIs_default(int is_default) {
		this.is_default = is_default;
	}
	public UserAddressVo(ResultSet rst) throws SQLException{
		this.uid = rst.getString("uid");
		this.title = rst.getString("title");
		this.detail = rst.getString("detail");
		this.lan = rst.getDouble("lan");
		this.lng = rst.getDouble("lng");
	}
}
