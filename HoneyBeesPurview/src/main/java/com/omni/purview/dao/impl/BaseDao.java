package com.omni.purview.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.omni.purview.util.DataSourceUtil;

public class BaseDao {
	/**
	 * 获取所有的数据条数
	 * @param tableName 表名
	 * @param keyWords 关键字
	 * @param keyParms 匹配关键字段
	 * @return
	 */
	public static int getCount(String tableName,String keyWords,String[] keyParms) {
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT COUNT(*) AS total  FROM "+tableName;
		for (int i = 0; i < keyParms.length; i++) {
			if(i == 0){
				sql += " where "+keyParms[i]+" like '%"+keyWords+"%'";
			}else{
				sql += " or "+keyParms[i]+" like '%"+keyWords+"%'";
			}
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
	
	/***
	 * 删除记录
	 * @param tableName
	 * @param recordId
	 * @return
	 */
	public static boolean deleteRecord(String tableName,String recordId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from "+tableName+" where id = ?" ;               
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,recordId);
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
	
	/**
	 * 
	 * @Title:        isExistColumn 
	 * @Description:  判定列是否存在
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月2日 上午9:49:21
	 */
	public static boolean isExistColumn(ResultSet rs, String columnName) {  
	    try {  
	        if (rs.findColumn(columnName) > 0 ) {  
	            return true;  
	        }   
	    }  
	    catch (SQLException e) {  
	        return false;  
	    }  
	      
	    return false;  
	}
	
	/**
	 * 
	 * @Title:        getString 
	 * @Description:  字符串获取
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:46:00
	 */
	public static String getString(ResultSet rst,String columnName) throws SQLException{
		if(!isExistColumn(rst, columnName)){
			return "";
		}
		return rst.getString(columnName);
	}
	
	/**
	 * 
	 * @Title:        getDouble 
	 * @Description:  浮点型获取
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       Double    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:47:10
	 */
	public static Double getDouble(ResultSet rst,String columnName) throws SQLException{
		if(!isExistColumn(rst, columnName)){
			return 0.0;
		}
		return rst.getDouble(columnName);
	}
	
	/**
	 * 
	 * @Title:        getInt 
	 * @Description:  整型获取
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:47:56
	 */
	public static int getInt(ResultSet rst,String columnName) throws SQLException{
		if(!isExistColumn(rst, columnName)){
			return 0;
		}
		return rst.getInt(columnName);
	}
	
	/**
	 * 
	 * @Title:        getBigDecimal 
	 * @Description:  金额获取
	 * @param:        @param rst
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       BigDecimal    
	 * @author        Albert
	 * @Date          2017年5月16日 上午11:00:54
	 */
	public static BigDecimal getBigDecimal(ResultSet rst,String columnName) throws SQLException{
		if(!isExistColumn(rst, columnName)){
			return null;
		}
		return rst.getBigDecimal(columnName);
	}
}
