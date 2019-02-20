package com.omni.scooter.entity.omni;

import com.omni.scooter.command.ICommand;
import com.omni.scooter.entity.BaseEntity;
/**
 * omni  事件通知 发给服务器的指令信息实体
 * @author cxiaox
 * @date 2018年11月10日  16:45:43
 */
public class EventNoticeEntity extends BaseEntity {
	
	public static final   int EVENT_TYPE_RESERVATION_START= ICommand.EVENT_TYPE_RESERVATION_START;
	public static final   int EVENT_TYPE_RESERVATION_CANCEL= ICommand.EVENT_TYPE_RESERVATION_CANCEL;

	public EventNoticeEntity() {
		super();
	}

	public EventNoticeEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取 时间类型
	 * @return  1:关机, 2:重启,10: 预约车,11: 取消预约  
	 */
	public int getEventType(){
		return getInt(tCont[4]);
	}

	@Override
	public String toString() {
		return "EventNoticeEntity [eventType()=" + getEventType() + ", getImei()=" + getImei() + "]";
	}

	 
	
	

}
