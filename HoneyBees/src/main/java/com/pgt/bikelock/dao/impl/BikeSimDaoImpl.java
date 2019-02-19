package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IBikeSimDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.BikeSim;
import com.pgt.bikelock.vo.BleBikeVo;
 

public class BikeSimDaoImpl implements IBikeSimDao {

 
	public boolean insert(long imei, String iccid) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT "+TABLE_NAME+" (imei,iccid) values (?,?) ";
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, imei);
			pstmt.setString(2,iccid);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	
	
 
	public boolean deleteByImei(long imei) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="DELETE FROM "+TABLE_NAME+" WHERE imei = ? ";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, imei);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}



	public boolean deleteById(int id) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="DELETE FROM "+TABLE_NAME+" WHERE id = ? ";
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, id);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}



	public boolean update(long imei, String iccid) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="UPDATE "+TABLE_NAME+" SET iccid=? WHERE imei=? ";
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,iccid);
			pstmt.setLong(2, imei);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	public BikeSim find(long imei) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM "+TABLE_NAME +" WHERE imei=?" ;
		BikeSim bikeSim=null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, imei);
			rs = pstmt.executeQuery();
			if(rs.next()){
				bikeSim = getBikeSim(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeSim;  
	}
	
	private BikeSim getBikeSim(ResultSet rs) throws SQLException{
		BikeSim bikeSim = new BikeSim();
		bikeSim.setIccid(rs.getString(COLUMN_ICCID));
		bikeSim.setId(rs.getInt(COLUMN_ID));
		bikeSim.setImei(rs.getLong(COLUMN_IMEI));
	
		return bikeSim;
	}




	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeSimDao#getBikePhoneInfo(java.lang.String)
	 */
	public BikeSim getBikePhoneInfo(String number) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM view_bike_phone where number = ?" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BikeSim bikeSim = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,number);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bikeSim = new BikeSim(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeSim; 
	}

	

}
