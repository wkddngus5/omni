package com.omni.scooter.entity;

public class UpdateVersionEntity extends BaseEntity{

	public UpdateVersionEntity() {
		super();
	}

	public UpdateVersionEntity(String content) {
		super(content);
	}
	

	/**
	 * Iot 设备软件版本 
	 * @return  110->V1.1.0
	 */
	public int getIotVersion(){
		return getInt(tCont[5]);
	}
	/**
	 *Iot 设备识别码（固定 85）
	 * @return  85
	 */
	public String getIotCode(){
		return  tCont[4];
	}
	/**
	 * Iot 设备软件编译日期
	 * @return  Jul 4 2018
	 */
	public String getIotBuildTime(){
		return  tCont[6];
	}

	/**
	 * 滑板车控制器软件版本
	 * @return  1101->0x1101 (代表 PCB 版本号为 1，固件版本号为 V1.0.1)
	 */
	public int getScooterVersion(){
		return  getInt(tCont[7]);
	}
	 
	/**
	 * 滑板车控制器识别码（固定 CT）
	 * @return  1101->0x1101 (代表 PCB 版本号为 1，固件版本号为 V1.0.1)
	 */
	public String getScooterCode(){
		return   (tCont[8]);
	}

	@Override
	public String toString() {
		return "UpdateVersionEntity [getIotVersion()=" + getIotVersion() + ", getIotCode()=" + getIotCode()
				+ ", getIotBuildTime()=" + getIotBuildTime() + ", getScooterVersion()=" + getScooterVersion()
				+ ", getScooterCode()=" + getScooterCode() + ", getImei()=" + getImei() + "]";
	}
	

	
}
