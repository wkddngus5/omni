package com.omni.scooter.entity.omni;

import com.omni.scooter.entity.BaseEntity;

/**
 * 固件升级OK
 * @author cxiaox
 * 2018-08-09
 *
 */
public class UpdateResultEntity extends BaseEntity{
	public static final int RESULT_OK = 0;
	public static final int RESULT_FAILT = 1;
	

	public UpdateResultEntity() {
		super();
	}

	public UpdateResultEntity(String content) {
		super(content);
	}
	

	/**
	 * 升级结果
	 * @return  0-1 。 0:成功,1:失败
	 */
	public int getResult(){
		return getInt(tCont[5]);
	}
	/**
	 *升级设备识别码Iot 设备识别码（固定 85）
	 * @return  字符串。"8A" omni iot 设备 。 "CT" 控制器设备
	 */
	public String getDeviceCode(){
		return  tCont[4];
	}
	 

	@Override
	public String toString() {
		return "UpdateOKEntity [getDeviceCode()=" + getDeviceCode() + ", getResult()=" + getResult()
				 + ", getImei()=" + getImei() + "]";
	}
	

	
}
