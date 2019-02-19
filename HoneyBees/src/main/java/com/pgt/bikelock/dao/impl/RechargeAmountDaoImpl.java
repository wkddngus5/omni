/**
 * FileName:     RechargeDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 下午3:14:36
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.pgt.bikelock.dao.IRechargeAmountDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.RechargeAmountVo;

 /**
 * @ClassName:     RechargeDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月9日 下午3:14:36
 *
 */
public class RechargeAmountDaoImpl implements IRechargeAmountDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRechargeAmountDao#getAmountList()
	 */
	public List<RechargeAmountVo> getAmountList(int cityId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_recharge_amount where 1=1" ;
		List<RechargeAmountVo>  list= new ArrayList<RechargeAmountVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if(cityId > 0){
				sql += " and (city_id = "+cityId+" or city_id = 0)";
			}
			sql += "  order by amount asc";
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				RechargeAmountVo amount = new RechargeAmountVo(rs);
				list.add(amount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRechargeAmountDao#addAmount(com.pgt.bikelock.vo.RechargeAmountVo)
	 */
	public String addAmount(RechargeAmountVo amount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_recharge_amount (amount,gift,date,city_id) values (?,?,now(),?)";
		String amountId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setBigDecimal(1,amount.getAmount());
			pstmt.setBigDecimal(2,amount.getGift());
			pstmt.setInt(3, amount.getCityId());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					amountId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return amountId; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRechargeAmountDao#updateAmount(com.pgt.bikelock.vo.RechargeAmountVo)
	 */
	public boolean updateAmount(RechargeAmountVo amount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_recharge_amount  set amount = ?,gift = ?,date = now(),city_id = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1,amount.getAmount());
			pstmt.setBigDecimal(2,amount.getGift());
			pstmt.setInt(3, amount.getCityId());
			pstmt.setString(4, amount.getId());
			
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
	 * @see com.pgt.bikelock.dao.IRechargeAmountDao#getAmount(java.lang.String)
	 */
	public RechargeAmountVo getAmount(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_recharge_amount where id = ?" ;
		RechargeAmountVo amount = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				amount = new RechargeAmountVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return amount; 
	}

}
