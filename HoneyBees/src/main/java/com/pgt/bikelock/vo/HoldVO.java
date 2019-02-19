package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.math.BigDecimal;



public class HoldVO {

	String id;
	String prevUseId;
	String nextUseId;
	String prevBikeId;
	String userId;
	Date startTime;
	Date expirationTime;
	String groupId;
	String tradeId;
	BigDecimal tradeAmount;

	public Date getEndTime () {

		if (tradeId == null) {
			return null;
		}
		return expirationTime;
		
	}

	public int getHoldDuration () {
		return (int)((expirationTime.getTime() - startTime.getTime()) / 1000);
	}


	public BigDecimal getTradeAmount () {
		return tradeAmount;
	}

	public void setTradeAmount (BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}


	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;	
	}


	public String getPrevUseId () {
		return prevUseId;
	}

	public void setPrevUseId (String prevUseId) {
		this.prevUseId = prevUseId;	
	}


	public String getNextUseId () {
		return nextUseId;
	}

	public void setNextUseId (String nextUseId) {
		this.nextUseId = nextUseId;	
	}


	public String getPrevBikeId () {
		return prevBikeId;
	}

	public void setPrevBikeId (String prevBikeId) {
		this.prevBikeId = prevBikeId;
	}


	public String getUserId () {
		return userId;
	}

	public void setUserId (String userId) {
		this.userId = userId;	
	}


	public Date getStartTime () {
		return startTime;
	}

	public void setStartTime (Date startTime) {
		this.startTime = startTime;	
	}


	public Date getExpirationTime () {
		return expirationTime;
	}

	public void setExpirationTime (Date expirationTime) {
		this.expirationTime = expirationTime;	
	}


	public String getGroupId () {
		return groupId;
	}

	public void setGroupId (String groupId) {
		this.groupId = groupId;	
	}


	public String getTradeId () {
		return tradeId;
	}

	public void setTradeId (String tradeId) {
		this.tradeId = tradeId;	
	}


	public boolean isActive () {
		return expirationTime == null || new Date().getTime() < expirationTime.getTime();
	}


	private void setWithResultSet (ResultSet rs, String prefix) throws SQLException {

		id = rs.getString(prefix + "id");
		prevUseId = rs.getString(prefix + "prev_use_id");
		nextUseId = rs.getString(prefix + "next_use_id");
		prevBikeId = rs.getString(prefix + "prev_bike_id");
		userId = rs.getString(prefix + "user_id");
		startTime = new Date(rs.getTimestamp(prefix + "start_time").getTime());
		expirationTime = new Date(rs.getTimestamp(prefix + "expiration_time").getTime());
		groupId = rs.getString(prefix + "group_id");
		tradeId = rs.getString(prefix + "trade_id");

	}


	public HoldVO () {
	}


	public HoldVO (ResultSet rs, String prefix) throws SQLException {
		setWithResultSet(rs, prefix);
	}


	public HoldVO (ResultSet rs) throws SQLException {
		setWithResultSet(rs, "");
	}


}