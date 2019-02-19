package com.pgt.bikelock.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * 
 * @ClassName:     HttpSender
 * @Description:创蓝国内短信通道/chuanglan domestic message channel
 * @author:    Albert
 * @date:        2017年4月7日 上午10:15:08/10:15:08 am, April 7, 2017
 *
 */
public class HttpSender {
	
	/**
	 *   Post 方式 发送普通短信 / post type send common message
	 * @param url
	 *            应用地址，类似于http://ip:port/msg/ application address similar with http://ip:port/msg/
	 * @param account
	 *            账号/account
	 * @param pswd
	 *            密码/password
	 * @param mobile
	 *            手机号码，多个号码使用","分割/mobile phone number, multi numbers are divided with ","
	 * @param msg
	 *            短信内容/message content
	 * @param rd
	 *            是否需要状态报告，需要1，不需要0 /need the status report or not, yes 1, no 0
	 * @return 返回值定义参见HTTP协议文档 return value definition reference to the HTTP agreement file
	 * @throws Exception
	 */
	public static String SendPost(String url, String un, String pw, String msg, String phone,
			String rd, String extno) throws Exception {
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager());
		PostMethod method = new PostMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "", false));
			method.setRequestBody(new NameValuePair[] {
					new NameValuePair("un", un),
					new NameValuePair("pw", pw),
					new NameValuePair("phone", phone),
					new NameValuePair("msg", msg),
					new NameValuePair("rd", rd),
					new NameValuePair("extno", extno) });
			HttpMethodParams params = new HttpMethodParams();
			params.setContentCharset("UTF-8");
			method.setParams(params);
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				in.close();// 1
				baos.close();// 2
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 *   get 发送普通短信 get send common message
	 * @param url
	 *            应用地址，类似于http://ip:port/msg/ application address similar with http://ip:port/msg/
	 * @param account
	 *            账号/account
	 * @param pswd
	 *            密码/password
	 * @param mobile
	 *            手机号码，多个号码使用","分割/ mobile phone number, multi numbers are divided with ","
	 * @param msg
	 *            短信内容/message content
	 * @param needstatus
	 *            是否需要状态报告，需要true，不需要false/need the status report or not, yes true, no false
	 * @return 返回值定义参见HTTP协议文档/return vale definition reference to the HTTP agreement file
	 * @throws Exception
	 */
	public static String batchSend(String url, String un, String pw, String phone, String msg,
			String rd, String extno) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "", false));
			method.setQueryString(new NameValuePair[] {
					new NameValuePair("un", un),
					new NameValuePair("pw", pw),
					new NameValuePair("phone", phone),
					new NameValuePair("msg", msg),
					new NameValuePair("rd", rd),
					new NameValuePair("extno", extno) });
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				in.close();// 1
				baos.close();// 2
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	
	/**
	 * 查询余额/check balance   
	 * @param url
	 * @param un
	 * @param pw
	 * @return
	 * @throws Exception
	 */
	public static String query(String url, String un, String pw) throws Exception {
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(base);
			method.setQueryString(new NameValuePair[] { 
					new NameValuePair("un", un),
					new NameValuePair("pw", pw)});
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				in.close();// 1
				baos.close();// 2
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	

	
	/**
	 * 定时短信发送/timed message sending   
	 * @param url
	 * @param un
	 * @param pw
	 * @param phone
	 * @param msg
	 * @param rd
	 * @param ex
	 * @param uid
	 * @param delay
	 * @return
	 * @throws Exception
	 */
	public static String senddelay(String url, String un, String pw, String phone, String msg,
			String rd, String ex,String delay) throws Exception {
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		PostMethod method = new PostMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "", false));
			method.setRequestBody(new NameValuePair[] {
					new NameValuePair("un", un),
					new NameValuePair("pw", pw), 
					new NameValuePair("phone", phone),
					new NameValuePair("msg", msg),
					new NameValuePair("rd", rd), 
					new NameValuePair("ex", ex),
					new NameValuePair("delay", delay) });
			HttpMethodParams params = new HttpMethodParams();
			params.setContentCharset("UTF-8");
			method.setParams(params);
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				in.close();// 1
				baos.close();// 2
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	
	/**
	 * 拉取状态报告/ pull status report
	 * @param url      应用地址/application address
	 * @param account  API账号 /API account 
	 * @param key	   API密钥/API key
	 * @param count	       拉取数量	Pulled quantity
	 * @return
	 * @throws Exception
	 */
	public static String getFlockStatusReport(String url, String account, String key,String count) throws Exception {
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		GetMethod method = new GetMethod();
		try {
			
			URI base = new URI(url, false);
			method.setURI(new URI(base, "?", false));
			method.setQueryString(new NameValuePair[] {
					new NameValuePair("account", account),
					new NameValuePair("key", key),
					new NameValuePair("count", count)});
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				in.close();// 1
				baos.close();// 2
				if(baos.toString().equals("")){
					return URLDecoder.decode("暂时没收到此号码的状态报告，请稍后再试！", "UTF-8");
				}else{
					return URLDecoder.decode(baos.toString(), "UTF-8");
				}
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}	
	}
	
	/**
	 * 变量短信发送 variable message sending
	 * @param url
	 *            应用地址，类似于http://ip:port/msg/ application address similar with http://ip:port/msg/
	 * @param account
	 *            账号/account 
	 * @param pswd
	 *            密码/password
	 * @param params
	 *            变量值如 18724118999,内容。。。。多个手机号码 后面用“;”分开/ variable value eg 18724118999, content ... multi phone numbers are divieded with ";"
	 * @param msg
	 *            短信内容/message content
	 * @param needstatus
	 *            是否需要状态报告，需要true，不需要false/need the status report or not, yes true, no false
	 * @return 返回值定义参见HTTP协议文档/return vale definition reference to the HTTP agreement file
	 * @throws Exception
	 */
	public static String batchSendVariable(String url, String account, String pswd, String msg,
			String params,boolean needstatus, String extno) throws Exception {
		HttpClient client = new HttpClient(new HttpClientParams(), new SimpleHttpConnectionManager(true));
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "HttpVarSM", false));
			method.setQueryString(new NameValuePair[] {
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd),
					new NameValuePair("needstatus", String.valueOf(needstatus)),
					new NameValuePair("msg", msg),
					new NameValuePair("params", params),
					new NameValuePair("extno", extno) });
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				in.close();// 1
				baos.close();// 2
				return URLDecoder.decode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}
	}
	
}
