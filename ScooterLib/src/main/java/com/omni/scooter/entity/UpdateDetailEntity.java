package com.omni.scooter.entity;

import java.util.HashMap;

public class UpdateDetailEntity extends BaseEntity {
	
 
 
	

	public UpdateDetailEntity() {
		super();
	}

	public UpdateDetailEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取到  固件请求第几包升级数据
	 * @return  整数
	 */
	public int getPack(){
		return getInt(tCont[4]);
	}
	/**
	 * 获取到  固件类型
	 * 基于协议v1.2.8
	 * {@link com.omni.scooter.command.ICommand#DEVICE_TYPE_IOT_SEGWAY ICommand.DEVICE_TYPE_IOT_SEGWAY}
	 * {@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_CONTROL ICommand.DEVICE_TYPE_SEGWAY_CONTROL},
	 * {@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_DISPLAY ICommand.DEVICE_TYPE_SEGWAY_DISPLAY}
	 * {@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_BATTERY1 ICommand.DEVICE_TYPE_SEGWAY_BATTERY1}
	 * {@link com.omni.scooter.command.ICommand#DEVICE_TYPE_SEGWAY_BATTERY2 ICommand.DEVICE_TYPE_SEGWAY_BATTERY2}
	 * @return  字符串。 "85"表示ninebot IOT程序，"20" 表示滑板车控制器
	 */
	public String getDeviceType(){
		return getString(tCont[5]);
	}

	@Override
	public String toString() {
		return "UpdateDetailEntity [getPack()=" + getPack() + ", getDeviceType()=" + getDeviceType() + ", getImei()="
				+ getImei() + "]";
	}
 
	
	

}
