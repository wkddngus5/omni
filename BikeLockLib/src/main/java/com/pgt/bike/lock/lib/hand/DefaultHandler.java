package com.pgt.bike.lock.lib.hand;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

import com.pgt.bike.lock.lib.listener.CallBackInterface;
import com.pgt.bike.lock.lib.tcp.SessionMap;

public class DefaultHandler extends ServerHandler {
	
	

	public DefaultHandler() {
		 
		 
	}

	public DefaultHandler(CallBackInterface listener) {
		super(listener);
	}
 

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		 
		super.exceptionCaught(session, cause);
	}

	@Override
	final public void messageReceived(IoSession session, Object message)
			throws Exception {
		 
		super.messageReceived(session, message);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
	 
		super.sessionIdle(session, status);
		
//		System.out.println("session="+session.getRemoteAddress().toString());
		
		
		if(status ==IdleStatus.READER_IDLE){
			SessionMap.newInstance().removeSession(session); 
			session.closeNow();
			 
		} 
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
//		System.out.println("sessionCreated ");
	
		
		
		
		SocketSessionConfig cfg= (SocketSessionConfig) session.getConfig();
		cfg.setKeepAlive(false);
		cfg.setSoLinger(0);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		
		 
		
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
	}
	
	
	
	
	@Override
	public void inputClosed(IoSession session) throws Exception {
		SessionMap.newInstance().removeSession(session); 
		super.inputClosed(session);
	}
	
	
	

}
