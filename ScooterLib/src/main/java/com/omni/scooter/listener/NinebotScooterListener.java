package com.omni.scooter.listener;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.omni.scooter.entity.ScooterInfo1;
import com.omni.scooter.entity.ScooterInfo2;
import com.omni.scooter.entity.ninebot.ScooterSet2Entity;
import com.omni.scooter.entity.ninebot.UpdateResultEntity;
import com.omni.scooter.entity.ninebot.UpdateVersionEntity;
import com.omni.scooter.entity.ninebot.VersionEntity;
import com.omni.scooter.entity.ninebot.VoiceEntity;
import com.omni.scooter.entity.ninebot.ScooterSet1Entity;
import com.omni.scooter.entity.UpdateDetailEntity;
import com.omni.scooter.entity.UpdateOKEntity;
import com.omni.scooter.entity.WarnEntity;
import com.omni.scooter.tcp.SessionMap;
import com.omni.scooter.utils.CommandUtil;
 
/**
 * 基于协议版本 1.2.8
 * @author cxiaox
 * @date 2018年10月22日 下午7:39:45
 */
public class NinebotScooterListener implements IScooterNinebotCallback {
	private static final Logger LOGGER = LoggerFactory.getLogger(NinebotScooterListener.class);
	private   String DeviceType ="OM";
	@Override
	public void setFilterDeviceType(String deviceType) {
		DeviceType =deviceType;
		
	}


	public void  deviceCodeErrorCallback(BaseEntity baseEntity,IoSession session){
		
	}
	
	@Override
	public void doHandCommand(IoSession session, String message) {
		String deviceCode =message.split(",")[1];
		BaseEntity baseEntity = new BaseEntity(message);
		if(!DeviceType.equals(deviceCode)){
			deviceCodeErrorCallback(baseEntity,session);
			return ;
		}
		
		String imei =message.split(",")[2];
		String sorder =message.split(",")[3];
		byte[] sorderbyte=sorder.getBytes();
		int orderCode=((sorderbyte[0]&0xFF)<<8)|(sorderbyte[1]&0xFF);
		updateSession(Long.valueOf(imei),session);
		
		switch (orderCode) {
		case Command.ORDER_REPORT_CODE:
			handReport(  session,   message);
			break;
		case Command.ORDER_HEART_CODE:
			handHeart(  session,   message);
			break;
		case Command.ORDER_CONTROL_REQUEST_CODE:
			handControlRequest(  session,   message);
			break;
		case Command.ORDER_LOCK_OPEN_CODE:
			handLockOpen(  session,   message);
			break;
		case Command.ORDER_LOCK_CLOSE_CODE:
			handLockClose(  session,   message);
			break;
		case Command.ORDER_IOT_SET_CODE:
			iotSetCallBack(new IotSetEntity(message), session);
			break;
		case Command.ORDER_SCOOTER_INFO_CODE:
			scooterInfo1CallBack(new ScooterInfo1(message), session);
			break;
		case Command.ORDER_SCOOTER_SET1_CODE:
			scooterSet1CallBack(new ScooterSet1Entity(message), session);
			break;
		case Command.ORDER_SCOOTER_INFO2_CODE:
			scooterInfo2CallBack(new ScooterInfo2(message), session);
			break;
		case Command.ORDER_SCOOTER_SET2_CODE:
			scooterSet2CallBack(new ScooterSet2Entity(message), session);
			break;
		case Command.ORDER_WARN_CODE:
			handWarn(session, message);
			break;
		case Command.ORDER_VOICE_CODE:
			voiceCallBack(new VoiceEntity(message), session);
			break;
		case Command.ORDER_POWER_CONTROL_CODE:
			powerControlCallBack(new PowerControlEntity(message), session); 
			break;
		case Command.ORDER_LOCATION_CODE:
			locationCallBack(new LocationEntity(message), session);
			break;
		case Command.ORDER_VERSION_CODE:
			versionCallBack (new VersionEntity(message), session);
			break;
		case Command.ORDER_ERROR_CODE:
			handError(session, message);
			break;
		case Command.ORDER_UPDATE_VERSION_CODE:
			 updateVersionCallBack(new UpdateVersionEntity(message), session);
			break;
		case Command.ORDER_UPDATE_CODE:
			updateDetailCallBack(new UpdateDetailEntity(message),session);
			break;
		case Command.ORDER_UPDATE_OK_CODE:
			 updateResultCallBack(new UpdateResultEntity(message),session);
			break;
		case Command.ORDER_DEVICE_KEY_CODE:
			deviceKeyCallBack(new DeviceKeyEntity(message),session);
			break;
		case Command.ORDER_MAC_CODE:
			deviceMacCallBack(new DeviceMacEntity(message),session);
			break;
		case Command.ORDER_ICCID_CODE:
			deviceIccidCallBack(new DeviceIccidEntity(message),session);
			break;
		default:
			break;
		}
	}

	private void handError(IoSession session, String message) {
		ErrorEntity entity = new ErrorEntity(message);
		byte[] response = CommandUtil.getResponseCommand(entity.getImei(), Command.ORDER_ERROR);
		session.write(IoBuffer.wrap(response));
		errorCallBack(entity, session);
	}

	private void handWarn(IoSession session, String message) {
		WarnEntity entity=new WarnEntity(message);
		byte[] response = CommandUtil.getResponseCommand(entity.getImei(), Command.ORDER_WARN);
		session.write(IoBuffer.wrap(response));
		
		warnCallBack(entity, session);
	}
	
	private void handLockClose(IoSession session, String message) {
		LockCloseEntity entity=new LockCloseEntity(message);
		// 返回 L1
		byte[] response = CommandUtil.getResponseCommand(entity.getImei(), Command.ORDER_LOCK_CLOSE);
		session.write(IoBuffer.wrap(response));
		// L1 回调
		lockCloseCallBack(entity, session);
	}

	private void handLockOpen(IoSession session, String message) {
		OpenLockEntity entity=new OpenLockEntity(message);
		// 返回 L0
		byte[] response = CommandUtil.getResponseCommand(entity.getImei(), Command.ORDER_LOCK_OPEN);
		session.write(IoBuffer.wrap(response));
		// L0 回调
		lockOpenCallBack(entity, session);		
	}

	private void handReport(IoSession session, String message){
		ReportEntity reportEntity=new  ReportEntity(message);
		reportCallBack(reportEntity,session);
	}
	private void handHeart(IoSession session, String message){
		HeartEntity heartEntity=new  HeartEntity(message);
		heartCallBack(heartEntity,session);
	}
	private void handControlRequest(IoSession session, String message){
		ControlRequestEntity entity=new  ControlRequestEntity(message);
		controlRequestCallBack(entity,session);
	}
	 	
	@Override
	public void onFailure(String msg) {

	}

	@Override
	public void onSuccessful(IoSession session, Object message) {

	}
	
	
	@Override
	public void deviceIccidCallBack(DeviceIccidEntity entity, IoSession session) {
		
	}
	
	@Override
	public void deviceMacCallBack(DeviceMacEntity entity, IoSession session) {
		
	}
	
	@Override
	public void deviceKeyCallBack(DeviceKeyEntity entity, IoSession session) {
		
	}

	@Override
	public void updateResultCallBack(UpdateResultEntity entity, IoSession session) {
		
	}
	
	@Override
	public void updateDetailCallBack(UpdateDetailEntity entity, IoSession session) {
		
	}
	
	@Override
	public void updateVersionCallBack(UpdateVersionEntity entity, IoSession session) {
		
	}
	
	@Override
	public void errorCallBack(ErrorEntity entity, IoSession session) {
		
	}
	
	 

	@Override
	public void reportCallBack(ReportEntity reportEntity, IoSession session) {
		

	}
	

	@Override
	public void controlRequestCallBack(ControlRequestEntity controlRequestEntity, IoSession session) {
		 
		
	}

	@Override
	public void heartCallBack(HeartEntity heartEntity, IoSession session) {
		
	}

	@Override
	public void lockOpenCallBack(OpenLockEntity entity, IoSession session) {
		
	}
	@Override
	public void lockCloseCallBack(LockCloseEntity entity, IoSession session) {
		
	}
	
	@Override
	public void iotSetCallBack(IotSetEntity entity, IoSession session) {
		
	}
	
	@Override
	public void scooterInfo1CallBack(ScooterInfo1 entity, IoSession session) {
		
	}
	@Override
	public void scooterInfo2CallBack(ScooterInfo2 entity, IoSession session) {
		
	}
	
	@Override
	public void scooterSet1CallBack(ScooterSet1Entity entity, IoSession session) {
		
	}
	@Override
	public void scooterSet2CallBack(ScooterSet2Entity entity, IoSession session) {
		
	}
	
	@Override
	public void warnCallBack(WarnEntity entity, IoSession session) {
		
	}
	
	@Override
	public void voiceCallBack(VoiceEntity entity, IoSession session) {
		
	}
	
	@Override
	public void powerControlCallBack(PowerControlEntity entity, IoSession session) {
		
	}
	
	@Override
	public void locationCallBack(LocationEntity entity, IoSession session) {
		
	}
	
	@Override
	public void onMoreCommand(IoSession session, String message) {
		
	}
	
	private void updateSession(long IMEI,IoSession session){ 
		if(!SessionMap.newInstance().contaninsKey(IMEI)){
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("session NOT KEY ="+IMEI +",ip="+session.getRemoteAddress() );
			}
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

	@Override
	public void versionCallBack(VersionEntity entity, IoSession session) {
		
	}

	 
	

}
