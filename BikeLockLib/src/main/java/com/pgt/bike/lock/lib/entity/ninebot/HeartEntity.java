package com.pgt.bike.lock.lib.entity.ninebot;

import com.pgt.bike.lock.lib.entity.BaseEntity;

public class HeartEntity extends BaseEntity{
	
	public HeartEntity() {
		super();
	}
	public HeartEntity(String content) {
		super(content);
	}
	/**
	 * 获取到 锁的 开关锁状态
	 * @return 0:解锁状态，1：锁车状态
	 */
	public int getLockStatus(){
		return getInt(tCont[4]);
	}
	/**
	 * 获取到 锁(IOT)的电量 412=4.12V
	 * @return 锁(IOT)的电压值。单位是0.1V ,412=4.12V
	 */
	public int getPower(){
		return getInt(tCont[5]);
	}
	/**
	 * 获取到 锁的网络信号值
	 * @return 网络信号值。2-32, 此值越大，信号越好
	 */
	public int getGsm(){
		return getInt(tCont[6]);
	}
	/**
	 * 获取到 滑板车的电量
	 * @return 滑板车的电量百分比。80=80%
	 */
	public int getScooterPower(){
		return getInt(tCont[7]);
	}
	/**
	 * 获取到 滑板车的充电状态
	 * @return 0：未充电，1： 充电
	 */
	public int getChargeStatus(){
		return getInt(tCont[8]);
		
	}
	@Override
	public String toString() {
		return "HeartEntity [lockStatus=" + getLockStatus() + ", power=" + getPower() + ", gsm="
				+ getGsm() + ", scooterPower=" + getScooterPower() + ", chargeStatus=" + getChargeStatus()
				+ ", imei=" + getImei() + "]";
	}
	
	

}
