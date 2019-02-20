package com.pgt.bike.lock.lib.entity.ninebot;

import java.io.Serializable;

import com.pgt.bike.lock.lib.entity.BaseEntity;
/**
 * 锁上传服务器 心跳信息的实体类
 * @author cxiaox
 *
 */
public class ReportEntity extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -3613423324585439607L;

	public ReportEntity(){
	}

	public ReportEntity(String content) {
		super(content);
	}
	
	/**
	 * 获取到 锁的电量 412=4.12V
	 * @return 锁的电压值。单位是0.1V ,412=4.12V
	 */
	public int getPower(){
		return getInt(tCont[4]);
	}
	/**
	 * 获取到 滑板车的电量
	 * @return 滑板车的电量百分比。80=80%
	 */
	public int getScooterPower(){
		return getInt(tCont[5]);
	}
	
	
	/**
	 * 获取到 GSM 信号值
	 * Segway IOT v1.2.8 版本后返回正确的GSM值，之前版本返回0.
	 * OMNI IOT 
	 * @return IOT 设备的信号值 越大越好
	 */
	public int getGsm(){
		if(tCont.length>=7){
			return getInt(tCont[6]);
		}
		
		return 0;
	}

	@Override
	public String toString() {
		return "ReportEntity [iotPower=" + getPower() + ",scooterPower=" + getScooterPower() + ",gsm=" + getGsm() + ", imei="
				+ getImei() + "]";
	}
	
	

}
