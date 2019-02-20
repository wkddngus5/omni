package com.pgt.bike.lock.lib.hand;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.pgt.bike.lock.lib.listener.CallBackInterface; 
import com.pgt.bike.lock.lib.listener.TCPCallBackBicmanListener;
import com.pgt.bike.lock.lib.listener.TCPCallBackFenceListener;

public class ServerHandler extends IoHandlerAdapter{
	private CallBackInterface listener=null;
	private TCPCallBackFenceListener fenceListener=null;
	private TCPCallBackBicmanListener bicmanListener=null;
	public ServerHandler( ){
	 
	}
	public ServerHandler(CallBackInterface listener){
		this.listener = listener;
		
	}
	
	public CallBackInterface getListener() {
		return listener;
	}
	public void setListener(CallBackInterface listener) {
		this.listener = listener;
	}
	public void setFenceListener(TCPCallBackFenceListener fenceListener) {
		this.fenceListener = fenceListener;
	}
	
	public TCPCallBackBicmanListener getBicmanListener() {
		return bicmanListener;
	}
	public void setBicmanListener(TCPCallBackBicmanListener bicmanListener) {
		this.bicmanListener = bicmanListener;
	}
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception { 
		
		cause.printStackTrace();
	}

	// 消息接收
	public void messageReceived(IoSession session, Object message)
			throws Exception { 
		
		if(listener==null){
			throw new RuntimeException("CallBackInterface lister can not null");
		}
		
		String str = message.toString();
		 
		if(str!=null&& str.length()>10){
//			String commandHead = str.substring(0,5);
			if(str.contains("*CMDR")&& str.contains("#")){
				int firstIndex = str.indexOf('#');
				if(firstIndex==-1){
					// 不是完整的指令
					listener.onFailure(message.toString());
				}else {
					listener.onSuccessful(session,message);
				}
			}else if(str.contains("*BGFR")&& str.contains("#")){
				int firstIndex = str.indexOf('#');
				if(firstIndex==-1){
					// 不是完整的指令
					fenceListener.onFailure(message.toString());
				}else {
					fenceListener.onSuccessful(session,message);
				}
			}else if(str.contains("*HBCR")&& str.contains("#")){
				int firstIndex = str.indexOf('#');
				if(firstIndex==-1){
					// 不是完整的指令
					bicmanListener.onFailure(message.toString());
				}else {
					bicmanListener.onSuccessful(session,message);
				}
			} else{
				// 不是完整的指令
				listener.onFailure(message.toString());
			}
			 
		} 
	}

	//会话空闲
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception { 
		System.out.println("IDLE "+session.getIdleCount(status));
		
	}

 
	public void sessionCreated(IoSession session) throws Exception {
 
	}

	public void sessionClosed(IoSession session) throws Exception {
 
 
	}

	public void sessionOpened(IoSession session) throws Exception {
 
	}

}
