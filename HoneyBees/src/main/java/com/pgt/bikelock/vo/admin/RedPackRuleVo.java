/**
 * FileName:     RedPackageRuleVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 上午10:23:08/10:23:08 am, April 26, 2017
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
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     RedPackageRuleVo
 * @Description:红包规则实体/red packet rule entity
 * @author:    Albert
 * @date:        2017年4月26日 上午10:23:08/10:23:08 am, April 26, 2017
 *
 */
public class RedPackRuleVo {
	String id;
	String free_use_time;//骑行免费时间//free time of cycling
	String least_use_time;//至少骑行时间可以获取红包//least cycling time to receive red packet
	BigDecimal max_amount;//最高金额//maximum amount
	String start_time;//红包开始时间//start time of red packet
	String end_time;//end_time
	String date;//红包领取时间//receiving time of red packet
	String name;//规则名称//rule name
	//2017年7月18日15:24:33，红包功能扩展为骑行区域后奖励优惠券//15:24:33, April 26, 2017, rewarding coupon after the red packet expanded to the riding area
	int type;
	String coupon_id;//优惠券ID//coupon ID
	int coupon_num;//优惠券数//coupon quantity
	String area_ids;//区域ID组合//area ID group
	int must_in_area;//是否必须骑行至目标区域//whether must ride to the target area
	
	/**显示属性**//**display attributes**/
	String couponName;
	String areaName;
	
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
	 * @return the free_use_time
	 */
	public String getFree_use_time() {
		return free_use_time;
	}
	/**
	 * @param free_use_time the free_use_time to set
	 */
	public void setFree_use_time(String free_use_time) {
		this.free_use_time = free_use_time;
	}
	/**
	 * @return the least_use_time
	 */
	public String getLeast_use_time() {
		return least_use_time;
	}
	/**
	 * @param least_use_time the least_use_time to set
	 */
	public void setLeast_use_time(String least_use_time) {
		this.least_use_time = least_use_time;
	}
	/**
	 * @return the max_amount
	 */
	public BigDecimal getMax_amount() {
		return max_amount;
	}
	/**
	 * @param max_amount the max_amount to set
	 */
	public void setMax_amount(BigDecimal max_amount) {
		this.max_amount = max_amount;
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
	 * @return the coupon_id
	 */
	public String getCoupon_id() {
		return coupon_id;
	}
	/**
	 * @param coupon_id the coupon_id to set
	 */
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	/**
	 * @return the coupon_num
	 */
	public int getCoupon_num() {
		return coupon_num;
	}
	/**
	 * @param coupon_num the coupon_num to set
	 */
	public void setCoupon_num(int coupon_num) {
		this.coupon_num = coupon_num;
	}
	/**
	 * @return the area_ids
	 */
	public String getArea_ids() {
		return area_ids;
	}
	/**
	 * @param area_ids the area_ids to set
	 */
	public void setArea_ids(String area_ids) {
		this.area_ids = area_ids;
	}
	/**
	 * @return the must_in_area
	 */
	public int getMust_in_area() {
		return must_in_area;
	}
	/**
	 * @param must_in_area the must_in_area to set
	 */
	public void setMust_in_area(int must_in_area) {
		this.must_in_area = must_in_area;
	}
	
	
	
	
	
	/**
	 * @return the couponName
	 */
	public String getCouponName() {
		return couponName;
	}
	/**
	 * @param couponName the couponName to set
	 */
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	
	/**
	 * @return the areaName
	 */
	public String getAreaName() {
		return areaName;
	}
	/**
	 * @param areaName the areaName to set
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	public RedPackRuleVo(){
		
	}
	
	public RedPackRuleVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst,"id");
		this.start_time = BaseDao.getString(rst,"start_time");
		this.end_time = BaseDao.getString(rst,"end_time");
		this.free_use_time = BaseDao.getString(rst,"free_use_time");
		this.least_use_time = BaseDao.getString(rst,"least_use_time");
		this.max_amount = BaseDao.getBigDecimal(rst,"max_amount");
		this.date = BaseDao.getString(rst,"date");
		this.name = BaseDao.getString(rst,"name");
		this.type =  BaseDao.getInt(rst,"type");
		this.coupon_id = BaseDao.getString(rst,"coupon_id");
		this.coupon_num =  BaseDao.getInt(rst,"coupon_num");
		this.couponName =  BaseDao.getString(rst,"coupon_name");
		this.area_ids = BaseDao.getString(rst,"area_ids");
		this.must_in_area =  BaseDao.getInt(rst,"must_in_area");
	
	}

	public RedPackRuleVo(HttpServletRequest req){
	
		this.start_time = TimeUtil.formateStrDateToLongStr(req.getParameter("start_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm);
		this.end_time = TimeUtil.formateStrDateToLongStr(req.getParameter("end_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm);;
		if(req.getParameter("id") != null){
			this.id = req.getParameter("id");
		}
		this.type = ValueUtil.getInt(req.getParameter("type"));
		this.name = req.getParameter("name");
		if(this.type == 1){
			this.free_use_time = req.getParameter("free_use_time");
			this.least_use_time = req.getParameter("least_use_time");
			this.max_amount = new BigDecimal(req.getParameter("max_amount"));
		}else if(this.type == 2){
			this.coupon_id = req.getParameter("couponVo.id");
			this.coupon_num = ValueUtil.getInt(req.getParameter("coupon_num"));
			this.area_ids = req.getParameter("areaVo.id");
			this.must_in_area = ValueUtil.getInt(req.getParameter("must_in_area"));
		}

	}
}
