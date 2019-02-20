package com.pgt.bike.lock.lib.entity;

public class LockBikeInfoEntity  extends BaseEntity{


	
	public LockBikeInfoEntity() {
		super();
	}


	public LockBikeInfoEntity(String content) {
		super(content);
	}


	/**
	 * 锁电量(电压值)
	 * @return 420=4.2V
	 */
	public int getLockPower(){
		return getInt(tCont[5]);
	}
	
	
	/**
	 * 锁的GSM 信息值
	 * @return  值越大连接网络信息越好
	 */
	public int getGSM(){
		 
		return getInt(tCont[6]);
	 
	}


	/**
	 * 锁定位使用的GPS 卫星个数
	 * @return 
	 */
	public int getGpsNumber(){
		if(tCont.length>=8){
			return getInt(tCont[7]);
		}
		return -1;
		 
	 
	}
	
	/**
	 * 锁状态,开锁或者关锁
	 * @return
	 */
	public int getLockStatus(){
		if(tCont.length>=9){
			return getInt(tCont[8]);
		}
		return -1;
		 
	}
	 
	/**
	 * 报警状态
	 * @return
	 */
	public int getAlarmStatus(){
		if(tCont.length>=10){
			return getInt(tCont[9]);
		}
		return -1;
	}
	
	/**
	 * 电动车电量
	 * @return
	 */
	public int getBikePower(){
		if(tCont.length>=11){
			return getInt(tCont[10]);
		}
		return -1;
	}


	@Override
	public String toString() {
		return "LockBikeInfoEntity [getLockPower()=" + getLockPower() + ", getGSM()=" + getGSM() + ", getGpsNumber()="
				+ getGpsNumber() + ", getLockStatus()=" + getLockStatus() + ", getAlarmStatus()=" + getAlarmStatus()
				+ ", getBikePower()=" + getBikePower() + ", getImei()=" + getImei() + "]";
	}


	 
	
	
	
	
	
	
}
