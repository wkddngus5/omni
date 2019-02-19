/**
 * FileName:     LanguageUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月18日 下午3:44:05/3:44:05 pm, April 18, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月18日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initialzing>
 */
package com.pgt.bikelock.utils;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.pgt.bikelock.resource.OthersSource;

/**
 * @ClassName:     LanguageUtil
 * @Description:预约包工具类/Reservation package tool
 * @author:    Albert
 * @date:        2017年4月18日 下午3:44:05/3:44:05 pm, April 18, 2017
 *
 */
public class LanguageUtil {

	public static final String Resource_Package = "resource";

	/**
	 * 
	 * @Title:        getDefaultValue 
	 * @Description:  获取默认语言属性值/get the default language attribute value
	 * @param:        @param key
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月18日 下午3:46:46/3:46:46 pm, April 18, 2017
	 */
	public static String getDefaultValue(String key){
		String[] lang = OthersSource.DEFAULT_LANGUAGE.split("_");

		Locale locale;
		if(lang.length >1){
			locale = new Locale(lang[0], lang[1]);
		}else{
			locale = Locale.getDefault();
		}

		ResourceBundle rb = ResourceBundle.getBundle(Resource_Package, locale);
		if(!rb.containsKey(key)){
			return  "";
		}
		return rb.getString(key);
	}

	/**
	 * 
	 * @Title:        getDefaultValue 
	 * @Description:  获取默认语言属性值/get the default language attribute value
	 * @param:        @param key
	 * @param:        @param params
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月1日 上午11:43:24/11:43:24 am, August 1, 2017
	 */
	public static String getDefaultValue(String key,Object params){
		String value = "";
		try {
			MessageFormat tipMF = new MessageFormat(getDefaultValue(key));
			value = tipMF.format(params);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}

	/**
	 * 获取默认语言包
	 * @Title:        getDefaultResource 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       ResourceBundle    
	 * @author        Albert
	 * @Date          2017年6月13日 下午2:44:56/2:44:56 pm, June 13, 2017
	 */
	public static ResourceBundle getDefaultResource(){
		String[] lang = OthersSource.DEFAULT_LANGUAGE.split("_");

		Locale locale;
		if(lang.length >1){
			locale = new Locale(lang[0], lang[1]);
		}else{
			locale = Locale.getDefault();
		}

		ResourceBundle rb = ResourceBundle.getBundle(Resource_Package, locale);
		return rb;
	}

	/**
	 * 
	 * @Title:        getValue 
	 * @Description:  获取指定语言属性值/get the specified language attribute value
	 * @param:        @param lang
	 * @param:        @param key
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月20日 下午4:03:01/4:03:01 pm, April 20, 2017
	 */
	public static String getValue(String lang,String key){
		Locale locale = getLocale(lang);  
		ResourceBundle rb = ResourceBundle.getBundle(Resource_Package, locale);
		if(!rb.containsKey(key)){
			return  "";
		}
		return rb.getString(key);
	}

	/**
	 * 
	 * @Title:        getLocale 
	 * @Description:  获取语言环境/get language environment
	 * @param:        @param lang
	 * @param:        @return    
	 * @return:       Locale    
	 * @author        Albert
	 * @Date          2017年4月20日 下午4:01:58/4:01:58 pm, April 20, 2017
	 */
	public static Locale getLocale(String lang){
		Locale locale = Locale.getDefault();
		if("zh_cn".equals(lang)){
			locale = new Locale("zh", "cn");
		}else if("en_us".equals(lang)){
			locale = new Locale("en", "us");
		}else if("ru_mo".equals(lang)){
			locale = new Locale("ru", "mo");
		}
		return locale;
	}

	/**
	 * 
	 * @Title:        getStatusStr 
	 * @Description:  获取状态字符/ get status string
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月28日 下午2:36:46/2:36:46 pm, July 28, 2017
	 */
	public static String getStatusStr(int status,String key) {
		if(key == null){
			key = "common_status_value";
		}
		String[] statusTitle = LanguageUtil.getDefaultValue(key).split(",");
		String statusStr = "";
		if(status > statusTitle.length){
			statusStr = "Unknow";
		}else{
			statusStr = statusTitle[status+1];
		}
		return statusStr;
	}



	/**
	 * 
	 * @Title:        getTypeStr 
	 * @Description:  获取类型字符/get type string
	 * @param:        @param typeKey 类型字典/type dictionary
	 * @param:        @param type 类型索引/type directories
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月28日 下午2:40:43/2:40:43 pm, July 28, 2017
	 */
	public static String getTypeStr(String typeKey,int type) {
		String typeStr = "Unknow";
		if(LanguageUtil.getDefaultValue(typeKey) == null){
			return typeStr;
		}
		String[] typeTitle = LanguageUtil.getDefaultValue(typeKey).split(",");

		if(type >= typeTitle.length){
			return typeStr;
		}else{
			typeStr = typeTitle[type];
		}
		return typeStr;
	}


}
