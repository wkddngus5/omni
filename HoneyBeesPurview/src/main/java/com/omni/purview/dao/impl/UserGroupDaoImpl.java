/**
 * FileName:     UserGroupDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月2日 下午6:40:28
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月2日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.omni.purview.dao.IUserGroupDao;
import com.omni.purview.util.DataSourceUtil;
import com.omni.purview.vo.UserGroupVo;

 /**
 * @ClassName:     UserGroupDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月2日 下午6:40:28
 *
 */
public class UserGroupDaoImpl implements IUserGroupDao {

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IUserGroupDao#getUserGroupList()
	 */
	
	public List<UserGroupVo> getUserGroupList() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_admin_group_mapping" ;
		List<UserGroupVo>  list= new ArrayList<UserGroupVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				UserGroupVo group = new UserGroupVo(rs);
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
	 * @see com.omni.purview.dao.IUserGroupDao#getUserGroupIdList(int)
	 */
	
	public List<String> getUserGroupIdList(int userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT group_id FROM  t_admin_group_mapping where user_id = ?" ;
		List<String>  list= new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("group_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IUserGroupDao#addUserGroup(int, java.lang.String[])
	 */
	
	public String addUserGroup(int userId, String[] groupIds) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String gIds = null;
		String sql="insert t_admin_group_mapping (group_id,user_id,date) values ";
		
		for (int i = 0; i < groupIds.length; i++) {
			if(i!=0){
				sql = sql +",";
			}
			sql = sql +" (?,?,now()) ";
		}

		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			
			for (int i = 0; i < groupIds.length; i++) {
				int index = i*2;
				pstmt.setString(1+index, groupIds[i]);
				pstmt.setInt(2+index,userId);
			}
		
			 
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					gIds = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return gIds;  
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IUserGroupDao#deleteUserGroup(int)
	 */
	
	public boolean deleteUserGroup(int userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_admin_group_mapping where user_id = ?" ;               
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,userId);
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
