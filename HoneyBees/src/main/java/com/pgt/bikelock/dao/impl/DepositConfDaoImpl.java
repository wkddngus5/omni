/**
 * FileName:     DepositConfDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月3日 下午2:22:03
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月3日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initilization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.pgt.bikelock.dao.IDepositConfDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.DepositConfVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     DepositConfDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年7月3日 下午2:22:03
 *
 */
public class DepositConfDaoImpl implements IDepositConfDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositConfDao#getDepositConfList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<DepositConfVo> getDepositConfList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_deposit_conf" ;
		if(requestVo.getCityId() > 0){
			sql += " where city_id = 0 or city_id = "+requestVo.getCityId();
		}
		List<DepositConfVo>  list= new ArrayList<DepositConfVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepositConfVo conf = new DepositConfVo(rs);
				
				list.add(conf);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositConfDao#getDepositConf(int)
	 */
	public DepositConfVo getDepositConf(int id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_deposit_conf where id = ?" ;
		DepositConfVo conf = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				conf = new DepositConfVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return conf; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositConfDao#getDepositConfWithCityId(int)
	 */
	public DepositConfVo getDepositConfWithCityId(int cityId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_deposit_conf where city_id = ?" ;
		DepositConfVo conf = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, cityId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				conf = new DepositConfVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return conf; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositConfDao#updateDepositConf(com.pgt.bikelock.vo.DepositConfVo)
	 */
	public boolean updateDepositConf(DepositConfVo confVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_deposit_conf set amount  = ?,return_min_day  = ?,return_max_day  = ?,date = now(),automatic_refund = ?"
				+ " where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, confVo.getAmount());
			pstmt.setInt(2, confVo.getReturn_min_day());
			pstmt.setInt(3, confVo.getReturn_max_day());
			pstmt.setInt(4, confVo.getAutomatic_refund());
			pstmt.setInt(5, confVo.getId());
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositConfDao#addDepositConf(com.pgt.bikelock.vo.DepositConfVo)
	 */
	public boolean addDepositConf(DepositConfVo confVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_deposit_conf (amount,return_min_day,return_max_day,city_id,date,automatic_refund) values"
				+ " (?,?,?,?,now(),?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, confVo.getAmount());
			pstmt.setInt(2, confVo.getReturn_min_day());
			pstmt.setInt(3, confVo.getReturn_max_day());
			pstmt.setInt(4, confVo.getCity_id());
			pstmt.setInt(5, confVo.getAutomatic_refund());
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag;  
	}

}
