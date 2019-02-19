/**
 * FileName:     UserCardVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月1日 下午7:39:30/7:39:30 pm, April 1, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

 /**
 * @ClassName:     UserCardVo
 * @Description:用户银行卡信息/User bank card information
 * @author:    Albert
 * @date:        2017年4月1日 下午7:39:30/7:39:30 pm, April 1, 2017
 *
 */
public class BankCardVo {
	String id;
	String uid;
	String card_number;//银行卡号//bank card No.
	String exp_date;//过期日期// expiration date
	String cvv;//信用卡校验码//check code of credit card
	String name_on_card;//卡主姓名//name of card holder
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
	 * @return the card_number
	 */
	public String getCard_number() {
		return card_number;
	}
	/**
	 * @param card_number the card_number to set
	 */
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	/**
	 * @return the exp_date
	 */
	public String getExp_date() {
		return exp_date;
	}
	/**
	 * @param exp_date the exp_date to set
	 */
	public void setExp_date(String exp_date) {
		this.exp_date = exp_date;
	}
	/**
	 * @return the cvv
	 */
	public String getCvv() {
		return cvv;
	}
	/**
	 * @param cvv the cvv to set
	 */
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	/**
	 * @return the name_on_card
	 */
	public String getName_on_card() {
		return name_on_card;
	}
	/**
	 * @param name_on_card the name_on_card to set
	 */
	public void setName_on_card(String name_on_card) {
		this.name_on_card = name_on_card;
	}

	public BankCardVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.uid = rst.getString("uid");
		this.card_number = rst.getString("card_number");
		this.exp_date = rst.getString("exp_date");
		this.cvv = rst.getString("cvv");
		this.name_on_card = rst.getString("name_on_card");
	}
	
	public BankCardVo(HttpServletRequest req,String[] parms,String uid){
		this.uid = uid;
		this.card_number = req.getParameter((parms[0]));;
		this.exp_date = req.getParameter((parms[1]));;
		this.cvv = req.getParameter((parms[2]));;
		this.name_on_card = req.getParameter((parms[3]));;
	}
}
