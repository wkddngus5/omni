/**
 * FileName:     BikeUseVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 下午7:18:01/7:18:01 am, March 24, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;

/**
 * @ClassName:     BikeUseVo
 * @Description: 单车使用实体/bicycle use entity
 * @author:    Albert
 * @date:        2017-3-24 下午7:18:01/7:18:01 am, March 24, 2017
 *
 */
public class BikeUseVo {
	/**使用状态，0-自由，1-使用，2-预约，3-正在解锁 /use status 0:available 1: using 2: reservation 3:unlocking */
	public static final byte STATUS_UNLOCK_ING = 3;
	public static final byte STATUS_RESERVED = 2;
	public static final byte STATUS_USING = 1;
	public static final byte STATUS_FREE = 0;


	String	id	;//用户使用记录ID/user use log ID
	String	uid	;//用户ID/User ID
	UserVo userVo;//用户实体/User entity
	String	bid	;//单车ID/bicycle ID
	BikeVo bikeVo;//单车实体Bicycle entity
	String	startTime	;//开始时间/start time
	String	endTime	;//结束时间/end time
	double	distance	;//骑行距离/cycling distance
	String	orbit	;//骑行轨迹/cycling track
	double	startLat	;//开始纬度（APP用户）/start latitude (for APP User)
	double	startLng	;//开始经度（APP用户）/start longitude (for APP User)
	double	endLat	;//结束纬度（锁设备）end latitude (for lock)
	double	endLng	;//结束经度（锁设备）end longitude (for lock)
	int ispay;//是否已支付 0：未支付 1：已支付 2:免费/ payment status 0:unpaid 1:paid 2:free
	String date;//开锁申请时间/unlock applying time
	int lockLocation;//锁位置是否上报（上锁）/whether the lock location is reported(locked)
	int out_area;//骑行越界/cycling beyond the border 0:normal 1:out of area 2:in Prohibited parking
	int openWay;//open lock way 0:gprs 1:ble(app)
	int closeWay;//close lock way 0:gprs 1:ble(app)
	int oldDuration;//old duration
	String adminId;
	long updateTime;//ride update time
	int hostId;
	String rideUser;
	BigDecimal rideAmount;
	int rideStatus;//ride status 0:unlocking 1:riding 2:end 3:paid
	int groupRide;
	
	/**显示属性**//**display attributes**/
	String number;//单车编号/ bicycle no.
	int useStatus;//使用状态，0-自由，1-使用，2-预约，3-正在解锁/ use status 0: available, 1:using 2:unlocking
	int duration;//使用时长/use duration
	String calorie;//消耗卡里路/consuming calorie
	String carbon;//节约碳排量/carbon saving
	BigDecimal amount;//金额/amount
	String tradeId;//订单ID
	int violationType;//违规类型/violation type
	int status;//1：正在解锁 2：骑行中 3:已完成/ 1:unlocking 2:cicyling 3:completed
	int activeDay;//活跃天数/active days
	int readpack;//红包单车： 1：是 0：否/red packet: 1: yes 0:no

	double currentLat;
	double currentLng;
	List<LatLng> latLngList;
	List<BikeUseVo> groupRideList;
	String adminName;
	
	long startStamp;
	long endStamp;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}

	public BikeVo getBikeVo() {
		return bikeVo;
	}
	public void setBikeVo(BikeVo bikeVo) {
		this.bikeVo = bikeVo;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}



	/**
	 * @return the calorie
	 */
	public String getCalorie() {
		return calorie;
	}
	/**
	 * @param calorie the calorie to set
	 */
	public void setCalorie(String calorie) {
		this.calorie = calorie;
	}
	/**
	 * @return the carbon
	 */
	public String getCarbon() {
		return carbon;
	}
	/**
	 * @param carbon the carbon to set
	 */
	public void setCarbon(String carbon) {
		this.carbon = carbon;
	}
	/**
	 * 根据开始和结束位置结算距离 caculating the distance according to the start and end position
	 * @Title:        setDistanceWithStartAndEnd 
	 * @Description:  TODO
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-27 下午2:51:37/2:51:37 pm, March 27, 2017
	 */
	public void setDistanceWithStartAndEnd(){
		//Add distance
		if(this.endLat > 0 || this.endLng > 0){
			this.distance = AMapUtil.calculateLineDistance(this.endLat, this.endLng, this.currentLat, this.currentLng);
		}else if(this.startLat > 0 || this.startLng > 0){
			this.distance = AMapUtil.calculateLineDistance(this.startLat, this.startLng, this.currentLat, this.currentLng);
		}
	}

	public String getOrbit() {
		return orbit;
	}
	public void setOrbit(String orbit) {
		this.orbit = orbit;
	}

	/**
	 * 
	 * @Title:        自动设置轨迹/automatically set the track
	 * @Description:  TODO
	 * @param:            
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-27 下午3:04:29/2:51:37 pm, March 27, 2017
	 */
	public void setOrbitWithPoint(double newLat,double newLng) {
		if(this.orbit == null){
			this.orbit =AMapUtil.encode(new LatLng(newLat,newLng));
		}else{
			List<LatLng> oldOrbit = AMapUtil.decode(this.orbit);
			oldOrbit.add(new LatLng(newLat, newLng));
			this.orbit = AMapUtil.encode(oldOrbit);
		}

	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void setEndTime(int runTime) {
		if(runTime == 0){
			runTime = 1;
		}
		long startTime = ValueUtil.getLong(this.startTime);
		long endTime = 0;
		if(startTime == 0){
			//unnormal finish use
			endTime = TimeUtil.getCurrentLongTime();
			startTime = endTime - runTime*60;
			this.startTime = startTime+"";
		}else{
			endTime = startTime + runTime*60;
			if(endTime > TimeUtil.getCurrentLongTime()){//结束时间不能大于当前时间/end time can't exceed the current time
				endTime = TimeUtil.getCurrentLongTime();
			}
		}
		
		this.endTime = endTime+"";
	}
	

	public double getStartLat() {
		return startLat;
	}
	public void setStartLat(double startLat) {
		this.startLat = startLat;
	}
	public double getStartLng() {
		return startLng;
	}
	public void setStartLng(double startLng) {
		this.startLng = startLng;
	}
	public double getEndLat() {
		return endLat;
	}
	public void setEndLat(double endLat) {
		this.endLat = endLat;
	}
	public double getEndLng() {
		return endLng;
	}
	public void setEndLng(double endLng) {
		this.endLng = endLng;
	}


	/**
	 * @return the ispay
	 */
	public int getIspay() {
		return ispay;
	}
	/**
	 * @param ispay the ispay to set
	 */
	public void setIspay(int ispay) {
		this.ispay = ispay;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}



	/**
	 * @return the openWay
	 */
	public int getOpenWay() {
		return openWay;
	}
	/**
	 * @param openWay the openWay to set
	 */
	public void setOpenWay(int openWay) {
		this.openWay = openWay;
	}
	/**
	 * @return the useStatus
	 */
	public int getUseStatus() {
		return useStatus;
	}
	/**
	 * @param useStatus the useStatus to set
	 */
	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}


	/**
	 * @return the oldDuration
	 */
	public int getOldDuration() {
		return oldDuration;
	}
	/**
	 * @param oldDuration the oldDuration to set
	 */
	public void setOldDuration(int oldDuration) {
		this.oldDuration = oldDuration;
	}
	/**
	 * @return the adminId
	 */
	public String getAdminId() {
		return adminId;
	}
	/**
	 * @param adminId the adminId to set
	 */
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	 * @return the violationType
	 */
	public int getViolationType() {
		return violationType;
	}
	/**
	 * @param violationType the violationType to set
	 */
	public void setViolationType(int violationType) {
		this.violationType = violationType;
	}
	
	
	
	
	/**
	 * @return the lockLocation
	 */
	public int getLockLocation() {
		return lockLocation;
	}
	/**
	 * @param lockLocation the lockLocation to set
	 */
	public void setLockLocation(int lockLocation) {
		this.lockLocation = lockLocation;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the out_area
	 */
	public int getOut_area() {
		return out_area;
	}
	/**
	 * @param out_area the out_area to set
	 */
	public void setOut_area(int out_area) {
		this.out_area = out_area;
	}
	
	
	
	
	/**
	 * @return the readpack
	 */
	public int getReadpack() {
		return readpack;
	}
	/**
	 * @param readpack the readpack to set
	 */
	public void setReadpack(int readpack) {
		this.readpack = readpack;
	}
	/**
	 * @return the activeDay
	 */
	public int getActiveDay() {
		return activeDay;
	}
	/**
	 * @param activeDay the activeDay to set
	 */
	public void setActiveDay(int activeDay) {
		this.activeDay = activeDay;
	}
	
	/**
	 * @return the currentLat
	 */
	public double getCurrentLat() {
		return currentLat;
	}
	/**
	 * @param currentLat the currentLat to set
	 */
	public void setCurrentLat(double currentLat) {
		this.currentLat = currentLat;
	}
	/**
	 * @return the currentLng
	 */
	public double getCurrentLng() {
		return currentLng;
	}
	/**
	 * @param currentLng the currentLng to set
	 */
	public void setCurrentLng(double currentLng) {
		this.currentLng = currentLng;
	}
	
	/**
	 * @return the latLngList
	 */
	public List<LatLng> getLatLngList() {
		return latLngList;
	}
	/**
	 * @param latLngList the latLngList to set
	 */
	public void setLatLngList(List<LatLng> latLngList) {
		this.latLngList = latLngList;
	}
	
	
	
	/**
	 * @return the closeWay
	 */
	public int getCloseWay() {
		return closeWay;
	}
	/**
	 * @param closeWay the closeWay to set
	 */
	public void setCloseWay(int closeWay) {
		this.closeWay = closeWay;
	}
	
	
	
	/**
	 * @return the adminName
	 */
	public String getAdminName() {
		return adminName;
	}
	/**
	 * @param adminName the adminName to set
	 */
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	
	
	
	
	/**
	 * @return the updateTime
	 */
	public long getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
	
	/**
	 * @return the hostId
	 */
	public int getHostId() {
		return hostId;
	}
	/**
	 * @param hostId the hostId to set
	 */
	public void setHostId(int hostId) {
		this.hostId = hostId;
	}
	/**
	 * @return the rideUser
	 */
	public String getRideUser() {
		return rideUser;
	}
	/**
	 * @param rideUser the rideUser to set
	 */
	public void setRideUser(String rideUser) {
		this.rideUser = rideUser;
	}
	
	
	
	
	/**
	 * @return the rideAmount
	 */
	public BigDecimal getRideAmount() {
		return rideAmount;
	}
	/**
	 * @param rideAmount the rideAmount to set
	 */
	public void setRideAmount(BigDecimal rideAmount) {
		this.rideAmount = rideAmount;
	}
	/**
	 * @return the rideStatus
	 */
	public int getRideStatus() {
		return rideStatus;
	}
	/**
	 * @param rideStatus the rideStatus to set
	 */
	public void setRideStatus(int rideStatus) {
		this.rideStatus = rideStatus;
	}
	
	
	
	/**
	 * @return the groupRide
	 */
	public int getGroupRide() {
		return groupRide;
	}
	/**
	 * @param groupRide the groupRide to set
	 */
	public void setGroupRide(int groupRide) {
		this.groupRide = groupRide;
	}
	/**
	 * @return the groupRideList
	 */
	public List<BikeUseVo> getGroupRideList() {
		return groupRideList;
	}
	/**
	 * @param groupRideList the groupRideList to set
	 */
	public void setGroupRideList(List<BikeUseVo> groupRideList) {
		this.groupRideList = groupRideList;
	}
	
	
	
	/**
	 * @return the startStamp
	 */
	public long getStartStamp() {
		return startStamp;
	}
	/**
	 * @param startStamp the startStamp to set
	 */
	public void setStartStamp(long startStamp) {
		this.startStamp = startStamp;
	}
	/**
	 * @return the endStamp
	 */
	public long getEndStamp() {
		return endStamp;
	}
	/**
	 * @param endStamp the endStamp to set
	 */
	public void setEndStamp(long endStamp) {
		this.endStamp = endStamp;
	}
	public BikeUseVo(){

	}

	/**
	 * 
	 * @param rst
	 * @throws SQLException
	 */
	public BikeUseVo(ResultSet rst,boolean formateDate) throws SQLException{

		this.startTime = BaseDao.getString(rst, "start_time");
		this.endTime = BaseDao.getString(rst, "end_time");
		if(formateDate){
			this.startTime = TimeUtil.formaStrDate(this.startTime,TimeUtil.Formate_HH_mm);
			this.endTime = TimeUtil.formaStrDate(this.endTime,TimeUtil.Formate_HH_mm);
		}
		this.distance = BaseDao.getDouble(rst,"distance");
		this.orbit = BaseDao.getString(rst, "orbit");
		this.startLat = BaseDao.getDouble(rst,"start_lat");
		this.startLng = BaseDao.getDouble(rst,"start_lng");
		this.endLat = BaseDao.getDouble(rst,"end_lat");
		this.endLng = BaseDao.getDouble(rst,"end_lng");

		this.id = BaseDao.getString(rst, "id");
		this.uid = BaseDao.getString(rst, "uid");
		this.bid = BaseDao.getString(rst, "bid");

		this.number = BaseDao.getString(rst, "number");
		this.status = BaseDao.getInt(rst,"status");
		this.useStatus = BaseDao.getInt(rst,"use_status");
		this.duration = TimeUtil.getMinutes(this.startTime, this.endTime);

		this.calorie = ValueUtil.getCalorie(this.duration, this.distance);
		this.carbon = ValueUtil.getCarbon(this.duration, this.distance);

		this.violationType = BaseDao.getInt(rst,"violationType");
		this.lockLocation = BaseDao.getInt(rst,"lock_location");
		this.out_area = BaseDao.getInt(rst,"out_area");
		this.date = BaseDao.getString(rst, "date");
		
		this.amount = BaseDao.getBigDecimal(rst, "amount");
		this.tradeId = BaseDao.getString(rst, "trade_id");
		this.openWay = BaseDao.getInt(rst,"open_way");
		this.closeWay = BaseDao.getInt(rst,"close_way");
		
		this.oldDuration = BaseDao.getInt(rst,"old_duration");
		this.adminName = BaseDao.getString(rst, "admin_name");
		
		this.rideStatus = BaseDao.getInt(rst, "ride_status");
		this.rideAmount = BaseDao.getBigDecimal(rst, "ride_amount");
		
		this.hostId = BaseDao.getInt(rst, "host_id");
		this.rideUser = BaseDao.getString(rst, "ride_user");
	}
}
