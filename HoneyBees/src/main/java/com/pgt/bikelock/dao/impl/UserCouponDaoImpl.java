/**
 * FileName:     IUserCouponDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午4:51:44
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.MediaSize.Other;

import com.pgt.bikelock.dao.IUserCouponDao;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserCouponVo;

 /**
 * @ClassName:     IUserCouponDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月10日 下午4:51:44
 *
 */
public class UserCouponDaoImpl implements IUserCouponDao{
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getUserCoupon(java.lang.String, java.math.BigDecimal)
	 */
	public UserCouponVo getUserValueCoupon(String userId, BigDecimal amount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where type = 2 and active_date is not null and used = 0 and amount >= ? and uid = ? order by amount asc limit 0,1" ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setBigDecimal(1, amount);
			pstmt.setString(2, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getUserPercentCoupon(java.lang.String, java.math.BigDecimal)
	 */
	public UserCouponVo getUserPercentCoupon(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where type = 1 and used = 0 and active_date is not null and uid = ? " ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,true);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getCoupon(java.lang.String)
	 */
	public CouponVo getCouponInfoForUser(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where  id = ? " ;
		CouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new CouponVo(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getUserCouponList(java.lang.String)
	 */
	public List<UserCouponVo> getUserCouponList(String userId,int startPage,int showAll) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where  uid = ? " ;
		if(showAll == 0){//显示已激活
			sql += " and active_date is not null";
		}else{
			/*if(OthersSource.getSourceString("register_coupon_auto_send") != null){
				//显示所有未激活/show all not activate
				sql += " and type = 2 and active_date is null";
			}else{
				//显示所有时，过滤未激活的注册充值免费骑类型，防止直接激活/show all, pass not activated register recharge free cycling type, prevent acitvated directly
				sql += " and type != 3 and active_date is null";
			}*/
			//显示未激活
			sql += " and type <= 3 and active_date is null";
	
		}
		sql += "  limit ?,?";
		List<UserCouponVo>   list = new ArrayList<UserCouponVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			pstmt.setInt(2, startPage);
			pstmt.setInt(3, BaseDao.pageSize);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				UserCouponVo couponVo = new UserCouponVo(rs,false);
				couponVo.getCouponVo().setValue(couponVo.getAmount());//显示实时余额/show real time balance
				list.add(couponVo);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#getTypeCouponCount(int)
	 */
	@Override
	public int getTypeCouponCount(String userId,int giftType) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT COUNT(*) AS total  FROM t_user_coupon where uid = ? and gift_from = ?";
	
		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, giftType);
			rs= pstmt.executeQuery();
			if(rs.next()) count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getUserCouponList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<UserCouponVo> getUserCouponList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon_all "
				+ " where (name like '%"+requestVo.getKeyWords()+"%'"
				+ " or phone like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		sql += " order by uc_id "+requestVo.getOrderDirection()+"  LIMIT ?,?";
		List<UserCouponVo>   list = new ArrayList<UserCouponVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				UserCouponVo userCoupon = new UserCouponVo(rs,true);
				userCoupon.getCouponVo().setStart_time(TimeUtil.formaStrDate(userCoupon.getCouponVo().getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				userCoupon.getCouponVo().setEnd_time(TimeUtil.formaStrDate(userCoupon.getCouponVo().getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				list.add(userCoupon);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#addCouponForUser(com.pgt.bikelock.vo.UserCouponVo)
	 */
	public boolean addCouponForUserNotActive(UserCouponVo coupon) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_user_coupon (uid,cid,date,start_time,end_time,amount,gift_from) values (?,?,now(),?,?,?,?) ";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, coupon.getUid());
			pstmt.setString(2, coupon.getCid());
			pstmt.setString(3, coupon.getStart_time());
			pstmt.setString(4, coupon.getEnd_time());
			pstmt.setBigDecimal(5, coupon.getAmount());
			pstmt.setInt(6, coupon.getGift_from());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#addCouponForUserActive(com.pgt.bikelock.vo.UserCouponVo)
	 */
	public boolean addCouponForUserActive(UserCouponVo coupon) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_user_coupon (uid,cid,date,active_date,start_time,end_time,amount) values (?,?,now(),now(),?,?,?) ";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, coupon.getUid());
			pstmt.setString(2, coupon.getCid());
			pstmt.setString(3, coupon.getStart_time());
			pstmt.setString(4, coupon.getEnd_time());
			pstmt.setBigDecimal(5, coupon.getAmount());
		
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}


	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#addCouponForUsers(java.util.List)
	 */
	public String addCouponForUsers(List<UserCouponVo> coupons,boolean active) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_user_coupon (uid,cid,date,active_date,start_time,end_time,amount) values ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String cId = null;
		String activeDate = "null";;
		if(active){
			activeDate = "now()";
		}
		for (int i = 0; i < coupons.size(); i++) {
			if(i!=0){
				sql = sql +",";
			}
			sql = sql +" (?,?,now(),"+activeDate+",?,?,?)";
		}
	
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			
			for (int i = 0; i < coupons.size(); i++) {
				int index = i*5;
				pstmt.setString(1+index, coupons.get(i).getUid());
				pstmt.setString(2+index, coupons.get(i).getCid());
				pstmt.setString(3+index, coupons.get(i).getStart_time());
				pstmt.setString(4+index, coupons.get(i).getEnd_time());
				pstmt.setBigDecimal(5+index, coupons.get(i).getAmount());
			}
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				cId = "";
				while(rs.next()){
					cId += rs.getString(1)+",";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return cId;
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#addCoupons(java.util.List)
	 */
	public String addCouponsNotActive(List<UserCouponVo> coupons,String code) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_user_coupon (cid,code,date,start_time,end_time,amount) values ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String cId = null;
		
		for (int i = 0; i < coupons.size(); i++) {
			if(i!=0){
				sql = sql +",";
			}
			sql = sql +" (?,?,now(),?,?,?)";
		}
		
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			
			for (int i = 0; i < coupons.size(); i++) {
				int index = i*5;
				pstmt.setString(1+index, coupons.get(i).getCid());
				if(code != null){
					pstmt.setString(2+index, code);
				}else{
					pstmt.setString(2+index, ValueUtil.getStringRandom(7));
				}
				
				pstmt.setString(3+index, coupons.get(i).getStart_time());
				pstmt.setString(4+index, coupons.get(i).getEnd_time());
				pstmt.setBigDecimal(5+index, coupons.get(i).getAmount());
			}
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					cId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return cId;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#getUserCoupon(java.lang.String)
	 */
	public UserCouponVo getUserCoupon(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where  uc_id = ? " ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#getUserCouponDetail(java.lang.String)
	 */
	@Override
	public UserCouponVo getUserCouponDetail(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon_all  where  uc_id = ? " ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,true);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#getCoupon(java.lang.String)
	 */
	public UserCouponVo getNotActiveCoupon(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where active_date is null and uc_id = ?" ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#getNotActiveCouponWithCode(java.lang.String)
	 */
	public UserCouponVo getNotActiveCouponWithCode(String code) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where active_date is null and phone is null and code = ? limit 0,1" ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, code);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#checkCouponHaveBeenGet(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean checkCouponHaveBeenGet(String code, String uid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where active_date is null and uid = ? and code = ?" ;
		boolean  flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, uid);
			pstmt.setString(2, code);
			rs = pstmt.executeQuery();
			if(rs.next()){
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
	 * @see com.pgt.bikelock.dao.IUserCouponDao#getRegisterCoupon(java.lang.String)
	 */
	public UserCouponVo getRegisterActivedCoupon(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where  type = 3 and active_date is not null and used = 0 and uid = ?" ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}
	

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#getRegisterNotActiveCoupon(java.lang.String)
	 */
	public UserCouponVo getRegisterNotActiveCoupon(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where  type = 3 and active_date is null and uid = ?" ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}

	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#getFreeCoupon(java.lang.String)
	 */
	public UserCouponVo getFreeCoupon(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_user_coupon  where  type >= 3 and used = 0 and uid = ?" ;
		UserCouponVo  couponVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				couponVo = new UserCouponVo(rs,false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return couponVo; 
	}


	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#updateUserCoupon(com.pgt.bikelock.vo.UserCouponVo)
	 */
	public boolean updateUserCoupon(UserCouponVo coupon) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_coupon set uid = ?,cid = ?,active_date=now() where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, coupon.getUid());
			pstmt.setString(2, coupon.getCid());
			pstmt.setString(3, coupon.getId());
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ICouponDao#deleteUserCoupon(java.lang.String)
	 */
	public boolean deleteUserCoupon(String recordId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_user_coupon where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,recordId);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#activeCoupon(java.lang.String, java.lang.String)
	 */
	public boolean activeCoupon(String userId, String userCouponId,boolean activeNow) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_coupon set uid = ? %s where id = ?";
		String activeNowStr = "";
		if(activeNow){
			activeNowStr = ",active_date=now()";
		}
		sql = String.format(sql, activeNowStr);
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			pstmt.setString(2, userCouponId);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#activeCoupon(java.lang.String)
	 */
	public boolean activeCoupon(String couponId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_coupon set active_date=now() where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, couponId);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#useCoupon(java.lang.String)
	 */
	public boolean useCoupon(String couponId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_coupon set used = 1 where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, couponId);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IUserCouponDao#useCouponAmount(java.lang.String)
	 */
	public boolean updateCouponAmount(UserCouponVo couponVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_user_coupon set amount = ? where id = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, couponVo.getAmount());
			pstmt.setString(2, couponVo.getId());
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}



	

}
