/**
 * FileName:     NewsDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 下午8:47:36
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/intilization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.INewsDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.NewsVo;
import com.pgt.bikelock.vo.RequestListVo;

/**
 * @ClassName:     NewsDaoImpl
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月9日 下午8:47:36
 *
 */
public class NewsDaoImpl implements INewsDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INewsDao#getNewsList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<NewsVo> getNewsList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		List<NewsVo> list = new ArrayList<NewsVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT n.title,n.id,n.date,n.start_time,n.end_time,n.city_id,path,width,height FROM t_news n left JOIN t_image m on n.thumb=m.id " +
				" where (title like '%"+requestVo.getKeyWords()+"%' or content like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getCityId() > 0){
			sql += " and city_id = "+requestVo.getCityId();
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
				NewsVo newsVo = new NewsVo(rs);
				newsVo.setStart_time(TimeUtil.formaStrDate(newsVo.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				newsVo.setEnd_time(TimeUtil.formaStrDate(newsVo.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				list.add(newsVo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INewsDao#getNews(java.lang.String)
	 */
	public NewsVo getNews(String newsId) {
		// TODO Auto-generated method stub
		NewsVo newsVo = null;

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT n.*,path,width,height FROM t_news n left JOIN t_image m on n.thumb=m.id where n.id = ?" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,newsId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				newsVo = new NewsVo(rs);
				newsVo.setStart_time(TimeUtil.formaStrDate(newsVo.getStart_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				newsVo.setEnd_time(TimeUtil.formaStrDate(newsVo.getEnd_time(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return newsVo; 
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INewsDao#getTopNews()
	 */
	public NewsVo getTopNews(int cityId) {
		// TODO Auto-generated method stub
		NewsVo newsVo = null;

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT n.title,n.id,n.start_time,n.end_time,path,width,height FROM t_news n left JOIN t_image m on n.thumb=m.id "
				+ "where start_time <= UNIX_TIMESTAMP() and end_time >= UNIX_TIMESTAMP() and (city_id = 0 or city_id = ?) ORDER BY id desc LIMIT 0,1" ;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, cityId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				newsVo = new NewsVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return newsVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INewsDao#addNews(com.pgt.bikelock.vo.NewsVo)
	 */
	public boolean addNews(NewsVo newsVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_news (title,thumb,content,start_time,end_time,date,city_id) values (?,?,?,?,?,now(),?) ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, newsVo.getTitle());
			pstmt.setString(2,newsVo.getThumb());
			pstmt.setString(3, newsVo.getContent());
			pstmt.setString(4,newsVo.getStart_time());
			pstmt.setString(5, newsVo.getEnd_time());
			pstmt.setInt(6, newsVo.getCityId());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.INewsDao#updateNews(com.pgt.bikelock.vo.NewsVo)
	 */
	public boolean updateNews(NewsVo newsVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_news set title = ?,thumb = ?,content = ?,date = now(),"
				+ "start_time = ?,end_time = ?,city_id = ? where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, newsVo.getTitle());
			pstmt.setString(2,newsVo.getThumb());
			pstmt.setString(3, newsVo.getContent());
			pstmt.setString(4,newsVo.getStart_time());
			pstmt.setString(5, newsVo.getEnd_time());
			pstmt.setInt(6, newsVo.getCityId());
			pstmt.setString(7, newsVo.getId()); 

			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}





}
