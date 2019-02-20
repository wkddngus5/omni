package com.pgt.bike.lock.lib.entity.ninebot;

import com.pgt.bike.lock.lib.entity.BaseEntity;

public class DeviceIccidEntity extends BaseEntity {

	public DeviceIccidEntity() {
		super();
	}

	public DeviceIccidEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取设备SIM的ICCID号
	 * @return  1 
	 */
	public String getIccid(){
		return getString(tCont[4]);
	}

	@Override
	public String toString() {
		return "DeviceMacEntity [getIccid()=" + getIccid() + ", getImei()=" + getImei() + "]";
	}

	 
	
	

}
