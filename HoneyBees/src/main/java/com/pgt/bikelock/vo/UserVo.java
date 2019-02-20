/**
 * FileName:     UserVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:20:47/10:20:47 am. March 24, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

/**
 * @ClassName:     UserVo
 * @Description:用户实体/user entity
 * @author:    Albert
 * @date:        2017-3-24 上午10:20:47/10:20:47 am. March 24, 2017
 *
 */
public class UserVo {
	String uId;//用户ID/user ID
	String phone;//用户手机号（登录账号）/User phone Number (resgistration account)
	String nickName;//昵称/nickname
	String industryId;//关联产业ID/industry ID 
	IndustryVo industryVo;//关联产业实体/industry entity
	String headUrl;//头像地址/head address
	BigDecimal money;//用户余额(单位：元)/user balance (unit: yuan)
	BigDecimal giftMoney;//赠送余额(单位：元)/gift amount (unit: yuan)
	int authStatus;//认证进度 0：未认证 1：手机 2：实名 3：押金 4：银行卡  5:申请押金退还中（不能使用单车）6:已退押金（不能使用单车) 7:关闭用户使用单车功能  8：停用账户/authentication staus 0: unverified 1: phone 2:authenticated 3: deposit 4:bank 5:applying return deposit (bicycle unavailable) 6: deposit refunded (bicycle unavailable) 7: stop user using the bicycle 8:stop the account 
	UserDeviceVo deviceVo;//用户设备信息/user device info
	String invite_code;//邀请码/invitation code
	String register_date;//注册时间/registration time
	String login_date;//最后登录时间/last login time
	int credit_score;//信用积分/credit score
	int cityId;//所在城市ID/city ID
	int country_id;
	String password;
	/**显示属性**//**display attribute**/
	String token;//登录验证token/verification for login
	String cityName;//所在城市名称/city name 
	String authStatusStr;//认证进度/authentication status
	UserDetailVo detailVo;//用户详情/user detail
	String phoneCode;

	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public void setDefaultNickName(String firstName,String lastName){
		if("zh_cn".equals(OthersSource.DEFAULT_LANGUAGE)){
			this.nickName = lastName +" "+firstName;
		}else {
			this.nickName = firstName +" "+lastName;
		}
	}

	public String getIndustryId() {
		return industryId;
	}
	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}



	public IndustryVo getIndustryVo() {
		return industryVo;
	}
	public void setIndustryVo(IndustryVo industryVo) {
		this.industryVo = industryVo;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}




	/**
	 * @return the giftMoney
	 */
	public BigDecimal getGiftMoney() {
		return giftMoney;
	}
	/**
	 * @param giftMoney the giftMoney to set
	 */
	public void setGiftMoney(BigDecimal giftMoney) {
		this.giftMoney = giftMoney;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}


	/**
	 * @return the authStatus
	 */
	public int getAuthStatus() {
		return authStatus;
	}
	/**
	 * @param authStatus the authStatus to set
	 */
	public void setAuthStatus(int authStatus) {
		this.authStatus = authStatus;
	}
	/**
	 * @return the deviceVo
	 */
	public UserDeviceVo getDeviceVo() {
		return deviceVo;
	}
	/**
	 * @param deviceVo the deviceVo to set
	 */
	public void setDeviceVo(UserDeviceVo deviceVo) {
		this.deviceVo = deviceVo;
	}
	/**
	 * @return the invite_code
	 */
	public String getInvite_code() {
		return invite_code;
	}
	/**
	 * @param invite_code the invite_code to set
	 */
	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}


	/**
	 * @return the register_date
	 */
	public String getRegister_date() {
		return register_date;
	}
	/**
	 * @param register_date the register_date to set
	 */
	public void setRegister_date(String register_date) {
		this.register_date = register_date;
	}
	/**
	 * @return the login_date
	 */
	public String getLogin_date() {
		return login_date;
	}
	/**
	 * @param login_date the login_date to set
	 */
	public void setLogin_date(String login_date) {
		this.login_date = login_date;
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
	 * @return the credit_score
	 */
	public int getCredit_score() {
		return credit_score;
	}
	/**
	 * @param credit_score the credit_score to set
	 */
	public void setCredit_score(int credit_score) {
		this.credit_score = credit_score;
	}



	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}
	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}



	/**
	 * @return the authStatusStr
	 */
	public String getAuthStatusStr() {
		if(authStatusStr != null){
			return authStatusStr;
		}
		authStatusStr = getAuthStr(this.authStatus);
		return authStatusStr;
	}
	/**
	 * @param authStatusStr the authStatusStr to set
	 */
	public void setAuthStatusStr(String authStatusStr) {
		this.authStatusStr = authStatusStr;
	}

	public static String getAuthStr(int status){
		String tempWay;
		String[] authStatus = LanguageUtil.getDefaultValue("user_auth_status_value").split(",");
		if(status >= authStatus.length){
			return "UnKnow";
		}
		tempWay = authStatus[status+1];
		return tempWay;
	}

	/**
	 * @return the country_id
	 */
	public int getCountry_id() {
		return country_id;
	}
	/**
	 * @param country_id the country_id to set
	 */
	public void setCountry_id(int country_id) {
		this.country_id = country_id;
	}
	
	/**
	 * @return the phoneCode
	 */
	public String getPhoneCode() {
		return phoneCode;
	}
	/**
	 * @param phoneCode the phoneCode to set
	 */
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	/**
	 * @return the detailVo
	 */
	public UserDetailVo getDetailVo() {
		return detailVo;
	}
	/**
	 * @param detailVo the detailVo to set
	 */
	public void setDetailVo(UserDetailVo detailVo) {
		this.detailVo = detailVo;
	}
	
	
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	public UserVo(){

	}


	private void setWithResultSet (ResultSet rs, String prefix) throws SQLException {

		this.phone = BaseDao.getString(rs, prefix + "phone");
		this.nickName = BaseDao.getString(rs, prefix + "nickname");
		this.authStatus = BaseDao.getInt(rs, prefix + "auth_status");
		this.uId = BaseDao.getString(rs, prefix + "id");
		this.industryId = BaseDao.getString(rs, prefix + "industry_id");
		this.headUrl = BaseDao.getString(rs, prefix + "head_url");
		this.invite_code = BaseDao.getString(rs, prefix + "invite_code");
		this.register_date = BaseDao.getString(rs, prefix + "register_date");
		this.login_date = BaseDao.getString(rs, prefix + "login_date");
		this.credit_score = BaseDao.getInt(rs, prefix + "credit_score");
		this.cityId = BaseDao.getInt(rs, prefix + "city_id");
		this.cityName = BaseDao.getString(rs, prefix + "city_name");
		this.money = BaseDao.getBigDecimal(rs, prefix + "money");
		this.giftMoney = BaseDao.getBigDecimal(rs, prefix + "gift_money");
		this.phoneCode = BaseDao.getString(rs, prefix + "phone_code");

	}


	public UserVo (ResultSet rs, String prefix) {

		try {
			setWithResultSet(rs, prefix);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public UserVo(ResultSet rst){
		try {
			setWithResultSet(rst, "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public UserVo(HttpServletRequest req){
		this.phone = req.getParameter("phone");
		this.nickName = req.getParameter("nickname");
		this.money = ValueUtil.getBigDecimal(req.getParameter("money"));
		this.giftMoney = ValueUtil.getBigDecimal(req.getParameter("giftMoney"));
		this.authStatus = ValueUtil.getInt(req.getParameter("authStatus"));
		this.invite_code = req.getParameter("invaitCode");
		this.credit_score = ValueUtil.getInt(req.getParameter("creditScore"));
		this.cityId = ValueUtil.getInt(req.getParameter("cityId"));
		this.password = req.getParameter("password");
	}
}
