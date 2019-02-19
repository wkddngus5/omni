package com.pgt.bikelock.bo;

import java.math.BigDecimal;

import java.util.List;
import java.util.Date;
import java.util.List;


import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.HoldVO;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.dao.impl.HoldDAO;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.bo.BikeBo;



public class HoldBO {

	public static final int DEFAULT_HOLD_TTL_SECONDS = 15 * 60;

	public static final int MISSING_ARUGMENT_ERRNO = -1;
	public static final int ALREADY_HAVE_ACTIVE_HOLD_ERRNO = -2;
	public static final int NO_PREVIOUS_BIKE_USAGE_ERRNO = -3;
	public static final int BIKE_IN_USE_ERRNO = -4;
	public static final int FAILED_TO_SAVE_HOLD_ERRNO = -5;
	public static final int BIKE_WAS_NOT_RESERVED_ERRNO = -6;
	public static final int FAILED_TO_UPDATE_BIKE_USE_STATUS_ERRNO = -7;
	public static final int NO_PREVIOUS_HOLD_ERRNO = -8;
	public static final int BIKE_NOT_FOUND_ERRNO = -9;
	public static final int BIKE_TYPE_NOT_FOUND_ERRNO = -10;

	public static String errnoString (int errno) {
		switch (errno) {
			case MISSING_ARUGMENT_ERRNO: return "Missing argument(s).";
			case ALREADY_HAVE_ACTIVE_HOLD_ERRNO: return "Bike hold is already active.";
			case NO_PREVIOUS_BIKE_USAGE_ERRNO: return "Could not find previous bike.";
			case BIKE_IN_USE_ERRNO: return "Bike is already in use.";
			case FAILED_TO_SAVE_HOLD_ERRNO: return "Failed to complete complete request.";
			case BIKE_WAS_NOT_RESERVED_ERRNO: return "Bike could not be reserved.";
			case FAILED_TO_UPDATE_BIKE_USE_STATUS_ERRNO: return "Bike could not be reserved.";
			case NO_PREVIOUS_HOLD_ERRNO: return "No active hold could be found.";
			case BIKE_NOT_FOUND_ERRNO: return "Could not find bike.";
			case BIKE_TYPE_NOT_FOUND_ERRNO: return "Could not find bike type.";
			default: return "server error";
		}
	}


	BikeUseDaoImpl bikeUseDAO;
	HoldDAO holdDAO;
	UserDaoImpl userDAO;
	BikeDaoImpl bikeDAO;


	static String generateGroupIdForUserId (String userId) {
		return userId + ":" + (new Date().getTime() / 1000);
	}


	public HoldBO () {

		bikeUseDAO = new BikeUseDaoImpl();
		holdDAO = new HoldDAO();
		userDAO = new UserDaoImpl();
		bikeDAO = new BikeDaoImpl();

	}


	public List<HoldVO> holdsForUser (String userId) {

		
		if (userId == null) {
			return null;
		}
		return holdDAO.getHoldsForUser(userId);

	}


	public BikeVo lastUseBikeForUser (UserVo user) {

		if (user == null) {
			return null;
		}
		return lastUseBikeForUser(user.getuId());

	}


	public BikeVo lastUseBikeForUser (String userId) {

		if (userId == null) {
			return null;
		}

		BikeUseVo lastUse = bikeUseDAO.getLastBikeUseDetialedForUser(userId);
		if (lastUse == null) {
			return null;
		}

		BikeVo lastBike = lastUse.getBikeVo();
		return lastBike;
		
	}


	public BikeTypeVo lastUseBikeTypeForUser (UserVo user) {

		if (user == null) {
			return null;
		}
		return lastUseBikeTypeForUser(user.getuId());

	}


	public BikeTypeVo lastUseBikeTypeForUser (String userId) {

		if (userId == null) {
			return null;
		}

		BikeVo lastBike = lastUseBikeForUser(userId);
		if (lastBike == null) {
			return null;
		}

		return lastBike.getTypeVo();

	}


	public HoldVO activeHoldForUser (UserVo user) {

		if (user == null) {
			return null;
		}
		return activeHoldForUser(user.getuId());

	}


	public HoldVO activeHoldForUser (String userId) {

		if (userId == null) {
			return null;
		}
		return holdDAO.getActiveHoldForUserId(userId);

	}


	public int holdBikeForUser (UserVo user) {

		if (user == null) {
			return MISSING_ARUGMENT_ERRNO;
		}
		return holdBikeForUser(user.getuId());

	}


	public int holdBikeForUser (String userId) {

		if (userId == null) {
			System.out.println("missing user.");
			return MISSING_ARUGMENT_ERRNO;
		}


		// 1. check if user has active hold
		HoldVO lastHold = holdDAO.getLastHoldForUser(userId);

		if (lastHold != null && lastHold.isActive()) {
			System.out.println("already have active hold.");
			return ALREADY_HAVE_ACTIVE_HOLD_ERRNO;
		}


		// 2. fetch last ride use for user
		BikeUseVo lastUse = bikeUseDAO.getLastBikeUseDetialedForUser(userId);

		if (lastUse == null) {
			System.out.println("no previous bike use.");
			return NO_PREVIOUS_BIKE_USAGE_ERRNO;
		}

		if (lastUse.getUseStatus() != BikeUseVo.STATUS_FREE) {
			System.out.println("bike is currently in use.");
			return BIKE_IN_USE_ERRNO;
		}


		// 3. generate group id for hold
		boolean generateNewGroup = lastHold == null ||
			lastHold.getNextUseId() == null ||
			!lastHold.getNextUseId().equals(lastUse.getId());

		String groupId = generateNewGroup
			? generateGroupIdForUserId(userId)
			: lastHold.getGroupId();


		// 4. build and presist new hold
		HoldVO newHold = new HoldVO();
		Date now = new Date();

		int maxHoldSeconds = DEFAULT_HOLD_TTL_SECONDS;
		BikeVo lastBike = lastUse.getBikeVo();
		if (lastBike != null) {
			BikeTypeVo type = lastBike.getTypeVo();
			if (type != null) {
				maxHoldSeconds = type.getMaxHoldDurationSeconds();
			}
		}
		Date expirationTime = new Date(now.getTime() + (maxHoldSeconds * 1000));
		

		newHold.setPrevUseId(lastUse.getId());
		newHold.setPrevBikeId(lastUse.getBid());
		newHold.setUserId(userId);
		newHold.setStartTime(now);
		newHold.setExpirationTime(expirationTime);
		newHold.setGroupId(groupId);

		String newHoldId = holdDAO.insertHold(newHold);

		if (newHold == null) {
			System.out.println("failed to save new hold.");
			return FAILED_TO_SAVE_HOLD_ERRNO;
		}


		// 5. change bike status to reserved
		int status = makeBikeUserStatusReserved(lastUse.getBid());
		if (status < 0) {
			return BIKE_WAS_NOT_RESERVED_ERRNO;
		}


		return 0;

	}


	public int payActiveHoldWhenStartRide (BikeUseVo use) {

		if (use == null || use.getUid() == null) {
			System.out.println("missing bike use.");
			return MISSING_ARUGMENT_ERRNO;
		}


		// 1. fetch last hold
		HoldVO lastHold = holdDAO.getLastHoldForUser(use.getUid());

		if (lastHold == null || !lastHold.isActive()) {
			System.out.println("no active active holds");
			return 0;
		}


		// 2. do payment processing
		String tradeId = payForHold(lastHold);


		// 3. change old bike status if reserved and if different bike
		if (!lastHold.getPrevBikeId().equals(use.getBid())) {
			System.out.println("user unlocked non-held bike, set held bike status to available.");
			makeBikeUseStatusAvailableIfReserved(lastHold.getPrevBikeId());
		}


		// 4. update last hold
		Date now = new Date();

		lastHold.setNextUseId(use.getId());
		lastHold.setExpirationTime(now);
		lastHold.setTradeId(tradeId);

		int status = holdDAO.closeHold(lastHold);

		if (status < 0) {
			System.out.println("failed to update last hold.");
			return FAILED_TO_SAVE_HOLD_ERRNO;
		}


		return 0;

	}


	public int cancelAndPayHoldForUser (UserVo user) {

		if (user == null) {
			return MISSING_ARUGMENT_ERRNO;
		}
		return cancelAndPayHoldForUser(user.getuId());

	}


	public int cancelAndPayHoldForUser (String userId) {

		if (userId == null) {
			return MISSING_ARUGMENT_ERRNO;
		}

		HoldVO activeHold = activeHoldForUser(userId);

		if (activeHold == null) {
			return NO_PREVIOUS_HOLD_ERRNO;
		}

		return cancelAndPayHold(activeHold);

	}


	public int cancelAndPayHold (String holdId) {

		if (holdId == null) {
			return MISSING_ARUGMENT_ERRNO;
		}
		HoldVO hold = holdDAO.getHold(holdId);
		return cancelAndPayHold(hold);

	}

	
	public int cancelAndPayHold (HoldVO hold) {

		if (hold.getTradeId() != null) {
			makeBikeUseStatusAvailableIfReserved(hold.getPrevBikeId());
			return 0;
		}


		// 1. do payment processing
		String tradeId = payForHold(hold);


		// 2. update bike status if reserved
		makeBikeUseStatusAvailableIfReserved(hold.getPrevBikeId());


		// 3. update hold expiration and trade details
		Date now = new Date();

		hold.setNextUseId(hold.getNextUseId());
		hold.setExpirationTime(now);
		hold.setTradeId(tradeId);

		int status = holdDAO.closeHold(hold);

		if (status < 0) {
			System.out.println("failed to update hold.");
			return FAILED_TO_SAVE_HOLD_ERRNO;
		}


		return 0;

	}


	public int checkExpiredHolds () {

		List<HoldVO> listOfExpiredHolds = holdDAO.getExpiredHolds();
		for (HoldVO expiredHold : listOfExpiredHolds) {
			cancelAndPayHold(expiredHold);
		}
		return 0;

	}


	public int enforceHoldForBikeId (String bikeId) {

		if (bikeId == null) {
			return MISSING_ARUGMENT_ERRNO;
		}

		if (holdDAO.getActiveHoldsCountWithBikeId(bikeId) > 0) {
			return makeBikeUserStatusReserved(bikeId);
		}

		return 0;

	}


	public int makeBikeUserStatusReserved (String bikeId) {

		if (bikeId == null) {
			return MISSING_ARUGMENT_ERRNO;
		}

		int RESERVED = 4;
		System.out.println("place bike on hold: " + bikeId);

		int status = bikeDAO.updateBikeUseStatus(bikeId, RESERVED);
		if (status < 1) {
			return FAILED_TO_UPDATE_BIKE_USE_STATUS_ERRNO;
		}

		return 0;

	}


	public int makeBikeUseStatusAvailableIfReserved (String bikeId) {

		if (bikeId == null) {
			return MISSING_ARUGMENT_ERRNO;
		}

		int AVAILABLE = 0;
		System.out.println("remove bike hold: " + bikeId);
		
		int status = bikeDAO.updateBikeUseStatus(bikeId, AVAILABLE);
		if (status < 1) {
			return FAILED_TO_UPDATE_BIKE_USE_STATUS_ERRNO;
		}

		return 0;

	}


	public String payForHold (HoldVO hold) {

		// prereqs
		if (hold == null) {
			return null;
		}
		
		BikeVo bike = bikeDAO.getBikePriceInfo(hold.getPrevBikeId());
		if (bike == null) {
			return null;
		}

		BikeTypeVo type = bike.getTypeVo();
		if (type == null) {
			return null;
		}

		UserVo user = userDAO.getUserWithIndustryInfo(hold.getUserId());
		if (user == null) {
			return null;
		}


		// calculate cost
		int duration = (int)(new Date().getTime() - hold.getStartTime().getTime()) / 1000;
		duration = duration > type.getMaxHoldDurationSeconds()? type.getMaxHoldDurationSeconds(): duration;
		duration = duration > 0? duration - 1: 0;

		int periodsToPayFor = (int)(duration / type.getHoldPeriodDurationSeconds()) + 1;
		BigDecimal amount = new BigDecimal(periodsToPayFor).multiply(type.getHoldPrice());

		// persist trade object
		TradeVo trade = new TradeVo(user.getuId(), 6, TradeVo.Trade_PayWay_Account, amount);

		// process account payment
		boolean success = PayBo.accountPay(user, trade);
		
		trade.setRecordId(hold.getId());
		trade.setStatus(success? 1: 0);

		String tradeId = new TradeDaoImpl().addTrade(trade);
		return tradeId;


	}


}