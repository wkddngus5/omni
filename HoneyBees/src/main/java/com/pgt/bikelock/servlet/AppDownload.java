package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.ocsp.RespID;

import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;

public class AppDownload extends HttpServlet {

	private static final int PHONE_TYPE_ANDROID = 1;
	private static final int PHONE_TYPE_IOS = 2;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userAgent = request.getHeader("user-agent");
		String number = request.getParameter("No");
		String industry = request.getParameter("idt");
		System.out.println("number="+number);
		System.out.println("user-agent="+userAgent);
		System.out.println("industry"+industry);
		int phoneType = getPhoneType(userAgent);

		if("4".equals(industry) || "5".equals(industry)){
			if(PHONE_TYPE_ANDROID==phoneType){
				// 跳转至应用宝下载页面/jump to application treasure download page
				response.sendRedirect(OthersSource.ANDROID_DOWNLOAD_URL);
			}else if(PHONE_TYPE_IOS==phoneType){
				// 跳转至app store 下载页面/download page
				response.sendRedirect(OthersSource.IOS_DOWNLOAD_URL);
			}
		}else{
			request.setAttribute("appName", OthersSource.getSourceString("project_name"));
			request.setAttribute("androidUrl", OthersSource.getSourceString("android_download_url"));
			request.setAttribute("iosUrl", OthersSource.getSourceString("ios_download_url"));
			request.getRequestDispatcher("../download.jsp").forward(request, response);
		}

	}



	private int  getPhoneType(String userAgent){
		if(userAgent.indexOf("Android")>0) return 1; //返回1，Android 手机/return 1. Android
		else if(userAgent.indexOf("iPhone")>0) return 2;// 返回2 ，IPHONE 手机/return 2, Iphone 
		return 1; // 默认返回Android 手机/default return android

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
