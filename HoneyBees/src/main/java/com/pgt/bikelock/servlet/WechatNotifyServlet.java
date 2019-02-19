package com.pgt.bikelock.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.pay.WechatPayUtil;


/**
 * 微信支付异步通知校验(非接口，提供给微信回调)/wechat payment notify verify(non interface, provide wechat back)
 * @author apple
 * 2017年03月07日17:30:30
 */
public class WechatNotifyServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		System.out.println("WechatPayNotifyServlet");
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int type = ValueUtil.getInt(req.getParameter("type"));
		if(xmlString == null){
			WechatPayUtil.responseStatus(resp,false,"参数格式校验错误",type);
			return;
		}

		//获取微信POST过来反馈信息/gain wechat post to feedback information
		WechatPayUtil.checkWechatPayData(xmlString, resp,true,type);

	}
}
