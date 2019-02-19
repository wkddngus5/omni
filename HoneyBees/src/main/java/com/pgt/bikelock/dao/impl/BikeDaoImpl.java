/**
 * FileName:     IBikeDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-25 下午4:37:17
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-25       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.print.attribute.standard.MediaSize.Other;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.bo.SystemConfigBo;
import com.pgt.bikelock.dao.IBikeDao;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.RequestListVo;


/**
 * @ClassName:     IBikeDaoImpl
 * @Description:单车信息接口实现/bike information interface achieve 
 * @author:    Albert
 * @date:        2017-3-25 下午4:37:17
 *
 */
public class BikeDaoImpl implements IBikeDao{

	//	static final String Near_Bike_Surround =  "0.1";//(100KM) 0.1 ,(1.2KM)0.006
	//	public static final String Near_Bike_Show_All = OthersSource.getSourceString("near_bike_show_all") == null ?"1":OthersSource.getSourceString("near_bike_show_all");

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getNearBike(com.pgt.bikelock.vo.BikeVo)
	 */
	public List<BikeVo> getNearBike(BikeVo bike,String industryType) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_near_bike where "
				+ "abs(g_lat-?) < "+SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SURROUND+" and abs(g_lng-?) <  "+
				SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SURROUND+ " order by abs(g_lat-?) asc,abs(g_lng-?) asc "
				+" limit 0,"+SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_COUNT;
		if(SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SHOWALL == 1){
			sql="SELECT * FROM  view_near_bike";
		}
		List<BikeVo>  list= new ArrayList<BikeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);

			if(SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SHOWALL != 1){
				pstmt.setDouble(1, bike.getgLat());
				pstmt.setDouble(2, bike.getgLng());
				pstmt.setDouble(3, bike.getgLat());
				pstmt.setDouble(4, bike.getgLng());
			}

			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeVo viewBike = new BikeVo(rs);

				list.add(viewBike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}


	public List<BikeVo> getNearBike(double lat, double lon, int cityId, String industryType) {

		Connection conn = DataSourceUtil.getConnection();

		String sql="SELECT * FROM  view_near_bike where abs(g_lat-?) < "+SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SURROUND+" and abs(g_lng-?) <  "
				+SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SURROUND+" AND city_id = ? limit 0,100" ;
		if(SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SHOWALL == 1){
			sql="SELECT * FROM  view_near_bike where city_id = ?";
		}

		List<BikeVo>  list= new ArrayList<BikeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);

			if(SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SHOWALL != 1){
				pstmt.setDouble(1, lat);
				pstmt.setDouble(2, lon);
				pstmt.setDouble(3, cityId);
			}
			else {
				pstmt.setDouble(1, cityId);
			}

			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeVo viewBike = new BikeVo(rs);
				list.add(viewBike);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return list;

	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeInfoWithNumber(java.lang.String)
	 */
	public BikeVo getBikeInfoWithNumber(String number) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.*,t.price,t.count,t.lock_type,t.hold_price,t.hold_unit_type,t.hold_count,t.hold_max_count FROM  t_bike b,t_bike_type t where b.type_id = t.id and number = ?" ;
		BikeVo  bikeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, number);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bikeVo = new BikeVo(rs);
				bikeVo.setPrice(rs.getBigDecimal("price"));
				bikeVo.setTypeCount(rs.getInt("count"));
				bikeVo.setTypeVo(new BikeTypeVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeInfoWithImei(java.lang.String)
	 */
	public BikeVo getBikeInfoWithImei(String imei) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_bike where imei = ? " ;
		BikeVo  bikeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, imei);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bikeVo = new BikeVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeDetailInfoWithImei(java.lang.String)
	 */
	@Override
	public BikeVo getBikeDetailInfoWithImei(long imei) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_bike where imei = ? " ;
		BikeVo  bikeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, imei);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bikeVo = new BikeVo(rs);
				if(!StringUtils.isEmpty(rs.getString("extend_info"))){
					bikeVo.setExtendInfo(JSONObject.parseObject(rs.getString("extend_info")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeWithTypeInfo(java.lang.String)
	 */
	public BikeVo getBikeWithTypeInfo(String imei) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.*,t.price,t.count,t.hold_price,t.hold_unit_type,t.hold_count,t.hold_max_count FROM  t_bike b,t_bike_type t where b.type_id = t.id and imei = ?" ;
		BikeVo  bikeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, imei);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bikeVo = new BikeVo(rs);
				bikeVo.setTypeVo(new BikeTypeVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeInfo(java.lang.String)
	 */
	public BikeVo getBikePriceInfo(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.*,t.price,t.count,t.lock_type,bl.mac,t.hold_price,t.hold_unit_type,t.hold_count,t.hold_max_count FROM  view_map_bike as b LEFT JOIN t_bike_type as t ON b.type_id = t.id "
				+ "LEFT JOIN t_bike_ble as bl  on b.id = bl.bid where b.id = ?" ;
		BikeVo  bikeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bikeVo = new BikeVo(rs);
				bikeVo.setTypeVo(new BikeTypeVo(rs));
				if(!StringUtils.isEmpty(rs.getString("extend_info"))){
					bikeVo.setExtendInfo(JSONObject.parseObject(rs.getString("extend_info")));
				}
				bikeVo.setBikeTypeStr(LanguageUtil.getTypeStr("bike_lock_Type_values", bikeVo.getBikeType()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeDetailInfo(java.lang.String)
	 */
	@Override
	public BikeVo getBikeDetailInfo(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.*,bl.mac FROM  t_bike as b LEFT JOIN  t_bike_ble as bl on b.id = bl.bid where b.id = ?" ;
		BikeVo  bikeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				bikeVo = new BikeVo(rs);
				bikeVo.setTypeVo(new BikeTypeVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getLockExtendInfo(long)
	 */
	@Override
	public JSONObject getLockExtendInfo(long imei) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT extend_info from t_bike where imei = ?" ;
		JSONObject data = new JSONObject();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, imei);
			rs = pstmt.executeQuery();
			while(rs.next()){
				if(!StringUtils.isEmpty(rs.getString("extend_info"))){
					data  = JSONObject.parseObject(rs.getString("extend_info"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return data; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateHeart(long, short, long)
	 */
	public boolean updateBikeInfoWithHeart(long imei, short power, long heartTime,int status,int bikeStatus) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set power=?,heart_time=?,status = ?,bike_status = ?   WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setShort(1,power);
			pstmt.setLong(2, heartTime);
			pstmt.setInt(3, status);
			pstmt.setInt(4, bikeStatus);
			pstmt.setLong(5,imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	public boolean updateHeart(long imei,short power,long heartTime,int status,int gsm){
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set power=?,heart_time=?,status = ?,gsm = ?   WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setShort(1,power);
			pstmt.setLong(2, heartTime);
			pstmt.setInt(3, status);
			pstmt.setInt(4, gsm);
			pstmt.setLong(5,imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateGps(long, double, double)
	 */
	public boolean updateGps(long imei, double gLat, double gLng) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set g_time=?,g_lat=?,g_lng = ?  WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, TimeUtil.getCurrentLongTime());
			pstmt.setDouble(2, gLat);
			pstmt.setDouble(3,gLng);
			pstmt.setLong(4, imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateBikeInfo(long, int, int)
	 */
	public boolean updateBikeInfo(long imei, int power, int gsm,int lockStatus) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set power=?,gsm=?,status = ?  WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, power);
			pstmt.setInt(2, gsm);
			pstmt.setInt(3, lockStatus);
			pstmt.setLong(4, imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	public boolean updateBikeDetailInfo(BikeVo bikeVo){
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set status = ?,city_id = ?,error_status = ?,type_id = ?,bike_type = ?,number= ?,imei = ?  WHERE id= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, bikeVo.getStatus());
			pstmt.setInt(2, bikeVo.getCityId());
			pstmt.setInt(3, bikeVo.getError_status());
			pstmt.setString(4, bikeVo.getTypeId());
			pstmt.setInt(5, bikeVo.getBikeType());
			pstmt.setString(6, bikeVo.getNumber());
			pstmt.setString(7, bikeVo.getImei());
			pstmt.setString(8, bikeVo.getBid());

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateRedPackageBike(java.lang.String, int)
	 */
	public boolean updateRedPackageBike(String bid, int redpack) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set readpack=? WHERE id= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, redpack);
			pstmt.setString(2, bid);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateBikeVersion(long, java.lang.String, java.lang.String)
	 */
	public boolean updateBikeVersion(long imei, String version,String deviceType,String buildTime) {
		// TODO Auto-generated method stub
		if(deviceType != null){
			version = deviceType+"_V"+version;
		}
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set version=?,version_time=?  WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, version);
			pstmt.setString(2, buildTime);
			pstmt.setLong(3, imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	public boolean updateLockStatus(String number,int status){
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set status=?,use_status = ? WHERE number= ? ";


		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			if(status == -1){
				//越界上锁/transboundary lock
				pstmt.setInt(1, 0);
				pstmt.setInt(2, 1);//将继续计费，标记为使用状态/go on caculate fee, sign status
			}else{
				pstmt.setInt(1, status);
				pstmt.setInt(2, status==1?1:0);
			}

			pstmt.setString(3, number);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateLockPower(java.lang.String)
	 */
	@Override
	public boolean updateLockPower(String number,int power) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set power=? WHERE number= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, power);
			pstmt.setString(2, number);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateLockPower(long, int)
	 */
	@Override
	public boolean updateLockPower(long imei, int power) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set power=? WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, power);
			pstmt.setLong(2, imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateBikeErrorStatus(java.lang.String, int)
	 */
	public boolean updateBikeErrorStatus(String number, int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set error_status=? WHERE number= ? and error_status != ?";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setString(2, number);
			pstmt.setInt(3, status);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateBikeErrorStatusWithImei(long, int)
	 */
	public boolean updateBikeErrorStatusWithImei(long imei, int newStatus) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set error_status=? WHERE imei= ? and error_status != ?";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, newStatus);
			pstmt.setLong(2, imei);
			pstmt.setInt(3, newStatus);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateBikeErrorStatusWithImei(java.lang.String, int, int)
	 */
	public boolean updateBikeErrorStatusWithImei(long imei, int oldStatus,
			int newStatus) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set error_status=? WHERE imei= ? and error_status = ?";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, newStatus);
			pstmt.setLong(2, imei);
			pstmt.setInt(3, oldStatus);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#autoCancelBikeError(java.lang.String)
	 */
	public boolean autoCancelBikeError(String number) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean updateLockStatusWithImei(long imei,int status,boolean updateUseStatus){
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set status=?";

		if(updateUseStatus){
			sql += " ,use_status = ?";
		}
		sql += "  WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, status);
			if(updateUseStatus){
				pstmt.setInt(2, status);
				pstmt.setLong(3, imei);
			}else{
				pstmt.setLong(2, imei);
			}


			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateBikeStatusWithImei(long, int)
	 */
	@Override
	public boolean updateBikeStatusWithImei(long imei, int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set bike_status=? WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, status);
			pstmt.setLong(2, imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateBikeServerIp(long, java.lang.String)
	 */
	@Override
	public boolean updateBikeServerIp(long imei, String serverIp,short power) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set server_ip = ?,heart_time = UNIX_TIMESTAMP(),power = ? WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, serverIp);
			pstmt.setShort(2, power);
			pstmt.setLong(3, imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#updateExtendInfo(long, com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public boolean updateExtendInfo(long imei, JSONObject data) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike set extend_info = ? WHERE imei= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, data.toJSONString());
			pstmt.setLong(2, imei);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<BikeVo> getBikeList(int type,int cityId,Boolean locationCheck) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_map_bike where 1=1 ";
		if(type == 1){//低电量，电压小于350 属于低电压/low battery,voltage less then 350 belong to low voltage
			sql += " and power < 350";
		}else if(type == 2){//解锁/unlock
			sql += " and status = 1";
		}else if(type == 3){//锁定/lock
			sql += " and status = 0";
		}
		if(cityId > 0){
			sql += " and city_id ="+cityId;
		}
		if(locationCheck){
			sql += " and g_lat is not null and g_lng is not null";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeVo>  list = new ArrayList<BikeVo>();
		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeVo bike = new BikeVo(rs);
				list.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getNormalBikeList()
	 */
	@Override
	public List<BikeVo> getNormalBikeList() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_bike where error_status = 0 ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeVo>  list = new ArrayList<BikeVo>();
		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeVo bike = new BikeVo(rs);
				list.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeList(com.pgt.bikelock.vo.RequestListVo)
	 */
	/**
	 * 
	 */
	public List<BikeVo> getBike(String id){
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_map_bike where id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeVo>  list = new ArrayList<BikeVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeVo bike = new BikeVo(rs);
				list.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#getBikeList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<BikeVo> getBikeList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_list_bike " +
				" where (number like '%"+requestVo.getKeyWords()
				+"%' or imei like '%"+requestVo.getKeyWords()+"%' "
				+"or mac like '%"+requestVo.getKeyWords()+"%' "
				+ "or version like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getStatus() == 3){//低电量，电压小于350 属于低电压/low battery
			sql += " and power < 350";
		}else if(requestVo.getStatus() == 1){//锁定/lock
			sql += " and status = 0";
		}else if(requestVo.getStatus() == 2){//解锁/unlock
			sql += " and status = 1";
		}else if(requestVo.getStatus() == 4){
			sql += " and bike_status = 2";
		}

		if(requestVo.getType() > 0){
			int type = requestVo.getType()-1;
			if(type == 0){//正常/normal
				sql += " and error_status = 0";
			}else if(type == 1){//故障/error
				sql += " and error_status = 1";
			}else if(type == 2){//自动故障/auto error
				sql += " and error_status = 2";
			}else if(type == 3){//报废/scrapped
				sql += " and error_status = 3";
			}else if(type == 4){//待激活/wait activate
				sql += " and error_status = 4";
			}
		}

		if(requestVo.getWay() ==1){
			//online
			sql += " and heart_time >= UNIX_TIMESTAMP() - "+BikeBo.BIKE_CONNECT_CHECK_SECONDS;
		}else if(requestVo.getWay() == 2){
			//offline
			sql += " and heart_time < UNIX_TIMESTAMP() - "+BikeBo.BIKE_CONNECT_CHECK_SECONDS;
		}

		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		if(requestVo.getExtendType1() > 0){
			sql += " and bike_type = "+requestVo.getExtendType1();
		}
		if(!StringUtils.isEmpty(requestVo.getTagIds())){
			sql += " and id in ("+requestVo.getTagIds()+")";
		}

		if(!StringUtils.isEmpty(requestVo.getOrderField()) && !StringUtils.isEmpty(requestVo.getOrderDirection())){
			sql += " order by "+requestVo.getOrderField()+" "+requestVo.getOrderDirection()+" LIMIT ?,?";
		}else{
			sql += " order by add_date desc LIMIT ?,?";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeVo>  list = new ArrayList<BikeVo>();

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			long nowTime = TimeUtil.getCurrentLongTime();
			while(rs.next()){
				BikeVo bike = new BikeVo(rs);
				if(ValueUtil.getLong(bike.getHeartTime()) >= nowTime-BikeBo.BIKE_CONNECT_CHECK_SECONDS){
					bike.setStatusStr(bike.getStatusStr().concat("[online]"));
				}else{
					bike.setStatusStr(bike.getStatusStr().concat("[offline]"));
				}
				bike.setgTime(TimeUtil.formaStrDate(bike.getgTime(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bike.setHeartTime(TimeUtil.formaStrDate(bike.getHeartTime(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				bike.setBikeTypeStr(LanguageUtil.getTypeStr("bike_lock_Type_values", bike.getBikeType()));
				bike.setMac(BaseDao.getString(rs, "mac"));
				list.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#getCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from view_list_bike " +
				" where (number like '%"+requestVo.getKeyWords()
				+"%' or imei like '%"+requestVo.getKeyWords()+"%' "
				+" or mac like '%"+requestVo.getKeyWords()+"%' "
				+ "or version like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getStatus() == 3){//低电量，电压小于350 属于低电压/low battery
			sql += " and power < 350";
		}else if(requestVo.getStatus() == 1){//锁定/lock
			sql += " and status = 0";
		}else if(requestVo.getStatus() == 2){//解锁/unlock
			sql += " and status = 1";
		}else if(requestVo.getStatus() == 4){
			sql += " and bike_status = 2";
		}

		if(requestVo.getType() > 0){
			int type = requestVo.getType()-1;
			if(type == 0){//正常/normal
				sql += " and error_status = 0";
			}else if(type == 1){//故障/error
				sql += " and error_status = 1";
			}else if(type == 2){//自动故障/auto error
				sql += " and error_status = 2";
			}else if(type == 3){//报废/scrapped
				sql += " and error_status = 3";
			}else if(type == 4){//待激活/wait activate
				sql += " and error_status = 4";
			}
		}

		if(requestVo.getWay() ==1){
			//online
			sql += " and heart_time >= UNIX_TIMESTAMP() - "+BikeBo.BIKE_CONNECT_CHECK_SECONDS;
		}else if(requestVo.getWay() == 2){
			//offline
			sql += " and heart_time < UNIX_TIMESTAMP() - "+BikeBo.BIKE_CONNECT_CHECK_SECONDS;
		}


		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		if(requestVo.getExtendType1() > 0){
			sql += " and bike_type = "+requestVo.getExtendType1();
		}
		if(!StringUtils.isEmpty(requestVo.getTagIds())){
			sql += " and id in ("+requestVo.getTagIds()+")";
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
	 * @see com.pgt.bikelock.dao.IBikeDao#addBike(com.pgt.bikelock.vo.BikeVo)
	 */
	public String addBike(BikeVo bike) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_bike (number,imei,type_id,city_id,add_date,bike_type) values (?,?,?,?,now(),?) ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String bikeId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, bike.getNumber());
			pstmt.setString(2,bike.getImei());
			pstmt.setString(3, bike.getTypeId());
			pstmt.setInt(4, bike.getCityId());
			pstmt.setInt(5, bike.getBikeType());

			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					bikeId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return bikeId;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeDao#deleteBike(java.lang.String)
	 */
	public boolean deleteBike(String bikeId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_bike where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, bikeId);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}



	public int updateBikeUseStatus (String bikeId, int useStatus) {

		if (bikeId == null) {
			return -1;
		}

		String sql = "UPDATE t_bike SET use_status = ? WHERE id = ? ";


		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setInt(1, useStatus);
			statement.setString(2, bikeId);

			return statement.executeUpdate();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			DataSourceUtil.close(statement, connection);
		}

		return -1;  

	}




}
