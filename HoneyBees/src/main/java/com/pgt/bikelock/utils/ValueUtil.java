/**
 * FileName:     ValueUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-25 下午6:39:43/6:39:43 pm, March 25, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-25       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.UUID;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.resource.OthersSource;


/**
 * @ClassName:     ValueUtil
 * @Description:数据值处理工具类/value processing tool
 * @author:    Albert
 * @date:        2017-3-25 下午6:39:43/6:39:43 pm, March 25, 2017
 *
 */
public class ValueUtil {
	
	public static final BigDecimal ZERO_AMOUNT = new BigDecimal(0);
	
	/**
	 * 
	 * @Title:        getString 
	 * @Description:  TODO
	 * @param:        @param str
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年12月26日 下午4:09:29
	 */
	public static String getString(Object str){
		String value = "";
		try {
			value = (String) str;
		} catch (Exception e) {
			// TODO: handle exception
		}

		return value;
	}
	
	/**
	 * 
	 * @Title:        getInt 
	 * @Description:  TODO
	 * @param:        @param str
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017-3-25 下午6:41:12/6:41:12 pm, March 25, 2017
	 */
	public static int getInt(String str){
		if(StringUtils.isEmpty(str)){
			return 0;
		}
		int value = 0;
		try {
			value = Integer.parseInt(str);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return value;
	}

	public static int getInt(Object str){
		int value = 0;
		try {
			value = Integer.parseInt(str.toString());
		} catch (Exception e) {
			// TODO: handle exception
		}

		return value;
	}

	/**
	 * 
	 * @Title:        getLong 
	 * @Description:  TODO
	 * @param:        @param str
	 * @param:        @return    
	 * @return:       long    
	 * @author        Albert
	 * @Date          2017-3-27 上午11:15:40/11:15:40 am, March 27, 2017
	 */
	public static long getLong(String str){
		long value = 0;
		try {
			value = Long.parseLong(str);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return value;
	}

	/**
	 * 
	 * @Title:        getDouble 
	 * @Description:  TODO
	 * @param:        @param str
	 * @param:        @return    
	 * @return:       double    
	 * @author        Albert
	 * @Date          2017-3-25 下午6:42:02/6:42:02 pm, March 25, 2017
	 */
	public static double getDouble(String str){
		double value = 0;
		try {
			value = Double.parseDouble(str);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return value;
	}

	public static BigDecimal getBigDecimal(String str){
		BigDecimal value = new BigDecimal(0);
		try {
			value = new BigDecimal(str);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return value;
	}


	/**
	 * 
	 * @Title:        getCalorie 
	 * @Description:  计算消耗卡里路值/calculating the calorie value
	 * @param:        @param duration 骑行时长/cycling duration
	 * @param:        @param distance 骑行距离/cycling distance
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年3月29日 下午5:26:54/5:26:54 pm, March 29, 2017
	 */
	public static String getCalorie(int duration,double distance){
		/*if(duration<0) return "0";
		double durationT = duration/60.00;//时长按小时计算/time is counted by hour
		double calorie =durationT * distance * 0.00352;
		if(calorie < 1 && duration > 0 && distance > 0){
			return "1";
		}*/
		double calorie = 0;
		distance = distance/1000;
		if(OthersSource.getSourceString("distance_show_miles") != null){
			distance = distance*0.6213712;
		}
		calorie = 56.5 * distance;
		return String.format("%.2f", calorie);
	}

	/**
	 * 
	 * @Title:        getCarbon 
	 * @Description:  计算节约碳排量/calculating the carbon emission saving
	 * @param:        @param duration 骑行时长/cycling duration
	 * @param:        @param distance 骑行距离/cycling distance
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年3月29日 下午5:27:31/5:27:31 pm, March 29, 2017
	 */
	public static String getCarbon(int duration,double distance){
		if(duration<0) return "0";
		double calorie = distance * 0.00148;
		return String.format("%.2f", calorie);
	}

	/**
	 * 
	 * @Title:        getStringRandom 
	 * @Description:  生成随机数字和字母,/generate random digits and letters
	 * @param:        @param length
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月29日 上午9:21:10/9:21:10 am, April 29, 2017
	 */
	public static String getStringRandom(int length) {  

		String val = "";  
		Random random = new Random();  

		//参数length，表示生成几位随机数/length means the quantity of random digits generated 
		for(int i = 0; i < length; i++) {  

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
			//输出字母还是数字/output letters or digits 
			if( "char".equalsIgnoreCase(charOrNum) ) {  
				//输出是大写字母还是小写字母/output capital letter or small letter  
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
				val += (char)(random.nextInt(26) + temp);  
			} else if( "num".equalsIgnoreCase(charOrNum) ) {  
				val += String.valueOf(random.nextInt(10));  
			}  
		}  
		return val;  
	} 

	/**
	 * 
	 * @Title:        getNumberRandom 
	 * @Description:  生成随机数字/generating random digits
	 * @param:        @param length
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月12日 下午12:03:50/12:03:50 pm, April 12, 2017
	 */
	public static String getNumberRandom(int length) {  

		String val = "";  
		Random random = new Random();  

		//参数length，表示生成几位随机数/length means the quantity of random digits generated 
		for(int i = 0; i < length; i++) { 
			val += String.valueOf(random.nextInt(10));  
		}  
		return val;  
	} 

	/**
	 * 
	 * @Title:        getUUIDString 
	 * @Description:  Get UUID
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年9月30日 上午11:59:55
	 */
	public static String getUUIDString(){
		return UUID.randomUUID().toString().toUpperCase();
	}

	/**
	 * 
	 * @Title:        getAmount 
	 * @Description:  TODO
	 * @param:        @param amount    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月21日 上午11:00:04/11:00:04 am, September 21, 2017
	 */
	public static void getAmount(BigDecimal amount){
		//金额比例转换，时间转金额/amount ratio conversion, time converted to amount
		if(ValueUtil.getInt(OthersSource.getSourceString("recharge_amount_percent")) != 0){
			amount = amount.divide(new BigDecimal(OthersSource.getSourceString("recharge_amount_percent")));
		}
	}

	/**
	 * 
	 * @Title:        mapToString 
	 * @Description:  add params map to string for reqeust
	 * @param:        @param paramsMap
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月23日 上午10:43:14/10:43:14 am, August 23, 2017
	 */
	public static String mapToString(Map<String, Object> paramsMap){
		String paramsStr = "";
		for (Entry<String, Object> param : paramsMap.entrySet()) {
			paramsStr += param.getKey()+"="+param.getValue()+"&";
		}
		if(paramsStr.length() > 0){
			paramsStr = paramsStr.substring(0, paramsStr.length()-1);
		}
		System.out.println(paramsStr);
		return paramsStr;
	}
	
	/**
	 * 
	 * @Title:        mapStringToObject 
	 * @Description:  TODO
	 * @param:        @param params
	 * @param:        @return    
	 * @return:       Map<String,Object>    
	 * @author        Albert
	 * @Date          2017年11月17日 下午6:20:23
	 */
	public static Map<String, Object> mapStringToObject(Map<String, String> params){
		Map<String, Object> objectMap = new  LinkedHashMap<String, Object>();
		for (Entry<String, String> param : params.entrySet()) {
			objectMap.put(param.getKey(), param.getValue());
		}
		return objectMap;
	}

	/**
	 * 
	 * @Title:        listToString 
	 * @Description:  TODO
	 * @param:        @param idsList
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年11月11日 下午5:47:01
	 */
	public static String listToString(List<String> idsList){
		String ids = null;
		for (String string : idsList) {
			if(ids == null){
				ids = string;
			}else{
				ids += ","+string;
			}
		}

		return ids;
	}


	/**
	 * 
	 * @Title:        checkItemInArr 
	 * @Description:  Check Item In Array
	 * @param:        @param arr
	 * @param:        @param item
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月28日 下午5:35:20
	 */
	public static boolean checkItemInArr(String[] arr,String item){

		if(arr == null || arr.length == 0){
			return true;
		}
		for (String string : arr) {
			if(string.equals(item)){
				return true;
			}
		}
		return false;
	}


	public static void getXmlNodes(Element node,Map<String, String> params){
		// 获取当前节点的所有属性节点/get all the attributes nodes of the current nodes 
		List<Attribute> list = node.attributes();  
		// 遍历属性节点/traverse the attribute node  
		if ((list == null || list.size() == 0)) {  
			params.put(node.getName(),node.getTextTrim());
			System.out.println(node.getName()+":"+node.getTextTrim());
		}  
		// 当前节点下面子节点迭代器/subnode iterator under the current node 
		Iterator<Element> it = node.elementIterator();  
		// 遍历/traverse  
		while (it.hasNext()) {  
			// 获取某个子节点对象/get a subnode  
			Element e = it.next();  
			// 对子节点进行遍历/traverse a subnode  
			getXmlNodes(e, params);  
		}  
	}
	
	/**
	 * 
	 * @Title:        getTreeIds 
	 * @Description:  TODO
	 * @param:        @param ids
	 * @param:        @return    
	 * @return:       String[]    
	 * @author        Albert
	 * @Date          2017年11月27日 下午5:28:11
	 */
	public static String[] getTreeIds(String ids){
		if(StringUtils.isEmpty(ids)){
			return new String[]{};
		}
		String tempFun =  ids.replace("[", "").replace("]", "").replace("'on',", "").replace("'", "");
		if( tempFun.length() == 0){
			return new String[]{};
		}
		//截取尾号/intercept the tail number
		String finalStr = tempFun.substring(tempFun.length()-1, tempFun.length());

		String[] funcIds;
		if(",".equals(finalStr)){//尾号为，/tail number as
			tempFun = tempFun.substring(0, tempFun.length()-1);
			funcIds = tempFun.split(",");
		}else{
			funcIds = tempFun.split(",");
		}
		return funcIds;
	}
	
	/**
	 * 
	 * @Title:        arrToList 
	 * @Description:  TODO
	 * @param:        @param strs
	 * @param:        @return    
	 * @return:       List<String>    
	 * @author        Albert
	 * @Date          2018年1月26日 下午3:30:49
	 */
	public static List<String> arrToList(String[] strs){
		List<String> list = new ArrayList<String>();
		for (String string : strs) {
			list.add(string);
		}
		return list;
	}
	
	/*public static String getUUIDValues(String ids){
		String[] arr = ids.split(",");
		
	}*/
	
	public static void main(String[] args) {
		System.out.println(getUUIDString().length());
	}
}
