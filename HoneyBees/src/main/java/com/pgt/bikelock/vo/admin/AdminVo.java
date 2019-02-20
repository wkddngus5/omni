package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.symmetric.Grain128.Base;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.ValueUtil;

public class AdminVo {
	private int id;
	private String userId;
	private String username;
	private String password;
	private String nickname;
	private String industryId;
	private String registerDate;
	private String loginDate;
	private int cityId;
	private String phone;
	private String email;
	private int isAdmin;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	 * @return the registerDate
	 */
	public String getRegisterDate() {
		return registerDate;
	}
	/**
	 * @param registerDate the registerDate to set
	 */
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	/**
	 * @return the loginDate
	 */
	public String getLoginDate() {
		return loginDate;
	}
	/**
	 * @param loginDate the loginDate to set
	 */
	public void setLoginDate(String loginDate) {
		this.loginDate = loginDate;
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
	 * @return the isAdmin
	 */
	public int getIsAdmin() {
		return isAdmin;
	}
	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	public AdminVo(){
		
	}

	public AdminVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst, "id");
		this.username =BaseDao.getString(rst, "user_name");
		this.nickname = BaseDao.getString(rst, "nickname");
		this.password = BaseDao.getString(rst, "password");
		this.registerDate = BaseDao.getString(rst, "register_date");
		this.loginDate = BaseDao.getString(rst, "login_date");
		this.cityId = BaseDao.getInt(rst, "city_id");
		this.industryId =BaseDao.getString(rst, "industryId");
		this.phone = BaseDao.getString(rst, "phone");
		this.email = BaseDao.getString(rst, "email");
		this.isAdmin = BaseDao.getInt(rst, "is_admin");
	}
	
	public AdminVo (HttpServletRequest request){
		if(request.getParameter("id") != null){
			this.id = ValueUtil.getInt(request.getParameter("id")) ;
		}
		this.username =request.getParameter("userName");
		this.nickname = request.getParameter("nickName");
		this.password = request.getParameter("password");
		this.cityId = ValueUtil.getInt(request.getAttribute("cityId"));
		this.phone = request.getParameter("phone");
		this.email = request.getParameter("email");
		this.isAdmin = ValueUtil.getInt(request.getParameter("isAdmin"));
	}

}
