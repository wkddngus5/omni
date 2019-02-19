package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.admin.OrderRecord;

 

public interface IOrderRecord {
	String TABLE_NAME ="t_bike_order_record";
	String COLUMN_IMEI ="imei";
	String COLUMN_TIME ="time";
	String COLUMN_CONTENT ="content";
	String COLUMN_ORDER_ID ="order_id";
	boolean insert(long imei,int orderId,long time,String content);
	
	OrderRecord findLastByImei(long imei,int orderId);

}
