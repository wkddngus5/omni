/**
 * FileName:     TradeReceiptVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 下午5:19:25/5:19:25 pm, April 5, 2017
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

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName:     TradeReceiptVo
 * @Description:订单收据实体/order receipt entity
 * @author:    Albert
 * @date:        2017年4月5日 下午5:19:25/5:19:25 pm, April 5, 2017
 *
 */
public class TradeReceiptVo {
	String id;
	String uid;
	String trade_ids;
	String firstname;
	String lastname;
	String phone;
	String address;
	String zip_code;
	String country;
	int status;//状态 0：待处理 1：已处理/status 0: to be processed 1: processed 
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
	 * @return the trade_ids
	 */
	public String getTrade_ids() {
		return trade_ids;
	}
	/**
	 * @param trade_ids the trade_ids to set
	 */
	public void setTrade_ids(String trade_ids) {
		this.trade_ids = trade_ids;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the zip_code
	 */
	public String getZip_code() {
		return zip_code;
	}
	/**
	 * @param zip_code the zip_code to set
	 */
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
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
	public TradeReceiptVo(HttpServletRequest req,String[] parms,String uid){
		this.uid = uid;
		this.trade_ids = req.getParameter(parms[0]);
		this.firstname = req.getParameter(parms[1]);
		this.lastname = req.getParameter(parms[2]);
		this.phone = req.getParameter(parms[3]);
		this.address = req.getParameter(parms[4]);
		this.zip_code = req.getParameter(parms[5]);
		this.country = req.getParameter(parms[6]);

	}

	public TradeReceiptVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.uid = rst.getString("uid");
		this.trade_ids = rst.getString("trade_ids");
		this.firstname = rst.getString("firstname");
		this.lastname = rst.getString("lastname");
		this.phone = rst.getString("phone");
		this.address = rst.getString("address");
		this.zip_code = rst.getString("zip_code");
		this.country = rst.getString("country");
		this.status = rst.getInt("status");
	}
}
