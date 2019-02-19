/**
 * FileName:     AgremmentDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午7:35:13
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IWebContentDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.WebContentVo;

 /**
 * @ClassName:     AgremmentDaoImpl
 * @Description:协议接口实现类/protocol access achieve
 * @author:    Albert
 * @date:        2017年4月10日 下午7:35:13
 *
 */
public class WebContentDaoImpl implements IWebContentDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAgreementDao#getAgremmentList(java.lang.String)
	 */
	public List<WebContentVo> getContentList(String industryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_web_content where industry_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<WebContentVo>  list = new ArrayList<WebContentVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, industryId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				WebContentVo agreementVo = new WebContentVo(rs);
				list.add(agreementVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAgreementDao#getAgremmentCount(java.lang.String)
	 */
	public int getContentCount(String industryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from t_web_content where industry_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0 ;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, industryId);
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
	 * @see com.pgt.bikelock.dao.IAgreementDao#getAgreement(java.lang.String)
	 */
	public WebContentVo getContent(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_web_content where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WebContentVo  agreementVo = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				agreementVo = new WebContentVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return agreementVo;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAgreementDao#getAgreement(java.lang.String, int)
	 */
	public WebContentVo getContent(String industryId, int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_web_content where industry_id = ? and type = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		WebContentVo  agreementVo = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, industryId);
			pstmt.setInt(2, type);
			rs = pstmt.executeQuery();
			while(rs.next()){
				agreementVo = new WebContentVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return agreementVo;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAgreementDao#updateAgreement(java.lang.String)
	 */
	public boolean updateContent(String id,String content) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_web_content set content = ?,date = now() where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,content);
			pstmt.setString(2, id);
			
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
