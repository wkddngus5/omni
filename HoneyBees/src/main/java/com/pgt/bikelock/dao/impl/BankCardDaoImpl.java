/**
 * FileName:     BankCardDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 上午9:32:04
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月5日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IBankCardDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.BankCardVo;
import com.pgt.bikelock.vo.BikeVo;

 /**
 * @ClassName:     BankCardDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月5日 上午9:32:04
 *
 */
public class BankCardDaoImpl implements IBankCardDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBankCardDao#addCard(com.pgt.bikelock.vo.BankCardVo)
	 */
	public boolean addCard(BankCardVo card) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_user_card (uid,card_number,exp_date,cvv,name_on_card) values (?,?,?,?,?)";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,card.getUid());
			pstmt.setString(2,card.getCard_number());
			pstmt.setString(3, card.getExp_date());
			pstmt.setString(4, card.getCvv());
			pstmt.setString(5,card.getName_on_card());
			
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

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBankCardDao#getCardList(java.lang.String)
	 */
	public List<BankCardVo> getCardList(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_user_card where uid = ?" ;
		List<BankCardVo>  list= new ArrayList<BankCardVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BankCardVo card = new BankCardVo(rs);
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
	 * @see com.pgt.bikelock.dao.IBankCardDao#getCardInfo(java.lang.String)
	 */
	public BankCardVo getCardInfo(String cardNumber,String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_user_card where card_number = ? and uid = ?" ;
		BankCardVo  cardVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, cardNumber);
			pstmt.setString(2, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				cardVo = new BankCardVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return cardVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBankCardDao#deleteCard(java.lang.String)
	 */
	public boolean deleteCard(String cardId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_user_card where id =  ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,cardId);
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
