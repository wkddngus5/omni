/**
 * FileName:     RechargeAmountVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 下午2:59:22/2:59:22 pm, May 9, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
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
 * @ClassName:     RechargeAmountVo
 * @Description:充值金额实体/recharge amount entity
 * @author:    Albert
 * @date:        2017年5月9日 下午2:59:22/2:59:22 pm, May 9, 2017
 *
 */
public class RechargeAmountVo {
	String id;
	BigDecimal amount;//充值金额/recharge amount
	BigDecimal gift;//赠送金额/gift amount
	String date;//编辑日期/edit date
	int cityId;
	int gift_type;//赠送类型 1：余额 2：优惠券/gift type 1:balance 2: coupon
	int gift_id;//赠送关联ID/gift correlation ID
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
	 * @return the gift
	 */
	public BigDecimal getGift() {
		return gift;
	}
	/**
	 * @param gift the gift to set
	 */
	public void setGift(BigDecimal gift) {
		this.gift = gift;
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
	 * @return the gift_type
	 */
	public int getGift_type() {
		return gift_type;
	}
	/**
	 * @param gift_type the gift_type to set
	 */
	public void setGift_type(int gift_type) {
		this.gift_type = gift_type;
	}
	/**
	 * @return the gift_id
	 */
	public int getGift_id() {
		return gift_id;
	}
	/**
	 * @param gift_id the gift_id to set
	 */
	public void setGift_id(int gift_id) {
		this.gift_id = gift_id;
	}
	public RechargeAmountVo(){
		
	}
	
	public RechargeAmountVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.amount = rst.getBigDecimal("amount");
		this.gift = rst.getBigDecimal("gift");
		this.date = rst.getString("date");
		this.cityId = BaseDao.getInt(rst, "city_id");
		this.gift_type = BaseDao.getInt(rst, "gift_type");
		this.gift_id = BaseDao.getInt(rst, "gift_id");
	}
	
	public RechargeAmountVo(HttpServletRequest req){ 
		this.id = req.getParameter("id");
		this.amount = new BigDecimal(req.getParameter("amount"));
		this.gift = new BigDecimal(req.getParameter("gift"));
		this.cityId = ValueUtil.getInt(req.getAttribute("cityId")) ;
	}
}
