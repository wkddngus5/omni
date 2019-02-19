package com.pgt.bikelock.servlet.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.servlet.BaseServlet;
import com.pgt.bikelock.utils.pay.YandexPayUtil;


/**
 * 
 * @ClassName:     YandexNotifyServlet
 * @Description:Yandex支付异步通知校验(非接口，提供给Paypal回调)/Payment Asynchronous Notification Check (non-interface, available to AcquiroPay callback)
 * @author:    Albert
 * @date:        2017年7月25日 下午4:28:40
 *
 */
public class YandexNotifyServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		System.out.println("YandexNotifyServlet");
		BufferedReader reader = null;
		String xmlString = null;
		try {
			reader = req.getReader();
			String line = "";

			StringBuffer inputString = new StringBuffer();

			while ((line = reader.readLine()) != null) {
				inputString.append(line);
			}
			xmlString = inputString.toString();
			if(!StringUtils.isEmpty(xmlString)){
				Document document = DocumentHelper.parseText(xmlString);
				Map<String, String> params = YandexPayUtil.getRequestInfo(document.getRootElement());
			}
			System.out.println("requst params:"+xmlString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
