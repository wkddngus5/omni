/**
 * FileName:     BikeTypeDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 上午10:55:12
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IBikeTypeDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     BikeTypeDaoImpl
 * @Description:单车类型接口实现/bike type protocol achieve
 * @author:    Albert
 * @date:        2017年4月6日 上午10:55:12
 *
 */
public class BikeTypeDaoImpl implements IBikeTypeDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeTypeDao#getTypeInfo(java.lang.String)
	 */
	public BikeTypeVo getTypeInfo(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_bike_type where id = ?" ;
		BikeTypeVo typeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				typeVo = new BikeTypeVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return typeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getTypeList()
	 */
	public List<BikeTypeVo> getTypeList(int industryId,int cityId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_bike_type where industry_id = ?";
		if(cityId > 0){
			sql += " and city_id = "+cityId;
		}
		List<BikeTypeVo>  list= new ArrayList<BikeTypeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,industryId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeTypeVo type = new BikeTypeVo(rs);
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
	 * @see com.pgt.bikelock.dao.IBikeTypeDao#updateTypeInfo(com.pgt.bikelock.vo.BikeTypeVo)
	 */
	public boolean updateTypeInfo(BikeTypeVo typeVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_type set price = ? ,count = ?,city_id = ?,unit_type = ?" +
			", hold_unit_type = ?, hold_count = ?, hold_price = ?, hold_max_count = ? where id = ?";


		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setBigDecimal(1, typeVo.getPrice());
			pstmt.setInt(2, typeVo.getCount());
			pstmt.setInt(3, typeVo.getCity_id());
			pstmt.setInt(4, typeVo.getUnit_type());
			pstmt.setInt(5, typeVo.getHoldUnitType());
			pstmt.setInt(6, typeVo.getHoldCount());
			pstmt.setBigDecimal(7, typeVo.getHoldPrice());
			pstmt.setInt(8, typeVo.getHoldMaxCount());
			pstmt.setString(9,typeVo.getId());

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
	 * @see com.pgt.bikelock.dao.IBikeTypeDao#addTypeInfo(com.pgt.bikelock.vo.BikeTypeVo)
	 */
	@Override
	public boolean addTypeInfo(BikeTypeVo typeVo,int industryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_bike_type (price,count,city_id,unit_type,industry_id,hold_unit_type,hold_count,hold_price,hold_max_count) " +
			"values (?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setBigDecimal(1, typeVo.getPrice());
			pstmt.setInt(2, typeVo.getCount());
			pstmt.setInt(3, typeVo.getCity_id());
			pstmt.setInt(4, typeVo.getUnit_type());
			pstmt.setInt(5, industryId);


			pstmt.setInt(6, typeVo.getHoldUnitType());
			pstmt.setInt(7, typeVo.getHoldCount());
			pstmt.setBigDecimal(8, typeVo.getHoldPrice());
			pstmt.setInt(9, typeVo.getHoldMaxCount());


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
	 * @see com.pgt.bikelock.dao.IBikeTypeDao#checkTypeExist(com.pgt.bikelock.vo.BikeTypeVo)
	 */
	@Override
	public BikeTypeVo checkTypeExist(BikeTypeVo typeVo,int industryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_bike_type where city_id = ? and unit_type = ? and industry_id = ?" ;
		BikeTypeVo oldTypeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, typeVo.getCity_id());
			pstmt.setInt(2, typeVo.getUnit_type());
			pstmt.setInt(3, industryId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				oldTypeVo = new BikeTypeVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return oldTypeVo; 
	}
}
