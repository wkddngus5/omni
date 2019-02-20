/**
 * FileName:     RequestClientVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月7日 下午2:17:06
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月7日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo;

import com.pgt.bikelock.utils.LanguageUtil;

 /**
 * @ClassName:     RequestClientVo
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月7日 下午2:17:06
 *
 */
public class RequestClientVo {
	/**安全拦截/Firewall**/
	//接口请求间隔
	public static double REQUEST_INTERVAL = 1;
	//单日接口请求总数
	public static int REQUEST_MAX_REQUEST_COUNT = 3000;
	//单日网页协议内容请求总数
	public static int OTHERS_REQUEST_MAX_REQUEST_COUNT = 300;
	
	//短信接口请求间隔，单日请求限制
	public static int SMS_REQUEST_INTERVAL = 60;
	public static int SMS_COUNT_MAX_IN_DAY = 5;
	
	String ip;
	long firstTime;
	long lastTime;
	String phone;
	int count;//one day success count
	int totalCount;//one day total request count
	boolean stopClient;
	int requestType;
	
	String firstDate;
	String lastDate;
	String statusStr;
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	/**
	 * @return the firstTime
	 */
	public long getFirstTime() {
		return firstTime;
	}
	/**
	 * @param firstTime the firstTime to set
	 */
	public void setFirstTime(long firstTime) {
		this.firstTime = firstTime;
	}
	/**
	 * @return the lastTime
	 */
	public long getLastTime() {
		return lastTime;
	}
	/**
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
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
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
	
	/**
	 * @return the stopClient
	 */
	public boolean isStopClient() {
		return stopClient;
	}
	/**
	 * @param stopClient the stopClient to set
	 */
	public void setStopClient(boolean stopClient) {
		this.stopClient = stopClient;
	}
	
	
	/**
	 * @return the requestType
	 */
	public int getRequestType() {
		return requestType;
	}
	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}
	
	
	
	/**
	 * @return the firstDate
	 */
	public String getFirstDate() {
		return firstDate;
	}
	/**
	 * @param firstDate the firstDate to set
	 */
	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}
	/**
	 * @return the lastDate
	 */
	public String getLastDate() {
		return lastDate;
	}
	/**
	 * @param lastDate the lastDate to set
	 */
	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}
	
	/**
	 * @return the statusStr
	 */
	public String getStatusStr() {
		if(statusStr != null){
			return statusStr;
		}
		return LanguageUtil.getStatusStr(stopClient?1:0, "system_config_request_status");
	}
	/**
	 * @param statusStr the statusStr to set
	 */
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public RequestClientVo(){
		
	}
	
	public RequestClientVo(String ip,long firstTime,int requestType){
		this.ip = ip;
		this.firstTime = firstTime;
		this.lastTime = firstTime;
		this.count = 1;
		this.totalCount = 1;
		this.requestType = requestType;
	}
}
