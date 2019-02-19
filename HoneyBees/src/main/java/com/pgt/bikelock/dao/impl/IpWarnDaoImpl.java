/**
 * FileName:     IpWarnDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月8日 下午3:51:11
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月8日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IIpWarnDao;
import com.pgt.bikelock.utils.DataSourceUtil;

 /**
 * @ClassName:     IpWarnDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月8日 下午3:51:11
 *
 */
public class IpWarnDaoImpl implements IIpWarnDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IIpWarnDao#addWarn(java.lang.String)
	 */
	@Override
	public boolean addWarn(String ip,String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "insert into t_request_warn(ip,uid,date) values (?,?,now())";
		PreparedStatement pst = null;
	
		boolean flag = false;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, ip);
			pst.setString(2, userId);
			
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

}
