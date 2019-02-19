/**
 * FileName:     ImageVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 下午3:18:14/3:18:14 pm, April 11, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

 /**
 * @ClassName:     ImageVo
 * @Description:图片实体/image entity
 * @author:    Albert
 * @date:        2017年4月11日 下午3:18:15/3:18:15 pm, April 11, 2017
 *
 */
public class ImageVo {
	String id;
	String path;
	double width;
	double height;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}
	/**
	 * @param width the width to set
	 */
	public void setWidth(double width) {
		this.width = width;
	}
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	public ImageVo(ResultSet rst) throws SQLException{
		this.path = rst.getString("path");
		this.width = rst.getDouble("width");
		this.height= rst.getDouble("height");
	}
	
	public ImageVo(String path,double width,double height){
		this.path = path;
		this.width = width;
		this.height = height;
	}
}
