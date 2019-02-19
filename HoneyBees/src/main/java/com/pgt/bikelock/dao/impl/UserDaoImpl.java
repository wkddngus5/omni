/**
 * FileName:     UserDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 下午4:04:10
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.JCEMac.MD4;

import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserDeviceVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;


 /**
 * @ClassName:     UserDaoImpl
 * @Description:用户相关接口实现/user related protocol achieve
 * @author:    Albert
 * @date:        2017-3-24 下午4:04:10
 *
 */
public class UserDaoImpl implements IUserDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#addUser(com.pgt.bikelock.vo.UserVo)
	 */
	public String addUser(UserVo user) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_user (phone,industry_id,register_date,login_date,auth_status,invite_code,password,country_id)"
				+ " VALUES(?,?,now(),now(),?,?,?,?)";
		String userId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1,user.getPhone());
			pstmt.setString(2, user.getIndustryId());			
			//生成6位邀请码/generate 6 number invitation code
			pstmt.setString(4, ValueUtil.getStringRandom(6));
			if("1".equals(OthersSource.getSourceString("register_auto_commit_userinfo"))){
				//手机号注册且是自动提交用户资料，自动完成实名认证/phone number register and auto forward user information, auto finish certification in real name
				pstmt.setInt(3, user.getPhone() != null?2:0);
			}else{
				//手机号注册，自动完成手机号认证/phone number register, auto finish phone number certification
				pstmt.setInt(3, 1);
			}
			if(!StringUtils.isEmpty(user.getPassword())){
				pstmt.setString(5, MD5Utils.getMD5(user.getPassword()));
			}else{
				pstmt.setString(5, MD5Utils.getMD5(ValueUtil.getStringRandom(6)));
			}
			
			pstmt.setInt(6, user.getCountry_id());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					userId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return userId;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUser(java.lang.String)
	 */
	public UserVo getUser(String phone,int industryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_user WHERE phone = ? and industry_id = ?" ;
		UserVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			pstmt.setInt(2, industryId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				user = new UserVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return user; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUserByEmail(java.lang.String, int)
	 */
	@Override
	public UserVo getUserByEmail(String email, int industryId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#gerUser(java.lang.String, java.lang.String)
	 */
	public UserVo getUser(String phone, String password,int industryId,int countryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_user WHERE phone = ? and password = ? and industry_id = ?" ;
		if(countryId > 0){
			sql += " and country_id ="+countryId;
		}
		UserVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, phone);
			pstmt.setString(2, password);
			pstmt.setInt(3, industryId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				user = new UserVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return user; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUserWithId(java.lang.String)
	 */
	public UserVo getUserWithId(String userId,boolean formateName) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT u.*,d.firstname,d.lastname,c.`name` as city_name FROM t_user u ,"
				+ "t_user_detail d,t_city c WHERE u.id = d.uid and u.city_id = c.id and u.id = ?" ;
		UserVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				user = new UserVo(rs);
				if(formateName){
					String firstname = rs.getString("firstname");
					String lastname = rs.getString("lastname");
					if(!StringUtils.isEmpty(firstname) && firstname.equals(lastname)){
						user.setNickName(firstname);
					}else{
						user.setNickName(lastname+" "+firstname);
					}
					if(StringUtils.isEmpty(user.getNickName())){
						if(!StringUtils.isEmpty(firstname)
								&& !StringUtils.isEmpty(lastname)){
							if(firstname.equals(lastname)){
								user.setNickName(firstname);
							}else{
								user.setDefaultNickName(firstname, lastname);
							}
						}else{
							user.setNickName(user.getPhone());
						}
						
					}
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return user; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUserWithId(java.lang.String, java.lang.String)
	 */
	public UserVo getUserWithId(String userId, String password,int industryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_user WHERE id = ? and password = ? and industry_id = ?" ;
		UserVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, password);
			pstmt.setInt(3, industryId);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				user = new UserVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return user; 
	}

	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUserWithIndustryInfo(java.lang.String)
	 */
	public UserVo getUserWithIndustryInfo(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT u.*,i.industry_name,i.consume_type FROM t_user u,t_industry i WHERE u.industry_id=i.id and u.id = ?" ;
		UserVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				user = new UserVo(rs);
				user.setIndustryVo(new IndustryVo(rs,1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return user; 
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUserDetailInfo(java.lang.String)
	 */
	public UserDetailVo getUserDetailInfo(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_user_detail WHERE uid = ?" ;
		UserDetailVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				user = new UserDetailVo(rs,1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return user; 
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUserId(java.lang.String)
	 */
	public String getUserId(String inviteCode) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT id FROM t_user WHERE invite_code = ?" ;
		String userId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, inviteCode);
			rs = pstmt.executeQuery();
			if(rs.next()){
				userId = rs.getString("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return userId; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#thirdUserLogin(java.lang.String, int)
	 */
	public UserVo thirdUserLogin(String uuid, int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT u.* FROM t_third_user h,t_user u WHERE h.uid=u.id and uuid=? and type=?";
		UserVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,uuid);
			pstmt.setInt(2, type);
			rs = pstmt.executeQuery();
			if(rs.next()){
				user = new UserVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return user; 
	}

	public String getThirdId(String uuid,int type){
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT id FROM t_third_user where uuid=? and type=?";
		String thirdId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,uuid);
			pstmt.setInt(2, type);
			rs = pstmt.executeQuery();
			if(rs.next()){
				thirdId = rs.getString("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return thirdId; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getThirdUUID(java.lang.String)
	 */
	@Override
	public String getThirdUUID(String uid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT uuid FROM t_third_user where uid=?";
		String thirdId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,uid);

			rs = pstmt.executeQuery();
			if(rs.next()){
				thirdId = rs.getString("uuid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return thirdId; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#addThirdInfo(java.lang.String, int, int)
	 */
	public String addThirdInfo(String uuid, int type, String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_third_user (uid,uuid,type) VALUES(?,?,?)";
		String thirdId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
		 
			pstmt.setString(1,userId);
			pstmt.setString(2,uuid);
			pstmt.setInt(3,type);
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					thirdId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return thirdId; 
	}
	
	public boolean updateThirdInfo(String userId,String thirdId){
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_third_user set uid = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userId);
			pstmt.setString(2,thirdId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateLoginDate(java.lang.String)
	 */
	public boolean updateLoginDate(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set login_date = now() where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateMoney(java.math.BigDecimal)
	 */
	public boolean updateMoney(BigDecimal money,String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set money = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1,money);
			pstmt.setString(2,userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateGiftMoney(java.math.BigDecimal, java.lang.String)
	 */
	@Override
	public boolean updateGiftMoney(BigDecimal money, String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set gift_money = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1,money);
			pstmt.setString(2,userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateUserMoney(java.math.BigDecimal, java.math.BigDecimal, java.lang.String)
	 */
	@Override
	public boolean updateUserMoney(BigDecimal balanceMoney,
			BigDecimal giftMoney, String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set money = ? , gift_money = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1,balanceMoney);
			pstmt.setBigDecimal(2,giftMoney);
			pstmt.setString(3,userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}
	

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateUserInfo(com.pgt.bikelock.vo.UserVo)
	 */
	public boolean updateUserInfo(UserVo userVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set phone = ?,nickname = ?,head_url = ?,invite_code = ?,city_id = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userVo.getPhone());
			pstmt.setString(2,userVo.getNickName());
			pstmt.setString(3, userVo.getHeadUrl());
			pstmt.setString(4, userVo.getInvite_code());
			pstmt.setInt(5, userVo.getCityId());
			pstmt.setString(6, userVo.getuId());
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateUserAllInfo(com.pgt.bikelock.vo.UserVo)
	 */
	public boolean updateUserAllInfo(UserVo userVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set phone = ?,nickname = ?,money = ?,auth_status = ?,invite_code = ?,"
				+ "credit_score = ?,city_id = ? ,gift_money = ?";
		if(!StringUtils.isEmpty(userVo.getPassword())){
			sql += " ,password = '"+MD5Utils.getMD5(userVo.getPassword())+"'";
		}
		sql += "  where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userVo.getPhone());
			pstmt.setString(2,userVo.getNickName());
			pstmt.setBigDecimal(3, userVo.getMoney());
			pstmt.setInt(4, userVo.getAuthStatus());
			pstmt.setString(5, userVo.getInvite_code());
			pstmt.setInt(6, userVo.getCredit_score());
			pstmt.setInt(7, userVo.getCityId());
			pstmt.setBigDecimal(8, userVo.getGiftMoney());
			pstmt.setString(9, userVo.getuId());
		
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updatePassword(java.lang.String, java.lang.String)
	 */
	public boolean updatePassword(String userId, String password) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set password  = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,password);
			pstmt.setString(2, userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateAuthStatus(java.lang.String, int)
	 */
	public boolean updateAuthStatusCheckFront(String userId, int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set auth_status  = ? where id = ? and auth_status = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,status);
			pstmt.setString(2, userId);
			pstmt.setInt(3,status-1);//关联前置认证进度/connect certification schedule
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateAuthStatus(java.lang.String, int)
	 */
	public boolean updateAuthStatus(String userId, int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set auth_status  = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,status);
			pstmt.setString(2, userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	public boolean updateAuthDeposit(String userId,int authNum) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set auth_status  = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authNum);
			pstmt.setString(2, userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateAuthStatus(java.lang.String, java.lang.String)
	 */
	public boolean updateAuthStatus(String userId, String phone) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set phone = ?,auth_status  = 1 where id = ? and auth_status = 0";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,phone);
			pstmt.setString(2, userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updatePhone(java.lang.String, java.lang.String)
	 */
	public boolean updatePhone(String userId, String phone,int countryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set phone = ?,country_id =? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,phone);
			pstmt.setInt(2, countryId);
			pstmt.setString(3, userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#updateCity(java.lang.String, int)
	 */
	@Override
	public boolean updateCity(String userId, int cityId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user set city_id = ? where  city_id != ? and id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cityId);
			pstmt.setInt(2, cityId);
			pstmt.setString(3, userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getUserList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<UserDetailVo> getUserList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_user_list " +
				" where (phone like '%"+requestVo.getKeyWords()
				+"%' or nickname like '%"+requestVo.getKeyWords()
				+"%' or firstname like '%"+requestVo.getKeyWords()
				+"%' or lastname like '%"+requestVo.getKeyWords()
				+"%' or uid like '%"+requestVo.getKeyWords()
				+"%' or email like '%"+requestVo.getKeyWords()+"%')";
				 
		
		if(requestVo.getStatus() > 0){
			sql += " and auth_status = "+(requestVo.getStatus()-1);
		}
		
		if(requestVo.getStartTime() != null){
			sql += " and register_date >= '"+requestVo.getStartTime()+"'";
		}
		
		if(requestVo.getEndTime() != null){
			sql += " and register_date <= '"+requestVo.getEndTime()+"'";
		}
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getOrderField()) && !StringUtils.isEmpty(requestVo.getOrderDirection())){
			sql += " order by "+requestVo.getOrderField()+" "+requestVo.getOrderDirection()+" LIMIT ?,?";
		}else{
			sql += " order by uid desc LIMIT ?,?";
		}
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserDetailVo>  list = new ArrayList<UserDetailVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
		
			rs = pstmt.executeQuery();
			while(rs.next()){
				UserDetailVo userVo = new UserDetailVo(rs,2);
				if(StringUtils.isEmpty(userVo.getUserVo().getNickName())){
					if(!StringUtils.isEmpty(userVo.getLastname())
							&& !StringUtils.isEmpty(userVo.getFirstname())){
						if(userVo.getFirstname().equals(userVo.getLastname())){
							userVo.getUserVo().setNickName(userVo.getFirstname());
						}else{
							userVo.getUserVo().setDefaultNickName(userVo.getFirstname(), userVo.getLastname());
						}
					}else{
						userVo.getUserVo().setNickName(userVo.getUserVo().getPhone());
					}
					
				}
				list.add(userVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from view_user_list " +
				" where (phone like '%"+requestVo.getKeyWords()
				+"%' or nickname like '%"+requestVo.getKeyWords()
				+"%' or firstname like '%"+requestVo.getKeyWords()
				+"%' or lastname like '%"+requestVo.getKeyWords()
				+"%' or uid like '%"+requestVo.getKeyWords()
				+"%' or email like '%"+requestVo.getKeyWords()+"%')";
				 
		
		if(requestVo.getStatus() > 0){
			sql += " and auth_status = "+(requestVo.getStatus()-1);
		}
		
		if(requestVo.getStartTime() != null){
			sql += " and register_date >= '"+requestVo.getStartTime()+"'";
		}
		
		if(requestVo.getEndTime() != null){
			sql += " and register_date <= '"+requestVo.getEndTime()+"'";
		}
		
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getRedPackAmount(java.lang.String)
	 */
	public BigDecimal getRedPackAmount(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT redpack FROM t_user_detail where uid = ?" ;
		BigDecimal amount = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				amount = rs.getBigDecimal("redpack");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return amount; 
	}
	
	public BigDecimal getMoneyAmount(String userId){
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT money FROM t_user where id = ?" ;
		BigDecimal amount = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				amount = rs.getBigDecimal("money");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return amount; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#getGifgMoneyAmount(java.lang.String)
	 */
	@Override
	public BigDecimal getGifgMoneyAmount(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT gifg_money FROM t_user where id = ?" ;
		BigDecimal amount = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				amount = rs.getBigDecimal("gifg_money");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return amount; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDao#cashRedPack(java.lang.String)
	 */
	public boolean cashRedPack(String userId,BigDecimal amount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_detail set redpack = redpack - ? where uid = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1,amount);
			pstmt.setString(2, userId);
			
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	
	public List<StatisticsVo> getStatusStatisticsList() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		//获取可用类型元素/gain available type element
		String sql="select count(*) as value,auth_status as title from t_user group by title";
		List<StatisticsVo> recordList = new ArrayList<StatisticsVo>();
		recordList.add(new StatisticsVo());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				StatisticsVo record = new StatisticsVo(rs);
				record.setTitle(UserVo.getAuthStr(ValueUtil.getInt(record.getTitle())));
				recordList.add(record);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return recordList; 
	}







}
