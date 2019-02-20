package com.omni.scooter.entity;

public class OpenLockEntity extends BaseEntity {
	/** 表示开锁成功 */
	public static final int STATUS_OPEN_SUCCESS = 0;
	/** 表示开锁失败 */
	public static final int STATUS_OPEN_FAILURE =1;
	/** KEY 错误或失效 */
	public static final int STATUS_OPEN_KEY_ERROR = 2;

	public OpenLockEntity() {
		super();
	}

	public OpenLockEntity(String content) {
		super(content);
	}
	
	/**
	 * 获取开锁状态
	 * {@link #STATUS_OPEN_SUCCESS},
	 * {@link #STATUS_OPEN_FAILURE},
	 * {@link #STATUS_OPEN_KEY_ERROR}
	 * @return 0-开锁成功，1-开锁失败，2-KEY 错误或失效
	 */
	public int getOpenStatus(){
		return getInt(tCont[4]);
	}
	
	/**
	 * 获取用户ID ,与服务器发送L0指令的一致
	 * @return 数据库中的用户ID
	 */
	public int getUserId(){
		return getInt(tCont[5]);
	}
	
	/**
	 * 获取开锁序列号ID ,与服务器发送L0指令的一致。这里建议使用Unix timestamp
	 * @return  开锁序列号。
	 */
	public long getOpenId(){
		return getLong(tCont[6]);
	}

	@Override
	public String toString() {
		return "OpenLockEntity [openStatus=" + getOpenStatus() + ", userId=" + getUserId() + ", openId="
				+ getOpenId() + ", imei=" + getImei() + "]";
	}
}
