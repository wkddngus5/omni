package com.omni.scooter.command;

import com.omni.scooter.entity.Command;

public class OmniCommand extends BaseCommandUtil  {
	private static final String SEND_OMNI_HEAD="*SCOS";
	private static   String CODE_OM="OM";
	
	public static void setDeviceType(String deviceType){
		CODE_OM=deviceType;
	}
	
	/**
	 * 操作请求指令R0 ,用来获取开锁，关锁需要的操作KEY
	 * @param IMEI  imei号
	 * @param action 操作类型  ，0-开锁，1-关锁。
	 * @param valid  key 有效时间
	 * @param uid 用户id
	 * @param timestamp 请求操作序列号,即操作时间戳，建议使用Unix timestamp
	 * @return
	 */
	public static byte[] getControlRequestCommand(String IMEI,int action,int valid,int uid,long timestamp){
		String content =String.format(",%s,%s,%s,%s#",action,valid,uid,timestamp);
//		String sImei = String.format("%015d", Long.parseLong(IMEI));
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI, Command.ORDER_CONTROL_REQUEST,content);
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
//		String sImei = String.format("%015d", Long.parseLong(IMEI));
		return getCommand(SEND_OMNI_HEAD,CODE_OM, IMEI,Command.ORDER_LOCK_OPEN,content);
	}
	
	/**
	 * 获取到关锁指令
	 * @param IMEI  IMEI号
	 * @param key  关锁KEY
	 * @return
	 */
	public static byte[] getCloseLockCommand( String IMEI,int key){
		String content =String.format(",%s#",key);
		return getCommand( SEND_OMNI_HEAD,CODE_OM,IMEI,Command.ORDER_LOCK_CLOSE,content);
	}
	
	/**
	 * 生成回应指令
	 * @param IMEI
	 * @param commandType
	 * @return
	 */
	public static byte[] getResponseCommand(String IMEI ,String commandType){
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI, commandType,"#");
	}
	
	/**
	 *  iot 设置指令
	 * @param imei  锁IMEI号
	 * @param Sensitivity  加速度计灵敏度 ，0:无效（不设置） 1:低 2:中 3:高（默认 2:中）
	 * @param InfoStatus  骑行中上传滑板车信息 1(S6),0:无效（不设置） 1:关闭 2:开启 （默认 2:开启）
	 * @param HeartInterval 心跳上传间隔，0:无效（不设置） 单位：秒（默认 240S）
	 * @param InfoInterval 骑行状态下滑板车信息 1 (S6)上传间隔，0:无效（不设置） 单位：秒（默认 10S）
	 * @return
	 */
	public static byte[] getIotSetCommand(String imei,int Sensitivity,int InfoStatus,int HeartInterval,int InfoInterval){
		//S5
		String content =String.format(",%s,%s,%s,%s#",Sensitivity,InfoStatus,HeartInterval,InfoInterval);
		return getCommand(SEND_OMNI_HEAD,CODE_OM,imei, Command.ORDER_IOT_SET_INFO,content);
	}
	
	/**
	 * 获取到 滑板车信息(滑板车当前电量，当前车速，滑板车当前模式，滑板车充电状态，电池1电压，电池2电压)
	 * @param IMEI
	 * @return
	 */
	public static byte[] getScooterInfoCommand( String IMEI){
		//s6
		return getCommand( SEND_OMNI_HEAD,CODE_OM,IMEI, Command.ORDER_SCOOTER_INFO,"#");
	}
	
	/**
	 *  滑板车设置指令1
	 * @param IMEI
	 * @param lightStatus  大灯开关 0:无效（不设置） 1:关闭 2:开启.默认值: 1:关闭
	 * @param speedMode    模式设置 0:无效（不设置） 1:低速 2:中速 3:高速.默认值: 2: 中速
	 * @param throttleStatus  油门响应 0:无效（不设置） 1:关闭 2:开启.默认值: 1:关闭
	 * @param tailLight  后灯闪烁 0:无效（不设置） 1:关闭 2:开启.默认值: 1:关闭
	 * @return
	 */
	public static byte[] getScooterSet1Command( String IMEI,int lightStatus,int speedMode,int throttleStatus,int tailLight){
		//S7
		String content =String.format(",%s,%s,%s,%s#",lightStatus,speedMode,throttleStatus,tailLight);
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI, Command.ORDER_SCOOTER_SET1,content);
	}
	
	/**
	 * 滑板车设置指令2
	 * @param IMEI 设备IMEI号
	 * @param inchStatus  英制速度显示 0:无效（不设置） 1:关闭 2:开启. 默认值: 1:关闭 
	 * @param cruiseControl  定速巡航   0-无效 （不设置）  1:关闭 2:开启 . 默认值: 1:关闭 
	 * @param startType     启动方式    0:无效（不设置） 1:非零启动 2:零启动 默认值: 1:非零启动
	 * @param changeMode 按键切换模式 0:无效（不设置） 1:关闭 2:开启    默认值: 2:开启
	 * @param changeLight 按键开关大灯 0:无效（不设置） 1:关闭 2:开启    默认值: 2:开启
	 * @param lowSpeedLimit 低速模式限速值 0:无效（不设置） 范围：6-25 km/h
	 * @param midSpeedLimit 中速模式限速值 0:无效（不设置）  范围：6-25 km/h
	 * @param highSpeedLimit 高速模式限速值 0:无效（不设置）  范围：6-25 km/h
	 * @return
	 */
	public static byte[] getScooterSet2Command( String IMEI,int inchStatus,int cruiseControl,int startType,int changeMode,int changeLight,int lowSpeedLimit,int midSpeedLimit,int highSpeedLimit){
		//s4
		String content = String.format(",%s,%s,%s,%s,%s,%s,%s,%s#", inchStatus,cruiseControl,startType,  changeMode,changeLight,lowSpeedLimit,midSpeedLimit,highSpeedLimit);
		return getCommand(SEND_OMNI_HEAD,CODE_OM, IMEI, Command.ORDER_SCOOTER_SET2,content);
	}
	
	/**
	 * 发送语音播报指令
	 * @param IMEI
	 * @param voiceId  播放内容 1:预留 提示 2:找车提示 
	 * @return
	 */
	public static byte[] getVoiceCommand(String IMEI,int voiceId ){
		// v0
		String content = String.format(",%s#", voiceId );
		return getCommand(SEND_OMNI_HEAD,CODE_OM, IMEI, Command.ORDER_VOICE,content);
	}
	
	/**
	 * 获取定位指令
	 * @param IMEI  设备的IMEI号
	 * @return  指令数组
	 */
	public static byte[] getLocationCommand(String IMEI){
		//D0
		return getCommand(SEND_OMNI_HEAD,CODE_OM, IMEI, Command.ORDER_LOCATION,"#");
	}
	 
	/**
	 * 获取追踪指令
	 * 基于XM滑板车协议V1.7.0
	 * @param IMEI  设备的IMEI号
	 * @param trackInterval  上传定位间隔(单位:秒)。0-停止追踪，60-开启追踪，且60s上传一次定位信息。
	 * @return
	 */
	public static byte[] getTrackCommand(String IMEI ,int trackInterval){
		//D1  trackInterval
		String content = String.format(",%s#",trackInterval );
		return getCommand(SEND_OMNI_HEAD,CODE_OM, IMEI, Command.ORDER_TRACE,content);
	}
	
	/**
	 * 外部设备控制指令，目前用于控制电池盖开
	 * @param IMEI
	 * @param controlType 0-开启电池盖
	 * @return
	 */
	public static byte[] getControlExtrenalDevice(String IMEI ,int controlType){
		//L5  controlType
		String content = String.format(",%s#",controlType );
		return getCommand(SEND_OMNI_HEAD,CODE_OM, IMEI, Command.ORDER_CONTROL_EXTRENAL_DEVICE,content);
	}
	
	/**
	 * 获取固件版本
	 * @param IMEI
	 * @return
	 */
	public static byte[] getVersionCommand( String IMEI){
		// g0
		return getCommand(  SEND_OMNI_HEAD,CODE_OM,IMEI, Command.ORDER_VERSION,"#");
	}
	 
	public static byte[] getVersionCommand( Long IMEI){
		// g0
		return getVersionCommand( String.valueOf(IMEI));
	}
	
	/**
	 * 设置 device key
	 * @param IMEI 设备IMEI号
	 * @param deviceKey  8字节 device key
	 * @return 指令数组
	 */
	public static byte[] getSetDeviceKey( String IMEI,String deviceKey){
		String content =String.format(",%s,%s#",1,deviceKey);
		return getCommand( SEND_OMNI_HEAD,CODE_OM,IMEI,Command.ORDER_DEVICE_KEY,content);
	}
	/**
	 * 读取 device key
	 * @param IMEI  设备IMEI号
	 * @return  指令数组
	 */
	public static byte[] getReadDeviceKey( String IMEI){
		String content =String.format(",%s,%s#",0,0);
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,Command.ORDER_DEVICE_KEY,content);
	}
	
	/**
	 * 获取 SIM 的 ICCID
	 * @param IMEI 设备IMEI号
	 * @return 指令数组
	 */
	public static byte[] getIccidCommand( String IMEI ){
		//I0
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,Command.ORDER_ICCID,"#");
	}
	
	/**
	 * 获取设备MAC地址
	 * @param IMEI 设备IMEI号
	 * @return 指令数组
	 */
	public static byte[] getMacCommand(String IMEI ){
		//M0
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,Command.ORDER_MAC,"#");
	}
	
	/**
	 * omni IOT 设备关机指令。进入关机，方便运输。
	 * @param IMEI 设备IMEI号
	 * @return 指令数组
	 */
	public static byte[] getShutDownCommand(String IMEI ){
		//S0
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,Command.ORDER_SLEEP,"#");
	}
	
	/**
	 * omni IOT 设备远程重启指令。远程重新启动IOT程序
	 * @param IMEI 设备IMEI号
	 * @return 指令数组
	 */
	public static byte[] getReStartCommand(String IMEI ){
		//S1
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,ICommand.ORDER_RESTART,"#");
	}
	public static byte[] getEventNotice(String IMEI,int eventType ){
		//S1
		String content =String.format(",%s#",eventType);
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,ICommand.ORDER_EVENT_NOTICE,content);
	}
	
	
	
	
	
	/**
	 * omni IOT 设备连接服务器的ip和端口
	 * @param IMEI 设备IMEI号
	 * @param ipMode 连接模式，ip或者域名， 0-ip,1-域名
	 * @param ip ip或者域名的值
	 * @param port 端口
	 * @return 指令数组
	 */
	public static byte[] getSetAddressCommand(String IMEI,int ipMode ,String ip,int port ){
		//S3
		String content =String.format(",%s,%s,%s#",ipMode,ip,port);
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,ICommand.ORDER_SET_ADDRESS,content);
	}
 
	/**
	 *  omni IOT 设备 SIM联系信息   SIM 的APN值，卡的用户名和密码
	 * @param IMEI
	 * @param apn APN 值
	 * @param apnMode 0 不验证用户及密码, 1验证用户名和密码
	 * @param user  用户名
	 * @param password 密码
	 * @return
	 */
	public static byte[] getSetAPNCommand(String IMEI,String apn ,int apnMode,String user,String password ){
		//S3
		String content =String.format(",%s,%s,%s,%s#",apn,apnMode,user,password);
		return getCommand(SEND_OMNI_HEAD,CODE_OM,IMEI,ICommand.ORDER_SIM_SET,content);
	}
	
	/**
	 * U0 指令，发送升级文件数据
	 * @param IMEI
	 * @param totalPack 升级数据总包数(按照每包1-128 byte 分包得到的总包数)
	 * @param realLength 升级文件的实际长度，(不填充0的长度)
	 * @param crcInt 升级数据总 CRC16 校验值 (添充0[每包数据是128,不满123填充0,总包数据末尾可能有多余的0]后的总CRC值)
	 * @param deviceType 升级文件对应的设备识别码(8A-> omni Iot 设备  CT->滑板车控制器)
	 * @param upCode 升级秘钥（Vgz7）
	 * @return
	 */
	public static byte[] getStartUpComm(String IMEI,int totalPack,int realLength,int crcInt,String deviceType,String upCode){
		String content=String.format(",%s,%s,%s,%s,%s#", totalPack,realLength,crcInt,deviceType,upCode);
		return getCommand(  SEND_OMNI_HEAD,CODE_OM,IMEI, Command.ORDER_UPDATE_VERSION, content);
	}

	/**
	 * U1 指令，发送每包升级数据详情
	 * @param IMEI 设备IMEI号
	 * @param npack 第几包数据
	 * @param packSize 该包数据大小
	 * @param crc 改包数据的CRC值。
	 * @param updateItem 改包升级数据byte[]
	 * @return 指令数组
	 */
	public static byte[] getUpdateDetailComm( String IMEI, int npack,int packSize,int crc,byte[] updateItem){
		String sImei = String.format("%015d", Long.parseLong(IMEI));
		String commad = String.format("%s,%s,%s,%s,%s,%s,%s,",SEND_OMNI_HEAD,CODE_OM,sImei,Command.ORDER_UPDATE,npack,packSize,crc);
		byte[] orderContent = commad.getBytes();
		byte[] t = add(orderContent,updateItem);
		String end = "#\n";
		t = add(t,end.getBytes());
		return add(order00,t);
	}

}
