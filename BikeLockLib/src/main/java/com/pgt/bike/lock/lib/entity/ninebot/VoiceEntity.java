package com.pgt.bike.lock.lib.entity.ninebot;

import com.pgt.bike.lock.lib.entity.BaseEntity;

public class VoiceEntity extends BaseEntity {

	public VoiceEntity() {
		super();
	}

	public VoiceEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取语音播放指令状态
	 * @return  1:使出 Geofence 提示 2:找车提示 3:低电量提醒
	 */
	public int getVoiceStatus(){
		return getInt(tCont[4]);
	}

	@Override
	public String toString() {
		return "VoiceEntity [getVoiceStatus()=" + getVoiceStatus() + ", getImei()=" + getImei() + "]";
	}

	 
	
	

}
