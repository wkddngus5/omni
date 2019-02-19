/**
 * FileName:     CountryVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月14日 下午3:23:33/3:23:33 pm, April 14, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月14日       CQCN         1.0             1.0
 * Why & What is modified: <初始化><initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     CountryVo
 * @Description:国家实体/country entity
 * @author:    Albert
 * @date:        2017年4月14日 下午3:23:33/3:23:33 pm, April 14, 2017
 *
 */
public class CountryVo {
	int id;
	String country_code;
	String english_name;
	String chinese_name;
	String local_name;
	String location;
	String local_language;
	String local_dialect;
	String phone_code;//电话区号/phone area code
	
	
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
	 * @return the country_code
	 */
	public String getCountry_code() {
		return country_code;
	}
	/**
	 * @param country_code the country_code to set
	 */
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	/**
	 * @return the english_name
	 */
	public String getEnglish_name() {
		return english_name;
	}
	/**
	 * @param english_name the english_name to set
	 */
	public void setEnglish_name(String english_name) {
		this.english_name = english_name;
	}
	/**
	 * @return the chinese_name
	 */
	public String getChinese_name() {
		return chinese_name;
	}
	/**
	 * @param chinese_name the chinese_name to set
	 */
	public void setChinese_name(String chinese_name) {
		this.chinese_name = chinese_name;
	}
	/**
	 * @return the local_name
	 */
	public String getLocal_name() {
		return local_name;
	}
	/**
	 * @param local_name the local_name to set
	 */
	public void setLocal_name(String local_name) {
		this.local_name = local_name;
	}
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the local_language
	 */
	public String getLocal_language() {
		return local_language;
	}
	/**
	 * @param local_language the local_language to set
	 */
	public void setLocal_language(String local_language) {
		this.local_language = local_language;
	}
	/**
	 * @return the local_dialect
	 */
	public String getLocal_dialect() {
		return local_dialect;
	}
	/**
	 * @param local_dialect the local_dialect to set
	 */
	public void setLocal_dialect(String local_dialect) {
		this.local_dialect = local_dialect;
	}
	/**
	 * @return the phone_code
	 */
	public String getPhone_code() {
		return phone_code;
	}
	/**
	 * @param phone_code the phone_code to set
	 */
	public void setPhone_code(String phone_code) {
		this.phone_code = phone_code;
	}
	
	public CountryVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst, "id");
		this.country_code = BaseDao.getString(rst,"country_code");
		this.english_name = BaseDao.getString(rst,"english_name");
		this.chinese_name = BaseDao.getString(rst,"chinese_name");
		this.local_name = BaseDao.getString(rst,"local_name");
		this.location = BaseDao.getString(rst,"location");
		this.local_language = BaseDao.getString(rst,"local_language");
		this.local_dialect = BaseDao.getString(rst,"local_dialect");
		this.phone_code = BaseDao.getString(rst,"phone_code");

	}
}
