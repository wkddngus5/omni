/**
 * FileName:     DepositDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月15日 下午3:23:23
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月15日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IDepositDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.DepositReturnVo;
import com.pgt.bikelock.vo.RequestListVo;

/**
 * @ClassName:     DepositDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月15日 下午3:23:23
 *
 */
public class DepositDaoImpl implements IDepositDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositDao#returnDeposit(java.lang.String)
	 */
	public String returnDeposit(String userId,String tradeId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_deposit_return (uid,trade_id,date) VALUES(?,?,now())";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String returnId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userId);
			pstmt.setString(2, tradeId);
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					returnId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return returnId;  
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositDao#getReturnList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<DepositReturnVo> getReturnList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_deposit_return"
				+ " where (phone like '%"+ requestVo.getKeyWords()+"%'"
				+" or id like '%"+ requestVo.getKeyWords()+"%'"
				+" or out_trade_no like '%"+ requestVo.getKeyWords()+"%')";
		if(requestVo.getStatus() > 0){
			sql += " and status ="+(requestVo.getStatus()-1);
		}
		if(requestVo.getStartTime() != null){
			sql += " and date >= '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date <= '"+requestVo.getEndTime()+"'";
		}
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?" ;
		List<DepositReturnVo>  list = new ArrayList<DepositReturnVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				DepositReturnVo tradeVo = new DepositReturnVo(rs);
				list.add(tradeVo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  view_deposit_return"
				+ " where (phone like '%"+ requestVo.getKeyWords()+"%'"
				+" or id like '%"+ requestVo.getKeyWords()+"%'"
				+" or out_trade_no like '%"+ requestVo.getKeyWords()+"%')";
		if(requestVo.getStatus() > 0){
			sql += " and status ="+(requestVo.getStatus()-1);
		}
		if(requestVo.getStartTime() != null){
			sql += " and date >= '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date <= '"+requestVo.getEndTime()+"'";
		}

		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}

		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 

			rs = pstmt.executeQuery();
			if(rs.next()){
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
	 * @see com.pgt.bikelock.dao.IDepositDao#updateDepositReturn(java.lang.String, java.lang.String, int)
	 */
	public boolean updateDepositReturn(String recordId,int status,String out_refund_no) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_deposit_return set status  = ?,out_refund_no = ? where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setString(2, out_refund_no);
			pstmt.setString(3, recordId);
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
	 * @see com.pgt.bikelock.dao.IDepositDao#refundDeposit(java.lang.String)
	 */
	public int refundDeposit(String recordIds) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_deposit_return set status  = ? where id in ("+recordIds+")";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, 1);
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IDepositDao#getDepositReturnInfo(java.lang.String)
	 */
	public DepositReturnVo getDepositReturnInfo(String recordId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_deposit_return  where id = ?";
		DepositReturnVo returnVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, recordId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				returnVo = new DepositReturnVo();
				returnVo.setUid(rs.getString("uid"));
				returnVo.setDate(rs.getString("date"));
				returnVo.setTrade_id(rs.getString("trade_id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return returnVo; 
	}
}
