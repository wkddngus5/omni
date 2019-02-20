/**
 * FileName:     RequestTokenVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年12月1日 下午5:31:11
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年12月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.vo;

 /**
 * @ClassName:     RequestTokenVo
 * @Description:请求令牌实体/request token entity
 * @author:    Albert
 * @date:        2017年12月1日 下午5:31:11
 *
 */
public class RequestTokenVo {
	String userId;//登录用户ID/register user id
	int tokenStatus;//token状态 -1：已过期 0：无效 1:可使用 2:异地登陆 3:冻结 4:待更新/token status 1:expired 0:invalid 1:available 2:others login 3：stop
	String industryId;//产业类型/industry type
	String currency;//消费币种/consumption currency
	int cityId;//所在城市/city located
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the tokenStatus
	 */
	public int getTokenStatus() {
		return tokenStatus;
	}
	/**
	 * @param tokenStatus the tokenStatus to set
	 */
	public void setTokenStatus(int tokenStatus) {
		this.tokenStatus = tokenStatus;
	}
	/**
	 * @return the industryId
	 */
	public String getIndustryId() {
		return industryId;
	}
	/**
	 * @param industryId the industryId to set
	 */
	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
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

}
