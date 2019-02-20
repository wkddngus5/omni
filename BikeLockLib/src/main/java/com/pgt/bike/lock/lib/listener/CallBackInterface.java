package com.pgt.bike.lock.lib.listener;

import org.apache.mina.core.session.IoSession;

public interface CallBackInterface {
	public void setDeviceType(String deviceType) ;
	void onFailure(String msg);
 
	void onSuccessful(IoSession session, Object message);
}
