package com.pgt.bike.lock.lib.utils;

import java.util.Calendar;


import com.pgt.bike.lock.lib.entity.Command;

 

public class BicmanCommandUtil {
	
	public static final int  POWER_OFF = 1;
	public static final int  POWER_ON = 2;
	
	private static byte[] order00=new byte[]{(byte) 0xFF,(byte) 0xFF};
	/**
	 * 
	 * @param code  厂商代码
	 * @param IMEI  设备IMEI号
	 * @param date  日期(YYMMddHHmmss)
	 * @param content 指令内容<br />无指令内容则为#<br />有单个指令内容则为,value#<br />有多个指令内容则为,value,value...#
	 * @return
	 */
	public static byte[] getCommand(String code,String IMEI,String orderCode,String content){
		String sImei = String.format("%015d", Long.parseLong(IMEI));
		String command =String.format("%s,%s,%s,%s%s\n", Command.SEND_BICMAN_HEAD,code,sImei,orderCode,content);
		byte[] orderContect=command.getBytes(); 
		return add(order00,orderContect);
	}

	public static byte[] getCommand( String IMEI,String orderCode,String content){
		String sImei = String.format("%015d", Long.parseLong(IMEI));
		String command =String.format("%s,%s,%s,%s%s\n", Command.SEND_BICMAN_HEAD,Command.CODE_BICMAN,sImei,orderCode,content);
		byte[] orderContect=command.getBytes(); 
		return add(order00,orderContect);
	}
 
 
	private static byte[] add(byte[] b1,byte[] b2){
		byte[] result =new byte[b1.length+b2.length];
		System.arraycopy(b1,0, result, 0, b1.length);
		System.arraycopy(b2,0, result, b1.length, b2.length);
		return result;
	}
	
	 
 
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
	
 
	public static byte[] getBicmanInfoCommand( String IMEI){
		//s6
		return getCommand( IMEI, Command.ORDER_BICMAN_INFO,"#");
	}
	/**
	 * 
	 * @param IMEI
	 * @param lightMode  大灯开关  0-无效，1-关闭，2-开启
	 * @param speedMde  速度设置 0-无效 ，1-低速，2-中速，3-高速
	 * @param acceleratorMode 油门响应 0-无效，1-关闭，2-开启
	 * @param lowSpeed 低速模式限速值 0:无效（不设置）  范围：6-25
	 * @param midSpeed 中速模式限速值 0:无效（不设置）  范围：6-25
	 * @param highSpeed高速模式限速值 0:无效（不设置）  范围：6-25
	 * @return
	 */
	public static byte[] getBicmanConfigCommand( String IMEI,int lightMode,int speedMde,int acceleratorMode,int lowSpeed,int midSpeed,int highSpeed){
		//s7
		String content = String.format(",%s,%s,%s,%s,%s,%s#", lightMode,speedMde,acceleratorMode,  lowSpeed,midSpeed,highSpeed);
		return getCommand( IMEI, Command.ORDER_BICMAN_CONFIG,content);
	}
 
	public static byte[] getVersionCommand( String IMEI){
		// g0
		return getCommand( IMEI, Command.ORDER_VERSION,"#");
	}
	 
	public static byte[] getVersionCommand( Long IMEI){
		// g0
		return getVersionCommand( String.valueOf(IMEI));
	}
	 
	public static byte[] getIccidCommand(String code,String IMEI ){
		//I0
		return getCommand(code,IMEI,Command.ORDER_ICCID,"#");
	}
	 
	 
	public static byte[] getIccidCommand(String code,Long IMEI ){
		//I0
		String sImei=String.format("%015d", IMEI);
		System.out.println("sImei="+sImei);
		return getIccidCommand(code,sImei);
	}
	 
	
	public static byte[] getMacCommand(String code,String IMEI ){
		//M0
		return getCommand(code,IMEI,Command.ORDER_MAC,"#");
	}
	public static byte[] getMacCommand(String code,long IMEI ){
		//M0
		return getMacCommand(code,String.valueOf(IMEI));
	}
	 
	 
  
	 
 
	public static byte[] getHeartSetCommand(String code,String IMEI,String period ){
		//T0
		String content = String.format(",%s#", period);
		return getCommand(code,IMEI,Command.ORDER_HEART_SET,content);
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
	
	 
	public static byte[] getInfo2Command(String IMEI){
		// S8	 
		return getCommand(IMEI, Command.ORDER_FIND_BIKE,"#");
	}
	public static byte[] getVoiceCommand(String IMEI,int voiceId ){
		// v0
		String content = String.format(",%s#", voiceId );
		return getCommand(IMEI, Command.ORDER_VOICE,content);
	}
	
	/**
	 * 操作请求指令R0 ,用来获取开锁，关锁需要的操作KEY
	 * @param IMEI  imei号
	 * @param action 操作类型  ，1-开锁，2-关锁。
	 * @param valid  key 有效时间
	 * @param uid 用户id
	 * @param timestamp 操作时间戳
	 * @return
	 */
	public static byte[] getControlRequestCommand(String IMEI,int action,int valid,int uid,long timestamp){
		String content =String.format(",%s,%s,%s,%s#",action,valid,uid,timestamp);
		return getCommand(IMEI, Command.ORDER_CONTROL_REQUEST,content);
	}
	 
	/**
	 * 
	 * @param IMEI
	 * @param onOrOff 1-关机，2-开机
	 * @return
	 */
	public static byte[] getPowerControlCommand(String IMEI,int onOrOff ){
		String content =String.format(",%s#",onOrOff);
		return getCommand(IMEI, Command.ORDER_BICMAN_SHUTDOWN,content);
	}
	
	/**
	 * 以当前时间 发送休眠指令
	 * @param code
	 * @param IMEI
	 * @return
	 */
	public static byte[] getSleepCommand(String code,String IMEI){
		return getCommand(code,IMEI, Command.ORDER_SLEEP,"#");
	}
	
	public static byte[] getRestartpCommand(String code,String IMEI){
		return getCommand(code,IMEI, Command.ORDER_RESTART,"#");
	}
	

	
	public static byte[] getLockCommand(String CODE,String IMEI ,int lackStatus){
		String content =String.format(",%s#",lackStatus);
		return getCommand(CODE,IMEI,Command.ORDER_SET_LOCK,content);
	}
	
	
	public static byte[] getLocationIntervalCommand(String CODE,String IMEI,int locationInterval){
		String content =String.format(",%s#",locationInterval);
		return getCommand(CODE,IMEI,Command.ORDER_LOCATION_INTERVAL,content);
	}
	
	
	/**
	 * 获取到开锁指令
	 * @param IMEI
	 * @param key
	 * @param uid
	 * @param timestamp
	 * @return
	 */
	public static byte[] getOpenLockCommand( String IMEI,int key,int uid,long timestamp){
		String content =String.format(",%s,%s,%s#",key,uid,timestamp);
		return getCommand( IMEI,Command.ORDER_SET_LOCK,content);
	}
	
	public static byte[] getCloseLockCommand( String IMEI,int key){
		String content =String.format(",%s#",key);
		return getCommand( IMEI,Command.ORDER_LOCK,content);
	}
	
	public static byte[] getBGMSetDeviceKey(String CODE,String IMEI,String deviceKey){
		String content =String.format(",%s,%s#",1,deviceKey);
		return getCommand(CODE,IMEI,Command.ORDER_DEVICE_KEY,content);
	}
	public static byte[] getBGMSetDeviceKey(String CODE,long IMEI,String deviceKey){
		String imei = String.valueOf(IMEI);
		return getBGMSetDeviceKey(CODE,imei,deviceKey);
	}
	public static byte[] getBGMReadDeviceKey(String CODE,String IMEI){
		String content =String.format(",%s,%s#",0,0);
		return getCommand(CODE,IMEI,Command.ORDER_DEVICE_KEY,content);
	}
	public static byte[] getBGMReadDeviceKey(String CODE,long IMEI){
		String imei = String.valueOf(IMEI);
		return getBGMReadDeviceKey(CODE,imei);
	}
 
 
	
	public static  byte[] getReportCommand( String IMEI,String commandCode){
		String content =String.format("#");
		byte[] order =getCommand( IMEI,commandCode,content);
		return order;
	}
	 
	public static  byte[] getReportCommand( long IMEI,String commandCode){
		return getReportCommand(String.valueOf(IMEI),commandCode);
	}
	 
	/**
	 * 
	 * @param Code 设备代码
	 * @param IMEI 设备IMEI号
	 * @param nPack  升级数据总包数
	 * @param size   升级数据的总长度
	 * @param crcInt 升级数据总 CRC16 校验值 
	 * @param deviceType 升级文件对应的设备识别码
	 * @param upCode 升级秘钥
	 * @return
	 */
	public static byte[] getStartUpComm(String Code,String IMEI,int nPack,int size,int crcInt,String deviceType,String upCode){
		String content=String.format(",%s,%s,%s,%s,%s#", nPack,size,crcInt,deviceType,upCode);
		return getCommand(Code, IMEI, Command.ORDER_UPDATE_VERSION, content);
	}
	
	/**
	 * 
	 * @param Code 设备代码
	 * @param IMEI 设备IMEI号
	 * @param nPack  升级数据总包数
	 * @param size   升级数据的总长度
	 * @param crcInt 升级数据总 CRC16 校验值 
	 * @param deviceType 升级文件对应的设备识别码
	 * @param upCode 升级秘钥
	 * @return
	 */
	public static byte[] getStartUpComm(String Code,Long IMEI,int nPack,int size,int crcInt,String deviceType,String upCode){
		String sImei=String.valueOf(IMEI);
		return getStartUpComm(Code,sImei,nPack,size,crcInt,deviceType,upCode);
	}
	
	// ==============文件获取指令============
	public static byte[] getStartUpController(String Code,String IMEI,int nPack,int size,int crcInt,String deviceType){
		String content=String.format(",%s,%s,%s,%s#", nPack,size,crcInt,deviceType);
		return getCommand(Code, IMEI, Command.ORDER_CONTROLLER_VERSION, content);
	}
	public static byte[] getStartUpController(String Code,Long IMEI,int nPack,int size,int crcInt,String deviceType){
		String sImei=String.valueOf(IMEI);
		return getStartUpController(Code,sImei,nPack,size,crcInt,deviceType);
	}
	public static byte[] getUpdateDetailController(String Code,String IMEI, String deviceType,int npack,int crc,byte[] updateItem){
		Calendar c = Calendar.getInstance();
	 
		String commad = String.format("%s,%s,%s,%s,%s,%s,%s,",Command.SEND_BICMAN_HEAD,Code,IMEI,Command.ORDER_CONTROLLER,deviceType,npack,crc);
		byte[] orderContent = commad.getBytes();
		byte[] t = add(orderContent,updateItem);
		String end = "#\n";
		t = add(t,end.getBytes());
		return add(order00,t);
	}
	public static byte[] getUpdateDetailController(String Code,Long IMEI, String deviceType, int npack,int crc,byte[] updateItem){
		return getUpdateDetailController(Code,String.valueOf(IMEI),deviceType,npack,crc,updateItem);
	}
	// ==============文件获取指令============
	
	
	public static byte[] getUpdateDetailComm(String Code,String IMEI, int npack,int crc,byte[] updateItem){
		 
		String commad = String.format("%s,%s,%s,%s,%s,%s,",Command.SEND_BICMAN_HEAD,Code,IMEI,"U1",npack,crc);
		byte[] orderContent = commad.getBytes();
		byte[] t = add(orderContent,updateItem);
		String end = "#\n";
		t = add(t,end.getBytes());
		return add(order00,t);
	}
	
	/**
	 * 新增获取升级文件详情的指令，增加了每包数据的长度的属性
	 * @param Code
	 * @param IMEI
	 * @param npack    第几包数据
	 * @param packSize  每包数据的长度
	 * @param crc
	 * @param updateItem
	 * @return
	 */
	public static byte[] getUpdateDetailComm(String Code,String IMEI, int npack,int packSize,int crc,byte[] updateItem){
		 
		 
		String commad = String.format("%s,%s,%s,%s,%s,%s,%s,",Command.SEND_BICMAN_HEAD,Code,IMEI,"U1",npack,packSize,crc);
		byte[] orderContent = commad.getBytes();
		byte[] t = add(orderContent,updateItem);
		String end = "#\n";
		t = add(t,end.getBytes());
		return add(order00,t);
	}
	
	public static byte[] getUpdateDetailComm(String Code,Long IMEI, int npack,int crc,byte[] updateItem){
		return getUpdateDetailComm(Code,String.valueOf(IMEI),npack,crc,updateItem);
	}
	
	
	
	public static byte[] getBGFUpdateDetailComm(String Code,String IMEI, int npack,int crc,byte[] updateItem){
		String commad = String.format("%s,%s,%s,%s,%s,%s,",Command.SEND_BICMAN_HEAD,Code,IMEI,"U1",npack,crc);
		byte[] orderContent = commad.getBytes();
		byte[] t = add(orderContent,updateItem);
		String end = "#\n";
		t = add(t,end.getBytes());
		return add(order00,t);
	}
	public static byte[] getBGFUpdateDetailComm(String Code,Long IMEI, int npack,int crc,byte[] updateItem){
		return getBGFUpdateDetailComm(Code,String.valueOf(IMEI),npack,crc,updateItem);
	}
}
