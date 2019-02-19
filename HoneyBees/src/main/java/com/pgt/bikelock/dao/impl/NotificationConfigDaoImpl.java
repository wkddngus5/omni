/**
 * FileName:     NotificationConfigDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月25日 下午5:13:25
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月25日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.INotificationConfigDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.admin.NotificationConfigVo;

 /**
 * @ClassName:     NotificationConfigDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月25日 下午5:13:25
 *
 */
public class NotificationConfigDaoImpl implements INotificationConfigDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationConfigDao#getConfigList()
	 */
	@Override
	public List<NotificationConfigVo> getConfigList() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_notification_config" ;
		List<NotificationConfigVo>  list= new ArrayList<NotificationConfigVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				NotificationConfigVo configVo = new NotificationConfigVo(rs);
				list.add(configVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationConfigDao#getConfigInfo(java.lang.String)
	 */
	@Override
	public NotificationConfigVo getConfigInfo(int id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_notification_config where id = ?" ;
		NotificationConfigVo configVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				configVo = new NotificationConfigVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return configVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationConfigDao#getConfigInfoByType(int)
	 */
	@Override
	public NotificationConfigVo getConfigInfoByType(int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_notification_config where type = ?" ;
		NotificationConfigVo configVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, type);
			rs = pstmt.executeQuery();
			while(rs.next()){
				configVo = new NotificationConfigVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return configVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationConfigDao#updateConfig(com.pgt.bikelock.vo.admin.NotificationConfigVo)
	 */
	@Override
	public boolean updateConfig(NotificationConfigVo configVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "update t_notification_config set template = ?,nf_sms = ?,nf_email = ?,other_config = ?,type = ?,date = now() where id = ?";
		PreparedStatement pst = null;
		boolean flag = false;
		try {
			pst = conn.prepareStatement(sql);
	
			pst.setString(1, configVo.getTemplate());
			pst.setInt(2, configVo.getSms());
			pst.setInt(3, configVo.getEmail());
			pst.setString(4, configVo.getOther_config());
			pst.setInt(5, configVo.getType());
			pst.setInt(6,configVo.getId());
			
			if(pst.executeUpdate() > 0){
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
	 * @see com.pgt.bikelock.dao.INotificationConfigDao#addConfig(com.pgt.bikelock.vo.admin.NotificationConfigVo)
	 */
	@Override
	public String addConfig(NotificationConfigVo configVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "insert into t_notification_config (template,nf_sms,nf_email,other_config,type,date) values (?,?,?,?,?,now())";
		PreparedStatement pst = null;
		ResultSet rs = null;
		String tempId = "";
		try {
			pst = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, configVo.getTemplate());
			pst.setInt(2, configVo.getSms());
			pst.setInt(3, configVo.getEmail());
			pst.setString(4, configVo.getOther_config());
			pst.setInt(5, configVo.getType());
			if(pst.executeUpdate()>0){
				rs= pst.getGeneratedKeys();
				if(rs.next()){
					tempId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pst, conn);
		}
		return tempId;
	}
}
