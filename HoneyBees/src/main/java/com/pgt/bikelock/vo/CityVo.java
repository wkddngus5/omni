/**
 * FileName:     CityVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月7日 下午5:02:19/5:02:19 pm, April 7, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月7日       CQCN         1.0             1.0
 * Why & What is modified: <初始化><initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     CityVo
 * @Description:城市实体/city entity
 * @author:    Albert
 * @date:        2017年4月7日 下午5:02:19/5:02:19 pm, April 7, 2017
 *
 */
public class CityVo {
	String id;
	String code;
	String name;
	String note;
	String area_detail;
	double area_lat;
	double area_lng;
	String currency;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the area_detail
	 */
	public String getArea_detail() {
		return area_detail;
	}
	/**
	 * @param area_detail the area_detail to set
	 */
	public void setArea_detail(String area_detail) {
		this.area_detail = area_detail;
	}
	/**
	 * @return the area_lat
	 */
	public double getArea_lat() {
		return area_lat;
	}
	/**
	 * @param area_lat the area_lat to set
	 */
	public void setArea_lat(double area_lat) {
		this.area_lat = area_lat;
	}
	/**
	 * @return the area_lng
	 */
	public double getArea_lng() {
		return area_lng;
	}
	/**
	 * @param area_lng the area_lng to set
	 */
	public void setArea_lng(double area_lng) {
		this.area_lng = area_lng;
	}
	
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public CityVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.code = BaseDao.getString(rst, "code");
		this.name = BaseDao.getString(rst, "name");
		this.note = BaseDao.getString(rst, "note");
		this.area_detail = BaseDao.getString(rst, "area_detail");
		this.area_lat = BaseDao.getDouble(rst, "area_lat");
		this.area_lng = BaseDao.getDouble(rst, "area_lng");
		this.currency = BaseDao.getString(rst, "currency");
	}
	
	public CityVo(HttpServletRequest req){
		this.id = req.getParameter("id");
		this.code = req.getParameter("code");
		this.name = req.getParameter("name");
		this.note = req.getParameter("note");
		this.area_detail = req.getParameter("detail");
		this.area_lat = ValueUtil.getDouble(req.getParameter("area_lat"));
		this.area_lng  = ValueUtil.getDouble(req.getParameter("area_lng"));
		//TODO 计算出多边形中心点/calculate the center of the polygon
//		if(!StringUtils.isEmpty(area_detail)){
//			List<LatLng> list = AMapUtil.decode(area_detail);
//			LatLng centerLL = list.get(0);// 目前拿第一个点当多边形的中心点/take the first point as the center of the polygon
//			this.area_lat = centerLL.latitude;
//			this.area_lng  = centerLL.longitude;
//		}

	}
}
