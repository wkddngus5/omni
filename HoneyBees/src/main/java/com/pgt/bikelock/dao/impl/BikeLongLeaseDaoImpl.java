/**
 * FileName:     BikeLongLeaseDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 上午11:28:06
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月5日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IBikeLongLeaseDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.BikeLongLeaseVo;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.BikeVo;

 /**
 * @ClassName:     BikeLongLeaseDaoImpl
 * @Description:单车长租接口实现/bike long rent protocol achieve
 * @author:    Albert
 * @date:        2017年4月5日 上午11:28:06
 *
 */
public class BikeLongLeaseDaoImpl implements IBikeLongLeaseDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeLongLeaseDao#addLease(com.pgt.bikelock.vo.BikeLongLeaseVo)
	 */
	public String addLease(BikeLongLeaseVo leaseVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_bike_long_lease (uid,start_time,end_time) values (?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String recordId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1,leaseVo.getUid());
			pstmt.setString(2, leaseVo.getStart_time());
			pstmt.setString(3, leaseVo.getEnd_time());
		
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					recordId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return recordId;  
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeLongLeaseDao#updatePaySuccess()
	 */
	public boolean updatePaySuccess(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_long_lease set ispay = 1 where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,id);

			if(pstmt.executeUpdate()>0){
				flag =  true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeLongLeaseDao#getUserLeaseInfo(java.lang.String)
	 */
	public BikeLongLeaseVo getUserLeaseInfo(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT *  FROM  t_bike_long_lease  where uid = ? and end_time > UNIX_TIMESTAMP() and ispay = 1" ;
		BikeLongLeaseVo  leaseVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				leaseVo = new BikeLongLeaseVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return leaseVo; 
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeLongLeaseDao#getLeaseInfo(java.lang.String)
	 */
	public BikeLongLeaseVo getLeaseInfo(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT *  FROM  t_bike_long_lease  where id = ?" ;
		BikeLongLeaseVo  leaseVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				leaseVo = new BikeLongLeaseVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return leaseVo; 
	}



	
}
