package com.pgt.bike.lock.lib.entity.ninebot;

import com.pgt.bike.lock.lib.entity.BaseEntity;

public class DeviceMacEntity extends BaseEntity {

	public DeviceMacEntity() {
		super();
	}

	public DeviceMacEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取设备的MAC地址
	 * @return   
	 */
	public String getMac(){
		return getString(tCont[4]);
	}

	@Override
	public String toString() {
		return "DeviceMacEntity [getMac()=" + getMac() + ", getImei()=" + getImei() + "]";
	}

	 
	
	

}
