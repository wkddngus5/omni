/**
 * FileName:     GroupDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月2日 下午1:05:46
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

import com.omni.purview.dao.IGroupDao;
import com.omni.purview.util.DataSourceUtil;
import com.omni.purview.vo.GroupVo;

 /**
 * @ClassName:     GroupDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月2日 下午1:05:46
 *
 */
public class GroupDaoImpl implements IGroupDao {

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupDao#getGroupList()
	 */
	
	public List<GroupVo> getGroupList(List<String> existId,int cityId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_admin_group" ;
		if(cityId != 0){
			sql += " where city_id = "+cityId;
		}
		List<GroupVo>  list= new ArrayList<GroupVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				GroupVo group = new GroupVo(rs,existId);
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
	 * @see com.omni.purview.dao.IGroupDao#addGroup(com.omni.purview.vo.GroupVo)
	 */
	
	public int addGroup(GroupVo groupVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_admin_group (name,note,date,city_id) values (?,?,now(),?) ";
		 
		int groupId = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, groupVo.getName());
			pstmt.setString(2,groupVo.getNote());
			pstmt.setInt(3, groupVo.getCityId()); 
			
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					groupId = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs, pstmt, conn);
		}
		return groupId;  
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupDao#updateGroup(com.omni.purview.vo.GroupVo)
	 */
	
	public boolean updateGroup(GroupVo groupVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_admin_group set name = ?,note = ?,city_id = ? where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, groupVo.getName());
			pstmt.setString(2,groupVo.getNote());
			pstmt.setInt(3, groupVo.getCityId()); 
			pstmt.setInt(4, groupVo.getId()); 
			
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupDao#deleteGroup(int)
	 */
	
	public boolean deleteGroup(int groupId) {
		// TODO Auto-generated method stub
		
		//删除关联
		
		return BaseDao.deleteRecord("t_admin_group", groupId+"");
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupDao#getGroupInfo(int)
	 */
	
	public GroupVo getGroupInfo(int groupId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_admin_group where id = ?" ;

		GroupVo group = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				group = new GroupVo(rs,null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return group; 
	}

	
}
