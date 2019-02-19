/**
 * FileName:     RfidCardServlet.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2018年8月16日 下午4:20:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2018年8月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pgt.bikelock.dao.IRfidCardDao;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.RfidCardDaoImpl;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.RfidCardVo;

/**
 * @ClassName:     RfidCardServlet
 * @Description:TODO
 * @author:    Albert
 * @date:        2018年8月16日 下午4:20:21
 *
 */
public class RfidCardServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final int RFID_CARD_NOT_ACTIVE = 5001;
	static final int RFID_CARD_WAS_FROZEN = 5002;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//		super.doGet(req, resp);
		switch (getRequestType(req)) {
		case 101:
			getUserCardList(req, resp);
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
		//		super.doPost(req, resp);
		switch (getRequestType(req)) {
		case 102:
			addCard(req, resp);
			break;
		case 103:
			deleteCard(req, resp);
			break;
		}
	}

	/**
	 * 101
	 * get
	 * @Title:        getUserCardList 
	 * @Description:  get user rfid card list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月16日 下午4:29:01
	 */
	protected void getUserCardList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		setData(resp, new RfidCardDaoImpl().getUserCardList(getUserId(req)));
	}

	/**
	 * 102
	 * post
	 * @Title:        addCard 
	 * @Description:  user add/bind card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月16日 下午5:24:34
	 */
	protected void addCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String params[] = new String[]{"cardNo"};
		if(!checkRequstParams(req, resp, params)){
			return;
		}
		IRfidCardDao cardDao = new RfidCardDaoImpl();
		RfidCardVo cardVo = cardDao.getCardInfoWithCardNo(req.getParameter(params[0]));
		if(cardVo == null){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		int uid =  ValueUtil.getInt(getUserId(req));
		if(cardVo.getuid() != 0){
			if(cardVo.getuid() == uid){
				//card was exist
				setCode(resp, HTTP_RESULT_DATA_EXIST_ERROR);
				return;
			}else{
				//card was bind by others
				setCode(resp, HTTP_RESULT_DATA_OTHERS_USED_ERROR);
				return;
			}
		}
		if(cardVo.getStatus() == 0){
			//card wait for active
			setCode(resp, RFID_CARD_NOT_ACTIVE);
		}else if(cardVo.getStatus() == 2){
			//card was frozen
			setCode(resp, RFID_CARD_WAS_FROZEN);
		}
		setFlagData(resp, cardDao.bindCard(uid, cardVo.getId()));
	}

	/**
	 * 103
	 * post
	 * @Title:        deleteCard 
	 * @Description:  delete user card
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年8月16日 下午5:52:11
	 */
	protected void deleteCard(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String params[] = new String[]{"id"};
		if(!checkRequstParams(req, resp, params)){
			return;
		}
		setFlagData(resp, BaseDao.deleteRecord(IRfidCardDao.TABLE_NAME, req.getParameter("id")));
	}
}
