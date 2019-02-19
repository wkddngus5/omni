/**
 * FileName:     FunctionDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月18日 下午5:34:46
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月18日       CQCN         1.0             1.0
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

import com.omni.purview.dao.IFunctionDao;
import com.omni.purview.util.DataSourceUtil;
import com.omni.purview.vo.FunctionVo;

 /**
 * @ClassName:     FunctionDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月18日 下午5:34:46
 *
 */
public class FunctionDaoImpl implements IFunctionDao{

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IFunctionDao#addFuncation(com.omni.purview.vo.FunctionVo)
	 */
	public String addFuncation(FunctionVo function) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_admin_function (parent_id,name,date) values (?,?,now()) ";
		 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String funcId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, function.getParent_id());
			pstmt.setString(2,function.getName());
			 
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					funcId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return funcId;  
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IFunctionDao#getFunctionList()
	 */
	
	public List<FunctionVo> getFunctionList(ResourceBundle rb,List<String> existFunc) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_admin_function where parent_id = 0 order by findex asc" ;
		List<FunctionVo>  list= new ArrayList<FunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				FunctionVo function = new FunctionVo(rs,existFunc,rb);

				function.setSubList(getSubFunctionList(function.getId(), conn,true,existFunc,rb));
	
				list.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IFunctionDao#getFunctionList()
	 */
	
	public List<FunctionVo> getFunctionList(boolean thirdFun,List<String> existFunc,ResourceBundle rb) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_admin_function where parent_id = 0 and findex > 0 order by findex asc" ;
		List<FunctionVo>  list= new ArrayList<FunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				FunctionVo function = new FunctionVo(rs,existFunc,rb);

				function.setSubList(getSubFunctionList(function.getId(), conn,thirdFun,existFunc,rb));
	
				list.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IFunctionDao#getFunctionList(boolean, java.util.List, java.util.ResourceBundle, java.lang.String)
	 */
	
	public List<FunctionVo> getFunctionList(boolean thirdFun,
			List<String> existFunc, ResourceBundle rb, String currentGroup) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_admin_group_function where group_id in ("+currentGroup+
				") and parent_id = 0 and findex > 0 GROUP BY function_id order by findex asc" ;
		List<FunctionVo>  list= new ArrayList<FunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				FunctionVo function = new FunctionVo(rs,existFunc,rb);

				function.setSubList(getSubFunctionList(function.getId(), conn,thirdFun,existFunc,rb));
	
				list.add(function);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IFunctionDao#getSubFunctionList(int)
	 */
	
	public List<FunctionVo> getSubFunctionList(int parentId,Connection conn,boolean thirdFun,List<String> existFunc,ResourceBundle rb) {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM  t_admin_function where parent_id = ? order by findex asc" ;
		List<FunctionVo>  list= new ArrayList<FunctionVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, parentId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				FunctionVo function = new FunctionVo(rs,existFunc,rb);
				if(thirdFun){
					//3级菜单
					function.setSubList(getSubFunctionList(function.getId(), conn,thirdFun,existFunc,rb));
				}
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
	 * @see com.omni.purview.dao.IFunctionDao#getSubFunctionList(int)
	 */
	
	public List<FunctionVo> getSubFunctionList(int parentId,ResourceBundle rb) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_admin_function where parent_id = ? order by findex asc" ;
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
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IFunctionDao#checkParentFunction(java.lang.String)
	 */
	
	public List<String>  checkParentFunction(String funcIds,List<String> funcList) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT id from t_admin_function where id in (SELECT parent_id from t_admin_function "
				+ "where id in ("+funcIds+")) and findex > 0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
		
			rs = pstmt.executeQuery();
			while(rs.next()){
				String id = rs.getString("id");
				if(!funcList.contains(id)){
					funcList.add(id);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return funcList;
	}

}
