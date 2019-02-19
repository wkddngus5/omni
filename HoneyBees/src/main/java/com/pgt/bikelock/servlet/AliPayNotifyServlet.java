package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.NA;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgt.bikelock.utils.pay.AliPayUtil;


/**
 * 支付宝支付异步通知校验Payment Asynchronous Notification Check
 * @author apple
 * 2017年03月07日17:33:37
 */
public class AliPayNotifyServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		System.out.println("AliPayNotifyServlet");
		//获取支付宝POST过来反馈信息/get alipay post feedback information
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = req.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
	
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。/Garbled solve, this code in the use of garbled.
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
	
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。/Remember alipaypublickey is the public key of Alipay, please go to open.alipay.com under the application of view.
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		AliPayUtil.checkAppAliPayData(params, resp);
		
		
	}
}
