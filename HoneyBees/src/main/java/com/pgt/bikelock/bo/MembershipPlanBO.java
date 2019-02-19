package com.pgt.bikelock.bo;

import java.math.BigDecimal;
import java.util.Date;
import com.pgt.bikelock.dao.impl.MembershipPlanDAO;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.vo.MembershipPlanVO;
import com.pgt.bikelock.vo.UserMembershipVO;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.utils.pay.StripPayUtil;
import com.pgt.bikelock.resource.OthersSource;
import com.stripe.model.Subscription;
import com.stripe.model.Plan;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;




public class MembershipPlanBO {

	static final String PAY_DESCRIPTION = OthersSource.STRIP_PAY_MEMBERSHIP_CHARGE_DESCRIPTION;

	MembershipPlanDAO planDAO;


	public MembershipPlanBO () {
		planDAO = new MembershipPlanDAO();
	}


	public boolean recieveStripeSubscriptionPayment (String stripeSubId, boolean wasPaid, BigDecimal amount) {

		System.out.println("recieveStripeSubscriptionPayment:" + stripeSubId + "," + wasPaid);

		if (stripeSubId == null) {
			return false;
		}

		UserMembershipVO userPlan = planDAO.getUserMembershipWithStripeId(stripeSubId);

		if (userPlan == null) {
			System.out.println("failed to fetch user plan.");
			return false;
		}

		if (!wasPaid) {

			planDAO.cancelImmediateUserMembershipForUser(userPlan.getUserId(), userPlan.getId());
			StripPayUtil.cancelImmediateSubscription(stripeSubId);

		}

		TradeVo membershipTrade = new TradeVo(userPlan.getUserId(), 4, TradeVo.Trade_PayWay_Stripe, amount);

		membershipTrade.setStatus(wasPaid? 1: 0);
		membershipTrade.setOut_trade_no(stripeSubId);

		String tradeId = new TradeDaoImpl().addTrade(membershipTrade);

		if (tradeId == null) {
			System.out.println("persist trade failed.");
			return false;
		}

		return true;

	}


	public MembershipPlanVO createMembershipPlan (MembershipPlanVO plan, String currency) {

		if (plan == null || currency == null) {
			return null;
		}

		MembershipPlanVO storedPlan = planDAO.insertMembershipPlan(plan);

		if (storedPlan == null) {
			return null;
		}

		Plan stripePlan = StripPayUtil.createPlan(
			"plan_" + storedPlan.getId(),
			currency,
			storedPlan.getIntervalDescription(),
			storedPlan.getTitle(),
			storedPlan.getPlanPrice(),
			storedPlan.getIntervalCount(),
			PAY_DESCRIPTION != null? PAY_DESCRIPTION: "honeybees membership"
		);

		return planDAO.updateMembershipPlanStripe(storedPlan, stripePlan.getId());

	}


	public boolean addUserToMembershipPlan (
		String userId, String planId, String currency, boolean userAutorenew) {

		if (userId == null || planId == null) {
			return false;
		}

		UserVo user = new UserDaoImpl().getUserWithId(userId, true);
		MembershipPlanVO plan = new MembershipPlanDAO().getMembershipPlan(planId);

		return addUserToMembershipPlan(user, plan, currency, userAutorenew);

	}


	public boolean addUserToMembershipPlan (
		UserVo user, MembershipPlanVO plan, String currency, boolean userAutorenew) {

		if (user == null || plan == null || currency == null) {
			return false;
		}


		// 1. make sure Stripe plan exists for Membership Plan
		if (plan.getStripeId() == null) {

			// create plan on-the-fly
			Plan stripePlan = StripPayUtil.createPlan(
				"plan_" + plan.getId(),
				currency,
				plan.getIntervalDescription(),
				plan.getTitle(),
				plan.getPlanPrice(),
				plan.getIntervalCount(),
				PAY_DESCRIPTION != null? PAY_DESCRIPTION: "honeybees membership"
			);

			if (stripePlan == null) {
				System.out.println("stripe plan failed creation.");
				return false;
			}

			plan = planDAO.updateMembershipPlanStripe(plan, stripePlan.getId());

			if (plan == null) {
				System.out.println("update to membership plan failed");
				return false;
			}

		}


		// 2. check if user has active plan
		UserMembershipVO currentUserPlan = planDAO.getActiveUserMembershipsForUser(user);
		if (currentUserPlan != null) {

			// 2. a. is current plan upgradeable
			MembershipPlanVO currentPlan = planDAO.getMembershipPlan(currentUserPlan.getMembershipPlanId());
			if (currentPlan != null && currentPlan.getPeriodMinutes() >= plan.getPeriodMinutes()) {
				System.out.println("cannot upgrade plan.");
				return false;
			}

			// 2. b. cancel current-plan
			boolean didCancel = cancelUserMembershipPlan(user.getuId(), currentUserPlan.getId());
			if (!didCancel) {
				System.out.println("could not cancel old plan.");
				return false;
			}

		}


		// 3. create Stripe Subscription
		Subscription subscription = StripPayUtil.subscribeCustomerToPlan(user.getuId(), plan.getStripeId());

		if (subscription == null) {
			System.out.println("create stripe subscription failed.");
			return false;
		}


		// 4. create User Membership 
		int userPlanId = planDAO.insertUserMembershipForUser(user, plan, subscription.getId());

		if (userPlanId < 0) {
			System.out.println("create user membership entry failed.");
			return false;
		}


		// 5. cancel non-renewable User Membership or user choose
		if (!userAutorenew || !plan.getIsRenewable()) {

			boolean didCancel = cancelUserMembershipPlan(user.getuId(), userPlanId + "");
			if (!didCancel) {
				System.out.println("cancel autorenew membership plan failed.");
			}

		}


		return true;

	}


	public boolean cancelUserMembershipPlan (String userId, String userPlanId) {
		
		if (userId == null || userPlanId == null) {
			return false;
		}

		UserVo user = new UserDaoImpl().getUserWithId(userId, true);
		UserMembershipVO userPlan = new MembershipPlanDAO().getUserMembership(userPlanId);

		return cancelUserMembershipPlan(user, userPlan);

	}


	public boolean cancelUserMembershipPlan (UserVo user, UserMembershipVO userPlan) {


		if (user == null || userPlan == null) {
			return false;
		}

		Subscription cancledSubscription = StripPayUtil.cancelSubscription(userPlan.getStripeId());

		if (cancledSubscription == null) {
			return false;
		}

		Date currentPeriodEnd = new Date(cancledSubscription.getCurrentPeriodEnd() * 1000);

		return new MembershipPlanDAO().cancelUserMembershipForUser(user, userPlan, currentPeriodEnd);

	}

}