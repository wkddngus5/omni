/**
 * FileName:     SmsVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月12日 上午11:33:32/11:33:32 am, April 12, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initialzing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;

 /**
 * @ClassName:     SmsVo
 * @Description:短信实体/sms entity
 * @author:    Albert
 * @date:        2017年4月12日 上午11:33:32/11:33:32 am, April 12, 2017
 *
 */
public class SmsVo {
	String id;
	String phone;
	String areaCode;//发送手机号区号/sent phone area code
	String code;
	String content;
	int type;//短信类型 1:验证类 2：服务通知类/SMS type 1:verification 2:service notification
	String date;
	int used;
	/**显示属性**/
	String typeStr;
	String usedStr;
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
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}
	/**
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
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
	 * @return the used
	 */
	public int getUsed() {
		return used;
	}
	/**
	 * @param used the used to set
	 */
	public void setUsed(int used) {
		this.used = used;
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
	 * @return the usedStr
	 */
	public String getUsedStr() {
		return usedStr;
	}
	/**
	 * @param usedStr the usedStr to set
	 */
	public void setUsedStr(String usedStr) {
		this.usedStr = usedStr;
	}
	public SmsVo(){
		
	}
	
	public SmsVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.phone = BaseDao.getString(rst, "phone");
		this.code = BaseDao.getString(rst, "code");
		this.content = BaseDao.getString(rst, "content");
		this.used = BaseDao.getInt(rst, "used");
		this.date = TimeUtil.formaStrDate(BaseDao.getString(rst, "date"));
		this.areaCode = BaseDao.getString(rst, "area_code");
		this.type =BaseDao.getInt(rst, "type");
		setUsedStr(LanguageUtil.getStatusStr(this.used, "common_yes_no_value"));
	}
	
	public SmsVo(String phone,String code,String content,int type,String areaCode){
		this.phone = phone;
		this.code = code;
		this.content = content;
		this.type = type;
		this.areaCode = areaCode;
	}
}
