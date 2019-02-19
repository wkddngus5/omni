package com.pgt.bikelock.listener;


import org.apache.mina.core.session.IoSession;

import com.pgt.bike.lock.lib.entity.GPSEntity;
import com.pgt.bike.lock.lib.listener.TCPCallBackListener;
import com.pgt.bikelock.utils.GpsTransformUtil;
import com.pgt.bikelock.vo.LatLng;


public class MyTcpCallBack extends TCPCallBackListener{

	
	CallBackUtils callBackUtils;
	
	public MyTcpCallBack(){		
		callBackUtils = new CallBackUtils();
	}

	@Override
	public void onFailure(String msg) {

		super.onFailure(msg);

		callBackUtils.onFailure(msg);
	}

	@Override
	public void reportCallback(long imei, short power, IoSession session) {
		super.reportCallback(imei, power);
		
		callBackUtils.reportCallback(imei, power, session);
	}
	

	@Override
	public void iccidCallback(long imei, String iccid, IoSession session) {
		callBackUtils.iccidCallback(imei, iccid);
	}

	@Override
	public void sleepCallBack(long imei) {
		callBackUtils.shutDownCallBack(imei);
	}


	@Override
	public void heartCallback(long imei, int lockStatus, short power, IoSession session) {

		

	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bike.lock.lib.listener.TCPCallBackListener#heartBGMCallback(long, int, short, int, int)
	 */
	public void heartBGMCallback(long imei, int lockStatus, short power,
			int gsm, int alarmStatus) {
		// TODO Auto-generated method stub
		super.heartBGMCallback(imei, lockStatus, power, gsm, alarmStatus);
	
		callBackUtils.heartBGMCallback(imei, lockStatus, power, gsm, alarmStatus);
	}

	@Override
	public void gpsLocationCallback(long imei, GPSEntity gpsEntity, String gpsContent) {
		LatLng location = new LatLng(0, 0);
		if(gpsEntity != null){
			location = GpsTransformUtil.conver2Amp(new LatLng(gpsEntity.getWGS48Lat(),gpsEntity.getWGS48Lng()));
		}
		callBackUtils.gpsLocationCallback(imei, location, gpsContent);
	}



	@Override
	public void lockCallback(long imei) {  
		callBackUtils.unLockCallback(imei, true, 0, 0);
	}



	@Override
	public void setLockCallback(long imei, boolean isOpen) {  
		callBackUtils.unLockCallback(imei, isOpen, 0, 0);
	}
	

	/* (non-Javadoc)
	 * @see com.pgt.bike.lock.lib.listener.TCPCallBackListener#lockBGMCallback(long, int, long, int)
	 * 关锁回调/turn off lock call back
	 */
	@Override
	public void lockBGMCallback(long imei, int uid, long timestamp, int runTime) {
		// TODO Auto-generated method stub
		super.lockBGMCallback(imei, uid, timestamp, runTime);

		callBackUtils.lockCallback(imei, uid, timestamp, runTime);
	}

	/* (non-Javadoc)
	 * @see com.pgt.bike.lock.lib.listener.TCPCallBackListener#setBGMLockCallback(long, boolean, int, long)
	 * 开锁回调/open lock call back
	 */
	@Override
	public void setBGMLockCallback(long imei, boolean isOpen, int uid,
			long timestamp) {
		// TODO Auto-generated method stub
		super.setBGMLockCallback(imei, isOpen, uid, timestamp);
		callBackUtils.unLockCallback(imei, isOpen, uid, timestamp);
	}


	
	/* (non-Javadoc)
	 * @see com.pgt.bike.lock.lib.listener.TCPCallBackListener#alertCallback(long, int, org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void alertCallback(long imei, int status, IoSession session) {
		// TODO Auto-generated method stub
		super.alertCallback(imei, status, session);
		callBackUtils.warnCallBack(imei, status);
	}


	@Override
	public void bikeInfoCallback(long imei, int power, int gsm, int gsmNum, int lockStatus, int alrm) {
		
		callBackUtils.basicInfoCallback(imei, power, gsm, gsmNum, lockStatus, alrm);
	}


	/* (non-Javadoc)
	 * @see com.pgt.bike.lock.lib.listener.TCPCallBackListener#versionCallback(long, java.lang.String, java.lang.String)
	 */
	@Override
	public void versionCallback(long imei, String version, String buildTime) {
		// TODO Auto-generated method stub
		callBackUtils.versionCallback(imei, version, buildTime);
	}

	@Override
	public void handSetAddress(long IMEI, byte[] command, IoSession session) {
		callBackUtils.handSetAddress(IMEI, command, session);
	}

	@Override
	public void handFindBike(long IMEI, byte[] command, IoSession session) {
		callBackUtils.handFindBike(IMEI, command, session);
	}

	public void heartPeriodCallback(long imei, String period, IoSession session) {
		callBackUtils.heartPeriodCallback(imei, period, session);
	}

	/* (non-Javadoc)
	 * @see com.pgt.bike.lock.lib.listener.TCPCallBackListener#heartBGMCallback(long, int, short, int)
	 */
	@Override
	public void heartBGMCallback(long imei, int lockStatus, short power, int gsm) {
		// TODO Auto-generated method stub
		super.heartBGMCallback(imei, lockStatus, power, gsm);

		callBackUtils.heartBGMCallback(imei, lockStatus, power, gsm, 0);

	}


	@Override
	public void updateVersionCallBack(long IMEI, int version,
			String deviceType, String buildTime, IoSession session) {
		callBackUtils.updateVersionCallBack(IMEI, version, deviceType, buildTime, session,1);
	}


	@Override
	public void updateInfoCallBack(long IMEI, int pack, String deviceType,
			IoSession session) {
		callBackUtils.updateInfoCallBack(IMEI, pack, deviceType, session,1);
	}

	@Override
	public void updateOKCallback(long imei) {
		callBackUtils.updateOKCallback(imei, null);
	}

	@Override
	public void updateOKCallback(long imei, String version, String buildTime) {
		callBackUtils.updateOKCallback(imei, null);
	}

	
	/* (non-Javadoc)
	 * @see com.pgt.bike.lock.lib.listener.TCPCallBackListener#macInfoCallback(long, java.lang.String)
	 */
	@Override
	public void macInfoCallback(long imei, String mac) {
		// TODO Auto-generated method stub
		super.macInfoCallback(imei, mac);
		
		callBackUtils.macInfoCallback(imei, mac,true);
	}


}
