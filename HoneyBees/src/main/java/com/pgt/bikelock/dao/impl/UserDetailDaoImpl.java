/**
 * FileName:     UserDetailDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月12日 下午4:45:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IUserDetailDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.UserDetailVo;

 /**
 * @ClassName:     UserDetailDaoImpl
 * @Description:用户详情业务实现/user detail business achieve
 * @author:    Albert
 * @date:        2017年7月12日 下午4:45:21
 *
 */
public class UserDetailDaoImpl implements IUserDetailDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDetailDao#getUserDetail(java.lang.String)
	 */
	public UserDetailVo getUserDetail(String uid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_user_detail WHERE uid = ?" ;
		UserDetailVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
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
	 * @see com.pgt.bikelock.dao.IUserDetailDao#getUserDetailWithEmail(java.lang.String)
	 */
	@Override
	public UserDetailVo getUserDetailWithEmail(String email) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_user_detail WHERE email = ?" ;
		UserDetailVo user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
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
	 * @see com.pgt.bikelock.dao.IUserDetailDao#updateUserDetail(com.pgt.bikelock.vo.UserDetailVo)
	 */
	public boolean updateUserDetail(UserDetailVo detailVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_detail set firstname = ?,lastname = ?,email = ?,"
				+ "address = ?,zip_code = ?,country = ?,idcard = ?,third_info=?,gender=?,"
				+ "birthday=?,email_auth = ? where uid = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,detailVo.getFirstname());
			pstmt.setString(2,detailVo.getLastname());
			pstmt.setString(3, detailVo.getEmail());
			pstmt.setString(4, detailVo.getAddress());
			pstmt.setString(5,detailVo.getZip_code());
			pstmt.setString(6,detailVo.getCountry());
			pstmt.setString(7,detailVo.getIdcard());
			pstmt.setString(8, detailVo.getThirdInfo());
			pstmt.setInt(9, detailVo.getGender());
			pstmt.setString(10, detailVo.getBirthday());
			pstmt.setInt(11, detailVo.getEmailAuth());
			pstmt.setString(12, detailVo.getUid());
			
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
	 * @see com.pgt.bikelock.dao.IUserDetailDao#emailAuth(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean emailAuth(String userId, String email,int authType) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_detail set email_auth = ?,email = ? where uid = ? ";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, authType);
			pstmt.setString(2,email);
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



}
