/**
 * FileName:     ViolationVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月16日 下午6:37:58/6:37:58 pm, May 16, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     ViolationVo
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月16日 下午6:37:58/6:37:58 pm, May 16, 2017
 *
 */
public class ViolationVo {
	String id;
	String uid;
	String useid;//骑行记录ID/cycling record ID
	int type;//违规类型 1:违停一次 2;忘记关锁 3:加装私锁 4:车辆丢失/violation type 1: illegal parking 1 time 2: unlocked 3: add private lock 4: bicycle lost
	String note;
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
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}
	/**
	 * @return the useid
	 */
	public String getUseid() {
		return useid;
	}
	/**
	 * @param useid the useid to set
	 */
	public void setUseid(String useid) {
		this.useid = useid;
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

	public ViolationVo(String[] parms,HttpServletRequest req){
		this.type = ValueUtil.getInt(req.getParameter(parms[0]));
		this.useid = req.getParameter(parms[1]);
		this.note= req.getParameter(parms[2]);
		this.uid = req.getParameter(parms[3]);
		this.date = req.getParameter("date");
	}
}
