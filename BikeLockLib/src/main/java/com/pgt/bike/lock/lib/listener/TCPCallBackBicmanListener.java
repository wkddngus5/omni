package com.pgt.bike.lock.lib.listener;

 

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.pgt.bike.lock.lib.entity.BikeConfigCommandVo;
import com.pgt.bike.lock.lib.entity.BikeInfoCommandVo;
import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.entity.ControlRequestCommandVo;
import com.pgt.bike.lock.lib.entity.GPSEntity;
import com.pgt.bike.lock.lib.entity.ninebot.DeviceIccidEntity;
import com.pgt.bike.lock.lib.entity.ninebot.DeviceKeyEntity;
import com.pgt.bike.lock.lib.entity.ninebot.DeviceMacEntity;
import com.pgt.bike.lock.lib.entity.ninebot.HeartEntity;
import com.pgt.bike.lock.lib.entity.ninebot.IotSetEntity;
import com.pgt.bike.lock.lib.entity.ninebot.LockCloseEntity;
import com.pgt.bike.lock.lib.entity.ninebot.LockOpenEntity;
import com.pgt.bike.lock.lib.entity.ninebot.ReportEntity;
import com.pgt.bike.lock.lib.entity.ninebot.ScooterInfo1;
import com.pgt.bike.lock.lib.entity.ninebot.VersionEntity;
import com.pgt.bike.lock.lib.entity.ninebot.VoiceEntity;
import com.pgt.bike.lock.lib.entity.ninebot.WarnEntity;
import com.pgt.bike.lock.lib.tcp.SessionMap; 
import com.pgt.bike.lock.lib.utils.BicmanCommandUtil;
import com.pgt.bike.lock.lib.utils.CommandUtil;

/**
 * 滑板车的指令回调
 * @author cxiaox
 * time 2018年6月23日
 */
public class TCPCallBackBicmanListener implements CallBackInterface{
	public static final boolean DEBUG=true;
	
	public static final String COMMAND_HEAD="*HBCR";

	public void setDeviceType(String deviceType) {
		
	}
	private void log(String msg){
		if(DEBUG) System.out.println(msg);
	}
	private void log(String TAG,String msg){
		if(DEBUG) System.out.println(String.format("%s %s", TAG,msg));
	}
	
	
	public void onFailure(String msg) {
		log("错误的指令格式："+msg);
	}

	public void onSuccessful(IoSession session, Object message) { 
		onSuccessful(session,message.toString());
	}
	
	public void onSuccessful(String msg) {
		
	}
	public void onMoreCommand(IoSession session,String msg){}
	
	public void onSuccessful(byte[] msg){ 
	
	}

	private void onSuccessful(IoSession session,String msg) { 
		// 以下是对外公开的方法
		onSuccessful(msg);
		onSuccessful(msg.getBytes());	
		
		int firstIndex = msg.indexOf('#');
		int lastIndex =msg.lastIndexOf('#');
		if(firstIndex==lastIndex){
			// 信息没有堵塞，只有一条指令过来 
		    // 找指令头
			int startIndex = msg.indexOf(COMMAND_HEAD);
			log("startIndex="+startIndex);
			String order = msg.substring(startIndex, lastIndex+1);
			log("order="+order);
			handCommand(session ,order);
		}else{ 
			// 多条指令过来
			onMoreCommand(session,msg);
			String[] strMsgChs = msg.split("#");
			for(String  oneStr :strMsgChs){
				log("oneStr="+oneStr);
				int startIndex = oneStr.indexOf(COMMAND_HEAD);
				log("startIndex="+startIndex);
				String oneComm=oneStr.substring(startIndex, oneStr.length());
				log("oneComm="+oneComm);
				handCommand(session ,String.format("%s#", oneComm));
			}
		}
	}
	
	
		
	private void handCommand(IoSession session,String command){
		log("接受到的指令:"+command);
		handCommand(session,command.getBytes());
	}
	

	private void handCommand(IoSession session,byte[] command){
	//	int orderCode = ((command[38]&0xFF)<<8)|(command[39]&0xFF);
		//*HBCR,NB,123456789123456,Q0,412,80#
		String sText = new String(command,0,command.length);
		// 设备类型
		String deviceCode =sText.split(",")[1];
		
		
//		if(!"NB".equals(deviceCode)){
//			// 错误的设备类型
//			errorCallBack(1001);
//			return ;
//		}
		
		
		String imei =sText.split(",")[2];
		// 指令类型
		String sOrder =sText.split(",")[3];
		byte[] sOrderBytes=sOrder.getBytes();
 
		int orderCode=((sOrderBytes[0]&0xFF)<<8)|(sOrderBytes[1]&0xFF);
		long IMEI = Long.parseLong(imei);
		switch (orderCode) {
		case Command.ORDER_REPORT_CODE:
			// 签到指令
			handReport(sText,session);
			break;
		case Command.ORDER_HEART_CODE:
			// 心跳包
			handHeart(sText,session);
			break;
		case Command.ORDER_LOCATION_CODE:
			// 定位的内容
			handGpsLocation(IMEI,command,session);
			break;
		case Command.ORDER_CONTROL_REQUEST_CODE:
			// 操作请求
			handControlRequest(IMEI,command,session);
			break;
		case Command.ORDER_LOCK_CODE:
			// 上锁的回调
			handScooterClose(sText,session);
			break;
		case Command.ORDER_SET_LOCK_CODE:
			// 设置锁状态的回调
			// 开锁回调
			handScooterOpen(sText,session);
			break;
		case Command.ORDER_SLEEP_CODE:
			// 关机指令的回调
			handSleep(IMEI,command,session);
			break;
		case Command.ORDER_FIND_BIKE_CODE:
			// 找车返回
			handFindBike(IMEI,command,session);
			break;
		case Command.ORDER_IOT_SET_INFO_CODE:
			// 锁信息  S5
			handLockInfo(sText,session);
			break;
		case Command.ORDER_BICMAN_INFO_CODE:
			// 车信息  S6
			handScooterInfo1(sText,session);
			break;
		case Command.ORDER_BICMAN_CONFIG_CODE:
			// 车配置信息
			handBicmanConfig(IMEI,command,session);
			break;
		case Command.ORDER_BICMAN_SHUTDOWN_CODE:
			// 车关机
			handBicmanShutDown(IMEI,command,session);
			break;
		case Command.ORDER_VERSION_CODE:
			// 版本号
			handVersion(IMEI,command,session);
			break;
		case Command.ORDER_SET_ADDRESS_CODE:
			handSetAddress(IMEI,command,session);
			break;
		case Command.ORDER_ALERT_CODE:
			
			handAlert(sText,session);
			break;
		case Command.ORDER_ICCID_CODE:
			handICCID(sText,session);
			break;
		 
		case Command.ORDER_UPDATE_CODE:
			handUpdateInfo(IMEI,command,session);
			break;
		case Command.ORDER_UPDATE_VERSION_CODE:
			handUpdateVersion(IMEI,command,session);
			break;
		case Command.ORDER_UPDATE_OK_CODE:
			handUpdateOK(IMEI,command,session);
			break;
		case Command.ORDER_SIM_SET_CODE:
			handSIMSetInfo(IMEI,command,session);
			break;
		case Command.ORDER_MAC_CODE:
			// 获取设备 MAC 地址
			handMacInfo(sText,session);
			break;
		case Command.ORDER_CONTROLLER_CODE:
			handControllerUpdateInfo(IMEI,command,session);
			break;
		case Command.ORDER_CONTROLLER_VERSION_CODE:
			handControllerUpdateVersion(IMEI,command,session);
			break;
		case Command.ORDER_CONTROLLER_OK_CODE:
			handControllerUpdateOK(IMEI,command,session);
			break;
		case Command.ORDER_DEVICE_KEY_CODE:
			handDeviceKey(sText,session);
			break;
		 
		case Command.ORDER_VOICE_CODE:
			handVoice(sText,session);
			break;
		default:
			break;
		}
	}
	
	private void handVoice(String content, IoSession session) {
		VoiceEntity entity = new VoiceEntity(content);
		updateSession(Long.valueOf(entity.getImei()),session);
		voiceCallBack(entity,session);
	}
	private void handBicmanShutDown(long imei, byte[] command, IoSession session) {
		String[] cont = new String(command,0,command.length).split(",");
		int status =getInt(cont[4]) ;
		bicmanShutDownCallback( imei,status,session);
	}
	public void bicmanShutDownCallback(long imei, int status, IoSession session) {
	 
		log("bicmanShutDownCallback", "status="+status);
		//*HBCR,NB,123456789123456,S2,1#
	}

	/**
	 * 滑板车语音播报回调
	 * @param entity
	 * @param session
	 */
	public void voiceCallBack(VoiceEntity entity, IoSession session) {
		log("voiceCallBack", entity.toString());
	}

 
	private void handControlRequest(long imei, byte[] command, IoSession session) {
	 
		String[] cont = new String(command,0,command.length).split(",");
		int action =getInt(cont[4]) ;
		int validSecond =getInt(cont[5]) ;
		int uid =getInt(cont[6]) ;
		long timestamp =getLong(cont[7]) ;
		
		ControlRequestCommandVo commandVo = new ControlRequestCommandVo(action, validSecond, uid, timestamp);
		commandVo.setImei(imei);
 
		 
		controlRequestCallback(commandVo,session);
	}


	 

	
	public void controlRequestCallback(ControlRequestCommandVo commandVo,
			IoSession session) {
		 log("controlRequestCallback","imei="+commandVo.getImei());
		 log("controlRequestCallback","action="+commandVo.getAction());
		 log("controlRequestCallback","请求获取的KEY="+commandVo.getKey());
		 log("controlRequestCallback","uid="+commandVo.getUid());
		 log("controlRequestCallback","时间戳="+commandVo.getTimestamp());
	}


	private void handDeviceKey(String content,IoSession session){
		
		DeviceKeyEntity entity = new DeviceKeyEntity(content);
		updateSession(Long.valueOf(entity.getImei()),session);
		deviceKeyCallBack(entity,session);
	}
	 
	private void handMacInfo(String content,IoSession session){
		DeviceMacEntity entity = new DeviceMacEntity(content);
		updateSession(Long.valueOf(entity.getImei()), session);
		deviceMacCallBack(entity, session); 
		
	}
	private void handSIMSetInfo(long IMEI,byte[] command,IoSession session){
		String[] cont = new String(command,0,command.length).split(",");
		String APN = cont[5];
		String PIN = cont[6] ;
		String PUK=cont[7];
		simSetCallback(IMEI,APN,PIN,PUK);
	}
	private void handUpdateVersion(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] temp = content.split(",");
		String iotCode =temp[4];
		String iotVersion =temp[5];
		String scooterControlVersion =temp[6];
		String scooterDisplayVersion =temp[7];
//		String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
		//U0
		updateVersionCallBack(IMEI,iotCode,iotVersion,scooterControlVersion,scooterDisplayVersion,session);
		
	}
	private void handControllerUpdateVersion(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] temp = content.split(",");
		int ver = getInt(temp[4]);
		String type =temp[5];
		String buildTime =temp[6];
		String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
		updateControllerVersionCallBack(IMEI,ver,type,buildTimeReal,session);
		
	}
	
	public void updateVersionCallBack(long IMEI,String iotCode,String iotVersion,String scooterControlVersion,String scooterDisplayVersion,IoSession session ){
		log("updateInfoCallBack  IMEI="+IMEI);
		log("updateInfoCallBack iotCode="+iotCode);
		log("updateInfoCallBack iotVersion="+iotVersion);
		log("updateInfoCallBack scooterControlVersion="+scooterControlVersion);
		log("updateInfoCallBack scooterDisplayVersion="+scooterDisplayVersion);
	}
	public void updateControllerVersionCallBack(long IMEI,int version,String deviceType,String buildTime,IoSession session ){
		 
	}
	
	private void handUpdateInfo(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] tStr=content.split(",");
		int tStrLen = tStr.length;
		int pack=getInt(tStr[4]);
		String deviceType = tStr[5];
		String deviceTypeReal=deviceType.substring(0,deviceType.length()-1);
		updateInfoCallBack(IMEI,pack,deviceTypeReal,session);
	}
	private void handControllerUpdateInfo(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] tStr=content.split(",");
		int tStrLen = tStr.length;
		int pack=getInt(tStr[4]);
		String deviceType = tStr[5];
		String deviceTypeReal=deviceType.substring(0,deviceType.length()-1);
		updateControlInfoCallBack(IMEI,pack,deviceTypeReal,session);
	}
	
	public void updateInfoCallBack(long IMEI,int pack,String deviceType,IoSession session ){
		log("updateInfoCallBack  IMEI="+IMEI);
		log("updateInfoCallBack pack="+pack);
		log("updateInfoCallBack deviceType="+deviceType);
		
	}
	public void updateControlInfoCallBack(long IMEI,int pack,String deviceType,IoSession session ){
	 
		
	}

	private void handUpdateOK(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		String[] cont = new String(command,0,command.length).split(",");
		if(cont.length==5){
			updateOKCallback(IMEI);
		}else{
			String version = cont[cont.length-2];
			String buildTime = cont[cont.length-1] ;
			String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
			updateOKCallback(IMEI,version,buildTimeReal);
		}
	}
	
	private void handControllerUpdateOK(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		String[] cont = new String(command,0,command.length).split(",");
		 
			String version = cont[cont.length-2];
			String buildTime = cont[cont.length-1] ;
			String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
			updateControllerOKCallback(IMEI,version,buildTimeReal);
	}
	
	public  void handFindBike(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);
		
		String[] cont = new String(command,0,command.length).split(",");
		String beepTimes = cont[4];
		
		findCallback(IMEI,getInt(beepTimes),session);
		
		
	}

	public void findCallback(long imei, int beepTimes, IoSession session) {
		 log("findCallback", "imei="+imei);
		 log("findCallback", "imei="+beepTimes);
		
	}
	private  void handAlert(String content,IoSession session){ 
		
		WarnEntity warnEntity = new WarnEntity(content);
		
		updateSession(Long.valueOf(warnEntity.getImei()),session);
		byte[] closeReport=BicmanCommandUtil.getReportCommand(  warnEntity.getImei(), Command.ORDER_ALERT);
		session.write(IoBuffer.wrap(closeReport));
		
		warnCallBack(warnEntity, session);
		 
	}
	
	private  void handICCID(String content,IoSession session){ 
		DeviceIccidEntity entity = new DeviceIccidEntity(content);
		updateSession(Long.valueOf(entity.getImei()),session);	 
		deviceIccidCallBack(entity, session);
	}
	 
	private  void handLockInfo(String  command,IoSession session){ 
		
		IotSetEntity entity = new IotSetEntity(command);
		
		updateSession(Long.valueOf(entity.getImei()),session);	
	    
		iotSetCallback(entity,session);
	}
	 
	public  void handScooterInfo1(String command,IoSession session){ 
		
		ScooterInfo1 info1 = new ScooterInfo1(command);
		updateSession(Long.valueOf(info1.getImei()),session);	
		scooterInfo1Callback(info1,session);
	}
	
	public  void handBicmanConfig(long IMEI,byte[] command,IoSession session){ 
		log("滑板车配置指令:"+new String(command,0,command.length));
		updateSession(IMEI,session);	
	    String[] sStr=new String(command,0,command.length).split(",");
 
		BikeConfigCommandVo commandVo = new BikeConfigCommandVo();
		commandVo.setLightMode(getInt(sStr[4]));
		commandVo.setSpeedMode(getInt(sStr[5]));
		commandVo.setAcceleratorMode(getInt(sStr[6]));
		commandVo.setAmode(getInt(sStr[7]));
		commandVo.setImei(IMEI);
		
		 
		bicmanConfigCallback(commandVo,session);
	}

	private void bicmanConfigCallback(BikeConfigCommandVo commandVo,
			IoSession session) {
		log("bicmanConfigCallback","大灯模式="+commandVo.getLightMode());
		log("bicmanConfigCallback","速度模式="+commandVo.getSpeedMode());
		log("bicmanConfigCallback","油门响应="+commandVo.getAcceleratorMode());
		log("bicmanConfigCallback","加速度计灵敏度="+commandVo.getAmode());
		
	}
	public  void handVersion(long IMEI,byte[] command,IoSession session){ 
		log("版本信息="+new String(command,0,command.length));
		updateSession(IMEI,session);
		String[] cont = new String(command,0,command.length).split(",");
		String iotVersion = cont[cont.length-4];
		String iotBuildTime = cont[cont.length-3] ;
		String bicmanVersion = cont[cont.length-2]; // 滑板车控制版本
		String buildTime = cont[cont.length-1] ;
		
		String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
		log("版本信息  version="+iotVersion);
		log("版本信息 buildTime="+buildTime);
		log("版本信息 buildTimeReal="+buildTimeReal);
		
		VersionEntity versionEntity = new VersionEntity(new String(command,0,command.length));
		versionCallback(versionEntity,session);
	 
	}
	

	public  void handSetAddress(long IMEI,byte[] command,IoSession session){ 
		
	}
	
	private  void handSleep(long IMEI,byte[] command,IoSession session){
		sleepCallBack(IMEI);
		sleepCallBack(IMEI,session);
		
	}
	private  void handReport(String command,IoSession session){
		// 保存session对象
		//将 IMEI号和电量回调出去
		ReportEntity entity = new ReportEntity(command);
		updateSession(Long.valueOf(entity.getImei()),session);
		reportCallback(entity, session);
	}
	private void handHeart(String command,IoSession session){
		HeartEntity entity = new HeartEntity(command);
		updateSession(Long.valueOf(entity.getImei()),session);
		//将 IMEI号和电量回调出去 
		heartCallback(entity,session);
	}
	 
	
	private void handGpsLocation(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
 
		byte[] locationReport=CommandUtil.getReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_LOCATION);
		session.write(IoBuffer.wrap(locationReport));
		String StrComm = new String(command,0,command.length);
		int startIndex = StrComm.indexOf("D0");
		String GPSContent =StrComm.substring(startIndex+3); 
		log("定位指令:"+GPSContent);
		if(GPSContent.split(",").length==13){
			GPSEntity gpsEntity=new GPSEntity(GPSContent);
			if(gpsEntity.isValid()){
				gpsLocationCallback(IMEI,gpsEntity,GPSContent);
				gpsLocationCallback(IMEI,gpsEntity,GPSContent,session);
			}else{
				gpsLocationCallback(IMEI,null,GPSContent);
				gpsLocationCallback(IMEI,null,GPSContent,session);
			}
		}else{
			gpsLocationCallback(IMEI,null,GPSContent);
			gpsLocationCallback(IMEI,null,GPSContent,session);
		}
	}
	
	private void handScooterClose(String content,IoSession session){
		LockCloseEntity entity = new LockCloseEntity(content);
		
		
		updateSession(Long.valueOf(entity.getImei()),session);
		byte[] closeReport=BicmanCommandUtil.getReportCommand(  entity.getImei(), Command.ORDER_RETURN_OFF);
		session.write(IoBuffer.wrap(closeReport));
		
		scooterCloseCallback(entity, session);
		
		
//		// 新指令结构解析
//		String[] tLockComm = new String(command).split(",");
//		int len = tLockComm.length;
//			// new command
//			// add uid,runTime
//		log("关锁指令指令: 新指令格式");
//		String status = tLockComm[len-4];
//		String uid = tLockComm[len-3];
//		String timestamp = tLockComm[len-2];
//		String runTime =tLockComm[len-1];// include '#'
//		closeLockCallback(IMEI, getInt(status) , getInt(uid),getLong(timestamp), getInt(runTime));
//		closeLockCallback(IMEI, getInt(status),getInt(uid),getLong(timestamp), getInt(runTime),session);
 
	}
	

	
	private void updateSession(Long IMEI,IoSession session){ 
		if(!SessionMap.newInstance().contaninsKey(IMEI)){
			System.out.println("session NOT KEY ="+IMEI +",ip="+session.getRemoteAddress());
			// 如果map不存在这个imei 号的KEY
			// 保存session对象
			SessionMap.newInstance().addSession(IMEI, session);
			return ;
		}
		IoSession writeSession = SessionMap.newInstance().getSession(IMEI);
//		writeSession.getId()
		if(! session.equals(writeSession)){
			System.out.println("session DIFF UPDATE="+IMEI+",new ip="+session.getRemoteAddress());
			System.out.println("session DIFF UPDATE="+IMEI+",old ip="+writeSession.getRemoteAddress());
			 // 当前这个IMEI 的读session 与写的不一样了
			//更新写的 session
			SessionMap.newInstance().addSession(IMEI, session);
		}
	}
	
 
	private void handScooterOpen(String  content,IoSession session){
		LockOpenEntity entity = new LockOpenEntity(content);
		updateSession(Long.valueOf(entity.getImei()),session);
		byte[] closeReport=BicmanCommandUtil.getReportCommand(  entity.getImei(), Command.ORDER_SET_LOCK);
		session.write(IoBuffer.wrap(closeReport));
		scooterOpenCallback(entity, session);
	}
	
	 
	public void sleepCallBack(long imei){
		
	}
	public void sleepCallBack(long imei,IoSession session){
		
	}
	
	/**
	 * 签到 指令回调 
	 * @param entity  签到回调实体对象
	 * @param session  TCP 连接对话
	 */
	public void reportCallback(ReportEntity entity,IoSession session){
		log("reportCallback entity="+entity.toString());
	}
	

	

	public void warnCallBack(WarnEntity entity, IoSession session) {
		
	}
	
 
	public void deviceIccidCallBack(DeviceIccidEntity entity, IoSession session) {
		
	}
	

	public void heartPeriodCallback(long imei,String period,IoSession session){
		
	}
	public void simSetCallback(long imei,String APN,String PIN,String PUK){
		log("imei="+imei);
		log("APN="+APN);
		log("PIN="+PIN);
		log("PUK="+PUK);
	}
	

	public void deviceMacCallBack(DeviceMacEntity entity, IoSession session) {
		
	}

 
	public void iotSetCallback( IotSetEntity entity ,IoSession session){}
	
	 
	public void scooterInfo1Callback(ScooterInfo1 info1,IoSession session){
		log("scooterInfo1Callback", "ScooterInfo1="+info1.toString());
		 
	}
	public void bicmanInfoCallback(BikeInfoCommandVo commandVo){}
	/**
	 * 硬件版本回调
	 * @param versionEntity  版本信息回调对象
	 * @param session  tcp连接对话
	 */
	public void versionCallback(VersionEntity versionEntity,IoSession session){
		 
		
	}
	public void updateOKCallback(long imei,String version,String buildTime){}
	public void updateControllerOKCallback(long imei,String version,String buildTime){}
	public void updateOKCallback(long imei ){}
 
	/**
	 * 心跳回调
	 * @param entity  心跳 回调对象实体
	 * @param session  连接对话
	 */
	public void heartCallback(HeartEntity entity, IoSession session ){
		log(String.format("heartCallback entity=%s", entity.toString() ));
	}
	/**
	 * 
	 * @param imei  IMEI号
	 * @param gpsEntity gps定位对象，解析不正确时返回null
	 * @param gpsContent gps定位返回的字符串
	 */
	public void gpsLocationCallback(long imei,GPSEntity gpsEntity,String gpsContent){
		if(gpsEntity!=null){
			log("imei="+imei);
			log("lat="+gpsEntity.getWGS48Lat());
			log("lng="+gpsEntity.getWGS48Lng());
		 
		}else{
			log("gpsContent="+gpsContent);
		}
	}
	public void gpsLocationCallback(long imei,GPSEntity gpsEntity,String gpsContent,IoSession session){
		if(gpsEntity!=null){
		 
		}
		
	}
	
	
	/**
	 * 滑板车 关锁回调
	 * @param entity
	 * @param session
	 */
	public void scooterCloseCallback(LockCloseEntity entity , IoSession session){
		log("scooterCloseCallback",entity.toString());
	}
	 
	/**
	 * 滑板车 开锁回调
	 * @param entity
	 * @param session
	 */
	public void scooterOpenCallback(LockOpenEntity entity, IoSession session){
		log("scooterOpenCallback", entity.toString());
	}
	
  
	
	/**
	 * 
	 * @param imei  上锁设备的IMEI号
	 * @param isFenceIn  true 表示在围栏内，false 表示在围栏外
	 * @param uid   骑行UID
	 * @param timestamp 开锁时间戳
	 * @param runTime  骑行时间
	 * @param session
	 */
	public void fenceCallback(long imei,boolean isFenceIn,int uid,long timestamp,int runTime,IoSession session){
		
		
		log("imei="+imei);
		log("isFenceIn="+isFenceIn);
		log("uid="+uid);
		log("timestamp="+timestamp);
		log("runTime="+runTime);
	}

	
	public void errorCallBack(int errorCode){
		// 1001 ,错误的设备类型
		log("errorCode="+errorCode);
	}
	
	 

	public void deviceKeyCallBack(DeviceKeyEntity entity, IoSession session) {
		log("deviceKeyCallBack="+entity.toString());
	}
	public void locationIntervalCallback(long imei,String locationInterval,IoSession session){
		log("imei="+imei);
		log("locationInterval="+locationInterval);
	}
	private int getInt(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return Integer.parseInt(content.trim());
		}else{
			return Integer.parseInt(content.substring(0,index).trim());
		}
		 
		
	}
	private Short getShort(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return Short.parseShort(content.trim());
		}else{
			return Short.parseShort(content.substring(0,index).trim());
		}
		 
		
	}
	
	private String getString(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return content;
		}else{
			return  (content.substring(0,index).trim());
		}
		 
		
	}
	private long getLong(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return Long.parseLong(content.trim());
			 
		}else{
			return Long.parseLong(content.substring(0,index).trim());
		}
	}
}
