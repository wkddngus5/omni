/**
 * FileName:     StripPayUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月1日 下午6:26:35/6:26:35 pm, April 1, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IUserDetailDao;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.UserDetailVo;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.DeletedExternalAccount;
import com.stripe.model.ExternalAccount;
import com.stripe.model.ExternalAccountCollection;
import com.stripe.model.Refund;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;

/**
 * @ClassName:     StripPayUtil
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月1日 下午6:26:35/6:26:35 pm, April 1, 2017
 *
 */
public class StripPayUtil{

	private static String Strip_Api_Key =  OthersSource.STRIP_PAY_KEY;
	
	public static String error_msg = "";

	/**
	 * 
	 * @Title:        init 
	 * @Description:  初始化/initialzing
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月7日 上午11:15:21/11:15:21 am, september 7, 2017
	 */
	public static void init(){
		Stripe.apiKey = Strip_Api_Key;
		
		error_msg = "";
	}



	/**
	 * 
	 * @Title:        stripBasePay 
	 * @Description:  生成Strip支付订单(基础支付，不保存客户信息，需每次输入)/generate Strip payment order(basic pay, unsave the customer info, input required for each use)
	 * @param:        @param token
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月1日 下午6:35:11/6:35:11 pm, April 1, 2017
	 */
	public static Charge stripBasePay(String token,BigDecimal amount,String currency,String body){

		init();

		// Charge the user's card:
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("amount", new BigDecimal(100).multiply(amount).setScale(0,  BigDecimal.ROUND_HALF_UP));//提交时为分(去掉小数点)/submitting time is minute (no fraction)
		params.put("currency", currency);
		params.put("description", body);
		params.put("source", token);

		try {
			Charge charge = Charge.create(params);
			return charge;

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}


	/**
	 * 
	 * @Title:        createUserAndPay 
	 * @Description:  快捷支付（创建用户信息并支付）/quick pay (creat user and pay)
	 * @param:        @param token
	 * @param:        @param amount
	 * @param:        @param currency
	 * @param:        @param body
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       Charge    
	 * @author        Albert
	 * @Date          2017年9月7日 下午4:26:53/4:26:53 pm, September 7, 2017
	 */
	public static Charge createUserAndPay(String token,BigDecimal amount,String currency,String body,String userId){

		IUserDetailDao detailDao = new UserDetailDaoImpl();
		UserDetailVo detailVo = detailDao.getUserDetail(userId);
		String customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_CUSTOMER_ID);
		String customerPaymentProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_PAYMENT_ID);
		if(StringUtils.isEmpty(customerProfileId) && !StringUtils.isEmpty(token)){
			//第一次支付，创建账户信息/creat account info at the first payment	 
			Customer customer= createUser(detailVo, token);
			if(customer != null){
				customerProfileId = customer.getId();
				customerPaymentProfileId = customer.getDefaultSource();
			}
			if(!StringUtils.isEmpty(customerProfileId) && !StringUtils.isEmpty(customerPaymentProfileId)){
				//创建成功，保存用户资料/save the customer profile after successful creat

				// TODO Auto-generated method stub
				//保存客户支付信息/save the customer payment info
				JSONObject thirdObject  = detailVo.getThirdInfoObject();
				if(thirdObject == null){
					thirdObject  = new JSONObject();
				}

				thirdObject.put(UserDetailVo.STRIPE_CUSTOMER_ID, customerProfileId);
				thirdObject.put(UserDetailVo.STRIPE_PAYMENT_ID, customerPaymentProfileId);
				detailVo.setThirdInfo(thirdObject.toJSONString());
				detailDao.updateUserDetail(detailVo);

			}

		}
		if(!StringUtils.isEmpty(customerProfileId) && !StringUtils.isEmpty(customerPaymentProfileId)){
			Charge charge = payByUser(amount, currency, body, customerProfileId,customerPaymentProfileId);
			return charge;
		}

		return null;

	}

	/**
	 * 
	 * @Title:        createUser 
	 * @Description:  创建用户/creat user
	 * @param:        @param detailVo
	 * @param:        @param token
	 * @param:        @return  cutomerId 
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年9月7日 下午4:22:03/4:22:03 pm, September 7, 2017
	 */
	public static Customer createUser(UserDetailVo detailVo,String token){

		init();

		Map<String, Object> customerParams = new HashMap<String, Object>();
		String email = detailVo.getEmail();
		if(StringUtils.isEmpty(email)){
			email = detailVo.getUid();
			if(!StringUtils.isEmpty(detailVo.getFirstname()) && !StringUtils.isEmpty(detailVo.getLastname())){
				email += "_"+detailVo.getFirstname()+" "+detailVo.getLastname();
			}
		}

		customerParams.put("email", email);

		if (token != null) {
			customerParams.put("source", token);
		}

		try {
			Customer customer = Customer.create(customerParams);
			return customer;

		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch(Exception e){
			error_msg = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @Title:        payByUser 
	 * @Description:  用户信息快捷支付/userinfo for quick pay
	 * @param:        @param amount
	 * @param:        @param currency
	 * @param:        @param body
	 * @param:        @param customerId
	 * @param:        @return    
	 * @return:       Charge    
	 * @author        Albert
	 * @Date          2017年9月7日 下午4:25:52/4:25:52 pm, September 7, 2017
	 */
	public static Charge payByUser(BigDecimal amount,String currency,String body,String customerId,String paymentId){

		init();

		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", new BigDecimal(100).multiply(amount).setScale(0,  BigDecimal.ROUND_HALF_UP));//提交时为分(去掉小数点)/submitting time is minute (no fraction)
		chargeParams.put("currency", currency);
		chargeParams.put("description", body);
		chargeParams.put("customer", customerId);
		chargeParams.put("source", paymentId);//不添加source为默认卡支付/no adding source for the default card payment

		try {
			Charge charge = Charge.create(chargeParams);
			System.out.println(charge.getFailureMessage());
			return charge;
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 
	 * @Title:        addCardForUser 
	 * @Description:  添加银行卡/add bank card 
	 * @param:        @param customerId
	 * @param:        @param cardToken
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月7日 下午4:29:33/4:29:33 pm, September 7, 2017
	 */
	public static String addCardForUser(String customerId,String cardToken){

		init();

		Customer customer;
		try {
			customer = Customer.retrieve(customerId);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("source",cardToken);
			ExternalAccount external = customer.getSources().create(params);//无重复判断/no repeat judging
			if(external != null && !StringUtils.isEmpty(external.getId())){
				customer = Customer.retrieve(customerId);
				return customer.getDefaultSource();
			}
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			error_msg = e.getMessage();
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @Title:        deleteUserCard 
	 * @Description:  删除银行卡/delete card
	 * @param:        @param customerId
	 * @param:        @param cardId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年3月23日 上午11:15:41
	 */
	public static String deleteUserCard(String customerId,String cardId){

		init();

		Customer customer;
		try {
			customer = Customer.retrieve(customerId);
			DeletedExternalAccount external = customer.getSources().retrieve(cardId).delete();
			if(external != null && !StringUtils.isEmpty(external.getId())){
				customer = Customer.retrieve(customerId);
				return customer.getDefaultSource();
			}
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @Title:        setDefaultCard 
	 * @Description:  设置默认银行卡信息/set default bank card info
	 * @param:        @param customerId
	 * @param:        @param paymentId    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月7日 下午6:25:31/6:25:31 pm, September 7, 2017
	 */
	public static void setDefaultCard(String customerId,String paymentId){
		init();

		Customer customer;
		try {
			customer = Customer.retrieve(customerId);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("default_source", paymentId);//card id
			customer.update(params);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	/**
	 * 
	 * @Title:        getCustomerCardList 
	 * @Description:  获取银行卡列表/get bank card list
	 * @param:        @param customerId
	 * @param:        @return    
	 * @return:       List<Card>    
	 * @author        Albert
	 * @Date          2017年9月7日 下午4:06:11/4:06:11 pm, September 7, 2017
	 */
	public static List<Map<String, String>> getCustomerCardList(String customerId){

		init();

		List<Map<String, String>> cardList = new ArrayList<Map<String, String>>();

		try {
			Customer customer = Customer.retrieve(customerId);
			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("limit", 100);
			customerParams.put("object", "card");
			customer.getDefaultSource();
			ExternalAccountCollection cl = customer.getSources().all(customerParams);
			List<ExternalAccount> list = cl.getData();

			for (ExternalAccount externalAccount : list) {
				Card card = (Card)externalAccount;
				//为了安全，重新生成银行卡信息，只返回基础信息/for security, it will return to basic info when regenerate the bank card info.			
				Map<String,String> cardMap = new HashMap<String, String>();
				cardMap.put("id", externalAccount.getId());
				cardMap.put("last4", card.getLast4());
				cardMap.put("brand", card.getBrand());
				//				cardMap.put("cardId", card.getId());

				cardList.add(cardMap);
			}

			//customer.getSources().list(customerParams);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cardList;

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
	 * @Date          2017年8月24日 上午9:44:54/9:44:54 am, August 24, 2017
	 */
	public static boolean refundOrder(String paymentId,BigDecimal amount){

		init();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("charge", paymentId);
		params.put("amount", new BigDecimal(100).multiply(amount).setScale(0,  BigDecimal.ROUND_HALF_UP));
		try {
			Refund refund = Refund.create(params);
			return true;
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}


	public static Plan createPlan (
			String id,
			String currency,
			String interval,
			String name,
			BigDecimal amount,
			int intervalCount,
			String statementDescriptor
			)
	{

		init();

		BigDecimal stripeAmount = new BigDecimal(100)
		.multiply(amount)
		.setScale(0,  BigDecimal.ROUND_HALF_UP);

		Map<String, Object> planParams = new HashMap<String, Object>();
		planParams.put("id", id);
		planParams.put("currency", currency);
		planParams.put("interval", interval);
		planParams.put("name", name);
		planParams.put("amount", stripeAmount);
		planParams.put("interval_count", intervalCount);
		planParams.put("statement_descriptor", statementDescriptor);


		try {
			return Plan.create(planParams);
		}
		catch (AuthenticationException e) {
			e.printStackTrace();
		}
		catch (InvalidRequestException e) {

			e.printStackTrace();
			// attempt to fetch existing plan; could already exist
			return retrievePlan(id);

		}
		catch (APIConnectionException e) {
			e.printStackTrace();
		}
		catch (CardException e) {
			e.printStackTrace();
		}
		catch (APIException e) {
			e.printStackTrace();
		}

		return null;

	}


	public static Plan retrievePlan (String planId) {

		init();

		try {
			return Plan.retrieve(planId);
		}
		catch (AuthenticationException e) {
			e.printStackTrace();
		}
		catch (InvalidRequestException e) {
			e.printStackTrace();
		}
		catch (APIConnectionException e) {
			e.printStackTrace();
		}
		catch (CardException e) {
			e.printStackTrace();
		}
		catch (APIException e) {
			e.printStackTrace();
		}

		return null;

	}


	public static Subscription cancelImmediateSubscription (String stripeSubscriptionId) {

		init();

		try {

			Subscription subscription = Subscription.retrieve(stripeSubscriptionId);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("at_period_end", false);
			subscription.cancel(params);

			return subscription;

		}
		catch (AuthenticationException e) {
			e.printStackTrace();
		}
		catch (InvalidRequestException e) {
			e.printStackTrace();
		}
		catch (APIConnectionException e) {
			e.printStackTrace();
		}
		catch (CardException e) {
			e.printStackTrace();
		}
		catch (APIException e) {
			e.printStackTrace();
		}

		return null;

	}


	public static Subscription cancelSubscription (String stripeSubscriptionId) {

		init();

		try {

			Subscription subscription = Subscription.retrieve(stripeSubscriptionId);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("at_period_end", true);
			subscription.cancel(params);

			return subscription;

		}
		catch (AuthenticationException e) {
			e.printStackTrace();
		}
		catch (InvalidRequestException e) {
			e.printStackTrace();
		}
		catch (APIConnectionException e) {
			e.printStackTrace();
		}
		catch (CardException e) {
			e.printStackTrace();
		}
		catch (APIException e) {
			e.printStackTrace();
		}

		return null;

	}


	public static Subscription subscribeCustomerToPlan (String userId, String stripePlanId) {

		init();

		if (userId == null || stripePlanId == null) {
			return null;
		}


		// lookup customer id
		UserDetailVo userDetails = new UserDetailDaoImpl().getUserDetail(userId);
		String stripeCustomerId = userDetails.getThirdInfoObject(UserDetailVo.STRIPE_CUSTOMER_ID);

		if (stripeCustomerId == null) {
			return null;
		}

		// build stripe subscription
		Map<String, Object> item = new HashMap<String, Object>();
		item.put("plan", stripePlanId);

		Map<String, Object> items = new HashMap<String, Object>();
		items.put("0", item);

		Map<String, Object> metadata = new HashMap<String, Object>();
		metadata.put("honeybees-user", userId);

		Map<String, Object> subscriptionParams = new HashMap<String, Object>();
		subscriptionParams.put("customer", stripeCustomerId);
		subscriptionParams.put("items", items);
		subscriptionParams.put("metadata", metadata);

		try {
			return Subscription.create(subscriptionParams);
		}
		catch (AuthenticationException e) {
			e.printStackTrace();
		}
		catch (InvalidRequestException e) {
			e.printStackTrace();
		}
		catch (APIConnectionException e) {
			e.printStackTrace();
		}
		catch (CardException e) {
			e.printStackTrace();
		}
		catch (APIException e) {
			e.printStackTrace();
		}

		return null;


	}


	public static boolean checkPayment(String userId){
		IUserDetailDao detailDao = new UserDetailDaoImpl();
		UserDetailVo detailVo = detailDao.getUserDetail(userId);
		String customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_CUSTOMER_ID);
		String customerPaymentProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_PAYMENT_ID);
		if(!StringUtils.isEmpty(customerProfileId) && !StringUtils.isEmpty(customerPaymentProfileId)){
			return true;
		}
		return false;
	}
}
