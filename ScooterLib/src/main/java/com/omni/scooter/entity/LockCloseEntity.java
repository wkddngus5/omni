package com.omni.scooter.entity;

/**
 * 表示 锁发给服务器的 关锁信息实体
 * @author cxiaox
 *
 * @date 2018年8月31日 下午4:00:23
 */
public class LockCloseEntity extends BaseEntity {
	/** 表示关锁成功 */
	public static final int STATUS_CLOSE_SUCCESS = 0;
	/** 表示关锁失败 */
	public static final int STATUS_CLOSE_FAILURE =1;
	/** KEY 错误或失效 */
	public static final int STATUS_CLOSE_KEY_ERROR = 2;

	public LockCloseEntity() {
		super();
	}

	public LockCloseEntity(String content) {
		super(content);
	}
	
	/**
	 * 获取关锁状态
	 * {@link #STATUS_CLOSE_SUCCESS},
	 * {@link #STATUS_CLOSE_FAILURE},
	 * {@link #STATUS_CLOSE_KEY_ERROR}
	 * @return 0-关锁成功，1-关锁失败，2-KEY 错误或失效
	 */
	public int getCloseStatus(){
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
	 * 关锁时，返回关锁的序列号
	 * 获取关锁序列号ID ,与服务器发送L1指令的一致。这里建议使用Unix timestamp
	 * @return  开锁序列号。
	 */
	public long getCloseId(){
		return getLong(tCont[6]);
	}
	
	
	/**
	 * 获取开锁时长，
	 * @return 开锁时长
	 */
	public int getOpenTime(){
		return getInt(tCont[7]);
	}
	

	@Override
	public String toString() {
		return "LockCloseEntity [closeStatus=" + getCloseStatus() + ", userId=" + getUserId() + ", closeId="
				+ getCloseId() + ",openTime="+getOpenTime()+ ", imei=" + getImei() + "]";
	}
	
	

}
