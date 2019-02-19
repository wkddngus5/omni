/**
 * FileName:     AppVersionVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月27日 下午3:34:14/3:34:14 pm, May 27, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月27日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     AppVersionVo
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月27日 下午3:34:14/3:34:14 pm, May 27, 2017
 *
 */
public class AppVersionVo {
	String id;
	String version_name;
	int version_code;
	int type;
	String url;
	String content;
	String date;
	
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
	 * @return the version_name
	 */
	public String getVersion_name() {
		return version_name;
	}
	/**
	 * @param version_name the version_name to set
	 */
	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	/**
	 * @return the version_code
	 */
	public int getVersion_code() {
		return version_code;
	}
	/**
	 * @param version_code the version_code to set
	 */
	public void setVersion_code(int version_code) {
		this.version_code = version_code;
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	public AppVersionVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.version_name = rst.getString("version_name");
		this.version_code = rst.getInt("version_code");
		this.type = rst.getInt("type");
		this.setTypeStr(LanguageUtil.getTypeStr("setting_version_type", type));
		this.url = rst.getString("url");
		this.content = rst.getString("content");
		this.date = rst.getString("date");
	}

	public AppVersionVo(HttpServletRequest req){
		this.version_name = (String) req.getParameter("name");
		this.version_code = ValueUtil.getInt(req.getParameter("code"));
		this.type = ValueUtil.getInt(req.getParameter("type"));
		this.url = (String) req.getParameter("url");
		this.content = (String) req.getParameter("content");
	}
}
