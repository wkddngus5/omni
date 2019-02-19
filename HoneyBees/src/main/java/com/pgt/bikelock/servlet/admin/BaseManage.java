package com.pgt.bikelock.servlet.admin;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Time;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.omni.purview.dao.impl.UserLogDaoImpl;
import com.omni.purview.vo.UserLogVo;
import com.pgt.bikelock.dao.IAdminDao;
import com.pgt.bikelock.dao.impl.AdminDaoImpl;
import com.pgt.bikelock.dao.impl.AdminLogDaoImpl;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.FileUtil;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.AdminVo;

import freemarker.template.utility.StringUtil;


/**
 * 后台管理业务对外接口基类/background business outside interface base class
 * @author apple
 * 2017年03月17日11:24:56
 */
public class BaseManage extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String modelName;//模块名称/template name
	protected String modelNavTabId;//模块导航栏地址/template navigation item address
	//操作类型定义/operation type definition
	private static  Object[] delTypeSuccessArr = null;//操作成功提示/operation success prompt
	private static  Object[] delTypeFailArr = null;//操作失败提示/operation failure prompt
	protected static String currentLang = null;//当前语言环境/current languagge environment
	private RequestListVo requestVo;

	protected enum DelType{
		DelType_Add,//增加/add
		DelType_Delete,//删除/delete
		DelType_Update,//修改/modify
		DelType_Review,//审核/处理/modify/tackle
	}

	/**
	 * 操作类型枚举/operate type list
	 * @param type
	 * @return
	 */
	private int getDelTypeIndex(DelType type){
		int index = 0;
		switch (type) {
		case DelType_Add:
			index = 0;
			break;
		case DelType_Delete:
			index = 1;
			break;
		case DelType_Update:
			index = 2;
			break;
		case DelType_Review:
			index = 3;
			break;
		default:
			break;
		}

		return index;
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		getCurrentLang(req);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		getCurrentLang(req);
	}

	/**
	 * 设置模块参数/set up template parameter 
	 * @param name
	 * @param navId
	 * @pram lang 当前语言环境/current language environment
	 */
	protected void setModelParams(String name,String navId) {

		delTypeSuccessArr =new Object[]{new MessageFormat(LanguageUtil.getValue(currentLang,"common_add_success_title")),
				new MessageFormat(LanguageUtil.getValue(currentLang,"common_delete_success_title")),
				new MessageFormat(LanguageUtil.getValue(currentLang,"common_update_success_title")),
				new MessageFormat(LanguageUtil.getValue(currentLang,"common_review_success_title"))};
		delTypeFailArr =new Object[]{new MessageFormat(LanguageUtil.getValue(currentLang,"common_add_fail_title")),
				new MessageFormat(LanguageUtil.getValue(currentLang,"common_delete_fail_title")),
				new MessageFormat(LanguageUtil.getValue(currentLang,"common_update_fail_title")),
				new MessageFormat(LanguageUtil.getValue(currentLang,"common_review_fail_title"))};

		this.modelName = name;
		this.modelNavTabId = navId;
	}

	/**
	 * 获取请求类型编号/gain request type item number
	 * @param req
	 * @return
	 */
	protected int getRequestType(HttpServletRequest req) {
		if(null != req.getParameter("requestType") && !"".equals(req.getParameter("requestType").trim())){
			System.out.println(Integer.parseInt(req.getParameter("requestType")));
			return Integer.parseInt(req.getParameter("requestType"));
		}
		return 0;
	}

	/**
	 * 校验必填参数/verify must write parameter
	 * @param req
	 * @param resp
	 * @param params
	 * @return
	 * @throws IOException 
	 */
	protected boolean checkRequstParams(HttpServletRequest req, HttpServletResponse resp,String[] params) throws IOException {
		for (int i = 0; i < params.length; i++) {
			if(StringUtils.isEmpty((String)req.getAttribute(params[i])) && StringUtils.isEmpty(req.getParameter(params[i]))){
				returnParamsError(resp);
				return false;
			}
			/*if(params[i].equals("cityId")){
				if(req.getParameter(params[i]) == null){
					req.setAttribute("cityId", req.getSession().getAttribute("cityId"));
				}else{
					req.setAttribute("cityId", req.getParameter("cityId"));
				}
				
			}*/
		}
		return true;
	}

	/**
	 * 
	 * @Title:        getReturnMessage 
	 * @Description:  获取返回消息（适用于异步请求返回）/obtain return news(suit for request return)
	 * @param:        @param type
	 * @param:        @param success
	 * @param:        @return
	 * @param:        @throws IOException    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月7日 上午11:03:33
	 */
	protected String getReturnMessage(DelType type,boolean success) throws IOException{
		return success?((MessageFormat)delTypeSuccessArr[getDelTypeIndex(type)]).format(new Object[]{modelName})
				:((MessageFormat)delTypeFailArr[getDelTypeIndex(type)]).format(new Object[]{modelName});// 提示框显示的文字/prompt item show word
	}

	/**
	 * 
	 * @Title:        returnParamsError 
	 * @Description:  参数异常信息返回/parameter abnormal information return
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月10日 下午3:37:30
	 */
	protected void returnParamsError( HttpServletResponse resp) throws IOException {
		JSONObject result = new JSONObject();
		result.put("statusCode", "300"); // 300失败/300 failure
		result.put("message", "parsms error");  // 提示框显示的文字/the prompt box displays the text

		PrintWriter out = resp.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}

	/**
	 * 返回操作结果/return operate result
	 * @param response
	 * @param delType 操作类型 0：增加 1：删除 2：修改 3:审核处理/operate type 0:add 1:delete 2:modify 3:check solve
	 * @param success 是否成功/whether success
	 * @throws IOException
	 */
	protected void returnData(HttpServletResponse response,DelType type,boolean success) throws IOException{
		JSONObject result = new JSONObject();
		result.put("statusCode", success?"200":"300"); // 200 表示操作成功 300失败/200 show operate success 300 failure
		result.put("message",success?((MessageFormat)delTypeSuccessArr[getDelTypeIndex(type)]).format(new Object[]{modelName})
				:((MessageFormat)delTypeFailArr[getDelTypeIndex(type)]).format(new Object[]{modelName}));// 提示框显示的文字/prompt box displays the text
		result.put("navTabId", modelNavTabId);  // 刷新界面, 刷新的是在index.html 设置的rel值/refresh page, refresh is in index.html set rel/refresh page
		result.put("rel", "");  // 刷新界面，另一个形式的/refresh page, another type

		if(success && type != DelType.DelType_Delete && type != DelType.DelType_Review){//当成功时，关闭窗口/when success, turn off window
			result.put("callbackType", "closeCurrent"); // 弹出dialog的形式,使用这个关闭当前弹出的窗口/pop up dialog, use this turn off current pop up window
		}

		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}


	/**
	 * 返回操作结果/return operate result
	 * @param response
	 * @param message
	 * @param success 是否成功/whether success
	 * @throws IOException
	 */
	protected void returnDataJustTip(HttpServletResponse response,boolean success,String message) throws IOException{
		JSONObject result = new JSONObject();
		result.put("statusCode", success?"200":"300"); // 200 表示操作成功 300失败/200 show operate success 300 failure

		result.put("message", message+" "+(success?getCurrentLangValue("common_success_tip_title"):getCurrentLangValue("common_fail_tip_title")));  // 提示框显示的文字/prompt box show word

		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}

	/**
	 * 返回失败信息/return failure information
	 * @param response
	 * @param errorMsg
	 * @throws IOException
	 */
	protected void returnFail(HttpServletResponse response,String errorMsg) throws IOException{
		JSONObject result = new JSONObject();
		result.put("statusCode", "300"); // 200 表示操作成功 300失败/200 show operate success 300 failure

		result.put("message", errorMsg);  // 提示框显示的文字/prompt box show word

		result.put("navTabId", modelNavTabId);  // 刷新界面, 刷新的是在index.html 设置的rel值/refresh page, refresh is in index.html, set rel/refresh page
		result.put("rel", "");  // 刷新界面，另一个形式的/refresh page, another type

		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}

	/**
	 * 
	 * @Title:        returnSuccess 
	 * @Description:  返回成功信息/return success information
	 * @param:        @param response
	 * @param:        @param successMsg
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午5:32:29
	 */
	protected void returnSuccess(HttpServletResponse response,String successMsg,boolean closeCurrent) throws IOException{
		JSONObject result = new JSONObject();
		result.put("statusCode", "200"); // 200 表示操作成功 300失败/200 show operate success 300 failure

		result.put("message", successMsg);  // 提示框显示的文字/prompt box show word

		result.put("navTabId", modelNavTabId);  // 刷新界面, 刷新的是在index.html 设置的rel值/refresh page, refresh is in index.html set up rel/refresh page
		result.put("rel", "");  // 刷新界面，另一个形式的/refresh page, another type
		if(closeCurrent){
			result.put("callbackType", "closeCurrent"); // 弹出dialog的形式,使用这个关闭当前弹出的窗口/pop up dialog, use this turn off current pop up window
		}
	
		PrintWriter out = response.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}

	protected void returnSuccess(HttpServletResponse response,String successMsg,RequestListVo requestV) throws IOException{
		requestVo = requestV;
		returnSuccess(response, successMsg,false);
	}

	protected void setReturnRequestVo(JSONObject result) {
		if(requestVo != null){
			result.put("pageSize",requestVo.getPageSize());
			result.put("pageNo",requestVo.getPageNo());
			result.put("totalCount",requestVo.getTotalCount()); // 所有数据条数/all data item
			result.put("keyWords",requestVo.getKeyWords());
			result.put("type",requestVo.getType());
		}
	}

	/**
	 * 返回数据集合/return data 
	 * @param request
	 * @param response
	 * @param requestVo
	 * @param dispatcherUrl
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void returnDataList(HttpServletRequest request,HttpServletResponse response,RequestListVo requestVo,String dispatcherUrl) throws IOException, ServletException{
		if(requestVo != null){
			if(requestVo.getPageSize() == 0 && ValueUtil.getInt(OthersSource.getSourceString("default_page_size")) > 0){
				request.setAttribute("pageSize",OthersSource.getSourceString("default_page_size"));
			}else{
				request.setAttribute("pageSize",requestVo.getPageSize());
			}

			request.setAttribute("pageNo",requestVo.getPageNo());
			request.setAttribute("totalCount",requestVo.getTotalCount()); // 所有数据条数/all data
			request.setAttribute("keyWords",requestVo.getKeyWords());
			request.setAttribute("type",requestVo.getType());
			request.setAttribute("extendType1",requestVo.getExtendType1());
			request.setAttribute("status",requestVo.getStatus());
			request.setAttribute("way",requestVo.getWay());
			request.setAttribute("tagIds",requestVo.getTagIds());
			request.setAttribute("orderField",requestVo.getOrderField());
			request.setAttribute("orderDirection",requestVo.getOrderDirection());
			if(!StringUtils.isEmpty(requestVo.getStartTime())){
				if(ValueUtil.getInt(requestVo.getStartTime()) > 0){
					request.setAttribute("startTime",TimeUtil.formaStrDate(requestVo.getStartTime(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				}else{
					request.setAttribute("startTime",requestVo.getStartTime());
				}

			}
			if(!StringUtils.isEmpty(requestVo.getEndTime())){
				if(ValueUtil.getInt(requestVo.getEndTime()) > 0){
					request.setAttribute("endTime",TimeUtil.formaStrDate(requestVo.getEndTime(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
				}else{
					request.setAttribute("endTime",requestVo.getEndTime());
				}
			}
		}
		request.getRequestDispatcher(dispatcherUrl).forward(request, response);
	}

	/**
	 * 返回数据对象/return data objection
	 * @param resp
	 * @param json
	 */
	protected void returnAjaxData(HttpServletResponse resp,Object json) {
		PrintWriter out;
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("data", JSON.toJSON(json));
			dataMap.put("statusCode", "200");
			out = resp.getWriter();
			out.write(dataMap.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	/**
	 * 
	 * @Title:        getIndustryId 
	 * @Description:  获取产业ID/obtain industry id
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:23:14
	 */
	protected int getIndustryId(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String industryId = (String) session.getAttribute("industryId");
		int value = ValueUtil.getInt(industryId);
		if(value == 0){
			goToReLogin(req, resp);
		}
		return value;
	}

	/**
	 * 
	 * @Title:        getAdminId 
	 * @Description:  获取登陆管理员ID/obtain log in registrator id
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @return
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月15日 下午4:58:52
	 */
	protected String getAdminId(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String adminId = (String) session.getAttribute("adminId");
		int value = ValueUtil.getInt(adminId);
		if(value == 0){
			goToReLogin(req, resp);

			return null;
		}
		return value+"";
	}

	/**
	 * 
	 * @Title:        goToReLogin 
	 * @Description:  重新登陆/renew log in
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月15日 下午5:30:38
	 */
	private void goToReLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		//		resp.sendRedirect(req.getContextPath()+"/admin/login.html");

		JSONObject result = new JSONObject();
		result.put("statusCode", "301"); //
		result.put("message", "\u4f1a\u8bdd\u8d85\u65f6\uff0c\u8bf7\u91cd\u65b0\u767b\u5f55\u3002");  // 提示框显示的文字/prompt box show word
		result.put("navTabId","");
		result.put("callbackType","");
		result.put("forwardUrl","");

		PrintWriter out = resp.getWriter();
		out.write(result.toString());
		out.flush();
		out.close();
	}

	/**
	 * 
	 * @Title:        getCurrentLang 
	 * @Description:  获取当前语言环境/obtain current language environment
	 * @param:        @param req
	 * @param:        @return
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月20日 下午4:23:44
	 */
	private void getCurrentLang(HttpServletRequest req)
			throws ServletException, IOException {
		currentLang = req.getSession().getAttribute("lang").toString();
	}

	/**
	 * 
	 * @Title:        getCurrentLangValue 
	 * @Description:  获取当前语言属性值/obtain current language parameter value
	 * @param:        @param key
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月20日 下午4:35:12
	 */
	protected String getCurrentLangValue(String key){
		return LanguageUtil.getValue(currentLang, key);  
	}
	
	/**
	 * 
	 * @Title:        getCurrentLangValue 
	 * @Description:  获取当前语言属性值/obtain current language value 
	 * @param:        @param key
	 * @param:        @param params
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年8月1日 下午3:03:50
	 */
	protected String getCurrentLangValue(String key,Object params){
		String value = "";
		try {
			MessageFormat tipMF = new MessageFormat(getCurrentLangValue(key));
			value = tipMF.format(params);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}
	

	/**
	 * 
	 * @Title:        getDataExistValue 
	 * @Description:  数据已存在提示/data already exist prompt
	 * @param:        @param module
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月20日 下午5:01:58
	 */
	protected String getDataExistValue(){
		MessageFormat existFmt = new MessageFormat(LanguageUtil.getValue(currentLang,"common_data_exist_error"));
		return existFmt.format(new Object[]{modelName}); 
	}

	/**
	 * 
	 * @Title:        getDataNotExistValue 
	 * @Description:  数据不存在提示/data not exist prompt
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月20日 下午5:15:16
	 */
	protected String getDataNotExistValue(){
		MessageFormat existFmt = new MessageFormat(LanguageUtil.getValue(currentLang,"common_data_not_exist_error"));
		return existFmt.format(new Object[]{modelName}); 
	}

	/**
	 * 
	 * @Title:        updateResultValue 
	 * @Description:  更新数据结果/updata data result
	 * @param:        @param success
	 * @param:        @param info
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月20日 下午5:31:20
	 */
	protected String getUpdateResultValue(boolean success,String info){
		MessageFormat existFmt = new MessageFormat(LanguageUtil.getValue(currentLang,
				success?"common_update_success_title":"common_update_fail_title"));
		return existFmt.format(new Object[]{info}); 
	}

	/**
	 * 
	 * @Title:        deleteResultValue 
	 * @Description:  更新数据结果/updata data result
	 * @param:        @param success
	 * @param:        @param info
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年4月20日 下午5:31:34
	 */
	protected String getDeleteResultValue(boolean success,String info){
		MessageFormat existFmt = new MessageFormat(LanguageUtil.getValue(currentLang,
				success?"common_delete_success_title":"common_delete_fail_title"));
		return existFmt.format(new Object[]{info}); 
	}


	/**
	 * 导出数据（Excel）/export excel(excel)
	 * @Title:        exportData 
	 * @Description:  TODO
	 * @param:        @param coloums
	 * @param:        @param fileName
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年6月8日 下午4:24:12
	 */
	protected String exportData(String[] coloums,Object list,HttpServletRequest req, HttpServletResponse resp,ExportDataCallBack callBack){
		String filePath = null;

		// 第一步，创建一个webbook，对应一个Excel文件/first step, creat a webbook, correspond an excel file  
		HSSFWorkbook wb = new HSSFWorkbook();  
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet /second step, add a sheet in webook, correspond sheet on excel
		HSSFSheet sheet = wb.createSheet(this.modelName);  

		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  /third step, add 0 row on head of excel, notice old version poi correspond row number have restrict short on excel
		HSSFRow row = sheet.createRow((int) 0);  
		// 第四步，创建单元格，并设置值表头 设置表头居中 /fourth step, creat unit excel, and set up head set up head in middle 
		HSSFCellStyle style = wb.createCellStyle();  
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式/create a middle formate  


		HSSFCell cell;
		for (int i = 0; i < coloums.length; i++) {
			cell = row.createCell(i);  
			cell.setCellValue(coloums[i]);
			cell.setCellStyle(style);  
			sheet.setColumnWidth(i, 20 * 256);
		}

		// 第五步，回调写入实体数据/five step, call back write real data
		callBack.setExportData(sheet, row, list);
		String fileName = this.modelName;//+"_"+TimeUtil.getCurrentLongTime();
		// 第六步，将文件存到指定位置/six step, put file keep in position  
		try  
		{  
			filePath = OthersSource.EXPORT_FILE_PATH+fileName+".xls";
			FileOutputStream fout = new FileOutputStream(filePath);  
			wb.write(fout);  
			fout.close();  
		}  
		catch (Exception e)  
		{  
			e.printStackTrace();  
			filePath = null;
		}  
		if(filePath != null){
			FileUtil.downloadFile(fileName+".xls", req, resp);
		}
		return filePath;

	}

	/**
	 * 
	 * @Title:        addLogForAddData 
	 * @Description:  添加新增数据日志/add new data log
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param dataId
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月15日 下午6:33:56
	 */
	protected void addLogForAddData(HttpServletRequest req, HttpServletResponse resp,String dataId) throws IOException{
		if(dataId == null){
			return;
		}
		HttpServletRequest httpRequest=(HttpServletRequest)req;
		HttpSession session=httpRequest.getSession(false);
		if(req.getAttribute("funcId") != null){
			int funcId = Integer.parseInt(req.getAttribute("funcId").toString());
			if(funcId > 0){
				String adminId = (String)session.getAttribute("adminId");
				new UserLogDaoImpl().addUserLog(new UserLogVo(adminId, funcId, dataId));
			}
		}
	}
	
	
	/**
	 * 当前城市获取
	 * @Title:        getCityId 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年6月28日 下午3:28:55
	 */
	protected int getCityId(HttpServletRequest httpRequest) {
	
		HttpSession session=httpRequest.getSession(false);
		String adminName = (String) session.getAttribute("userName");
		int cityId = 0;
		if("admin".equals(adminName)||"omni".equals(adminName)){
			//只有超级管理员能切换城市
			cityId = ValueUtil.getInt(httpRequest.getAttribute("cityId"));
		}else{
			//其他管理员只能使用当前城市
			cityId = ValueUtil.getInt(session.getAttribute("cityId"));
		}
		
		return cityId;
	}

	/**
	 * 默认地址地址获取/default address gain
	 * @Title:        setDefaultLocation 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:42:56
	 */
	protected void setDefaultLocation(HttpServletRequest req, HttpServletResponse resp){
		
		req.setAttribute("centerLat",OthersSource.getSourceString("default_location_lat"));
		req.setAttribute("centerLng",OthersSource.getSourceString("default_location_lng"));
		
		int cityId = getCityId(req);
		if(cityId > 0){
			//set current city location
			CityVo cityVo = new CityDaoImpl().getCityInfo(cityId+"");
			if(cityVo.getArea_lat() > 0 || cityVo.getArea_lng() > 0){
				req.setAttribute("centerLat",cityVo.getArea_lat());
				req.setAttribute("centerLng",cityVo.getArea_lng());
			}
		}
		
	}
	
	/**
	 * 
	 * @Title:        adminSecurityCheck 
	 * @Description:  管理员安全验证/administrator safe verfication
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月24日 下午5:45:18
	 */
	protected boolean adminSecurityCheck(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"admin_password"};
		if(!checkRequstParams(req, resp, parms)){
			return false;
		}
		HttpSession session = req.getSession();
		
		String userName = (String) session.getAttribute("userName");
		if(StringUtils.isEmpty(userName)){
			HttpServletResponse httpResponse=(HttpServletResponse)resp;
			HttpServletRequest httpRequest=(HttpServletRequest)req;
			httpResponse.sendRedirect(httpRequest.getContextPath()+"/admin/login.jsp");
			return false;
		}
		String password = req.getParameter(parms[0]);
		IAdminDao loginDao =  new AdminDaoImpl();
		AdminVo user=loginDao.find(userName,MD5Utils.getMD5(password));
		if(user == null){
			returnFail(resp, getCurrentLangValue("admin_password_check_error"));
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @Title:        deleteRecord 
	 * @Description:  删除记录/delete record
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param tableName
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月14日 下午7:46:07
	 */
	protected void deleteRecord(HttpServletRequest req, HttpServletResponse resp,String tableName) throws IOException{
		String id = req.getParameter("id");
		String ids = req.getParameter("ids");
		if(StringUtils.isEmpty(id) && StringUtils.isEmpty(ids)){
			returnParamsError(resp);
			return;
		}
		boolean flag = false;
		if(!StringUtils.isEmpty(ids)){
			flag = BaseDao.deleteRecord(tableName, ids);
		}else{
			flag = BaseDao.deleteRecord(tableName, id);
		}
		returnData(resp, DelType.DelType_Delete, flag);
	}
	
}
