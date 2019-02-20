package com.pgt.bike.lock.lib.test;

import java.io.IOException;
import java.util.Arrays;

import com.pgt.bike.lock.lib.up.DeviceType;
import com.pgt.bike.lock.lib.up.UpFileUtil;

public class TestUpFile {
	public static void main(String[] args) throws IOException {
		String path="E:/sn32f700b.aes";
		UpFileUtil.init(DeviceType.GPRS_GPS_BLE,path);
		
		int allCRC=UpFileUtil.getAllCRC(DeviceType.GPRS_GPS_BLE);
		System.out.println("all crc="+String.format("%02X", allCRC));
		System.out.println("all totalPack="+UpFileUtil.getTotalPack(DeviceType.GPRS_GPS_BLE));
		System.out.println("all size="+UpFileUtil.getSize());
		
		System.out.println("0 data="+Arrays.toString(UpFileUtil.getUpdateBytes(DeviceType.GPRS_GPS_BLE,0)));
		System.out.println("0 crc="+UpFileUtil.getUpdateCRC(DeviceType.GPRS_GPS_BLE,0));
		
		System.out.println("1 data="+Arrays.toString(UpFileUtil.getUpdateBytes(DeviceType.GPRS_GPS_BLE,1)));
		System.out.println("1 crc="+UpFileUtil.getUpdateCRC(DeviceType.GPRS_GPS_BLE,1));
		
		 
		
	}

}
