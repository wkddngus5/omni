/**
 * FileName:     GroupPurviewDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月2日 下午1:51:02
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
import java.util.ResourceBundle;

import com.omni.purview.dao.IGroupFunctionDao;
import com.omni.purview.util.DataSourceUtil;
import com.omni.purview.vo.FunctionVo;
import com.omni.purview.vo.GroupFunctionVo;
import com.omni.purview.vo.UserLogVo;

 /**
 * @ClassName:     GroupPurviewDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月2日 下午1:51:02
 *
 */
public class GroupFunctionDaoImpl implements IGroupFunctionDao {

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupPurviewDao#getGroupPurviewList(int)
	 */
	
	public List<GroupFunctionVo> getGroupFunctionList(int groupId,ResourceBundle rb) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_admin_group_function where group_id = ?" ;
		List<GroupFunctionVo>  list= new ArrayList<GroupFunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, groupId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				GroupFunctionVo group = new GroupFunctionVo(rs,rb);
				group.getFunctionVo().setSubList(new FunctionDaoImpl().getSubFunctionList(group.getFunctionVo().getId(), conn, true,null,rb));
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
	 * @see com.omni.purview.dao.IGroupFunctionDao#getGroupsFunctionList(java.lang.String)
	 */
	
	public List<FunctionVo> getGroupsFunctionList(String groupIds,ResourceBundle rb) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_admin_group_function where group_id in ("+groupIds+") and parent_id = 0 and findex > 0 group by id order by findex asc" ;
		List<FunctionVo>  list= new ArrayList<FunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				GroupFunctionVo group = new GroupFunctionVo(rs,rb);
				group.getFunctionVo().setSubList(getSubFunctionList(group.getFunctionVo().getId(),groupIds, conn,rb));
				list.add(group.getFunctionVo());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}


	private List<FunctionVo> getSubFunctionList(int parentId,String groupIds,Connection conn,ResourceBundle rb) {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM  view_admin_group_function where group_id in ("+groupIds+") and parent_id = ? group by id order by id asc " ;
		List<FunctionVo>  list= new ArrayList<FunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, parentId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				FunctionVo function = new FunctionVo(rs,null,rb);
				list.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs, pstmt, null);
		}
		return list;
	}

	
	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupFunctionDao#getThirdGroupsFunctionList(int, java.lang.String)
	 */
	
	public List<FunctionVo> getThirdGroupsFunctionList(int parentId,
			String groupIds,ResourceBundle rb) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_admin_group_function where group_id in ("+groupIds+") and parent_id = ?" ;
		List<FunctionVo>  list= new ArrayList<FunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, parentId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				GroupFunctionVo group = new GroupFunctionVo(rs,rb);
				list.add(group.getFunctionVo());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}



	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupFunctionDao#getGroupsFunctionList(java.util.List)
	 */
	
	public boolean checkGroupsFunction(String groupIds,int actionId,String dataId,String adminId){
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  view_admin_group_function where group_id in ("+groupIds+") and id = ?" ;
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, actionId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				flag = rs.getInt("num") > 0?true:false;
			}
			if(flag && dataId != null){
				//增加日志
				new UserLogDaoImpl().addUserLog( new UserLogVo(adminId, actionId, dataId), conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}	
	
	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupFunctionDao#getGroupFunctionIdList(int)
	 */
	
	public List<String> getGroupFunctionIdList(int groupId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT function_id FROM  view_admin_group_function where group_id = ?" ;
		List<String>  list= new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, groupId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				list.add(rs.getString("function_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupFunctionDao#addGroupFunction(int, java.lang.String[])
	 */
	
	public String addGroupFunction(int groupId,List<String> funcIds) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String ids = null;
		String sql="insert t_admin_group_function (group_id,function_id,date) values ";
		
		for (int i = 0; i < funcIds.size(); i++) {
			if(i!=0){
				sql = sql +",";
			}
			sql = sql +" (?,?,now()) ";
		}
		
		
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			
			for (int i = 0; i < funcIds.size(); i++) {
				int index = i*2;
				pstmt.setInt(1+index, groupId);
				pstmt.setString(2+index,funcIds.get(i));
			}
		
			 
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					ids = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return ids;  
	}


	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IGroupFunctionDao#deleteGroupFunction(int)
	 */
	
	public boolean deleteGroupFunction(int groupId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_admin_group_function where group_id = ?" ;               
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,groupId);
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
