/**
 * FileName:     ReceiveSmsServlet.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年1月16日 上午10:05:11
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年1月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgt.bikelock.dao.impl.SmsDaoImpl;
import com.pgt.bikelock.vo.SmsVo;

/**
 * @ClassName:     ReceiveSmsServlet
 * @Description:短信接收服务类/receive sms server
 * @author:    Albert
 * @date:        2018年1月16日 上午10:05:11
 *
 */
public class ReceiveSmsServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkType(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		checkType(request, response);
	}
	
	public void checkType(HttpServletRequest request, HttpServletResponse response){
		if("plivo".equals(request.getParameter("type"))){
			receivePlivoSms(request,response);
		}
	}

	/**
	 * 
	 * @Title:        receivePlivoSms 
	 * @Description:  plivo sms receive
	 * @param:        @param request
	 * @param:        @param response    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年1月16日 上午10:08:29
	 */
	public void receivePlivoSms(HttpServletRequest request, HttpServletResponse response){
		System.out.println("receivePlivoSms");
		//Sender's phone numer
		String from_number = request.getParameter("From");
		//Receiver's phone number - Plivo number
		String  to_number = request.getParameter("To");
		//The text which was received
		String  text = request.getParameter("Text");
		//Print the message
		System.out.println(String.format("Message received - From: %s, To: %s, Text: %s", from_number, to_number, text));
		
		SmsVo smsVo = new SmsVo();
		smsVo.setPhone(from_number);
		smsVo.setContent(text);
		smsVo.setType(3);
		new SmsDaoImpl().addSms(smsVo);
	}

}
