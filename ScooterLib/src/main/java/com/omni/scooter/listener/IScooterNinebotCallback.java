package com.omni.scooter.listener;

import org.apache.mina.core.session.IoSession;

 
import com.omni.scooter.entity.ScooterInfo1;
import com.omni.scooter.entity.ScooterInfo2;
import com.omni.scooter.entity.ninebot.ScooterSet2Entity;
import com.omni.scooter.entity.ninebot.VoiceEntity;
import com.omni.scooter.entity.ninebot.UpdateResultEntity;
import com.omni.scooter.entity.ninebot.UpdateVersionEntity;
import com.omni.scooter.entity.ninebot.VersionEntity;
import com.omni.scooter.entity.ninebot.ScooterSet1Entity;

 

public interface IScooterNinebotCallback extends IScooterCallback {
	   
	/**
	 * ninebot滑板车信息1的回调
	 * @param entity  滑板车信息回调信息实体
	 * @param session 连接会话对象
	 */
	void scooterInfo1CallBack(ScooterInfo1 entity,IoSession session);
	
	 
	/**
	 * ninebot滑板车信息2 的回调
	 * @param entity  滑板车信息2回调信息实体
	 * @param session 连接会话对象
	 */
	void scooterInfo2CallBack(ScooterInfo2 entity,IoSession session);
	
	 

	
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
	 * 基于协议版本v1.2.8
	 * @param entity  固件版本信息实体
	 * @param session 连接会话对象
	 */
	void versionCallBack(VersionEntity entity,IoSession session);
}
