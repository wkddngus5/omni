package com.omni.scooter.test;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omni.scooter.command.NinebotCommand;
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
import com.omni.scooter.entity.WarnEntity;
import com.omni.scooter.entity.ninebot.ScooterSet1Entity;
import com.omni.scooter.entity.ninebot.ScooterSet2Entity;
import com.omni.scooter.entity.ninebot.UpdateResultEntity;
import com.omni.scooter.entity.ninebot.UpdateVersionEntity;
import com.omni.scooter.entity.ninebot.VersionEntity;
import com.omni.scooter.entity.ninebot.VoiceEntity;
import com.omni.scooter.listener.NinebotScooterListener;

public class MyNinetbotListener extends NinebotScooterListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyNinetbotListener.class);
	@Override
	public void reportCallBack(ReportEntity reportEntity, IoSession session) {
		 
		LOGGER.debug(reportEntity.toString());
	}

	@Override
	public void heartCallBack(HeartEntity heartEntity, IoSession session) {
		LOGGER.debug(heartEntity.toString());
	}
	
	@Override
	public void controlRequestCallBack(ControlRequestEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
		int key=entity.getControlKey();
		String imei = entity.getImei();
		int uid = entity.getUserId();
		
		
		if(entity.getControlType() == ControlRequestEntity.TYPE_OPEN){
			// send open command
			long openId=entity.getControlRequestId();
			byte[] openCommand = NinebotCommand.getOpenLockCommand(imei, key, uid, openId);
			session.write(IoBuffer.wrap(openCommand));
		}else if(entity.getControlType() == ControlRequestEntity.TYPE_CLOSE){
			// send close command
			byte[] closeCommand = NinebotCommand.getCloseLockCommand(imei, key);
			session.write(IoBuffer.wrap(closeCommand));
		}
	}
	
	@Override
	public void lockOpenCallBack(OpenLockEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void lockCloseCallBack(LockCloseEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void iotSetCallBack(IotSetEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void scooterInfo1CallBack(ScooterInfo1 entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void scooterInfo2CallBack(ScooterInfo2 entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void scooterSet1CallBack(ScooterSet1Entity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	@Override
	public void scooterSet2CallBack(ScooterSet2Entity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void warnCallBack(WarnEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void voiceCallBack(VoiceEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void powerControlCallBack(PowerControlEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void locationCallBack(LocationEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void versionCallBack(VersionEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	 
	
	@Override
	public void errorCallBack(ErrorEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void updateVersionCallBack(UpdateVersionEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	public void updateDetailCallBack(com.omni.scooter.entity.UpdateDetailEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	};
	
	@Override
	public void updateResultCallBack(UpdateResultEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void deviceKeyCallBack(DeviceKeyEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void deviceIccidCallBack(DeviceIccidEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
	
	@Override
	public void deviceMacCallBack(DeviceMacEntity entity, IoSession session) {
		LOGGER.debug(entity.toString());
	}
}
