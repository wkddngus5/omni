/**
 * FileName:     CallBackUtils.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月21日 下午6:19:42
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月21日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.listener;

import java.util.Map.Entry;

import javax.validation.constraints.Size;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;
import com.omni.lock.upfile.DeviceType;
import com.omni.lock.upfile.UpFileUtil;
import com.omni.scooter.command.OmniCommand;
import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.entity.GPSEntity;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.bo.HoldBO;
import com.pgt.bikelock.bo.NotificationBo;
import com.pgt.bikelock.dao.IBikeDao;
import com.pgt.bikelock.dao.IBikeOrderRecord;
import com.pgt.bikelock.dao.IBikeSimDao;
import com.pgt.bikelock.dao.IBikeUseDao;
import com.pgt.bikelock.dao.IBleBikeDao;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeOrderRecordDaoImpl;
import com.pgt.bikelock.dao.impl.BikeSimDaoImpl;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.dao.impl.BleBikeDaoImpl;
import com.pgt.bikelock.utils.GpsTransformUtil;
import com.pgt.bikelock.utils.LockUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.ZoneDate;
import com.pgt.bikelock.vo.BikeSim;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.BleBikeVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.admin.BikeOrderRecord;
import com.pgt.bikelock.vo.admin.NotificationConfigVo.NotificationType;

/**
 * @ClassName:     CallBackUtils
 * @Description:TODO
 * @author:    Albert
 * @date:        2018年8月21日 下午6:19:42
 *
 */
public class CallBackUtils {

	private IBikeDao bikeDao ;
	private IBikeUseDao bikeUseDao ;


	private static IBikeOrderRecord orderRecordDao;
	private static  NotificationBo notifyBo;

	public CallBackUtils(){
		bikeDao = new BikeDaoImpl();
		bikeUseDao = new BikeUseDaoImpl();
		orderRecordDao = new BikeOrderRecordDaoImpl();
		notifyBo = new NotificationBo();
	}
	
	/*******信息回调/information callback********/

	/**
	 * 
	 * @Title:        onFailure 
	 * @Description:  异常指令回调/error order
	 * @param:        @param msg    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月22日 上午9:57:48
	 */
	public void onFailure(String msg) {
		orderRecordDao.insert(0L, BikeOrderRecord.ORDER_ERROR, TimeUtil.getCurrentLongTime(), "ERROR");
	}
	
	/**
	 * 
	 * @Title:        reportCallback 
	 * @Description:  签到回调/sing in 
	 * @param:        @param imei
	 * @param:        @param power
	 * @param:        @param session    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午6:24:03
	 */
	public void reportCallback(long imei, short power, IoSession session){
		// 签到/check in
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_SIGN_IN, TimeUtil.getCurrentLongTime(), "SING IN,power:"+power);

		//激活设备/activate device
		bikeDao.updateBikeErrorStatusWithImei(imei, 4, 0);

		String serverIp= session.getLocalAddress().toString().split(":")[0].replace("/", "");
		bikeDao.updateBikeServerIp(imei,serverIp,power);
	}

	/**
	 * 
	 * @Title:        heartBGMCallback 
	 * @Description:  心跳回调/heart
	 * @param:        @param imei
	 * @param:        @param lockStatus
	 * @param:        @param power
	 * @param:        @param gsm
	 * @param:        @param alarmStatus    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午6:24:39
	 */
	public void heartBGMCallback(long imei, int lockStatus, short power,
			int gsm, int alarmStatus) {
		// TODO Auto-generated method stub
		finishBikeUseAtHeart(imei, lockStatus);

		String content =String.format("power=%s,lockStatus=%s,gsm=%s,bikeStatus=%s", power,lockStatus,gsm,alarmStatus);
		long second = TimeUtil.getCurrentLongTime();
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_HEART, second, content);

		System.out.println("omni bike heart imei="+imei+"  ;power="+power +";lockStatus="+ lockStatus+";bikeStatus="+ alarmStatus+";time:"+new ZoneDate().toLocaleString());
		bikeDao.updateBikeInfoWithHeart(imei, power,second,lockStatus==1?0:1,alarmStatus==2?2:1);
	}

	/**
	 * 
	 * @Title:        finishBikeUseAtHeart 
	 * @Description:  心跳时结束骑行/heartbeat finish cycling
	 * @param:        @param imei
	 * @param:        @param lockStatus    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月13日 上午11:32:07
	 */
	private void finishBikeUseAtHeart(long imei, int lockStatus){
		BikeVo bike = bikeDao.getBikeInfoWithImei(imei+"");
		if(bike != null && bike.getStatus() == lockStatus){
			if(lockStatus == 0){

				// begin rid
				if (bikeUseDao.startUseBikeSuccess(bike.getBid())) {
					BikeUseVo bikeUseVo = bikeUseDao.getUseIngDetailInfo(imei+"",null);
					new HoldBO().payActiveHoldWhenStartRide(bikeUseVo);
				}

			}else if(lockStatus == 1){
				//结算骑行/caculate riding
				BikeUseVo bikeUseVo = bikeUseDao.getUseIngDetailInfo(imei+"",null);
				if(bikeUseVo != null){
					if(bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_USING && 
							(
									//									bikeUseVo.getBikeVo().getStatus()==BikeVo.LOCK_ON || 
									bikeUseVo.getOut_area() == 1)){
						// 当前 单车正在被租赁,且锁是打开的或者越界状态	/present bike is renting, and lock is open or transboundary status
						new BikeBo().dealBikeOrder(bikeUseVo,true);
					}
				}

			}
		}
	}

	/**
	 * 
	 * @Title:        gpsLocationCallback 
	 * @Description:  定位回调/location
	 * @param:        @param imei
	 * @param:        @param gpsEntity
	 * @param:        @param gpsContent    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午6:29:30
	 */
	public void gpsLocationCallback(long imei, LatLng location, String gpsContent) {
//		GPSEntity gpsEntity = (GPSEntity)entity;
		if(location!=null){
			//wgs to gcj
//			LatLng location = GpsTransformUtil.conver2Amp(new LatLng(gpsEntity.getWGS48Lat(), gpsEntity.getWGS48Lng()));

			bikeDao.updateGps(imei, location.getLatitude(), location.getLongitude());
			System.out.println("update gps imei:"+imei+";lat:"+location.getLatitude()+";lng:"+location.getLongitude()+",time:"+new ZoneDate().toLocaleString());

			//save location command 
			String content1 =String.format("lat=%s,lng=%s", location.getLatitude(),location.getLongitude());
			orderRecordDao.insert(imei, BikeOrderRecord.ORDER_LOCATION, TimeUtil.getCurrentLongTime(), content1);
			//关联更新对应的使用结束位置/connect update correspond use finish position
			BikeUseVo bikeUseVo = bikeUseDao.getFinalUseInfoWithNoLockLocation(imei);
			if(bikeUseVo != null){

				bikeUseVo.setCurrentLat(location.getLatitude());
				bikeUseVo.setCurrentLng(location.getLongitude());

				if(bikeUseVo.getDistance()  == 0){
					//计算骑行距离(如果用户未上传)/cacaulate riding distance(if user not to upload)
					bikeUseVo.setDistanceWithStartAndEnd();
				}
				if(bikeUseVo.getOrbit() == null){
					//生成路径(如果用户未上传)/generate route(if user not to upload)
					bikeUseVo.setOrbitWithPoint(location.getLatitude(), location.getLongitude());
				}
				bikeUseVo.setEndLat(location.getLatitude());
				bikeUseVo.setEndLng(location.getLongitude());
				bikeUseDao.updateUseBikeEndLocation(bikeUseVo);
				System.out.println("updateUseBikeEndLocation:"+imei);
				//上锁定位后检查是否越界、停止计费
				if(bikeUseVo.getStatus() == 0 && bikeUseVo.getStartLat() > 0 && "0".equals(bikeUseVo.getEndTime())){
					new BikeBo().dealBikeOrder(bikeUseVo, true);
				}
				// reset the status to reserved for held bikes
				new HoldBO().enforceHoldForBikeId(bikeUseVo.getBid());
			}
		}else{
			orderRecordDao.insert(imei, BikeOrderRecord.ORDER_LOCATION, TimeUtil.getCurrentLongTime(), gpsContent);
		}

	}
	
	/**
	 * 
	 * @Title:        unLockCallback 
	 * @Description:  开锁回调/unlock call back
	 * @param:        @param imei
	 * @param:        @param isOpen
	 * @param:        @param uid
	 * @param:        @param timestamp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午6:36:46
	 */
	public void unLockCallback(long imei, boolean isOpen, int uid,
			long timestamp) {
		if(isOpen){
			System.out.println("Open device success;stamp:"+timestamp+"time:"+new ZoneDate().toLocaleString());
			// 开锁成功的回调/open lock success callback
			BikeVo bike = bikeDao.getBikeInfoWithImei(imei+"");

			if(bike != null){
				// 设置开锁成功/set up open lock success
				if(!bikeUseDao.startUseBikeSuccess(uid,timestamp,-1)){
					//未能正常开始骑行时，手动标记开关/can't riding, manual sign switch
					BikeUseVo useVo = bikeUseDao.getUseInfo(timestamp);
					if(useVo == null || (ValueUtil.getInt(useVo.getStartTime()) != 0 && ValueUtil.getInt(useVo.getEndTime()) == 0)){
						bikeDao.updateLockStatus(bike.getNumber(),1);
					}
					
				}
				else {

					BikeUseVo bikeUseVo = bikeUseDao.getUseIngDetailInfo(imei+"",uid+"");
					new HoldBO().payActiveHoldWhenStartRide(bikeUseVo);
					
				}
			}else{
				System.out.println("bike not exist in system;time:"+new ZoneDate().toLocaleString());
			}
			orderRecordDao.insert(imei, BikeOrderRecord.ORDER_UN_LOCK, TimeUtil.getCurrentLongTime(), "UN LOCK,"+timestamp+","+uid);
		}else{
//			bikeUseDao.deleteBikeUse(timestamp);
			orderRecordDao.insert(imei, BikeOrderRecord.ORDER_UN_LOCK, TimeUtil.getCurrentLongTime(), "UN LOCK FAIl,"+timestamp+","+uid);
		}
	}

	/**
	 * 
	 * @Title:        lockCallback 
	 * @Description:  关锁回调/lock callback
	 * @param:        @param imei
	 * @param:        @param uid
	 * @param:        @param timestamp
	 * @param:        @param runTime    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午6:37:01
	 */
	public void lockCallback(long imei, int uid, long timestamp, int runTime) {
		BikeUseVo bikeUseVo = bikeUseDao.getUseIngDetailInfo(uid,timestamp);

		//直接修改锁状态为关闭/direct modify lock status as closed
		new BikeDaoImpl().updateLockStatusWithImei(imei, 0,false);

		if(bikeUseVo == null){
			orderRecordDao.insert(imei, BikeOrderRecord.ORDER_LOCK, TimeUtil.getCurrentLongTime(),  "LOCK,time:"+timestamp+",uid:"+uid+"runTime:"+runTime);
			System.out.println("LOCK,time:"+timestamp+",uid:"+uid+"runTime:"+runTime+"omni bike is not is use"+";time:"+new ZoneDate().toLocaleString());

			return ;
		}
		System.out.println("lock "+bikeUseVo.getBikeVo().getNumber());
		if(bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_USING  || bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_UNLOCK_ING
				//				&& (bikeUseVo.getBikeVo().getStatus()==BikeVo.LOCK_ON || bikeUseVo.getBikeVo().getStatus()==BikeVo.LOCK_ERROR)
				){
			// 当前 单车正在被租赁,且锁是打开的/present bike is renting, and lock is open
			bikeUseVo.setEndTime(runTime);
			new BikeBo().dealBikeOrder(bikeUseVo,true);
		}else{
			System.out.println("LOCK,time:"+timestamp+",uid:"+uid+"runTime:"+runTime+"useStatus:"+bikeUseVo.getBikeVo().getUseStatus()+",omni bike is not in use or not lock"+";time:"+new ZoneDate().toLocaleString());
		}
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_LOCK, TimeUtil.getCurrentLongTime(), "LOCK,time:"+timestamp+",uid:"+uid+"runTime:"+runTime);
	}
	
	/**
	 * 
	 * @Title:        warnCallBack 
	 * @Description:  报警/warn
	 * @param:        @param imei
	 * @param:        @param status    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午6:49:40
	 */
	public void warnCallBack(long imei, int status){
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_ALERT, TimeUtil.getCurrentLongTime(), "status:"+status);
		notifyBo.sendNotifyToAdmin(NotificationType.Bike_Warn, null, imei+"",0);
		if(status == 2){
			bikeDao.updateBikeStatusWithImei(imei, 2);
		}
	}
	
	/**
	 * 
	 * @Title:        infoCallback 
	 * @Description:  基础信息回调/basic info callback
	 * @param:        @param imei
	 * @param:        @param power
	 * @param:        @param gsm
	 * @param:        @param gsmNum
	 * @param:        @param lockStatus
	 * @param:        @param alrm    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午6:52:05
	 */
	public void basicInfoCallback(long imei, int power, int gsm, int gsmNum, int lockStatus, int alrm) {
		bikeDao.updateBikeInfo(imei, power, gsm,lockStatus == 0?1:0);
		String content =String.format("power=%s,gsm=%s,gpsNum=%s,reportLockStatus=%s,alrm=%s", power,gsm,gsmNum,lockStatus,alrm);
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_INFO, TimeUtil.getCurrentLongTime(), content);
		
		finishBikeUseAtHeart(imei,lockStatus);
	}
	
	/**
	 * 
	 * @Title:        extendInfoCallback 
	 * @Description:  扩展信息更新/extend info update
	 * @param:        @param imei
	 * @param:        @param data    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月27日 下午5:38:29
	 */
	public void extendInfoCallback(long imei,JSONObject data,String key){
		System.out.println("info:"+data.toJSONString());
		BikeVo bikeVo = bikeDao.getBikeDetailInfoWithImei(imei);
		String content = key+":";
		if(bikeVo != null){
			if(bikeVo.getExtendInfo() != null){
				for (Entry<String, Object> param : data.entrySet()) {
					bikeVo.getExtendInfo().put(param.getKey(), param.getValue());
					content += param.getValue()+",";
				}
				data = bikeVo.getExtendInfo();
			}
			
		}
		bikeDao.updateExtendInfo(imei, data);
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_INFO, TimeUtil.getCurrentLongTime(), content);
		
	}
	
	
	/**
	 * 
	 * @Title:        versionCallback 
	 * @Description:  版本回调/verison
	 * @param:        @param imei
	 * @param:        @param version
	 * @param:        @param buildTime    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午7:02:03
	 */
	public void versionCallback(long imei, String version, String buildTime) {
		System.out.println("get versionCallback"+ imei+ ":"+ version + ";" +buildTime+";time:"+new ZoneDate().toLocaleString());
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_VERSION, TimeUtil.getCurrentLongTime(), String.format("get version upload:%s,buildTime:%s", version,buildTime));
		bikeDao.updateBikeVersion(imei, version, null, buildTime);
	}
	
	/**
	 * 
	 * @Title:        macInfoCallback 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param mac    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午7:08:52
	 */
	public void macInfoCallback(long imei, String mac,boolean saveOrder) {
		if(saveOrder){
			orderRecordDao.insert(imei, BikeOrderRecord.ORDER_INFO, TimeUtil.getCurrentLongTime(), "mac:"+mac);
		}
		BikeVo bikeVo = new BikeDaoImpl().getBikeInfoWithImei(imei+"");
		BleBikeVo bleVo = null;
		IBleBikeDao bleDao = new BleBikeDaoImpl();
		if(bikeVo != null){
			bleVo = bleDao.getBleBike(bikeVo.getNumber());
			if(bleVo == null){
				//新增蓝牙信息/add bluetooth information
				bleVo = new BleBikeVo();
				bleVo.setBid(bikeVo.getBid());
				bleVo.setMac(mac);
				bleDao.addBleBike(bleVo);
			}else{
				//更新蓝牙信息/update bluetooth information
				bleVo.setMac(mac);
				bleDao.updateBleBike(bleVo);
			}
		}
	}
	
	/**
	 * 
	 * @Title:        iccidCallback 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param iccid
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月21日 下午7:09:07
	 */
	public void iccidCallback(long imei, String iccid) {

		IBikeSimDao bikeSimDao = new BikeSimDaoImpl();
		BikeSim bikeSim= bikeSimDao.find(imei);
		if(bikeSim==null){
			// 没有数据，插入新数据/no data, insert new data
			bikeSimDao.insert(imei, iccid);
		}else{
			// 已经存在/already exist
			// 如果之前保存的ICCID号和现在的是一样的，则没有必要更新/if reserved before iccd is same with now, no need update
			if(!bikeSim.getIccid().equals(iccid)){
				// 保存的ICCID号和现在的是不一样的，更新ICCID 号/reserved iccd number is different with now,update iccid number
				bikeSimDao.update(imei, iccid);
			}
		}

		long second = TimeUtil.getCurrentLongTime();
		String content =String.format("%s,%s", imei,iccid);
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_GET_ICCID, second, content);
	}
	
	/**
	 * 
	 * @Title:        updatePower 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param power    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年9月17日 下午7:17:58
	 */
	public void updatePower(long imei,int power){
		bikeDao.updateLockPower(imei, power);
	}
	
	/*******状态回调/status callback********/
	public void shutDownCallBack(long imei) {
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_SHUT_DOWN, TimeUtil.getCurrentLongTime(), "SHUT DOWN");
	}
	
	public void startUpCallBack(long imei) {
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_START_UP, TimeUtil.getCurrentLongTime(), "START UP");
	}

	public void handSetAddress(long IMEI, byte[] command, IoSession session) {
		orderRecordDao.insert(IMEI, BikeOrderRecord.ORDER_IP, TimeUtil.getCurrentLongTime(), "SET IP");
	}


	public void handFindBike(long IMEI, byte[] command, IoSession session) {
		orderRecordDao.insert(IMEI, BikeOrderRecord.ORDER_FIND, TimeUtil.getCurrentLongTime(), "FIND");
	}

	public void heartPeriodCallback(long imei, String period, IoSession session) {
		orderRecordDao.insert(imei, BikeOrderRecord.ORDER_HEART_PERIOD, TimeUtil.getCurrentLongTime(), "P="+period);
	}
	
	/***版本升级/upgrade****/
	
	/**
	 * 
	 * @Title:        updateVersionCallBack 
	 * @Description:  TODO
	 * @param:        @param IMEI
	 * @param:        @param version
	 * @param:        @param deviceType
	 * @param:        @param buildTime
	 * @param:        @param session
	 * @param:        @param versionType 1:bike 2:scooter iot 3:scooter ctl
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年12月8日 上午10:10:50
	 */
	public void updateVersionCallBack(long IMEI, int version,
			String deviceType, String buildTime, IoSession session,int versionType) {
		System.out.println("updateVersionCallBack,"+IMEI);
		String versionStr = (double)version/10+"";
		bikeDao.updateBikeVersion(IMEI,versionStr,deviceType, buildTime);
		orderRecordDao.insert(IMEI, BikeOrderRecord.ORDER_VERSION, TimeUtil.getCurrentLongTime(), String.format("update version callback:%s,buildTime:%s", 
				deviceType+"_V"+versionStr,buildTime));
		byte[] comm = LockUtil.getUploadVersionData(IMEI, version, deviceType,true,versionType);
		if(comm != null){
			session.write(IoBuffer.wrap(comm));
		}
	}
	
	/**
	 * 
	 * @Title:        updateInfoCallBack 
	 * @Description:  TODO
	 * @param:        @param IMEI
	 * @param:        @param pack
	 * @param:        @param deviceType
	 * @param:        @param session
	 * @param:        @param lockType 1:bike 2:scooter
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年12月8日 上午10:03:29
	 */
	public void updateInfoCallBack(long IMEI, int pack, String deviceType,
			IoSession session,int lockType) {
		System.out.println("upload  pack="+pack);
		System.out.println("upload  deviceType="+deviceType);
		
		int type = 0;
		
		if(lockType == 2){
			type = DeviceType.SCOOTER_IOT_OMNI;
			if(DeviceType.VALUE_CONTROL_XM.equals(deviceType)){//scooter controller
				type = DeviceType.CONTROL_XM;
			}
			if(type > 0){
				int crc = UpFileUtil.getUpdateCRC(type,pack);
				System.out.println("crc 0x="+String.format("%04X", crc));
				byte[] updateItem=UpFileUtil.getUpdateBytes(type, pack);
				byte[] comm = OmniCommand.getUpdateDetailComm(String.valueOf(IMEI),pack, updateItem.length,crc, updateItem);
				session.write(IoBuffer.wrap(comm));
			}
		}else{
			if(DeviceType.VALUE_GPRS_GPS.equals(deviceType)){
				type = DeviceType.GPRS_GPS;
			}else if(DeviceType.VALUE_GPRS_GPS_BLE.equals(deviceType)){	
				type = DeviceType.GPRS_GPS_BLE;
			}else if(DeviceType.VALUE_MODEL_GPRS_GPS_BLE.equals(deviceType)){
				type = DeviceType.MODEL_GPRS_GPS_BLE;
			}
			if(type > 0){
				int crc = UpFileUtil.getUpdateCRC(type,pack);
				System.out.println("crc 0x="+String.format("%04X", crc));
				byte[] updateItem=UpFileUtil.getUpdateBytes(type, pack);
				byte[] comm = CommandUtil.getUpdateDetailComm(
						Command.CODE, 
						String.valueOf(IMEI),
						pack, crc, updateItem);

				session.write(IoBuffer.wrap(comm));
			}
		}	
		

	}
	
	public void updateOKCallback(long imei,String result) {
		System.out.println("handUpdateOK");
		long seconds = new ZoneDate().getTime()/1000;
		orderRecordDao.insert(imei, 15, seconds, "UPDATE OK,"+result != null?result:"");
	}

	/**
	 * error callback
	 * @param imei
	 * @param code
	 */
	public void errorCallback(String imei,int code) {
		long seconds = new ZoneDate().getTime()/1000;
		orderRecordDao.insert(ValueUtil.getLong(imei),BikeOrderRecord.ORDER_ERROR, seconds,"error code:"+code);
	}
}
