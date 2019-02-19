/**
 * FileName:     test.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月19日 上午9:34:22
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月19日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;

import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     test
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月19日 上午9:34:22
 *
 */
public class test {

	/** 
	 * @Title:        main 
	 * @Description:  TODO
	 * @param:        @param args    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月19日 上午9:34:22 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double a = 0.01;
		BigDecimal total_amount = new BigDecimal(0.00);
		BigDecimal total_amount2 = new BigDecimal(0);
		System.out.println(total_amount.compareTo(total_amount2));

//		System.out.println(new ZoneDate().getTime()+";"+System.currentTimeMillis());
		
//		UUID uuid = UUID.randomUUID();
//		System.out.println(uuid.toString());
	}

}
