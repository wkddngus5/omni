package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.SystemConfigBo;
import com.pgt.bikelock.dao.IAreaDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.AreaVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;

public class AreaDaoImpl implements IAreaDao {

	public List<AreaVo> getAreaList(RequestListVo requestVo) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_area  where 1=1 ";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (name like '%"+requestVo.getKeyWords()+"%' or note like '%"+requestVo.getKeyWords()+"%')";
		}
		if(requestVo.getType() != 0){
			sql += " and type = "+requestVo.getType();
		}
		if(requestVo.getCityId() != 0){
			sql += " and city_id = "+requestVo.getCityId();
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AreaVo>  list = new ArrayList<AreaVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				AreaVo area = new AreaVo(rs);
				list.add(area);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAreaDao#getAreaList(int, com.pgt.bikelock.vo.LatLng)
	 */
	@Override
	public List<AreaVo> getAreaList(int cityId, LatLng position,int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT *,ABS(lat-?) as dis_lat,ABS(lng-?) as dis_lng FROM `t_area` where city_id = ? and type = ? ORDER BY dis_lat,dis_lng asc" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AreaVo>  list = new ArrayList<AreaVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setDouble(1, position.getLatitude());
			pstmt.setDouble(2, position.getLongitude());
			pstmt.setInt(3, cityId);
			pstmt.setInt(4, type);
			rs = pstmt.executeQuery();
			while(rs.next()){
				AreaVo area = new AreaVo(rs);
				list.add(area);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAreaDao#getAreaList()
	 */
	public List<AreaVo> getAreaList(int cityId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_area where city_id = ?" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AreaVo>  list = new ArrayList<AreaVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cityId);
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				AreaVo area = new AreaVo(rs);
				list.add(area);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	public List<AreaVo> getAreaList(String ids,double lng,double lat,int cityId){
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_area where 1=1";
		if(lng > 0 || lat > 0){
			sql += " and abs(lat-?) < "+SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SURROUND+" and abs(lng-?) <  "+SystemConfigBo.SYSTEM_CONFIG_NEAR_BIKE_SURROUND;
		}
		if(!StringUtils.isEmpty(ids)){
			sql += " and id in ("+ids+")";
		}
		if(cityId > 0){
			sql += " and city_id = "+cityId;
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<AreaVo>  list = new ArrayList<AreaVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			if(lng > 0 || lat > 0){
				pstmt.setDouble(1, lat);
				pstmt.setDouble(2, lng);
			}

			rs = pstmt.executeQuery();
			while(rs.next()){
				AreaVo area = new AreaVo(rs);
				list.add(area);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	public AreaVo findById(int id) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_area  where id= ?" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AreaVo area = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,id);
			 
			rs = pstmt.executeQuery();
			while(rs.next()){
				area = new AreaVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return area; 
	}
	
	public String addArea(AreaVo area) {
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT "+TABLE_NAME+" (note,name,detail,lat,lng,city_id,type) values (?,?,?,?,?,?,?) ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String areaId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, area.getNote());
			pstmt.setString(2,area.getName());
			pstmt.setString(3, area.getDetail());
			pstmt.setDouble(4, area.getLat());
			pstmt.setDouble(5, area.getLng());
			pstmt.setInt(6, area.getCity_id());
			pstmt.setInt(7, area.getType());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					areaId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return areaId;  
	} 
	

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAreaDao#updateArea(com.pgt.bikelock.vo.AreaVo)
	 */
	public boolean updateArea(AreaVo areaVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_area set name = ?,note = ?, detail= ?,lat = ?,lng = ?,city_id = ?,type = ? where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, areaVo.getName());
			pstmt.setString(2,areaVo.getNote());
			pstmt.setString(3, areaVo.getDetail());
			pstmt.setDouble(4, areaVo.getLat());
			pstmt.setDouble(5, areaVo.getLng());
			pstmt.setInt(6, areaVo.getCity_id());
			pstmt.setInt(7, areaVo.getType());
			pstmt.setInt(8, areaVo.getId());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

}
