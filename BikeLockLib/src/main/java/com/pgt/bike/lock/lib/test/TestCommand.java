package com.pgt.bike.lock.lib.test;

import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.utils.CommandUtil;

public class TestCommand {
	
	public static void main(String[] args) {
		String code = Command.CODE;
		String IMEI ="63158022988725";
//		long IMEI =63158022988725;
		int openStatus = Command.LOCK_ON_CLEART;
		int uid = 0;
		long timestamp = System.currentTimeMillis()/1000;
		int delayTime = 30;
		byte[] openCommand = CommandUtil.getBGFLockCommand(code, IMEI, openStatus, uid, timestamp, delayTime);
	
		byte[] locationCommand =CommandUtil.getBGFLocationCommand(code, IMEI);
		byte[] infoCommand =CommandUtil.getBGFInfoCommand(code, IMEI);
		byte[] findCommand =CommandUtil.getBGFFindBikeCommand(code, IMEI, 5, 0);
		byte[] versionCommand =CommandUtil.getBGFVersionCommand(code, IMEI);
		byte[] macCommand =CommandUtil.getBGFMacCommand(code, IMEI);
		byte[] iccidCommand =CommandUtil.getBGFIccidCommand(code, IMEI);
		byte[] trCommand =CommandUtil.getBGFTimestampRequestCommand(code, IMEI);
		byte[] tsCommand =CommandUtil.getBGFTimestampSetCommand(code, IMEI, "1",timestamp);
		
		byte[] setipCommand =CommandUtil.getBGFSetIpCommand(code, IMEI,0,"120.24.228.199",9666,0);
		byte[] setapnCommand =CommandUtil.getBGFSetSIMCommand(code, IMEI, "APN","PIN","puk");
		byte[] setheartCommand =CommandUtil.getBGFHeartIntervalCommand(code, IMEI, 2400);
		byte[] setLocationIntervalCommand =CommandUtil.getLocationIntervalCommand(code, IMEI,  60);
		
		
		// 0-开启，1-关闭
		byte[] batteryPowerCommand =CommandUtil.getBatteryPower(code, IMEI, 0);
	
		System.out.println("开锁指令="+new String(openCommand,0,openCommand.length));
		System.out.println("定位指令="+new String(locationCommand,0,locationCommand.length));
		System.out.println("信息指令="+new String(infoCommand,0,infoCommand.length));
		System.out.println("找车指令="+new String(findCommand,0,findCommand.length));
		System.out.println("版本指令="+new String(versionCommand,0,versionCommand.length));
		System.out.println("mac指令="+new String(macCommand,0,macCommand.length));
		System.out.println("iccid指令="+new String(iccidCommand,0,iccidCommand.length));
		System.out.println("请求更新时间戳指令="+new String(trCommand,0,trCommand.length));
		System.out.println("设置时间戳指令="+new String(tsCommand,0,tsCommand.length));
		System.out.println("设置 ip 指令="+new String(setipCommand,0,setipCommand.length));
		System.out.println("设置 apn 指令="+new String(setapnCommand,0,setapnCommand.length));
		System.out.println("设置 heart 指令="+new String(setheartCommand,0,setheartCommand.length));
		System.out.println("设置 定位间隔 指令="+new String(setLocationIntervalCommand,0,setLocationIntervalCommand.length));
		System.out.println("设置  电池电源 开关 指令="+new String(batteryPowerCommand,0,batteryPowerCommand.length));
	
	
	}

}
