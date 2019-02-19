/**
 * FileName:     BikeUseImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-25 下午5:11:26
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-25       CQCN         1.0             1.0
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
import com.pgt.bikelock.dao.IBikeUseDao;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;

/**
 * @ClassName:     BikeUseImpl
 * @Description:单车使用接口实现/bike user protocol achieve
 * @author:    Albert
 * @date:        2017-3-25 下午5:11:26
 *
 */
public class BikeUseDaoImpl implements IBikeUseDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#startUseBike(com.pgt.bikelock.vo.BikeUseVo)
	 */
	public boolean startUseBike(BikeUseVo useVo) {
		// TODO Auto-generated method stub

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  view_useing_bike where uid = ? and bid = ?";
		boolean  flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, useVo.getUid());
			pstmt.setString(2, useVo.getBid());
			rs = pstmt.executeQuery();

			if(rs.next()){
				flag = rs.getInt("num") > 0?true:false;
				rs.close();
				pstmt.close();

				if(!flag){//防止重复插入/prevent repeat enter
					sql="INSERT INTO t_bike_use (bid,uid,start_lat,start_lng,end_lat,end_lng,date,open_way,update_time,host_id,ride_user,group_ride)"
							+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
					pstmt = conn.prepareStatement(sql);

					pstmt.setString(1,useVo.getBid());
					pstmt.setString(2, useVo.getUid());
					pstmt.setDouble(3, useVo.getStartLat());
					pstmt.setDouble(4, useVo.getStartLng());
					pstmt.setDouble(5, useVo.getStartLat());
					pstmt.setDouble(6, useVo.getStartLng());
					pstmt.setString(7, useVo.getDate());
					pstmt.setInt(8, useVo.getOpenWay());
					pstmt.setString(9, useVo.getDate());
					pstmt.setInt(10, useVo.getHostId());
					pstmt.setString(11, useVo.getRideUser());
					pstmt.setInt(12, useVo.getGroupRide());
					
					if(pstmt.executeUpdate()>0){
						flag =  true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 

	}

	public boolean updatePaySuccess(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set ride_status = 3 where id = ? or host_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,id);
			pstmt.setString(2,id);

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#startUseBikeSuccess(java.lang.String)
	 */
	public boolean startUseBikeSuccess(String bId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set start_time = ?,ride_status = 1 where bid = ? and start_time = 0";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,TimeUtil.getCurrentLongTime());
			pstmt.setString(2, bId);

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

	public boolean startUseBikeSuccess(int uid,long date,int openWay){
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set start_time = ?,ride_status = 1";
		if(openWay != -1){
			sql += ",open_way = "+openWay;
		}
		sql += " where ride_status != 3 and start_time = 0 and date = ? and uid = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1,TimeUtil.getCurrentLongTime());
			pstmt.setLong(2, date);
			pstmt.setInt(3, uid);

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#finishUseBike(com.pgt.bikelock.vo.BikeUseVo)
	 */
	public boolean updateUseBikeProcess(BikeUseVo useVo,boolean finish) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set distance = distance + ?,orbit = ?,end_lat = ?,end_lng = ?,out_area=?,update_time =? where bid = ?";
		if(!finish){
			sql += "  and end_time = 0";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setDouble(1,useVo.getDistance());
			pstmt.setString(2, useVo.getOrbit());
			pstmt.setDouble(3, useVo.getCurrentLat());
			pstmt.setDouble(4, useVo.getCurrentLng());
			pstmt.setInt(5, useVo.getOut_area());
			pstmt.setLong(6, TimeUtil.getCurrentLongTime());
			pstmt.setString(7, useVo.getBid());

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#updateRideInfo(com.pgt.bikelock.vo.BikeUseVo)
	 */
	@Override
	public boolean updateRideInfo(BikeUseVo useVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set distance =  ?,orbit = ?,end_lat = ?,end_lng = ?,out_area=?,update_time =? where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setDouble(1,useVo.getDistance());
			pstmt.setString(2, useVo.getOrbit());
			pstmt.setDouble(3, useVo.getCurrentLat());
			pstmt.setDouble(4, useVo.getCurrentLng());
			pstmt.setInt(5, useVo.getOut_area());
			pstmt.setLong(6, TimeUtil.getCurrentLongTime());
			pstmt.setString(7, useVo.getId());

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#updateUseBikeRequestDate(java.lang.String, long)
	 */
	public boolean updateUseBikeRequestDate(String useId, long requestTime) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set date=? where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1,requestTime);
			pstmt.setString(2, useId);

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#updateUseBikeRider(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateUseBikeRider(String useId, String rider) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set uid=? where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,rider);
			pstmt.setString(2, useId);

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#updateUseBikeUpdateTime(java.lang.String, long)
	 */
	@Override
	public boolean updateUseBikeUpdateTime(String useId, long updateTime) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set update_time=? where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1,updateTime);
			pstmt.setString(2, useId);

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#updateRideIsGroup(java.lang.String)
	 */
	@Override
	public boolean updateRideIsGroup(String useId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set group_ride=1 where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, useId);

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#finishUseBike(java.lang.String, long)
	 */
	public boolean finishUseBike(BikeUseVo useVo,long endTime,int closeWay) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set start_time = ?,end_time = ?,close_way = ?,out_area = ?,old_duration = ?"
				+ ",admin_id = ?,ride_status = ?,ride_amount = ? where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, ValueUtil.getLong(useVo.getStartTime()));
			pstmt.setLong(2, endTime);
			pstmt.setInt(3, closeWay);
			pstmt.setInt(4, useVo.getOut_area());
			pstmt.setInt(5, useVo.getOldDuration());
			pstmt.setString(6, useVo.getAdminId());
			pstmt.setInt(7, useVo.getRideStatus());
			pstmt.setBigDecimal(8, useVo.getRideAmount());
			pstmt.setString(9, useVo.getId());

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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#finishUseBikeWithDefaultTime(java.lang.String, long)
	 */
	public boolean finishUseBikeWithTime(String useId,int minutes) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set end_time = (start_time + ?),ride_status = 2 where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, minutes*60);
			pstmt.setString(2, useId);
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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#finishUseBike(com.pgt.bikelock.vo.BikeUseVo)
	 */
	public boolean updateUseBikeEndLocation(BikeUseVo useVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set distance = ?,orbit = ?,end_lat = ?,end_lng = ?,lock_location=1 where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setDouble(1,useVo.getDistance());
			pstmt.setString(2, useVo.getOrbit());
			pstmt.setDouble(3, useVo.getEndLat());
			pstmt.setDouble(4, useVo.getEndLng());
			pstmt.setString(5, useVo.getId());

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


	public BikeUseVo getLastBikeUseDetialedForUser (UserVo user) {

		if (user == null) {
			return null;
		}
		return getLastBikeUseDetialedForUser(user.getuId());
	}


	public BikeUseVo getLastBikeUseDetialedForUser (String userId) {

		String sql = "SELECT u.*,b.g_lat,b.g_lng,t.price,t.hold_unit_type,t.hold_count,t.hold_price,t.hold_max_count FROM view_bike_use_detail u JOIN t_bike b ON b.id = u.bid JOIN t_bike_type t ON b.type_id = t.id " +
			"WHERE uid = ? ORDER BY end_time DESC LIMIT 1";

		Connection connection = DataSourceUtil.getConnection();
		BikeUseVo  usage = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql); 
			statement.setInt(1, ValueUtil.getInt(userId));
			results = statement.executeQuery();

			if(results.next()) {

				usage = new BikeUseVo(results, false);
				usage.setStartTime(TimeUtil.formaStrDate(usage.getStartTime(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				usage.setUserVo(new UserVo(results));

				BikeVo bike = new BikeVo(results);
				bike.setTypeVo(new BikeTypeVo(results));
				usage.setBikeVo(bike);

			}


		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			DataSourceUtil.close(results, statement, connection);
		}

		return usage;

	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseInfo(java.lang.String)
	 */
	public BikeUseVo getUseInfo(String useId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_bike_use_detail where id = ?" ;
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, useId);

			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo = new BikeUseVo(rs,false);
				useVo.setStartTime(TimeUtil.formaStrDate(useVo.getStartTime(),TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				useVo.setUserVo(new UserVo(rs));
				useVo.setBikeVo(new BikeVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseInfo(long)
	 */
	@Override
	public BikeUseVo getUseInfo(long date) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_bike_use where date = ?" ;
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, date);

			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo = new BikeUseVo(rs,false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseInfo(java.lang.String)
	 */
	public BikeUseVo getUseInfo(String userId,String bikeId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_bike_use where uid = ? and bid = ? and start_time > 0 and end_time = 0" ;
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			pstmt.setString(2, bikeId);

			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo = new BikeUseVo(rs,false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}


	public BikeUseVo getFinalUseInfoWithNoLockLocation(long imei){
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_bike_use_detail where start_time > 0 and imei = ? order by id desc limit 0,1" ;
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, imei);

			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo = new BikeUseVo(rs,false);
				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setImei(BaseDao.getString(rs, "imei"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				if(BaseDao.isExistColumn(rs, "readpack")){
					bike.setReadpack(rs.getInt("readpack"));
				}
				if(BaseDao.isExistColumn(rs, "city_id")){
					bike.setCityId(rs.getInt("city_id"));
				}
				bike.setTypeId(BaseDao.getString(rs, "type_id"));
//				bike.setTypeVo(new BikeTypeVo(rs));
				useVo.setBikeVo(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseInfoWithImei(java.lang.String)
	 */
	public BikeUseVo getUseIngDetailInfo(String imei,String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT v.*, t.hold_price,t.hold_unit_type,t.hold_count,t.hold_max_count FROM  view_useing_bike v JOIN t_bike_type t ON v.type_id = t.id where imei = ?";
		if(userId != null){
			sql="SELECT v.*, t.hold_price,t.hold_unit_type,t.hold_count,t.hold_max_count FROM  view_useing_bike v JOIN t_bike_type t ON v.type_id = t.id where uid = ?";
		}
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, imei);
			if(userId != null){
				pstmt.setString(1, userId);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo =  new BikeUseVo();
				useVo.setId(rs.getString("id"));
				useVo.setUid(rs.getString("uid"));
				if(BaseDao.isExistColumn(rs, "bid")){
					useVo.setBid(rs.getString("bid"));
				}

				useVo.setStartTime(rs.getString("start_time"));
				useVo.setDistance(rs.getDouble("distance"));
				useVo.setOrbit(rs.getString("orbit"));
				useVo.setEndLat(BaseDao.getDouble(rs, "end_lat"));
				useVo.setEndLng(BaseDao.getDouble(rs, "end_lng"));
				useVo.setOut_area(BaseDao.getInt(rs, "out_area"));
				useVo.setDate(BaseDao.getString(rs, "date"));
				useVo.setHostId(BaseDao.getInt(rs, "host_id"));

				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setImei(BaseDao.getString(rs, "imei"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				if(BaseDao.isExistColumn(rs, "readpack")){
					bike.setReadpack(rs.getInt("readpack"));
				}
				if(BaseDao.isExistColumn(rs, "city_id")){
					bike.setCityId(rs.getInt("city_id"));
				}
				bike.setBikeType(BaseDao.getInt(rs, "bike_type"));
				bike.setTypeVo(new BikeTypeVo(rs));
				useVo.setBikeVo(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseInfoWithImei(java.lang.String)
	 */
	public BikeUseVo getUseIngDetailInfo(int userId,long date) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql= "";
		if(userId > 0){
			sql="SELECT v.*, t.hold_price,t.hold_unit_type,t.hold_count,t.hold_max_count FROM "
					+ " view_useing_bike v JOIN t_bike_type t ON v.type_id = t.id where  uid = ?";
			if(date > 0){
				sql += "  and date = ?";
			}
		}else{
			sql="SELECT v.*, t.hold_price,t.hold_unit_type,t.hold_count,t.hold_max_count FROM "
					+ " view_useing_bike v JOIN t_bike_type t ON v.type_id = t.id where  date = ?";
		}
		
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			
			if(userId > 0){
				pstmt.setInt(1, userId);
				if(date > 0){
					pstmt.setLong(2, date);
				}
			}else{
				pstmt.setLong(1, date);
			}
	
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo =  new BikeUseVo();
				useVo.setId(rs.getString("id"));
				useVo.setUid(rs.getString("uid"));
				if(BaseDao.isExistColumn(rs, "bid")){
					useVo.setBid(rs.getString("bid"));
				}

				useVo.setStartTime(rs.getString("start_time"));
				useVo.setDistance(rs.getDouble("distance"));
				useVo.setOrbit(rs.getString("orbit"));
				useVo.setEndLat(BaseDao.getDouble(rs, "end_lat"));
				useVo.setEndLng(BaseDao.getDouble(rs, "end_lng"));
				useVo.setDate(BaseDao.getString(rs, "date"));
				useVo.setHostId(BaseDao.getInt(rs, "host_id"));
				useVo.setRideUser(BaseDao.getString(rs, "ride_user"));
				useVo.setGroupRide(BaseDao.getInt(rs, "group_ride"));
				
				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setImei(BaseDao.getString(rs, "imei"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				if(BaseDao.isExistColumn(rs, "readpack")){
					bike.setReadpack(rs.getInt("readpack"));
				}
				if(BaseDao.isExistColumn(rs, "city_id")){
					bike.setCityId(rs.getInt("city_id"));
				}
				bike.setTypeVo(new BikeTypeVo(rs));
				useVo.setBikeVo(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseInfoWithUser(java.lang.String)
	 */
	public BikeUseVo getUseIngInfoWithUser(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_useing_bike where uid = ? and host_id = 0" ;
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo =  new BikeUseVo(rs, false);
				/*				useVo.setId(rs.getString("id"));
				useVo.setUid(rs.getString("uid"));
				useVo.setStartTime(rs.getString("start_time"));
				useVo.setStartLat(rs.getDouble("start_lat"));
				useVo.setStartLng(rs.getDouble("start_lng"));
				useVo.setEndLat(rs.getDouble("end_lat"));
				useVo.setEndLng(rs.getDouble("end_lng"));
				useVo.setDistance(rs.getDouble("distance"));
				useVo.setOrbit(rs.getString("orbit"));*/
				useVo.setGroupRide(BaseDao.getInt(rs, "group_ride"));
				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				bike.setPrice(rs.getBigDecimal("price"));
				bike.setTypeCount(rs.getInt("count"));
				bike.setReadpack(BaseDao.getInt(rs,"readpack"));
				bike.setCityId(BaseDao.getInt(rs, "city_id"));
				bike.setBikeType(BaseDao.getInt(rs, "bike_type"));
				bike.setPower(BaseDao.getInt(rs,"power"));
				useVo.setBikeVo(bike);
				useVo.setReadpack(bike.getReadpack());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseIngCountWithUser(java.lang.String)
	 */
	@Override
	public int getRideIngCountWithUser(String userId,String bnumber,boolean started) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  view_useing_bike where uid = ?" ;
		if(bnumber != null){
			sql += " and number != ?";
		}
		if(started){
			sql += " and start_time > 0";
		}
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			if(bnumber != null){
				pstmt.setString(2, bnumber);
			}
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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getRideListWithUser(java.lang.String, int)
	 */
	@Override
	public List<BikeUseVo> getRideListWithUser(String userId,int rideStatus) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_bike_use where  uid = ?" ;
		if(rideStatus == 0){
			sql += "  and ride_status != 3";
		}else{
			sql += " and ride_status = "+rideStatus;
		}
		List<BikeUseVo> useList = new ArrayList<BikeUseVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeUseVo useVo =  new BikeUseVo(rs, false);
			
				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				bike.setPrice(rs.getBigDecimal("price"));
				bike.setTypeCount(rs.getInt("count"));
				bike.setReadpack(BaseDao.getInt(rs,"readpack"));
				bike.setCityId(BaseDao.getInt(rs, "city_id"));
				bike.setBikeType(BaseDao.getInt(rs, "bike_type"));
				bike.setPower(BaseDao.getInt(rs, "power"));
				useVo.setBikeVo(bike);
				useVo.setReadpack(bike.getReadpack());
				useVo.setDuration(BaseDao.getInt(rs, "duration"));				
				
				useList.add(useVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getRideListWithHost(java.lang.String)
	 */
	@Override
	public List<BikeUseVo> getRideListWithHost(String hostId,boolean finish) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_bike_use where " ;
		if(finish){
			sql += "  ride_status = 3";
		}else{
			sql += "  ride_status !=3";
		}
		sql += " and (id = ? or host_id = ?)";
		List<BikeUseVo> useList = new ArrayList<BikeUseVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, hostId);
			pstmt.setString(2, hostId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeUseVo useVo =  new BikeUseVo(rs, false);
				
				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				bike.setPrice(rs.getBigDecimal("price"));
				bike.setTypeCount(rs.getInt("count"));
				bike.setReadpack(BaseDao.getInt(rs,"readpack"));
				bike.setCityId(BaseDao.getInt(rs, "city_id"));
				useVo.setBikeVo(bike);
				useVo.setReadpack(bike.getReadpack());
				if(useVo.getRideStatus() >= 2){
					int duration = ValueUtil.getInt((ValueUtil.getLong(useVo.getEndTime())-ValueUtil.getLong(useVo.getStartTime())));
					useVo.setDuration(duration);
				}
				useList.add(useVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useList; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseIngInfoWithBike(java.lang.String)
	 */
	public BikeUseVo getUseIngInfoWithBike(String bikeNumber) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_useing_bike where number = ?" ;
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, bikeNumber);
			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo =  new BikeUseVo();
				useVo.setId(rs.getString("id"));
				useVo.setUid(rs.getString("uid"));
				useVo.setStartTime(rs.getString("start_time"));
				useVo.setStartLat(rs.getDouble("start_lat"));
				useVo.setStartLng(rs.getDouble("start_lng"));
				useVo.setEndLat(rs.getDouble("end_lat"));
				useVo.setEndLng(rs.getDouble("end_lng"));
				useVo.setDistance(rs.getDouble("distance"));
				useVo.setOrbit(rs.getString("orbit"));
				useVo.setOut_area(rs.getInt("out_area"));
				useVo.setDate(rs.getString("date"));
				useVo.setUpdateTime(rs.getLong("update_time"));

				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				bike.setPrice(rs.getBigDecimal("price"));
				useVo.setBikeVo(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}




	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getTodayUseInfo(java.lang.String)
	 */
	public BikeUseVo getTodayUseInfo(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT SUM(distance) as distance,SUM(duration) as duration,SUM(amount) as amount"
				+ " from view_bike_used_today where uid = ? ";
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo =  new BikeUseVo();
				useVo.setDistance(rs.getDouble("distance")/1000.00);
				useVo.setDuration(rs.getInt("duration"));
				useVo.setAmount(rs.getBigDecimal("amount"));
				useVo.setCalorie(ValueUtil.getCalorie(useVo.getDuration(), useVo.getDistance()));
				useVo.setCarbon(ValueUtil.getCarbon(useVo.getDuration(), useVo.getDistance()));
				useVo.setDistance(rs.getDouble("distance")/1000.00);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getTotalUseInfo(java.lang.String)
	 */
	public BikeUseVo getTotalUseInfo(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT SUM(distance) as distance,SUM(duration) as duration,SUM(amount) as amount"
				+ " from view_bike_used_total where uid = ? ";
		BikeUseVo  useVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				useVo =  new BikeUseVo();
				useVo.setDistance(rs.getDouble("distance"));
				int duration = 0 ;
				if(rs.getInt("duration") > 0){
					duration = rs.getInt("duration")/60;
				}
				if(duration == 0){
					duration = 1;
				}
				useVo.setDuration(duration);
				useVo.setAmount(rs.getBigDecimal("amount"));
				useVo.setCalorie(ValueUtil.getCalorie(useVo.getDuration(), useVo.getDistance()));
				useVo.setCarbon(ValueUtil.getCarbon(useVo.getDuration(), useVo.getDistance()));
				useVo.setDistance(rs.getDouble("distance")/1000.00);
				useVo.setActiveDay(getActiveDay(userId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useVo; 
	}

	/**
	 * 
	 * @Title:        getActiveDay 
	 * @Description:  获取用户骑行活跃天数/get user riding activity day
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年8月2日 下午2:22:44
	 */
	private int getActiveDay(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql=" SELECT * from view_bike_used_total  where uid = ? and type = 1  GROUP BY date ";
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				count ++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getUseingInfoWithUser(java.lang.String)
	 */
	public boolean haveUseingBike(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  view_useing_bike where uid = ?";
		boolean  flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				flag = rs.getInt("num") > 0?true:false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}



	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getMaxUseId(java.lang.String)
	 */
	public String getMaxUseId(String bid) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT id FROM  t_bike_use where bid = ?  and end_time = 0" ;
		String maxId = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, bid);

			rs = pstmt.executeQuery();
			if(rs.next()){
				maxId = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return maxId; 
	}


	public List<BikeUseVo> getBikeUseListForUser (UserVo user,int pageNo) {

		if (user == null) {
			return null;
		}
		List<BikeUseVo> list = getBikeUseListForUser(user.getuId(),pageNo);
	/*	for (BikeUseVo bikeUseVo : list) {
			if(bikeUseVo.getHostId() == 0 && bikeUseVo.getGroupRide() == 1){
				List<BikeUseVo> subList = getBikeUseListForUser(user.getuId(),bikeUseVo.getId(),0);
				if(subList.size() > 1){
					//group ride
					bikeUseVo.setGroupRideList(subList);
				}
			}
		}*/
		
		
		return list;
	}


	public List<BikeUseVo> getBikeUseListForUser (String userId,int pageNo) {

		Connection conn = DataSourceUtil.getConnection();

		String sql = "SELECT * FROM view_bike_use_detail " + 
			"WHERE  host_id = 0 and uid = ? AND start_time > 0 and end_time > 0 " +
			"ORDER BY start_time desc";
		
		if(pageNo > 0){
			sql += " limit "+(pageNo-1)*BaseDao.pageSize+","+BaseDao.pageSize;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeUseVo>  list = new ArrayList<BikeUseVo>();

		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);

			rs = pstmt.executeQuery();
			while(rs.next()){

				BikeUseVo bikeUse= new BikeUseVo(rs, false);
				bikeUse.setStatus(3);
				bikeUse.setGroupRide(BaseDao.getInt(rs, "group_ride"));
				bikeUse.setStartTime(TimeUtil.formaStrDate(rs.getString("start_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bikeUse.setEndTime(TimeUtil.formaStrDate(rs.getString("end_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bikeUse.setStartStamp(rs.getLong("start_time"));
				bikeUse.setEndStamp(rs.getLong("end_time"));
				list.add(bikeUse);

			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DataSourceUtil.close(rs, pstmt, conn);
		}

		return list;

	}
	
	public List<BikeUseVo> getGroupRideListForUser (String userId,String hostId,int pageNo) {

		Connection conn = DataSourceUtil.getConnection();

		String sql = "SELECT * FROM view_bike_use_detail " + 
					"WHERE uid = ? and (id = ? or host_id = ?) AND start_time > 0 and end_time > 0 " +
					"ORDER BY id limit ?,?";
	
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeUseVo>  list = new ArrayList<BikeUseVo>();

		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, hostId);
			pstmt.setString(3, hostId);			
			pstmt.setInt(4, pageNo);
			pstmt.setInt(5, BaseDao.pageSize);

			rs = pstmt.executeQuery();
			while(rs.next()){

				BikeUseVo bikeUse= new BikeUseVo(rs, false);
				bikeUse.setStatus(3);
				bikeUse.setGroupRide(BaseDao.getInt(rs, "group_ride"));
				bikeUse.setDuration(BaseDao.getInt(rs, "duration"));
				list.add(bikeUse);

			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DataSourceUtil.close(rs, pstmt, conn);
		}

		return list;

	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getBikeUseList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<BikeUseVo> getBikeUseList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_bike_use_detail " +
				" where (number like '%"+requestVo.getKeyWords()+"%' or"
				+ " id like '%"+requestVo.getKeyWords()+"%' or phone like '%"+requestVo.getKeyWords()+"%'"
				+" or email like '%"+requestVo.getKeyWords()+"%'"		
				+ ")";
		if(requestVo.getType() == 1){//正在开锁/unlocking
			sql += " and start_time = 0";
		}else if(requestVo.getType() == 2){//使用中/using
			sql += " and start_time > 0 and end_time = 0";
		}else if(requestVo.getType() == 3){//完成/finish
			sql += " and start_time > 0 and end_time > 0";
		}
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		if(requestVo.getWay() > 0){
			sql += " and open_way = "+(requestVo.getWay()-1);
		}
		
		if(requestVo.getExtendType1() > 0){
			sql += " and close_way = "+(requestVo.getExtendType1()-1);
		}
		
		if(requestVo.getStartTime() != null){
			sql += " and start_time >= "+requestVo.getStartTime();
		}

		if(requestVo.getEndTime() != null){
			sql += " and start_time <= "+requestVo.getEndTime();
		}
		sql += " order by "+requestVo.getOrderField()+" "+requestVo.getOrderDirection()+" LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeUseVo>  list = new ArrayList<BikeUseVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeUseVo bikeUse= new BikeUseVo(rs,false);
				
				if(ValueUtil.getInt(bikeUse.getStartTime()) == 0){
					bikeUse.setStatus(1);
				}else if(ValueUtil.getInt(bikeUse.getStartTime()) > 0 
						&& ValueUtil.getInt(bikeUse.getEndTime()) == 0){
					bikeUse.setStatus(2);
				}else if(ValueUtil.getInt(bikeUse.getStartTime()) > 0 
						&& ValueUtil.getInt(bikeUse.getEndTime()) > 0){
					bikeUse.setStatus(3);
				}

				bikeUse.setStartTime(TimeUtil.formaStrDate(rs.getString("start_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bikeUse.setEndTime(TimeUtil.formaStrDate(rs.getString("end_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm));

				UserVo userVo = new UserVo(rs);
				UserDetailVo deailVo = new UserDetailVo();
				deailVo.setEmail(BaseDao.getString(rs, "email"));
				userVo.setDetailVo(deailVo);
				bikeUse.setUserVo(userVo);
				bikeUse.setBikeVo(new BikeVo(rs));

				list.add(bikeUse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num  from view_bike_use_detail " +
				" where (number like '%"+requestVo.getKeyWords()+"%' or"
				+ " id like '%"+requestVo.getKeyWords()+"%' or phone like '%"+requestVo.getKeyWords()+"%'"
				+" or email like '%"+requestVo.getKeyWords()+"%'"		
				+ ")";
		if(requestVo.getType() == 1){//增在开锁/locking
			sql += " and start_time = 0";
		}else if(requestVo.getType() == 2){//使用中/using
			sql += " and start_time > 0 and end_time = 0";
		}else if(requestVo.getType() == 3){//完成/achieve
			sql += " and start_time > 0 and end_time > 0";
		}
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		
		if(requestVo.getWay() > 0){
			sql += " and open_way = "+(requestVo.getWay()-1);
		}
		
		if(requestVo.getExtendType1() > 0){
			sql += " and close_way = "+(requestVo.getExtendType1()-1);
		}
		
		if(requestVo.getStartTime() != null){
			sql += " and start_time >= "+requestVo.getStartTime();
		}

		if(requestVo.getEndTime() != null){
			sql += " and start_time <= "+requestVo.getEndTime();
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getBikeUseList()
	 */
	@Override
	public List<BikeUseVo> getBikeUseList(Long startDate,Long endDate) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_bike_use_detail where 1=1";
		if(startDate > 0){
			sql += " and date >"+startDate;
		}
		if(endDate > 0){
			sql += " and date <="+endDate;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeUseVo>  list = new ArrayList<BikeUseVo>();
		try {
			pstmt = conn.prepareStatement(sql);
	
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeUseVo bikeUse= new BikeUseVo(rs,false);

				bikeUse.setStartTime(TimeUtil.formaStrDate(rs.getString("start_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bikeUse.setEndTime(TimeUtil.formaStrDate(rs.getString("end_time"), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				if(!StringUtils.isEmpty(bikeUse.getOrbit())){
					List<LatLng> latLngList = AMapUtil.decode(bikeUse.getOrbit());
					bikeUse.setLatLngList(latLngList);
				}else{
					bikeUse.setLatLngList(new ArrayList<LatLng>());
				}
				list.add(bikeUse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getBikeUseingList()
	 */
	@Override
	public List<BikeUseVo> getBikeUseingList() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_useing_bike" ;
		List<BikeUseVo>  useList = new ArrayList<BikeUseVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
	
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeUseVo useVo =  new BikeUseVo();
				useVo.setId(rs.getString("id"));
				useVo.setUid(rs.getString("uid"));
				useVo.setStartTime(rs.getString("start_time"));
				useVo.setStartLat(rs.getDouble("start_lat"));
				useVo.setStartLng(rs.getDouble("start_lng"));
				useVo.setEndLat(rs.getDouble("end_lat"));
				useVo.setEndLng(rs.getDouble("end_lng"));
				useVo.setDistance(rs.getDouble("distance"));
				useVo.setOrbit(rs.getString("orbit"));
				useVo.setOut_area(rs.getInt("out_area"));
				useVo.setDate(rs.getString("date"));

				BikeVo bike = new BikeVo();
				bike.setNumber(rs.getString("number"));
				bike.setStatus(rs.getInt("status"));
				bike.setUseStatus(rs.getInt("use_status"));
				bike.setPrice(rs.getBigDecimal("price"));
				useVo.setBikeVo(bike);
				
				useList.add(useVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return useList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#getStatisticsList()
	 */
	@Override
	public List<StatisticsVo> getStatisticsList(String ids) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="select min(start_time) as min_time,max(start_time) as max_time,date_format(FROM_UNIXTIME(start_time),'%k') as title,"
				+ "CONVERT(date_format(FROM_UNIXTIME(start_time),'%k'),signed) as title_number"
				+ " from t_bike_use where id in ("+ids+") group by title ORDER BY title_number";
		
		List<String> typeValueList = new ArrayList<String>();
		List<String> typeMinTimeValueList = new ArrayList<String>();
		List<String> typeMaxTimeValueList = new ArrayList<String>();
		List<StatisticsVo> recordList = new ArrayList<StatisticsVo>();
		recordList.add(new StatisticsVo());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String value = rs.getString("title");
				if(value != null){
					typeValueList.add(value);
					typeMinTimeValueList.add(rs.getString("min_time"));
					typeMaxTimeValueList.add(rs.getString("max_time"));
				}

			}
			
			//遍历统计元素/check Statistical elements
			for (int i=0;i<typeValueList.size();i++) {
				sql = "select count(*) as value from t_bike_use  where start_time >= ? and start_time <= ?";
				pstmt = conn.prepareStatement(sql);
				String typeValue = typeValueList.get(i);
				pstmt.setString(1, typeMinTimeValueList.get(i));
				pstmt.setString(2, typeMaxTimeValueList.get(i));
				rs = pstmt.executeQuery();
				while(rs.next()){
					StatisticsVo record = new StatisticsVo(rs);
					record.setTitle(typeValue);
					recordList.add(record);
					
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return recordList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#haveUsedInTime(java.lang.String, long)
	 */
	public boolean haveUsedInTime(String bid, long time) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  t_bike_use where bid = ?  and start_time > ?" ;
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, bid);
			pstmt.setLong(2, time);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getInt("num") > 0){
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#haveOpenSuccess(java.lang.String, long)
	 */
	public boolean haveOpenSuccess(long time) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  t_bike_use where date = ?  and start_time > 0" ;
		boolean flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, time);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getInt("num") > 0){
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeUseDao#setBikeUseRedPack(java.lang.String)
	 */
	public boolean setBikeUseRedPack(String useId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_use set redpack where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, useId);
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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#deleteBikeUse(long)
	 */
	@Override
	public boolean deleteBikeUse(long time) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_bike_use where start_time = 0 and date = ? " ;               
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, time);
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
	 * @see com.pgt.bikelock.dao.IBikeUseDao#deleteBikeUse(long)
	 */
	@Override
	public boolean deleteBikeUse(long time,int uid,boolean openFail) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_bike_use where date = ? and uid = ?" ;  
		if(openFail){
			sql += " and start_time = 0";
		}
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, time);
			pstmt.setInt(2, uid);
			
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
