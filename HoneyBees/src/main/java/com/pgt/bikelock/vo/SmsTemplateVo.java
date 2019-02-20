/**
 * FileName:     SmsTemplateVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月12日 下午12:14:38/12:14:38 pm, April 12,2017 
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

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     SmsTemplateVo
 * @Description:短信模板实体/SMS template entity
 * @author:    Albert
 * @date:        2017年4月12日 下午12:14:38/12:14:38 pm, April 12,2017
 *
 */
public class SmsTemplateVo {
	String id;
	String industry_id;
	int type;//短信类型 1:验证类 2：服务通知类/SMS type 1:verification 2:service notification 
	String template;
	String date;
	String title;
	/****/
	String typeStr;
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
	 * @return the industry_id
	 */
	public String getIndustry_id() {
		return industry_id;
	}
	/**
	 * @param industry_id the industry_id to set
	 */
	public void setIndustry_id(String industry_id) {
		this.industry_id = industry_id;
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
	public SmsTemplateVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.industry_id = rst.getString("industry_id");
		this.type = rst.getInt("type");
		this.template = rst.getString("template");
		this.title = BaseDao.getString(rst, "title");
		this.date = rst.getString("date");
		setTypeStr(LanguageUtil.getTypeStr("notification_management_template_type_values", type));
	}
	
	public SmsTemplateVo (HttpServletRequest request){
		if(request.getParameter("id") != null){
			this.id = request.getParameter("id");
		}
		this.template = request.getParameter("content");
		this.title = request.getParameter("title");
		this.type = ValueUtil.getInt(request.getParameter("type")) ;
	}
}
