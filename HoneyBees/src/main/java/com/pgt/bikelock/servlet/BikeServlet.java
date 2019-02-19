/**
 * FileName:     BikeServlet.java
 * @Description: 对外单车锁相关业务/related business about bike lock
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:15:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 * 2017-4-21       CQCN         1.0.1             1.0.1
 * Why & What is modified:<解决未开锁成功返回开始计费问题>/solve not unlock success return and begin to caculate problem
 * 2017-4-25       CQCN         1.0.1.1             1.0.1.1
 * Why & What is modified:<故障报告类型多选addBikeErrorForDamaged设置errorType为数组>/error report type multi choose addbikeerrorfor damaged set error type as number group
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;







import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.aliyun.oss.model.SetObjectAclRequest;
import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.bo.LockOrderBo;
import com.pgt.bikelock.bo.NotificationBo;
import com.pgt.bikelock.bo.SystemConfigBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.bo.HoldBO;
import com.pgt.bikelock.dao.IBikeErrorDao;
import com.pgt.bikelock.dao.IBikeReserveDao;
import com.pgt.bikelock.dao.IBikeUseDao;
import com.pgt.bikelock.dao.IMessageBoxDao;
import com.pgt.bikelock.dao.impl.AppVersionDaoImpl;
import com.pgt.bikelock.dao.impl.AreaDaoImpl;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeErrorDaoImpl;
import com.pgt.bikelock.dao.impl.BikeLongLeaseDaoImpl;
import com.pgt.bikelock.dao.impl.BikeOrderRecordDaoImpl;
import com.pgt.bikelock.dao.impl.BikeReserveDaoImpl;
import com.pgt.bikelock.dao.impl.BikeTypeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.dao.impl.BleBikeDaoImpl;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.dao.impl.ImageDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.MessageBoxDaoImpl;
import com.pgt.bikelock.dao.impl.NewsDaoImpl;
import com.pgt.bikelock.dao.impl.RedPackRuleDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.listener.MyTcpCallBack;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.AppVersionVo;
import com.pgt.bikelock.vo.AreaVo;
import com.pgt.bikelock.vo.BikeErrorVo;
import com.pgt.bikelock.vo.BikeLongLeaseVo;
import com.pgt.bikelock.vo.BikeReserveVo;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.BleBikeVo;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.NewsVo;
import com.pgt.bikelock.vo.TradeReceiptVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.HoldVO;
import com.pgt.bikelock.vo.UserMembershipVO;
import com.pgt.bikelock.dao.impl.MembershipPlanDAO;
import com.pgt.bikelock.vo.admin.BikeOrderRecord;
import com.pgt.bikelock.vo.admin.RedPackRuleVo;
import com.pgt.bikelock.vo.admin.NotificationConfigVo.NotificationType;



/**
 *  接口定义起点:30000/interface definition startpoint:3000
 *  访问前缀 app/bike /access prefix
 * @ClassName:     BikeServlet
 * @Description:单车相关业务接口/bike related business interface
 * @author:    Albert
 * @date:        2017-3-24 上午10:19:49
 *
 */
public class BikeServlet extends BaseServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final int HTTP_RESULT_BIKE_IS_USEING_ERROR = 30001; // 单车正在使用中/bike is using
	public static final int HTTP_RESULT_BIKE_IS_ERROR = 30002; // 单车已损坏/bike broken
	public static final int HTTP_RESULT_BIKE_CANNOT_USE_ERROR = 30003; // 单车已报废/bike scrapped
	public static final int HTTP_RESULT_BIKE_IS_RESERVE_ERROR = 30004; // 单车已被预约/bike booked
	public static final int HTTP_RESULT_BIKE_UNLOCKING_SUCCESS = 30005; // 单车解锁成功/bike unlock success
	public static final int HTTP_RESULT_HAD_RESERVE_BIKE_ERROR = 30006; // 已有预约的单车/already booked bike
	public static final int HTTP_RESULT_USER_NOT_AUTH_ERROR = 30007; // 用户未通过认证/user are not get certification
	public static final int HTTP_RESULT_USER_NOT_ENOUGH_ERROR = 30008; // 用户余额不足/user balance not enough
	public static final int HTTP_RESULT_USER_HAVE_NO_PAY_ORDER_ERROR = 30009; // 用户有未支付的订单/user have unpaid order
	public static final int HTTP_RESULT_BIKE_UNLOCKING = 30010; // 单车正在解锁/bike is unlocking
	public static final int HTTP_RESULT_BIKE_IS_USEING_OTHERS = 30011; // 已在使用其他单车/already using other bike
	public static final int HTTP_RESULT_BIKEUSE_IS_OUT_AREA = 30012; // 骑行越界/riding transboundary
	public static final int HTTP_RESULT_USER_NOT_BIND_CARD = 30013; // 未绑定银行卡/bind bank card
	public static final int HTTP_RESULT_BIKE_FINISH_USE = 30014; // 骑行已结束/finish ride
	public static final int HTTP_RESULT_BIKEUSE_IS_IN_FORCED_PARKING_AREA = 30015; // 禁停区/in Prohibited parking
	public static final int HTTP_RESULT_BIKE_IS_RESERVE_NOT_COUNT = 30016; // 取消预约次数已上限，不能预约/Cancel reserve count is max
	public static final int HTTP_RESULT_BIKE_IS_RIDE_NOT_COUNT = 30017; // 骑行次数已上限/ride count is max 
	public static final int HTTP_RESULT_BIKE_IS_DISCONNECT = 30018; // 单车未连接/bike is disconnect 
	public static final int HTTP_RESULT_BIKE_IS_NOT_ACTIVE = 30019; // bike is not active 

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		super.doGet(req, resp);


		switch (getRequestType(req)) {
		case 30001:
			getNearBike(req, resp);
			break;
		case 30003:
			getNearBikesInUserCity(req, resp);
			break;
		case 30004:
			getStartUseBikeProcess(req, resp);
		case 30006:
			try {
				getCurrentUseInfo(req, resp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 30007:
			getBikeTypeList(req, resp);
			break;
		case 30008:
			getBikeUseList(req, resp);
			break;
		case 30014:
			getBikeAreaList(req, resp);
			break;
		case 30015:
			getBikeUseTrade(req, resp);
			break;
		case 30024:
			getBikePriceInfo(req, resp);
			break;
		case 30021:
			getErrorReportList(req, resp);
			break;
		case 30022:
			getErrorReportDetail(req, resp);
			break;
		case 30023:
			getBikeRideHistoryForUser(req, resp);
			break;
		case 30026:
			lastBike(req, resp);
			break;
		case 30028:
			usageAndHold(req, resp);
			break;

		default:
			break;
		}

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
		case 30002:
			reserveBike(req, resp);
			break;
		case 30003:
			cancelReserveBike(req, resp);
			break;
		case 30004:
			startUseBike(req, resp);
			break;
		case 30005:
			updateUseBikeProcess(req, resp);
			break;
		case 30009:
			addTradeReceipt(req, resp);
			break;
		case 30010:
			updateNotifySuccess(req, resp);
			break;
		case 30011:
			addBikeErrorForCanNotOpenBike(req, resp);
			break;
		case 30012:
			addBikeErrorForDamaged(req, resp);
			break;
		case 30013:
			addBikeErrorForViolations(req, resp);
			break;
		case 30016:
			addBikeError(req, resp);
			break;
		case 30017:
			startBikeUse(req, resp);
			break;
		case 30018:
			finishBikeUse(req, resp);
			break;
		case 30019:
			fundBike(req, resp);
			break;
		case 30024:
			openBikeFail(req, resp);
			break;
		case 30025:
			holdBike(req, resp);
			break;
		case 30027:
			cancelBikeHold(req, resp);
			break;
		case 30029:
			updateUserRideProcess(req, resp);
			break;
		case 30030:
			lockRide(req, resp);
			break;
		case 30031:
			updateRideInfo(req, resp);
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: checkUserCanUseBike
	 * @Description: 检查用户是否能使用单车/check user whether can use bike
	 * @param: @param req
	 * @param: @param resp
	 * @param: @param bikeVo
	 * @param: @return
	 * @return: boolean
	 * @author Albert
	 * @Date 2017年4月8日 下午6:31:11
	 */



	public boolean checkUserCanUseBike(HttpServletRequest req, HttpServletResponse resp, BikeVo bikeVo) {
		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req), true);
		if (userVo == null) {
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return false;
		}
		//获取认证步骤数/obtain certification step number
		IndustryVo industryVo = new IndustryDaoImpl().getIndustryInfo(getIndustryId(req));
		if (industryVo == null) {
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return false;
		}
		if (userVo.getAuthStatus() == 7) {
			//stop use bike
			setCode(resp, UserServlet.HTTP_RESULT_USER_BE_STOP_USE_BIKE);
			return false;
		}
		if (userVo.getAuthStatus() != industryVo.getRegister_auth_num()) {
			//未通过认证/not pass certification
			setCode(resp, HTTP_RESULT_USER_NOT_AUTH_ERROR);
			return false;
		}

		if ("1".equals(OthersSource.getSourceString("bike_use_must_bind_card"))) {
			//判定是否绑定银行卡信息/judge whether bind bank card information
			boolean bindCard = UserBo.checkUserBindCard(userVo.getuId());
			if (!bindCard) {
				setCode(resp, HTTP_RESULT_USER_NOT_BIND_CARD);
				return false;
			}
		}


		BikeTypeVo typeVo = new BikeTypeDaoImpl().getTypeInfo(bikeVo.getTypeId());
		if (typeVo == null) {
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return false;
		}

		//注册优惠券校验/register coupon verfify
		if (CouponBo.haveFreeCoupon(getUserId(req), typeVo.getPrice(), userVo.getCityId())) {
			return true;
		}

		//长租（会员）/long time rent(member)
		BikeLongLeaseVo leaseVo = new BikeLongLeaseDaoImpl().getUserLeaseInfo(getUserId(req));
		if (leaseVo != null) {
			return true;
		}


		/*if(CouponBo.haveTimeFreeCoupon(userVo.getCityId()) != null){
			return true;
		}*/

		if (CouponBo.haveTimeFreeCoupon(bikeVo.getCityId()) != null) {
			return true;
		}

		// check the membership status, otherwise people who bought a membership with 0 balance couldn't ride the bike
		//        if  (currentBikeUseVo != null) {
		if (checkValidMembershipPlan(userVo)) {
			return true;
		}
		//        }


		if (ValueUtil.getInt(getIndustryId(req)) != 5) {
			int r = userVo.getMoney().add(userVo.getGiftMoney()).compareTo(typeVo.getPrice()); //对比用户余额是否足够/compare user balance amount is enough
			if (r == -1) {
				//用户余额不足/user balance is not enough
				setCode(resp, HTTP_RESULT_USER_NOT_ENOUGH_ERROR);
				return false;
			}
		}


		TradeVo tradeVo = new TradeDaoImpl().getNoPayTrade(getUserId(req));
		if (tradeVo != null) {
			//用户有未支付的订单/user have unpaid order
			setCode(resp, HTTP_RESULT_USER_HAVE_NO_PAY_ORDER_ERROR);
			return false;
		}

		return true;
	}


	/**
	 * @return boolean
	 * @Title: checkValidMembershipPlan
	 * @Description: Check if there is valid membership plan
	 * @author: K
	 */

	public boolean checkValidMembershipPlan(UserVo userVo) {
		UserMembershipVO userPlan = new MembershipPlanDAO().getActiveUserMembershipsForUser(userVo);

		if (userPlan == null) {
			return false;
		}

		if (!userPlan.getCanceled()) {
			return true;
		}

		if (TimeUtil.getCurrentLongTime() < userPlan.getThroughTime().getTime() / 1000) {
			return true;
		}
		return false;
	}





	/**
	 * 
	 * @Title:        checkBikeExistWithNumber 
	 * @Description:  检查单车是否存在/check bike is exist
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param number
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月11日 下午2:37:01
	 */
	public String checkBikeExistWithNumber(HttpServletRequest req, HttpServletResponse resp,String number){
		BikeVo bikeVo = new BikeDaoImpl().getBikeInfoWithNumber(number);
		if(bikeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return null;
		}
		return bikeVo.getBid();
	}

	/**
	 * 30001
	 * get
	 * @Title:        getNearBike 
	 * @Description:  获取附近单车/gain nearby bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-25 下午6:21:33
	 */
	protected void getNearBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req),true);
		if(userVo != null && userVo.getAuthStatus() == 7){
			//被关闭使用单车功能/closed use bike function
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		String[] parms = new String[]{"lat","lng"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		double currentLat = ValueUtil.getDouble(req.getParameter("cur_lat"));
		double currentLng = ValueUtil.getDouble(req.getParameter("cur_lng"));
		System.out.println("currentLat1:"+currentLat+";currentLng"+currentLng);
		//获取当前经纬度信息，用于后续的过滤/obtain current longitude and latitude, use for subsequeent filtering
		BikeVo bikeVo = new BikeVo();
		//		if(currentLat > 0 || currentLng > 0){
		//			bikeVo.setgLat(currentLat);
		//			bikeVo.setgLng(currentLng);
		//		}else{
		bikeVo.setgLat(ValueUtil.getDouble(req.getParameter(parms[0])));
		bikeVo.setgLng(ValueUtil.getDouble(req.getParameter(parms[1])));
		//		}

		System.out.println(bikeVo.getgLat()+";"+bikeVo.getgLng());
		List<BikeVo> list = new BikeDaoImpl().getNearBike(bikeVo,getIndustryId(req));
		setData(resp, list);
	}


	protected void getNearBikesInUserCity (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{


		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req), true);
		if (userVo == null) {
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}


		String[] params = new String[]{"lat", "lng"};
		boolean hasParams = checkRequstParams(req, resp, params);

		if (!hasParams) {
			return;
		}


		double lat = ValueUtil.getDouble(req.getParameter(params[0]));
		double lon = ValueUtil.getDouble(req.getParameter(params[1]));
		int cityId = userVo.getCityId();
		String industryType = (String)req.getParameter(getIndustryId(req));

		List<BikeVo> list = new BikeDaoImpl().getNearBike(lat, lon, cityId, industryType);
		setData(resp, list);

	}



	protected void getBikeRideHistoryForUser (HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{

		UserVo userVo = new UserDaoImpl().getUserWithId(getUserId(req), true);
		if (userVo == null) {
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		List<BikeUseVo> list = new ArrayList<BikeUseVo>();

		int hostId = ValueUtil.getInt(req.getParameter("hostId"));
		int pageNo = ValueUtil.getInt(req.getParameter("pageNo"));

		if(hostId > 0){
			list = new BikeUseDaoImpl().getGroupRideListForUser(userVo.getuId(),req.getParameter("hostId"),getStartPage(req));
		}else{
			list = new BikeUseDaoImpl().getBikeUseListForUser(userVo,pageNo);
		}

		setData(resp, list);

	}



	/**
	 * 30002
	 * post
	 * @Title:        reserveBike 
	 * @Description:  预约单车/book bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-25 下午6:28:45
	 */
	protected void reserveBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"bikeId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		// check if exceeding number of bikes allowed to reserve in day 
		if(BikeBo.getUserLeftReserverCountInDay(getUserId(req)) == 0){
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", "0");
			dataMap.put("code", HTTP_RESULT_BIKE_IS_RESERVE_NOT_COUNT);
			dataMap.put("maxCancelCount", BikeReserveVo.Reserve_Cancel_Count_In_Day);
			dataMap.put("leftCount", BikeBo.getUserLeftReserverCountInDay(getUserId(req)));
			setObjectData(resp,dataMap);
			return;
		}

		IBikeReserveDao bikeUseDao = new BikeReserveDaoImpl();
		if(bikeUseDao.userHaveReserve(getUserId(req))){
			// 已有预约的单车/already booked bike
			setCode(resp, HTTP_RESULT_HAD_RESERVE_BIKE_ERROR);
			return;
		}

		String bikeId = req.getParameter(parms[0]);

		BikeVo bikeVo = new BikeDaoImpl().getBikePriceInfo(bikeId);
		if(bikeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		if(bikeVo.getError_status() == 3){ //单车已报废/bike already scrapped
			setCode(resp, HTTP_RESULT_BIKE_CANNOT_USE_ERROR);
			return;
		}else if(bikeVo.getError_status() == 4){//wait for active
			setCode(resp, HTTP_RESULT_BIKE_IS_NOT_ACTIVE);
			return;
		}else if(bikeVo.getError_status() > 0){//故障/error
			setCode(resp, HTTP_RESULT_BIKE_IS_ERROR);
			return;
		}
		if(bikeVo.getUseStatus() == BikeUseVo.STATUS_UNLOCK_ING || bikeVo.getUseStatus() == BikeUseVo.STATUS_USING){
			//can't reserve in use bike
			setCode(resp, HTTP_RESULT_BIKE_IS_USEING_ERROR);
			return;
		}

		if(bikeVo.getUseStatus() ==2){//已被预约，判断是否为预约用户/already booked, judge whether is booked user
			BikeReserveVo reserveVo = new BikeReserveDaoImpl().getReserveInfo(bikeVo.getBid());
			if(reserveVo != null && !reserveVo.getUid().equals(getUserId(req))){
				//非预约用户，返回被预约/not booked user, return booked
				setCode(resp, HTTP_RESULT_BIKE_IS_RESERVE_ERROR);
				return;
			}
		}

		if(!checkUserCanUseBike(req, resp, bikeVo)){
			return;
		}

		boolean flag = bikeUseDao.reserveBike(getUserId(req), bikeId);

		JSONObject dataMap= new JSONObject();
		dataMap.put("data", JSON.toJSON(flag?"1":"0"));
		dataMap.put("timeOut", BikeReserveVo.Reserve_TimeOut);
		dataMap.put("code", HTTP_RESULT_OK);
		dataMap.put("maxCancelCount", BikeReserveVo.Reserve_Cancel_Count_In_Day);
		dataMap.put("leftCount", BikeBo.getUserLeftReserverCountInDay(getUserId(req)));
		setObjectData(resp,dataMap);
	}

	/**
	 * 30003
	 * @Title:        cancelReserveBike 
	 * @Description:  取消预约单车/cancel booked bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-25 下午6:29:12
	 */
	protected void cancelReserveBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean flag = new BikeReserveDaoImpl().updateReserveStatus(getUserId(req), 3);

		JSONObject dataObject = new JSONObject();
		dataObject.put("data", flag?"1":"0");
		dataObject.put("maxCancelCount", BikeReserveVo.Reserve_Cancel_Count_In_Day);
		dataObject.put("leftCount", BikeBo.getUserLeftReserverCountInDay(getUserId(req)));
		dataObject.put("code", HTTP_RESULT_OK);
		setMap(resp, dataObject);
	}


	/**
	 * 30004
	 * get
	 * @Title:        getStartUseBikeProcess 
	 * @Description:  获取开锁进度/obtain unlock schedule
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月25日 下午6:23:57
	 */
	protected void getStartUseBikeProcess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"bikeNumber"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String bikeNumber = req.getParameter(parms[0]);
		IBikeUseDao useDao = new BikeUseDaoImpl();
		//判定是否开锁成功,获取当前使用信息/judge whether open lock success, obtain current use information
		BikeUseVo bikeUseVo = useDao.getUseIngInfoWithBike(bikeNumber);
		if(bikeUseVo != null && bikeUseVo.getUid().equals(getUserId(req))
				){
			if(bikeUseVo.getBikeVo().getStatus() == 1 && ValueUtil.getLong(bikeUseVo.getStartTime()) > 0){
				//返回使用信息/return use information
				setCode(resp, HTTP_RESULT_BIKE_UNLOCKING_SUCCESS);
				return;
			}else{
				//正在解锁/unlocking
				setCode(resp, HTTP_RESULT_BIKE_UNLOCKING);
				return;
			}

		}
		long useDate = ValueUtil.getLong(req.getParameter("date"));
		if(useDate > 0){
			bikeUseVo = useDao.getUseInfo(useDate);
			if(bikeUseVo != null && ValueUtil.getInt(bikeUseVo.getEndTime()) != 0){
				//trip is quickly end
				setCode(resp, HTTP_RESULT_BIKE_FINISH_USE);
				return;
			}
		}

		//请求不存在或已超时/request not exist or overtime
		setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
	}

	private boolean getMoreRide(HttpServletRequest req) {
		boolean moreRide = ValueUtil.getInt(req.getParameter("moreRide")) == 1?true:false;
		return moreRide;
	}

	/**
	 * 30004
	 * post
	 * @Title:        startUseBike 
	 * @Description:  开始使用单车/begin to use bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-25 下午6:53:05
	 */
	protected void startUseBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"bikeNumber","startLat","startLng"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		String bikeNumber = req.getParameter(parms[0]);
		if(ValueUtil.getLong(bikeNumber) == 0){
			String[] temp = bikeNumber.split("No=");
			if(temp.length > 1){
				bikeNumber = temp[1];
			}
		}
		if(ValueUtil.getLong(bikeNumber) == 0){
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		//获取单车详情/gain bike details
		BikeVo bikeVo =  new BikeDaoImpl().getBikeInfoWithNumber(bikeNumber);
		if(bikeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		//获取当前预约信息/gain current booked information
		BikeReserveVo reserveVo = new BikeReserveDaoImpl().getUserReserveInfo(getUserId(req));
		if(reserveVo != null && !bikeNumber.equals(reserveVo.getBikeVo().getNumber())){
			// 已有预约的单车/already booked bike
			//			setCode(resp, HTTP_RESULT_HAD_RESERVE_BIKE_ERROR);
			//			return;
			//解锁当前车时，取消其他预约
			new BikeReserveDaoImpl().updateReserveStatus(getUserId(req), 2);
		}
		//越界上锁标记/transboundary unlock sign
		boolean outAreaLock = false;

		IBikeUseDao bikeUseDao = new BikeUseDaoImpl();
		boolean moreRide = getMoreRide(req);

		synchronized (BikeServlet.class) {
			//判定是否开锁成功,获取当前使用信息/judge whether open lock success, obtain current use information
			BikeUseVo bikeUseVo = new BikeUseDaoImpl().getUseIngInfoWithBike(bikeNumber);
			if(bikeUseVo != null && bikeUseVo.getOut_area() > 0 && bikeUseVo.getBikeVo().getStatus() == 0){
				//越界上锁，可继续开锁骑行/transboundary lock, can go on open lock to ride
				outAreaLock = true;
			}
			//获取当前使用信息/gain current use information
			BikeUseVo currentBikeUseVo = bikeUseDao.getUseIngInfoWithUser(getUserId(req));

			boolean hostRide = false;
			String rideUser = "";

			if(moreRide){
				int rideingCount = bikeUseDao.getRideIngCountWithUser(getUserId(req),null,false);
				if(!outAreaLock &&  rideingCount> SystemConfigBo.SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT){
					setCode(resp, HTTP_RESULT_BIKE_IS_RIDE_NOT_COUNT);
					return;
				}
				if(rideingCount == 0){
					//first ride set host
					hostRide = true;
				}else if(bikeUseVo == null) 
				{
					if(StringUtils.isEmpty(rideUser = req.getParameter("rideUser"))){
						setCode(resp, HTTP_RESULT_PARAMETER_MISS);
						return;
					}else if(LockOrderBo.checkLockOnline(bikeVo.getBikeType(), bikeVo.getImei(), false)  == 0){
						setCode(resp, HTTP_RESULT_BIKE_IS_DISCONNECT);
						return;
					}
				}

			}else{


				if(currentBikeUseVo != null && !bikeNumber.equals(currentBikeUseVo.getBikeVo().getNumber())){
					//有正在使用的单车，不能使用下一辆/have bike are using, can't use next bike
					setCode(resp, HTTP_RESULT_BIKE_IS_USEING_OTHERS);
					return;
				}
			}

			if(!outAreaLock && bikeUseVo != null && bikeUseVo.getUid().equals(getUserId(req))
					&& ValueUtil.getLong(bikeUseVo.getStartTime()) > 0 && bikeUseVo.getBikeVo().getStatus() == 1){
				//返回使用信息/return use information
				setCode(resp, HTTP_RESULT_BIKE_UNLOCKING_SUCCESS);
				return;
			}


			double startLat = ValueUtil.getDouble(req.getParameter(parms[1]));
			double startLng = ValueUtil.getDouble(req.getParameter(parms[2]));
			if(startLat == 0 || startLng == 0){
				setCode(resp, HTTP_RESULT_NO_GPS);
				return;
			}



			//更新锁GPS位置/update lock gps position
			new BikeDaoImpl().updateGps(ValueUtil.getLong(bikeVo.getImei()) , startLat, startLng);


			if(!checkUserCanUseBike(req, resp, bikeVo)){
				return;
			}
			if(bikeVo.getError_status() == 3){ //单车已报废/bike already scrapped
				setCode(resp, HTTP_RESULT_BIKE_CANNOT_USE_ERROR);
				return;
			}else if(bikeVo.getError_status() == 4){//wait for active
				setCode(resp, HTTP_RESULT_BIKE_IS_NOT_ACTIVE);
				return;
			}else if(bikeVo.getError_status() > 0){//故障/error
				setCode(resp, HTTP_RESULT_BIKE_IS_ERROR);
				return;
			}
			if(bikeVo.getUseStatus() ==2){//已被预约，判断是否为预约用户/already booked, judge whether is booked client
				reserveVo = new BikeReserveDaoImpl().getReserveInfo(bikeVo.getBid());
				if(reserveVo != null && !reserveVo.getUid().equals(getUserId(req))){
					//非预约用户，返回被预约/not booked client, return book
					setCode(resp, HTTP_RESULT_BIKE_IS_RESERVE_ERROR);
					return;
				}
			}
			//关联蓝牙信息/conncet bluetooth information
			BleBikeVo bleVo = null;
			if(bikeVo.getTypeVo().haveBleLock()){
				bleVo = new BleBikeDaoImpl().getBleBike(bikeNumber);
			}
			int tcpConnected = LockOrderBo.checkLockOnline(bikeVo.getBikeType(), bikeVo.getImei(),true);
			//纯GPRS版故障判断/pure gprs version error judge
			if(bleVo == null && bikeVo.getTypeVo().haveGprsLock() && tcpConnected == 0){
				setCode(resp, HTTP_RESULT_BIKE_IS_ERROR);
				return;
			}


			long nowTime = TimeUtil.getCurrentLongTime();

			if(bikeUseVo != null && !bikeUseVo.getUid().equals(getUserId(req)) 
//					&& (ValueUtil.getLong(bikeUseVo.getStartTime()) > 0 || bikeUseVo.getOut_area() > 0)
					){
				if(tcpConnected == 0 && bleVo != null && bikeUseVo != null  
						&& bikeUseVo.getUpdateTime() < nowTime-10
						){
					//蓝牙锁，返回MAC地址和使用信息，防止异常状态上报/bluetooth lock, return mac address and use information, in case fo abnormal status reported
					JSONObject dataMap= new JSONObject();
					dataMap.put("data", "");
					dataMap.put("mac", bleVo.getMac());
//					if(tcpConnected == 0){
						dataMap.put("power", bikeVo.getPower());
//					}

					dataMap.put("date", bikeUseVo.getDate());
					dataMap.put("bikeType", bikeVo.getBikeType());
					dataMap.put("code", HTTP_RESULT_BIKE_IS_USEING_ERROR);
					setMap(resp, dataMap);
					return;
				}else{
					//已被他人使用/already been used by others
					setCode(resp, HTTP_RESULT_BIKE_IS_USEING_ERROR );
					return;
				}

			}else if(bikeVo.getStatus() == 1 && bikeVo.getUseStatus() == 1){ //单车正在使用中/bike is using
				if(tcpConnected == 0 && bleVo != null && bikeUseVo != null 
//						&& bikeUseVo.getUpdateTime() < nowTime-10
						){
					//蓝牙锁，返回MAC地址和使用信息，防止异常状态上报/bluetooth lock, return mac address and use information, prevent abnormal status report
					JSONObject dataMap= new JSONObject();
					dataMap.put("data", "");
					dataMap.put("mac", bleVo.getMac());
//					if(tcpConnected == 0){
						dataMap.put("power", bikeVo.getPower());
//					}
					dataMap.put("date", bikeUseVo.getDate());
					dataMap.put("bikeType", bikeVo.getBikeType());
					dataMap.put("code", HTTP_RESULT_BIKE_IS_USEING_ERROR);
					setMap(resp, dataMap);
					return;
				}else{
					setCode(resp, HTTP_RESULT_BIKE_IS_USEING_ERROR);
					return;
				}

			}




			boolean flag = false;
			long timestamp = TimeUtil.getCurrentLongTime();

			if(moreRide){
				if(bikeUseVo != null){
					flag = true;
					long oldDate = bikeUseVo.getUpdateTime();
					if(oldDate > timestamp-120 && bikeUseVo.getOut_area() == 0){
						System.out.println("request again,use old time");
						//原记录没有超时用旧时间，防止时间不一致情况
						timestamp = oldDate;
					}else{
						System.out.println("request again,use new time");
						//更新时间戳，用于重新开锁请求/update timedraw, use for renew open lock request
						bikeUseDao.updateUseBikeRequestDate(bikeUseVo.getId(), timestamp);
					}
				}else{
					BikeUseVo useVo = new BikeUseVo();
					useVo.setUid(getUserId(req));
					useVo.setBid(bikeVo.getBid());
					useVo.setStartLat(startLat);
					useVo.setStartLng(startLng);
					useVo.setDate(timestamp+"");
					useVo.setOpenWay(ValueUtil.getInt(req.getParameter("openWay")));
					useVo.setRideUser(rideUser);

					if(!hostRide && currentBikeUseVo != null && !bikeNumber.equals(currentBikeUseVo.getBikeVo().getNumber())){
						useVo.setHostId(ValueUtil.getInt(currentBikeUseVo.getId()));
						if(currentBikeUseVo.getGroupRide() == 0){
							//tag host is group ride
							bikeUseDao.updateRideIsGroup(currentBikeUseVo.getId());
						}
						useVo.setGroupRide(1);
					}
					//骑行越界判断/riding transboundary judge
					BikeBo bikeBo = new BikeBo();
					LatLng postion = new LatLng(startLat, startLng);
					if(bikeBo.checkBikeInProhibitedParkingArea(bikeVo.getCityId(), postion)){
						useVo.setOut_area(2);//in Prohibited parking
					}else if(!bikeBo.checkBikeIsInCityArea(bikeVo.getCityId(), postion)){
						useVo.setOut_area(1);//out of area
					}else{
						useVo.setOut_area(0);//normal
					}
					//增加使用记录/add use record
					flag = bikeUseDao.startUseBike(useVo);
				}
			}else{
				if(!bikeUseDao.haveUseingBike(getUserId(req))){//判定记录是否存在/judge record whether exist
					BikeUseVo useVo = new BikeUseVo();
					useVo.setUid(getUserId(req));
					useVo.setBid(bikeVo.getBid());
					useVo.setStartLat(startLat);
					useVo.setStartLng(startLng);
					useVo.setDate(timestamp+"");
					useVo.setOpenWay(ValueUtil.getInt(req.getParameter("openWay")));
					//骑行越界判断/riding transboundary judge
					BikeBo bikeBo = new BikeBo();
					LatLng postion = new LatLng(startLat, startLng);
					if(bikeBo.checkBikeInProhibitedParkingArea(bikeVo.getCityId(), postion)){
						useVo.setOut_area(2);//in Prohibited parking
					}else if(!bikeBo.checkBikeIsInCityArea(bikeVo.getCityId(), postion)){
						useVo.setOut_area(1);//out of area
					}else{
						useVo.setOut_area(0);//normal
					}
					//增加使用记录/add use record
					flag = bikeUseDao.startUseBike(useVo);


				}else{
					flag = true;
					BikeUseVo useVo = bikeUseDao.getUseIngInfoWithUser(getUserId(req));
					long oldDate = useVo.getUpdateTime();
					if(oldDate > timestamp-120 && useVo.getOut_area() == 0){
						//原记录没有超时用旧时间，防止时间不一致情况
						timestamp = oldDate;
					}else{
						//更新时间戳，用于重新开锁请求/update timedraw, use for renew open lock request
						bikeUseDao.updateUseBikeRequestDate(useVo.getId(), timestamp);
					}
				}
			}

			boolean inputNumber = ValueUtil.getInt(req.getParameter("inputNumber")) == 1?true:false;
			if(!inputNumber && flag && bikeVo.getTypeVo().haveGprsLock()){
				//发送开锁指令/send open lock command
				LockOrderBo.sendUnlockOrder(bikeVo.getBikeType(), bikeVo.getImei(), ValueUtil.getInt(getUserId(req)), timestamp);

			}
			JSONObject object = new JSONObject();

			int industryId = ValueUtil.getInt(getIndustryId(req));
			if(industryId == 4 || industryId == 5 || industryId == 7){
				//二合一/2 in 1
				setFlagData(resp,  flag);
			}else{
				//三合一/3 in 1
				object.put("data", flag?"1":"0");
				if(bleVo != null){
					object.put("mac", bleVo.getMac());
					object.put("date", timestamp);
				}
//				if(tcpConnected == 0){
					object.put("power", bikeVo.getPower());
//				}
				if(bikeVo.getReadpack() == 1){
					RedPackRuleVo rule = new RedPackRuleDaoImpl().getRuleByBikeId(bikeVo.getBid());
					if(rule != null){
						object.put("redpackRule", rule);
					}
				}
				//当前区域边界信息/current area board information
				CityVo cityVo = new CityDaoImpl().getCityInfo(bikeVo.getCityId()+"");
				if(cityVo != null){
					object.put("cityVo", cityVo);
				}
				object.put("tcpConnected", tcpConnected);
				object.put("bikeType", bikeVo.getBikeType());
				setData(resp, object);
			}



			/*//关联电话卡信息/connect phone card information
			BikeSim bikeSim = new BikeSimDaoImpl().getBikePhoneInfo(bikeNumber);
			if(bikeSim != null){
				//短信解锁/short message unlock
				SmsBo.smsUnlockBike(bikeSim.getBikeVo().getImei(), bikeSim.getPhoneVo().getPhone());
			}*/
		}

		
	}

	/**
	 * 30005
	 * @Title:        updateUseBikeProcess 
	 * @Description:  更新单车骑行路径/update bike riding route
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-25 下午7:10:49
	 */
	protected void updateUseBikeProcess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"bikeNumber","lat","lng"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		String bikeNumber = req.getParameter(parms[0]);
		double currentLat = ValueUtil.getDouble(req.getParameter(parms[1]));
		double currentLng = ValueUtil.getDouble(req.getParameter(parms[2]));

		if(currentLat == 0 || currentLng == 0){
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		//获取单车详情
		BikeVo bikeVo =  new BikeDaoImpl().getBikeInfoWithNumber(bikeNumber);

		if(bikeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		IBikeUseDao bikeUseDao = new BikeUseDaoImpl();

		//获取使用信息(正在使用中)/obtain use information(using)
		BikeUseVo useVo = bikeUseDao.getUseInfo(getUserId(req),bikeVo.getBid());

		//获取位置点信息（适用于APP后台运行的位置）/obtain position point information(suit for app background run out position)
		String lats[] = null,lngs[] = null;
		if(!StringUtils.isEmpty(req.getParameter("lats")) && !StringUtils.isEmpty(req.getParameter("lngs"))){
			lats = req.getParameter("lats").split(",");
			lngs = req.getParameter("lngs").split(",");
		}
		if(useVo == null){
			//获取使用完成的订单信息/obtain use finish transaction information
			TradeVo tradeVo = new TradeDaoImpl().getNotifyTradeDetail(getUserId(req));
			if(tradeVo != null){
				//返回使用完成的订单信息/return use finish order information
				setData(resp, new BikeBo().getBikeOrderInfo(tradeVo),2,2);
				//修改订单状态为已通知/modify transaction status as noticed
				new TradeDaoImpl().updateNotifySuccess(getUserId(req));

				//已结束的订单，直接更新历史路径/already finish order, directy update history route
				if(new BikeBo().getBikeUseRouleInfo(lats, lngs, tradeVo.getBikeUseVo(), bikeVo.getCityId())){
					bikeUseDao.updateUseBikeProcess(useVo,false);
				}
				return;
			}else{
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return;
			}
		}

		if(useVo.getEndLat() == currentLat && useVo.getEndLng() == currentLng){
			//同一位置，直接返回使用信息/same position, direct rturn use information
			setData(resp, new BikeBo().getUseingBikeInfo(useVo, bikeVo),1,1);
			//更新时间
			bikeUseDao.updateUseBikeUpdateTime(useVo.getId(),TimeUtil.getCurrentLongTime());
			return;
		}


		//获取历史路径信息/obtain history route information
		new BikeBo().getBikeUseRouleInfo(lats, lngs, useVo, bikeVo.getCityId());

		useVo.setCurrentLat(currentLat);
		useVo.setCurrentLng(currentLng);
		if(ValueUtil.getInt(req.getParameter("errorLocation ")) == 0){
			//计算骑行距离/caculate riding distance
			useVo.setDistanceWithStartAndEnd();
			//生成路径/generate route
			useVo.setOrbitWithPoint(currentLat, currentLng);
		}

		//骑行越界判断/riding transboundary judge
		BikeBo bikeBo = new BikeBo();
		LatLng postion = new LatLng(currentLat, currentLng);
		int oldStatus = useVo.getOut_area();
		if(bikeBo.checkBikeInProhibitedParkingArea(bikeVo.getCityId(), postion)){
			useVo.setOut_area(2);//in Prohibited parking

		}else if(!bikeBo.checkBikeIsInCityArea(bikeVo.getCityId(), postion)){
			useVo.setOut_area(1);//out of area

		}else{
			useVo.setOut_area(0);//normal

		}
		boolean flag = bikeUseDao.updateUseBikeProcess(useVo,false);
		if( useVo.getOut_area() == 0
				//				&& oldStatus> 0 
				&& bikeVo.getStatus() == 0){
			IBikeUseDao useDao = new BikeUseDaoImpl();
			//锁已关，越界状态由越界变为正常时，需触发结费业务
			BikeUseVo bikeUseVo =  useDao.getUseIngDetailInfo(useVo.getDate(),useVo.getUid());
			if(bikeUseVo != null && useDao.getRideIngCountWithUser(useVo.getUid(),null,true) == 1){
				int result = new BikeBo().dealBikeOrder(bikeUseVo, true);
				System.out.println("updateUseBikeProcess end rideing:"+result);
			}


		}
		//返回使用信息/return use information
		setData(resp, new BikeBo().getUseingBikeInfo(useVo, bikeVo),1,2);
		if(useVo.getOut_area() > 0){
			System.out.println("updateUseBikeProcess number:"+bikeNumber+";lat:"+currentLat+";lng:"+currentLng+";orbit:"+useVo.getOrbit()
					+";outArea:"+useVo.getOut_area()+";bikeCity:"+bikeVo.getCityId());
		}

	}

	/**
	 * 30006
	 * @Title:        getCurrentUseInfo 
	 * @Description:  返回当前的使用信息/return current use information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws JSONException    
	 * @return:       void    
	 * @author        Albert
	 * @throws JSONException 
	 * @Date          2017年4月1日 下午2:34:20
	 */
	protected void getCurrentUseInfo(HttpServletRequest req, HttpServletResponse resp) throws JSONException{
		JSONObject dataMap= new JSONObject();
		dataMap.put("maxRideCount", SystemConfigBo.SYSTEM_CONFIG_RIDE_GROUP_MAX_COUNT);
		//最新活动资讯获取/final activist data obtain
		NewsVo newsVo = new NewsDaoImpl().getTopNews(getCityId(req));
		if(newsVo != null){
			dataMap.put("newsVo", JSON.toJSON(newsVo));
		}

		//最新版本获取/final version obtain
		int versionCode = ValueUtil.getInt(req.getParameter("versionCode"));
		if(versionCode > 0){
			AppVersionVo topVersion = new AppVersionDaoImpl().getTopVersion(versionCode);
			if(topVersion  != null){
				dataMap.put("topVersion", JSON.toJSON(topVersion));
			}
		}

		String userId = getUserId(req);

		//信息未读消息数/data unread news number
		IMessageBoxDao messageDao = new MessageBoxDaoImpl();
		int messageCount = messageDao.getNoReadMessageCount(userId);
		messageCount += messageDao.getNoReadReplyCount(userId);
		if(messageCount > 0){
			dataMap.put("messageCount", messageCount);
		}


		//判定是否绑定银行卡信息/judge whether bind bank card information
		boolean bindCard = UserBo.checkUserBindCard(userId);
		dataMap.put("bindCard", bindCard==true?1:0);

		//当前区域边界信息/current area board information
		CityVo cityVo = new CityDaoImpl().getCityInfo(getCityId(req)+"");
		if(cityVo != null){
			dataMap.put("cityVo", cityVo);
		}

		// add active hold
		HoldVO activeHold = new HoldBO().activeHoldForUser(getUserId(req));
		if (activeHold != null) {

			dataMap.put("hold", activeHold);

			BikeVo heldBike = new HoldBO().lastUseBikeForUser(getUserId(req));
			if (heldBike != null) {
				dataMap.put("heldBike", heldBike);
			}

		}

		//获取当前预约信息/obtain current book information
		BikeReserveVo reserveVo = new BikeReserveDaoImpl().getUserReserveInfo(userId);
		if(reserveVo != null){
			dataMap.put("data", JSON.toJSON(reserveVo));
			dataMap.put("type", 1);
			dataMap.put("maxCancelCount", BikeReserveVo.Reserve_Cancel_Count_In_Day);
			dataMap.put("leftCount", BikeBo.getUserLeftReserverCountInDay(getUserId(req)));
			//当前区域边界信息/current area boad information
			cityVo = new CityDaoImpl().getCityInfo(reserveVo.getBikeVo().getCityId()+"");
			if(cityVo != null){
				dataMap.put("cityVo", cityVo);
			}
			//返回预约记录/return book record
			setMapWithCodeOk(resp, dataMap);
			return;
		}

		boolean moreRide = getMoreRide(req);

		if(moreRide){
			List<BikeUseVo> rideList = new BikeUseDaoImpl().getRideListWithUser(userId,0);
			if(rideList.size() > 0){
				dataMap.put("type", 2);
				for (BikeUseVo bikeUseVo : rideList) {
					bikeUseVo = new BikeBo().getUseingBikeInfo(bikeUseVo, bikeUseVo.getBikeVo());
				}
				dataMap.put("data", rideList);
			}
		}else{
			//获取当前使用信息/obtain current use information
			BikeUseVo bikeUseVo = new BikeUseDaoImpl().getUseIngInfoWithUser(userId);
			//mark 2017-4-21 09:59:14 增加&& ValueUtil.getLong(bikeUseVo.getStartTime()) > 0判定，解决未开锁成功返回开始计费问题/0 judge, solve unlock failure return begin caculate fee problem
			if(bikeUseVo != null && ValueUtil.getLong(bikeUseVo.getStartTime()) > 0){
				//返回使用信息/return use information
				//			setData(resp,JSON.toJSON(new BikeBo().getUseingBikeInfo(bikeUseVo,
				//					bikeUseVo.getBikeVo())) , 2);
				dataMap.put("type", 2);
				dataMap.put("data", JSON.toJSON(new BikeBo().getUseingBikeInfo(bikeUseVo,
						bikeUseVo.getBikeVo())));
				//当前区域边界信息/current area boad information
				cityVo = new CityDaoImpl().getCityInfo(bikeUseVo.getBikeVo().getCityId()+"");
				if(cityVo != null){
					dataMap.put("cityVo", cityVo);
				}
			}
		}



		//获取使用完成的订单信息/gain use finish order information
		TradeDaoImpl tradeDao = new TradeDaoImpl();
		TradeVo tradeVo = tradeDao.getNotifyTradeDetail(getUserId(req));
		if(tradeVo == null){
			//current trip order info
			long currentDate = ValueUtil.getLong(req.getParameter("date"));
			tradeVo = tradeDao.getBikeUseTradeDetail(currentDate);
		}
		if(tradeVo != null){
			//返回使用完成的订单信息/return use finish order information
			dataMap.put("type", 3);
			dataMap.put("data", JSON.toJSON(new BikeBo().getBikeOrderInfo(tradeVo)));
			if(tradeVo.getStatus() == 1){
				//修改订单状态为已通知/modify order status as already inform
				new TradeDaoImpl().updateNotifySuccess(getUserId(req));
			}
		}


		if(dataMap.size() > 0){
			setMapWithCodeOk(resp, dataMap);
		}else{
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}

	}

	/**
	 * 30007
	 * @Title:        getBikeTypeList 
	 * @Description:  获取单车类型/gain bike type
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月5日 上午11:13:26
	 */
	protected void getBikeTypeList(HttpServletRequest req, HttpServletResponse resp){
		List<BikeTypeVo> typeList = new BikeTypeDaoImpl().getTypeList(ValueUtil.getInt(getIndustryId(req)),getCityId(req));
		setData(resp, typeList);
	}

	/**
	 * 30008
	 * @Title:        getBikeUseList 
	 * @Description:  获取行程集合/obtain route gather
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月5日 下午4:31:50
	 */
	protected void getBikeUseList(HttpServletRequest req, HttpServletResponse resp){
		List<TradeVo>  list = new TradeDaoImpl().getBikeTradeList(getUserId(req),getStartPage(req));
		setData(resp, list);
	}

	/**
	 * 30009
	 * @Title:        addTradeReceipt 
	 * @Description:  开收据/open receipt
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月5日 下午5:28:25
	 */
	protected void addTradeReceipt(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"trade_ids","firstname","lastname","phone","address","zipCode","country"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new com.pgt.bikelock.dao.impl.TradeReciptDaoImpl().addTradeReceipt(new TradeReceiptVo(req, parms, getUserId(req)));
		setData(resp, flag?"1":"0");
	}

	/**
	 * 30010(废弃)
	 * @Title:        updateNotifySuccess 
	 * @Description:  修改订单状态为已通知/modify order status as already inform
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午6:07:45
	 */
	protected void updateNotifySuccess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		boolean flag = new TradeDaoImpl().updateNotifySuccess(getUserId(req));
		setData(resp, flag?"1":"0");
	}

	/**
	 * 30011（停用）
	 * @Title:        addBikeErrorForCanNotOpenBike 
	 * @Description:  报告单车信息-不能开锁/report bike information-can't open lock
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午12:06:12
	 */
	protected void addBikeErrorForCanNotOpenBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"number"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeErrorVo errorVo = new BikeErrorVo(parms, req);
		String bid = checkBikeExistWithNumber(req, resp, errorVo.getBnumber());
		if(bid == null){
			return;
		}

		errorVo.setType(1);

		boolean flag = new BikeErrorDaoImpl().addBikeError(errorVo);
		setData(resp, flag?"1":"0");
		if(flag){
			BikeBo.autoSetBikeError(errorVo.getBnumber());
			new NotificationBo().sendNotifyToAdmin(NotificationType.Bike_Error_Report, getUserId(req), errorVo.getBnumber(),0);
		}
	}

	/**
	 * 30012（停用）
	 * @Title:        addBikeErrorForDamaged 
	 * @Description: 报告单车信息-故障/report bike information-error
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午12:10:35
	 */
	protected void addBikeErrorForDamaged(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"number","errorType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeErrorVo errorVo = new BikeErrorVo(parms, req);
		if(checkBikeExistWithNumber(req, resp, errorVo.getBnumber())  == null){
			return;
		}
		errorVo.setType(2);
		if(errorVo.getListImage() != null){
			//添加图片/add photo
			errorVo.setImage_ids(new ImageDaoImpl().addImages(errorVo.getListImage()));
		}

		boolean flag = new BikeErrorDaoImpl().addBikeError(errorVo);
		if(flag){
			BikeBo.autoSetBikeError(errorVo.getBnumber());
		}
		setData(resp, flag?"1":"0");
		if(flag){
			new NotificationBo().sendNotifyToAdmin(NotificationType.Bike_Error_Report, getUserId(req), errorVo.getBnumber(),0);
		}
	}

	/**
	 * 30013（停用）
	 * @Title:        addBikeErrorForViolations 
	 * @Description:  报告单车信息-违章/report bike information-illegal
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午12:11:48
	 */
	protected void addBikeErrorForViolations(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"number"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeErrorVo errorVo = new BikeErrorVo(parms, req);

		if(checkBikeExistWithNumber(req, resp, errorVo.getBnumber())  == null){
			return;
		}

		errorVo.setType(3);
		if(errorVo.getListImage() != null){
			//添加图片/add photo
			errorVo.setImage_ids(new ImageDaoImpl().addImages(errorVo.getListImage()));
		}

		boolean flag = new BikeErrorDaoImpl().addBikeError(errorVo);
		setData(resp, flag?"1":"0");
		if(flag){
			new NotificationBo().sendNotifyToAdmin(NotificationType.Bike_Error_Report, getUserId(req), errorVo.getBnumber(),0);
		}
	}

	/**
	 * 30014
	 * @Title:        getBikeAreaList 
	 * @Description:  获取停车区域/obtain parking area
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午4:55:37
	 */
	protected void getBikeAreaList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"lat","lng"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		double currentLat = ValueUtil.getDouble(req.getParameter("cur_lat"));
		double currentLng = ValueUtil.getDouble(req.getParameter("cur_lng"));
		System.out.println("currentLat2:"+currentLat+";currentLng"+currentLng);

		List<AreaVo> list = new AreaDaoImpl().getAreaList(req.getParameter("ids"),currentLng,currentLat,getCityId(req));
		setData(resp, list);
	}

	/**
	 * 30015
	 * @Title:        getBikeUseTrade 
	 * @Description:  骑行订单详情/riding order details
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月17日 上午9:35:47
	 */
	protected void getBikeUseTrade(HttpServletRequest req, HttpServletResponse resp){
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		TradeVo tradeVo= new TradeDaoImpl().getBikeUseTradeDetail(req.getParameter(parms[0]));
		if(tradeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		setData(resp, JSON.toJSON(new BikeBo().getBikeOrderInfo(tradeVo)));
	}

	/**
	 * 30016
	 * @Title:        addBikeErrorForViolations 
	 * @Description:  报告单车信息/report bike information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午12:11:48
	 */
	protected void addBikeError(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"number","type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeErrorVo errorVo = new BikeErrorVo(parms, req);
		errorVo.setType(ValueUtil.getInt(req.getParameter(parms[1])));
		errorVo.setUid(getUserId(req));

		BikeVo bikeVo = new BikeDaoImpl().getBikeInfoWithNumber(errorVo.getBnumber());

		if(bikeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		BikeUseVo useVo = new BikeUseDaoImpl().getUseIngDetailInfo(null, getUserId(req));
		if(errorVo.getType() == 4){
			if(useVo == null){
				//无骑行记录/no history record
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return;
			}
			errorVo.setBike_useid(useVo.getId());
		}

		IBikeErrorDao errorDao = new BikeErrorDaoImpl();
		//判断故障申请是否已存在（等待审核）/judge error apply whehter already exist(wait check)
		if(errorDao.checkErrorIsWaitForApply(errorVo.getUid(), errorVo.getBnumber(),errorVo.getType())){
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;
		}

		if(errorVo.getType() == 1 || errorVo.getType() == 4){
			//不能开锁或关锁未结费故障，检测锁连接是否正常/can't open lock or closed lock not paid error, test lock whether normal
			BikeBo bikeBo = new BikeBo();
			if((useVo.getOpenWay() == 2 || (!bikeVo.getTypeVo().haveBleLock() && bikeVo.getTypeVo().haveGprsLock())) 
					&& !bikeBo.checkBikeIsConnected(ValueUtil.getLong(bikeVo.getImei()))){
				//锁连接异常，标记为自动处理故障(2合1，纯GRPS版)/lock connect abnormal, sign as auto tackle erro(2 in 1, pure gprs version)
				errorVo.setStatus(4);
				if(useVo != null){
					//自动结束骑行/auto finish cycling
					useVo.setCloseWay(3);
					bikeBo.autoFinishBikeUseWithError(useVo,true);
				}

			}
		}

		if(errorVo.getListImage() != null){
			//添加图片/add photo
			errorVo.setImage_ids(new ImageDaoImpl().addImages(errorVo.getListImage()));
		}

		boolean flag = errorDao.addBikeError(errorVo);

		if(flag){
			if(errorVo.getType() == 1 || errorVo.getType() == 2 || errorVo.getType() == 4
					|| errorVo.getType() == 7 || errorVo.getType() == 9){
				//标记单车为故障状态/sign biek as error status
				BikeBo.autoSetBikeError(errorVo.getBnumber());
			}
			new NotificationBo().sendNotifyToAdmin(NotificationType.Bike_Error_Report, getUserId(req), errorVo.getBnumber(),0);
		}

		setData(resp, flag?"1":"0");
	}

	/**
	 * 30017
	 * @Title:        startBikeUse 
	 * @Description:  开始骑行（适用于带蓝牙设备）/begin cycling(suit for device with bluetooth)
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月16日 下午4:01:31
	 */
	protected void startBikeUse(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("startBikeUse");
		String[] parms = new String[]{"date"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String userId = getUserId(req);
		long time = ValueUtil.getLong(req.getParameter(parms[0]));
		String number = ValueUtil.getString(req.getParameter("number"));

		System.out.println("Ble startBikeUse date:"+time+";number:"+number+";time:"+TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm_ss));

		IBikeUseDao useDao = new BikeUseDaoImpl();


		BikeUseVo useVo = useDao.getUseIngDetailInfo(ValueUtil.getInt(userId), 
				time);
		if(useVo == null){
			//解决时间戳不同步问题,找出当前用户的骑行信息/get user current ride info
			useVo = useDao.getUseIngDetailInfo(ValueUtil.getInt(userId), 
					0);
			if(useVo == null){
				useVo = useDao.getUseIngDetailInfo(0, time);
				if(useVo != null){
					useDao.updateUseBikeRider(useVo.getId(), userId);
				}
			}
			if(useVo != null){
				//更新时间戳/update timestamp
				useDao.updateUseBikeRequestDate(useVo.getId(), time);
			}

		}
		if(useVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			BikeVo bikeVo = new BikeDaoImpl().getBikeInfoWithNumber(number);
			if(bikeVo != null){
				//save bike ble record
				new BikeOrderRecordDaoImpl().insert(ValueUtil.getLong(bikeVo.getImei()), BikeOrderRecord.ORDER_UN_LOCK, TimeUtil.getCurrentLongTime(),
						"BLE UNLOCK,Start Ride Fail, Ride request not exist,time:"+time+",uid:"+userId);
			}
			return;
		}
		String bikeNumber = useVo.getBikeVo().getNumber();


		//检查TCP是否已开锁成功 / Check if TCP has been unlocked successfully
		if(useDao.haveOpenSuccess(time)){
			//已通过TCP开锁，直接返回 / Has been opened by TCP, direct return
			setFlagData(resp, true);
			if(useVo.getBikeVo().getStatus() == 0){
				new BikeDaoImpl().updateLockStatus(bikeNumber,1);
			}
			return;
		}

		boolean flag = useDao.startUseBikeSuccess(ValueUtil.getInt(getUserId(req)), 
				time,1);

		if (flag) {

			BikeUseVo bikeUseVo = useDao.getUseIngDetailInfo(ValueUtil.getInt(getUserId(req)), time);
			new HoldBO().payActiveHoldWhenStartRide(bikeUseVo);

		}

		if(!flag){
			//未能正常开始骑行时，手动标记开关/can't riding, manual sign switch
			new BikeDaoImpl().updateLockStatus(bikeNumber,1);
		}
		setFlagData(resp, flag);
		//save bike ble record
		new BikeOrderRecordDaoImpl().insert(ValueUtil.getLong(useVo.getBikeVo().getImei()), BikeOrderRecord.ORDER_UN_LOCK, TimeUtil.getCurrentLongTime(),
				"BLE UNLOCK ,Start Ride "+(flag?"Success":"Fail")+",time:"+time+",uid:"+userId);
		if(req.getParameter("power") != null){
			new BikeDaoImpl().updateLockPower(bikeNumber, ValueUtil.getInt(req.getParameter("power")));
		}
	}

	protected void usageAndHold (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		// req: 30028
		String userId = getUserId(req);
		if (userId == null) {
			setCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		List<HoldVO> list = new HoldBO().holdsForUser(userId);

		if (list == null) {
			setCode(res, HTTP_RESULT_DATA_NULL_ERROR);
		}

		setData(res, list);

	}


	protected void lastBike (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		String userId = getUserId(req);
		if (userId == null) {
			setCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		BikeVo bike = new HoldBO().lastUseBikeForUser(userId);

		if (bike == null) {
			setCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		setData(res, bike);

	}


	protected void cancelBikeHold (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		String userId = getUserId(req);
		if (userId == null) {
			setCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		int status = new HoldBO().cancelAndPayHoldForUser(userId);

		if (status != 0) {
			System.out.println("hold bike failed with status: " + HoldBO.errnoString(status));
			writeJSONCodeWithMessage(res, HTTP_RESULT_DATA_NULL_ERROR, HoldBO.errnoString(status));
			return;
		}

		setCode(res, HTTP_RESULT_OK);

	}


	protected void holdBike (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		String userId = getUserId(req);
		if (userId == null) {
			setCode(res, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}

		int status = new HoldBO().holdBikeForUser(userId);

		if (status != 0) {
			System.out.println("hold bike failed with status: " + HoldBO.errnoString(status));
			writeJSONCodeWithMessage(res, HTTP_RESULT_DATA_NULL_ERROR, HoldBO.errnoString(status));
			return;
		}

		setCode(res, HTTP_RESULT_OK);


	}

	/**
	 * 30018
	 * @Title:        finishBikeUse 
	 * @Description:  结束骑行（适用于带蓝牙设备）/finish cyclcing(suit for device with bluetooth)
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月16日 下午4:01:31
	 */
	protected void finishBikeUse(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String[] parms = new String[]{"uid","date","runTime"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int userId = ValueUtil.getInt(req.getParameter(parms[0]));
		String number = ValueUtil.getString(req.getParameter("number"));
		System.out.println("Ble finishBikeUse number:"+number+";uid"+userId+";date:"+req.getParameter(parms[1])
				+";time:"+TimeUtil.getCurrentTime(TimeUtil.Formate_YYYY_MM_dd_HH_mm_ss));

		IBikeUseDao useDao = new BikeUseDaoImpl();

		long date = ValueUtil.getLong(req.getParameter(parms[1]));
		int runTime = ValueUtil.getInt(req.getParameter(parms[2]));

		BikeUseVo bikeUseVo =  useDao.getUseIngDetailInfo(userId,date);

		BikeDaoImpl bikeDao = new BikeDaoImpl();

		if(bikeUseVo == null){
			//解决时间戳和用户ID不对应问题
			bikeUseVo =  useDao.getUseIngDetailInfo(0,date);
		}

		if(bikeUseVo == null){

			System.out.println("get useing info is null by date");

			BikeVo bikeVo = bikeDao.getBikeInfoWithNumber(number);
			if(bikeVo != null){
				//save bike ble record
				new BikeOrderRecordDaoImpl().insert(ValueUtil.getLong(bikeVo.getImei()), BikeOrderRecord.ORDER_LOCK, TimeUtil.getCurrentLongTime(),
						"BLE LOCK,End ride fail,History not exist,time:"+date+",uid:"+userId+"runTime:"+runTime);

				//修改锁状态为关闭/modify lock status as turned off
				bikeDao.updateLockStatusWithImei(ValueUtil.getLong(bikeVo.getImei()), 0,false);
			}

			//获取使用完成的订单信息/gain use finish order information
			TradeVo tradeVo = new TradeDaoImpl().getNotifyTradeDetail(getUserId(req));
			if(tradeVo != null){
				//返回使用完成的订单信息/return use finish order information
				setData(resp, JSON.toJSON(new BikeBo().getBikeOrderInfo(tradeVo)),2,3);
				//修改订单状态为已通知/modify order status as already inform
				new TradeDaoImpl().updateNotifySuccess(getUserId(req));
				return;
			}else{
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return;
			}
		}

		System.out.println("Ble finishBikeUse date:"+date+";number:"+bikeUseVo.getBikeVo().getNumber()+
				"status:"+bikeUseVo.getBikeVo().getStatus()+";usestatus:"+bikeUseVo.getBikeVo().getUseStatus());

		if(req.getParameter("power") != null){
			new BikeDaoImpl().updateLockPower(bikeUseVo.getBikeVo().getNumber(), ValueUtil.getInt(req.getParameter("power")));
		}

		if(bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_USING || bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_UNLOCK_ING
				//				&& bikeUseVo.getBikeVo().getStatus()==BikeVo.LOCK_ON
				){

			//修改锁状态为关闭/odify lock status as turned off
			bikeDao.updateLockStatusWithImei(ValueUtil.getLong(bikeUseVo.getBikeVo().getImei()), 0,false);

			// 当前 单车正在被租赁,且锁是打开的	/current bike is renting, and lock is open
			bikeUseVo.setEndTime(runTime);
			bikeUseVo.setCloseWay(1);
			int result = new BikeBo().dealBikeOrder(bikeUseVo,true);
			if(result == 1 || result == 0){
				//获取使用完成的订单信息/gain use finish order information
				TradeVo tradeVo = new TradeDaoImpl().getNotifyTradeDetail(getUserId(req));
				if(tradeVo != null){
					//返回使用完成的订单信息/return 
					setData(resp, new BikeBo().getBikeOrderInfo(tradeVo),2,3);
					//修改订单状态为已通知/modify order status as notified
					new TradeDaoImpl().updateNotifySuccess(getUserId(req));
				}else{
					setCode(resp, HTTP_RESULT_OK);
				}
			}else if(result == -1){
				setCode(resp, HTTP_RESULT_BIKEUSE_IS_OUT_AREA);
			}else if(result == -2){
				setCode(resp, HTTP_RESULT_BIKEUSE_IS_IN_FORCED_PARKING_AREA);
			}else{
				System.out.println("finish order fail");
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			}

			//save bike ble record
			new BikeOrderRecordDaoImpl().insert(ValueUtil.getLong(bikeUseVo.getBikeVo().getImei()), BikeOrderRecord.ORDER_LOCK, TimeUtil.getCurrentLongTime(),
					"BLE LOCK,End ride ,result:"+result+",time:"+date+",uid:"+userId+"runTime:"+runTime);

		}else{
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			//修改锁状态为关闭/odify lock status as turned off
			bikeDao.updateLockStatusWithImei(ValueUtil.getLong(bikeUseVo.getBikeVo().getImei()), 0,false);
			//save bike ble record
			new BikeOrderRecordDaoImpl().insert(ValueUtil.getLong(bikeUseVo.getBikeVo().getImei()), BikeOrderRecord.ORDER_LOCK, TimeUtil.getCurrentLongTime(),
					"BLE LOCK,End ride fail,bike not in use ,time:"+date+",uid:"+userId+"runTime:"+runTime);
			return;
		}
	}

	/**
	 * 30019
	 * @Title:        fundBike 
	 * @Description:  寻车铃/find bike alarm
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月8日 下午6:17:46
	 */
	protected void fundBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"bikeId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String bikeId = req.getParameter(parms[0]);

		BikeVo bikeVo = new BikeDaoImpl().getBikePriceInfo(bikeId);
		if(bikeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		byte[] order = CommandUtil.getFindBikeCommand(Command.CODE_OM,  bikeVo.getImei(), 3, 1);
		boolean flag = TCPService.sendOrder(ValueUtil.getLong(bikeVo.getImei()), order)== 0?false:true;
		setFlagData(resp, flag);
	}

	/**
	 * 30024
	 * @Title:        getBikePriceInfo 
	 * @Description:  获取单车价格信息/obtain bike 
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月28日 下午4:51:58
	 */
	protected void getBikePriceInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bikeId = req.getParameter("bikeId");
		BikeTypeVo typeVo = null;
		if(!StringUtils.isEmpty(bikeId)){
			BikeVo bikeVo = new BikeDaoImpl().getBikePriceInfo(bikeId);
			if(bikeVo == null){
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				return;
			}
			typeVo = new BikeTypeDaoImpl().getTypeInfo(bikeVo.getTypeId());
		}else{
			typeVo = new BikeTypeDaoImpl().getTypeInfo("1");
		}
		if(typeVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		setData(resp, typeVo);
	}

	/**
	 * 30021
	 * @Title:        getErrorReportList 
	 * @Description:  用户故障上报列表 / get user report list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月16日 上午11:33:36
	 */
	public void getErrorReportList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		setData(resp, new BikeErrorDaoImpl().getBikeErrorList(getUserId(req), getStartPage(req)));
	}

	/**
	 * 30022
	 * get
	 * @Title:        getErrorReportDetail 
	 * @Description:  故障详情/Error Report detail
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月16日 上午11:46:22
	 */
	public void getErrorReportDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeErrorVo errorVo = new BikeErrorDaoImpl().getBikeError(req.getParameter(parms[0]));
		if(errorVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		setData(resp, errorVo);
	}

	/**
	 * 30024
	 * @Title:        openBikeFail 
	 * @Description:  开锁失败/open bike fail
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年4月3日 下午5:32:41
	 */
	protected void openBikeFail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String[] parms = new String[]{"date"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		long date = ValueUtil.getLong(req.getParameter(parms[0]));
		setFlagData(resp, new BikeUseDaoImpl().deleteBikeUse(date,ValueUtil.getInt(getUserId(req)),true));
		return;
	}

	/**
	 * 30029
	 * @Title:        updateUserRideProcess 
	 * @Description:  更新用户所有骑行路径
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年6月15日 下午2:34:34
	 */
	protected void updateUserRideProcess(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"lat","lng"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		double currentLat = ValueUtil.getDouble(req.getParameter(parms[0]));
		double currentLng = ValueUtil.getDouble(req.getParameter(parms[1]));

		if( currentLat == 0 || currentLng == 0){
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return;
		}


		IBikeUseDao bikeUseDao = new BikeUseDaoImpl();
		String userId = getUserId(req);



		if(bikeUseDao.getRideIngCountWithUser(userId, null,false) == 0){
			//获取使用完成的订单信息/obtain use finish transaction information
			TradeVo tradeVo = new TradeDaoImpl().getNotifyTradeDetail(getUserId(req));
			if(tradeVo != null){
				//返回使用完成的订单信息/return use finish order information
				setData(resp, new BikeBo().getBikeOrderInfo(tradeVo),2,2);
				//修改订单状态为已通知/modify transaction status as noticed
				new TradeDaoImpl().updateNotifySuccess(getUserId(req));

				System.out.println("正常返回结束订单");
				return;
			}else{
				setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
				System.out.println("异常返回结束订单");
				return;
			}
		}

		//获取使用信息(正在使用中)/obtain use information(using)
		List<BikeUseVo> useList = bikeUseDao.getRideListWithUser(userId, 0);

		for (BikeUseVo useVo : useList) {
			BikeVo bikeVo = useVo.getBikeVo();
			if(useVo.getEndLat() == currentLat && useVo.getEndLng() == currentLng){
				//同一位置，直接返回使用信息/same position, direct rturn use information
				useVo = new BikeBo().getUseingBikeInfo(useVo, bikeVo);
				//更新时间
				bikeUseDao.updateUseBikeUpdateTime(useVo.getId(),TimeUtil.getCurrentLongTime());
			}else{
				useVo.setCurrentLat(currentLat);
				useVo.setCurrentLng(currentLng);

				if(ValueUtil.getInt(req.getParameter("errorLocation ")) == 0){
					//计算骑行距离/caculate riding distance
					useVo.setDistanceWithStartAndEnd();
					//生成路径/generate route
					useVo.setOrbitWithPoint(currentLat, currentLng);
				}

				//骑行越界判断/riding transboundary judge
				BikeBo bikeBo = new BikeBo();
				LatLng postion = new LatLng(currentLat, currentLng);
				if(bikeBo.checkBikeInProhibitedParkingArea(bikeVo.getCityId(), postion)){
					useVo.setOut_area(2);//in Prohibited parking
				}else if(!bikeBo.checkBikeIsInCityArea(bikeVo.getCityId(), postion)){
					useVo.setOut_area(1);//out of area
				}else{

					if(bikeVo.getStatus() == 0 && useVo.getOut_area() > 0 && bikeBo.checkBikeIsInCityArea(bikeVo.getCityId(), postion)){
						//关锁越界中的订单，自动结束
						useVo.setBikeVo(bikeVo);
						new BikeBo().dealBikeOrder(useVo, true);
					}
					useVo.setOut_area(0);//normal

				}

				if(useVo.getRideStatus() == 1){
					useVo.setDuration(ValueUtil.getInt(TimeUtil.getCurrentLongTime()-ValueUtil.getLong(useVo.getStartTime())));
				}

				boolean flag = bikeUseDao.updateUseBikeProcess(useVo,false);
			}

		}

		setData(resp, useList,1,2);
	}

	/**
	 * 30030
	 * @Title:        lockRide 
	 * @Description:  骑行关锁/lock for ride
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月29日 下午6:56:12
	 */
	protected void lockRide(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"date"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		long date = ValueUtil.getLong(req.getParameter(parms[0]));
		int userId = ValueUtil.getInt(getUserId(req));
		BikeUseVo bikeUseVo =  new BikeUseDaoImpl().getUseIngDetailInfo(userId,date);
		if(bikeUseVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		boolean flag = LockOrderBo.sendLockOrder(bikeUseVo.getBikeVo().getImei(), userId,date);
		setFlagData(resp, flag);
	}
	
	/**
	 * 30031
	 * @Title:        updateRideInfo 
	 * @Description:  update ride info
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年10月27日 下午2:56:42
	 */
	protected void updateRideInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"rideId","outArea","orbit","lat","lng","distance"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IBikeUseDao bikeUseDao = new BikeUseDaoImpl();
		if(bikeUseDao.getRideIngCountWithUser(getUserId(req), null,false) == 0){
			//获取使用完成的订单信息/obtain use finish transaction information
			TradeVo tradeVo = new TradeDaoImpl().getNotifyTradeDetail(getUserId(req));
			if(tradeVo != null){
				//返回使用完成的订单信息/return use finish order information
				setData(resp, new BikeBo().getBikeOrderInfo(tradeVo),2,2);
				//修改订单状态为已通知/modify transaction status as noticed
				new TradeDaoImpl().updateNotifySuccess(getUserId(req));

//				System.out.println("正常返回结束订单");
				return;
			}
		}
		BikeUseVo useVo = new BikeUseVo();
		useVo.setId(ValueUtil.getString(req.getParameter(parms[0])));
		useVo.setOut_area(ValueUtil.getInt(req.getParameter(parms[1])));
		useVo.setOrbit(ValueUtil.getString(req.getParameter(parms[2])));
		useVo.setCurrentLat(ValueUtil.getDouble(req.getParameter(parms[3])));
		useVo.setCurrentLng(ValueUtil.getDouble(req.getParameter(parms[4])));
		useVo.setDistance(ValueUtil.getDouble(req.getParameter(parms[5])));
		setFlagData(resp, bikeUseDao.updateRideInfo(useVo));
	}
}
