package com.omni.scooter.entity;

import java.io.Serializable;
 
/**
 * 锁发给服务器的 操作请求信息实体类
 * @author cxiaox
 * @date 2018年8月31日 下午2:23:43
 */
public class ControlRequestEntity extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -896107558786928178L;

	/**
	 * 无参构造器
	 */
	public ControlRequestEntity() {
		super();
	}
	/**
	 * 带参构造器
	 * @param content 操作请求指令
	 */
	public ControlRequestEntity(String content) {
		super(content);
	}
	
	/**表示解锁(开锁)操作*/
	public static final int TYPE_OPEN = 0;
	/**表示锁车(关锁)操作*/
	public static final int TYPE_CLOSE = 1;
	
	/**表示解锁(开锁)操作*/
	public static final int TYPE_RFID_OPEN = 2;
	/**表示锁车(关锁)操作*/
	public static final int TYPE_RFID_CLOSE = 3;
	
	/**
	 * 获取到请求操作的类型
	 * {@link #TYPE_OPEN},{@link #TYPE_CLOSE}
	 * @return 1：表示锁车(关锁)操作，0:表示解锁(开锁)操作
	 */
	public int getControlType(){
		return getInt(tCont[4]);
	}
	/**
	 * 获取到操作通信KEY，用于开关锁指令
	 * @return 0-255的值。
	 */
	public int getControlKey(){
		return getInt(tCont[5]);
	}
	/**
	 * 获取到用户ID，与服务器发送R0指令时发送的一致。
	 * @return 数据库中的用户ID。
	 */
	public int getUserId(){
		return getInt(tCont[6]);
	}
	
	/**
	 * 返回操作序列号,与服务器发送R0指令时发送的一致。这里建议使用Unix 时间戳为序列号
	 * @return long类型的正整数
	 */
	public long getControlRequestId(){
		return getLong(tCont[7]);
	}
	@Override
	public String toString() {
		return "ControlRequestEntity [controlType=" + getControlType() + ", controlKey=" + getControlKey()
				+ ", userId=" + getUserId() + ", controlRequestId=" + getControlRequestId() + ", imei="
				+ getImei() + "]";
	}
	
	
	
}
