/**
 * FileName:     DepositConfVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月3日 下午2:08:15/2:08:15 pm, July 3, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月3日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initialzing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     DepositConfVo
 * @Description:押金设置实体/deposit set entity
 * @author:    Albert
 * @date:        2017年7月3日 下午2:08:15/2:08:15 pm, July 3, 2017
 *
 */
public class DepositConfVo {
	int id;
	BigDecimal amount;//押金金额/deposit amount
	int return_min_day;//押金退款最短时间/minimum time of the deposit refund
	int return_max_day;//押金退款最长时间/maximum time of the deposit refund
	int city_id;//城市ID/city ID 
	String date;//编辑时间/ edit tiem
	int automatic_refund;//是否自动退款/refund automatically or not
	String automaticRefundStr;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
	 * @return the return_min_day
	 */
	public int getReturn_min_day() {
		return return_min_day;
	}
	/**
	 * @param return_min_day the return_min_day to set
	 */
	public void setReturn_min_day(int return_min_day) {
		this.return_min_day = return_min_day;
	}
	/**
	 * @return the return_max_day
	 */
	public int getReturn_max_day() {
		return return_max_day;
	}
	/**
	 * @param return_max_day the return_max_day to set
	 */
	public void setReturn_max_day(int return_max_day) {
		this.return_max_day = return_max_day;
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
	 * @return the automatic_refund
	 */
	public int getAutomatic_refund() {
		return automatic_refund;
	}
	/**
	 * @param automatic_refund the automatic_refund to set
	 */
	public void setAutomatic_refund(int automatic_refund) {
		this.automatic_refund = automatic_refund;
	}
	
	
	
	
	/**
	 * @return the automaticRefundStr
	 */
	public String getAutomaticRefundStr() {
		return automaticRefundStr;
	}
	/**
	 * @param automaticRefundStr the automaticRefundStr to set
	 */
	public void setAutomaticRefundStr(String automaticRefundStr) {
		this.automaticRefundStr = automaticRefundStr;
	}

	public DepositConfVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst, "id");
		this.amount = BaseDao.getBigDecimal(rst, "amount");
		this.return_min_day = BaseDao.getInt(rst, "return_min_day");
		this.return_max_day = BaseDao.getInt(rst, "return_max_day");
		this.city_id = BaseDao.getInt(rst, "city_id");
		this.date = BaseDao.getString(rst, "date");
		this.automatic_refund = BaseDao.getInt(rst, "automatic_refund");
		setAutomaticRefundStr(LanguageUtil.getTypeStr("common_yes_no_value", automatic_refund+1));
	}
	
	public DepositConfVo(HttpServletRequest req){
		this.id = ValueUtil.getInt(req.getParameter("id"));
		this.amount = ValueUtil.getBigDecimal(req.getParameter("amount"));
		this.return_min_day = ValueUtil.getInt(req.getParameter("return_min_day"));
		this.return_max_day = ValueUtil.getInt(req.getParameter("return_max_day"));
		this.city_id = ValueUtil.getInt(req.getParameter("cityId"));
		this.automatic_refund = ValueUtil.getInt(req.getParameter("automatic_refund"));
	}
}
