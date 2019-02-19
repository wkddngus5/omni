/**
 * FileName:     CreditScoreDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月12日 下午4:57:44
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.ICreditScoreDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.CreditScoreVo;

 /**
 * @ClassName:     CreditScoreDaoImpl
 * @Description:信用积分接口实现类/credit protocol achieve
 * @author:    Albert
 * @date:        2017年5月12日 下午4:57:44
 *
 */
public class CreditScoreDaoImpl implements ICreditScoreDao{

	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICreditScoreDao#addUserCreditRecord(com.pgt.bikelock.vo.UserCreditScoreVo)
	 */
	public boolean addUserCreditRecord(CreditScoreVo scoreVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_user_credit_score (uid,rule_type,data_type,count,date) values (?,?,?,?,now()) ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, scoreVo.getUid());
			pstmt.setInt(2, scoreVo.getRule_type());
			pstmt.setInt(3, scoreVo.getData_type());
			pstmt.setInt(4, scoreVo.getCount());
			
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICreditScoreDao#getUserCreditList(java.lang.String)
	 */
	public List<CreditScoreVo> getUserCreditList(String userId,int startPage) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT s.*,u.phone from t_user_credit_score s ,t_user u where s.uid = u.id and uid = ? order by s.id desc  limit ?,?" ;
		List<CreditScoreVo>  list= new ArrayList<CreditScoreVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			pstmt.setInt(2, startPage);
			pstmt.setInt(3, BaseDao.pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CreditScoreVo scoreVo = new CreditScoreVo(rs);
				list.add(scoreVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

}
