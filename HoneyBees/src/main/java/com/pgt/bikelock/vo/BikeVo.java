/**
 * FileName:     BikeVo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:21:16/10:21:16 am, March 24, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>//<initializing>
 */
package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.admin.RedPackBikeVo;
import com.pgt.bikelock.vo.admin.RedPackRuleVo;

/**
 * @ClassName:     BikeVo
 * @Description:单车实体/bicycle use entity
 * @author:    Albert
 * @date:        2017-3-24 上午10:21:16
 *
 */
public class BikeVo {

	/**设备状态：0-关 1-开 2-已损坏 3-已报废 / bicycle status: 0: off 1: on 2: broken 3: scrapped */
	public static final byte LOCK_OFF = 0;
	public static final byte LOCK_ON = 1;
	public static final byte LOCK_ERROR = 2;
	public static final byte LOCK_SCRAPPED = 3;
	
	public static final int LOCATION_TYPE_GPS=2;
	public static final int LOCATION_TYPE_GCJ02=1;

	//extend info
	public static final String LOCK_EXTEND_INFO_POWER = "LOCK_EXTEND_INFO_POWER";
	public static final String LOCK_EXTEND_INFO_SPEED = "LOCK_EXTEND_INFO_SPEED";
	public static final String LOCK_EXTEND_INFO_SPEED_MODE = "LOCK_EXTEND_INFO_SPEED_MODE";
	public static final String LOCK_EXTEND_INFO_TOTAL_MILEAGE = "LOCK_EXTEND_INFO_TOTAL_MILEAGE";
	public static final String LOCK_EXTEND_INFO_PRESCIENT_MILEAGE = "LOCK_EXTEND_INFO_PRESCIENT_MILEAGE";
	public static final String LOCK_EXTEND_INFO_TOTAL_TIME = "LOCK_EXTEND_INFO_TOTAL_TIME";
	
	public static final String LOCK_EXTEND_INFO_CHARGE_STATUS = "LOCK_EXTEND_INFO_CHARGE_STATUS";
	public static final String LOCK_EXTEND_INFO_VOLTAGE = "LOCK_EXTEND_INFO_VOLTAGE";
	public static final String LOCK_EXTEND_INFO_CYCLES = "LOCK_EXTEND_INFO_CYCLES";
	public static final String LOCK_EXTEND_INFO_POWER1 = "LOCK_EXTEND_INFO_POWER1";
	public static final String LOCK_EXTEND_INFO_POWER2 = "LOCK_EXTEND_INFO_POWER2";
	public static final String LOCK_EXTEND_INFO_BUILTIN_TEMP1 = "LOCK_EXTEND_INFO_BUILTIN_TEMP1";
	public static final String LOCK_EXTEND_INFO_BUILTIN_TEMP2 = "LOCK_EXTEND_INFO_BUILTIN_TEMP2";
	public static final String LOCK_EXTEND_INFO_PLUGIN_TEMP1 = "LOCK_EXTEND_INFO_PLUGIN_TEMP1";
	public static final String LOCK_EXTEND_INFO_PLUGIN_TEMP2 = "LOCK_EXTEND_INFO_PLUGIN_TEMP2";
	
	public static final String LOCK_EXTEND_INFO_HEAD_LIGHT = "LOCK_EXTEND_INFO_HEAD_LIGHT";
	public static final String LOCK_EXTEND_INFO_HEAD_LIGHT_TWINKLING = "LOCK_EXTEND_INFO_HEAD_LIGHT_TWINKLING";
	public static final String LOCK_EXTEND_INFO_TAIL_LIGHT_TWINKLING = "LOCK_EXTEND_INFO_TAIL_LIGHT_TWINKLING";
	public static final String LOCK_EXTEND_INFO_ACCELERATOR_RESPONSE = "LOCK_EXTEND_INFO_ACCELERATOR_RESPONSE";
	
	public static final String LOCK_EXTEND_INFO_INCH_SPEED = "LOCK_EXTEND_INFO_INCH_SPEED";
	public static final String LOCK_EXTEND_INFO_CRUISE_SPEED = "LOCK_EXTEND_INFO_CRUISE_SPEED";
	public static final String LOCK_EXTEND_INFO_BLE_BROADCAST = "LOCK_EXTEND_INFO_BLE_BROADCAST";
	public static final String LOCK_EXTEND_INFO_BUTTON_CHANGE_MODE = "LOCK_EXTEND_INFO_BUTTON_CHANGE_MODE";
	public static final String LOCK_EXTEND_INFO_LOW_SPEED_LIMIT = "LOCK_EXTEND_INFO_LOW_SPEED_LIMIT";
	public static final String LOCK_EXTEND_INFO_MEDIUM_SPEED_LIMIT = "LOCK_EXTEND_INFO_MEDIUM_SPEED_LIMIT";
	public static final String LOCK_EXTEND_INFO_HIGH_SPEED_LIMIT = "LOCK_EXTEND_INFO_HIGH_SPEED_LIMIT";
	public static final String LOCK_EXTEND_INFO_START_TYPE = "LOCK_EXTEND_INFO_START_TYPE";
	public static final String LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT = "LOCK_EXTEND_INFO_BUTTON_OPEN_HEADLIGHT";
	
	public static final String LOCK_EXTEND_INFO_IOT_ACCELEROMETER_SENSITIVITY = "LOCK_EXTEND_INFO_IOT_ACCELEROMETER_SENSITIVITY";
	public static final String LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE = "LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE";
	public static final String LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE_INTERVAL = "LOCK_EXTEND_INFO_IOT_UPLOAD_IN_RIDE_INTERVAL";
	public static final String LOCK_EXTEND_INFO_IOT_HEART_INTERVAL = "LOCK_EXTEND_INFO_IOT_HEART_INTERVAL";
	
	public static final String LOCK_EXTEND_INFO_KEY_BLE = "LOCK_EXTEND_INFO_KEY_BLE";
	
	public static final String LOCK_EXTEND_INFO_ADDRESS_IP = "LOCK_EXTEND_INFO_ADDRESS_IP";
	public static final String LOCK_EXTEND_INFO_ADDRESS_MODE = "LOCK_EXTEND_INFO_ADDRESS_MODE";
	public static final String LOCK_EXTEND_INFO_ADDRESS_PORT = "LOCK_EXTEND_INFO_ADDRESS_PORT";
	public static final String LOCK_EXTEND_INFO_ADDRESS_APN = "LOCK_EXTEND_INFO_ADDRESS_APN";
	public static final String LOCK_EXTEND_INFO_APN_USER = "LOCK_EXTEND_INFO_APN_USER";
	public static final String LOCK_EXTEND_INFO_APN_PASSWORD = "LOCK_EXTEND_INFO_APN_PASSWORD";
	
	String bid;//
	String number;//单车编号/bicycle no.
	String imei;//单车通讯IMEI号/bicycle communication IMEI No.
	String typeId;//单车类型ID/bicycle type ID
	int typeCount;//默认类型数量（最小骑行时间）/default type quantity (minimum riding time)
	BikeTypeVo typeVo;//单车类型实体/bicycle type entiy
	String gTime;//GPS定位坐标插入时间/GPS positioning coordinate insertion time
	double gLat;//GPS定位坐标经度/GPS positioning coordinate latitue
	double gLng;//GPS定位坐标纬度/GPS positioning coordinate longtitude
	int status;//设备状态：0-关 1-开 2-已损坏 3-已报废/bicycle status: 0: off 1: on 2: broken 3: scrapped
	int useStatus;//使用状态，0-自由，1-使用，2-预约，3-正在解锁/use status: 0: available, 1: using, 2: reservation, 3:unlocking
	int power;//电量/power
	int gsm;//GPS信号值/GPS signal
	int gpsNumber;//GPS卫星数/GPS satellite number
	int cityId;//投放城市ID/city ID
	String heartTime;//上一次心跳检查时间/last check for hear rate
	String version;//当前版本号/current version
	String versionTime;//当前版本生成时间/generating time of the current version
	int readpack;//红包单车： 1：是 0：否/ red packet: 1. yes 0: no
	String add_date;//设备添加时间 add time of bicycle
	int error_status;//故障状态 0：无故障  1：手动标记故障 2：自动标记故障/fault status 0: no fault 1: mark fault manually 2: mark fault automatically
	int bikeStatus;// 车状态 1：正常 2：倒地  / 1：normal 2：fall
	int bikeType;//1:bicycle 2:scooter
	JSONObject extendInfo;//扩展信息/extend info
	/**显示属性**//**display attributes**/
	BigDecimal price;//价格(单位：分)
	RedPackRuleVo redpackRuleVo;//红包信息/red packet information
	int powerPercent;//显示电量/power percentage
	String statusStr;
	String errorStr;
	String serverIp;//服务器地址
	int rideCount;//骑行次数/Ride count
	int reportCount;//待处理故障次数
	String mac;
	String lastRideDate;//上次骑行时间/Last ride date
	String bikeTypeStr;
	
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the typeCount
	 */
	public int getTypeCount() {
		return typeCount;
	}
	/**
	 * @param typeCount the typeCount to set
	 */
	public void setTypeCount(int typeCount) {
		this.typeCount = typeCount;
	}
	public BikeTypeVo getTypeVo() {
		return typeVo;
	}
	public void setTypeVo(BikeTypeVo typeVo) {
		this.typeVo = typeVo;
	}
	public String getgTime() {
		return gTime;
	}
	public void setgTime(String gTime) {
		this.gTime = gTime;
	}
	public double getgLat() {
		return gLat;
	}
	public void setgLat(double gLat) {
		this.gLat = gLat;
	}
	public double getgLng() {
		return gLng;
	}
	public void setgLng(double gLng) {
		this.gLng = gLng;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public int getUseStatus() {
		return useStatus;
	}
	public void setUseStatus(int useStatus) {
		this.useStatus = useStatus;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getGsm() {
		return gsm;
	}
	public void setGsm(int gsm) {
		this.gsm = gsm;
	}
	public int getGpsNumber() {
		return gpsNumber;
	}
	public void setGpsNumber(int gpsNumber) {
		this.gpsNumber = gpsNumber;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getHeartTime() {
		return heartTime;
	}
	public void setHeartTime(String heartTime) {
		this.heartTime = heartTime;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersionTime() {
		return versionTime;
	}
	public void setVersionTime(String versionTime) {
		this.versionTime = versionTime;
	}

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	 * @return the add_date
	 */
	public String getAdd_date() {
		return add_date;
	}
	/**
	 * @param add_date the add_date to set
	 */
	public void setAdd_date(String add_date) {
		this.add_date = add_date;
	}


	/**
	 * @return the redpackRuleVo
	 */
	public RedPackRuleVo getRedpackRuleVo() {
		return redpackRuleVo;
	}
	/**
	 * @param redpackRuleVo the redpackRuleVo to set
	 */
	public void setRedpackRuleVo(RedPackRuleVo redpackRuleVo) {
		this.redpackRuleVo = redpackRuleVo;
	}

	/**
	 * @return the statusStr
	 */
	public String getStatusStr() {
		return statusStr;
	}
	
	/**
	 * @param statusStr the statusStr to set
	 */
	public void setStatusStr(int status) {
		String[] statusTitle = LanguageUtil.getDefaultValue("bike_status_value").split(",");
		if(this.bikeType != 2 && this.power < 350 && this.power > 0){
			status = 2;
		}
		
		if(status > statusTitle.length){
			this.statusStr = "Unknow";
		}else{
			this.statusStr = statusTitle[status+1];
		}
		if(this.error_status != 0){
			this.statusStr += "["+LanguageUtil.getStatusStr(this.error_status, "bike_lock_error_value")+"]";
		}
	
		if(this.bikeStatus == 2){
			this.statusStr += "["+LanguageUtil.getStatusStr(3, "bike_status_value")+"]";
		}
	}
	
	public int getPowerPercent() {
		if(this.bikeType == 2){
			return power;
		}else{
			if(power>=420) return 100;
			else if(power<420 && power>=411) return (95+(power-411)*5/9);
			else if(power<411 && power>=395) return 90+(power-395)*5/16;
			else if(power<395 &&power>=386) return 80+(power-386)*10/9;
			else if(power<386 &&power>=379) return 70+(power-379)*10/7;
			else if(power<379 &&power>=373) return 60+(power-373)*10/6;
			else if(power<373 &&power>=369) return 50+(power-369)*10/4;
			else if(power<369 &&power>=365) return 40+(power-365)*10/4;
			else if(power<365 &&power>=363) return 30+(power-363)*10/2;
			else if(power<363 &&power>=359) return 20+(power-359)*10/4;
			else if(power<359 &&power>=354) return 10+(power-354)*10/5;
			else if(power<354 && power>340) return (power-340)*10/14;
			else if(power<=340) return 0;
			return powerPercent;
		}
		
	}
	
	public static void main(String[] args) {
		System.out.println((350-340)*10/14);
	}

	/**
	 * @return the error_status
	 */
	public int getError_status() {
		
		return error_status;
	}
	/**
	 * @param error_status the error_status to set
	 */
	public void setError_status(int error_status) {
		this.error_status = error_status;
	}
	
	/**
	 * @param statusStr the statusStr to set
	 */
	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	
	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}
	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	
	/**
	 * @return the errorStr
	 */
	public String getErrorStr() {
		return LanguageUtil.getStatusStr(error_status, "bike_lock_error_value");
	}
	/**
	 * @param errorStr the errorStr to set
	 */
	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}
	
	
	/**
	 * @return the rideCount
	 */
	public int getRideCount() {
		return rideCount;
	}
	/**
	 * @param rideCount the rideCount to set
	 */
	public void setRideCount(int rideCount) {
		this.rideCount = rideCount;
	}
	/**
	 * @return the reportCount
	 */
	public int getReportCount() {
		return reportCount;
	}
	/**
	 * @param reportCount the reportCount to set
	 */
	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}
	
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	/**
	 * @return the lastRideDate
	 */
	public String getLastRideDate() {
		return lastRideDate;
	}
	/**
	 * @param lastRideDate the lastRideDate to set
	 */
	public void setLastRideDate(String lastRideDate) {
		this.lastRideDate = lastRideDate;
	}
	
	/**
	 * @return the bikeStatus
	 */
	public int getBikeStatus() {
		return bikeStatus;
	}
	/**
	 * @param bikeStatus the bikeStatus to set
	 */
	public void setBikeStatus(int bikeStatus) {
		this.bikeStatus = bikeStatus;
	}
	
	
	/**
	 * @return the bikeType
	 */
	public int getBikeType() {
		return bikeType;
	}
	/**
	 * @param bikeType the bikeType to set
	 */
	public void setBikeType(int bikeType) {
		this.bikeType = bikeType;
	}
	
	
	
	/**
	 * @return the extendInfo
	 */
	public JSONObject getExtendInfo() {
		return extendInfo;
	}
	/**
	 * @param extendInfo the extendInfo to set
	 */
	public void setExtendInfo(JSONObject extendInfo) {
		this.extendInfo = extendInfo;
	}
	
	
	
	/**
	 * @return the bikeTypeStr
	 */
	public String getBikeTypeStr() {
		return bikeTypeStr;
	}
	/**
	 * @param bikeTypeStr the bikeTypeStr to set
	 */
	public void setBikeTypeStr(String bikeTypeStr) {
		this.bikeTypeStr = bikeTypeStr;
	}
	public BikeVo(){

	}

	/**
	 * 构造方法
	 * @param rst
	 * @throws SQLException
	 */
	public BikeVo(ResultSet rst) throws SQLException{

		this.number = BaseDao.getString(rst,"number");
		try {
			this.bid = rst.getString("bid");
		}
		catch (SQLException e) {
			this.bid = BaseDao.getString(rst,"id");
		}
		this.gLat = BaseDao.getDouble(rst,"g_lat");
		this.gLng = BaseDao.getDouble(rst,"g_lng");
		this.readpack = BaseDao.getInt(rst,"readpack");

		this.price = BaseDao.getBigDecimal(rst,"price");
		this.redpackRuleVo  = new RedPackRuleVo();
		if(this.readpack == 1){
			this.redpackRuleVo = new RedPackRuleVo(rst);
			this.redpackRuleVo.setId(BaseDao.getString(rst, "rule_id"));
		}


		this.imei =  BaseDao.getString(rst,"imei");
		this.gTime =  BaseDao.getString(rst,"g_time");
		this.status = BaseDao.getInt(rst,"status");
		this.power =  BaseDao.getInt(rst,"power");
		this.error_status = BaseDao.getInt(rst, "error_status");
		this.bikeStatus =  BaseDao.getInt(rst, "bike_status");
		this.bikeType = BaseDao.getInt(rst, "bike_type");
		setStatusStr(this.status);
		
		this.useStatus = BaseDao.getInt(rst,"use_status");

		this.gsm =  BaseDao.getInt(rst,"gsm");
		this.gpsNumber =  BaseDao.getInt(rst,"gps_number");
		this.heartTime = BaseDao.getString(rst,"heart_time");
		this.version = BaseDao.getString(rst,"version");
		this.versionTime = BaseDao.getString(rst,"version_time");
		this.add_date = BaseDao.getString(rst,"add_date");

		this.typeId = BaseDao.getString(rst,"type_id");
		this.typeCount = BaseDao.getInt(rst,"count");
		this.cityId = BaseDao.getInt(rst,"city_id");

		this.serverIp = BaseDao.getString(rst, "server_ip");
		this.reportCount = BaseDao.getInt(rst, "report_count");
		this.rideCount = BaseDao.getInt(rst, "ride_count");
		this.mac = BaseDao.getString(rst, "mac");
		this.lastRideDate = BaseDao.getString(rst, "last_ride_date");
		
		//this.extendInfo = JSONObject.parseObject(BaseDao.getString(rst, "extend_info"));
	}

	public BikeVo(String[] parms,HttpServletRequest req){
		this.number = req.getParameter(parms[0]);
		this.imei= req.getParameter(parms[1]);
		this.typeId = req.getParameter(parms[2]);
		this.cityId = ValueUtil.getInt(req.getAttribute(parms[3]));
		this.bikeType = ValueUtil.getInt(req.getParameter(parms[4]));
	}
}
