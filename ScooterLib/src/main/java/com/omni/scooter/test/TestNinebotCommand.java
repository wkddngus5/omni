package com.omni.scooter.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omni.scooter.command.ICommand;
import com.omni.scooter.command.NinebotCommand;
import com.omni.scooter.command.OmniCommand;
import com.omni.scooter.utils.CommandUtil;

public class TestNinebotCommand {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestNinebotCommand.class.getSimpleName());
	public static void main(String[] args) {
		String IMEI="863158022988725";
		int uid = 1;
		long timestamp = System.currentTimeMillis()/1000;
		
		/**
		 * getControlRequestCommand 
		 * action -- 请求类型参数 说明
		 * ICommand.CONTROL_REQUEST_ACTION_OPEN  开锁请求
		 * ICommand.CONTROL_REQUEST_ACTION_CLOSE 关锁请求
		 */
		//操作请求-解锁
		long requestOpenId = timestamp;
		byte[] requestOpen = NinebotCommand.getControlRequestCommand(IMEI, ICommand.CONTROL_REQUEST_ACTION_OPEN, 30, uid, requestOpenId);
		//操作请求-关锁
		long requestCloseId = timestamp;
		byte[] requestClose = NinebotCommand.getControlRequestCommand(IMEI, ICommand.CONTROL_REQUEST_ACTION_CLOSE, 30, uid, requestCloseId);
	
		
		
		//开锁指令
		byte[] openCommand = NinebotCommand.getOpenLockCommand(IMEI, 30, uid, timestamp);
		//关锁指令
		byte[] closeCommand = NinebotCommand.getCloseLockCommand(IMEI, 30);
		// IOT 设置指令
		byte[] iotSet = NinebotCommand.getIotSetCommand(IMEI, 0, 0, 200, 0);
		// 获取 滑板车信息1指令
		byte[] scooterInfo1 = NinebotCommand.getScooterInfo1Command(IMEI);
		// 获取 滑板车信息2指令
		byte[] scooterInfo2 = NinebotCommand.getScooterInfo2Command(IMEI);
		// 滑板车设置指令1
		byte[] scooterSet1 = NinebotCommand.getScooterSet1Command(IMEI, 1, 2, 3, 4,5);
		// 滑板车设置指令2
		byte[] scooterSet2 = NinebotCommand.getScooterSet2Command(IMEI, 1, 2, 3, 4, 5,6,7);
		// 获取声音
		byte[] voice = NinebotCommand.getVoiceCommand(IMEI, ICommand.VOICE_FIND);
		byte[] powerControl = NinebotCommand.getPowerControlCommand(IMEI, 1);
		// 获取定位指令
		byte[] location = NinebotCommand.getLocationCommand(IMEI);
		// 获取追踪指令
		byte[] trace = NinebotCommand.getTraceCommand(IMEI,60);
		// 获取版本指令
		byte[] version = NinebotCommand.getVersionCommand(IMEI);
		
		/**
		 * getStartUpComm 
		 * deviceType  升级设备类型 参数
		 * ICommand.DEVICE_TYPE_IOT_SEGWAY         iot设备 软件程序
		 * ICommand.DEVICE_TYPE_SEGWAY_CONTROL     滑板车控制器 软件程序
		 * ICommand.DEVICE_TYPE_SEGWAY_DISPLAY     滑板车仪表 软件程序
		 * ICommand.DEVICE_TYPE_SEGWAY_BATTERY1           滑板车内置电池 软件程序
		 * ICommand.DEVICE_TYPE_SEGWAY_BATTERY2           滑板车外置电池 软件程序
		 */
		// 发送升级指令-升级 iot 程序
		byte[] startUpIot = NinebotCommand.getStartUpComm(IMEI, 220, 128, 3254, ICommand.DEVICE_TYPE_IOT_SEGWAY, "C7qn");
		// 发送升级指令-升级 控制器程序
		// {ICommand.D}
		 
		byte[] startUpControl = NinebotCommand.getStartUpComm(IMEI, 220, 128, 3254,ICommand.DEVICE_TYPE_SEGWAY_CONTROL, "C7qn");
		
		// 发送升级详情
		int npack = 1;
		int packSize = 128;
		int packCrc = 123;
		byte[] updateData = new byte[]{1,2,3};
		byte[] updateDetail = NinebotCommand.getUpdateDetailComm(IMEI,npack, packSize,packCrc, updateData);
		
		// 读取 设备 device key
		byte[] readDeviceKey = NinebotCommand.getReadDeviceKey(IMEI);
		// 设置 设备 device key
		byte[] deviceKey = NinebotCommand.getSetDeviceKey(IMEI, "12345678");
		
		// 获取MAC地址
		byte[] getMac = NinebotCommand.getMacCommand( IMEI);
		// 获取ICCID地址
		byte[] getIccid = NinebotCommand.getIccidCommand( IMEI);
		
		
		// 远程关机指令，内部使用
		byte[] shutdown = NinebotCommand.getShutDownCommand(IMEI);
//		byte[] restart = NinebotCommand.getReStartCommand(IMEI);
//		byte[] setaddress = NinebotCommand.getSetAddressCommand(IMEI, 0, "120.24.228.199", 9696);
//		byte[] setapn = NinebotCommand.getReStartCommand(IMEI);
		
		LOGGER.info("1.3.3  获取到解锁请求={}",new String(requestOpen));
		LOGGER.info("1.3.3 获取到关锁请求={}",new String(requestClose));
		LOGGER.info("1.3.4 获取到开锁指令={}",new String(openCommand));
		LOGGER.info("1.3.5 获取到关锁指令={}",new String(closeCommand));
		LOGGER.info("1.3.6 获取到iot设置指令={}",new String(iotSet));
		LOGGER.info("1.3.7 滑板车信息指令={}",new String(scooterInfo1));
		LOGGER.info("1.3.8 滑板车设置1指令={}",new String(scooterSet1));
		LOGGER.info("1.3.9 滑板车信息指令={}",new String(scooterInfo2));
		LOGGER.info("1.3.10 滑板车设置2指令={}",new String(scooterSet2));
		 
		LOGGER.info("1.3.12 voice指令={}",new String(voice));//
		LOGGER.info("1.3.13 S2 滑板车开关机控制={}",new String(powerControl));//
		LOGGER.info("1.3.14  定位指令={}",new String(location));//
		LOGGER.info("1.3.15  追踪指令={}",new String(trace));//
		
		
		LOGGER.info("1.3.16  版本指令={}",new String(version));//
		
		LOGGER.info("1.3.18 开始升级 iot指令={}",new String(startUpIot));
		LOGGER.info("1.3.18 开始升级 控制器指令={}",new String(startUpControl));
		LOGGER.info("1.3.19 升级包数据指令={}",new String(updateDetail));
		
		LOGGER.info("1.3.21 读取deviceKEY指令={}",new String(readDeviceKey));
		LOGGER.info("1.3.21 设置deviceKEY指令={}",new String(deviceKey));
		
		LOGGER.info("1.4.1 get iccid={}",new String(getIccid));
		LOGGER.info("1.4.2 get mac={}",new String(getMac));
		
		LOGGER.info("1.4.3  关机 shutdown={}",new String(shutdown));
//		LOGGER.info("内部使用  重启 restart={}",new String(restart));
//		LOGGER.info("内部使用   restart={}",new String(setaddress));
//		LOGGER.info("内部使用  apn={}",new String(setapn));
	}

}
