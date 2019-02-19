/**
 * FileName:     RfidCardDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月16日 上午11:05:19
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.SystemConfigBo;
import com.pgt.bikelock.dao.IRfidCardDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.RfidCardVo;

 /**
 * @ClassName:     RfidCardDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2018年8月16日 上午11:05:19
 *
 */
public class RfidCardDaoImpl implements IRfidCardDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRfidCardDao#getCardList(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public List<RfidCardVo> getCardList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select * from view_rfid_card where 1=1 ";
		if(requestVo.getStatus()  > 0){
			sql += " and status = "+(requestVo.getStatus()-1);
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (id like '%"+requestVo.getKeyWords()+"%'"
					+ " or card_id like '%"+requestVo.getKeyWords()+"%'"
							+ " or card_no like '%"+requestVo.getKeyWords()+"%'"
									+ " or phone like '%"+requestVo.getKeyWords()+"%')";
		}
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >="+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <"+requestVo.getEndTime();
		}
		sql += " limit ?,?";
		List<RfidCardVo>  list= new ArrayList<RfidCardVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, requestVo.getStartPage());
			pstmt.setInt(2, requestVo.getPageSize());
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				RfidCardVo card = new RfidCardVo(rs);

				list.add(card);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRfidCardDao#getCardCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public int getCardCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select count(*) as num from view_rfid_card where 1=1 ";
		if(requestVo.getStatus()  > 0){
			sql += " and status = "+(requestVo.getStatus()-1);
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (id like '%"+requestVo.getKeyWords()+"%'"
					+ " or card_id like '%"+requestVo.getKeyWords()+"%'"
							+ " or card_no like '%"+requestVo.getKeyWords()+"%'"
									+ " or phone like '%"+requestVo.getKeyWords()+"%')";
		}
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >="+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <"+requestVo.getEndTime();
		}
		
		int count = 0;
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
	 * @see com.pgt.bikelock.dao.IRfidCardDao#getUserCardList(java.lang.String)
	 */
	@Override
	public List<RfidCardVo> getUserCardList(String uid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select * from view_rfid_card where uid = ? ";
		
		List<RfidCardVo>  list= new ArrayList<RfidCardVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				RfidCardVo card = new RfidCardVo(rs);

				list.add(card);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRfidCardDao#getCardInfo(java.lang.String)
	 */
	@Override
	public RfidCardVo getCardInfo(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select * from view_rfid_card where id = ? ";
		
		RfidCardVo cardVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,id);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				cardVo = new RfidCardVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cardVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRfidCardDao#getCardInfoWithCardNo(java.lang.String)
	 */
	@Override
	public RfidCardVo getCardInfoWithCardNo(String cardNo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select * from view_rfid_card where card_no = ? ";
		
		RfidCardVo cardVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,cardNo);
			
			rs = pstmt.executeQuery();
			if(rs.next()){
				cardVo = new RfidCardVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cardVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRfidCardDao#addCard(com.pgt.bikelock.vo.RfidCardVo)
	 */
	@Override
	public String addCard(RfidCardVo cardVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_rfid_card (id,card_id,card_no,uid,status,date) values (?,?,?,?,?,now()) ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			String id = ValueUtil.getUUIDString();
			pstmt.setString(1,id);
			pstmt.setString(2,cardVo.getCardId());
			pstmt.setString(3, cardVo.getCardNo());
			pstmt.setInt(4, cardVo.getuid());
			pstmt.setInt(5, cardVo.getStatus());

			if(pstmt.executeUpdate()>0){
				return id;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return null;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRfidCardDao#updateCard(com.pgt.bikelock.vo.RfidCardVo)
	 */
	@Override
	public boolean updateCard(RfidCardVo cardVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_rfid_card set card_id = ?,card_no = ?,uid = ?,status = ?,date = now() where id = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,cardVo.getCardId());
			pstmt.setString(2, cardVo.getCardNo());
			pstmt.setInt(3, cardVo.getuid());
			pstmt.setInt(4, cardVo.getStatus());
			pstmt.setString(5, cardVo.getId());

			if(pstmt.executeUpdate()>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRfidCardDao#bindCard(int, java.lang.String)
	 */
	@Override
	public boolean bindCard(int uid, String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_rfid_card set uid = ? where id = ?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, uid);
			pstmt.setString(2, id);

			if(pstmt.executeUpdate()>0){
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return false;
	}
}
