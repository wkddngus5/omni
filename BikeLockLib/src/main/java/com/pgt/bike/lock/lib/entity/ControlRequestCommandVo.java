package com.pgt.bike.lock.lib.entity;

public class ControlRequestCommandVo {
	
	private long imei;
	
	private int action;
	private int key;
	private int uid;
	private long timestamp;
	
	public ControlRequestCommandVo() {
		super();
		 
	}
	
	
	public ControlRequestCommandVo(int action, int key, int uid,
			long timestamp) {
		super();
		this.action = action;
		this.key = key;
		this.uid = uid;
		this.timestamp = timestamp;
	}
	
	
	
	public long getImei() {
		return imei;
	}


	public void setImei(long imei) {
		this.imei = imei;
	}


	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	

}
