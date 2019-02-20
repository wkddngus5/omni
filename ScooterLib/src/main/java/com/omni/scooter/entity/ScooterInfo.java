package com.omni.scooter.entity;

/**
 * omni 滑板车 信息的返回
 * @author cxiaox
 * @date 2018年8月31日 下午4:11:24
 */
public class ScooterInfo extends BaseEntity {

	public ScooterInfo() {
		super();
	}

	public ScooterInfo(String content) {
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
	 * 当前车速，单位是0.1KM/H
	 * @return 221-> 22.1km/h
	 */
	public int getSpeed(){
		return getInt(tCont[6]);
	}
	
	/**
	 * 滑板车充电状态。
	 * @return 0-> 未充电，1->充电
	 */
	public int getChargeStatus(){
		return getInt(tCont[7]);
	}
	
	/**
	 * 电池1电压。滑板车 内置电池
	 * @return 单位 0.1V, 372->37.2V
	 */
	public int getPower1(){
		return getInt(tCont[8]);
	}
	
	/**
	 * 电池2电压。滑板车 外挂电池
	 * @return 单位 0.1V, 372->37.2V
	 */
	public int getPower2(){
		return getInt(tCont[9]);
	}
	
	/**
	 * 获取滑板车状态
	 * XM滑板车版本V1.7.0 之后此值是真实值，之前版本此值为0.
	 * @return
	 */
	public int getLockStatus(){
		if(tCont.length>=11){
			return getInt(tCont[10]);
		}
		return 0;
	}
	
	/**
	 * 获取滑板车 GSM 信息值。
	 * XM滑板车版本V1.7.0 之后此值是真实值，之前版本此值为0.
	 * @return
	 */
	public int getGsm(){
		if(tCont.length==12){
			return getInt(tCont[11]);
		}
		return 0;
	}

	@Override
	public String toString() {
		return "ScooterInfo [scooterPower=" + getScooterPower() + ", speedMode=" + getSpeedMode()
				+ ",speed=" + getSpeed() + ", getChargeStatus()=" + getChargeStatus() + ", power1="
				+ getPower1() + ", power2=" + getPower2() + ", lockStatus=" + getLockStatus() + ", gsm="
				+ getGsm() + ", imei=" + getImei() + "]";
	}
}
