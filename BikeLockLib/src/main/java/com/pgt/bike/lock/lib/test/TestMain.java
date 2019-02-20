package com.pgt.bike.lock.lib.test;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;

import com.pgt.bike.lock.lib.listener.TCPCallBackBicmanListener;
import com.pgt.bike.lock.lib.listener.TCPCallBackFenceListener;
import com.pgt.bike.lock.lib.listener.TCPCallBackListener;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bike.lock.lib.up.UpFileUtil;
import com.pgt.bike.lock.lib.utils.CRCUtil;
import com.pgt.bike.lock.lib.utils.CommandUtil;

public class TestMain {
	public static void main(String[] args) throws IOException {
		
		
		
//		byte[] tData=new byte[]{(byte) 0xFE,0x69,0x00,0x26,0x36,0x00};
//		int crc = CRCUtil.calcCRC(tData);
//		System.out.println("crc="+crc);
//		System.out.println("crc "+String.format("0x%02X", crc));
//		
		TCPService tcpService = new TCPService(9666,new TCPCallBackListener("OL"),new TCPCallBackFenceListener(),new TCPCallBackBicmanListener());
////		
		tcpService.start();
//		
// 
//		
		
//		byte[] sleep=new byte[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//		byte[] order= CommandUtil.getLockCommand(sleep, "GM", "863725031194671",  1);
//		System.out.println("command="+new String(order,0,order.length));
		
		
		//2237.75314
		//11408.62621
//		
//		String lat ="2237";//GPS 上传的纬度坐标ddmm.mmmm 格式
//		String lng ="11408";//GPS 上传的经度坐标dddmm.mmmm 格式
		
		//1.将GPS 上传的度分格式坐标，转换成度格式坐标
//		String lat ="2237.75314";//GPS 上传的纬度坐标ddmm.mmmm 格式
//		String lng ="11408.62621";//GPS 上传的经度坐标dddmm.mmmm 格式
//		String latFirst =lat.substring(0,2); // 纬度坐标的 度
//		String lngFirst =lng.substring(0,3); // 经度坐标的 度
//		String latSub =lat.substring(2,lat.length()); // 纬度坐标的 分
//		String lngSub = lng.substring(3,lng.length());// 经度坐标的 分
//		double wgs84Lat=Double.parseDouble(latFirst)+Double.parseDouble(latSub) /60D;
//		double wgs84Lng=Double.parseDouble(lngFirst)+Double.parseDouble(lngSub) /60D;
		//2. 将GPS转换成 高德坐标系
		//该过程参考高德地图官方坐标转换API。
		
//		double n=0.12345678910;
//		NumberFormat nf = NumberFormat.getNumberInstance();
//		nf.setMaximumFractionDigits(6);
//		System.out.println("format n="+nf.format(n));
		
//		String path="E:/sn32f700b.aes";
//		UpFileUtil.init(path);
//		int allCRC=UpFileUtil.getAllCRC();
//		System.out.println("all crc="+String.format("%02X", allCRC));
//		System.out.println("all totalPack="+UpFileUtil.getTotalPack());
//		System.out.println("all size="+UpFileUtil.getSize());
//		
//		System.out.println("0 data="+Arrays.toString(UpFileUtil.getA1UpdateBytes(0)));
//		System.out.println("0 crc="+UpFileUtil.getA1UpdateCRC(0));
//		
//		System.out.println("1 data="+Arrays.toString(UpFileUtil.getA1UpdateBytes(1)));
//		System.out.println("1 crc="+UpFileUtil.getA1UpdateCRC(1));
	}

}
