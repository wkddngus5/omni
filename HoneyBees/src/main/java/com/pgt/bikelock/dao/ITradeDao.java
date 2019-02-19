/**
 * FileName:     ITradeDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-28 上午9:53:59
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-28       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;


import java.math.BigDecimal;
import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;

 /**
 * @ClassName:     ITradeDao
 * @Description:交易订单接口类/transaction order interface type
 * @author:    Albert
 * @date:        2017-3-28 上午9:53:59
 *
 */
public interface ITradeDao {
	
	String TABLE_NAME="t_trade";
	
	/**
	 * 
	 * @Title:        addTrade 
	 * @Description:  增加骑行订单信息/add cycling order information
	 * @param:        @param tradeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-28 上午10:05:43
	 */
	boolean addBikeTrade(TradeVo tradeVo);
	
	/**
	 * 
	 * @Title:        addTrade 
	 * @Description:  添加订单/add order
	 * @param:        @param tradeVo
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月6日 上午11:42:40
	 */
	String addTrade(TradeVo tradeVo);
	
	/**
	 * 
	 * @Title:        getNotifyTradeDetail 
	 * @Description:  获取待通知提醒订单详情信息/obtain wait to inform order detail information
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       TradeVo    
	 * @author        Albert
	 * @Date          2017年3月29日 下午4:25:38
	 */
	TradeVo getNotifyTradeDetail(String userId);
	
	/**
	 * 
	 * @Title:        getBikeUseTradeDetail 
	 * @Description:  获取骑行订单信息/obatin riding order information
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       TradeVo    
	 * @author        Albert
	 * @Date          2017年5月17日 上午10:01:49
	 */
	TradeVo getBikeUseTradeDetail(String id);
	
	/**
	 * 
	 * @Title:        getBikeUseTradeDetail 
	 * @Description:  获取骑行订单信息/obatin riding order information
	 * @param:        @param useDate
	 * @param:        @return    
	 * @return:       TradeVo    
	 * @author        Albert
	 * @Date          2017年10月14日 上午10:44:10
	 */
	TradeVo getBikeUseTradeDetail(long useDate);
	
	/**
	 * 
	 * @Title:        getNoPayTrade 
	 * @Description:  获取待支付订单信息/obtain wait to pay order information
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       TradeVo    
	 * @author        Albert
	 * @Date          2017年4月1日 下午5:37:15
	 */
	TradeVo getNoPayTrade(String userId);
	
	/**
	 * 
	 * @Title:        getTradeInfo 
	 * @Description:  获取支付订单信息/obtain pay order information
	 * @param:        @param tradeId 订单记录ID/order record ID
	 * @param:        @return    
	 * @return:       TradeVo    
	 * @author        Albert
	 * @Date          2017年3月29日 下午6:12:37
	 */
	TradeVo getTradeInfo(String tradeId);
	
	/**
	 * 
	 * @Title:        getTradeDetailInfo 
	 * @Description:  TODO
	 * @param:        @param tradeId
	 * @param:        @return    
	 * @return:       TradeVo    
	 * @author        Albert
	 * @Date          2017年12月15日 下午6:21:36
	 */
	TradeVo getTradeDetailInfo(String tradeId);
	
	/**
	 *
	 * @Title:        getFinalDepositTradeId 
	 * @Description:  获取指定用户最后一条押金支付ID/obtain specify user final one deposit pay id
	 * @param:        @param uid
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年6月12日 上午10:33:56
	 */
	TradeVo getFinalDepositTradeInfo(String uid);
	
	/**
	 * 
	 * @Title:        updateNotifySuccess 
	 * @Description:  设置用户所有已支付订单通知成功/set up user all already paid order inform success
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月8日 下午6:03:14
	 */
	boolean updateNotifySuccess(String userId);
	
	/**
	 * 
	 * @Title:        updatePayWay 
	 * @Description:  支付方式修改/payment ways modify
	 * @param:        @param tradeId
	 * @param:        @param way
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月16日 下午2:21:50
	 */
	boolean updatePayWay(String tradeId,int way);
	
	/**
	 * 
	 * @Title:        updateTradeSuccess 
	 * @Description:  修改订单状态为支付成功(主动支付，不用再通知用户，需同时修改notify=1)/modify order status as payment success(pay initiative, no need inform user, need modify notify=1 meanwhile)
	 * @param:        @param trainId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年3月29日 下午6:17:45
	 */
	boolean updateTradeSuccess(String trainId,String out_trade_no);
	
	/**
	 * 
	 * @Title:        getTradeList 
	 * @Description:  获取骑行订单集合/gain riding order 
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       List<TradeVo>    
	 * @author        Albert
	 * @Date          2017年4月5日 下午4:29:42
	 */
	List<TradeVo> getBikeTradeList(String userId,int startPage);
	
	
	/**
	 * 
	 * @Title:        getTradeList 
	 * @Description:  获取所有订单集合/gain all order transaction
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       List<TradeVo>    
	 * @author        Albert
	 * @Date          2017年5月13日 下午4:43:09
	 */
	List<TradeVo> getTradeList(String userId,int startPage,int tradeType);
	
	
	
	/**********************后台业务*****************************/

	/**
	 * 
	 * @Title:        getTradeList 
	 * @Description:  获取订单集合/gain transaction set
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<TradeVo>    
	 * @author        Albert
	 * @Date          2017年4月8日 下午1:44:23
	 */
	List<TradeVo> getTradeList(RequestListVo requestVo);
	
	/**
 	 * 
 	 * @Title:        getCount 
 	 * @Description:  获取列表总数/gain list total amount
 	 * @param:        @param requestVo
 	 * @param:        @return    
 	 * @return:       int    
 	 * @author        Albert
 	 * @Date          2017年4月8日 上午11:18:00
 	 */
 	int getCount(RequestListVo requestVo);
 	
 	/**
 	 * 
 	 * @Title:        getTypeCount 
 	 * @Description:  获取制定类型订单数(已成功)/obtain develop type order number(already success)
 	 * @param:        @param type
 	 * @param:        @return    
 	 * @return:       int    
 	 * @author        Albert
 	 * @Date          2017年4月15日 下午3:31:14
 	 */
 	int getTypeCount(int type);
 	
 	/**
 	 * 
 	 * @Title:        getStatisticsList 
 	 * @Description:  支付统计/payment caculate
 	 * @param:        @param dateType
 	 * @param:        @param payType
 	 * @param:        @return    
 	 * @return:       List<StatisticsVo>    
 	 * @author        Albert
 	 * @Date          2017年7月4日 下午3:54:28
 	 */
 	List<StatisticsVo> getStatisticsList(int dateType,int payType,String date);
 	
 	/**
 	 * 
 	 * @Title:        getPayWayStatisticsList 
 	 * @Description:  支付类型统计/payment type caculate
 	 * @param:        @param dateType
 	 * @param:        @param payType
 	 * @param:        @return    
 	 * @return:       List<StatisticsVo>    
 	 * @author        Albert
 	 * @Date          2017年7月4日 下午3:54:40
 	 */
 	List<StatisticsVo> getPayWayStatisticsList(int dateType,int payType);
 	
 	/**
 	 * 
 	 * @Title:        updateTradeInfo 
 	 * @Description:  修改订单信息/modify transaction information
 	 * @param:        @param tradeId
 	 * @param:        @return    
 	 * @return:       boolean    
 	 * @author        Albert
 	 * @Date          2017年7月24日 下午4:10:02
 	 */
 	boolean updateTradeInfo(String tradeId,BigDecimal amount,int status);
 	
 	/**
 	 * 
 	 * @Title:        updateTradeBalance 
 	 * @Description:  更新订单余额/ update trade balance
 	 * @param:        @param tradeId
 	 * @param:        @param amount
 	 * @param:        @return    
 	 * @return:       boolean    
 	 * @author        Albert
 	 * @Date          2017年11月6日 下午6:26:31
 	 */
 	boolean updateTradeBalance(String tradeId,BigDecimal amount);
 	
 	
	boolean updateTradePayAmount(String tradeId,BigDecimal accountAmount,BigDecimal giftAmount);
 	
 	/**
 	 * 
 	 * @Title:        getUserTradeList 
 	 * @Description:  获取用户订单列表/Get user trade list
 	 * @param:        @param userId
 	 * @param:        @param type 
 	 * @param:        @param status
 	 * @param:        @return    
 	 * @return:       List<TradeVo>    
 	 * @author        Albert
 	 * @Date          2017年11月3日 下午8:45:04
 	 */
 	List<TradeVo> getUserTradeList(String userId,int type,int status);

 	/**
 	 * 
 	 * @Title:        getUserRechargeSuccessTradeList 
 	 * @Description:  Get user recharge success trade list
 	 * @param:        @param userId
 	 * @param:        @param status 0
 	 * @param:        @return    
 	 * @return:       List<TradeVo>    
 	 * @author        Albert
 	 * @Date          2017年11月6日 下午5:28:31
 	 */
 	List<TradeVo> getUserRechargeSuccessTradeList(String userId);

 	/**
 	 * 
 	 * @Title:        checkRideRecordExist 
 	 * @Description:  校验骑行订单是否存在，防止骑行重复支付
 	 * @param:        @param rideId
 	 * @param:        @return    
 	 * @return:       boolean    
 	 * @author        Albert
 	 * @Date          2018年7月17日 下午3:26:10
 	 */
 	boolean checkRideRecordExist(String rideId);
}
