package com.omni.scooter.entity;

 

public class LocationEntity extends BaseEntity {
	private GPSEntity gpsEntity=null;
	private String gpsContent;

	public LocationEntity() {
		super();
	}

	public LocationEntity(String content) {
		super(content);
		int startIndex = content.indexOf("D0");
		gpsContent =content.substring(startIndex+3);
		if("A".equals(tCont[6])){
			gpsEntity=new GPSEntity(gpsContent);
		}
	}

	public GPSEntity getGpsEntity() {
		return gpsEntity;
	}

	public void setGpsEntity(GPSEntity gpsEntity) {
		this.gpsEntity = gpsEntity;
	}

	public String getGpsContent() {
		return gpsContent;
	}

	public void setGpsContent(String gpsContent) {
		this.gpsContent = gpsContent;
	}

	@Override
	public String toString() {
		return "LocationEntity [gpsEntity=" + gpsEntity + ", gpsContent=" + gpsContent + ", getImei()=" + getImei()
				+ "]";
	}
	
	
	

}
