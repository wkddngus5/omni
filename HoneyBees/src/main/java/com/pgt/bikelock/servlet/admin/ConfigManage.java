/**
 * FileName:     ConfigManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午7:46:30
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags.Flag;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.formula.functions.T;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.StringUtils;
import com.amazonaws.client.builder.AwsAsyncClientBuilder;
import com.pgt.bikelock.bo.SystemConfigBo;
import com.pgt.bikelock.dao.IAppVersionDao;
import com.pgt.bikelock.dao.IBikeDao;
import com.pgt.bikelock.dao.IImageDao;
import com.pgt.bikelock.dao.INewsDao;
import com.pgt.bikelock.dao.impl.AppVersionDaoImpl;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.ImageDaoImpl;
import com.pgt.bikelock.dao.impl.NewsDaoImpl;
import com.pgt.bikelock.dao.impl.RechargeAmountDaoImpl;
import com.pgt.bikelock.dao.impl.SystemConfigDaoImpl;
import com.pgt.bikelock.dao.impl.UserDeviceDaoImpl;
import com.pgt.bikelock.dao.impl.WebContentDaoImpl;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.AliOSSUtil;
import com.pgt.bikelock.utils.AwsS3Util;
import com.pgt.bikelock.utils.FileUtil;
import com.pgt.bikelock.utils.MD5Utils;
import com.pgt.bikelock.utils.PushUtil;
import com.pgt.bikelock.utils.PushUtil.PushType;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.AppVersionVo;
import com.pgt.bikelock.vo.ImageVo;
import com.pgt.bikelock.vo.NewsVo;
import com.pgt.bikelock.vo.RechargeAmountVo;
import com.pgt.bikelock.vo.RequestClientVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.WebContentVo;
import com.pgt.bikelock.vo.admin.SystemConfigVo;

/**
 * 接口起点 60000/interface start point
 * @ClassName:     ConfigManage
 * @Description:设置管理/set up manage
 * @author:    Albert
 * @date:        2017年4月10日 下午7:46:30
 *
 */
public class ConfigManage extends BaseManage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);

		setModelParams(getCurrentLangValue("setting_manage"), "web_list");
		switch (getRequestType(req)) {
		case 60001:
			getWebContentList(req, resp);
			break;
		case 60002:
			toUpdateWebContent(req, resp);
			break;
		case 60003:
			getNewsList(req, resp);
			break;
		case 60004:
			toUpdateNews(req, resp);
			break;
		case 60006:
			openOutUrl(req, resp);
			break;
		case 60007:
			getClientRequestList(req, resp);
			break;
		case 60008:
			toClientRequestRuleSetting(req, resp);
			break;
		case 60009:
			toUpdateClientRequest(req, resp);
			break;
		case 60011:
			getAppVersionList(req, resp);
			break;
		case 60012:
			toAddAppVersion(req, resp);
			break;
		case 60013:
			getSystemConfigList(req, resp);
			break;
		case 60014:
			toEditSystemConfig(req, resp);
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
		setModelParams(getCurrentLangValue("setting_manage"), "web_list");
		switch (getRequestType(req)) {
		case 60001:
			getWebContentList(req, resp);
			break;
		case 60002:
			updateWebContent(req, resp);
			break;
		case 60003:
			getNewsList(req, resp);
			break;
		case 60004:
			updateNews(req, resp);
			break;
		case 60005:
			deleteNews(req, resp);
			break;
		case 60007:
			getClientRequestList(req, resp);
			break;
		case 60008:
			clientRequestRuleSetting(req, resp);
			break;
		case 60009:
			updateClientRequest(req, resp);
			break;
		case 60010:
			deleteClientRequest(req, resp);
			break;
		case 60011:
			getAppVersionList(req, resp);
			break;
		case 60012:
			addAppVersion(req, resp);
			break;
		case 60013:
			getSystemConfigList(req, resp);
			break;
		case 60014:
			editSystemConfig(req, resp);
			break;
		}
	}

	/**
	 * 60001
	 * @Title:        getAgremmentList 
	 * @Description:  获取用户协议列表/obtain user protocol list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午7:51:40
	 */
	protected void getWebContentList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<WebContentVo> webList = new WebContentDaoImpl().getContentList(getIndustryId(req, resp)+"");
		req.setAttribute("web_list",webList);
		
		returnDataList(req, resp, null,"config/webContentList.jsp");
	}

	/**
	 * 60002
	 * get
	 * @Title:        toUpdateWebContent 
	 * @Description:  加载更新协议页/loading update protocol page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午8:00:17
	 */
	protected void toUpdateWebContent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		WebContentVo contentVo = new WebContentDaoImpl().getContent(req.getParameter(parms[0]));
		if(contentVo == null){
			return;
		}

		req.setAttribute("contentVo", contentVo);
		req.getRequestDispatcher("config/updateWebContentDialog.jsp").forward(req, resp);
	
	}

	/**
	 * 60002
	 * post
	 * @Title:        updateWebContent 
	 * @Description:  更新网页内容/update webpage information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午8:00:37
	 */
	protected void updateWebContent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id","content"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		WebContentVo agreementVo = new WebContentVo(parms, req);
		boolean flag = new WebContentDaoImpl().updateContent(agreementVo.getId(),agreementVo.getContent());
		returnData(resp, DelType.DelType_Update, flag);
	}
	
	/**
	 * 60003
	 * @Title:        getNewsList 
	 * @Description:  资讯、活动列表/information, activity list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 下午8:31:04
	 */
	protected void getNewsList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		List<NewsVo> newsList = new NewsDaoImpl().getNewsList(requestVo);
		req.setAttribute("newsList",newsList);
		requestVo.setTotalCount(BaseDao.getCount(INewsDao.TABLE_NAME,requestVo,
				new String[]{INewsDao.COLUMN_TITLE,INewsDao.COLUMN_CONTENT}));
		
		returnDataList(req, resp, requestVo,"config/newsList.jsp");
	}
	
	/**
	 * 60004
	 * get
	 * @Title:        toUpdateNews 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 下午9:00:22
	 */
	protected void toUpdateNews(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		NewsVo newsVo = new NewsVo();
		if("1".equals(req.getParameter("status"))){
			String[] parms = new String[]{"id"};
			if(!checkRequstParams(req, resp, parms)){
				return;
			}
			
			newsVo = new NewsDaoImpl().getNews(req.getParameter("id"));
		}
		req.setAttribute("tagCityId", newsVo.getCityId());
		req.setAttribute("newsVo", newsVo);
		req.getRequestDispatcher("config/updateNewsDialog.jsp").forward(req, resp);
	}
	
	/**
	 * 60004
	 * post
	 * @Title:        updateNews 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 下午8:25:03
	 */
	protected void updateNews(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//upload image
		List<ImageVo> imageList = FileUtil.uploadImage(req);
		
		
		NewsVo newsVo = new NewsVo();
		String[] parms = new String[]{"title","content","start","end"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		newsVo = new NewsVo(req);
	
		String oldThumb = newsVo.getThumb();
		if(imageList.size() > 0){
			newsVo.setThumb(new ImageDaoImpl().addImages(imageList));
		}
	
		boolean flag = false;
		DelType type;
		if(StringUtils.isEmpty(newsVo.getId())){
			flag = new NewsDaoImpl().addNews(newsVo);
			type = DelType.DelType_Add;
		}else{
			flag = new NewsDaoImpl().updateNews(newsVo);
			type = DelType.DelType_Update;
			
			if(!StringUtils.isEmpty(oldThumb) && !oldThumb.equals(newsVo.getThumb())){
				//delete old image
				IImageDao imageDao = new ImageDaoImpl();
				
				ImageVo imageVo = imageDao.getImage(oldThumb);
				FileUtil.deleteFile(imageVo.getPath());
				//delete t_image record
				imageDao.deleteImages(oldThumb);
			}
			
		}
		setModelParams(getCurrentLangValue("setting_manage_news"), "news_list");
		returnData(resp, type, flag);
	}
	
	/**
	 * 60005
	 * @Title:        deleteNews 
	 * @Description:  删除资讯/delete information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月10日 下午3:08:13
	 */
	protected void deleteNews(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String newsId = req.getParameter(parms[0]);
		INewsDao newsDao = new NewsDaoImpl();
		
		NewsVo newsVo = newsDao.getNews(newsId);
		if(newsVo == null){
			returnParamsError(resp);
			return;
		}
		if(!StringUtils.isEmpty(newsVo.getImageVo().getPath())){
			//delete file
			FileUtil.deleteFile(newsVo.getImageVo().getPath());
			//delete t_image record
			new ImageDaoImpl().deleteImages(newsVo.getThumb());
		}
		
		boolean flag = BaseDao.deleteRecord(INewsDao.TABLE_NAME,newsId);
		setModelParams(getCurrentLangValue("setting_manage_news"), "news_list");
		returnData(resp, DelType.DelType_Delete, flag);
	}
	
	/**
	 * 60006
	 * @Title:        openOutUrl 
	 * @Description:  打开外部链接/open outside link
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月10日 下午4:57:21
	 */
	protected void openOutUrl(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"url"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		resp.sendRedirect(req.getParameter(parms[0]));
	}
	
	/**
	 * 60007
	 * @Title:        getClientRequestList 
	 * @Description:  获取请求客户端
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月9日 下午2:57:30
	 */
	protected void getClientRequestList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<RequestClientVo> clientList = new ArrayList<RequestClientVo>();
		if (ParamsFilter.clientMap != null && ParamsFilter.clientMap.size() > 0) {
			for (Entry<String, RequestClientVo> param : ParamsFilter.clientMap.entrySet()) {
				RequestClientVo client = param.getValue();
				client.setFirstDate(TimeUtil.formaStrDate(client.getFirstTime()+"",TimeUtil.Formate_YYYY_MM_dd_HH_mm_ss));
				client.setLastDate(TimeUtil.formaStrDate(client.getLastTime()+"",TimeUtil.Formate_YYYY_MM_dd_HH_mm_ss));
				clientList.add(client);
			}
		}
		req.setAttribute("clientList", clientList);
		req.getRequestDispatcher("config/clientList.jsp").forward(req, resp);
	
	}
	
	/**
	 * 60008
	 * get
	 * @Title:        toClientRequestRuleSetting 
	 * @Description:  加载请求规则设置/load request setting
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月9日 下午4:03:23
	 */
	protected void toClientRequestRuleSetting(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("REQUEST_INTERVAL", RequestClientVo.REQUEST_INTERVAL);
		req.setAttribute("REQUEST_MAX_REQUEST_COUNT", RequestClientVo.REQUEST_MAX_REQUEST_COUNT);
		req.setAttribute("OTHERS_REQUEST_MAX_REQUEST_COUNT", RequestClientVo.OTHERS_REQUEST_MAX_REQUEST_COUNT);
		req.setAttribute("SMS_REQUEST_INTERVAL", RequestClientVo.SMS_REQUEST_INTERVAL);
		req.setAttribute("SMS_COUNT_MAX_IN_DAY", RequestClientVo.SMS_COUNT_MAX_IN_DAY);
		req.getRequestDispatcher("config/requestRuleConfig.jsp").forward(req, resp);
	}
	
	/**
	 * 60008
	 * post
	 * @Title:        clientRequestRuleSetting 
	 * @Description:  请求规则设置/ request setting
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月9日 下午4:03:57
	 */
	protected void clientRequestRuleSetting(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"REQUEST_INTERVAL","REQUEST_MAX_REQUEST_COUNT","OTHERS_REQUEST_MAX_REQUEST_COUNT",
				"SMS_REQUEST_INTERVAL","SMS_COUNT_MAX_IN_DAY"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		RequestClientVo.REQUEST_INTERVAL = ValueUtil.getDouble(req.getParameter(parms[0]));
		RequestClientVo.REQUEST_MAX_REQUEST_COUNT = ValueUtil.getInt(req.getParameter(parms[1]));
		RequestClientVo.OTHERS_REQUEST_MAX_REQUEST_COUNT = ValueUtil.getInt(req.getParameter(parms[2]));
		RequestClientVo.SMS_REQUEST_INTERVAL = ValueUtil.getInt(req.getParameter(parms[3]));
		RequestClientVo.SMS_COUNT_MAX_IN_DAY = ValueUtil.getInt(req.getParameter(parms[4]));
//		setModelParams(getCurrentLangValue("setting_manage_news"), "news_list");
		returnData(resp, DelType.DelType_Update, true);
	
	}
	/**
	 * 60009
	 * get
	 * @Title:        toUpdateClientRequest 
	 * @Description:  加载请求设置页/load request setting
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月9日 下午4:13:00
	 */
	protected void toUpdateClientRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		RequestClientVo clientVo = ParamsFilter.clientMap.get(req.getParameter(parms[0]));
		req.setAttribute("clientVo", clientVo);
		req.getRequestDispatcher("config/requestConfig.jsp").forward(req, resp);
	}
	/**
	 * 60009
	 * post
	 * @Title:        updateClientRequest 
	 * @Description:   请求设置页/ request setting
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月9日 下午4:19:26
	 */
	protected void updateClientRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id","status"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String ip = req.getParameter(parms[0]);
		RequestClientVo clientVo = ParamsFilter.clientMap.get(ip);
		clientVo.setStopClient(ValueUtil.getInt(req.getParameter(parms[1])) == 1?true:false);
		ParamsFilter.clientMap.put(ip, clientVo);
		setModelParams(getCurrentLangValue("system_config_request_manage"), "client_request_list");
		returnData(resp, DelType.DelType_Update, true);
	}
	/**
	 * 60010
	 * @Title:        deleteClientRequest 
	 * @Description:  删除请求/delete request
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月9日 下午4:20:25
	 */
	protected void deleteClientRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"ids"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String ips[]= req.getParameter(parms[0]).split(",");
		for (String ip : ips) {
			ParamsFilter.clientMap.remove(ip);
		}
		setModelParams(getCurrentLangValue("system_config_request_manage"), "client_request_list");
		returnData(resp, DelType.DelType_Delete, true);
	}
	
	/**
	 * 60011
	 * @Title:        getAppVersionList 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月25日 上午11:11:39
	 */
	protected void getAppVersionList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IAppVersionDao versionDao = new AppVersionDaoImpl();
		List<AppVersionVo> versionList = versionDao.getVersionList(requestVo);
		req.setAttribute("versionList",versionList);
		requestVo.setTotalCount(versionDao.getVersionCount(requestVo));
		setModelParams("setting_version_manage", "app_version_list");
		returnDataList(req, resp, requestVo,"config/appVersionList.jsp");
	}
	
	/**
	 * 60012
	 * get
	 * @Title:        toAddAppVersion 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月25日 上午11:26:13
	 */
	protected void toAddAppVersion(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("config/addAppVersionDialog.jsp").forward(req, resp);
	}
	
	/**
	 * 60012
	 * post
	 * @Title:        addAppVersion 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月25日 上午11:26:29
	 */
	protected void addAppVersion(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"type","name","code","content","url"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		AppVersionVo versionVo = new AppVersionVo(req);
		setModelParams(getCurrentLangValue("setting_version_manage"), "app_version_list");
		String versionId = new AppVersionDaoImpl().addVersion(versionVo);
		boolean flag = false;
		if(!StringUtils.isEmpty(versionId)){
			addLogForAddData(req, resp, versionId);
			flag = true;
		}
		returnData(resp, DelType.DelType_Add, flag);
		
		//通知用户更新
		List<String> tokenList = new UserDeviceDaoImpl().getDeviceTokenList(versionVo.getType());
		
		String content = getCurrentLangValue("push_new_version_content");
		Map<String, String> customerDic = PushUtil.getPushCustomerDictionary(PushType.Push_Type_New_Version);
		if(versionVo.getType() == 1){//android
			String title = getCurrentLangValue("push_new_version_title");
			for (String token : tokenList) {
				System.out.println("fcm push:"+token);
				PushUtil.fcmPush(token, title, content, customerDic);
			}
		}else if(versionVo.getType() == 2){//ios
			for (String token : tokenList) {
				System.out.println("apple push:"+token);
				PushUtil.applePush(token, content, customerDic);
			}
		}
		
	}
	
	/**
	 * 60013
	 * @Title:        getSystemConfigList 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年5月18日 上午10:51:22
	 */
	protected void getSystemConfigList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<SystemConfigVo> configList = new SystemConfigDaoImpl().getConfigList(false);
		req.setAttribute("configList", configList);
		returnDataList(req, resp, null,"config/systemConfigList.jsp");
	}
	
	/**
	 * 60014
	 * get
	 * @Title:        toEditSystemConfig 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年5月18日 上午11:03:46
	 */
	protected void toEditSystemConfig(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		SystemConfigVo config = new SystemConfigDaoImpl().getConfig(ValueUtil.getInt(req.getParameter(parms[0])));
		req.setAttribute("config", config);
		returnDataList(req, resp, null,"config/editSystemConfig.jsp");	
	}
	
	/**
	 * 60014
	 * post
	 * @Title:        editSystemConfig 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年5月18日 上午11:04:29
	 */
	protected void editSystemConfig(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		
		String[] parms = new String[]{"id","name","config"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag;
		DelType type;
		SystemConfigVo config = new SystemConfigVo(req);
		if(config.getId() > 0){
			//edit
			flag = new SystemConfigDaoImpl().updateConfig(config);
			type = DelType.DelType_Update;
		}else{
			//add
			flag = new SystemConfigDaoImpl().addConfig(config);
			type = DelType.DelType_Add;
		}
		if(flag && !StringUtils.isEmpty(config.getConfig())){
			SystemConfigBo.getInstance().setConfigValue(config.getKey(), JSON.parseObject(config.getConfig()));
		}
		setModelParams(getCurrentLangValue("system_config_title"), "system_config_list");
		returnData(resp, type, flag);
	}
}
