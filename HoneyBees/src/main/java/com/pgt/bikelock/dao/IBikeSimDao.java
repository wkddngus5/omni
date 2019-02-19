package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.BikeSim;

 

public interface IBikeSimDao {
	String TABLE_NAME="t_bike_sim";
	 
	String COLUMN_ID="id";
	String COLUMN_IMEI= "imei";
	String COLUMN_ICCID="iccid";
	
	
	boolean insert(long imei,String iccid);
	boolean deleteByImei(long imei);
	boolean deleteById(int id);
	boolean update(long imei,String iccid);
	
	
	BikeSim find(long imei );

	
	/**
	 * 获取锁电话卡信息/obtain sim card information
	 * @Title:        getBikePhoneInfo 
	 * @Description:  TODO
	 * @param:        @param number
	 * @param:        @return    
	 * @return:       BikeSim    
	 * @author        Albert
	 * @Date          2017年6月16日 下午5:11:21
	 */
	BikeSim getBikePhoneInfo(String number);
}
