package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.authorize.api.contract.v1.CustomerPaymentProfileListItemType;
import net.authorize.api.contract.v1.CustomerPaymentProfileMaskedType;
import net.authorize.api.contract.v1.GetCustomerPaymentProfileResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.braintreegateway.Result;
import com.google.gson.JsonObject;
import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.bo.DepositBo;
import com.pgt.bikelock.bo.PayBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.IBikeLongLeaseDao;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.IUserDetailDao;
import com.pgt.bikelock.dao.impl.BikeLongLeaseDaoImpl;
import com.pgt.bikelock.dao.impl.BikeTypeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.RechargeAmountDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.pay.AcquiroPayUtil;
import com.pgt.bikelock.utils.pay.AliPayUtil;
import com.pgt.bikelock.utils.pay.AnetPayUtil;
import com.pgt.bikelock.utils.pay.PayPalUtil;
import com.pgt.bikelock.utils.pay.PayuUtil;
import com.pgt.bikelock.utils.pay.StripPayUtil;
import com.pgt.bikelock.utils.pay.WechatPayUtil;
import com.pgt.bikelock.vo.BikeLongLeaseVo;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.RechargeAmountVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserVo;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

/**
 *  接口定义起点:40000/
 *  访问前缀 app/pay
 * @ClassName:     PayServlet
 * @Description:支付相关业务接口/payment related business interface
 * @author:    Albert
 * @date:        2017-3-24 上午10:19:49
 *
 */
public class PayServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	/**状态码 status code**/
	public static final int HTTP_PAY_ERROR = 40001;//支付异常/abnormal payment
	public static final int HTTP_PAY_ACCOUNT_NOT_ENOUGH_ERROR = 40002;//余额不足/balance not less
	public static final int HTTP_PAY_NO_DEFAULT_PAYMENT = 40003;//无默认银行卡信息/no default bank card information
	public static final int HTTP_PAY_PAYMENT = 40003;//无默认银行卡信息/no default bank card information
	public static final int HTTP_ADD_PAYMENT_ERROR = 40004;//添加银行卡失败/add card fail

	private int payType ; //支付方式 1:账户余额 2：微信 3：支付宝 4:PayPay 5:VISA 6:Strip 7-9:系统支付  10:Anet 11:AcquiroPay 12:PayU/payment way 1:account balance 2:wechat 3:alipay 4:paypay 5"visa 6:strip 7-9:system pay 
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		super.doGet(req, resp);
		switch (getRequestType(req)) {
		case 40007:
			getDeposit(req, resp);
			break;
		case 40008:
			getRechargeAmount(req, resp);
			break;
		case 40009:
			getPayMentList(req, resp);
			break;
		case 40011:
			getPayPalToken(req, resp);
			break;
		case 40013:
			loadPayU(req, resp);
			break;
		default:
			break;
		}
		doPost(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		super.doPost(req, resp);

		switch (getRequestType(req)) {
		case 40001:
			payForDeposit(req, resp);
			break;
		case 40002:
			payForRecharge(req, resp);
			break;
		case 40003:
			payForBickUse(req, resp);
			break;
		case 40004:
			payForBickLongLease(req, resp);
			break;
		case 40005:
			checkAppAliPayResult(req, resp);
			break;
		case 40006:
			checkAppPayResult(req, resp);
			break;
		case 40010:
			addPayMent(req, resp);
			break;
		case 40012:
			updatePaymentDefault(req, resp);
			break;
		case 40014:
			deletePaymentCard(req, resp);
			break;
		default:
			break;
		}
	}



	/**
	 * 40001
	 * post
	 * @Title:        payForDeposit 
	 * @Description:  押金支付/deposit payment
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 上午10:08:14
	 */
	private void payForDeposit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] parms = new String[]{"payType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		IndustryVo industry = new IndustryDaoImpl().getIndustryInfo(getIndustryId(req));
		if(industry == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}

		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req),true);

		if(userVo.getAuthStatus() == industry.getRegister_auth_num()){
			//已成功支付押金，返回支付成功（解决终端未能正常请求支付结果，再次请求时判断状态直接返回）/already paid deposit, return payment success(solve final not work normal request payment result, request again judge status direct return)
			setFlagData(resp, true);
			return;
		}


		if(!getPayType(req, resp, parms)){
			return;
		}

		ITradeDao tradeDao = new TradeDaoImpl();
		BigDecimal depositAmount = new DepositBo().getDepositAmount(getCityId(req));
		//添加支付订单/add payment order
		String tradeId = tradeDao.addTrade(new TradeVo(getUserId(req),3,payType,depositAmount));
		//调起支付/payment
		MessageFormat subjectFmt = new MessageFormat(LanguageUtil.getDefaultValue("pay_deposit_subject"));
		String out_trade_no = doPay(req, resp, depositAmount, LanguageUtil.getDefaultValue("pay_deposit_body"), 
				subjectFmt.format(new Object[]{OthersSource.getSourceString("project_name")}),tradeId);
		if(out_trade_no != null){
			//修改订单状态成功/modify order status success
			boolean flag = tradeDao.updateTradeSuccess(tradeId,out_trade_no);
			System.out.println("trade status flag="+flag);
			//修改用户押金缴付状态为成功/modify user deposit paid status as success
			flag = new DepositBo().depositAuth(userVo);
			System.out.println("use deposit status flag="+flag);
			setFlagData(resp, flag);

		}


	}

	/**
	 * 40002
	 * @Title:        payForRecharge 
	 * @Description:  充值支付/recharge payment
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 上午10:30:41
	 */
	private void payForRecharge(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] parms = new String[]{"payType","amount"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		if(!getPayType(req, resp, parms)){
			return;
		}

		ITradeDao tradeDao = new TradeDaoImpl();
		BigDecimal amount = new BigDecimal(req.getParameter(parms[1]));
		//添加充值订单/add recharge payment
		TradeVo rechargeTrade = new TradeVo(getUserId(req),2,payType,amount);
		String amountId = req.getParameter("amountId");
		if(!StringUtils.isEmpty(amountId)){
			//设置充值记录对应金额ID/set up recharge record correspond amount id
			rechargeTrade.setRecordId(amountId);
		}

		String tradeId = tradeDao.addTrade(rechargeTrade);
		//调起支付/payment
		MessageFormat subjectFmt = new MessageFormat(LanguageUtil.getDefaultValue("pay_recharge_subject"));
		String out_trade_no = doPay(req, resp, amount, LanguageUtil.getDefaultValue("pay_recharge_body"), 
				subjectFmt.format(new Object[]{OthersSource.getSourceString("project_name")}), tradeId);
		if(out_trade_no != null){
			//修改订单状态成功/modify order status success
			boolean flag = tradeDao.updateTradeSuccess(tradeId,out_trade_no);
			flag = UserBo.rechargeForUser(getUserId(req), amount,amountId);
			setFlagData(resp, flag);

		}


	}
	/**
	 * 40003
	 * @Title:        payForBickUse 
	 * @Description:  骑行订单支付/riding order payment
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月1日 下午6:15:15
	 */
	private void payForBickUse(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] parms = new String[]{"payType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		if(!getPayType(req, resp, parms)){
			return;
		}
		ITradeDao tradeDao = new TradeDaoImpl();
		TradeVo tradeVo = tradeDao.getNoPayTrade(getUserId(req));
		if(tradeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		tradeDao.updatePayWay(tradeVo.getId(), payType);
		MessageFormat subjectFmt = new MessageFormat(LanguageUtil.getDefaultValue("pay_bike_use_subject"));
		BigDecimal amount = tradeVo.getAmount();
		ValueUtil.getAmount(amount);
		
		String out_trade_no = doPay(req, resp, amount, LanguageUtil.getDefaultValue("pay_bike_use_body"), 
				subjectFmt.format(new Object[]{OthersSource.getSourceString("project_name")}), tradeVo.getId());
		if(out_trade_no != null){
			//修改订单状态成功/modify order status success
			boolean flag = tradeDao.updateTradeSuccess(tradeVo.getId(),out_trade_no);
			if(flag){
				//update ride history pay success
				new BikeUseDaoImpl().updatePaySuccess(tradeVo.getRecordId());
			}
			setFlagData(resp, flag);
		}
	}

	/**
	 * 40004
	 * @Title:        payForBickLongLease 
	 * @Description:  单车长租支付/bike long time rent payment
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 上午10:48:02
	 */
	private void payForBickLongLease(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] parms = new String[]{"payType","typeId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		if(!getPayType(req, resp, parms)){
			return;
		}
		BikeTypeVo typeVo = new BikeTypeDaoImpl().getTypeInfo(req.getParameter(parms[1]));

		if(typeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}
		IBikeLongLeaseDao leaseDao = new BikeLongLeaseDaoImpl();
		//增加长租记录/add long time rent
		BikeLongLeaseVo leaseVo = new BikeLongLeaseVo(getUserId(req));
		leaseVo.setEnd_time(typeVo.getEndTime(Long.parseLong(leaseVo.getStart_time())));
		String recordId = leaseDao.addLease(leaseVo);
		if(recordId == null){
			setCode(resp, ERROR_EXCEPTION);
			return;
		}
		ITradeDao tradeDao = new TradeDaoImpl();

		TradeVo tradeVo = new TradeVo(getUserId(req),4,payType,typeVo.getPrice());
		tradeVo.setRecordId(recordId);
		String orderId = tradeDao.addTrade(tradeVo);
		if(orderId == null){
			setCode(resp, ERROR_EXCEPTION);
			return;
		}
		MessageFormat subjectFmt = new MessageFormat(LanguageUtil.getDefaultValue("pay_bike_longlease_subject"));
		String out_trade_no = doPay(req, resp, tradeVo.getAmount(), LanguageUtil.getDefaultValue("pay_bike_longlease_body"), 
				subjectFmt.format(new Object[]{OthersSource.getSourceString("project_name")}), orderId);
		if(out_trade_no != null){
			//修改订单状态成功/modify order status success
			boolean flag = tradeDao.updateTradeSuccess(orderId,out_trade_no);
			//修改长租记录为已支付/repair long time rent reord as already paid
			flag = leaseDao.updatePaySuccess(recordId);
			setFlagData(resp, flag);
		}
	}

	/**
	 * 
	 * @Title:        doPay 
	 * @Description:  执行支付/carry out payment
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param amount
	 * @param:        @param body
	 * @param:        @param subject
	 * @param:        @param tradeNo
	 * @param:        @return
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:        第三方订单ID/信息/third party order id      
	 * @author        Albert
	 * @Date          2017年5月4日 上午10:10:18
	 */
	private String doPay(HttpServletRequest req, HttpServletResponse resp,BigDecimal amount,
			String body,String subject,String tradeNo)
					throws ServletException, IOException {
		String orderId = null;
		String orderInfo = null;
		String currency = PayBo.getCurrency(getIndustryId(req), getCityId(req));
		String openId = "";
		switch (payType) {
		case TradeVo.Trade_PayWay_Wechat:
			WechatPayUtil.getWechatPayOrderInfo(body, tradeNo,Double.parseDouble(amount.toString()) , 
					req.getRemoteAddr(), resp,0,null);
			break;
		case 1000:
			openId = new UserDaoImpl().getThirdUUID(getUserId(req));
			if(StringUtils.isEmpty(openId)){
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return null;
			}
			WechatPayUtil.getWechatPayOrderInfo(body, tradeNo,Double.parseDouble(amount.toString()) , 
					req.getRemoteAddr(), resp,1,openId);
			break;
		case TradeVo.Trade_PayWay_Alipay:
			orderInfo = AliPayUtil.getAlipayOrderInfo(body, subject, tradeNo, amount+"");
			if(orderInfo != null){
				setData(resp, orderInfo);
			}else{
				//签名信息失败/signature information failure
				setCode(resp, ERROR_EXCEPTION);
			}
			break;
		case TradeVo.Trade_PayWay_PayPal:
			String[] parms = new String[]{"paymentMethodNonce","firstName","lastName"};
			if(!checkRequstParams(req, resp, parms)){
				return null;
			}
			UserDetailVo detailVo = new UserDetailDaoImpl().getUserDetail(getUserId(req));
			Result<com.braintreegateway.Transaction> result = PayPalUtil.createTransaction(req, resp, amount, currency, tradeNo, detailVo);
			if (result.isSuccess()) {
				com.braintreegateway.Transaction transaction = result.getTarget();
				//				System.out.println("Success ID: " 
				//				+ transaction.getPayPalDetails().getAuthorizationId()+";id:"+transaction.getId()+";orderId:"+transaction.getOrderId());
				orderId = transaction.getId();
			} else {
				System.out.println("Message: " + result.getMessage());
				setErrorCode(resp, HTTP_PAY_ERROR, result.getMessage());
			}
			break;
		case TradeVo.Trade_PayWay_Stripe:
			Charge charge = null;
			String token = "";
			if(!StringUtils.isEmpty(req.getParameter("tokenId"))){
				token = req.getParameter("tokenId").toString();
			}
			if(!StripPayUtil.checkPayment(getUserId(req))){
				setCode(resp, HTTP_PAY_NO_DEFAULT_PAYMENT);
				return null;
			}
			if("1".equals(req.getParameter("quickPay"))){
				//快捷支付/quickly payment
				charge = StripPayUtil.createUserAndPay(token, amount, currency, body, getUserId(req));
			}else{
				charge = StripPayUtil.stripBasePay(token, 
						amount,currency,body);
			}
			if(charge == null){
				
				System.out.println("errorMsg:"+StripPayUtil.error_msg);
				setErrorCode(resp, HTTP_PAY_ERROR, StripPayUtil.error_msg);						
				
				return null;
			}else if(charge != null && !StringUtils.isEmpty(charge.getFailureMessage())){//支付失败，返回失败信息/payment failure, return failure information
				setErrorCode(resp, HTTP_PAY_ERROR, charge.getFailureMessage());
			}else{//支付成功/payment success
				orderId = charge.getId();
			}
			break;
		case TradeVo.Trade_PayWay_Anet:
			orderId = AnetPayUtil.anetPay(req, amount, body,getUserId(req));
			if(orderId == null){//支付失败/payment failure
				setCode(resp, HTTP_PAY_ERROR);
				return null;
			}else if("00".equals(orderId)){
				setCode(resp, HTTP_PAY_NO_DEFAULT_PAYMENT);
				return null;
			}
			break;
		case TradeVo.Trade_PayWay_AcquiroPay:
			orderInfo = AcquiroPayUtil.careatePaymentUrl(body, tradeNo, amount,getUserId(req));
			if(orderInfo != null){
				setData(resp, orderInfo);
			}else{
				//签名信息失败/signature information failure
				setCode(resp, ERROR_EXCEPTION);
			}
			break;
		case TradeVo.Trade_PayWay_PayU:
			orderInfo = PayuUtil.loadPaymentParams(body, tradeNo, amount, getUserId(req),currency);
			if(orderInfo != null){
				setData(resp, orderInfo);
			}else{
				//签名信息失败/signature information failure
				setCode(resp, ERROR_EXCEPTION);
			}
			break;
		case TradeVo.Trade_PayWay_Account:
			UserVo userVo = new UserDaoImpl().getUserWithIndustryInfo(getUserId(req));
			TradeVo tradeVo = new TradeVo();
			tradeVo.setAmount(amount);
			tradeVo.setId(tradeNo);
			if(!PayBo.accountPay(userVo, tradeVo)){
				setCode(resp, HTTP_PAY_ERROR);
				return null;
			}
			orderId = "balance";
			break;
		default:
			break;
		}
		return orderId;
	}

	/**
	 * 
	 * @Title:        getPayType 
	 * @Description:  获取支付类型/obtain payment type
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param parms
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月6日 上午11:56:01
	 */
	private boolean getPayType(HttpServletRequest req, HttpServletResponse resp,String[] parms){
		payType = ValueUtil.getInt(req.getParameter(parms[0]));
		if(payType == 0){
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return false;
		}
		return true;
	}

	/**
	 * 40005 
	 * post
	 * 校验APP端返回的支付宝结果信息/verify app back alipay result information
	 * @param req
	 * @param resp
	 */
	private void checkAppAliPayResult(HttpServletRequest req, HttpServletResponse resp){
		String[] parms = new String[]{"result"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		System.out.println(req.getParameter(parms[0]));
		JSONObject resultJson = JSONObject.parseObject(req.getParameter(parms[0]));

		JSONObject responseJson = resultJson.getJSONObject("alipay_trade_app_pay_response");
		String  response =AliPayUtil.sortResponse(responseJson);

		Map<String,String> params = new HashMap<String,String>();
		params.put("sign",resultJson.get("sign").toString());
		params.put("paramsStr",response);

		for (Iterator iter = responseJson.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String value = (String)responseJson.get(name);
			//乱码解决，这段代码在出现乱码时使用。/code solve, this code use on mixed code
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, value);
		}

		AliPayUtil.checkAppAliPayData(params, resp);
	}


	/**
	 * 40006 
	 * post
	 * 校验APP端返回的结果信息/verify app return payment result information
	 * @param req
	 * @param resp
	 */
	private void checkAppPayResult(HttpServletRequest req, HttpServletResponse resp){
		String[] parms = new String[]{"tradeNo"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String tradeNo = req.getParameter(parms[0]);
		if(payType == TradeVo.Trade_PayWay_PayU){
			boolean result = PayuUtil.checkPaymentStatus(tradeNo);
			setFlagData(resp, result);
		}else if(payType == 1000){
			WechatPayUtil.orderQuery(tradeNo, resp,1);
		}else{
			WechatPayUtil.orderQuery(tradeNo, resp,0);
		}

	}

	/**
	 * 40007
	 * get
	 * @Title:        getDeposit 
	 * @Description:  获取押金额度/gaint deposit amount
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月5日 下午3:24:23
	 */
	private void getDeposit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		setData(resp, new DepositBo().getDepositAmount(getCityId(req)));
	}

	/**
	 * 40008
	 * get
	 * @Title:        getRechargeAmount 
	 * @Description:  获取充值金额列表/obtain recharge amount
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 下午3:21:45
	 */
	private void getRechargeAmount(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<RechargeAmountVo> list = new RechargeAmountDaoImpl().getAmountList(getCityId(req));
		if(list.size() == 0){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		setData(resp, list);
	}

	/**
	 * 40011
	 * @Title:        getPayPalToken 
	 * @Description:  获取PayPal支付信息/gain paypal payment information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月26日 下午2:48:55
	 */
	private void getPayPalToken(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"payRequestType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		int payType = ValueUtil.getInt(req.getParameter(parms[0]));
		IndustryVo industry = new IndustryDaoImpl().getIndustryInfo(getIndustryId(req));
		if(industry == null){
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		BigDecimal amount = null;
		String currency = industry.getCurrency();
		switch (payType) {
		case 40001://押金/deposit
			amount = new DepositBo().getDepositAmount(getCityId(req));
			break;
		case 40002://充值/deposit
			if(req.getParameter("amount") == null){
				setCode(resp, HTTP_RESULT_PARAMETER_MISS);
				return;
			}
			amount = new BigDecimal(req.getParameter("amount"));
			break;
		case 40003://骑行订单/cycling order
			TradeVo tradeVo = new TradeDaoImpl().getNoPayTrade(getUserId(req));
			if(tradeVo == null){
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return;
			}
			amount = tradeVo.getAmount();
			ValueUtil.getAmount(amount);
			break;
		case 40004://长租/long time rent
			BikeTypeVo typeVo = new BikeTypeDaoImpl().getTypeInfo(req.getParameter("typeId"));
			if(typeVo == null){
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return;
			}
			amount = typeVo.getPrice();
			break;
		}

		if(amount == null){
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return;
		}

		JSONObject object = new JSONObject();
		object.put("token", PayPalUtil.createToken());
		object.put("currency", currency);
		object.put("amount", amount);
		setData(resp, object);
	}


	/**
	 * 40009
	 * get
	 * @Title:        getPayMentList 
	 * @Description:  用户银行卡列表获取(仅限Anet支付或绑定的银行卡)/user bank card list access(only anet payment or bind bank card)
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月12日 下午5:42:13
	 */
	private void getPayMentList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		payType = ValueUtil.getInt(req.getParameter("payType"));
		String customerProfileId = "";
		UserDetailVo detailVo = new UserDetailDaoImpl().getUserDetail(getUserId(req));
		if(payType == TradeVo.Trade_PayWay_Stripe){
			customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_CUSTOMER_ID);

		}else{//Anet
			customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.NETPAY_CUSTOMER_ID); 

		}
		if(StringUtils.isEmpty(customerProfileId)){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		JSONObject dataObject = new JSONObject();
		if(payType == TradeVo.Trade_PayWay_Stripe){
			List<Map<String, String>> list  = StripPayUtil.getCustomerCardList(customerProfileId);
			String customerPaymentProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_PAYMENT_ID);
			dataObject.put("paymentList", list);
			dataObject.put("defaultPaymentId", customerPaymentProfileId);
		}else{
			String customerPaymentProfileId = detailVo.getThirdInfoObject(UserDetailVo.NETPAY_PAYMENT_ID);
			List<CustomerPaymentProfileMaskedType> list = AnetPayUtil.getCustomerPaymentProfileList(customerProfileId,customerPaymentProfileId);
			dataObject.put("paymentList", list);
			dataObject.put("defaultPaymentId", customerPaymentProfileId);
		}

		JSONObject object = new JSONObject();
		object.put("data", dataObject);
		setMapWithCodeOk(resp, object);
	}

	/**
	 * 40010
	 * post
	 * @Title:        addPayMent 
	 * @Description:  保存银行卡/keep bank card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月12日 下午5:54:56
	 */
	private void addPayMent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {


		payType = ValueUtil.getInt(req.getParameter("payType"));
		String customerProfileId = "";
		String paymentId = null;
		IUserDetailDao detailDao = new UserDetailDaoImpl();
		UserDetailVo detailVo = detailDao.getUserDetail(getUserId(req));
		if(detailVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		if(payType == TradeVo.Trade_PayWay_Stripe){
			String[] parms = new String[]{"tokenId"};
			if(!checkRequstParams(req, resp, parms)){
				return;
			}
			customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_CUSTOMER_ID);
		}else{//Anet
			String[] parms = new String[]{"cardNumber","expirDate"};
			if(!checkRequstParams(req, resp, parms)){
				return;
			}
			customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.NETPAY_CUSTOMER_ID); 

		}


		JSONObject dataObject = detailVo.getThirdInfoObject();
		if(dataObject == null){
			dataObject = new JSONObject();
		}
		if(payType == TradeVo.Trade_PayWay_Stripe){
			boolean flag = false;
			if(StringUtils.isEmpty(customerProfileId)){
				//创建用户/create user
				Customer customer = StripPayUtil.createUser(detailVo, req.getParameter("tokenId"));
				if(customer == null){					
					System.out.println("errorMsg:"+StripPayUtil.error_msg);
					setErrorCode(resp, HTTP_ADD_PAYMENT_ERROR, StripPayUtil.error_msg);	
					return ;
				}
				if(customer != null){
					customerProfileId = customer.getId();
					paymentId = customer.getDefaultSource();
				}
				if(!StringUtils.isEmpty(customerProfileId) && !StringUtils.isEmpty(paymentId)){
					flag = true;
					dataObject.put(UserDetailVo.STRIPE_CUSTOMER_ID, customerProfileId);
					dataObject.put(UserDetailVo.STRIPE_PAYMENT_ID, paymentId);
					detailVo.setThirdInfo(dataObject.toJSONString());
					detailDao.updateUserDetail(detailVo);
				}
			}else{
				//保存银行卡/keep bank card
				paymentId  = StripPayUtil.addCardForUser(customerProfileId, req.getParameter("tokenId"));
				if(!StringUtils.isEmpty(paymentId)){
					flag = true;
					dataObject.put(UserDetailVo.STRIPE_PAYMENT_ID, paymentId);
					detailVo.setThirdInfo(dataObject.toJSONString());
					detailDao.updateUserDetail(detailVo);
				}else{
					System.out.println("errorMsg:"+StripPayUtil.error_msg);
					setErrorCode(resp, HTTP_ADD_PAYMENT_ERROR, StripPayUtil.error_msg);	
					return ;
				}
			}
			setFlagData(resp, flag);

		}else{

			if(StringUtils.isEmpty(customerProfileId)){
				String customerIds[] = AnetPayUtil.createCustomerProfile(detailVo.getUid(), req);
				dataObject.put(UserDetailVo.NETPAY_CUSTOMER_ID, customerIds[0]);
				dataObject.put(UserDetailVo.NETPAY_PAYMENT_ID, customerIds[1]);
				detailVo.setThirdInfo(dataObject.toJSONString());
				detailDao.updateUserDetail(detailVo);

				paymentId = customerIds[1];
			}else{
				paymentId = AnetPayUtil.createCustomerPaymentProfile(detailVo, req);
			}

			setData(resp, StringUtils.isEmpty(paymentId) ?"0":"1");
		}


	}


	/**
	 * 40012
	 * @Title:        updatePaymentDefault 
	 * @Description:  设置默认支付银卡信息/set up default payment bank card information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月26日 下午2:53:19
	 */
	private void updatePaymentDefault(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"customerPaymentProfileId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		payType = ValueUtil.getInt(req.getParameter("payType"));
		IUserDetailDao detailDao = new UserDetailDaoImpl();
		UserDetailVo detailVo = detailDao.getUserDetail(getUserId(req));

		JSONObject thirdObject  = JSONObject.parseObject(detailVo.getThirdInfo());
		String paymentId = req.getParameter(parms[0]);
		if(payType == TradeVo.Trade_PayWay_Stripe){
			StripPayUtil.setDefaultCard(thirdObject.getString(UserDetailVo.STRIPE_CUSTOMER_ID),paymentId);
			thirdObject.put(UserDetailVo.STRIPE_PAYMENT_ID, paymentId);
		}else{
			thirdObject.put(UserDetailVo.NETPAY_PAYMENT_ID, paymentId);
		}

		detailVo.setThirdInfo(thirdObject.toJSONString());

		boolean flag = detailDao.updateUserDetail(detailVo);
		setFlagData(resp, flag);
	}

	/**
	 * 40013
	 * @Title:        loadPayU 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月20日 上午10:51:13
	 */
	private void loadPayU(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//		String payParams = PayuUtil.loadPaymentPage();
		//		setData(resp, payParams);
	}
	
	/**
	 * 40014
	 * @Title:        deletePaymentCard 
	 * @Description:  删除银行卡/delete payment card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年3月23日 上午11:35:41
	 */
	private void deletePaymentCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"customerPaymentProfileId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		payType = ValueUtil.getInt(req.getParameter("payType"));
		IUserDetailDao detailDao = new UserDetailDaoImpl();
		UserDetailVo detailVo = detailDao.getUserDetail(getUserId(req));

		JSONObject thirdObject  = JSONObject.parseObject(detailVo.getThirdInfo());
		String paymentId = req.getParameter(parms[0]);
		if(payType == TradeVo.Trade_PayWay_Stripe){
			String customerId = thirdObject.getString(UserDetailVo.STRIPE_CUSTOMER_ID);
			paymentId = StripPayUtil.deleteUserCard(customerId,paymentId);
			thirdObject.put(UserDetailVo.STRIPE_PAYMENT_ID, paymentId);
		}

		detailVo.setThirdInfo(thirdObject.toJSONString());

		boolean flag = detailDao.updateUserDetail(detailVo);
		setFlagData(resp, flag);
	}
}
