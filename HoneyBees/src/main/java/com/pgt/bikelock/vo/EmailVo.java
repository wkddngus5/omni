/**
 * FileName:     EmailVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月29日 下午3:25:12
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月29日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;

 /**
 * @ClassName:     EmailVo
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月29日 下午3:25:12
 *
 */
public class EmailVo {
	String id;
	String sender;
	String receiver;
	String subject;
	String content;
	int status;
	String date;
	String code;
	/***/
	String receiverName;
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
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	/**
	 * @return the receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	/**
	 * @param receiver the receiver to set
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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
	 * @return the receiverName
	 */
	public String getReceiverName() {
		return receiverName;
	}
	/**
	 * @param receiverName the receiverName to set
	 */
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	public EmailVo(){
		
	}
	
	public EmailVo(String receiver,String receiverName,String subject,String content){
		this.receiver = receiver;
		this.receiverName = receiverName;
		this.subject = subject;
		this.content = content;
	}
	
	public EmailVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.sender = BaseDao.getString(rst, "sender");
		this.receiver = BaseDao.getString(rst, "receiver");
		this.content = BaseDao.getString(rst, "content");
		this.subject = BaseDao.getString(rst, "subject");
		this.date = TimeUtil.formaStrDate(BaseDao.getString(rst, "date"));
		this.status = BaseDao.getInt(rst, "status");
		if(this.status == 2){
			this.statusStr = "Used";
		}else{
			setStatusStr(LanguageUtil.getStatusStr(this.status, "common_yes_no_value"));			
		}
		
	}
}
