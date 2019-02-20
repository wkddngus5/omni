package com.omni.scooter.entity.ninebot;

public class UpdateVersionEntity extends VersionEntity {

	public UpdateVersionEntity() {
		super();
	}

	public UpdateVersionEntity(String content) {
		super(content);
	}
	
	public String getIotCode(){
		return tCont[4];
	}
	

	@Override
	public String getIotVersion() {
		 
		return tCont[5];
	}
	
	@Override
	public String getScooterVersion() {
		return tCont[6];
	}
	
	@Override
	public String getDisplayerVersion() {
		return  tCont[7];
	}
	
	@Override
	public String getInternalBatteryVersion() {
		return  tCont[8];
	}
	
	@Override
	public String getExtralBatteryVersion() {
		return  getString(tCont[9]);
	}

	@Override
	public String toString() {
		return "UpdateVersionEntity [getIotCode()=" + getIotCode() + ", getIotVersion()=" + getIotVersion()
				+ ", getScooterVersion()=" + getScooterVersion() + ", getDisplayerVersion()=" + getDisplayerVersion()
				+ ", getInternalBatteryVersion()=" + getInternalBatteryVersion() + ", getExtralBatteryVersion()="
				+ getExtralBatteryVersion() + "]";
	}
	
	
	
}
