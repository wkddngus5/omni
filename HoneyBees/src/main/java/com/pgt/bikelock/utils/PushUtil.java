/**
+ * FileName:     PushUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年3月30日 上午10:54:17/10:54:17 am, March 30, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年3月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.UserDeviceVo;
import com.tencent.xinge.Message;
import com.tencent.xinge.XingeApp;

import javapns.devices.Device;
import javapns.devices.implementations.basic.BasicDevice;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PushNotificationManager;
import javapns.notification.PushNotificationPayload;
import javapns.notification.PushedNotification;

/**
 * @ClassName:     PushUtil
 * @Description:推送工具类/push tool
 * @author:    Albert
 * @date:        2017年3月30日 上午10:54:17/10:54:17 am, March 30, 2017
 *
 */
public class PushUtil {

	static final String IOS_PUSH_FILE_PATH = OthersSource.getSourceString("ios_push_file");
	static final String IOS_PUSH_FILE_PASSWORD = OthersSource.getSourceString("ios_push_key");
	static final String FCM_PASSWORD = OthersSource.getSourceString("android_push_google_fcm_key");
	static final Long TENCENT_XINGE_KEY = ValueUtil.getLong(OthersSource.getSourceString("android_push_xg_key"));
	static final String TENCENT_XINGE_SECRET =OthersSource.getSourceString("android_push_xg_secret");
	/**
	 * 
	 * @ClassName:     PushType
	 * @Description:推送类型/push type
	 * @author:    Albert
	 * @date:        2017年12月1日 下午5:17:12
	 *
	 */
	public enum PushType{
		Push_Type_Other_Device_Login,//异地登陆
		Push_Type_New_Version,//新版提示
		Push_Type_New_Message,//新消息
		Push_Type_Ride_Change,//骑行状态变化
	}

	/**
	 * 
	 * @Title:        push 
	 * @Description:  TODO
	 * @param:        @param deviceVo
	 * @param:        @param title
	 * @param:        @param content
	 * @param:        @param customerDic    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月8日 下午4:03:37
	 */
	public static void push(UserDeviceVo deviceVo,String title,String content,Map<String, String> customerDic){		
		if(deviceVo == null || StringUtils.isEmpty(deviceVo.getToken())){
			return;
		}
		if(deviceVo.getType() == 1){
			//android
			if(TENCENT_XINGE_KEY != 0){
				tencentXingPush(deviceVo.getToken(), title, content, customerDic);
			}else if(FCM_PASSWORD != null){
				fcmPush(deviceVo.getToken(), title, content, customerDic);
			}
			
		}else if(deviceVo.getType() == 2){
			//ios
			applePush(deviceVo.getToken(), content, customerDic);
		}
	}



	/**
	 * 
	 * @Title:        applePush 
	 * @Description:  Apple push
	 * @param:        @param token
	 * @param:        @param alert
	 * @param:        @param customerDic    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月1日 下午5:13:15
	 */
	public static void applePush(String token,String alert,Map<String, String> customerDic){

		if(StringUtils.isEmpty(token)){
			return;
		}
		System.out.println("ios push "+token);
		int badge = 1;//图标小红圈的数值/the small red circle value of icon  
		String certificatePath = IOS_PUSH_FILE_PATH;  
		String certificatePassword = IOS_PUSH_FILE_PASSWORD;

		try  
		{  
			PushNotificationPayload payLoad = new PushNotificationPayload();  
			payLoad.addAlert(alert); // 消息内容/message content  
			payLoad.addBadge(badge); // iphone应用图标上小红圈上的数值/the small red circle value of iphone appliaction icon   
			payLoad.addSound("default");
			if(customerDic != null && customerDic.size() > 0){
				for (Entry<String, String> param : customerDic.entrySet()) {
					payLoad.addCustomDictionary(param.getKey(), param.getValue());
				}

			}

			PushNotificationManager pushManager = new PushNotificationManager();  
			//true：表示的是产品发布推送服务 false：表示的是产品测试推送服务/true: send push service false: test push service   
			pushManager.initializeConnection(new AppleNotificationServerBasicImpl(certificatePath, certificatePassword, true));  
			List<PushedNotification> notifications = new ArrayList<PushedNotification>();  
			// 发送push消息/send push message  

			Device device = new BasicDevice();  
			device.setToken(token);  
			PushedNotification notification = pushManager.sendNotification(device, payLoad, true);  
			notifications.add(notification);  

			pushManager.stopConnection();  
		}  
		catch (Exception e)  
		{  
			e.printStackTrace();  
		}  
	}

	/**
	 * 
	 * @Title:        fcmPush 
	 * @Description:  google fcm push
	 * @param:        @param token
	 * @param:        @param title
	 * @param:        @param content
	 * @param:        @param customerDic    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月8日 下午4:02:30
	 */
	public static void fcmPush(String token,String title,String content,Map<String, String> customerDic){
		Map<String, String> headMap = new HashMap<String, String>();
		headMap.put("Authorization", "key="+FCM_PASSWORD);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("to", token);
		dataMap.put("data.title", title);
		dataMap.put("data.content", content);
		for (Entry<String, String> param : customerDic.entrySet()) {
			dataMap.put("data."+param.getKey(), param.getValue());
		}
		HttpRequest.sendPost("https://fcm.googleapis.com/fcm/send", ValueUtil.mapToString(dataMap), headMap);
	}

	/**
	 * 
	 * @Title:        tencentXingPush 
	 * @Description:  腾讯信鸽推送（Android）
	 * @param:        @param token
	 * @param:        @param title
	 * @param:        @param content
	 * @param:        @param customerDic    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年1月12日 下午5:56:42
	 */
	public static void tencentXingPush(String token,String title,String content,Map<String, String> customerDic){
		XingeApp xinge = new XingeApp(TENCENT_XINGE_KEY,TENCENT_XINGE_SECRET);
		Message message = new Message();
		message.setTitle(title);
		message.setContent(content);
		message.setType(Message.TYPE_MESSAGE);
		message.setExpireTime(86400);
		
		if(customerDic != null && customerDic.size() > 0){
			Map<String, Object> dic = new HashMap<String, Object>();
			for (Entry<String, String> param : customerDic.entrySet()) {
				dic.put(param.getKey(),param.getValue());
			}
			message.setCustom(dic);
		}
		JSONObject ret = xinge.pushSingleDevice(token, message);
		System.out.println(ret.toString());
	}


	public static void main(String[] args) {
		//5627c8e3181b1617614c10c9e757594cec78aacc91b2f01453e4fce73e4cad63
//		e2c7e7aa27964ccf4de1e9f34eccd6198b9905a6b89ee5a290637d5e42e58a42
		applePush("9d028a99a68ad226957bca0bcaea8c1c117f1dc0431593e15e097e0c039da64f", "hello", null);
//		tencentXingPush("57c5c3e3c91020c947a59750dd4484c8b7147b7f", "hello title", "hello content", getPushCustomerDictionary(PushType.Push_Type_Other_Device_Login));
	}

	/**
	 * 
	 * @Title:        getPushCustomerDictionary 
	 * @Description:  TODO
	 * @param:        @param pushType
	 * @param:        @return    
	 * @return:       Map<String,String>    
	 * @author        Albert
	 * @Date          2017年12月1日 下午5:19:53
	 */
	public static Map<String, String> getPushCustomerDictionary(PushType pushType){		
		int typeValue = 0;
		switch (pushType) {
		case Push_Type_Other_Device_Login:
			typeValue = 1;
			break;
		case Push_Type_New_Version:
			typeValue = 2;
			break;
		case Push_Type_New_Message:
			typeValue = 3;
			break;
			case Push_Type_Ride_Change:
			typeValue = 4;
			break;
		default:
			break;
		}
		if(typeValue == 0){
			return null;
		}

		Map<String, String> cdMap = new LinkedHashMap<String, String>();
		cdMap.put("pushType", typeValue+"");
		return cdMap;
	}


}

