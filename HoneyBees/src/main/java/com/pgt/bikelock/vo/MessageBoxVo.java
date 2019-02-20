/**
 * FileName:     MessageBoxVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月20日 下午4:48:18/4:48:18 pm, July 20, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月20日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     MessageBoxVo
 * @Description:信息消息实体/message entity
 * @author:    Albert
 * @date:        2017年7月20日 下午4:48:18/4:48:18 pm, July 20, 2017
 *
 */
public class MessageBoxVo {
	String id;
	String from_type;//消息来源: admin后台 user 用户/message from: admin user
	String admin_id;//管理员ID,0为系统消息/admin ID, system message 0
	String user_id;//用户ID/user ID
	String message_type;//消息类型/message type
	String content;//消息内容/message content
	String date;//消息生成时间/generated time of message
	String update_date;//消息更新时间/update time of message
	int isread;//是否已读/read or not
	String title;//标题/title
	int reply;//是否已回复/reply or not
	int status;//消息状态 -1：已删除 0：正常 1：收藏/ message status -1:deleted 0: normal 1:favorite
	//回复/reply
	String msgbox_id;//主题ID/topic ID
	
	//显示属性/display attribute
	String userPhone;//用户手机号/user phone number 
	String adminName;//管理员名称/admin name
	String replyContent;//回复内容/reply content
	Long dateStamp;
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
	 * @return the from_type
	 */
	public String getFrom_type() {
		return from_type;
	}
	/**
	 * @param from_type the from_type to set
	 */
	public void setFrom_type(String from_type) {
		this.from_type = from_type;
	}
	/**
	 * @return the admin_id
	 */
	public String getAdmin_id() {
		return admin_id;
	}
	/**
	 * @param admin_id the admin_id to set
	 */
	public void setAdmin_id(String admin_id) {
		this.admin_id = admin_id;
	}
	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	/**
	 * @return the message_type
	 */
	public String getMessage_type() {
		return message_type;
	}
	/**
	 * @param message_type the message_type to set
	 */
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
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
	 * @return the update_date
	 */
	public String getUpdate_date() {
		return update_date;
	}
	/**
	 * @param update_date the update_date to set
	 */
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	/**
	 * @return the isread
	 */
	public int getIsread() {
		return isread;
	}
	/**
	 * @param isread the isread to set
	 */
	public void setIsread(int isread) {
		this.isread = isread;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the msgbox_id
	 */
	public String getMsgbox_id() {
		return msgbox_id;
	}
	/**
	 * @param msgbox_id the msgbox_id to set
	 */
	public void setMsgbox_id(String msgbox_id) {
		this.msgbox_id = msgbox_id;
	}
	
	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}
	/**
	 * @param userPhone the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	/**
	 * @return the adminName
	 */
	public String getAdminName() {
		return adminName;
	}
	/**
	 * @param adminName the adminName to set
	 */
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	
	/**
	 * @return the reply
	 */
	public int getReply() {
		return reply;
	}
	/**
	 * @param reply the reply to set
	 */
	public void setReply(int reply) {
		this.reply = reply;
	}
	
	/**
	 * @return the replyContent
	 */
	public String getReplyContent() {
		return replyContent;
	}
	/**
	 * @param replyContent the replyContent to set
	 */
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
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
	 * @return the dateStamp
	 */
	public Long getDateStamp() {
		return dateStamp;
	}
	/**
	 * @param dateStamp the dateStamp to set
	 */
	public void setDateStamp(Long dateStamp) {
		this.dateStamp = dateStamp;
	}
	/**
	 * 用户提交消息构造/user submiting the message structure
	 * @param userId
	 * @param title
	 * @param content
	 */
	public MessageBoxVo(String userId,String title,String content){
		this.user_id = userId;
		this.title = title;
		this.content = content;
		this.message_type = "text";
		this.admin_id = "0";
		this.from_type = "user";
	}
	
	/**
	 * 后台提交消息构造/admin submiting message structure
	 * @param adminId
	 * @param userId
	 * @param title
	 * @param content
	 */
	public MessageBoxVo(String adminId,String userId,String title,String content){
		this.user_id = userId;
		this.admin_id = adminId;
		this.from_type = "admin";
		this.title = title;
		this.content = content;
		this.message_type = "text";
		this.reply = 1;//后台发出的消息，标记消息已回复，不显示在待回复列表/message admin sent, message marked replied, not display in the to-be-reply list
	}
	
	public MessageBoxVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.from_type = BaseDao.getString(rst, "from_type");
		this.admin_id = BaseDao.getString(rst, "admin_id");
		this.user_id = BaseDao.getString(rst, "user_id");
		this.message_type = BaseDao.getString(rst, "message_type");
		this.content = BaseDao.getString(rst, "content");
		this.date = BaseDao.getString(rst, "date");
		this.dateStamp =rst.getTimestamp("date").getTime()/1000;
		this.update_date = BaseDao.getString(rst, "update_date");
		this.isread = BaseDao.getInt(rst, "isread");
		this.title = BaseDao.getString(rst, "title");
		this.reply = BaseDao.getInt(rst, "reply");
		this.userPhone = BaseDao.getString(rst, "user_phone");
		this.adminName = BaseDao.getString(rst, "admin_name");
		this.replyContent = BaseDao.getString(rst, "reply_content");
		this.status = BaseDao.getInt(rst, "status");
	}
	

	public MessageBoxVo(HttpServletRequest req){
		this.title = req.getParameter("title");
		this.content = req.getParameter("content");
		this.message_type = "text";
		this.from_type = "admin";
	}
}
