package com.omni.scooter.entity;

/**
 * ninebot 滑板车信息的返回
 * 基于协议版本v1.2.8  修改时间：2018-10-22
 * @author cxiaox
 *
 * @date 2018年8月31日 下午4:10:57
 */
public class ScooterInfo1 extends BaseEntity {

	public ScooterInfo1() {
		super();
	}

	public ScooterInfo1(String content) {
		super(content);
	}
	
	/**
	 * 获取滑板车当前电量百分比
	 * @return 0-100 ，80->80%
	 */
	public int getScooterPower(){
		return getInt(tCont[4]);
	}
	
	/**
	 * 获取当前滑板车速度模式
	 * @return 1：低速，2：中速，3：高速
	 */
	public int getSpeedMode(){
		return getInt(tCont[5]);
	}
	/**
	 * 当前车速，单位是 KM/H
	 * 基于协议 1.2.8 ,之前的协议版本单位是0.1 KM/H
	 * @return 22-> 22 km/h
	 */
	public int getSpeed(){
		return getInt(tCont[6]);
	}
	/**
	 * 总骑行里程，单位是 m
	 * 基于协议 1.2.8 ,之前的协议版本单位是 10m
	 * @return   100->100m
	 */
	public int getTotalMileage(){
		return getInt(tCont[7]);
	}
	
	/**
	 * 预计剩余骑行里程，预计剩余可骑行里程。
	 * @return 单位 10m, 500->5000m
	 */
	public int getPrescientMileage(){
		return getInt(tCont[8]);
	}
	
	/**
	 * 总骑行时间 ,单位 ： second
	 * @return
	 */
	public int getTotalTime(){
		return getInt(tCont[9]);
	}
	
	/**
	 * 滑板车状态
	 * 基于协议版本v1.2.8,之前没有该字段
	 * @return 0-解锁状态,1-锁车状态
	 */
	public int getLockStatus(){
		if(tCont.length>=11){
		return getInt(tCont[10]);
		}
		return 0;
	}

	@Override
	public String toString() {
		return "ScooterInfo1 [scooterPower()=" + getScooterPower() + ", speedMode()=" + getSpeedMode()
				+ ", speed()=" + getSpeed() + ", totalMileage()=" + getTotalMileage() + ", prescientMileage()="
				+ getPrescientMileage() + ",totalTime()=" + getTotalTime()+ ",lockStatus()=" + getLockStatus() + ", imei=" + getImei() + "]";
	}
	
	
}
