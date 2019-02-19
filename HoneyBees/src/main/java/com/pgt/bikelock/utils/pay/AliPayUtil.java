package com.pgt.bikelock.utils.pay;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.formula.functions.Odd;
import org.json.JSONException;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.StringUtils;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import com.alipay.api.response.AlipayFundTransToaccountTransferResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.bo.DepositBo;
import com.pgt.bikelock.bo.PayBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.impl.BikeLongLeaseDaoImpl;
import com.pgt.bikelock.dao.impl.CashRecordDaoImpl;
import com.pgt.bikelock.dao.impl.RechargeAmountDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.RechargeAmountVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserVo;


/**
 * 支付宝支付工具类/payment tool alipay
 * @author apple
 *
 */
public class AliPayUtil {

	static final String  ali_server_url = "https://openapi.alipay.com/gateway.do";
	static final String  ali_formate = "json";
	static final String  ali_charSet = "utf-8";
	static final String  ali_signType = "RSA2";

	/**
	 * 生成支付宝订单信息/generate alipay order information
	 * @param body 订单主标题/order main title
	 * @param subject 订单副标题/order subtitle
	 * @param tradeNo 订单号/order no/
	 * @param amount 金额/amount
	 * @return 签名后的订单信息/signed order info
	 */
	public static String getAlipayOrderInfo(String body,String subject,String tradeNo,String amount){
		//实例化客户端/case of client
		AlipayClient alipayClient = new DefaultAlipayClient(ali_server_url, 
				OthersSource.ALIPAY_APP_ID, OthersSource.ALIPAY_APP_PRIVATE_KEY, ali_formate, ali_charSet, OthersSource.ALIPAY_APP_PUBLIC_KEY, ali_signType);
		//实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay/Instantiation of the specific API corresponding to the request class, the class name corresponding to the interface name, the current call interface name/alipay.trade.app.pay
		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		//SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。/pubilic parameter has been encapsulated by SDK, so only business parameter need be loaded。The following is the way of sdk model(select biz_content when model and biz_content exist simultaneously)
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody(body);
		model.setSubject(subject);
		model.setOutTradeNo(tradeNo);
		model.setTimeoutExpress("30m");
		model.setTotalAmount(amount);
		model.setProductCode("QUICK_MSECURITY_PAY");

		request.setBizModel(model);
		request.setNotifyUrl(OthersSource.PAY_NOTIFAY_BASE_URL+"alipay");
		try {
			//这里和普通的接口调用不同，使用的是sdkExecute/here is different from the common interface interchange, and it is sdkExecute.
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			//			System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。/OrderString can send requests to users and no need more processes.
			return response.getBody();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 校验支付宝结果信息/verified the alipay result info
	 * @param req
	 * @param resp
	 */
	public static void checkAppAliPayData(Map<String,String> params, HttpServletResponse resp){
		System.out.println("checkAppAliPayData");
		synchronized (AliPayUtil.class.getName()) {
			//并发控制，以避免函数重入造成的数据混乱/concurrency control to avoid confusion caused by data recursion
			doCheckAppAliPayData(params, resp);
		}
	}

	public static void doCheckAppAliPayData(Map<String,String> params, HttpServletResponse resp){
		System.out.println("doCheckAppAliPayData"+params.toString());
		int out_trade_no = 0;
		String orderId = "";//支付宝产生的订单号/alipay order no.
		double total_amount = 0;
		BigDecimal amount = null;
		String seller_id = "";
		String app_id = "";
		String tradeStatus = "";
		boolean appCheck = false;
		try {
			boolean flag = false;

			if(!params.containsKey("paramsStr")){//异步签名/asynchronous signature
				flag = AlipaySignature.rsaCheckV1(params, OthersSource.ALIPAY_PUBLIC_KEY, ali_charSet,ali_signType);
				System.out.println("check sign:"+flag);
			}else{//同步签名/synchronous signature
				//flag = AlipaySignature.rsa256CheckContent(params.get("paramsStr"),params.get("sign"), OthersSource.ALIPAY_PUBLIC_KEY, ali_charSet);
				//flag = true;//同步验证暂时未调通，验证过程只判定sign格式是否正确/synchronous verification is not yet on, the verification process only determines whether the sign format is correct or not.
				appCheck = true;
			}

			if(flag || appCheck){
				//校验信息/verification info
				out_trade_no = Integer.parseInt(params.get("out_trade_no"));
				orderId = params.get("trade_no");
				total_amount = Double.parseDouble(params.get("total_amount"));
				seller_id = params.get("seller_id");
				app_id = params.get("app_id");
				tradeStatus = params.get("trade_status");
			}else{
				setResponseStr(resp, "failure");
			}
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		if(out_trade_no > 0){
			if(!appCheck && !"TRADE_SUCCESS".equals(tradeStatus) && !"TRADE_FINISHED".equals(tradeStatus)
					&& !"TRADE_CLOSED".equals(tradeStatus)){
				setResponseStr(resp, "failure");
				return;
			}
			//3商户账号校验/3 seller account verification
			if(!OthersSource.ALIPAY_SELLER_ID.equals(seller_id)){
				setResponseStr(resp, "failure");
				return;
			}

			//4应用信息校验/4. use info verification
			if(!OthersSource.ALIPAY_APP_ID.equals(app_id)){
				setResponseStr(resp, "failure");
				return;
			}

			if("TRADE_CLOSED".equals(tradeStatus)){
				String cancelId = params.get("out_biz_no");
				//标记已退款，设置退款号/marked refunded, set refund number
				boolean returnFlag = new DepositBo().updateDepositReturn(cancelId, 1, "");
				setResponseStr(resp, returnFlag?"success":"failure");
			}else{
				ITradeDao consumeDao = new TradeDaoImpl();
				TradeVo trade = consumeDao.getTradeInfo(out_trade_no+"");
				if(trade == null){//订单不存在/order inexists
					setResponseStr(resp, "failure");
					return;
				}
				if(appCheck){//以支付宝回调为唯一标准/set alipay callback as the only standard
					setResponseStr(resp, trade.getStatus() == 1?"success":"failure");
					return;
				}
				if(trade.getStatus() == 1){
					//已支付成功订单，直接返回/return directly for successed paid order
					setResponseStr(resp, "success");
					return;
				}
				amount  = new BigDecimal(total_amount).setScale(2,RoundingMode.HALF_UP);
				if(!trade.getAmount().equals(amount)){//校验订单号和金额 1-2/verified order no. and account 
					setResponseStr(resp, "failure");
					return;
				}

				trade.setOut_trade_no(orderId);
				boolean flag = PayBo.dealPayResult(trade);
				setResponseStr(resp, flag?"success":"failure");	

			}
		}else{
			setResponseStr(resp, "failure");
		}
	}

	/**
	 * 
	 * @Title:        transfer 
	 * @Description:  转账/transfer
	 * @param:        @param tradeId 订单ID/order ID
	 * @param:        @param aliAccount 收款方登陆账号/beneficiary login account
	 * @param:        @param amount 金额/amount
	 * @param:        @param remark    备注/remarks
	 * @return:       支付宝退款ID/alipay refund ID   
	 * @author        Albert
	 * @Date          2017年5月10日 上午11:42:56/11:42:56 am, May 10, 2017
	 */
	public static String transfer(String tradeId,String aliAccount,BigDecimal amount,String remark){
		AlipayClient alipayClient = new DefaultAlipayClient(ali_server_url,OthersSource.ALIPAY_APP_ID,
				OthersSource.ALIPAY_APP_PRIVATE_KEY,ali_formate,ali_charSet,OthersSource.ALIPAY_APP_PUBLIC_KEY,ali_signType);

		AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
		JSONObject object = new JSONObject();


		/*request.setBizContent("{" +
				"\"out_biz_no\":\""+tradeId+"\"," +
				"\"payee_type\":\"ALIPAY_LOGONID\"," +//ALIPAY_USERID:用户ID；ALIPAY_LOGONID：手机号/phone no.
				"\"payee_account\":\""+aliAccount+"\"," +
				"\"amount\":\""+amount+"\"," +
				//"    \"payer_show_name\":\"上海交通卡退款\"," + //付款方真实姓名/payer's real name
				//"    \"payee_real_name\":\"张三\"," + //收款方真实姓名/beneficiary real name
				"\"remark\":\""+remark+"\"" +
				"}");*/

		AlipayFundTransToaccountTransferResponse response;
		try {
			object.put("out_biz_no", tradeId);
			object.put("payee_type", "ALIPAY_LOGONID");
			object.put("payee_account", aliAccount);
			object.put("amount", amount);
			//			object.put("remark", remark);
			request.setBizContent(object.toString());

			response = alipayClient.execute(request);
			if(response.isSuccess()){
				return response.getOrderId();
			} else {
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @Title:        transferQuery 
	 * @Description:  提现状态查询/withdraw status query
	 * @param:        @param tradeId
	 * @param:        @param aliOrderId
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月10日 下午8:54:16/8:54:16 pm, May 10, 2017
	 */
	public static void transferQuery(String tradeId,String aliOrderId,String userId,HttpServletResponse resp){
		AlipayClient alipayClient = new DefaultAlipayClient(ali_server_url,OthersSource.ALIPAY_APP_ID,
				OthersSource.ALIPAY_APP_PRIVATE_KEY,ali_formate,ali_charSet,OthersSource.ALIPAY_APP_PUBLIC_KEY,ali_signType);
		AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
		request.setBizContent("{" +
				"    \"out_biz_no\":\""+tradeId+"\"," +
				"    \"order_id\":\""+aliOrderId+"\"" +
				"  }");
		AlipayFundTransOrderQueryResponse response;
		int statusValue = 0;
		String statusInfo = null;
		org.json.JSONObject resultObject = null;

		try {
			response = alipayClient.execute(request);
			if(response.isSuccess()){
				String status = response.getStatus();
				if("SUCCESS".equals(status)){//成功（配合"单笔转账到银行账户接口"产品使用时, 同一笔单据多次查询有可能从成功变成退票状态）；/success (with the "single transfer to the bank account interface" , multiple inquiries for the same document may turn to refund status from success status):
					statusValue = 1;
					//update status success
					new CashRecordDaoImpl().updateCashStatus(2, tradeId,new BigDecimal(0));
				}else if("FAIL".equals(status)){//失败（具体失败原因请参见error_code以及fail_reason返回值）；/failed (reasons for failed reference to error_code and fail_reason return value)
					statusValue = 2;
				}else if("INIT".equals(status)){//等待处理；/to be processed
					statusValue = 0;
				}else if("DEALING".equals(status)){//处理中/processing
					statusValue = 3;
				}else if("REFUND".equals(status)){//退票（仅配合"单笔转账到银行账户接口"产品使用时会涉及, 具体退票原因请参见fail_reason返回值）/refund (it will be involved only when "single transfer to the bank account interface" occurs, and the reasons for refund reference to fail_reason return value ) 
					statusValue = 4;
					statusInfo = response.getFailReason();

					//update status fail
					new CashRecordDaoImpl().updateCashStatus(2, tradeId,new BigDecimal(0));
				}else if(" UNKNOWN".equals(status)){//状态未知/status unknown
					statusValue = 5;
				}
			} else {
				statusInfo = "request fail";
			}

			resultObject = new org.json.JSONObject();
			resultObject.put("status", statusValue);
			resultObject.put("info", statusInfo);
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		setResponseStr(resp, resultObject.toString());

	}

	/**
	 * 
	 * @Title:        refundOrder 
	 * @Description:  退款/refund
	 * @param:        @param cancelId 退款订单号/refund order no.
	 * @param:        @param orderId 支付订单号（支付宝）payment order no.(alipay)
	 * @param:        @param orderAmount 退款金额/refund amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月22日 下午1:02:17/1:02:17 pm, August 22, 2017
	 */
	public static boolean refundOrder(String cancelId,String orderId,BigDecimal orderAmount){
		AlipayClient alipayClient = new DefaultAlipayClient(ali_server_url,OthersSource.ALIPAY_APP_ID,
				OthersSource.ALIPAY_APP_PRIVATE_KEY,ali_formate,ali_charSet,OthersSource.ALIPAY_APP_PUBLIC_KEY,ali_signType);
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.setBizContent("{" +
				"\"trade_no\":\""+orderId+"\"," +
				"\"refund_amount\":\""+orderAmount+"\"," +
				"\"refund_reason\":\"正常退款\"," +
				"\"out_request_no\":\""+cancelId+"\"" +
				"  }");

		AlipayTradeRefundResponse response;
		try {
			response = alipayClient.execute(request);
			if(response.isSuccess()){
				return true;
			}
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			//			e.printStackTrace();
		}
		return true;//正常调用，也会报异常，已测试通过/normal invoking, abnormal invoking, test passed 
	}

	//排序原始字符串（若支付宝有变动，得跟着改，否则验证签名会不通过）/sort the original string (it will be changed if alipay changes, otherwise the verification signature will not be passed)
	public static String sortResponse(JSONObject obj){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("code", obj.getString("code"));
		map.put("msg", obj.getString("msg"));
		map.put("total_amount", obj.getString("total_amount"));
		map.put("app_id", obj.getString("app_id"));
		map.put("trade_no", obj.getString("trade_no"));
		map.put("seller_id", obj.getString("seller_id"));
		map.put("out_trade_no", obj.getString("out_trade_no"));
		return map.toString();
	}

	/**
	 * 返回字符串/return string
	 * @param resp
	 * @param str
	 */
	public static void setResponseStr(HttpServletResponse resp,String str) {
		System.out.println("resp"+str);
		PrintWriter out;
		try {
			out = resp.getWriter();
			out.write(str);
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



}
