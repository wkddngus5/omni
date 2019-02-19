/**
 * FileName:     PayBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 下午2:43:54
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bikelock.dao.IBikeDao;
import com.pgt.bikelock.dao.IBikeOrderRecord;
import com.pgt.bikelock.dao.IBikeReserveDao;
import com.pgt.bikelock.dao.IBikeUseDao;
import com.pgt.bikelock.dao.INotificationConfigDao;
import com.pgt.bikelock.dao.IRedPackBikeDao;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.IUserCouponDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.impl.*;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.HttpRequest;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.PushUtil;
import com.pgt.bikelock.utils.PushUtil.PushType;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.pay.AnetPayUtil;
import com.pgt.bikelock.vo.*;
import com.pgt.bikelock.vo.UserCouponVo.GiftFromType;
import com.pgt.bikelock.vo.admin.BikeOrderRecord;
import com.pgt.bikelock.vo.admin.NotificationConfigVo;
import com.pgt.bikelock.vo.admin.NotificationConfigVo.NotificationType;
import com.pgt.bikelock.vo.admin.RedPackBikeVo;
import com.pgt.bikelock.vo.admin.RedPackRuleVo;



/**
 * @ClassName:     BikeBo
 * @Description:单车业务控制类/controller of bike business
 * @author:    Albert
 * @date:        2017年4月6日 下午2:43:54
 */
public class BikeBo {

	public static final int BIKE_CONNECT_CHECK_SECONDS = 960;//连接状态检测时间间隔/Connection status detection interval
	public static final int BIKE_NOT_USED_IN_DAYS = OthersSource.getSourceString("bike_not_use_in_day") == null
			?3:ValueUtil.getInt(OthersSource.getSourceString("bike_not_use_in_day"));//单车未被使用异常天数/Abnormal number of days in which bicycles were not used
	public static final int BIKE_WARN_COUNT = 3;//频繁报警单车次数/Frequency alarm bike number
	public static final int AUTO_SET_BIKE_ERROR_COUNT = 3;//自动设置单车故障异常次数/automatic set up bike error number


	ScheduledExecutorService heartCheckService;

	ScheduledExecutorService lowPowerCheckService;

	ScheduledExecutorService locationCheckService;

	private IBikeDao bikeDao;
	private IBikeOrderRecord bikeOrderDao;
	private	IBikeUseDao bikeUseDao;

	NotificationBo notifyBo;

	static BikeBo bikeBo;
	/**
	 * 
	 * @Title:        getInstance 
	 * @Description:  单利单车控制器
	 * @param:        @return    
	 * @return:       BikeBo    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:29:52
	 */
	public static synchronized  BikeBo getInstance(){
		if(bikeBo == null){

			synchronized (BikeBo.class) {    
				if (bikeBo == null) {    
					bikeBo = new BikeBo();
				}    
			}

		}
		return bikeBo;
	}

	public BikeBo(){
		bikeDao = new BikeDaoImpl();
		bikeOrderDao = new BikeOrderRecordDaoImpl();
		notifyBo = new NotificationBo();
		bikeUseDao = new BikeUseDaoImpl();
	}

	/**
	 * 
	 * @Title:        dealBikeOrder 
	 * @Description:  骑行订单处理/riding order tackle
	 * @param:        @param bikeUseVo
	 * @param:        @return    
	 * @return:       int -1：越界/Transboundary 0：失败/failure 1：成功/success
	 * @author        Albert
	 * @Date          2017年9月15日 下午1:57:13
	 */
	public int dealBikeOrder(BikeUseVo bikeUseVo, boolean checkArea){

		ITradeDao tradeDao = new TradeDaoImpl();
		if(tradeDao.checkRideRecordExist(bikeUseVo.getId())){
			System.out.println("ride histroy have been generate order");
			return 0;
		}

		synchronized (BikeBo.class) {

			if(this.bikeUseDao.getUseIngDetailInfo(bikeUseVo.getBikeVo().getImei(), bikeUseVo.getUid()) == null){
				return 0;
			}

			IUserDao userDao = new UserDaoImpl();
			final String userId = bikeUseVo.getUid();
			int oldDuration = bikeUseVo.getOldDuration();
			String adminId = bikeUseVo.getAdminId();

			final int cityId = bikeUseVo.getBikeVo().getCityId();
			LatLng location = new LatLng(bikeUseVo.getEndLat(), bikeUseVo.getEndLng());



			// 1. Check if bike is in Prohibited Parking Area
			if(checkArea && checkBikeInProhibitedParkingArea(cityId, location)){

				// persist lock status but continue to charge
				new BikeDaoImpl().updateLockStatus(bikeUseVo.getBikeVo().getNumber(), -1);
				bikeUseVo.setOut_area(2);
				bikeUseDao.updateUseBikeProcess(bikeUseVo,false);
				System.out.println("in Prohibited parking, lock:"+bikeUseVo.getBikeVo().getNumber()+";date:"+bikeUseVo.getDate());
				return -2;

			}


			boolean inArea = bikeUseVo.getOut_area() > 0 ? false:true;
			// 2. Check if bike is in City Area
			if(checkArea && !(inArea = checkBikeIsInCityArea(cityId,location ))){

				// persist lock status but continue to charge
				new BikeDaoImpl().updateLockStatus(bikeUseVo.getBikeVo().getNumber(), -1);
				bikeUseVo.setOut_area(1);
				bikeUseDao.updateUseBikeProcess(bikeUseVo,false);
				System.out.println("OutOfCityArea lock:"+bikeUseVo.getBikeVo().getNumber()+";date:"+bikeUseVo.getDate());
				return -1;

			}

			int closeWay = bikeUseVo.getCloseWay();
			String endTimeStr = bikeUseVo.getEndTime();

			// 3. persist lock status and finish bike ride

			bikeUseVo = bikeUseDao.getUseIngDetailInfo(ValueUtil.getInt(userId), ValueUtil.getLong(bikeUseVo.getDate()));
			if(bikeUseVo == null){
				//check current riding again!
				bikeUseVo = bikeUseDao.getUseIngDetailInfo(ValueUtil.getInt(userId),0);
				if(bikeUseVo == null){
					return 0;
				}

			}
			bikeUseVo.setOldDuration(oldDuration);
			bikeUseVo.setAdminId(adminId);
			bikeUseVo.setEndTime(endTimeStr);
			if(inArea){
				//定位时恢复位置，解除越界状态/remove out area status where location right
				bikeUseVo.setOut_area(0);
			}



			if(ValueUtil.getLong(bikeUseVo.getStartTime()) == 0){
				System.out.println("unnormal ride end,start time is 0:"+bikeUseVo.getDate()+","+new Date().toLocaleString());
				bikeUseVo.setStartTime(bikeUseVo.getDate());
			}

			UserVo user = userDao.getUserWithIndustryInfo(bikeUseVo.getUid());

			long endTime = ValueUtil.getInt(bikeUseVo.getEndTime()) == 0
					? TimeUtil.getCurrentLongTime()
							: ValueUtil.getLong(bikeUseVo.getEndTime());



					if(bikeUseVo.getEndLat() != 0 && bikeUseVo.getEndLng() != 0){
						new BikeDaoImpl().updateGps(
								ValueUtil.getLong(bikeUseVo.getBikeVo().getImei()),
								bikeUseVo.getEndLat(),
								bikeUseVo.getEndLng()
								);
					}

					bikeUseVo.setEndTime(endTime+"");

					boolean rideEnd = false;

					// 4. handle payment processing
					boolean payFlag = false;
					final TradeVo tradeVo = new TradeVo();
					if(user!=null) {
						//init trade amount
						tradeVo.setAmount(new BigDecimal(0));
						// 4. a. build trade object

						tradeVo.setUid(bikeUseVo.getUid());

						String freeCouponId = CouponBo.haveTimeFreeCoupon(cityId);

						if(bikeUseDao.getRideIngCountWithUser(userId,null,true) == 1){
							rideEnd = true;
						}


						if(!StringUtils.isEmpty(freeCouponId)){
							// city coupon
							tradeVo.setOut_pay_id(freeCouponId);
							tradeVo.setWay(TradeVo.Trade_PayWay_Coupon);
							bikeUseVo.setRideAmount(new BigDecimal(0));
						}else {

							BigDecimal bikePrice = bikeUseVo.getBikeVo().getTypeVo().getPrice();
							BigDecimal lowCreditPrice = CreditScoreBo.getUserCreditPrice(user.getCredit_score());

							if(lowCreditPrice != null){
								bikePrice = lowCreditPrice;
							}

							// get usage count modulous bike-type-count
							int numUnits = getUseTimeCount(
									bikeUseVo.getStartTime(),
									bikeUseVo.getEndTime(),
									bikeUseVo.getBikeVo().getTypeVo().getCount()
									);
							//Todo: get the time and identify whether the location has a promotion or not
							
							
							int freeMinutes = 0;// applyPromotion(cityId + "");
							
							int usedMinutes = TimeUtil.getMinutes(bikeUseVo.getStartTime(), bikeUseVo.getEndTime()); 
							
							if (freeMinutes >= usedMinutes) {
								numUnits = 0;
							}

							// calculate cost
							BigDecimal bikeUseMoney = bikePrice.multiply(new BigDecimal(numUnits));
							if(bikeUseMoney.compareTo(SystemConfigBo.SYSTEM_CONFIG_RIDE_AMOUNT_MAX) == 1){
								bikeUseMoney = SystemConfigBo.SYSTEM_CONFIG_RIDE_AMOUNT_MAX;
							}

							bikeUseVo.setRideAmount(bikeUseMoney);
							tradeVo.setAmount(bikeUseMoney);
							if(rideEnd){
								//get ride list
								if(bikeUseVo.getGroupRide() == 1){
									System.out.println("check group ride");
									List<BikeUseVo> rideList = bikeUseDao.getRideListWithHost(bikeUseVo.getHostId()== 0 
											?bikeUseVo.getId():bikeUseVo.getHostId()+"",false);
									for (BikeUseVo rideVo : rideList) {
										//add total ride amount
										tradeVo.setAmount(tradeVo.getAmount().add(rideVo.getRideAmount()));
									}	
								}													

								// adjust by user price programs
								applyMembership(user, bikeUseVo, tradeVo, bikePrice);

								while (true) {
									if (getReadPack(bikeUseVo, tradeVo, numUnits, bikePrice)) {
										break;

									}
									if (getLongLease(tradeVo)) {
										break;
									}
									getCoupon(tradeVo, bikePrice);
									break;
								}
							}

						}

						if(rideEnd){
							// 4. b. charge
							boolean wasFree = tradeVo.getAmount().compareTo(new BigDecimal(0)) <= 0;

							if(wasFree){
								payFlag = true;
							}else{

								payFlag = PayBo.accountPay(user, tradeVo);

								if(tradeVo.getWay() == 0){
									// set the payment method to from-account
									tradeVo.setWay(TradeVo.Trade_PayWay_Account);
								}

							}							

							// 4. c. persist trade
							tradeVo.setRecordId(bikeUseVo.getHostId() ==0 ?bikeUseVo.getId():bikeUseVo.getHostId()+"");
							tradeVo.setStatus(payFlag? 1: 0);
							tradeVo.setCityId(cityId);

							if(tradeDao.addBikeTrade(tradeVo)){
								// update credit rating
								CreditScoreBo.addScoreRecord(tradeVo.getUid(), 1, 4);
							}
						}

					}

					bikeUseVo.setRideStatus(payFlag?3:2);
					if(payFlag){
						//update ride history pay success
						bikeUseDao.updatePaySuccess(tradeVo.getRecordId());
					}
					bikeUseDao.finishUseBike(bikeUseVo,endTime,closeWay);
					final String rideId = bikeUseVo.getId();
					final String rideUser = bikeUseVo.getRideUser();
					final boolean rideEndTag = rideEnd;
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							Map<String, String> dicMap = PushUtil.getPushCustomerDictionary(PushType.Push_Type_Ride_Change);
							if(!rideEndTag){
								dicMap.put("rideId",rideId);
								dicMap.put("rideUser",rideUser);
							}
							//通知结费
							UserDeviceVo deviceVo = new UserDeviceDaoImpl().getDeviceInfo(userId);
							//push_ride_part_end_content
							PushUtil.push(deviceVo, LanguageUtil.getDefaultValue("push_ride_end_title"), 
									LanguageUtil.getDefaultValue("push_ride_end_content",new Object[]{tradeVo.getAmount()}),dicMap
									);

							//设置用户所在区域为本次骑行区域/set user area is bike area
							if(cityId > 0 && new UserDaoImpl().updateCity(userId, cityId)){
								UserDeviceVo deviceInfo = new UserDeviceDaoImpl().getDeviceInfo(userId);
								if(deviceInfo != null && !StringUtils.isEmpty(deviceInfo.getRequestToken())){
									ParamsFilter.setTokenStatus(deviceInfo.getRequestToken(), 4);
								}

							}

						}
					}).start();

					return payFlag == true? 1: 0;	
		}

	}

	/**
	 * 
	 * @Title:        getUseTime 
	 * @Description:  获取使用时长 / Calculate trip time
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月6日 下午4:31:40
	 */
	private int getUseTimeCount(String start,String end,int minMinutes){
		if(minMinutes == 0){
			minMinutes = 30;
		}
		int minute = TimeUtil.getMinutes(start, end);  // 计算出分钟 / Calculate minutes
		int num = 0; 
		num = minute/minMinutes+(minute%minMinutes > 0?1:0);
		if(num == 0){
			num = 1;
		}
		return num;
	}

	/**
	 * 
	 * @Title:        getLongLease 
	 * @Description:  获取长租订单/get long rent order
	 * @param:        @param tradeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月6日 下午3:08:11
	 */
	private boolean getLongLease(TradeVo tradeVo){
		BikeLongLeaseVo leaseVo = new BikeLongLeaseDaoImpl().getUserLeaseInfo(tradeVo.getUid());
		if(leaseVo == null){
			return false;
		}
		tradeVo.setOut_pay_id(leaseVo.getId());
		tradeVo.setWay(TradeVo.Trade_PayWay_LongLease);
		tradeVo.setAmount(new BigDecimal(0));//设置费用为0/set up fees as 0
		return true;
	}


	private boolean applyMembership (
			UserVo user, BikeUseVo bikeUse, TradeVo trade, BigDecimal bikePrice)
	{

		UserMembershipVO userPlan = new MembershipPlanDAO().getActiveUserMembershipsForUser(user);

		if (userPlan == null) {
			return false;
		}

		int freeSeconds = userPlan.getPlan().getFreeMinutes() * 60;

		long startTimestamp = ValueUtil.getLong(bikeUse.getStartTime());
		long endTimestamp = ValueUtil.getLong(bikeUse.getEndTime());
		long adjustedEndTimestamp = endTimestamp - freeSeconds;

		int numUnits = adjustedEndTimestamp < startTimestamp
				? 0
						: getUseTimeCount(
								bikeUse.getStartTime(),
								adjustedEndTimestamp + "",
								bikeUse.getBikeVo().getTypeVo().getCount()
								);

		// System.out.println("membership: " + startTimestamp + ", " + endTimestamp + ", " + adjustedEndTimestamp + ", " + numUnits);
		BigDecimal chargeAmount = bikePrice.multiply(new BigDecimal(numUnits));

		trade.setOut_pay_id(userPlan.getId());
		trade.setWay(TradeVo.Trade_PayWay_Membership);
		trade.setAmount(chargeAmount);

		return true;

	}


	private int applyPromotion (String cityId)
	{
		int result = 0;

		PromotionVO promotion = new PromotionDaoImpl().findByCityId(cityId);

		if (promotion == null) return result;

		JsonObject body = new JsonParser().parse(promotion.getPolicy()).getAsJsonObject();
		JsonElement JsonFreeMinutes =  body.get("freeMinutes");
		

		if( JsonFreeMinutes == null)  return result;

		result += JsonFreeMinutes.getAsInt();
		


		return result;

	}

	/**
	 * 
	 * @Title:        getCoupon 
	 * @Description:  匹配用户优惠券信息/match user coupon information
	 * @param:        @param tradeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月6日 下午3:55:17
	 */
	private boolean getCoupon(TradeVo tradeVo,BigDecimal bikePrice){
		IUserCouponDao couponDao = new UserCouponDaoImpl();
		//		//优先匹配免费优惠券/priority match free coupon
		//		UserCouponVo couponVo = couponDao.getFreeCoupon(tradeVo.getUid());
		//		if(couponVo == null){
		//匹配额度优惠券/match amount coupon
		UserCouponVo couponVo = couponDao.getUserValueCoupon(tradeVo.getUid(), tradeVo.getAmount());
		if(couponVo == null){
			//折扣优惠券匹配/discount coupon match
			couponVo = couponDao.getUserPercentCoupon(tradeVo.getUid());

			if(couponVo == null){
				return false;
			}else{
				tradeVo.setAmount(tradeVo.getAmount().multiply(couponVo.getCouponVo().getValue().divide(new BigDecimal(100))));//折扣费用/dicount fee
				//修改优惠券为已使用/modify coupon as used
				couponDao.useCoupon(tradeVo.getOut_pay_id());
			}
		}else{

			if(couponVo.getCouponVo().getRepeat() == 1){//可重复使用/can use repeat
				//优惠券减去消费，修改余额/coupon reduce consumption,modify balance
				couponVo.setAmount(couponVo.getAmount().subtract(tradeVo.getAmount()));
				couponDao.updateCouponAmount(couponVo);
				if(couponVo.getAmount().compareTo(bikePrice) == -1){//余额已小于最小骑行/balance less then smalled riding
					couponDao.useCoupon(couponVo.getId());//标记为已使用/sign as used
				}
			}else{
				//修改优惠券为已使用/modify coupon as used
				couponDao.useCoupon(tradeVo.getOut_pay_id());
			}
			tradeVo.setAmount(new BigDecimal(0));//定额优惠，设置费用为0/quota discount, set up fee as 0
		}
		//		}else{
		//			tradeVo.setAmount(new BigDecimal(0));//免费骑行，设置费用为0/free cycling, set up fee as 0
		//		}

		tradeVo.setOut_pay_id(couponVo.getId());
		tradeVo.setWay(TradeVo.Trade_PayWay_Coupon);


		return true;
	}

	/**
	 * 
	 * @Title:        getReadPack 
	 * @Description:  红包处理 / luck bike match
	 * @param:        @param tradeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月27日 下午7:51:32
	 */
	private boolean getReadPack(BikeUseVo bikeVo,TradeVo tradeVo,int minutes,BigDecimal bikePrice){
		if(bikeVo.getBikeVo().getReadpack() == 0){
			return false;
		}
		//获取红包详情/get red envelopes details
		IRedPackBikeDao packDao = new RedPackBikeDaoImpl();
		RedPackBikeVo packVo = packDao.getNoUseRedPackBike(bikeVo.getBid());
		//获取规则详情/get rules details
		RedPackRuleVo ruleVo = new RedPackRuleDaoImpl().getRule(packVo.getRule_id());
		if(ruleVo.getType() == 1){
			//现金红包/cash red envelopes
			int newMinutes = 0;
			if(minutes > ValueUtil.getInt(ruleVo.getFree_use_time())){
				//减去优惠骑行时间/reduce riding time
				newMinutes = minutes - ValueUtil.getInt(ruleVo.getFree_use_time());
			}
			//重新计算骑行费用/caculate riding fee renew
			BigDecimal bikeUseMoney = bikePrice.multiply(new BigDecimal(newMinutes));
			tradeVo.setAmount(bikeUseMoney);
			tradeVo.setOut_pay_id(packVo.getId());
			tradeVo.setWay(TradeVo.Trade_PayWay_Redpack);
			BigDecimal redpack = new BigDecimal(0);
			if(minutes >= ValueUtil.getInt(ruleVo.getLeast_use_time())){//满足红包单车最低骑行时间要求/meet red envelopes bike lowest riding time demand
				//生成红包/generate red envelopes
				redpack = new BigDecimal( new Random().nextInt(ruleVo.getMax_amount().intValue()*100)/100.00);
			}
			//创建红包，并设置红包单车为普通单车（触发器）/creat red envelope,set red envelope bike as ordinary bike(trigger)
			packDao.createRedPack(bikeVo.getUid(), packVo.getId(), redpack);
		}else if(ruleVo.getType() == 2){
			//优惠券红包/coupon red envelope 
			boolean gift = false;
			if(ruleVo.getMust_in_area() == 1 && !StringUtils.isEmpty(ruleVo.getArea_ids())){
				//校验是否停放至指定区域/check whether parking area ruled
				List<AreaVo> list = new AreaDaoImpl().getAreaList(ruleVo.getArea_ids(),bikeVo.getEndLng(),bikeVo.getEndLat(),0);
				for (AreaVo areaVo : list) {
					if(AMapUtil.checkPointInAreaWithDefaultRaidus(new LatLng(bikeVo.getEndLat(), bikeVo.getEndLng()), areaVo.getDetail())){
						gift = true;
						break;
					}
				}
			}else{
				gift = true;
			}
			//添加优惠券/add coupon
			if(gift && OthersSource.getSourceString("redpack_bike_coupon_num") != null){
				tradeVo.setOut_pay_id(packVo.getId());
				tradeVo.setWay(TradeVo.Trade_PayWay_Redpack);
				CouponBo.addSystemCouponForUser(bikeVo.getUid(), 
						ValueUtil.getInt(OthersSource.getSourceString("redpack_bike_coupon_num")),GiftFromType.LuckBike);
				//创建红包，并设置红包单车为普通单车（触发器）creat red envelope,set red envelope bike as ordinary bike(trigger)
				packDao.createRedPack(bikeVo.getUid(), packVo.getId(), new BigDecimal(0));
			}
		}
		return true;
	}


	/**
	 * 
	 * @Title:        getBikeOrderInfo 
	 * @Description:  获取骑行订单信息/get cycling order information
	 * @param:        @param tradeVo
	 * @param:        @return    
	 * @return:       JSONObject    
	 * @author        Albert
	 * @Date          2017年4月6日 下午4:11:09
	 */
	public JSONObject getBikeOrderInfo(TradeVo tradeVo){
		JSONObject object = new JSONObject();
		object.put("tradeVo", tradeVo);
		if(tradeVo.getOut_pay_id() != null){
			if(tradeVo.getWay() == TradeVo.Trade_PayWay_LongLease){
				//获取长租优惠信息/obtain long rent dicount information
				BikeLongLeaseVo leaseVo = new BikeLongLeaseDaoImpl().getLeaseInfo(tradeVo.getOut_pay_id());
				if(leaseVo != null){
					object.put("leaseVo", leaseVo);
				}
			}else if(tradeVo.getWay() == TradeVo.Trade_PayWay_Coupon){
				//获取优惠劵信息/obtain coupon information
				UserCouponVo couponVo = new UserCouponDaoImpl().getUserCoupon(tradeVo.getOut_pay_id());
				if(couponVo != null){
					couponVo.setUserVo(null);
					object.put("couponVo", couponVo);
				}
			}else if(tradeVo.getWay() == TradeVo.Trade_PayWay_Redpack){
				//获取红包信息/obtain red envelopes information
				RedPackBikeVo packVo =  new RedPackBikeDaoImpl().getRedPackInfo(tradeVo.getOut_pay_id());
				if(packVo != null){
					object.put("redpackVo", packVo);
				}
			}
		}

		if(tradeVo.getStatus() == 0){
			UserVo userVo = new UserDaoImpl().getUserWithIndustryInfo(tradeVo.getUid());
			BigDecimal balance = userVo.getMoney().add(userVo.getGiftMoney());
			if(balance.compareTo(tradeVo.getAmount()) >= 0){
				//标记可用余额支付/tag balance pay
				object.put("balancePay", 1);
			}else{
				object.put("balancePay", 0);
			}
			object.put("balance", balance);
		}

		List<BikeUseVo> rideList = new BikeUseDaoImpl().getRideListWithHost(tradeVo.getRecordId(),true);
		if(rideList.size() > 0){
			object.put("rideList", rideList);
		}

		return object;
	}

	/**
	 * 
	 * @Title:        getUseingBikeInfo 
	 * @Description:  获取使用中的单车信息/obtain bike information are using
	 * @param:        @param useVo
	 * @param:        @param price    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 下午4:42:50
	 */
	public BikeUseVo getUseingBikeInfo(BikeUseVo useVo,BikeVo bikeVo){
		String endTime = TimeUtil.getCurrentLongTimeStr();
		
		//计算已骑行时长/caculate time of riding
//		useVo.setDuration(TimeUtil.getMinutes(useVo.getStartTime(),nowTime ));
		int timeUnit = getUseTimeCount(useVo.getStartTime(), endTime,bikeVo.getTypeCount());
		//计算骑行费用/caculate cycling fee
		BigDecimal bikeUseMoney = bikeVo.getPrice().multiply(new BigDecimal(timeUnit));
		useVo.setAmount(bikeUseMoney);
		useVo.setCalorie(ValueUtil.getCalorie(useVo.getDuration(), useVo.getDistance()));
		useVo.setCarbon(ValueUtil.getCarbon(useVo.getDuration(), useVo.getDistance()));
		useVo.setStatus(bikeVo.getStatus());
		useVo.setReadpack(bikeVo.getReadpack());
//		int duration = ValueUtil.getInt((ValueUtil.getLong(nowTime)-ValueUtil.getLong(useVo.getStartTime()))/60);
		if(useVo.getRideStatus() == 1){
			useVo.setDuration(ValueUtil.getInt(TimeUtil.getCurrentLongTime()-ValueUtil.getLong(useVo.getStartTime())));
		}
		return useVo;
	}



	/**
	 * 
	 * @Title:        checkBikeIsConnected 
	 * @Description:  判定设备是否连接正常/judge device whether connect normal
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月27日 下午2:36:39
	 */
	public boolean checkBikeIsConnected(long imei){
		if(TCPService.getIoSession(imei) == null ){//连接不存在/connection not exist
			return false;
		}

		//判定是否需要保存指令/judge whether need keep command
		if("1".equals(OthersSource.getSourceString("save_lock_order"))){
			IBikeOrderRecord orderDao = new BikeOrderRecordDaoImpl();
			//指令是否正常/command whether normal
			return orderDao.haveRecordInMinutes(BIKE_CONNECT_CHECK_SECONDS, imei);
		}else{
			return true;
		}

	}


	/**
	 * 
	 * @Title:        autoFinishBikeUseWithError 
	 * @Description:  TODO
	 * @param:        @param useVo
	 * @param:        @param checkArea
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2018年4月25日 下午8:37:51
	 */
	public int autoFinishBikeUseWithError(BikeUseVo useVo,boolean checkArea){

		if(useVo == null){
			return -1;
		}

		//		BikeUseDaoImpl useDao = new BikeUseDaoImpl();
		int duration = useVo.getDuration();	

		if("0".equals(useVo.getStartTime())){
			//未开始骑行，即异常开锁，直接删除/not riding, abnormal unlock,delete directly
			BaseDao.deleteRecord(IBikeUseDao.TABLE_NAME, useVo.getId());
			return 1;
		}
		
		if(useVo.getDuration() > 0){
			useVo.setEndTime(useVo.getDuration());
			//admin finish,record old time
			if(useVo.getAdminId() != null){
				useVo.setOldDuration(TimeUtil.getMinutes(useVo.getStartTime(), TimeUtil.getCurrentLongTimeStr()));
			}

		}else{
			long endTime = ValueUtil.getLong(useVo.getStartTime()) + SystemConfigBo.SYSTEM_CONFIG_RIDE_ADMIN_END_TIME*60;
			if(endTime > TimeUtil.getCurrentLongTime()){//结束时间不能大于当前时间/finish time can't more than present time
				endTime = TimeUtil.getCurrentLongTime();
			}
			useVo.setEndTime(endTime+"");
		}
		//		useVo = useDao.getUseIngDetailInfo(null, useVo.getUid());
		useVo.setDuration(duration);		
		if(useVo.getAdminId() != null){
			useVo.setAdminId(useVo.getAdminId());
			useVo.setCloseWay(2);		
		}

		return dealBikeOrder(useVo, false) == 1?1:0;
		
		/*//admin end ride,finish group ride
		List<BikeUseVo> rideList = bikeUseDao.getRideListWithHost(useVo.getHostId()== 0 
									?useVo.getId():useVo.getHostId()+"",false);
		for (BikeUseVo bikeUseVo : rideList) {
			if(bikeUseVo.getDuration() > 0){
				bikeUseVo.setEndTime(bikeUseVo.getDuration());
				//admin finish,record old time
				if(useVo.getAdminId() != null){
					bikeUseVo.setOldDuration(TimeUtil.getMinutes(bikeUseVo.getStartTime(), TimeUtil.getCurrentLongTimeStr()));
				}

			}else{
				long endTime = ValueUtil.getLong(bikeUseVo.getStartTime()) + AUTO_FINISH_BIKE_USE_MINUTES*60;
				if(endTime > TimeUtil.getCurrentLongTime()){//结束时间不能大于当前时间/finish time can't more than present time
					endTime = TimeUtil.getCurrentLongTime();
				}
				bikeUseVo.setEndTime(endTime+"");
			}
			//		useVo = useDao.getUseIngDetailInfo(null, useVo.getUid());
			bikeUseVo.setDuration(duration);		
			if(useVo.getAdminId() != null){
				bikeUseVo.setAdminId(useVo.getAdminId());
				bikeUseVo.setCloseWay(2);		
			}
	
			dealBikeOrder(bikeUseVo, false);
		}
		return 1;*/
	}

	/**
	 * 判定指定车辆有无使用异常/judge bike whether abnormal use
	 * @Title:        haveUsedInTime 
	 * @Description:  TODO
	 * @param:        @param bid
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月5日 上午11:45:33
	 */
	public boolean haveUsedInTime(String bid,int idleDay) {
		int tempDay = BIKE_NOT_USED_IN_DAYS;
		if(idleDay > 0){
			tempDay = idleDay;
		}
		long time = TimeUtil.getCurrentLongTime()-tempDay*24*60*60;
		return new BikeUseDaoImpl().haveUsedInTime(bid, time);
	}

	/**
	 * 
	 * @Title:        checkBikeIsInCityArea 
	 * @Description:  检查单车是否越界/check bike whether transboundary
	 * @param:        @param cityId
	 * @param:        @param position
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月7日 下午3:34:49
	 */
	public boolean checkBikeIsInCityArea(int cityId,LatLng position){
		////强制停车区域/Forced parking area
		List<AreaVo> areaList =  new AreaDaoImpl().getAreaList(cityId, position,3);
		if(areaList.size() > 0){
			for (AreaVo areaVo : areaList) {
				if(!StringUtils.isEmpty(areaVo.getDetail()) && position.getLatitude() != 0 && position.getLongitude() != 0){
					boolean result = AMapUtil.checkPointInArea(position, areaVo.getDetail(),AMapUtil.getBorderRaidus("forced_park_area_border_distance"));
					if(result){
						return true;
					}
				}

			}
			return false;
			//City area
		}else{
			CityVo cityVo = new CityDaoImpl().getCityInfo(cityId+"");
			if(cityVo == null || StringUtils.isEmpty(cityVo.getArea_detail()) || cityVo.getArea_detail().trim().equals("")
					|| (position.getLatitude() == 0 && position.getLongitude() == 0)){
				return true;
			}
			return AMapUtil.checkPointInAreaWithDefaultRaidus(position, cityVo.getArea_detail());
		}

	}

	/**
	 * 
	 * @Title:        checkBikeInProhibitedParkingArea 
	 * @Description:  禁停区/Check Bike In Prohibited Parking Area
	 * @param:        @param cityId
	 * @param:        @param position
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年10月23日 上午11:03:21
	 */
	public boolean checkBikeInProhibitedParkingArea(int cityId,LatLng position){
		List<AreaVo> areaList =  new AreaDaoImpl().getAreaList(cityId, position,2);

		for (AreaVo areaVo : areaList) {
			if(!StringUtils.isEmpty(areaVo.getDetail()) && position.getLatitude() != 0 && position.getLongitude() != 0){
				boolean result = AMapUtil.checkPointInArea(position, areaVo.getDetail());
				if(result){//禁止停车区域/Prohibited parking
					return true;//In Prohibited parking area
				}
			}

		}
		return false;
	}


	/**
	 * 
	 * @Title:        getBikeUseRouleInfo 
	 * @Description:  获取骑行路线信息（适用于路径批量更新）/get riding route information(suitable for route update batch)
	 * @param:        @param lats
	 * @param:        @param lngs
	 * @param:        @param bikeUseVo
	 * @param:        @param cityId    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月11日 下午1:48:32
	 */
	public boolean getBikeUseRouleInfo(String[] lats,String[] lngs,BikeUseVo bikeUseVo,int cityId){

		if(lats == null || lats.length == 0){
			return false;
		}

		double lat = 0,lng = 0;
		//为了提高准确性，距离和路径需要遍历，2点计算/to improve accurate,distance and route need,2 point caculate
		for (int i = 0; i < lngs.length; i++) {
			lat = ValueUtil.getDouble(lats[i]);
			lng = ValueUtil.getDouble(lngs[i]);
			bikeUseVo.setCurrentLat(lat);
			bikeUseVo.setCurrentLng(lng);
			//计算骑行距离/caculate riding distance
			bikeUseVo.setDistanceWithStartAndEnd();
			//生成路径/generate the path
			bikeUseVo.setOrbitWithPoint(lat,lng);
		}

		//骑行越界判断（最后一个位置）/riding transboundary judge(last position)
		LatLng postion = new LatLng(lat, lng);
		if(checkBikeInProhibitedParkingArea(cityId, postion)){
			bikeUseVo.setOut_area(2);//in Prohibited parking
		}else if(!checkBikeIsInCityArea(cityId, postion)){
			bikeUseVo.setOut_area(1);//out of area
		}else{
			bikeUseVo.setOut_area(0);//normal
		}

		return true;

	}

	/**
	 * 
	 * @Title:        bikeIsErrorStatus 
	 * @Description:  检查锁是否为故障状态/check lock whether is error status
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月15日 上午10:57:19
	 */
	public static boolean bikeIsErrorStatus(int status){
		if(status == 2 || status == 4){
			return true;
		}
		return false;
	}

	public static void autoSetBikeError(String number){
		int count = new BikeErrorDaoImpl().getWaitForDealCount(number,true).size();
		if(count == AUTO_SET_BIKE_ERROR_COUNT){
			new BikeDaoImpl().updateBikeErrorStatus(number, 2);
		}
	}


	/**
	 * 
	 * @Title:        getUserLeftReserverCountInDay 
	 * @Description:  获取当日取消预约剩余次数
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年11月3日 下午3:07:25
	 */
	public static int getUserLeftReserverCountInDay(String userId){
		return BikeReserveVo.Reserve_Cancel_Count_In_Day-new BikeReserveDaoImpl().getUserCancelCountInDay(userId);
	}





	/************检查服务开始/check service start*************/

	/**
	 * 
	 * @Title:        startBikeCheckService 
	 * @Description:  开启单车业务检查服务/start bike check service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:32:46
	 */
	public void startBikeCheckService(){
		startBikeReserveService();

		startBikeHoldService();

		startCheckRidingService();

		/*INotificationConfigDao configDao = new NotificationConfigDaoImpl();

		NotificationConfigVo heartConfig = configDao.getConfigInfoByType(3);
		if(heartConfig != null && heartConfig.getJsonValue() > 0 && heartConfig.getJsonFrequency() > 0){
			startBikeHeartService(false);
		}

		NotificationConfigVo locationConfig = configDao.getConfigInfoByType(4);
		if(locationConfig != null && locationConfig.getJsonValue() > 0 && locationConfig.getJsonFrequency() > 0){
			startBikeLocationService(false);
		}


		NotificationConfigVo powerConfig = configDao.getConfigInfoByType(6);
		if(powerConfig != null && powerConfig.getJsonValue() > 0 && powerConfig.getJsonFrequency() > 0){
			startBikePowerService(false);
		}*/

	}

	/**
	 * 
	 * @Title:        startBikeReserveService 
	 * @Description:  开启单车预约检查服务/start bike reserve service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:33:42
	 */
	public void startBikeReserveService(){
		Runnable runnable = new Runnable() {  
			public void run() {  
				// task to run goes here  
				checkReserveBike();
			}  
		};
		ScheduledExecutorService service = Executors  
				.newSingleThreadScheduledExecutor();  
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间/second parameter is first carry out delay time, third parameter is  timed execution interval time
		service.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.SECONDS); 
	}


	public void startBikeHoldService () {

		System.out.println("start bike hold service");
		final HoldBO holdBO = new HoldBO();
		Runnable runnable = new Runnable() {  
			public void run() {  
				holdBO.checkExpiredHolds();
			}  
		};
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();  
		service.scheduleAtFixedRate(runnable, 10, 10, TimeUnit.SECONDS);

	}

	/**
	 * 
	 * @Title:        checkReserveBike 
	 * @Description:  检查预约中的单车/check booking bike
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 下午8:53:49
	 */
	public void checkReserveBike(){
		IBikeReserveDao reserveDao = new BikeReserveDaoImpl();
		List<BikeReserveVo> list = reserveDao.getReserveListInTwoMinutes();
		long nowTime = TimeUtil.getCurrentLongTime();
		for (BikeReserveVo bikeReserveVo : list) {
			if( nowTime - ValueUtil.getLong(bikeReserveVo.getDate())  >= BikeReserveVo.Reserve_TimeOut * 60){
				//已过期，删除预约记录/expired,delete book record
				//				reserveDao.cancelReserveBike(bikeReserveVo.getUid());
				reserveDao.updateReserveStatus(bikeReserveVo.getUid(), 4);
				System.out.println("预约已被删除："+bikeReserveVo.getRid());
			}else{
				//即将过期，进行提示/about to expire,have prompt 
				System.out.println("预约即将过期："+bikeReserveVo.getRid());
			}
		}
	}

	/**
	 * 
	 * @Title:        startBikeHeartService 
	 * @Description:  开启单车心跳检查服务/start bike heart service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:37:22
	 */
	public void startBikeHeartService(boolean restart){
		Runnable runnable = new Runnable() {  
			public void run() {  
				// task to run goes here  
				checkBikeHeart();
			}  
		};
		if(restart && heartCheckService != null){
			heartCheckService.shutdownNow();
		}
		heartCheckService = Executors  
				.newSingleThreadScheduledExecutor();  
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间/second parameter is first carry out delay time, third parameter is  timed execution interval time
		heartCheckService.scheduleAtFixedRate(runnable, 1, NotificationConfigVo.HEART_MONITORING_FREQUENCY, TimeUnit.HOURS); 
	}

	/**
	 * 
	 * @Title:        stopBikeHeartService 
	 * @Description:  停止单车心跳检查服务/stop bike heart service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午4:45:11
	 */
	public void stopBikeHeartService(){
		if(heartCheckService != null){
			heartCheckService.shutdownNow();
		}
	}

	/**
	 * 
	 * @Title:        restartBikeHeartService 
	 * @Description:  重设监听参数/reset service property
	 * @param:        @param interval
	 * @param:        @param frequency    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:58:30
	 */
	public void restartBikeHeartService(int interval,int frequency){
		NotificationConfigVo.HEART_STATISTICS_INTERVAL = interval;
		if(NotificationConfigVo.HEART_MONITORING_FREQUENCY != frequency){
			NotificationConfigVo.HEART_MONITORING_FREQUENCY = frequency;
			//restart service
			startBikeHeartService(true);
		}
	}

	/**
	 * 
	 * @Title:        checkBikeHeart 
	 * @Description:  心跳监测/bike heart check
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 上午11:59:22
	 */
	public void checkBikeHeart(){
		List<BikeVo> bikeList = bikeDao.getNormalBikeList();
		int unNormalCount = 0;

		for (BikeVo bikeVo : bikeList) {
			if(ValueUtil.getLong(bikeVo.getHeartTime())+NotificationConfigVo.HEART_STATISTICS_INTERVAL*60 < TimeUtil.getCurrentLongTime()){
				unNormalCount++;
			}
		}

		if(unNormalCount > 0){
			notifyBo.sendNotifyToAdmin(NotificationType.Bike_Heart, null, null, unNormalCount);
		}
	}


	/**
	 * 
	 * @Title:        startBikeHeartService 
	 * @Description:  开启单车定位检查服务/start bike location service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:37:22
	 */
	public void startBikeLocationService(boolean restart){
		Runnable runnable = new Runnable() {  
			public void run() {  
				// task to run goes here  
				checkBikeLocation();
			}  
		};
		if(restart  && locationCheckService != null){
			locationCheckService.shutdownNow();
		}
		locationCheckService = Executors  
				.newSingleThreadScheduledExecutor();  
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间/second parameter is first carry out delay time, third parameter is  timed execution interval time
		locationCheckService.scheduleAtFixedRate(runnable, 2, NotificationConfigVo.LOCATION_MONITORING_FREQUENCY, TimeUnit.HOURS); 
	}

	/**
	 * 
	 * @Title:        stopBikeLocationService 
	 * @Description:  停止单车定位检查服务/stop bike location service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午4:45:11
	 */
	public void stopBikeLocationService(){
		if(locationCheckService != null){
			locationCheckService.shutdownNow();
		}
	}

	/**
	 * 
	 * @Title:        restartBikeLocaltionService 
	 * @Description:  重设监听参数/reset service property
	 * @param:        @param interval
	 * @param:        @param frequency    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:59:54
	 */
	public void restartBikeLocaltionService(int interval,int frequency){
		NotificationConfigVo.LOCATION_STATISTICS_INTERVAL = interval;
		if(NotificationConfigVo.LOCATION_MONITORING_FREQUENCY != frequency){
			NotificationConfigVo.LOCATION_MONITORING_FREQUENCY = frequency;
			//restart service
			startBikeLocationService(true);
		}
	}

	/**
	 * 
	 * @Title:        checkBikeLocation 
	 * @Description:  定位检查/Location check
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:07:26
	 */
	public void checkBikeLocation(){
		List<BikeOrderRecord> orderList = bikeOrderDao.getLastOrderRecord(5);
		int unNormalCount = 0;
		for (BikeOrderRecord orderVo : orderList) {
			if(orderVo.getTime()+NotificationConfigVo.LOCATION_STATISTICS_INTERVAL*3600 < TimeUtil.getCurrentLongTime()){
				unNormalCount++;
			}
		}

		if(unNormalCount > 0){
			notifyBo.sendNotifyToAdmin(NotificationType.Bike_Location, null, null, unNormalCount);
		}
	}


	/**
	 * 
	 * @Title:        startBikeHeartService 
	 * @Description:  开启单车电量检查服务/start bike power service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:37:22
	 */
	public void startBikePowerService(boolean restart){
		Runnable runnable = new Runnable() {  
			public void run() {  
				// task to run goes here  
				checkBikeLowPower();
			}  
		};
		if(restart  && lowPowerCheckService != null){
			lowPowerCheckService.shutdownNow();
		}
		lowPowerCheckService = Executors  
				.newSingleThreadScheduledExecutor();  
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间/second parameter is first carry out delay time, third parameter is  timed execution interval time
		lowPowerCheckService.scheduleAtFixedRate(runnable, 1, NotificationConfigVo.LOW_POWER_FREQUENCY, TimeUnit.HOURS); 
	}

	/**
	 * 
	 * @Title:        stopBikePowerervice 
	 * @Description:  停止单车电量检查服务/stop bike power service
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午4:45:11
	 */
	public void stopBikePowerervice(){
		if(lowPowerCheckService != null){
			lowPowerCheckService.shutdownNow();
		}
	}

	/**
	 * 
	 * @Title:        restartBikePowerService 
	 * @Description:  重设监听参数/reset service property
	 * @param:        @param value
	 * @param:        @param frequency    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午2:59:54
	 */
	public void restartBikePowerService(int value,int frequency){
		NotificationConfigVo.LOW_POWER_VALUE = value;
		if(NotificationConfigVo.LOW_POWER_FREQUENCY != frequency){
			NotificationConfigVo.LOW_POWER_FREQUENCY = frequency;
			//restart service
			startBikePowerService(true);
		}
	}

	/**
	 * 
	 * @Title:        checkBikeLowPower 
	 * @Description:  低电量监测/low power check
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午12:04:28
	 */
	public void checkBikeLowPower(){
		List<BikeVo> bikeList = bikeDao.getNormalBikeList();
		int unNormalCount = 0;

		for (BikeVo bikeVo : bikeList) {
			if(bikeVo.getPowerPercent() < NotificationConfigVo.LOW_POWER_VALUE){
				unNormalCount++;
			}
		}

		if(unNormalCount > 0){
			notifyBo.sendNotifyToAdmin(NotificationType.Bike_Low_Power, null, null, unNormalCount);
		}
	}

	/**
	 * 
	 * @Title:        restartBikeCheckService 
	 * @Description:  重启单车监测服务/restart bike check service
	 * @param:        @param type
	 * @param:        @param value
	 * @param:        @param frequency    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午4:28:59
	 */
	public void restartBikeCheckService(final int type,final int value,final int frequency){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (type) {
				case 3:
					restartBikeHeartService(value, frequency);
					break;
				case 4:
					restartBikeLocaltionService(value, frequency);
					break;
				case 6:
					restartBikePowerService(value, frequency);
					break;
				default:
					break;
				}
			}
		}).start();

	}

	/**
	 * 
	 * @Title:        stopBikeCheckService 
	 * @Description:  停止单车监测服务/stop bike check service
	 * @param:        @param type    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月30日 下午4:48:01
	 */
	public void stopBikeCheckService(final int type){
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (type) {
				case 3:
					stopBikeHeartService();
					break;
				case 4:
					stopBikeLocationService();
					break;
				case 6:
					stopBikePowerervice();
					break;
				default:
					break;
				}
			}
		}).start();

	}

	/**
	 * 
	 * @Title:        startCheckRidingService 
	 * @Description:  Riding check service   
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年4月20日 下午3:44:58
	 */
	public void startCheckRidingService(){
		Runnable runnable = new Runnable() {  
			public void run() {  
				// task to run goes here  
				checkRiding();
			}  
		};

		ScheduledExecutorService ridingService = Executors  
				.newSingleThreadScheduledExecutor();  
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间/second parameter is first carry out delay time, third parameter is  timed execution interval time
		ridingService.scheduleAtFixedRate(runnable, 1,5, TimeUnit.MINUTES); 
	}


	public void checkRiding(){
		List<BikeUseVo> bikeList = bikeUseDao.getBikeUseingList();
		int unNormalCount = 0;

		for (BikeUseVo bikeUseVo : bikeList) {
			if(TimeUtil.getCurrentLongTime() - ValueUtil.getLong(bikeUseVo.getStartTime()) > 60*120){
				System.out.println("unnormal riding:+"+bikeUseVo.getId());
				notifyBo.sendNotifyToAdmin(NotificationType.Bike_Riding, bikeUseVo.getUid(), 
						bikeUseVo.getBikeVo().getNumber(), unNormalCount);
				//				unNormalCount++;
			}
		}

	}


	/************检查服务结束/check service end*************/

	/**
	 * 
	 * @Title:        sendBikeOrder 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param type 
	 * 1001	开锁
	 * 1002	位置获取
	 * 1003	电量获取
	 * 1004	版本
	 * 1005	找车
	 * 1006	关机
	 * 1007	蓝牙Mac地址获取   
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月19日 下午6:11:46
	 */
	public boolean sendBikeOrder(long imei,int type){
		try {
			BikeVo bikeVo = bikeDao.getBikeInfoWithImei(imei + "");
			if(StringUtils.isEmpty(bikeVo.getServerIp())){
				return false;
			}
			String serverUrl = String.format(
					OthersSource.LOCK_SERVER_REQUEST_URL, bikeVo.getServerIp());
			String params = String.format(
					"imei=%s&type=%s&token=%s&timestamp=%s", imei, type,
					OthersSource.LOCK_SERVER_REQUEST_TOKEN,
					TimeUtil.getCurrentLongTime());
			String result = HttpRequest.sendPost(serverUrl, params);
			System.out.println("sendBikeOrder result:"+result);
			if(StringUtils.isEmpty(result)){
				return false;
			}
			JSONObject object = JSONObject.parseObject(result);
			if(object.getIntValue("code") == 1){
				return true;
			}
			return false;
		} catch (Exception e) {
			// TODO: handle exception

		}
		return false;
	}

}
