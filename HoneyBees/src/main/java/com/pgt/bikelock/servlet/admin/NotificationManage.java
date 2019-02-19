/**
 * FileName:     NotificationManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月22日 下午2:17:58
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月22日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.bo.EmailBo;
import com.pgt.bikelock.bo.SmsBo;
import com.pgt.bikelock.dao.IEmailDao;
import com.pgt.bikelock.dao.INotificationConfigDao;
import com.pgt.bikelock.dao.ISmsDao;
import com.pgt.bikelock.dao.ISmsTemplateDao;
import com.pgt.bikelock.dao.impl.AdminDaoImpl;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.EmailDaoImpl;
import com.pgt.bikelock.dao.impl.NotificationConfigDaoImpl;
import com.pgt.bikelock.dao.impl.SmsDaoImpl;
import com.pgt.bikelock.dao.impl.SmsTemplateDaoImpl;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.servlet.admin.BaseManage.DelType;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.EmailVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.SmsTemplateVo;
import com.pgt.bikelock.vo.SmsVo;
import com.pgt.bikelock.vo.admin.AdminVo;
import com.pgt.bikelock.vo.admin.NotificationConfigVo;

/**
 * 接口定义起点:11001
 * @ClassName:     NotificationManage
 * @Description:消息通知管理/Notification management
 * @author:    Albert
 * @date:        2017年11月22日 下午2:17:58
 *
 */
public class NotificationManage extends BaseManage {

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
		case 11001:
			getSmsList(req, resp);
			break;
		case 11002:
			getSmsTemplateList(req, resp);
			break;
		case 11003:
			toEditTemplate(req, resp);
			break;
		case 11006:
			toSendSms(req, resp);
			break;
		case 11007:
			getNotificationConfigList(req, resp);
			break;
		case 11008:
			toEditNotificationConfig(req, resp);
			break;
		case 11010:
			getEmailList(req, resp);
			break;
		case 11012:
			toSendEmail(req, resp);
			break;
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
		setModelParams(LanguageUtil.getDefaultValue("notification_management_sms_list"), "sms_list");
		switch (getRequestType(req)) {
		case 11001:
			getSmsList(req, resp);
			break;
		case 11002:
			getSmsTemplateList(req, resp);
			break;
		case 11003:
			editTemplate(req, resp);
			break;
		case 11004:
			deleteSms(req, resp);
			break;
		case 11005:
			deleteSmsTemplate(req, resp);
			break;
		case 11006:
			sendSms(req, resp);
			break;
		case 11007:
			getNotificationConfigList(req, resp);
			break;
		case 11008:
			editNotificationConfig(req, resp);
			break;
		case 11009:
			deleteNotificationConfig(req, resp);
			break;
		case 11010:
			getEmailList(req, resp);
			break;
		case 11011:
			deleteEmail(req, resp);
			break;
		case 11012:
			sendEmail(req, resp);
			break;
		}
	}

	/**
	 * 11001
	 * @Title:        getSmsList 
	 * @Description:  短信列表获取/Get sms list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月23日 下午2:34:34
	 */
	private void getSmsList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,true,false);
		ISmsDao smsDao = new SmsDaoImpl();
		List<SmsVo> smsList = smsDao.getSmsList(requestVo);
		setModelParams(getCurrentLangValue("notification_management_sms_list"), "sms_list");
		if(req.getParameter("export") != null){//导出/export
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("notification_management_sms_phone_area_code"),
					getCurrentLangValue("user_phone_title"),getCurrentLangValue("notification_management_sms_code"),
					getCurrentLangValue("common_content_title"),getCurrentLangValue("common_date_title"),
					getCurrentLangValue("notification_management_sms_used_title")},smsList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					List<SmsVo> smsList =(List<SmsVo>)list;
					for (int i = 0; i < smsList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						SmsVo sms = smsList.get(i);  
						//创建单元格，并设置值 /creat unit excel and set up value 
						row.createCell( 0).setCellValue(sms.getId());  
						row.createCell(1).setCellValue(sms.getAreaCode());  
						row.createCell(2).setCellValue(sms.getPhone());  
						row.createCell(3).setCellValue(sms.getCode());  
						row.createCell(4).setCellValue(sms.getContent());  
						row.createCell(5).setCellValue(sms.getDate());  
						row.createCell(6).setCellValue(sms.getUsedStr());  
					}  
				}
			});

		}else{
			req.setAttribute("smsList",smsList);
			requestVo.setTotalCount(smsDao.getSmsCount(requestVo));
			returnDataList(req, resp, requestVo, "notification/smsList.jsp");
		}
	
	}

	/**
	 * 11002
	 * @Title:        getSmsTemplateList 
	 * @Description:  获取短信模板列表/get sms template list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月23日 下午4:21:20
	 */
	private void getSmsTemplateList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,true,false);
		ISmsTemplateDao templateDao = new SmsTemplateDaoImpl();
		
		List<SmsTemplateVo> tempList = templateDao.getTemplateList(requestVo);
		setModelParams(getCurrentLangValue("notification_management_template_list"), "template_list");
		if(req.getParameter("export") != null){//导出/export
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("common_type_title"),
					getCurrentLangValue("common_title"),
					getCurrentLangValue("common_content_title"),getCurrentLangValue("common_date_title")},tempList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					List<SmsTemplateVo> tempList = (List<SmsTemplateVo>)list;
					for (int i = 0; i < tempList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						SmsTemplateVo temp = tempList.get(i);  
						//创建单元格，并设置值 /creat unit excel and set up value 
						row.createCell( 0).setCellValue(temp.getId());  
						row.createCell(1).setCellValue(temp.getTypeStr());  
						row.createCell(2).setCellValue(temp.getTitle());  
						row.createCell(3).setCellValue(temp.getTemplate());   
						row.createCell(4).setCellValue(temp.getDate());  
					}  
				}
			});

		}else{
			req.setAttribute("templateList",tempList);
			requestVo.setTotalCount(BaseDao.getCount(ISmsTemplateDao.table_name, requestVo, new String[]{"template"}));
			req.setAttribute("lookup", ValueUtil.getInt(req.getParameter("lookup")));
			returnDataList(req, resp, requestVo, "notification/templateList.jsp");
		}
		
	}

	/**
	 * 11003
	 * get
	 * @Title:        toEditTemplate 
	 * @Description:  加载编辑模板页/load eidt template page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月23日 下午4:40:41
	 */
	private void toEditTemplate(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int type = 0;
		if(req.getParameter("id") != null){
			SmsTemplateVo templateVo = new SmsTemplateDaoImpl().getTemplate(req.getParameter("id"));
			type = templateVo.getType();
			req.setAttribute("templateVo",templateVo);
		}
		req.setAttribute("type",type);
		req.getRequestDispatcher("notification/editTemplate.jsp").forward(req, resp);
	}

	/**
	 * 11003
	 * post
	 * @Title:        editTemplate 
	 * @Description:  编辑模板/edit template
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月23日 下午4:41:15
	 */
	private void editTemplate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(!adminSecurityCheck(request, response)){
			return;
		}
		ISmsTemplateDao templateDao = new SmsTemplateDaoImpl();
		SmsTemplateVo templateVo = new SmsTemplateVo(request);
		templateVo.setIndustry_id(getIndustryId(request, response)+"");
		//重复类型判断/type check
		SmsTemplateVo oldTemplateVo = templateDao.getTemplate(templateVo.getIndustry_id(), templateVo.getType());
		boolean flag  = false;
		DelType type;
		if(!StringUtils.isEmpty(request.getParameter("id"))){
			type = DelType.DelType_Update;
			int templateId = ValueUtil.getInt(request.getParameter("id"));
			if(templateVo.getType() == 1 && ValueUtil.getInt(templateVo.getId()) != templateId){
				//类型已存在/type already exist
				returnFail(response, getCurrentLangValue("consume_deposit_city_exist_title"));
				return;
			}
			flag = templateDao.updateTemplate(templateVo);
		}else{
			type = DelType.DelType_Add;

			if(oldTemplateVo != null && templateVo.getType() == 1){
				//类型已存在/type already exist
				returnFail(response, getCurrentLangValue("consume_deposit_city_exist_title"));
				return;
			}
			String tempId = templateDao.addTemplate(templateVo);
			flag = tempId == null ?false:true;
			addLogForAddData(request, response, tempId);
		}
		setModelParams(getCurrentLangValue("notification_management_template_list"), "template_list");
		returnData(response, type, flag);
	}


	/**
	 * 11004
	 * @Title:        deleteSms 
	 * @Description:  删除短信记录/delete sms record
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月23日 下午5:58:27
	 */
	protected void deleteSms(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String ids = req.getParameter("ids");
		if(StringUtils.isEmpty(ids)){
			returnParamsError(resp);
			return;
		}
		boolean flag = BaseDao.deleteRecord(ISmsDao.table_name, ids);
		returnData(resp, DelType.DelType_Delete, flag);
	}

	/**
	 * 11005
	 * @Title:        deleteSmsTemplate 
	 * @Description:  删除短信模板/delete sms template
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月23日 下午6:01:47
	 */
	protected void deleteSmsTemplate(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String ids = req.getParameter("ids");
		if(StringUtils.isEmpty(ids)){
			returnParamsError(resp);
			return;
		}
		boolean flag = BaseDao.deleteRecord(ISmsTemplateDao.table_name, ids);
		setModelParams(getCurrentLangValue("notification_management_template_list"), "template_list");
		returnData(resp, DelType.DelType_Delete, flag);
	}

	/**
	 * 11006
	 * get
	 * @Title:        toSendSms 
	 * @Description:  加载短信发送界面/load sned sms
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月23日 下午6:43:07
	 */
	private void toSendSms(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("notification/sendSms.jsp").forward(req, resp);
	}

	/**
	 * 11006
	 * post
	 * @Title:        sendSms 
	 * @Description:  发送短信/Send sms
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月24日 下午3:16:11
	 */
	private void sendSms(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		String[] parms = new String[]{"userVo.phone","templateVo.template"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String[] phones = req.getParameter(parms[0]).split(",");
		String content = req.getParameter(parms[1]);
		SmsBo smsBo = new SmsBo();
		int count = 0;
		for (String  phone : phones) {
			if(smsBo.sendSms(phone, content)){
				count ++;
			}
		}
		setModelParams(getCurrentLangValue("notification_management_sms_list"), "sms_list");
		String message = String.format(getCurrentLangValue("notification_management_sms_send_tips"),phones.length,count);
		returnSuccess(resp, message,true);
	}

	/**
	 * 11007
	 * @Title:        getNotificationConfigList 
	 * @Description:  获取通知配置列表/get notification configuration list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月25日 下午5:47:16
	 */
	private void getNotificationConfigList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,true,false);
		List<NotificationConfigVo> configList = new NotificationConfigDaoImpl().getConfigList();
		req.setAttribute("configList", configList);
		req.setAttribute("lookup", 0);
		returnDataList(req, resp, requestVo, "notification/configList.jsp");
	}

	/**
	 * 11008
	 * get
	 * @Title:        toEditNotificationConfig 
	 * @Description:  加载编辑通知设置页/load notification configuration page
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月27日 下午2:03:42
	 */
	private void toEditNotificationConfig(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int type = 0;
		if(request.getParameter("id") != null){
			NotificationConfigVo configVo = new NotificationConfigDaoImpl().getConfigInfo(ValueUtil.getInt(request.getParameter("id")));
			type = configVo.getType();
			request.setAttribute("configVo",configVo);
		}
		request.setAttribute("heart_value", NotificationConfigVo.HEART_STATISTICS_INTERVAL);
		request.setAttribute("heart_frequency", NotificationConfigVo.HEART_MONITORING_FREQUENCY);

		request.setAttribute("location_value", NotificationConfigVo.LOCATION_STATISTICS_INTERVAL);
		request.setAttribute("location_frequency", NotificationConfigVo.LOCATION_MONITORING_FREQUENCY);

		request.setAttribute("power_value", NotificationConfigVo.LOW_POWER_VALUE);
		request.setAttribute("power_frequency", NotificationConfigVo.LOW_POWER_FREQUENCY);
		request.setAttribute("type",type);
		request.getRequestDispatcher("notification/editConfig.jsp").forward(request, response);
	}

	/**
	 * 11008
	 * post
	 * @Title:        editNotificationConfig 
	 * @Description:  编辑通知设置/configuration notification
	 * @param:        @param request
	 * @param:        @param response
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月27日 下午2:03:21
	 */
	private void editNotificationConfig(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(!adminSecurityCheck(request, response)){
			return;
		}
		INotificationConfigDao configDao = new NotificationConfigDaoImpl();
		NotificationConfigVo configVo = new NotificationConfigVo(request);
		//重复类型判断/type check
		NotificationConfigVo oldConfigVo = configDao.getConfigInfoByType(configVo.getType());
		boolean flag  = false;
		DelType type;
		if(configVo.getId() > 0){
			type = DelType.DelType_Update;
			if(oldConfigVo.getType() != configVo.getType() && oldConfigVo.getId() != configVo.getId()){
				//类型已存在/type already exist
				returnFail(response, getCurrentLangValue("consume_deposit_city_exist_title"));
				return;
			}
			flag = configDao.updateConfig(configVo);

			//停止旧服务/stop old service
			if(flag && oldConfigVo.getType() != configVo.getType()){
				BikeBo.getInstance().stopBikeCheckService(oldConfigVo.getType());
			}
			
		}else{
			type = DelType.DelType_Add;

			if(oldConfigVo != null){
				//类型已存在/type already exist
				returnFail(response, getCurrentLangValue("consume_deposit_city_exist_title"));
				return;
			}
			String tempId = configDao.addConfig(configVo);
			flag = tempId == null ?false:true;
			addLogForAddData(request, response, tempId);
		}

		setModelParams(getCurrentLangValue("notification_management_config_list"), "config_list");
		returnData(response, type, flag);

		//开启新服务/start new service
		if(flag && configVo.getJsonValue() > 0 && configVo.getJsonFrequency() > 0){
			BikeBo.getInstance().restartBikeCheckService(configVo.getType(), configVo.getJsonValue(),configVo.getJsonFrequency());
		}
	}

	/**
	 * 11009
	 * @Title:        deleteNotificationConfig 
	 * @Description:  删除通知设置/delete notification configuration
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月27日 下午4:05:26
	 */
	protected void deleteNotificationConfig(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String ids = req.getParameter("ids");
		if(StringUtils.isEmpty(ids)){
			returnParamsError(resp);
			return;
		}

		INotificationConfigDao configDao = new NotificationConfigDaoImpl();
		for (String id : ids.split(",")) {
			NotificationConfigVo configVo = configDao.getConfigInfo(ValueUtil.getInt(id));
			BikeBo.getInstance().stopBikeCheckService(configVo.getType());
		}

		boolean flag = BaseDao.deleteRecord(INotificationConfigDao.table_name, ids);
		setModelParams(getCurrentLangValue("notification_management_config_list"), "config_list");
		returnData(resp, DelType.DelType_Delete, flag);
	}

	/**
	 * 11010
	 * @Title:        getEmailList 
	 * @Description:  获取邮件列表/get email list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月29日 下午4:10:08
	 */
	private void getEmailList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,true,false);
		IEmailDao emailDao = new EmailDaoImpl();
		List<EmailVo> emailList = emailDao.getEmailList(requestVo);
		setModelParams(getCurrentLangValue("notification_management_email_list"), "email_list");
		if(req.getParameter("export") != null){//导出/export
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("notification_management_email_sender"),
					getCurrentLangValue("notification_management_email_receiver"),getCurrentLangValue("notification_management_email_subject"),
					getCurrentLangValue("common_content_title"),getCurrentLangValue("common_date_title"),
					getCurrentLangValue("common_success_title")},emailList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					List<EmailVo> emailList = (List<EmailVo>)list;
					for (int i = 0; i < emailList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						EmailVo email = emailList.get(i);  
						//创建单元格，并设置值 /creat unit excel and set up value 
						row.createCell( 0).setCellValue(email.getId());  
						row.createCell(1).setCellValue(email.getSender());  
						row.createCell(2).setCellValue(email.getReceiver());  
						row.createCell(3).setCellValue(email.getSubject());  
						row.createCell(4).setCellValue(email.getContent());  
						row.createCell(5).setCellValue(email.getDate());  
						row.createCell(6).setCellValue(email.getStatusStr());  
					}  
				}
			});

		}else{
			req.setAttribute("emailList",emailList);
			requestVo.setTotalCount(emailDao.getEmailCount(requestVo));
			returnDataList(req, resp, requestVo, "notification/emailList.jsp");
		}
	
	}

	/**
	 * 11011
	 * @Title:        deleteEmail 
	 * @Description:  删除邮件记录/delete email record
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月29日 下午5:21:03
	 */
	protected void deleteEmail(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		String ids = req.getParameter("ids");
		if(StringUtils.isEmpty(ids)){
			returnParamsError(resp);
			return;
		}
		boolean flag = BaseDao.deleteRecord(IEmailDao.table_name, ids);
		setModelParams(getCurrentLangValue("notification_management_email_list"), "email_list");
		returnData(resp, DelType.DelType_Delete, flag);
	}

	/**
	 * 11012
	 * get
	 * @Title:        toSendEmail 
	 * @Description:  加载邮箱发送界面/load send email
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月29日 下午5:25:02
	 */
	private void toSendEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("notification/sendEmail.jsp").forward(req, resp);
	}


	/**
	 * 11012
	 * post
	 * @Title:        sendEmail 
	 * @Description:  发送邮件/Send email
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月29日 下午5:26:22
	 */
	private void sendEmail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		String[] parms = new String[]{"userVo.email","templateVo.template","templateVo.title"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String[] emails = req.getParameter(parms[0]).split(",");
		String content = req.getParameter(parms[1]);
		String title = req.getParameter(parms[2]);
		EmailBo emailBo = new EmailBo();
		int count = 0;
		for (String  email : emails) {
			EmailVo emailVo = new EmailVo(email, email, title, content);
			if(emailBo.sendSystemEmail(emailVo)){
				count ++;
			}
		}
		setModelParams(getCurrentLangValue("notification_management_email_list"), "email_list");
		String message = String.format(getCurrentLangValue("notification_management_email_send_tips"),emails.length,count);
		returnSuccess(resp, message,true);
	}
}
