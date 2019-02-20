/**
 * FileName:     BikeRedPackVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 上午10:23:47/10:23:47 am, April 26, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo.admin;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.TimeUtil;

 /**
 * @ClassName:     BikeRedPackVo
 * @Description:单车红包实体/bicycle red packet entity 
 * @author:    Albert
 * @date:        2017年4月26日 上午10:23:47/10:23:47 am, April 26, 2017
 *
 */
public class RedPackBikeVo {
	String id;
	String bid;//红包被绑定单车ID//bicycle ID that red packet is binded to
	String uid;//红包领取用户ID//user ID receiving red packet
	String rule_id;//规则ID//rule ID
	BigDecimal amount;//红包金额//red packet amount
	String start_time;//红包开始时间//start time of red packet 
	String end_time;//
	String date;//红包生成时间//generated time of red packet
	String user_date;//红包领取时间//receiving time of red packet
	//显示属性//display attributes
	String number;
	String phone;
	int count;//红包数//red packet quantities
	
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
	 * @return the rule_id
	 */
	public String getRule_id() {
		return rule_id;
	}
	/**
	 * @param rule_id the rule_id to set
	 */
	public void setRule_id(String rule_id) {
		this.rule_id = rule_id;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * @return the user_date
	 */
	public String getUser_date() {
		return user_date;
	}
	/**
	 * @param user_date the user_date to set
	 */
	public void setUser_date(String user_date) {
		this.user_date = user_date;
	}
	
	
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	public RedPackBikeVo(){
		
	}
	
	/**
	 * 
	 * @param rst
	 * @param type 1：基础 2：详情
	 * @throws SQLException
	 */
	public RedPackBikeVo(ResultSet rst,int type) throws SQLException{
		this.id = rst.getString("id");
		this.bid = BaseDao.getString(rst,"bid");
		this.rule_id = rst.getString("rule_id");
		this.start_time = rst.getString("start_time");
		this.end_time = rst.getString("end_time");
		this.amount = rst.getBigDecimal("amount");
		this.date = rst.getString("date");
		this.user_date = rst.getString("user_date");
		this.count = BaseDao.getInt(rst, "count");
		this.uid = BaseDao.getString(rst, "uid");
		if(type == 2){
			this.number = rst.getString("number");
			this.phone = rst.getString("phone");
		}
	}
	
	public RedPackBikeVo(String[] parms,HttpServletRequest req){
		this.rule_id = req.getParameter(parms[1]);
		this.start_time = TimeUtil.formateStrDateToLongStr(req.getParameter(parms[2]), TimeUtil.Formate_YYYY_MM_dd_HH_mm);
		this.end_time = TimeUtil.formateStrDateToLongStr(req.getParameter(parms[3]), TimeUtil.Formate_YYYY_MM_dd_HH_mm);;
	}

}
