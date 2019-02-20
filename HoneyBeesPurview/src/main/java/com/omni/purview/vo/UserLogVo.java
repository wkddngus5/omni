/**
 * FileName:     UserLogVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月14日 上午10:30:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月14日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.vo;

 /**
 * @ClassName:     UserLogVo
 * @Description:用户日志实体
 * @author:    Albert
 * @date:        2017年6月14日 上午10:30:38
 *
 */
public class UserLogVo {
	String id;
	String admin_id;
	int func_id;
	String data_id;
	String date;
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
	 * @return the func_id
	 */
	public int getFunc_id() {
		return func_id;
	}
	/**
	 * @param func_id the func_id to set
	 */
	public void setFunc_id(int func_id) {
		this.func_id = func_id;
	}
	/**
	 * @return the data_id
	 */
	public String getData_id() {
		return data_id;
	}
	/**
	 * @param data_id the data_id to set
	 */
	public void setData_id(String data_id) {
		this.data_id = data_id;
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

	public UserLogVo(){
		
	}
	
	public UserLogVo(String adminId,int actionId,String dataId){
		this.admin_id = adminId;
		this.func_id = actionId;
		this.data_id = dataId;
	}
}
