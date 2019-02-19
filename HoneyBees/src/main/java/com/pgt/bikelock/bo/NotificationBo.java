/**
 * FileName:     NotifyBo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年8月1日 上午10:31:52
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年8月1日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.bo;

import java.util.List;
import java.util.Map;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IMessageBoxDao;
import com.pgt.bikelock.dao.impl.MessageBoxDaoImpl;
import com.pgt.bikelock.dao.impl.NotificationMappingDaoImpl;
import com.pgt.bikelock.dao.impl.UserDeviceDaoImpl;
import com.pgt.bikelock.utils.EmailUtil;
import com.pgt.bikelock.utils.PushUtil;
import com.pgt.bikelock.utils.PushUtil.PushType;
import com.pgt.bikelock.vo.EmailVo;
import com.pgt.bikelock.vo.MessageBoxVo;
import com.pgt.bikelock.vo.UserDeviceVo;
import com.pgt.bikelock.vo.admin.AdminVo;
import com.pgt.bikelock.vo.admin.NotificationConfigVo;
import com.pgt.bikelock.vo.admin.NotificationConfigVo.NotificationType;

 /**
 * @ClassName:     NotificationBo
 * @Description:通知控制器/notification controller
 * @author:    Albert
 * @date:        2017年8月1日 上午10:31:52
 *
 */
public class NotificationBo {

	private NotificationMappingDaoImpl notifyDao = new NotificationMappingDaoImpl();
	private UserBo userBo = new UserBo();
	private SmsBo smgBo = new SmsBo();
	private EmailBo emailBo = new EmailBo();

	/**
	 * 
	 * @Title:        addNotifiyMessage 
	 * @Description:  添加通知消息/add inform news
	 * @param:        @param flag
	 * @param:        @param adminId
	 * @param:        @param userId
	 * @param:        @param title
	 * @param:        @param content
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月1日 下午2:09:31
	 */
	public static void addNotifiyMessage(boolean flag,final String adminId,final String userId,final String title,final String content){
		if(!flag){
			return;
		}
		new Thread(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				sendMessage(adminId, userId, title, content);
			}
		}).start();
		
	}
	
	/**
	 * 
	 * @Title:        sendMessage 
	 * @Description:  TODO
	 * @param:        @param adminId
	 * @param:        @param userId
	 * @param:        @param title
	 * @param:        @param content
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年5月8日 下午6:29:08
	 */
	public static boolean sendMessage(final String adminId,final String userId,final String title,final String content){
		System.out.println("NotifiyMessage title"+title+",content:"+content);
		MessageBoxVo messageVo = new MessageBoxVo(adminId, userId, title, content);
		messageVo.setAdmin_id(adminId);
		boolean msgFlag = new MessageBoxDaoImpl().addMessage(messageVo);
		if(msgFlag){
			//推送通知/push notifications
			UserDeviceVo deviceVo = new UserDeviceDaoImpl().getDeviceInfo(userId);
			if(deviceVo != null && !StringUtils.isEmpty(deviceVo.getToken())){
				Map<String, String> customerDic = PushUtil.getPushCustomerDictionary(PushType.Push_Type_New_Message);
				//信息未读消息数/data unread news number
				IMessageBoxDao messageDao = new MessageBoxDaoImpl();
				int messageCount = messageDao.getNoReadMessageCount(userId);
				messageCount += messageDao.getNoReadReplyCount(userId);
				if(messageCount > 0){
					customerDic.put("messageCount", messageCount+"");
				}
				PushUtil.push(deviceVo, title, content, customerDic);
			}
			
			
		}
		return msgFlag;
	}
	
	/**
	 * 
	 * @Title:        addSystemNotifiyMessage 
	 * @Description:  系统消息通知/system message notification
	 * @param:        @param flag
	 * @param:        @param userId
	 * @param:        @param title
	 * @param:        @param content
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年9月7日 下午9:20:36
	 */
	public static void addSystemNotifiyMessage(boolean flag,String userId,String title,String content){
		addNotifiyMessage(flag, "0", userId, title, content);
	}
	
	/**
	 * 
	 * @Title:        sendNotifyToAdmin 
	 * @Description:  向管理员发送通知/Send notification to manager
	 * @param:        @param type 
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月25日 下午4:02:50
	 */
	public void sendNotifyToAdmin(final NotificationType type,final String userId,final String bikeNumber,final int unNormalCount){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<NotificationConfigVo> mappingList = notifyDao.getMappingList(NotificationConfigVo.getType(type));
				for (NotificationConfigVo notificationConfigVo : mappingList) {
					String content = notificationConfigVo.getTemplate();
					AdminVo adminVo = notificationConfigVo.getAdminVo();
					if(content.contains("{admin}")){
						content = content.replace("{admin}", adminVo.getNickname());
					}
					if(!StringUtils.isEmpty(bikeNumber) && content.contains("{bike}")){
						content = content.replace("{bike}", bikeNumber);
					}
					if(!StringUtils.isEmpty(userId) && content.contains("{user}")){
						content = content.replace("{user}", userBo.getUserName(userId));
					}
					if(unNormalCount>0 && content.contains("{UnNormalCount}")){
						content = content.replace("{UnNormalCount}", unNormalCount+"");
					}
					if(notificationConfigVo.getSms() == 1 && !StringUtils.isEmpty(adminVo.getPhone())){
						smgBo.sendSms(adminVo.getPhone(), content);
					}
					if(notificationConfigVo.getEmail() == 1 && !StringUtils.isEmpty(adminVo.getEmail())){
						emailBo.sendSystemEmail(new EmailVo(adminVo.getEmail(), adminVo.getNickname(), notificationConfigVo.getTypeStr(), content));
					}
				}
			}
		}).start();
		
	}
}
