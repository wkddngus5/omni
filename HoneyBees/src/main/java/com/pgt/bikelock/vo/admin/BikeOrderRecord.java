package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

public class BikeOrderRecord {
	
	public static int ORDER_SIGN_IN = 1;
	public static int ORDER_HEART = 2;
	public static int ORDER_UN_LOCK = 3;
	public static int ORDER_LOCK = 4;
	public static int ORDER_LOCATION = 5;
	public static int ORDER_INFO= 6;
	public static int ORDER_FIND= 7;
	public static int ORDER_VERSION= 8;
	public static int ORDER_IP= 9;
	public static int ORDER_ERROR=10;
	public static int ORDER_ALERT=11;
	public static int ORDER_HEART_PERIOD=12;
	public static int ORDER_GET_ICCID=13;
	public static int ORDER_SHUT_DOWN=14;
	public static int ORDER_START_UP=15;
	
	
	private long id;
	private String bid;
	private  long imei;
	private int orderId;
	private long time ;
	private String content;
	private int from;
	
	//显示属性//display attributes
	private String number;
	private String typeStr;
	private String date;
	int count;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}
	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}
	public long getImei() {
		return imei;
	}
	public void setImei(long imei) {
		this.imei = imei;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	/**
	 * @return the from
	 */
	public int getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(int from) {
		this.from = from;
	}
	
	public BikeOrderRecord(){
		
	}
	
	public BikeOrderRecord(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst,"id");
		this.bid = BaseDao.getString(rst, "bid");
		this.imei = BaseDao.getLong(rst,"imei");
		this.time = BaseDao.getLong(rst, "time");
		this.number = BaseDao.getString(rst, "number");
		int type = BaseDao.getInt(rst, "order_id");
		setTypeStr(LanguageUtil.getTypeStr("bike_order_record_type_value", type));
		this.content = BaseDao.getString(rst, "content");
		this.count = BaseDao.getInt(rst, "count");
//		this.from = BaseDao.getInt(rst, "from");
	}	
}
