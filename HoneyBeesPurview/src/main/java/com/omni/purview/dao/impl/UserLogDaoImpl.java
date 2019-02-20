/**
 * FileName:     UserLogDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月14日 上午10:33:04
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月14日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.omni.purview.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.omni.purview.dao.IUserLogDao;
import com.omni.purview.util.DataSourceUtil;
import com.omni.purview.vo.UserLogVo;

 /**
 * @ClassName:     UserLogDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年6月14日 上午10:33:04
 *
 */
public class UserLogDaoImpl implements IUserLogDao {

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IUserLogDao#addUserLog(com.omni.purview.vo.UserLogVo, java.sql.Connection)
	 */
	
	public void addUserLog(UserLogVo logVo, Connection conn) {
		// TODO Auto-generated method stub
		String sql="insert t_admin_log (admin_id,func_id,data_id,date) values (?,?,?,now()) ";
		 
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, logVo.getAdmin_id());
			pstmt.setInt(2,logVo.getFunc_id());
			pstmt.setString(3,logVo.getData_id());

			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.omni.purview.dao.IUserLogDao#addUserLog(com.omni.purview.vo.UserLogVo)
	 */
	
	public void addUserLog(UserLogVo logVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_admin_log (admin_id,func_id,data_id,date) values (?,?,?,now()) ";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, logVo.getAdmin_id());
			pstmt.setInt(2,logVo.getFunc_id());
			pstmt.setString(3,logVo.getData_id());

			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
	}

}
