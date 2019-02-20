/**
 * FileName:     BikeReserveVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-25 下午4:56:44/4:56:44 pm, March 25, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-25       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     BikeReserveVo
 * @Description:单车预约实体/bicycle reservation entity
 * @author:    Albert
 * @date:        2017-3-25 下午4:56:44/4:56:44 pm, March 25, 2017
 *
 */
public class BikeReserveVo {
	
	public static final int Reserve_TimeOut =  OthersSource.getSourceString("bike_reserve_out_time") != null ? ValueUtil.getInt(OthersSource.getSourceString("bike_reserve_out_time")):15;//预约过期时间/reservation expiration time
	public static final int Reserve_Cancel_Count_In_Day =  OthersSource.getSourceString("bike_reserve_cancel_count_in_day") != null ? ValueUtil.getInt(OthersSource.getSourceString("bike_reserve_cancel_count_in_day")):10;//一天取消预约次数/Cancel count in day
	
	String rid;//单车预约记录ID/bicycle resevration log ID
	String bid;//单车ID/bicycle ID
	BikeVo bikeVo;//单车实体/bicycle entity
	String uid;//用户ID/User ID
	String date;//预约时间/reservation time
	String out_date;//过期时间/expirtion time
	int status;//预约状态 0：待解锁 1：已解锁 2：已解其他锁 3：已取消 4：已超时

	/**
	 * @return the rid
	 */
	public String getRid() {
		return rid;
	}
	/**
	 * @param rid the rid to set
	 */
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
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
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * @return the out_date
	 */
	public String getOut_date() {
		return out_date;
	}
	/**
	 * @param out_date the out_date to set
	 */
	public void setOut_date(String out_date) {
		this.out_date = out_date;
	}
	
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * 
	 * @param rst
	 * @param type 1:预约详情 2：预约记录
	 * @throws SQLException
	 */
	public BikeReserveVo(ResultSet rst,int type) throws SQLException{

		this.date = rst.getString("date");
		this.out_date = rst.getString("out_date");
		if(type == 1){
			this.rid = rst.getString("id");
			this.bid = rst.getString("bid");
			this.uid = rst.getString("uid");
		}else if(type == 2){
			this.bikeVo = new BikeVo(rst);
		}
		this.status = BaseDao.getInt(rst, "status");
	}
}
