/**
 * FileName:     PayPalUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月18日 上午10:46:54/10:46:54 am, July 18, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月18日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils.pay;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.internal.util.StringUtils;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Refund;
import com.paypal.api.payments.RefundRequest;
import com.paypal.api.payments.Sale;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.UserDetailVo;

/**
 * @ClassName:     PayPalUtil
 * @Description:PayPal支付工具类/PayPal payment tool
 * @author:    Albert
 * @date:        2017年7月18日 上午10:46:54/10:46:54 am, July 18, 2017
 *
 */
public class PayPalUtil {

	public static final String clientID = OthersSource.getSourceString("paypal_client_id");
	public static final String clientSecret = OthersSource.getSourceString("paypal_client_secret");
	public static final String accessToken = OthersSource.getSourceString("paypal_access_token");

	public static String createToken(){
		BraintreeGateway gateway = new BraintreeGateway(accessToken);
		return gateway.clientToken().generate();

	}

	/**
	 * 
	 * @Title:        createTransaction 
	 * @Description:  创建交易/creat transaction
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param amount
	 * @param:        @param currency
	 * @param:        @param tradeNo
	 * @param:        @param detailVo
	 * @param:        @return    
	 * @return:       Result<com.braintreegateway.Transaction>    
	 * @author        Albert
	 * @Date          2017年8月2日 下午4:55:21/4:55:21 pm, August 2, 2017
	 */
	public static Result<com.braintreegateway.Transaction> createTransaction(HttpServletRequest req,HttpServletResponse resp,BigDecimal amount,
			String currency,String tradeNo,UserDetailVo detailVo){
		BraintreeGateway gateway = new BraintreeGateway(accessToken);
		Result<Transaction> result = null;
		if(StringUtils.isEmpty(detailVo.getAddress())){
			detailVo.setAddress("Default Address");
		}
		try {
			TransactionRequest request = new TransactionRequest()
			.amount(amount)
			.orderId(tradeNo)
			.merchantAccountId(currency)
			.paymentMethodNonce(req.getParameter("paymentMethodNonce"))
			.shippingAddress()
			.firstName(detailVo.getFirstname())
			.lastName(detailVo.getLastname())
			.company("Braintree")
			.streetAddress("1 E 1st St")
			.extendedAddress("Suite 403")
			.locality("Bartlett")
			.region("IL")
			.postalCode("60103")
			.countryCodeAlpha2("US")
			.done()
			.options()
			.submitForSettlement(true)
			.done();

			result = gateway.transaction().sale(request);


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}



		return result;
	}


	/**
	 * 
	 * @Title:        refundOrder 
	 * @Description:  Cancel (refund) payment order
	 * @param:        @param paymentId
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月24日 上午9:37:51/9:37:51 am, August 24, 2017
	 */
	public static boolean refundOrder(String paymentId,BigDecimal amount){
		BraintreeGateway gateway = new BraintreeGateway(accessToken);
		Result<Transaction> result = gateway.transaction().refund(paymentId);
		if(result.getErrors() != null){
			for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
				System.out.println("error:"+error.getMessage());
			}
		}

		return result.isSuccess();
	}

	public static void main(String[] args) {
		System.out.println(refundOrder("meraxdy2", new BigDecimal(100)));
	}
}
