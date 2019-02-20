package com.omni.scooter.entity;

 

public class GPSEntity {
	private String old; // 标记是否是旧数据 ,1-旧数据，0-新数据
	private String utcTime; // hhmmss.ms
	private String locationStaus; //A=有效定位，V=无效定位
	private String latDm; // 度分格式下纬度值（ddmm.mmmm）
	private String nOrS ; // N-北半球，S-南半球
	private String lngDm; // 度分格式下的经度值(dddmm.mmmm)
	private String eOrW; // E 东经，W-西经
	private String spd; // 地面速率  000.0-999.9
	private String cog ;// 地面航向  000.0-359.9 
	private String utcDate; //UTC 日期  ddmmyy
	private String declination; // 磁偏角 000.0-180.0
	private String declinationDirection; // 磁偏角方向  E-东,W-西
	private String mode; // 指示模式，A=自主定位，D=差分，E=估算，N=数据无效
	
	 
	private String gpsContent;
	
	public GPSEntity(String GPSContent){
		this.gpsContent = GPSContent;
		String[] t=GPSContent.split(",");
		old = t[0];
		utcTime = t[1];
		locationStaus=t[2];
		latDm = t[3];
		nOrS =  t[4];
		lngDm = t[5];
		eOrW = t[6];
		spd = t[7];
		cog = t[8];
		utcDate = t[9];
		declination =t[10];
		declinationDirection =t[11];
		mode = t[12];
		
	}
	
	/**
	 * GPS 卫星个数
	 * @return
	 */
	public String getGpsNumber(){
		return spd;
	}
	/**
	 * 海拔高度
	 * @return
	 */
	public String getHight(){
		return declination;
	}
	/**
	 * 定位精度
	 * @return
	 */
	public String getHDOP(){
		return cog;
	}
	/**
	 * 高度单位
	 * @return
	 */
	public String getHightUnit(){
		return declinationDirection;
	}
	
	// 是否是旧数据
	@Deprecated
	public boolean isOld(){
		if("1".equals(old)) return true;
		return false;
	}
	
	
	/**
	 * 是否是 追踪的定位数据
	 * XM滑板车协议V1.7.0
	 * @return
	 */
	public boolean isTrack(){
		if("1".equals(old)) return true;
		return false;
	}
	
	
	
	
	/**
	 * 判断是否为有效定位
	 * @return true为有效定位，false为无效定位
	 */
	public boolean isValid(){
		if("A".equals(locationStaus)) return true;
		return false;
		
	}
	
	/**
	 * 获取到 WGS48 坐标系下的 纬度坐标
	 * @return
	 */
	public double getWGS84Lat(){
		int dotIndex = latDm.indexOf('.'); // 小数点所在的位置
		// 出现异常// index out of range :-3,可能出现2233 这种数据及22度，33分
		// 2233.1
		String dd="0";
		String mm="0";
		//ddmm.mmmmm
		if(latDm.length()!=10){
			System.out.println("not std lat="+gpsContent);
			System.out.println("not std latDm="+latDm);
		}
	
		
		if(dotIndex!=-1){
			//22.23 这种形式
			 try {
				dd = latDm.substring(0,dotIndex-2);
				mm = latDm.substring(dotIndex-2,latDm.length());
			} catch (Exception e) {
				System.out.println("err location lat dot:"+gpsContent);
			}
		}else{
			System.out.println("定位非标准格式数据 lat="+latDm);
			//2233 这种形式
			try {
				dd = latDm.substring(0, latDm.length()-2);
				mm = latDm.substring(latDm.length()-2, latDm.length());
			} catch (Exception e) {
				System.out.println("err location lat:"+gpsContent);
			}
		}
		
		double lat = Double.parseDouble(dd);
		lat += Double.parseDouble(mm) /60D;
		if("S".equals(nOrS)){
			// 南半的纬度为 负数
			lat *=-1;
		}
		return lat;
	}
	
	public double getWGS84Lng(){
		int dotIndex = lngDm.indexOf('.'); // 小数点所在的位置
		String dd="0";
		String mm="0";
		//12345.1111
		//
		//dddmm.mmmm
		if(lngDm.length()!=11){
			System.out.println("not std lng="+gpsContent);
		}
	
		if(dotIndex!=-1){
			 try {
				dd = lngDm.substring(0,dotIndex-2);
				 mm = lngDm.substring(dotIndex-2,lngDm.length());
			} catch (Exception e) {
				System.out.println("err location lng dot:"+gpsContent);
			}
		}else{
			System.out.println("not std not dot="+gpsContent);
			//2233 这种形式
			//2233 这种形式
			try {
				dd = lngDm.substring(0, lngDm.length()-2);
				mm = lngDm.substring(lngDm.length()-2, lngDm.length());
			} catch (Exception e) {
				System.out.println("err location lng :"+gpsContent);
			}
		}
		double lng = Double.parseDouble(dd);
		lng += Double.parseDouble(mm) /60D;
		
		if("W".equals(eOrW)){
			// 西经 为 负数
			lng *=-1;
		}
		return lng;
	}
	public String getOld() {
		return old;
	}
	public void setOld(String old) {
		this.old = old;
	}
	public String getUtcTime() {
		return utcTime;
	}
	public void setUtcTime(String utcTime) {
		this.utcTime = utcTime;
	}
	public String getLocationStaus() {
		return locationStaus;
	}
	public void setLocationStaus(String locationStaus) {
		this.locationStaus = locationStaus;
	}
	public String getLatDm() {
		return latDm;
	}
	public void setLatDm(String latDm) {
		this.latDm = latDm;
	}
	public String getnOrS() {
		return nOrS;
	}
	public void setnOrS(String nOrS) {
		this.nOrS = nOrS;
	}
	public String getLngDm() {
		return lngDm;
	}
	public void setLngDm(String lngDm) {
		this.lngDm = lngDm;
	}
	public String geteOrW() {
		return eOrW;
	}
	public void seteOrW(String eOrW) {
		this.eOrW = eOrW;
	}
	/**
	 * 参考{@link #getGpsNumber()}方法
	 * @return
	 */
	@Deprecated
	public String getSpd() {
		return spd;
	}
	public void setSpd(String spd) {
		this.spd = spd;
	}
	/**
	 * 参考{@link #getHDOP()}方法
	 * @return
	 */
	@Deprecated
	public String getCog() {
		return cog;
	}
	public void setCog(String cog) {
		this.cog = cog;
	}
	public String getUtcDate() {
		return utcDate;
	}
	public void setUtcDate(String utcDate) {
		this.utcDate = utcDate;
	}
	
	/**
	 * 参考 {@link #getHight()}方法
	 * @return
	 */
	@Deprecated
	public String getDeclination() {
		return declination;
	}
	public void setDeclination(String declination) {
		this.declination = declination;
	}
	/**
	 * 参考 {@link #getHightUnit()}方法
	 * @return
	 */
	@Deprecated
	public String getDeclinationDirection() {
		return declinationDirection;
	}
	public void setDeclinationDirection(String declinationDirection) {
		this.declinationDirection = declinationDirection;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	@Override
	public String toString() {
		return "GPSEntity [old=" + old + ", utcTime=" + utcTime
				+ ", locationStaus=" + locationStaus + ", latDm=" + latDm
				+ ", nOrS=" + nOrS + ", lngDm=" + lngDm + ", eOrW=" + eOrW
				+ ", gpsNumber=" + spd + ", HDOP=" + cog + ", utcDate=" + utcDate
				+ ", declination=" + declination + ", hightUnit="
				+ declinationDirection + ", mode=" + mode + "]";
	}
	
	
	


}
