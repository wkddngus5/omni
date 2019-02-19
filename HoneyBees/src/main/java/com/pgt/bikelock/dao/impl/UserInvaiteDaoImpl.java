/**
 * FileName:     UserInvaiteDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 下午3:41:39
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月5日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IUserInvaiteDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.InvaiteVo;
import com.pgt.bikelock.vo.RequestListVo;


 /**
 * @ClassName:     UserInvaiteDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月5日 下午3:41:39
 *
 */
public class UserInvaiteDaoImpl implements IUserInvaiteDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserInvaiteDao#shipExist(java.lang.String, java.lang.String)
	 */
	public boolean shipExist(String userId, String invaiteUserId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM t_user_invaite WHERE uid = ? and i_uid = ?" ;
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, invaiteUserId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				flag = rs.getInt("num") > 0?true:false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserInvaiteDao#addInvaiteShip(java.lang.String, java.lang.String)
	 */
	public boolean addShip(String userId, String invaiteUserId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_user_invaite (uid,i_uid,date) values (?,?,now())";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,userId);
			pstmt.setString(2,invaiteUserId);
			
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
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserInvaiteDao#getInvaiteList(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public List<InvaiteVo> getInvaiteList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_invaite"
				+ " where (phone like '%"+requestVo.getKeyWords()+"%'"
				+ " or i_phone like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getStartTime() != null){
			sql += " and date > '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date < '"+requestVo.getEndTime()+"'";
		}
		
		sql += " order by "+requestVo.getOrderField()+" "+requestVo.getOrderDirection()+" LIMIT ?,?";

		List<InvaiteVo> inviteList = new ArrayList<InvaiteVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				inviteList.add(new InvaiteVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return inviteList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserInvaiteDao#getInvaiteCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public int getInvaiteCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  view_user_invaite"
				+ " where (phone like '%"+requestVo.getKeyWords()+"%'"
				+ " or i_phone like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getStartTime() != null){
			sql += " and date > '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date < '"+requestVo.getEndTime()+"'";
		}

		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count; 
	}

}
