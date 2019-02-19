package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

public class AreaVo {
	private int id;
	private String name;
	private String detail;
	private double lat;
	private double lng;
	private String note;
	private int type;//区域类型 1：停车区域 2：禁停区域 3:强制停车区域
	
	public static String[] parkingTypeIcon = {"parking","prohibited_parking","forced_parking"};
	
	private int city_id;
	private String date;
	
	/**show property**/
	private String typeStr;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
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
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the city_id
	 */
	public int getCity_id() {
		return city_id;
	}
	/**
	 * @param city_id the city_id to set
	 */
	public void setCity_id(int city_id) {
		this.city_id = city_id;
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
	 * @return the typeStr
	 */
	public String getTypeStr() {
		return typeStr;
	}
	/**
	 * @param typeStr the typeStr to set
	 */
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public AreaVo() {
		super();
	}
	
	public AreaVo(String note,String name, String detail) {
		 
		this.name = name;
		this.detail = detail;
		this.note = note;
	}
	
	public AreaVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst, "id");
		this.name = BaseDao.getString(rst, "name");
		this.note = BaseDao.getString(rst, "note");
		this.detail = BaseDao.getString(rst, "detail");
		this.lat = BaseDao.getDouble(rst, "lat");
		this.lng = BaseDao.getDouble(rst, "lng");
		this.type = BaseDao.getInt(rst, "type");
		setTypeStr(LanguageUtil.getTypeStr("bike_area_type", type));
		this.city_id = BaseDao.getInt(rst, "city_id");
		this.date = BaseDao.getString(rst, "date");
	}
	
	public AreaVo(String[] parms,HttpServletRequest req){
		this.note = req.getParameter("note");
		this.name= req.getParameter("name");
		this.city_id = ValueUtil.getInt(req.getParameter("cityId"));
		if(this.city_id == 0){
			this.city_id = ValueUtil.getInt(req.getAttribute("cityId"));
		}
		this.type = ValueUtil.getInt(req.getParameter("type"));
		this.detail = req.getParameter("detail");
		this.id = ValueUtil.getInt(req.getParameter("id"));
		//TODO 计算出多边形中心点//calculated the center of the polygon
		List<LatLng> list = AMapUtil.decode(detail);
		LatLng centerLL = list.get(0);// 目前拿第一个点当多边形的中心点//make the first point as the center of the polygon
		this.lat = centerLL.latitude;
		this.lng  = centerLL.longitude;
		
	}
	@Override
	public String toString() {
		return "AreaVo [id=" + id + ", name=" + name + ", detail=" + detail
				+ ", note=" + note + "]";
	}
	public List<LatLng> getLatLngs(){
		List<LatLng> list =null ;
		list=AMapUtil.decode(detail);
		return list;
	}

}
