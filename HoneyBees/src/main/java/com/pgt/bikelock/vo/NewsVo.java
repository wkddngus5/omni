/**
 * FileName:     NewsVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 下午8:42:37/8:42:37 pm, May 9, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initialzing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;


 /**
 * @ClassName:     NewsVo
 * @Description:资讯实体/news entity
 * @author:    Albert
 * @date:        2017年5月9日 下午8:42:37/8:42:37 pm, May 9, 2017
 *
 */
public class NewsVo {
	String id;
	String title;
	String thumb;
	ImageVo imageVo;
	String content;
	String date;
	String start_time;
	String end_time;
	int cityId;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the thumb
	 */
	public String getThumb() {
		return thumb;
	}
	/**
	 * @param thumb the thumb to set
	 */
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * @return the imageVo
	 */
	public ImageVo getImageVo() {
		return imageVo;
	}
	/**
	 * @param imageVo the imageVo to set
	 */
	public void setImageVo(ImageVo imageVo) {
		this.imageVo = imageVo;
	}
	
	/**
	 * @return the start_time
	 */
	public String getStart_time() {
		return start_time;
	}
	/**
	 * @param start_time the start_time to set
	 */
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	/**
	 * @return the end_time
	 */
	public String getEnd_time() {
		return end_time;
	}
	/**
	 * @param end_time the end_time to set
	 */
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	/**
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public NewsVo(){
		
	}
	
	public NewsVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.title = rst.getString("title");
		this.start_time = rst.getString("start_time");
		this.end_time = rst.getString("end_time");
		this.cityId = BaseDao.getInt(rst,"city_id");
		if(BaseDao.isExistColumn(rst, "thumb")){
			this.thumb = rst.getString("thumb");
		}
		if(BaseDao.isExistColumn(rst, "content")){
			this.content = rst.getString("content");
		}
		if(BaseDao.isExistColumn(rst, "date")){
			this.date = rst.getString("date");
		}

		if(BaseDao.isExistColumn(rst, "path")){
			this.imageVo = new ImageVo(rst);
		}
		
	}
	
	public NewsVo(HttpServletRequest req){ 
		this.id = (String) req.getAttribute("id");
		this.title = (String) req.getAttribute("title");
		this.thumb = (String) req.getAttribute("thumb");
		this.content = (String) req.getAttribute("content");
		this.cityId = ValueUtil.getInt(req.getAttribute("cityId"));
		this.start_time = TimeUtil.formateStrDateToLongStr((String) req.getAttribute("start"), TimeUtil.Formate_YYYY_MM_dd_HH_mm);;
		this.end_time = TimeUtil.formateStrDateToLongStr((String) req.getAttribute("end"), TimeUtil.Formate_YYYY_MM_dd_HH_mm);;
		
		
		
	}

}
