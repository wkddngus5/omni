package com.omni.scooter.entity.omni;

import com.omni.scooter.entity.BaseEntity;

 
/**
 * OMNI滑板车设置指令2 信息实体
 * @author cxiaox
 *
 * @date 2018年8月31日 下午5:07:59
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
	 * 启动方式 状态。
	 * @return 0：无效，1：非零启动，2：零启动。
	 */
	public int getStartType(){
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
	 * 按键开关大灯模式。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getChangeLight(){
		return getInt(tCont[8]);
	}
	
	/**
	 * 低速模式限速值。
	 * @return  0:无效（不设置） 范围：6-25。
	 */
	public int getLowSpeedLimit(){
		return getInt(tCont[9]);
	}

	 
	/**
	 * 中速模式限速值。
	 * @return  0:无效（不设置） 范围：6-25。
	 */
	public int getMidSpeedLimit(){
		return getInt(tCont[10]);
	}
	
	/**
	 * 高速模式限速值。
	 * @return  0:无效（不设置） 范围：6-25。
	 */
	public int getHighSpeedLimit(){
		return getInt(tCont[11]);
	}

	@Override
	public String toString() {
		return "ScooterSet2Entity [getInchStatus()=" + getInchStatus() + ", getCruiseControl()=" + getCruiseControl()
				+ ", getStartType()=" + getStartType() + ", getChangeMode()=" + getChangeMode()+ ", getChangeLight()=" + getChangeLight()
				+ ", getLowSpeedLimit()=" + getLowSpeedLimit() + ", getMidSpeedLimit()=" + getMidSpeedLimit()
				+ ", getHighSpeedLimit()=" + getHighSpeedLimit() + ", getImei()=" + getImei() + "]";
	}
	
	
}
