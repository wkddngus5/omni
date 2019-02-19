/**
 * FileName:     EmailDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月29日 下午3:29:14
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月29日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IEmailDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.EmailVo;
import com.pgt.bikelock.vo.RequestListVo;


 /**
 * @ClassName:     EmailDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月29日 下午3:29:14
 *
 */
public class EmailDaoImpl implements IEmailDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IEmailDao#addEmail(com.pgt.bikelock.vo.EmailVo)
	 */
	@Override
	public boolean addEmail(EmailVo email) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_email (sender,receiver,subject,content,date,status,code) VALUES(?,?,?,?,UNIX_TIMESTAMP(),?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,email.getSender());
			pstmt.setString(2, email.getReceiver());
			pstmt.setString(3, email.getSubject());
			pstmt.setString(4,email.getContent());
			pstmt.setInt(5, email.getStatus());
			pstmt.setString(6, email.getCode());
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

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IEmailDao#getEmailList(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public List<EmailVo> getEmailList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		List<EmailVo> emailList = new ArrayList<EmailVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_email where 1=1";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (sender like '%"+requestVo.getKeyWords()+"%'"
					+ " or receiver like '%"+requestVo.getKeyWords()+"%'"
					+ " or subject like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%')";
		}
		
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >= "+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <= "+requestVo.getEndTime();
		}
		if(requestVo.getStatus() > 0){
			sql += " and status="+(requestVo.getStatus()-1);
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				emailList.add(new EmailVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return emailList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IEmailDao#getEmailCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public int getEmailCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		int count = 0;

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from t_email where 1=1";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (sender like '%"+requestVo.getKeyWords()+"%'"
					+ " or receiver like '%"+requestVo.getKeyWords()+"%'"
					+ " or subject like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%')";
		}
		
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >= "+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <= "+requestVo.getEndTime();
		}
		if(requestVo.getStatus() > 0){
			sql += " and status="+(requestVo.getStatus()-1);
		}
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

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IEmailDao#checkEmailCode(java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean checkEmailCode(String code, String email, int validMinutes) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		//通过更新受影响的行数判定数据是否存在/Determine whether the data exists by updating the number of rows affected
		String sql="update t_email set status = 2 where code = ? and receiver = ? and status = 1 and date >= UNIX_TIMESTAMP()-?" ;
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, code);
			pstmt.setString(2, email);
			pstmt.setInt(3, validMinutes*60);

			if(pstmt.executeUpdate() > 0){
				flag = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}
	
}
