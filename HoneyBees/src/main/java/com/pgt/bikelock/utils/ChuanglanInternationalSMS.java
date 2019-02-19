package com.pgt.bikelock.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @ClassName:     ChuanglanSMS
 * @Description:创蓝国际短信通道/Chuanglan international SMS channel
 * @author:    Albert
 * @date:        2017年4月7日 上午10:14:49/10:14:49 am, April 7, 2017
 *
 */
public class ChuanglanInternationalSMS {
	private CloseableHttpClient client;
	private String account;
	private String password;

	private static final String INTERNATIONAL_URL="http://222.73.117.140:8044/mt";

	public ChuanglanInternationalSMS(String account,String password){
		this.account = account;
		this.password = password;
		client = HttpClients.createDefault();
	}



	public CloseableHttpResponse sendInternationalMessage(String phone, String content) {
		String encodedContent = null;
		try {
			encodedContent = URLEncoder.encode(content, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		StringBuffer strBuf = new StringBuffer(INTERNATIONAL_URL);
		strBuf.append("?un=").append(account);
		strBuf.append("&pw=").append(password);
		strBuf.append("&da=").append(phone);
		strBuf.append("&sm=").append(encodedContent);
		strBuf.append("&dc=15&rd=1&rf=2&tf=3");
		HttpGet get = new HttpGet( strBuf.toString() );
		
		try {
			return client.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void close() {
		if(client != null){
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void main(String[] args) {
		ChuanglanInternationalSMS client = new ChuanglanInternationalSMS("I3900928","qSFwbnzg0D0607");
		CloseableHttpResponse response = null;
		try {
			
			//发送国际验证码 0085264748414/ sent international verified code 0085264748414 
			response = client.sendInternationalMessage("0031657552440","[创蓝文化]您的验证码是：1234567/[chuanlan culture]your verified code: 1234567");
			if(response != null && response.getStatusLine().getStatusCode()==200){
				System.out.println(EntityUtils.toString(response.getEntity()));
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.close();
	}
}
