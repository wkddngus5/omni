package com.pgt.bikelock.vo.admin;

public class MapBike {
	
	private String bid;
	private double lat;
	private double lng;
	private String icon;
	private long number;
	
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	/**
	 * @return the bid
	 */
	public String getBid() {
		return bid;
	}
	/**
	 * @param bid the bid to set
	 */
	public void setBid(String bid) {
		this.bid = bid;
	}
	
	

}
