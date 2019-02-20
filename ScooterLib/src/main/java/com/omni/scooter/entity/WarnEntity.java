package com.omni.scooter.entity;

public class WarnEntity extends BaseEntity {

	public WarnEntity() {
		super();
	}

	public WarnEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取报警指令状态
	 * @return  1:非法移动报警 2:倒地报警 3:非法拆除报警
	 */
	public int getWarnStatus(){
		return getInt(tCont[4]);
	}

	@Override
	public String toString() {
		return "WranEntity [getWarnStatus()=" + getWarnStatus() + ", getImei()=" + getImei() + "]";
	}
	
	

}
