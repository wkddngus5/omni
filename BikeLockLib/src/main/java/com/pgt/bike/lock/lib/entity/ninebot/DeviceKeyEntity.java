package com.pgt.bike.lock.lib.entity.ninebot;

import com.pgt.bike.lock.lib.entity.BaseEntity;

public class DeviceKeyEntity extends BaseEntity {

	public DeviceKeyEntity() {
		super();
	}
	public DeviceKeyEntity(String content) {
		super(content);
	}
	/**
	 * 蓝牙KEY
	 * @return  获取到蓝牙连接的 device key
	 */
	public String getDeviceKey(){
		return getString(tCont[4]);
	}
	@Override
	public String toString() {
		return "DeviceKeyEntity [getDeviceKey()=" + getDeviceKey() + ", getImei()=" + getImei() + "]";
	}
	 
	
}
