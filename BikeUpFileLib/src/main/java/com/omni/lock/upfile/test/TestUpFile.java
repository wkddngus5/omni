package com.omni.lock.upfile.test;

import java.io.IOException;
import java.util.Arrays;

import com.omni.lock.upfile.DeviceType;
import com.omni.lock.upfile.UpFileUtil;

 

public class TestUpFile {
	public static void main(String[] args) throws IOException {
		
//		String path="E:/proxy-custom.pac";
		String path="D:/lock_b3_67.aes";
//		String path="E:/cx/apache-tomcat-7.0.55/webapps/CarportServer/META-INF/carport.aes";
//		UpFileUtil.init(DeviceType.GPRS_GPS_BLE, path);
	
		UpFileUtil.setControlXMPath(path);
		UpFileUtil.reLoad();
		
		int deviceType = DeviceType.CONTROL_XM;
		int allCRC=UpFileUtil.getAllCRC(deviceType);
		int totalPack = UpFileUtil.getTotalPack(deviceType) ;
		
		System.out.println("all crc="+String.format("=0x%02X", allCRC));
//		System.out.println("all crc="+String.format("%s", allCRC));
		System.out.println("all totalPack="+totalPack);
		System.out.println("all size(每包128字节)="+UpFileUtil.getSize());
		
//		for(int i=0;i<UpFileUtil.getTotalPack(deviceType);i++){
//			System.out.println( String.format("pack %s: data=%s",i, toHexStr(UpFileUtil.getUpdateBytes(deviceType,i))));
//			System.out.println( String.format("pack %s: crc=0x%04X",i, UpFileUtil.getUpdateCRC(deviceType,i)));
//		}
//		
//		String head="*CMDS,OM,865793033516257,000000000000,U1,0,37843,";
//		System.out.println("head="+ toHexStr(head.getBytes()) );
//		System.out.println("head 10="+ toShiStr(head.getBytes()) );
//		System.out.println("0 data 16="+ toHexStr(UpFileUtil.getUpdateBytes(deviceType,0)) );
//		System.out.println("0 data 10="+ toShiStr(UpFileUtil.getUpdateBytes(deviceType,0)) );
//		System.out.println("0 data="+ Arrays.toString(UpFileUtil.getUpdateBytes(DeviceType.GPRS_GPS_BLE,0)) );
//		System.out.println("0 crc==0x"+String.format("%02X", UpFileUtil.getUpdateCRC(deviceType,0)));
//		System.out.println("0 crc== "+String.format("%s", UpFileUtil.getUpdateCRC(deviceType,0)));
		
//		System.out.println("1 data="+toHexStr(UpFileUtil.getUpdateBytes(deviceType,1)));
//		System.out.println("1 crc=0x"+String.format("%02X", UpFileUtil.getUpdateCRC(deviceType,1)));
		
//		path="E:/2in10703.bin.aes";
//		UpFileUtil.init(DeviceType.GPRS_GPS,path);
//		allCRC=UpFileUtil.getAllCRC(DeviceType.GPRS_GPS);
//		System.out.println("all crc="+String.format("%02X", allCRC));
//		System.out.println("all totalPack="+UpFileUtil.getTotalPack(DeviceType.GPRS_GPS));
//		System.out.println("all size="+UpFileUtil.getSize());
//		
//		System.out.println("0 data="+Arrays.toString(UpFileUtil.getUpdateBytes(DeviceType.GPRS_GPS,0)));
//		System.out.println("0 crc="+UpFileUtil.getUpdateCRC(DeviceType.GPRS_GPS,0));
//		
//		System.out.println("1 data="+Arrays.toString(UpFileUtil.getUpdateBytes(DeviceType.GPRS_GPS,1)));
//		System.out.println("1 crc="+UpFileUtil.getUpdateCRC(DeviceType.GPRS_GPS,1));
		
		
	}

	
	private static String toHexStr(byte[] data){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<data.length;i++){
			sb.append(String.format("%02X ", data[i]));
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
		
	}
	private static String toShiStr(byte[] data){
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for(int i=0;i<data.length;i++){
			sb.append(String.format("%s ", data[i]&0xFF));
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
		
	}
}
