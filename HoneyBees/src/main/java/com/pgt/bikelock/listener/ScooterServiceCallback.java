/**
 * FileName:     ScooterServiceCallback.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月21日 下午4:25:05
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月21日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.listener;


import com.omni.scooter.entity.*;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;
import com.omni.scooter.command.ICommand;
import com.omni.scooter.command.OmniCommand;
import com.omni.scooter.entity.omni.ScooterSet1Entity;
import com.omni.scooter.entity.omni.UpdateResultEntity;
import com.omni.scooter.entity.omni.UpdateVersionEntity;
import com.omni.scooter.listener.OmniScooterListener;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.utils.GpsTransformUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.LatLng;


/**
 * @ClassName:     ScooterServiceCallback
 * @Description:滑板车服务回调/scotter service callback
 * @author:    Albert
 * @date:        2018年8月21日 下午4:25:05
 *
 */
public class ScooterServiceCallback extends OmniScooterListener {


	CallBackUtils callBackUtils;

	public ScooterServiceCallback(){		
		callBackUtils = new CallBackUtils();
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#onFailure(java.lang.String)
	 */
	@Override
	public void onFailure(String msg) {
		// TODO Auto-generated method stub
		super.onFailure(msg);
		callBackUtils.onFailure(msg);
	}

	@Override
	public void reportCallBack(ReportEntity reportEntity, IoSession session) {
		Long imei = ValueUtil.getLong(reportEntity.getImei());
		callBackUtils.reportCallback(imei, Short.parseShort(reportEntity.getScooterPower()+""), session);

	}

	@Override
	public void heartCallBack(HeartEntity heartEntity, IoSession session) {
		callBackUtils.heartBGMCallback(ValueUtil.getLong(heartEntity.getImei()), heartEntity.getLockStatus(),
				Short.parseShort(heartEntity.getScooterPower()+""), heartEntity.getGsm(), 0);
	}




	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#controlRequestCallBack(com.omni.scooter.entity.ControlRequestEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void controlRequestCallBack(
			ControlRequestEntity controlRequestEntity, IoSession session) {
		// TODO Auto-generated method stub
		super.controlRequestCallBack(controlRequestEntity, session);
		//		logger.debug(controlRequestEntity.toString());
		String imei = controlRequestEntity.getImei();
		int key = controlRequestEntity.getControlKey();
		if(controlRequestEntity.getControlType()==ControlRequestEntity.TYPE_OPEN){
			// 发送开锁指令 
			byte[] open = OmniCommand.getOpenLockCommand(imei, key, controlRequestEntity.getUserId(), controlRequestEntity.getControlRequestId());
			session.write(IoBuffer.wrap(open));
		}else{
			byte[] close= OmniCommand.getCloseLockCommand(imei, key);
			session.write(IoBuffer.wrap(close));
		}
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#locationCallBack(com.omni.scooter.entity.LocationEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void locationCallBack(LocationEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.locationCallBack(entity, session);		
		LatLng location = GpsTransformUtil.conver2Amp(new LatLng( entity.getGpsEntity().getWGS84Lat(),entity.getGpsEntity().getWGS84Lng()));
		callBackUtils.gpsLocationCallback(ValueUtil.getLong(entity.getImei()),location, entity.getGpsContent());
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#lockOpenCallBack(com.omni.scooter.entity.OpenLockEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void lockOpenCallBack(OpenLockEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.lockOpenCallBack(entity, session);
		callBackUtils.unLockCallback(ValueUtil.getLong(entity.getImei()), entity.getOpenStatus()==0?true:false, 
				entity.getUserId(),entity.getOpenId());
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#lockCloseCallBack(com.omni.scooter.entity.LockCloseEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void lockCloseCallBack(LockCloseEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.lockCloseCallBack(entity, session);
		callBackUtils.lockCallback(ValueUtil.getLong(entity.getImei()), entity.getUserId(),
				entity.getCloseId(), entity.getOpenTime());
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#warnCallBack(com.omni.scooter.entity.WarnEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void warnCallBack(WarnEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.warnCallBack(entity, session);
		callBackUtils.warnCallBack(ValueUtil.getLong(entity.getImei()), entity.getWarnStatus());
	}

	/* (non-Javadoc)
	 * 车里程信息回调
	 * @see com.omni.scooter.listener.OmniScooterListener#scooterInfoCallBack(com.omni.scooter.entity.ScooterInfo1, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void scooterInfoCallBack(ScooterInfo entity, IoSession session) {
		// TODO Auto-generated method stub
		super.scooterInfoCallBack(entity, session);
		//信息保存
		JSONObject data = new JSONObject();
		data.put(BikeVo.LOCK_EXTEND_INFO_POWER, entity.getScooterPower());
		data.put(BikeVo.LOCK_EXTEND_INFO_SPEED, entity.getSpeed());
		data.put(BikeVo.LOCK_EXTEND_INFO_SPEED_MODE, entity.getSpeedMode());
		data.put(BikeVo.LOCK_EXTEND_INFO_CHARGE_STATUS, entity.getChargeStatus());
		data.put(BikeVo.LOCK_EXTEND_INFO_POWER1, entity.getPower1());
		data.put(BikeVo.LOCK_EXTEND_INFO_POWER2, entity.getPower2());
		
		callBackUtils.extendInfoCallback(ValueUtil.getLong(entity.getImei()), data,"scooterInfoCallBack");
		
		callBackUtils.updatePower(ValueUtil.getLong(entity.getImei()), entity.getScooterPower());
	}
	

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#scooterSet1CallBack(com.omni.scooter.entity.ScooterSetEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void scooterSet1CallBack(ScooterSet1Entity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.scooterSet1CallBack(entity, session);
		//信息保存
		JSONObject data = new JSONObject();
		data.put(BikeVo.LOCK_EXTEND_INFO_HEAD_LIGHT, entity.getLightStatus());
		data.put(BikeVo.LOCK_EXTEND_INFO_TAIL_LIGHT_TWINKLING, entity.getTailLightStatus());
		data.put(BikeVo.LOCK_EXTEND_INFO_ACCELERATOR_RESPONSE, entity.getThrottleStatus());
		data.put(BikeVo.LOCK_EXTEND_INFO_SPEED_MODE, entity.getSpeedModeStatus());
		callBackUtils.extendInfoCallback(ValueUtil.getLong(entity.getImei()), data,"scooterSet1CallBack");
	}


	

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#scooterSet2CallBack(com.omni.scooter.entity.ScooterSet2Entity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void scooterSet2CallBack(com.omni.scooter.entity.omni.ScooterSet2Entity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.scooterSet2CallBack(entity, session);
		JSONObject data = new JSONObject();
		data.put(BikeVo.LOCK_EXTEND_INFO_INCH_SPEED, entity.getInchStatus());
		data.put(BikeVo.LOCK_EXTEND_INFO_CRUISE_SPEED, entity.getCruiseControl());
		data.put(BikeVo.LOCK_EXTEND_INFO_LOW_SPEED_LIMIT, entity.getLowSpeedLimit());
		data.put(BikeVo.LOCK_EXTEND_INFO_MEDIUM_SPEED_LIMIT, entity.getMidSpeedLimit());
		data.put(BikeVo.LOCK_EXTEND_INFO_HIGH_SPEED_LIMIT, entity.getHighSpeedLimit());
		data.put(BikeVo.LOCK_EXTEND_INFO_BUTTON_CHANGE_MODE, entity.getChangeMode());
		data.put(BikeVo.LOCK_EXTEND_INFO_START_TYPE, entity.getStartType());
		data.put(BikeVo.LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT, entity.getChangeLight());
		callBackUtils.extendInfoCallback(ValueUtil.getLong(entity.getImei()), data,"scooterSet2CallBack");
	}
	

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#versionCallBack(com.omni.scooter.entity.VersionEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void versionCallBack(VersionEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.versionCallBack(entity, session);
		callBackUtils.versionCallback(ValueUtil.getLong(entity.getImei()), 
				"IOT:"+entity.getIotVersion()+";SCOOTER:"+entity.getScooterVersion(), 
				"IOT:"+entity.getIotBuildTime());
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#deviceMacCallBack(com.omni.scooter.entity.DeviceMacEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void deviceMacCallBack(DeviceMacEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.deviceMacCallBack(entity, session);
		callBackUtils.macInfoCallback(ValueUtil.getLong(entity.getImei()),entity.getMac(),true);
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#deviceIccidCallBack(com.omni.scooter.entity.DeviceIccidEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void deviceIccidCallBack(DeviceIccidEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.deviceIccidCallBack(entity, session);
		callBackUtils.iccidCallback(ValueUtil.getLong(entity.getImei()),entity.getIccid());
	}

	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#powerControlCallBack(com.omni.scooter.entity.PowerControlEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void powerControlCallBack(PowerControlEntity entity,
			IoSession session) {
		// TODO Auto-generated method stub
		super.powerControlCallBack(entity, session);
		if(entity.getControlStatus() == 1){
			callBackUtils.shutDownCallBack(ValueUtil.getLong(entity.getImei()));
		}else if(entity.getControlStatus() == 2){
			callBackUtils.startUpCallBack(ValueUtil.getLong(entity.getImei()));
		}

	}
	
	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#iotSetCallBack(com.omni.scooter.entity.IotSetEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void iotSetCallBack(IotSetEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.iotSetCallBack(entity, session);
		JSONObject data = new JSONObject();
		data.put(BikeVo.LOCK_EXTEND_INFO_IOT_ACCELEROMETER_SENSITIVITY, entity.getSensitivity());
		data.put(BikeVo.LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE, entity.getInfo1Status());
		data.put(BikeVo.LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE_INTERVAL, entity.getInfo1Interval());
		data.put(BikeVo.LOCK_EXTEND_INFO_IOT_HEART_INTERVAL, entity.getHeartInterval());
		callBackUtils.extendInfoCallback(ValueUtil.getLong(entity.getImei()), data,"iotSetCallBack");
	}
	
	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#deviceKeyCallBack(com.omni.scooter.entity.DeviceKeyEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void deviceKeyCallBack(DeviceKeyEntity entity, IoSession session) {
		// TODO Auto-generated method stub
		super.deviceKeyCallBack(entity, session);
		JSONObject data = new JSONObject();
		data.put(BikeVo.LOCK_EXTEND_INFO_KEY_BLE, entity.getDeviceKey());
		callBackUtils.extendInfoCallback(ValueUtil.getLong(entity.getImei()), data,"deviceKeyCallBack");
	}
	
	/* (non-Javadoc)
	 * 版本检测/version check
	 * @see com.omni.scooter.listener.OmniScooterListener#updateVersionCallBack(com.omni.scooter.entity.omni.UpdateVersionEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void updateVersionCallBack(UpdateVersionEntity entity,
			IoSession session) {
		// TODO Auto-generated method stub
		super.updateVersionCallBack(entity, session);

		callBackUtils.updateVersionCallBack(ValueUtil.getLong(entity.getImei()), entity.getIotVersion(), ICommand.DEVICE_TYPE_IOT_OMNI, "", session,2);
		callBackUtils.updateVersionCallBack(ValueUtil.getLong(entity.getImei()), entity.getIotVersion(), ICommand.DEVICE_TYPE_CT, "", session,3);
	}
	
	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#updateDetailCallBack(com.omni.scooter.entity.UpdateDetailEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void updateDetailCallBack(UpdateDetailEntity entity,
			IoSession session) {
		// TODO Auto-generated method stub
		super.updateDetailCallBack(entity, session);		
		callBackUtils.updateInfoCallBack(ValueUtil.getLong(entity.getImei()), entity.getPack(), entity.getDeviceType(), session,2);
	}
	
	/* (non-Javadoc)
	 * @see com.omni.scooter.listener.OmniScooterListener#updateResultCallBack(com.omni.scooter.entity.omni.UpdateResultEntity, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void updateResultCallBack(UpdateResultEntity entity,
			IoSession session) {
		// TODO Auto-generated method stub
		super.updateResultCallBack(entity, session);
		callBackUtils.updateOKCallback(ValueUtil.getLong(entity.getImei()), "result:"+entity.getResult()+";code:"+entity.getDeviceCode());
	}

	@Override
	public void errorCallBack(ErrorEntity entity, IoSession session) {
		super.errorCallBack(entity, session);
		callBackUtils.errorCallback(entity.getImei(),entity.getErrorCode());
	}
}
