package com.omni.scooter.entity.ninebot;

import com.omni.scooter.entity.BaseEntity;

public class ScooterSet1Entity extends BaseEntity {

	public ScooterSet1Entity() {
		super();
	}

	public ScooterSet1Entity(String content) {
		super(content);
	}
	
	/**
	 * 大灯开关。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getLightStatus(){
		return getInt(tCont[4]);
	}

	
	/**
	 * 速度模式设置返回。
	 * @return 0：无效，1：低速，2：中速，3：高速。
	 */
	public int getSpeedModeStatus(){
		return getInt(tCont[5]);
	}
	
	/**
	 * 油门响应。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getThrottleStatus(){
		return getInt(tCont[6]);
	}
	
	/**
	 * 前灯闪烁状态。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getHeadlightStatus(){
		return getInt(tCont[7]);
	}
	
	/**
	 * 尾灯闪烁状态。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getTailLightStatus(){
		return getInt(tCont[8]);
	}

	@Override
	public String toString() {
		return "ScooterSetEntity [getLightStatus()=" + getLightStatus() + ", getSpeedModeStatus()="
				+ getSpeedModeStatus() + ", getThrottleStatus()=" + getThrottleStatus() + ", getHeadlightStatus()="
				+ getHeadlightStatus() + ", getTailLightStatus()=" + getTailLightStatus() + ", getImei()=" + getImei()
				+ "]";
	}

	 
	
	
}
