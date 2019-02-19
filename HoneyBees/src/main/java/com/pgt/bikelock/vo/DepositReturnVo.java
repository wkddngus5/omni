/**
 * FileName:     DepositReturnVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月15日 下午3:05:47/3:05:47 pm, April 15, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月15日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;

 /**
 * @ClassName:     DepositReturnVo
 * @Description:押金退还实体/deposit refund entity
 * @author:    Albert
 * @date:        2017年4月15日 下午3:05:47/3:05:47 pm, April 15, 2017
 *
 */
public class DepositReturnVo {
	String id;
	String uid;
	UserVo userVo;
	int status;
	String date;
	String out_refund_no;//外部退款号/external refund No.
	TradeVo tradeVo;
	String trade_id;

	/**显示属性 / display attribute */
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
	 * @return the userVo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	
	
	
	/**
	 * @return the out_refund_no
	 */
	public String getOut_refund_no() {
		return out_refund_no;
	}
	/**
	 * @param out_refund_no the out_refund_no to set
	 */
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	/**
	 * @param userVo the userVo to set
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	
	/**
	 * @return the tradeVo
	 */
	public TradeVo getTradeVo() {
		return tradeVo;
	}
	/**
	 * @param tradeVo the tradeVo to set
	 */
	public void setTradeVo(TradeVo tradeVo) {
		this.tradeVo = tradeVo;
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
	 * @return the trade_id
	 */
	public String getTrade_id() {
		return trade_id;
	}
	/**
	 * @param trade_id the trade_id to set
	 */
	public void setTrade_id(String trade_id) {
		this.trade_id = trade_id;
	}
	
	
	

	public DepositReturnVo(){
		
	}
	
	public DepositReturnVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst,"id");
		this.uid = BaseDao.getString(rst,"uid");
		this.status = BaseDao.getInt(rst,"status");
		setStatusStr(LanguageUtil.getTypeStr("consume_deposit_return_status_value", status+1));
		this.date = rst.getString("date");
		this.out_refund_no = BaseDao.getString(rst, "out_refund_no");

		this.userVo = new UserVo(rst);
		TradeVo tradeVo = new TradeVo();
		tradeVo.setId(rst.getString("trade_id"));
		tradeVo.setWay(rst.getInt("way"));
		tradeVo.setDate(rst.getString("trade_date"));
		tradeVo.setOut_trade_no(rst.getString("out_trade_no"));
		this.tradeVo = tradeVo;
	}
	
}
