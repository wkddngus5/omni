package com.omni.scooter.entity.ninebot;

import com.omni.scooter.entity.BaseEntity;
/**
 * 固件版本信息
 * 基于协议版本v1.2.8
 *
 * @date 2018年10月23日 上午11:19:26
 */
public class VersionEntity extends BaseEntity {

	public VersionEntity() {
		super();
	}

	public VersionEntity(String content) {
		super(content);
	}
	
	
	/**
	 * Iot 设备软件版本 
	 * @return  110->V1.1.0
	 */
	public String getIotVersion(){
		return tCont[4];
	}
	/**
	 * 滑板车控制器固件版本
	 * @return   1101-> PCB 版本 1 固件版本 V1.0.1
	 */
	public String getScooterVersion(){
		return  tCont[5];
	}

	/**
	 * 滑板车仪表软件版本
	 * @return 1101-> PCB 版本 1 固件版本 V1.0.1
	 */
	public String getDisplayerVersion(){
		return  getString(tCont[6]);
	}
	
	/**
	 * 滑板车内置电池固件版本
	 * @return   1101-> PCB 版本 1 固件版本 V1.0.1
	 */
	public String getInternalBatteryVersion(){
		return  getString(tCont[7]);
	}
	
	/**
	 * 滑板车外置电池固件版本
	 * @return   1101-> PCB 版本 1 固件版本 V1.0.1
	 */
	public String getExtralBatteryVersion(){
		return  getString(tCont[8]);
	}

	@Override
	public String toString() {
		return "VersionEntity [getIotVersion()=" + getIotVersion() + ", getScooterVersion()=" + getScooterVersion()
				+ ", getDisplayerVersion()=" + getDisplayerVersion() + ", getInternalBatteryVersion()="
				+ getInternalBatteryVersion() + ", getExtralBatteryVersion()=" + getExtralBatteryVersion() + "]";
	}


	 
	

}
