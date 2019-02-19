/**
 * FileName:     SmsBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月12日 上午11:59:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.SmsDaoImpl;
import com.pgt.bikelock.dao.impl.SmsTemplateDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.ChuanglanInternationalSMS;
import com.pgt.bikelock.utils.ChuanglanSMS;
import com.pgt.bikelock.utils.SMSUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.SmsTemplateVo;
import com.pgt.bikelock.vo.SmsVo;

/**
 * @ClassName:     SmsBo
 * @Description:短信业务控制 / SMS service control
 * @author:    Albert
 * @date:        2017年4月12日 上午11:59:38
 *
 */
public class SmsBo {
	public static int SMS_Invalid_Minute  = 5;//短信有效时间 / SMS active time
	
	/**
	 * 
	 * @Title:        sendSmsCode 
	 * @Description:  发送验证码类短信 / Send a code class SMS
	 * @param:        @param industryId
	 * @param:        @param phone
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月12日 下午12:25:56
	 */
	public boolean sendSmsCode(String industryId,String phone,String phoneCode){
		//生成6位随机数 / Generates 6 random numbers
		String code = ValueUtil.getNumberRandom(6);
		//内容生成 / 内容生成
		String content = setContent(industryId, 1, code);
		if(StringUtils.isEmpty(content)){
			return false;//不合法的内容不能发送 / Illegal content can not be sent
		}
		IndustryVo industryVo = new IndustryDaoImpl().getIndustryInfo(industryId);
		if(industryVo == null || StringUtils.isEmpty(industryVo.getArea_code())){
			return false;
		}
		if(phoneCode != null){
			industryVo.setArea_code("+"+phoneCode);
		}
		//调用发送接口 / Call the send interface
		boolean sendSms = false;
		if(industryVo.getArea_code().equals("+86") && OthersSource.CHUANGLAN_CHINA_SMS_API_KEY != null){//中国国内通道 / China's domestic channel
			System.out.println("send sms to"+phone+";content:"+content);
			sendSms = sendChuangLanMsg(content, phone);
		}else if(OthersSource.NEXMO_SMS_API_KEY != null){//Nexmo国际通道 /nexmo international
			String sendPhone = industryVo.getArea_code()+phone;//为手机号追加区号 / add area code
			SMSUtil client = new SMSUtil();
			sendSms = client.sendNexmoMessage(sendPhone, content);
		}else if(OthersSource.CHUANGLAN_INTERNATIONAL_SMS_API_KEY != null){
			String sendPhone = industryVo.getArea_code_number()+phone;//为手机号追加区号 / add area code
			System.out.println("send internationa sms to"+sendPhone+";content:"+content);
			sendSms = sendChuangLanInternationalMsg(content, sendPhone);
		}else if(OthersSource.getSourceString("isms_api_key") != null){
			String sendPhone = industryVo.getArea_code()+phone;//为手机号追加区号 / add area code
			SMSUtil client = new SMSUtil();
			sendSms = client.sendIsmsMessage(sendPhone, content);
		}else if(OthersSource.getSourceString("twilio_api_key") != null){
			String sendPhone = industryVo.getArea_code()+phone;//为手机号追加区号 / add area code
			SMSUtil client = new SMSUtil();
			sendSms = client.sendTwilioMessage(sendPhone, content);
		}else if(OthersSource.getSourceString("plivo_api_key") != null){
			String sendPhone = industryVo.getArea_code()+phone;//为手机号追加区号 / add area code
			SMSUtil client = new SMSUtil();
			sendSms = client.sendPlivoMessage(sendPhone, content);
		}else if(OthersSource.getSourceString("soprano_api_key") != null){
			String sendPhone = industryVo.getArea_code()+phone;//为手机号追加区号 / add area code
			sendSms = SMSUtil.sendSopranoMessage(sendPhone, content);
		}else if(OthersSource.getSourceString("alisms_api_key") != null){
			sendSms = SMSUtil.sendAliMessage(phone, code);
		}
		if(sendSms == false){
			return false;
		}
		
		SmsVo smsVo = new SmsVo(phone, code, content, 1,industryVo.getArea_code());
		return new SmsDaoImpl().addSms(smsVo);
	}
	
	/**
	 * 
	 * @Title:        sendSms 
	 * @Description:  普通短信内容发送/send sms
	 * @param:        @param sendPhone
	 * @param:        @param content
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月24日 下午3:38:48
	 */
	public boolean sendSms(String sendPhone,String content){
		if(StringUtils.isEmpty(content)){
			return false;//不合法的内容不能发送 / Illegal content can not be sent
		}
		
		//调用发送接口 / Call the send interface
		boolean sendSms = false;
		if(OthersSource.CHUANGLAN_CHINA_SMS_API_KEY != null){//中国国内通道 / China's domestic channel
			sendSms = sendChuangLanMsg(content, sendPhone);
		}else if(OthersSource.NEXMO_SMS_API_KEY != null){//Nexmo国际通道 /nexmo international
			SMSUtil client = new SMSUtil();
			sendSms = client.sendNexmoMessage(sendPhone, content);
		}else if(OthersSource.CHUANGLAN_INTERNATIONAL_SMS_API_KEY != null){
			sendSms = sendChuangLanInternationalMsg(content, sendPhone);
		}else if(OthersSource.getSourceString("isms_api_key") != null){
			SMSUtil client = new SMSUtil();
			sendSms = client.sendIsmsMessage(sendPhone, content);
		}else if(OthersSource.getSourceString("twilio_api_key") != null){
			SMSUtil client = new SMSUtil();
			sendSms = client.sendTwilioMessage(sendPhone, content);
		}else if(OthersSource.getSourceString("plivo_api_key") != null){
			SMSUtil client = new SMSUtil();
			sendSms = client.sendPlivoMessage(sendPhone, content);
		}else if(OthersSource.getSourceString("soprano_api_key") != null){
			sendSms = SMSUtil.sendSopranoMessage(sendPhone, content);
		}
		if(sendSms == false){
			return false;
		}
		SmsVo smsVo = new SmsVo(sendPhone, "0", content, 0,"0");
		return new SmsDaoImpl().addSms(smsVo);
	}

	/**
	 * 
	 * @Title:        setContent 
	 * @Description:  设置短信内容 / Set content
	 * @param:        @param industryId
	 * @param:        @param type
	 * @param:        @param code
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月12日 下午12:25:04
	 */
	public static String setContent(String industryId,int type,String code){
		SmsTemplateVo templateVo = new SmsTemplateDaoImpl().getTemplate(industryId, type);
		if(templateVo == null || StringUtils.isEmpty(templateVo.getTemplate())
				|| !templateVo.getTemplate().contains("{code}")){
			return "";
		}
		String content = templateVo.getTemplate().replace("{code}", code);//set code
		return content.replace("{invalidtime}", SMS_Invalid_Minute+"");//set invalid time
	}

	/**
	 * 
	 * @Title:        sendChuangLanMsg 
	 * @Description:  创蓝国内短信发送
	 * @param:        @param content
	 * @param:        @param phone    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月12日 下午2:26:35
	 */
	private boolean sendChuangLanMsg(String content,String phone){
		//发送普通短信方法
		String url = "http://sms.253.com/msg/send";  //应用地址 (无特殊情况时无需修改)

		String extno = null;     	// 扩展码(可选参数,可自定义)
		String rd="1";				// 是否需要状态报告(需要:1,不需要:0)
		try {
			String result = ChuanglanSMS.SendPost(url,OthersSource.CHUANGLAN_CHINA_SMS_API_KEY,
					OthersSource.CHUANGLAN_CHINA_SMS_API_SECRET,
					content.toString(),phone,rd,extno);
			if(!com.alipay.api.internal.util.StringUtils.isEmpty(result)){
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	/**
	 * 
	 * @Title:        sendChuangLanInternationalMsg 
	 * @Description:  国际短信发送
	 * @param:        @param content
	 * @param:        @param phone    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月13日 下午6:51:29
	 */
	private boolean sendChuangLanInternationalMsg(String content,String phone){
		//发送国际短信方法
		ChuanglanInternationalSMS client = new ChuanglanInternationalSMS(OthersSource.CHUANGLAN_INTERNATIONAL_SMS_API_KEY,
				OthersSource.CHUANGLAN_INTERNATIONAL_SMS_API_SECRET);
		CloseableHttpResponse response = null;
		boolean flag = false;
		try {
			
			//发送国际验证码
			response = client.sendInternationalMessage(phone,content);
			if(response != null && response.getStatusLine().getStatusCode()==200){
				System.out.println(EntityUtils.toString(response.getEntity()));
				flag = true;
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			client.close();
		}

		return flag;
	}

	/**
	 * 
	 * @Title:        checkSmsCode 
	 * @Description:  校验验证码/Verification code
	 * @param:        @param code
	 * @param:        @param phone
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月12日 下午12:35:25
	 */
	public boolean checkSmsCode(String code,String phone){
		return new SmsDaoImpl().checkSmsCode(code, phone, SMS_Invalid_Minute);
	}
	
	/**
	 * 短信解锁/sms to open lock
	 * @Title:        smsUnlockBike 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param phone
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年6月16日 下午5:30:47
	 */
	public static int smsUnlockBike(String imei,String phone){
		return 0;
	}
	
	public static void main(String[] args) {
		System.out.println("1".replace("1", "3"));
	}
}
