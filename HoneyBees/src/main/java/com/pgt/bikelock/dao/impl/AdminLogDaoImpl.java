/**
 * FileName:     AdminLogDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月15日 下午4:45:52
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月15日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pgt.bikelock.dao.IAdminLogDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.AdminLogVo;

/**
 * @ClassName:     AdminLogDaoImpl
 * @Description:管理员日志接口实现类/administrator Log interface implementation class
 * @author:    Albert
 * @date:        2017年4月15日 下午4:45:52
 *
 */
public class AdminLogDaoImpl implements IAdminLogDao{


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAdminLogDao#getLogList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<AdminLogVo> getLogList(RequestListVo request,String funcIds) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_admin_log_detail"
				+ " where (user_name like '%"+request.getKeyWords()+"%'"
				+ " or nickname like '%"+request.getKeyWords()+"%')";

		if(funcIds != null){
			sql += " and func_id in ("+funcIds+")";
		}
		
		if(request.getStartTime() != null){
			sql += " and date > '"+request.getStartTime()+"'";
		}

		if(request.getEndTime() != null){
			sql += " and date < '"+request.getEndTime()+"'";
		}
		
		if(request.getCityId() != 0){
			sql += " and city_id ="+request.getCityId();
		}
		
		sql += " limit ?,?";

		List<AdminLogVo>  list= new ArrayList<AdminLogVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ResourceBundle rb = LanguageUtil.getDefaultResource();
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,request.getStartPage());
			pstmt.setInt(2,request.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				AdminLogVo group = new AdminLogVo(rs,rb);
				list.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAdminLogDao#getCount(com.pgt.bikelock.vo.RequestListVo, java.lang.String)
	 */
	public int getCount(RequestListVo request, String funcIds) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  view_admin_log_detail"
				+ " where (user_name like '%"+request.getKeyWords()+"%'"
				+ " or nickname like '%"+request.getKeyWords()+"%')";

		if(funcIds != null){
			sql += " and func_id in ("+funcIds+")";
		}
		
		if(request.getStartTime() != null){
			sql += " and date > '"+request.getStartTime()+"'";
		}

		if(request.getEndTime() != null){
			sql += " and date < '"+request.getEndTime()+"'";
		}
		
		if(request.getCityId() != 0){
			sql += " and city_id ="+request.getCityId();
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


}
