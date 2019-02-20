package com.omni.scooter.entity.omni;

import com.omni.scooter.entity.BaseEntity;
/**
 * omni 控制外部设备发给服务器的指令信息实体
 * 基于指令IOT协议V1.2.0
 * @author cxiaox
 * @date 2018年11月10日 下午14:31:43
 */
public class ControlExtrDeviceEntity extends BaseEntity {

	public ControlExtrDeviceEntity() {
		super();
	}

	public ControlExtrDeviceEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取控制类型
	 * @return  0:解锁 电池盖  
	 */
	public int getControlType(){
		return getInt(tCont[4]);
	}
	
	/**
	 * 获取控制 状态
	 * @return  0:解锁成功，1-解锁失败
	 */
	public int getControlStatus(){
		return getInt(tCont[5]);
	}

	@Override
	public String toString() {
		return "ControlExtrDeviceEntity [controlType()=" + getControlType()+",controlStatus="+getControlStatus() + ", getImei()=" + getImei() + "]";
	}

	 
	
	

}
