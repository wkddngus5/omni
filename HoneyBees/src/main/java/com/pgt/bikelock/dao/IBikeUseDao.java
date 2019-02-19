/**
 * FileName:     IBikeUseDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-25 下午5:06:52
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-25       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;

 /**
 * @ClassName:     IBikeUseDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017-3-25 下午5:06:52
 *
 */
public interface IBikeUseDao {
	
	static final String TABLE_NAME= "t_bike_use";
	static final String VIEW_BIKE_USE = "view_bike_use_detail";
	
	/**触发器说明/trigger explian
	 * tg_bike_use_start insert 当开始一个设备的使用时，修改设备状态为正在解锁/when begin a device are using, modify device status as unlocking
	 * update t_bike set use_status = 3 where id = new.bid
	 * tg_bike_use_finish update 当完成一次骑行时，修改设备状态为正常状态（自由）;将结束时的时间、经纬度关联更新对应设备时间、经纬度；when finish a riding,modify device status as normal status(free);when finish, time,latitude and longitude update corresponding device time,latitude and longitude
	 * if(new.end_time != 0)
		then update t_bike set use_status = 0,g_time = new.end_time,g_lat = new.end_lat,g_lng = new.end_lng where id = new.bid;
		end if
	 * 
	 * **/
	
	
	
	/**
	 * 
	 * @Title:        startUseBike 
	 * @Description:  开始使用单车/begin use bike
	 * @param:        @param useVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-24 下午7:18:33
	 */
	boolean startUseBike(BikeUseVo useVo);
	
	/**
	 * 
	 * @Title:        updatePaySuccess 
	 * @Description:  修改订单状态为支付成功/modify order status as payment success
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月5日 上午11:44:28
	 */
	public boolean updatePaySuccess(String id);
	
	/**
	 * 
	 * @Title:        startUseBikeSuccess 
	 * @Description:  成功启用设备，开始使用/begin to use when enabled device succeed
	 * @param:        @param bikeId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-27 下午2:32:57
	 */
	boolean startUseBikeSuccess(String bikeId);
	
	/**
	 * 
	 * @Title:        startUseBikeSuccess 
	 * @Description:  成功启用设备，开始使用/begin to use when enabled device succeed
	 * @param:        @param uid
	 * @param:        @param date
	 * @param:        @param openWay open lock way 0:unknow 1:ble(app) 2:small program
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月13日 下午2:14:40
	 */
	boolean startUseBikeSuccess(int uid,long date,int openWay);
	
	/**
	 * 
	 * @Title:        finishUseBike 
	 * @Description:  更新使用单车进度（状态）/update bike schedule(status)
	 * @param:        @param useVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-24 下午7:18:59
	 */
	boolean updateUseBikeProcess(BikeUseVo useVo,boolean finish);
	
		/**
	 * 
	 * @Title:        updateRideInfo 
	 * @Description:  TODO
	 * @param:        @param useVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年10月27日 下午2:59:36
	 */
	boolean updateRideInfo(BikeUseVo useVo);
	
	
	/**
	 * 
	 * @Title:        updateUseBikeRequestDate 
	 * @Description:  更新骑行请求时间（用于越界骑行，重新解锁）/update riding apply time(for transboundary riding, renew unlock)
	 * @param:        @param useId
	 * @param:        @param requestTime
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月30日 下午5:54:30
	 */
	boolean updateUseBikeRequestDate(String useId,long requestTime);
	
	/**
	 * 
	 * @Title:        updateUseBikeRider 
	 * @Description:  TODO
	 * @param:        @param useId
	 * @param:        @param rider
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月22日 下午2:29:24
	 */
	boolean updateUseBikeRider(String useId,String rider);
	
	/**
	 * 
	 * @Title:        updateUseBikeUpdateTime 
	 * @Description:  TODO
	 * @param:        @param useId
	 * @param:        @param updateTime
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年6月22日 上午10:55:44
	 */
	boolean updateUseBikeUpdateTime (String useId ,long updateTime);
	
	/**
	 * 
	 * @Title:        updateRideIsGroup 
	 * @Description:  TODO
	 * @param:        @param useId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月1日 下午2:30:03
	 */
	boolean updateRideIsGroup (String useId);
	
	/**
	 * 
	 * @Title:        finishUseBike 
	 * @Description:  结束使用单车,更新结束时间/finish use bike, update finish time
	 * @param:        @param useId
	 * @param:        @param endTime
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-27 下午1:06:14
	 */
	boolean finishUseBike (BikeUseVo useVo,long endTime,int closeWay);
	
	/**
	 * 
	 * @Title:        finishUseBikeWithDefaultTime 
	 * @Description:   结束使用单车,结束时间为开始时间的3分钟/finish using bike, finish time as begin time's 3 minutes
	 * @param:        @param useId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月6日 下午5:44:01
	 */
	boolean finishUseBikeWithTime(String useId,int minutes);

	/**
	 * 
	 * @Title:        finishUseBike 
	 * @Description:  结束使用单车,更新位置等信息/finish using bike,update location information
	 * @param:        @param useVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-27 下午3:42:19
	 */
	boolean updateUseBikeEndLocation (BikeUseVo useVo);
	
	
	/**
	 * 
	 * @Title:        getUseInfo 
	 * @Description:  获取使用详情/obtain using information
	 * @param:        @param useId
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:08:00
	 */
	BikeUseVo getUseInfo(String useId);
	
	
	/**
	 * 
	 * @Title:        getUseInfo 
	 * @Description:  获取使用详情/obtain using information
	 * @param:        @param date
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年10月12日 下午12:02:19
	 */
	BikeUseVo getUseInfo(long date);
	
	/**
	 * 
	 * @Title:        getUseInfo 
	 * @Description:  获取使用信息/obtain using information
	 * @param:        @param userId
	 * @param:        @param bikeId
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年4月1日 下午3:35:34
	 */
	BikeUseVo getUseInfo(String userId,String bikeId);
	
	/**
	 * 
	 * @Title:        getFinalUseInfoWithNoLockLocation 
	 * @Description:  获取最后一条没有更新设备位置的骑行记录/obtain last one no update device postion's cycling record
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年6月29日 上午10:08:00
	 */
	BikeUseVo getFinalUseInfoWithNoLockLocation(long imei);
	
	/**
	 * 
	 * @Title:        getUseIngDetailInfo 
	 * @Description:  获取当前用户正在使用的单车信息/obtain current user is using bike information
	 * @param:        @param imei
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年7月13日 上午11:44:54
	 */
	BikeUseVo getUseIngDetailInfo(String imei,String userId);
	
	/**
	 * 
	 * @Title:        getUseIngDetailInfo 
	 * @Description:  获取当前用户正在使用的单车信息/obtain current user are using bike information
	 * @param:        @param userId
	 * @param:        @param date 解锁申请时间/unlocking apply time
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年7月13日 上午11:43:59
	 */
	BikeUseVo getUseIngDetailInfo(int userId,long date);

	/**
	 * 
	 * @Title:        getUseIngInfoWithUser 
	 * @Description:  TODO
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年4月1日 下午2:26:37
	 */
	BikeUseVo getUseIngInfoWithUser(String userId);
	
	/**
	 * 
	 * @Title:        getRideIngCountWithUser 
	 * @Description:  TODO
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2018年7月18日 下午3:15:28
	 */
	int getRideIngCountWithUser(String userId,String bnumber,boolean started);
	
	
	/**
	 * 
	 * @Title:        getRideListWithUser 
	 * @Description:  TODO
	 * @param:        @param userId
	 * @param:        @param rideStatus 0:all no paid 2:end
	 * @param:        @return    
	 * @return:       List<BikeUseVo>    
	 * @author        Albert
	 * @Date          2018年7月18日 下午5:16:53
	 */
	List<BikeUseVo> getRideListWithUser(String userId,int rideStatus);
	
	/**
	 * 
	 * @Title:        getRideListWithHost 
	 * @Description:  TODO
	 * @param:        @param hostId
	 * @param:        @return    
	 * @return:       List<BikeUseVo>    
	 * @author        Albert
	 * @Date          2018年7月18日 下午6:08:31
	 */
	List<BikeUseVo> getRideListWithHost(String hostId,boolean finish);
	
	
	/**
	 * 
	 * @Title:        getUseIngInfoWithBike 
	 * @Description:  TODO
	 * @param:        @param bikeNumber
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年5月22日 上午11:29:35
	 */
	BikeUseVo getUseIngInfoWithBike(String bikeNumber);
	
	/**
	 * 
	 * @Title:        getTodayUseInfo 
	 * @Description:  获取今日骑行信息/obtain cycling information today
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年4月5日 下午3:02:23
	 */
	BikeUseVo getTodayUseInfo(String userId);
	
	/**
	 * 
	 * @Title:        getTotalUseInfo 
	 * @Description:  获取总骑行信息/obtain total cycling information
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017年8月2日 下午2:20:38
	 */
	BikeUseVo getTotalUseInfo(String userId);
	
	/**
	 * 
	 * @Title:        getUseingStatusWithUser 
	 * @Description:  判断当前用户是否有正在使用的单车/judge current user whether are using bike
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean
	 * @author        Albert
	 * @Date          2017年4月1日 上午10:59:28
	 */
	boolean haveUseingBike(String userId);
	
	/**
	 * 
	 * @Title:        getMaxUseId 
	 * @Description:  获取最大使用记录ID/obtain larger use record id
	 * @param:        @param bid
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月1日 上午10:13:48
	 */
	String getMaxUseId(String bid);
	
	/**
	 * 
	 * @Title:        getBikeUseList 
	 * @Description:  骑行列表获取/riding sheet obtain
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<BikeUseVo>    
	 * @author        Albert
	 * @Date          2017年5月15日 下午3:57:46
	 */
	List<BikeUseVo> getBikeUseList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getCount 
	 * @Description:  获取数量/obtain quantity
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月11日 下午2:08:15
	 */
	int getCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getBikeUseList 
	 * @Description:  获取所有骑行集合/get all ride list
	 * @param:        @return    
	 * @return:       List<BikeUseVo>    
	 * @author        Albert
	 * @Date          2017年11月21日 上午11:31:18
	 */
	List<BikeUseVo> getBikeUseList(Long startDate,Long endDate);
	
	/**
	 * 
	 * @Title:        getBikeUseingList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<BikeUseVo>    
	 * @author        Albert
	 * @Date          2018年4月20日 下午3:46:35
	 */
	List<BikeUseVo> getBikeUseingList();
	

	/**
	 * 
	 * @Title:        getStatisticsList 
	 * @Description:  获取骑行统计/get ride statistics
	 * @param:        @param ids
	 * @param:        @return    
	 * @return:       List<StatisticsVo>    
	 * @author        Albert
	 * @Date          2017年11月21日 下午4:39:30
	 */
	List<StatisticsVo> getStatisticsList(String ids);
	
	/**
	 * 
	 * @Title:        haveUsedInTime 
	 * @Description:  判定指定车辆在指定时间有无使用/judge specify bike whether be use in specify time
	 * @param:        @param bid
	 * @param:        @param time
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月5日 上午11:40:21
	 */
	boolean haveUsedInTime(String bid,long time);
	
	
	/**
	 * 
	 * @Title:        haveOpenSuccess 
	 * @Description:  判断是否开锁成功 / To determine whether the success of unlocking
	 * @param:        @param time
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月26日 上午11:27:01
	 */
	boolean haveOpenSuccess(long time);
	
	/**
	 * 
	 * @Title:        setBikeUseRedPack 
	 * @Description:  设置骑行为红包单车/set up riding as red envelope bike
	 * @param:        @param useId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月2日 下午5:52:29
	 */
	boolean setBikeUseRedPack(String useId);
	
	/**
	 * 
	 * @Title:        deleteBikeUse 
	 * @Description:  open bike file,delete record
	 * @param:        @param time
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年10月14日 下午4:37:05
	 */
	boolean deleteBikeUse(long time);

	/**
	 * 
	 * @Title:        deleteBikeUse 
	 * @Description:  TODO
	 * @param:        @param time
	 * @param:        @param uid
	 * @param:        @param openFail
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年4月10日 下午4:35:29
	 */
	public boolean deleteBikeUse(long time,int uid,boolean openFail);
}
