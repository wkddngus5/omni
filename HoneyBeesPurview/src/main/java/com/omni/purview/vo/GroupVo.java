/**
 * FileName:     GroupView.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月1日 下午8:03:00
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 * @ClassName:     GroupView
 * @Description:权限组实体
 * @author:    Albert
 * @date:        2017年6月1日 下午8:03:00
 *
 */
public class GroupVo {

	int id;
	String name;
	String note;
	String date;
	int cityId;

	/**显示属性**/
	boolean checked;//是否选中


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
	 * @return the name
	 */
	public String getName() {
		return name;
	}




	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}




	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}




	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
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
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}




	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}




	/**
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}




	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}




	/**
	 * 
	 */
	public GroupVo() {
		// TODO Auto-generated constructor stub
	}
	

	public GroupVo(ResultSet rst,List<String> existFunc) throws SQLException{
		this.id = rst.getInt("id");
		this.name = rst.getString("name");
		this.note = rst.getString("note");
		this.date = rst.getString("date");
		this.cityId = rst.getInt("city_id");
		if(existFunc != null && existFunc.contains(this.id+"")){
			this.checked = true;
		}
	}
}
