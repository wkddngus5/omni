/**
 * FileName:     MessageBoxServlet.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月21日 下午4:39:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月21日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IMessageBoxDao;
import com.pgt.bikelock.dao.impl.MessageBoxDaoImpl;
import com.pgt.bikelock.dao.impl.UserDeviceDaoImpl;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.MessageBoxVo;
import com.pgt.bikelock.vo.UserDeviceVo;

/**
 * 接口定义起点:60000/interface definition start point
 * 访问前缀 app/message
 * @ClassName:     MessageBoxServlet
 * @Description:信箱对外业务接口/mailbox outside business interface
 * @author:    Albert
 * @date:        2017年7月21日 下午4:39:55
 *
 */
public class MessageBoxServlet extends BaseServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		switch (getRequestType(req)) {
		case 60001:
			getMessageList(req, resp);
			break;
		case 60003:
			getReplyMessageList(req, resp);
			break;
		}
		doPost(req, resp);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		switch (getRequestType(req)) {
		case 60002:
			addMessage(req, resp);
			break;
		case 60004:
			replyMessage(req, resp);
			break;
		case 60005:
			updateMessageStatus(req, resp);
			break;
		}
	}
	
	/**
	 * 60001
	 * @Title:        getMessageList 
	 * @Description:  获取消息列表/obtain news list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月21日 下午4:47:41
	 */
	protected void getMessageList(final HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		final String userId = getUserId(req);
		List<MessageBoxVo> list = new MessageBoxDaoImpl().getMessageList(userId, getStartPage(req));
		if(list.size() == 0){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		setData(resp, list);
		
		new Thread(new Runnable() {
			public void run() {
				//修改消息为已读/modify news as read
				IMessageBoxDao messageDao = new MessageBoxDaoImpl();
				messageDao.updateAllMessageIsRead(userId);
				messageDao.updateAllReplyIsRead(userId);
			}
		}).start();
	}
	
	/**
	 * 60002
	 * @Title:        addMessage 
	 * @Description:  添加消息/add news
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月21日 下午4:55:26
	 */
	protected void addMessage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"title","content"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		MessageBoxVo messageVo = new MessageBoxVo(getUserId(req), req.getParameter(parms[0]), req.getParameter(parms[1]));
		boolean flag = new MessageBoxDaoImpl().addMessage(messageVo);
		setFlagData(resp, flag);
	}
	
	/**
	 * 60003
	 * @Title:        getReplyMessageList 
	 * @Description:  获取消息回复列表/obtain news reply list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月22日 上午11:12:45
	 */
	protected void getReplyMessageList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		final String messageId = req.getParameter(parms[0]);
		final IMessageBoxDao messageDao = new MessageBoxDaoImpl();
		List<MessageBoxVo> list = messageDao.getReplyMessageList(messageId, getStartPage(req));
		if(list.size() == 0){
			setCode(resp, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		setData(resp, list);
		
	/*	new Thread(new Runnable() {
			public void run() {
				//修改消息为已读/modify news as read
				MessageBoxVo messageVo = messageDao.getMessageInfo(messageId);
				if(messageVo.getIsread() == 0){
					messageDao.updateMessageIsRead(messageId);
				}
			}
		}).start();*/
	}
	
	/**
	 * 60004
	 * @Title:        replyMessage 
	 * @Description:  回复消息/reply news
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月22日 上午11:16:19
	 */
	protected void replyMessage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id","content"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		MessageBoxVo messageVo = new MessageBoxDaoImpl().getMessageInfo(req.getParameter(parms[0]));
		MessageBoxVo replyVo = new MessageBoxVo(req);
		replyVo.setAdmin_id(messageVo.getAdmin_id());
		replyVo.setUser_id(getUserId(req));
		replyVo.setMsgbox_id(req.getParameter("id"));

		IMessageBoxDao messageDao = new MessageBoxDaoImpl();
		boolean flag = messageDao.addReplyMessage(replyVo);
		if(flag){
			//修改消息为待回复状态,已读状态/modify news as reply status, read
			messageDao.updateMessageStatus(req.getParameter(parms[0]),0,1);
		}
		setFlagData(resp, flag);

	}
	
	/**
	 * 60005
	 * @Title:        updateMessageStatus 
	 * @Description:  修改消息状态/modify news status
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年9月1日 上午11:27:43
	 */
	protected void updateMessageStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id","status"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		boolean flag = new MessageBoxDaoImpl().updateMessageStatus(req.getParameter(parms[0]), 
				ValueUtil.getInt(req.getParameter(parms[1])));
		setFlagData(resp, flag);
	}
}
