/**
 * FileName:     UserLoginBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-28 下午3:33:53
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-28       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.bo;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.ICashRecordDao;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.impl.CashRecordDaoImpl;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.dao.impl.CouponDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.RechargeAmountDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserCouponDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.dao.impl.UserDeviceDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.HttpRequest;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.SecurityUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.ZoneDate;
import com.pgt.bikelock.utils.pay.AnetPayUtil;
import com.pgt.bikelock.utils.pay.StripPayUtil;
import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RechargeAmountVo;
import com.pgt.bikelock.vo.RequestTokenVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserCouponVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserVo;
import com.stripe.model.Charge;


/**
 * @ClassName:     UserLoginBo
 * @Description:用户登录业务控制/user log in business control
 * @author:    Albert
 * @date:        2017-3-28 下午3:33:53
 *
 */
public class UserBo {

	public static final String DataFormate = "yyyy-MM-dd HH:mm";
	public static final int TokenExpiredDay = 30;//token过期天数/token expired days

	UserDetailDaoImpl userDetailDao = new UserDetailDaoImpl();
	UserDaoImpl userDao = new UserDaoImpl();




	/**
	 * 
	 * @Title:        createUserToken 
	 * @Description:  生成token信息/generate token information
	 * @param:        @param userId
	 * @param:        @param industryId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017-3-28 下午4:52:11
	 */
	public static String createUserToken(final UserVo user,boolean saveToken){
		JSONObject tokenJson = new JSONObject();
		tokenJson.put("userId", user.getuId());
		tokenJson.put("industryId", user.getIndustryId());
		tokenJson.put("cityId", user.getCityId());
		//获取产业设置信息/obtain industry set up information
		IndustryVo industry = new IndustryDaoImpl().getIndustryInfo(user.getIndustryId());
		//设置消费币种/set up consumer currency
		tokenJson.put("currency", industry.getCurrency());
		//过期时间生成/expired time generate
		SimpleDateFormat sf = new SimpleDateFormat(DataFormate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, TokenExpiredDay);
		tokenJson.put("tokenExpiredDate", sf.format(c.getTime()));
		tokenJson.put("timeStamp",TimeUtil.getCurrentLongTimeStr());

		String mapStr = tokenJson.toString();
		System.out.println(mapStr);
		//加密信息/encryption information
		final String token =SecurityUtil.encrypt(mapStr, SecurityUtil.DoubKey);


/*		if(saveToken){
			//更新令牌信息/update request token info
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					new UserDeviceDaoImpl().updateRequestToken(user.getuId(), token);
				}
			}).start();
		}*/

		
		return token;
	}


	/**
	 * 
	 * @Title:        analyTokenMap 
	 * @Description:  解析token数据/analyse token data
	 * @param:        @param token
	 * @param:        @return    
	 * @return:       JSONObject    
	 * @author        Albert
	 * @Date          2017-3-28 下午4:11:09
	 */
	public  RequestTokenVo analyTokenMap(String token,HttpServletRequest req){

		String tokenValue = SecurityUtil.decrypt(token, SecurityUtil.DoubKey);
		JSONObject tokenJson = JSON.parseObject(tokenValue);

		RequestTokenVo tokenVo = new RequestTokenVo();

		if(tokenJson == null ||tokenJson.get("userId") == null || tokenJson.get("industryId") == null 
				|| tokenJson.get("tokenExpiredDate") == null|| tokenJson.get("currency") == null ){
			tokenVo.setTokenStatus(-1);
			return tokenVo;
		}

		DateFormat df = new SimpleDateFormat(DataFormate);
		try {
			Date dtEnd = df.parse(tokenJson.getString("tokenExpiredDate"));
			Date dtNow = new ZoneDate();
			if (dtEnd.getTime() > dtNow.getTime()) {
				tokenVo.setTokenStatus(1);
			}else{
				tokenVo.setTokenStatus(0);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		tokenVo.setUserId(tokenJson.getString("userId"));
		tokenVo.setCurrency(tokenJson.getString("currency"));
		tokenVo.setIndustryId(tokenJson.getString("industryId"));
		int cityId = ValueUtil.getInt(tokenJson.getString("cityId"));//城市默认1/default city as 1
		tokenVo.setCityId(cityId == 0 ? 1:cityId);

		return tokenVo;
	}

	/**
	 * 
	 * @Title:        addRechargeOrder 
	 * @Description:  添加充值订单/Add recharge order
	 * @param:        @param userId
	 * @param:        @param payType
	 * @param:        @param amount
	 * @param:        @param paymentId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年11月7日 下午8:14:40
	 */
	public static String addRechargeOrder(String userId,int payType,BigDecimal amount,String paymentId){
		//添加充值订单/add recharge payment
		TradeVo rechargeTrade = new TradeVo(userId,2,payType,amount);
		rechargeTrade.setOut_trade_no(paymentId);
		rechargeTrade.setStatus(1);
		String tradeId = new TradeDaoImpl().addTrade(rechargeTrade);
		return tradeId;
	}

	/**
	 * 
	 * @Title:        rechargeForUser 
	 * @Description:  为用户充值/recharge for user
	 * @param:        @param userId
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年3月29日 下午6:23:50
	 */
	public static boolean rechargeForUser(final String userId,BigDecimal amount,String amountId){
		if(!StringUtils.isEmpty(amountId)){
			//获取充值金额详情/obtain recharge amount details
			final RechargeAmountVo amountVo = new RechargeAmountDaoImpl().getAmount(amountId);

			if(amountVo.getGift_type() == 1){
				//设置实际获得金额（付款金额+赠送金额）/set up actual obtain amount(payment amount+gift amount
				amount = amountVo.getAmount().add(amountVo.getGift());
			}else if(amountVo.getGift_type() == 2){
				new Thread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub
						int count = amountVo.getGift().intValue();
						for (int i = 0; i < count; i++) {
							//优惠券赠送/send coupon
							CouponVo couponVo = new CouponDaoImpl().getCouponInfo(amountVo.getGift_id()+"",false);
							if(couponVo != null){
								UserCouponVo userCoupon = new UserCouponVo();
								userCoupon.setUid(userId);
								userCoupon.setCid(amountVo.getGift_id()+"");
								userCoupon.setStart_time(couponVo.getStart_time());
								userCoupon.setEnd_time(couponVo.getEnd_time());
								userCoupon.setAmount(couponVo.getValue());
								if("1".equals(OthersSource.getSourceString("coupon_auto_active"))
										&&  new UserCouponDaoImpl().addCouponForUserActive(userCoupon)){
									CouponBo.couponNotify(1, couponVo.getId(), userId, couponVo.getName());
								}else if(new UserCouponDaoImpl().addCouponForUserNotActive(userCoupon)){
									CouponBo.couponNotify(1, couponVo.getId(), userId, couponVo.getName());
								}
								
							}
						}

					}
				}).start();

			}
		}
		IUserDao userDao = new UserDaoImpl();
		UserVo userVo = userDao.getUserWithId(userId,true);
		if(userVo == null){
			return false;
		}
		
		
		//自动支付骑行订单/auto pay ride order
//		ITradeDao tradeDao = new TradeDaoImpl();
//		TradeVo tradeVo =  tradeDao.getNoPayTrade(userId);
//		if(tradeVo != null && amount.compareTo(tradeVo.getAmount()) >= 0){
//			tradeDao.updateTradeSuccess(tradeVo.getId(), "");
//			amount = amount.subtract(tradeVo.getAmount());
//		}
		
		userVo.setMoney(userVo.getMoney().add(amount));
		boolean flag = userDao.updateMoney(userVo.getMoney(), userId);
		if(flag){
			//激活注册优惠券/activae register coupon
			CouponBo.activeRegisterCoupon(userId, amount);
		}
		
		return flag;
	}

	/**
	 * 
	 * @Title:        autoRecharge 
	 * @Description:  自动充值/auto recharger
	 * @param:        @param userId
	 * @param:        @param amount
	 * @param:        @param payType
	 * @param:        @param title
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月5日 下午2:58:30
	 */
	public static boolean autoRecharge(String userId,String industryId,BigDecimal amount,int payType,String title){
		if(OthersSource.getSourceString("strippay_key") == null && OthersSource.getSourceString("anet_pay_key") == null ){
			return false;
		}
		ITradeDao tradeDao = new TradeDaoImpl();
		//添加充值订单/add recharger order
		TradeVo rechargeTrade = new TradeVo(userId,2,payType,amount);
		String tradeId = tradeDao.addTrade(rechargeTrade);
		IndustryVo industryVo = new IndustryDaoImpl().getIndustryInfo(industryId);
		String orderId = "";
		if(payType == TradeVo.Trade_PayWay_Stripe && OthersSource.getSourceString("strippay_key") != null){
			Charge charge =  StripPayUtil.createUserAndPay("", amount, industryVo.getCurrency(), title, userId);
			if(charge != null){//支付成功/payment success
				orderId = charge.getId();
			}else{
				return false;
			}
		}else if(payType == TradeVo.Trade_PayWay_Anet && OthersSource.getSourceString("anet_pay_key") != null){
			orderId = AnetPayUtil.anetPay(null, amount, title,userId);
			if(orderId == null){
				return false;
			}
		}
		if(orderId != null){
			//修改订单状态成功/modify order status success
			boolean flag = tradeDao.updateTradeSuccess(tradeId,orderId);
			flag = rechargeForUser(userId, amount,null);
			if(flag){
				NotificationBo.addSystemNotifiyMessage(true, userId,LanguageUtil.getDefaultValue("pay_recharge_body") ,title);
				return true;
			}

		}

		return false;
	}

	/**
	 * 
	 * @Title:        checkUserBindCard 
	 * @Description:  验证用户是否绑定银行卡/verify user whether connect bank card
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月28日 下午4:43:01
	 */
	public static boolean checkUserBindCard(String userId){
		UserDetailVo detailVo = new UserDetailDaoImpl().getUserDetail(userId);
		if(detailVo == null){
			return false;
		}
		String customerProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_CUSTOMER_ID);
		String customerPaymentProfileId = detailVo.getThirdInfoObject(UserDetailVo.STRIPE_PAYMENT_ID);
		if(!StringUtils.isEmpty(customerProfileId) && !StringUtils.isEmpty(customerPaymentProfileId)){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title:        subUserBalance 
	 * @Description:  扣除用户余额 / Subtract user balance
	 * @param:        @param userId
	 * @param:        @param money
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月6日 下午5:02:48
	 */
	public static boolean subUserBalance(String userId,BigDecimal money){
		IUserDao userDao = new UserDaoImpl();
		UserVo user = userDao.getUserWithId(userId, false);
		BigDecimal userMoney= user.getMoney() == null?new BigDecimal(0):user.getMoney(); //用户的余额/user balance
		//扣除账户余额/deduct account balance
		userMoney = userMoney.subtract(money);
		boolean flag = userDao.updateMoney(userMoney, user.getuId());
		return flag;
	}

	/**
	 * 
	 * @Title:        getUserName 
	 * @Description:  TODO
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年11月29日 下午12:26:03
	 */
	public String getUserName(String userId){
		UserDetailVo userDetail = userDao.getUserDetailInfo(userId);
		if(userDetail == null){
			return "";
		}
		String name = "";
		if(!StringUtils.isEmpty(userDetail.getFirstname())){
			name = name + userDetail.getFirstname();
		}
		if(!StringUtils.isEmpty(userDetail.getLastname())){
			name = name + userDetail.getLastname();
		}
		if(StringUtils.isEmpty(name)){
			name = name + userDetail.getUserVo().getNickName();
		}
		if(StringUtils.isEmpty(name)){
			name = name + userDetail.getUserVo().getPhone();
		}

		return name;
	}

	/**
	 * 
	 * @Title:        balanceRefund 
	 * @Description:  TODO
	 * @param:        @param userId
	 * @param:        @param refundAmount
	 * @param:        @param recordId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年12月26日 下午5:28:51
	 */
	public static String balanceRefund(String userId,BigDecimal refundAmount,String recordId){
		int count = 0;
		ITradeDao tradeDao = new TradeDaoImpl();
		//get recharge trade list
		List<TradeVo> tradeList = tradeDao.getUserRechargeSuccessTradeList(userId);
		BigDecimal leftAmount = refundAmount;
		String paymentId = null;
		for (TradeVo tradeVo: tradeList) {
			BigDecimal  tempAmount;
			boolean stop = false;
			if(leftAmount.compareTo(tradeVo.getBalance()) == 1){
				//退款金额大于订单金额
				tempAmount = tradeVo.getBalance();
			}else{
				//退款金额小于或等于订单金额
				tempAmount = leftAmount;
				stop = true;
			}
			if(tempAmount.compareTo(tradeVo.getBalance()) >= 0){
				tradeVo.setBalance(new BigDecimal(0));
			}else{
				tradeVo.setBalance(tradeVo.getBalance().subtract(tempAmount));
			}
			boolean flag = PayBo.refundOrder(recordId, tradeVo, 1, tempAmount ,null, null);
			if(flag){
				count ++;
				leftAmount = refundAmount.subtract(tempAmount);
				tradeDao.updateTradeBalance(tradeVo.getId(), tradeVo.getBalance());
				if(paymentId == null){
					paymentId = tradeVo.getOut_trade_no();
				}else{
					paymentId += ","+tradeVo.getOut_trade_no();
				}
			}
			if(stop){
				//当所有充值记录全都退款时，设置退款申请为退款/update status success when all recharge order is refunded
				ICashRecordDao cashDao = new CashRecordDaoImpl();
				CashRecordVo cashVo = cashDao.getCashDetail(recordId);
				if(cashVo != null && cashDao.updateCashStatus(1, recordId,refundAmount)){
					if(UserBo.subUserBalance(cashVo.getUid(), refundAmount)){
						//添加退款订单/add refund balance order
						TradeVo rechargeTrade = new TradeVo(cashVo.getUid(),5,tradeVo.getWay(),refundAmount);
						rechargeTrade.setOut_trade_no(paymentId);
						rechargeTrade.setStatus(1);
						new TradeDaoImpl().addTrade(rechargeTrade);
					}
				}
				break;
			}
		}
		String message = String.format(LanguageUtil.getDefaultValue("consume_order_refund_tips"), tradeList.size(),count);
		return message;
	}

	/**
	 * 
	 * @Title:        autoSetUserCity 
	 * @Description:  自动匹配设置用户所在城市/auto set user city
	 * @param:        @param location
	 * @param:        @param userId
	 * @param:        @param cityId    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年2月7日 下午5:53:04
	 */
	public static int autoSetUserCity(final LatLng location,final String userId,final int cityId,boolean sync){

		if(sync){
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					List<CityVo> cityList = new CityDaoImpl().getNearCitylist(location);
					for (CityVo cityVo : cityList) {
						if(!StringUtils.isEmpty(cityVo.getArea_detail())
								&& AMapUtil.checkPointInAreaWithDefaultRaidus(location, cityVo.getArea_detail())){

							if(ValueUtil.getInt(cityVo.getId()) != cityId){
								new UserDaoImpl().updateCity(userId,ValueUtil.getInt(cityVo.getId()));
							}						
							return;
						}
					}
					//use first city
					CityVo cityVo = cityList.get(0);
					if(ValueUtil.getInt(cityVo.getId()) != cityId){
						new UserDaoImpl().updateCity(userId,ValueUtil.getInt(cityVo.getId()));
					}	
				}
			}).start();
		}else{
			List<CityVo> cityList = new CityDaoImpl().getNearCitylist(location);
			for (CityVo cityVo : cityList) {
				if(!StringUtils.isEmpty(cityVo.getArea_detail())
						&& AMapUtil.checkPointInAreaWithDefaultRaidus(location, cityVo.getArea_detail())){

					if(ValueUtil.getInt(cityVo.getId()) != cityId){
						new UserDaoImpl().updateCity(userId,ValueUtil.getInt(cityVo.getId()));
						return ValueUtil.getInt(cityVo.getId());
					}						
					
				}
			}
			//use first city
			CityVo cityVo = cityList.get(0);
			if(ValueUtil.getInt(cityVo.getId()) != cityId){
				new UserDaoImpl().updateCity(userId,ValueUtil.getInt(cityVo.getId()));
				return ValueUtil.getInt(cityVo.getId());
			}	
		}

		return 0;
	}
	
	public static String getWechatOpenId(String code){
		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		paramsMap.put("appid", OthersSource.WECHATPAY_SMALL_PROGRAM_APP_ID);
		
		paramsMap.put("appid", OthersSource.getSourceString("wechatpay_small_program_appid"));
		paramsMap.put("secret", OthersSource.getSourceString("wechatpay_small_program_app_secret"));
		paramsMap.put("js_code", code);
		String result = HttpRequest.sendPost("https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code&", 
				ValueUtil.mapToString(paramsMap));
		JSONObject obj = JSONObject.parseObject(result);
		String openId = obj.getString("openid");
		if(!StringUtils.isEmpty(openId)){
			System.out.println(openId);
			return openId;
		}else{
			System.out.println(obj.getString("errcode")+":"+obj.getString("errmsg"));
		}
		return "";
	}
	
	/**
	 * 
	 * @Title:        userBlancePay 
	 * @Description:  用户账户余额/赠送余额支付 / user balance money and gift money
	 * @param:        @param amount
	 * @param:        @param userVo
	 * @param:        @param recordId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年5月31日 下午6:05:39
	 */
	/*public boolean userBlancePay(BigDecimal amount,UserVo userVo,String recordId){
		if(userVo.getGiftMoney().compareTo(ValueUtil.ZERO_AMOUNT) == 1){
			if(userVo.getGiftMoney().compareTo(amount) >= 0){
				amount = ValueUtil.ZERO_AMOUNT;
			}else{
				amount = amount.subtract(userVo.getGiftMoney());
			}
			userVo.setGiftMoney(userVo.getGiftMoney().subtract(amount));
		}
		
		if(amount.compareTo(ValueUtil.ZERO_AMOUNT) == 1){
			if(userVo.getMoney().compareTo(amount) >= 0){
				amount = ValueUtil.ZERO_AMOUNT;
			}else{
				amount = amount.subtract(userVo.getMoney());
			}
			userVo.setMoney(userVo.getMoney().subtract(amount));
		}
		
		
		
	}*/
	

	public static void main(String[] args) {
	/*	UserVo userVo = new UserVo();
		userVo.setuId("1");
		userVo.setIndustryId("1");
		String token = UserBo.createUserToken(userVo);
		System.out.println(token);
	*/
		UserBo loginBo = new UserBo();
		RequestTokenVo tokenVo = loginBo.analyTokenMap("0BCCE4BE5C9CCA17482D5813B26A43BCDFFD0BA2EC5E4650C1AD3E5E5BC134CB0D2DD7087AE968FAF5E84B447D3179BCBA3363E6FCA5A0AC1C115A1210A3A18DF22669C4FD9018CEF3106E3A160B3D4B89F5FCF919612D0FE926E416512E51511C9B5B111463C97ACAB8AA248DA4060BDC73587C138BE06C54A63451CF8A1BC2",null);
		if(tokenVo.getTokenStatus() == 1){
			System.out.println("token success:"+tokenVo.getUserId()+","+tokenVo.getCityId());
		}else if(tokenVo.getTokenStatus() == 0){
			System.out.println("token expired");
		}else{
			System.out.println("token invalid");
		}
		
		
	}
}
