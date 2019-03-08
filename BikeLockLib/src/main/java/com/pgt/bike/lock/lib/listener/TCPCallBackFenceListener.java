package com.pgt.bike.lock.lib.listener;

 

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.entity.GPSEntity;
import com.pgt.bike.lock.lib.tcp.SessionMap; 
import com.pgt.bike.lock.lib.utils.CommandUtil;

public class TCPCallBackFenceListener implements CallBackInterface{
	public static final boolean DEBUG=false;

	
	
	public void setDeviceType(String deviceType) {
		
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
			int startIndex = msg.indexOf("*BGFR");
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
				int startIndex = oneStr.indexOf("*BGFR");
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
		String commandText = new String(command,0,command.length);
		String[] t = commandText.split(",");
		int orderCode = ((command[25]&0xFF)<<8)|(command[26]&0xFF);
		String imei =new String (command,9,15);
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
		case Command.ORDER_LOCK_TIMESTAMP_REQUEST_CODE:
			 
			handTimestampRequest(IMEI,command,session);
			break;
		case Command.ORDER_LOCK_TIMESTAMP_SET_CODE:
			 
			handTimestampSet(IMEI,command,session);
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
		default:
			break;
		}
	}
	
	private void handFenceInfo(long IMEI,byte[] command,IoSession session){
		
		byte[] F1Report=CommandUtil.getBGFReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_FENCE);
		session.write(IoBuffer.wrap(F1Report));
		
		
		String[] cont = new String(command,0,command.length).split(",");
		String status = cont[4];
		String uid = cont[5];
		String timestamp = cont[6];
		String time = cont[7];
		int runTime=getInt(time);
		
		int fenceStatus = getInt(status);
		 
		fenceCallback(IMEI,fenceStatus==1,getInt(uid),getLong(timestamp),runTime,session);
		
	}
	
	private void handMacInfo(long IMEI,byte[] command,IoSession session){
		String[] cont = new String(command,0,command.length).split(",");
		String mac = cont[4];
		String macReal=mac.substring(0,mac.length()-1);
		macInfoCallback(IMEI,macReal);
		macInfoCallback(IMEI,macReal,session);
		
	}
	private void handSIMSetInfo(long IMEI,byte[] command,IoSession session){
		String[] cont = new String(command,0,command.length).split(",");
		String APN = cont[4];
		String PIN = cont[5] ;
		String PUK=cont[6];
		simSetCallback(IMEI,APN,PIN,PUK);
	}
	private void handUpdateVersion(long IMEI,byte[] command,IoSession session){
		String content = new String(command);
		String[] temp = content.split(",");
		int ver = getInt(temp[4]);
		String type =temp[5];
		String buildTime =temp[6];
		String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
		updateVersionCallBack(IMEI,ver,type,buildTimeReal,session);
		
	}
	
	public void updateVersionCallBack(long IMEI,int version,String deviceType,String buildTime,IoSession session ){
		log("updateInfoCallBack  IMEI="+IMEI);
		log("updateInfoCallBack version="+version);
		log("updateInfoCallBack deviceType="+deviceType);
		log("updateInfoCallBack buildTime="+buildTime);
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
	
	public void updateInfoCallBack(long IMEI,int pack,String deviceType,IoSession session ){
		log("updateInfoCallBack  IMEI="+IMEI);
		log("updateInfoCallBack pack="+pack);
		log("updateInfoCallBack deviceType="+deviceType);
		
	}

	private void handUpdateOK(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		String[] cont = new String(command,0,command.length).split(",");
		 
			String version = cont[cont.length-2];
			String buildTime = cont[cont.length-1] ;
			String buildTimeReal=buildTime.substring(0,buildTime.length()-1);
			updateOKCallback(IMEI,version,buildTimeReal);
		 
	}
	
	public  void handFindBike(long IMEI,byte[] command,IoSession session){ 
		String[] tContent = new String(command,0,command.length).split(",");
		String  beepTimes=tContent[4];
		
		updateSession(IMEI,session);
		
		findCallBack(IMEI,getInt(beepTimes),session);
		
	}

	private  void handAlert(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);
		
		// 报警会有返回
		byte[] alertReport=CommandUtil.getBGFReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_ALERT);
		session.write(IoBuffer.wrap(alertReport));
		
		String commandText = new String(command,0,command.length);
		
		String[] tComm = commandText.split(",");
		int alertStatus = getInt(tComm[4]) ;
		alertCallback(IMEI,alertStatus,session);
	}
	
	private  void handICCID(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);	
		String[] tContent = new String(command,0,command.length).split(",");
		String iccid = tContent[4];
		String realIccid=iccid.substring(0,iccid.length()-1);
		iccidCallback(IMEI,realIccid,session);
	}
	
	private  void handTimestampRequest(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);	 
		 
	//	timestampRequestCallback(IMEI, session);
	}
	private  void handTimestampSet(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);	 
		 
		String[] tContent = new String (command,0,command.length).split(",");
		String setNo = tContent[4];
		String realSetNo=setNo.substring(0,setNo.length()-1);
		timestampSetCallback(IMEI,realSetNo, session);
	}
	
	private  void handHeartPeriod(long IMEI,byte[] command,IoSession session){ 
		updateSession(IMEI,session);	 
		String Period=new String(command,41,command.length-42);
		heartPeriodCallback(IMEI,Period,session);
	}
	public  void handBikeInfo(long IMEI,byte[] command,IoSession session){ 
		log("获取信息指令:"+new String(command,0,command.length));
		updateSession(IMEI,session);	
	    String[] sStr=new String(command,0,command.length).split(",");
	    
	    String reportPower =sStr[4];
		String reportGSM = sStr[5];
		String reportFenceStatus = sStr[6];
		String reportLockStatus = sStr[7];
		String reportLockTimestamp = sStr[8];
 
		int power = getInt(reportPower);
		int gsm = getInt(reportGSM);
		int fenceStatus = getInt(reportFenceStatus);
		int lockStatus = getInt(reportLockStatus);
		long timestamp =getLong(reportLockTimestamp);
		
		 
		bikeInfoCallback(IMEI,power,gsm,fenceStatus,lockStatus,timestamp);
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
		versionBGFCallback(IMEI,version,buildTimeReal);
		versionBGFCallback(IMEI,version,buildTimeReal,session);
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
		String reportContent=new String(command,0,command.length);
		String[] t= reportContent.split(",");
		
		short power = getShort(t[4]);
		
		log("签到指令:"+reportContent);
		reportCallback(IMEI,command,new String(command,0,command.length));
		reportCallback(IMEI,power);
		reportCallback(IMEI,power,session);
	}
	private void handHeart(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		//将 IMEI号和电量回调出去
		String StrComm = new String(command,0,command.length);
		 
		// 新指令返回 1,123,23  [ 开锁状态，电量值,gsm]
		String[] heartStr=StrComm.split(",");
		 
		int lockStatus =getInt(heartStr[4].trim()); // 开锁状态
		short power=getShort(heartStr[5].trim()); // 电量
		int gsm = getInt(heartStr[6].trim()); // GSM值
		
		heartBGFCallback(IMEI,lockStatus,power,gsm);
		heartBGFCallback(IMEI,lockStatus,power,gsm,session);
		 
	}
	
	private void handGpsLocation(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
		byte[] locationReport=CommandUtil.getBGFReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_LOCATION);
		session.write(IoBuffer.wrap(locationReport));
		
		
		if(command.length==65){
			// 新指令格式
			String GPSContent =new String(command,0,command.length);
			log("定位指令:"+GPSContent);
			
			String[] tContent = GPSContent.split(",");
			String locationStatus = tContent[4];
			String slat = tContent[5];
			String latDirection = tContent[6];
			String slng = tContent[7];
			String lngDirection = tContent[8];
			String spdop = tContent[9];
			String shdop = tContent[10];
			String svdop = tContent[11];
			log("slat="+slat);
			log("slng="+slng);
			log("spdop="+spdop);
			double lat = getWGS84Lat(slat, latDirection);
			double lng = getWGS84Lng(slng, lngDirection);
			double pdop = getDouble(spdop);
			double hdop = getDouble(shdop);
			double vdop = getDouble(svdop);
			gpsFenceLocationCallback(IMEI,lat,lng,pdop,hdop,vdop,session);
			
		}else{
			String GPSContent =new String(command,28,command.length-29);
			System.out.println("gpsconent="+GPSContent);
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
		

		
	}
	
	private double getWGS84Lat(String latDm,String latDir){
		int dotIndex = latDm.indexOf('.'); // 小数点所在的位置
		// 出现异常// index out of range :-3,可能出现2233 这种数据及22度，33分
		// 2233.1
		String dd="0";
		String mm="0";
		//ddmm.mmmmm
		 
		if(dotIndex!=-1){
			//22.23 这种形式
			 try {
				dd = latDm.substring(0,dotIndex-2);
				mm = latDm.substring(dotIndex-2,latDm.length());
			} catch (Exception e) {
			 
			}
		}else{
			System.out.println("定位非标准格式数据 lat="+latDm);
			//2233 这种形式
			try {
				dd = latDm.substring(0, latDm.length()-2);
				mm = latDm.substring(latDm.length()-2, latDm.length());
			} catch (Exception e) {
			 
			}
		}
		
		double lat = Double.parseDouble(dd);
		lat += Double.parseDouble(mm) /60D;
		if("S".equals(latDir)){
			// 南半的纬度为 负数
			lat *=-1;
		}
		return lat;
	}
	private double getWGS84Lng(String lngDm,String lngDir){
		int dotIndex = lngDm.indexOf('.'); // 小数点所在的位置
		String dd="0";
		String mm="0";
		//12345.1111
		//
		//dddmm.mmmm
		 
	
		if(dotIndex!=-1){
			 try {
				dd = lngDm.substring(0,dotIndex-2);
				 mm = lngDm.substring(dotIndex-2,lngDm.length());
			} catch (Exception e) {
			}
		}else{
			//2233 这种形式
			//2233 这种形式
			try {
				dd = lngDm.substring(0, lngDm.length()-2);
				mm = lngDm.substring(lngDm.length()-2, lngDm.length());
			} catch (Exception e) {
			}
		}
		double lng = Double.parseDouble(dd);
		lng += Double.parseDouble(mm) /60D;
		
		if("W".equals(lngDir)){
			// 西经 为 负数
			lng *=-1;
		}
		return lng;
	}
	
	private void handLock(long IMEI,byte[] command,IoSession session){
		updateSession(IMEI,session);
 
		byte[] closeReport=CommandUtil.getBGFReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_OFF);
		session.write(IoBuffer.wrap(closeReport));
		// 新指令结构解析
		String[] tLockComm = new String(command).split(",");
		log("关锁指令指令: 新指令格式");
		String uid = tLockComm[4];
		String timestamp = tLockComm[5];
		String runTime =tLockComm[6];// include '#'
		lockBGFCallback(IMEI, getInt(uid),getLong(timestamp), getInt(runTime));
		lockBGFCallback(IMEI, getInt(uid),getLong(timestamp), getInt(runTime),session);
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
		byte[] closeReport=CommandUtil.getBGFReportCommand(Command.CODE, IMEI, Command.ORDER_RETURN_ON);
		session.write(IoBuffer.wrap(closeReport));
		
		String[] tSetLock=new String(command).split(",");
		 
			log("开锁指令: 新指令格式");
			String lockStatus=tSetLock[4];
			String uid  = tSetLock[5]; 
			String timestamp  = tSetLock[6]; // include '#'
		//	String sdelayTime = tSetLock[7];
			int status = getInt(lockStatus);// 转成了数值 0
		//	int delayTime = getInt(sdelayTime);
			
			setBGFLockCallback(IMEI,status==0,getInt(uid),getInt(timestamp));
			setBGFLockCallback(IMEI,status==0,getInt(uid),getInt(timestamp),session);
		 
	}
	
	public void reserveCallBack(long imei){
		
	}
	public void reserveCallBack(long imei,IoSession session){
		
	}
	public void sleepCallBack(long imei){
		log("关机");
		
	}
	public void sleepCallBack(long imei,IoSession session){
		
	}
	

	public void findCallBack(long imei,int beepTimes,IoSession session){
		log("FIND= imei"+imei+",beepTimes="+beepTimes);
	}
	
	/**
	 * 签到 
	 * @param imei  IMEI号
	 * @param power 电量
	 */
	public void reportCallback(long imei,short power){
		log("reportCallback  power="+power);
	
	}
	public void reportCallback(long imei,byte[] command,String content){
		
	}
	public void reportCallback(long imei,short power,IoSession session){
		
	}
	

	 
	public void alertCallback(long imei,int status,IoSession session){
		log("alertCallback  status="+status);
	}

	public void iccidCallback(long imei,String iccid,IoSession session){
		log("iccidCallback  iccid="+iccid);
	}
	

	public void heartPeriodCallback(long imei,String period,IoSession session){
		
	}

	private void timestampRequestCallback(long imei,IoSession session){
		log("请求时间戳  imei="+imei);
	}
	

	/**
	 * 锁主动请求 更新时间戳的回调
	 * @param imei
	 * @param session
	 */
	public void timestampSetCallback(long imei,String setNo,IoSession session){
		log("锁 设置时间戳请求：  imei="+imei+",setNo="+setNo);
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
	public void bikeInfoCallback(long imei,int power,int gsm,int fencestatus,int lockStatus,long lockTimestamp){
		String tag=String.format("imei=%s,power=%s,gsm=%s,fs=%s,lockStatus=%s,lts=%s", imei,power,gsm,fencestatus,lockStatus,lockTimestamp);
		log(tag);
		
	}
	/**
	 * 硬件版本回调
	 * @param version  版本号
	 * @param buildTime  版本编译时间
	 */
	public void versionBGFCallback(long imei,String version,String buildTime){}
	public void versionBGFCallback(long imei,String version,String buildTime,IoSession session){}
	public void updateOKCallback(long imei,String version,String buildTime){}
	public void updateOKCallback(long imei ){}
	 
	 
	 
	
	public void heartBGFCallback(long imei,int lockStatus,short power,int gsm){
		log(String.format("heartBGFCallback imei=%s,power=%s,lockStatus=%s,gsm=%s", imei,power,lockStatus,gsm));
	}
	public void heartBGFCallback(long imei,int lockStatus,short power,int gsm,IoSession session){
		log(String.format("heartBGFCallback imei=%s,power=%s,lockStatus=%s,gsm=%s", imei,power,lockStatus,gsm));
	}
	/**
	 * 
	 * @param imei  IMEI号
	 * @param gpsEntity gps定位对象，解析不正确时返回null
	 * @param gpsContent gps定位返回的字符串
	 */
	public void gpsLocationCallback(long imei,GPSEntity gpsEntity,String gpsContent){}
	public void gpsLocationCallback(long imei,GPSEntity gpsEntity,String gpsContent,IoSession session){
		if(gpsEntity!=null){
		log("imei="+imei);
		log("lat="+gpsEntity.getWGS48Lat());
		log("lng="+gpsEntity.getWGS48Lng());
		}
		
	}
	public void gpsFenceLocationCallback(long imei,double lat,double lng,double pdop,double hdop,double vdop,IoSession session){
		log("imei="+imei);
		log("lat="+lat);
		log("lng="+lng);
		log("pdop="+pdop);
		log("hdop="+hdop);
		log("vdop="+vdop);
	 
		
	}
 
	public void lockBGFCallback(long imei,int uid,long timestamp,int runTime){
		log("imei="+imei);
		log("uid="+uid);
		log("runTime="+runTime);
		log("timestamp="+timestamp);
	}
	public void lockBGFCallback(long imei,int uid,long timestamp,int runTime,IoSession session){
	}
	public void setBGFLockCallback(long imei,boolean isOpen,int uid,long timestamp){
		
		
	}
	public void setBGFLockCallback(long imei,boolean isOpen,int uid,long timestamp,IoSession session){
		
		log("imei="+imei);
		log("isOpen="+isOpen);
		log("uid="+uid);
		log("timestamp="+timestamp);
		
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
	
	private double getDouble(String content){
		// 判断是否末尾是否带有 #
		int index = content.indexOf('#');
		if(index==-1){
			// 没有# 
			return Double.parseDouble(content.trim());
			 
		}else{
			return Double.parseDouble(content.substring(0,index).trim());
		}
	}
}
