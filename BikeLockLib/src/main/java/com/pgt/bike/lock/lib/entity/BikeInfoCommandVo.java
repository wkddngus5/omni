package com.pgt.bike.lock.lib.entity;

public class BikeInfoCommandVo extends BaseCommandVo{
	 
	private int power;
	private int chargeStatus;
	private int speed;
	private int mileage;
	private int forecastMileage;
	
	
	
	public BikeInfoCommandVo() {
		super();
	}
	
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getChargeStatus() {
		return chargeStatus;
	}
	public void setChargeStatus(int chargeStatus) {
		this.chargeStatus = chargeStatus;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public int getForecastMileage() {
		return forecastMileage;
	}
	public void setForecastMileage(int forecastMileage) {
		this.forecastMileage = forecastMileage;
	}
	
	
	
	

}
