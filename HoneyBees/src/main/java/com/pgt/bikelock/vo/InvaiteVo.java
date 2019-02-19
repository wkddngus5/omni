/**
 * FileName:     InvaiteVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月7日 下午8:23:44
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月7日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     InvaiteVo
 * @Description:好友邀请实体/User invaite entity
 * @author:    Albert
 * @date:        2017年11月7日 下午8:23:44
 *
 */
public class InvaiteVo {
	String id;
	String uid;//join user id
	String i_uid;//invite user id
	String date;
	
	/**显示属性**/
	String phone;//join user phone
	String iphone;//invite user phone
	
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
	 * @return the i_uid
	 */
	public String getI_uid() {
		return i_uid;
	}
	/**
	 * @param i_uid the i_uid to set
	 */
	public void setI_uid(String i_uid) {
		this.i_uid = i_uid;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the iphone
	 */
	public String getIphone() {
		return iphone;
	}
	/**
	 * @param iphone the iphone to set
	 */
	public void setIphone(String iphone) {
		this.iphone = iphone;
	}
	public InvaiteVo(){
		
	}
	
	public InvaiteVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.uid = BaseDao.getString(rst, "uid");
		this.i_uid = BaseDao.getString(rst, "i_uid");
		this.phone = BaseDao.getString(rst, "phone");
		this.iphone = BaseDao.getString(rst, "i_phone");
		this.date = BaseDao.getString(rst, "date");
	}
}
