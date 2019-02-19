/**
 * FileName:     BikeErrorDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 上午11:46:12
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/intialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pgt.bikelock.dao.IBikeErrorDao;
import com.pgt.bikelock.dao.IImageDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.BikeErrorVo;
import com.pgt.bikelock.vo.RequestListVo;

/**
 * @ClassName:     BikeErrorDaoImpl
 * @Description:单车故障接口实现/bike error protocol achieve
 * @author:    Albert
 * @date:        2017年4月11日 上午11:46:12
 *
 */
public class BikeErrorDaoImpl implements IBikeErrorDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#addBikeError(com.pgt.bikelock.vo.BikeErrorVo)
	 */
	public boolean addBikeError(BikeErrorVo errorVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_bike_error (bnumber,uid,type,error_type,content,image_ids,date,status,bike_useid"
				+ ",lat,lng) values (?,?,?,?,?,?,now(),?,?,?,?) ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, errorVo.getBnumber());
			pstmt.setString(2, errorVo.getUid());
			pstmt.setInt(3, errorVo.getType());
			pstmt.setString(4,errorVo.getError_type());
			pstmt.setString(5, errorVo.getContent());
			pstmt.setString(6, errorVo.getImage_ids());
			pstmt.setInt(7, errorVo.getStatus());
			pstmt.setString(8, errorVo.getBike_useid());
			pstmt.setDouble(9, errorVo.getLat());
			pstmt.setDouble(10, errorVo.getLng());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#getBikeErrorList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<BikeErrorVo> getBikeErrorList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.*,u.phone,bk.id as bid from t_bike_error b,t_user u,t_bike bk where b.uid = u.id and b.bnumber = bk.number"
				+ " and (bnumber like '%"+requestVo.getKeyWords()+"%' or"
						+ " phone like '%"+requestVo.getKeyWords()+"%' or"
						+ " b.id like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getType() != 0){
			sql += " and b.type = " +requestVo.getType();
		}
		if(requestVo.getStartTime() != null){
			sql += " and b.date >= '" +requestVo.getStartTime()+"'";
		}
		if(requestVo.getEndTime() != null){
			sql += " and b.date <= '" +requestVo.getEndTime()+"'";
		}
		if(requestVo.getStatus() != 0){
			sql += " and b.status = '" +(requestVo.getStatus()-1)+"'";
		}
		
		if(requestVo.getCityId() > 0){
			sql += " and bk.city_id = "+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getTagIds())){
			sql += " and b.id in ("+requestVo.getTagIds()+")";
		}

		sql += " order by b.id "+requestVo.getOrderDirection()+" LIMIT ?,?" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeErrorVo>  list = new ArrayList<BikeErrorVo>();

		IImageDao imageDao = new ImageDaoImpl();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeErrorVo errorVo = new BikeErrorVo(rs);
				//设置图片集合/set up photo album
				if(!StringUtils.isEmpty(errorVo.getImage_ids())){
					errorVo.setListImage(imageDao.getImages(errorVo.getImage_ids(), conn));
				}
				list.add(errorVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#getBikeErrorList(java.lang.String, int)
	 */
	@Override
	public List<BikeErrorVo> getBikeErrorList(String userId, int startPage) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_bike_error where uid = ? LIMIT ?,?";
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeErrorVo>  list = new ArrayList<BikeErrorVo>();

		IImageDao imageDao = new ImageDaoImpl();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, startPage);
			pstmt.setInt(3, BaseDao.pageSize);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeErrorVo errorVo = new BikeErrorVo(rs);
				//设置图片集合/set up photo album
				if(errorVo.getType() > 1 && !StringUtils.isEmpty(errorVo.getImage_ids())){
					errorVo.setListImage(imageDao.getImages(errorVo.getImage_ids(), conn));
				}
				list.add(errorVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#getBikeError(java.lang.String)
	 */
	public BikeErrorVo getBikeError(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT b.*,u.phone from t_bike_error b,t_user u where b.uid = u.id and b.id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BikeErrorVo errorVo = null;

		IImageDao imageDao = new ImageDaoImpl();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				errorVo = new BikeErrorVo(rs);
				//设置图片集合/set up photo album
				if(errorVo.getType() > 1 && !StringUtils.isEmpty(errorVo.getImage_ids())){
					errorVo.setListImage(imageDao.getImages(errorVo.getImage_ids(), conn));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return errorVo; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#getCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from t_bike_error b,t_user u,t_bike bk where b.uid = u.id and b.bnumber = bk.number"
				+ " and (bnumber like '%"+requestVo.getKeyWords()+"%' or"
						+ " phone like '%"+requestVo.getKeyWords()+"%' or"
						+ " b.id like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getType() != 0){
			sql += " and b.type = " +requestVo.getType();
		}
		if(requestVo.getStartTime() != null){
			sql += " and b.date >= '" +requestVo.getStartTime()+"'";
		}
		if(requestVo.getEndTime() != null){
			sql += " and b.date <= '" +requestVo.getEndTime()+"'";
		}
		if(requestVo.getStatus() != 0){
			sql += " and b.status = '" +(requestVo.getStatus()-1)+"'";
		}
		
		if(requestVo.getCityId() > 0){
			sql += " and bk.city_id = "+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getTagIds())){
			sql += " and b.id in ("+requestVo.getTagIds()+")";
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
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#getWaitForDealCount(java.lang.String)
	 */
	public List<String> getWaitForDealCount(String number,boolean groupByUser) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT id from t_bike_error where bnumber = ? and status = 0 ";
		if(groupByUser){
			sql += " GROUP BY uid";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<String> idList = new ArrayList<String>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, number);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				idList.add(rs.getString("id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return idList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#updateBikeErrorStatus(int)
	 */
	public boolean updateBikeErrorStatus(BikeErrorVo errorVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_error set status=?,bike_useid=?,review_note=?,review_date=now()  WHERE id= ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, errorVo.getStatus());
			pstmt.setString(2, errorVo.getBike_useid());
			pstmt.setString(3, errorVo.getReview_note());
			pstmt.setString(4, errorVo.getId());

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#batchDealReport(java.lang.String, int)
	 */
	public boolean batchDealReport(String ids, int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_bike_error set status=?,review_date=now()  WHERE id in ("+ids+") ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,status);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#deleteBikeError(java.lang.String)
	 */
	public boolean deleteBikeError(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from t_bike_error where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeErrorDao#checkErrorIsWaitForApply(java.lang.String, java.lang.String)
	 */
	public boolean checkErrorIsWaitForApply(String uid, String bnumber,int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from t_bike_error where uid = ? and bnumber = ? and type = ? and status < 2";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,uid);
			pstmt.setString(2,bnumber);
			pstmt.setInt(3, type);
			rs = pstmt.executeQuery();
			if(rs.next()){
				flag = rs.getInt("num") > 0 ? true:false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}



}
