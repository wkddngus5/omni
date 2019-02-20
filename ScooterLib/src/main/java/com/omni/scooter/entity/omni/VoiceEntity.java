package com.omni.scooter.entity.omni;

import com.omni.scooter.entity.BaseEntity;
/**
 * omni 语音播报 锁发给服务器的指令信息实体
 * @author cxiaox
 * @date 2018年8月31日 下午5:31:43
 */
public class VoiceEntity extends BaseEntity {

	public VoiceEntity() {
		super();
	}

	public VoiceEntity(String content) {
		super(content);
	}
	
	
	/**
	 * 获取语音播放指令状态
	 * @return  1:保留 提示 2:找车提示  
	 */
	public int getVoiceStatus(){
		return getInt(tCont[4]);
	}

	@Override
	public String toString() {
		return "VoiceEntity [getVoiceStatus()=" + getVoiceStatus() + ", getImei()=" + getImei() + "]";
	}

	 
	
	

}
