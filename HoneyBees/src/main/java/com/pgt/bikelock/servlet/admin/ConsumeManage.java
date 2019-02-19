/**
 * FileName:     ConsumeManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月8日 下午12:40:17
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月8日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.record.formula.functions.Odd;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.DepositBo;
import com.pgt.bikelock.bo.NotificationBo;
import com.pgt.bikelock.bo.PayBo;
import com.pgt.bikelock.bo.UserBo;
import com.pgt.bikelock.dao.ICashRecordDao;
import com.pgt.bikelock.dao.IDepositConfDao;
import com.pgt.bikelock.dao.IDepositDao;
import com.pgt.bikelock.dao.IRechargeAmountDao;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.dao.ITradeRecipt;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.CashRecordDaoImpl;
import com.pgt.bikelock.dao.impl.CouponDaoImpl;
import com.pgt.bikelock.dao.impl.DepositConfDaoImpl;
import com.pgt.bikelock.dao.impl.DepositDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.RechargeAmountDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.TradeReciptDaoImpl;
import com.pgt.bikelock.dao.impl.UserCouponDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.CashRecordVo;
import com.pgt.bikelock.vo.DepositConfVo;
import com.pgt.bikelock.vo.DepositReturnVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.RechargeAmountVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.TradeReceiptVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserCouponVo;
import com.pgt.bikelock.vo.admin.BikeMaintainVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;
/**
 * 接口定义起点40000/interface definition startpoint
 * @ClassName:     ConsumeManage
 * @Description:消费管理相关业务接口/consumption manage related business interface
 * @author:    Albert
 * @date:        2017年4月8日 下午12:40:17
 *
 */
public class ConsumeManage extends BaseManage{

	private String[] orderStatus;//订单状态/transaction status

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);

		setModelParams(getCurrentLangValue("consume_manage"), "consume_list");
		switch (getRequestType(req)) {
		case 40001:
			getConsumeList(req, resp);
			break;
		case 40002:
			getDepositConfigList(req, resp);
			break;
		case 40003:
			setModelParams(getCurrentLangValue("consume_receipt_title"), "trade_recipt_list");
			getTradeReciptList(req, resp);
			break;
		case 40006:
			getDepositReturnList(req, resp);
			break;
		case 40009:
			getRechargeAmountList(req, resp);
			break;
		case 40011:
			toUpdateRechargeAmount(req, resp);
			break;
		case 40014:
			getStatisticsList(req, resp);
			break;
		case 40015:
			toEditDepositConfig(req, resp);
			break;
		case 40017:
			toUpdateTradeInfo(req, resp);
			break;
		case 40020:
			getBalanceCashList(req, resp);
			break;
		case 40021:
			getBalanceCashDetail(req, resp);
			break;
		case 40023:
			getOrderDetail(req, resp);
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
		setModelParams(getCurrentLangValue("consume_manage"), "consume_list");
		switch (getRequestType(req)) {
		case 40001:
			getConsumeList(req, resp);
			break;
		case 40002:
			getDepositConfigList(req, resp);
			break;
		case 40003:
			getTradeReciptList(req, resp);
			break;
		case 40004:
			setModelParams(getCurrentLangValue("consume_receipt_title"), "trade_recipt_list");
			updateTradeReciptStatus(req, resp);
			break;
		case 40005:
			setModelParams(getCurrentLangValue("consume_receipt_title"), "trade_recipt_list");
			deleteTradeRecipt(req, resp);
			break;
		case 40006:
			getDepositReturnList(req, resp);
			break;
		case 40007:
			dealDepositReturn(req, resp);
			break;
		case 40008:
			deleteDepositReturn(req, resp);
			break;
		case 40009:
			getRechargeAmountList(req, resp);
			break;
		case 40010:
			deleteRechargeAmount(req, resp);
			break;
		case 40012:
			updateRechargeAmount(req, resp);
			break;
		case 40013:
			addRechargeAmount(req, resp);
			break;
		case 40015:
			editDepositConfig(req, resp);
			break;
		case 40016:
			deleteDepositConfig(req, resp);
			break;
		case 40017:
			updateTradeInfo(req, resp);
			break;
		case 40018:
			batchDealDepositReturn(req, resp);
			break;
		case 40019:
			refundDepositReturn(req, resp);
			break;
		case 40020:
			getBalanceCashList(req, resp);
			break;
		case 40022:
			refundRechargeOrder(req, resp);
			break;

		}
	}

	/**
	 * 40001
	 * @Title:        getConsumeList 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月8日 下午12:43:06
	 */
	protected void getConsumeList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		ITradeDao tradeDao = new TradeDaoImpl();
		List<TradeVo> tradeList = tradeDao.getTradeList(requestVo);

		if(req.getParameter("export") != null){//导出

			if(orderStatus == null){
				orderStatus =  getCurrentLangValue("consume_order_pay_status_value").split(",");
			}

			setModelParams(getCurrentLangValue("consume_manage"), "consume_list");
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("user_title"),
					getCurrentLangValue("consume_order_amount_title"),
					getCurrentLangValue("consume_order_account_pay_amount"),
					getCurrentLangValue("consume_order_gift_pay_amount"),
					getCurrentLangValue("common_type_title"),
					getCurrentLangValue("consume_order_pay_way_title"),getCurrentLangValue("common_status_title")
					,getCurrentLangValue("consume_order_pay_out_id"),getCurrentLangValue("common_date_title")},
					tradeList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<TradeVo> tradeList = (List<TradeVo>)list;
					for (int i = 0; i < tradeList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						TradeVo trade = tradeList.get(i);  
						//创建单元格，并设置值/creat unit excel, and set up value
						row.createCell(0).setCellValue(trade.getId());  
						row.createCell(1).setCellValue(trade.getUserVo().getPhone());  
						row.createCell(2).setCellValue(trade.getAmount()+"");  
						row.createCell(3).setCellValue(trade.getAccountPayAmount() == null?"":trade.getAccountPayAmount().toString());  
						row.createCell(4).setCellValue(trade.getGiftPayAmount() == null?"":trade.getGiftPayAmount().toString());  
						row.createCell(5).setCellValue(trade.getTypeStr());  
						row.createCell(6).setCellValue(trade.getWayStr()); 
						row.createCell(7).setCellValue(trade.getStatusStr());  
						row.createCell(8).setCellValue(trade.getOut_trade_no());  
						row.createCell(9).setCellValue(trade.getDate());  
					}  
				}
			});
		}else{
			req.setAttribute("tradeList",tradeList);
			requestVo.setTotalCount(tradeDao.getCount(requestVo));

			returnDataList(req, resp, requestVo,"consume/consumeList.jsp");
		}



	}

	/**
	 * 40002
	 * @Title:        toSetDeposit 
	 * @Description:  加载押金设置/loading deposit set up
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:21:01
	 */
	protected void getDepositConfigList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req, false, true);
		List<DepositConfVo> confList = new DepositConfDaoImpl().getDepositConfList(requestVo);
		req.setAttribute("confList",confList);
		req.getRequestDispatcher("consume/depositConfigList.jsp").forward(req, resp);
	}

	/**
	 * 40003
	 * @Title:        getTradeReciptList 
	 * @Description:  获取收据申请列表/obtain receipt apply list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @throws ServletException 
	 * @Date          2017年4月10日 下午6:29:47
	 */
	private void getTradeReciptList(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		RequestListVo requestVo = new RequestListVo(req,false,false);
		List<TradeReceiptVo> reciptList = new TradeReciptDaoImpl().getTradeReciptList(requestVo);
		req.setAttribute("reciptList",reciptList);
		requestVo.setTotalCount(BaseDao.getCount(ITradeRecipt.TABLE_NAME, requestVo, 
				new String[]{ITradeRecipt.CLOUM_ADDRESS,ITradeRecipt.CLOUM_COUNTRY,ITradeRecipt.CLOUM_FIRSTNAME,ITradeRecipt.CLOUM_LASTNAME,
				ITradeRecipt.CLOUM_PHONE}));
		returnDataList(req, resp, requestVo,"consume/tradeReciptList.jsp");
	}

	/**
	 * 40004
	 * @Title:        updateTradeReciptStatus 
	 * @Description:  修改订单收据申请状态/modify order receipt apply status
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午6:42:17
	 */
	private void updateTradeReciptStatus(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id","status"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		boolean flag = new TradeReciptDaoImpl().updateReciptStatus
				(req.getParameter(parms[0]),ValueUtil.getInt(req.getParameter(parms[1])));
		returnSuccess(resp, getUpdateResultValue(flag, getCurrentLangValue("consume_receipt_title")),false);
	}

	/**
	 * 40005
	 * @Title:        deleteTradeRecipt 
	 * @Description:  删除订单收据申请/delete order receipt apply
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月10日 下午6:42:39
	 */
	private void deleteTradeRecipt(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		boolean flag = new TradeReciptDaoImpl().deleteRecipt(req.getParameter(parms[0]));
		returnData(resp, DelType.DelType_Delete,flag);
	}

	/**
	 * 40006
	 * @Title:        getDepositReturnList 
	 * @Description:  获取押金退还列表/obtain deposit refund list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月15日 下午4:21:19
	 */
	private void getDepositReturnList(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IDepositDao depositDao = new DepositDaoImpl();
		List<DepositReturnVo> reciptList = depositDao.getReturnList(requestVo);


		if(req.getParameter("export") != null){//导出/export

			setModelParams(getCurrentLangValue("consume_manage_deposit_return_list"), "deposit_return_list");

			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("user_phone_title"),getCurrentLangValue("common_status_title"),
					getCurrentLangValue("common_date_title"),getCurrentLangValue("consume_order_pay_way_title"),
					getCurrentLangValue("consume_order_pay_out_id"),getCurrentLangValue("consume_order_pay_date")},
					reciptList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					List<DepositReturnVo> returnList = (List<DepositReturnVo>)list;
					for (int i = 0; i < returnList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						DepositReturnVo returnVo = returnList.get(i);  
						//创建单元格，并设置值/create unit excel, and set up value  
						row.createCell( 0).setCellValue(returnVo.getId());  
						row.createCell(1).setCellValue(returnVo.getUserVo().getPhone());  
						row.createCell(2).setCellValue(returnVo.getStatusStr());  
						row.createCell(3).setCellValue(returnVo.getDate());  
						row.createCell(4).setCellValue(returnVo.getTradeVo().getWayStr());  
						row.createCell(5).setCellValue(returnVo.getTradeVo().getOut_trade_no());  
						row.createCell(6).setCellValue(returnVo.getTradeVo().getDate());  
					}  
				}
			});
		}else{
			req.setAttribute("returnList",reciptList);
			requestVo.setTotalCount(depositDao.getCount(requestVo));
			returnDataList(req, resp, requestVo,"consume/depositReturnList.jsp");
		}
	}


	/**
	 * 40007
	 * @Title:        dealDepositReturn 
	 * @Description:  处理押金退还/solve deposit refund
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月15日 下午4:54:33
	 */
	private void dealDepositReturn(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id","status"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		int status = ValueUtil.getInt(req.getParameter(parms[1]));
		if(status == 0){
			return;
		}

		boolean flag = new DepositBo().dealDepositReturn(req.getParameter(parms[0]), status);
		setModelParams(getCurrentLangValue("consume_manage_deposit_return_list"), "deposit_return_list");
		returnData(resp, DelType.DelType_Review, flag);

		if(flag){
			DepositReturnVo returnVo = new DepositDaoImpl().getDepositReturnInfo(req.getParameter(parms[0]));
			//添加通知/add invoice
			NotificationBo.addNotifiyMessage(flag,getAdminId(req, resp) ,returnVo.getUid(), LanguageUtil.getDefaultValue("consume_deal_deposit_return_notify_title"),
					LanguageUtil.getDefaultValue("consume_deal_deposit_return_notify_content", 
							new Object[]{returnVo.getDate()}));
		}

	}

	/**
	 * 40008
	 * @Title:        deleteDepositReturn 
	 * @Description:  删除押金退还/delete deposit refund
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月15日 下午5:09:12
	 */
	private void deleteDepositReturn(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"ids"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		boolean flag =BaseDao.deleteRecord(IDepositDao.TABLE_DEPOSIT_RETURN, req.getParameter(parms[0]));
		setModelParams(getCurrentLangValue("consume_manage_deposit_return_list"), "deposit_return_list");
		returnData(resp, DelType.DelType_Delete,flag);
	}

	/**
	 * 40009
	 * @Title:        getRechargeAmountList 
	 * @Description:  获取充值金额列表/obtain recharge amount list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 下午4:22:25
	 */
	private void getRechargeAmountList(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		RequestListVo requestVo = new RequestListVo(req,false,true);
		List<RechargeAmountVo> amountList = new RechargeAmountDaoImpl().getAmountList(requestVo.getCityId());
		req.setAttribute("amountList",amountList);
		returnDataList(req, resp, null, "consume/rechargeAmountList.jsp");
	}

	/**
	 * 40010
	 * @Title:        deleteRechargeAmount 
	 * @Description:  删除充值金额/delete recharge amount
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 下午4:33:24
	 */
	private void deleteRechargeAmount(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag =BaseDao.deleteRecord(IRechargeAmountDao.TABLE_RECHARGE_AMOUNT, req.getParameter(parms[0]));
		setModelParams(getCurrentLangValue("consume_recharge_amount_title"), "recharge_amount_list");
		returnData(resp, DelType.DelType_Delete,flag);
	}

	/**
	 * 40011
	 * @Title:        toUpdateRechargeAmount 
	 * @Description:  加载金额详情/upload amount details
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月9日 下午4:34:31
	 */
	protected void toUpdateRechargeAmount(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RechargeAmountVo amount = new RechargeAmountVo();
		if("1".equals(req.getParameter("status"))){
			String[] parms = new String[]{"id"};
			if(!checkRequstParams(req, resp, parms)){
				return;
			}

			amount = new RechargeAmountDaoImpl().getAmount(req.getParameter(parms[0]));
		}
		req.setAttribute("amount", amount);
		req.setAttribute("tagCityId", amount.getCityId());
		req.getRequestDispatcher("consume/amountDialog.jsp").forward(req, resp);
	}


	/**
	 * 40012
	 * @Title:        updateRechargeAmount 
	 * @Description:  修改金额/modify amount of money
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @throws ServletException 
	 * @Date          2017年4月7日 下午6:35:49
	 */
	protected void updateRechargeAmount(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{

		if(!adminSecurityCheck(req, resp)){
			return;
		}

		String[] parms = new String[]{"id","amount","gift"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		RechargeAmountVo amountVo = new RechargeAmountVo(req);
		boolean flag = new RechargeAmountDaoImpl().updateAmount(amountVo);
		setModelParams(getCurrentLangValue("consume_recharge_amount_title"), "recharge_amount_list");
		returnData(resp,DelType.DelType_Update, flag);
	}

	/**
	 * 40013
	 * @Title:        addRechargeAmount 
	 * @Description:  增加金额/add amount of money
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @throws ServletException 
	 * @Date          2017年5月9日 下午4:46:46
	 */
	private void addRechargeAmount(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{

		if(!adminSecurityCheck(req, resp)){
			return;
		}

		String[] parms = new String[]{"amount","gift"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		RechargeAmountVo amountVo = new RechargeAmountVo(req);
		String amountId = new RechargeAmountDaoImpl().addAmount(amountVo);
		setModelParams(getCurrentLangValue("consume_recharge_amount_title"), "recharge_amount_list");
		returnData(resp,DelType.DelType_Add, amountId == null?false:true);
		if(amountId != null){
			addLogForAddData(req, resp, amountId);
		}
	}

	/**
	 * 40014
	 * @Title:        getStatisticsList 
	 * @Description:  获取订单报表/obtain order list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月30日 下午4:06:39
	 */
	private void getStatisticsList(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		int type = ValueUtil.getInt(req.getParameter("type"));
		int dateType = ValueUtil.getInt(req.getParameter("dateType"));
		int payType = ValueUtil.getInt(req.getParameter("payType"));
		String date = ValueUtil.getString(req.getParameter("date"));
		List<StatisticsVo> dataList = null,payDataList = null;
		if(type == 1){//订单统计/order caculate
			ITradeDao tradeDao = new TradeDaoImpl();
			dataList = tradeDao.getStatisticsList(dateType, payType,date);
			payDataList = tradeDao.getPayWayStatisticsList(dateType, payType);
		}else if(type == 2){//押金退款统计/deposit refund cacualate
			dataList = BaseDao.getStatisticsList(IDepositDao.TABLE_DEPOSIT_RETURN,null,dateType,date);
		}

		if(dateType > 0 || payType > 0){
			JSONObject dataMap= new JSONObject();
			dataMap.put("dataList", dataList);
			dataMap.put("payDataList", payDataList);
			returnAjaxData(resp, dataMap);
		}else{
			req.setAttribute("dataList", dataList);
			req.setAttribute("payDataList", payDataList);
			req.setAttribute("type", req.getParameter("type"));
			req.getRequestDispatcher("consume/statisticsView.jsp").forward(req, resp);
		}

	}

	/**
	 * get
	 * 40015
	 * @Title:        editDepositConfig 
	 * @Description:  押金设置/deposit setup
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月3日 下午2:57:36
	 */
	private void toEditDepositConfig(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		if(!StringUtils.isEmpty(req.getParameter("id"))){
			DepositConfVo config = new DepositConfDaoImpl().getDepositConf(ValueUtil.getInt(req.getParameter("id")));
			req.setAttribute("config", config);
			req.setAttribute("tagCityId", config.getCity_id());
		}

		req.getRequestDispatcher("consume/configDepositDialog.jsp").forward(req, resp);
	}

	/**
	 * post
	 * 40015
	 * @Title:        editDepositConfig 
	 * @Description:  押金设置/deposit deposit
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月3日 下午3:25:15
	 */
	private void editDepositConfig(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{

		if(!adminSecurityCheck(req, resp)){
			return;
		}

		DepositConfVo confVo = new DepositConfVo(req);
		IDepositConfDao configDao = new DepositConfDaoImpl();
		DepositConfVo oldConf = configDao.getDepositConfWithCityId(confVo.getCity_id());
		if(oldConf != null && oldConf.getId() != confVo.getId()){
			returnFail(resp, getCurrentLangValue("consume_deposit_city_exist_title"));
			return;
		}
		boolean flag = false;
		DelType delType;

		if(confVo.getId() > 0){
			flag = configDao.updateDepositConf(confVo);
			delType = DelType.DelType_Update;
		}else{

			flag = configDao.addDepositConf(confVo);
			delType = DelType.DelType_Add;
		}

		setModelParams(getCurrentLangValue("consume_manage_deposit_setting"), "deposit_conf_list");
		returnData(resp,delType, flag);
	}

	/**
	 * 40016
	 * @Title:        deleteDepositConfig 
	 * @Description:  删除押金设置/delete deposit set up
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月3日 下午3:30:16
	 */
	private void deleteDepositConfig(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}

		boolean flag =BaseDao.deleteRecord(IDepositConfDao.table_name, req.getParameter(parms[0]));
		setModelParams(getCurrentLangValue("consume_manage_deposit_setting"), "deposit_conf_list");
		returnData(resp, DelType.DelType_Delete,flag);
	}

	/**
	 * 40017
	 * get
	 * @Title:        toUpdateTradeInfo 
	 * @Description:  加载修改订单信息页/upload modify order information page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月24日 下午5:17:15
	 */
	private void toUpdateTradeInfo(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		TradeVo tradeVo = new TradeDaoImpl().getTradeInfo(req.getParameter(parms[0]));
		req.setAttribute("tradeVo", tradeVo);
		req.getRequestDispatcher("consume/updateTradeDialog.jsp").forward(req, resp);
	}

	/**
	 * 40017
	 * post
	 * @Title:        updateTradeInfo 
	 * @Description:  修改订单信息/modify order information
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月24日 下午4:13:27
	 */
	private void updateTradeInfo(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{

		if(!adminSecurityCheck(req, resp)){
			return;
		}

		String[] parms = new String[]{"id","amount","status"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new TradeDaoImpl().updateTradeInfo(req.getParameter(parms[0]),
				new BigDecimal(req.getParameter(parms[1])),ValueUtil.getInt(req.getParameter(parms[2])));
		setModelParams(getCurrentLangValue("consume_manage"), "consume_list");
		returnData(resp, DelType.DelType_Update,flag);
	}

	/**
	 * 40018
	 * @Title:        batchDealDepositReturn 
	 * @Description:  批量处理押金退款/solve deposit refund
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月30日 下午6:10:43
	 */
	protected void batchDealDepositReturn(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"ids"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String ids[] = req.getParameter(parms[0]).split(",");
		int count = 0;
		for (String id : ids) {
			boolean flag = new DepositBo().dealDepositReturn(id, 1);
			if(flag){
				count ++;
				DepositReturnVo returnVo = new DepositDaoImpl().getDepositReturnInfo(req.getParameter(parms[0]));
				//添加通知/add inform
				NotificationBo.addNotifiyMessage(flag,getAdminId(req, resp) ,returnVo.getUid(), LanguageUtil.getDefaultValue("consume_deal_deposit_return_notify_title"),
						LanguageUtil.getDefaultValue("consume_deal_deposit_return_notify_content", 
								new Object[]{returnVo.getDate()}));
			}
		}
		setModelParams(getCurrentLangValue("consume_manage_deposit_return_list"), "deposit_return_list");
		String message = String.format(getCurrentLangValue("consume_manage_deposit_batch_return_tips"), ids.length,count);
		returnSuccess(resp, message,false);

	}

	/**
	 * 40019
	 * @Title:        refundDepositReturn 
	 * @Description:  已退还押金申请/already refund deposit apply
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月21日 上午11:13:59
	 */
	protected void refundDepositReturn(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"ids"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		int count = new DepositDaoImpl().refundDeposit(req.getParameter(parms[0]));
		String message = String.format(getCurrentLangValue("consume_manage_deposit_batch_return_tips"), count,count);
		setModelParams(getCurrentLangValue("consume_manage_deposit_return_list"), "deposit_return_list");
		returnSuccess(resp, message,false);
	}

	/**
	 * 40020
	 * @Title:        getBalanceCashList 
	 * @Description:  获取余额退款列表/Get Balance Cash List
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException
	 * @param:        @throws ServletException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月3日 下午6:20:55
	 */
	private void getBalanceCashList(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException{
		RequestListVo requestVo = new RequestListVo(req,false,true);
		requestVo.setType(2);
		ICashRecordDao cashDao = new CashRecordDaoImpl();
		List<CashRecordVo> cashList = cashDao.getCashList(requestVo);
		req.setAttribute("cashList",cashList);
		requestVo.setTotalCount(cashDao.getCashCount(requestVo));
		returnDataList(req, resp, requestVo, "consume/balanceReturnList.jsp");
	}

	/**
	 * 40021
	 * @Title:        getBalanceCashDetail 
	 * @Description:  获取余额退款详情/Get Balance Cash Detail 
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月3日 下午8:39:32
	 */
	protected void getBalanceCashDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		
		CashRecordVo cashVo = new CashRecordDaoImpl().getCashDetail(req.getParameter(parms[0]));
		if(cashVo.getStatus() == 0){
			//get user current balance
			BigDecimal amount = new UserDaoImpl().getMoneyAmount(cashVo.getUid());
			if(amount.compareTo(cashVo.getAmount()) == -1){
				cashVo.setRefund_amount(amount);
			}else{
				cashVo.setRefund_amount(cashVo.getAmount());
			}
			
		}

		//get recharge trade list
		List<TradeVo> tradeList = new TradeDaoImpl().getUserRechargeSuccessTradeList(cashVo.getUid());
		req.setAttribute("cashVo", cashVo);
		req.setAttribute("tradeList", tradeList);
		req.getRequestDispatcher("consume/balanceReturnDialog.jsp").forward(req, resp);
	}

	/**
	 * 40022
	 * @Title:        refundRechargeOrder 
	 * @Description:  充值退款/Refund Order
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月6日 下午2:31:02
	 */
	protected void refundRechargeOrder(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"refundId","userId","amount"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		setModelParams(getCurrentLangValue("consume_manage_balance_return_list"), "consume_manage_balance_return_list");
		returnAjaxData(resp, UserBo.balanceRefund(req.getParameter(parms[1]), 
				new BigDecimal(req.getParameter(parms[2])), req.getParameter(parms[0])));
	}
	
	/**
	 * 40023
	 * @Title:        getOrderDetail 
	 * @Description:  获取订单详情/Get Order Detail 
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月3日 下午8:39:32
	 */
	protected void getOrderDetail(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		ITradeDao tradeDao = new TradeDaoImpl();
		TradeVo tradeVo = tradeDao.getTradeDetailInfo(req.getParameter(parms[0]));
		if(tradeVo.getWay() == 8 && !StringUtils.isEmpty(tradeVo.getOut_pay_id())){
			UserCouponVo couponVo = new UserCouponDaoImpl().getUserCouponDetail(tradeVo.getOut_pay_id());
			if(couponVo != null){
				req.setAttribute("couponVo",couponVo);
			}
		}
		req.setAttribute("tradeVo",tradeVo);
		req.getRequestDispatcher("consume/tradeDetailView.jsp").forward(req, resp);
	}
	
}
