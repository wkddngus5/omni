package com.omni.scooter.entity;
/**
 * 指令实体
 * @author cxiaox
 * time 2018年8月8日
 */
public class Command {
	/** 发送的头指令 */
	public final static String SEND_HEAD="*HBCS";
	 
	
	private final static String CODE_OM="OM"; 
	public static String CODE =CODE_OM;
	
	/**网络签到*/
	public final static String ORDER_REPORT="Q0";
	/**网络签到 代码*/
	public final static int ORDER_REPORT_CODE=0x5130;
	/**心跳包*/
	public final static String ORDER_HEART="H0";
	/**心跳包 代码*/
	public final static int ORDER_HEART_CODE=0x4830;
	/**操作请求*/
	public final static String ORDER_CONTROL_REQUEST="R0";
	/**操作 请求代码*/
	public final static int ORDER_CONTROL_REQUEST_CODE=0x5230;
	/**设置锁状态 指令 ,状态代码：0为开锁,1为上锁*/
	public final static String ORDER_LOCK_OPEN="L0";
	/**设置锁状态 代码*/
	public final static int ORDER_LOCK_OPEN_CODE=0x4C30;
	/**上锁指令*/
	public final static String ORDER_LOCK_CLOSE="L1";
	/**上锁指令 代码*/
	public final static int ORDER_LOCK_CLOSE_CODE=0x4C31;
	
	
	/**解锁外部设备指令*/
	public final static String ORDER_CONTROL_EXTRENAL_DEVICE="L5";
	/**解锁外部设备 代码*/
	public final static int ORDER_CONTROL_EXTRENAL_DEVICE_CODE=0x4C35;
	
	/**rfid 开锁请求指令*/
	public final static String ORDER_RFID_REQUEST="C0";
	/**rfid 开锁请求 代码*/
	public final static int ORDER_RFID_REQUEST_CODE=0x4330;
	
	
	/**IOT设备设置指令*/
	public final static String ORDER_IOT_SET_INFO="S5";
	/**IOT设备设置指令 代码*/
	public final static int ORDER_IOT_SET_CODE=0x5335;
	/**获取滑板车信息1*/
	public final static String ORDER_SCOOTER_INFO="S6";
	/**获取滑板车信息1 代码*/
	public final static int ORDER_SCOOTER_INFO_CODE=0x5336;
	/**滑板车设置指令1*/
	public final static String ORDER_SCOOTER_SET1="S7";
	/**滑板车设置指令1 代码*/
	public final static int ORDER_SCOOTER_SET1_CODE=0x5337;
	/**获取滑板车信息2*/
	public final static String ORDER_SCOOTER_INFO2="S8";
	/**获取滑板车信息2 代码*/
	public final static int ORDER_SCOOTER_INFO2_CODE=0x5338;
	/**滑板车设置指令2*/
	public final static String ORDER_SCOOTER_SET2="S4";
	/**滑板车设置指令2 代码*/
	public final static int ORDER_SCOOTER_SET2_CODE=0x5334;
	/**报警指令*/
	public final static String ORDER_WARN="W0";
	/**报警指令代码*/
	public final static int ORDER_WARN_CODE=0x5730;
	/**设置 语音播报*/
	public final static String ORDER_VOICE="V0";
	/**设置 语音播报 代码*/
	public final static int ORDER_VOICE_CODE=0x5630;
	
	/**滑板车开关机指令*/
	public final static String ORDER_POWER_CONTROL="S2";
	/**滑板车开关机指令 代码*/
	public final static int ORDER_POWER_CONTROL_CODE=0x5332;
	
	/**定位*/
	public final static String ORDER_LOCATION="D0";
	/**定位 代码*/
	public final static int ORDER_LOCATION_CODE=0x4430;
	/**获取固件版本*/
	public final static String ORDER_VERSION="G0";
	/**获取固件版本 代码*/
	public final static int ORDER_VERSION_CODE=0x4730;
	/**控制器故障 */
	public final static String ORDER_ERROR="E0";
	/**获取控制器故障 代码*/
	public final static int ORDER_ERROR_CODE=0x4530;
	/**硬件发送当前 硬件版本 */
	public final static String ORDER_UPDATE_VERSION="U0";
	/**硬件发送当前 硬件版本  代码*/
	public final static int ORDER_UPDATE_VERSION_CODE=0x5530;
	/**硬件请求升级数据包  */
	public final static String ORDER_UPDATE="U1";
	/**硬件请求升级数据包  代码*/
	public final static int ORDER_UPDATE_CODE=0x5531;
	/**硬件升级完成   */
	public final static String ORDER_UPDATE_OK="U2";
	/**硬件升级完成  代码 */
	public final static int ORDER_UPDATE_OK_CODE=0x5532;
	/**蓝牙KEY 设置获取指令   */
	public final static String ORDER_DEVICE_KEY="K0";
	/**蓝牙KEY 设置获取指令   代码*/
	public final static int ORDER_DEVICE_KEY_CODE=0x4B30;
	
	
	
	/**设置心跳包 间隔*/
	public final static String ORDER_BGF_HEART_INTERVAL="H1";
	/**心跳包间隔 代码*/
	public final static int ORDER_BGF_HEART_INTERVAL_CODE=0x4831;
	//TODO D1指令未做解析
	/**追踪*/
	public final static String ORDER_TRACE="D1";
	/**追踪 代码*/
	public final static int ORDER_TRACE_CODE=0x4431;
	/**睡眠关机*/
	public final static String ORDER_SLEEP="S0";
	/**睡眠关机 代码*/
	public final static int ORDER_SLEEP_CODE=0x5330;
 
	/**设置重连服务器地址*/
	public final static String ORDER_SET_ADDRESS="S3";
	/**设置重连服务器地址 代码*/
	public final static int ORDER_SET_ADDRESS_CODE=0x5333;
	/**获取SIM卡 ICCID信息*/
	public final static String ORDER_ICCID="I0";
	/**获取SIM卡 ICCID信息 代码*/
	public final static int ORDER_ICCID_CODE=0x4930;
	
	
	
	
	/**设置SIM 连接信息*/
	public final static String ORDER_SIM_SET="S9";
	/**设置SIM 连接信息 代码*/
	public final static int ORDER_SIM_SET_CODE=0x5339;
 
	
	
	
	/**获取MAC地址*/
	public final static String ORDER_MAC="M0";
	/**获取MAC地址 代码*/
	public final static int ORDER_MAC_CODE=0x4D30;
	
 
	

	
	
	
	
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
	public final static String ORDER_RETURN_TRACK="D1";
	public final static String ORDER_RETURN_ALERT="W0";
	//WO
}
