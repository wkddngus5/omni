/**
 * FileName:     CouponDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 下午3:38:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.ICouponDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     CouponDaoImpl
 * @Description:优惠券接口实现类/coupon protocol achieve
 * @author:    Albert
 * @date:        2017年4月6日 下午3:38:57
 *
 */
public class CouponDaoImpl implements ICouponDao{

	
	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getCouponList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<CouponVo> getCouponList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_coupon "
				+ " where name like '%"+requestVo.getKeyWords()+"%'";
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		if(requestVo.getType() == 1){
			sql += " and type <= 2";
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		List<CouponVo>   list = new ArrayList<CouponVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				CouponVo coupon = new CouponVo(rs);
				coupon.setStart_time(TimeUtil.formaStrDate(coupon.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				coupon.setEnd_time(TimeUtil.formaStrDate(coupon.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				list.add(coupon);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	public int getCount(RequestListVo requestVo) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  t_coupon "
				+ " where name like '%"+requestVo.getKeyWords()+"%'";
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		if(requestVo.getType() == 1){
			sql += " and type <= 2";
		}
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
		
			rs = pstmt.executeQuery();
			if(rs.next()) count = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#addCoupon(com.pgt.bikelock.vo.CouponVo)
	 */
	public String addCoupon(CouponVo couponVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_coupon (name,value,day,type,isrepeat,start_time,end_time,city_id) values (?,?,?,?,?,?,?,?) ";
		 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String couponId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, couponVo.getName());
			pstmt.setBigDecimal(2,couponVo.getValue());
			pstmt.setInt(3, couponVo.getDay());
			pstmt.setInt(4, couponVo.getType());
			pstmt.setInt(5, couponVo.getRepeat());
			pstmt.setString(6, couponVo.getStart_time());
			pstmt.setString(7, couponVo.getEnd_time());
			pstmt.setInt(8, couponVo.getCityId());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					couponId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return couponId;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getCouponInfo(java.lang.String)
	 */
	public CouponVo getCouponInfo(String id,boolean formateDate) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_coupon  where  id = ? " ;
		CouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new CouponVo(rs);
				if(formateDate){
					couponVo.setStart_time(TimeUtil.formaStrDate(couponVo.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
					couponVo.setEnd_time(TimeUtil.formaStrDate(couponVo.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getRegisterCouponInfo()
	 */
	public CouponVo getFreeCouponInfo(int type,int cityId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_coupon  where  type = ? and (city_id = ? or city_id = 0 ) and start_time <= UNIX_TIMESTAMP() and end_time >= UNIX_TIMESTAMP()" ;
		CouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, type);
			pstmt.setInt(2, cityId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new CouponVo(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}

	

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#updateCoupon(com.pgt.bikelock.vo.CouponVo)
	 */
	public boolean updateCoupon(CouponVo couponVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_coupon set name = ? ,value = ?,type = ?,day = ?,isrepeat = ?,"
				+ "start_time = ?,end_time = ?,city_id = ? where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, couponVo.getName());
			pstmt.setBigDecimal(2,couponVo.getValue());
			pstmt.setInt(3, couponVo.getType());
			pstmt.setInt(4, couponVo.getDay());
			pstmt.setInt(5, couponVo.getRepeat());
			pstmt.setString(6, couponVo.getStart_time());
			pstmt.setString(7, couponVo.getEnd_time());
			pstmt.setInt(8, couponVo.getCityId());
			pstmt.setString(9, couponVo.getId());
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#deleteCoupon(java.lang.String)
	 */
	public boolean deleteCoupon(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_coupon where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}


	
	


}
