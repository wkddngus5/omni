package com.pgt.bikelock.vo;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.pgt.bikelock.utils.TimeUtil;



public class MembershipPlanVO {

	public static final int MINUTE = 1;
	public static final int HOUR = 2;
	public static final int MONTH = 3;
	public static final int DAY = 4;



	String id;
	String cityId;
	int interval;
	int intervalCount;
	BigDecimal planPrice;
	int rideUnit;
	int rideFreeUnitCount;
	String title;
	String description;
	boolean active;
	String stripeId;
	boolean isRenewable;
	boolean isEducation;

	

	static String unitToString (int unit) {
		switch (unit) {
			case MINUTE: return "minute";
			case HOUR: return "hour";
			case MONTH: return "month";
			case DAY: return "day";
			default:
				return "undefined";
		}
	}


	public String getIntervalDescription () {
		return unitToString(interval);
	}


	public int getFreeMinutes () {

		switch (rideUnit) {
			case MINUTE: return rideFreeUnitCount;
			case HOUR:   return 60 * rideFreeUnitCount;
			case MONTH:  return 730 * 60 * rideFreeUnitCount;
			case DAY: return 24 * 60 * rideFreeUnitCount;
			default:
				return 0;
		}

	}


	static int calculatePeriodMinutes (int interval, int count) {

		switch (interval) {
			case MINUTE: return count;
			case HOUR:   return 60 * count;
			case MONTH:  return 730 * 60 * count;
			case DAY: return 24 * 60 * count;
			default:
				return 0;
		}

	}


	public int getPeriodMinutes () {
		return calculatePeriodMinutes(getInterval(), getIntervalCount());
	}


	public String getId () {
		return id;
	}

	public void setId (String id) {
		this.id = id;
	}


	public String getCityId () {
		return cityId;
	}

	public void setCityId (String cityId) {
		this.cityId = cityId;
	}


	public int getInterval () {
		return interval;
	}

	public void setInterval (int interval) {
		this.interval = interval;
	}


	public int getIntervalCount () {
		return intervalCount;
	}

	public void setIntervalCount (int intervalCount) {
		this.intervalCount = intervalCount;
	}


	public BigDecimal getPlanPrice () {
		return planPrice;
	}

	public void setPlanPrice (BigDecimal planPrice) {
		this.planPrice = planPrice;
	}


	public int getRideUnit () {
		return rideUnit;
	}

	public void setRideUnit (int rideUnit) {
		this.rideUnit = rideUnit;
	}


	public int getRideFreeUnitCount () {
		return rideFreeUnitCount;
	}

	public void setRideFreeUnitCount (int rideFreeUnitCount) {
		this.rideFreeUnitCount = rideFreeUnitCount;
	}


	public String getRideUnitDescription () {
		return MembershipPlanVO.unitToString(rideUnit);
	}

	public String getTitle () {
		return title;
	}

	public void setTitle (String title) {
		this.title = title;
	}


	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}


	public boolean getActive () {
		return active;
	}

	public void setActive (boolean active) {
		this.active = active;
	}


	public String getStripeId () {
		return stripeId;
	}

	public void setStripeId (String stripeId) {
		this.stripeId = stripeId;
	}


	public boolean getIsRenewable () {
		return isRenewable;
	}

	public void setIsRenewable (boolean isRenewable) {
		this.isRenewable = isRenewable;
	}


	/**
	 * @return the isEducation
	 */
	public boolean isEducation() {
		return isEducation;
	}


	/**
	 * @param isEducation the isEducation to set
	 */
	public void setEducation(boolean isEducation) {
		this.isEducation = isEducation;
	}


	private void setWithResultSet (ResultSet rs, String prefix) throws SQLException {

		this.id = rs.getString(prefix + "id");
		this.cityId = rs.getString(prefix + "city_id");
		this.interval = rs.getInt(prefix + "interval");
		this.intervalCount = rs.getInt(prefix + "interval_count");
		this.planPrice = rs.getBigDecimal(prefix + "plan_price");
		this.rideUnit = rs.getInt(prefix + "ride_unit");
		this.rideFreeUnitCount = rs.getInt(prefix + "ride_free_unit_count");
		this.title = rs.getString(prefix + "title");
		this.description = rs.getString(prefix + "description");
		this.active = rs.getInt(prefix + "active") != 0;
		this.stripeId = rs.getString(prefix + "stripe_id");
		this.isRenewable = rs.getInt(prefix + "is_renewable") != 0;
		this.isEducation = rs.getInt(prefix + "is_education") != 0;
	}


	public MembershipPlanVO () {
	}


	public MembershipPlanVO (ResultSet rs, String prefix) throws SQLException {
		setWithResultSet(rs, prefix);
	}


	public MembershipPlanVO (ResultSet rs) throws SQLException {
		setWithResultSet(rs, "");
	}

}