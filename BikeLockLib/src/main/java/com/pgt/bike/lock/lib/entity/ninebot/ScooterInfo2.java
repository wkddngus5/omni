package com.pgt.bike.lock.lib.entity.ninebot;

import com.pgt.bike.lock.lib.entity.BaseEntity;

/**
 * ninebot 滑板车信息2
 * 基于协议版本v1.2.8
 * @author cxiaox
 * 2018-08-09
 *
 */
public class ScooterInfo2 extends BaseEntity {

	public ScooterInfo2() {
		super();
	}

	public ScooterInfo2(String content) {
		super(content);
	}
	/**
	 * 获取到 滑板车的充电状态
	 * @return 0：未充电，1：充电
	 */
	public int getChargeStatus(){
		return getInt(tCont[4]);
	}
	
	/**
	 * 控制器驱动电压 单位： V
	 * 基于协议版本1.2.8
	 * @return 36->36V
	 */
	public int getVoltage(){
		return getInt(tCont[5]);
	}
	/**
	 * 电池循环次数
	 * @return  
	 */
	public int getCycle(){
		return getInt(tCont[6]);
	}
	/**
	 * 电池 1 电量。
	 * 电量百分比
	 * @return   90--> 90%
	 */
	public int getPower1(){
		return getInt(tCont[7]);
	}
	
	/**
	 * 电池 2 电量。
	 * 电量百分比
	 * @return   90--> 90%
	 */
	public int getPower2(){
		return getInt(tCont[8]);
	}
	
	/**
	 * 内置电池温度 1 ,单位：℃
	 * @return 30--> 30℃
	 */
	public int getBuiltInTemp1(){
		return getInt(tCont[9]);
	}

	/**
	 * 内置电池温度 2,单位：℃
	 * @return 30--> 30℃
	 */
	public int getBuiltInTemp2(){
		return getInt(tCont[10]);
	}
	
	/**
	 * 外挂电池温度 1 ,单位：℃
	 * @return 30--> 30℃
	 */
	public int getPlugInTemp1(){
		return getInt(tCont[11]);
	}

	/**
	 * 外挂电池温度 2 ,单位：℃
	 * @return 30--> 30℃
	 */
	public int getPlugInTemp2(){
		return getInt(tCont[12]);
	}

	@Override
	public String toString() {
		return "ScooterInfo2 [getChargeStatus()=" + getChargeStatus() + ", getVoltage()=" + getVoltage()
				+ ", getCycle()=" + getCycle() + ", getPower1()=" + getPower1() + ", getPower2()=" + getPower2()
				+ ", getBuiltInTemp1()=" + getBuiltInTemp1() + ", getBuiltInTemp2()=" + getBuiltInTemp2()
				+ ", getPlugInTemp1()=" + getPlugInTemp1() + ", getPlugInTemp2()=" + getPlugInTemp2() + ", getImei()="
				+ getImei() + "]";
	}
	
	
}
