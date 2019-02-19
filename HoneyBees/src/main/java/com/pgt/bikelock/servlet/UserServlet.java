/**
 * FileName:     UserServlet.java
 * @Description: 对外用户相关业务/outside user related business
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:16:14
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.bo.CreditScoreBo;
import com.pgt.bikelock.bo.DepositBo;
import com.pgt.bikelock.bo.EmailBo;
import com.pgt.bikelock.bo.NotificationBo;
import com.pgt.bikelock.bo.SmsBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.bo.MembershipPlanBO;
import com.pgt.bikelock.dao.IBankCardDao;
import com.pgt.bikelock.dao.ICashRecordDao;
import com.pgt.bikelock.dao.IUserCouponDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.IUserDetailDao;
import com.pgt.bikelock.dao.IUserInvaiteDao;
import com.pgt.bikelock.dao.impl.AppVersionDaoImpl;
import com.pgt.bikelock.dao.impl.BankCardDaoImpl;
import com.pgt.bikelock.dao.impl.BikeErrorDaoImpl;
import com.pgt.bikelock.dao.impl.BikeLongLeaseDaoImpl;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.dao.impl.CashRecordDaoImpl;
import com.pgt.bikelock.dao.impl.CountryDaoImpl;
import com.pgt.bikelock.dao.impl.CreditScoreDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.MessageBoxDaoImpl;
import com.pgt.bikelock.dao.impl.RedPackBikeDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserCouponDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.dao.impl.UserInvaiteDaoImpl;
import com.pgt.bikelock.dao.impl.WebContentDaoImpl;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.dao.impl.MembershipPlanDAO;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.SecurityUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.pay.AliPayUtil;
import com.pgt.bikelock.vo.AppVersionVo;
import com.pgt.bikelock.vo.BankCardVo;
import com.pgt.bikelock.vo.BikeErrorVo;
import com.pgt.bikelock.vo.BikeLongLeaseVo;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.CountryVo;
import com.pgt.bikelock.vo.CreditScoreVo;
import com.pgt.bikelock.vo.DepositConfVo;
import com.pgt.bikelock.vo.EmailVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserCouponVo;
import com.pgt.bikelock.vo.WebContentVo;
import com.pgt.bikelock.vo.UserCouponVo.GiftFromType;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.UserMembershipVO;
import com.pgt.bikelock.vo.admin.NotificationConfigVo.NotificationType;
import com.pgt.bikelock.vo.admin.RedPackBikeVo;

/**
 * 接口定义起点:20000
 * 访问前缀 app/user
 * @ClassName:     UserServlet
 * @Description:用信息相关接口/use information related protocol
 * @author:    Albert
 * @date:        2017-3-24 上午10:20:10
 *
 */
public class UserServlet extends BaseServlet {

	public static final int HTTP_RESULT_COUPON_IS_EXPIRE = 20001; // 优惠券已过期/coupon invalid
	public static final int HTTP_RESULT_COUPON_IS_BE_ACTIVE = 20002; // 优惠券已被激活/coupon activated
	public static final int HTTP_RESULT_READ_PACK_NOT_ENOUGHT = 20003; // 红包余额不足/red envelopes balance not enough
	public static final int HTTP_RESULT_USER_BE_STOP_USE_BIKE = 20004; // 用户被暂停使用单车/user can't use bike
	public static final int HTTP_RESULT_USER_BE_FROST = 20005; // 用户被冻结/user account is frozen
	public static final int HTTP_RESULT_BALANCE_NOT_ENOUGHT = 20006; // 余额不足/balance not enough
	public static final int HTTP_RESULT_EMAIL_IS_REGISTER = 20007; // 邮箱已注册/email was register

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		super.doGet(req, resp);
		switch (getRequestType(req)) {
		case 20001:
			getPercenterInfo(req, resp);
			break;
		case 20008:
			getUserInfo(req, resp);
			break;
		case 20009:
			getUserCouponList(req, resp);
			break;
		case 20013:
			getReadPackInfo(req, resp);
			break;
		case 20016:
			getTradeList(req, resp);
			break;
		case 20018:
			getCreditScoreList(req, resp);
			break;
		case 20019:
			getMoneyInfo(req, resp);
			break;
		case 20020:
			getRedPackList(req, resp);
			break;
		case 20021:
			getTopVersion(req, resp);
			break;
		case 20026:
			refreshUserToken(req, resp);
			break;
		case 20027:
			getUserMemberships(req, resp);
			break;

		default:
			break;
		}
		doPost(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		super.doPost(req, resp);
		switch (getRequestType(req)) {
		case 20002:
			updateUserInfo(req, resp);
			break;
		case 20003:
			updatePassword(req, resp);
			break;
		case 20004:
			authPhone(req, resp);
			break;
		case 20005:
			updateUserDetailInfo(req, resp);
			break;
		case 20006:
			bindBankCard(req, resp);
			break;
		case 20007:
			updateInviteCode(req, resp);
			break;
		case 20010:
			deleteBankCard(req, resp);
			break;
		case 20011:
			returnDeposit(req, resp);
			break;
		case 20012:
			activeCoupon(req, resp);
			break;
		case 20014:
			cashReadPack(req, resp);
			break;
		case 20015:
			queryCash(req, resp);
			break;
		case 20017:
			shareSuccess(req, resp);
			break;
		case 20022:
			resetPassword(req, resp);
			break;
		case 20023:
			updateUserPhone(req, resp);
			break;
		case 20024:
			addCoupon(req, resp);
			break;
		case 20025:
			refundBalance(req, resp);
			break;
		case 20026:
			subscribeUserMembership(req, resp);
			break;
		case 20027:
			cancelUserMembership(req, resp);
			break;
		case 20028:
			sendEmailValidate(req, resp);
			break;
		default:
			break;
		}
	}

	/**
	 * 20001
	 * @Title:        getUserInfo 
	 * @Description:  获取个人中心用户信息/obtain personal center user information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-25 下午5:42:10
	 */
	public void getPercenterInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req),true);
		if(userVo != null){
			JSONObject object = new JSONObject();
			object.put("userInfo", userVo);
			object.put("useInfo", new BikeUseDaoImpl().getTotalUseInfo(getUserId(req)));
			//会员信息/member information
			BikeLongLeaseVo leaseVo = new BikeLongLeaseDaoImpl().getUserLeaseInfo(getUserId(req));
			if(leaseVo != null){
				object.put("member", 1);
			}
			setData(resp, object);
		}else{
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}
	}

	/**
	 * 20002
	 * @Title:        updateUserInfo 
	 * @Description:  修改用户基础资料/modify user base information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-28 下午2:11:52
	 */
	public void updateUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		IUserDao userDao = new UserDaoImpl();
		UserVo userVo = userDao.getUserWithId(getUserId(req),true);
		if(userVo != null){
			String phone =  req.getParameter("phone");
			String nickName = req.getParameter("nickName");
			String headUrl = req.getParameter("headUrl");
			String inviteCode = req.getParameter("inviteCode");
			if(phone != null){
				userVo.setPhone(phone);
			}
			if(nickName != null){
				userVo.setNickName(nickName);
			}
			if(headUrl != null){
				userVo.setHeadUrl(headUrl);
			}
			if(inviteCode != null){
				userVo.setInvite_code(inviteCode);
			}


			boolean flag = userDao.updateUserInfo(userVo);

			setData(resp, flag?"1":"0");
		}else{
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}
	}

	/**
	 * 20003
	 * @Title:        updatePassword 
	 * @Description:  修改密码/modify password
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-28 下午2:12:11
	 */
	public void updatePassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"oldPassword","newPassword"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IUserDao userDao = new UserDaoImpl();
		UserVo userVo = userDao.getUserWithId(getUserId(req),req.getParameter(parms[0]),ValueUtil.getInt(getIndustryId(req)) );
		if(userVo != null){
			boolean flag = userDao.updatePassword(getUserId(req), req.getParameter(parms[1]));
			setData(resp, flag?"1":"0");
		}else{
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}
	}

	/**
	 * 20004
	 * post
	 * @Title:        authPhone 
	 * @Description:  手机认证（暂提供第三方注册使用，手机号注册已自动完成认证）/phone certified(provide third party register use, phone number register finish certification)
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月1日 下午7:15:57
	 */
	public void authPhone(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"phone","code"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		String phone = req.getParameter(parms[0]);
		String code = req.getParameter(parms[1]);

		UserVo user = new UserDaoImpl().getUser(phone,ValueUtil.getInt(getIndustryId(req)) );
		if(user != null && !user.getuId().equals(getUserId(req))){
			//手机号已被绑定/phone number bind
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;
		}

		//校验短信验证码/verify short message code
		if(!new SmsBo().checkSmsCode(code, phone)){
			setCode(resp, HTTP_RESULT_SMS_CODE_ERROR);
			return;
		}

		boolean flag = new UserDaoImpl().updateAuthStatus(getUserId(req),phone);
		setData(resp, flag?"1":"0");
	}


	/**
	 * 20005
	 * post
	 * @Title:        updateUserDetailInfo 
	 * @Description:  实名认证/修改用户详细信息/real name approve/modify user detail information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月1日 下午7:37:09
	 */
	public void updateUserDetailInfo(final HttpServletRequest req,HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"firstName","lastName","type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		final String userId = getUserId(req);
		//添加类型 1：认证 2：修改/add type 1:approve 2:modify
		int updateType = ValueUtil.getInt(req.getParameter("type"));
		IUserDao userDao = new UserDaoImpl();

		boolean flag = false;
		if(!StringUtils.isEmpty(req.getParameter("password"))){
			//密码设置/password setup
			flag = userDao.updatePassword(userId, req.getParameter("password"));
		}

		boolean changeCity = false;
		UserVo userVo  = userDao.getUserWithId(getUserId(req),true);;
		if(ValueUtil.getInt(req.getParameter("cityId")) > 0){
			if(userVo.getCityId() != ValueUtil.getInt(req.getParameter("cityId"))){
				changeCity = true;
			}
			userVo.setCityId(ValueUtil.getInt(req.getParameter("cityId")));
			if(req.getParameter("nickName") != null){
				userVo.setNickName(req.getParameter("nickName"));
			}
			userDao.updateUserInfo(userVo);
		}
		//详情信息/detail information
		IUserDetailDao detailDao = new UserDetailDaoImpl();
		final UserDetailVo userDetail = detailDao.getUserDetail(userId);
		final String email = req.getParameter("email");

		if(!StringUtils.isEmpty(email) && 
				(!email.equals(userDetail.getEmail()) || userDetail.getEmailAuth() == 0)){

			//邮箱认证邮件发送/send email auth email
			final WebContentVo content = new WebContentDaoImpl().getContent(getIndustryId(req), 12);
			if(content != null){
				UserDetailVo detailVo = new UserDetailDaoImpl().getUserDetailWithEmail(email);
				if(detailVo != null && !detailVo.getUid().equals(userId)){
					//邮箱已被注册/email was register by others
					setCode(resp, HTTP_RESULT_EMAIL_IS_REGISTER);
					return;
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						//create link token
						JSONObject tokenJson = new JSONObject();
						tokenJson.put("userId", userId);
						tokenJson.put("email", email);
						//过期时间生成/expired time generate
						SimpleDateFormat sf = new SimpleDateFormat(UserBo.DataFormate);
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DAY_OF_MONTH, 3);
						tokenJson.put("tokenExpiredDate", sf.format(c.getTime()));
						tokenJson.put("timeStamp",TimeUtil.getCurrentLongTimeStr());

						String mapStr = tokenJson.toString();
						System.out.println(mapStr);
						//加密信息/encryption information
						String token =SecurityUtil.encrypt(mapStr, SecurityUtil.DoubKey);

						content.setContent(content.getContent().replace("linkToken", token));

						EmailVo emailVo = new EmailVo(email, email, "honeybees E-mail Verification", content.getContent());
						NotificationBo.addNotifiyMessage(new EmailBo().sendSystemEmail(emailVo), "0", userId, "honeybees Email Verification",
								"A verification has been sent to your associated email. Please check it.");
						userDetail.setEmailAuth(0);
					}	
					
				}).start();

			}




		}
		userDetail.setUserDetailVo(req);
		flag = detailDao.updateUserDetail(userDetail);

		if(updateType == 1){
			//更新实名认证进度/update real time certified schedule
			flag = userDao.updateAuthStatusCheckFront(userId,2);
			//返回产业对应的押金额度/return industry correspond deposit amount
			JSONObject object = new JSONObject();
			object.put("result", flag?"1":"0");
			if(flag){
				object.put("deposit", new DepositBo().getDepositAmount(getCityId(req)));
			}
			if(changeCity == true){
				object.put("token",UserBo.createUserToken(userVo,true));
			}
			setData(resp, object);
		}else if(changeCity == true){
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", JSON.toJSON(flag?"1":"0"));
			dataMap.put("token",UserBo.createUserToken(userVo,true));
			dataMap.put("code", HTTP_RESULT_OK);
			setMapWithCodeOk(resp, dataMap);
		}else{
			if(userVo.getAuthStatus() == 1){
				//更新实名认证进度/update real time certified schedule
				flag = userDao.updateAuthStatusCheckFront(userId,2);
			}
			setData(resp, flag?"1":"0");
		}


	}

	/**
	 * 20006
	 * post
	 * @Title:        bindBankCard 
	 * @Description:  绑定银行卡认证/bind bank card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月5日 上午9:47:11
	 */
	public void bindBankCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"card_number","exp_date","cvv","name_on_card","type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		//添加类型 1：绑定 2：添加/add type 1:bind 2:add
		int bindType = ValueUtil.getInt(req.getParameter(parms[4]));

		IBankCardDao cardDao = new BankCardDaoImpl();
		//判定卡信息是否存在/judge card information whether exist
		BankCardVo cardVo = cardDao.getCardInfo(req.getParameter(parms[0]),getUserId(req));
		if(cardVo != null){
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;
		}

		cardVo = new BankCardVo(req, parms, getUserId(req));
		boolean flag = cardDao.addCard(cardVo);
		if(bindType == 1){
			//更新银行卡绑定进度/update bank card bind schedule
			flag = new UserDaoImpl().updateAuthStatusCheckFront(getUserId(req),4);
		}
		setData(resp, flag?"1":"0");
	}

	/**
	 * 20007
	 * post
	 * @Title:        updateInviteCode 
	 * @Description:  更新邀请码/update user friend invite code
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月5日 上午10:08:20
	 */
	public void updateInviteCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"code"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		String code = req.getParameter(parms[0]);
		String invaiteUserId = new UserDaoImpl().getUserId(code);
		if(invaiteUserId == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		IUserInvaiteDao invaiteDao = new UserInvaiteDaoImpl();
		if(invaiteDao.shipExist(getUserId(req), invaiteUserId)){//判定关系是否已存在/judge relationship whether exist
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;
		}
		//增加请码关系/add invite code relation
		boolean flag = invaiteDao.addShip(getUserId(req), invaiteUserId);
		if(flag){
			//为双方增加积分/add score for both party
			CreditScoreBo.addScoreRecord(getUserId(req), 1, 2);
			CreditScoreBo.addScoreRecord(invaiteUserId, 1, 3);

			//添加邀请好友优惠券/add invited friend coupon
			if(OthersSource.getSourceString("invite_friend_register_coupon_num") != null){
				CouponBo.addSystemCouponForUser(invaiteUserId, 
						ValueUtil.getInt(OthersSource.getSourceString("invite_friend_register_coupon_num")),GiftFromType.Invite);
				CouponBo.addSystemCouponForUser(getUserId(req), 
						ValueUtil.getInt(OthersSource.getSourceString("invite_friend_register_coupon_num")),GiftFromType.Invite);
			}


		}
		setData(resp, flag?"1":"0");
	}

	/**
	 * 20008
	 * @Title:        getUserInfo 
	 * @Description:  获取用户详细信息/obtain user detail information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月5日 下午3:14:41
	 */
	public void getUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserDetailVo userVo = new UserDaoImpl().getUserDetailInfo(getUserId(req));
		if(userVo != null){
			JSONObject object = new JSONObject();
			object.put("userInfo", userVo);
			object.put("cardList", new BankCardDaoImpl().getCardList(getUserId(req)));
			setData(resp, object);
			setData(resp, userVo);
		}else{
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}
	}

	/**
	 * 20009
	 * @Title:        getUserCouponList 
	 * @Description:  获取用户优惠券列表/get user coupon list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月6日 下午4:22:29
	 */
	public void getUserCouponList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<UserCouponVo> list = new UserCouponDaoImpl().getUserCouponList(getUserId(req),
				getStartPage(req),ValueUtil.getInt(req.getParameter("showAll")));
		setData(resp, list);
	}

	/**
	 * 20010
	 * @Title:        deleteBankCard 
	 * @Description:  删除银行卡/delete bank card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月15日 上午11:52:31
	 */
	public void deleteBankCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"cardId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new BankCardDaoImpl().deleteCard(req.getParameter(parms[0]));
		setFlagData(resp, flag);
	}

	/**
	 * 20011
	 * @Title:        returnDeposit 
	 * @Description:  退还押金/refund deposit
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月15日 下午4:15:16
	 */
	public void returnDeposit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {


		TradeVo tradeVo = new TradeDaoImpl().getNoPayTrade(getUserId(req));
		if(tradeVo != null){
			//用户有未支付的订单/user have unpaid order
			setCode(resp, BikeServlet.HTTP_RESULT_USER_HAVE_NO_PAY_ORDER_ERROR);
			return;
		}

		//获取当前使用信息/gain current use information
		BikeUseVo currentBikeUseVo = new BikeUseDaoImpl().getUseIngInfoWithUser(getUserId(req));
		if(currentBikeUseVo != null && ValueUtil.getInt(currentBikeUseVo.getStartTime()) > 0){
			//有正在使用的单车/have using bike
			setCode(resp, BikeServlet.HTTP_RESULT_BIKE_IS_USEING_ERROR);
			return;
		}

		int status = new DepositBo().returnDeposit(getUserId(req),req.getParameter("cardNumber"),req.getParameter("expireDate"));
		JSONObject dataMap= new JSONObject();
		dataMap.put("data", JSON.toJSON(status+""));
		dataMap.put("code", HTTP_RESULT_OK);
		if(status == 1){
			DepositConfVo depositConfig = new DepositBo().getDepositConfig(getCityId(req));
			dataMap.put("dealDay",depositConfig.getReturn_min_day()+"-"+depositConfig.getReturn_max_day());
			//send notification
			new NotificationBo().sendNotifyToAdmin(NotificationType.User_Refund_Deposit, getUserId(req), null, 0);
		}
		setMapWithCodeOk(resp, dataMap);
	}

	/**
	 * 20012
	 * @Title:        activeCoupon 
	 * @Description:  激活优惠券/activate coupon
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月29日 上午10:41:06
	 */
	public void activeCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"couponCode","couponId"};
		String couponCode = req.getParameter(parms[0]);
		String couponId = req.getParameter(parms[1]);
		if(StringUtils.isEmpty(couponCode) && StringUtils.isEmpty(couponId)){
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return; 
		}
		IUserCouponDao couponDao = new UserCouponDaoImpl();
		UserCouponVo userCouponVo = null;
		if(!StringUtils.isEmpty(couponCode)){
			if(couponDao.checkCouponHaveBeenGet(couponCode, getUserId(req))){
				setCode(resp, HTTP_RESULT_COUPON_IS_BE_ACTIVE);
				return;
			}
			userCouponVo = couponDao.getNotActiveCouponWithCode(couponCode);
		}else if(!StringUtils.isEmpty(couponId)){
			userCouponVo = couponDao.getNotActiveCoupon(couponId);
		}
		if(userCouponVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		if(ValueUtil.getLong(userCouponVo.getCouponVo().getEnd_time())  < TimeUtil.getCurrentLongTime()){
			setCode(resp, HTTP_RESULT_COUPON_IS_EXPIRE);
			return;
		}
		if(userCouponVo.getActive_date() != null){
			setCode(resp, HTTP_RESULT_COUPON_IS_BE_ACTIVE);
			return;
		}
		boolean flag = couponDao.activeCoupon(getUserId(req), userCouponVo.getId(),true);

		if(flag && (userCouponVo.getCouponVo().getType() == 2 || userCouponVo.getCouponVo().getType() == 3) 
				&& ValueUtil.getInt(OthersSource.getSourceString("recharge_coupon_to_amount")) == 1){
			//将优惠券的时间充值到余额/coupon time recharge to amount of money
			IUserDao userDao = new UserDaoImpl();
			UserVo userVo =  userDao.getUserWithId(getUserId(req),true);
			userVo.setGiftMoney(userVo.getGiftMoney().add(userCouponVo.getAmount()));
			flag = userDao.updateGiftMoney(userVo.getGiftMoney(),getUserId(req));
			if(flag){
				//标记优惠券已使用，防止结算时自动抵扣/sign coupon already used, prevent caculate auto reduce
				couponDao.useCoupon(userCouponVo.getId());
				//
				UserBo.addRechargeOrder(getUserId(req), TradeVo.Trade_PayWay_Coupon, userCouponVo.getAmount(), userCouponVo.getId());
			}

		}
		setFlagData(resp, flag);
		if(flag){
			//通知/notice
			CouponBo.couponNotify(2, userCouponVo.getCid(), getUserId(req),null);
		}
	}

	/**
	 * 20013
	 * @Title:        getReadPackInfo 
	 * @Description:  获取红包信息/gain red envelope information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月11日 上午11:27:02
	 */
	public void getReadPackInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BigDecimal amount = new UserDaoImpl().getRedPackAmount(getUserId(req));
		JSONObject object = new JSONObject();
		object.put("amount", amount);
		object.put("minAmount", 10);
		object.put("cashCount", 1);
		setData(resp, object);
	}

	/**
	 * 20014
	 * post
	 * @Title:        cashReadPack 
	 * @Description:  红包提现/red envelopes in cash
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月11日 上午11:20:19
	 */
	public void cashReadPack(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"amount","type","account"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BigDecimal cashAmount = new BigDecimal(req.getParameter(parms[0]));
		BigDecimal amount = new UserDaoImpl().getRedPackAmount(getUserId(req));
		if(amount.compareTo(cashAmount) == -1){
			setCode(resp, HTTP_RESULT_READ_PACK_NOT_ENOUGHT);
			return;
		}
		ICashRecordDao cashDao = new CashRecordDaoImpl();
		String cashId = cashDao.addCash(new CashRecordVo( getUserId(req),cashAmount, ValueUtil.getInt(req.getParameter(parms[1])),1));

		String orderId = AliPayUtil.transfer(cashId, req.getParameter(parms[2]), cashAmount, "红包提现");
		if(StringUtils.isEmpty(orderId)){
			setCode(resp, ERROR_EXCEPTION);
		}else{			
			JSONObject object = new JSONObject();
			object.put("cashId", cashId);
			object.put("orderId", orderId);
			setData(resp, object);
			//update cash orderid
			cashDao.updateCashOrderId(orderId, cashId);
		}
	}

	/**
	 * 20015
	 * post
	 * @Title:        queryCash 
	 * @Description:  提现进度查询/in cash schedule check
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月11日 下午3:58:18
	 */
	public void queryCash(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"cashId","orderId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		AliPayUtil.transferQuery(req.getParameter(parms[0]), req.getParameter(parms[1]),getUserId(req) ,resp);
	}

	/**
	 * 20016
	 * @Title:        getTradeList 
	 * @Description:  获取所有订单集合/obtain all order focus
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月5日 下午4:31:50
	 */
	protected void getTradeList(HttpServletRequest req, HttpServletResponse resp){
		int tradeType = ValueUtil.getInt(req.getParameter("tradeType"));
		List<TradeVo>  list = new TradeDaoImpl().getTradeList(getUserId(req),getStartPage(req),tradeType);
		setData(resp, list);
	}

	/**
	 * 20017
	 * @Title:        shareSuccess 
	 * @Description:  分享成功/share success
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月15日 下午3:08:24
	 */
	public void shareSuccess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean result = CreditScoreBo.addScoreRecord(getUserId(req), 1, 1);
		setCode(resp, result?HTTP_RESULT_OK:ERROR_EXCEPTION);
	}

	/**
	 * 20018
	 * @Title:        getCreditScoreList 
	 * @Description:  获取信用积分列表/gain credit score list
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月15日 下午5:56:03
	 */
	protected void getCreditScoreList(HttpServletRequest req, HttpServletResponse resp){
		List<CreditScoreVo>  list = new CreditScoreDaoImpl().getUserCreditList(getUserId(req),getStartPage(req));
		setData(resp, list);
	}

	/**
	 * 20019
	 * @Title:        getMoneyInfo 
	 * @Description:  用户钱包信息/user red envelope information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:03:25
	 */
	public void getMoneyInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		IndustryVo industryVo = new IndustryDaoImpl().getIndustryInfo(getIndustryId(req));

		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req),true);
		if(userVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		JSONObject object = new JSONObject();
		object.put("amount", userVo.getMoney().add(userVo.getGiftMoney()));
		TradeVo tradeVo = new TradeDaoImpl().getFinalDepositTradeInfo(getUserId(req));
		//押金/deposit
		if(userVo.getAuthStatus() != industryVo.getRegister_auth_num() || tradeVo == null){
			object.put("deposit", 0);
		}else{

			object.put("deposit", tradeVo.getAmount());
		}
		object.put("authStatus", userVo.getAuthStatus());
		//会员信息/memeber information
		BikeLongLeaseVo leaseVo = new BikeLongLeaseDaoImpl().getUserLeaseInfo(getUserId(req));
		if(leaseVo != null){
			object.put("member", leaseVo);
		}

		//优惠券信息/Coupon info
		UserCouponVo couponVo = new UserCouponDaoImpl().getUserPercentCoupon(getUserId(req));
		if(couponVo != null){
			object.put("couponVo", couponVo);
		}

		//余额提现记录
		CashRecordVo cashVo = new CashRecordDaoImpl().getUserBalanceCash(getUserId(req));
		if(cashVo != null){
			object.put("cashVo", cashVo);
		}

		setData(resp, object);
	}

	/**
	 * 20020
	 * @Title:        getRedPackList 
	 * @Description:  用户红包列表获取/user red envelopes list obtain
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月18日 下午3:09:46
	 */
	protected void getRedPackList(HttpServletRequest req, HttpServletResponse resp){
		List<RedPackBikeVo>  list = new RedPackBikeDaoImpl().getUserRedPackList(getUserId(req),getStartPage(req));
		setData(resp, list);
	}

	/**
	 * 20021
	 * @Title:        getTopVersion 
	 * @Description:  获取最新版/obtain final version
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月27日 下午4:20:35
	 */
	public void getTopVersion(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"versionCode"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int versionCode = ValueUtil.getInt(req.getParameter(parms[0]));
		if(versionCode > 0){
			AppVersionVo topVersion = new AppVersionDaoImpl().getTopVersion(versionCode);
			if(topVersion  != null){
				setData(resp, JSON.toJSON(topVersion));
			}else{
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			}
		}else{
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
		}
	}


	public void getUserMemberships (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		String userId = (String)req.getAttribute("userId");
		if(StringUtils.isEmpty(userId)) {
			writeJSONCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}


		List<UserMembershipVO> list = new MembershipPlanDAO().getUserMembershipsForUser(userId);
		writeJSON(res, list);

	}

	public void cancelUserMembership (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		String userId = (String)req.getAttribute("userId");
		if (StringUtils.isEmpty(userId)) {
			writeJSONCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		String userPlanId = (String)req.getParameter("userMembershipId");
		if (StringUtils.isEmpty(userPlanId)) {
			writeJSONCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}


		boolean success = new MembershipPlanBO().cancelUserMembershipPlan(userId, userPlanId);

		writeJSON(res, success? "1": "0");

	}


	public void subscribeUserMembership (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		String userId = (String)req.getAttribute("userId");
		if (StringUtils.isEmpty(userId)) {
			writeJSONCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		String planId = req.getParameter("membershipPlanId");
		if (StringUtils.isEmpty(planId)) {
			writeJSONCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		String industryId = (String)req.getAttribute("industryId");
		if (StringUtils.isEmpty(industryId)) {
			writeJSONCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		String autoRenew = req.getParameter("autoRenew");
		if (StringUtils.isEmpty(autoRenew)) {
			autoRenew = "1";
		}

		IndustryVo industryVo = new IndustryDaoImpl().getIndustryInfo(industryId);

		boolean success = new MembershipPlanBO().addUserToMembershipPlan(
				userId,
				planId,
				industryVo.getCurrency(),
				ValueUtil.getInt(autoRenew) != 0
				);

		writeJSON(res, success? "1": "0");

	}


	/**
	 * 20022
	 * @Title:        resetPassword 
	 * @Description:  重置密码/reset password
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月8日 下午8:32:01
	 */
	public void resetPassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"password"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		boolean flag = new UserDaoImpl().updatePassword(getUserId(req), req.getParameter(parms[0]));
		setData(resp, flag?"1":"0");

	}

	/**
	 * 20023
	 * @Title:        updateUserPhone 
	 * @Description:  修改手机号/modify phone number
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月7日 下午6:08:33
	 */
	public void updateUserPhone(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"phone","code"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		String phone = req.getParameter(parms[0]);
		String code = req.getParameter(parms[1]);
		UserVo user;
		if(!StringUtils.isEmpty(req.getParameter("password"))){
			user = new UserDaoImpl().getUserWithId(getUserId(req),req.getParameter("password"),ValueUtil.getInt(getIndustryId(req)) );
			if(user == null){
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return;
			}
		}else{
			user = new UserDaoImpl().getUser(phone,ValueUtil.getInt(getIndustryId(req)) );
		}

		if(user != null && !user.getuId().equals(getUserId(req))){
			//手机号已被绑定/phone number binded
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;
		}

		//校验短信验证码/verify short message code
		if(!new SmsBo().checkSmsCode(code, phone)){
			setCode(resp, HTTP_RESULT_SMS_CODE_ERROR);
			return;
		}
		int phoneCode = ValueUtil.getInt(req.getParameter("phoneCode"));
		int countryId = 0;
		if(phoneCode > 0){
			CountryVo countryVo = new CountryDaoImpl().getCountry(phoneCode);
			if(countryVo != null){
				countryId =countryVo.getId();
			}

		}
		boolean flag = new UserDaoImpl().updatePhone(getUserId(req),phone,countryId);
		setData(resp, flag?"1":"0");
	}

	/**
	 * 20024
	 * @Title:        addCoupon 
	 * @Description:  通过代码添加优惠券（不激活）/through code add coupon(unactivate)
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月11日 上午9:48:55
	 */
	public void addCoupon(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"couponCode"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String code = req.getParameter(parms[0]);
		IUserCouponDao couponDao = new UserCouponDaoImpl();

		if(couponDao.checkCouponHaveBeenGet(code, getUserId(req))){
			setCode(resp, HTTP_RESULT_COUPON_IS_BE_ACTIVE);
			return;
		}

		UserCouponVo userCouponVo = couponDao.getNotActiveCouponWithCode(code);
		if(userCouponVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		if(ValueUtil.getLong(userCouponVo.getCouponVo().getEnd_time())  < TimeUtil.getCurrentLongTime()){
			setCode(resp, HTTP_RESULT_COUPON_IS_EXPIRE);
			return;
		}
		if(userCouponVo.getUid() != null){
			setCode(resp, HTTP_RESULT_COUPON_IS_BE_ACTIVE);
			return;
		}
		boolean flag = new UserCouponDaoImpl().activeCoupon(getUserId(req), userCouponVo.getId(),false);
		setFlagData(resp, flag);
		if(flag){
			CouponBo.couponNotify(1, userCouponVo.getId(), getUserId(req), userCouponVo.getCouponVo().getName());
		}

	}

	/**
	 * 20025
	 * @Title:        refundBalance 
	 * @Description:  余额退款/Refund Balance
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月3日 下午5:07:58
	 */
	public void refundBalance(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"amount"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		ICashRecordDao cashDao = new CashRecordDaoImpl();
		CashRecordVo cashVo = cashDao.getUserBalanceCash(getUserId(req));
		if(cashVo != null && cashVo.getStatus() == 0){
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;
		}

		BigDecimal cashAmount = new BigDecimal(req.getParameter(parms[0]));
		BigDecimal amount = new UserDaoImpl().getMoneyAmount(getUserId(req));
		if(amount.compareTo(cashAmount) == -1){
			setCode(resp, HTTP_RESULT_BALANCE_NOT_ENOUGHT);
			return;
		}

		String cashId = cashDao.addCash(new CashRecordVo( getUserId(req),cashAmount, 0,2));
		boolean flag = cashId != null?true:false;
		if(flag){
			//send notification
			new NotificationBo().sendNotifyToAdmin(NotificationType.User_Refund_Balance, getUserId(req), null, 0);
		}
		setFlagData(resp, flag);
	}

	/**
	 * 20026
	 * @Title:        refreshUserToken 
	 * @Description:  刷新用户token
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年2月25日 下午5:37:15
	 */
	public void refreshUserToken(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req), false);
		if(userVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		String oldToken = req.getParameter("token");
		ParamsFilter.removeToken(oldToken);
		String newToken = UserBo.createUserToken(userVo,true);
		setData(resp, newToken);
	}

	/**
	 * 20028
	 * @Title:        sendEmailValidate 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年4月13日 下午5:53:25
	 */
	protected void sendEmailValidate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"email"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		WebContentVo content = new WebContentDaoImpl().getContent(getIndustryId(req), 12);
		if(content == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		String userId = getUserId(req);
		String email = req.getParameter(parms[0]);

		UserDetailVo detailVo = new UserDetailDaoImpl().getUserDetailWithEmail(email);
		if(detailVo != null && !detailVo.getUid().equals(userId)){
			//邮箱已被注册/email was register by others
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;
		}else if(detailVo != null && detailVo.getUid().equals(userId) 
				&& email.equals(detailVo.getEmail()) && detailVo.getEmailAuth() > 0){
			//邮箱已验证
			setData(resp, 2);
			return;
		}

		//create link token
		JSONObject tokenJson = new JSONObject();
		tokenJson.put("userId", userId);
		tokenJson.put("email", email);
		//过期时间生成/expired time generate
		SimpleDateFormat sf = new SimpleDateFormat(UserBo.DataFormate);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 3);
		tokenJson.put("tokenExpiredDate", sf.format(c.getTime()));
		tokenJson.put("timeStamp",TimeUtil.getCurrentLongTimeStr());

		String mapStr = tokenJson.toString();
		System.out.println(mapStr);
		//加密信息/encryption information
		String token =SecurityUtil.encrypt(mapStr, SecurityUtil.DoubKey);

		content.setContent(content.getContent().replace("linkToken", token));

		String reciver = req.getParameter(parms[0]);
		EmailVo emailVo = new EmailVo(reciver, reciver, "honeybees E-mail Verification", content.getContent());
		setFlagData(resp, new EmailBo().sendSystemEmail(emailVo));
	}
}
