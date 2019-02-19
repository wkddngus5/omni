/**
 * FileName:     TradeReciptDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午6:07:13
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.ITradeRecipt;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.TradeReceiptVo;

/**
 * @ClassName:     TradeReciptDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月10日 下午6:07:13
 *
 */
public class TradeReciptDaoImpl implements ITradeRecipt{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#addTradeReceipt(com.pgt.bikelock.vo.TradeReceiptVo)
	 */
	public boolean addTradeReceipt(TradeReceiptVo receipt) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_trade_receipt (uid,trade_ids,firstname,lastname,phone,address,zip_code,country) "
				+ "VALUES(?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,receipt.getUid());
			pstmt.setString(2,receipt.getTrade_ids());
			pstmt.setString(3, receipt.getFirstname());
			pstmt.setString(4, receipt.getLastname());
			pstmt.setString(5,receipt.getPhone());
			pstmt.setString(6,receipt.getAddress());
			pstmt.setString(7, receipt.getZip_code());
			pstmt.setString(8, receipt.getCountry());

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
	 * @see com.pgt.bikelock.dao.ITradeRecipt#getTradeReciptList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<TradeReceiptVo> getTradeReciptList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT r.* from t_trade_receipt r,t_user u" +
				" where r.uid = u.id and phone like '%"+requestVo.getKeyWords()
				+"%' or country like '%"+requestVo.getKeyWords()
				+"%' or firstname like '%"+requestVo.getKeyWords()
				+"%' or lastname like '%"+requestVo.getKeyWords()
				+"%' or address like '%"+requestVo.getKeyWords();
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		sql += "%' LIMIT ?,?" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TradeReceiptVo>  list = new ArrayList<TradeReceiptVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				TradeReceiptVo tradeVo = new TradeReceiptVo(rs);
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
	 * @see com.pgt.bikelock.dao.ITradeRecipt#updateReciptStatus(java.lang.String)
	 */
	public boolean updateReciptStatus(String id,int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_trade_receipt set status  = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,status);
			pstmt.setString(2, id);

			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeRecipt#deleteRecipt(java.lang.String)
	 */
	public boolean deleteRecipt(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_trade_receipt  where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
}
