/**
 * FileName:     BikeCardPhone.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月16日 下午5:00:05/5:00:05 pm, June 16, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     BikeCardPhone
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月16日 下午5:00:05/5:00:05 pm, June 16, 2017
 *
 */
public class BikeCardPhoneVo {
	String id;
	String iccid;
	String phone;
	String date;
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
	 * @return the iccid
	 */
	public String getIccid() {
		return iccid;
	}
	/**
	 * @param iccid the iccid to set
	 */
	public void setIccid(String iccid) {
		this.iccid = iccid;
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
	
	public BikeCardPhoneVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst,"id");
		this.iccid = BaseDao.getString(rst,"iccid");
		this.phone = BaseDao.getString(rst,"phone");
		this.date = BaseDao.getString(rst,"date");
	}
}
