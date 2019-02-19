/**
 * FileName:     PayuUtil.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年8月28日 上午10:16:57/10:16:57 am, August 28, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年8月28日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils.pay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.print.attribute.standard.MediaSize.Other;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.alipay.api.internal.util.StringUtils;
import com.paypal.api.openidconnect.Userinfo;
import com.pgt.bikelock.bo.PayBo;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.HttpRequest;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserVo;

 /**
 * @ClassName:     PayuUtil
 * @Description:PayU支付工具类/PayU payment tool
 * @author:    Albert
 * @date:        2017年8月28日 上午10:16:57/10:16:57 am, August 28, 2017
 *
 */
public class PayuUtil {

	static final String PAYU_IOS_URL = "https://secure.payu.ro/order/ios.php";//instant order status/search(check)
	static final String PAYU_IRN_URL = " https://secure.payu.ro/order/irn.php ";//Instant Refund/Reverse Notification(refund)
	
	static final String PayU_Merchant_Code = OthersSource.getSourceString("payu_merchant_code");
	static final String PayU_Secret_Key = OthersSource.getSourceString("payu_secret_key");
	
	/**
	 * 
	 * @Title:        loadPaymentParams 
	 * @Description:  TODO
	 * @param:        @param body
	 * @param:        @param tradeNo
	 * @param:        @param amount
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年9月30日 下午4:10:03
	 */
	public static String loadPaymentParams(String body,String tradeNo,BigDecimal amount,String userId,String currency){
		Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
		
		//general data
		paramsMap.put("MERCHANT", PayU_Merchant_Code);
		paramsMap.put("ORDER_REF", tradeNo);
		paramsMap.put("ORDER_DATE", TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm_ss));
		//order detail
		paramsMap.put("ORDER_PNAME[]", "Ape Rider Deposit");
		paramsMap.put("ORDER_PCODE[]", "1");
		paramsMap.put("ORDER_PINFO[]", body);//
		paramsMap.put("ORDER_PRICE[]", amount);
		paramsMap.put("ORDER_QTY[]", "1");//count
		paramsMap.put("ORDER_VAT[]", "1");
		paramsMap.put("ORDER_SHIPPING", "0");
		paramsMap.put("PRICES_CURRENCY", currency);

		paramsMap.put("DISCOUNT", "0");
		
		paramsMap.put("DESTINATION_CITY", "Bucuresti");
		paramsMap.put("DESTINATION_STATE", "Bucuresti");
		paramsMap.put("DESTINATION_COUNTRY", "RO");
		paramsMap.put("PAY_METHOD", "CCVISAMC");
		paramsMap.put("ORDER_PRICE_TYPE[]", "GROSS");
	//	paramsMap.put("SELECTED_INSTALLMENTS_NO", "GROSS");
		paramsMap.put("TESTORDER", "TRUE");
		
		String token = generateSignature(paramsMap);
		paramsMap.put("ORDER_HASH",token);
		//customer info
		
		UserDetailVo userVo = new UserDaoImpl().getUserDetailInfo(userId);
		
		paramsMap.put("BILL_FNAME", userVo.getLastname());
		paramsMap.put("BILL_LNAME", userVo.getFirstname());
		paramsMap.put("BILL_EMAIL", userVo.getEmail());
		paramsMap.put("BILL_PHONE", userVo.getUserVo().getPhone());
		paramsMap.put("BILL_COUNTRYCODE", "RO");
		//additional info
		paramsMap.put("LANGUAGE", "EN");		
//		paramsMap.put("BACK_REF","http://omni0755.iok.la:19679/OmniBikeLock/payresult");
		return ValueUtil.mapToString(paramsMap);
	}
	
	/**
	 * 
	 * @Title:        checkPaymentStatus 
	 * @Description:  Check Payment Status
	 * @param:        @param tradeNo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月30日 下午4:17:03
	 */
	public static boolean checkPaymentStatus(String tradeNo){
		Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
		paramsMap.put("MERCHANT", PayU_Merchant_Code);
		paramsMap.put("REFNOEXT", tradeNo);
		paramsMap.put("HASH",generateSignature(paramsMap));
		String xmlString = HttpRequest.sendPost(PAYU_IOS_URL, ValueUtil.mapToString(paramsMap));
		if(!StringUtils.isEmpty(xmlString)){
			Document document = null;
			try {
				document = DocumentHelper.parseText(new String(xmlString.getBytes()));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String, String> resultParams = new LinkedHashMap<String, String>();
			ValueUtil.getXmlNodes(document.getRootElement(), resultParams);
			String hashTemp = (String) resultParams.get("HASH");
			resultParams.remove("HASH");
			resultParams.remove("Order");
			if(!hashTemp.equals(generateSignature(ValueUtil.mapStringToObject(resultParams)))){
				System.out.println("token error");
				return false;
			}
			String paymentId = resultParams.get("REFNO");
			String refNoext = resultParams.get("REFNOEXT");
			String status = resultParams.get("ORDER_STATUS");
			if(!StringUtils.isEmpty(paymentId) && !StringUtils.isEmpty(refNoext)){
				status = status.toUpperCase();
				if(!status.equals("TEST") && !status.equals("COMPLETE") && !status.equals("IN_PROGRESS")
						&& !status.equals("PAYMENT_AUTHORIZED")	&& !StringUtils.isEmpty(status)){
					return false;
				}
				
				if(!refNoext.equals(tradeNo)){
					return false;
				}
				
				ITradeDao consumeDao = new TradeDaoImpl();
				TradeVo trade = consumeDao.getTradeInfo(tradeNo);
				if(trade == null){//订单不存在/order inexisting
					return false;
				}
				if(trade.getStatus() == 1){
					return true;
				}
				trade.setOut_trade_no(paymentId);
				boolean flag = PayBo.dealPayResult(trade);
				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title:        refundOrder 
	 * @Description:  退款
	 * @param:        @param cancelId
	 * @param:        @param paymentId
	 * @param:        @param amount
	 * @param:        @param currency
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月30日 下午4:53:35
	 */
	public static boolean refundOrder(String cancelId,String paymentId,BigDecimal amount,String currency){
		Map<String, Object> paramsMap = new LinkedHashMap<String, Object>();
		paramsMap.put("MERCHANT", PayU_Merchant_Code);
		paramsMap.put("ORDER_REF", paymentId);
		paramsMap.put("ORDER_AMOUNT", amount);
		paramsMap.put("ORDER_CURRENCY", "RON");
		paramsMap.put("IRN_DATE", TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm_ss));
		paramsMap.put("ORDER_HASH",generateSignature(paramsMap));
		paramsMap.put("AMOUNT", amount);
		String xmlString = HttpRequest.sendPost(PAYU_IRN_URL, ValueUtil.mapToString(paramsMap));
		if(!StringUtils.isEmpty(xmlString)){
			Document document = null;
			try {
				document = DocumentHelper.parseText(new String(xmlString.getBytes()));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Map<String, String> resultParams = new LinkedHashMap<String, String>();
			ValueUtil.getXmlNodes(document.getRootElement(), resultParams);
			String refundPaymentId = resultParams.get("ORDER_REF");
			String code = resultParams.get("RESPONSE_CODE");
			String msg = resultParams.get("RESPONSE_MSG");
			if(!StringUtils.isEmpty(refundPaymentId) && !StringUtils.isEmpty(code) && !StringUtils.isEmpty(msg)){
				
				
				if(!refundPaymentId.equals(paymentId)){
					return false;
				}
				
				msg = msg.toUpperCase();
				if(code.equals("1") && msg.equals("OK")){
					return false;
				}
		
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @Title:        generateSignature 
	 * @Description:  Signature token
	 * @param:        @param paramsMap
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年9月30日 下午2:24:29
	 */
	public static String generateSignature(Map<String, Object> paramsMap){
		String paramsStr = "";
		for (Entry<String, Object> param : paramsMap.entrySet()) {
			paramsStr +=param.getValue().toString().length()+""+param.getValue();
		}
//		System.out.println("params:"+paramsStr);
		String token = MD5Utils.getHmacMd5(PayU_Secret_Key, paramsStr).toLowerCase();
//		System.out.println(token);
		return token;
	}
	
	
	public static void main(String[] args) {
		checkPaymentStatus("5527");
	}
	
}
