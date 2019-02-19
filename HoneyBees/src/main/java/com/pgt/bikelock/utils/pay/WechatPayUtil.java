package com.pgt.bikelock.utils.pay;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyStore;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
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
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserVo;



/**
 * 微信支付工具类/wechat payment
 * @author apple
 *	时间/time
 */
public class WechatPayUtil {

	/**
	 * 获取订单信息/get order info
	 * @param body
	 * @param tradeNo
	 * @param amount
	 * @param userIp
	 * @param resp
	 */
	public static void getWechatPayOrderInfo(String body,String tradeNo,double amount,
			String userIp,HttpServletResponse resp,int type,String openid){
		
		Map<String,Object> reqeustParams = new HashMap<String,Object>();  

		reqeustParams.put("appid",type == 0?OthersSource.WECHATPAY_APP_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_APP_ID);  //上面的appid，注意大小写/note the capital and small letter for the appid

		reqeustParams.put("mch_id",type == 0?OthersSource.WECHATPAY_MCH_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_MCH_ID); //商户id/seller ID

		if(type == 1){
			reqeustParams.put("openid",openid);
		}
		
		reqeustParams.put("nonce_str",getRandomNumber(32));  //32位随机数/32-bit random number 

		reqeustParams.put("body",body); //商品描述/commodity description

		reqeustParams.put("out_trade_no",tradeNo); //应用后台生成的订单id/generated order ID of the application admin  

		reqeustParams.put("total_fee",(int)(amount*100)); //总金额/total amount  

		reqeustParams.put("spbill_create_ip",userIp); //用户终端ip/end user IP 

		reqeustParams.put("notify_url",OthersSource.PAY_NOTIFAY_BASE_URL+"wechatpay?type="+type); //异步通知URL/asynchronous notification URL  

		reqeustParams.put("trade_type",type == 0?"APP":"JSAPI"); //交易方式，参见微信接口文档/transaction type, reference to the wechat interface documents 

		String requstContent = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", 
				getXmlFromParamsMap(reqeustParams,true,type));
		Map<String, String> responseParams;
		try {
			responseParams = getParamsMapFromXml(requstContent);
			if(responseParams.containsKey("sign")&& responseParams.get("prepay_id") != null &&  
					!"".equals(responseParams.get("prepay_id"))&& !"null".equals(responseParams.get("prepay_id"))){  
				if(checkSign(responseParams,type)){//签名认证成功/success for signature verification
					//封装响应终端数据/ encapsulation response end data
					Map<String,Object> responseAppParams = new HashMap<String,Object>();
					String prepayId = "";
					for(Map.Entry<String, String> param : responseParams.entrySet()) {  
						if("appid".equals(param.getKey())){
							responseAppParams.put(type==0?"appid":"appId", param.getValue());
						}else if(type == 0 && "mch_id".equals(param.getKey())) { 
							responseAppParams.put("partnerid", param.getValue());
						}else if("prepay_id".equals(param.getKey())) { 
							prepayId = param.getValue();
							if(type == 0){
								responseAppParams.put("prepayid", prepayId);
							}							
						}else{  
							continue;  
						}
					}  
			
					responseAppParams.put(type==0?"noncestr":"nonceStr", getRandomNumber(32));
					if(type == 0){
						responseAppParams.put("package", "Sign=WXPay");
					
					}else{
						responseAppParams.put("package", "prepay_id="+prepayId);
						responseAppParams.put("signType", "MD5");
					}
					
					responseAppParams.put(type==0?"timestamp":"timeStamp", TimeUtil.getCurrentLongTime());
					//订单号，该参数仅返回给终端用户支付成功后与服务器校验，不参与加密运算/order no. This parameter is only verified with the server when returns the end user successful payment, not for the encryption operation
					responseAppParams.put("tradeNo", tradeNo);
					//返回数据/return data
					String resultStr = getXmlFromParamsMap(responseAppParams,true,type);
					PrintWriter out;
					try {
						out = resp.getWriter();
						out.write(resultStr);
						out.flush();
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}  
			}else{  
				System.out.println("响应签名认证失败/verified failed for response signature:"+new String(responseParams.get("return_msg")));
				responseStatus(resp,false,"签名失败/signature failed",type);
			}  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  

	}

	/**
	 * 主动查询订单支付状态/initiative check the order payment status
	 * @param tradeNo
	 * @param resp
	 */
	public static void orderQuery(String tradeNo,HttpServletResponse resp,int type){

		Map<String,Object> reqeustParams = new HashMap<String,Object>();  

		reqeustParams.put("appid",type == 0?OthersSource.WECHATPAY_APP_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_APP_ID);  //上面的appid，注意大小写/note the capital and small letter for the appid  

		reqeustParams.put("mch_id",type == 0?OthersSource.WECHATPAY_MCH_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_MCH_ID); //商户id/seller ID

		reqeustParams.put("nonce_str",getRandomNumber(32));  //32位随机数/32-bit random number

		reqeustParams.put("out_trade_no",tradeNo); //应用后台生成的订单id/generate the order ID by application admin 


		String requstContent = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/orderquery", 
				getXmlFromParamsMap(reqeustParams,true,type));
		checkWechatPayData(requstContent, resp,false,type);
	}
	/**
	 * 校验微信结果信息/verify the wechat result information
	 * @param req
	 * @param resp
	 */
	public static void checkWechatPayData(String paramsStr, HttpServletResponse resp,boolean isNotify,int type){
		synchronized (WechatPayUtil.class.getName()) {
			//并发控制，以避免函数重入造成的数据混乱/concurrency control to avoid confusion caused by data recursion
			doCheckWechatPayData(paramsStr, resp,isNotify,type);
		}
	}
	public static void doCheckWechatPayData(String paramsStr, HttpServletResponse resp,boolean isNotify,int type){
		System.out.println("doCheckWechatPayData"+paramsStr);
		Map<String, String> params = null;
		try {
			params = getParamsMapFromXml(paramsStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseStatus(resp,false,"参数格式校验错误/verification error of the parameter format",type);
			return;
		}  
		int out_trade_no = 0;
		BigDecimal amount = null;
		String seller_id = "";
		String app_id = "";
		String orderId = "";//微信产生的订单号/generated order ID by wechat
		try {
			if("SUCCESS".equals(params.get("return_code"))){//通讯状态/communication status
				if(params.containsKey("sign") && checkSign(params,type)){  //验证签名/verify signature
					if("SUCCESS".equals(params.get("result_code"))){//验证结果状态/verify result status
						if(!isNotify){//主动校验，需判定状态/initiative verified, judge the status
							if("SUCCESS".equals(params.get("trade_state"))){//交易状态/transaction status
								//校验信息/verified information
								out_trade_no = Integer.parseInt(params.get("out_trade_no"));
								orderId = params.get("transaction_id");
								amount = new BigDecimal(params.get("total_fee"));
								seller_id = params.get("mch_id");
								app_id = params.get("appid");
							}else{
								responseStatus(resp,false,"支付失败,"+params.get("trade_state_desc"),type);
							}
						}else{//通知，无需判定状态/notification, no need to judge the status
							//校验信息
							out_trade_no = Integer.parseInt(params.get("out_trade_no"));
							orderId = params.get("transaction_id");
							amount = new BigDecimal(params.get("total_fee"));
							seller_id = params.get("mch_id");
							app_id = params.get("appid");
						}

					}else{
						responseStatus(resp,false,"验证支付结果失败/verified payment result failed"+params.get("err_code_des"),type);
					}
				}else{
					responseStatus(resp,false,"签名失败/signature failed",type);
				}

			}else{//通讯失败/communication failed
				responseStatus(resp,false,"验证支付结果失败/verified payment result failed"+params.get("return_msg"),type);
			}

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(out_trade_no > 0){
			//为了安全起见，以下校验步骤参考支付宝，微信并无推荐此流程/for security, please refer to the alipay for verification steps, no recommended process for wechat
			ITradeDao consumeDao = new TradeDaoImpl();
			TradeVo trade = consumeDao.getTradeInfo(out_trade_no+"");
			if(trade == null){//订单不存在/order inexists
				responseStatus(resp,false,"订单信息不存在/order info inexist",type);
				return;
			}
			if(trade.getStatus() == 1){
				//已支付成功订单，直接返回/return directly for successful paid order
				responseStatus(resp,true,null,type);
				return;
			}
			if(!trade.getAmount().multiply(new BigDecimal(100)).setScale(0,RoundingMode.HALF_UP).equals(amount)){//校验订单号和金额 1-2/verified order no. and amount
				responseStatus(resp,false,"参数格式校验错误/verified error for parameter format",type);
				return;
			}
			//3商户账号校验/3 seller account verified
			if(!seller_id.equals(type == 0?OthersSource.WECHATPAY_MCH_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_MCH_ID)){
				responseStatus(resp,false,"参数格式校验错误/verified error for parameter format",type);
				return;
			}
			
			//4应用信息校验/4 application info verified
			if(!app_id.equals(type == 0?OthersSource.WECHATPAY_APP_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_APP_ID)){
				responseStatus(resp,false,"参数格式校验错误/verified error for parameter format",type);
				return;
			}

			trade.setOut_trade_no(orderId);
			boolean flag = PayBo.dealPayResult(trade);
			responseStatus(resp,flag,null,type);
		}
	}

	/**
	 * 
	 * @Title:        refundOrder 
	 * @Description:  退款申请（一年内的订单）/refund application (order within 1 year)
	 * @param:        @param cancelId 商户退款号/refund no. of seller
	 * @param:        @param orderId 订单号/order ID
	 * @param:        @param orderAmount 订单总金额/order amount
	 * @param:        @param cancelAmount 退款总金额/refund amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月22日 上午11:35:12/11:35:12 am, August 22, 2017
	 */
	public static boolean refundOrder(String cancelId,String orderId,double orderAmount,
			double cancelAmount,String outReFundId,int type){
		System.out.println("refundOrder");
		try {

			/** 
			 * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的/note the certification PKCS12 download from wechat platform-》account set -》API security
			 */  
			String mchId = type == 0?OthersSource.WECHATPAY_MCH_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_MCH_ID;
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");  
			String certFile = OthersSource.getSourceString("wechatpay_cert_file");
			if(type == 1){
				certFile = OthersSource.getSourceString("wechatpay_small_program_cert_file");
			}
			System.out.println("refundOrder type:"+type+";certFile:"+certFile);
			FileInputStream instream = new FileInputStream(new File(certFile));//P12文件目录/P12 file directory 
			try {   
				
				keyStore.load(instream, mchId.toCharArray());//默认MCHID/default MCHID  
			} finally {  
				instream.close();  
			}  

			// Trust own CA and all self-signed certs  

			SSLContext sslcontext = SSLContexts.custom()  
					.loadKeyMaterial(keyStore, mchId.toCharArray())//   
					.build();  
			// Allow TLSv1 protocol only  
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(  
					sslcontext,  
					new String[] { "TLSv1" },  
					null,  
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);  
			CloseableHttpClient httpclient = HttpClients.custom()  
					.setSSLSocketFactory(sslsf)  
					.build();  


			Map<String,Object> reqeustParams = new HashMap<String,Object>();  

			reqeustParams.put("appid",type == 0?OthersSource.WECHATPAY_APP_ID:OthersSource.WECHATPAY_SMALL_PROGRAM_APP_ID);  //上面的appid，注意大小写/note capital and small letter for appid 

			reqeustParams.put("mch_id",mchId); //商户id/seller ID 

			reqeustParams.put("nonce_str",getRandomNumber(32));  //32位随机数/32-bit random number  

			reqeustParams.put("transaction_id",orderId); //微信订单ID/wechat order ID  

			reqeustParams.put("out_refund_no",cancelId); //退款订单ID/refund order ID

			reqeustParams.put("total_fee",(int)(orderAmount*100)); //订单金额/order amount  

			reqeustParams.put("refund_fee",(int)(cancelAmount*100)); //退款金额/refund amount


			HttpPost httpost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund"); // 设置响应头信息/set the response header info  
			httpost.addHeader("Connection", "keep-alive");  
			httpost.addHeader("Accept", "*/*");  
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  
			httpost.addHeader("Host", "api.mch.weixin.qq.com");  
			httpost.addHeader("X-Requested-With", "XMLHttpRequest");  
			httpost.addHeader("Cache-Control", "max-age=0");  
			httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");  
			httpost.setEntity(new StringEntity(getXmlFromParamsMap(reqeustParams,true,type), "UTF-8"));  
			CloseableHttpResponse response = httpclient.execute(httpost);  
			HttpEntity entity = response.getEntity();  

			String requstContent = EntityUtils.toString(entity, "UTF-8");  

			Map<String, String> responseParams;

			if(StringUtils.isEmpty(requstContent)){
				System.out.println("wechat refund requst null");
				return false;

			}
			responseParams = getParamsMapFromXml(requstContent);
			if("SUCCESS".equals(responseParams.get("return_code"))){
				if(responseParams.containsKey("sign")&& responseParams.get("refund_id") != null &&  
						!"".equals(responseParams.get("refund_id"))&& !"null".equals(responseParams.get("refund_id"))){  
					if(checkSign(responseParams,type)){//签名认证成功/signature verified success
						String out_refund_no = responseParams.get("out_refund_no");
						outReFundId = responseParams.get("refund_id");
						if(cancelId.equals(out_refund_no)){
							//标记已退款，设置退款号/marked with refunded, and set refund no.
							return  true;
						}else{
							System.out.println("out_refund_no error");
						}

					}else{
						System.out.println("sign error");
					}
				}else{
					System.out.println("params error");
				}

			}else{
				System.out.println("refund error");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return false;
	}

	/**
	 * 响应状态/response status
	 * @param resp
	 * @param success
	 * @param msg
	 */
	public static void responseStatus(HttpServletResponse resp,boolean success,String msg,int type){

		Map<String,Object> returnParams = new HashMap<String,Object>();  

		returnParams.put("return_code",success?"SUCCESS":"FAIL");  //状态码/status code
		if(!success){
			returnParams.put("return_msg",success?"OK":msg); //错误信息/error info
		}

		String resultStr = getXmlFromParamsMap(returnParams,false,type);

		PrintWriter out;
		try {
			out = resp.getWriter();
			out.write(resultStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/** 
	 * 将微信支付所需参数拼接为xml字符串/the required parameter for wechat payment are concatenated into xml strings 
	 * @param <T> 
	 *  
	 * @param paramsMap 
	 * @param addSign 是否生成签名信息/generated signature info or not
	 * @return  
	 * @throws Exception 
	 */  
	public static <T> String getXmlFromParamsMap(Map<String, T> paramsMap,boolean addSign,int type){  
		if (paramsMap != null && paramsMap.size() > 0) {  
			Map<String, Object> params = new TreeMap<String, Object>(new Comparator<String>() {  
				public int compare(String s1, String s2) {  
					return s1.compareTo(s2);  
				}  
			});  
			params.putAll(paramsMap);  
			StringBuffer ss = new StringBuffer("<xml>");  
			for (Entry<String, Object> param : params.entrySet()) {  
				if (!"key".equals(param.getKey())) {  
					ss.append("<" + param.getKey() + "><![CDATA[").append(param.getValue())  
					.append("]]></" + param.getKey() + ">");  
				}  
			}
			if(addSign){
				String sign = getSignFromParamMap(params,type);  
				ss.append("<sign>" + sign + "</sign>");  
			}
			ss.append("</xml>");  
			String xmlString = ss.toString();
			System.out.println("xml:"+xmlString);
			return  xmlString;
		}  
		return null;  
	}  
	/** 
	 * 从xml字符串中解析参数/resolve the parameters from the xml string
	 * @param xml 
	 * @return 
	 * @throws Exception 
	 */  
	public static Map<String, String> getParamsMapFromXml(String xml) throws Exception {  
		System.out.println("getParamsMapFromXml："+xml);
		if(StringUtils.isEmpty(xml)){
			return null;
		}
		Map<String, String> params = new HashMap<String, String>(0);  
		Document read = DocumentHelper.parseText(new String(xml.getBytes()));
		Element node = read.getRootElement();  
		listNodes(node, params);  
		
		return params;  
	} 


	public static void listNodes(Element node, Map<String, String> params) {  
		// 获取当前节点的所有属性节点/get all the attributes nodes of the current nodes 
		List<Attribute> list = node.attributes();  
		// 遍历属性节点/traverse the attribute node  
		if ((list == null || list.size() == 0) && !(node.getTextTrim().equals(""))) {  
			if(node.getTextTrim().contains("<![CDATA[")){  
				String[] split = node.getTextTrim().split("<![CDATA[");  
				split[1].replaceAll("]]>", "");  
				params.put(node.getName(), split[1]);
			}else{  
				params.put(node.getName(),node.getTextTrim());  
			}  
		}  
		// 当前节点下面子节点迭代器/subnode iterator under the current node 
		Iterator<Element> it = node.elementIterator();  
		// 遍历/traverse  
		while (it.hasNext()) {  
			// 获取某个子节点对象/get a subnode  
			Element e = it.next();  
			// 对子节点进行遍历/traverse a subnode  
			listNodes(e, params);  
		}  
	}  

	/** 
	 * 生成count位的随机数/generate count-bit random digit 
	 *  
	 * @param count 
	 * @return 
	 */  
	public static String getRandomNumber(int count) {  
		StringBuffer ss = new StringBuffer(count);  
		for (int i = 0; i < count; i++) {  
			int a = (int) (Math.random() * 10);  
			ss.append(a);  
		}  
		return ss.toString();  
	}  


	/** 
	 * 从map中获取签名sign/get signature from map 
	 * @param paramsMap 
	 * @return 
	 * @throws Exception 
	 */  
	public static <T> String getSignFromParamMap(Map<String, T> paramsMap,int type){  
		try {
			if (paramsMap != null && paramsMap.size() > 0) {
				Map<String, T> params = new TreeMap<String, T>(
						new Comparator<String>() {
							public int compare(String s1, String s2) {
								return s1.compareTo(s2);
							}

						});
				params.putAll(paramsMap);
				StringBuffer tempStr = new StringBuffer();
				for (Entry<String, T> param : params.entrySet()) {
					if (!"sign".equals(param.getKey())
							&& !"key".equals(param.getKey())
							&& !"".equals(param.getValue())
							&& param.getValue() != null
							&& !"tradeNo".equals(param.getKey())
							) {
						
							tempStr.append(param.getKey() + "=" + param.getValue()
									+ "&");
						
						
					}
				}
			
				String temp = tempStr.toString().concat(
						"key=" + (type == 0?OthersSource.WECHATPAY_MCH_KEY:OthersSource.WECHATPAY_SMALL_PROGRAM_MCH_KEY));
				System.out.println("temp:"+temp);
				return MD5Utils.getMD5(temp).toUpperCase();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;  
	}  

	/** 
	 * 签名认证/signature verification 
	 * @param paramsMap 
	 * @return 
	 * @throws Exception 
	 */  
	public static <T> boolean checkSign(Map<String, T> paramsMap,int type) throws Exception {  
		String sign = getSignFromParamMap(paramsMap,type);  
		return paramsMap.get("sign").equals(sign);  
	}  

	public static void main(String[] args) {
		System.out.println(MD5Utils.getMD5("RHYLAPPOMKJFORRH2017"));
	}
}
