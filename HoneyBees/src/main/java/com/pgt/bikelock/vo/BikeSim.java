package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

public class BikeSim {
	private int id;
	private String bid;
	private String cid;
	
	private long imei;
	private String iccid;
	
	private BikeVo bikeVo;
	private BikeCardPhoneVo phoneVo;
	
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
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
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
	/**
	 * @return the cid
	 */
	public String getCid() {
		return cid;
	}
	/**
	 * @param cid the cid to set
	 */
	public void setCid(String cid) {
		this.cid = cid;
	}
	/**
	 * @return the bikeVo
	 */
	public BikeVo getBikeVo() {
		return bikeVo;
	}
	/**
	 * @param bikeVo the bikeVo to set
	 */
	public void setBikeVo(BikeVo bikeVo) {
		this.bikeVo = bikeVo;
	}
	/**
	 * @return the phoneVo
	 */
	public BikeCardPhoneVo getPhoneVo() {
		return phoneVo;
	}
	/**
	 * @param phoneVo the phoneVo to set
	 */
	public void setPhoneVo(BikeCardPhoneVo phoneVo) {
		this.phoneVo = phoneVo;
	}
	
	public BikeSim(){}
	
	public BikeSim(ResultSet rst) throws SQLException{
		this.id = BaseDao.getInt(rst,"id");
		this.bid = BaseDao.getString(rst,"bid");
		this.cid = BaseDao.getString(rst,"cid");
		this.iccid = BaseDao.getString(rst,"iccid");
		this.bikeVo = new BikeVo(rst);
		this.phoneVo = new  BikeCardPhoneVo(rst);
	}

}
