/**
 * FileName:     GpsTransformUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 上午10:57:03/10:57:03 am, May 9, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.LatLng;

/**
 * @ClassName:     GpsTransformUtil
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月9日 上午10:57:03/10:57:03 am, May 9, 2017
 *
 */
public class GpsTransformUtil {
	static double pi = 3.14159265358979324;

	//
	// Krasovsky 1940
	//
	// a = 6378245.0, 1/f = 298.3
	// b = a * (1 - f)
	// ee = (a^2 - b^2) / a^2;
	static double a = 6378245.0;
	static double ee = 0.00669342162296594323;
	
	//高德在线转换 Amap online conversion
	private static final String KEY="519efd057c1ce8597a03ae388c084656";
	private static final String COORDSYS_GPS="gps";
	private static final String R_STATUS="status";
	private static final String R_INFO="info";
	private static final String R_INFO_CODE="infocode";
	private static final String R_LOCATIONS="locations";
	private static String URL ="http://restapi.amap.com/v3/assistant/coordinate/convert";

	//
	// World Geodetic System ==> Mars Geodetic System
	/**
	 * WGS-84 to GCJ-02(腾讯/阿里云/高德/灵图)/(tecent/alicloud/amap/lingtu)
	 * @Title:        wgsToGcj 
	 * @Description:  TODO
	 * @param:        @param wgLat 输入/input
	 * @param:        @param wgLon 输入/input
	 * @param:        @param mgLat 输出/output
	 * @param:        @param mgLon 输出/output  
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 上午11:00:07
	 */
	public static void wgsToGcj(double wgLat, double wgLon,  double mgLat,  double mgLon)
	{
		if (outOfChina(wgLat, wgLon))
		{
			mgLat = wgLat;
			mgLon = wgLon;
			return;
		}
		double dLat = transformLat(wgLon - 105.0, wgLat - 35.0);
		double dLon = transformLon(wgLon - 105.0, wgLat - 35.0);
		double radLat = wgLat / 180.0 * pi;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
		mgLat = wgLat + dLat;
		mgLon = wgLon + dLon;
		
		System.out.println("lat:"+mgLat+";lon:"+mgLon);
	}

	static boolean outOfChina(double lat, double lon)
	{
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	static double transformLat(double x, double y)
	{
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	static double transformLon(double x, double y)
	{
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
		return ret;
	}


	/**
	 * WGS-84 to BD09 (百度)/(baidu)
	 * @Title:        wgsToBd 
	 * @Description:  TODO
	 * @param:        @param gg_lat
	 * @param:        @param gg_lon
	 * @param:        @param bd_lat
	 * @param:        @param bd_lon    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 上午11:06:21/11:06:21 am, May 9, 2017
	 */
	public static void wgsToBd(double gg_lat, double gg_lon, double bd_lat, double bd_lon)  

	{  

		double x = gg_lon, y = gg_lat;  

		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * pi);  

		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * pi);  

		bd_lon = z * Math.cos(theta) + 0.0065;  

		bd_lat = z * Math.sin(theta) + 0.006;
		
		System.out.println("lat:"+bd_lat+";lon:"+bd_lon);

	} 

	/**
	 * BD09 to WGS-84(百度)/(baidu)
	 * @Title:        bdToWgs 
	 * @Description:  TODO
	 * @param:        @param bd_lat
	 * @param:        @param bd_lon
	 * @param:        @param gg_lat
	 * @param:        @param gg_lon    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 上午11:08:22/11:08:22 am, 11:06:21 am, May 9, 2017
	 */
	public static void bdToWgs(double bd_lat, double bd_lon, double gg_lat, double gg_lon)  

	{  

		double x = bd_lon - 0.0065, y = bd_lat - 0.006;  

		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * pi);  

		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * pi);  

		gg_lon = z * Math.cos(theta);  

		gg_lat = z * Math.sin(theta);  

	}
	
	/**
	 * WGS-84 to GCJ-02(腾讯/阿里云/高德/灵图)/(tecent/alicloud/amap/lingtu)
	 * @Title:        conver2Amp 
	 * @Description:  TODO
	 * @param:        @param gps
	 * @param:        @return    
	 * @return:       LatLng    
	 * @author        Albert
	 * @Date          2017年5月9日 下午12:19:22/12:19:22 pm, May 9, 2017
	 */
	public static LatLng conver2Amp(LatLng gps){
		//非中国地区，不转换/ no conversion for non-Chinese region
		if(!"zh_cn".equals(OthersSource.getSourceString("default_language"))){
			return new LatLng(gps.latitude,gps.longitude);
		}
		String location=String.format("%s,%s",gps.longitude,gps.latitude);
		String param="key="+KEY+"&coordsys="+COORDSYS_GPS+"&locations="+location;
		String result=HttpRequest.sendGet(URL, param);
		JSONObject resJson;
		try {
			resJson = new JSONObject(result);
			if("1".equals(resJson.getString(R_STATUS))){
				String locations =resJson.getString(R_LOCATIONS);
				String[] postion=locations.split(",");
				
				double lat = Double.parseDouble(postion[1]);
				double lng = Double.parseDouble(postion[0]);
				return new LatLng(lat, lng);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
		 
		return null;
		
	}

	
	public static void main(String[] args) {
		double lat = 0,lon = 0;
		wgsToBd(22.63475, 114.12552333333333, lat, lon);		
		wgsToGcj(22.63475, 114.12552333333333, lat, lon);
		
		LatLng gps = new LatLng(22.63475, 114.125523);
		LatLng ll= conver2Amp(gps);
		System.out.println("amp ll="+ll.toString());
	}
}
