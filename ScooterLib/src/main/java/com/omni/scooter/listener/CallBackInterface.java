package com.omni.scooter.listener;

import org.apache.mina.core.session.IoSession;

public interface CallBackInterface {
	
	
	/**
	 * 多条指令同时回调的方法
	 */
	void onMoreCommand(IoSession session, String message); 
	
	/**
	 * 处理 指令的方法
	 * 多条指令会被分割成单条指令，进入该方法被处理
	 */
	void doHandCommand(IoSession session, String message); 
	
	/**
	 * 接收指令失败的方法
	 * @param msg 接收到的信息
	 */
	void onFailure(String msg);
 
	/**
	 * 接收指令成功的方法
	 * @param session 连接会话对象
	 * @param message  指令对象
	 */
	void onSuccessful(IoSession session, Object message);
}
