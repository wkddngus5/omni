package com.pgt.bike.lock.lib.tcp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;


public class SessionMap {
	
	public static  final int     STATUS_SEND_SESSION_NULL = 0;
	public static  final int     STATUS_SEND_SUCCESSFULLY = 1;
	public static  final int     STATUS_SEND_FAILING = 2;
	
	
	
//	private Map<Long,IoSession> map =new HashMap<Long, IoSession>();
	// 线程安全的 map
	private Map<Long,IoSession> map =new ConcurrentHashMap<Long, IoSession>();
	
	
	//单例模式 线程安全，高效实现
	private static volatile SessionMap instance ;
	private SessionMap(){}
	public static SessionMap newInstance(){
		if(instance==null){
			synchronized (SessionMap.class) {
				if(instance==null){
					instance = new SessionMap();
				}
			}
		}
		return instance;
	}
	
	
	public void addSession(Long key ,IoSession session){
		if(map.containsKey(key)){
			// close the old session
//			System.out.println("map close imei="+key+",map= "+map);
			map.get(key).closeNow();
		}
		// add new session
		map.put(key, session);
	}
	public void removeSession (IoSession session){
		Set<Entry<Long, IoSession>> set= map.entrySet();
		Iterator<Entry<Long, IoSession>> ite= set.iterator();
		while(ite.hasNext()){
			Entry<Long,IoSession> entry= ite.next();
			IoSession io = entry.getValue();
			if(session.equals(io)){
//				Long key = entry.getKey();
//				map.remove(key);
				
				// 单线程使用 Iterator . remove ,防止ConcurrentModificationException
				//
				ite.remove();
				break;
			}
		}	
	}
	public IoSession getSession(Long key){
		return map.get(key);
	}
	public boolean contaninsKey(Long key){
		return map.containsKey(key);
	}
	
	public int sendMessage(Long key,String msg){
		return sendMessage(key,msg,false);
	}
	
	/**
	 * 发送字符串
	 * @param key 
	 * @param msg
	 * @param isDebug
	 * @return
	 */
	public int sendMessage(Long key,String msg,boolean isDebug){
		IoSession session = getSession(key);
		if(session==null){
			boolean hasNot= map.containsKey(key);
//			System.out.println("IMEI:"+key+" hasNot ="+hasNot);
			System.out.println("IMEI:"+key+" has not connected the service");
			return STATUS_SEND_SESSION_NULL;
		}
		if(isDebug && session != null){
			System.out.println("debug write ip="+session.getRemoteAddress().toString());
		}
		

		session.write(msg);
		
		return STATUS_SEND_SUCCESSFULLY;
	}
	public int sendMessage(Long key,byte[] order,boolean isDebug){
		return sendMessageArray(key, order,isDebug);
	}
	
	/**
	 * 使用IoBuffer 发送byte[]
	 * @param key
	 * @param order
	 * @return 0-没有session实例,1-信息发送成功,2-信息发送失败
	 */
	public int sendMessageArray(Long key,byte[] order,boolean isDebug){
		IoSession session = getSession(key);
		if(session==null) {
			 
			System.out.println("IMEI:"+key+" has not connected the service ");
			return STATUS_SEND_SESSION_NULL;
		}
		if(isDebug && session != null && session.getRemoteAddress() != null){
			System.out.println("debug write ip="+session.getRemoteAddress().toString());
		}
		session.write(IoBuffer.wrap(order));
		
		return STATUS_SEND_SUCCESSFULLY;
	}
}
