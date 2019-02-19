/**
 * FileName:     RedPackBikeDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 下午5:30:27
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/intialization
 */
package com.pgt.bikelock.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IRedPackBikeDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.RedPackBikeVo;


 /**
 * @ClassName:     RedPackBikeDaoImpl
 * @Description:红包单车业务接口实现/red envelope bike business protocol achieve
 * @author:    Albert
 * @date:        2017年4月26日 下午5:30:27
 *
 */
public class RedPackBikeDaoImpl implements IRedPackBikeDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackBikeDao#getRedPackBikeList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<RedPackBikeVo> getRedPackBikeList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_redpack_bike"
				+ " where (phone like '%"+ requestVo.getKeyWords()+"%'"
					+" or number like '%"+ requestVo.getKeyWords()+"%')";
		if(requestVo.getCityId() != 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		sql +=" order by id "+requestVo.getOrderDirection()+" LIMIT ?,?" ;
		List<RedPackBikeVo> bikeList = new ArrayList<RedPackBikeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				RedPackBikeVo bike = new RedPackBikeVo(rs,2);
				bike.setStart_time(TimeUtil.formaStrDate(bike.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bike.setEnd_time(TimeUtil.formaStrDate(bike.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bikeList.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackBikeDao#addRedPackBike(com.pgt.bikelock.vo.admin.RedPackBikeVo)
	 */
	public String addRedPackBike(String bikeIds[],RedPackBikeVo bikeVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_red_package_bike (bid,rule_id,start_time ,end_time,date) values ";
		String ids = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			for (int i = 0; i < bikeIds.length; i++) {
				if(i!=0){
					sql = sql +",";
				}
				sql = sql +" (?,?,?,?,now())";
			}
			
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			
			for (int i = 0; i < bikeIds.length; i++) {
				int index = i*4;
				pstmt.setString(1+index, bikeIds[i]);
				pstmt.setString(2+index, bikeVo.getRule_id());
				pstmt.setString(3+index, bikeVo.getStart_time());
				pstmt.setString(4+index, bikeVo.getEnd_time());
			}
			
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				ids = "";
				while(rs.next()){
					ids += rs.getString(1)+",";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return ids; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackBikeDao#deleteRedPackBike(java.lang.String)
	 */
	public boolean deleteRedPackBike(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_red_package_bike where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			if(pstmt.executeUpdate() > 0){
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
	 * @see com.pgt.bikelock.dao.IRedPackBikeDao#getRedPackBike(java.lang.String)
	 */
	public RedPackBikeVo getNoUseRedPackBike(String bikeId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_red_package_bike where bid = ? and  uid = 0";
		RedPackBikeVo pack = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bikeId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				pack = new RedPackBikeVo(rs,1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return pack; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackBikeDao#getRedPackInfo(java.lang.String)
	 */
	public RedPackBikeVo getRedPackInfo(String packId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.*,r.coupon_num as count from t_red_package_bike b,t_red_package_rule r where b.rule_id = r.id and b.id = ?";
		RedPackBikeVo pack = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, packId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				pack = new RedPackBikeVo(rs,1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return pack; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackBikeDao#createRedPack(java.lang.String, java.math.BigDecimal)
	 */
	public boolean createRedPack(String userId,String packId, BigDecimal amount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update  t_red_package_bike set uid = ?,amount = ? , user_date = now() where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setBigDecimal(2, amount);
			pstmt.setString(3, packId);
			if(pstmt.executeUpdate() > 0){
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
	 * @see com.pgt.bikelock.dao.IRedPackBikeDao#getUserRedPackList(java.lang.String)
	 */
	public List<RedPackBikeVo> getUserRedPackList(String userId,int startPage) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_redpack_bike where uid = ?   limit ?,?";
		List<RedPackBikeVo> bikeList = new ArrayList<RedPackBikeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,userId);
			pstmt.setInt(2, startPage);
			pstmt.setInt(3, BaseDao.pageSize);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				RedPackBikeVo bike = new RedPackBikeVo(rs,2);
				bike.setStart_time(TimeUtil.formaStrDate(bike.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bike.setEnd_time(TimeUtil.formaStrDate(bike.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bikeList.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeList; 
	}


}
