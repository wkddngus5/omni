package com.pgt.bikelock.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
/**
 * 普通短信发送测试Main/test for common message sending
 * @author 
 *
 */
public class HttpSenderTest {
	
	private static String account="N8398193"; // 发送短信的账号(非登录账号) (示例:N987654)/account sending message (not the login account)(example:N987654)
	private static String pswd="UgbalkSo2126";// 发送短信的密码(非登录密码)/password sending message (not the login password)
	
	//调用Main() 方法前记得更改 —— 电话号码、 账号、密码  等信息/please change the phone number, account, password, etc before invoking Main () method
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		sendMessage();//调用发送普通短信方法/invoking the method of sending common message
		
		//sendDelay(); //调用定时短信测试/invoking the test of timed message
		
		//SendVariable();//调用变量短信测试/invoking the test of variable message
		
		//getReport();//拉取状态报告测试/test of the pulling the status report
		
		//getbalance();//查询余额/check the balance
	}
	
	//发送普通短信方法/the method of sending common message
	public static void sendMessage(){
		String url = "http://sms.253.com/msg/send";  //应用地址 (无特殊情况时无需修改)/application address (no need to modify normally)
		String msg = "测试短信,您的验证码是123456/test message, your verified code is 123456"; //您的签名+短信内容 //your signature + message content
		
		String extno = null;     	// 扩展码(可选参数,可自定义)/extended code (optional parameter, customizable)
		String phone="13410996848";	// 短信接收号码,多个号码用英文,隔开/number of receiving message, multi number could be divided by ","
		String rd="1";				// 是否需要状态报告(需要:1,不需要:0)/need the status report or not (yes:1, No, 0)
		try {
			    String returnString2 = HttpSender.SendPost(url,"N650262_N6733035", "FCQm0Ov6rt0f54",msg.toString(),phone,rd,extno); 
			    System.out.println(returnString2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//定时短信测试/timed message test
	public static void sendDelay() throws UnsupportedEncodingException {
		String url = "http://sms.253.com/msg/sendDelay";// 应用地址(无特殊情况时无需修改)/application address (no need to modify normally)
		String msg = "【253云通讯】测试定时短信内容[253 cloud communication test timed message content]";            // 短信内容/message content
		String rd="1";					// 是否需要状态报告(需要:1,不需要:0)/need the status report or not (yes:1, No, 0)
		String ex = null;     			// 扩展码(可选参数,可自定义)/extended code (optional parameter, customizable)
		String delay="201703091317";	//时间格式 yyyyMMDDHHmm (例如：201703091301——2017年3月9日13点00)/time format yyyyMMDDHHmm (example:201703091301-13:00, March 9, 2017)
		String phone="187********";		// 短信接收号码/number of receiving message
		
		try {
			String returnString = HttpSender.senddelay(url, account, pswd, phone, msg, rd, ex, delay);
			System.out.println(returnString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//变量短信测试/variable message test
	public static void SendVariable() {
		String url = "http://sms.253.com/msg/HttpVarSM";// 应用地址(无特殊情况时无需修改)/application address (no need to modify normally)
		String msg = "尊敬的{$var},您好,这是一条测试变量短信,您的验证码是{$var},{$var}分钟内有效/Dear {$var}, this is a test message, your verified code is {$var},which is valid in {$var} minute";// 短信内容/message content
		String params = "187********,田女士,654321,3;130********,李先生,859437,5";//第一个固定为手机号码,之后参数一一对应各个变量,每组参数中间用英文分号;分开/the first is a fixed mobile phone number,the followings parameters are corresponding to each variable, parameters are divided by ";"
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false/need the status report or not (yes:true, No, false)
		String extno = null;	  // 扩展码(可选参数,可自定义)/extended code (optional parameter, customizable)

		try {
			String returnString = HttpSender.batchSendVariable(url,account, pswd,msg,params,needstatus,extno);
			System.out.println(returnString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//查询账户余额/check account balance
	public static void getbalance() {
		String url = "http://sms.253.com/msg/balance";// 应用地址(无特殊情况时无需修改)/application address (no need to modify normally)
		try {
			String returnString = HttpSender.query(url,account, pswd);
			System.out.println(returnString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//拉取状态报告/pull the status report
	public static void getReport() {
		String url = "http://stat.253.com/dragon_api/pull/report";// 应用地址(无特殊情况时无需修改)/application address (no need to modify normally)
		String key = "*******";		 //联系客服配置 key 方可拉取状态报告/pull the status report before contacting customer service configuration key
		String count="2";			 //拉取状态报告个数/the quantity of pull the status report
		try {
			String returnString = HttpSender.getFlockStatusReport(url, account, key, count);
			System.out.println(returnString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
