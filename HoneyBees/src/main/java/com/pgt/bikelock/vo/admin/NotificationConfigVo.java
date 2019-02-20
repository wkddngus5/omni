/**
 * FileName:     NotificationConfigVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月25日 下午4:35:03
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月25日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

/**
 * @ClassName:     NotificationConfigVo
 * @Description:Notification configuration empty
 * @author:    Albert
 * @date:        2017年11月25日 下午4:35:03
 *
 */
public class NotificationConfigVo {

	/***default value**/
	public static int HEART_STATISTICS_INTERVAL = 4;//心跳监测间隔（分钟）/STATISTICS INTERVAL minutes
	public static  int HEART_MONITORING_FREQUENCY = 1;//心跳监测频率（小时）/MONITORING FREQUENCY hours

	public static  int LOW_POWER_VALUE = 30;//低电量值/low power %
	public static  int LOW_POWER_FREQUENCY = 1;//低电量监测频率（小时）/LOW POWER hours

	public static  int LOCATION_STATISTICS_INTERVAL = 1;//定位监测间隔（小时）/LOCATION INTERVAL hours
	public static  int LOCATION_MONITORING_FREQUENCY = 4;//定位监测频率（小时）/LOCATION FREQUENCY hours

	int id;
	int type;//通知类型 1：故障反馈 2：报警  3：心跳异常 4：定位异常 5：移动速度异常 6:低电量 7：退押金 8:退余额
	//1:report 2:warn  3:heart 4:location 5：move fast warn 6:low power 7:refund deposit 8:refund balance
	String template;//通知模板内容/template content
	int sms;//短信通知/if sms notification
	int email;//邮件通知/if email notification
	String date;
	String other_config;//Others Configuration（JSON）
	/****/
	String typeStr;
	AdminVo adminVo;
	int jsonValue;
	int jsonFrequency;


	public enum NotificationType{
		Bike_Error_Report,
		Bike_Warn,
		Bike_Heart,
		Bike_Location,
		Bike_Move_Fast,
		Bike_Low_Power,
		User_Refund_Deposit,
		User_Refund_Balance,
		Bike_Riding,
	}

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
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	public static int getType(NotificationType nType) {
		int value = 0;
		switch (nType) {
		case Bike_Error_Report:
			value = 1;
			break;
		case Bike_Warn:
			value = 2;
			break;
		case Bike_Heart:
			value = 3;
			break;
		case Bike_Location:
			value = 4;
			break;
		case Bike_Move_Fast:
			value = 5;
			break;
		case Bike_Low_Power:
			value = 6;
			break;
		case User_Refund_Deposit:
			value = 7;
			break;
		case User_Refund_Balance:
			value = 8;
			break;
		case Bike_Riding:
			value = 9;
			break;
		default:
			break;
		}
		return value;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(String template) {
		this.template = template;
	}
	/**
	 * @return the sms
	 */
	public int getSms() {
		return sms;
	}
	/**
	 * @param sms the sms to set
	 */
	public void setSms(int sms) {
		this.sms = sms;
	}
	/**
	 * @return the email
	 */
	public int getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(int email) {
		this.email = email;
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
	 * @return the other_config
	 */
	public String getOther_config() {
		return other_config;
	}
	/**
	 * @param other_config the other_config to set
	 */
	public void setOther_config(String other_config) {
		this.other_config = other_config;
	}

	/**
	 * @return the typeStr
	 */
	public String getTypeStr() {
		return typeStr;
	}
	/**
	 * @param typeStr the typeStr to set
	 */
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

	/**
	 * @return the adminVo
	 */
	public AdminVo getAdminVo() {
		return adminVo;
	}
	/**
	 * @param adminVo the adminVo to set
	 */
	public void setAdminVo(AdminVo adminVo) {
		this.adminVo = adminVo;
	}



	/**
	 * @return the jsonValue
	 */
	public int getJsonValue() {
		return jsonValue;
	}
	/**
	 * @param jsonValue the jsonValue to set
	 */
	public void setJsonValue(int jsonValue) {
		this.jsonValue = jsonValue;
	}
	/**
	 * @return the jsonFrequency
	 */
	public int getJsonFrequency() {
		return jsonFrequency;
	}
	/**
	 * @param jsonFrequency the jsonFrequency to set
	 */
	public void setJsonFrequency(int jsonFrequency) {
		this.jsonFrequency = jsonFrequency;
	}
	public NotificationConfigVo(){

	}

	public NotificationConfigVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst, "id");
		this.type =BaseDao.getInt(rst, "type");
		this.template = BaseDao.getString(rst, "template");
		this.sms = BaseDao.getInt(rst, "nf_sms");
		this.email = BaseDao.getInt(rst, "nf_email");
		this.date = BaseDao.getString(rst, "date");
		this.other_config = BaseDao.getString(rst, "other_config");
		setTypeStr(LanguageUtil.getTypeStr("notification_management_config_type", type));
		this.adminVo = new AdminVo(rst);
		if(!StringUtils.isEmpty(this.other_config)){
			JSONObject object = JSONObject.parseObject(this.other_config);
			this.jsonValue = object.getIntValue("value");
			this.jsonFrequency = object.getIntValue("frequency");
		}
	}

	public NotificationConfigVo (HttpServletRequest request){
		this.id = ValueUtil.getInt(request.getParameter("id"));
		this.template = request.getParameter("content");
		this.type = ValueUtil.getInt(request.getParameter("type"));
		this.sms = ValueUtil.getInt(request.getParameter("sms"));
		this.email = ValueUtil.getInt(request.getParameter("email"));

		if(this.type == 3 || this.type == 4 || this.type == 6){
			JSONObject object = new JSONObject();
			this.jsonValue = ValueUtil.getInt(request.getParameter("defaultValue"));
			this.jsonFrequency =  ValueUtil.getInt(request.getParameter("frequency"));
			object.put("value", this.jsonValue);
			object.put("frequency",this.jsonFrequency);
			this.other_config = object.toJSONString();

		}
	}
}
