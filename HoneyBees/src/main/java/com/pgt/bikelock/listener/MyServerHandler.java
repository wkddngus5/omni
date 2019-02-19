package com.pgt.bikelock.listener;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.pgt.bike.lock.lib.hand.DefaultHandler;
public class MyServerHandler extends DefaultHandler   {
	
	private int  conncetNum=0;

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		super.exceptionCaught(session, cause);
	}




	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		super.sessionIdle(session, status);

	}
	
	

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		conncetNum ++;
		System.out.println("create num="+conncetNum);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
	}
	
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		conncetNum--;
		System.out.println("close num="+conncetNum);
	}
	

}
