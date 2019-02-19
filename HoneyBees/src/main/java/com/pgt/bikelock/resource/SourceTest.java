/**
 * FileName:     SourceTest.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月7日 上午11:06:16
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月7日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.servlet.admin.test;

 /**
 * @ClassName:     SourceTest
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月7日 上午11:06:16
 *
 */
public class SourceTest {

	/** 
	 * @Title:        main 
	 * @Description:  TODO
	 * @param:        @param args    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 上午11:06:16 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		  //设置本地区语言（默认）  
//        Locale locale = Locale.CHINESE; //Locale.getDefault();  
//        //可以使用Local的常量设置具体的语言环境  
////        Locale locale = new Locale("en","us"); 
//        //根据地区不同加载不同的资源文件  
//        ResourceBundle rb = ResourceBundle.getBundle("com.pgt.bikelock.resource.resource", locale);  
//        //根据key获得value值  
//        String title = rb.getString("common_display_page_size_title");  
//        System.out.println(title);
//        
//        MessageFormat mf=new MessageFormat(rb.getString("common_display_page_size_title"));
//        System.out.println("size="+mf.format(new Object[]{"10"}));
//        
//        System.out.println(rb.getString("bike_type_unit_value"));
//		int value = 100;
//		System.out.println(new Random().nextInt(value*100)/100.00);
//		System.out.println(new ZoneDate().toLocaleString());
//		
//		BigDecimal a= new BigDecimal(0.031);
//		BigDecimal b= new BigDecimal(0.02);
//		System.out.println(a.compareTo(b));
//		System.out.println(a.multiply(new BigDecimal(100)).setScale(0,RoundingMode.HALF_UP));
//		String str = "";
//		testV(str);
//		System.out.println("str:"+str);\
		String customerProfileId = null;
		System.out.println(StringUtils.isEmpty(customerProfileId));
	}

	public static void testV(String a){
		a = "a";
	}
}
