package com.pgt.bikelock.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.pgt.bikelock.resource.OthersSource;

public class DataSourceUtil { 

	private static DataSource dataSource;
	static{
		try {
			Context context =  new InitialContext();
			dataSource=(DataSource) context.lookup("java:comp/env/jdbc/bikeLockDS");
		} catch (NamingException e) {
			 throw new ExceptionInInitializerError("数据库源初始化失败/the database souce failed initialization");
		}
	}
	
	public static Connection getConnection(){
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void close(ResultSet rs,PreparedStatement pstmt,Connection conn){
		if(rs!=null){
			try {
				if(!rs.isClosed()){
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		close(pstmt,conn);

	}

	public static void close(PreparedStatement pstmt,Connection conn){
		if(pstmt!=null){
			try {
				if(!pstmt.isClosed()){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(conn!=null){
			try {
				if(!conn.isClosed()){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
