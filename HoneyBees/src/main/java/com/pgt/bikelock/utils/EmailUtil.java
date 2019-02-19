/**
 * FileName:     EmailUtil.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月22日 上午10:33:41
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月22日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.vo.EmailVo;

/**
 * @ClassName:     EmailUtil
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月22日 上午10:33:41
 *
 */
public class EmailUtil {

	//发送方账号密码（授权码）/ Sender account and password(Authorization code)
	public static String sendEmailAccount =  OthersSource.getSourceString("email_account");
	public static String sendEmailPassword = OthersSource.getSourceString("email_password");
	// 发件人邮箱的 SMTP 服务器地址 / Email server smtp
	public static String sendEmailSMTPHost = OthersSource.getSourceString("email_smtp_host");

	public static void main(String[] args) throws Exception {
		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();                    // 参数配置
		props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
		props.setProperty("mail.smtp.host", sendEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
		props.setProperty("mail.smtp.auth", "true");            // 需要请求认证

		if(!StringUtils.isEmpty(OthersSource.getSourceString("email_smtp_port"))){
		    props.setProperty("mail.smtp.port", OthersSource.getSourceString("email_smtp_port"));
	        props.put("mail.smtp.starttls.enable", "true");
		}    
//        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		// PS: 某些邮箱服务器要求 SMTP 连接需要使用 SSL 安全认证 (为了提高安全性, 邮箱支持SSL连接, 也可以自己开启),
		//     如果无法连接邮件服务器, 仔细查看控制台打印的 log, 如果有有类似 “连接失败, 要求 SSL 安全连接” 等错误,
		//     打开下面 /* ... */ 之间的注释代码, 开启 SSL 安全连接。
		/*
	        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
	        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
	        //                  QQ邮箱的SMTP(SLL)端口为465或587, 其他邮箱自行去查看)
	        final String smtpPort = "465";
	        props.setProperty("mail.smtp.port", smtpPort);
	        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        props.setProperty("mail.smtp.socketFactory.fallback", "false");
	        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
		 */

		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getInstance(props);
		session.setDebug(true);                                 // 设置为debug模式, 可以查看详细的发送 log

		// 3. 创建一封邮件
		MimeMessage message = createMimeMessage(session, "chengming_wu10678@163.com","Albert","异常的单车","766644为异常单车");
		// 4. 根据 Session 获取邮件传输对象
		Transport transport = session.getTransport();

		// 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
		// 
		//    PS_01: 成败的判断关键在此一句, 如果连接服务器失败, 都会在控制台输出相应失败原因的 log,
		//           仔细查看失败原因, 有些邮箱服务器会返回错误码或查看错误类型的链接, 根据给出的错误
		//           类型到对应邮件服务器的帮助网站上查看具体失败原因。
		//
		//    PS_02: 连接失败的原因通常为以下几点, 仔细检查代码:
		//           (1) 邮箱没有开启 SMTP 服务;
		//           (2) 邮箱密码错误, 例如某些邮箱开启了独立密码;
		//           (3) 邮箱服务器要求必须要使用 SSL 安全连接;
		//           (4) 请求过于频繁或其他原因, 被邮件服务器拒绝服务;
		//           (5) 如果以上几点都确定无误, 到邮件服务器网站查找帮助。
		//
		//    PS_03: 仔细看log, 认真看log, 看懂log, 错误原因都在log已说明。
		transport.connect(sendEmailAccount, sendEmailPassword);

		// 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
		transport.sendMessage(message, message.getAllRecipients());

		// 7. 关闭连接
		transport.close();
	}

	/**
	 * 
	 * @Title:        createMimeMessage 
	 * @Description:  创建信息/create message
	 * @param:        @param session
	 * @param:        @param receiveMail
	 * @param:        @param receiverName
	 * @param:        @param subject
	 * @param:        @param content
	 * @param:        @return
	 * @param:        @throws Exception    
	 * @return:       MimeMessage    
	 * @author        Albert
	 * @Date          2017年11月29日 上午11:03:44
	 */
	public static MimeMessage createMimeMessage(Session session, String receiveMail,String receiverName,String subject,String content) throws Exception {
		// 1. 创建一封邮件
		MimeMessage message = new MimeMessage(session);

		// 2. From: 发件人
		message.setFrom(new InternetAddress(sendEmailAccount, OthersSource.getSourceString("project_name"), "UTF-8"));
		
		// 3. To: 收件人（可以增加多个收件人、抄送、密送）
		message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, receiverName, "UTF-8"));

		// 4. Subject: 邮件主题
		message.setSubject(subject, "UTF-8");

		// 5. Content: 邮件正文（可以使用html标签)
		message.setContent(content, "text/html;charset=UTF-8");

		// 6. 设置发件时间
		message.setSentDate(new Date());

		// 7. 保存设置
		message.saveChanges();

		return message;
	}
	

	/**
	 * 
	 * @Title:        sendSystemEmail 
	 * @Description:  发送系统邮件/send system email
	 * @param:        @param emailVo    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月29日 下午3:53:44
	 */
	public boolean sendSystemEmail(EmailVo emailVo){
		if(StringUtils.isEmpty(sendEmailAccount)){
			return false;
		}
		emailVo.setSender(sendEmailAccount);
		//configuration
		Properties props = new Properties();  
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", sendEmailSMTPHost);
		props.setProperty("mail.smtp.auth", "true");
		if(!StringUtils.isEmpty(OthersSource.getSourceString("email_smtp_port"))){
		    props.setProperty("mail.smtp.port", OthersSource.getSourceString("email_smtp_port"));
	        props.put("mail.smtp.starttls.enable", "true");
		} 
		//create session
		Session session = Session.getInstance(props);
		session.setDebug(false); // 设置为debug模式, 可以查看详细的发送 log

		//create message
		MimeMessage message;
		try {
			message = createMimeMessage(session, emailVo.getReceiver(),emailVo.getReceiverName(),emailVo.getSubject(),emailVo.getContent());
			//get transport
			Transport transport = session.getTransport();
			transport.connect(sendEmailAccount, sendEmailPassword);

			//send email
			transport.sendMessage(message, message.getAllRecipients());

			//close session
			transport.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			emailVo.setStatus(0);
		}
		emailVo.setStatus(1);
		return true;
	}

}
