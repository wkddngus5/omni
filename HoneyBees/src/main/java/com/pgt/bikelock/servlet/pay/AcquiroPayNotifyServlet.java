package com.pgt.bikelock.servlet.pay;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pgt.bikelock.servlet.BaseServlet;
import com.pgt.bikelock.utils.pay.AcquiroPayUtil;


/**
 * 
 * @ClassName:     AcquiroPayNotifyServlet
 * @Description:AcquiroPay支付异步通知校验(非接口，提供给AcquiroPay回调)/Payment Asynchronous Notification Check (non-interface, available to AcquiroPay callback)
 * @author:    Albert
 * @date:      2017年8月23日10:26:35
 *
 */
public class AcquiroPayNotifyServlet extends BaseServlet {
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
		System.out.println("AcquiroPayNotifyServlet,type:"+req.getParameter("type"));
		Map<String, String[]> paramsMap = req.getParameterMap();
		for (Entry<String, String[]> param : paramsMap.entrySet()) {
			System.out.println(param.getKey()+":"+req.getParameter(param.getKey()));
		}
		
		if("callback".equals(req.getParameter("type"))){
			AcquiroPayUtil.checkPaymentResult(req);
		}
		
		
	}
}
