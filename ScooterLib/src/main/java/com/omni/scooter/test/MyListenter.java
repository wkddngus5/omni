package com.omni.scooter.test;

 
 
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omni.lock.upfile.DeviceType;
import com.omni.lock.upfile.UpFileUtil;
import com.omni.scooter.command.ICommand;
import com.omni.scooter.command.OmniCommand;
import com.omni.scooter.entity.BaseEntity;
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
import com.omni.scooter.entity.ScooterInfo;
import com.omni.scooter.entity.UpdateDetailEntity;
import com.omni.scooter.entity.VersionEntity;
import com.omni.scooter.entity.WarnEntity;
import com.omni.scooter.entity.omni.ControlExtrDeviceEntity;
import com.omni.scooter.entity.omni.EventNoticeEntity;
import com.omni.scooter.entity.omni.RFIDRequestEntity;
import com.omni.scooter.entity.omni.ScooterSet1Entity;
import com.omni.scooter.entity.omni.UpdateResultEntity;
import com.omni.scooter.listener.OmniScooterListener;
import com.omni.scooter.tcp.TCPService;

public class MyListenter extends OmniScooterListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MyListenter.class);
//	private static final Logger LOGGER = LogManager.getLogger(MyListenter.class);

	
	
	@Override
	public void onFailure(String msg) {
		LOGGER.debug("非法指令={}",msg);
		super.onFailure(msg);
	}
	
	public MyListenter() {
		super();
	}

	public MyListenter(String deviceType) {
		super(deviceType);
	}
	 

	@Override
	public void onSuccessful(IoSession session, Object message) {
		LOGGER.debug("接收到有效指令={}",message);
		super.onSuccessful(session, message);
	}
	
	@Override
	public void doHandCommand(IoSession session, String message) {
		
		LOGGER.debug("准备处理指令={}",message);
		super.doHandCommand(session, message);
	}

	@Override
	public void reportCallBack(ReportEntity reportEntity, IoSession session) {
		LOGGER.debug(reportEntity.toString());
		
	}

	@Override
	public void heartCallBack(HeartEntity heartEntity, IoSession session) {
		LOGGER.debug(heartEntity.toString());
	}
	
	@Override
	public void controlRequestCallBack(ControlRequestEntity controlRequestEntity, IoSession session) {
		LOGGER.debug(controlRequestEntity.toString());
		String imei = controlRequestEntity.getImei();
		int key = controlRequestEntity.getControlKey();
		if(controlRequestEntity.getControlType()==ControlRequestEntity.TYPE_OPEN){
			// 发送开锁指令 
			int uid = 1;
			long timestamp = System.currentTimeMillis()/1000;
			byte[] open = OmniCommand.getOpenLockCommand(imei, key, uid, timestamp);
			session.write(IoBuffer.wrap(open));
		}else if(controlRequestEntity.getControlType()==ControlRequestEntity.TYPE_RFID_OPEN){
			int uid = 1;
			long timestamp = System.currentTimeMillis()/1000;
			byte[] open = OmniCommand.getOpenLockCommand(imei, key, uid, timestamp);
			session.write(IoBuffer.wrap(open));
		}else if(controlRequestEntity.getControlType()==ControlRequestEntity.TYPE_RFID_CLOSE){
			// 发送关锁指令 
			byte[] close= OmniCommand.getCloseLockCommand(imei, key);
			session.write(IoBuffer.wrap(close));
		}
		
		else{
			// 发送关锁指令 
			byte[] close= OmniCommand.getCloseLockCommand(imei, key);
			session.write(IoBuffer.wrap(close));
		}
	}
	
	@Override
	public void lockOpenCallBack(OpenLockEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
		 
	}
	
	@Override
	public void lockCloseCallBack(LockCloseEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}

	
	@Override
	public void iotSetCallBack(IotSetEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}

	@Override
	public void updateResultCallBack(UpdateResultEntity entity, IoSession session) {
		super.updateResultCallBack(entity, session);
	}

	@Override
	public void updateVersionCallBack(com.omni.scooter.entity.omni.UpdateVersionEntity entity, IoSession session) {
		super.updateVersionCallBack(entity, session);
	}

	@Override
	public void scooterInfoCallBack(ScooterInfo entity, IoSession session) {
		LOGGER.info(entity.toString());
	}

	@Override
	public void scooterSet1CallBack(ScooterSet1Entity entity, IoSession session) {
		super.scooterSet1CallBack(entity, session);
	}

	@Override
	public void scooterSet2CallBack(com.omni.scooter.entity.omni.ScooterSet2Entity entity, IoSession session) {
		super.scooterSet2CallBack(entity, session);
	}

	@Override
	public void voiceCallBack(com.omni.scooter.entity.omni.VoiceEntity entity, IoSession session) {
		super.voiceCallBack(entity, session);
	}

	@Override
	public void warnCallBack(WarnEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}

 
	
	@Override
	public void powerControlCallBack(PowerControlEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	
	@Override
	public void locationCallBack(LocationEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
		
		
		
		if(entity.getGpsEntity()==null){
			LOGGER.info("无效定位");
		}else{
			
			boolean isTrack=entity.getGpsEntity().isTrack();
			LOGGER.info("isTrack={} ",isTrack);
			
			LOGGER.info("imei={},lat={},lng={}",entity.getImei(),entity.getGpsEntity().getWGS84Lat(),entity.getGpsEntity().getWGS84Lng());
		}
	}
	
	@Override
	public void versionCallBack(VersionEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	
	@Override
	public void errorCallBack(ErrorEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	
	 
	
	@Override
	public void updateDetailCallBack(UpdateDetailEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
		if(ICommand.DEVICE_TYPE_IOT_OMNI.equals( entity.getDeviceType())){
			String imei = entity.getImei();
			int npack = entity.getPack();
			int packSize = 128; // 该包发送的字节数
			int crc = UpFileUtil.getUpdateCRC(DeviceType.SCOOTER_IOT_OMNI, npack);
			byte[] updateItem = UpFileUtil.getUpdateBytes(DeviceType.SCOOTER_IOT_OMNI, npack);
			
			byte[] command = OmniCommand.getUpdateDetailComm(imei, npack, packSize, crc, updateItem);
			 
			TCPService.sendOrder(imei, command);
			
		}
	}
	
	 
	
	@Override
	public void deviceKeyCallBack(DeviceKeyEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	
	@Override
	public void deviceMacCallBack(DeviceMacEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	
	@Override
	public void deviceIccidCallBack(DeviceIccidEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	
	
	@Override
	public void controlExtrenalDeviceCallBack(ControlExtrDeviceEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	 
	
	@Override
	public void rfIdRequestCallBack(RFIDRequestEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
		if(entity.getControlType()==RFIDRequestEntity.TYPE_CONTROL_OPEN){
			// 通过 R0 指令发送 RFID 开锁指令
			String imei = entity.getImei();
			
			int action = 2;// rfid 卡 解锁操作
			// action = ControlRequestEntity.TYPE_RFID_OPEN  // rfid 卡 解锁操作
			int valid = 60 ; //R0 指令返回的KEY的 有效时间(单位:秒)
			int uid = 0;
			long timestamp = System.currentTimeMillis()/1000;
			
			byte[] openCommand = OmniCommand.getControlRequestCommand(imei, action, valid, uid, timestamp);
			TCPService.sendOrder(imei, openCommand);
		}else if(entity.getControlType()==RFIDRequestEntity.TYPE_CONTROL_CLOSE){
			// 通过 R0 指令发送 RFID 关锁指令

			String imei = entity.getImei();
			int action = ControlRequestEntity.TYPE_RFID_CLOSE;// rfid 卡 关锁操作
			// action = 3   // rfid 卡 关锁操作
			int valid = 60 ; //R0 指令返回的KEY的 有效时间(单位:秒)
			int uid = 0;
			long timestamp = System.currentTimeMillis()/1000;
			
			byte[] openCommand = OmniCommand.getControlRequestCommand(imei, action, valid, uid, timestamp);
			TCPService.sendOrder(imei, openCommand);
		}
	}
	
	@Override
	public void eventNoticeCallBack(EventNoticeEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}

}
