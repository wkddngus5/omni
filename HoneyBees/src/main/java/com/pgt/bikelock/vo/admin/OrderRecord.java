package com.pgt.bikelock.vo.admin;

public class OrderRecord {
	private int id;
	private  long imei;
	private int orderId;
	private long time ;
	private String content;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getImei() {
		return imei;
	}
	public void setImei(long imei) {
		this.imei = imei;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	
	
}
