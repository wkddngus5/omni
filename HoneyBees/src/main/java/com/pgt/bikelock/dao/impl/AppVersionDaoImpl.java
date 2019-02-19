/**
 * FileName:     AppVersionDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月27日 下午3:47:25
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月27日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IAppVersionDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.AppVersionVo;
import com.pgt.bikelock.vo.RequestListVo;

/**
 * @ClassName:     AppVersionDaoImpl
 * @Description:App版本业务实现类/app version business implementation class
 * @author:    Albert
 * @date:        2017年5月27日 下午3:47:25
 *
 */
public class AppVersionDaoImpl implements IAppVersionDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAppVersionDao#getTopVersion(int)
	 */
	public AppVersionVo getTopVersion(int versionCode) {
		// TODO Auto-generated method stub
		String sql="SELECT * from t_app_version where version_code > ?";
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AppVersionVo  versionVo = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, versionCode);
			rs = pstmt.executeQuery();
			while(rs.next()){
				versionVo = new AppVersionVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return versionVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAppVersionDao#getVersionList(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public List<AppVersionVo> getVersionList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		String sql="SELECT * from t_app_version where (version_name like '%"+requestVo.getKeyWords()+"%'"
				+ " or version_code like '%"+requestVo.getKeyWords()+"%'"
				+ " or url like '%"+requestVo.getKeyWords()+"%'"
				+ "or content like '%"+requestVo.getKeyWords()+"%')";
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >"+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <="+requestVo.getEndTime();
		}
		if(requestVo.getType() > 0){
			sql += " and type = "+requestVo.getType();
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AppVersionVo>  versionList = new ArrayList<AppVersionVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				versionList.add(new AppVersionVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return versionList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAppVersionDao#getVersionCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public int getVersionCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		String sql="SELECT count(*) as num from t_app_version where (version_name like '%"+requestVo.getKeyWords()+"%'"
				+ " or version_code like '%"+requestVo.getKeyWords()+"%'"
				+ " or url like '%"+requestVo.getKeyWords()+"%'"
				+ "or content like '%"+requestVo.getKeyWords()+"%')";
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >"+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <="+requestVo.getEndTime();
		}
		if(requestVo.getType() > 0){
			sql += " and type = "+requestVo.getType();
		}
		
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
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
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAppVersionDao#addVersion(com.pgt.bikelock.vo.AppVersionVo)
	 */
	@Override
	public String addVersion(AppVersionVo versionVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "insert into t_app_version (version_name,version_code,type,url,content,date) values (?,?,?,?,?,now())";
		PreparedStatement pst = null;
		ResultSet rs = null;
		String versionId = null;
		try {
			pst = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
	
			pst.setString(1, versionVo.getVersion_name());
			pst.setInt(2, versionVo.getVersion_code());
			pst.setInt(3, versionVo.getType());
			pst.setString(4, versionVo.getUrl());
			pst.setString(5, versionVo.getContent());
			if(pst.executeUpdate()>0){
				rs= pst.getGeneratedKeys();
				if(rs.next()){
					versionId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pst, conn);
		}
		return versionId;
	}
}
