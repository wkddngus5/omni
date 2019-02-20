package com.pgt.bike.lock.lib.entity;

public class LockHeartEntity  extends BaseEntity{

	 
	public LockHeartEntity() {
		super();
	}


	public LockHeartEntity(String content) {
		super(content);
	}


	/**
	 * 返回锁状态
	 * @return 0-成功，1-失败
	 */
	public int getLockStatus(){
		return getInt(tCont[5]);
	}
	
	
	/**
	 * 锁电量  电压值
	 * @return 240=2.4V ,-1 表示无法获取锁电压
	 */
	public int getLockPower(){
		if(tCont.length>=7){
			return getInt(tCont[6]);
		}
		return -1;
	}
	
	
	/**
	 * 锁的 GSM 信息值。
	 * @return 显示GSM 格式，-1表示无法获取
	 */
	public int getGsm(){
		if(tCont.length>=8){
			return getInt(tCont[7]);
		}
		return -1;
	}
	
	/**
	 * 报警状态（）
	 * @return  
	 */
	public int getAlarmStatus(){
		if(tCont.length>=9){
			return getInt(tCont[8]);
		}
		return -1;
	}
	
	
	/**
	 * 电动车电量，百分比格式
	 * @return 百分比格式显示电动车的电量，-1表示无法获取
	 */
	public int getBikePower(){
		if(tCont.length>=10){
			return getInt(tCont[9]);
		}
		return -1;
	}


	@Override
	public String toString() {
		return "LockHeartEntity [getLockStatus()=" + getLockStatus() + ", getLockPower()=" + getLockPower()
				+ ", getGsm()=" + getGsm() + ", getAlarmStatus()=" + getAlarmStatus() + ", getBikePower()="
				+ getBikePower() + ", getImei()=" + getImei() + "]";
	}


	 
	
	

}
