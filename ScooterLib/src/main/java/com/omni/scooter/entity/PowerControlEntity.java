package com.omni.scooter.entity;

/**
 * 滑板车电源开关机控制
 * @author cxiaox
 * 2018-08-09
 */
public class PowerControlEntity extends BaseEntity {

	public PowerControlEntity() {
		super();
	}

	public PowerControlEntity(String content) {
		super(content);
	}
	
	
	/**
	 *  获取滑板车开关机控制状态
	 * @return  1:关机 2:开机
	 */
	public int getControlStatus(){
		return getInt(tCont[4]);
	}

	@Override
	public String toString() {
		return "PowerControlEntity [getControlStatus()=" + getControlStatus() + ", getImei()=" + getImei() + "]";
	}

	 
	

}
