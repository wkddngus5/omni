/**
 * FileName:     BikeReserveDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-25 下午5:10:48
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-25       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IBikeReserveDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.BikeReserveVo;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.BikeVo;

 /**
 * @ClassName:     BikeReserveDaoImpl
 * @Description:单车预约接口实现/bike booking protocol achieve
 * @author:    Albert
 * @date:        2017-3-25 下午5:10:48
 *
 */
public class BikeReserveDaoImpl implements IBikeReserveDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#reserveBike(java.lang.String, java.lang.String)
	 */
	public boolean reserveBike(String userId, String bikeId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_bike_reserve (bid,uid,date,out_date) VALUES(?,?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(2,userId);
			pstmt.setString(1, bikeId);
			long nowTime = TimeUtil.getCurrentLongTime();
			pstmt.setLong(3, nowTime);
			pstmt.setLong(4, nowTime + BikeReserveVo.Reserve_TimeOut*60);
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
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#cancelReserveBike(java.lang.String, java.lang.String)
	 */
	public boolean cancelReserveBike(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_bike_reserve where uid = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1,userId);

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
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#updateReserveStatus(java.lang.String, int)
	 */
	@Override
	public boolean updateReserveStatus(String userId, int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_reserve set status = ? where uid = ?  and status = 0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,status);
			pstmt.setString(2,userId);

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
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#getReserveInfo(java.lang.String)
	 */
	public BikeReserveVo getReserveInfo(String bid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_bike_reserve where bid = ? and status = 0" ;
		BikeReserveVo  reserveVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, bid);
			rs = pstmt.executeQuery();
			while(rs.next()){
				reserveVo =  new BikeReserveVo(rs,1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return reserveVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#getUserReserveInfo(java.lang.String)
	 */
	public BikeReserveVo getUserReserveInfo(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.id,number,g_lat,g_lng,date,out_date FROM t_bike_reserve r left JOIN t_bike b ON r.bid = b.id "
				+ "where uid = ? and r.status = 0" ;
		BikeReserveVo  reserveVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				reserveVo =  new BikeReserveVo(rs,2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return reserveVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#getUserCancelCountInDay(java.lang.String)
	 */
	@Override
	public int getUserCancelCountInDay(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num  FROM t_bike_reserve where "
				+ "date_format(FROM_UNIXTIME(date),\"%Y-%m-%d\") = date_format(CURDATE(),\"%Y-%m-%d\") and status > 2 and uid = ?" ;
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt("num") ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#userHaveReserve(java.lang.String)
	 */
	public boolean userHaveReserve(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  t_bike_reserve where uid = ? and status = 0" ;
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				flag = rs.getInt("num") > 0? true:false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeReserveDao#getReserveListInTwoMinutes()
	 */
	public List<BikeReserveVo> getReserveListInTwoMinutes() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_bike_reserve where out_date >= UNIX_TIMESTAMP()-60*2 and status = 0" ;
		List<BikeReserveVo>  list = new ArrayList<BikeReserveVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeReserveVo reserve =  new BikeReserveVo(rs,1);
				list.add(reserve);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}


	

}
