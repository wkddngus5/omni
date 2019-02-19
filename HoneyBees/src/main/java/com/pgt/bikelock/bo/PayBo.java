/**
 * FileName:     PayBo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年10月16日 下午3:18:25
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年10月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.math.BigDecimal;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.ICashRecordDao;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.impl.BikeLongLeaseDaoImpl;
import com.pgt.bikelock.dao.impl.CashRecordDaoImpl;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.pay.AcquiroPayUtil;
import com.pgt.bikelock.utils.pay.AliPayUtil;
import com.pgt.bikelock.utils.pay.AnetPayUtil;
import com.pgt.bikelock.utils.pay.PayPalUtil;
import com.pgt.bikelock.utils.pay.PayuUtil;
import com.pgt.bikelock.utils.pay.StripPayUtil;
import com.pgt.bikelock.utils.pay.WechatPayUtil;
import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserVo;

 /**
 * @ClassName:     PayBo
 * @Description:Payment Controller
 * @author:    Albert
 * @date:        2017年10月16日 下午3:18:25
 *
 */
public class PayBo {
	
	/**
	 * 
	 * @Title:        getCurrency 
	 * @Description:  Get Payment currency
	 * @param:        @param industryId
	 * @param:        @param cityId
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年10月16日 下午3:27:27
	 */
	public static String getCurrency(String industryId,int cityId){
		
		//get city currency
		CityVo cityVo = new CityDaoImpl().getCityInfo(cityId+"");
		if(cityVo != null && !StringUtils.isEmpty(cityVo.getCurrency())){
			return cityVo.getCurrency();
		}
		//get default currency
		IndustryVo industryVo = new IndustryDaoImpl().getIndustryInfo(industryId);
		if(industryVo != null){
			return industryVo.getCurrency();
		}
		
		return "";
	}
	
	/**
	 * 
	 * @Title:        dealPayResult 
	 * @Description:  Deal Pay Result
	 * @param:        @param trade
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月30日 下午3:51:23
	 */
	public static boolean dealPayResult(TradeVo trade){
		boolean result = new TradeDaoImpl().updateTradeSuccess(trade.getId(),trade.getOut_trade_no());
		boolean flag = false;
		if(result){
			if(trade.getType() == 2){
				flag = UserBo.rechargeForUser(trade.getUid(), trade.getAmount(),trade.getRecordId());
			}else if(trade.getType() == 3){
				//修改用户押金缴付状态为成功/update the deposit status to success
				UserVo userVo = new UserDaoImpl().getUserWithId(trade.getUid(),true);
				flag = new DepositBo().depositAuth(userVo);
			}else if(trade.getType() == 4){
				//修改长租记录为已支付/update the long-term record to paid
				flag = new BikeLongLeaseDaoImpl().updatePaySuccess(trade.getRecordId());
				//notify user by sms
				
			}else{
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 
	 * @Title:        refundOrder 
	 * @Description:  TODO
	 * @param:        @param recordId
	 * @param:        @param tradeVo
	 * @param:        @param status
	 * @param:        @param cardNumber
	 * @param:        @param expireDate
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月6日 下午2:42:37
	 */
	public static boolean refundOrder(String recordId,TradeVo tradeVo,int status,BigDecimal amount,String cardNumber,String expireDate){
		boolean flag = false;

/*		if(status > 0){
			flag = new DepositDaoImpl().updateDepositReturn(recordId, status,"");
		}else{*/
			String outNo = tradeVo.getOut_trade_no();
			//BigDecimal amount = tradeVo.getAmount();
			String outReFundId = "";
			switch (tradeVo.getWay()) {
			case TradeVo.Trade_PayWay_Wechat:
				flag = WechatPayUtil.refundOrder(recordId, outNo,Double.parseDouble(tradeVo.getAmount().toString()),
						Double.parseDouble(amount.toString()), outReFundId,0);
				break;
			case 1000:
				flag = WechatPayUtil.refundOrder(recordId, outNo,Double.parseDouble(tradeVo.getAmount().toString()),
						Double.parseDouble(amount.toString()), outReFundId,1);
				break;
			case TradeVo.Trade_PayWay_Alipay:
				flag = AliPayUtil.refundOrder(recordId, outNo, amount);
				break;
			case TradeVo.Trade_PayWay_PayPal:
				flag = PayPalUtil.refundOrder(outNo, amount);
				break;
			case TradeVo.Trade_PayWay_Stripe:
				flag = StripPayUtil.refundOrder(outNo, amount);
				break;
			case TradeVo.Trade_PayWay_Anet:
				flag = AnetPayUtil.refundOrder(recordId, outNo,amount , cardNumber, expireDate);
				break;
			case TradeVo.Trade_PayWay_AcquiroPay:
				flag = AcquiroPayUtil.refundOrder(recordId, outNo, amount);
				break;
			case TradeVo.Trade_PayWay_PayU:
				flag = PayuUtil.refundOrder(recordId, outNo, amount,"");
				break;
			default:
				break;
			}
			int result = 0;
			if(tradeVo.getWay() != TradeVo.Trade_PayWay_Alipay){
				if(flag){//退款成功/refund success
					if(status == 1){//手动退款/manual refund
						result = 1;
					}else{//自动退款/auto refund
						result = 2;
					}
				}else{//退款失败/refund failure
					if(status == 1){//手动退款/manual refund
						result = 3;
					}else{//自动退款/auto refund
						result = 4;
					}
				}
			}
			if(flag && tradeVo.getWay() != TradeVo.Trade_PayWay_Alipay){
				//同步成功时，修改状态(标记为自动)。支付宝需要异步回调/When synchronization is successful, modify the status (marked as automatic). Alipay requires an asynchronous callback /
				//update trade status to refunded
				int orderStatus = tradeVo.getBalance().compareTo(new BigDecimal(0)) == 0 ? 4:3;//refund all / Partial refund
				new TradeDaoImpl().updateTradeInfo(tradeVo.getId(), tradeVo.getAmount(), orderStatus);
				if(tradeVo.getType() == 3){
					//deposit
					new DepositBo().updateDepositReturn(recordId, result,outReFundId);
				}else if(tradeVo.getType() == 2){
					//recharge
		
				}else if(tradeVo.getType() == 1){
					//ride
					
				}else if(tradeVo.getType() == 4){
					//member
					
				}
				
			}
//		}
		return flag;
	}
	
	/**
	 * 
	 * @Title:        updateBalanceRefundStatus 
	 * @Description:  TODO
	 * @param:        @param userId    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月6日 下午3:11:18
	 */
	private static void updateBalanceRefundStatus(String userId,String recordId){
		//get recharge trade list
		List<TradeVo> tradeList = new TradeDaoImpl().getUserTradeList(userId, 2, 1);
		for (TradeVo tradeVo : tradeList) {
			return;
		}
		//当所有充值记录全都退款时，设置退款申请为自动退款/update status success when all recharge order is refunded
		ICashRecordDao cashDao = new CashRecordDaoImpl();
		CashRecordVo cashVo = cashDao.getCashDetail(recordId);
		if(cashVo != null && cashDao.updateCashStatus(1, recordId,new BigDecimal(0))){
			UserBo.subUserBalance(userId, cashVo.getAmount());
		}
	}


	/**
	 * 
	 * @Title:        accountPay 
	 * @Description:  余额支付/balance payment
	 * @param:        @param user
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月8日 下午5:53:27
	 */
	public static boolean accountPay(UserVo user,TradeVo tradeVo){
		
		IUserDao userDao = new UserDaoImpl();
		BigDecimal amount = tradeVo.getAmount();
		
		/** 骑行结束，扣费 **/ //cycling over,charge
		BigDecimal userMoney= user.getMoney() == null?ValueUtil.ZERO_AMOUNT:user.getMoney(); //用户的余额/user balance
		BigDecimal giftMoney= user.getGiftMoney() == null?ValueUtil.ZERO_AMOUNT:user.getGiftMoney(); //用户的赠送金额/user gift balance
		

		if(giftMoney.compareTo(ValueUtil.ZERO_AMOUNT) == 1){
			
			if(giftMoney.compareTo(amount) >= 0){
				tradeVo.setGiftPayAmount(amount);
				boolean flag = userDao.updateGiftMoney(giftMoney.subtract(amount), user.getuId());
				amount = ValueUtil.ZERO_AMOUNT;						
				return flag;
			}else{
				tradeVo.setGiftPayAmount(giftMoney);
				amount = amount.subtract(giftMoney);				
				giftMoney = ValueUtil.ZERO_AMOUNT;
			}
			
		}
		
		
		int r=userMoney.compareTo(amount); //对比用户余额是否足够/compare user balance whether enough
		if(r == -1){
			BigDecimal tempAmount = amount;
			ValueUtil.getAmount(tempAmount);
			//用户余额不足,自动充值/user balance not enough,auto top up
			BigDecimal rechargeAmount =  new BigDecimal(5);

			if(rechargeAmount.compareTo(tempAmount) == -1){//默认5元，如果不足时充值金额为骑行金额/default 5 yuan, if not enough recharge amount equal riding amount
				rechargeAmount = tempAmount;

			}
//			return false;
			if(UserBo.autoRecharge(user.getuId(),user.getIndustryId(), rechargeAmount, TradeVo.Trade_PayWay_Stripe, LanguageUtil.getDefaultValue("pay_ride_auto_recharge_body"))){
				//获取最新金额/obtain latest amount
				userMoney = userDao.getMoneyAmount(user.getuId());
			}else{
				return false;
			}

		}
		//扣除账户余额/deduct account balance
		userMoney = userMoney.subtract(amount);
		tradeVo.setAccountPayAmount(amount);
		boolean flag = userDao.updateUserMoney(userMoney, giftMoney, user.getuId());
		
		if(flag && !StringUtils.isEmpty(tradeVo.getId())){
			//update trade info
			new TradeDaoImpl().updateTradePayAmount(tradeVo.getId(), tradeVo.getAccountPayAmount(), tradeVo.getGiftPayAmount());
		}
		return flag;
	}

}
