package com.omni.scooter.hand;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.omni.scooter.listener.CallBackInterface;

 

 

public abstract class BaseServerHandler extends IoHandlerAdapter{
	private CallBackInterface listener=null;
	
	/**
	 * 设置过滤的指令头
	 * @return
	 */
	protected abstract String getFilterCommandHead();
	
	public BaseServerHandler( ){
	 
	}
	public BaseServerHandler(CallBackInterface listener){
		this.listener = listener;
		
	}
	
	public CallBackInterface getListener() {
		return listener;
	}
	public void setListener(CallBackInterface listener) {
		this.listener = listener;
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
			if(str.contains(getFilterCommandHead())&& str.contains("#")){
				int firstIndex = str.indexOf('#');
				if(firstIndex==-1){
					// 不是完整的指令
					listener.onFailure(message.toString());
				}else {
					listener.onSuccessful(session,message);
					int lastIndex =str.lastIndexOf('#');
					
					if(firstIndex==lastIndex){
						// 信息没有堵塞，只有一条指令过来 
					    // 找指令头
						int startIndex = str.indexOf(getFilterCommandHead());
						String order = str.substring(startIndex, lastIndex+1);
						 
						listener.doHandCommand(session ,order);
						 
					}else{ 
						// 多条指令过来
						listener.onMoreCommand(session,str);
						String[] strMsgChs = str.split("#");
						for(String  oneStr :strMsgChs){
							int startIndex = oneStr.indexOf(getFilterCommandHead());
							String oneComm=oneStr.substring(startIndex, oneStr.length());
							listener.doHandCommand(session ,String.format("%s#", oneComm));
						}
					}
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
	}

	public void sessionCreated(IoSession session) throws Exception {
 
	}

	public void sessionClosed(IoSession session) throws Exception {
 
	}

	public void sessionOpened(IoSession session) throws Exception {
 
	}

}
