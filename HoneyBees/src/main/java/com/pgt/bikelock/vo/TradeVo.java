/**
 * FileName:     TradeVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-28 上午9:40:05/9:40:05 am, March 28, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-28       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;

/**
 * @ClassName:     TradeVo
 * @Description:订单交易实体/order trade entity
 * @author:    Albert
 * @date:        2017-3-28 上午9:40:05/9:40:05 am, March 28, 2017
 *
 */
public class TradeVo {

	public static final int Trade_PayWay_Account = 1;
	public static final int Trade_PayWay_Wechat = 2;
	public static final int Trade_PayWay_Alipay = 3;
	public static final int Trade_PayWay_PayPal = 4;
	public static final int Trade_PayWay_Visa = 5;
	public static final int Trade_PayWay_Stripe = 6;
	public static final int Trade_PayWay_LongLease = 7;
	public static final int Trade_PayWay_Coupon = 8;
	public static final int Trade_PayWay_Redpack = 9;
	public static final int Trade_PayWay_Anet = 10;
	public static final int Trade_PayWay_AcquiroPay = 11;
	public static final int Trade_PayWay_PayU = 12;
	public static final int Trade_PayWay_Membership = 13;

	String id;
	String recordId;//消费ID，外键使用ID/record ID, external key record ID
	BikeUseVo bikeUseVo;//使用记录实体/use record entity
	int status;//支付状态 0：待支付 1：支付成功  2:交易关闭 3;已部分退款 4:已全额退款/payment status 0: to be paid 1:paid 2:closed 3:Partially refunded 4:Refunded
	int way	;//支付方式 1:账户余额 2：微信 3：支付宝 4:PayPay 5:VISA 6:Strip 7:长租免费 8:优惠券/payment type 1: account balance 2: wechat 3: alipay 4: PayPay 5:VISA 6: Long-term rent for free 8: coupon
	String date;//订单日期/order date
	BigDecimal amount;//金额/amount
	int type;//支付类型 1：消费 2：充值 3:押金 4：长租  5:退款余额 /payment type 1: consume 2: charge 3: deposit 4: long-term rent 5：refund balance 6: membership payment
	String uid;//用户ID/user ID
	UserVo userVo;//用户实体/User entity 
	String out_pay_id;//关联支付ID（优惠券，长租）/out pay ID (coupon, long-term rent)
	String out_trade_no;//第三方支付订单ID/ payment ID of third party 
	BigDecimal balance;//订单余额
	int cityId;
	BigDecimal accountPayAmount;//余额支付金额/account pay amount
	BigDecimal giftPayAmount;//优惠券支付金额/优惠金额 gift pay amount
	
	/**显示属性**//**display attribute**/
	String wayStr;
	String typeStr;
	String statusStr;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}


	/**
	 * @return the bikeUseVo
	 */
	public BikeUseVo getBikeUseVo() {
		return bikeUseVo;
	}
	/**
	 * @param bikeUseVo the bikeUseVo to set
	 */
	public void setBikeUseVo(BikeUseVo bikeUseVo) {
		this.bikeUseVo = bikeUseVo;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getWay() {
		return way;
	}
	public void setWay(int way) {
		this.way = way;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}


	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}



	/**
	 * @return the out_pay_id
	 */
	public String getOut_pay_id() {
		return out_pay_id;
	}
	/**
	 * @param out_pay_id the out_pay_id to set
	 */
	public void setOut_pay_id(String out_pay_id) {
		this.out_pay_id = out_pay_id;
	}


	/**
	 * @return the userVo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	/**
	 * @param userVo the userVo to set
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}

	/**
	 * @return the wayStr
	 */
	public String getWayStr() {
		if(wayStr != null){
			return wayStr;
		}
		wayStr = getWayStr(this.way);
		return wayStr;
	}
	
	public static String getWayStr(int way){
		String tempWay;
		String[] payWay;
		if(OthersSource.getSourceString("consume_order_pay_way_value") != null){
			payWay = OthersSource.getSourceString("consume_order_pay_way_value").split(",");
		}else{
			payWay = LanguageUtil.getDefaultValue("consume_order_pay_way_value").split(",");
		}
		if(way <= 0 || way >= payWay.length){
			return "UnKnow";
		}
		tempWay = payWay[way];
		return tempWay;
	}
	
	/**
	 * @return the typeStr
	 */
	public String getTypeStr() {

		if(typeStr != null){
			return typeStr;
		}
		String[] payType = LanguageUtil.getDefaultValue("consume_order_pay_type_value").split(",");
		if(this.type <= 0 || this.type > payType.length){
			return "UnKnow";
		}
		typeStr = payType[this.type];
		return typeStr;
	}



	/**
	 * @return the cityId
	 */
	public int getCityId() {
		return cityId;
	}
	/**
	 * @param cityId the cityId to set
	 */
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	/**
	 * @return the out_trade_no
	 */
	public String getOut_trade_no() {
		return out_trade_no;
	}
	/**
	 * @param out_trade_no the out_trade_no to set
	 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	/**
	 * @return the statusStr
	 */
	public String getStatusStr() {
		if(statusStr != null){
			return statusStr;
		}
		this.statusStr = LanguageUtil.getStatusStr(status, "consume_order_pay_status_value");
		return statusStr;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	
	
	
	/**
	 * @return the accountPayAmount
	 */
	public BigDecimal getAccountPayAmount() {
		return accountPayAmount;
	}
	/**
	 * @param accountPayAmount the accountPayAmount to set
	 */
	public void setAccountPayAmount(BigDecimal accountPayAmount) {
		this.accountPayAmount = accountPayAmount;
	}
	/**
	 * @return the giftPayAmount
	 */
	public BigDecimal getGiftPayAmount() {
		return giftPayAmount;
	}
	/**
	 * @param giftPayAmount the giftPayAmount to set
	 */
	public void setGiftPayAmount(BigDecimal giftPayAmount) {
		this.giftPayAmount = giftPayAmount;
	}
	public TradeVo(){

	}

	/**
	 * 
	 * @param userId
	 * @param type 订单类型  1：消费 2：充值 3:押金 4：长租/order type 1: consume 2: charge 3: deposit 4: long-term rent
	 * @param way 支付方式/payment way 
	 * @param amount 金额/amount
	 */
	public TradeVo(String userId,int type,int way,BigDecimal amount){
		this.uid = userId;
		this.type = type;
		this.way = way;
		this.amount = amount;
	}

	/**
	 * 
	 * @param rst
	 * @throws SQLException
	 */
	public TradeVo(ResultSet rst) throws SQLException{
		this.id = BaseDao.getString(rst, "id");
		this.status = BaseDao.getInt(rst, "status");
		this.amount = BaseDao.getBigDecimal(rst,"amount");
		this.uid = BaseDao.getString(rst, "uid");
		this.way = BaseDao.getInt(rst, "way");
		this.out_pay_id = BaseDao.getString(rst, "out_pay_id");
		this.out_trade_no = BaseDao.getString(rst, "out_trade_no");
		if(StringUtils.isEmpty(this.out_trade_no) && !StringUtils.isEmpty(this.out_pay_id)){
			this.out_trade_no = this.out_pay_id;
		}
		this.type = BaseDao.getInt(rst, "type");
		this.date = BaseDao.getString(rst, "date");
//		if(type == 1){
			this.recordId = BaseDao.getString(rst, "record_id");
//		}
	
		this.balance = BaseDao.getBigDecimal(rst, "balance");
		this.accountPayAmount = BaseDao.getBigDecimal(rst, "account_pay_amount");
		this.giftPayAmount = BaseDao.getBigDecimal(rst, "gift_pay_amount");
	}
}
