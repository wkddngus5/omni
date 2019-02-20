package com.pgt.bikelock.vo;

public class LatLng {
	public final double latitude;
	public final double longitude;
	
	
	
	
	
	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public LatLng(double latitude, double longitude) { 
		this.latitude = latitude;
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "LatLng [latitude=" + latitude + ", longitude=" + longitude
				+ "]";
	}
	
	
	
	

}
