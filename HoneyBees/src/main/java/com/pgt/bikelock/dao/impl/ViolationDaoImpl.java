/**
 * FileName:     ViolationDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月16日 下午6:41:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.IViolationDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.ViolationVo;

 /**
 * @ClassName:     ViolationDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月16日 下午6:41:21
 *
 */
public class ViolationDaoImpl implements IViolationDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IViolationDao#addViolation(com.pgt.bikelock.vo.ViolationVo)
	 */
	public boolean addViolation(ViolationVo violation) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_violation (uid,useid,type,note,date) VALUES(?,?,?,?,now())";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,violation.getUid());
			pstmt.setString(2, violation.getUseid());
			pstmt.setInt(3, violation.getType());
			pstmt.setString(4, violation.getNote());
			if(pstmt.executeUpdate()>0){
				flag =  true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag;  

	}

}
