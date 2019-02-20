package com.omni.lock.upfile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UpFileUtil {
	private static byte[] allA1Data;
	private static int totalA1Pack=0;
	
	private static byte[] allA2Data;
	private static int totalA2Pack=0;
	
	private static byte[] allA0Data;
	private static int totalA0Pack=0;
	
	private static byte[] allC0Data;
	private static int totalC0Pack=0;
	
	private static byte[] allC1Data;
	private static int totalC1Pack=0;
	
	// 新模2in1 升级文件数据
	private static byte[] allA3Data;
	private static int totalA3Pack=0;
	
	// 旧模 围栏锁
	private static byte[] allF0Data;
	private static int totalF0Pack=0;
	
	// 旧模 围栏锁
	private static byte[] allF1Data;
	private static int totalF1Pack=0;
	
	// 新模 3G
	private static byte[] allB1Data;
	private static int totalB1Pack=0;
	
	//  3G - 电动车
	private static byte[] allB3Data;
	private static int totalB3Pack=0;
	
	
	// 电力车锁
	private static byte[] allE0Data;
	private static int totalE0Pack=0;
	
	//车位锁
	private static byte[] all80Data;
	private static int total80Pack=0;
	
	//滑板车 IOT omni
	private static byte[] all8aData;// 加0后的 数组值。
	private static int total8aPack=0;
	private static int scooterRealLen = 0; // 不加0 的真实长度
	
	//滑板车 IOT sn
	private static byte[] allIotSnData;// 加0后的 数组值。
	private static int totalIotSnPack=0;
	private static int iotSnRealLen = 0; // 不加0 的真实长度
	
	//滑板车 内置电池
	private static byte[] all22Data;// 加0后的 数组值。
	private static int total22Pack=0;
	private static int battery22RealLen = 0; // 不加0 的真实长度
	
	//滑板车 外置电池
	private static byte[] all23Data;// 加0后的 数组值。
	private static int total23Pack=0;
	private static int battery23RealLen = 0; // 不加0 的真实长度
	
	
	//滑板车 控制器
	private static byte[] allctData;// 加0后的 数组值。
	private static int totalctPack=0;
	private static int ctRealLen = 0; // 不加0 的真实长度
	
	//滑板车 控制器
	private static byte[] allctXMData;// 加0后的 数组值。
	private static int totalctXMPack=0;
	private static int ctXMRealLen = 0; // 不加0 的真实长度
	
	//滑板车 仪表
	private static byte[] allDispalyData;// 加0后的 数组值。
	private static int totalDispalyPack=0;
	private static int dispalyRealLen = 0; // 不加0 的真实长度
	
	private static int size = 128;
	
	/** 三合一(GPRS+GPS+BLE) 版本  升级文件地址*/
	private static String newGprsBlePath ;
	/** 二合一(GPRS+GPS) 版本  升级文件地址*/
	private static String gprsPath;
	/** 骑车锁  升级文件地址  */
	private static String carPath;
	
	private static String car3IN1Path;
	
	/** 新模三合一  升级文件地址*/
	private static String modelGprsBlePath ;
	/** 新模2in1  升级文件地址*/
	private static String modelGprsPath ;
	
	/** 旧模 围栏锁地址*/
	private static String aFenceLockPath ;
	/** 新模  围栏锁地址*/
	private static String bFenceLockPath ;
	/** 新模 3G  升级文件地址*/
	private static String model3GPath ;
	
	/** 电力车锁  升级文件地址*/
	private static String electricPath ;
	
	/** 车位锁  升级文件地址*/
	private static String carportPath ;
	
	/** 滑板车iot omni  升级文件地址*/
	public static String scooterPath ;
	
	/** 滑板车 iot sn 升级文件地址*/
	public static String scooterIotSnPath ;
	
	/** 内置电池  升级文件地址*/
	public static String battery22Path ;
	/** 外置电池  升级文件地址*/
	public static String battery23Path ;
	
	/** 控制器-sn滑板车  升级文件地址*/
	public static String ctPath ;
	/** 控制器-xm滑板车  升级文件地址*/
	public static String ctXMPath ;
	/** 滑板车 仪表  升级文件地址*/
	public static String displayPath ;
	
	/** 3G 电动车 升级文件地址 */
	public static String electric3gPath ;
 
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
			in.close();
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
			in.close();
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
			in.close();
		}else if(DeviceType.MODEL_GPRS_GPS_BLE==deviceType){
			modelGprsBlePath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalA2Pack = len/size+(len%size ==0?0:1);
			allA2Data=new byte[totalA2Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA2Data, 0, buff.length);
			in.close();
		}else if(DeviceType.MODEL_GPRS_GPS==deviceType){
			modelGprsPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalA3Pack = len/size+(len%size ==0?0:1);
			allA3Data=new byte[totalA3Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA3Data, 0, buff.length);
			in.close();
		}else if(DeviceType.A_FENCE_LOCK==deviceType){
			// 旧模围栏锁
			aFenceLockPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalF0Pack = len/size+(len%size ==0?0:1);
			allF0Data=new byte[totalF0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allF0Data, 0, buff.length);
			in.close();
		}else if(DeviceType.A_FENCE_LOCK==deviceType){
			// 新模围栏锁
			bFenceLockPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalF1Pack = len/size+(len%size ==0?0:1);
			allF1Data=new byte[totalF1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allF1Data, 0, buff.length);
			in.close();
		}else if(DeviceType.MODEL_3G==deviceType){
			// 新模围栏锁
			model3GPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalB1Pack = len/size+(len%size ==0?0:1);
			allB1Data=new byte[totalB1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allB1Data, 0, buff.length);
			in.close();
		}else if(DeviceType.ELECTRIC==deviceType){
			// 新模围栏锁
			electricPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalE0Pack = len/size+(len%size ==0?0:1);
			allE0Data=new byte[totalE0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allE0Data, 0, buff.length);
			in.close();
		}else if(DeviceType.CAR_3IN1==deviceType){
			car3IN1Path=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalC1Pack = len/size+(len%size ==0?0:1);
			allC1Data=new byte[totalC1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allC1Data, 0, buff.length);
			in.close();
		}else if(DeviceType.CARPORT==deviceType){
			carportPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			total80Pack = len/size+(len%size ==0?0:1);
			all80Data=new byte[total80Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all80Data, 0, buff.length);
			in.close();
		}else if(DeviceType.SCOOTER_IOT_OMNI==deviceType){
			scooterPath=path;
			InputStream in = new FileInputStream(path);
			scooterRealLen = in.available();
			total8aPack = scooterRealLen/size+(scooterRealLen%size ==0?0:1);
			all8aData=new byte[total80Pack * size];
			byte[] buff =new byte[scooterRealLen];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all8aData, 0, buff.length);
			in.close();
		}else if(DeviceType.SCOOTER_IOT_SN==deviceType){
			scooterIotSnPath=path;
			InputStream in = new FileInputStream(path);
			iotSnRealLen = in.available();
			
			totalIotSnPack = iotSnRealLen/size+(iotSnRealLen%size ==0?0:1);
			allIotSnData=new byte[totalIotSnPack * size];
			byte[] buff =new byte[iotSnRealLen];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allIotSnData, 0, buff.length);
			in.close();
		}
		
		else if(DeviceType.CONTROL==deviceType){
			
			ctPath=path;
			InputStream in = new FileInputStream(path);
			ctRealLen = in.available();
			totalctPack = ctRealLen/size+(ctRealLen%size ==0?0:1);
			allctData=new byte[totalctPack * size];
			byte[] buff =new byte[ctRealLen];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allctData, 0, buff.length);
			in.close();
		}
		else if(DeviceType.CONTROL_XM==deviceType){
			
			ctXMPath=path;
			InputStream in = new FileInputStream(path);
			ctXMRealLen = in.available();
			totalctXMPack = ctXMRealLen/size+(ctXMRealLen%size ==0?0:1);
			allctXMData=new byte[totalctXMPack * size];
			byte[] buff =new byte[ctXMRealLen];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allctXMData, 0, buff.length);
			in.close();
		}
		else if(DeviceType.BATTERY_22==deviceType){
			
			battery22Path=path;
			InputStream in = new FileInputStream(path);
			battery22RealLen = in.available();
			total22Pack = battery22RealLen/size+(battery22RealLen%size ==0?0:1);
			
			all22Data=new byte[total22Pack * size];
			byte[] buff =new byte[battery22RealLen];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all22Data, 0, buff.length);
			in.close();
		}else if(DeviceType.BATTERY_23==deviceType){
			
			battery23Path=path;
			InputStream in = new FileInputStream(path);
			battery23RealLen = in.available();
			total23Pack = battery23RealLen/size+(battery23RealLen%size ==0?0:1);
			
			all23Data=new byte[total23Pack * size];
			byte[] buff =new byte[battery23RealLen];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all23Data, 0, buff.length);
			in.close();
		}else if(DeviceType.SCOOTER_DISPLAY==deviceType){
			displayPath=path;
			InputStream in = new FileInputStream(path);
			dispalyRealLen = in.available();
			
			totalDispalyPack = dispalyRealLen/size+(dispalyRealLen%size ==0?0:1);
			
			allDispalyData=new byte[totalDispalyPack * size];
			byte[] buff =new byte[dispalyRealLen];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allDispalyData, 0, buff.length);
			in.close();
		}else if(DeviceType.ELECTRIC_3G==deviceType){
			electric3gPath=path;
			InputStream in = new FileInputStream(path);
			int len = in.available();
			totalB3Pack = len/size+(len%size ==0?0:1);
			allB3Data=new byte[totalB3Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allB3Data, 0, buff.length);
			in.close();
		}
	}
	/**
	 * 
	 * @param gprsBlePath  三合一升级文件地址
	 * @param gprsPath     二合一升级文件地址
	 * @throws IOException
	 */
	@Deprecated
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
	/**
	 * 设置新模三合一升级文件的地址，设置地址后需调用{@link UpFileUtil#reLoad()}方法,重新载入升级文件
	 * @param newGprsGpsBlePath 三合一升级文件的地址
	 */
	public static void setNGGBPath(String newGprsGpsBlePath){
		modelGprsBlePath =newGprsGpsBlePath;
	}
	
	/**
	 * 设置新模二合一升级文件的地址，设置地址后需调用{@link UpFileUtil#reLoad()}方法,重新载入升级文件
	 * @param newGprsGpsPath 二合一升级文件的地址
	 */
	public static void setNGGPath(String newGprsGpsPath){
		modelGprsPath =newGprsGpsPath;
	}
	/**
	 * 设置新模围栏锁升级文件的地址，设置地址后需调用{@link UpFileUtil#reLoad()}方法,重新载入升级文件
	 * @param newBGFPath 新模围栏锁升级文件的地址
	 */
	public static void setNBGFPath(String newBGFPath){
		bFenceLockPath =newBGFPath;
	}
	/**
	 * 设置旧模围栏锁升级文件的地址，设置地址后需调用{@link UpFileUtil#reLoad()}方法,重新载入升级文件
	 * @param newBGFPath 新模围栏锁升级文件的地址
	 */
	public static void setBGFPath(String newBGFPath){
		aFenceLockPath =newBGFPath;
	}
	public static void set3GPath(String new3GPath){
		model3GPath =new3GPath;
	}
	
	public static void setElectricPath(String newElectricPath){
		electricPath =newElectricPath;
	}
	public static void setCarportPath(String newCarportPath){
		carportPath =newCarportPath;
	}
	
	/**
	 * 滑板车 IOT OMNI 升级文件地址
	 * @param newScooterPath
	 */
	public static void setScooterPath(String newScooterPath){
		scooterPath =newScooterPath;
	}
	/**
	 * 滑板车 IOT segway nineboot 升级文件地址
	 * @param newScooterPath
	 */
	public static void setScooterIotSnPath(String newScooterIotSnPath){
		scooterIotSnPath =newScooterIotSnPath;
	}
	/**
	 * 设置控制升级文件的地址。
	 * @param newControlPath 升级文件地址
	 */
	public static void setControlPath(String newControlPath){
		ctPath =newControlPath;
	}
	/**
	 * 设置控制升级文件的地址。
	 * @param newControlPath 升级文件地址
	 */
	public static void setControlXMPath(String newControlXMPath){
		ctXMPath =newControlXMPath;
	}
	
	/**
	 * 设置 内置电池 升级文件的地址。
	 * @param newBattery22Path 内置电池升级文件地址
	 */
	public static void setBattery22Path(String newBattery22Path){
		battery22Path =newBattery22Path;
	}
	/**
	 * 设置 外置电池 升级文件的地址。
	 * @param newBattery22Path 内置电池升级文件地址
	 */
	public static void setBattery23Path(String newBattery23Path){
		battery23Path =newBattery23Path;
	}
	
	/**
	 * 设置 滑板车 仪表程序 升级文件的地址。
	 * @param newDisplayPath 内置电池升级文件地址
	 */
	public static void setDisplayPath(String newDisplayPath){
		displayPath =newDisplayPath;
	}
	
	/**
	 * 设置 3g 电动车升级。
	 * @param newElectric3gPath 3g电动车升级文件地址
	 */
	public static void setElectric3gPath(String newElectric3gPath){
		electric3gPath =newElectric3gPath;
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
			in.close();
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
			in.close();
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
			in.close();
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
			in.close();
		}
		
		if(modelGprsPath!=null){
			InputStream in = new FileInputStream(modelGprsBlePath);
			int len = in.available();
			totalA3Pack = len/size+(len%size ==0?0:1);
			allA3Data=new byte[totalA3Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allA3Data, 0, buff.length);
			in.close();
		}
		
		if(aFenceLockPath!=null){
			InputStream in = new FileInputStream(aFenceLockPath);
			int len = in.available();
			totalF0Pack = len/size+(len%size ==0?0:1);
			allF0Data=new byte[totalF0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allF0Data, 0, buff.length);
			in.close();
		}
		
		if(bFenceLockPath!=null){
			InputStream in = new FileInputStream(aFenceLockPath);
			int len = in.available();
			totalF1Pack = len/size+(len%size ==0?0:1);
			allF1Data=new byte[totalF1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allF1Data, 0, buff.length);
			in.close();
		}
		
		if(model3GPath!=null){
			InputStream in = new FileInputStream(model3GPath);
			int len = in.available();
			totalB1Pack = len/size+(len%size ==0?0:1);
			allB1Data=new byte[totalB1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allB1Data, 0, buff.length);
			in.close();
		}
		
		if(electricPath!=null){
			InputStream in = new FileInputStream(electricPath);
			int len = in.available();
			totalE0Pack = len/size+(len%size ==0?0:1);
			allE0Data=new byte[totalE0Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allE0Data, 0, buff.length);
			in.close();
		}
		
		if(car3IN1Path!=null){
			InputStream in = new FileInputStream(electricPath);
			int len = in.available();
			totalC1Pack = len/size+(len%size ==0?0:1);
			allC1Data=new byte[totalC1Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allC1Data, 0, buff.length);
			in.close();
		}
		if(carportPath!=null){
			InputStream in = new FileInputStream(carportPath);
			int len = in.available();
			total80Pack = len/size+(len%size ==0?0:1);
			all80Data=new byte[total80Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all80Data, 0, buff.length);
			in.close();
		}
		if(scooterPath!=null){
			InputStream in = new FileInputStream(scooterPath);
			int len = in.available();
			scooterRealLen = len;
			total8aPack = len/size+(len%size ==0?0:1);
			all8aData=new byte[total8aPack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all8aData, 0, buff.length);
			in.close();
		}
		if(scooterIotSnPath!=null){
			InputStream in = new FileInputStream(scooterIotSnPath);
			int len = in.available();
			iotSnRealLen   = len;
			totalIotSnPack = len/size+(len%size ==0?0:1);
			allIotSnData =new byte[totalIotSnPack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allIotSnData, 0, buff.length);
			in.close();
		}
		if(ctPath!=null){
			InputStream in = new FileInputStream(ctPath);
			int len = in.available();
			ctRealLen = len;
			totalctPack = len/size+(len%size ==0?0:1);
			allctData=new byte[totalctPack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allctData, 0, buff.length);
			in.close();
		}
		if(ctXMPath!=null){
			InputStream in = new FileInputStream(ctXMPath);
			int len = in.available();
			ctXMRealLen = len;
			totalctXMPack = len/size+(len%size ==0?0:1);
			allctXMData=new byte[totalctXMPack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allctXMData, 0, buff.length);
			in.close();
		}
		if(battery22Path!=null){
			InputStream in = new FileInputStream(battery22Path);
			int len = in.available();
			battery22RealLen = len;
			total22Pack  = len/size+(len%size ==0?0:1);
			all22Data=new byte[total22Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all22Data, 0, buff.length);
			in.close();
		}
		
		if(battery23Path!=null){
			InputStream in = new FileInputStream(battery23Path);
			int len = in.available();
			battery23RealLen = len;
			total23Pack  = len/size+(len%size ==0?0:1);
			all23Data=new byte[total23Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, all23Data, 0, buff.length);
			in.close();
		}
		if(displayPath!=null){
			InputStream in = new FileInputStream(displayPath);
			int len = in.available();
			dispalyRealLen = len;
			totalDispalyPack  = len/size+(len%size ==0?0:1);
			allDispalyData =new byte[totalDispalyPack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allDispalyData, 0, buff.length);
			in.close();
		}
		if(electric3gPath!=null){
			InputStream in = new FileInputStream(electric3gPath);
			int len = in.available();
			totalB3Pack  = len/size+(len%size ==0?0:1);
			allB3Data =new byte[totalB3Pack * size];
			byte[] buff =new byte[len];
			while((in.read(buff))!=-1){
			}
			System.arraycopy(buff, 0, allB3Data, 0, buff.length);
			in.close();
		}
	}
	public static void setItemSize(int itemSize){
		size=itemSize;
	}
	
	/**
	 * 获取升级数据包的真实大小，没有补充0的大小.
	 * 目前只有 scooter 和control的文件有效
	 * @param deviceType
	 * @return
	 */
	public static int getRealLen(int deviceType){
		if(deviceType==DeviceType.SCOOTER_IOT_OMNI){
			return scooterRealLen;
		}else if(deviceType==DeviceType.CONTROL){
			return ctRealLen;
		}
		else if(deviceType==DeviceType.CONTROL_XM){
			return ctXMRealLen;
		}
		else if(deviceType==DeviceType.BATTERY_22){
			return battery22RealLen;
		}else if(deviceType==DeviceType.BATTERY_23){
			return battery23RealLen;
		}else if(deviceType==DeviceType.SCOOTER_DISPLAY){
			return dispalyRealLen;
		}else if(deviceType==DeviceType.SCOOTER_IOT_SN){
			return iotSnRealLen;
		}
		return 0;
	}
	
	/**
	 * 获取升级文件所有升级数据的CRC值，不填充0的升级数据
	 * @param deviceType
	 * @return
	 */
	public static int getRealAllCRC(int deviceType){
		if(deviceType==DeviceType.SCOOTER_IOT_OMNI){
			return CRCUtil.calcCRC(all8aData,0,getRealLen(deviceType));
		}else if(deviceType==DeviceType.CONTROL){
			return CRCUtil.calcCRC(allctData,0,getRealLen(deviceType));
		}else if(deviceType==DeviceType.BATTERY_22){
			return CRCUtil.calcCRC(all22Data,0,getRealLen(deviceType));
		}else if(deviceType==DeviceType.BATTERY_23){
			return CRCUtil.calcCRC(all23Data,0,getRealLen(deviceType));
		}else if(deviceType==DeviceType.SCOOTER_DISPLAY){
			return CRCUtil.calcCRC(allDispalyData,0,getRealLen(deviceType));
		}else if(deviceType==DeviceType.SCOOTER_IOT_SN){
			return CRCUtil.calcCRC(allIotSnData,0,getRealLen(deviceType));
		}else if(deviceType==DeviceType.CONTROL_XM){
			return CRCUtil.calcCRC(allctXMData,0,getRealLen(deviceType));
		}
		//dd
		return 0;
	}
	
	/**
	 * 这是加0后的 所有升级文件的CRC值
	 * @param deviceType
	 * @return
	 */
	public static int getAllCRC(int deviceType){
		if(deviceType==DeviceType.GPRS_GPS_BLE){
			return CRCUtil.calcCRC(allA1Data);
		}else if(deviceType==DeviceType.GPRS_GPS){
			return CRCUtil.calcCRC(allA0Data);
		}else if(deviceType==DeviceType.CAR){
			return CRCUtil.calcCRC(allC0Data);
		}else if(deviceType==DeviceType.MODEL_GPRS_GPS_BLE){
			return CRCUtil.calcCRC(allA2Data);
		}else if(deviceType==DeviceType.MODEL_GPRS_GPS){
			return CRCUtil.calcCRC(allA3Data);
		}else if(deviceType==DeviceType.A_FENCE_LOCK){
			return CRCUtil.calcCRC(allF0Data);
		}else if(deviceType==DeviceType.B_FENCE_LOCK){
			return CRCUtil.calcCRC(allF1Data);
		}else if(deviceType==DeviceType.MODEL_3G){
			return CRCUtil.calcCRC(allB1Data);
		}else if(deviceType==DeviceType.ELECTRIC){
			return CRCUtil.calcCRC(allE0Data);
		}else if(deviceType==DeviceType.CAR_3IN1){
			return CRCUtil.calcCRC(allC1Data);
		}else if(deviceType==DeviceType.CARPORT){
			return CRCUtil.calcCRC(all80Data);
		}else if(deviceType==DeviceType.SCOOTER_IOT_OMNI){
			return CRCUtil.calcCRC(all8aData);
		}else if(deviceType==DeviceType.SCOOTER_IOT_SN){
			return CRCUtil.calcCRC(allIotSnData);
		}
		else if(deviceType==DeviceType.CONTROL){
			return CRCUtil.calcCRC(allctData);
		}else if(deviceType==DeviceType.BATTERY_22){
			return CRCUtil.calcCRC(all22Data);
		}else if(deviceType==DeviceType.BATTERY_23){
			return CRCUtil.calcCRC(all23Data);
		}else if(deviceType==DeviceType.SCOOTER_DISPLAY){
			return CRCUtil.calcCRC(allDispalyData);
		}else if(deviceType==DeviceType.ELECTRIC_3G){
			return CRCUtil.calcCRC(allB3Data);
		}else if(deviceType==DeviceType.CONTROL_XM){
			return CRCUtil.calcCRC(allctXMData);
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
		}else if(deviceType==DeviceType.MODEL_GPRS_GPS){
			return totalA3Pack;
		}else if(deviceType==DeviceType.A_FENCE_LOCK){
			return totalF0Pack;
		}else if(deviceType==DeviceType.B_FENCE_LOCK){
			return totalF1Pack;
		}else if(deviceType==DeviceType.MODEL_3G){
			return totalB1Pack;
		}else if(deviceType==DeviceType.ELECTRIC){
			return totalE0Pack;
		}else if(deviceType==DeviceType.CAR_3IN1){
			return totalC1Pack;
		}else if(deviceType==DeviceType.CARPORT){
			return total80Pack;
		}else if(deviceType==DeviceType.SCOOTER_IOT_OMNI){
			return total8aPack;
		}else if(deviceType==DeviceType.SCOOTER_IOT_SN){
			return totalIotSnPack;
		}else if(deviceType==DeviceType.CONTROL){
			return totalctPack;
		}else if(deviceType==DeviceType.BATTERY_22){
			return total22Pack;
		}else if(deviceType==DeviceType.BATTERY_23){
			return total23Pack;
		}else if(deviceType==DeviceType.SCOOTER_DISPLAY){
			return totalDispalyPack;
		}else if(deviceType==DeviceType.ELECTRIC_3G){
			return totalB3Pack;
		}else if(deviceType==DeviceType.CONTROL_XM){
			return totalctXMPack;
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
		}else if(deviceType==DeviceType.MODEL_GPRS_GPS){
			System.arraycopy(allA3Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.A_FENCE_LOCK){
			System.arraycopy(allF0Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.B_FENCE_LOCK){
			System.arraycopy(allF1Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.MODEL_3G){
			System.arraycopy(allB1Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.ELECTRIC){
			System.arraycopy(allE0Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.CAR_3IN1){
			System.arraycopy(allC1Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.CARPORT){
			System.arraycopy(all80Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.SCOOTER_IOT_OMNI){
			System.arraycopy(all8aData, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.SCOOTER_IOT_SN){
			System.arraycopy(allIotSnData, pack*size, upDetail,0, size);
		}
		else if(deviceType==DeviceType.CONTROL){
			System.arraycopy(allctData, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.BATTERY_22){
			System.arraycopy(all22Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.BATTERY_23){
			System.arraycopy(all23Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.SCOOTER_DISPLAY){
			System.arraycopy(allDispalyData, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.ELECTRIC_3G){
			System.arraycopy(allB3Data, pack*size, upDetail,0, size);
		}else if(deviceType==DeviceType.CONTROL_XM){
			System.arraycopy(allctXMData, pack*size, upDetail,0, size);
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
