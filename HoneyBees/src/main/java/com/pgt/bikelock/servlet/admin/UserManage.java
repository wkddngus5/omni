/**
 * FileName:     UserManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月8日 上午10:46:15
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月8日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.NotificationBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.IDepositDao;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.IUserDetailDao;
import com.pgt.bikelock.dao.IUserInvaiteDao;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.dao.impl.CashRecordDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.dao.impl.UserDeviceDaoImpl;
import com.pgt.bikelock.dao.impl.UserInvaiteDaoImpl;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.InvaiteVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserDetailVo;
import com.pgt.bikelock.vo.UserDeviceVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;


/** 接口定义起点 30000/interface definition startpoint
 * @ClassName:     UserManage
 * @Description:用户管理相关业务接口/user manage related business interface
 * @author:    Albert
 * @date:        2017年4月8日 上午10:46:15
 *
 */
public class UserManage extends BaseManage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] authStatusType;//认证状态/certifaction

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);

		setModelParams("user", "user_list");
		switch (getRequestType(req)) {
		case 30001:
			getUserList(req, resp);
			break;
		case 30002:
			getUserListLookup(req, resp);
			break;
		case 30003:
			toCashUserMoney(req, resp);
			break;
		case 30004:
			getStatisticsList(req, resp);
			break;
		case 30005:
			toUpdaetUserStatus(req, resp);
			break;
		case 30006:
			showUserDetail(req, resp);
			break;
		case 30007:
			toUpdateUserInfo(req, resp);
			break;
		case 30008:
			getUserInviteList(req, resp);
			break;
		case 30009:
			toAddUser(req, resp);
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
		super.doPost(req, resp);
		setModelParams(getCurrentLangValue("user_title"), "user_list");
		switch (getRequestType(req)) {
		case 30001:
			getUserList(req, resp);
			break;
		case 30002:
			getUserListLookup(req, resp);
			break;
		case 30003:
			cashUserMoney(req, resp);
			break;
		case 30005:
			updateUserStatus(req, resp);
			break;
		case 30007:
			updateUserInfo(req, resp);
			break;
		case 30008:
			getUserInviteList(req, resp);
			break;
		case 30009:
			addUser(req, resp);
			break;
		}
	}

	/**
	 * 30001
	 * @Title:        getUserList 
	 * @Description:  获取用户列表/obtain user list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 上午11:21:25
	 */
	protected void getUserList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = doGetUserList(req, resp);

		returnDataList(req, resp, requestVo,"user/userList.jsp");

	}

	/**
	 * 30002
	 * @Title:        getUserListLookup 
	 * @Description:  查询用户并返回/check user and return
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午2:58:23
	 */
	protected void getUserListLookup(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = doGetUserList(req, resp);
		boolean only = false;
		if(req.getParameter("only") != null){
			only = true;
		}
		req.setAttribute("only", only);
		returnDataList(req, resp, requestVo,"user/userListLookup.jsp");
	}

	protected RequestListVo doGetUserList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IUserDao userDao = new UserDaoImpl();
		List<UserDetailVo> userList = userDao.getUserList(requestVo);

		if(req.getParameter("export") != null){//导出/export

			if(authStatusType == null){
				authStatusType =  getCurrentLangValue("user_auth_status_value").split(",");
			}
			setModelParams(getCurrentLangValue("user_manage_list"), "user_list");
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("common_name_title"),getCurrentLangValue("user_phone_title"),
					getCurrentLangValue("user_email_title"),getCurrentLangValue("user_idcard_title"),
					getCurrentLangValue("user_money_title"),getCurrentLangValue("user_auth_status_title"),
					getCurrentLangValue("user_login_date_title"),getCurrentLangValue("user_register_date_title")}
			,userList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<UserDetailVo> userList = (List<UserDetailVo>)list;
					for (int i = 0; i < userList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						UserDetailVo user = userList.get(i);  
						//创建单元格，并设置值/creat unit excel, and set up value 
						row.createCell( 0).setCellValue(user.getUid());  
						row.createCell(1).setCellValue(user.getUserVo().getNickName());  
						row.createCell(2).setCellValue(user.getUserVo().getPhone());  
						row.createCell(3).setCellValue(user.getEmail());  
						row.createCell(4).setCellValue(user.getIdcard());  
						row.createCell(5).setCellValue(user.getUserVo().getMoney()+"");
						if(user.getUserVo().getAuthStatus() <= authStatusType.length){
							row.createCell(6).setCellValue(authStatusType[user.getUserVo().getAuthStatus()+1]);  
						}

						row.createCell(7).setCellValue(user.getUserVo().getLogin_date());  
						row.createCell(8).setCellValue(user.getUserVo().getRegister_date());  
					}  
				}
			});

			return null;
		}else{
			req.setAttribute("userList",userList);
			requestVo.setTotalCount(userDao.getCount(requestVo));

			return requestVo;
		}

	}

	/**
	 * 30003
	 * get
	 * @Title:        toCashUserMoney 
	 * @Description:  加载用户余额提现页/loadiing user balance amount cash page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月23日 下午2:55:05
	 */
	protected void toCashUserMoney(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		UserVo userVo = new UserDaoImpl().getUserWithId(req.getParameter(parms[0]),true);
		req.setAttribute("userVo",userVo);
		req.getRequestDispatcher("user/cashMoneyDialog.jsp").forward(req, resp);
	}

	/**
	 * 30003
	 * post
	 * @Title:        cashUserMoney 
	 * @Description:  用户余额提现/user balance in cash
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月23日 下午2:55:50
	 */
	protected void cashUserMoney(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		
		String[] parms = new String[]{"userId","amount"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		CashRecordVo recordVo = new CashRecordVo(parms, req);
		UserVo userVo = new UserDaoImpl().getUserWithId(recordVo.getUid(),true);
		
		if(recordVo.getAmount().compareTo(userVo.getMoney()) > 0){
			returnDataJustTip(resp, false, getCurrentLangValue("user_balance_refund_not_enough"));
			return;
		}
		
		String recordId = new CashRecordDaoImpl().addAccountCash(recordVo);
		boolean flag = false;
		if(!StringUtils.isEmpty(recordId)){
			flag = true;
			userVo.setMoney(userVo.getMoney().subtract(recordVo.getAmount()));
//			flag = new UserDaoImpl().updateMoney(userVo.getMoney(), userVo.getuId());
			UserBo.balanceRefund(recordVo.getUid(),recordVo.getAmount(),recordId);
			//添加通知/add inform
			NotificationBo.addNotifiyMessage(flag,getAdminId(req, resp) ,recordVo.getUid(), LanguageUtil.getDefaultValue("user_cash_money_title"),
					LanguageUtil.getDefaultValue("user_cash_money_notify_content", 
							new Object[]{recordVo.getAmount(),userVo.getMoney()}));
		}
		setModelParams(getCurrentLangValue("user_cash_money_title"), "user_list");
		returnData(resp, DelType.DelType_Add, flag);
	}

	/**
	 * 30004
	 * @Title:        getStatisticsList 
	 * @Description:  报表统计/report statistics
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 上午10:16:16
	 */
	private void getStatisticsList(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		int type = ValueUtil.getInt(req.getParameter("type"));
		int dateType = ValueUtil.getInt(req.getParameter("dateType"));
		String date = ValueUtil.getString(req.getParameter("date"));
		List<StatisticsVo> dataList = null,statusDataList = null;
		dataList = BaseDao.getStatisticsList(IUserDao.TABLE_NAME,"register_date",dateType,date);
		if(dateType == 0){
			//第一页时，获取用户状态统计/first page, get user status statistics
			//statusDataList = new UserDaoImpl().getStatusStatisticsList();
		}

		if(dateType > 0){
			JSONObject dataMap= new JSONObject();
			dataMap.put("dataList", dataList);
			returnAjaxData(resp, dataMap);
		}else{
			req.setAttribute("dataList", dataList);
			//req.setAttribute("statusDataList", statusDataList);
			req.setAttribute("type", req.getParameter("type"));
			req.getRequestDispatcher("user/statisticsView.jsp").forward(req, resp);
		}

	}

	/**
	 * 30005
	 * get
	 * @Title:        toUpdaetUserStatus 
	 * @Description:  加载修改用户状态页/load modify user status page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月28日 下午5:47:00
	 */
	protected void toUpdaetUserStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		UserVo userVo = new UserDaoImpl().getUserWithId(req.getParameter(parms[0]),true);
		req.setAttribute("userVo",userVo);
		req.setAttribute("status",userVo.getAuthStatus());
		req.getRequestDispatcher("user/updateStatusDialog.jsp").forward(req, resp);
		

	}

	/**
	 * 30005
	 * post
	 * @Title:        updateUserStatus 
	 * @Description:  修改用户状态页/modify user status page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月28日 下午5:49:17
	 */
	protected void updateUserStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id","status"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String userId = req.getParameter(parms[0]);
		boolean flag = new UserDaoImpl().updateAuthStatus(userId,
				ValueUtil.getInt(req.getParameter(parms[1])));
		returnData(resp, DelType.DelType_Update, flag);
		
		updateUserAuthStatus(userId);
	}
	
	/**
	 * 
	 * @Title:        updateUserAuthStatus 
	 * @Description:  修改用户认证状态/update user auth status
	 * @param:        @param userId    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月27日 上午11:02:07
	 */
	private void updateUserAuthStatus(String userId){
		UserVo userVo = new UserDaoImpl().getUserWithId(userId,false);
		if(userVo.getAuthStatus() == 8){
			UserDeviceVo deviceVo = new UserDeviceDaoImpl().getDeviceInfo(userId);
			if(deviceVo != null && !StringUtils.isEmpty(deviceVo.getRequestToken())){
				//set token was stop
				ParamsFilter.setTokenStatus(deviceVo.getRequestToken(), 3);
			}
		}
	}

	/**
	 * 30006
	 * @Title:        toShowUserDetail 
	 * @Description:  显示用户详情/modify user details
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月29日 下午3:02:29
	 */
	protected void showUserDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String userId = req.getParameter(parms[0]);
		UserVo userVo = new UserDaoImpl().getUserWithId(userId,true);
		userVo.setDetailVo(new UserDetailDaoImpl().getUserDetail(req.getParameter(parms[0])));
		req.setAttribute("userVo",userVo);
		BikeUseVo useVo = new BikeUseDaoImpl().getTotalUseInfo(userId);
		if(OthersSource.getSourceString("distance_show_miles") != null){
			useVo.setDistance(useVo.getDistance()*0.6213712);
			req.setAttribute("distanceUnit", getCurrentLangValue("user_riding_info_distance", new Object[]{"miles"}));
		}else{
			req.setAttribute("distanceUnit", getCurrentLangValue("user_riding_info_distance", new Object[]{"km"}));
		}
		req.setAttribute("useInfo", useVo);
		req.setAttribute("distance", String.format("%.2f", useVo.getDistance()));
		req.getRequestDispatcher("user/userDetailDialog.jsp").forward(req, resp);
	}

	/**
	 * 30007
	 * get
	 * @Title:        toUpdateUserInfo 
	 * @Description:  加载修改用户资料页/loading modify user data page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月31日 下午2:56:26
	 */
	protected void toUpdateUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		UserVo userVo = new UserDaoImpl().getUserWithId(req.getParameter(parms[0]),false);
		userVo.setDetailVo(new UserDetailDaoImpl().getUserDetail(req.getParameter(parms[0])));
		req.setAttribute("userVo",userVo);
		req.setAttribute("tagCityId",userVo.getCityId());
		req.getRequestDispatcher("user/updateUserDialog.jsp").forward(req, resp);
	}

	/**
	 * 30007
	 * post
	 * @Title:        updateUserInfo 
	 * @Description:  修改用户资料/modify user data
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月31日 下午2:57:02
	 */
	protected void updateUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		UserVo userVo = new UserVo(req);
		userVo.setuId(req.getParameter(parms[0]));
		boolean flag = new UserDaoImpl().updateUserAllInfo(userVo);
		//详情信息/detail data
		IUserDetailDao detailDao = new UserDetailDaoImpl();
		UserDetailVo userDetail = detailDao.getUserDetail(userVo.getuId());
		userDetail.setUserDetailVo(req);
		flag = detailDao.updateUserDetail(userDetail);
		returnAjaxData(resp, getReturnMessage(DelType.DelType_Update, flag));
		
		updateUserAuthStatus(userVo.getuId());
	}

	/**
	 * 30008
	 * @Title:        getUserInviteList 
	 * @Description:  获取用户邀请列表/get User Invite List
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月7日 下午9:10:12
	 */
	protected void getUserInviteList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IUserInvaiteDao inviteDao = new UserInvaiteDaoImpl();
		List<InvaiteVo> inviteList = inviteDao.getInvaiteList(requestVo);

		if(req.getParameter("export") != null){//导出/export

			setModelParams(getCurrentLangValue("user_invite_list"), " invite_list");
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("user_phone_title"),getCurrentLangValue("invite_user_phone"),
					getCurrentLangValue("common_date_title")}
			,inviteList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<InvaiteVo> inviteList = (List<InvaiteVo>)list;
					for (int i = 0; i < inviteList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						InvaiteVo invite = inviteList.get(i);  
						//创建单元格，并设置值/creat unit excel, and set up value 
						row.createCell( 0).setCellValue(invite.getId());  
						row.createCell(1).setCellValue(invite.getPhone());  
						row.createCell(2).setCellValue(invite.getIphone());  
						row.createCell(3).setCellValue(invite.getDate());  
					}  
				}
			});

		}else{
			req.setAttribute("inviteList",inviteList);
			requestVo.setTotalCount(inviteDao.getInvaiteCount(requestVo));
			returnDataList(req, resp, requestVo,"user/userInviteList.jsp");
		}

	}
	
	/**
	 * 30009
	 * get
	 * @Title:        addUser 
	 * @Description:  添加用户/add user
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月6日 上午10:02:09
	 */
	protected void toAddUser(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("user/addUserDialog.jsp").forward(req, resp);
	}
	
	/**
	 * 30009
	 * post
	 * @Title:        addUser 
	 * @Description:  添加用户/add user
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月6日 上午10:02:09
	 */
	protected void addUser(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		
		String[] parms = new String[]{"phone","password"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		String phone = req.getParameter(parms[0]);
		IUserDao userDao = new UserDaoImpl();
		UserVo userVo = userDao.getUser(phone, getIndustryId(req, resp));
		if(userVo != null){
			//账号已存在/account already exist
			returnFail(resp, getCurrentLangValue("consume_deposit_city_exist_title"));
			return;
		}
		UserVo newUser =  new UserVo();
		newUser.setPhone(phone);
		newUser.setIndustryId(getIndustryId(req, resp)+"");
		newUser.setPassword(req.getParameter(parms[1]));
		String userId = userDao.addUser(newUser);
		returnData(resp, DelType.DelType_Add, userId!=null?true:false);
	}
}
