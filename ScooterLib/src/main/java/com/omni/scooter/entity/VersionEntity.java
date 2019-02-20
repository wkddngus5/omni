package com.omni.scooter.entity;

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
	 * Iot 设备软件编译日期
	 * @return  Jul 4 2018
	 */
	public String getIotBuildTime(){
		return  tCont[5];
	}

	/**
	 * 滑板车控制器软件版本
	 * @return  1101->0x1101 (代表 PCB 版本号为 1，固件版本号为 V1.0.1)
	 */
	public String getScooterVersion(){
		return  getString(tCont[6]);
	}
	
	/**
	 * 保留值
	 * @return   0
	 */
	public String getExtraValue(){
		return  getString(tCont[7]);
	}


	@Override
	public String toString() {
		return "VersionEntity [getIotVersion()=" + getIotVersion() + ", getIotBuildTime()=" + getIotBuildTime()
				+ ", getScooterVersion()=" + getScooterVersion() + ", getImei()=" + getImei() + "]";
	}
	
	

}
