/**
 * FileName:     NotificationMappingDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月25日 下午5:14:10
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

import com.pgt.bikelock.dao.INotificationMappingDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.admin.NotificationConfigVo;
import com.pgt.bikelock.vo.admin.NotificationMappingVo;

 /**
 * @ClassName:     NotificationMappingDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月25日 下午5:14:10
 *
 */
public class NotificationMappingDaoImpl implements INotificationMappingDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationMappingDao#getMapping(java.lang.String)
	 */
	@Override
	public List<NotificationMappingVo> getMappingList(String groupId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_notification_mapping where group_id = ?" ;
		List<NotificationMappingVo> mappingList = new ArrayList<NotificationMappingVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				mappingList.add(new NotificationMappingVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return mappingList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationMappingDao#getMappingList(int)
	 */
	@Override
	public List<NotificationConfigVo> getMappingList(int notifyType) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_admin_notification where type = ?" ;
		List<NotificationConfigVo> mappingList = new ArrayList<NotificationConfigVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, notifyType);
			rs = pstmt.executeQuery();
			while(rs.next()){
				mappingList.add(new NotificationConfigVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return mappingList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationMappingDao#getMappingIdList(java.lang.String)
	 */
	@Override
	public List<String> getMappingTypeList(String groupId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT c.type FROM  t_notification_mapping m,t_notification_config c where m.notify_config_type = c.type and m.group_id = ?" ;
		List<String> mappingList = new ArrayList<String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				mappingList.add(rs.getString("type"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return mappingList; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INotificationMappingDao#addMapping(java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean addMapping(String groupId, String[] notifyIds) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "insert into t_notification_mapping (group_id,notify_config_type,date) values ";
		for (String string : notifyIds) {
			sql += "("+groupId+","+string+",now()),";
		}
		sql = sql.substring(0, sql.length()-1);
		PreparedStatement pst = null;
		boolean flag = false;
		try {
			pst = conn.prepareStatement(sql);
			
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
	 * @see com.pgt.bikelock.dao.INotificationMappingDao#deleteMapping(java.lang.String)
	 */
	@Override
	public boolean deleteMapping(String groupId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_notification_mapping where group_id = ?" ;               
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, groupId);
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
