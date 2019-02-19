/**
 * FileName:     ImageDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 下午3:32:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IImageDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.ImageVo;

/**
 * @ClassName:     ImageDaoImpl
 * @Description:图片接口实现类/picture protocol achieve
 * @author:    Albert
 * @date:        2017年4月11日 下午3:32:38
 *
 */
public class ImageDaoImpl implements IImageDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IImageDao#addImages(java.lang.String[])
	 */
	public String addImages(List<ImageVo> list) {
		// TODO Auto-generated method stub
		String ids = "";

		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_image (path,width,height) values (?,?,?) ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < list.size(); i++) {
				ImageVo imageVo = list.get(i);
				pstmt.setString(1,imageVo.getPath());
				pstmt.setDouble(2, imageVo.getWidth());
				pstmt.setDouble(3, imageVo.getHeight());

				if(pstmt.executeUpdate()>0){
					rs= pstmt.getGeneratedKeys();
					if(rs.next()){
						if(i != list.size()-1){
							ids += rs.getString(1)+",";
						}else{
							ids += rs.getString(1);
						}
						
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return ids;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IImageDao#getImages(java.lang.String)
	 */
	public List<ImageVo> getImages(String ids,Connection conn) {
		// TODO Auto-generated method stub
		String sql="SELECT * from t_image where id in ("+ids+")";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<ImageVo>  list = new ArrayList<ImageVo>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ImageVo bike = new ImageVo(rs);
				list.add(bike);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IImageDao#getImage(java.lang.String)
	 */
	public ImageVo getImage(String id) {
		// TODO Auto-generated method stub
		String sql="SELECT * from t_image where id = ?";
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ImageVo  imageVo = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while(rs.next()){
				imageVo = new ImageVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return imageVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IImageDao#deleteImages(java.lang.String)
	 */
	public boolean deleteImages(String ids) {
		// TODO Auto-generated method stub
		String sql="delete from t_image where id in ("+ids+")";
		Connection conn = DataSourceUtil.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);
			if(pstmt.executeUpdate() > 0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		
		return flag;
	}

}
