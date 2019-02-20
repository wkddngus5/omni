package com.omni.scooter.listener;

import org.apache.mina.core.session.IoSession;

 
import com.omni.scooter.entity.ScooterInfo;
import com.omni.scooter.entity.UpdateOKEntity;
import com.omni.scooter.entity.VersionEntity;
import com.omni.scooter.entity.omni.UpdateVersionEntity;
import com.omni.scooter.entity.omni.VoiceEntity;
import com.omni.scooter.entity.omni.ControlExtrDeviceEntity;
import com.omni.scooter.entity.omni.EventNoticeEntity;
import com.omni.scooter.entity.omni.RFIDRequestEntity;
import com.omni.scooter.entity.omni.ScooterSet1Entity;
import com.omni.scooter.entity.omni.ScooterSet2Entity;
import com.omni.scooter.entity.omni.UpdateResultEntity;
 

 

public interface IScooterOmniCallback extends IScooterCallback {
	 

 
	
	/**
	 * OMNI滑板车信息的回调
	 * @param entity  滑板车信息回调信息实体
	 * @param session 连接会话对象
	 */
	void scooterInfoCallBack(ScooterInfo entity,IoSession session);
	
	
	/**
	 * OMNI滑板车设置1指令的回调
	 * @param entity  滑板车设置回调信息实体
	 * @param session 连接会话对象
	 */
	void scooterSet1CallBack(ScooterSet1Entity entity,IoSession session);
 
	
	/**
	 * OMNI滑板车设置2指令的回调
	 * @param entity  滑板车设置2回调信息实体
	 * @param session 连接会话对象
	 */
	void scooterSet2CallBack(ScooterSet2Entity entity,IoSession session);
	
	/**
	 * 语音播放指令的回调
	 * @param entity  语音播放回调信息实体
	 * @param session 连接会话对象
	 */
	void voiceCallBack(VoiceEntity entity,IoSession session);
	 
	
	/**
	 * OMNI 固件自动上报版本信息,检测自动升级的回调
	 * @param entity  自动上报版本信息实体
	 * @param session 连接会话对象
	 */
	void updateVersionCallBack(UpdateVersionEntity entity,IoSession session);
	  

	/**
	 * 固件升级结果的回调
	 * @param entity  升级成功内容信息实体
	 * @param session 连接会话对象
	 */
	void updateResultCallBack(UpdateResultEntity entity,IoSession session);
	
	/**
	 * 固件版本的回调
	 * @param entity  固件版本信息实体
	 * @param session 连接会话对象
	 */
	void versionCallBack(VersionEntity entity,IoSession session);
	
	/**
	 * RFID 请求开锁的回调
	 * 基于IOT协议v1.2.0
	 * @param entity  固件版本信息实体
	 * @param session 连接会话对象
	 */
	void rfIdRequestCallBack(RFIDRequestEntity entity,IoSession session);
	
	/**
	 * L5 解锁外部设备--目前是开电池盖
	 * 基于IOT协议v1.2.0
	 * @param entity  控制外面设备信息回调信息实体
	 * @param session 连接会话对象
	 */
	void controlExtrenalDeviceCallBack(ControlExtrDeviceEntity entity,IoSession session);
	
	void eventNoticeCallBack(EventNoticeEntity entity,IoSession session);
}
