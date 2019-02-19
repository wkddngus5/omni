/**
 * FileName:     TimeUtil.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年3月29日 下午5:00:36/5:00:36 pm, March 29, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年3月29日/March 29, 2017       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.joda.time.chrono.ZonedChronology;

import com.alipay.api.internal.util.StringUtils;

 /**
 * @ClassName:     TimeUtil
 * @Description:时间工具类/time tool
 * @author:    Albert
 * @date:        2017年3月29日 下午5:00:36/5:00:36 pm, March 29,2017
 *
 */
public class TimeUtil {
	
	public static String Formate_HH_mm = "HH:mm";
	public static String Formate_YYYY_MM_dd = "yyyy-MM-dd";
	public static String Formate_YYYY_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public static String Formate_YYYY_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 
	 * @Title:        formaStrDate 
	 * @Description:  格式化时间戳/format time stamps
	 * @param:        @param longTime
	 * @param:        @param formateStr
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年3月29日 下午5:15:02/5:15:02 pm, March 29, 2017
	 */
	public static String formaStrDate(String longTime,String formateStr){
		if(StringUtils.isEmpty(longTime)){
			return "";
		}
		new ZoneDate();
		SimpleDateFormat sf = new SimpleDateFormat(formateStr);
		Date date = new Date(ValueUtil.getLong(longTime)*1000);
		return sf.format(date);
	}
	
	/**
	 * 
	 * @Title:        formaStrDate 
	 * @Description:  格式化时间戳(默认格式)/format time stamps(default format)
	 * @param:        @param longTime
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年9月2日 下午6:38:02/6:38:02 pm, September 2, 2017
	 */
	public static String formaStrDate(String longTime){
		new ZoneDate();
		SimpleDateFormat sf = new SimpleDateFormat(Formate_YYYY_MM_dd_HH_mm);
		Date date = new Date(ValueUtil.getLong(longTime)*1000);
		return sf.format(date);
	}
	
	public static String formaStrDate(Date date){
		new ZoneDate();
		SimpleDateFormat sf = new SimpleDateFormat(Formate_YYYY_MM_dd_HH_mm);
		return sf.format(date);
	}
	
	public static String formateStrDateToLongStr(String dataStr,String formateStr){
		new ZoneDate();
		String longTime = null;
		SimpleDateFormat sf = new SimpleDateFormat(formateStr);
		try {
			Date date = sf.parse(dataStr);
			longTime = date.getTime()/1000 + "";
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		
		return longTime;
	}
	
	/**
	 * 
	 * @Title:        getMinutes 
	 * @Description:  计算分钟数/calculating minutes
	 * @param:        @param start
	 * @param:        @param end
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年3月29日 下午5:09:41/5:09:41 pm, March 29,2017
	 */
	public static int getMinutes(long start,long end){
		if(end == 0){
			end = TimeUtil.getCurrentLongTime();
		}
		long rideTime = end - start; // 秒/seconds
		int minute =  (int) (rideTime/60);  // 计算出分钟/calculating minute
		// TODO  <60 返回1/return 1
		if(minute == 0){
			return 1;
		}
		// TODO <120 也返回1/return 1
		return minute;
	}
	
	/**
	 * 
	 * @Title:        getMinutes 
	 * @Description:  根据开始结束时间结算分钟数/calculating minutes according to the start and end time
	 * @param:        @param start
	 * @param:        @param end
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月6日 下午1:13:24/1:13:24 pm, April 6, 2017
	 */
	public static int getMinutes(String start,String end){
		return getMinutes(ValueUtil.getLong(start),ValueUtil.getLong(end));
	}
	
	/**
	 * 
	 * @Title:        getCurrentLongTimeStr 
	 * @Description:  当前时间戳字符串获取/get the current timestamp string
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月6日 下午1:13:42/1:13:42 pm, April 6, 2017
	 */
	public static String getCurrentLongTimeStr(){
		return getCurrentLongTime()+"";
	}
	
	/**
	 * 
	 * @Title:        getCurrentLongTime 
	 * @Description:  当前时间戳获取/get the current timestamp 
	 * @param:        @return    
	 * @return:       long    
	 * @author        Albert
	 * @Date          2017年4月6日 下午1:14:29/1:14:29 pm, April 6, 2017
	 */
	public static long getCurrentLongTime(){
		return new ZoneDate().getTime()/1000;
		
	}
	
	/**
	 * 
	 * @Title:        getCurrentTime 
	 * @Description:  当前时间/current time
	 * @param:        @param formateStr
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月8日 下午4:47:10/4:47:10 pm, April 8, 2017
	 */
	public static String getCurrentTime(String formateStr){
		new ZoneDate();
		SimpleDateFormat sf = new SimpleDateFormat(formateStr);
		Date time = new ZoneDate();
		return sf.format(time);
	}
	
	public static String getCurrentTime(String formateStr,String timeZone){
		new ZoneDate();
		SimpleDateFormat sf = new SimpleDateFormat(formateStr);
		Date time = new ZoneDate();
		time = changeTimeZone(time, TimeZone.getTimeZone("UTC"), TimeZone.getTimeZone("GMT"));  
		return sf.format(time);
	}
	public static Date changeTimeZone(Date date, TimeZone oldZone, TimeZone newZone) {  
	    Date dateTmp = null;  
	    if (date != null) {  
	        int timeOffset = oldZone.getRawOffset() - newZone.getRawOffset();  
	        dateTmp = new Date(date.getTime() - timeOffset);  
	    }  
	    return dateTmp;  
	}  
	public static void main(String[] args) {
		Date dtNow = new ZoneDate();
		System.out.println(formaStrDate((1520394286)+""));
		System.out.println(formaStrDate(getCurrentLongTimeStr()));
	}
}
