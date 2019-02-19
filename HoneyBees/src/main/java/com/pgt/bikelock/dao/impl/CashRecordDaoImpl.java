/**
 * FileName:     CaseRecordDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月11日 上午10:03:16
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月11日       CQCN         1.0             1.0
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

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.ICashRecordDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     CaseRecordDaoImpl
 * @Description:提现业务接口实现类/Prove the business interface implementation class
 * @author:    Albert
 * @date:        2017年5月11日 上午10:03:16
 *
 */
public class CashRecordDaoImpl implements ICashRecordDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICashRecordDao#addCash(com.pgt.bikelock.vo.CashRecordVo)
	 */
	public String addCash(CashRecordVo cashVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_cash_record (uid,amount,amount_type,type,date) values (?,?,?,?,now()) ";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String cashId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, cashVo.getUid());
			pstmt.setBigDecimal(2, cashVo.getAmount());
			pstmt.setInt(3, cashVo.getAmount_type());
			pstmt.setInt(4,cashVo.getType());
			
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					cashId = rs.getString(1);
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cashId;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICashRecordDao#addAccountCash(com.pgt.bikelock.vo.CashRecordVo)
	 */
	public String addAccountCash(CashRecordVo cashVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_cash_record (uid,amount,amount_type,type,order_id,status,date,refund_amount,refund_date)"
				+ " values (?,?,2,?,?,1,now(),?,?) ";
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String recordId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS); 
			pstmt.setString(1, cashVo.getUid());
			pstmt.setBigDecimal(2, cashVo.getAmount());
			pstmt.setInt(3,cashVo.getType());
			pstmt.setString(4, cashVo.getOrder_id());
			pstmt.setBigDecimal(5, cashVo.getRefund_amount());
			pstmt.setString(6, cashVo.getRefund_date());
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
	 * @see com.pgt.bikelock.dao.ICashRecordDao#updateCashOrderId(java.lang.String, java.lang.String)
	 */
	public boolean updateCashOrderId(String orderId, String cashId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_cash_record set order_id = ? where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,orderId);
			pstmt.setString(2, cashId);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICashRecordDao#updateCashStatus(int, java.lang.String)
	 */
	public boolean updateCashStatus(int status,String recordId,BigDecimal refundAmount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_cash_record set status = ?,refund_amount = ?,refund_date = now() where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, status);
			pstmt.setBigDecimal(2, refundAmount);
			pstmt.setString(3, recordId);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICashRecordDao#getCashList(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public List<CashRecordVo> getCashList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT *  FROM view_cash_record where 1=1";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and phone like '%"+requestVo.getKeyWords()+"%'";
		}
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		if(requestVo.getType() > 0){
			sql += " and amount_type ="+requestVo.getType();
		}
		if(requestVo.getStatus() > 0){
			sql += " and status ="+requestVo.getType();
		}
		if(requestVo.getStartTime() != null){
			sql += " and date > '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date < '"+requestVo.getEndTime()+"'";
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?" ;
		PreparedStatement pstmt = null;
		List<CashRecordVo>  cashList = new ArrayList<CashRecordVo>();
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs= pstmt.executeQuery();
			while (rs.next()) {
				cashList.add(new CashRecordVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cashList;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICashRecordDao#getCashCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public int getCashCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT COUNT(*) AS total  FROM view_cash_record where 1=1";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and phone like '%"+requestVo.getKeyWords()+"%'";
		}
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		if(requestVo.getType() > 0){
			sql += " and amount_type ="+requestVo.getType();
		}
		if(requestVo.getStatus() > 0){
			sql += " and status ="+requestVo.getType();
		}
		if(requestVo.getStartTime() != null){
			sql += " and date > '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date < '"+requestVo.getEndTime()+"'";
		}

		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs= pstmt.executeQuery();
			if(rs.next()) count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICashRecordDao#getCashDetail(java.lang.String)
	 */
	@Override
	public CashRecordVo getCashDetail(String cashId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT *  FROM view_cash_record where id=?";
		
		PreparedStatement pstmt = null;
		CashRecordVo  cashVo = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, cashId);
			rs= pstmt.executeQuery();
			while (rs.next()) {
				cashVo = new CashRecordVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cashVo;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICashRecordDao#getUserBalanceCash(java.lang.String)
	 */
	@Override
	public CashRecordVo getUserBalanceCash(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT *  FROM t_cash_record where uid=? order by id desc limit 0,1";
		
		PreparedStatement pstmt = null;
		CashRecordVo  cashVo = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs= pstmt.executeQuery();
			while (rs.next()) {
				cashVo = new CashRecordVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cashVo;
	}

}
