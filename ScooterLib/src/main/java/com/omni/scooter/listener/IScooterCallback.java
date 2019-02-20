package com.omni.scooter.listener;

import org.apache.mina.core.session.IoSession;

import com.omni.scooter.entity.ControlRequestEntity;
import com.omni.scooter.entity.DeviceIccidEntity;
import com.omni.scooter.entity.DeviceKeyEntity;
import com.omni.scooter.entity.DeviceMacEntity;
import com.omni.scooter.entity.ErrorEntity;
import com.omni.scooter.entity.HeartEntity;
import com.omni.scooter.entity.IotSetEntity;
import com.omni.scooter.entity.LocationEntity;
import com.omni.scooter.entity.LockCloseEntity;
import com.omni.scooter.entity.OpenLockEntity;
import com.omni.scooter.entity.PowerControlEntity;
import com.omni.scooter.entity.ReportEntity;
import com.omni.scooter.entity.ScooterInfo1;
import com.omni.scooter.entity.ScooterInfo2;
import com.omni.scooter.entity.ScooterSet2Entity;
import com.omni.scooter.entity.ScooterSetEntity;
import com.omni.scooter.entity.UpdateDetailEntity;
import com.omni.scooter.entity.UpdateOKEntity;
import com.omni.scooter.entity.UpdateVersionEntity;
import com.omni.scooter.entity.VersionEntity;
import com.omni.scooter.entity.WarnEntity;
import com.omni.scooter.entity.ninebot.VoiceEntity;

 

public interface IScooterCallback extends CallBackInterface {
	
	void setFilterDeviceType(String deviceType);
	
	/**
	 * 签到信息的回调
	 * @param reportEntity 签到回调信息实体
	 * @param session 连接会话对象
	 */
	void reportCallBack(ReportEntity reportEntity,IoSession session);
	/**
	 * 心跳信息的回调
	 * @param heartEntity  心跳回调信息实体
	 * @param session 连接会话对象
	 */ 
	void heartCallBack(HeartEntity heartEntity,IoSession session);
	/**
	 * 请求操作信息的回调
	 * @param heartEntity  请求操作回调信息实体
	 * @param session 连接会话对象
	 */ 
	void controlRequestCallBack(ControlRequestEntity controlRequestEntity,IoSession session);
	
	/**
	 * 开锁信息的回调
	 * @param entity  开锁回调信息实体
	 * @param session 连接会话对象
	 */
	void lockOpenCallBack(OpenLockEntity entity,IoSession session);

	/**
	 * 关锁信息的回调
	 * @param entity  关锁回调信息实体
	 * @param session 连接会话对象
	 */
	void lockCloseCallBack(LockCloseEntity entity,IoSession session);

	/**
	 * IOT设备设置信息的回调
	 * @param entity  IOT设备设置回调信息实体
	 * @param session 连接会话对象
	 */
	void iotSetCallBack(IotSetEntity entity,IoSession session);
	
 
	
	
	/**
	 * 报警信息指令的回调
	 * @param entity  滑板车设置2回调信息实体
	 * @param session 连接会话对象
	 */
	void warnCallBack(WarnEntity entity,IoSession session);
	
	/**
	 * 滑板车电源开关机指令的回调。与shutdown 要区别开来.segway特有,segway的滑板车需要先开电源，在发送开锁指令才能开锁。
	 * @param entity  电源开关机回调信息实体
	 * @param session 连接会话对象
	 */
	void powerControlCallBack(PowerControlEntity entity,IoSession session);
	/**
	 * 定位指令的回调
	 * @param entity  定位回调信息实体
	 * @param session 连接会话对象
	 */
	void locationCallBack(LocationEntity entity,IoSession session);
	
	/**
	 * 控制器故障的回调
	 * @param entity  控制器故障信息实体
	 * @param session 连接会话对象
	 */
	void errorCallBack(ErrorEntity entity,IoSession session);
	
	
	/**
	 * 固件获取升级数据的回调
	 * @param entity  询问升级数据包内容信息实体
	 * @param session 连接会话对象
	 */
	void updateDetailCallBack(UpdateDetailEntity entity,IoSession session);
	
	
	/**
	 * 获取或者设置，设备KEY的回调
	 * @param entity  设备KEY信息实体
	 * @param session 连接会话对象
	 */
	void deviceKeyCallBack(DeviceKeyEntity entity,IoSession session);
	/**
	 * 获取设备MAC的回调
	 * @param entity  设备MAC信息实体
	 * @param session 连接会话对象
	 */
	void deviceMacCallBack(DeviceMacEntity entity,IoSession session);
	/**
	 * 获取设备SIM卡的ICCID的回调
	 * @param entity  设备ICCID信息实体
	 * @param session 连接会话对象
	 */
	void deviceIccidCallBack(DeviceIccidEntity entity,IoSession session);

}
