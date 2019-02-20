package com.pgt.bike.lock.lib.up;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import com.pgt.bike.lock.lib.utils.CRCUtil;

public class UpFileUtil {
	
	private static byte[] allA1Data;
	private static int totalA1Pack=0;
	
	private static byte[] allA2Data;
	private static int totalA2Pack=0;
	
	private static byte[] allA0Data;
	private static int totalA0Pack=0;
	
	private static byte[] allC0Data;
	private static int totalC0Pack=0;
	private static int size = 128;
	
	/** 三合一(GPRS+GPS+BLE) 版本  升级文件地址*/
	private static String newGprsBlePath ;
	/** 二合一(GPRS+GPS) 版本  升级文件地址*/
	private static String gprsPath;
	/** 骑车锁  升级文件地址  */
	private static String carPath;
	/** 新模三合一  升级文件地址*/
	private static String modelGprsBlePath ;
 
	public static void init(int deviceType,String  path) throws IOException{
		if(DeviceType.GPRS_GPS_BLE==deviceType){
			newGprsBlePath = path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalA1Pack = len/size+(len%size ==0?0:1);
			allA1Data=new byte[totalA1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA1Data, 0, buff.length);
		}else if(DeviceType.GPRS_GPS==deviceType){
			gprsPath =path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalA0Pack = len/size+(len%size ==0?0:1);
			allA0Data=new byte[totalA0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA0Data, 0, buff.length);
		}else if(DeviceType.CAR==deviceType){
			carPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalC0Pack = len/size+(len%size ==0?0:1);
			allC0Data=new byte[totalC0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allC0Data, 0, buff.length);
		}
	}
	/**
	 * 
	 * @param gprsBlePath  三合一升级文件地址
	 * @param gprsPath     二合一升级文件地址
	 * @throws IOException
	 */
	public static void init(String gprsBlePath,String newGprsPath) throws IOException{
		newGprsBlePath = gprsBlePath;
		InputStream in = new FileInputStream(gprsBlePath);
		int len = in.available();
		totalA1Pack = len/size+(len%size ==0?0:1);
		allA1Data=new byte[totalA1Pack * size];
		byte[] buff =new byte[len];
		while((in.read(buff))!=-1){
		}
		System.arraycopy(buff, 0, allA1Data, 0, buff.length);
		gprsPath =newGprsPath;
		in = new FileInputStream(gprsPath);
		len = in.available();
		totalA0Pack = len/size+(len%size ==0?0:1);
		allA0Data=new byte[totalA0Pack * size];
		buff =new byte[len];
		while((in.read(buff))!=-1){
		}
		System.arraycopy(buff, 0, allA0Data, 0, buff.length);
	}
	
	 
	/**
	 * 设置三合一升级文件的地址,设置地址后需调用{@link UpFileUtil#reLoad()}方法,重新载入升级文件
	 * @param gprsBlePath  三合一升级文件的地址
	 */
	public static void setGprsBlePath(String gprsBlePath){
		newGprsBlePath =gprsBlePath;
	}
	/**
	 * 设置二合一升级文件的地址，设置地址后需调用{@link UpFileUtil#reLoad()}方法,重新载入升级文件
	 * @param gprsPath 二合一升级文件的地址
	 */
	public static void setGprsPath(String newGprsPath){
		gprsPath =newGprsPath;
	}
	/**
	 * 设置汽车锁升级文件的地址，设置地址后需调用{@link UpFileUtil#reLoad()}方法,重新载入升级文件
	 * @param gprsPath 二合一升级文件的地址
	 */
	public static void setCarPath(String newCarPath){
		carPath =newCarPath;
	}
	
	public static void setNewPath(String newModelGprsBlePath){
		modelGprsBlePath =newModelGprsBlePath;
	}
	public static void reLoad() throws IOException{
		if(newGprsBlePath!=null){
			InputStream in = new FileInputStream(newGprsBlePath);
			int len = in.available();
			totalA1Pack = len/size+(len%size ==0?0:1);
			allA1Data=new byte[totalA1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA1Data, 0, buff.length);
		}
		
		if(gprsPath!=null){
			InputStream in = new FileInputStream(gprsPath);
			int len = in.available();
			totalA0Pack = len/size+(len%size ==0?0:1);
			allA0Data=new byte[totalA0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA0Data, 0, buff.length);
		}
		
		if(carPath!=null){
			InputStream in = new FileInputStream(carPath);
			int len = in.available();
			totalC0Pack = len/size+(len%size ==0?0:1);
			allC0Data=new byte[totalC0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allC0Data, 0, buff.length);
		}
		
		if(modelGprsBlePath!=null){
			InputStream in = new FileInputStream(modelGprsBlePath);
			int len = in.available();
			totalA2Pack = len/size+(len%size ==0?0:1);
			allA2Data=new byte[totalA2Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA2Data, 0, buff.length);
		}
		
	}
	public static void setItemSize(int itemSize){
		size=itemSize;
		 
	}
	 
	public static int getAllCRC(int deviceType){
		if(deviceType==DeviceType.GPRS_GPS_BLE){
			return CRCUtil.calcCRC(allA1Data);
		}else if(deviceType==DeviceType.GPRS_GPS){
			return CRCUtil.calcCRC(allA0Data);
		}else if(deviceType==DeviceType.CAR){
			return CRCUtil.calcCRC(allC0Data);
		}else if(deviceType==DeviceType.MODEL_GPRS_GPS_BLE){
			return CRCUtil.calcCRC(allA2Data);
		}
		return 0;
	}
	
	public static int getTotalPack(int deviceType){
		if(deviceType==DeviceType.GPRS_GPS_BLE){
			return totalA1Pack;
		}else if(deviceType==DeviceType.GPRS_GPS){
			return totalA0Pack;
		}else if(deviceType==DeviceType.CAR){
			return totalC0Pack;
		}else if(deviceType==DeviceType.MODEL_GPRS_GPS_BLE){
			return totalA2Pack;
		}
		return 0;
	}
	public static int getSize(){
		return size;
	}
	/**
	 * 获取升级文件详情，
	 * @param deviceType  锁类型({@link  DeviceType#GPRS_GPS_BLE},{@link  DeviceType#GPRS_GPS})
	 * @param pack 第几包数据
	 * @return
	 */
	public static byte[] getUpdateBytes(int deviceType,int pack){
		byte[] upDetail = new byte[size] ;
		if(deviceType==DeviceType.GPRS_GPS_BLE){
			System.arraycopy(allA1Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.GPRS_GPS){
			System.arraycopy(allA0Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.CAR){
			System.arraycopy(allC0Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.MODEL_GPRS_GPS_BLE){
			System.arraycopy(allA2Data, pack*size, upDetail,0, size);
		}
		return upDetail;
	}
	/**
	 * 获取某段更新文件的CRC校验值
	 * @param deviceType 锁类型({@link  DeviceType#GPRS_GPS_BLE},{@link  DeviceType#GPRS_GPS})
	 * @param pack  第几包数据
	 * @return
	 */
	public static int getUpdateCRC(int deviceType,int pack){
		return CRCUtil.calcCRC(getUpdateBytes(deviceType,pack));
	}
	
}
