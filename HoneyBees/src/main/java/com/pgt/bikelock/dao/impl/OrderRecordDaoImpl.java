package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IOrderRecord;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.admin.OrderRecord;

 

public class OrderRecordDaoImpl implements IOrderRecord{

	public boolean insert(long imei, int orderId, long time, String content) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO "+TABLE_NAME+"("
		               +COLUMN_ORDER_ID+"," 
		               +COLUMN_IMEI+"," 
		               +COLUMN_TIME+"," 
		               +COLUMN_CONTENT+") VALUES(?,?,?,?)";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,orderId);
			pstmt.setLong(2,imei);
			pstmt.setLong(3,time);
			pstmt.setString(4,content);
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

	public OrderRecord findLastByImei(long imei,int orderId) {
		Connection conn = DataSourceUtil.getConnection();
		OrderRecord  orderRecord =null;
		String sql="SELECT * FROM "+TABLE_NAME+" WHERE imei=? and order_id=? ORDER BY id DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, imei);
			pstmt.setInt(2, orderId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				orderRecord=new OrderRecord();
				orderRecord.setContent(rs.getString("content"));
				orderRecord.setId(rs.getInt("id"));
				orderRecord.setImei(rs.getLong("imei"));
				orderRecord.setOrderId(rs.getInt("order_id"));
				orderRecord.setTime(rs.getLong("time"));
				 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return orderRecord;
	}

}
