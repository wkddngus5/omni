/**
 * FileName:     RfidCardVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月16日 上午10:57:04
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     RfidCardVo
 * @Description:TODO
 * @author:    Albert
 * @date:        2018年8月16日 上午10:57:04
 *
 */
public class RfidCardVo {
	String id;
	String cardId;
	String cardNo;
	int uid;
	int status;//0:未激活，1:激活，2:冻结
	Date date;
	
	/**show**/
	UserVo userVo;
	
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
	 * @return the cardId
	 */
	public String getCardId() {
		return cardId;
	}
	/**
	 * @param cardId the cardId to set
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	/**
	 * @return the cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}
	/**
	 * @param cardNo the cardNo to set
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	/**
	 * @return the uId
	 */
	public int getuid() {
		return uid;
	}
	/**
	 * @param uId the uId to set
	 */
	public void setuid(int uid) {
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
	public Date getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
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
	public RfidCardVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.cardId = rst.getString("card_id");
		this.cardNo = rst.getString("card_no");
		this.uid = rst.getInt("uid");
		this.status = rst.getInt("status");
		this.date = new Date(rst.getTimestamp("date").getTime());
		this.userVo = new UserVo(rst);
	}
	
	public RfidCardVo(HttpServletRequest request){
		this.id = request.getParameter("id");
		this.cardId = request.getParameter("cardId");
		this.cardNo = request.getParameter("cardNo");
		this.uid = ValueUtil.getInt(request.getParameter("userVo.id"));
		this.status = ValueUtil.getInt(request.getParameter("status"));
	}
}
