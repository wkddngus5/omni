package com.pgt.bike.lock.lib.test;

import com.pgt.bike.lock.lib.utils.BicmanCommandUtil;

public class TestBicmanCommand {
	
	public static void main(String[] args) {
		 
		String IMEI ="012345678910125";
	 
		int uid = 0;
		long timestamp = System.currentTimeMillis()/1000;
		int delayTime = 30;
		// 请求操作指令
		byte[] requestCommand = BicmanCommandUtil.getControlRequestCommand( IMEI, 0, 30, uid, timestamp);
		byte[] openCommand = BicmanCommandUtil.getOpenLockCommand(IMEI, 55, 0, timestamp) ;
		byte[] closeCommand = BicmanCommandUtil.getCloseLockCommand(IMEI, 55) ;
	
//		byte[] locationCommand =CommandUtil.getBGFLocationCommand(code, IMEI);
//		byte[] infoCommand =BicmanCommandUtil.getLockInfoCommand( IMEI);
		byte[] bicmanCommand =BicmanCommandUtil.getBicmanInfoCommand( IMEI);
		byte[] configCommand =BicmanCommandUtil.getBicmanConfigCommand( IMEI,0,1,0,0,0,0);
		byte[] findCommand =BicmanCommandUtil.getInfo2Command(IMEI) ;
		byte[] voiceCommand =BicmanCommandUtil.getVoiceCommand(  IMEI, 1);
//		byte[] versionCommand =CommandUtil.getBGFVersionCommand(code, IMEI);
		byte[] macCommand =BicmanCommandUtil.getMacCommand("SN", IMEI);
		byte[] iccidCommand =BicmanCommandUtil.getIccidCommand("SN", Long.parseLong(IMEI));
//		byte[] trCommand =CommandUtil.getBGFTimestampRequestCommand(code, IMEI);
//		byte[] tsCommand =CommandUtil.getBGFTimestampSetCommand(code, IMEI, "1",timestamp);
//		
//		byte[] setipCommand =CommandUtil.getBGFSetIpCommand(code, IMEI,0,"120.24.228.199",9666,0);
//		byte[] setapnCommand =CommandUtil.getBGFSetSIMCommand(code, IMEI, "APN","PIN","puk");
//		byte[] setheartCommand =CommandUtil.getBGFHeartIntervalCommand(code, IMEI, 2400);
//		byte[] setLocationIntervalCommand =CommandUtil.getLocationIntervalCommand(code, IMEI,  60);
	
		System.out.println("请求操作指令="+new String(requestCommand,0,requestCommand.length));
		System.out.println("开锁指令="+new String(openCommand,0,openCommand.length));
		System.out.println("关锁指令="+new String(closeCommand,0,closeCommand.length));
//		System.out.println("定位指令="+new String(locationCommand,0,locationCommand.length));
//		System.out.println("信息指令="+new String(infoCommand,0,infoCommand.length));
		System.out.println("滑板车信息指令="+new String(bicmanCommand,0,bicmanCommand.length));
		System.out.println("滑板车配置信息指令="+new String(configCommand,0,configCommand.length));
		System.out.println("获取滑板车信息2="+new String(findCommand,0,findCommand.length));
		System.out.println("播放语音指令="+new String(voiceCommand,0,voiceCommand.length));
//		System.out.println("版本指令="+new String(versionCommand,0,versionCommand.length));
		System.out.println("mac指令="+new String(macCommand,0,macCommand.length));
		System.out.println("iccid指令="+new String(iccidCommand,0,iccidCommand.length));
//		System.out.println("请求更新时间戳指令="+new String(trCommand,0,trCommand.length));
//		System.out.println("设置时间戳指令="+new String(tsCommand,0,tsCommand.length));
//		System.out.println("设置 ip 指令="+new String(setipCommand,0,setipCommand.length));
//		System.out.println("设置 apn 指令="+new String(setapnCommand,0,setapnCommand.length));
//		System.out.println("设置 heart 指令="+new String(setheartCommand,0,setheartCommand.length));
//		System.out.println("设置 定位间隔 指令="+new String(setLocationIntervalCommand,0,setLocationIntervalCommand.length));
	
	
	}

}
