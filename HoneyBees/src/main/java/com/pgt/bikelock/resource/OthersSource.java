/**
 * FileName:     OthersSource.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 上午11:34:29
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.resource;


import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pgt.bikelock.utils.LogHelper;


 /**
 * @ClassName:     OthersSource
 * @Description:第三方资源配置文件
 * @author:    Albert
 * @date:        2017年4月26日 上午11:34:29
 *
 */
public class OthersSource {
	static String CONFIG_FILE = "OthersConfig";
	public static String DEFAULT_LANGUAGE;
	
	public static String LOCK_SERVER_REQUEST_TOKEN;
	public static String LOCK_SERVER_REQUEST_URL;
	
	//NexmoSMS
	public static  String NEXMO_SMS_API_KEY;
	public static  String NEXMO_SMS_API_SECRET;
	public static  String NEXMO_SMS_API_FROM;
	public static  String NEXMO_SMS_API_INTERNATION_URL;
	
	//ChuanglanSMS
	public static  String CHUANGLAN_CHINA_SMS_API_KEY;
	public static  String CHUANGLAN_CHINA_SMS_API_SECRET;
	public static  String CHUANGLAN_INTERNATIONAL_SMS_API_KEY;
	public static  String CHUANGLAN_INTERNATIONAL_SMS_API_SECRET;
	
	//Strip pay
	public static  String STRIP_PAY_KEY;
	public static  String STRIP_PAY_MEMBERSHIP_CHARGE_DESCRIPTION;
	
	//AliPay
	public static  String ALIPAY_APP_ID ;
	public static  String ALIPAY_APP_PRIVATE_KEY;
	public static  String ALIPAY_APP_PUBLIC_KEY;
	public static  String ALIPAY_PUBLIC_KEY;
	public static  String ALIPAY_SELLER_ID;
	
	//Wechat Pay
	public static  String WECHATPAY_APP_ID ;//应用ID
	public static  String WECHATPAY_MCH_ID ;//商户号
	public static  String WECHATPAY_MCH_KEY ;//商户密钥
	public static  String WECHATPAY_SMALL_PROGRAM_APP_ID ;//应用ID
	public static  String WECHATPAY_SMALL_PROGRAM_MCH_ID ;//商户号
	public static  String WECHATPAY_SMALL_PROGRAM_MCH_KEY ;//商户密钥
	
	public static String PAY_NOTIFAY_BASE_URL;//支付回调地址
	
	//文件路径
	public static String EXPORT_FILE_PATH;
	
	//下载链接
	public static String TENCENT_DOWNLOAD_URL;
	public static String ANDROID_DOWNLOAD_URL;
	public static String IOS_DOWNLOAD_URL;
	
	public static ResourceBundle rb;
	
	private static Log logger = LogFactory.getLog(OthersSource.class);
	
	/**
	 * 
	 * @Title:        init 
	 * @Description:  资源初始化（在AutoStartServlet自启动服务调用，可实现）
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 上午11:41:30
	 */
	public static void init(){
		rb = ResourceBundle.getBundle(CONFIG_FILE);
		
		DEFAULT_LANGUAGE = getSourceString("default_language");
		LOCK_SERVER_REQUEST_TOKEN =getSourceString("lock_server_request_token");
		LOCK_SERVER_REQUEST_URL =getSourceString("lock_server_request_url");
		
		NEXMO_SMS_API_KEY =  getSourceString("nexmo_api_key");
		NEXMO_SMS_API_SECRET = getSourceString("nexmo_api_secret");
		NEXMO_SMS_API_FROM = getSourceString("nexmo_api_from");
		NEXMO_SMS_API_INTERNATION_URL =  getSourceString("nexmo_api_internationl_url");
		
		CHUANGLAN_CHINA_SMS_API_KEY = getSourceString("chuanglan_china_api_key");
		CHUANGLAN_CHINA_SMS_API_SECRET = getSourceString("chuanglan_china_api_secret");
		CHUANGLAN_INTERNATIONAL_SMS_API_KEY = getSourceString("chuanglan_internationl_api_key");
		CHUANGLAN_INTERNATIONAL_SMS_API_SECRET = getSourceString("chuanglan_internationl_api_secret");
		
		ALIPAY_APP_ID = getSourceString("alipay_app_id");
		ALIPAY_APP_PRIVATE_KEY = getSourceString("alipay_app_private_key");
		ALIPAY_APP_PUBLIC_KEY = getSourceString("alipay_app_public_key");
		ALIPAY_PUBLIC_KEY = getSourceString("alipay_public_key");
		ALIPAY_SELLER_ID = getSourceString("alipay_seller_id");
		System.out.println("ALIPAY_SELLER_ID:"+ALIPAY_SELLER_ID);
		
		WECHATPAY_APP_ID = getSourceString("wechatpay_appid");
		WECHATPAY_MCH_ID = getSourceString("wechatpay_mch_id");
		WECHATPAY_MCH_KEY = getSourceString("wechatpay_mch_key");
		
		WECHATPAY_SMALL_PROGRAM_APP_ID = getSourceString("wechatpay_small_program_appid");
		WECHATPAY_SMALL_PROGRAM_MCH_ID = getSourceString("wechatpay_small_program_mch_id");
		WECHATPAY_SMALL_PROGRAM_MCH_KEY = getSourceString("wechatpay_small_program_mch_key");
		
		PAY_NOTIFAY_BASE_URL = getSourceString("pay_notifay_base_url");
		
		STRIP_PAY_KEY = getSourceString("strippay_key");
		STRIP_PAY_MEMBERSHIP_CHARGE_DESCRIPTION = getSourceString("strippay_membership_charge_description");
		
		TENCENT_DOWNLOAD_URL = getSourceString("tencent_download_url");
		ANDROID_DOWNLOAD_URL = getSourceString("android_download_url");
		IOS_DOWNLOAD_URL = getSourceString("ios_download_url");
		
		EXPORT_FILE_PATH = getSourceString("export_file_path");
	}
	
	/**
	 * 
	 * @Title:        getSourceString 
	 * @Description:  获取资源
	 * @param:        @param key
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年5月12日 下午6:26:10
	 */
	public static String getSourceString(String key){
		try {
			
			if (rb != null && rb.containsKey(key)) {
				return rb.getString(key);
			}
			if(rb == null){
				rb = ResourceBundle.getBundle(CONFIG_FILE);
				if (rb.containsKey(key)) {
					return rb.getString(key);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			if(rb == null){
				rb = ResourceBundle.getBundle(CONFIG_FILE);
				if (rb.containsKey(key)) {
					return rb.getString(key);
				}
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		LogHelper.info(logger, "test", null);
	}
}
