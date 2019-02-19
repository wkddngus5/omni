/**
 * FileName:     LockUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月19日 下午2:24:22/2:24:22 pm, July 19, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月19日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;


import com.omni.lock.upfile.DeviceType;
import com.omni.lock.upfile.UpFileUtil;
import com.omni.scooter.command.ICommand;
import com.omni.scooter.command.OmniCommand;
import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.resource.OthersSource;

/**
 * @ClassName:     LockUtil
 * @Description:锁工具类/lock tool
 * @author:    Albert
 * @date:        2017年7月19日 下午2:24:22
 *
 */
public class LockUtil {
	
	/**
	 * 
	 * @Title:        getUploadVersionData 
	 * @Description:  获取版本升级数据/get updated data version
	 * @param:        @param IMEI
	 * @param:        @param version
	 * @param:        @param deviceType
	 * @param:        @param checkVersion
	 * @param:        @param versionType 1:bike 2:scooter iot 3:scooter ctl
	 * @param:        @return    
	 * @return:       byte[]    
	 * @author        Albert
	 * @Date          2018年12月8日 上午10:08:07
	 */
	public static byte[] getUploadVersionData(long IMEI, int version,
			String deviceType,boolean checkVersion,int versionType){
		
		if(versionType == 1){
			if(DeviceType.VALUE_GPRS_GPS_BLE.equals(deviceType)){
				String sVersion =  OthersSource.getSourceString("lock_gprs_ble_version");
				int confVer = ValueUtil.getInt(sVersion);
				if(version < confVer || !checkVersion){
					// BLE+GPRS +MSM
					int size = UpFileUtil.getSize();
					int totalPack = UpFileUtil.getTotalPack(DeviceType.GPRS_GPS_BLE);
					int allCRC=UpFileUtil.getAllCRC(DeviceType.GPRS_GPS_BLE);
					String deviceUpCode= OthersSource.getSourceString("lock_version_code");
					byte[] comm = CommandUtil.getStartUpComm(
							Command.CODE, String.valueOf(IMEI), 
							totalPack, size, allCRC, DeviceType.VALUE_GPRS_GPS_BLE, deviceUpCode);
					return comm;
				}else{
					System.out.println("update version error");
				}
			}else if(DeviceType.VALUE_MODEL_GPRS_GPS_BLE.equals(deviceType)){
				String sVersion =  OthersSource.getSourceString("lock_gprs_ble_b_version");
				int confVer = ValueUtil.getInt(sVersion);
				if(version < confVer || !checkVersion){
					// BLE+GPRS +MSM
					int size = UpFileUtil.getSize();
					int totalPack = UpFileUtil.getTotalPack(DeviceType.MODEL_GPRS_GPS_BLE);
					int allCRC=UpFileUtil.getAllCRC(DeviceType.MODEL_GPRS_GPS_BLE);
					String deviceUpCode= OthersSource.getSourceString("lock_version_code");
					byte[] comm = CommandUtil.getStartUpComm(
							Command.CODE, String.valueOf(IMEI), 
							totalPack, size, allCRC, DeviceType.VALUE_MODEL_GPRS_GPS_BLE, deviceUpCode);
					System.out.println(new String(comm));
					return comm;
				}else{
					System.out.println("update version error");
				}
			}else if(DeviceType.VALUE_GPRS_GPS.equals(deviceType)){
				
				String sVersion = OthersSource.getSourceString("lock_gprs_version");
				int confVer = ValueUtil.getInt(sVersion);
				if(version < confVer  || !checkVersion  ){
					// BLE+GPRS 
					int size = UpFileUtil.getSize();
					int totalPack = UpFileUtil.getTotalPack(DeviceType.GPRS_GPS);
					int allCRC=UpFileUtil.getAllCRC(DeviceType.GPRS_GPS);
					String deviceUpCode= OthersSource.getSourceString("lock_version_code");
					byte[] comm = CommandUtil.getStartUpComm(Command.CODE, String.valueOf(IMEI), totalPack, size, allCRC, DeviceType.VALUE_GPRS_GPS, deviceUpCode);
					return comm;
				}else{
					System.out.println("update version error");
				}
			}
		}else if(versionType == 2){
			//scooter iot
			int scooterIotVersion = ValueUtil.getInt(OthersSource.getSourceString("scooter_iot_version"));
			if(version < scooterIotVersion || !checkVersion){
				int size = UpFileUtil.getSize();
				int totalPack = UpFileUtil.getTotalPack(DeviceType.SCOOTER_IOT_OMNI);
				int allCRC=UpFileUtil.getAllCRC(DeviceType.SCOOTER_IOT_OMNI);
				String deviceUpCode= OthersSource.getSourceString("scooter_iot_code");
				byte[] comm = OmniCommand.getStartUpComm(String.valueOf(IMEI), totalPack, size, allCRC, deviceType, deviceUpCode);
				return comm;
			}else{
				System.out.println("update version error");
			}
		}else if(versionType == 3){
			//scooter ctl
			int ctlVersion = ValueUtil.getInt(OthersSource.getSourceString("scooter_ctl_version"));
			if(version < ctlVersion || !checkVersion){
				int size = UpFileUtil.getRealLen(DeviceType.CONTROL_XM);
				int totalPack = UpFileUtil.getTotalPack(DeviceType.CONTROL_XM);
				int allCRC=UpFileUtil.getRealAllCRC(DeviceType.CONTROL_XM);
				String deviceUpCode= OthersSource.getSourceString("scooter_iot_code");
				byte[] comm = OmniCommand.getStartUpComm(String.valueOf(IMEI), totalPack, size, allCRC,deviceType, deviceUpCode);
				return comm;
			}else{
				System.out.println("update version error");
			}
		}

		System.out.println("version file error");
		return null;
	}
}
