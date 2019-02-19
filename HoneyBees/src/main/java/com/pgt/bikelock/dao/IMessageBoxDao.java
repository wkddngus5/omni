/**
 * FileName:     IMessageBoxDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月20日 下午4:46:56
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月20日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.MessageBoxVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     IMessageBoxDao
 * @Description:信箱业务接口定义/mailbox business interface definition
 * @author:    Albert
 * @date:        2017年7月20日 下午4:46:56
 *
 */
public interface IMessageBoxDao {
	
	final String VIEW_MESSAGE = "view_message_box";
	final String VIEW_MESSAGE_REPLY = "view_message_box_reply";
	final String CLOUMN_TITLE = "title";
	final String CLOUMN_CONTENT = "content";
	final String CLOUMN_REPLY_CONTENT = "reply_content";
	final String CLOUMN_USER_PHONE = "user_phone";
	final String CLOUMN_ADMIN_NAME = "admin_name";

	/**
	 * 
	 * @Title:        addMessage 
	 * @Description:  添加消息/add information
	 * @param:        @param messageVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月21日 下午2:50:13
	 */
	boolean addMessage(MessageBoxVo messageVo);
	
	/**
	 * 
	 * @Title:        addReplyMessage 
	 * @Description:  添加回复消息/add answer news
	 * @param:        @param messageVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月20日 下午6:15:13
	 */
	boolean addReplyMessage(MessageBoxVo messageVo);
	
	/**
	 * 
	 * @Title:        updateMessageIsRead 
	 * @Description:  修改消息为已读/modify news as have read
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月22日 上午9:53:38
	 */
	boolean updateMessageIsRead(String id);
	
	/**
	 * 
	 * @Title:        updateMessageStatus 
	 * @Description:  修改消息状态/modify news status
	 * @param:        @param id
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月1日 上午11:24:59
	 */
	boolean updateMessageStatus(String id,int status);
	
	/**
	 * 
	 * @Title:        updateAllMessageIsRead 
	 * @Description:  修改用户所有消息为已读/modify user all news as have read
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月7日 下午4:57:04
	 */
	boolean updateAllMessageIsRead(String userId);
	
	/**
	 * 
	 * @Title:        updateAllReplyIsRead 
	 * @Description:  修改用户所有回复为已读/modify user all reply as have read
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月7日 下午5:00:41
	 */
	boolean updateAllReplyIsRead(String userId);
	

	/**
	 * 
	 * @Title:        updateMessageStatus 
	 * @Description:  修改消息状态/modify news status
	 * @param:        @param id
	 * @param:        @param replay 是否已回复（管理员）/whether replied(administrator)
	 * @param:        @param isRead 是否已已读（用户）/whether have read(user)
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月22日 上午11:56:40
	 */
	boolean updateMessageStatus(String id,int replay,int isRead);
	
	/**
	 * 
	 * @Title:        getMessageList 
	 * @Description:  获取消息列表/obtain news list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<MessageBoxVo>    
	 * @author        Albert
	 * @Date          2017年7月20日 下午6:00:17
	 */
	List<MessageBoxVo> getMessageList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getWaitReplyMessageList 
	 * @Description:  获取待回复消息列表/obtain news list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<MessageBoxVo>    
	 * @author        Albert
	 * @Date          2017年7月21日 下午5:25:26
	 */
	List<MessageBoxVo> getWaitReplyMessageList(RequestListVo requestVo);
	
	
	/**
	 * 
	 * @Title:        getReplyMessageList 
	 * @Description:  获取回复消息列表/obtain reply news list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<MessageBoxVo>    
	 * @author        Albert
	 * @Date          2017年7月22日 上午10:09:24
	 */
	List<MessageBoxVo> getReplyMessageList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getWaitReplyMessageCount 
	 * @Description:  获取待回复消息总数/obtain reply news total amount
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年7月21日 下午5:27:32
	 */
	int getWaitReplyMessageCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getMessageList 
	 * @Description:  获取当前用户消息列表/obtain current user news list
	 * @param:        @param userId
	 * @param:        @param startPage
	 * @param:        @return    
	 * @return:       List<MessageBoxVo>    
	 * @author        Albert
	 * @Date          2017年7月21日 下午4:51:00
	 */
	List<MessageBoxVo> getMessageList(String userId,int startPage);
	
	/**
	 * 
	 * @Title:        getReplyMessageList 
	 * @Description:  获取回复消息列表/obtain reply news list
	 * @param:        @param messageId
	 * @param:        @param startPage
	 * @param:        @return    
	 * @return:       List<MessageBoxVo>    
	 * @author        Albert
	 * @Date          2017年7月22日 上午11:13:31
	 */
	List<MessageBoxVo> getReplyMessageList(String messageId,int startPage);
	
	/**
	 * 
	 * @Title:        获取信箱详情/obtain mailbox details 
	 * @Description:  TODO
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       MessageBoxVo    
	 * @author        Albert
	 * @Date          2017年7月22日 上午9:35:00
	 */
	MessageBoxVo getMessageInfo(String id);
	
	/**
	 * 
	 * @Title:        getNoReadMessageCount 
	 * @Description:  获取未读消息数/obtain unread news
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年8月7日 下午3:50:54
	 */
	int getNoReadMessageCount(String userId);
	
	/**
	 * 
	 * @Title:        getNoReadReplyCount 
	 * @Description:  获取未读未回复消息数/obtain unread unreply news 
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年8月7日 下午3:57:02
	 */
	int getNoReadReplyCount(String userId);
}
