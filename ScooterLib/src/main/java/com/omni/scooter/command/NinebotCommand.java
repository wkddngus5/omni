package com.omni.scooter.command;

import java.util.Calendar;

import com.omni.scooter.entity.Command;


 

 
 
public class NinebotCommand extends BaseCommandUtil{
 
	private static final String SEND_NINE_BOT_HEAD="*HBCS";
	
	private static final String CODE_NINE_BOT="NB";
 
 
	/**
	 * @param code 厂商代码
	 * @param IMEI  设备IMEI号
	 * @param orderCode  指令类型
	 * @param content  指令内容<br />无指令内容则为#<br />有单个指令内容则为,value#<br />有多个指令内容则为,value,value...#
	 * @return
	 */
	public static byte[] getCommand(String code,String IMEI,String orderCode,String content){
		return getCommand(SEND_NINE_BOT_HEAD,code,IMEI,orderCode,content);
	}

	/**
	 * 厂商代码为默认 NB
	 * @param IMEI 设备IMEI号
	 * @param orderCode  指令类型
	 * @param content 指令内容<br />无指令内容则为#<br />有单个指令内容则为,value#<br />有多个指令内容则为,value,value...#
	 * @return
	 */
	public static byte[] getCommand( String IMEI,String orderCode,String content){
		return getCommand(SEND_NINE_BOT_HEAD,CODE_NINE_BOT,IMEI,orderCode,content);
	}
 
	
	
	
	
	
	/**
	 * 操作请求指令R0 ,用来获取开锁，关锁需要的操作KEY
	 * @param IMEI  imei号
	 * @param action 操作类型  ，1-开锁，2-关锁。
	 * @param valid  key 有效时间
	 * @param uid 用户id
	 * @param timestamp 请求操作序列号,即操作时间戳，建议使用Unix timestamp
	 * @return
	 */
	public static byte[] getControlRequestCommand(String IMEI,int action,int valid,int uid,long timestamp){
		String content =String.format(",%s,%s,%s,%s#",action,valid,uid,timestamp);
		return getCommand(IMEI, Command.ORDER_CONTROL_REQUEST,content);
	}
	/**
	 * 获取到开锁指令
	 * @param IMEI imei号
	 * @param key  操作KEY ,由R0指令返回。
	 * @param uid  用户id
	 * @param timestamp 开锁操作序列号,即操作时间戳，建议使用Unix timestamp
	 * @return
	 */
	public static byte[] getOpenLockCommand( String IMEI,int key,int uid,long timestamp){
		String content =String.format(",%s,%s,%s#",key,uid,timestamp);
		return getCommand( IMEI,Command.ORDER_LOCK_OPEN,content);
	}
	
	/**
	 * 获取到关锁指令
	 * @param IMEI  IMEI号
	 * @param key  关锁KEY
	 * @return
	 */
	public static byte[] getCloseLockCommand( String IMEI,int key){
		String content =String.format(",%s#",key);
		return getCommand( IMEI,Command.ORDER_LOCK_CLOSE,content);
	}
	
	public static byte[] getVersionCommand( String IMEI){
		// g0
		return getCommand( IMEI, Command.ORDER_VERSION,"#");
	}
	 
	public static byte[] getVersionCommand( Long IMEI){
		// g0
		return getVersionCommand( String.valueOf(IMEI));
	}
	
	
	
 
	/**
	 * 获取定位指令
	 * @param IMEI  设备的IMEI号
	 * @return 
	 */
	public static byte[] getLocationCommand(String IMEI){
		//D0
		return getCommand(IMEI, Command.ORDER_LOCATION,"#");
	}
	/**
	 * 获取追踪指令
	 * @param IMEI  设备的IMEI号
	 * @param traceInterval  追踪间隔，D0上传的间隔,当间隔为0时，停止追踪
	 * @return 
	 */
	public static byte[] getTraceCommand(String IMEI,int traceInterval){
		//D1
		String content =String.format(",%s#",traceInterval);
		return getCommand(IMEI, Command.ORDER_TRACE,content);
	}
	
	/**
	 * 滑板车 电源开关机指令
	 * @param IMEI
	 * @param onOrOff 1-关机，2-开机
	 * @return
	 */
	public static byte[] getPowerControlCommand(String IMEI,int onOrOff ){
		// s2
		String content =String.format(",%s#",onOrOff);
		return getCommand(IMEI, Command.ORDER_POWER_CONTROL,content);
	}
 
	/**
	 *  iot 设置指令
	 * @param imei  锁IMEI号
	 * @param Sensitivity  加速度计灵敏度 ，0:无效（不设置） 1:低 2:中 3:高（默认 2:中）
	 * @param Info1Status  骑行中上传滑板车信息 1,0:无效（不设置） 1:关闭 2:开启 （默认 2:开启）
	 * @param HeartInterval 心跳上传间隔，0:无效（不设置） 单位：秒（默认 240S）
	 * @param Info1Interval 骑行状态下滑板车信息 1 上传间隔，0:无效（不设置） 单位：秒（默认 10S）
	 * @return
	 */
	public static byte[] getIotSetCommand(String imei,int Sensitivity,int Info1Status,int HeartInterval,int Info1Interval){
		//S5
		String content =String.format(",%s,%s,%s,%s#",Sensitivity,Info1Status,HeartInterval,Info1Interval);
		return getCommand(imei, Command.ORDER_IOT_SET_INFO,content);
	}
	
	/**
	 * 获取到 滑板车信息1(滑板车当前电量，当前车速，滑板车当前模式，总骑行里程，预计剩余骑行里程，总骑行时间等)
	 * @param IMEI
	 * @return
	 */
	public static byte[] getScooterInfo1Command( String IMEI){
		//s6
		return getCommand( IMEI, Command.ORDER_SCOOTER_INFO,"#");
	}
	
	/**
	 *  滑板车设置指令1,协议中最后预留了一个字段
	 * @param IMEI
	 * @param lightStatus  大灯开关 0:无效（不设置） 1:关闭 2:开启
	 * @param speedMode    模式设置 0:无效（不设置） 1:低速 2:中速 3:高速
	 * @param throttleStatus  油门响应 0:无效（不设置） 1:关闭 2:开启
	 * @param headlight  前灯闪烁 0:无效（不设置） 1:关闭 2:开启
	 * @param tailLight  后灯闪烁 0:无效（不设置） 1:关闭 2:开启
	 * @return
	 */
	public static byte[] getScooterSet1Command( String IMEI,int lightStatus,int speedMode,int throttleStatus,int headlight,int tailLight){
		//S7  协议中最后预留了一个字段
		String content =String.format(",%s,%s,%s,%s,%s,0#",lightStatus,speedMode,throttleStatus,headlight,tailLight);
		return getCommand(IMEI, Command.ORDER_SCOOTER_SET1,content);
	}
	 
	/**
	 * 获取到 滑板车信息2(滑板车充电状态，控制器驱动电压，电池循环次数，电池 电量，内置电池温度，外挂电池温度等)
	 * @param IMEI
	 * @return
	 */
	public static byte[] getScooterInfo2Command( String IMEI){
		//s8
		return getCommand( IMEI, Command.ORDER_SCOOTER_INFO2,"#");
	}
	
	
	
	/**
	 * 滑板车设置指令2
	 * @param IMEI
	 * @param inchStatus  英制速度显示 0:无效（不设置） 1:关闭 2:开启
	 * @param cruiseControl  定速巡航  0-无效 ， 1:关闭 2:开启
	 * @param scooterBle 车体蓝牙广播 0:无效（不设置） 1:关闭 2:开启
	 * @param changeMode 按键切换模式 0:无效（不设置） 1:关闭 2:开启
	 * @param lowSpeedLimit 低速模式限速值 0:无效（不设置） 范围：6-25
	 * @param midSpeedLimit 中速模式限速值 0:无效（不设置）  范围：6-25
	 * @param highSpeedLimit 高速模式限速值 0:无效（不设置）  范围：6-25
	 * @return
	 */
	public static byte[] getScooterSet2Command( String IMEI,int inchStatus,int cruiseControl,int scooterBle,int changeMode,int lowSpeedLimit,int midSpeedLimit,int highSpeedLimit){
		//s4
		String content = String.format(",%s,%s,%s,%s,%s,%s,%s#", inchStatus,cruiseControl,scooterBle,  changeMode,lowSpeedLimit,midSpeedLimit,highSpeedLimit);
		return getCommand( IMEI, Command.ORDER_SCOOTER_SET2,content);
	}
	
	/**
	 * 发送语音播报指令
	 * @param IMEI
	 * @param voiceId  播放内容 1:使出 Geofence 提示 2:找车提示 3:低电量提醒
	 * @return
	 */
	public static byte[] getVoiceCommand(String IMEI,int voiceId ){
		// v0
		String content = String.format(",%s#", voiceId );
		return getCommand(IMEI, Command.ORDER_VOICE,content);
	}
	
 
	/**
	 * 生成回应指令
	 * @param IMEI
	 * @param commandType
	 * @return
	 */
	public static byte[] getResponseCommand(String IMEI ,String commandType){
		return getCommand(IMEI, commandType,"#");
	}
	/**
	 * 设置 device key
	 * @param IMEI
	 * @param deviceKey
	 * @return
	 */
	public static byte[] getSetDeviceKey( String IMEI,String deviceKey){
		String content =String.format(",%s,%s#",1,deviceKey);
		return getCommand( IMEI,Command.ORDER_DEVICE_KEY,content);
	}
	/**
	 * 读取 device key
	 * @param IMEI
	 * @return
	 */
	public static byte[] getReadDeviceKey( String IMEI){
		String content =String.format(",%s,%s#",0,0);
		return getCommand(IMEI,Command.ORDER_DEVICE_KEY,content);
	}
	 
	/**
	 * U0 指令，发送升级文件数据
	 * 基于协议v1.2.8
	 * @param IMEI
	 * @param nPack 升级数据总包数
	 * @param realLen 升级数据的真实长度
	 * @param crcInt 升级数据总 CRC16 校验值
	 * @param deviceType 升级文件对应的设备识别码(85->Iot 设备,20->滑板车控制器,21->滑板车仪表,22->内置电池,23->外置电池)
	 * @param upCode 升级秘钥（C7qn）
	 * @return
	 */
	public static byte[] getStartUpComm(String IMEI,int nPack,int realLen,int crcInt,String deviceType,String upCode){
		String content=String.format(",%s,%s,%s,%s,%s#", nPack,realLen,crcInt,deviceType,upCode);
		return getCommand( IMEI, Command.ORDER_UPDATE_VERSION, content);
	}
	/**
	 * U0 指令，发送升级文件数据
	 * @param IMEI
	 * @param nPack 升级数据总包数
	 * @param size 每包数据大小 单位：Byte， 最大 128
	 * @param crcInt 升级数据总 CRC16 校验值
	 * @param deviceType 升级文件对应的设备识别码(85->Iot 设备 CT->滑板车控制器)
	 * @param upCode 升级秘钥（C7qn）
	 * @return
	 */
	public static byte[] getStartUpComm(Long IMEI,int nPack,int size,int crcInt,String deviceType,String upCode){
		String sImei=String.valueOf(IMEI);
		return getStartUpComm(sImei,nPack,size,crcInt,deviceType,upCode);
	}
	
	 
	public static byte[] getIccidCommand( String IMEI ){
		//I0
		return getCommand(IMEI,Command.ORDER_ICCID,"#");
	}
	
	public static byte[] getMacCommand(String IMEI ){
		//M0
		return getCommand(IMEI,Command.ORDER_MAC,"#");
	}
	 
	public static byte[] getSetIpCommand(String code,String IMEI,int ipMode,String ipAddress,int port,int redialTimes){
		// s3
		String content = String.format(",%s,%s,%s,%s#", ipMode,ipAddress,port,redialTimes);
		return getCommand(code,IMEI, Command.ORDER_SET_ADDRESS,content);
	}
	 
	public static byte[] getSetSIMCommand(String code,String IMEI,String APN,String PIN,String PUK){
		// s9
		String content = String.format(",%s,%s,%s#", APN,PIN,PUK);
		return getCommand(code,IMEI, Command.ORDER_SIM_SET,content);
	}
	
 
	
	/**
	 * 整车关机指令S0 ,与滑板车关机指令S2(getPowerControlCommand)要区别开来。
	 * 发送整车关机指令S0 后，滑板车关机,IOT 网络关闭。仅保留IOT蓝牙用于开机。
	 * 用于运输。
	 * @param IMEI
	 * @return 指令数组
	 */
	public static byte[] getShutDownCommand( String IMEI){
		return getCommand( IMEI, Command.ORDER_SLEEP,"#");
	}
	
 
	/**
	 * 发送升级数据包详情 
	 * 基于协议v1.2.8
	 * @param IMEI  设备IMEI号
	 * @param npack 第几包数据
	 * @param size  该包升级文件大小
	 * @param crc   该包升级文件CRC值。
	 * @param updateItem 该包升级文件数据
	 * @return 指令数组
	 */
	public static byte[] getUpdateDetailComm( String IMEI, int npack,int size,int crc,byte[] updateItem){
		String commad = String.format("%s,%s,%s,%s,%s,%s,%s,",Command.SEND_HEAD,Command.CODE,IMEI,Command.ORDER_UPDATE,npack,size,crc);
		byte[] orderContent = commad.getBytes();
		byte[] t = add(orderContent,updateItem);
		String end = "#\n";
		t = add(t,end.getBytes());
		return add(order00,t);
	}
}
