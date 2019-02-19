/**
 * FileName:     CountryDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月14日 下午3:27:53
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月14日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.ICountryDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.CountryVo;

 /**
 * @ClassName:     CountryDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月14日 下午3:27:53
 *
 */
public class CountryDaoImpl implements ICountryDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICountryDao#getCountryList()
	 */
	public List<CountryVo> getCountryList() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_country where phone_code != ''  order by english_name" ;
		List<CountryVo>  list= new ArrayList<CountryVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 

			rs = pstmt.executeQuery();
			while(rs.next()){
				CountryVo country = new CountryVo(rs);
				list.add(country);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICountryDao#getCountry(java.lang.String)
	 */
	@Override
	public CountryVo getCountry(int phoneCode) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_country where phone_code = ? " ;
		CountryVo country = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, phoneCode);
			rs = pstmt.executeQuery();
			while(rs.next()){
				country = new CountryVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return country; 
	}
}
