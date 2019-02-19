/**
 * FileName:     RedPackRuleDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 下午5:33:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IRedPackRuleDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.RedPackRuleVo;

 /**
 * @ClassName:     RedPackRuleDaoImpl
 * @Description:红包规则接口实现/red envelopes rules protocol achieve
 * @author:    Albert
 * @date:        2017年4月26日 下午5:33:38
 *
 */
public class RedPackRuleDaoImpl implements IRedPackRuleDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackRuleDao#getRuleList()
	 */
	public List<RedPackRuleVo> getRuleList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_redpack_rule " ;
		if(requestVo.getCityId() != 0){
			sql += " where city_id = "+requestVo.getCityId();
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		List<RedPackRuleVo> ruleList = new ArrayList<RedPackRuleVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				RedPackRuleVo rule = new RedPackRuleVo(rs);
				rule.setStart_time(TimeUtil.formaStrDate(rule.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				rule.setEnd_time(TimeUtil.formaStrDate(rule.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				ruleList.add(rule);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return ruleList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackRuleDao#getRule()
	 */
	public RedPackRuleVo getRule(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_redpack_rule where id = ?";
		RedPackRuleVo rule = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				rule = new RedPackRuleVo(rs);
				rule.setStart_time(TimeUtil.formaStrDate(rule.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				rule.setEnd_time(TimeUtil.formaStrDate(rule.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return rule; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackRuleDao#getRuleByBikeId(java.lang.String)
	 */
	public RedPackRuleVo getRuleByBikeId(String bid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_redpack_bike_rule where bid = ?";
		RedPackRuleVo rule = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bid);
			rs = pstmt.executeQuery();
			while(rs.next()){
				rule = new RedPackRuleVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return rule; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackRuleDao#updateRule(com.pgt.bikelock.vo.admin.RedPackRuleVo)
	 */
	public boolean updateRule(RedPackRuleVo ruleVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_red_package_rule set free_use_time = ?,least_use_time = ?,"
				+ "max_amount = ?,start_time = ?,end_time = ?,type = ?,coupon_id = ?,coupon_num = ?,area_ids = ?"
				+ ",must_in_area = ?,name = ? where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, ruleVo.getFree_use_time());
			pstmt.setString(2, ruleVo.getLeast_use_time());
			pstmt.setBigDecimal(3, ruleVo.getMax_amount());
			pstmt.setString(4, ruleVo.getStart_time());
			pstmt.setString(5, ruleVo.getEnd_time());
			pstmt.setInt(6, ruleVo.getType());
			pstmt.setString(7, ruleVo.getCoupon_id());
			pstmt.setInt(8, ruleVo.getCoupon_num());
			pstmt.setString(9, ruleVo.getArea_ids());
			pstmt.setInt(10, ruleVo.getMust_in_area());
			pstmt.setString(11, ruleVo.getName());
			pstmt.setString(12, ruleVo.getId());
			if(pstmt.executeUpdate() > 0){
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
	 * @see com.pgt.bikelock.dao.IRedPackRuleDao#addRule(com.pgt.bikelock.vo.admin.RedPackRuleVo)
	 */
	public String addRule(RedPackRuleVo ruleVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_red_package_rule (free_use_time,least_use_time,"
				+ "max_amount,start_time,end_time,date,type,coupon_id,coupon_num,area_ids,must_in_area,name) values (?,?,?,?,?,now(),?,?,?,?,?,?)";
		String ruleId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, ruleVo.getFree_use_time());
			pstmt.setString(2, ruleVo.getLeast_use_time());
			pstmt.setBigDecimal(3, ruleVo.getMax_amount());
			pstmt.setString(4, ruleVo.getStart_time());
			pstmt.setString(5, ruleVo.getEnd_time());
			pstmt.setInt(6, ruleVo.getType());
			pstmt.setString(7, ruleVo.getCoupon_id());
			pstmt.setInt(8, ruleVo.getCoupon_num());
			pstmt.setString(9, ruleVo.getArea_ids());
			pstmt.setInt(10, ruleVo.getMust_in_area());
			pstmt.setString(11, ruleVo.getName());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					ruleId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return ruleId; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IRedPackRuleDao#deleteRule(java.lang.String)
	 */
	public boolean deleteRule(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_red_package_rule where id = ?";
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			if(pstmt.executeUpdate() > 0){
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
