/**
 * FileName:     UserDeviceDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年3月30日 上午11:44:54
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年3月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IUserDeviceDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.UserDeviceVo;

 /**
 * @ClassName:     UserDeviceDaoImpl
 * @Description:用户设备接口实现/user device protocol achieve
 * @author:    Albert
 * @date:        2017年3月30日 上午11:44:54
 *
 */
public class UserDeviceDaoImpl implements IUserDeviceDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDeviceDao#addDevice(com.pgt.bikelock.vo.UserDeviceVo)
	 */
	public boolean addDevice(UserDeviceVo deviceVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_user_device (type,token,uid,uuid,request_token) values (?,?,?,?,?)";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,deviceVo.getType());
			pstmt.setString(2, deviceVo.getToken());
			pstmt.setString(3, deviceVo.getuId());
			pstmt.setString(4, deviceVo.getUuid());
			pstmt.setString(5, deviceVo.getRequestToken());
			
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
	 * @see com.pgt.bikelock.dao.IUserDeviceDao#updateDevice(com.pgt.bikelock.vo.UserDeviceVo)
	 */
	public boolean updateDevice(UserDeviceVo deviceVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_device set type = ? ,token = ?,uuid = ?,request_token = ? where uid = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,deviceVo.getType());
			pstmt.setString(2, deviceVo.getToken());
			pstmt.setString(3, deviceVo.getUuid());
			pstmt.setString(4, deviceVo.getRequestToken());
			pstmt.setString(5, deviceVo.getuId());
			
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
	 * @see com.pgt.bikelock.dao.IUserDeviceDao#updateRequestToken(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateRequestToken(String uid, String token) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_device set request_token = ? where uid = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
	
			pstmt.setString(1, token);
			pstmt.setString(2, uid);
			
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
	 * @see com.pgt.bikelock.dao.IUserDeviceDao#getDeviceInfo(java.lang.String)
	 */
	public UserDeviceVo getDeviceInfo(String uid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select * from t_user_device where uid = ?";
		PreparedStatement pstmt = null;
		UserDeviceVo deviceVo = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			
			ResultSet rst = pstmt.executeQuery();
			
			while (rst.next()) {
				deviceVo = new UserDeviceVo(rst);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return deviceVo;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserDeviceDao#getDeviceTokenList(int)
	 */
	@Override
	public List<String> getDeviceTokenList(int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select token from t_user_device where type = ?";
		PreparedStatement pstmt = null;
		List<String> tokenList = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,type);
			
			ResultSet rst = pstmt.executeQuery();
			
			while (rst.next()) {
				tokenList.add(rst.getString("token"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return tokenList;
	}
}
