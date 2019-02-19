package com.pgt.bikelock.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.pgt.bikelock.utils.pay.PayPalUtil;
import com.pgt.bikelock.utils.pay.WechatPayUtil;


/**
 * 
 * @ClassName:     PaypalNotifyServlet
 * @Description:Paypal支付异步通知校验(非接口，提供给Paypal回调)/Payment Asynchronous Notification Check (non-interface, available to Paypal callback)
 * @author:    Albert
 * @date:        2017年7月25日 下午4:28:40
 *
 */
public class PaypalNotifyServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		System.out.println("PaypalNotifyServlet");

		Payment payment = new Payment();
		payment.setId(req.getParameter("paymentId"));

		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(req.getParameter("PayerID"));
		try {
			APIContext apiContext = new APIContext(PayPalUtil.clientID, PayPalUtil.clientSecret, "sandbox");
			Payment createdPayment = payment.execute(apiContext, paymentExecution);
			System.out.println(createdPayment);
		} catch (PayPalRESTException e) {
			System.err.println(e.getDetails());
		}

	}
}
