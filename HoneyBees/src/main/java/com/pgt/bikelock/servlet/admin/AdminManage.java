/**
 * FileName:     AdminManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月19日 下午9:11:25
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月19日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.alipay.api.internal.util.StringUtils;
import com.omni.purview.dao.impl.FunctionDaoImpl;
import com.omni.purview.dao.impl.UserGroupDaoImpl;
import com.omni.purview.vo.FunctionVo;
import com.pgt.bikelock.dao.IAdminDao;
import com.pgt.bikelock.dao.impl.AdminDaoImpl;
import com.pgt.bikelock.dao.impl.AdminLogDaoImpl;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.filter.AdminLoginFilter;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.AdminLogVo;
import com.pgt.bikelock.vo.admin.AdminVo;


/**
 * @ClassName:     AdminManage
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月19日 下午9:11:25
 *
 */
public class AdminManage extends BaseManage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		setModelParams(LanguageUtil.getDefaultValue("system_config_manager_list"), "admin_list");
		switch (getRequestType(req)) {
		case 10003:
			getAdminList(req, resp);
			break;
		case 10008:
			getAdminListLoockUp(req, resp);
			break;
		case 10004:
			toEditUserInfo(req, resp);
			break;
		case 10006:
			getAdminLogList(req, resp);
			break;
		case 10007:
			changeLanguage(req, resp);
			break;
		default:
			break;
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
		setModelParams(LanguageUtil.getDefaultValue("system_config_manager_list"), "admin_list");
		switch (getRequestType(req)) {
		case 10001:
			updatePassword(req, resp);
			break;
		case 10002:
			updateNickname(req, resp);
			break;
		case 10003:
			getAdminList(req, resp);
			break;
		case 10008:
			getAdminListLoockUp(req, resp);
			break;
		case 10004:
			editUserInfo(req, resp);
			break;
		case 10005:
			deleteAdmin(req, resp);
			break;
		case 10006:
			getAdminLogList(req, resp);
			break;

		}

	}

	/**
	 * 10001
	 * @Title:        updatePassword 
	 * @Description:  修改密码/modify password
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月18日 下午10:07:43
	 */
	protected void updatePassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"oldPassword","newPassword","confirmPassword"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		if(!req.getParameter(parms[1]).equals(req.getParameter(parms[2]))){
			returnFail(resp, getCurrentLangValue("confirm_password_error"));
			return;
		}
		String adminId = req.getSession().getAttribute("adminId").toString();
		boolean flag = new AdminDaoImpl().updatePassword(adminId, MD5Utils.getMD5(req.getParameter(parms[0])),  
				MD5Utils.getMD5(req.getParameter(parms[1])));
		setModelParams(getCurrentLangValue("login_password"), null);
		returnData(resp, DelType.DelType_Update, flag);
	}

	/**
	 * 10002
	 * @Title:        updateNickname 
	 * @Description:  修改昵称/modify nickname
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月18日 下午10:07:58
	 */
	protected void updateNickname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"nickname"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String adminId = req.getSession().getAttribute("adminId").toString();
		boolean flag = new AdminDaoImpl().updateNickname(adminId, req.getParameter(parms[0]));
		if(flag){
			HttpSession session = req.getSession();
			session.setAttribute("adminNickName", req.getParameter(parms[0]));
		}
		setModelParams(getCurrentLangValue("nickname_title"), null);
		returnData(resp, DelType.DelType_Update, flag);
	}

	/**
	 * 10003
	 * @Title:        getAdminList 
	 * @Description:  管理员列表/administrator list
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月4日 上午12:10:29
	 */
	private void getAdminList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		RequestListVo requestVo = new RequestListVo(request, false, true);
		List<AdminVo> adminList = new AdminDaoImpl().getAdminList(requestVo);

		if(request.getParameter("export") != null){//导出/export
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("login_username"),getCurrentLangValue("nickname_title")},
					adminList,request,response,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<AdminVo> adminList = (List<AdminVo>)list;
					for (int i = 0; i < adminList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						AdminVo adminVo = adminList.get(i);  
						//创建单元格，并设置值/create cell, and set up value  
						row.createCell( 0).setCellValue(adminVo.getId());  
						row.createCell(1).setCellValue(adminVo.getUsername());  
						row.createCell(2).setCellValue(adminVo.getNickname());  
					}  
				}
			});

		}else{
			request.setAttribute("adminList",adminList);

			returnDataList(request, response, null,"purview/adminList.jsp");

		}

	}
	
	/**
	 * 10008
	 * @Title:        getAdminListLoockUp 
	 * @Description:  获取管理员列表并返回/obtain administrator list and back
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月28日 上午9:46:44
	 */
	private void getAdminListLoockUp(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		RequestListVo requestVo = new RequestListVo(request, false, true);
		List<AdminVo> adminList = new AdminDaoImpl().getAdminList(requestVo);
		request.setAttribute("adminList",adminList);
		returnDataList(request, response, null,"purview/adminListLookup.jsp");
	}

	/**
	 * 10004
	 * get
	 * @Title:        toEditUserInfo 
	 * @Description:  加载 新增/编辑管理员资料 页/upload add/edit administrator data
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月5日 下午5:52:11
	 */
	private void toEditUserInfo(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{

		if(request.getParameter("id") != null){
			int adminId = Integer.parseInt(request.getParameter("id"));
			AdminVo adminVo = new AdminDaoImpl().getAdminInfo(adminId);
			request.setAttribute("tagCityId", adminVo.getCityId());
			request.setAttribute("adminVo",adminVo);
		}

		request.getRequestDispatcher("purview/editUserInfoDialog.jsp").forward(request, response);
	}

	/**
	 * 10004
	 * post
	 * @Title:        editUserInfo 
	 * @Description:  新增/编辑管理员资料/add/edit administrator data
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月5日 下午6:03:22
	 */
	private void editUserInfo(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{

		if(!adminSecurityCheck(request, response)){
			return;
		}
		
		AdminVo adminVo = new AdminVo(request);
		//老用户/old user
		AdminVo oldAdmin = new AdminDaoImpl().getAdminInfo(adminVo.getUsername());
		boolean flag  = false;
		DelType type;
		if(!StringUtils.isEmpty(request.getParameter("id"))){
			type = DelType.DelType_Update;
			int adminId = Integer.parseInt(request.getParameter("id"));
			if(ValueUtil.getInt(oldAdmin.getId()) != adminId){
				//账号已存在/account already exist
				returnFail(response, getCurrentLangValue("system_config_manager_username_exist"));
				return;
			}
			AdminVo oldAdminVo = new AdminDaoImpl().getAdminInfo(adminId);
			if(!oldAdminVo.getPassword().equals(adminVo.getPassword())){
				//已修改密码/already modify password
				adminVo.setPassword(MD5Utils.getMD5(adminVo.getPassword()));
			}

			flag = new AdminDaoImpl().updateAdminInfo(adminVo);
		}else{
			type = DelType.DelType_Add;

			if(oldAdmin != null){
				//账号已存在/account already exist
				returnFail(response, getCurrentLangValue("system_config_manager_username_exist"));
				return;
			}

			adminVo.setPassword(MD5Utils.getMD5(adminVo.getPassword()));
			String userId = new AdminDaoImpl().addAdmin(adminVo);
			flag = userId == null ?false:true;

			addLogForAddData(request, response, userId);
		}

		returnData(response, type, flag);
	}


	/**
	 * 10005
	 * @Title:        deleteAdmin 
	 * @Description:  删除管理员/delete administrator
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月5日 下午6:26:47
	 */
	private void deleteAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int adminId = Integer.parseInt(req.getParameter(parms[0]));
		boolean flag = BaseDao.deleteRecord(IAdminDao.TABLE_NAME, adminId+"");
		if(flag){
			//权限回收/right back
			new UserGroupDaoImpl().deleteUserGroup(adminId);
		}
		returnData(resp, DelType.DelType_Delete, flag);
	}

	/**
	 * 10006
	 * @Title:        getAdminLogList 
	 * @Description:  获取管理员日志/obtain administrator log
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月14日 下午2:33:23
	 */
	private void getAdminLogList(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		RequestListVo requestVo = new RequestListVo(request,false,true);

		String funcIdsStr = null;
		if(!StringUtils.isEmpty(request.getParameter("funcIds"))){
			String tempFun =  request.getParameter("funcIds").replace("[", "").replace("]", "").replace("on,", "");
			funcIdsStr = tempFun;
			funcIdsStr = funcIdsStr.substring(0, funcIdsStr.length()-1);
		}


		List<AdminLogVo> logList = new AdminLogDaoImpl().getLogList(requestVo,funcIdsStr);

		if(request.getParameter("export") != null){//导出/export
			setModelParams(getCurrentLangValue("system_config_manager_log"),"admin_log_list");
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("login_username"),getCurrentLangValue("nickname_title"),
					getCurrentLangValue("purview_function_name"),getCurrentLangValue("common_data"),
					getCurrentLangValue("common_date_title")},
					logList,request,response,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<AdminLogVo> logList = (List<AdminLogVo>)list;
					for (int i = 0; i < logList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						AdminLogVo logVo = logList.get(i);  
						//创建单元格，并设置值/create unit excel, and set up value 
						row.createCell( 0).setCellValue(logVo.getId());  
						row.createCell(1).setCellValue(logVo.getAdminVo().getUsername());  
						row.createCell(2).setCellValue(logVo.getAdminVo().getNickname());  
						row.createCell(3).setCellValue(logVo.getFuncName());  
						row.createCell(4).setCellValue(logVo.getDataId());  
						row.createCell(5).setCellValue(logVo.getDate());  
					}  
				}
			});

		}else{
			request.setAttribute("LogList",logList);
			requestVo.setTotalCount(new AdminLogDaoImpl().getCount(requestVo, funcIdsStr));


			List<String> funcIds = null;
			if(!StringUtils.isEmpty(request.getParameter("funcIds"))){
				String tempFun =  request.getParameter("funcIds").replace("[", "").replace("]", "").replace("on,", "");
				//截取尾号/Intercept the tail number
				String finalStr = tempFun.substring(tempFun.length()-1, tempFun.length());

				String[] funcIdArr;
				if(",".equals(finalStr)){//尾号为，/tail number is
					tempFun = tempFun.substring(0, tempFun.length()-1);
					funcIdArr = tempFun.split(",");
				}else{
					funcIdArr = tempFun.split(",");
				}
				funcIds = new ArrayList<String>();
				for (String string : funcIdArr) {
					funcIds.add(string);
				}
				request.setAttribute("funcIds",request.getParameter("funcIds"));
			}
			//所有权限/all right
			ResourceBundle rb = ResourceBundle.getBundle("com.pgt.bikelock.resource.resource", LanguageUtil.getLocale(currentLang));
			List<FunctionVo> funcList = new FunctionDaoImpl().getFunctionList(rb,funcIds);
			request.setAttribute("funcList",funcList);

			returnDataList(request, response, requestVo,"purview/adminLogList.jsp");

		}

	}

	/**
	 * 10007
	 * @Title:        changeLanguage 
	 * @Description:  切换语言环境/switch language environment
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月10日 下午2:46:00
	 */
	private void changeLanguage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpServletRequest httpRequest=(HttpServletRequest)req;
		HttpSession session=httpRequest.getSession(true);
		AdminLoginFilter.setLanguage(req, session);
		req.getRequestDispatcher("index.jsp").forward(req, resp);
	}
	
	//下一接口10009/next interface
}
