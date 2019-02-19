/**
 * FileName:     IndustryVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-28 上午10:23:03/10:23:03 am, March 28,2017
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

 /**
 * @ClassName:     产业信息实体/industry info entity
 * @Description:TODO
 * @author:    Albert
 * @date:        2017-3-28 上午10:23:03/10:23:03 am, March 28,2017
 *
 */
public class IndustryVo {
	String id;
	String name;//产业名称/industry name
	int consumeType;//消费类型 1：余额 2：第三方 3：余额加第三方/consume type 1:balance 2: third party 3:balance+third party
	String currency;//消费币种/ currency
	BigDecimal deposit;//押金金额/deposit amout
	String area_code;//国家区号/country code
	int register_auth_num;//注册认证步骤数/number of registeration authentication steps
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getConsumeType() {
		return consumeType;
	}
	public void setConsumeType(int consumeType) {
		this.consumeType = consumeType;
	}
	
	
	
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	
	
	/**
	 * @return the deposit
	 */
	public BigDecimal getDeposit() {
		return deposit;
	}
	/**
	 * @param deposit the deposit to set
	 */
	public void setDeposit(BigDecimal deposit) {
		this.deposit = deposit;
	}
	
	
	
	/**
	 * @return the area_code
	 */
	public String getArea_code() {
		return area_code;
	}
	
	public String getArea_code_number() {
		return area_code.replace("+", "00");
	}
	
	/**
	 * @param area_code the area_code to set
	 */
	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}
	
	

	/**
	 * @return the register_auth_num
	 */
	public int getRegister_auth_num() {
		return register_auth_num;
	}
	/**
	 * @param register_auth_num the register_auth_num to set
	 */
	public void setRegister_auth_num(int register_auth_num) {
		this.register_auth_num = register_auth_num;
	}
	public IndustryVo(String[] parms,HttpServletRequest req){
		this.id = req.getParameter(parms[0]);
		this.deposit= new BigDecimal(req.getParameter(parms[1]));
	}
	
	
	
	/**
	 * 
	 * @param rst
	 * @param type 1:简单信息 2：详细信息/1: brief information 2: detail information
	 * @throws SQLException
	 */
	public IndustryVo(ResultSet rst,int type) throws SQLException{
		
		this.name = rst.getString("industry_name");
		this.consumeType =  rst.getInt("consume_type");
		if(type == 2){
			this.id = rst.getString("id");
			this.currency = rst.getString("currency");
			this.deposit = rst.getBigDecimal("deposit");
			this.area_code = rst.getString("area_code");
			this.register_auth_num = rst.getInt("register_auth_num");
		}
		
	}
	
}
