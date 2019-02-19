/**
 * FileName:     SystemConfigDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月30日 下午6:46:40
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.ISystemConfigDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.admin.SystemConfigVo;

 /**
 * @ClassName:     SystemConfigDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月30日 下午6:46:40
 *
 */
public class SystemConfigDaoImpl implements ISystemConfigDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISystemConfigDao#addConfig(com.pgt.bikelock.vo.admin.SystemConfigVo)
	 */
	@Override
	public boolean addConfig(SystemConfigVo configVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "insert into t_system_config (name,config,date,cf_key,note) values (?,?,now(),?,?)";
		PreparedStatement pst = null;
		boolean flag = false;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, configVo.getName());
			pst.setString(2, configVo.getConfig());
			pst.setString(3, configVo.getKey());
			pst.setString(4, configVo.getNote());
			
			if(pst.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pst, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISystemConfigDao#updateConfig(com.pgt.bikelock.vo.admin.SystemConfigVo)
	 */
	@Override
	public boolean updateConfig(SystemConfigVo configVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "update t_system_config set name = ?,config = ?,note = ?,cf_key = ?,date = now() where id= ?";
		PreparedStatement pst = null;
		boolean flag = false;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, configVo.getName());
			pst.setString(2, configVo.getConfig());
			pst.setString(3, configVo.getNote());
			pst.setString(4, configVo.getKey());
			pst.setInt(5, configVo.getId());
			
			if(pst.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pst, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISystemConfigDao#getConfig(java.lang.String)
	 */
	@Override
	public SystemConfigVo getConfig(int id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_system_config where id = ?" ;
		SystemConfigVo configVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				configVo = new SystemConfigVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return configVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISystemConfigDao#getConfig(java.lang.String)
	 */
	@Override
	public SystemConfigVo getConfig(String key) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_system_config where cf_key = ?" ;
		SystemConfigVo configVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, key);
			rs = pstmt.executeQuery();
			while(rs.next()){
				configVo = new SystemConfigVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return configVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISystemConfigDao#getConfigList()
	 */
	@Override
	public List<SystemConfigVo> getConfigList(boolean checkKey) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_system_config" ;
		if(checkKey){
			sql += " where cf_key != '' ";
		}
			
		List<SystemConfigVo> configVoList = new ArrayList<SystemConfigVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				configVoList.add(new SystemConfigVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return configVoList; 
	}
}
