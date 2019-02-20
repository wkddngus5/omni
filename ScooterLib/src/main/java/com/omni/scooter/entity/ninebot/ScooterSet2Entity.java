package com.omni.scooter.entity.ninebot;

import com.omni.scooter.entity.BaseEntity;

/**
 * 滑板车设置指令2 信息实体
 * @author cxiaox
 * 2018-08-09
 *
 */
public class ScooterSet2Entity extends BaseEntity {

	public ScooterSet2Entity() {
		super();
	}

	public ScooterSet2Entity(String content) {
		super(content);
	}
	
	/**
	 * 英制速度显示。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getInchStatus(){
		return getInt(tCont[4]);
	}

	
	/**
	 * 定速巡航 状态。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getCruiseControl(){
		return getInt(tCont[5]);
	}
	
	/**
	 * 车体蓝牙广播 状态。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getScooterBle(){
		return getInt(tCont[6]);
	}
	
	/**
	 * 按键切换模式。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getChangeMode(){
		return getInt(tCont[7]);
	}
	
	/**
	 * 低速模式限速值。
	 * @return  0:无效（不设置） 范围：6-25。
	 */
	public int getLowSpeedLimit(){
		return getInt(tCont[8]);
	}

	 
	/**
	 * 中速模式限速值。
	 * @return  0:无效（不设置） 范围：6-25。
	 */
	public int getMidSpeedLimit(){
		return getInt(tCont[9]);
	}
	
	/**
	 * 高速模式限速值。
	 * @return  0:无效（不设置） 范围：6-25。
	 */
	public int getHighSpeedLimit(){
		return getInt(tCont[10]);
	}

	@Override
	public String toString() {
		return "ScooterSet2Entity [getInchStatus()=" + getInchStatus() + ", getCruiseControl()=" + getCruiseControl()
				+ ", getScooterBle()=" + getScooterBle() + ", getChangeMode()=" + getChangeMode()
				+ ", getLowSpeedLimit()=" + getLowSpeedLimit() + ", getMidSpeedLimit()=" + getMidSpeedLimit()
				+ ", getHighSpeedLimit()=" + getHighSpeedLimit() + ", getImei()=" + getImei() + "]";
	}
	
	
}
