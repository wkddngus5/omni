/**
 * FileName:     MessageBoxServelet.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月20日 下午6:21:23
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月20日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.bo.NotificationBo;
import com.pgt.bikelock.dao.ICouponDao;
import com.pgt.bikelock.dao.IMessageBoxDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.impl.BaseDao;
import com.pgt.bikelock.dao.impl.MessageBoxDaoImpl;
import com.pgt.bikelock.dao.impl.UserCouponDaoImpl;
import com.pgt.bikelock.servlet.admin.BaseManage.DelType;
import com.pgt.bikelock.vo.MessageBoxVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserCouponVo;

/**
 * 接口定义起点 90000/interface definition startpoint 90000
 * @ClassName:     MessageBoxManage
 * @Description:信箱管理/mailbox manage
 * @author:    Albert
 * @date:        2017年7月20日 下午6:21:23
 *
 */
public class MessageBoxManage  extends BaseManage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);

		setModelParams(getCurrentLangValue("message_box_title"), "message_box_list");
		switch (getRequestType(req)) {
		case 90001:
			getMessageBoxList(req, resp);
			break;
		case 90002:
			toAddMessage(req, resp);
			break;
		case 90003:
			getWaitReplyMessageBoxList(req, resp);
			break;
		case 90004:
			toReplyMessage(req, resp);
			break;
		case 90005:
			getReplyMessageBoxList(req, resp);
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
		setModelParams(getCurrentLangValue("message_box_title"), "message_box_list");
		switch (getRequestType(req)) {
		case 90001:
			getMessageBoxList(req, resp);
			break;
		case 90002:
			addMessage(req, resp);
			break;
		case 90003:
			getWaitReplyMessageBoxList(req, resp);
			break;
		case 90004:
			replyMessage(req, resp);
			break;
		case 90005:
			getReplyMessageBoxList(req, resp);
			break;
		}
	}

	/**
	 * 90001
	 * @Title:        getMessageBoxList 
	 * @Description:  获取信箱列表/obtain mailbox list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月20日 下午6:24:45
	 */
	protected void getMessageBoxList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		List<MessageBoxVo> list = new MessageBoxDaoImpl().getMessageList(requestVo);
		req.setAttribute("messageList",list);
		requestVo.setTotalCount(BaseDao.getCount(IMessageBoxDao.VIEW_MESSAGE, requestVo, 
				new String[]{IMessageBoxDao.CLOUMN_TITLE,IMessageBoxDao.CLOUMN_CONTENT
				,IMessageBoxDao.CLOUMN_USER_PHONE,IMessageBoxDao.CLOUMN_ADMIN_NAME}));
		returnDataList(req, resp, requestVo,"messagebox/messageList.jsp");
	}

	/**
	 * 90002
	 * get
	 * @Title:        toAddMessage 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月21日 下午3:21:30
	 */
	protected void toAddMessage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.getRequestDispatcher("messagebox/addMessageDialog.jsp").forward(req, resp);
	}

	/**
	 * 90002
	 * post
	 * @Title:        addMessage 
	 * @Description:  添加消息/add news
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月21日 下午3:12:33
	 */
	protected void addMessage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"userVo.id","title","content"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		MessageBoxVo messageVo = new MessageBoxVo(req);
		messageVo.setAdmin_id(getAdminId(req, resp));
		messageVo.setUser_id(req.getParameter("userVo.id"));
		messageVo.setReply(1);
//		boolean flag = new MessageBoxDaoImpl().addMessage(messageVo);
		boolean flag = NotificationBo.sendMessage(messageVo.getAdmin_id(), messageVo.getUser_id(), messageVo.getTitle(), messageVo.getContent());
		returnData(resp, DelType.DelType_Add,flag?true:false);
		
		
	}

	/**
	 * 90003
	 * @Title:        getMessageBoxList 
	 * @Description:  获取待审核信箱列表/obtain waiting mailbox item list
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月21日 下午5:30:04
	 */
	protected void getWaitReplyMessageBoxList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IMessageBoxDao messageDao = new MessageBoxDaoImpl();
		List<MessageBoxVo> list = messageDao.getWaitReplyMessageList(requestVo);
		req.setAttribute("messageList",list);
		requestVo.setTotalCount(messageDao.getWaitReplyMessageCount(requestVo));

		returnDataList(req, resp, requestVo,"messagebox/waitReplyMessageList.jsp");
	}

	/**
	 * 90004
	 * get
	 * @Title:        toReplyMessage 
	 * @Description:  加载回复消息界面/loading reply news page
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月22日 上午9:38:20
	 */
	protected void toReplyMessage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		IMessageBoxDao messageDao = new MessageBoxDaoImpl();
		MessageBoxVo messageVo = messageDao.getMessageInfo(req.getParameter(parms[0]));
		req.setAttribute("messageVo", messageVo);
		if(messageVo != null){
			List<MessageBoxVo> list = messageDao.getReplyMessageList(req.getParameter(parms[0]), 0);
			req.setAttribute("messageList", list);
		}
		req.getRequestDispatcher("messagebox/replyMessageDialog.jsp").forward(req, resp);
	}

	/**
	 * 90004
	 * post
	 * @Title:        replyMessage 
	 * @Description:  回复消息/reply news
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月22日 上午9:31:26
	 */
	protected void replyMessage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] parms = new String[]{"id","content"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		MessageBoxVo messageVo = new MessageBoxDaoImpl().getMessageInfo(req.getParameter(parms[0]));
		MessageBoxVo replyVo = new MessageBoxVo(req);
		replyVo.setAdmin_id(getAdminId(req, resp));
		replyVo.setUser_id(messageVo.getUser_id());
		replyVo.setMsgbox_id(req.getParameter("id"));

		IMessageBoxDao messageDao = new MessageBoxDaoImpl();
		boolean flag = messageDao.addReplyMessage(replyVo);

		if(flag && messageVo.getReply() == 0){
			//修改消息为已回复状态,未读状态/modify news as already replied status, unread status
			messageDao.updateMessageStatus(req.getParameter(parms[0]),1,0);
		}
		setModelParams(getCurrentLangValue("message_reply_title"), "wait_reply_message_box_list");
		returnData(resp, DelType.DelType_Add,flag?true:false);


	}
	
	/**
	 * 90005
	 * @Title:        getReplyMessageBoxList 
	 * @Description:  获取回复消息列表/obtain reply news item
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws ServletException
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月22日 上午10:14:57
	 */
	protected void getReplyMessageBoxList(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		RequestListVo requestVo = new RequestListVo(req,false,true);
		IMessageBoxDao messageDao = new MessageBoxDaoImpl();
		List<MessageBoxVo> list = messageDao.getReplyMessageList(requestVo);
		req.setAttribute("messageList",list);
		requestVo.setTotalCount(BaseDao.getCount(IMessageBoxDao.VIEW_MESSAGE_REPLY, requestVo, 
				new String[]{IMessageBoxDao.CLOUMN_TITLE,IMessageBoxDao.CLOUMN_CONTENT,IMessageBoxDao.CLOUMN_REPLY_CONTENT
				,IMessageBoxDao.CLOUMN_USER_PHONE,IMessageBoxDao.CLOUMN_ADMIN_NAME}));
		returnDataList(req, resp, requestVo,"messagebox/replyMessageList.jsp");
	}
}
