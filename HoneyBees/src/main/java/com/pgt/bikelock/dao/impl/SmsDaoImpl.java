/**
 * FileName:     SmsDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月12日 上午11:48:12
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月12日       CQCN         1.0             1.0
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
import com.pgt.bikelock.dao.ISmsDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.MessageBoxVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.SmsVo;

/**
 * @ClassName:     SmsDaoImpl
 * @Description:短信业务接口实现/shorm message business protocol achieve
 * @author:    Albert
 * @date:        2017年4月12日 上午11:48:12
 *
 */
public class SmsDaoImpl implements ISmsDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsDao#addSms(com.pgt.bikelock.vo.SmsVo)
	 */
	public boolean addSms(SmsVo sms) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_sms (phone,code,content,type,date,area_code) VALUES(?,?,?,?,UNIX_TIMESTAMP(),?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,sms.getPhone());
			pstmt.setString(2, sms.getCode());
			pstmt.setString(3, sms.getContent());
			pstmt.setInt(4,sms.getType());
			pstmt.setString(5, sms.getAreaCode());
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
	 * @see com.pgt.bikelock.dao.ISmsDao#checkSmsCode(java.lang.String, java.lang.String)
	 */
	public boolean checkSmsCode(String code, String phone,int validMinutes) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		//通过更新受影响的行数判定数据是否存在/Determine whether the data exists by updating the number of rows affected
		String sql="update t_sms set used = 1 where code = ? and phone = ? and used = 0 and date >= UNIX_TIMESTAMP()-?" ;
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, code);
			pstmt.setString(2, phone);
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

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsDao#getPhoneCountInDay(java.lang.String)
	 */
	@Override
	public int getPhoneCountInDay(String phone) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		//通过更新受影响的行数判定数据是否存在/Determine whether the data exists by updating the number of rows affected
		String sql="SELECT count(*) as num  FROM t_sms where date_format(FROM_UNIXTIME(date),\"%Y-%m-%d\") = date_format(CURDATE(),\"%Y-%m-%d\") "
				+ " and phone = ?" ;
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, phone);
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
	 * @see com.pgt.bikelock.dao.ISmsDao#getSmsList(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public List<SmsVo> getSmsList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		List<SmsVo> smsList = new ArrayList<SmsVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_sms where 1=1";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (phone like '%"+requestVo.getKeyWords()+"%'"
					+ " or code like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%')";
		}
		if(requestVo.getStatus() > 0){
			sql += " and used="+(requestVo.getStatus()-1);
		}
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >= "+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <= "+requestVo.getEndTime();
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
				smsList.add(new SmsVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return smsList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsDao#getSmsCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public int getSmsCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		int count = 0;

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from t_sms where 1=1";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (phone like '%"+requestVo.getKeyWords()+"%'"
					+ " or code like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%')";
		}
		if(requestVo.getStatus() > 0){
			sql += " and used="+(requestVo.getStatus()-1);
		}
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >= "+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <= "+requestVo.getEndTime();
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
}
