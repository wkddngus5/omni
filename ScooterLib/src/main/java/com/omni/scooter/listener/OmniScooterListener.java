package com.omni.scooter.listener;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omni.scooter.command.ICommand;
import com.omni.scooter.command.OmniCommand;
import com.omni.scooter.entity.BaseEntity;
import com.omni.scooter.entity.Command;
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
import com.omni.scooter.entity.omni.ScooterSet2Entity;
import com.omni.scooter.entity.omni.UpdateResultEntity;
import com.omni.scooter.entity.omni.ControlExtrDeviceEntity;
import com.omni.scooter.entity.omni.EventNoticeEntity;
import com.omni.scooter.entity.omni.RFIDRequestEntity;
import com.omni.scooter.entity.omni.ScooterSet1Entity;
import com.omni.scooter.entity.UpdateDetailEntity;
import com.omni.scooter.entity.UpdateOKEntity;
import com.omni.scooter.entity.omni.UpdateVersionEntity;
import com.omni.scooter.entity.VersionEntity;
import com.omni.scooter.entity.omni.VoiceEntity;
import com.omni.scooter.entity.WarnEntity;
import com.omni.scooter.tcp.SessionMap;
 
 

public class OmniScooterListener implements IScooterOmniCallback {
	private static final Logger LOGGER = LoggerFactory.getLogger(OmniScooterListener.class);
	private   String DeviceType ="OM";
	
	
	public OmniScooterListener() {
		super();
	}
	public OmniScooterListener(String deviceType) {
		super();
		DeviceType =deviceType;
	}
	
	


	public void setFilterDeviceType(String deviceType) {
		DeviceType =deviceType;
	}

	public void doHandCommand(IoSession session, String message) {
		 
		
		String deviceCode =message.split(",")[1];
		BaseEntity baseEntity = new BaseEntity(message);
		if(!DeviceType.equals(deviceCode)){
			//不是过滤的类型
			if(!"OM".equals(deviceCode)){
				// 也不是OM 类型
				deviceCodeErrorCallback(baseEntity,session);
				return ;
			}
		}
		
		
		
		String imei =message.split(",")[2];
		String sorder =message.split(",")[3];
		byte[] sorderbyte=sorder.getBytes();
		int orderCode=((sorderbyte[0]&0xFF)<<8)|(sorderbyte[1]&0xFF);
		updateSession(Long.valueOf(imei),session);
		
		switch (orderCode) {
		case Command.ORDER_REPORT_CODE:
			//1.3.1  签到
			reportCallBack(new ReportEntity(message),session);
			break;
		case Command.ORDER_HEART_CODE:
			//1.3.2  心跳
			heartCallBack(new HeartEntity(message),session);
			break;
		case Command.ORDER_CONTROL_REQUEST_CODE:
			//1.3.3  操作请求
			controlRequestCallBack(new ControlRequestEntity(message),session);
			break;
		case Command.ORDER_LOCK_OPEN_CODE:
			//1.3.4  开锁返回
			handLockOpen(  session,   message);
			break;
		case Command.ORDER_LOCK_CLOSE_CODE:
			//1.3.5  关锁返回
			handLockClose(  session,   message);
			break;
		case Command.ORDER_IOT_SET_CODE:
			//1.3.6 IOT设备返回
			iotSetCallBack(new IotSetEntity(message), session);
			break;
		case Command.ORDER_SCOOTER_INFO_CODE:
			//1.3.7  S6滑板车信息返回
			scooterInfoCallBack(new ScooterInfo(message), session);
			break;
		case Command.ORDER_SCOOTER_SET1_CODE:
			//1.3.8  S7滑板车设置1信息返回
			scooterSet1CallBack(new ScooterSet1Entity(message), session);
			break;
		 
		case Command.ORDER_SCOOTER_SET2_CODE:
			//1.3.9  S4滑板车设置2信息返回
			scooterSet2CallBack(new ScooterSet2Entity(message), session);
			break;
		case Command.ORDER_WARN_CODE:
			//1.3.10  W0 报警信息返回
			handWarn(session, message);
			break;
		case Command.ORDER_VOICE_CODE:
			//1.3.11  V0   语音播报信息返回
			voiceCallBack(new VoiceEntity(message), session);
			break;
	 
		case Command.ORDER_LOCATION_CODE:
			//1.3.12  D0 定位指令信息返回
			locationCallBack(new LocationEntity(message), session);
			break;
		case Command.ORDER_VERSION_CODE:
			//1.3.14  G0 固件信息返回
			versionCallBack (new VersionEntity(message), session);
			break;
		case Command.ORDER_ERROR_CODE:
			//1.3.15  E0 控制器故障代码信息返回
			handError(session, message);
			break;
		case Command.ORDER_UPDATE_VERSION_CODE:
			//1.3.16  U0 固件自动上传版本信息(用于自动升级，如果有需要)
			 updateVersionCallBack(new UpdateVersionEntity(message), session);
			break;
		case Command.ORDER_UPDATE_CODE:
			//1.3.17  U1 获取升级文件详情
			updateDetailCallBack(new UpdateDetailEntity(message),session);
			break;
		case Command.ORDER_UPDATE_OK_CODE:
			//1.3.18  U2  升级OK
			 updateResultCallBack(new UpdateResultEntity(message),session);
			break;
		case Command.ORDER_DEVICE_KEY_CODE:
			//1.3.19  K0  设备device key 返回
			deviceKeyCallBack(new DeviceKeyEntity(message),session);
			break;
		case Command.ORDER_MAC_CODE:
			//1.4.1  M0  获取设备MAC地址
			deviceMacCallBack(new DeviceMacEntity(message),session);
			break;
		case Command.ORDER_ICCID_CODE:
			//1.4.2  I0  获取SIM iccid
			deviceIccidCallBack(new DeviceIccidEntity(message),session);
			break;
		case Command.ORDER_RFID_REQUEST_CODE:
			rfIdRequestCallBack( new RFIDRequestEntity(message),session);
			break;
		case Command.ORDER_CONTROL_EXTRENAL_DEVICE_CODE:
			controlExtrenalDeviceCallBack( new ControlExtrDeviceEntity(message),session);
			break;
		case ICommand.ORDER_EVENT_NOTICE_CODE:
			eventNoticeCallBack(new EventNoticeEntity(message),session);
			break;
	
		default:
			break;
		}
	}
	
	
	private void  deviceCodeErrorCallback(BaseEntity baseEntity,IoSession session){
		session.closeNow();
	}
	

	public void eventNoticeCallBack(EventNoticeEntity entity, IoSession session) {
		 
		
	}

	public void controlExtrenalDeviceCallBack(ControlExtrDeviceEntity entity, IoSession session) {
		
	}

	public void rfIdRequestCallBack(RFIDRequestEntity entity, IoSession session) {
		 
		
	}

	private void handError(IoSession session, String message) {
		ErrorEntity entity = new ErrorEntity(message);
		byte[] response = OmniCommand.getResponseCommand(entity.getImei(), Command.ORDER_ERROR);
		session.write(IoBuffer.wrap(response));
		errorCallBack(entity, session);
	}

	private void handWarn(IoSession session, String message) {
		WarnEntity entity=new WarnEntity(message);
		byte[] response = OmniCommand.getResponseCommand(entity.getImei(), Command.ORDER_WARN);
		session.write(IoBuffer.wrap(response));
		warnCallBack(entity, session);
	}
	
	private void handLockClose(IoSession session, String message) {
		// receive the L1 command  from lock
		LockCloseEntity entity=new LockCloseEntity(message);
		// service response  L1 to lock
		byte[] response = OmniCommand.getResponseCommand(entity.getImei(), Command.ORDER_LOCK_CLOSE);
		session.write(IoBuffer.wrap(response));
		// L1  callback 
		lockCloseCallBack(entity, session);
	}

	private void handLockOpen(IoSession session, String message) {
		// receive the L0 command  from lock
		OpenLockEntity entity=new OpenLockEntity(message);
		// service response  L0 to lock
		byte[] response = OmniCommand.getResponseCommand(entity.getImei(), Command.ORDER_LOCK_OPEN);
		System.out.println("response="+new String(response));
		session.write(IoBuffer.wrap(response));
		// L0  callback 
		lockOpenCallBack(entity, session);
		
	}
 

	public void onFailure(String msg) {

	}

	public void onSuccessful(IoSession session, Object message) {

	}
	

	public void deviceIccidCallBack(DeviceIccidEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void deviceMacCallBack(DeviceMacEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
		
	}
	

	public void deviceKeyCallBack(DeviceKeyEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}


	public void updateResultCallBack(UpdateResultEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void updateDetailCallBack(UpdateDetailEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void updateVersionCallBack(UpdateVersionEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void errorCallBack(ErrorEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void versionCallBack(VersionEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}


	public void reportCallBack(ReportEntity reportEntity, IoSession session) {
		LOGGER.info(reportEntity.toString());
	}
	

	public void heartCallBack(HeartEntity heartEntity, IoSession session) {
		LOGGER.info(heartEntity.toString());
	}


	public void controlRequestCallBack(ControlRequestEntity controlRequestEntity, IoSession session) {
		LOGGER.info(controlRequestEntity.toString());
	}
	

	public void lockOpenCallBack(OpenLockEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}

	public void lockCloseCallBack(LockCloseEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void iotSetCallBack(IotSetEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void scooterInfoCallBack(ScooterInfo entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	 
	

	public void scooterSet1CallBack(ScooterSet1Entity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}

	public void scooterSet2CallBack(ScooterSet2Entity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void warnCallBack(WarnEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void voiceCallBack(VoiceEntity entity, IoSession session) {
		LOGGER.info(entity.toString());
	}
	

	public void powerControlCallBack(PowerControlEntity entity, IoSession session) {
		
	}
	

	public void locationCallBack(LocationEntity entity, IoSession session) {
		
	}
	

	public void onMoreCommand(IoSession session, String message) {
		
	}
	
	private void updateSession(long IMEI,IoSession session){ 
		if(!SessionMap.newInstance().contaninsKey(IMEI)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("session NOT KEY ="+IMEI +",ip="+session.getRemoteAddress() );
			}
			LOGGER.debug("session NOT KEY ="+IMEI +",ip="+session.getRemoteAddress() );
			// 如果map不存在这个imei 号的KEY
			// 保存session对象
			SessionMap.newInstance().addSession(IMEI, session);
			return ;
		}
		
		IoSession writeSession = SessionMap.newInstance().getSession(IMEI);
//		writeSession.getId()
		if(! session.equals(writeSession)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("session DIFF UPDATE="+IMEI+",new ip="+session.getRemoteAddress() );
				LOGGER.debug("session DIFF UPDATE="+IMEI+",old ip="+writeSession.getRemoteAddress() );
			}
			// 当前这个IMEI 的读session 与写的不一样了
			//更新写的 session
			SessionMap.newInstance().addSession(IMEI, session);
		}
	}

	 
	

}
