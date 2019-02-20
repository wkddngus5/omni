/**
 * FileName:     UserDeviceVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年3月30日 上午11:37:53/11:37:53 am, March 30, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年3月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

import javapns.devices.Device;

 /**
 * @ClassName:     UserDeviceVo
 * @Description:用户设备信息实体/user device info entity
 * @author:    Albert
 * @date:        2017年3月30日 上午11:37:53/11:37:53 am, March 30, 2017
 *
 */
public class UserDeviceVo {
	String uId;
	int type;//设备类型 1：Android 2：IOS/device type 1：Android 2：IOS
	String token;//设备推送证书/certificate device pushed
	String uuid;
	String requestToken;
	/**
	 * @return the uId
	 */
	public String getuId() {
		return uId;
	}
	/**
	 * @param uId the uId to set
	 */
	public void setuId(String uId) {
		this.uId = uId;
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
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	/**
	 * @return the requestToken
	 */
	public String getRequestToken() {
		return requestToken;
	}
	/**
	 * @param requestToken the requestToken to set
	 */
	public void setRequestToken(String requestToken) {
		this.requestToken = requestToken;
	}
	
	/**
	 * 
	 * @param rst
	 * @throws SQLException
	 */
	public UserDeviceVo(ResultSet rst) throws SQLException{
		this.uId = rst.getString("uid");
		this.type = rst.getInt("type");
		this.token = rst.getString("token");
		this.uuid = BaseDao.getString(rst, "uuid");
		this.requestToken = BaseDao.getString(rst, "request_token");
	}
	
	/**
	 * 
	 * @param userId
	 * @param type
	 * @param token
	 * @param uuid
	 * @param requestToken
	 */
	public UserDeviceVo(String userId,int type,String token,String uuid,String requestToken){
		this.uId = userId;
		this.type = type;
		this.token = token;
		this.uuid = uuid;
		this.requestToken = requestToken;
	}
}
