package com.omni.scooter.entity.omni;

import com.omni.scooter.entity.BaseEntity;

public class UpdateVersionEntity extends BaseEntity{

	public UpdateVersionEntity() {
		super();
	}

	public UpdateVersionEntity(String content) {
		super(content);
	}
	
	
	/**
	 *Iot 设备识别码（固定 8A）
	 * @return  字符串 "8A"
	 */
	public String getIotCode(){
		return  tCont[4];
	}

	/**
	 * Iot 设备软件版本 
	 * @return  整数 110->V1.1.0
	 */
	public int getIotVersion(){
		return getInt(tCont[5]);
	}
	
	 

	/**
	 * 滑板车控制器软件版本
	 * @return  1101->0x1101 (代表 PCB 版本号为 1，固件版本号为 V1.0.1)
	 */
	public int getScooterVersion(){
		return  getInt(tCont[6]);
	}
	 
 

	@Override
	public String toString() {
		return "UpdateVersionEntity [getIotVersion()=" + getIotVersion() + ", getIotCode()=" + getIotCode()
				+ ", getScooterVersion()=" + getScooterVersion()
				+ ", getImei()=" + getImei() + "]";
	}
	

	
}
