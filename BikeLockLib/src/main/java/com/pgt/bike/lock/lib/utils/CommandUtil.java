package com.pgt.bike.lock.lib.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.management.DescriptorKey;

import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.up.DeviceType;

 

public class CommandUtil {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("YYMMddHHmmss");
	private static byte[] order00=new byte[]{(byte) 0xFF,(byte) 0xFF};
	/**
	 * 
	 * @param code  厂商代码
	 * @param IMEI  设备IMEI号
	 * @param date  日期(YYMMddHHmmss)
	 * @param orderCode 指令代码
	 * @param content 指令内容<br />无指令内容则为#<br />有单个指令内容则为,value#<br />有多个指令内容则为,value,value...#
	 * @return
	 */
	public static byte[] getCommand(String code,String IMEI,String date,String orderCode,String content){
//		String sImei = String.format("%015d", Long.parseLong(IMEI));
		String sImei = IMEI;
		String command =String.format("%s,%s,%s,%s,%s%s\n", Command.SEND_HEAD,code,sImei,date,orderCode,content);
		byte[] orderContect=command.getBytes(); 
		return add(order00,orderContect);
	}
	public static byte[] getBGFCommand(String code,String IMEI,String orderCode,String content){
//		String sImei = String.format("%015d", Long.parseLong(IMEI));
		String sImei =IMEI;
		String command =String.format("%s,%s,%s,%s%s\n", Command.SEND_BGF_HEAD,code,sImei,orderCode,content);
		byte[] orderContect=command.getBytes(); 
		return add(order00,orderContect);
	}
	
 
	/**
	 * 以当前日期时间为 发送时间
	 * @param code  厂商代码
	 * @param IMEI  设备IMEI号
	 * @param orderCode 指令代码
	 * @param content 指令内容<br />无指令内容则为#<br />有单个指令内容则为,value#<br />有多个指令内容则为,value,value...#
	 * @return
	 */
	public static byte[] getCommand(String code,String IMEI,String orderCode,String content){
		Calendar c = Calendar.getInstance();
		String date=sdf.format(c.getTime());
		return getCommand(code,IMEI,date,orderCode,content);
	}
	 
	public static byte[] getCommandForSleep(byte[] sleepHead,String code,String IMEI,String date,String orderCode,String content){
		String command =String.format("%s,%s,%s,%s,%s%s\n", Command.SEND_HEAD,code,IMEI,date,orderCode,content);
		byte[] orderContect=command.getBytes(); 
		return add(sleepHead,orderContect);
	}
	public static byte[] getCommandForSleep(byte[] sleepHead,String code,String IMEI, String orderCode,String content){
		Calendar c = Calendar.getInstance();
		String date=sdf.format(c.getTime());
		String command =String.format("%s,%s,%s,%s,%s%s\n", Command.SEND_HEAD,code,IMEI,date,orderCode,content);
		byte[] orderContect=command.getBytes(); 
		return add(sleepHead,orderContect);
	}
	
	
	private static byte[] add(byte[] b1,byte[] b2){
		byte[] result =new byte[b1.length+b2.length];
		System.arraycopy(b1,0, result, 0, b1.length);
		System.arraycopy(b2,0, result, b1.length, b2.length);
		return result;
	}
	
	 
	/**
	 * 
	 * @param code 
	 * @param IMEI 
	 * @param date 日期(00+秒的时间戳，字符串格式)
	 * @return 
	 */
	public static byte[] getLocationCommand(String code,String IMEI,String date){
		return getCommand(code,IMEI,date,Command.ORDER_LOCATION,"#");
	}
	
	
	
	public static byte[] getLocationCommand(String code,String IMEI){
		return getCommand(code,IMEI, Command.ORDER_LOCATION,"#");
	}
	
	
	/**
	 * 电池电源  开启关闭.控制电池是否供电
	 * @param code  设备代码
	 * @param IMEI  设备IMEI号
	 * @param status  0-开启，1-关闭
	 * @return
	 */
	public static byte[] getBatteryPower(String code,String IMEI,int status){
		String content =String.format(",%s#", status);
		return getCommand(code,IMEI, Command.ORDER_BATTERY_POWER,content);
	}
	
	
	
	
	/**
	 * fence lock  location command ,when lock Receive the command ,start location
	 * @param code  device code
	 * @param IMEI  device imei
	 * @return 
	 */
	public static byte[] getBGFLocationCommand(String code,String IMEI){
		return getBGFCommand(code,IMEI, Command.ORDER_LOCATION,"#");
	}
	
	
	public static byte[] getInfoCommand(String code,String IMEI,String date){
		String info=Command.ORDER_INFO;
		return getCommand(code,IMEI,date,info,"#");
	}
	public static byte[] getInfoCommand(String code,String IMEI){
		//s5
		return getCommand(code,IMEI, Command.ORDER_INFO,"#");
	}
	/**
	 * 围栏锁 获取锁信息指令
	 * @param code
	 * @param IMEI
	 * @return
	 */
	public static byte[] getBGFInfoCommand(String code,String IMEI){
		//s5
		return getBGFCommand(code,IMEI, Command.ORDER_INFO,"#");
	}
	public static byte[] getVersionCommand(String code,String IMEI,String date){
		return getCommand(code,IMEI,date,Command.ORDER_VERSION,"#");
	}
	
	public static byte[] getVersionCommand(String code,String IMEI){
		// g0
		return getCommand(code,IMEI, Command.ORDER_VERSION,"#");
	}
	public static byte[] getBGFVersionCommand(String code,String IMEI){
		// g0
		return getBGFCommand(code,IMEI, Command.ORDER_VERSION,"#");
	}
	public static byte[] getVersionCommand(String code,Long IMEI){
//		String sImei = String.format("%015d", IMEI);
		return getVersionCommand(code,String.valueOf(IMEI));
	}
	public static byte[] getIccidCommand(String code,String IMEI,String date){
		//I0
		return getCommand(code,IMEI,date,Command.ORDER_ICCID,"#");
	}
	public static byte[] getIccidCommand(String code,String IMEI ){
		//I0
		return getCommand(code,IMEI,Command.ORDER_ICCID,"#");
	}
	public static byte[] getBGFIccidCommand(String code,String IMEI ){
		//I0
		return getBGFCommand(code,IMEI,Command.ORDER_ICCID,"#");
	}
	public static byte[] getBGFIccidCommand(String code,long IMEI ){
		//I0
		return getBGFIccidCommand(code,String.valueOf(IMEI));
	}
	public static byte[] getIccidCommand(String code,Long IMEI ){
		//I0
		return getIccidCommand(code,String.valueOf(IMEI));
	}
	public static byte[] getBGFMacCommand(String code,String IMEI ){
		//M0
		return getBGFCommand(code,IMEI,Command.ORDER_MAC,"#");
	}
	
	public static byte[] getMacCommand(String code,String IMEI ){
		//M0
		return getCommand(code,IMEI,Command.ORDER_MAC,"#");
	}
	public static byte[] getMacCommand(String code,long IMEI ){
		//M0
		return getMacCommand(code,String.valueOf(IMEI));
	}
	public static byte[] getBGFMacCommand(String code,long IMEI ){
		//M0
		return getBGFMacCommand(code,String.valueOf(IMEI));
	}
	/**
	 * 让 锁去请求更新时间戳
	 * @param code
	 * @param IMEI
	 * @return
	 */
	public static byte[] getBGFTimestampRequestCommand(String code,long IMEI ){
		//T0
		return getBGFTimestampRequestCommand(code,String.valueOf(IMEI));
	}
	/**
	 * 让 锁去请求更新时间戳
	 * @param code
	 * @param IMEI
	 * @return
	 */
	public static byte[] getBGFTimestampRequestCommand(String code,String IMEI ){
		//T0
		return getBGFCommand(code,IMEI,Command.ORDER_LOCK_TIMESTAMP_REQUEST,"#");
	}
	/**
	 * 设置 围栏锁新的时间戳
	 * @param code
	 * @param IMEI
	 * @return
	 */
	public static byte[] getBGFTimestampSetCommand(String code,long IMEI ,String setNo,long timestamp){
		//T1
		return getBGFTimestampSetCommand(code,String.valueOf(IMEI),setNo,timestamp);
	}
	/**
	 * 设置 围栏锁新的时间戳
	 * @param code
	 * @param IMEI
	 * @return
	 */
	public static byte[] getBGFTimestampSetCommand(String code,String IMEI ,String setNo,long timestamp){
		//T1
		String content = String.format(",%s,%s#", setNo,timestamp);
		return getBGFCommand(code,IMEI,Command.ORDER_LOCK_TIMESTAMP_SET,content);
	}
	public static byte[] getHeartSetCommand(String code,String IMEI,String date,String period){
		//T0
		String content = String.format(",%s#", period);
		return getCommand(code,IMEI,date,Command.ORDER_HEART_SET,content);
	}
	public static byte[] getHeartSetCommand(String code,String IMEI,String period ){
		//T0
		String content = String.format(",%s#", period);
		return getCommand(code,IMEI,Command.ORDER_HEART_SET,content);
	}
	public static byte[] getBGFHeartIntervalCommand(String code,String IMEI,int period){
		//H1
		String content = String.format(",%s#", period);
		return getBGFCommand(code,IMEI,Command.ORDER_BGF_HEART_INTERVAL,content);
	}
	public static byte[] getBGFHeartIntervalCommand(String code,long IMEI,int period ){
		//H1
		 
		return getBGFHeartIntervalCommand(code,String.valueOf(IMEI), period);
	}
	public static byte[] getSetIpCommand(String code,String IMEI,int ipMode,String ipAddress,int port,int redialTimes){
		// s3
		String content = String.format(",%s,%s,%s,%s#", ipMode,ipAddress,port,redialTimes);
		return getCommand(code,IMEI, Command.ORDER_SET_ADDRESS,content);
	}
	/***
	 * 围栏锁 设置重连IP和端口
	 * @param code
	 * @param IMEI
	 * @param ipMode
	 * @param ipAddress
	 * @param port
	 * @param redialTimes
	 * @return
	 */
	public static byte[] getBGFSetIpCommand(String code,String IMEI,int ipMode,String ipAddress,int port,int redialTimes){
		// s3
		String content = String.format(",%s,%s,%s,%s#", ipMode,ipAddress,port,redialTimes);
		return getBGFCommand(code,IMEI, Command.ORDER_SET_ADDRESS,content);
	}
	public static byte[] getSetSIMCommand(String code,String IMEI,String APN,String PIN,String PUK){
		// s9
		String content = String.format(",%s,%s,%s#", APN,PIN,PUK);
		return getCommand(code,IMEI, Command.ORDER_SIM_SET,content);
	}
	
	public static byte[] getBGFSetSIMCommand(String code,String IMEI,String APN,String PIN,String PUK){
		// s9
		String content = String.format(",%s,%s,%s#", APN,PIN,PUK);
		return getBGFCommand(code,IMEI, Command.ORDER_SIM_SET,content);
	}
	public static byte[] getFindBikeCommand(String code,String IMEI,int beepTimes,int ledTimes){
		// S8
		String content = String.format(",%s,%s#", beepTimes,ledTimes);
		return getCommand(code,IMEI, Command.ORDER_FIND_BIKE,content);
	}
	/**
	 * 围栏锁 找车指令
	 * @param code
	 * @param IMEI
	 * @param beepTimes  响的持续时间
	 * @param ledTimes   0
	 * @return
	 */
	public static byte[] getBGFFindBikeCommand(String code,String IMEI,int beepTimes,int ledTimes){
		// S8
		String content = String.format(",%s,%s#", beepTimes,ledTimes);
		return getBGFCommand(code,IMEI, Command.ORDER_FIND_BIKE,content);
	}
	public static byte[] getReserveCommand(String code,String IMEI,String date){
		return getCommand(code,IMEI,date,Command.ORDER_RESERVE,"#");
	}
	public static byte[] getReserveCommand(String code,String IMEI ){
		return getCommand(code,IMEI, Command.ORDER_RESERVE,"#");
	}
	
	public static byte[] getReserveCommand(byte[] sleepHead,String code,String IMEI ){
		return getCommandForSleep (sleepHead,code,IMEI, Command.ORDER_RESERVE,"#");
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
	
	
	public static byte[] getBGFSleepCommand(String code,String IMEI){
		return getBGFCommand(code,IMEI, Command.ORDER_SLEEP,"#");
	}
	
	/**
	 * 发送开关锁指令
	 * @param IMEI 设备IMEI号
	 * @param date 日期(YYMMDDHHMMSS)
	 * @param lackStatus  开关锁状态，查看 {@link  Command#LOCK_ON} 和{@link  Command#LOCK_OFF}
	 * @return
	 */
	@Deprecated
	public static byte[] getOMLockCommand(String IMEI,String date,int lackStatus){
		String content =String.format(",%s#",lackStatus);
		return getCommand(Command.CODE,IMEI,date,Command.ORDER_SET_LOCK,content);
	}
	
	public static byte[] getLockCommand(String CODE,String IMEI,String date,int lackStatus){
		String content =String.format(",%s#",lackStatus);
		return getCommand(CODE,IMEI,date,Command.ORDER_SET_LOCK,content);
	}
	
	
	public static byte[] getLocationIntervalCommand(String CODE,String IMEI,int locationInterval){
		String content =String.format(",%s#",locationInterval);
		return getCommand(CODE,IMEI,Command.ORDER_LOCATION_INTERVAL,content);
	}
	
	
	/**
	 * 
	 * @param CODE
	 * @param IMEI
	 * @param date
	 * @param lackStatus
	 * @return
	 */
	public static byte[] getBGMLockCommand(String CODE,String IMEI,int lackStatus,int uid,long timestamp){
		String content =String.format(",%s,%s,%s#",lackStatus,uid,timestamp);
		return getCommand(CODE,IMEI,Command.ORDER_SET_LOCK,content);
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
 
	/**
	 * 
	 * @param CODE  设备代码
	 * @param IMEI  设备IMEI号
	 * @param openType 开关锁类型，查看 {@link  Command#LOCK_ON_IN} 和{@link  Command#LOCK_ON_CONTINUE}
	 * @param uid   用户ID
	 * @param timestamp  开锁时间戳
	 * @param delayTime  最大开锁延迟秒数
	 * @return
	 */
	public static byte[] getBGFLockCommand(String CODE,String IMEI,int openType,int uid,long timestamp,int delayTime){
		String content =String.format(",%s,%s,%s,%s#",openType,uid,timestamp,delayTime);
		return getBGFCommand(CODE,IMEI,Command.ORDER_SET_LOCK,content);
	}
	public static byte[] getLockCommand(byte[] sleephead,String CODE,String IMEI,String date,int lackStatus){
		String content =String.format(",%s#",lackStatus);
		return getCommandForSleep(sleephead,CODE,IMEI,date,Command.ORDER_SET_LOCK,content);
	}
	public static byte[] getLockCommand(byte[] sleephead,String CODE,String IMEI ,int lackStatus){
		String content =String.format(",%s#",lackStatus);
		return getCommandForSleep(sleephead,CODE,IMEI,Command.ORDER_SET_LOCK,content);
	}
	
	
	public static byte[] getLockCommand(byte[] sleephead,String CODE,long IMEI ,int lackStatus){
		return getLockCommand(sleephead,CODE,String.valueOf(IMEI),lackStatus);
	}
	
	/**
	 * 发送开关锁指令,以当前时间为基
	 * @param IMEI 设备IMEI号
	 * @param lackStatus  开关锁状态，查看 {@link  Command#LOCK_ON} 和{@link  Command#LOCK_OFF}
	 * @return
	 * @deprecated   replaced by <code>getLockCommand(code,IMEI,secondTime,lockstatus)</code>.
	 */
	@Deprecated
	public static  byte[] getOMLockCommand(String IMEI,int lackStatus){
		String content =String.format(",%s#",lackStatus);
		return getCommand(Command.CODE,IMEI,Command.ORDER_SET_LOCK,content);
	}
	
	/**
	 * 
	 * @param IMEI
	 * @param lackStatus
	 * @return command
	 * @deprecated   replaced by <code>getLockCommand(code,IMEI,secondTime,lockstatus)</code>.
	 */
	@Deprecated
	public static  byte[] getOMLockCommand(long IMEI,int lackStatus){
		return getOMLockCommand(String.valueOf(IMEI) ,lackStatus);
	}

	/**
	 * 
	 * @param IMEI
	 * @param lackStatus
	 * @return
	 * @deprecated   replaced by <code>getLockCommand(code,IMEI,secondTime,lockstatus)</code>.
	 */
	@Deprecated
	public static  String getOMLockCommandStr(String IMEI,int lackStatus){
		String content =String.format(",%s#",lackStatus);
		byte[] order =getCommand(Command.CODE,IMEI,Command.ORDER_SET_LOCK,content);
		return new String(order,0,order.length);
	}

	/**
	 * 
	 * @param IMEI
	 * @param ipMode
	 * @param ipAddress
	 * @param port
	 * @param redialTimes
	 * @return
	 * @deprecated   replaced by <code>getSetIpCommand(code,IMEI,ipmode,ipaddress,port,redialTimes)</code>.
	 */
	@Deprecated
	public static  byte[] getOMSetIPCommand(String IMEI,String ipMode,String ipAddress,String port,int redialTimes){
		Calendar c = Calendar.getInstance();
		String date=sdf.format(c.getTime());
		String content =String.format(",%s,%s,%s,%s#",ipMode,ipAddress,port,redialTimes );
		return getCommand(Command.CODE,IMEI,date,Command.ORDER_SET_ADDRESS,content);
	}
	
	/**
	 * @param IMEI
	 * @param statusContent
	 * @return
	 * @deprecated   replaced by <code>getReportCommand(code,IMEI,statusContent)</code>.
	 */
	@Deprecated
	public static  String getOMOpenLockReport(String IMEI,String statusContent){
		String content =String.format(",%s#",statusContent);
		byte[] order =getCommand(Command.CODE,IMEI,Command.ORDER_RETURN,content);
		return new String(order,0,order.length);
	}
	/**
	 * @param IMEI
	 * @param statusContent
	 * @return
	 * @deprecated   replaced by <code>getReportCommand(code,IMEI,statusContent)</code>.
	 */
	@Deprecated
	public static  String getOMLockReport(String IMEI,String statusContent){
		String content =String.format(",%s#",statusContent);
		byte[] order =getCommand(Command.CODE,IMEI,Command.ORDER_RETURN,content);
		return new String(order,0,order.length);
	}
	
	public static  byte[] getReportCommand(String code,String IMEI,String statusContent){
		String content =String.format(",%s#",statusContent);
		byte[] order =getCommand(code,IMEI,Command.ORDER_RETURN,content);
		return order;
	}
	public static  byte[] getBGFReportCommand(String code,String IMEI,String statusContent){
		String content =String.format(",%s#",statusContent);
		byte[] order =getBGFCommand(code,IMEI,Command.ORDER_RETURN,content);
		return order;
	}
	public static  byte[] getReportCommand(String code, long IMEI,String statusContent){
//		String sImei=String.format("%015d", IMEI);
		String sImei=String.format("%s", IMEI);
		return getReportCommand(code,sImei,statusContent);
	}
	public static  byte[] getBGFReportCommand(String code,long IMEI,String statusContent){
		return getBGFReportCommand(code,String.valueOf(IMEI),statusContent);
	}
	public static byte[] getStartUpComm(String Code,String IMEI,int nPack,int size,int crcInt,String deviceType,String upCode){
		String content=String.format(",%s,%s,%s,%s,%s#", nPack,size,crcInt,deviceType,upCode);
		return getCommand(Code, IMEI, Command.ORDER_UPDATE_VERSION, content);
	}
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
		String date = sdf.format(c.getTime());
		String commad = String.format("%s,%s,%s,%s,%s,%s,%s,%s,",Command.SEND_HEAD,Code,IMEI,date,Command.ORDER_CONTROLLER,deviceType,npack,crc);
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
	public static byte[] getBGFStartUpComm(String Code,String IMEI,int nPack,int size,int crcInt,String deviceType,String upCode){
		String content=String.format(",%s,%s,%s,%s,%s#", nPack,size,crcInt,deviceType,upCode);
		return getBGFCommand(Code, IMEI, Command.ORDER_UPDATE_VERSION, content);
	}
	public static byte[] getBGFStartUpComm(String Code,long IMEI,int nPack,int size,int crcInt,String deviceType,String upCode){
		String imei = String.valueOf(IMEI);
		return getBGFStartUpComm( Code, imei, nPack, size, crcInt, deviceType, upCode);
	}
	public static byte[] getUpdateDetailComm(String Code,String IMEI, int npack,int crc,byte[] updateItem){
		Calendar c = Calendar.getInstance();
		String date = sdf.format(c.getTime());
		String commad = String.format("%s,%s,%s,%s,%s,%s,%s,",Command.SEND_HEAD,Code,IMEI,date,"U1",npack,crc);
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
		String commad = String.format("%s,%s,%s,%s,%s,%s,",Command.SEND_BGF_HEAD,Code,IMEI,"U1",npack,crc);
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
