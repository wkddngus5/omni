/**
 * FileName:     BleBikeVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月16日 下午3:25:19/3:25:19 pm, June 16,2017 
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化><initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     BleBikeVo
 * @Description:蓝牙单车实体定义类/bluetooth bicycle entity definition
 * @author:    Albert
 * @date:        2017年6月16日 下午3:25:19/3:25:19 pm, June 16,2017
 *
 */
public class BleBikeVo {
	String id;
	String bid;
	String mac;
	String date;
	BikeVo bikeVo;
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
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}
	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
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
	 * @return the bikeVo
	 */
	public BikeVo getBikeVo() {
		return bikeVo;
	}
	/**
	 * @param bikeVo the bikeVo to set
	 */
	public void setBikeVo(BikeVo bikeVo) {
		this.bikeVo = bikeVo;
	}
	
	public BleBikeVo(){
		
	}
	
	public BleBikeVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst,"id");
		this.bid = BaseDao.getString(rst,"bid");
		this.mac = BaseDao.getString(rst,"mac");
		this.date = BaseDao.getString(rst,"date");
		this.bikeVo = new BikeVo(rst);
	}
}
