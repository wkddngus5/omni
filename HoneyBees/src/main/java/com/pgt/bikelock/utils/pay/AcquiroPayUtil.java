/**
 * FileName:     AcquiroPayUtil.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年8月22日 上午10:09:10/10:09:10 am, August 22, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年8月22日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<>initializing
 */
package com.pgt.bikelock.utils.pay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.DepositBo;
import com.pgt.bikelock.bo.PayBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.impl.BikeLongLeaseDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.HttpRequest;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserVo;

 /**
 * @ClassName:     AcquiroPayUtil
 * @Description:AcquiroPay支付工具类/payment tool
 * @author:    Albert
 * @date:        2017年8月22日 上午10:09:10/10:09:10 am, August 22, 2017
 *
 */
public class AcquiroPayUtil {

	private static final String acquiropay_api_url = "https://secure.acquiropay.com";
	private static final String acquiropay_getway_url = "https://gateway.acquiropay.com";

	private static final String acquiropay_merchant_id =  OthersSource.getSourceString("acquiropay_merchant_id");
	private static final String acquiropay_product_id = OthersSource.getSourceString("acquiropay_product_id");
	private static final String acquiropay_secret_word = OthersSource.getSourceString("acquiropay_secret_word");
	private static final String acquiropay_notify_url = OthersSource.PAY_NOTIFAY_BASE_URL;
	
	
	public static void main(String[] args) {
//		careatePaymentUrl("depost payment", "10000", new BigDecimal(10.00),"1");
		System.out.println(refundOrder("","c3a8e0d1315f42b4a6f381c6e5e60fc2", new BigDecimal(1000.00)));
	}
	
	/**
	 * 
	 * @Title:        careatePaymentUrl 
	 * @Description:  Create Payment reqeust pamrams and url
	 * @param:        @param body
	 * @param:        @param tradeNo
	 * @param:        @param amount    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月23日 上午10:42:11/10:42:11 am, August 23, 2017
	 */
	public static String careatePaymentUrl(String body,String tradeNo,BigDecimal amount,String userId){
		Map<String, Object> paramsMap = initRequestMap();
		paramsMap.put("token", createToken(body, tradeNo, amount,userId));
		paramsMap.put("amount", amount.toString());
		paramsMap.put("cf", body);
		paramsMap.put("cf2", tradeNo);
		paramsMap.put("cf3", userId);
//		paramsMap.put("language", "en");
		paramsMap.put("cb_url", acquiropay_notify_url+"acquiro?type=callback");
		//paramsMap.put("ok_url", acquiropay_notify_url+"acquiro?type=success");
		paramsMap.put("ko_url", acquiropay_notify_url+"acquiro?type=fail");
		String paymentUrl = acquiropay_api_url+"?"+ValueUtil.mapToString(paramsMap);
		System.out.println(paymentUrl);
		
		return paymentUrl;
	}
	
	/**
	 * 
	 * @Title:        checkPaymentResult 
	 * @Description:  check payment result
	 * @param:        @param req    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月23日 上午11:41:13/11:41:13 am, August 23, 2017
	 */
	public static void checkPaymentResult(HttpServletRequest req){
		
		//check base info
		if("PURCHASE".equals(req.getParameter("transaction_status"))
				&& acquiropay_merchant_id.equals(req.getParameter("merchant_id"))
						&& acquiropay_product_id.equals(req.getParameter("product_id"))){
			
			String tradeNo = req.getParameter("cf2");
			String payment_id = req.getParameter("payment_id");
			String amountStr = req.getParameter("amount");
			String sign = req.getParameter("sign");
			String status = req.getParameter("status");
			String cf = req.getParameter("cf");			
			String userId = req.getParameter("cf3");		
			//check order info
			if(!StringUtils.isEmpty(tradeNo) && !StringUtils.isEmpty(payment_id)
					&& !StringUtils.isEmpty(amountStr)&& !StringUtils.isEmpty(sign)
					&& !StringUtils.isEmpty(status)&& !StringUtils.isEmpty(cf)
					&& !StringUtils.isEmpty(userId)){
				
				String signStr = acquiropay_merchant_id + payment_id + status + cf + tradeNo + userId + acquiropay_secret_word;
				String signToken = MD5Utils.getMD5(signStr).toLowerCase();
				if(!signToken.equals(sign)){
					System.out.println("checkPaymentResult:check sign fail");
					return;
				}
				
				ITradeDao consumeDao = new TradeDaoImpl();
				TradeVo trade = consumeDao.getTradeInfo(tradeNo);
				if(trade == null){//订单不存在/order inexisting
					return;
				}
				if(trade.getStatus() == 1){
					return;
				}
				BigDecimal amount  = new BigDecimal(amountStr).setScale(2,RoundingMode.HALF_UP);
				if(!trade.getAmount().equals(amount)){//校验订单号和金额 1-2/verified order no and amount
					return;
				}
				trade.setOut_trade_no(payment_id);
				//修改订单状态为成功/update order success
				boolean flag = PayBo.dealPayResult(trade);
			}
			
			
		}
	}
	
	/**
	 * 
	 * @Title:        refundOrder 
	 * @Description:  Cancel (refund) payment order
	 * @param:        @param paymentId
	 * @param:        @param amount    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月23日 下午12:00:14/12:00:14 pm, August 23, 2017
	 */
	public static boolean refundOrder(String cancelId,String paymentId,BigDecimal amount){
		Map<String, Object> paramsMap = initRequestMap();
	
		paramsMap.put("opcode", "1");
		paramsMap.put("payment_id", paymentId);
		paramsMap.put("amount", amount.toString());
		
		String tokenStr = acquiropay_merchant_id+""+paymentId+""+amount+""+acquiropay_secret_word;
		String token = MD5Utils.getMD5(tokenStr).toLowerCase();
		paramsMap.put("token", token);
		//paramsMap.put("language", "en");
		
		String refundUrl = acquiropay_getway_url+"?"+ValueUtil.mapToString(paramsMap);
		System.out.println(refundUrl);
		String response = HttpRequest.sendPost(acquiropay_getway_url,ValueUtil.mapToString(paramsMap));
		if(StringUtils.isEmpty(response)){
			return false;
		}
		try {
			Document document = DocumentHelper.parseText(new String(response.getBytes()));
			Map<String, String> responseMap = new HashMap<String, String>();
			responseXmlToMap(document.getRootElement(), responseMap);
			if("CANCEL".equals(responseMap.get("status")) && !StringUtils.isEmpty(cancelId)){
				//标记已退款，设置退款号/ marked as refunded, set refund no.
				return true;
			}else{
				System.out.println(responseMap.get("description"));
				return false;
			}
			
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(response);
		return false;
	}
	

	
	
	/**
	 * 
	 * @Title:        initRequestMap 
	 * @Description:  init requst map and add public params
	 * @param:        @return    
	 * @return:       Map<String,String>    
	 * @author        Albert
	 * @Date          2017年8月23日 上午10:42:43/10:42:43 pm, August 23, 2017
	 */
	private static Map<String, Object> initRequestMap(){
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("product_id", acquiropay_product_id);
		paramsMap.put("product_name", "Go Bike");
		
		return paramsMap;
	}
	

	
	/**
	 * 
	 * @Title:        responseXmlToMap 
	 * @Description:  add params xml to map  for reponse
	 * @param:        @param node
	 * @param:        @param params    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月23日 下午4:42:51/4:42:51 pm, August 23, 2017
	 */
	public static void responseXmlToMap(Element node, Map<String, String> params) {  
		// 获取当前节点的所有属性节点/get all attributes of the current node
		List<Attribute> list = node.attributes();  
		// 遍历属性节点 /traverse attributes node 
		if ((list == null || list.size() == 0) && !(node.getTextTrim().equals(""))) {  
	
			params.put(node.getName(),node.getTextTrim());  
			System.out.println(node.getName()+"r:"+node.getTextTrim());
		}  
		// 当前节点下面子节点迭代器/the subnode iterator under the current node
		Iterator<Element> it = node.elementIterator();  
		// 遍历 /traverse 
		while (it.hasNext()) {  
			// 获取某个子节点对象 / get a subnode 
			Element e = it.next();  
			// 对子节点进行遍历 traverse the subnode
			responseXmlToMap(e, params);  
		}  
	}  
	
	/**
	 * 
	 * @Title:        createToken 
	 * @Description:  Create reqeust token
	 * @param:        @param body
	 * @param:        @param tradeNo
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月22日 下午6:53:25/6:53:25 pm, August 22, 2017
	 */
	private static String createToken(String body,String tradeNo,BigDecimal amount,String userId){
		String tokenStr = acquiropay_merchant_id+""+acquiropay_product_id+""+amount+""+body+""+tradeNo+""+userId+""+acquiropay_secret_word;
		return MD5Utils.getMD5(tokenStr).toLowerCase();
	}
}
