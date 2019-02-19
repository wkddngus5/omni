/**
 * FileName:     BikeMaintainDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月27日 下午4:50:35
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月27日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IBikeMaintainDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.BikeMaintainVo;

 /**
 * @ClassName:     BikeMaintainDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年7月27日 下午4:50:35
 *
 */
public class BikeMaintainDaoImpl implements IBikeMaintainDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeMaintainDao#getMaintainList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<BikeMaintainVo> getMaintainList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		List<BikeMaintainVo> maintainList = new ArrayList<BikeMaintainVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_bike_maintain where 1=1";
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		
		if(requestVo.getType() > 0){
			sql += " and type ="+requestVo.getType();
		}
		
		if(requestVo.getStatus() > 0){
			sql += " and status ="+(requestVo.getStatus()-1);
		}
		
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and (date >= '"+requestVo.getStartTime()
					+ "' or deal_date >= '"+requestVo.getStartTime()+"')";
		}
		
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and (date <= '"+requestVo.getEndTime()
					+ "' or deal_date <= '"+requestVo.getEndTime()+"')";
		}
		
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (number like '%"+requestVo.getKeyWords()+"%'"
					+ " or note like '%"+requestVo.getKeyWords()
					+ "%' or nickname like '%"+requestVo.getKeyWords()+"%')";
		}
		
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				maintainList.add(new BikeMaintainVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return maintainList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeMaintainDao#getMaintainCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getMaintainCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from view_bike_maintain where 1=1";
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		
		if(requestVo.getType() > 0){
			sql += " and type ="+requestVo.getType();
		}
		
		if(requestVo.getStatus() > 0){
			sql += " and status ="+(requestVo.getStatus()-1);
		}
		
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and (date >= '"+requestVo.getStartTime()
					+ "' or deal_date >= '"+requestVo.getStartTime()+"')";
		}
		
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and (date <= '"+requestVo.getEndTime()
					+ "' or deal_date <= '"+requestVo.getEndTime()+"')";
		}
		
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (number like '%"+requestVo.getKeyWords()+"%'"
					+ " or note like '%"+requestVo.getKeyWords()
					+ "%' or nickname like '%"+requestVo.getKeyWords()+"%')";
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
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
	 * @see com.pgt.bikelock.dao.IBikeMaintainDao#addMaintain(com.pgt.bikelock.vo.admin.BikeMaintain)
	 */
	public String addMaintain(BikeMaintainVo maintainVo) {
		// TODO Auto-generated method stub
		String[] bikeIds = maintainVo.getBid().split(",");
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_bike_maintain (bid,type,status,date,admin_id,deal_date,note) values  ";
		for (int i = 0; i < bikeIds.length; i++) {
			if(i!=0){
				sql = sql +",";
			}
			sql = sql +" (?,?,?,now(),?,?,?)";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String ids = "";
		
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS); 			
			for (int i = 0; i < bikeIds.length; i++) {
				int index = i*5;
				pstmt.setString(1+index,bikeIds[i]);
				pstmt.setInt(2+index,maintainVo.getType());
				pstmt.setInt(3+index, maintainVo.getStatus());
				pstmt.setString(4+index, maintainVo.getAdmin_id());
				pstmt.setString(5+index, maintainVo.getDeal_date());
				pstmt.setString(6+index, maintainVo.getNote());
			}
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					if(!rs.isFirst()){
						ids += rs.getString(1)+",";
					}else{
						ids += rs.getString(1);
					}
					
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return ids;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeMaintainDao#updateMaintain(com.pgt.bikelock.vo.admin.BikeMaintainVo)
	 */
	public boolean updateMaintain(BikeMaintainVo maintainVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_maintain set type=?,status=?,admin_id=?,deal_date=?,note=? where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, maintainVo.getType());
			pstmt.setInt(2,maintainVo.getStatus());
			pstmt.setString(3, maintainVo.getAdmin_id());
			pstmt.setString(4, maintainVo.getDeal_date());
			pstmt.setString(5, maintainVo.getNote());
			pstmt.setString(6,maintainVo.getId());
	

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeMaintainDao#getMaintainInfo(java.lang.String)
	 */
	public BikeMaintainVo getMaintainInfo(String id) {
		// TODO Auto-generated method stub
		BikeMaintainVo maintainVo = null;

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_bike_maintain where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				maintainVo = new BikeMaintainVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return maintainVo; 
	}

}
