/**
 * FileName:     BikeMaintain.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月27日 下午4:39:26/4:39:26 pm, July 27, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月27日/July 27, 2017       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<Initializing>
 */
package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     BikeMaintain
 * @Description:单车维护实体/Bicycle Maintenance Entity
 * @author:    Albert
 * @date:        2017年7月27日 下午4:39:26/4:39:26 pm, July 27, 2017
 *
 */
public class BikeMaintainVo {
	String id;
	String bid;
	int type;//维护类型 1：移动 2：维修/maintenenance type 1: mobile 2: maintenace/
	int status;//状态 0：待操作 1：已完成/status 0: to be operated 1: completed/
	String date;//添加时间/add date/
	String admin_id;//处理管理员ID/processing the administrator ID/
	String deal_date;//处理时间//processing the time
	String note;
	/**显示属性**//**display attributes**/
	String nickname;
	String number;
	int cityId;
	String typeStr;
	String statusStr;
	
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
	 * @return the admin_id
	 */
	public String getAdmin_id() {
		return admin_id;
	}
	/**
	 * @param admin_id the admin_id to set
	 */
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	/**
	 * @return the deal_date
	 */
	public String getDeal_date() {
		return deal_date;
	}
	/**
	 * @param deal_date the deal_date to set
	 */
	public void setDeal_date(String deal_date) {
		this.deal_date = deal_date;
	}
	
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
	
	
	/**
	 * @return the typeStr
	 */
	public String getTypeStr() {
		return typeStr;
	}
	/**
	 * @param typeStr the typeStr to set
	 */
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	/**
	 * @return the statusStr
	 */
	public String getStatusStr() {
		return statusStr;
	}
	/**
	 * @param statusStr the statusStr to set
	 */
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	public BikeMaintainVo(){
		
	}
	
	public BikeMaintainVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.bid = BaseDao.getString(rst, "bid");
		this.type =  BaseDao.getInt(rst, "type");
		setTypeStr(LanguageUtil.getTypeStr("bike_maintain_type", this.type));
		this.status =  BaseDao.getInt(rst, "status");
		setStatusStr(LanguageUtil.getStatusStr(this.status,null));
		this.date = BaseDao.getString(rst, "date");
		this.admin_id = BaseDao.getString(rst, "admin_id");
		this.deal_date = BaseDao.getString(rst, "deal_date");
		this.nickname = BaseDao.getString(rst, "nickname");
		this.number = BaseDao.getString(rst, "number");
		this.cityId =  BaseDao.getInt(rst, "city_id");
		this.note = BaseDao.getString(rst, "note");
	}
	
	public BikeMaintainVo(HttpServletRequest req){
		this.id = req.getParameter("id");
		this.bid  = req.getParameter("bikeVo.id");
		this.type = ValueUtil.getInt(req.getParameter("type"));
		this.status = ValueUtil.getInt(req.getParameter("status"));
		if(this.status == 1){
			this.deal_date = TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm);
		}
		this.admin_id = req.getParameter("adminVo.id");
		this.note = req.getParameter("note");
	}
	
}
