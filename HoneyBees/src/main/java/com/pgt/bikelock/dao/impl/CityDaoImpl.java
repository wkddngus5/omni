/**
 * FileName:     CityDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月7日 下午5:05:03
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月7日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.ICityDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     CityDaoImpl
 * @Description:城市接口实现/city protocol achieve
 * @author:    Albert
 * @date:        2017年4月7日 下午5:05:03
 *
 */
public class CityDaoImpl implements ICityDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICityDao#getCityLlist()
	 */
	public List<CityVo> getCityLlist(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_city ";
		if(requestVo != null){
			sql += " order by id "+requestVo.getOrderDirection();
		}
		List<CityVo>  list= new ArrayList<CityVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 

			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVo type = new CityVo(rs);
				list.add(type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICityDao#getNearCitylist(com.pgt.bikelock.vo.LatLng)
	 */
	@Override
	public List<CityVo> getNearCitylist(LatLng location) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_city ORDER BY abs(area_lat - ?) asc,abs(area_lng-?) asc";
		List<CityVo>  list= new ArrayList<CityVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setDouble(1, location.getLatitude());
			pstmt.setDouble(2, location.getLongitude());
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				CityVo type = new CityVo(rs);
				list.add(type);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICityDao#getCityInfo(java.lang.String)
	 */
	public CityVo getCityInfo(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_city where id = ?" ;
		CityVo  cityVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				cityVo = new CityVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cityVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICityDao#addCity(com.pgt.bikelock.vo.CityVo)
	 */
	public String addCity(CityVo cityVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_city (name,code,note,area_detail,area_lat,area_lng) values (?,?,?,?,?,?) ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String cityId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, cityVo.getName());
			pstmt.setString(2,cityVo.getCode());
			pstmt.setString(3, cityVo.getNote());
			pstmt.setString(4, cityVo.getArea_detail());
			pstmt.setDouble(5, cityVo.getArea_lat());
			pstmt.setDouble(6, cityVo.getArea_lng());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					cityId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return cityId;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICityDao#updateCity(com.pgt.bikelock.vo.CityVo)
	 */
	public boolean updateCity(CityVo cityVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_city set name = ?,code = ?,note= ?,area_detail= ?,area_lat= ?,area_lng= ? where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, cityVo.getName());
			pstmt.setString(2,cityVo.getCode());
			pstmt.setString(3, cityVo.getNote());
			pstmt.setString(4, cityVo.getArea_detail());
			pstmt.setDouble(5, cityVo.getArea_lat());
			pstmt.setDouble(6, cityVo.getArea_lng());
			pstmt.setString(7, cityVo.getId());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

}
