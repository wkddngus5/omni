package com.pgt.bike.lock.lib.entity;
/**
 * 指令实体
 * @author cxiaox
 * time 2017年1月6日
 */
public class Command {
	/** 发送的头指令 */
	public final static String SEND_HEAD="*CMDS";
	/** 滑板车 头指令*/
	public final static String SEND_BICMAN_HEAD="*HBCS";
	/**
	 * 围栏锁
	 */
	public final static String SEND_BGF_HEAD="*BGFS";
	
	public final static String CODE_OM="OM";
	public final static String CODE_BICMAN="NB";
	public final static String CODE_GM="GM";
	public static String CODE =CODE_OM;
	/**上锁指令*/
	public final static String ORDER_LOCK="L1";
	/**上锁指令 代码*/
	public final static int ORDER_LOCK_CODE=0x4C31;
	/**设置锁状态 指令 ,状态代码：0为开锁,1为上锁*/
	public final static String ORDER_SET_LOCK="L0";
	/**设置锁状态 代码*/
	public final static int ORDER_SET_LOCK_CODE=0x4C30;
	
	
	/**设置锁状态 指令 ,状态代码：0为开锁,1为上锁*/
	public final static String ORDER_BATTERY_POWER="L3";
	/**设置锁状态 代码*/
	public final static int ORDER_BATTERY_LOCK_CODE=0x4C33;
	
	
	
	
	
	/**网络签到*/
	public final static String ORDER_REPORT="Q0";
	/**网络签到 代码*/
	public final static int ORDER_REPORT_CODE=0x5130;
	/**心跳包*/
	public final static String ORDER_HEART="H0";
	/**心跳包 代码*/
	public final static int ORDER_HEART_CODE=0x4830;
	/**设置心跳包 间隔*/
	public final static String ORDER_BGF_HEART_INTERVAL="H1";
	/**心跳包间隔 代码*/
	public final static int ORDER_BGF_HEART_INTERVAL_CODE=0x4831;
	/**定位*/
	public final static String ORDER_LOCATION="D0";
	/**定位 代码*/
	public final static int ORDER_LOCATION_CODE=0x4430;
	
	/**定位*/
	public final static String ORDER_TRACE="D1";
	/**定位 代码*/
	public final static int ORDER_TRACE_CODE=0x4431;
	
	/**定位 间隔*/
	public final static String ORDER_LOCATION_INTERVAL="D3";
	/**定位 代码*/
	public final static int ORDER_LOCATION_INTERVAL_CODE=0x4433;
	
	/**睡眠关机*/
	public final static String ORDER_SLEEP="S0";
	/**睡眠关机 代码*/
	public final static int ORDER_SLEEP_CODE=0x5330;
	
	/**重启*/
	public final static String ORDER_RESTART="S1";
	/**重启 代码*/
	public final static int ORDER_RESTART_CODE=0x5331;
	
	/**设置重连服务器地址*/
	public final static String ORDER_BICMAN_SHUTDOWN="S2";
	/**设置重连服务器地址 代码*/
	public final static int ORDER_BICMAN_SHUTDOWN_CODE=0x5332;
	
	/**设置重连服务器地址*/
	public final static String ORDER_SET_ADDRESS="S3";
	/**设置重连服务器地址 代码*/
	public final static int ORDER_SET_ADDRESS_CODE=0x5333;
	
	/**获取锁信息*/
	public final static String ORDER_INFO="S5";
	/**获取锁信息 代码*/
	public final static int ORDER_INFO_CODE=0x5335;
	
	/**设置 IOT信息*/
	public final static String ORDER_IOT_SET_INFO="S5";
	/**设置 IOT信息 代码*/
	public final static int ORDER_IOT_SET_INFO_CODE=0x5335;
	
	/**获取滑板车信息*/
	public final static String ORDER_BICMAN_INFO="S6";
	/**获取滑板车信息 代码*/
	public final static int ORDER_BICMAN_INFO_CODE=0x5336;
	
	/** 设置滑板车配置信息*/
	public final static String ORDER_BICMAN_CONFIG="S7";
	/**设置滑板车配置 代码*/
	public final static int ORDER_BICMAN_CONFIG_CODE=0x5337;
	
	/**设置SIM 连接信息*/
	public final static String ORDER_SIM_SET="S9";
	/**设置SIM 连接信息 代码*/
	public final static int ORDER_SIM_SET_CODE=0x5339;
	
	/**找车*/
	public final static String ORDER_FIND_BIKE="S8";
	/**找车 代码*/
	public final static int ORDER_FIND_BIKE_CODE=0x5338;
	/**获取固件版本*/
	public final static String ORDER_VERSION="G0";
	/**获取固件版本 代码*/
	public final static int ORDER_VERSION_CODE=0x4730;
	
	/**操作请求*/
	public final static String ORDER_CONTROL_REQUEST="R0";
	/**操作 请求代码*/
	public final static int ORDER_CONTROL_REQUEST_CODE=0x5230;
	
	/**预约*/
	public final static String ORDER_RESERVE="Y0";
	/**预约 代码*/
	public final static int ORDER_RESERVE_CODE=0x5930;
	
	public final static String ORDER_ALERT="W0";
	public final static int ORDER_ALERT_CODE=0x5730;
	
	public final static String ORDER_DEVICE_KEY="K0";
	public final static int ORDER_DEVICE_KEY_CODE=0x4B30;
	
	// 获取ICCID卡信息
	public final static String ORDER_ICCID="I0";
	public final static int ORDER_ICCID_CODE=0x4930;
	
	/**获取MAC地址*/
	public final static String ORDER_MAC="M0";
	/**获取MAC地址 代码*/
	public final static int ORDER_MAC_CODE=0x4D30;
	
	/**围栏上锁*/
	public final static String ORDER_FENCE="F1";
	/**围栏上锁 代码*/
	public final static int ORDER_FENCE_CODE=0x4631;
	
	// 硬件发送当前 硬件版本
	public final static String ORDER_UPDATE_VERSION="U0";
	public final static int ORDER_UPDATE_VERSION_CODE=0x5530;
	
	// 硬件请求当前升级数据包
	public final static String ORDER_UPDATE="U1";
	public final static int ORDER_UPDATE_CODE=0x5531;
	
	// 硬件接受完 升级
	public final static String ORDER_UPDATE_OK="U2";
	public final static int ORDER_UPDATE_OK_CODE=0x5532;
	
	
	//控制器发送当前 控制器版本
	public final static String ORDER_CONTROLLER_VERSION="G1";
	public final static int ORDER_CONTROLLER_VERSION_CODE=0x4731;
		
	// 控制器请求当前升级数据包
	public final static String ORDER_CONTROLLER="G2";
	public final static int ORDER_CONTROLLER_CODE=0x4732;
		
	// 控制器接受完 升级
	public final static String ORDER_CONTROLLER_OK="G3";
	public final static int ORDER_CONTROLLER_OK_CODE=0x4733;
	
	/**二合一使用 修改 心跳间隔*/
	public final static String ORDER_HEART_SET="T0";
	/**二合一使用 修改心跳间隔 代码*/
	public final static int ORDER_HEART_SET_CODE=0x5430;
	
	/**请求 更新锁时间戳*/
	public final static String ORDER_LOCK_TIMESTAMP_REQUEST="T0";
	/**请求 更新锁时间戳 代码*/
	public final static int ORDER_LOCK_TIMESTAMP_REQUEST_CODE=0x5430;
	
	/**设置 锁时间戳*/
	public final static String ORDER_LOCK_TIMESTAMP_SET="T1";
	/**设置 锁时间戳 代码*/
	public final static int ORDER_LOCK_TIMESTAMP_SET_CODE=0x5431;
	
	/**设置 语音播报*/
	public final static String ORDER_VOICE="V0";
	/**设置 语音播报 代码*/
	public final static int ORDER_VOICE_CODE=0x5630;
	
	public final static int LOCK_ON=0;
	/**
	 * 无效的定义
	 *  @deprecated   replaced by <code>LOCK_ON_OUT</code>.
	 */
	@Deprecated
	public final static int LOCK_OFF=1;
	/**
	 * 围栏内开锁，即会清除上一次骑行数据的开锁类型
	 */
	public final static int LOCK_ON_CLEART=0;
	/**
	 * 围栏外开锁，即不会清除上一次骑行数据的开锁类型
	 */
	public final static int LOCK_ON_CONTINUE=1;
	
	public final static String ORDER_RETURN="Re";
	public final static String ORDER_RETURN_ON="L0";
	public final static String ORDER_RETURN_OFF="L1";
	public final static String ORDER_RETURN_FENCE="F1";
	public final static String ORDER_RETURN_LOCATION="D0";
	public final static String ORDER_RETURN_ALERT="W0";
	//WO
}
