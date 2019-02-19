/**
 * FileName:     AnetPayUtils.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月8日 上午10:01:07/10:01:07 am, July 8, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月8日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.utils.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IUserDetailDao;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.resource.EnvironmentConfig;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.UserDetailVo;

import net.authorize.Environment;
import net.authorize.api.contract.v1.ANetApiResponse;
import net.authorize.api.contract.v1.CreateCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.CreateCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.CreateCustomerProfileRequest;
import net.authorize.api.contract.v1.CreateCustomerProfileResponse;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardMaskedType;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerAddressType;
import net.authorize.api.contract.v1.CustomerPaymentProfileExType;
import net.authorize.api.contract.v1.CustomerPaymentProfileListItemType;
import net.authorize.api.contract.v1.CustomerPaymentProfileMaskedType;
import net.authorize.api.contract.v1.CustomerPaymentProfileOrderFieldEnum;
import net.authorize.api.contract.v1.CustomerPaymentProfileSearchTypeEnum;
import net.authorize.api.contract.v1.CustomerPaymentProfileSorting;
import net.authorize.api.contract.v1.CustomerPaymentProfileType;
import net.authorize.api.contract.v1.CustomerProfilePaymentType;
import net.authorize.api.contract.v1.CustomerProfileType;
import net.authorize.api.contract.v1.CustomerTypeEnum;
import net.authorize.api.contract.v1.GetCustomerPaymentProfileListRequest;
import net.authorize.api.contract.v1.GetCustomerPaymentProfileListResponse;
import net.authorize.api.contract.v1.GetCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.GetCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.GetCustomerProfileRequest;
import net.authorize.api.contract.v1.GetCustomerProfileResponse;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.MessageTypeEnum;
import net.authorize.api.contract.v1.Paging;
import net.authorize.api.contract.v1.PaymentProfile;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.contract.v1.TransactionResponse;
import net.authorize.api.contract.v1.TransactionTypeEnum;
import net.authorize.api.contract.v1.UpdateCustomerPaymentProfileRequest;
import net.authorize.api.contract.v1.UpdateCustomerPaymentProfileResponse;
import net.authorize.api.contract.v1.ValidationModeEnum;
import net.authorize.api.controller.CreateCustomerPaymentProfileController;
import net.authorize.api.controller.CreateCustomerProfileController;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.GetCustomerPaymentProfileController;
import net.authorize.api.controller.GetCustomerPaymentProfileListController;
import net.authorize.api.controller.GetCustomerProfileController;
import net.authorize.api.controller.UpdateCustomerPaymentProfileController;
import net.authorize.api.controller.base.ApiOperationBase;

/**
 * @ClassName:     AnetPayUtils
 * @Description:Anet支付工具类/Anet payment tool
 * @author:    Albert
 * @date:        2017年7月8日 上午10:01:07/10:01:07 am, July 8, 2017
 *
 */
public class AnetPayUtil {

	protected static Environment environment = Environment.SANDBOX;
	protected static final String LOGIN_ID = OthersSource.getSourceString("anet_pay_key");
	protected static final String TRANSACTION_KEY = OthersSource.getSourceString("anet_pay_secret");


	/**
	 * 
	 * @Title:        init 
	 * @Description:  支付环境初始化/initializing the payment environment 
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月12日 下午3:45:39/3:45:39 pm, July 12, 2017
	 */
	private static void init(){
		ApiOperationBase.setEnvironment(environment);

		MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
		merchantAuthenticationType.setName(LOGIN_ID);
		merchantAuthenticationType.setTransactionKey(TRANSACTION_KEY);

		System.out.println("anet id:"+LOGIN_ID+";key:"+TRANSACTION_KEY);

		ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
	}

	/**
	 * 
	 * @Title:        anetPay 
	 * @Description:  Anet支付/Anet Pay
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param amount
	 * @param:        @param body
	 * @param:        @return    
	 * @return:       String   订单ID/order ID
	 * @author        Albert
	 * @Date          2017年7月12日 下午5:30:57/5:30:57 pm. July 12, 2017
	 */
	public static String anetPay(final HttpServletRequest req,BigDecimal amount,String body,String userId){
		final IUserDetailDao detailDao = new UserDetailDaoImpl();
		final UserDetailVo detailVo = detailDao.getUserDetail(userId);
		final String customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.NETPAY_CUSTOMER_ID);
		String customerPaymentProfileId = null;
		if(req != null){
			customerPaymentProfileId = req.getParameter("customerPaymentProfileId");
		}
		if(StringUtils.isEmpty(customerPaymentProfileId)){
			//默认银行卡快捷支付/ default bank quick pay
			customerPaymentProfileId = detailVo.getThirdInfoObject(UserDetailVo.NETPAY_PAYMENT_ID);
		}
		String orderId = null;
		if(req != null && (StringUtils.isEmpty(customerProfileId) || StringUtils.isEmpty(customerPaymentProfileId))){

			final String cardNumber = req.getParameter("cardNumber");
			final String expirDate = req.getParameter("expirDate");
			if(StringUtils.isEmpty(cardNumber) || StringUtils.isEmpty(expirDate)){
				return "00";//无卡信息，无默认/no card info, no default
			}
			//卡号支付（第一次）/card pay(fist time)
			orderId = AnetPayUtil.chargeWithCreditCard(amount,cardNumber,expirDate,req.getParameter("cardCode"));
			if(orderId != null){
				new Thread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						//异步保存客户支付信息/saving the payment info asynchronously
						JSONObject thirdObject  = detailVo.getThirdInfoObject();
						if(thirdObject == null){
							thirdObject  = new JSONObject();
						}
						String customerIds[] = AnetPayUtil.createCustomerProfile(detailVo.getUid(), req);
						if(!StringUtils.isEmpty(customerIds[0]) && !StringUtils.isEmpty(customerIds[1])){
							thirdObject.put(UserDetailVo.NETPAY_CUSTOMER_ID, customerIds[0]);
							thirdObject.put(UserDetailVo.NETPAY_PAYMENT_ID, customerIds[1]);
							detailVo.setThirdInfo(thirdObject.toJSONString());
							System.out.println("save thirdInfo"+thirdObject.toJSONString());
							detailDao.updateUserDetail(detailVo);
						}

					}
				}).start();
			}


		}else{
			if(!StringUtils.isEmpty(customerPaymentProfileId) && !customerPaymentProfileId.equals(detailVo.getThirdInfoObject(UserDetailVo.NETPAY_PAYMENT_ID))){
				//更新快捷支付银行卡/update the quick pay card
				JSONObject thirdObject = JSONObject.parseObject(detailVo.getThirdInfo());
				thirdObject.put(UserDetailVo.NETPAY_PAYMENT_ID, customerPaymentProfileId);
				detailVo.setThirdInfo(thirdObject.toJSONString());
				detailDao.updateUserDetail(detailVo);
			}

			//快捷支付/quick pay
			orderId = AnetPayUtil.chargeWithCustomerProfile(customerProfileId, 
					customerPaymentProfileId, amount);
		}
		return orderId;
	}

	public static String getPaymentId(GetCustomerProfileResponse response){
		String paymentId = "";
		if (response!=null) {

			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				if(EnvironmentConfig.DEBUG){
					System.out.println(response.getMessages().getMessage().get(0).getCode());
					System.out.println(response.getMessages().getMessage().get(0).getText());

					System.out.println(response.getProfile().getMerchantCustomerId());
					System.out.println(response.getProfile().getDescription());
					System.out.println(response.getProfile().getEmail());
					System.out.println(response.getProfile().getCustomerProfileId());
				}


				if((!response.getProfile().getPaymentProfiles().isEmpty()) &&
						(response.getProfile().getPaymentProfiles().get(0).getBillTo() != null)){
					if(EnvironmentConfig.DEBUG){
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getFirstName());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getLastName());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getCompany());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getAddress());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getCity());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getState());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getZip());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getCountry());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getPhoneNumber());
						System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getFaxNumber());
					}

					paymentId = response.getProfile().getPaymentProfiles().get(0).getCustomerPaymentProfileId();
				}
			}
		}
		return paymentId;
	}

	/**
	 * 
	 * @Title:        chargeWithCreditCard 
	 * @Description:  信用卡支付/credit card pay
	 * @param:        @param cardNumber
	 * @param:        @param expirDate
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       String	订单ID/order ID
	 * @author        Albert
	 * @Date          2017年7月12日 下午3:38:32/3:38:32 pm, July 12, 2017
	 */
	public static String chargeWithCreditCard(BigDecimal amount,String cardNumber,String expirDate,String cardCode){
		//Common code to set for all requests
		init();
		// Populate the payment data
		PaymentType paymentType = new PaymentType();
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber(cardNumber);
		creditCard.setExpirationDate(expirDate);
		creditCard.setCardCode(cardCode);
		paymentType.setCreditCard(creditCard);

		// Create the payment transaction request
		TransactionRequestType txnRequest = new TransactionRequestType();
		txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
		txnRequest.setPayment(paymentType);
		txnRequest.setAmount(amount);
		// Make the API Request
		CreateTransactionRequest apiRequest = new CreateTransactionRequest();
		apiRequest.setTransactionRequest(txnRequest);
		CreateTransactionController controller = new CreateTransactionController(apiRequest);
		controller.execute();

		CreateTransactionResponse response = controller.getApiResponse();
		String transId = null;
		if (response!=null) {
			// If API Response is ok, go ahead and check the transaction response
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
				TransactionResponse result = response.getTransactionResponse();
				if(result.getMessages() != null){
					transId = result.getTransId();
					if(EnvironmentConfig.DEBUG){
						System.out.println("Successfully created transaction with Transaction ID: " + result.getTransId());
						System.out.println("Response Code: " + result.getResponseCode());
						System.out.println("Message Code: " + result.getMessages().getMessage().get(0).getCode());
						System.out.println("Description: " + result.getMessages().getMessage().get(0).getDescription());
						System.out.println("Auth Code: " + result.getAuthCode());
					}

				}
				else if(EnvironmentConfig.DEBUG){
					System.out.println("Failed Transaction.");
					if(response.getTransactionResponse().getErrors() != null){
						System.out.println("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
						System.out.println("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
					}
				}
			}
			else if(EnvironmentConfig.DEBUG){
				System.out.println("Failed Transaction.");
				if(response.getTransactionResponse() != null && response.getTransactionResponse().getErrors() != null){
					System.out.println("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
					System.out.println("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
				}
				else {
					System.out.println("Error Code: " + response.getMessages().getMessage().get(0).getCode());
					System.out.println("Error message: " + response.getMessages().getMessage().get(0).getText());
				}
			}
		}
		else if(EnvironmentConfig.DEBUG){
			System.out.println("Null Response.");
		}
		return transId;
	}

	/**
	 * 
	 * @Title:        chargeWithCustomerProfile 
	 * @Description:  用户资料信息快捷支付/customer profile quick pay
	 * @param:        @param customerProfileId
	 * @param:        @param customerPaymentProfileId
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月12日 下午3:50:12/3:50:12 pm, July 12, 2017
	 */
	public static String chargeWithCustomerProfile(String customerProfileId,String customerPaymentProfileId,BigDecimal amount){
		//Common code to set for all requests
		init();

		// Set the profile ID to charge
		CustomerProfilePaymentType profileToCharge = new CustomerProfilePaymentType();
		profileToCharge.setCustomerProfileId(customerProfileId);
		PaymentProfile paymentProfile = new PaymentProfile();
		paymentProfile.setPaymentProfileId(customerPaymentProfileId);
		profileToCharge.setPaymentProfile(paymentProfile);

		// Create the payment transaction request
		TransactionRequestType txnRequest = new TransactionRequestType();
		txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
		txnRequest.setProfile(profileToCharge);
		txnRequest.setAmount(amount);
		//txnRequest.setAmount(new BigDecimal(10).setScale(2, RoundingMode.CEILING));

		CreateTransactionRequest apiRequest = new CreateTransactionRequest();
		apiRequest.setTransactionRequest(txnRequest);
		CreateTransactionController controller = new CreateTransactionController(apiRequest);
		controller.execute();


		CreateTransactionResponse response = controller.getApiResponse();
		String transId = null;
		if (response!=null) {
			// If API Response is ok, go ahead and check the transaction response
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
				TransactionResponse result = response.getTransactionResponse();
				if(result.getMessages() != null){
					transId = result.getTransId();
					if(EnvironmentConfig.DEBUG){
						System.out.println("Successfully created transaction with Transaction ID: " + result.getTransId());
						System.out.println("Response Code: " + result.getResponseCode());
						System.out.println("Message Code: " + result.getMessages().getMessage().get(0).getCode());
						System.out.println("Description: " + result.getMessages().getMessage().get(0).getDescription());
						System.out.println("Auth Code: " + result.getAuthCode());
					}

				}
				else if(EnvironmentConfig.DEBUG){
					System.out.println("Failed Transaction.");
					if(response.getTransactionResponse().getErrors() != null){
						System.out.println("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
						System.out.println("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
					}
				}
			}
			else if(EnvironmentConfig.DEBUG){
				System.out.println("Failed Transaction.");
				if(response.getTransactionResponse() != null && response.getTransactionResponse().getErrors() != null){
					System.out.println("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
					System.out.println("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
				}
				else {
					System.out.println("Error Code: " + response.getMessages().getMessage().get(0).getCode());
					System.out.println("Error message: " + response.getMessages().getMessage().get(0).getText());
				}
			}
		}
		else if(EnvironmentConfig.DEBUG){
			System.out.println("Null Response.");
		}

		return transId;
	}

	/**
	 * 
	 * @Title:        createCustomerProfile 
	 * @Description:  保存客户资料/saving customer profile
	 * @param:        @param eMail (支付平台唯一标记，必填，暂时替换为用户ID)/payment platform unique tag, required, temporary replacement with user ID
	 * @param:        @param cardNumber
	 * @param:        @param expirDate
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月12日 下午3:53:44/3:53:44 pm, July 12, 2017
	 */
	public static String[] createCustomerProfile(String eMail,HttpServletRequest req){

		init();

		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber(req.getParameter("cardNumber"));//"4111111111111111"
		creditCard.setExpirationDate(req.getParameter("expirDate"));//"1220"

		CustomerAddressType customerAddress = new CustomerAddressType();
		customerAddress.setFirstName(req.getParameter("firstName"));
		customerAddress.setLastName(req.getParameter("lastName"));

		PaymentType paymentType = new PaymentType();
		paymentType.setCreditCard(creditCard);

		CustomerPaymentProfileType customerPaymentProfileType = new CustomerPaymentProfileType();
		customerPaymentProfileType.setCustomerType(CustomerTypeEnum.INDIVIDUAL);
		customerPaymentProfileType.setPayment(paymentType);
		customerPaymentProfileType.setBillTo(customerAddress);
		customerPaymentProfileType.setDefaultPaymentProfile(true);

		CustomerProfileType customerProfileType = new CustomerProfileType();
		customerProfileType.setMerchantCustomerId("M_" + eMail);
		customerProfileType.setDescription("Profile description for " + eMail);
		customerProfileType.setEmail(eMail);
		customerProfileType.getPaymentProfiles().add(customerPaymentProfileType);
		System.out.println("cardNumber:"+req.getParameter("cardNumber")+";expirDate"+req.getParameter("expirDate")
				+";firstName"+req.getParameter("firstName")+";lastName"+req.getParameter("lastName")
				+";eMail"+eMail);
		CreateCustomerProfileRequest apiRequest = new CreateCustomerProfileRequest();
		apiRequest.setProfile(customerProfileType);
		apiRequest.setValidationMode(ValidationModeEnum.TEST_MODE);
		CreateCustomerProfileController controller = new CreateCustomerProfileController(apiRequest);
		controller.execute();
		CreateCustomerProfileResponse response = controller.getApiResponse();
		String[] customerIds = {"",""};
		if (response!=null) {

			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
				customerIds[0] = response.getCustomerProfileId();
				if(EnvironmentConfig.DEBUG){
					System.out.println(response.getCustomerProfileId());
					if(!response.getCustomerPaymentProfileIdList().getNumericString().isEmpty())
						System.out.println(response.getCustomerPaymentProfileIdList().getNumericString().get(0));
					customerIds[1] = response.getCustomerPaymentProfileIdList().getNumericString().get(0);
					if(!response.getCustomerShippingAddressIdList().getNumericString().isEmpty())
						System.out.println(response.getCustomerShippingAddressIdList().getNumericString().get(0));
					if(!response.getValidationDirectResponseList().getString().isEmpty())
						System.out.println(response.getValidationDirectResponseList().getString().get(0));
				}

			}
			else if(EnvironmentConfig.DEBUG)
			{
				System.out.println("Failed to create customer profile:  " + response.getMessages().getResultCode());
				if(response.getMessages().getMessage().size() > 0){
					System.out.println("Error Code: " + response.getMessages().getMessage().get(0).getCode());
					System.out.println("Error message: " + response.getMessages().getMessage().get(0).getText());
				}


			}
		}else{
			if(EnvironmentConfig.DEBUG){
				System.out.println("response null");
			}
		}
		return customerIds;

	}

	/**
	 * 
	 * @Title:        getCustomerProfile 
	 * @Description:  获取客户资料/get customer profile
	 * @param:        @param customerProfileId
	 * @param:        @return    
	 * @return:       GetCustomerProfileResponse    
	 * @author        Albert
	 * @Date          2017年7月12日 下午3:55:30/3:55:30 pm, July 12, 2017
	 */
	public static GetCustomerProfileResponse getCustomerProfile(String customerProfileId){

		init();

		GetCustomerProfileRequest apiRequest = new GetCustomerProfileRequest();
		apiRequest.setCustomerProfileId(customerProfileId);

		GetCustomerProfileController controller = new GetCustomerProfileController(apiRequest);
		controller.execute();

		GetCustomerProfileResponse response = new GetCustomerProfileResponse();
		response = controller.getApiResponse();

		if (response!=null && EnvironmentConfig.DEBUG) {

			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				System.out.println(response.getMessages().getMessage().get(0).getCode());
				System.out.println(response.getMessages().getMessage().get(0).getText());

				System.out.println(response.getProfile().getMerchantCustomerId());
				System.out.println(response.getProfile().getDescription());
				System.out.println(response.getProfile().getEmail());
				System.out.println(response.getProfile().getCustomerProfileId());

				if((!response.getProfile().getPaymentProfiles().isEmpty()) &&
						(response.getProfile().getPaymentProfiles().get(0).getBillTo() != null)){
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getFirstName());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getLastName());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getCompany());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getAddress());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getCity());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getState());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getZip());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getCountry());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getPhoneNumber());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getBillTo().getFaxNumber());

					System.out.println(response.getProfile().getPaymentProfiles().get(0).getCustomerPaymentProfileId());

					System.out.println(response.getProfile().getPaymentProfiles().get(0).getPayment().getCreditCard().getCardNumber());
					System.out.println(response.getProfile().getPaymentProfiles().get(0).getPayment().getCreditCard().getExpirationDate());
				}

				if(!response.getProfile().getShipToList().isEmpty()){
					System.out.println(response.getProfile().getShipToList().get(0).getFirstName());
					System.out.println(response.getProfile().getShipToList().get(0).getLastName());
					System.out.println(response.getProfile().getShipToList().get(0).getCompany());
					System.out.println(response.getProfile().getShipToList().get(0).getAddress());
					System.out.println(response.getProfile().getShipToList().get(0).getCity());
					System.out.println(response.getProfile().getShipToList().get(0).getState());
					System.out.println(response.getProfile().getShipToList().get(0).getZip());
					System.out.println(response.getProfile().getShipToList().get(0).getCountry());
					System.out.println(response.getProfile().getShipToList().get(0).getPhoneNumber());
					System.out.println(response.getProfile().getShipToList().get(0).getFaxNumber());
				}

				if((response.getSubscriptionIds() != null) && (response.getSubscriptionIds().getSubscriptionId() != null) && 
						(!response.getSubscriptionIds().getSubscriptionId().isEmpty())){
					System.out.println("List of subscriptions:");
					for(String subscriptionid : response.getSubscriptionIds().getSubscriptionId())
						System.out.println(subscriptionid);
				}

			}
			else
			{
				System.out.println("Failed to get customer profile:  " + response.getMessages().getResultCode());
			}
		}
		return response;
	}

	/**
	 * 
	 * @Title:        getCustomerPaymentProfile 
	 * @Description:  用户银行卡列表获取/get cusomer payment profile
	 * @param:        @param customerProfileId
	 * @param:        @return    
	 * @return:       List<CustomerPaymentProfileListItemType>    
	 * @author        Albert
	 * @Date          2017年7月12日 下午5:40:15/5:40:15 pm, July 12, 2017
	 */
	public static List<CustomerPaymentProfileListItemType> getCustomerPaymentProfile2(String customerProfileId){

		init();

		//Setting the paging
		Paging paging = new Paging();
		paging.setLimit(100);
		paging.setOffset(1);

		// Setting the sorting order
		CustomerPaymentProfileSorting sorting = new CustomerPaymentProfileSorting();
		CustomerPaymentProfileOrderFieldEnum orderByEnum = CustomerPaymentProfileOrderFieldEnum.ID;
		sorting.setOrderBy(orderByEnum);
		sorting.setOrderDescending(false);

		// Setting the searchType
		CustomerPaymentProfileSearchTypeEnum searchType = CustomerPaymentProfileSearchTypeEnum.CARDS_EXPIRING_IN_MONTH;

		// Making the API request
		GetCustomerPaymentProfileListRequest apiRequest = new GetCustomerPaymentProfileListRequest();
		apiRequest.setPaging(paging);
		apiRequest.setSearchType(searchType);
		apiRequest.setSorting(sorting);
		apiRequest.setMonth("2020-12");

		// Calling the controller
		GetCustomerPaymentProfileListController controller = new GetCustomerPaymentProfileListController(apiRequest);
		controller.execute();
		// Getting the response
		GetCustomerPaymentProfileListResponse response = new GetCustomerPaymentProfileListResponse();
		response = controller.getApiResponse();
		// Checking id the response is not null then printing the details

		List <CustomerPaymentProfileListItemType> paymentList = null;
		if(response != null)
		{
			if(response.getMessages().getResultCode() == MessageTypeEnum.OK)
			{
				paymentList = response.getPaymentProfiles().getPaymentProfile();

				if(EnvironmentConfig.DEBUG){
					System.out.println("Successfully got customer payment profile list");
					System.out.println("Message Code:" + response.getMessages().getMessage().get(0).getCode());
					System.out.println("Message Text:" + response.getMessages().getMessage().get(0).getText());
					System.out.println("Total Number of Results in the Results Set:" + response.getTotalNumInResultSet());


					for(CustomerPaymentProfileListItemType element : paymentList)
					{
						System.out.println();
						if(element.getBillTo()!=null)
							System.out.println("Customer name : "+element.getBillTo().getFirstName()+" "+element.getBillTo().getLastName());
						System.out.println("Credit card number: "+element.getPayment().getCreditCard().getCardNumber());
						System.out.println("Customer Profile ID : "+element.getCustomerProfileId());
						System.out.println("Customer Payment Profile ID : "+element.getCustomerPaymentProfileId());
						System.out.println("Default Customer Payment : "+element.isDefaultPaymentProfile());
					}
				}


			}
			else if(EnvironmentConfig.DEBUG)
			{
				// When the error occurs
				System.out.println("Failed to get customer payment profile list");
				if(!response.getMessages().getMessage().isEmpty())
					System.out.println("Error: "+response.getMessages().getMessage().get(0).getCode()+" "+ response.getMessages().getMessage().get(0).getText());
			}
		}
		else if(EnvironmentConfig.DEBUG)
		{
			// Displaying the error code and message when response is null 
			ANetApiResponse errorResponse = controller.getErrorResponse();
			System.out.println("Failed to get response");
			if(!errorResponse.getMessages().getMessage().isEmpty())
				System.out.println("Error: "+errorResponse.getMessages().getMessage().get(0).getCode()+" \n"+ errorResponse.getMessages().getMessage().get(0).getText());
		}
		return paymentList;
	}

	/**
	 * 
	 * @Title:        getCustomerPaymentProfile 
	 * @Description:  用户银行卡列表获取/get customer payment profile
	 * @param:        @param customerProfileId
	 * @param:        @return    
	 * @return:       List<CustomerPaymentProfileMaskedType>    
	 * @author        Albert
	 * @Date          2017年8月4日 下午2:54:30/2:54:30 pm, August 4, 2017
	 */
	public static List<CustomerPaymentProfileMaskedType> getCustomerPaymentProfileList(String customerProfileId,String customerPaymentProfileId){
		GetCustomerProfileResponse response = getCustomerProfile(customerProfileId);
		List <CustomerPaymentProfileMaskedType> paymentList = null;
		if (response!=null) {
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
				if(!response.getProfile().getPaymentProfiles().isEmpty()){
					paymentList = response.getProfile().getPaymentProfiles();
				}
			}
		}
		return paymentList;
	}

	/**
	 * 
	 * @Title:        getCustomerPaymentProfile 
	 * @Description:  获取银卡详细信息/get customer payment profile
	 * @param:        @param customerProfileId
	 * @param:        @param customerPaymentProfileId
	 * @param:        @return    
	 * @return:       GetCustomerPaymentProfileResponse    
	 * @author        Albert
	 * @Date          2017年8月4日 上午10:02:35/10:02:35 am, August 4, 2017
	 */
	public static GetCustomerPaymentProfileResponse getCustomerPaymentProfile(String customerProfileId,String customerPaymentProfileId){

		init();

		GetCustomerPaymentProfileRequest apiRequest = new GetCustomerPaymentProfileRequest();
		apiRequest.setCustomerProfileId(customerProfileId);
		apiRequest.setCustomerPaymentProfileId(customerPaymentProfileId);

		GetCustomerPaymentProfileController controller = new GetCustomerPaymentProfileController(apiRequest);
		controller.execute();

		GetCustomerPaymentProfileResponse response = new GetCustomerPaymentProfileResponse();
		response = controller.getApiResponse();

		CreditCardMaskedType cardInfo = null;

		if (response!=null) {

			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				if(EnvironmentConfig.DEBUG){
					System.out.println(response.getMessages().getMessage().get(0).getCode());
					System.out.println(response.getMessages().getMessage().get(0).getText());
					if((response.getPaymentProfile().getBillTo()!= null)){
						System.out.println(response.getPaymentProfile().getBillTo().getFirstName());
						System.out.println(response.getPaymentProfile().getBillTo().getLastName());
						System.out.println(response.getPaymentProfile().getBillTo().getCompany());
						System.out.println(response.getPaymentProfile().getBillTo().getAddress());
						System.out.println(response.getPaymentProfile().getBillTo().getCity());
						System.out.println(response.getPaymentProfile().getBillTo().getState());
						System.out.println(response.getPaymentProfile().getBillTo().getZip());
						System.out.println(response.getPaymentProfile().getBillTo().getCountry());
						System.out.println(response.getPaymentProfile().getBillTo().getPhoneNumber());
						System.out.println(response.getPaymentProfile().getBillTo().getFaxNumber());
					}
					System.out.println(response.getPaymentProfile().getCustomerPaymentProfileId());
					System.out.println(response.getPaymentProfile().getPayment().getCreditCard().getCardNumber());
					System.out.println(response.getPaymentProfile().getPayment().getCreditCard().getExpirationDate());

					if((response.getPaymentProfile().getSubscriptionIds() != null) && (response.getPaymentProfile().getSubscriptionIds().getSubscriptionId() != null) && 
							(!response.getPaymentProfile().getSubscriptionIds().getSubscriptionId().isEmpty())){
						System.out.println("List of subscriptions:");
						for(String subscriptionid : response.getPaymentProfile().getSubscriptionIds().getSubscriptionId())
							System.out.println(subscriptionid);
					}
				}

				cardInfo = response.getPaymentProfile().getPayment().getCreditCard();

			}
			else if(EnvironmentConfig.DEBUG){
				{
					System.out.println("Failed to get customer payment profile:  " + response.getMessages().getResultCode());
				}
			}

		}

		return response;
	}


	/**
	 * 
	 * @Title:        createCustomerPaymentProfile 
	 * @Description:  新增银行卡信息/creat customer payment profile
	 * @param:        @param detailVo
	 * @param:        @param cardNumber
	 * @param:        @param expirDate
	 * @param:        @param cardCode
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月12日 下午6:08:31/6:08:31 pm, July 12, 2017
	 */
	public static String createCustomerPaymentProfile(UserDetailVo detailVo,HttpServletRequest req){

		init();
		//private String getPaymentDetails(MerchantAuthenticationType merchantAuthentication, String customerprofileId, ValidationModeEnum validationMode) {
		CreateCustomerPaymentProfileRequest apiRequest = new CreateCustomerPaymentProfileRequest();
		apiRequest.setCustomerProfileId(detailVo.getThirdInfoObject(UserDetailVo.NETPAY_CUSTOMER_ID));	
		//customer address
		CustomerAddressType customerAddress = new CustomerAddressType();
		customerAddress.setFirstName(req.getParameter("firstName"));
		customerAddress.setLastName(req.getParameter("lastName"));
		customerAddress.setAddress(detailVo.getAddress());
		//				customerAddress.setCity(deatailVo.getc);
		//				customerAddress.setState("WA");
		customerAddress.setZip(detailVo.getZip_code());
		customerAddress.setCountry(detailVo.getCountry());
		//				customerAddress.setPhoneNumber("000-000-0000");

		//credit card details
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber(req.getParameter("cardNumber"));
		creditCard.setExpirationDate(req.getParameter("expirDate"));
		creditCard.setCardCode(req.getParameter("cardCode"));

		CustomerPaymentProfileType profile = new CustomerPaymentProfileType();
		profile.setBillTo(customerAddress);
		//profile.setDefaultPaymentProfile(true);

		PaymentType payment = new PaymentType();
		payment.setCreditCard(creditCard);
		profile.setPayment(payment);

		apiRequest.setPaymentProfile(profile);

		CreateCustomerPaymentProfileController controller = new CreateCustomerPaymentProfileController(apiRequest);
		controller.execute();

		CreateCustomerPaymentProfileResponse response = new CreateCustomerPaymentProfileResponse();
		response = controller.getApiResponse();
		String paymentId = null;
		if (response!=null) {
			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
				paymentId = response.getCustomerPaymentProfileId();
				if(EnvironmentConfig.DEBUG){
					System.out.println(response.getCustomerPaymentProfileId());
					System.out.println(response.getMessages().getMessage().get(0).getCode());
					System.out.println(response.getMessages().getMessage().get(0).getText());
					if(response.getValidationDirectResponse() != null)
						System.out.println(response.getValidationDirectResponse());
				}

			}
			else  if(EnvironmentConfig.DEBUG)
			{
				System.out.println("Failed to create customer payment profile:  " + response.getMessages().getResultCode());
				if(!response.getMessages().getMessage().isEmpty())
					System.out.println("Error: "+response.getMessages().getMessage().get(0).getCode()+" \n"+ response.getMessages().getMessage().get(0).getText());
			}
		}else{
			if(EnvironmentConfig.DEBUG){
				System.out.println("response null");
			}
		}

		return paymentId;

	}

	/**
	 * 
	 * @Title:        updateCustomerPaymentProfile 
	 * @Description:  修改银行卡信息（暂支持修改默认）/update customer payment profile(temporary supporting default modified)
	 * @param:        @param customerProfileId
	 * @param:        @param customerPaymentProfileId
	 * @param:        @param customerPaymentProfileResponse
	 * @param:        @param defaultPayment
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月4日 上午10:46:40/10:46:40 am, August 4, 2017
	 */
	public static boolean updateCustomerPaymentProfile(String customerProfileId,String customerPaymentProfileId,
			GetCustomerPaymentProfileResponse customerPaymentProfileResponse,boolean defaultPayment){
		//customer address
		CustomerAddressType customerAddress = customerPaymentProfileResponse.getPaymentProfile().getBillTo();

		//credit card details
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber(customerPaymentProfileResponse.getPaymentProfile().getPayment().getCreditCard().getCardNumber());
		creditCard.setExpirationDate(customerPaymentProfileResponse.getPaymentProfile().getPayment().getCreditCard().getExpirationDate());


		PaymentType paymentType = new PaymentType();
		paymentType.setCreditCard(creditCard);

		CustomerPaymentProfileExType customer = new CustomerPaymentProfileExType();
		customer.setPayment(paymentType);
		customer.setCustomerPaymentProfileId(customerPaymentProfileId);
		customer.setBillTo(customerAddress);
		customer.setDefaultPaymentProfile(defaultPayment);

		UpdateCustomerPaymentProfileRequest apiRequest = new UpdateCustomerPaymentProfileRequest();
		apiRequest.setCustomerProfileId(customerProfileId);	
		apiRequest.setPaymentProfile(customer);
		apiRequest.setValidationMode(ValidationModeEnum.TEST_MODE);

		UpdateCustomerPaymentProfileController controller = new UpdateCustomerPaymentProfileController(apiRequest);
		controller.execute();

		UpdateCustomerPaymentProfileResponse response = new UpdateCustomerPaymentProfileResponse();
		response = controller.getApiResponse();
		boolean flag = false;
		if (response!=null) {

			if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

				System.out.println(response.getMessages().getMessage().get(0).getCode());
				System.out.println(response.getMessages().getMessage().get(0).getText());

				flag = true;
			}
			else
			{
				System.out.println("Failed to update customer payment profile:  " + response.getMessages().getResultCode());
			}
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        refundOrder 
	 * @Description:  Cancel (refund) payment order
	 * @param:        @param cancelId
	 * @param:        @param paymentId
	 * @param:        @param amount
	 * @param:        @param cardNumber 4 digits without spaces. 银行卡后4位/last 4 digits of card number
	 * @param:        @param expireDate 过期日期/expiration date
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月24日 上午9:51:20/9:51:20 am, August 24, 2017
	 */
	public static boolean refundOrder(String cancelId,String paymentId,BigDecimal amount,String cardNumber,String expireDate){
		init();
		// Create a payment object, last 4 of the credit card and expiration date are required
		PaymentType paymentType = new PaymentType();
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber(cardNumber);
		creditCard.setExpirationDate(expireDate);
		paymentType.setCreditCard(creditCard);

		// Create the payment transaction request
		TransactionRequestType txnRequest = new TransactionRequestType();
		txnRequest.setTransactionType(TransactionTypeEnum.REFUND_TRANSACTION.value());
		txnRequest.setRefTransId(paymentId);
		txnRequest.setAmount(new BigDecimal(amount.toString()));
		txnRequest.setPayment(paymentType);

		// Make the API Request
		CreateTransactionRequest apiRequest = new CreateTransactionRequest();
		apiRequest.setTransactionRequest(txnRequest);
		CreateTransactionController controller = new CreateTransactionController(apiRequest);
		controller.execute(); 

		CreateTransactionResponse response = controller.getApiResponse();

		if (response!=null) {
			if(EnvironmentConfig.DEBUG){
				// If API Response is ok, go ahead and check the transaction response
				if (response.getMessages().getResultCode() == MessageTypeEnum.OK ) {
					TransactionResponse result = response.getTransactionResponse();
					if(result.getMessages() != null){
						System.out.println("Successfully created transaction with Transaction ID: " + result.getTransId());
						System.out.println("Response Code: " + result.getResponseCode());
						System.out.println("Message Code: " + result.getMessages().getMessage().get(0).getCode());
						System.out.println("Description: " + result.getMessages().getMessage().get(0).getDescription());
						System.out.println("Auth Code: " + result.getAuthCode());
					}
					else {
						System.out.println("Failed Transaction.");
						if(response.getTransactionResponse().getErrors() != null){
							System.out.println("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
							System.out.println("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
						}
					}
				}
				else {
					System.out.println("Failed Transaction.");
					if(response.getTransactionResponse() != null && response.getTransactionResponse().getErrors() != null){
						System.out.println("Error Code: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorCode());
						System.out.println("Error message: " + response.getTransactionResponse().getErrors().getError().get(0).getErrorText());
					}
					else {
						System.out.println("Error Code: " + response.getMessages().getMessage().get(0).getCode());
						System.out.println("Error message: " + response.getMessages().getMessage().get(0).getText());
					}
				}
			}
			return true;
		}
		else {
			System.out.println("Null Response.");
			return false;
		}
	}
}
