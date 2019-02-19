/**
 * FileName:     IBikeReserve.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-25 下午5:05:47
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-25       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/inilization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.BikeReserveVo;

 /**
 * @ClassName:     IBikeReserve
 * @Description:单车预约业务接口/bike book business protocol 
 * @author:    Albert
 * @date:        2017-3-25 下午5:05:47
 *
 */
public interface IBikeReserveDao {
	
	/**触发器说明/trigger instruction
	 * tg_reserve_add insert 当新增一条设备的预约记录时，修改设备状态为预约中/when add a device reservation record, modify device status as booking
	 * uupdate t_bike set use_status = 2 where id = new.bid
	 * tg_reserve_update delete /**当一条设备的预约记录被删除时，修改设备状态为正常状态（自由/when a device reservation record is deleted, modify device status as normal status
	 * update t_bike set use_status = 0 where id = old.bid
	 * **/
	
	/**
	 * 
	 * @Title:        reserveBike 
	 * @Description:  预约预约单车/reserve bike
	 * @param:        @param userId
	 * @param:        @param bikeId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-25 下午5:10:04
	 */
	boolean reserveBike(String userId,String bikeId);
	
	/**
	 * 
	 * @Title:        cancelReserveBike 
	 * @Description:  取消预约单车/cancel reserve bike
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-25 下午5:09:52
	 */
	boolean cancelReserveBike(String userId);
	
	/**
	 * 
	 * @Title:        updateReserveStatus 
	 * @Description:  Update Reserve Status
	 * @param:        @param userId
	 * @param:        @param status 预约状态 0：待解锁 1：已解锁 2：已解其他锁 3：已取消 4：已超时
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月3日 下午2:30:51
	 */
	boolean updateReserveStatus(String userId,int status);
	
	/**
	 * 
	 * @Title:        getReserveInfo 
	 * @Description:  获取预约信息/obtain reserve information
	 * @param:        @param bid
	 * @param:        @return    
	 * @return:       BikeReserveVo    
	 * @author        Albert
	 * @Date          2017年4月1日 上午11:17:45
	 */
	BikeReserveVo getReserveInfo(String bid);
	
	/**
	 * 
	 * @Title:        getUserReserveInfo 
	 * @Description:  获取当前用户的预约信息/obtain current user reservation information
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       BikeReserveVo    
	 * @author        Albert
	 * @Date          2017年4月1日 下午2:03:11
	 */
	BikeReserveVo getUserReserveInfo(String userId);
	
	/**
	 * 
	 * @Title:        getUserCancelCountInDay 
	 * @Description:  Get User Cancel Count in day
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年11月3日 下午2:58:03
	 */
	int getUserCancelCountInDay(String userId);
	
	
	/**
	 * 
	 * @Title:        userHaveReserve 
	 * @Description:  判定用户是否有预约记录/judge user whether have reservation record
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月1日 上午11:45:53
	 */
	boolean userHaveReserve(String userId);
	
	/**
	 * 
	 * @Title:        getReserveListInTwoMinutes 
	 * @Description:  获取两分钟类到期的预约/obtain two minutes expire's reservation
	 * @param:        @return    
	 * @return:       List<BikeReserveVo>    
	 * @author        Albert
	 * @Date          2017年4月6日 下午8:42:29
	 */
	List<BikeReserveVo> getReserveListInTwoMinutes();
}
