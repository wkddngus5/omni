/**
 * FileName:     BikeLongLeaseVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 上午11:24:28/11:24:28 am, April 5, 2017
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

import com.pgt.bikelock.utils.TimeUtil;

 /**
 * @ClassName:     BikeLongLeaseVo
 * @Description:单车长租实体/bicycle long term rent entity
 * @author:    Albert
 * @date:        2017年4月5日 上午11:24:28/11:24:28 am, April 5, 2017
 *
 */
public class BikeLongLeaseVo {
	String id;
	String uid;
	String start_time;
	String end_time;
	int ispay;//是否已支付 0：未支付 1：已支付 2:免费// payment status 0: unpaid 1: paid 2:free
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
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}
	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}
	/**
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	/**
	 * @return the ispay
	 */
	public int getIspay() {
		return ispay;
	}
	/**
	 * @param ispay the ispay to set
	 */
	public void setIspay(int ispay) {
		this.ispay = ispay;
	}
	
	public BikeLongLeaseVo(String uid){
		this.uid = uid;
		this.start_time = TimeUtil.getCurrentLongTimeStr();
	}
	
	public BikeLongLeaseVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.uid = rst.getString("uid");
		this.start_time = rst.getString("start_time");
		this.end_time = rst.getString("end_time");
	}

}
