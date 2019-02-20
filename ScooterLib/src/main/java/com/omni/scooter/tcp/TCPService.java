package com.omni.scooter.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.omni.scooter.command.OmniCommand;
import com.omni.scooter.entity.Command;
import com.omni.scooter.hand.BaseServerHandler;
import com.omni.scooter.hand.OmniScooterHandler;
import com.omni.scooter.listener.CallBackInterface;
import com.omni.scooter.listener.IScooterCallback;

 

 

 
 
public class TCPService {
	
	/**
	 * 服务器端口
	 */
	private int port ; // 服务器端口
	private IoAcceptor acceptor;
	private CallBackInterface listener;
	/**
	 * 
	 * @param port    服务器TCP 监听端口
	 * @param acceptor IoAcceptor
	 * @param listener  客户端发送过来的回调监听
	 */
	public TCPService(int port,IoAcceptor acceptor){
		this.port = port;
		this.acceptor = acceptor;
	}
	
	/**
	 * 
	 * @param port
	 * @param serverHandler  目前有两种实现类，OmniScooterHandler, NinebotScooterHandler
	 * @param listener 目前有两种实现类，OmniScooterListener, NinebotScooterListener
	 */
	public TCPService(int port,BaseServerHandler serverHandler,IScooterCallback listener){
		this.port = port;
		serverHandler.setListener(listener);
		
		acceptor = new NioSocketAcceptor();
		// 添加日志
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		// 添加编码解码器
		acceptor.getFilterChain().addLast("codec", 
				new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"))));
		acceptor.getFilterChain().addLast("exceutor",new ExecutorFilter());
		// 添加处理器(用于接收数据后处理数据逻辑)
		acceptor.setHandler(serverHandler);
		
		//设置读取数据缓存byte
		acceptor.getSessionConfig().setReadBufferSize(2048);
	}
	
 
	
	
	
	/**
	 *  使用默认的 IoAcceptor(NioSocketAcceptor)
	 *  默认的识别 omni 类型
	 * @param port
	 * @param listener
	 */
	public TCPService(int port,IScooterCallback listener){
		this(port,new OmniScooterHandler(),listener);

	}
	/**
	 * 
	 * @param port
	 * @param listener  
	 * @param deviceType  filter device type
	 */
	public TCPService(int port,IScooterCallback listener,String deviceType){
		listener.setFilterDeviceType(deviceType);
		OmniCommand.setDeviceType(deviceType);
		this.port = port;
		
		
		BaseServerHandler serverHandler=new OmniScooterHandler();
		serverHandler.setListener(listener);
		
		acceptor = new NioSocketAcceptor();
		// 添加日志
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		// 添加编码解码器
		acceptor.getFilterChain().addLast("codec", 
				new ProtocolCodecFilter(
				new TextLineCodecFactory(Charset.forName("UTF-8"))));
		acceptor.getFilterChain().addLast("exceutor",new ExecutorFilter());
		// 添加处理器(用于接收数据后处理数据逻辑)
		acceptor.setHandler(serverHandler);
		
		//设置读取数据缓存byte
		acceptor.getSessionConfig().setReadBufferSize(2048);
	}
	 
	/**
	 * 设置指令发送时的CODE
	 * @param code
	 */
	public void setCommandCode(String code){
		Command.CODE=code;
	}
	
	public void setIdleTime(IdleStatus idleStatus,int second){ 
		acceptor.getSessionConfig().setIdleTime(idleStatus, second);
	}
	
	public void start () throws IOException{
				// 绑定某个端口，作为数据入口
		acceptor.bind(new InetSocketAddress(port));
		
	}
	
	public static int sendOrder(long key,String msg,boolean isDebug){
		return SessionMap.newInstance().sendMessage(key, msg,isDebug);
		 
	}
	public static int sendOrder(long key,byte[] order,boolean isDebug){
		return SessionMap.newInstance().sendMessage(key, order,isDebug);
	}
	public static int sendOrder(long key,String msg){
		return SessionMap.newInstance().sendMessage(key, msg,false);
		 
	}
	public static int sendOrder(String key,byte[] order){
		return sendOrder(Long.valueOf(key), order,false);		
	}
	public static int sendOrder(Long key,byte[] order){
		return SessionMap.newInstance().sendMessage(key, order,false);		
	}
	public static int sendOrderArray(Long key,byte[] order){
		return SessionMap.newInstance().sendMessageArray(key, order,false);		
	}
	public static int sendOrderArray(long key,byte[] order,boolean isDebug){
		return SessionMap.newInstance().sendMessageArray(key, order,isDebug);		
	}
	public static IoSession getIoSession(long key){
		return SessionMap.newInstance().getSession(key);
	}
	public static void addIoSession(long key,IoSession ioSession){
		SessionMap.newInstance().addSession(key, ioSession);
	}
	
	public void stop(){ 
	}
	
	
	
	private class Invalid{}
	

}
