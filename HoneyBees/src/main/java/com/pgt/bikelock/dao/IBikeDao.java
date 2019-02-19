
/**
 * FileName:     IBikeDao.java
 * @Description: 单车锁相关业务接口类/bike lock related business protocol type
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:17:36
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.RequestListVo;

/**
 * 
 * @ClassName:     IBikeDao
 * @Description:单车信息业务接口/bike lock information business protocol
 * @author:    Albert
 * @date:        2017-3-24 上午10:19:30
 *
 */
public interface IBikeDao {
	String TABLE_NAME="t_bike";
	String COLUMN_NUMBER="number";
	String COLUMN_IMEI="imei";
	
	
	/**
	 * @Title:        getNearBike 
	 * @Description:  获取周边单车信息/obtain nearby bike information
	 * @param:        @param bike
	 * @param:        @return    
	 * @return:       List<BikeVo>    
	 * @author        Albert
	 * @Date          2017-3-24 下午7:12:49
	 */
	List<BikeVo> getNearBike(BikeVo bike,String industryType);
	
	/**
	 * 
	 * @Title:        getBikeInfo 
	 * @Description:  获取单车信息/obtain bike lock information
	 * @param:        @param number
	 * @param:        @return    
	 * @return:       BikeVo    
	 * @author        Albert
	 * @Date          2017-3-27 上午11:02:18
	 */
	BikeVo getBikeInfoWithNumber(String number);
	
	/**
	 * 
	 * @Title:        getBikeInfoWithImei 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       BikeVo    
	 * @author        Albert
	 * @Date          2017-3-27 上午11:38:59
	 */
	BikeVo getBikeInfoWithImei(String imei);
	
	/**
	 * 
	 * @Title:        getBikeDetailInfoWithImei 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       BikeVo    
	 * @author        Albert
	 * @Date          2018年8月27日 下午5:45:45
	 */
	BikeVo getBikeDetailInfoWithImei(long imei);
	
	/**
	 * 
	 * @Title:        getBikeWithTypeInfo 
	 * @Description:  获取价格信息，关联类型信息/obtain price information, connnect type information
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       BikeUseVo    
	 * @author        Albert
	 * @Date          2017-3-28 上午11:05:43
	 */
	BikeVo getBikeWithTypeInfo(String imei);

	/**
	 * 
	 * @Title:        getBikeInfo 
	 * @Description:  获取单车详情/obtain bike information
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       BikeVo    
	 * @author        Albert
	 * @Date          2017年4月7日 下午5:23:13
	 */
	BikeVo getBikePriceInfo(String id);
	
	/**
	 * 
	 * @Title:        getBikeDetailInfo 
	 * @Description:  TODO
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       BikeVo    
	 * @author        Albert
	 * @Date          2018年11月6日 下午6:33:22
	 */
	BikeVo getBikeDetailInfo(String id);
	
	/**
	 * 
	 * @Title:        getLockExtendInfo 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @return    
	 * @return:       JSONObject    
	 * @author        Albert
	 * @Date          2018年8月29日 下午2:58:10
	 */
	JSONObject getLockExtendInfo(long imei);
	
	
	/**
	 * 
	 * @Title:        updateBikeInfoWithHeart 
	 * @Description:  更新心跳值/update hearbeat number
	 * @param:        @param imei
	 * @param:        @param power
	 * @param:        @param heartTime
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-27 上午11:24:26
	 */
	boolean updateBikeInfoWithHeart(long imei,short power,long heartTime,int status,int bikeStatus);
	
	/**
	 * 
	 * @Title:        updateHeart 
	 * @Description:  更新心跳值/update heartbeat number
	 * @param:        @param imei
	 * @param:        @param power
	 * @param:        @param heartTime
	 * @param:        @param status
	 * @param:        @param gsm
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月13日 上午11:29:30
	 */
	boolean updateHeart(long imei,short power,long heartTime,int status,int gsm);
	
	/**
	 * 
	 * @Title:        updateGps 
	 * @Description:  更新GPS定位信息/update gps position information
	 * @param:        @param imei
	 * @param:        @param gLat
	 * @param:        @param gLng
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017-3-27 上午11:28:44
	 */
	boolean updateGps(long imei,double gLat,double gLng); 
	
	/**
	 * 
	 * @Title:        updateBikeInfo 
	 * @Description:  更新设备信息/update device information
	 * @param:        @param imei
	 * @param:        @param power
	 * @param:        @param gsm
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月7日 下午4:51:36
	 */
	boolean updateBikeInfo(long imei, int power, int gsm,int lockStatus); 
	
	
	/**
	 * 
	 * @Title:        updateBikeDetailInfo 
	 * @Description:  TODO
	 * @param:        @param bikeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月8日 下午12:07:01
	 */
	boolean updateBikeDetailInfo(BikeVo bikeVo); 
	
	
	/**
	 * 
	 * @Title:        updateRedPackageBike 
	 * @Description:  修改红包单车/repair red envelope bike 
	 * @param:        @param bid
	 * @param:        @param redpack
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月2日 下午5:43:17
	 */
	boolean updateRedPackageBike(String bid,int redpack);
	
	/**
	 * 
	 * @Title:        updateBikeVersion 
	 * @Description:  更新设备版本信息/update device version information
	 * @param:        @param imei
	 * @param:        @param version
	 * @param:        @param buildTime
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月7日 下午4:51:46
	 */
	boolean updateBikeVersion(long imei, String version,String deviceType, String buildTime); 

	
	/**
	 * 
	 * @Title:        updateLockStatus 
	 * @Description:  更新设备状态/update device status
	 * @param:        @param bid
	 * @param:        @param status 0-关/lock 1-开/open 2-已损坏/broken 3-已报废/scrapped
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月14日 下午5:52:55
	 */
	boolean updateLockStatus(String number,int status);
	
	/**
	 * 
	 * @Title:        updateLockPower 
	 * @Description:  更新电量信息/update power
	 * @param:        @param number
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年4月10日 下午4:05:59
	 */
	boolean updateLockPower(String number,int power);
	
	/**
	 * 
	 * @Title:        updateLockPower 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param power
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月17日 下午7:15:15
	 */
	boolean updateLockPower(long imei,int power);
	
	/**
	 * 
	 * @Title:        updateBikeErrorStatusWithImei 
	 * @Description:  更新设备故障状态(IMEI)/update device erro status
	 * @param:        @param imei
	 * @param:        @param newStatus
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月18日 上午11:30:36
	 */
	boolean updateBikeErrorStatusWithImei(long imei,int newStatus);

	/**
	 * 
	 * @Title:        updateBikeErrorStatus 
	 * @Description:  更新设备故障状态(IMEI)/update device erros status
	 * @param:        @param number
	 * @param:        @param oldStatus 旧状态/old status
	 * @param:        @param newStatus 新状态/new status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月18日 上午11:19:02
	 */
	boolean updateBikeErrorStatusWithImei(long imei,int oldStatus,int newStatus);
	

	
	/**
	 * 
	 * @Title:        autoCancelBikeError 
	 * @Description:  自动还原故障/auto recover error
	 * @param:        @param number
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月15日 下午1:01:29
	 */
	boolean autoCancelBikeError(String number);
	
	/**
	 * 
	 * @Title:        updateLockStatusWithImei 
	 * @Description:  更新设备状态/update device status
	 * @param:        @param imei
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月13日 下午3:26:09
	 */
	boolean updateLockStatusWithImei(long imei,int status,boolean updateUseStatus);
	

	
	
	/**
	 * 
	 * @Title:        updateBikeStatusWithImei 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年3月30日 下午2:05:07
	 */
	boolean updateBikeStatusWithImei(long imei,int status);
	
	/**
	 * 
	 * @Title:        updateBikeServerIp 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param serverIp
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年6月6日 下午3:17:39
	 */
	boolean updateBikeServerIp(long imei, String serverIp,short power);
	
	/**
	 * 
	 * @Title:        updateExtendInfo 
	 * @Description:  TODO
	 * @param:        @param imei
	 * @param:        @param data
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年8月27日 下午5:10:55
	 */
	boolean updateExtendInfo(long imei,JSONObject data);
	
	/**********************后台业务*****************************/

	/**
	 * 
	 * @Title:        getBikeList 
	 * @Description:  显示所有单车/show al bike
	 * @param:        @param type 0:所有/all 1：低电量/low battery 2：解锁/unlock 3：锁定/lock
	 * @param:        @return    
	 * @return:       List<BikeVo>    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:00:22
	 */
	List<BikeVo> getBikeList(int type,int cityId,Boolean locationCheck);
	
	/**
	 * 
	 * @Title:        getNormalBikeList 
	 * @Description:  获取正常状态的单车（非异常）/get normal bike list (not error)
	 * @param:        @return    
	 * @return:       List<BikeVo>    
	 * @author        Albert
	 * @Date          2017年11月30日 上午11:35:17
	 */
	List<BikeVo> getNormalBikeList();
	
	/**
	 * 
	 * @Title:        getBike 
	 * @Description:  获取单车信息，目前用于请求单车在地图显示/obtain bike information, apply for reqest bike show on map
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       List<BikeVo>    
	 * @author        Albert
	 * @Date          2017年4月25日 下午6:22:13
	 */
	List<BikeVo> getBike(String id);
	
	/**
	 * 
	 * @Title:        getBikeList 
	 * @Description:  获取单车列表/obtain bike list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<BikeVo>    
	 * @author        Albert
	 * @Date          2017年4月7日 上午11:38:39
	 */
	List<BikeVo> getBikeList(RequestListVo requestVo);
	
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
	 * @Title:        addBike 
	 * @Description:  添加单车设备/add bike device
	 * @param:        @param bike
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月7日 下午1:50:50
	 */
	String addBike(BikeVo bike);
	
	/**
	 * 
	 * @Title:        deleteBike 
	 * @Description:  删除设备/delete device
	 * @param:        @param bikeId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月7日 下午1:51:41
	 */
	boolean deleteBike(String bikeId);
}
