/**
 * FileName:     UserDetailVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月1日 下午7:21:30/7:21:30 pm, April 1, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.fasterxml.jackson.databind.deser.Deserializers.Base;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

/**
 * @ClassName:     UserDetailVo
 * @Description:用户详情实体/user detail entity
 * @author:    Albert
 * @date:        2017年4月1日 下午7:21:30/7:21:30 pm, April 1, 2017
 *
 */
public class UserDetailVo {
	
	public static final String NETPAY_CUSTOMER_ID = "netPayCustomerProfileId";
	public static final String NETPAY_PAYMENT_ID = "netPayCustomerPaymentProfileId";
	public static final String STRIPE_CUSTOMER_ID = "stripCustomerProfileId";
	public static final String STRIPE_PAYMENT_ID = "stripCustomerPaymentProfileId";
	
	String uid;
	UserVo userVo;//用户基础信息实体//user basic infomation entity
	String firstname;
	String lastname;
	String email;
	String address;
	String zip_code;
	String country;
	String idcard;
	String birthday;
	int gender;//性别 1:男 2：女/gender 1:male 2:female
	String thirdInfo;//关联第三方信息（JSON）/third party information
	BigDecimal redpack;//未提现红包总额/not withdrawed amount of red packet
	CountryVo countryVo;
	int emailAuth;//email auth 0:no 1:yes
	/**显示属性**//**display attribute**/
	JSONObject thirdInfoObject;
	String genderStr;
	int rideCount;
	
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
	 * @return the userVo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	/**
	 * @param userVo the userVo to set
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the zip_code
	 */
	public String getZip_code() {
		return zip_code;
	}
	/**
	 * @param zip_code the zip_code to set
	 */
	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	
	
	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	/**
	 * @return the gender
	 */
	public int getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(int gender) {
		this.gender = gender;
	}
	/**
	 * @return the idcard
	 */
	public String getIdcard() {
		return idcard;
	}
	/**
	 * @param idcard the idcard to set
	 */
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	
	
	/**
	 * @return the thirdInfo
	 */
	public String getThirdInfo() {
		return thirdInfo;
	}
	/**
	 * @param thirdInfo the thirdInfo to set
	 */
	public void setThirdInfo(String thirdInfo) {
		this.thirdInfo = thirdInfo;
	}
	
	/**
	 * @return the redpack
	 */
	public BigDecimal getRedpack() {
		return redpack;
	}
	/**
	 * @param redpack the redpack to set
	 */
	public void setRedpack(BigDecimal redpack) {
		this.redpack = redpack;
	}
	/**
	 * @return the thirdInfoObject
	 */
	public String getThirdInfoObject(String key) {
		if(thirdInfoObject == null){
			return null;
		}
		return (String) thirdInfoObject.get(key);
	}
	
	public JSONObject getThirdInfoObject() {
		return thirdInfoObject;
	}
	
	/**
	 * @param thirdInfoObject the thirdInfoObject to set
	 */
	public void setThirdInfoObject() {
		if(!StringUtils.isEmpty(this.thirdInfo)){
			thirdInfoObject = JSONObject.parseObject(this.thirdInfo);
		}
		
	}
	
	
	
	/**
	 * @return the countryVo
	 */
	public CountryVo getCountryVo() {
		return countryVo;
	}
	/**
	 * @param countryVo the countryVo to set
	 */
	public void setCountryVo(CountryVo countryVo) {
		this.countryVo = countryVo;
	}
	/**
	 * @return the genderStr
	 */
	public String getGenderStr() {
		return genderStr;
	}
	/**
	 * @param genderStr the genderStr to set
	 */
	public void setGenderStr(String genderStr) {
		this.genderStr = genderStr;
	}
	
	
	
	/**
	 * @return the rideCount
	 */
	public int getRideCount() {
		return rideCount;
	}
	/**
	 * @param rideCount the rideCount to set
	 */
	public void setRideCount(int rideCount) {
		this.rideCount = rideCount;
	}
	
	
	
	
	/**
	 * @return the emailAuth
	 */
	public int getEmailAuth() {
		return emailAuth;
	}
	/**
	 * @param emailAuth the emailAuth to set
	 */
	public void setEmailAuth(int emailAuth) {
		this.emailAuth = emailAuth;
	}
	public UserDetailVo(){

	}

	/**
	 * 
	 * @param rst
	 * @param type 1:详情 2：列表/1: detail 2: list
	 * @throws SQLException
	 */
	public UserDetailVo(ResultSet rst,int type) throws SQLException{
		this.firstname = rst.getString("firstname");
		this.lastname = rst.getString("lastname");
		this.email = rst.getString("email");
		this.uid = rst.getString("uid");
		this.address = BaseDao.getString(rst, "address");
		this.zip_code = BaseDao.getString(rst, "zip_code");
		this.country = BaseDao.getString(rst, "country");
		this.idcard = BaseDao.getString(rst,"idcard");
		this.birthday = BaseDao.getString(rst,"birthday");
		this.gender = BaseDao.getInt(rst,"gender");
		this.emailAuth = BaseDao.getInt(rst, "email_auth");
		setGenderStr(LanguageUtil.getTypeStr("user_gender_value", this.gender));
		this.thirdInfo = BaseDao.getString(rst, "third_info");
		this.redpack = BaseDao.getBigDecimal(rst, "redpack");
		setThirdInfoObject();
		
		this.userVo = new UserVo(rst);
		this.countryVo = new CountryVo(rst);
		this.rideCount = BaseDao.getInt(rst,"ride_count");
	}

	/**
	 * 
	 * @param req
	 */
	public void setUserDetailVo(HttpServletRequest req){
		this.firstname = req.getParameter("firstName");
		this.lastname = req.getParameter("lastName");
		this.email = req.getParameter("email");
		this.address = req.getParameter("address");
		this.zip_code = req.getParameter("zipCode");
		this.country = req.getParameter("counTry");
		if(req.getParameter("idcard") != null){
			this.idcard = req.getParameter("idcard");
		}
		if(req.getParameter("birthday") != null){
			this.birthday = req.getParameter("birthday");
		}
		if(req.getParameter("gender") != null){
			this.gender = ValueUtil.getInt(req.getParameter("gender"));
		}
		if(req.getParameter("redpack") != null){
			this.redpack = ValueUtil.getBigDecimal(req.getParameter("redpack"));
		}
	}

}
