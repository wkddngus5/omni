/**
 * FileName:     BikeManageServlet.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月7日 上午11:33:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月7日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialziation
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.record.formula.functions.NumericFunction.OneArg;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat.Value;
import com.omni.scooter.command.ICommand;
import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bike.lock.lib.up.DeviceType;
import com.pgt.bike.lock.lib.up.UpFileUtil;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.bo.CouponBo;
import com.pgt.bikelock.bo.CreditScoreBo;
import com.pgt.bikelock.bo.LockOrderBo;
import com.pgt.bikelock.bo.NotificationBo;
import com.pgt.bikelock.bo.PayBo;
import com.pgt.bikelock.bo.MembershipPlanBO;
import com.pgt.bikelock.dao.IAdminDao;
import com.pgt.bikelock.dao.IBikeDao;
import com.pgt.bikelock.dao.IBikeErrorDao;
import com.pgt.bikelock.dao.IBikeMaintainDao;
import com.pgt.bikelock.dao.IBikeOrderRecord;
import com.pgt.bikelock.dao.IBikeTypeDao;
import com.pgt.bikelock.dao.IBikeUseDao;
import com.pgt.bikelock.dao.IBleBikeDao;
import com.pgt.bikelock.dao.impl.AreaDaoImpl;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeErrorDaoImpl;
import com.pgt.bikelock.dao.impl.BikeMaintainDaoImpl;
import com.pgt.bikelock.dao.impl.BikeOrderRecordDaoImpl;
import com.pgt.bikelock.dao.impl.BikeSimDaoImpl;
import com.pgt.bikelock.dao.impl.BikeTypeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.dao.impl.BleBikeDaoImpl;
import com.pgt.bikelock.dao.impl.CityDaoImpl;
import com.pgt.bikelock.dao.impl.ViolationDaoImpl;
import com.pgt.bikelock.dao.impl.MembershipPlanDAO;
import com.pgt.bikelock.listener.CallBackUtils;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.listener.MyTcpCallBack;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.servlet.admin.BaseManage.DelType;
import com.pgt.bikelock.utils.AMapUtil;
import com.pgt.bikelock.utils.FileUtil;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.LockUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.AreaVo;
import com.pgt.bikelock.vo.BikeErrorVo;
import com.pgt.bikelock.vo.BikeSim;
import com.pgt.bikelock.vo.BikeTypeVo;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.BleBikeVo;
import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.ImageVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserCouponVo.GiftFromType;
import com.pgt.bikelock.vo.ViolationVo;
import com.pgt.bikelock.vo.admin.BikeMaintainVo;
import com.pgt.bikelock.vo.admin.BikeOrderRecord;
import com.pgt.bikelock.vo.admin.MapBike;
import com.pgt.bikelock.vo.admin.StatisticsVo;
import com.pgt.bikelock.vo.MembershipPlanVO;
import com.pgt.bikelock.vo.UserMembershipVO;


/*接口定义起点: 20000/interface definition start point
 * @ClassName:     BikeManageServlet
 * @Description:单车管理相关业务接口/bike manage related business interface
 * @author:    Albert
 * @date:        2017年4月7日 上午11:33:21
 *
 */
public class BikeManage extends BaseManage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BikeVo targetBikeVo;
	private String bikeImei;
	private int bikeType;//1:bicycle 2:scooter
	private String[] useStatusType;//使用状态/use normal
	private String[] violationType;//违章状态/violation status
	private String[] reportType;//故障类型/erro type
	private String[] reportStatus;//故障状态/erro status


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);


		setModelParams(getCurrentLangValue("bike_title"), "bike_list");
		switch (getRequestType(req)) {
		case 20001:
			getBikeList(req, resp);
			break;
		case 20021:
			getBikeListLookup(req, resp);
			break;
		case 20002:
			toAddBike(req, resp);
			break;
		case 20010:
			showAllBikeInMap(req, resp);
			break;
		case 20011:
			showLowPowerBikeInMap(req, resp);
			break;
		case 20012:
			showUnlockBikeInMap(req, resp);
			break;
		case 20013:
			showLockBikeInMap(req, resp);
			break;
		case 20014:
			showBikeDensityInMap(req, resp);
			break;
		case 20015:
			getBikeTypeList(req, resp);
			break;
		case 20016:
			toSetType(req, resp);
			break;
		case 20017:
			getBikeErrorList(req, resp);
			break;
		case 20018:
			toUpdateBikeErrorStatus(req, resp);
			break;
		case 20020:
			showBikeInMap(req, resp);
			break;
		case 20022:
			getBikeUseList(req, resp);
			break;
		case 20023:
			getBikeUseInfo(req, resp);
			break;
		case 20024:
			showBikeUseInMap(req, resp);
			break;
		case 20025:
			toAddVilation(req, resp);
			break;
		case 20028:
			toFinishBikeUse(req, resp);
			break;
		case 20029:
			toUpdateBikeInfo(req, resp);
			break;
		case 20030:
			getStatisticsList(req,resp);
			break;
		case 20032:
			getBikeMaintainList(req, resp);
			break;
		case 20033:
			toUpdateMaintainInfo(req, resp);
			break;
		case 20036:
			showLocationInMap(req, resp);
			break;
		case 20037:
			getBikeOrderList(req, resp);
			break;
		case 20038:
			getBikeWarnOrderList(req, resp);
			break;
		case 20040:
			showAlarmLocation(req, resp);
			break;
		case 20041:
			getBikeStatistics(req, resp);
			break;
		case 20042:
			showBikeInfoInMap(req, resp);
			break;
		case 20043:
			showBikeInfo(req, resp);
			break;
		case 20044:
			showRideStatisticsInMap(req, resp);
			break;
		case 20055:
			getMembershipPlansList(req, resp);
			break;
		case 20056:
			getUserMembershipPlansList(req, resp);
			break;
		case 20057:
			toCreateMemembership(req, resp);
			break;
		case 20102:
			toScooterSettings(req, resp);
			break;
		case 20103:
			toUpgradeTargetTypeLock(req, resp);
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
		super.doPost(req, resp);

		setModelParams(getCurrentLangValue("bike_title"), "bike_list");
		switch (getRequestType(req)) {
		case 20001:
			getBikeList(req, resp);
			break;
		case 20021:
			getBikeListLookup(req, resp);
			break;
		case 20002:
			doAddBike(req, resp);
			break;
		case 20003:
			deleteBike(req, resp);
			break;
		case 20004:
			unlockBikeOrder(req, resp);
			break;
		case 20005:
			getBikeLocationOrder(req,resp);
			break;
		case 20006:
			getBikeInfoOrder(req, resp);
			break;
		case 20007:
			getBikeVersionOrder(req, resp);
			break;
		case 20008:
			findBikeOrder(req, resp);
			break;
		case 20009:
			showDonwBikeOrder(req, resp);
			break;
		case 20015:
			getBikeTypeList(req, resp);
			break;
		case 20016:
			setModelParams(getCurrentLangValue("bike_type_title"), "type_list");
			setType(req, resp);
			break;
		case 20017:
			getBikeErrorList(req, resp);
			break;
		case 20018:
			updateBikeErrorStatus(req, resp);
			break;
		case 20019:
			deleteBikeError(req, resp);
			break;
		case 20022:
			getBikeUseList(req, resp);
			break;
		case 20025:
			addVilation(req, resp);
			break;
		case 20027:
			deleteBikeUse(req, resp);
			break;
		case 20028:
			finishBikeUse(req, resp);
			break;
		case 20029:
			updateBikeInfo(req, resp);
			break;
		case 20031:
			updateBikeVersion(req, resp);
			break;
		case 20032:
			getBikeMaintainList(req, resp);
			break;
		case 20033:
			updateMaintainInfo(req, resp);
			break;
		case 20034:
			getBikeMacOrder(req, resp);
			break;
		case 20035:
			batchDealReport(req, resp);
			break;
		case 20037:
			getBikeOrderList(req, resp);
			break;
		case 20038:
			getBikeWarnOrderList(req, resp);
			break;
		case 20039:
			deleteOrderRecord(req, resp);
			break;
		case 20041:
			getBikeStatistics(req, resp);
			break;
		case 20044:
			showRideStatisticsInMap(req, resp);
			break;
		case 20055:
			//post for refresh list
			getMembershipPlansList(req, resp);
			break;
		case 20056:
			getUserMembershipPlansList(req, resp);
			break;
		case 20057:
			createMemembership(req, resp);
			break;
		case 20100:
			lockBikeOrder(req, resp);
			break;
		case 20101:
			startUpBikeOrder(req, resp);
			break;
		case 20102:
			scooterSettings(req, resp);
			break;
		case 20103:
			upgradeTargetTypeLock(req, resp);
			break;
		default:
			break;
		}
	}

	/**
	 * 20001
	 * @Title:        getBikeList 
	 * @Description:  获取单车列表/otain bike list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 上午11:44:17
	 */
	protected void getBikeList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = doGetBikeList(req, resp);
		if(requestVo != null){

			//			requestVo.setTotalCount(returnList.size());
			returnDataList(req, resp, requestVo,"bike/bikeList.jsp");
		}

	}


	/**
	 * 20021
	 * @Title:        getBikeListLookup 
	 * @Description:  获取单车列表并返回/obtain bike list and return
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月27日 下午4:25:17
	 */
	protected void getBikeListLookup(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = doGetBikeList(req, resp);
		if(requestVo != null){
			boolean only = false;
			if(req.getParameter("only") != null){
				only = true;
			}
			req.setAttribute("only", only);
			returnDataList(req, resp, requestVo,"bike/bikeListLookup.jsp");
		}

	}

	private RequestListVo doGetBikeList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IBikeDao bikeDao = new BikeDaoImpl();
		List<BikeVo> bikeList = bikeDao.getBikeList(requestVo);

		if(req.getParameter("export") != null){//导出/export
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),"IMEI",
					getCurrentLangValue("bike_gps_time"),getCurrentLangValue("bike_gps_lng"),
					getCurrentLangValue("bike_gps_lat"),getCurrentLangValue("bike_power"),
					getCurrentLangValue("bike_gsm"),getCurrentLangValue("bike_heart_time"),
					getCurrentLangValue("bike_version_title"),getCurrentLangValue("bike_version_time"),
					getCurrentLangValue("common_add_date_title")},bikeList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					List<BikeVo> bikeList = (List<BikeVo>)list;
					for (int i = 0; i < bikeList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						BikeVo bike = bikeList.get(i);  
						//创建单元格，并设置值 /creat unit excel and set up value 
						row.createCell(0).setCellValue(bike.getNumber());  
						row.createCell(1).setCellValue(bike.getImei());  
						row.createCell(2).setCellValue(bike.getgTime());  
						row.createCell(3).setCellValue(bike.getgLng());  
						row.createCell(4).setCellValue(bike.getgLat());  
						row.createCell(5).setCellValue(bike.getPowerPercent());  
						row.createCell(6).setCellValue(bike.getGsm());  
						row.createCell(7).setCellValue(bike.getHeartTime());  
						row.createCell(8).setCellValue(bike.getVersion());  
						row.createCell(9).setCellValue(bike.getVersionTime());  
						row.createCell(10).setCellValue(bike.getAdd_date());  
					}  
				}
			});

			return null;
		}else{
			req.setAttribute("bikeList",bikeList);
			requestVo.setTotalCount(bikeDao.getCount(requestVo));
			return requestVo;
		}

	}



	/**
	 * 20002
	 * get
	 * @Title:        toAddBike 
	 * @Description:  跳转添加单车/jump add bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午1:49:11
	 */
	protected void toAddBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int industryId = getIndustryId(req, resp);
		if(industryId == 0){
			return;
		}
		RequestListVo requestVo = new RequestListVo(req,false,false);
		List<BikeTypeVo> typeList = new BikeTypeDaoImpl().getTypeList(getIndustryId(req, resp),requestVo.getCityId());
		req.setAttribute("typeList",typeList);
		req.getRequestDispatcher("bike/addDialog.jsp").forward(req, resp);
	}

	/**
	 * 20002
	 * post
	 * @Title:        doAddBike 
	 * @Description:  执行添加单车/carry out add bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午1:49:47
	 */
	protected void doAddBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"number","imei","typeId","cityId","lockType"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IBikeDao bikeDao = new BikeDaoImpl();
		BikeVo bikeVo = bikeDao.getBikeInfoWithNumber(req.getParameter(parms[0]));
		if(bikeVo != null){
			returnFail(resp, getDataExistValue());
			return;
		}
		bikeVo = new BikeVo(parms,req);
		String bikeId = bikeDao.addBike(bikeVo);
		if(!StringUtils.isEmpty(req.getParameter("mac"))){
			BleBikeVo bleVo = new BleBikeVo();
			bleVo.setBid(bikeId);
			bleVo.setMac(req.getParameter("mac"));
			new BleBikeDaoImpl().addBleBike(bleVo);
		}
		returnData(resp, DelType.DelType_Add,bikeId == null?false:true);
		if(bikeId != null){
			addLogForAddData(req, resp, bikeId);
		}
	}

	/**
	 * 20003
	 * @Title:        deleteBike 
	 * @Description:  删除单车/delete bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午1:59:46
	 */
	protected void deleteBike(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new BikeDaoImpl().deleteBike(req.getParameter(parms[0]));
		returnData(resp, DelType.DelType_Delete,flag);
	}

	/**
	 * 20004
	 * @Title:        unlockBikeOrder 
	 * @Description:  解锁设备指令/unlock device command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:05:07
	 */
	protected void unlockBikeOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendUnlockOrder(bikeType, bikeImei, 0, 0);	
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_unlock_title"));
		}
	}

	/**
	 * 20005
	 * @Title:        getBikeLocationOrder
	 * @Description:  获取设备位置指令/obtain device position command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:15:49
	 */
	protected void getBikeLocationOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendLocationOrder(bikeType, bikeImei);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_location_title"));
		}
	}

	/**
	 * 20006
	 * @Title:        getBikeInfoOrder 
	 * @Description:  获取详情指令/obtain detail command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:27:30
	 */
	protected void getBikeInfoOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendInfoOrder(bikeType, bikeImei);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_info_do_title"));
		}
	}

	/**
	 * 20007
	 * @Title:        getBikeVersionOrder 
	 * @Description:  获取设备版本指令/obtain device version command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:32:22
	 */
	protected void getBikeVersionOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendVersionOrder(bikeType, bikeImei);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_version_do_title"));
		}
	}

	/**
	 * 20008
	 * @Title:        findBikeOrder 
	 * @Description:  找车指令/find bike command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:34:01
	 */
	protected void findBikeOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendVoiceOrder(bikeType, bikeImei,2);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_find_title"));
		}
	}

	/**
	 * 20009
	 * @Title:        showDonwBikeOrder 
	 * @Description:  设备关机指令/device close command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:35:12
	 */
	protected void showDonwBikeOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendShutdownOrder(bikeType, bikeImei);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_shutdown_title"));
			if(flag){
				//标记设备待激活/sign device wait to activate
				new BikeDaoImpl().updateBikeErrorStatusWithImei(ValueUtil.getLong(bikeImei),4);
			}
		}
	}

	/**
	 * 
	 * @Title:        checkDevice 
	 * @Description:  检查设备是否存在/check device whether exist
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @return
	 * @param:        @throws IOException    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:09:43
	 */
	private boolean checkDevice(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return false;
		}
		targetBikeVo = new BikeDaoImpl().getBikeDetailInfo(req.getParameter(parms[0]));
		if(targetBikeVo == null){
			returnFail(resp, getDataNotExistValue());
			return false;
		}
		bikeImei = targetBikeVo.getImei();
		bikeType = targetBikeVo.getBikeType();
		return true;
	}

	/**
	 * 20010
	 * @Title:        showAllBikeInMap 
	 * @Description:  在地图显示所有单车/show all bike on map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午5:45:30
	 */
	protected void showAllBikeInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<BikeVo> bikeList = new BikeDaoImpl().getBikeList(0,getCityId(req),true);
		doShowBikeInMap(req, resp, bikeList);
	}

	/**
	 * 20011
	 * @Title:        showLowPowerBikeInMap 
	 * @Description:  在地图显示低电量单车/show low battery bike on map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:04:24
	 */
	protected void showLowPowerBikeInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<BikeVo> bikeList = new BikeDaoImpl().getBikeList(1,getCityId(req),true);
		doShowBikeInMap(req, resp, bikeList);
	}

	/**
	 * 20012
	 * @Title:        showUnlockBikeInMap 
	 * @Description:  在地图显示已解锁单车/show already unlock bike on map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:04:59
	 */
	protected void showUnlockBikeInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<BikeVo> bikeList = new BikeDaoImpl().getBikeList(2,getCityId(req),true);
		doShowBikeInMap(req, resp, bikeList);
	}

	/**
	 * 20013
	 * @Title:        showLockBikeInMap 
	 * @Description:  在地图显示已锁单车/show unlock bike on map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:06:15
	 */
	protected void showLockBikeInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<BikeVo> bikeList = new BikeDaoImpl().getBikeList(3,getCityId(req),true);
		doShowBikeInMap(req, resp, bikeList);
	}
	/**
	 * 
	 * @Title:        doShowBikeInMap 
	 * @Description:  执行地图展示/carry out map show
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @param bikeList
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午5:57:18
	 */
	private void doShowBikeInMap(HttpServletRequest req, HttpServletResponse resp,List<BikeVo> bikeList)
			throws ServletException, IOException {
		// 需要电量，经纬度/need power, longitude and latitude
		List<MapBike> mapBikes=new ArrayList<MapBike>();
		for(BikeVo bike :bikeList){
			MapBike mapBike =new MapBike(); 
			mapBike.setBid(bike.getBid());
			mapBike.setNumber(ValueUtil.getLong(bike.getNumber()));

			//wgs to gcj
			//			LatLng location = GpsTransformUtil.conver2Amp(new LatLng(bike.getgLat(), bike.getgLng()));
			//			mapBike.setLat(location.getLatitude());
			//			mapBike.setLng(location.getLongitude());

			mapBike.setLat(bike.getgLat());
			mapBike.setLng(bike.getgLng());
			mapBike.setIcon(getBikeIcon(bike));
			mapBikes.add(mapBike);

		}

		if(bikeList !=null && bikeList.size() > 0 
				//				&& (Math.abs(defaultLat-bikeList.get(0).getgLat()) < 10 && Math.abs(defaultLng-bikeList.get(0).getgLng()) < 10) 
				&& bikeList.size() == 1){
			req.setAttribute("centerLat",bikeList.get(0).getgLat());
			req.setAttribute("centerLng",bikeList.get(0).getgLng());
		}else{
			setDefaultLocation(req, resp);
		}

		int cityId = getCityId(req);
		if(cityId > 0 && req.getAttribute("latLngList") == null){
			CityVo cityVo = new CityDaoImpl().getCityInfo(cityId+"");
			if(cityVo != null && !StringUtils.isEmpty(cityVo.getArea_detail())){
				List<LatLng> list=AMapUtil.decode(cityVo.getArea_detail());
				req.setAttribute("latLngList",list);
			}

		}

		System.out.println("mapbikes size="+mapBikes.size());
		req.setAttribute("mapBikes", mapBikes);
		if("zh_cn".equals(OthersSource.getSourceString("default_language"))){
			req.getRequestDispatcher("bike/mapShow.jsp").forward(req, resp);
		}else{
			req.getRequestDispatcher("bike/gmapShow.jsp").forward(req, resp);
		}

	}

	/**
	 * 
	 * @Title:        getBikeIcon 
	 * @Description:  TODO
	 * @param:        @param bike
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年11月14日 上午10:28:32
	 */
	private String getBikeIcon(BikeVo bike) {
		String lockType = "bike";
		if(bike.getBikeType() == 2){
			lockType = "scooter";
		}
		String icon = lockType+"_mark_lock.png";
		if(bike.getStatus()==BikeVo.LOCK_OFF){
			icon = lockType+"_mark_lock.png";
			if(bike.getReportCount() > 0){
				icon = lockType+"_report_lock.png";
			}else if(bike.getBikeType() == 1 && bike.getPower()<=350){// <350 是低电量/low battery
				icon = lockType+"_mark_low_lock.png";
			}
		}else{
			icon = lockType+"_mark.png";
			if(bike.getReportCount() > 0){
				icon = lockType+"_report.png";
			}else if(bike.getBikeType() == 1 && bike.getPower()<=350){
				// <350 是低电量/is low battery
				icon = lockType+"_mark_low.png";
			}
		}
		if(bike.getReadpack() == 1){
			icon = lockType+"_mark_lucky.png";
		}
		return icon;
	}

	/**
	 * 20014
	 * @Title:        showBikeDensityInMap 
	 * @Description:  显示单车密度/show bike destiny
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:08:34
	 */
	protected void showBikeDensityInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		List<BikeVo> bikeList = new BikeDaoImpl().getBikeList(3,getCityId(req),true);
		List<MapBike> mapBikes=new ArrayList<MapBike>();
		for(BikeVo bike :bikeList){
			MapBike mapBike =new MapBike();
			mapBike.setLat(bike.getgLat());
			mapBike.setLng(bike.getgLng());
			mapBike.setNumber(ValueUtil.getLong(bike.getNumber()));
			mapBike.setIcon("map_star.png");
			mapBikes.add(mapBike);
		}
		req.setAttribute("centerLat",OthersSource.getSourceString("default_location_lat"));
		req.setAttribute("centerLng",OthersSource.getSourceString("default_location_lng"));
		req.setAttribute("mapBikes", mapBikes);
		int cityId = getCityId(req);
		if(cityId > 0){
			CityVo cityVo = new CityDaoImpl().getCityInfo(cityId+"");
			if(cityVo != null && !StringUtils.isEmpty(cityVo.getArea_detail())){
				List<LatLng> list=AMapUtil.decode(cityVo.getArea_detail());
				req.setAttribute("latLngList",list);
			}

		}
		if("zh_cn".equals(OthersSource.getSourceString("default_language"))){
			req.getRequestDispatcher("bike/blackMap.jsp").forward(req, resp);
		}else{
			req.getRequestDispatcher("bike/gBlackMap.jsp").forward(req, resp);
		}

	}



	/**
	 * 20015
	 * @Title:        getBikeTypeList 
	 * @Description:  类型列表/type list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午7:22:24
	 */
	protected void getBikeTypeList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int industryId = getIndustryId(req, resp);
		if(industryId == 0){
			return;
		}
		RequestListVo requestVo = new RequestListVo(req,false,true);
		List<BikeTypeVo> typeList = new BikeTypeDaoImpl().getTypeList(getIndustryId(req, resp),requestVo.getCityId());
		req.setAttribute("typeList", typeList);
		returnDataList(req, resp, null,"bike/typeList.jsp");
	}


	protected void getMembershipPlansList (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		RequestListVo requestVo = new RequestListVo(req, false, true);
		MembershipPlanDAO planDao = new MembershipPlanDAO();

		List<MembershipPlanVO> plans = planDao.getMembershipPlans(requestVo);
		req.setAttribute("membershipPlanList", plans);
		requestVo.setTotalCount(planDao.getMembershipPlansCount(requestVo));
		returnDataList(req, res, requestVo, "bike/membershipPlanList.jsp");

	}


	protected void getUserMembershipPlansList (HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException
	{

		RequestListVo requestVo = new RequestListVo(req, false, true);

		MembershipPlanDAO shipDao = new MembershipPlanDAO();

		List<UserMembershipVO> plans = shipDao.getUserMemberships(requestVo);
		req.setAttribute("userMembershipList", plans);
		requestVo.setTotalCount(shipDao.getUserMembershipsCount(requestVo));
		returnDataList(req, res, requestVo, "bike/userMembershipList.jsp");

	}


	/**
	 * 20016
	 * @Title:        toSetType 
	 * @Description:  加载类型设置/loading type set
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午7:12:45
	 */
	protected void toSetType(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!StringUtils.isEmpty(req.getParameter(parms[0]))){
			BikeTypeVo typeVo = new BikeTypeDaoImpl().getTypeInfo(req.getParameter(parms[0]));
			req.setAttribute("typeVo", typeVo);
			req.setAttribute("tagCityId", typeVo.getCity_id());
		}
		req.getRequestDispatcher("bike/editTypeDialog.jsp").forward(req, resp);
	}


	protected void toCreateMemembership(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{

		if (!StringUtils.isEmpty(req.getParameter("id"))) {
			MembershipPlanVO plan = new MembershipPlanDAO().getMembershipPlan(req.getParameter("id"));
			req.setAttribute("plan", plan);
		}
		req.getRequestDispatcher("bike/editMembershipPlanDialog.jsp").forward(req, resp);

	}

	protected void createMemembership(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{

		//set model for refresh list view
		setModelParams(getCurrentLangValue("bike_manage_membership_plans"), "membership_plans");

		if (!StringUtils.isEmpty(req.getParameter("id"))) {

			String id = ValueUtil.getString(req.getParameter("id"));
			String title = req.getParameter("title");
			String description = req.getParameter("description");
			boolean active = ValueUtil.getInt(req.getParameter("active")) == 1;
			boolean education = ValueUtil.getInt(req.getParameter("education")) == 1;

			boolean success = new MembershipPlanDAO().updateMembershipPlan(id, title, description, active,education);

			DelType type = DelType.DelType_Update;
			returnData(resp, type, success);
			return;

		}

		MembershipPlanVO plan = new MembershipPlanVO();
		plan.setInterval(ValueUtil.getInt(req.getParameter("interval")));
		plan.setIntervalCount(ValueUtil.getInt(req.getParameter("intervalCount")));
		plan.setPlanPrice(ValueUtil.getBigDecimal(req.getParameter("planPrice")));
		plan.setRideUnit(ValueUtil.getInt(req.getParameter("rideUnit")));
		plan.setRideFreeUnitCount(ValueUtil.getInt(req.getParameter("rideFreeUnitCount")));
		plan.setTitle(ValueUtil.getString(req.getParameter("title")));
		plan.setDescription(ValueUtil.getString(req.getParameter("description")));
		plan.setIsRenewable(ValueUtil.getInt(req.getParameter("isRenewable")) == 1);
		plan.setEducation(ValueUtil.getInt(req.getParameter("education")) == 1);
		plan.setCityId(ValueUtil.getString(req.getParameter("cityId")));

		String industryId = getIndustryId(req, resp) + "";
		int cityId = ValueUtil.getInt(req.getParameter("cityId"));
		String currency = PayBo.getCurrency(industryId, cityId);

		plan = new MembershipPlanBO().createMembershipPlan(plan, currency);

		DelType type = DelType.DelType_Add;

		returnData(resp, type, true);

	}

	/**
	 * 20016
	 * @Title:        setType 
	 * @Description:  执行类型设置/carry out type set
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午7:33:57
	 */
	protected void setType(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if(!adminSecurityCheck(req, resp)){
			return;
		}

		String[] parms = new String[]{
				"price","count","cityId","type","holdUnitType","holdCount","holdPrice","holdMaxCount"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IBikeTypeDao typeDao = new BikeTypeDaoImpl();
		BikeTypeVo typeVo = new BikeTypeVo(parms, req);
		int industryId = getIndustryId(req, resp);
		//		BikeTypeVo oldTypeVo = typeDao.checkTypeExist(typeVo,industryId);

		boolean flag = false;
		DelType type;
		if(!StringUtils.isEmpty(typeVo.getId())){
			//			if(oldTypeVo != null && !oldTypeVo.getId().equals(typeVo.getId())){
			//				returnFail(resp, getDataExistValue());
			//				return;
			//			}
			flag = typeDao.updateTypeInfo(typeVo);
			type = DelType.DelType_Update;
		}else{
			//			if(oldTypeVo != null){
			//				returnFail(resp, getDataExistValue());
			//				return;
			//			}
			flag = typeDao.addTypeInfo(typeVo,industryId);
			type = DelType.DelType_Add;
		}
		returnData(resp, type,flag);
	}

	/**
	 * 20017
	 * @Title:        getBikeErrorList 
	 * @Description:  获取单车报告列表/obtain bike report list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午12:38:49
	 */
	protected void getBikeErrorList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IBikeErrorDao errorDao = new BikeErrorDaoImpl();
		List<BikeErrorVo> errorList = errorDao.getBikeErrorList(requestVo);

		if(req.getParameter("export") != null){//导出/export

			if(reportType == null){
				reportStatus =  getCurrentLangValue("bike_report_review_result_value").split(",");
			}

			setModelParams(getCurrentLangValue("bike_manage_report"), "bike_error_list");
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("bike_number"),getCurrentLangValue("user_title"),
					getCurrentLangValue("common_type_title"),
					getCurrentLangValue("common_content_title"),getCurrentLangValue("common_image_title")
					,getCurrentLangValue("common_date_title"),getCurrentLangValue("common_status_title")},
					errorList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<BikeErrorVo> errorList = (List<BikeErrorVo>)list;
					for (int i = 0; i < errorList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						BikeErrorVo error = errorList.get(i);  
						//创建单元格，并设置值/create unit excel, and set up value  
						row.createCell( 0).setCellValue(error.getId());  
						row.createCell(1).setCellValue(error.getBnumber());  
						row.createCell(2).setCellValue(error.getUserVo().getPhone());  
						row.createCell(3).setCellValue(error.getTypeStr());  
						row.createCell(4).setCellValue(error.getContent());  
						String imageStr = "";
						if(error.getListImage() != null){
							for (ImageVo image : error.getListImage()) {
								imageStr += image.getPath()+";";
							}
						}

						row.createCell(5).setCellValue(imageStr); 
						row.createCell(6).setCellValue(error.getDate());
						if(error.getStatus() >= 0 && error.getStatus() < reportStatus.length){
							row.createCell(7).setCellValue(reportStatus[error.getStatus()+1]);  
						}else{
							row.createCell(7).setCellValue("");  
						}

					}  
				}
			});

		}else{
			if(req.getParameter("fromExternal") != null){
				req.setAttribute("jumpUrl", "bikeManage?requestType=20017&tagIds="+requestVo.getTagIds());
				req.getRequestDispatcher("index.jsp").forward(req, resp);
			}else{
				req.setAttribute("errorList",errorList);
				requestVo.setTotalCount(new BikeErrorDaoImpl().getCount(requestVo));
				returnDataList(req, resp, requestVo,"bike/bikeErrorList.jsp");
			}

		}

	}


	/**
	 * get
	 * 20018
	 * @Title:        toUpdateBikeErrorStatus 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月16日 下午4:41:19
	 */
	protected void toUpdateBikeErrorStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		req.setAttribute("errorVo", new BikeErrorDaoImpl().getBikeError(req.getParameter(parms[0])));

		req.getRequestDispatcher("bike/dealBikeErrorDialog.jsp").forward(req, resp);
	}
	/**
	 * post
	 * 20018
	 * @Title:        updateBikeErrorStatus 
	 * @Description:  更新故障状态/update error status
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午1:31:44
	 */
	protected void updateBikeErrorStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id","result","note","solve","number"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IBikeErrorDao errorDao = new BikeErrorDaoImpl();
		BikeErrorVo oldErrorVo = errorDao.getBikeError(req.getParameter(parms[0]));
		BikeErrorVo errorVo = new BikeErrorVo(parms, req);
		boolean flag = errorDao.updateBikeErrorStatus(errorVo);
		setModelParams(getCurrentLangValue("bike_manage_report"), "bike_error_list");
		returnData(resp, DelType.DelType_Update, flag);
		boolean solve = false;
		if(ValueUtil.getInt(req.getParameter(parms[3])) == 1){
			solve = true;
		}
		if(flag){
			addGiftToUserForReport(ValueUtil.getInt(req.getParameter(parms[1])), oldErrorVo, solve, getAdminId(req, resp), 
					req.getParameter(parms[2]));
		}
	}

	/**
	 * 
	 * @Title:        addGiftToUserForReport 
	 * @Description:  add gift to report user and notify
	 * @param:        @param reviewStatus
	 * @param:        @param oldErrorVo
	 * @param:        @param solve
	 * @param:        @param adminId
	 * @param:        @param note    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月26日 下午3:36:36
	 */
	private void addGiftToUserForReport(int reviewStatus,BikeErrorVo oldErrorVo,boolean solve,String adminId,String note){
		if(reviewStatus == 2){
			//增加积分(通过审核)/add integral(pass check)
			CreditScoreBo.addScoreRecord(oldErrorVo.getUid(), 1, 5);
			if(OthersSource.getSourceString("bike_error_coupon_num") != null){
				//奖励优惠券(praise coupon)
				CouponBo.addSystemCouponForUser(oldErrorVo.getUid(), 
						ValueUtil.getInt(OthersSource.getSourceString("bike_error_coupon_num")),GiftFromType.ErrorReport);
			}
			if(solve){
				//问题已解决,更新单车为正常状态/problem solved
				new BikeDaoImpl().updateBikeErrorStatus(oldErrorVo.getBnumber(), 0);
			}
		}

		//添加通知/add notify
		NotificationBo.addNotifiyMessage(true,adminId ,oldErrorVo.getUid(), LanguageUtil.getDefaultValue("bike_report_notify_message_title"),
				LanguageUtil.getDefaultValue("bike_report_notify_message_content", new Object[]{
						LanguageUtil.getTypeStr("bike_report_type_value", oldErrorVo.getType()),
						oldErrorVo.getDate(),LanguageUtil.getStatusStr(reviewStatus,"bike_report_review_result_value"),
						note}));
	}

	/**
	 * 20019
	 * @Title:        deleteBikeError 
	 * @Description:  删除故障记录/delete error record
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午1:47:32
	 */
	protected void deleteBikeError(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new BikeErrorDaoImpl().deleteBikeError(req.getParameter(parms[0]));
		super.modelNavTabId = "bike_error_list";
		returnData(resp, DelType.DelType_Delete, flag);
	}

	/**
	 * 20020
	 * @Title:        showBikeInMap 
	 * @Description:  显示指定单车在地图中/show bike on map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月25日 下午6:24:52
	 */
	protected void showBikeInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		List<BikeVo> bikeList = new BikeDaoImpl().getBike(req.getParameter(parms[0]));
		req.setAttribute("only",true);


		int cityId = bikeList.get(0).getCityId();

		if(cityId > 0){
			//city area
			CityVo cityVo = new CityDaoImpl().getCityInfo(cityId+"");
			List<LatLng> list=AMapUtil.decode(cityVo.getArea_detail());
			req.setAttribute("area",cityVo);
			req.setAttribute("mapZoom",10);
			req.setAttribute("latLngList",list);

			//parking area
			List<AreaVo> areaList = new AreaDaoImpl().getAreaList(cityId);
			List<Object> areaLine = new ArrayList<Object>();
			for (AreaVo areaVo : areaList) {
				Map<String, Object> areaMap = new HashMap<String, Object>();
				areaMap.put("path", AMapUtil.decode(areaVo.getDetail()));
				areaMap.put("icon", AreaVo.parkingTypeIcon[areaVo.getType()-1]);
				areaMap.put("lat", areaVo.getLat());
				areaMap.put("lng", areaVo.getLng());
				areaMap.put("color", areaVo.getType()==2?"#FF0000":"#0ABBB5");
				areaLine.add(areaMap);
			}
			if(areaLine.size() > 0){
				req.setAttribute("areaLine",areaLine);
			}
		}

		doShowBikeInMap(req, resp, bikeList);
	}

	/**下一接口20022**/

	/**
	 * 20022
	 * @Title:        getBikeUseList 
	 * @Description:  获取骑行列表/obtain riding list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月15日 下午3:54:23
	 */
	protected void getBikeUseList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,true,true);
		IBikeUseDao useDao = new BikeUseDaoImpl();
		List<BikeUseVo> useList = useDao.getBikeUseList(requestVo);
		for (BikeUseVo bikeUseVo : useList) {
			if(LockOrderBo.checkLockOnline(bikeUseVo.getBikeVo().getBikeType(), bikeUseVo.getBikeVo().getImei(), false) == 0){
				bikeUseVo.getBikeVo().setStatusStr(bikeUseVo.getBikeVo().getStatusStr()+"(Disconnect)");
			}
		}
		if(req.getParameter("export") != null){//导出/excel

			if(useStatusType == null){
				useStatusType =  getCurrentLangValue("bike_use_status_option").split(",");
				violationType =  getCurrentLangValue("credit_dec_score_type").split(",");
			}
			setModelParams(getCurrentLangValue("bike_use_title"), "bike_use_list");
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("bike_number"),getCurrentLangValue("user_title"),
					getCurrentLangValue("common_date_start_title"),getCurrentLangValue("common_date_end_title"),
					getCurrentLangValue("common_status_title"),getCurrentLangValue("violation_title")},
					useList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<BikeUseVo> useList = (List<BikeUseVo>)list;
					for (int i = 0; i < useList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						BikeUseVo bikeUse = useList.get(i);  
						//创建单元格，并设置值/create unit excel and set up value  
						row.createCell( 0).setCellValue(bikeUse.getId());  
						row.createCell(1).setCellValue(bikeUse.getBikeVo().getNumber());  
						row.createCell(2).setCellValue(!StringUtils.isEmpty(bikeUse.getUserVo().getPhone())?bikeUse.getUserVo().getPhone():
							bikeUse.getUserVo().getDetailVo().getEmail());  
						row.createCell(3).setCellValue(bikeUse.getStartTime());  
						row.createCell(4).setCellValue(bikeUse.getEndTime());  
						row.createCell(5).setCellValue(useStatusType[bikeUse.getStatus()]);
						if(bikeUse.getViolationType() > 1){
							row.createCell(6).setCellValue(violationType[bikeUse.getViolationType()-1]);  
						}

					}  
				}
			});

		}else{
			req.setAttribute("bikeUseList",useList);
			requestVo.setTotalCount(useDao.getCount(requestVo));
			returnDataList(req, resp, requestVo, "bike/bikeUseList.jsp");
		}

	}

	/**
	 * 20023
	 * @Title:        getBikeUseInfo 
	 * @Description:  获取单车使用详情/obtain bike use information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:28:59
	 */
	protected void getBikeUseInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeUseVo useVo = new BikeUseDaoImpl().getUseInfo(req.getParameter(parms[0]));
		req.setAttribute("useVo",useVo);
		req.getRequestDispatcher("bike/bikeUseInfoDialog.jsp").forward(req, resp);
	}

	/**
	 * 20024
	 * @Title:        showBikeUseInMap 
	 * @Description:  在地图中显示骑行信息/show cycling information on map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月16日 上午11:47:40
	 */
	protected void showBikeUseInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeUseVo useVo = new BikeUseDaoImpl().getUseInfo(req.getParameter(parms[0]));
		req.setAttribute("useVo",useVo);

		//ride path
		if(!StringUtils.isEmpty(useVo.getOrbit())){
			List<LatLng> pathLine = AMapUtil.decode(useVo.getOrbit());
			req.setAttribute("pathLine",pathLine);
		}

		int cityId = useVo.getBikeVo().getCityId();
		//city area
		CityVo cityVo = new CityDaoImpl().getCityInfo(cityId+"");
		if(cityVo != null && !StringUtils.isEmpty(cityVo.getArea_detail())){
			List<LatLng> cityLine = AMapUtil.decode(cityVo.getArea_detail());
			req.setAttribute("cityLine",cityLine);
		}

		//parking area
		List<AreaVo> areaList = new AreaDaoImpl().getAreaList(cityId);
		List<Object> areaLine = new ArrayList<Object>();
		for (AreaVo areaVo : areaList) {
			Map<String, Object> areaMap = new HashMap<String, Object>();
			areaMap.put("path", AMapUtil.decode(areaVo.getDetail()));
			areaMap.put("icon", AreaVo.parkingTypeIcon[areaVo.getType()-1]);
			areaMap.put("lat", areaVo.getLat());
			areaMap.put("lng", areaVo.getLng());
			areaMap.put("color", areaVo.getType()==2?"#FF0000":"#0ABBB5");
			areaLine.add(areaMap);
		}
		if(areaLine.size() > 0){
			req.setAttribute("areaLine",areaLine);
		}


		if("zh_cn".equals(OthersSource.getSourceString("default_language"))){
			req.getRequestDispatcher("bike/bikeUseMap.jsp").forward(req, resp);
		}else{
			req.getRequestDispatcher("bike/gbikeUseMap.jsp").forward(req, resp);
		}

	}


	/**
	 * get
	 * 20025
	 * @Title:        toAddVilation 
	 * @Description:  加载添加违规界面/loading add vilation interface
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月16日 下午4:41:19
	 */
	protected void toAddVilation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeUseVo useVo = new BikeUseDaoImpl().getUseInfo(req.getParameter(parms[0]));
		req.setAttribute("useVo", useVo);

		req.getRequestDispatcher("bike/addViolationDialog.jsp").forward(req, resp);
	}
	/**
	 * post
	 * 20025
	 * @Title:        addVilation 
	 * @Description:  添加违规/add violation
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月11日 下午1:31:44
	 */
	protected void addVilation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"type","useId","note","uid"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		ViolationVo violation = new ViolationVo(parms, req);

		boolean flag = new ViolationDaoImpl().addViolation(violation);
		setModelParams(getCurrentLangValue("violation_title"), "bike_use_list");
		if(flag){
			//扣除积分(确定违规)/reduce integral(make sure violation)
			CreditScoreBo.addScoreRecord(violation.getUid(), 2, violation.getType());
		}
		returnData(resp, DelType.DelType_Add, flag);

		//添加通知/add inform
		NotificationBo.addNotifiyMessage(flag,getAdminId(req, resp) ,violation.getUid(), LanguageUtil.getDefaultValue("violation_notify_title"),
				LanguageUtil.getDefaultValue("violation_notify_content", new Object[]{violation.getDate(),
						LanguageUtil.getTypeStr("credit_dec_score_type", violation.getType()-1),violation.getNote()}));
	}

	/**
	 * 20027
	 * @Title:        deleteBikeUse 
	 * @Description:  删除骑行记录（未开锁，删除开锁请求）/delete cycling record(not unlock, delete open lock request)
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月6日 下午5:39:19
	 */
	protected void deleteBikeUse(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = BaseDao.deleteRecord(IBikeUseDao.TABLE_NAME, req.getParameter(parms[0]));
		super.modelNavTabId = "bike_use_list";
		returnData(resp, DelType.DelType_Delete, flag);
	}

	/**
	 * 20028
	 * get
	 * @Title:        toFinishBikeUse 
	 * @Description:  加载结束使用记录/load finish using record
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年3月16日 上午9:35:11
	 */
	protected void toFinishBikeUse(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeUseVo useVo = new BikeUseDaoImpl().getUseInfo(req.getParameter(parms[0]));
		req.setAttribute("useVo",useVo);
		req.getRequestDispatcher("bike/finishBikeUseDialog.jsp").forward(req, resp);
	}

	/**
	 * 20028
	 * post
	 * @Title:        finishBikeUse 
	 * @Description:  结束使用记录/finish using record
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月6日 下午5:42:33
	 */
	protected void finishBikeUse(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		String[] parms = new String[]{"id","duration"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IBikeUseDao useDao = new BikeUseDaoImpl();
		BikeUseVo useVo = useDao.getUseInfo(req.getParameter(parms[0]));
		useVo = useDao.getUseIngDetailInfo(null, useVo.getUid());
		useVo.setDuration(ValueUtil.getInt(req.getParameter(parms[1])));
		useVo.setCloseWay(2);
		useVo.setAdminId(getAdminId(req, resp));
		if(useVo.getBikeVo().getBikeType() == 2){
			//滑板车发出关锁指令/send lock order
			LockOrderBo.sendLockOrder(useVo.getBikeVo().getImei(),ValueUtil.getInt(useVo.getUid()), ValueUtil.getLong(useVo.getDate()));
		}
		int result = new BikeBo().autoFinishBikeUseWithError(useVo,false);
		String resultStr = "";
		boolean flag;
		if(result == -1){
			resultStr = getUpdateResultValue(false, getCurrentLangValue("bike_use_title"));
			flag = false;
		}else{
			if(result == 0){
				resultStr = getCurrentLangValue("bike_use_finish_wait_pay");
			}else{
				resultStr = getUpdateResultValue(true, getCurrentLangValue("bike_use_title"));
			}
			flag = true;
		}

		if(flag){
			super.modelNavTabId = "bike_use_list";
			returnSuccess(resp, resultStr,true);
		}else{
			returnFail(resp, resultStr);
		}
	}


	/**
	 * 20029
	 * get
	 * @Title:        toUpdateBikeInfo 
	 * @Description:  加载更新单车/loading update bike
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月14日 下午6:27:41
	 */
	protected void toUpdateBikeInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeVo bikeVo = new BikeDaoImpl().getBikePriceInfo(req.getParameter(parms[0]));

		req.setAttribute("bikeVo", bikeVo);
		req.setAttribute("tagCityId", bikeVo.getCityId());
		req.setAttribute("connect", LockOrderBo.checkLockOnline(bikeVo.getBikeType(), bikeVo.getImei(), true));
		BleBikeVo bleVo = new BleBikeDaoImpl().getBleBike(bikeVo.getNumber());
		if(bleVo != null){
			req.setAttribute("mac", bleVo.getMac());
		}
		if(req.getParameter("fromExternal") != null){
			req.setAttribute("jumpUrl", "bikeManage?requestType=20029&isNavTab=1&id="+req.getParameter(parms[0]));
			req.getRequestDispatcher("index.jsp").forward(req, resp);
		}else{
			if(ValueUtil.getInt(req.getParameter("isNavTab")) == 1){
				req.setAttribute("isNavTab", 1);
			}
			List<BikeTypeVo> typeList = new BikeTypeDaoImpl().getTypeList(getIndustryId(req, resp),bikeVo.getCityId());
			req.setAttribute("typeList",typeList);
			req.getRequestDispatcher("bike/updateStatusDialog.jsp").forward(req, resp);
		}

	}

	/**
	 * 20029
	 * post
	 * @Title:        updateBikeInfo 
	 * @Description:  更新单车状态/update bike status
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月14日 下午6:03:40
	 */
	protected void updateBikeInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"number","status","cityId","error","type","lockType","imei"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IBikeDao bikeDao = new BikeDaoImpl();
		BikeVo bikeVo = bikeDao.getBikePriceInfo(req.getParameter("id"));
		bikeVo.setNumber(req.getParameter(parms[0]));
		bikeVo.setStatus(ValueUtil.getInt(req.getParameter(parms[1])));
		if(req.getParameter(parms[2]) != null){
			bikeVo.setCityId(ValueUtil.getInt(req.getParameter(parms[2])));
		}

		bikeVo.setError_status(ValueUtil.getInt(req.getParameter(parms[3])));
		bikeVo.setTypeId(req.getParameter(parms[4]));
		bikeVo.setBikeType(ValueUtil.getInt(req.getParameter(parms[5])));
		bikeVo.setImei(req.getParameter(parms[6]));

		boolean flag = bikeDao.updateBikeDetailInfo(bikeVo);		

		new CallBackUtils().macInfoCallback(ValueUtil.getLong(bikeVo.getImei()), ValueUtil.getString(req.getParameter("mac")),false);

		if(ValueUtil.getInt(req.getParameter("isNavTab")) == 1){
			returnDataJustTip(resp, flag, getReturnMessage(DelType.DelType_Update, flag));
		}else{
			returnData(resp, DelType.DelType_Update, flag);
		}

	}

	/**
	 * 20030
	 * @Title:        getStatisticsList 
	 * @Description:  获取单车报表/obtain bike report
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月30日 下午4:06:39
	 */
	private void getStatisticsList(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeVo bikeVo = new BikeDaoImpl().getBikeDetailInfo(req.getParameter(parms[0]));
		int type = ValueUtil.getInt(req.getParameter("type"));
		int dateType = ValueUtil.getInt(req.getParameter("dateType"));
		int dataType = ValueUtil.getInt(req.getParameter("dataType"));

		List<StatisticsVo> dataList = null,payDataList = null;

		dataList = new BikeOrderRecordDaoImpl().getStatisticsList(bikeVo.getImei(),dateType, dataType);


		if( dateType > 0){
			JSONObject dataMap= new JSONObject();
			dataMap.put("dataList", dataList);
			dataMap.put("payDataList", payDataList);
			returnAjaxData(resp, dataMap);
		}else{
			req.setAttribute("dataList", dataList);
			req.setAttribute("payDataList", payDataList);
			req.setAttribute("type", req.getParameter("type"));
			req.setAttribute("id", req.getParameter(parms[0]));
			req.getRequestDispatcher("bike/orderStatistics.jsp").forward(req, resp);
		}

	}

	/**
	 * 20031
	 * @Title:        updateBikeVersion 
	 * @Description:  更新锁版本/update lock version
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月19日 下午2:40:13
	 */
	protected void updateBikeVersion(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendUpgradeOrder(targetBikeVo);			
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_update_version_title"));
		}
	}

	/**
	 * 20032
	 * @Title:        getBikeMaintainList 
	 * @Description:  获取单车维护列表/obtain bike maintain list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午7:22:24
	 */
	protected void getBikeMaintainList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IBikeMaintainDao maintainDao = new BikeMaintainDaoImpl();
		List<BikeMaintainVo> maintainList = maintainDao.getMaintainList(requestVo);


		if(req.getParameter("export") != null){//导出/export

			setModelParams(getCurrentLangValue("bike_maintain_title"), "bike_maintain_list");

			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("bike_number"),getCurrentLangValue("nickname_title"),
					getCurrentLangValue("common_status_title"),getCurrentLangValue("common_type_title"),
					getCurrentLangValue("common_date_title"),getCurrentLangValue("common_deal_data_title")},
					maintainList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					List<BikeMaintainVo> maintainList = (List<BikeMaintainVo>)list;
					for (int i = 0; i < maintainList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						BikeMaintainVo maintainVo = maintainList.get(i);  
						//创建单元格，并设置值/create unit excel and set up value  
						row.createCell( 0).setCellValue(maintainVo.getId());  
						row.createCell(1).setCellValue(maintainVo.getNumber());  
						row.createCell(2).setCellValue(maintainVo.getNickname());  
						row.createCell(3).setCellValue(maintainVo.getStatusStr());  
						row.createCell(4).setCellValue(maintainVo.getTypeStr());  
						row.createCell(5).setCellValue(maintainVo.getDate());  
						row.createCell(6).setCellValue(maintainVo.getDeal_date());  
					}  
				}
			});
		}else{
			req.setAttribute("maintainList", maintainList);
			requestVo.setTotalCount(maintainDao.getMaintainCount(requestVo));
			returnDataList(req, resp, requestVo, "bike/bikeMaintainList.jsp");
		}
	}

	/**
	 * 20033
	 * get
	 * @Title:        toUpdateMaintainInfo 
	 * @Description:  加载更新维护界面/load update maintain page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月27日 下午6:44:28
	 */
	protected void toUpdateMaintainInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!StringUtils.isEmpty(req.getParameter("id"))){
			BikeMaintainVo maintainVo = new BikeMaintainDaoImpl().getMaintainInfo(req.getParameter("id"));
			req.setAttribute("maintainVo", maintainVo);
			req.setAttribute("type", maintainVo.getType());
		}

		req.getRequestDispatcher("bike/updateMaintainDialog.jsp").forward(req, resp);
	}

	/**
	 * 20033
	 * post
	 * @Title:        toUpdateMaintainInfo 
	 * @Description:  更新维护/update maintain
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月27日 下午6:44:28
	 */
	protected void updateMaintainInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BikeMaintainVo maintainVo = new BikeMaintainVo(req);
		IBikeMaintainDao maintainDao = new BikeMaintainDaoImpl();
		DelType delType;
		boolean flag = false;
		if(!StringUtils.isEmpty(req.getParameter("id"))){
			flag = maintainDao.updateMaintain(maintainVo);
			delType = DelType.DelType_Update;
		}else{
			String ids = maintainDao.addMaintain(maintainVo);
			delType = DelType.DelType_Add;
			if(!StringUtils.isEmpty(ids)){
				addLogForAddData(req, resp, ids);
				flag = true;
			}
		}
		setModelParams(getCurrentLangValue("bike_maintain_title"), "bike_maintain_list");
		returnData(resp, delType,flag);
	}

	/**
	 * 20034
	 * @Title:        getBikeVersionOrder 
	 * @Description:  获取设备MAC地址指令/obtain device MAC address command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午2:32:22
	 */
	protected void getBikeMacOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag= LockOrderBo.sendMacOrder(bikeType, bikeImei);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_get_mac_title"));
		}
	}

	/**
	 * 20035
	 * @Title:        batchDealReport 
	 * @Description:  批处理故障/solve error
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月30日 下午4:48:50
	 */
	protected void batchDealReport(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"ids"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		boolean flag = new BikeErrorDaoImpl().batchDealReport(req.getParameter("ids"), 2);
		setModelParams(getCurrentLangValue("bike_manage_report"), "bike_error_list");
		String message = getReturnMessage(DelType.DelType_Update, flag);
		if(flag){
			returnSuccess(resp, message,false);
			final String adminId = getAdminId(req, resp);
			final String ids[] = req.getParameter("ids").split(",");
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					IBikeErrorDao errorDao = new BikeErrorDaoImpl();
					for (String id : ids) {
						BikeErrorVo oldErrorVo = errorDao.getBikeError(id);
						addGiftToUserForReport(2, oldErrorVo, true, adminId, "");	
					}
				}
			}).start();

		}else{
			returnFail(resp, message);
		}

	}

	/**
	 * 20036
	 * @Title:        showLocationInMap 
	 * @Description:  地图显示位置/map show position
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月2日 下午2:59:05
	 */
	protected void showLocationInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"lat","lng","title"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		if(ValueUtil.getDouble(req.getParameter(parms[0])) == 0 && ValueUtil.getDouble(req.getParameter(parms[1])) == 0){
			req.setAttribute("lat",OthersSource.getSourceString("default_location_lat"));
			req.setAttribute("lng",OthersSource.getSourceString("default_location_lng"));
		}else{
			req.setAttribute("lat",req.getParameter(parms[0]));
			req.setAttribute("lng",req.getParameter(parms[1]));
		}

		req.setAttribute("title",req.getParameter(parms[2]));

		if("zh_cn".equals(OthersSource.getSourceString("default_language"))){
			req.getRequestDispatcher("others/mapShow.jsp").forward(req, resp);
		}else{
			req.getRequestDispatcher("others/gmapShow.jsp").forward(req, resp);
		}

	}

	/**
	 * 20037
	 * @Title:        getBikeOrderList 
	 * @Description:  获取单车指令/obtain bike command
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月2日 下午4:07:14
	 */
	protected void getBikeOrderList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,true,true);

		IBikeOrderRecord recordDao = new BikeOrderRecordDaoImpl();

		List<BikeOrderRecord> orderList = recordDao.getOrderRecord(requestVo);

		if(req.getParameter("export") != null){//导出/export

			setModelParams(getCurrentLangValue("bike_order_record_manage"), "bike_order_list");

			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("bike_number"),"IMEI",
					getCurrentLangValue("common_type_title"),getCurrentLangValue("common_content_title"),
					getCurrentLangValue("common_date_title")},
					orderList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					List<BikeOrderRecord> orderList = (List<BikeOrderRecord>)list;
					for (int i = 0; i < orderList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						BikeOrderRecord orderVo = orderList.get(i);  
						//创建单元格，并设置值/create unit excel and set up value  
						row.createCell( 0).setCellValue(orderVo.getId());  
						row.createCell(1).setCellValue(orderVo.getNumber());  
						row.createCell(2).setCellValue(orderVo.getImei());  
						row.createCell(3).setCellValue(orderVo.getTypeStr());  
						row.createCell(4).setCellValue(orderVo.getContent());  
						row.createCell(5).setCellValue(orderVo.getDate());  
					}  
				}
			});
		}else{
			req.setAttribute("orderList", orderList);
			int count = recordDao.getOrderRecordCount(requestVo);
			requestVo.setTotalCount(count);
			returnDataList(req, resp, requestVo, "bike/bikeOrderList.jsp");
		}

	}

	/**
	 * 20038
	 * @Title:        getBikeWarnOrderList 
	 * @Description:  获取报警信息/obtain alarm information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月14日 下午4:53:24
	 */
	protected void getBikeWarnOrderList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,true,true);
		int errorType = ValueUtil.getInt(req.getParameter("type"));//10 error 11 alarm
		requestVo.setType(errorType);

		IBikeOrderRecord recordDao = new BikeOrderRecordDaoImpl();

		List<BikeOrderRecord> orderList = recordDao.getOrderRecord(requestVo);

		if(req.getParameter("export") != null){//导出/export

			setModelParams(getCurrentLangValue("bike_alarm_manage"), "bike_alarm_list");

			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
							getCurrentLangValue("bike_number"),"IMEI",
							getCurrentLangValue("common_type_title"),getCurrentLangValue("common_content_title"),
							getCurrentLangValue("common_date_title")},
					orderList,req,resp,new ExportDataCallBack() {

						public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
							// TODO Auto-generated method stub
							List<BikeOrderRecord> orderList = (List<BikeOrderRecord>)list;
							for (int i = 0; i < orderList.size(); i++)
							{
								row = sheet.createRow((int) i + 1);
								BikeOrderRecord orderVo = orderList.get(i);
								//创建单元格，并设置值/create unit excel and set up value
								row.createCell( 0).setCellValue(orderVo.getId());
								row.createCell(1).setCellValue(orderVo.getNumber());
								row.createCell(2).setCellValue(orderVo.getImei());
								row.createCell(3).setCellValue(orderVo.getTypeStr());
								row.createCell(4).setCellValue(orderVo.getContent());
								row.createCell(5).setCellValue(orderVo.getDate());
							}
						}
					});
		}else{
			int count = recordDao.getOrderRecordCount(requestVo);

			req.setAttribute("orderList", orderList);
			requestVo.setTotalCount(count);


			if(requestVo.getPageNo() == 1){
				List<BikeOrderRecord> orderGroupList = recordDao.getWarnOrderRecordGroup();
				String imeis = "";
				int imeisCount = 0 ;
				for (BikeOrderRecord bikeOrderRecord : orderGroupList) {
					if(bikeOrderRecord.getCount() >= BikeBo.BIKE_WARN_COUNT){
						imeis += ""+bikeOrderRecord.getImei();
						imeisCount ++;
					}

				}
				imeis = imeis.replaceFirst(",", "");
				String tips = "";
				if(errorType == 11){
					tips = getCurrentLangValue("bike_order_warn_status_tips", new Object[]{addBikeWarnStatisticLink(imeisCount,imeis)});
				}else if(errorType == 10){
					tips = getCurrentLangValue("scooter_order_error_status_tips", new Object[]{addBikeWarnStatisticLink(imeisCount,imeis)});
				}
				HttpSession session = req.getSession();
				session.setAttribute("bike_order_warn_status_tips", tips);
			}
			req.setAttribute("errorType",errorType);
			returnDataList(req, resp, requestVo, "bike/bikeWarnOrderList.jsp");
		}

	}

	/**
	 * 
	 * @Title:        addBikeWarnStatisticLink 
	 * @Description:  警告统计链接/warn caculate link
	 * @param:        @param content
	 * @param:        @param ids
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年9月14日 下午5:46:23
	 */
	private String addBikeWarnStatisticLink(int content,String ids){
		return "<a href='#' style='color:green;' onclick='showTagImeisOrder(\""+ids+"\")'>  "+content+"  </a>";
	}

	/**
	 * 20039
	 * @Title:        deleteOrderRecord 
	 * @Description:  删除指令记录/delete command record
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月14日 下午6:20:48
	 */
	protected void deleteOrderRecord(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int type = ValueUtil.getInt(req.getParameter("type"));
		if(type == 0){
			returnParamsError(resp);
			return;
		}
		if(type == 1){
			super.modelNavTabId = "bike_order_record_list";
		}else{
			super.modelNavTabId = "bike_alarm_manage_list";
		}
		deleteRecord(req, resp, IBikeOrderRecord.TABLE_NAME);
	}

	/**
	 * 20040
	 * @Title:        showAlarmLocation 
	 * @Description:  Show Alarm Location
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月9日 下午2:54:54
	 */
	protected void showAlarmLocation(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"imei","id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		long imei = ValueUtil.getLong(req.getParameter(parms[0]));
		String id = req.getParameter(parms[1]);
		LatLng location = new BikeOrderRecordDaoImpl().getAlarmLocation(imei, id);
		if(location == null || location.getLatitude() == 0 && location.getLongitude() == 0){
			req.setAttribute("lat",OthersSource.getSourceString("default_location_lat"));
			req.setAttribute("lng",OthersSource.getSourceString("default_location_lng"));
		}else{
			req.setAttribute("lat",location.getLatitude());
			req.setAttribute("lng",location.getLongitude());
		}

		req.setAttribute("title",imei);

		if("zh_cn".equals(OthersSource.getSourceString("default_language"))){
			req.getRequestDispatcher("others/mapShow.jsp").forward(req, resp);
		}else{
			req.getRequestDispatcher("others/gmapShow.jsp").forward(req, resp);
		}

	}

	/**
	 * 20041
	 * @Title:        getBikeStatistics 
	 * @Description:  获取单车统计/get bike statics
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 上午11:44:17
	 */
	protected void getBikeStatistics(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//获取所有单车，统计使用情况/obtain all bike, caculate use information
		List<BikeVo> bikeList = new BikeDaoImpl().getBikeList(0, getCityId(req),false);
		int connectedCount = 0,notUsedCount = 0;
		String connectedIds="",notConnectedIds="",notUsedIds="";
		int idleDay = ValueUtil.getInt(req.getParameter("idleDay"));
		if(idleDay == 0){
			idleDay = BikeBo.BIKE_NOT_USED_IN_DAYS;
		}
		for (BikeVo bikeVo : bikeList) {
			if(LockOrderBo.checkLockOnline(bikeVo.getBikeType(), bikeVo.getImei(), false) == 0){
				connectedCount++;
				connectedIds += ","+bikeVo.getBid();
			}else{
				notConnectedIds += ","+bikeVo.getBid();
			}
			if(!new BikeBo().haveUsedInTime(bikeVo.getBid(),idleDay)){
				notUsedCount++;
				notUsedIds += ","+bikeVo.getBid();
			}
		}
		connectedIds=connectedIds.replaceFirst(",", "");
		notConnectedIds=notConnectedIds.replaceFirst(",", "");
		notUsedIds=notUsedIds.replaceFirst(",", "");
		String tips = getCurrentLangValue("bike_status_tips", new Object[]{addBikeStatisticLink(bikeList.size(),""),addBikeStatisticLink(connectedCount,connectedIds),
				addBikeStatisticLink(bikeList.size()-connectedCount,notConnectedIds),addBikeStatisticLink(notUsedCount,notUsedIds)});
		req.setAttribute("bike_status_tips", tips);
		req.setAttribute("idleDay", idleDay);

		req.getRequestDispatcher("bike/statusStatistics.jsp").forward(req, resp);

	}

	/**
	 * 
	 * @Title:        addBikeStatisticLink 
	 * @Description:  单车统计链接/bike caculate link
	 * @param:        @param content
	 * @param:        @param ids
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年9月14日 下午5:25:06
	 */
	private String addBikeStatisticLink(int content,String ids){
		String link = "<a href=\"bikeManage?requestType=20001&funcId=40&tagIds="+ids+"\" style=\"color:green;font-size: 20px;\" target=\"navTab\" mask=\"true\">  "+content+"  </a>";
		return link;
	}

	/**
	 * 20042
	 * @Title:        showBikeInfoInMap 
	 * @Description:  地图显示单个车详情
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月11日 下午3:50:17
	 */
	protected void showBikeInfoInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeVo bikeVo = new BikeDaoImpl().getBikePriceInfo(req.getParameter(parms[0]));
		bikeVo.setgTime(TimeUtil.formaStrDate(bikeVo.getgTime()));
		bikeVo.setHeartTime(TimeUtil.formaStrDate(bikeVo.getHeartTime()));
		String infoContent = "<ul>"
				+ "<li><h2>"+getCurrentLangValue("common_id_title")+":"+bikeVo.getNumber()+"</h2></li>"
				+ "<li>IMEI:"+bikeVo.getImei()+"</li>"
				+ "<li>"+getCurrentLangValue("bike_gps_time")+":"+bikeVo.getgTime()
				+"<li>"+getCurrentLangValue("bike_gps_lat")+":"+bikeVo.getgLat()
				+"<li>"+getCurrentLangValue("bike_gps_lng")+":"+bikeVo.getgLng()+"</li>"
				+"<li>"+getCurrentLangValue("bike_power")+":"+bikeVo.getPowerPercent()+"</li>"
				+"<li>"+getCurrentLangValue("bike_heart_time")+":"+bikeVo.getHeartTime()+"</li>"
				+"<li>"+getCurrentLangValue("common_status_title")+":"+bikeVo.getStatusStr()+"</li>"
				+"<li>"+getCurrentLangValue("bike_lock_error_title")+":"+bikeVo.getErrorStr()+"</li>"
				+"<li>"+getCurrentLangValue("bike_lock_type_title")+":"+bikeVo.getBikeTypeStr()+"</li>"
				//				+"<li><a href='#' style='color:green;' onclick='openDialog(\"bikeManage?requestType=20029&fromExternal=1&id="+bikeVo.getBid()+"\")' >"+getCurrentLangValue("common_update_title")+"</a></li>"
				+"<li><a style='color:green;' href=\"bikeManage?requestType=20029&fromExternal=1&id="+bikeVo.getBid()+"\" target=\"navTab\"  external=\"false\">"+getCurrentLangValue("common_update_title")+"</a></li>"
				;
		if(bikeVo.getReportCount() > 0){
			List<String> idsList = new BikeErrorDaoImpl().getWaitForDealCount(bikeVo.getNumber(),false);
			String ids = ValueUtil.listToString(idsList);
			infoContent += "<li><a  href=\"bikeManage?requestType=20017&funcId=47&fromExternal=1&tagIds="+ids+"\" target=\"navTab\"  external=\"false\">"+
					getCurrentLangValue("bike_report_statictis_in_map", new Object[]{bikeVo.getReportCount()})+"</a></li>";
		}
		if(bikeVo.getReadpack() == 0){
			infoContent += "<li><a style='color:green;' href=\"redpackManage?requestType=70008&fromExternal=1&id="
					+bikeVo.getBid()+"&number="+bikeVo.getNumber()+"\" target=\"navTab\"  external=\"false\">"
					+getCurrentLangValue("redpack_bike_set")+"</a></li>";
		}
		infoContent  += "</ul>";
		returnAjaxData(resp, infoContent);

	}

	/**
	 * 20043
	 * @Title:        showBikeInfo 
	 * @Description:  显示单车详情/Show bike detail info
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月14日 上午10:19:24
	 */
	protected void showBikeInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		BikeVo bikeVo = new BikeDaoImpl().getBikePriceInfo(req.getParameter(parms[0]));
		bikeVo.setgTime(TimeUtil.formaStrDate(bikeVo.getgTime(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
		bikeVo.setHeartTime(TimeUtil.formaStrDate(bikeVo.getHeartTime(), TimeUtil.Formate_YYYY_MM_dd_HH_mm));
		if(bikeVo.getExtendInfo() != null){
			req.setAttribute("extendInfo", bikeVo.getExtendInfo().toString());
		}
		req.setAttribute("bikeVo", bikeVo);
		req.setAttribute("tagCityId", bikeVo.getCityId());
		req.setAttribute("connect", LockOrderBo.checkLockOnline(bikeVo.getBikeType(), bikeVo.getImei(), true));
		BikeSim simVo = new BikeSimDaoImpl().getBikePhoneInfo(bikeVo.getNumber());
		if(simVo != null){
			req.setAttribute("simVo", simVo);
		}
		if(bikeVo.getReportCount() > 0){
			List<String> idsList = new BikeErrorDaoImpl().getWaitForDealCount(bikeVo.getNumber(),false);
			String ids = ValueUtil.listToString(idsList);
			req.setAttribute("reportContent", "<a  href=\"bikeManage?requestType=20017&funcId=47&tagIds="+ids+"\" target=\"navTab\" >"+
					getCurrentLangValue("bike_report_statictis_in_map", new Object[]{bikeVo.getReportCount()})+"</a>");

		}
		req.setAttribute("mapIcon", getBikeIcon(bikeVo));
		req.getRequestDispatcher("bike/detailView.jsp").forward(req, resp);

	}

	/**
	 * 20044
	 * get
	 * @Title:        toShowRideStatisticsInMap 
	 * @Description:  地图显示骑行统计/Show ride statistics in map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年12月27日 下午2:10:52
	 */
	protected void toShowRideStatisticsInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("centerLat",OthersSource.getSourceString("default_location_lat"));
		req.setAttribute("centerLng",OthersSource.getSourceString("default_location_lng"));
		req.getRequestDispatcher("bike/gMapRideStatistics.jsp").forward(req, resp);
	}

	/**
	 * 20044
	 * post
	 * @Title:        showRideStatisticsInMap 
	 * @Description:  地图显示骑行统计/Show ride statistics in map
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月21日 上午11:30:05
	 */
	protected void showRideStatisticsInMap(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		IBikeUseDao useDao = new BikeUseDaoImpl();
		//默认统计一周数据
		long endTime = TimeUtil.getCurrentLongTime();
		long startTime =  endTime-604800;
		if(!StringUtils.isEmpty(req.getParameter("startTime"))){
			startTime = ValueUtil.getLong(TimeUtil.formateStrDateToLongStr(req.getParameter("startTime"),
					TimeUtil.Formate_YYYY_MM_dd));
		}
		if(!StringUtils.isEmpty(req.getParameter("endTime"))){
			endTime = ValueUtil.getLong(TimeUtil.formateStrDateToLongStr(req.getParameter("endTime"),
					TimeUtil.Formate_YYYY_MM_dd));
		}
		List<BikeUseVo> rideList = useDao.getBikeUseList(startTime,endTime);
		List<String> idList = new ArrayList<String>();
		if(!StringUtils.isEmpty(req.getParameter("targetLocation"))){
			String areaPath = req.getParameter("targetLocation");
			for (BikeUseVo bikeUseVo : rideList) {
				for (LatLng orbit : bikeUseVo.getLatLngList()) {
					if(AMapUtil.checkPointInAreaWithDefaultRaidus(orbit, areaPath)){
						idList.add(bikeUseVo.getId());
					}
				}
			}
			List<StatisticsVo> statisticsList = useDao.getStatisticsList(ValueUtil.listToString(idList));
			returnAjaxData(resp, statisticsList);
		}else if(!StringUtils.isEmpty(req.getParameter("type"))){
			returnAjaxData(resp, rideList);
		}else{
			req.setAttribute("centerLat",OthersSource.getSourceString("default_location_lat"));
			req.setAttribute("centerLng",OthersSource.getSourceString("default_location_lng"));
			req.setAttribute("rideList", JSON.toJSON(rideList));
			req.setAttribute("startTime", TimeUtil.formaStrDate(startTime+"",TimeUtil.Formate_YYYY_MM_dd_HH_mm));
			req.setAttribute("endTime", TimeUtil.formaStrDate(endTime+"",TimeUtil.Formate_YYYY_MM_dd_HH_mm));
			req.getRequestDispatcher("bike/gMapRideStatistics.jsp").forward(req, resp);
		}

	}

	/**
	 * 20100
	 * @Title:        lockBikeOrder 
	 * @Description:  关锁指令/lock order
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月22日 下午12:00:34
	 */
	protected void lockBikeOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendLockOrder(bikeImei, 0, 0);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_lock_title"));
		}
	}

	/**
	 * 20101
	 * @Title:        startUpBikeOrder 
	 * @Description:  开机指令/startup
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月22日 下午12:01:31
	 */
	protected void startUpBikeOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(checkDevice(req, resp)){
			boolean flag = LockOrderBo.sendReStartOrder(bikeImei);
			returnDataJustTip(resp, flag, getCurrentLangValue("bike_startup_title"));
		}
	}

	/**
	 * 20102
	 * get
	 * @Title:        toSetScooterIOT 
	 * @Description:  滑板车设置/scooter settings
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月29日 上午11:29:15
	 */
	protected void toScooterSettings(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"imei","type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		long imei= ValueUtil.getLong( req.getParameter("imei"));
		JSONObject extendInfo = new BikeDaoImpl().getLockExtendInfo(imei);
		req.setAttribute("extendInfo",extendInfo.toString());
		req.setAttribute("imei",imei);
		req.setAttribute("type", req.getParameter("type"));
		req.getRequestDispatcher("bike/scooterSettingDialog.jsp").forward(req, resp);
	}

	/**
	 * 20102
	 * post
	 * @Title:        scooterSettings 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月29日 上午11:44:06
	 */
	protected void scooterSettings(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"imei","type"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = false;
		String type = req.getParameter("type");
		String tips = "";
		if("IOT".equals(type)){
			flag = LockOrderBo.sendIOTSetOrder(req);
			tips = getCurrentLangValue("bike_extend_iot_settings");
		}else if("light".equals(type)){
			flag = LockOrderBo.sendLightSetOrder(req);
			tips = getCurrentLangValue("bike_extend_light_settings");
		}else if("speed".equals(type)){
			flag = LockOrderBo.sendSpeedSetOrder(req);
			tips = getCurrentLangValue("bike_extend_speed_settings");
		}else if("key".equals(type)){
			flag = LockOrderBo.sendLockKeySetOrder(req);
			tips = getCurrentLangValue("bike_extend_key_settings");
		}else if("voice".equals(type)){
			int voiceType = ValueUtil.getInt(req.getParameter("voiceType"));
			flag = LockOrderBo.sendVoiceOrder(2, req.getParameter("imei"), voiceType);
		}else if("address".equals(type)){
			flag = LockOrderBo.sendLockAddressSetOrder(req);
		}else if("apn".equals(type)){
			flag = LockOrderBo.sendLockCardInfoSetOrder(req);
		}
		//		returnDataJustTip(resp, flag, tips);
		returnData(resp, DelType.DelType_Update, flag);
	}
	
	/**
	 * 20103
	 * get
	 * @Title:        toUpgradeTargetTypeLock 
	 * @Description:  升级目标类型锁
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年12月8日 上午10:18:08
	 */
	protected void toUpgradeTargetTypeLock(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("imei", req.getParameter("imei"));
		req.getRequestDispatcher("bike/upgradeLockDialog.jsp").forward(req, resp);
	}


	/**
	 * 20103
	 * post
	 * @Title:        upgradeTargetTypeLock 
	 * @Description:  升级目标类型滑板车
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年12月8日 上午10:18:08
	 */
	protected void upgradeTargetTypeLock(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String lockFile = FileUtil.uploadLockFile(req);
		long imei= ValueUtil.getLong(req.getAttribute("imei").toString());
		int versionType = ValueUtil.getInt(req.getAttribute("versionType"));
		String lockType = req.getAttribute("lockType").toString();
		
		int result = LockOrderBo.sendTargetTypeUpgradeOrder(imei, lockType,versionType,lockFile);
		String message = "";
		if(result == 0){
			message = "滑板车未连接[Scooter Disconnect]";
		}else if(result == 1){
			message = "发送升级指令成功[Send upgrade order success]";
		}else{
			message = "发送升级指令失败[Send upgrade order fail]";
		}
//		returnAjaxData(resp, message);
		returnSuccess(resp, message, true);
	}
}
