package com.pgt.bike.lock.lib.entity;

public class LockBatteryPowerEntity  extends BaseEntity{

	 
	
	
	
	public LockBatteryPowerEntity() {
		super();
	}


	public LockBatteryPowerEntity(String content) {
		super(content);
	}


	/**
	 * 返回控制操作结果
	 * @return 0-成功，1-失败
	 */
	public int getResult(){
		return getInt(tCont[5]);
	}
	
	
	/**
	 * 返回控制操作类型
	 * @return -1,0,1。 -1 表示无法获取操作类型,0：开启电源操作,1：关闭电池电源
	 */
	public int getControlType(){
		if(tCont.length>=7){
			return getInt(tCont[6]);
		}
		return -1;
	}


	@Override
	public String toString() {
		return "LockBatteryPowerEntity [result()=" + getResult() + ", ControlType()=" + getControlType()
				+ ", Imei()=" + getImei() + "]";
	}
}
