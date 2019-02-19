/**
 * FileName:     NexMoSMSUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 下午5:13:53/5:13:53 pm, April 11,2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils;

import java.io.IOException;
import java.util.LinkedHashMap;

import javax.print.attribute.standard.MediaSize.Other;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.alipay.api.internal.util.StringUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.isms.HttpClientUtil;
import com.pgt.bikelock.utils.isms.WebNetEncode;
import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.message.MessageResponse;
import com.plivo.helper.exception.PlivoException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 * @ClassName:     NexMoSMSUtil
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月11日 下午5:13:53/5:13:53 pm, April 11,2017
 *
 */
public class SMSUtil {

	private CloseableHttpClient client = HttpClients.createDefault();


	/**
	 * Nexmo短信发送
	 * @Title:        sendNexmoMessage 
	 * @Description:  TODO
	 * @param:        @param phone
	 * @param:        @param content
	 * @param:        @return    
	 * @return:       CloseableHttpResponse    
	 * @author        Albert
	 * @Date          2017年6月20日 下午3:19:42/3:19:42 pm, April 20, 2017
	 */
	public boolean sendNexmoMessage(String phone, String content)
	{
		try
		{
			// Create the JSON object
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("to", phone);
			jsonObject.put("api_key", OthersSource.NEXMO_SMS_API_KEY);
			jsonObject.put("api_secret", OthersSource.NEXMO_SMS_API_SECRET);
			jsonObject.put("from", OthersSource.NEXMO_SMS_API_FROM);
			jsonObject.put("text", content);

			System.out.println(String.format("sendMessage() jsonObject = %s INTERANATIONAL_URL = %s", jsonObject.toString(), OthersSource.NEXMO_SMS_API_INTERNATION_URL));

			HttpPost request = new HttpPost(OthersSource.NEXMO_SMS_API_INTERNATION_URL);
			StringEntity params = new StringEntity(jsonObject.toString());
			params.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

			request.setEntity(params);
			request.setHeader("Content-type", "application/json");
			HttpResponse response = client.execute(request);

			System.out.println(response.toString());
			System.out.println(response.getEntity().toString());

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public boolean sendIsmsMessage(String phone, String content)
	{
		try {
			String ip = "210.51.190.233";
			int port = 8085;
			// HTTP 请求工具/request tool
			HttpClientUtil util = new HttpClientUtil(ip, port, "/mt/MT3.ashx");
			String user = OthersSource.getSourceString("isms_api_key");//你的用户名/your account
			String pwd = OthersSource.getSourceString("isms_api_secret");//你的密码：/your password
			String ServiceID = "SEND"; //固定，不需要改变/fixed, no need to change
			String dest = phone; // 你的目的号码【收短信的电话号码】/your target number[the number which receiving message]
			String sender = "";// 你的原号码,可空【大部分国家原号码带不过去，只有少数国家支持透传，所有一般为空】/your original number, null [the number is unavailable in many countries, and only a few countries support your original number, so it is normally null]
			String msg = content;//你的短信内容/your message content
			// codec=8 Unicode 编码/code,  3 ISO-8859-1, 0 ASCII
			// 短信内容/message content HEX 编码/code，8 为 UTF-16BE HEX 编码/8 is the code of UTF-16BE HEX， dataCoding = 8 ,支持所有国家的语言，建议直接使用 8/dataCoding = 8, supports all the languages, so 8 is recommended
			String hex = WebNetEncode.encodeHexStr(8, msg);
			hex = hex.trim() + "&codec=8";
			System.out.println("POST MT3");
			// HTTP 封包请求/encapsulation, util.sendPostMessage  返回结果/return result，
			// 如果是以 “-” 开头的为发送失败，请查看错误代码，否则为MSGID/It means failed to send if the start is "-", and check the error code, otherwise MSGID
			System.out.println("msgid = "
					+ util.sendPostMessage(user, pwd, ServiceID, dest, sender,
							hex));
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public boolean sendTwilioMessage(String phone, String content)
	{

		Twilio.init(OthersSource.getSourceString("twilio_api_key"), OthersSource.getSourceString("twilio_api_secret"));

		try {
			Message message = Message
					.creator(
							new PhoneNumber(phone),
							new PhoneNumber(OthersSource
									.getSourceString("twilio_api_from_phone")),
									content).create();
			System.out.println(message.getSid());
			if(!StringUtils.isEmpty(message.getSid())){
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

		return false;

	}

	public boolean sendPlivoMessage(String phone, String content){
		RestAPI api = new RestAPI(OthersSource.getSourceString("plivo_api_key"),
				OthersSource.getSourceString("plivo_api_secret"), "v1");

		LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("src", OthersSource.getSourceString("plivo_api_from_phone")); // Sender's phone number with country code
		parameters.put("dst", phone); // Receiver's phone number with country code
		parameters.put("text", content); // Your SMS text message
		// Send Unicode text
		//parameters.put("text", "こんにちは、元気ですか？"); // Your SMS text message - Japanese
		//parameters.put("text", "Ce est texte généré aléatoirement"); // Your SMS text message - French
		parameters.put("url", "http://example.com/report/"); // The URL to which with the status of the message is sent
		parameters.put("method", "GET"); // The method used to call the url

		try {
			// Send the message
			MessageResponse msgResponse = api.sendMessage(parameters);

			// Print the response
			System.out.println(msgResponse);
			// Print the Api ID
			System.out.println("Api ID : " + msgResponse.apiId);
			// Print the Response Message
			System.out.println("Message : " + msgResponse.message);

			if (msgResponse.serverCode == 202) {
				// Print the Message UUID
				System.out.println("Message UUID : " + msgResponse.messageUuids.get(0).toString());
				return true;
			} else {
				System.out.println(msgResponse.error);
			}
		} catch (PlivoException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return false;
	}

	public void close()
	{
		if(client != null)
		{
			try
			{
				client.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static boolean sendSopranoMessage(String phone, String content){
		try {
			content = content.replace(" ", "+");
			HttpRequest
			.sendPost(
					"https://ro.sopranodesign.com/cgphttp/servlet/sendmsg?"
							+ "username="+OthersSource.getSourceString("soprano_api_key")+"&password="
							+OthersSource.getSourceString("soprano_api_secret")+"&destination="
							+ phone + "&text=" + content, null);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Title:        sendAliMessage 
	 * @Description:  阿里云短信发送
	 * @param:        @param phone 手机号
	 * @param:        @param code 验证码
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年1月12日 下午5:35:15
	 */
	public static boolean sendAliMessage(String phone, String code){
		//设置超时时间-可自行调整
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");
		//初始化ascClient需要的几个参数
		final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
		final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
		//替换成你的AK
		final String accessKeyId = OthersSource.getSourceString("alisms_api_key");//你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = OthersSource.getSourceString("alisms_api_secret");//你的accessKeySecret，参考本文档步骤2
		//初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
				accessKeySecret);
		try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
		} catch (ClientException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return false;
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		//组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		//使用post提交
		request.setMethod(MethodType.POST);
		//必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
		request.setPhoneNumbers(phone);
		//必填:短信签名-可在短信控制台中找到
		request.setSignName(OthersSource.getSourceString("project_name"));
		//必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(OthersSource.getSourceString("alisms_api_template_code"));
		//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		//友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		request.setTemplateParam("{\"code\":\""+code+"\"}");
		//可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
		//request.setSmsUpExtendCode("90997");
		//可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
		request.setOutId("1");
		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
				//请求成功
				return true;
			}
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	
	}


	/** 
	 * @Title:        main 
	 * @Description:  TODO
	 * @param:        @param args    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午5:13:53/5:13:53 pm, April 11, 2017
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//配置初始化/Configuration initialization
		//		OthersSource.init();
		//		SMSUtil client = new SMSUtil();
		//		//		client.sendMessage("31624864623", "test sms code from omni");
		//		client.sendNexmoMessage("8613410996848", "test sms code from omni");
		//		//		client.sendMessage("31614921430", "232223 sms code");
		//		client.close();

		//client.sendPlivoMessage("+8613410996848", "你好/hello,Albert，测试/test,2017年6月21日16:04:56");
		//		client.sendNexmoMessage("8613410996848", "test sms code from omni"+TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm));

//		if(sendSopranoMessage("40722889321", "sms content from aperider"+TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm))){
//			System.out.println("send success");
//		}else{
//			System.out.println("send fail");
//		}
		System.out.println(sendAliMessage("13410996848","123456"));
	}

}
