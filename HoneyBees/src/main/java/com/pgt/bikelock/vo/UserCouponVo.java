/**
 * FileName:     UserCouponVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 下午3:26:34/3:26:34 pm, April 6, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.TimeUtil;

//import javax.xml.registry.infomodel.User;

 /**
 * @ClassName:     UserCouponVo
 * @Description:用户领取的优惠券/user received coupon
 * @author:    Albert
 * @date:        2017年4月6日 下午3:26:34/3:26:34 pm, April 6, 2017
 *
 */
public class UserCouponVo {
	String id;
	String uid;
	String cid;
	CouponVo couponVo;
	UserVo userVo;
	String code;//优惠券代码/coupon code
	String date;
	String active_date;
	BigDecimal amount;//优惠券余额/coupon amount
	int used;//是否已使用/used or not
	String start_time;//优惠开始时间/start time of coupon
	String end_time;//结束时间/end time
	int gift_from;//奖励类型 1：注册 2：邀请好友 3：故障上报 4：红包车
	
	public enum GiftFromType{
		Register,
		Invite,
		ErrorReport,
		LuckBike
	}
	
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
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}

	
	
	/**
	 * @return the couponVo
	 */
	public CouponVo getCouponVo() {
		return couponVo;
	}
	/**
	 * @param couponVo the couponVo to set
	 */
	public void setCouponVo(CouponVo couponVo) {
		this.couponVo = couponVo;
	}
	/**
	 * @return the userVo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	/**
	 * @param userVo the userVo to set
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the active_date
	 */
	public String getActive_date() {
		return active_date;
	}
	/**
	 * @param active_date the active_date to set
	 */
	public void setActive_date(String active_date) {
		this.active_date = active_date;
	}
	
	
	
	/**
	 * @return the used
	 */
	public int getUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public void setUsed(int used) {
		this.used = used;
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
	 * @return the gift_from
	 */
	public int getGift_from() {
		return gift_from;
	}
	
	public static int getGift_from(GiftFromType type) {
		int typeValue = 0;
		switch (type) {
		case Register:
			typeValue = 1;
			break;
		case Invite:
			typeValue = 2;
			break;
		case ErrorReport:
			typeValue = 3;
			break;
		case LuckBike:
			typeValue = 4;
			break;
		default:
			break;
		}
		return typeValue;
	}
	
	/**
	 * @param gift_from the gift_from to set
	 */
	public void setGift_from(int gift_from) {
		this.gift_from = gift_from;
	}
	
	public UserCouponVo(ResultSet rst,boolean formateDate) throws SQLException{
		this.id = BaseDao.getString(rst,"uc_id");
		if(StringUtils.isEmpty(this.id)){
			this.id = BaseDao.getString(rst,"id");
		}
		this.uid = BaseDao.getString(rst, "uid");
		this.cid = BaseDao.getString(rst,"cid");
		this.code = BaseDao.getString(rst,"code");
		this.date = BaseDao.getString(rst,"date");
		this.active_date = BaseDao.getString(rst,"active_date");
		this.amount = BaseDao.getBigDecimal(rst,"amount");
		this.used = BaseDao.getInt(rst,"used");
		this.start_time = BaseDao.getString(rst,"start_time");
		this.end_time = BaseDao.getString(rst,"end_time");
		this.gift_from = BaseDao.getInt(rst, "gift_from");
		if(formateDate){
			this.start_time = TimeUtil.formaStrDate(this.start_time, TimeUtil.Formate_YYYY_MM_dd_HH_mm);
			this.end_time =  TimeUtil.formaStrDate(this.end_time, TimeUtil.Formate_YYYY_MM_dd_HH_mm);
			this.userVo = new UserVo(rst);
		}
		this.couponVo = new CouponVo(rst);
		
		
	}
	
	public UserCouponVo(){
	}
	
	public UserCouponVo(String cid,String startTime,String endTime,String value){
		this.cid = cid;
		this.start_time = TimeUtil.formateStrDateToLongStr(startTime, TimeUtil.Formate_YYYY_MM_dd_HH_mm);
		this.end_time =  TimeUtil.formateStrDateToLongStr(endTime, TimeUtil.Formate_YYYY_MM_dd_HH_mm);
		this.amount = new BigDecimal(value) ;
	}
	
	
	public UserCouponVo(String[] parms,HttpServletRequest req){
		this.id = req.getParameter(parms[0]);
		this.uid= req.getParameter(parms[1]);
		this.cid= req.getParameter(parms[2]);
	}
}
