package com.omni.scooter.entity.omni;

import com.omni.scooter.entity.BaseEntity;
/**
 * omni RFID 请求开 锁发给服务器的指令信息实体
 * @author cxiaox
 * @date 2018年11月10日 下午13:42:00
 */
public class RFIDRequestEntity extends BaseEntity {
	
	public static final int TYPE_CONTROL_OPEN = 0;
	public static final int TYPE_CONTROL_CLOSE = 1;
	

	public RFIDRequestEntity() {
		super();
	}

	public RFIDRequestEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取操作类型
	 * @return  0:解锁操作,1:锁车操作  
	 */
	public int getControlType(){
		return getInt(tCont[4]);
	}
	
	/**
	 * 卡类型
	 * @return  0:type a卡,1:type B 卡  
	 */
	public int getCardType(){
		return getInt(tCont[5]);
	}
	
	/**
	 * id
	 * @return  卡ID 
	 */
	public String getCardId(){
		return getString(tCont[6]);
	}

	@Override
	public String toString() {
		return "RFIDRequestEntity [controlType=" + getControlType() +",CardType=" + getCardType() +",getCardId=" + getCardId() + ", imei=" + getImei() + "]";
	}

	 
	
	

}
