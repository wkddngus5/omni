package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Timestamp;


public class UserMembershipVO {

	String id;
	String userId;
	String membershipPlanId;
	boolean canceled;
	MembershipPlanVO plan;
	Date startTime;
	Date canceledTime;
	Date throughTime;
	UserVo user;
	String stripeId;
	int interval;
	int intervalCount;
	int available;


	public String getId () {
		return id;		
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getUserId () {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getMembershipPlanId () {
		return membershipPlanId;
	}

	public void setMembershipPlanId(String membershipPlanId) {
		this.membershipPlanId = membershipPlanId;
	}


	public boolean getCanceled () {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}


	public MembershipPlanVO getPlan () {
		return plan;
	}

	public void setPlan (MembershipPlanVO plan) {
		this.plan = plan;
	}


	public Date getStartTime () {
		return startTime;
	}

	public void setStartTime (Date startTime) {
		this.startTime = startTime;
	}


	public Date getCanceledTime () {
		return canceledTime;
	}

	public void setCanceledTime (Date canceledTime) {
		this.canceledTime = canceledTime;
	}


	public Date getThroughTime () {
		return throughTime;
	}

	public void setThroughTime (Date throughTime) {
		this.throughTime = throughTime;
	}


	public UserVo getUser () {
		return user;
	}

	public void setUser (UserVo user) {
		this.user = user;
	}


	public String getStripeId () {
		return stripeId;
	}

	public void setStripeId (String stripeId) {
		this.stripeId = stripeId;
	}


	/**
	 * @return the available
	 */
	public int getAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(int available) {
		this.available = available;
	}

	public Date getRenewDate () {

		long periodDuration_ms = (long)MembershipPlanVO.calculatePeriodMinutes(interval, intervalCount) * 60 * 1000;

		if (canceled) {

			long period = (long)(canceledTime.getTime() - startTime.getTime()) / periodDuration_ms;
			long end_ms = (period * periodDuration_ms) + startTime.getTime();
			return new Date(end_ms);

		}

		Date now = new Date();

		long period = (long)(now.getTime() - startTime.getTime()) / periodDuration_ms;
		long end_ms = ((period + 1) * periodDuration_ms) + startTime.getTime();
		return new Date(end_ms);

	}

	public void setWithResultSet (ResultSet rs, String prefix) throws SQLException {

		this.id = rs.getString(prefix + "id");
		this.userId = rs.getString(prefix + "user_id");
		this.membershipPlanId = rs.getString(prefix + "membership_plan_id");
		this.canceled = rs.getInt(prefix + "canceled") != 0;

		Timestamp startTimestamp = rs.getTimestamp(prefix + "start_time");
		this.startTime = startTimestamp == null? null: new Date(startTimestamp.getTime());

		Timestamp canceledTimestamp = rs.getTimestamp(prefix + "canceled_time");
		this.canceledTime = canceledTimestamp == null? null: new Date(canceledTimestamp.getTime());

		Timestamp throughTimestamp = rs.getTimestamp(prefix + "through_time");
		this.throughTime = throughTimestamp == null? null: new Date(throughTimestamp.getTime());

		this.stripeId = rs.getString(prefix + "stripe_id");
		this.interval = rs.getInt(prefix + "interval");
		this.intervalCount = rs.getInt(prefix + "interval_count");

	}


	public UserMembershipVO () {
	}


	public UserMembershipVO (ResultSet rs, String prefix) throws SQLException {
		setWithResultSet(rs, prefix);
	}


	public UserMembershipVO (ResultSet rs) throws SQLException {
		setWithResultSet(rs, "");
	}

}