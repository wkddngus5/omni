/**
 * FileName:     BikeTypeVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-28 上午11:07:47/11:07:47 am, March 28, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-28       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     BikeTypeVo
 * @Description:单车类型实体/bicycle type entity
 * @author:    Albert
 * @date:        2017-3-28 上午11:07:47/11:07:47 am, March 28, 2017
 *
 */
public class BikeTypeVo {
	String id;
	BigDecimal price;//单车单价(单位：分)/unit price per bicycle(unit: minute)
	int unit_type;//单位 1：分钟 2：小时 3：月份/unit 1:minute 2:hour 3: month
	int count;//数量/quantity
	int city_id;
	int lock_type;//锁类型 1：GPRS(默认) 2：BLE 3：GPRS+BLE 4：GPRS+BLE+SMS/Lock type 1:GPRS(default) 2:BLE 3: GPRS+BLE 4：GPRS+BLE+SMS 
	/**显示属性**//**display attributes**/
	String typeAndValue;
	BigDecimal hold_price;
	int hold_unit_type;
	int hold_count;
	int hold_max_count;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public int getMaxHoldDurationSeconds () {

		switch (hold_unit_type) {

			case 1: return hold_max_count * 60;
			case 2: return hold_max_count * 3600;
			case 3: return hold_max_count * 3600 * 730;

		}
		return 0;

	}


	public int getHoldPeriodDurationSeconds () {

		switch (hold_unit_type) {

			case 1: return hold_count * 60;
			case 2: return hold_count * 3600;
			case 3: return hold_count * 3600 * 730;

		}
		return 0;

	}


	public String getHoldUnitTypeDescription () {

		switch (hold_unit_type) {

			case 1: return "minute";
			case 2: return "hour";
			case 3: return "month";

		}
		return null;

	}
	
	
	/**
	 * @return the unit_type
	 */
	public int getUnit_type() {
		return unit_type;
	}
	/**
	 * @param unit_type the unit_type to set
	 */
	public void setUnit_type(int unit_type) {
		this.unit_type = unit_type;
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


	public BigDecimal getHoldPrice () {
		return hold_price;
	}

	public void setHoldPrice (BigDecimal hold_price) {
		this.hold_price = hold_price;
	}


	public int getHoldUnitType () {
		return hold_unit_type;
	}

	public void setHoldUnitType (int hold_unit_type) {
		this.hold_unit_type = hold_unit_type;
	}


	public int getHoldCount () {
		return hold_count;
	}

	public void setHoldCount (int hold_count) {
		this.hold_count = hold_count;
	}


	public int getHoldMaxCount () {
		return hold_max_count;
	}

	public void setHoldMaxCount (int hold_max_count) {
		this.hold_max_count = hold_max_count;
	}
	
	
	/**
	 * @return the city_id
	 */
	public int getCity_id() {
		return city_id;
	}
	/**
	 * @param city_id the city_id to set
	 */
	public void setCity_id(int city_id) {
		this.city_id = city_id;
	}
	
	
	
	
	/**
	 * @return the lock_type
	 */
	public int getLock_type() {
		return lock_type;
	}
	/**
	 * @param lock_type the lock_type to set
	 */
	public void setLock_type(int lock_type) {
		this.lock_type = lock_type;
	}
	
	/**
	 * 
	 * @Title:        haveBleLock 
	 * @Description:  判断锁是否包含蓝牙功能/tell the lock has bluetooth or not
	 * @param:        @param lock_type
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月13日 上午10:34:44/10:34:44 am, July 13, 2017
	 */
	public boolean haveBleLock(){
		if(this.lock_type > 1){
			return true;
		}
		return false;
	}
	
	public boolean haveGprsLock(){
		if(this.lock_type != 2){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @Title:        getEndTime 
	 * @Description:  计算长租结束时间/caculating the end time of long rent
	 * @param:        @param startTime
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月6日 上午11:14:23/11:14:23 am, April 6, 2017
	 */
	public String getEndTime(long startTime){
		long value = 0;
		//1：分钟 2：小时 3：月份/1: minute 2: hour 3: month
		if(this.unit_type == 1){
			value = this.count*60;
		}else if(this.unit_type == 2){
			value = this.count*3600;
		}else if(this.unit_type == 3){
			value = this.count*3600*24*30;
		}
		long endTime = startTime + value;
		
		return endTime+"";
	}
	
	/**
	 * @return the typeAndValue
	 */
	public String getTypeAndValue() {
		typeAndValue = this.price +"/";
		if(this.unit_type == 1){
			typeAndValue += "Minutes";
		}else if(this.unit_type == 2){
			typeAndValue += "Times";
		}else if(this.unit_type == 3){
			typeAndValue += "Monthes";
		}
		return typeAndValue ;
	} 
	/**
	 * @param typeAndValue the typeAndValue to set
	 */
	public void setTypeAndValue(String typeAndValue) {
		this.typeAndValue = typeAndValue;
	}
	
	
	public BikeTypeVo(){
		
	}

	public BikeTypeVo(String[] parms,HttpServletRequest req){
		this.id = req.getParameter("id");
		this.price= new BigDecimal(req.getParameter(parms[0]));
		this.count = ValueUtil.getInt(req.getParameter(parms[1]));
		this.city_id = ValueUtil.getInt(req.getParameter(parms[2]));
		this.unit_type = ValueUtil.getInt(req.getParameter(parms[3]));
		this.hold_unit_type = ValueUtil.getInt(req.getParameter(parms[4]));
		this.hold_count = ValueUtil.getInt(req.getParameter(parms[5]));
		this.hold_price = new BigDecimal(req.getParameter(parms[6]));
		this.hold_max_count = ValueUtil.getInt(req.getParameter(parms[7]));
	}
	
	/**
	 * 
	 * @param rst
	 * @throws SQLException
	 */
	public BikeTypeVo(ResultSet rst) throws SQLException {
		this.id = BaseDao.getString(rst,"id");
		this.price = BaseDao.getBigDecimal(rst, "price");
		this.unit_type = BaseDao.getInt(rst,"unit_type");
		this.count = BaseDao.getInt(rst,"count");
		this.lock_type = BaseDao.getInt(rst, "lock_type");
		this.city_id = BaseDao.getInt(rst, "city_id");
		this.hold_price = BaseDao.getBigDecimal(rst, "hold_price");
		this.hold_unit_type = BaseDao.getInt(rst, "hold_unit_type");
		this.hold_count = BaseDao.getInt(rst, "hold_count");
		this.hold_max_count = BaseDao.getInt(rst, "hold_max_count");
	}
	
}
