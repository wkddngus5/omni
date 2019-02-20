package com.omni.scooter.command;

import java.util.Calendar;

import com.omni.scooter.entity.Command;


 

 

public   class BaseCommandUtil {
	
	public static final int  POWER_OFF = 1;
	public static final int  POWER_ON = 2;
	
	protected static byte[] order00=new byte[]{(byte) 0xFF,(byte) 0xFF};
 
	/**
	 * 获取指令方法
	 * @param sendHead  发送的指令头
	 * @param code 发送给的指令代码
	 * @param IMEI 设备IMEI号
	 * @param orderCode 指令类型
	 * @param content 指令内容
	 * @return 指令数组
	 */
	public static byte[] getCommand(String sendHead,String code,String IMEI,String orderCode,String content){
		String sImei = String.format("%015d", Long.parseLong(IMEI));
		String command =String.format("%s,%s,%s,%s%s\n",sendHead,code,sImei,orderCode,content);
		byte[] orderContect=command.getBytes(); 
		return add(order00,orderContect);
	}
	
	protected   static byte[] add(byte[] b1,byte[] b2){
		byte[] result =new byte[b1.length+b2.length];
		System.arraycopy(b1,0, result, 0, b1.length);
		System.arraycopy(b2,0, result, b1.length, b2.length);
		return result;
	}
 
}
