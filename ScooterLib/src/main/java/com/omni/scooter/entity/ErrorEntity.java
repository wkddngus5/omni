package com.omni.scooter.entity;

/**
 * 控制器故障上报
 * @author cxiaox
 * 2019-08-09
 *
 */
public class ErrorEntity extends BaseEntity {

	public ErrorEntity() {
		super();
	}

	public ErrorEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 错误代码
	 * @return  
	 */
	public int getErrorCode(){
		return getInt(tCont[4]);
	}

	@Override
	public String toString() {
		return "ErrorEntity [getErrorCode()=" + getErrorCode() + ", getImei()=" + getImei() + "]";
	}

	 
	
	

}
