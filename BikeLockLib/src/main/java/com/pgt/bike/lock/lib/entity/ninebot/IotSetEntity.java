package com.pgt.bike.lock.lib.entity.ninebot;

import com.pgt.bike.lock.lib.entity.BaseEntity;

/**
 * nineboot 滑板车IOT 设备设置信息 回调实体
 * @author cxiaox
 *
 * @date 2018年11月1日 下午3:03:39
 */
public class IotSetEntity extends BaseEntity {

	public IotSetEntity() {
		super();
	}

	public IotSetEntity(String content) {
		super(content);
	}
	
	/**
	 * 获取加速度灵敏度，默认为2：中。
	 * @return 0：无效，1：低，2：中，3：高。
	 */
	public int getSensitivity(){
		return getInt(tCont[4]);
	}

	
	/**
	 * 骑行中是否上传滑板车信息1(S6)，默认为2：开启。
	 * @return 0：无效，1：关闭，2：开启。
	 */
	public int getInfo1Status(){
		return getInt(tCont[5]);
	}
	
	/**
	 * 心跳上传间隔，默认为240S。
	 * @return 0：无效，单位：秒，默认240S。
	 */
	public int getHeartInterval(){
		return getInt(tCont[6]);
	}
	
	/**
	 * 骑行状态下滑板车信息1(S6)上传间隔，默认为10S。
	 * @return 0：无效，单位：秒，默认10S。
	 */
	public int getInfo1Interval(){
		return getInt(tCont[7]);
	}

	@Override
	public String toString() {
		return "IotSetEntity [sensitivity=" + getSensitivity() + ", info1Status=" + getInfo1Status()
				+ ", heartInterval=" + getHeartInterval() + ",info1Interval=" + getInfo1Interval()
				+ ", imei=" + getImei() + "]";
	}
	
	
}
