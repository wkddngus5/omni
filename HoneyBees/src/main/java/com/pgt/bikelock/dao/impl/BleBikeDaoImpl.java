/**
 * FileName:     BleBikeDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月16日 下午3:28:32
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IBleBikeDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.BleBikeVo;

 /**
 * @ClassName:     BleBikeDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月16日 下午3:28:32
 *
 */
public class BleBikeDaoImpl implements IBleBikeDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBleBikeDao#addBleBike(com.pgt.bikelock.vo.BleBikeVo)
	 */
	public boolean addBleBike(BleBikeVo bikeVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_bike_ble (bid,mac,date) values (?,?,now()) ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, bikeVo.getBid());
			pstmt.setString(2,bikeVo.getMac());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBleBikeDao#updateBleBike(com.pgt.bikelock.vo.BleBikeVo)
	 */
	public boolean updateBleBike(BleBikeVo bikeVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_ble set mac = ?,date = now() where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,bikeVo.getMac());
			pstmt.setString(2, bikeVo.getId());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBleBikeDao#getBleBike(java.lang.String)
	 */
	public BleBikeVo getBleBike(String number) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM view_bike_ble where number = ?" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BleBikeVo bleVo = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,number);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bleVo = new BleBikeVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bleVo; 
	}

}
