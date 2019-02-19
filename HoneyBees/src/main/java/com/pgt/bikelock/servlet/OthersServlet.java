/**
 * FileName:     WebServlet.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 上午10:11:47
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.EmailBo;
import com.pgt.bikelock.bo.SmsBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.dao.impl.CountryDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.IpWarnDaoImpl;
import com.pgt.bikelock.dao.impl.NewsDaoImpl;
import com.pgt.bikelock.dao.impl.SmsDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.dao.impl.WebContentDaoImpl;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.resource.EnvironmentConfig;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.AESUtil;
import com.pgt.bikelock.utils.SecurityUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.ZoneDate;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.CountryVo;
import com.pgt.bikelock.vo.EmailVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.NewsVo;
import com.pgt.bikelock.vo.RequestClientVo;
import com.pgt.bikelock.vo.RequestTokenVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.WebContentVo;

/**
 * 接口定义起点 50000/interface definition startpoint
 * @ClassName:     WebServlet
 * @Description:其他业务类/other business class
 * @author:    Albert
 * @date:        2017年4月11日 上午10:11:47
 *
 */
public class OthersServlet extends BaseServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**状态码 status code**/ 


	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		super.doGet(req, resp);

		int requestType = getRequestType(req);
		if((requestType != 50001 && requestType != 50007) && !checkDevice(req, resp)){
			return;
		}
		if(!doCheckClient(requestType, req, resp)){
			return;
		}
		switch (requestType) {
		case 50001:
			getWebContent(req, resp);
			break;
		case 50002:
			sendSmsCode(req, resp);
			break;
		case 50003:
			getCountryList(req, resp);
			break;
		case 50004:
			getNewsContent(req, resp);
			break;
		case 50005:
			getBikeCityList(req, resp);
			break;
		case 50007:
			emailValidate(req, resp);
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
		int requestType = getRequestType(req);
		if((requestType != 50008 && requestType != 50007) && !checkDevice(req, resp)){
			return;
		}

		if(!doCheckClient(requestType, req, resp)){
			return;
		}
		switch (requestType) {
		case 50002:
			sendSmsCode(req, resp);
			break;
		case 50006:
			checkInvaitCode(req, resp);
			break;
		case 50007:
			emailValidate(req, resp);
			break;
		case 50008:
			sendEmailCode(req, resp);
			break;
		default:
			break;
		}
	}

	private boolean checkDevice(HttpServletRequest req, HttpServletResponse resp){

		if(EnvironmentConfig.DEBUG){
			return true;
		}

		String userAgent = req.getHeader("user-agent");
		if(!ParamsFilter.checkUserAgent(userAgent)){
			System.out.println("error request,client:"+req.getRemoteAddr());
			setCode(resp, ParamsFilter.HTTP_RESULT_DEVICE_ERROR);
			return false;
		}
		return true;
	}

	private boolean doCheckClient(int requestType,HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{

		if(EnvironmentConfig.DEBUG){
			return true;
		}

		if(requestType == 50002){//短信验证通道，60秒1次，一天SMS_COUNT_MAX_IN_DAY次
			return checkClient(req, resp,requestType,RequestClientVo.SMS_REQUEST_INTERVAL,RequestClientVo.OTHERS_REQUEST_MAX_REQUEST_COUNT);
		}else{//其他通道，3秒一次，一天REQUEST_MAX_REQUEST_COUNT次
			return checkClient(req, resp,requestType,RequestClientVo.REQUEST_INTERVAL,RequestClientVo.OTHERS_REQUEST_MAX_REQUEST_COUNT);
		}
	}

	/**
	 * 
	 * @Title:        checkClient 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param phone
	 * @param:        @return
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月7日 下午6:07:28
	 */
	protected boolean checkClient(HttpServletRequest req, HttpServletResponse resp,int requestType,double interval,int maxInDay)
			throws ServletException, IOException {
		String clientIp = req.getRemoteAddr();
		long nowTime = TimeUtil.getCurrentLongTime();
		if(!ParamsFilter.clientMap.containsKey(clientIp)){
			ParamsFilter.clientMap.put(clientIp, new RequestClientVo(clientIp, nowTime,requestType));
			return true;
		}
		RequestClientVo clientVo = ParamsFilter.clientMap.get(clientIp);
		if(clientVo.isStopClient()){
			System.out.println("request warn,ip:"+clientIp);
			return false;
		}
		clientVo.setCount(clientVo.getCount()+1);
		long intervals = nowTime-clientVo.getLastTime();
		System.out.println("intervals:"+intervals);
		boolean flag = false;
		if(clientVo.getRequestType() == requestType && intervals < interval){
			setCode(resp, HTTP_RESULT_QUICK_ERROR);
		}else{
			if(nowTime-clientVo.getFirstTime() < Long.parseLong(86400+"")){//in day	
				if(clientVo.getRequestType() == requestType && clientVo.getCount() > maxInDay){
					if(clientVo.getCount() > RequestClientVo.OTHERS_REQUEST_MAX_REQUEST_COUNT){
						clientVo.setStopClient(true);
					}
					//					new IpWarnDaoImpl().addWarn(clientIp,"0");
				}else{
					flag = true;
					clientVo.setLastTime(nowTime);
					clientVo.setCount(clientVo.getCount()+1);
				}

			}else{//after day
				flag = true;
				clientVo.setLastTime(nowTime);
				clientVo.setCount(0);
			}
		}


		//update client map
		ParamsFilter.clientMap.put(clientIp, clientVo);
		return flag;
	}

	/**
	 * 50001
	 * @Title:        getWebContent 
	 * @Description:  获取用户协议/obtain user protocol
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 上午10:26:03
	 */
	protected void getWebContent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"industryId","type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		WebContentVo webVo = new WebContentDaoImpl().getContent(req.getParameter(parms[0]),
				ValueUtil.getInt(req.getParameter(parms[1])));
		req.setAttribute("webVo",webVo);
		req.getRequestDispatcher("app/webContent.jsp").forward(req, resp);
	}

	/**
	 * 50002
	 * @Title:        sendSmsCode 
	 * @Description:  发送验证码/send password code
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月12日 下午12:32:14
	 */
	protected void sendSmsCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String[] parms = new String[]{"industryId","phone","smsType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String industryId = req.getParameter(parms[0]);
		String phone = req.getParameter(parms[1]);
		if(phone.contains("==") && OthersSource.getSourceString("sms_api_key") != null){
			phone = AESUtil.aesDecrypt(phone, OthersSource.getSourceString("sms_api_key"));
		}
		if(StringUtils.isEmpty(phone)){
			System.out.println("error phone");
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		int smsType = ValueUtil.getInt(req.getParameter(parms[2]));
		UserVo user = new UserDaoImpl().getUser(phone, ValueUtil.getInt(industryId));

		if(smsType == 1 &&  user!= null){//register			
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;			
		}else if(smsType == 2 && user == null){//login for reset password
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;	
		}
		System.out.println(phone);
		if(new SmsDaoImpl().getPhoneCountInDay(phone) >= RequestClientVo.SMS_COUNT_MAX_IN_DAY){
			setCode(resp, HTTP_RESULT_COUNT_MAX_IN_DAY);
			return;
		}
		String phoneCode = req.getParameter("phoneCode");

		if(!StringUtils.isEmpty(phoneCode) && "01".equals(phoneCode)){
			phoneCode = "1";
		}
		boolean flag = new SmsBo().sendSmsCode(industryId, phone,phoneCode);
		setFlagData(resp, flag);
	}




	/**
	 * 50003
	 * @Title:        getCountryList 
	 * @Description:  获取国家列表/obtain nation list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月14日 下午3:33:09
	 */
	protected void getCountryList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<CountryVo>  list= new CountryDaoImpl().getCountryList();
		setData(resp, list);
	}

	/**
	 * 50004
	 * @Title:        getNewsContent 
	 * @Description:  获取资讯活动内容/obtain data activity content
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月10日 下午5:38:45
	 */
	protected void getNewsContent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"newsId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		NewsVo newsVo = new NewsDaoImpl().getNews(req.getParameter(parms[0]));
		req.setAttribute("newsVo",newsVo);
		req.getRequestDispatcher("app/newsContent.jsp").forward(req, resp);
	}

	/**
	 * 50005
	 * @Title:        getBikeCityList 
	 * @Description:  获取单车支持城市/obtain bike support city
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月27日 下午6:01:45
	 */
	protected void getBikeCityList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<CityVo> cityList = new CityDaoImpl().getCityLlist(null);
		setData(resp, cityList);
	}

	/**
	 * 50006
	 * @Title:        checkInvaitCode 
	 * @Description:  验证邀请码/verify invited code
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月28日 下午5:11:35
	 */
	protected void checkInvaitCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"inviteCode"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String userId = new UserDaoImpl().getUserId(req.getParameter(parms[0]));
		if(StringUtils.isEmpty(userId)){
			setData(resp, "0");
		}else{
			setData(resp, "1");
		}
	}



	/**
	 * 
	 * @Title:        emailValidate 
	 * @Description:  50007
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年4月13日 下午5:57:54
	 */
	protected void emailValidate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"token"};
		for (int i = 0; i < parms.length; i++) {
			if(req.getParameter(parms[i]) == null || "".equals(req.getParameter(parms[i]).trim())){
				goToEmailAuthResult(req, resp, "error", false);
				return;
			}
		}

		String tokenValue = SecurityUtil.decrypt(req.getParameter(parms[0]), SecurityUtil.DoubKey);
		JSONObject tokenJson = JSON.parseObject(tokenValue);

		if(tokenJson == null ||tokenJson.get("userId") == null || tokenJson.get("email") == null 
				|| tokenJson.get("tokenExpiredDate") == null|| tokenJson.get("timeStamp") == null ){
			goToEmailAuthResult(req, resp, "error", false);
			return;
		}

		DateFormat df = new SimpleDateFormat(UserBo.DataFormate);
		try {
			Date dtEnd = df.parse(tokenJson.getString("tokenExpiredDate"));
			Date dtNow = new ZoneDate();
			if (dtEnd.getTime() < dtNow.getTime()) {
				goToEmailAuthResult(req, resp, "token expire", false);
				return;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		String userId = tokenJson.getString("userId");
		String email = tokenJson.getString("email");
		System.out.println("userId:"+userId+";email:"+email);
		boolean flag = EmailBo.userEmailAuth(userId, email);
		//		setFlagData(resp, flag);
		String tips = "Sorry, the verification email failed!";
		if(flag){
			tips = "Congratulations, you have successfully verified your email!";
		}
		goToEmailAuthResult(req, resp, tips, true);
	}

	private void goToEmailAuthResult(HttpServletRequest req, HttpServletResponse resp,String tips,boolean flag) throws ServletException, IOException{
		req.setAttribute("tips", tips);
		req.setAttribute("flag", flag);
		req.getRequestDispatcher("email_auth_result.jsp").forward(req, resp);
	}

	/**
	 * 50008
	 * @Title:        sendEmailCode 
	 * @Description:  发送邮箱验证码
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年9月1日 下午2:48:13
	 */
	protected void sendEmailCode(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String[] parms = new String[]{"industryId","email","emailType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String industryId = req.getParameter(parms[0]);
		String email = req.getParameter(parms[1]);
		if(OthersSource.getSourceString("sms_api_key") != null){
			email = AESUtil.aesDecrypt(email, OthersSource.getSourceString("sms_api_key"));
		}
		if(StringUtils.isEmpty(email)){
			System.out.println("error email");
			setCode(resp, HTTP_RESULT_PARAMETER_MISS);
			return;
		}
		int emailType = ValueUtil.getInt(req.getParameter(parms[2]));
		UserDetailVo user = new UserDetailDaoImpl().getUserDetailWithEmail(email);

		if(emailType == 1 &&  user!= null){//register			
			setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
			return;			
		}else if(emailType == 2 && user == null){//login for reset password
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;	
		}
		setFlagData(resp, new EmailBo().sendCodeEmail(industryId, email));
	}
}
