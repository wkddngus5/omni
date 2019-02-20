package com.omni.purview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.omni.purview.resource.OthersConfig;

public class DataSourceUtil {
	
	public static Connection getConnection(){
		try{    
			Class.forName("com.mysql.jdbc.Driver");//指定连接类型  
			Connection con =     
					DriverManager.getConnection(OthersConfig.getSourceString("database_url"),
							OthersConfig.getSourceString("database_username") , 
							OthersConfig.getSourceString("database_password")) ;

			return con;
		}catch(SQLException se){    
			System.out.println("数据库连接失败！");    
			se.printStackTrace() ;    
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
