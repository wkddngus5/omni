/**
 * FileName:     IdustryDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 上午9:42:24
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IIndustryDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.UserVo;

 /**
 * @ClassName:     IdustryDaoImpl
 * @Description:产业相关接口实现/industry related protocol achieve
 * @author:    Albert
 * @date:        2017年4月6日 上午9:42:24
 *
 */
public class IndustryDaoImpl implements IIndustryDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IIndustryDao#getIndustryInfo(java.lang.String)
	 */
	public IndustryVo getIndustryInfo(String industryId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM t_industry  WHERE id = ?" ;
		IndustryVo industry = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, industryId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				industry = new IndustryVo(rs,2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return industry; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IIndustryDao#updateIndustryInfo(com.pgt.bikelock.vo.IndustryVo)
	 */
	public boolean updateIndustryInfo(IndustryVo industryVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_industry set deposit=? WHERE id= ? ";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setBigDecimal(1, industryVo.getDeposit());
			pstmt.setString(2, industryVo.getId());
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

}
