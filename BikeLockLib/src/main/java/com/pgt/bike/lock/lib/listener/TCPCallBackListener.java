package com.pgt.bike.lock.lib.listener;

 

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.entity.GPSEntity;
import com.pgt.bike.lock.lib.entity.LockBatteryPowerEntity;
import com.pgt.bike.lock.lib.entity.LockBikeInfoEntity;
import com.pgt.bike.lock.lib.entity.LockHeartEntity;
import com.pgt.bike.lock.lib.tcp.SessionMap; 
import com.pgt.bike.lock.lib.utils.CommandUtil;

public class TCPCallBackListener implements CallBackInterface{
	public static final boolean DEBUG= false;
	public static String DEVICE_TYPE ="OM";
	
	

	public TCPCallBackListener() {
		super();
	}
	/**
	 * 构造器
	 * @param deviceType  过滤的设备类型
	 */
	public TCPCallBackListener(String deviceType) {
		super();
		DEVICE_TYPE = deviceType;
	}

	public void setDeviceType(String deviceType){
		DEVICE_TYPE=deviceType;
	}
	

	private void log(String msg){
		if(DEBUG) System.out.println(msg);
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
			int startIndex = msg.indexOf("*CMDR");
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
				int startIndex = oneStr.indexOf("*CMDR");
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
	/**
	 * 错误的回调。
	 * 1001 ,是错误的设备类型
	 * @param errorCode
	 */
	private void errorCallBack(int errorCode,IoSession session){
		// 1001 ,错误的设备类型
		if(1001==errorCode){
			log(String.format("errorCode=%s,device type error", errorCode));
		}
		session.closeNow();
	 
	}

	private void handCommand(IoSession session,byte[] command){
	//	int orderCode = ((command[38]&0xFF)<<8)|(command[39]&0xFF);
		String sText = new String(command,0,command.length);
		String deviceType =sText.split(",")[1];
		String imei =sText.split(",")[2];
		String sorder =sText.split(",")[4];
		byte[] sorderbyte=sorder.getBytes();
		
		if(!DEVICE_TYPE.equals(deviceType)){
			// 不是过滤的值
			if(!"OM".equals(deviceType)){
				// 也不是出样的OM
				// 错误的设备类型-断开连接
				errorCallBack(1001,session);
				return ;
				
			}
			
		}
 
		int orderCode=((sorderbyte[0]&0xFF)<<8)|(sorderbyte[1]&0xFF);
		long IMEI = Long.parseLong(imei);
		switch (orderCode) {
		case Command.ORDER_REPORT_CODE:
			// 签到指令
			handReport(IMEI,command,session);
			break;
		case Command.ORDER_HEART_CODE:
			// 心跳包
			handHeart(IMEI,command,session);
			break;
		case Command.ORDER_LOCATION_CODE:
			// 定位的内容
			handGpsLocation(IMEI,command,session);
			break;
		case Command.ORDER_LOCK_CODE:
			// 上锁的回调
			handLock(IMEI,command,session);
			break;
		case Command.ORDER_SET_LOCK_CODE:
			// 设置锁状态的回调
			handSetLock(IMEI,command,session);
			break;
		case Command.ORDER_RESERVE_CODE:
			// 预约的回调
			handReserve(IMEI,command,session);
			break;
		case Command.ORDER_SLEEP_CODE:
			// 关机指令的回调
			handSleep(IMEI,command,session);
			break;
		case Command.ORDER_FIND_BIKE_CODE:
			// 找车返回
			handFindBike(IMEI,command,session);
			break;
		case Command.ORDER_INFO_CODE:
			handBikeInfo(IMEI,command,session);
			break;
		case Command.ORDER_VERSION_CODE:
			handVersion(IMEI,command,session);
			break;
		case Command.ORDER_SET_ADDRESS_CODE:
			handSetAddress(IMEI,command,session);
			break;
		case Command.ORDER_ALERT_CODE:
			handAlert(IMEI,command,session);
			break;
		case Command.ORDER_ICCID_CODE:
			handICCID(IMEI,command,session);
			break;
		case Command.ORDER_HEART_SET_CODE:
			handHeartPeriod(IMEI,command,session);
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
			handMacInfo(IMEI,command,session);
			break;
		case Command.ORDER_FENCE_CODE:
			handFenceInfo(IMEI,command,session);
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
			handDeviceKey(IMEI,command,session);
			break;
		case Command.ORDER_LOCATION_INTERVAL_CODE:
			handLocationInterval(IMEI,command,session);
			break;
			
		case Command.ORDER_BATTERY_LOCK_CODE:
			handBatteryPower(sText,session);
			break;
		default:
			break;
		}
	}
	
	private void handBatteryPower(String    command,IoSession session){
		
		LockBatteryPowerEntity entity = new LockBatteryPowerEntity(command);
		
		batteryPowerCallback(entity,session);
		
	}
	
	
	private void handFenceInfo(long IMEI,byte[] command,IoSession session){
		
		byte[] F1Report=CommandUtil.getReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_FENCE);
		session.write(IoBuffer.wrap(F1Report));
		
		
		String[] cont = new String(command,0,command.length).split(",");
		String status = cont[5];
		String uid = cont[6];
		String timestamp = cont[7];
		String time = cont[8];
		String realTime=time.substring(0,time.length()-1);
		
		int fenceStatus = getInt(status);
		
//		fenceCallback(IMEI,macReal);
		fenceCallback(IMEI,fenceStatus==1,getInt(uid),getLong(timestamp),getInt(realTime),session);
		
	}

	
	private void handDeviceKey(long IMEI,byte[] command,IoSession session){
		
		String[] cont = new String(command,0,command.length).split(",");
		String deviceKey = cont[5];
		String realDeviceKey=deviceKey.substring(0,deviceKey.length()-1);
	 
		deviceKeyCallback(IMEI,realDeviceKey,session);
		
	}
	private void handLocationInterval(long IMEI,byte[] command,IoSession session){
		
		String[] cont = new String(command,0,command.length).split(",");
		String locationInterval = cont[5];
		String realLocationInterval=locationInterval.substring(0,locationInterval.length()-1);
		
		locationIntervalCallback(IMEI,realLocationInterval,session);
		
	}
	private void handMacInfo(long IMEI,byte[] command,IoSession session){
		String[] cont = new String(command,0,command.length).split(",");
		String mac = cont[5];
		String macReal=mac.substring(0,mac.length()-1);
		macInfoCallback(IMEI,macReal);
		macInfoCallback(IMEI,macReal,session);
		
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
		int ver = getInt(temp[5]);
		String type =temp[6];
		String buildTime =temp[7];
		String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
		updateVersionCallBack(IMEI,ver,type,buildTimeReal,session);
		
	}
	private void handControllerUpdateVersion(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] temp = content.split(",");
		int ver = getInt(temp[5]);
		String type =temp[6];
		String buildTime =temp[7];
		String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
		updateControllerVersionCallBack(IMEI,ver,type,buildTimeReal,session);
		
	}
	
	public void updateVersionCallBack(long IMEI,int version,String deviceType,String buildTime,IoSession session ){
		log("updateInfoCallBack  IMEI="+IMEI);
		log("updateInfoCallBack version="+version);
		log("updateInfoCallBack deviceType="+deviceType);
		log("updateInfoCallBack buildTime="+buildTime);
	}
	public void updateControllerVersionCallBack(long IMEI,int version,String deviceType,String buildTime,IoSession session ){
		 
	}
	
	private void handUpdateInfo(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] tStr=content.split(",");
		int tStrLen = tStr.length;
		int pack=getInt(tStr[5]);
		String deviceType = tStr[6];
		String deviceTypeReal=deviceType.substring(0,deviceType.length()-1);
		updateInfoCallBack(IMEI,pack,deviceTypeReal,session);
	}
	private void handControllerUpdateInfo(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] tStr=content.split(",");
		int tStrLen = tStr.length;
		int pack=getInt(tStr[5]);
		String deviceType = tStr[6];
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
		
	}

	private  void handAlert(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);
		
		// 报警会有返回
		byte[] alertReport=CommandUtil.getReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_ALERT);
		session.write(IoBuffer.wrap(alertReport));
		
		String[] cont = new String(command,0,command.length).split(",");
		String reportAlert = cont[5];
		String reportAlertReal=reportAlert.substring(0,reportAlert.length()-1);
		alertCallback(IMEI,reportAlertReal,session);
		alertCallback(IMEI,getInt(reportAlertReal),session);
	}
	
	private  void handICCID(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);	 
		String[] sStr=new String(command,0,command.length).split(",");
		String iccid=sStr[5];
		String iccidReal=iccid.substring(0,iccid.length()-1);
		iccidCallback(IMEI,iccidReal,session);
	}
	private  void handHeartPeriod(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);	 
		
		String[] sStr=new String(command,0,command.length).split(",");
		String period=sStr[5];
		String periodReal=period.substring(0,period.length()-1);
		 
		
		heartPeriodCallback(IMEI,periodReal,session);
	}
	public  void handBikeInfo(long IMEI,byte[] command,IoSession session){ 
		log("获取信息指令:"+new String(command,0,command.length));
		updateSession(IMEI,session);	
	    String[] sStr=new String(command,0,command.length).split(",");
	    bikeInfoCallback(new LockBikeInfoEntity(new String(command)), session);
	    
	    
	    String reportPower =sStr[5];
		String reportGSM = sStr[6];
		String reportGpsNum = sStr[7];
		String reportLockStatus = sStr[8];
		String reportAlarm = sStr[9];
 
		int power = getInt(reportPower);
		int gsm = getInt(reportGSM);
		int gsmNum = getInt(reportGpsNum);
		int lockStatus = getInt(reportLockStatus);
		int alrm =getInt(reportAlarm);
		
		bikeInfoCallback(IMEI,power,gsm);
		bikeInfoCallback(IMEI,power,gsm,gsmNum,lockStatus,alrm);
		
	}

	public  void handVersion(long IMEI,byte[] command,IoSession session){ 
		log("版本信息="+new String(command,0,command.length));
		updateSession(IMEI,session);
		String[] cont = new String(command,0,command.length).split(",");
		String version = cont[cont.length-2];
		String buildTime = cont[cont.length-1] ;
		String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
		log("版本信息  version="+version);
		log("版本信息 buildTime="+buildTime);
		log("版本信息 buildTimeReal="+buildTimeReal);
		versionCallback(IMEI,version,buildTimeReal);
		versionCallback(IMEI,version,buildTimeReal,session);
	}
	

	public  void handSetAddress(long IMEI,byte[] command,IoSession session){ 
		
	}
	
	private  void handSleep(long IMEI,byte[] command,IoSession session){
		sleepCallBack(IMEI);
		sleepCallBack(IMEI,session);
		
	}
	private  void handReport(long IMEI,byte[] command,IoSession session){
		// 保存session对象
		updateSession(IMEI,session);
		//将 IMEI号和电量回调出去
		String reportContent = new String(command,0,command.length) ;
		String[] cont = new String(command,0,command.length).split(","); 
		String powerStr=cont[5].trim();
		
		log("签到指令:"+reportContent);
		reportCallback(IMEI,command,reportContent);
		short power=getShort(powerStr); // 旧锁会出现 " 56"的情况
		reportCallback(IMEI,power);
		reportCallback(IMEI,power,session);
	}
	private void handHeart(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		//将 IMEI号和电量回调出去 
		String StrComm = new String(command,0,command.length);
		heartCallback(IMEI,command,StrComm);
		heartCallback(new LockHeartEntity(StrComm),session);
		
		// 新指令返回 1,123,23  [ 开锁状态，电量值,gsm]
		String[] heartStr=StrComm.split(",");
		if(heartStr.length==6){
			heartCallback(IMEI,getShort(heartStr[5].trim()));
			heartCallback(IMEI,getShort(heartStr[5].trim()),session);
		}else if(heartStr.length==7){
			int lockStatus =getInt(heartStr[5].trim()); // 开锁状态
			short power=getShort(heartStr[6].trim()); // 电量
			
			heartCallback(IMEI,lockStatus,power);
			heartCallback(IMEI,lockStatus,power,session);
		}else if(heartStr.length==8){
			int lockStatus =getInt(heartStr[5].trim()); // 开锁状态
			short power=getShort(heartStr[6].trim()); // 电量
			int gsm = getInt(heartStr[7].trim()); // GSM值
			
			heartBGMCallback(IMEI,lockStatus,power,gsm);
			heartBGMCallback(IMEI,lockStatus,power,gsm,session);
		}else if(heartStr.length==9){
			int lockStatus =getInt(heartStr[5].trim()); // 开锁状态
			short power=getShort(heartStr[6].trim()); // 电量
			int gsm = getInt(heartStr[7].trim()); // GSM值
			int alarmStatus=getInt(heartStr[8].trim()); // 报警状态
			
			heartBGMCallback(IMEI,lockStatus,power,gsm,alarmStatus);
			heartBGMCallback(IMEI,lockStatus,power,gsm,alarmStatus,session);
		}
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
	
	private void handLock(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
//		System.out.println("返回 关锁 应答");
		byte[] closeReport=CommandUtil.getReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_OFF);
		session.write(IoBuffer.wrap(closeReport));
		// 新指令结构解析
		String[] tLockComm = new String(command).split(",");
		int len = tLockComm.length;
		if(len==5){
			// old command
			log("关锁指令指令: 旧指令格式");
			lockCallback(IMEI);
			lockCallback(IMEI,session);
		}else{
			// new command
			// add uid,runTime
			log("关锁指令指令: 新指令格式");
			String uid = tLockComm[len-3];
			String timestamp = tLockComm[len-2];
			String runTime =tLockComm[len-1];// include '#'
			lockBGMCallback(IMEI, getInt(uid),getLong(timestamp), getInt(runTime));
			lockBGMCallback(IMEI, getInt(uid),getLong(timestamp), getInt(runTime),session);
		}
//		lockCallback(IMEI);
	}
	

	
	private void updateSession(long IMEI,IoSession session){ 
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
	
	private void handReserve(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		reserveCallBack(IMEI);
		reserveCallBack(IMEI,session);
	}
	private void handSetLock(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		byte[] closeReport=CommandUtil.getReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_ON);
		session.write(IoBuffer.wrap(closeReport));
		
		String[] tSetLock=new String(command).split(",");
		if(tSetLock.length==6){
			//old
			log("开锁指令: 旧指令格式");
			String lockStatus=tSetLock[5];// include '#'
			int status = getInt(lockStatus);//除去'#' 转成了数值 0
			setLockCallback(IMEI,status==0);
			setLockCallback(IMEI,status==0,session);
		}else{
			// new
			log("开锁指令: 新指令格式");
			String lockStatus=tSetLock[5];
			String uid  = tSetLock[6]; 
			String timestamp  = tSetLock[7];// include '#'
			int status = getInt(lockStatus);// 转成了数值 0
			
			setBGMLockCallback(IMEI,status==0,getInt(uid),getInt(timestamp));
			setBGMLockCallback(IMEI,status==0,getInt(uid),getInt(timestamp),session);
		}
	}
	
	public void reserveCallBack(long imei){
		
	}
	public void reserveCallBack(long imei,IoSession session){
		
	}
	public void sleepCallBack(long imei){
		
	}
	public void sleepCallBack(long imei,IoSession session){
		
	}
	
	/**
	 * 签到 
	 * @param imei  IMEI号
	 * @param power 电量
	 */
	public void reportCallback(long imei,short power){
		log("reportCallback imei="+imei);
		log("power power="+power);
	
	}
	public void reportCallback(long imei,byte[] command,String content){
		
	}
	public void reportCallback(long imei,short power,IoSession session){
		
	}
	
	public void batteryPowerCallback(LockBatteryPowerEntity entity,IoSession session){
		
		log("batteryPowerCallback  "+entity.toString());
	}
	

	/**
	 * 单车锁报警上传
	 * @param imei 锁的IMEI号
	 * @param status  0-表示震动报警，1-表示跌倒报警
	 * @param session 连接对话
	 */
	public void alertCallback(long imei,String status,IoSession session){
		log("alertCallback imei="+imei);
		log("alertCallback status="+status);
	}

	public void alertCallback(long imei,int status,IoSession session){
		log("alertCallback imei="+imei);
		log("alertCallback status="+status);
	}

	public void iccidCallback(long imei,String iccid,IoSession session){
		
	}
	

	public void heartPeriodCallback(long imei,String period,IoSession session){
		
	}
	public void simSetCallback(long imei,String APN,String PIN,String PUK){
		log("imei="+imei);
		log("APN="+APN);
		log("PIN="+PIN);
		log("PUK="+PUK);
	}
	public void macInfoCallback(long imei,String mac){
	}
	public void macInfoCallback(long imei,String mac,IoSession session){
	}
	public void bikeInfoCallback(long imei,int power,int gsm){}
	public void bikeInfoCallback(long imei,int power,int gsm,int gsmNum,int lockStatus,int alrm){}
	public void bikeInfoCallback(LockBikeInfoEntity entity, IoSession session ){
		log("bikeInfoCallback = "+entity.toString());
	}
	/**
	 * 硬件版本回调
	 * @param version  版本号
	 * @param buildTime  版本编译时间
	 */
	public void versionCallback(long imei,String version,String buildTime){}
	public void versionCallback(long imei,String version,String buildTime,IoSession session){}
	public void updateOKCallback(long imei,String version,String buildTime){}
	public void updateControllerOKCallback(long imei,String version,String buildTime){}
	public void updateOKCallback(long imei ){}
	/**
	 * 心跳包
	 * @param imei IMEI号
	 * @param power 电量
	 */
	public void heartCallback(long imei,short power){
		log("heartCallback imei="+imei+" power="+power);
	}
	public void heartCallback(long imei,int lockStatus,short power){
		log("heartCallback imei="+imei+" power="+power+" lockStatus="+lockStatus);
	}
	public void heartCallback(long imei,byte[] command,String content){
		
	}

	public void heartCallback(LockHeartEntity entity,IoSession session){
		log("heartCallback ="+entity.toString());
		
	}
	public void heartCallback(long imei,short power,IoSession session){
		log("heartCallback imei="+imei+" power="+power);
	}
	public void heartCallback(long imei,int lockStatus,short power,IoSession session){
		log("heartCallback imei="+imei+" power="+power+" lockStatus="+lockStatus);
	}
	
	public void heartBGMCallback(long imei,int lockStatus,short power,int gsm){
		log(String.format("heartCallback imei=%s,power=%s,lockStatus=%s,gsm=%s", imei,power,lockStatus,gsm));
	}
	/**
	 * 心跳回调
	 * @param imei  锁的IMEI号
	 * @param lockStatus 开关锁状态(0-开,1表示关锁)
	 * @param power 电量值(单位0.01V)
	 * @param gsm  GSM 信息值
	 * @param alarmStatus 报警状态(0-无跌倒报警，1-跌倒报警)
	 */
	public void heartBGMCallback(long imei,int lockStatus,short power,int gsm,int alarmStatus){
		log(String.format("heartCallback imei=%s,power=%s,lockStatus=%s,gsm=%s,alarmStatus=%s", imei,power,lockStatus,gsm,alarmStatus));
	}
	public void heartBGMCallback(long imei,int lockStatus,short power,int gsm,IoSession session){
		log(String.format("heartCallback imei=%s,power=%s,lockStatus=%s,gsm=%s", imei,power,lockStatus,gsm));
	}
	/**
	 * 心跳回调
	 * @param imei  锁的IMEI号
	 * @param lockStatus 开关锁状态(0-开,1表示关锁)
	 * @param power 电量值(单位0.01V)
	 * @param gsm  GSM 信息值
	 * @param alarmStatus 报警状态(0-无跌倒报警，1-跌倒报警)
	 * @param session 连接对话
	 */
	public void heartBGMCallback(long imei,int lockStatus,short power,int gsm,int alarmStatus,IoSession session){
		log(String.format("heartCallback imei=%s,power=%s,lockStatus=%s,gsm=%s,alarmStatus=%s", imei,power,lockStatus,gsm,alarmStatus));
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
	
	public void lockCallback(long imei){}
	public void lockCallback(long imei,IoSession session){}
	public void lockBGMCallback(long imei,int uid,long timestamp,int runTime){
		log("imei="+imei);
		log("uid="+uid);
		log("runTime="+runTime);
		log("timestamp="+timestamp);
	}
	public void lockBGMCallback(long imei,int uid,long timestamp,int runTime,IoSession session){
	}
	public void setLockCallback(long imei,boolean isOpen){}
	public void setBGMLockCallback(long imei,boolean isOpen,int uid,long timestamp){
		log("imei="+imei);
		log("isOpen="+isOpen);
		log("uid="+uid);
		log("timestamp="+timestamp);
	}
	public void setBGMLockCallback(long imei,boolean isOpen,int uid,long timestamp,IoSession session){}
	public void setLockCallback(long imei,boolean isOpen,IoSession session){}
	
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

	public void deviceKeyCallback(long imei,String deviceKey,IoSession session){
		log("imei="+imei);
		log("deviceKey="+deviceKey);
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
