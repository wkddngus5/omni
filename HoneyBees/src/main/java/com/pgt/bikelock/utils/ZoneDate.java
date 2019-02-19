/**
 * FileName:     ZoneDate.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年10月11日 下午1:40:46
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年10月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.pgt.bikelock.resource.OthersSource;

 /**
 * @ClassName:     ZoneDate
 * @Description:地区时间对象/Zone Date
 * @author:    Albert
 * @date:        2017年10月11日 下午1:40:46
 *
 */
public class ZoneDate extends Date{

	public ZoneDate(){
		//时区设置
		if(OthersSource.getSourceString("time_zone") != null){
			TimeZone.setDefault(TimeZone.getTimeZone(OthersSource.getSourceString("time_zone")));
		}
	}
	
	public static void main(String[] args) {
//		System.out.println(new ZoneDate().toString());
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date time = new Date((1520354044*1000)+"");
//		System.out.println(sf.format(time));
//		System.out.println(new ZoneDate().getTime()/1000);
	}
}
