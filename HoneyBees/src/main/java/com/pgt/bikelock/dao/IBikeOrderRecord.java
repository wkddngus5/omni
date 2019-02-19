package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.BikeOrderRecord;
import com.pgt.bikelock.vo.admin.StatisticsVo;

 

public interface IBikeOrderRecord {
	String TABLE_NAME ="t_bike_order_record";
	String COLUMN_IMEI ="imei";
	String COLUMN_TIME ="time";
	String COLUMN_CONTENT ="content";
	String COLUMN_ORDER_ID ="order_id";
	boolean insert(long imei,int orderId,long time,String content);
	
	
	BikeOrderRecord findLastByImei(long imei,int orderId);


	/**
	 * 
	 * @Title:        haveRecordInMinutes 
	 * @Description:  判定设备在时间范围内有无指令上传/judge device whether upload command in time
	 * @param:        @param seconds
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月27日 下午2:32:38
	 */
	boolean haveRecordInMinutes(int seconds,long imei);
	
	/**
	 * 
	 * @Title:        getStatisticsList 
	 * @Description:  报表统计/datasheet caculate
	 * @param:        @param dateType
	 * @param:        @param dataType
	 * @param:        @return    
	 * @return:       List<StatisticsVo>    
	 * @author        Albert
	 * @Date          2017年7月5日 下午3:12:54
	 */
	List<StatisticsVo> getStatisticsList(String imei,int dateType,int dataType);
	
	/**
	 * 
	 * @Title:        getOrderRecord 
	 * @Description:  获取指令列表/obtain command list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<BikeOrderRecord>    
	 * @author        Albert
	 * @Date          2017年9月2日 下午3:48:54
	 */
	List<BikeOrderRecord> getOrderRecord(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getLastOrderRecord 
	 * @Description:  TODO
	 * @param:        @param type
	 * @param:        @return    
	 * @return:       List<BikeOrderRecord>    
	 * @author        Albert
	 * @Date          2017年11月30日 下午1:48:19
	 */
	List<BikeOrderRecord> getLastOrderRecord(int type);
	
	/**
	 * 
	 * @Title:        getWarnOrderRecordGroup 
	 * @Description:  获取报警分组列表/obtain alarm group divide list
	 * @param:        @return    
	 * @return:       List<BikeOrderRecord>    
	 * @author        Albert
	 * @Date          2017年9月14日 下午5:21:11
	 */
	List<BikeOrderRecord> getWarnOrderRecordGroup();
	
	/**
	 * 
	 * @Title:        getOrderRecordCount 
	 * @Description:  获取指令总数/obtain command total amount
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年9月2日 下午3:57:38
	 */
	int getOrderRecordCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getAlarmLocation 
	 * @Description:  get Alarm Location
	 * @param:        @param imei
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       LatLng    
	 * @author        Albert
	 * @Date          2017年10月9日 下午2:21:52
	 */
	LatLng getAlarmLocation(long imei,String id);
	
}
