/**
 * FileName:     CouponBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月17日 下午3:50:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月17日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.math.BigDecimal;

import org.bouncycastle.jce.provider.JDKGOST3410Signer.gost3410;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IBikeUseDao;
import com.pgt.bikelock.dao.IUserCouponDao;
import com.pgt.bikelock.dao.impl.CouponDaoImpl;
import com.pgt.bikelock.dao.impl.UserCouponDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.CouponVo;
import com.pgt.bikelock.vo.UserCouponVo;
import com.pgt.bikelock.vo.UserCouponVo.GiftFromType;

/**
 * @ClassName:     CouponBo
 * @Description:优惠券业务控制/coupon business control
 * @author:    Albert
 * @date:        2017年5月17日 下午3:50:55
 *
 */
public class CouponBo {

	/**
	 * 
	 * @Title:        crateRegisterCoupon 
	 * @Description:  生成注册优惠券/gererate register coupon
	 * @param:        @param userId    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月17日 下午4:14:36
	 */
	public static void crateRegisterCoupon(String userId,int cityId){
		long nowTime = TimeUtil.getCurrentLongTime();
		CouponVo couponVo = new CouponDaoImpl().getFreeCouponInfo(3,cityId);
		if(couponVo == null || ValueUtil.getLong(couponVo.getEnd_time()) < nowTime){
			return;
		}

		UserCouponVo userCouponVo = new UserCouponVo();
		userCouponVo.setUid(userId);
		userCouponVo.setCid(couponVo.getId());
		userCouponVo.setStart_time(nowTime+"");
		userCouponVo.setEnd_time((nowTime+couponVo.getDay()*24*60*60)+"");
		userCouponVo.setAmount(couponVo.getValue());

		if(couponVo.getValue().compareTo(new BigDecimal(0)) == 0){//验证金额为0，自动激活/verify amount as 0, auto activating
			userCouponVo.setActive_date("now()");
			new UserCouponDaoImpl().addCouponForUserActive(userCouponVo);
			//通知/inform
			couponNotify(1, couponVo.getId(), userId,null);
		}else{//待充值对应金额后激活/active after recharge amount
			new UserCouponDaoImpl().addCouponForUserNotActive(userCouponVo);
		}

	}
	
	/**
	 * 
	 * @Title:        crateRegisterAutoSendCoupon 
	 * @Description:  创建注册自动赠送优惠券/creat register auto send coupon
	 * @param:        @param userId
	 * @param:        @param cityId    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月15日 下午9:56:17
	 */
	public static void crateRegisterAutoSendCoupon(String userId,int cityId){
		if(OthersSource.getSourceString("register_coupon_auto_send") == null){
			return;
		}
		long nowTime = TimeUtil.getCurrentLongTime();
		CouponVo couponVo = new CouponDaoImpl().getFreeCouponInfo(3,cityId);
		if(couponVo == null || ValueUtil.getLong(couponVo.getEnd_time()) < nowTime){
			return;
		}
		for (int i = 0; i < couponVo.getDay(); i++) {
			UserCouponVo userCouponVo = new UserCouponVo();
			userCouponVo.setUid(userId);
			userCouponVo.setCid(couponVo.getId());
			userCouponVo.setStart_time(nowTime+"");
			userCouponVo.setEnd_time((nowTime+couponVo.getDay()*24*60*60)+"");
			userCouponVo.setAmount(couponVo.getValue());

			//待激活/to be activated
			if(new UserCouponDaoImpl().addCouponForUserNotActive(userCouponVo)){
				//通知/inform
				couponNotify(1, couponVo.getId(), userId,couponVo.getName());
			}
	
		}
		
	}
	

	/**
	 * 
	 * @Title:        activeRegisterCoupon 
	 * @Description:  激活注册优惠券/activate register coupon
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月19日 上午10:49:18
	 */
	public static boolean activeRegisterCoupon(String userId,BigDecimal amount){
		IUserCouponDao couponDao = new UserCouponDaoImpl();

		UserCouponVo couponVo = couponDao.getRegisterNotActiveCoupon(userId);
		if(couponVo == null || amount.compareTo(couponVo.getCouponVo().getValue()) == -1){
			return false;
		}
		boolean flag = couponDao.activeCoupon(couponVo.getId());
		if(flag){
			//通知/inform
			couponNotify(2, couponVo.getId(), userId,null);
		}
		return flag;
	}

	/**
	 * 
	 * @Title:        haveFreeCoupon 
	 * @Description:  判定用户有无免费骑行（注册、定额优惠券）/judge user whether have free riding
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月17日 下午4:30:29
	 */
	public static boolean haveFreeCoupon(String userId,BigDecimal price,int cityId){

		IUserCouponDao couponDao = new UserCouponDaoImpl();

		if(!StringUtils.isEmpty(haveTimeFreeCoupon(cityId))){
			return true;
		}
		if(haveRegisterCoupon(userId, couponDao)){
			return true;
		}
		if(haveValueCoupon(userId, couponDao, price)){
			return true;
		}

		return false;
	}

	/**
	 * 
	 * @Title:        haveTimeFreeCoupon 
	 * @Description:  有无限时优惠券/whether have time-limited coupon
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年6月22日 下午3:06:09
	 */
	public static String haveTimeFreeCoupon(int cityId){
		CouponVo couponVo = new CouponDaoImpl().getFreeCouponInfo(4,cityId);
		if(couponVo == null){
			return null;
		}
		return couponVo.getId();
	}

	/**
	 * 
	 * @Title:        haveRegisterCoupon 
	 * @Description:  有无注册免费优惠券/whether have register free coupon
	 * @param:        @param userId
	 * @param:        @param couponDao
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月19日 下午2:58:19
	 */
	public static boolean haveRegisterCoupon(String userId,IUserCouponDao couponDao){
		long nowTime = TimeUtil.getCurrentLongTime();

		UserCouponVo couponVo = couponDao.getRegisterActivedCoupon(userId);
		if(couponVo == null || couponVo.getActive_date() == null){//优惠券必须激活/coupon must activate
			return false;
		}else if(ValueUtil.getLong(couponVo.getEnd_time()) < nowTime){//已过期/expired
			couponDao.useCoupon(couponVo.getId());//标记为已使用/sign as used
			return false;
		}
		return true;
	}


	/**
	 * 
	 * @Title:        haveValueCoupon 
	 * @Description:  有无定额优惠券/whether have coupon in fixed amount
	 * @param:        @param userId
	 * @param:        @param couponDao
	 * @param:        @param price
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月19日 下午3:00:03
	 */
	public static boolean haveValueCoupon(String userId,IUserCouponDao couponDao,BigDecimal price){
		long nowTime = TimeUtil.getCurrentLongTime();

		UserCouponVo couponVo = couponDao.getUserValueCoupon(userId, price);
		if(couponVo == null){//优惠券必须激活/coupon must activate
			return false;
		}else if(ValueUtil.getLong(couponVo.getEnd_time()) < nowTime){//已过期/expired
			couponDao.useCoupon(couponVo.getId());//标记为已使用/sign as used
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Title:        getSystemCoupon 
	 * @Description:  获取系统优惠券/obtain system coupon
	 * @param:        @return    
	 * @return:       CouponVo    
	 * @author        Albert
	 * @Date          2017年7月18日 下午5:33:16
	 */
	public static CouponVo getSystemCoupon(){
		if(OthersSource.getSourceString("system_coupon_id") == null){
			return null;
		}
		return new CouponDaoImpl().getCouponInfo(OthersSource.getSourceString("system_coupon_id"),false);
	}
	
	/**
	 * 
	 * @Title:        addSystemCouponForUser 
	 * @Description:  为用户添加/add for user
	 * @param:        @param userId
	 * @param:        @param num
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月17日 下午4:58:21
	 */
	public static boolean addSystemCouponForUser(String userId,int num,GiftFromType giftFrom){
		if(getSystemCoupon() == null){
			return false;
		}
		
		IUserCouponDao couponDao = new UserCouponDaoImpl();
		int giftFromValue = UserCouponVo.getGift_from(giftFrom);
		int typeCount = couponDao.getTypeCouponCount(userId, giftFromValue);
		int maxInviteCount = ValueUtil.getInt(OthersSource.getSourceString("invite_friend_register_coupon_num_count"));
		int maxReportCount = ValueUtil.getInt(OthersSource.getSourceString("bike_error_coupon_num_count"));
		if((maxInviteCount > 0 && typeCount >= maxInviteCount) ||
				(maxReportCount > 0 && typeCount >= maxReportCount)){
			return false;
		}
		
		for (int i = 0; i < num; i++) {
			//优惠券赠送/send coupon
			CouponVo couponVo = new CouponDaoImpl().getCouponInfo(OthersSource.getSourceString("system_coupon_id"),false);
			if(couponVo != null){
				UserCouponVo userCoupon = new UserCouponVo();
				userCoupon.setUid(userId);
				userCoupon.setCid(couponVo.getId());
				userCoupon.setStart_time(couponVo.getStart_time());
				userCoupon.setEnd_time(couponVo.getEnd_time());
				userCoupon.setAmount(couponVo.getValue());
				userCoupon.setGift_from(giftFromValue);
				couponDao.addCouponForUserNotActive(userCoupon);
				
				//通知/inform
				couponNotify(1, couponVo.getId(), userId,null);
			}
		}

		return true;
	}
	
	/**
	 * 
	 * @Title:        couponNotify 
	 * @Description:  优惠券变动通知/coupon change inform
	 * @param:        @param type 1：添加（已激活或可手动激活）/add(activated or manual activate)  2：激活activate
	 * @param:        @param couponId
	 * @param:        @param userId    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月1日 下午4:48:29
	 */
	public static void couponNotify(int type,String couponId,String userId,String couponName){
		if(couponName == null){
			CouponVo couponVo = new CouponDaoImpl().getCouponInfo(couponId,false);
			if(couponVo == null){
				return;
			}
			couponName = couponVo.getName();
		}
		
		
		//添加通知/add modify
		if(type == 1){//添加（已激活或可手动激活）add(activated or manual activate)
			NotificationBo.addNotifiyMessage(true,"0",userId, LanguageUtil.getDefaultValue("coupon_add_notify_title"),
					LanguageUtil.getDefaultValue("coupon_add_notify_content", 
							new Object[]{couponName}));
		}else if(type == 2){//激活/activate
			NotificationBo.addNotifiyMessage(true,"0",userId, LanguageUtil.getDefaultValue("coupon_active_notify_title"),
					LanguageUtil.getDefaultValue("coupon_active_notify_content"));
		}
	}
}
