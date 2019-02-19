/**
 * FileName:     LoginServlet.java
 * @Description: 对外登录注册相关业务/outside 
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-24 上午10:12:06
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-24       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.bo.EmailBo;
import com.pgt.bikelock.bo.SmsBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.IUserDeviceDao;
import com.pgt.bikelock.dao.impl.CountryDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.dao.impl.UserDeviceDaoImpl;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.PushUtil;
import com.pgt.bikelock.utils.PushUtil.PushType;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.CountryVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserDeviceVo;
import com.pgt.bikelock.vo.UserVo;

/**
 * 接口定义起点:10000/interface definition starpoint
 * 访问前缀 app/login
 * @ClassName:     LoginServlet
 * @Description:登录注册相关接口/log in register related interface
 * @author:    Albert
 * @date:        2017-3-24 上午10:19:54
 *
 */
public class LoginServlet extends BaseServlet {

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
		//super.doGet(req, resp);

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
		case 10001:
			login(req, resp);
			break;
		case 10002:
			loginWithPhoneAutoRegister(req, resp);
			break;
		case 10003:
			thirdUserLogin(req, resp);
			break;
		case 10004:
			loginWithPhone(req, resp);
			break;
		default:
			break;
		}
	}

	/**
	 * 10001
	 * post
	 * @Title:        login 
	 * @Description:  手机号加密码登录/phone number plue password register
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-24 下午4:17:00
	 */
	private void login(HttpServletRequest req, HttpServletResponse resp) {
		String[] parms = new String[]{"phone","password","industryType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		int industryType = ValueUtil.getInt(req.getParameter(parms[2]));
		if(industryType == 0 ){
			setCode(resp,BaseServlet.HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		//请求参数/apply parameter
		String phone = req.getParameter(parms[0]);
		String password = req.getParameter(parms[1]);
		int phoneCode = ValueUtil.getInt(req.getParameter("phoneCode"));
		int countryId = 0;
		if(phoneCode > 0){
			CountryVo countryVo = new CountryDaoImpl().getCountry(phoneCode);
			if(countryVo != null){
				countryId = countryVo.getId();
			}

		}
		IUserDao userDao =  new UserDaoImpl();
		UserVo  user = null;
		if(ValueUtil.getLong(phone) == 0){
			UserDetailVo userDetail = new UserDetailDaoImpl().getUserDetailWithEmail(phone);
			if(userDetail != null){
				user = userDao.getUserWithId(userDetail.getUid(), false);
			}
		}else{
			user = userDao.getUser(phone, password,industryType,countryId);
		}
		//		UserDetailVo detailVo = new UserDetailDaoImpl().getUserDetail(user.getuId());
		if(user!=null){
			if(!checkUserAuthStatus(user, resp)){
				return;
			}
			userDao.updateLoginDate(user.getuId());
			if(req.getParameter("lat") != null || req.getParameter("lng") != null){
				int cityId = UserBo.autoSetUserCity(new LatLng(ValueUtil.getDouble(req.getParameter("lat")),
						ValueUtil.getDouble(req.getParameter("lng"))), user.getuId(), user.getCityId(),false);
				if(cityId > 0){
					user.setCityId(cityId);
				}else{
					user.setCityId(1);
				}
			}
			//返回请求token/return request token
			user.setToken(UserBo.createUserToken(user,false));			
			setData(resp, user);

			int deviceType = ValueUtil.getInt(req.getParameter("deviceType"));
			String deviceToken = req.getParameter("deviceToken");
			String deviceUUID = req.getParameter("deviceUUID");

			saveDeviceInfo(req, user,deviceType,deviceToken,deviceUUID);
		
		}else{
			// 登录失败/log in failure
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}

	}


	/**
	 * 10002
	 * post
	 * @Title:        loginWithPhoneAutoRegister 
	 * @Description:  手机号直接登录（自动注册）/phone number direct register(auto register)
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-24 下午4:20:12
	 */
	private void loginWithPhoneAutoRegister(HttpServletRequest req, HttpServletResponse resp) {
		String[] parms = new String[]{"phone","industryType","code"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int industryType = ValueUtil.getInt(req.getParameter(parms[1]));
		if(industryType == 0){
			setCode(resp,BaseServlet.HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		//请求参数/apply parameter
		String phone = req.getParameter(parms[0]);
		String code = req.getParameter(parms[2]);
		int codeType = ValueUtil.getLong(phone)==0 ? 2:1;
		//校验短信验证码/verify short message code
		if(ValueUtil.getLong(phone) == 0  && !new EmailBo().checkEmailCode(code, phone)){
			setCode(resp, HTTP_RESULT_SMS_CODE_ERROR);
			return;
		}else if(ValueUtil.getLong(phone) > 0  && !new SmsBo().checkSmsCode(code, phone)){
			setCode(resp, HTTP_RESULT_SMS_CODE_ERROR);
			return;
		}


		IUserDao userDao =  new UserDaoImpl();
		UserVo  user = userDao.getUser(phone,industryType);
		String userId = null;
		if(user != null){
			if(!checkUserAuthStatus(user, resp)){
				return;
			}
			userDao.updateLoginDate(user.getuId());
			//返回请求token/reply request token
			user.setToken(UserBo.createUserToken(user,false));
			setData(resp, user);

			int deviceType = ValueUtil.getInt(req.getParameter("deviceType"));
			String deviceToken = req.getParameter("deviceToken");
			String deviceUUID = req.getParameter("deviceUUID");

			saveDeviceInfo(req, user,deviceType,deviceToken,deviceUUID);

			userId = user.getuId();
		}else{
			//账号不存在，自动注册/account not exist, auto register
			UserVo newUser =  new UserVo();
			if(codeType != 2){
				newUser.setPhone(phone);
			}
			
			newUser.setIndustryId(req.getParameter("industryType"));
			int phoneCode = ValueUtil.getInt(req.getParameter("phoneCode"));
			if(phoneCode > 0){
				CountryVo countryVo = new CountryDaoImpl().getCountry(phoneCode);
				if(countryVo != null){
					newUser.setCountry_id(countryVo.getId());
				}

			}
			//小程序设置为实名认证
			if("smallprogram".equals(req.getParameter("requestFrom"))){
				newUser.setAuthStatus(2);
			}
			userId = userDao.addUser(newUser);
			if(userId != null){
				Map<String, String> jData = new HashMap<String, String>();
				// 登录成功/regsister succcess
				jData.put("uId", userId+"");
				jData.put("isRegister", "1");//标记为新注册用户/sign as new register user
				//返回请求token/reply request token
				UserVo userVo = new UserVo();
				userVo.setuId(userId);
				userVo.setIndustryId(newUser.getIndustryId());
				if(req.getParameter("lat") != null || req.getParameter("lng") != null){
					int cityId = UserBo.autoSetUserCity(new LatLng(ValueUtil.getDouble(req.getParameter("lat")),
							ValueUtil.getDouble(req.getParameter("lng"))), userId, newUser.getCityId(),false);
					if(cityId > 0){
						userVo.setCityId(cityId);
					}else{
						userVo.setCityId(1);
					}
				}
				String token = UserBo.createUserToken(userVo,true);
				jData.put("token",token );
				setData(resp, jData);
				userVo.setToken(token);
				
				int deviceType = ValueUtil.getInt(req.getParameter("deviceType"));
				String deviceToken = req.getParameter("deviceToken");
				String deviceUUID = req.getParameter("deviceUUID");

				newUser.setuId(userId);
				saveDeviceInfo(req, newUser,deviceType,deviceToken,deviceUUID);

				
				
				CouponBo.crateRegisterAutoSendCoupon(userId, 0);

				if(codeType == 2){
					//修改用户邮箱信息
					EmailBo.userEmailAuth(userId, phone);
				}
			}else{
				// 注册失败/register failure
				setCode(resp, ERROR_EXCEPTION);
			}
		}

		//第三方授权关联/third party authority connect
		if(ValueUtil.getInt(req.getParameter("thirdId")) != 0){
			userDao.updateThirdInfo(userId, req.getParameter("thirdId"));
		}

		
	}

	/**
	 * 10003
	 * post
	 * @Title:        thirdUserLogin 
	 * @Description:  第三方平台用户登录（自动注册）/third party platform user register(auto register)
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-24 下午4:33:20
	 */
	private void thirdUserLogin(HttpServletRequest req, HttpServletResponse resp) {
		String[] parms = new String[]{"uuid","type","industryType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int industryType = ValueUtil.getInt(req.getParameter(parms[2]));
		int onlyGetThirdId = ValueUtil.getInt("onlyGetThirdId");

		if(industryType == 0){
			setCode(resp,BaseServlet.HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		//请求参数
		String uuid = req.getParameter(parms[0]);
		int type = Integer.parseInt(req.getParameter(parms[1]));

		if(type == 7){
			uuid = UserBo.getWechatOpenId(uuid);
			if(uuid == ""){
				setCode(resp,BaseServlet.HTTP_RESULT_PARAMETER_MISS);
				return;
			}
		}

		IUserDao userDao =  new UserDaoImpl();
		UserVo  user = userDao.thirdUserLogin(uuid, type);
		if(onlyGetThirdId !=0 && user!=null){
			if(!checkUserAuthStatus(user, resp)){
				return;
			}
			userDao.updateLoginDate(user.getuId());
			//返回请求token
			user.setToken(UserBo.createUserToken(user,false));
			setData(resp, user);

			int deviceType = ValueUtil.getInt(req.getParameter("deviceType"));
			String deviceToken = req.getParameter("deviceToken");
			String deviceUUID = req.getParameter("deviceUUID");

			saveDeviceInfo(req, user,deviceType,deviceToken,deviceUUID);
		}else{
			//账号不存在，自动注册/account not exist, auto register
			String thirdId = userDao.getThirdId(uuid, type);
			if(thirdId == null){
				thirdId = userDao.addThirdInfo(uuid, type, "0");
			}
			if(thirdId != null){
				Map<String, String> jData = new HashMap<String, String>();
				if(industryType == 7){
					//注册用户
					UserVo newUser =  new UserVo();
					newUser.setIndustryId(industryType+"");
					String userId = userDao.addUser(newUser);
					//第三方授权关联/third party authority connect
					if(userId != null){
						userDao.updateThirdInfo(userId,thirdId);
						newUser.setuId(userId);
						String token = UserBo.createUserToken(newUser,true);
						jData.put("token",token );
					}
				}
				
				// 登录成功/regsiter success
				jData.put("thirdId", thirdId);
				jData.put("isRegister", "1");//标记为新注册用户/sign as new register user
				setData(resp, jData);
				
			
			}else{
				// 注册失败/register failure
				setCode(resp, ERROR_EXCEPTION);
			}
		}

	}
	

	/**
	 * 10004
	 * post
	 * @Title:        loginWithPhone 
	 * @Description:  手机号直接登录/phone number direct log in
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017-3-24 下午4:20:12
	 */
	private void loginWithPhone(HttpServletRequest req, HttpServletResponse resp) {
		String[] parms = new String[]{"phone","industryType","code"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int industryType = ValueUtil.getInt(req.getParameter(parms[1]));
		if(industryType == 0){
			setCode(resp,BaseServlet.HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		//请求参数/apply parameter
		String phone = req.getParameter(parms[0]);
		String code = req.getParameter(parms[2]);

		int codeType = ValueUtil.getLong(phone) == 0?2:1;
		//校验短信验证码/verify short message code
		if(codeType == 2 && !new EmailBo().checkEmailCode(code, phone)){
			setCode(resp, HTTP_RESULT_SMS_CODE_ERROR);
			return;
		}else if(codeType == 1 && !new SmsBo().checkSmsCode(code, phone)){
			setCode(resp, HTTP_RESULT_SMS_CODE_ERROR);
			return;
		}
		UserVo  user = null;
		if(codeType == 2){
			UserDetailVo userDetail = new UserDetailDaoImpl().getUserDetailWithEmail(phone);
			if(userDetail != null){
				user = new UserDaoImpl().getUserWithId(userDetail.getUid(), false);
			}
		}else{
			user = new UserDaoImpl().getUser(phone,industryType);
		}
		if(user != null){
			if(!checkUserAuthStatus(user, resp)){
				return;
			}
			//返回请求token/return request token
			setData(resp, UserBo.createUserToken(user,false));
			
			int deviceType = ValueUtil.getInt(req.getParameter("deviceType"));
			String deviceToken = req.getParameter("deviceToken");
			String deviceUUID = req.getParameter("deviceUUID");

			saveDeviceInfo(req, user,deviceType,deviceToken,deviceUUID);
		}else{
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
		}

	}
	
	/**
	 * 
	 * @Title:        checkUserAuthStatus 
	 * @Description:  验证用户认证状态
	 * @param:        @param user
	 * @param:        @param resp
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年12月27日 上午10:44:47
	 */
	private boolean checkUserAuthStatus(UserVo  user, HttpServletResponse resp){
		if(user.getAuthStatus() == 8){
			setCode(resp, UserServlet.HTTP_RESULT_USER_BE_FROST);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @Title:        saveDeviceInfo 
	 * @Description:  保存用户设备信息/reserve user device information
	 * @param:        @param req
	 * @param:        @param userVo
	 * @param:        @param deviceType
	 * @param:        @param deviceToken
	 * @param:        @param deviceUUID    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月8日 下午4:13:13
	 */
	private void saveDeviceInfo(final HttpServletRequest req,final UserVo userVo,final int deviceType,final String deviceToken,final String deviceUUID){

		new Thread(new Runnable() {
			public void run() {
				if(deviceType == 0 || (deviceToken == null && deviceUUID == null)){
					return;
				}
				UserDeviceVo deviceVo = new UserDeviceVo(userVo.getuId(), deviceType, deviceToken,deviceUUID,userVo.getToken());
				IUserDeviceDao deviceDao  =  new UserDeviceDaoImpl();

				UserDeviceVo oldDeviceVo = deviceDao.getDeviceInfo(userVo.getuId());
				if(oldDeviceVo == null){
					deviceDao.addDevice(deviceVo);
				}else{
					deviceDao.updateDevice(deviceVo);

					if(!StringUtils.isEmpty(oldDeviceVo.getUuid()) && !oldDeviceVo.getUuid().equals(deviceUUID)
							&& !oldDeviceVo.getToken().equals(deviceToken)
							&& !oldDeviceVo.getRequestToken().equals(userVo.getToken())){
						//device changed
						//push notification
						PushUtil.push(oldDeviceVo,LanguageUtil.getDefaultValue("push_others_device_login_title")
								,LanguageUtil.getDefaultValue("push_others_device_login_content"), PushUtil.getPushCustomerDictionary(PushType.Push_Type_Other_Device_Login));
						//set token was other device login
						ParamsFilter.setTokenStatus(oldDeviceVo.getRequestToken(), 2);
					}
				}
			}
		}).start();

	}

}
