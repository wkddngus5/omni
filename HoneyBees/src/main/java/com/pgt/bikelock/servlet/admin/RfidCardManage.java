/**
 * FileName:     RfidCardManage.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月16日 上午11:32:40
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月16日       CQCN         1.0             1.0
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

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IRfidCardDao;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.RfidCardDaoImpl;
import com.pgt.bikelock.listener.ExportDataCallBack;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.RfidCardVo;
import com.pgt.bikelock.vo.TradeVo;


/**
 * @ClassName:     RfidCardManage
 * @Description:TODO
 * @author:    Albert
 * @date:        2018年8月16日 上午11:32:40
 *
 */
public class RfidCardManage extends BaseManage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		setModelParams(getCurrentLangValue("rfid_card_list"), "rfid_card_list");
		switch (getRequestType(req)) {
			case 101:
				getCardList(req, resp);
				break;
			case 102:
				toEditCard(req, resp);
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
		setModelParams(getCurrentLangValue("rfid_card_list"), "rfid_card_list");
		switch (getRequestType(req)) {
		case 101:
			getCardList(req, resp);
			break;
		case 102:
			editCard(req, resp);
			break;
		case 103:
			deleteCard(req, resp);
			break;
		}
	}
	
	/**
	 * 101
	 * @Title:        getCardList 
	 * @Description:  get rfid card list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:42:31
	 */
	protected void getCardList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IRfidCardDao cardDao =  new RfidCardDaoImpl();
		List<RfidCardVo> cardList = cardDao.getCardList(requestVo);
		if(req.getParameter("export") != null){//导出
			super.exportData(new String[]{getCurrentLangValue("common_id_title"),
					getCurrentLangValue("rfid_card_id"),
					getCurrentLangValue("rfid_card_no"),
					getCurrentLangValue("user_title"),
					getCurrentLangValue("common_status_title"),
					getCurrentLangValue("common_date_title")},
					cardList,req,resp,new ExportDataCallBack() {

				public void setExportData(HSSFSheet sheet, HSSFRow row, Object list) {
					// TODO Auto-generated method stub
					@SuppressWarnings("unchecked")
					List<RfidCardVo> cardList = (List<RfidCardVo>)list;
					for (int i = 0; i < cardList.size(); i++)  
					{  
						row = sheet.createRow((int) i + 1);  
						RfidCardVo card = cardList.get(i);  
						//创建单元格，并设置值/creat unit excel, and set up value
						row.createCell(0).setCellValue(card.getId());  
						row.createCell(1).setCellValue(card.getCardId());  
						row.createCell(2).setCellValue(card.getCardNo());  
						if(card.getUserVo() != null){
							row.createCell(3).setCellValue(card.getUserVo().getPhone());  
						}						
						row.createCell(4).setCellValue(LanguageUtil.getStatusStr(card.getStatus(),"rfid_card_status_value"));  
						row.createCell(5).setCellValue(TimeUtil.formaStrDate(card.getDate()));  
					}  
				}
			});
		}else{
			req.setAttribute("cardList",cardList);
			requestVo.setTotalCount(cardDao.getCardCount(requestVo));
			
			returnDataList(req, resp, requestVo, "rfid/cardList.jsp");
		}
		
	}
	
	/**
	 * 102
	 * get
	 * @Title:        toEditCard 
	 * @Description:  to edit card page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:49:26
	 */
	protected void toEditCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String id = "";
		if(!StringUtils.isEmpty(id = req.getParameter("id"))){
			RfidCardVo cardVo = new RfidCardDaoImpl().getCardInfo(id);
			req.setAttribute("cardVo", cardVo);
		}
		req.getRequestDispatcher("rfid/editCard.jsp").forward(req, resp);
	}
	
	/**
	 * 102
	 * post
	 * @Title:        editCard 
	 * @Description:  edit /add  rfid card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月16日 上午11:43:38
	 */
	protected void editCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!adminSecurityCheck(req, resp)){
			return;
		}
		RfidCardVo cardVo = new RfidCardVo(req);
		String id = "";
		boolean flag = false;
		DelType type;
		if(!StringUtils.isEmpty(id = req.getParameter("id"))){
			type = DelType.DelType_Update;
			cardVo.setId(id);
			flag = new RfidCardDaoImpl().updateCard(cardVo);
		}else{
			type = DelType.DelType_Add;
			id = new RfidCardDaoImpl().addCard(cardVo);
			if(!StringUtils.isEmpty(id)){
				addLogForAddData(req, resp, id);
				flag = true;
			}
		}
		returnData(resp, type, flag);
		
	}
	
	/**
	 * 103
	 * @Title:        deleteCard 
	 * @Description:  delete rfid card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月16日 下午1:54:37
	 */
	protected void deleteCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"ids"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String ids = req.getParameter("ids");
		boolean flag = BaseDao.deleteRecord(IRfidCardDao.TABLE_NAME, ids);
		if(flag){
			addLogForAddData(req, resp, ids);
		}
		returnData(resp, DelType.DelType_Delete,flag);
	}
}
