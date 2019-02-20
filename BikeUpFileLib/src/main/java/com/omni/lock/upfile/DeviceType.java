package com.omni.lock.upfile;

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
	/** 汽车锁  */
	int CAR=4;
	/**新模  三合一 */
	int MODEL_GPRS_GPS_BLE=5;
	/**新模 二合一 */
	int MODEL_GPRS_GPS=6;
	/**旧模 围栏锁 */
	int A_FENCE_LOCK=7;
	/**新模 围栏锁 */
	int B_FENCE_LOCK=8;
	/**新模 3G锁 */
	int MODEL_3G=9;
	/**电力锁*/
	int ELECTRIC=10;
	/**方向盘锁(3in1)*/
	int CAR_3IN1=11;
	/**车位锁*/
	int CARPORT=12;
	/**
	 * 滑板车  IOT  OMNI
	 */
	int SCOOTER_IOT_OMNI=13;
	
	/**
	 * 滑板车 IOT   segway nineboot
	 */
	int SCOOTER_IOT_SN=18;
	
	
	/**
	 * 控制器 
	 */
	int CONTROL=14;
	
	/**
	 * 内置电池
	 */
	int BATTERY_22=15;
	
	/**
	 * 外置电池
	 */
	int BATTERY_23=16;
	
	/**
	 * 滑板车仪表
	 */
	int SCOOTER_DISPLAY=17;
	/**
	 * 3g 电动车
	 */
	int ELECTRIC_3G=19;
	
	/**
	 * 控制器 
	 */
	int CONTROL_XM=20;
	
	
	
	String VALUE_GPRS_GPS_BLE="A1"; // 旧模 3in1
	String VALUE_GPRS_GPS="A0";     // 旧模 2in1
	String VALUE_CAR="C0";
	String VALUE_CAR_3IN1="C1";
	String VALUE_MODEL_GPRS_GPS_BLE="A2";  // 新模三合一
	String VALUE_MODEL_GPRS_GPS="A3";  // 新模2合一
	
	/** 旧模  围栏锁 */
	String VALUE_A_FENCE_LOCK="F0";  // 旧模 围栏锁
	/** 新模  围栏锁 */
	String VALUE_B_FENCE_LOCK="F1";  // 新模 围栏锁
	/** 新模 3G */
	String VALUE_MODEL_3G="B1";  // 新模 3G
	/** 电力锁 */
	String VALUE_MODEL_ELECTRIC="E0";  // 电力锁
	/**3G 电动车 */
	String VALUE_ELECTRIC_3G="B3";   
	
	/** 车位锁 */
	String VALUE_CARPORT="80";  // 车位锁
	
	
	/** 滑板车omni IOT设备 */
	String VALUE_SCOOTER_IOT_OMNI="8A";  // 滑板车
	/** 滑板车 segway nineboot  IOT设备 */
	String VALUE_SCOOTER_IOT_SN="85";  // 滑板车
	
	/**滑板车  控制器 这个控制器是SN的控制 */
	String VALUE_CONTROL="20";  // 控制器
	/**滑板车  控制器 这个控制器是XM的控制 */
	String VALUE_CONTROL_XM="20";  // 控制器
	
	/** 内置电池 */
	String VALUE_BATTERY_22="22";  // 内置电池 
	/** 外置电池 */
	String VALUE_BATTERY_23="23";  // 外置电池
	String VALUE_SCOOTER_DISPLAY="21";  // 滑板车仪表

}
