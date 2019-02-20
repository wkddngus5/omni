package com.pgt.bike.lock.lib.up;

/**
 * 锁类型接口
 */
public interface DeviceType {
	/** 二合一 */
	int GPRS_GPS=1;
	/**  三合一 */
	int GPRS_GPS_BLE=2;
	/** 纯蓝牙  */
	int BLE=3;
	/** 骑车锁  */
	int CAR=4;
	
	int MODEL_GPRS_GPS_BLE=5;
	
	/**
	 * 已废弃，请使用{@link DeviceType#VALUE_GPRS_GPS_BLE}
	 */
	@Deprecated
	String GPRS_GPS_BLE_VALUE="A1";
	/**
	 * 已废弃，请使用{@link DeviceType#VALUE_GPRS_GPS}
	 */
	@Deprecated
	String GPRS_GPS_VALUE="A0";
	
	String VALUE_GPRS_GPS_BLE="A1";  // 三合一
	String VALUE_MODEL_GPRS_GPS_BLE="A2";  // 新模三合一
	String VALUE_GPRS_GPS="A0"; // 二合一
	String VALUE_CAR="C0"; // 汽车锁

}
