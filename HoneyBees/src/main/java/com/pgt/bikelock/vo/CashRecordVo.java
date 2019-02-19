/**
 * FileName:     CashVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月10日 下午8:58:08/8:58:08 pm, May 10, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     CashVo
 * @Description:提现记录实体/withdraw deposit log entity
 * @author:    Albert
 * @date:        2017年5月10日 下午8:58:08/8:58:08 pm, May 10, 2017
 *
 */
public class CashRecordVo {
	String id;
	String uid;
	BigDecimal amount;//金额/amount
	int amount_type;//1:红包 2：余额/1. red packet 2: balance
	int type;//1:微信 2:支付宝/1.wechat 2: alipay
	String order_id;//第三方提现订单ID/ withdraw deposit order No. of the third party
	int status;//状态 0：申请中 1:成功 2:失败/status 0: applying 1.verified
	String date;
	UserVo userVo;
	BigDecimal refund_amount;//实际退款金额/Refund amount
	/**显示属性**/
	String statusStr;
	String refund_date;
	
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
	 * @return the amount_type
	 */
	public int getAmount_type() {
		return amount_type;
	}
	/**
	 * @param amount_type the amount_type to set
	 */
	public void setAmount_type(int amount_type) {
		this.amount_type = amount_type;
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
	 * @return the order_id
	 */
	public String getOrder_id() {
		return order_id;
	}
	/**
	 * @param order_id the order_id to set
	 */
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
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
	 * @param userVo the userVo to set
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	
	
	/**
	 * @return the refund_amount
	 */
	public BigDecimal getRefund_amount() {
		return refund_amount;
	}
	/**
	 * @param refund_amount the refund_amount to set
	 */
	public void setRefund_amount(BigDecimal refund_amount) {
		this.refund_amount = refund_amount;
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
	 * @return the refund_date
	 */
	public String getRefund_date() {
		return refund_date;
	}
	/**
	 * @param refund_date the refund_date to set
	 */
	public void setRefund_date(String refund_date) {
		this.refund_date = refund_date;
	}
	public CashRecordVo(){
		
	}
	

	/**
	 * 新增提现实体构造/new added physical structure of deposit withdraw 
	 * @param userId
	 * @param amount
	 * @param type 1:微信 2:支付宝/1: wechat 2: alipay
	 * @param amountType 1:红包 2：余额/ 1: red packet 2:balance
	 */
	public CashRecordVo(String userId,BigDecimal amount,int type,int amountType){
		this.uid = userId;
		this.amount = amount;
		this.type = type;
		this.amount_type = amountType;
	}
	
	/**
	 * 新增余额退款实体构造/new added physical structure of deposit withdraw 
	 * @param amount
	 * @param type 1:微信 2:支付宝//1: wechat 2: alipay
	 * @param orderId 退款订单号/ refund order no.
	 */
	public CashRecordVo(BigDecimal amount,int type,String orderId){
		this.amount = amount;
		this.type = type;
		this.amount_type = 2;
		this.order_id = orderId;
	}
	
	public CashRecordVo(String[] parms,HttpServletRequest req){
		this.uid = req.getParameter(parms[0]);
		this.amount= new BigDecimal(req.getParameter(parms[1]));
		this.refund_amount = this.amount;
		this.refund_date = TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm_ss);
//		this.type = ValueUtil.getInt(req.getParameter(parms[2]));
//		this.order_id = req.getParameter(parms[3]);
	}
	
	public CashRecordVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.uid = BaseDao.getString(rst, "uid");
		this.amount = BaseDao.getBigDecimal(rst, "amount");
		this.amount_type = BaseDao.getInt(rst, "amount_type");
		this.type = BaseDao.getInt(rst, "type");
		this.order_id = BaseDao.getString(rst, "order_id");
		this.status = BaseDao.getInt(rst, "status");
		this.date = BaseDao.getString(rst, "date");
		this.userVo = new UserVo(rst);
		this.statusStr = LanguageUtil.getStatusStr(status, "consume_deposit_return_status_value");
		this.refund_amount = BaseDao.getBigDecimal(rst, "refund_amount");
		this.refund_date = BaseDao.getString(rst, "refund_date");
	}

}
