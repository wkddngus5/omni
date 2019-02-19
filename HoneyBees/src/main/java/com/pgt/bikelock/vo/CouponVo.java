/**
 * FileName:     CouponVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 下午3:20:17/3:20:17 pm, April 6, 2017
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

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     CouponVo
 * @Description:优惠券实体/coupon entity
 * @author:    Albert
 * @date:        2017年4月6日 下午3:20:17/3:20:17 pm, April 6, 2017
 *
 */
public class CouponVo {
	String id;
	String name;//优惠券名称/coupon name
	BigDecimal value;//优惠值 (单位：分) coupon amount (unit: minute)
	int day;//优惠天数/coupon days
	int type;//优惠类型 1：折扣 2：抵扣 3:新用户免费骑行 4:限时免费/coupon type 1: discount 2: deduction 3: free cycling for new user
	int repeat;//是否重复使用/reuse or not
	String start_time;//优惠开始时间/start tiem of coupon
	String end_time;//结束时间/end time
	int cityId;//城市ID/city ID

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
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
	 * @return the repeat
	 */
	public int getRepeat() {
		return repeat;
	}
	/**
	 * @param repeat the repeat to set
	 */
	public void setRepeat(int repeat) {
		this.repeat = repeat;
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
	
	
	

	public CouponVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst,"id");
		this.value = BaseDao.getBigDecimal(rst,"value");
		this.name = BaseDao.getString(rst,"name");
		this.type =  BaseDao.getInt(rst,"type");
		this.day =  BaseDao.getInt(rst,"day");
		this.repeat =  BaseDao.getInt(rst,"isrepeat");
		this.start_time = BaseDao.getString(rst,"start_time");
		this.end_time = BaseDao.getString(rst,"end_time");
		this.cityId = BaseDao.getInt(rst,"city_id");
	
	}

	public CouponVo(String[] parms,HttpServletRequest req){
		this.name = req.getParameter(parms[0]);
		this.type= ValueUtil.getInt(req.getParameter(parms[1]));
		this.start_time = TimeUtil.formateStrDateToLongStr(req.getParameter(parms[2]), TimeUtil.Formate_YYYY_MM_dd_HH_mm);
		this.end_time = TimeUtil.formateStrDateToLongStr(req.getParameter(parms[3]), TimeUtil.Formate_YYYY_MM_dd_HH_mm);;
		this.cityId = ValueUtil.getInt(req.getAttribute(parms[4]));
		this.value =  ValueUtil.getBigDecimal(req.getParameter("value"));
		this.day = ValueUtil.getInt(req.getParameter("day"));
		this.repeat = ValueUtil.getInt(req.getParameter("repeat"));
		
	}
}
