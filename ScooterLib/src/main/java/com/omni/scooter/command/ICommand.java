package com.omni.scooter.command;

/**
 * 指令接口，用于规范指令生成方法 ,一些生成指令使用的常量
 * @author cxiaox
 *
 * @date 2018年8月31日 下午2:33:31
 */
public interface ICommand {
	
	//TODO S1 改为事件通知指令
	// 带一个参数
	/**重启*/
	public final static String ORDER_RESTART="S1";
	/**重启 代码*/
	public final static int ORDER_RESTART_CODE=0x5331;
	
	/**事件通知指令 基于IOT协议V1.2.0*/
	public final static String ORDER_EVENT_NOTICE="S1";
	/**时间通知指令代码 基于IOT协议V1.2.0*/
	public final static int ORDER_EVENT_NOTICE_CODE=0x5331;
	
	/**设置重连服务器地址*/
	public final static String ORDER_SET_ADDRESS="S3";
	/**设置重连服务器地址 代码*/
	public final static int ORDER_SET_ADDRESS_CODE=0x5333;
	
	/**设置SIM 连接信息*/
	public final static String ORDER_SIM_SET="S9";
	/**设置SIM 连接信息 代码*/
	public final static int ORDER_SIM_SET_CODE=0x5339;
	
	
	
	
	/**  控制请求-开锁 */
	public static final int CONTROL_REQUEST_ACTION_OPEN = 0;
	/**  控制请求-关锁  */
	public static final int CONTROL_REQUEST_ACTION_CLOSE = 1;
	
	
	/** 语音播报-找车  */
	public static final int VOICE_FIND = 2;
	
	/**  固件类型- omni IOT 设备 */
	public static final String DEVICE_TYPE_IOT_OMNI="8A";
	/**  固件类型- 控制器设备 */
	public static final String DEVICE_TYPE_CT="CT";
	
	/**  固件类型- segway IOT 设备 */
	public static final String DEVICE_TYPE_IOT_SEGWAY="85";
	/**  固件类型- segway 控制器 */
	public static final String DEVICE_TYPE_SEGWAY_CONTROL="20"; 
	/**  固件类型- segway 仪表 */
	public static final String DEVICE_TYPE_SEGWAY_DISPLAY="21"; 
	/**  固件类型- segway 内置电池 */
	public static final String DEVICE_TYPE_SEGWAY_BATTERY1="22"; 
	/**  固件类型- segway 外置电池*/
	public static final String DEVICE_TYPE_SEGWAY_BATTERY2="23"; 
	
	
	
	
	/**
	 * 事件通知--开始预约
	 */
	public static final int EVENT_TYPE_RESERVATION_START=10; 
	/**
	 * 事件通知--取消预约
	 */
	public static final int EVENT_TYPE_RESERVATION_CANCEL=11; 
	/**
	 * 事件通知--IOT 关机
	 */
	public static final int EVENT_TYPE_IOT_SHUT_DOWN=1; 
	/**
	 * 事件通知--IOT 重启
	 */
	public static final int EVENT_TYPE_IOT_RESTART=2; 
	
	/**
	 * 事件通知--故障标记
	 */
	public static final int EVENT_TYPE_BREAKDOWN=12; 
	/**
	 * 事件通知--取消故障标记
	 */
	public static final int EVENT_TYPE_BREAKDOWN_CANCEL=13; 
	
	/**
	 * 事件通知--车丢失标记
	 */
	public static final int EVENT_TYPE_LOST=16; 
	/**
	 * 事件通知--取消车丢失标记
	 */
	public static final int EVENT_TYPE_LOST_CANCEL=17; 
	
}
