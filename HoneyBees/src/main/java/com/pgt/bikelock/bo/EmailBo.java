/**
 * FileName:     EmailBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月29日 下午3:36:51
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月29日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import org.apache.commons.lang.StringUtils;

import com.pgt.bikelock.dao.IEmailDao;
import com.pgt.bikelock.dao.impl.EmailDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.SmsTemplateDaoImpl;
import com.pgt.bikelock.dao.impl.UserDetailDaoImpl;
import com.pgt.bikelock.utils.EmailUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.EmailVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.SmsTemplateVo;

/**
 * @ClassName:     EmailBo
 * @Description:Email Controller
 * @author:    Albert
 * @date:        2017年11月29日 下午3:36:51
 *
 */
public class EmailBo {

	private IEmailDao emailDao = new EmailDaoImpl();
	private EmailUtil emailUtil = new EmailUtil();

	/**
	 * 
	 * @Title:        sendSystemEmail 
	 * @Description:  发送系统邮件/Send system email
	 * @param:        @param emailVo    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年11月29日 下午3:55:51
	 */
	public boolean sendSystemEmail(EmailVo emailVo){
		if(emailUtil.sendSystemEmail(emailVo)){
			emailDao.addEmail(emailVo);
		}

		return emailVo.getStatus() == 1?true:false;
	}

	/**
	 * 
	 * @Title:        sendCodeEmail 
	 * @Description:  发送邮箱验证码/send email code
	 * @param:        @param industryId
	 * @param:        @param email
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月6日 下午4:37:21
	 */
	public boolean sendCodeEmail(String industryId,String email){
		//生成6位随机数 / Generates 6 random numbers
		String code = ValueUtil.getNumberRandom(6);
		//内容生成 / 内容生成
		SmsTemplateVo templateVo = new SmsTemplateDaoImpl().getTemplate(industryId, 1);
		if(templateVo == null || StringUtils.isEmpty(templateVo.getTemplate())
				|| !templateVo.getTemplate().contains("{code}")){
			return false;
		}
		String content = templateVo.getTemplate().replace("{code}", code);//set code
		content = content.replace("{invalidtime}", SmsBo.SMS_Invalid_Minute+"");//set invalid time
		if(StringUtils.isEmpty(content)){
			return false;//不合法的内容不能发送 / Illegal content can not be sent
		}
		EmailVo emailVo = new EmailVo(email, email, templateVo.getTitle(), content);
		emailVo.setCode(code);
		
		//debug/test
		emailVo.setStatus(1);
//		if(emailUtil.sendSystemEmail(emailVo)){
			emailDao.addEmail(emailVo);
//		}
		return emailVo.getStatus() == 1?true:false;
	}
	
	/**
	 * 
	 * @Title:        checkEmailCode 
	 * @Description:  check email code
	 * @param:        @param code
	 * @param:        @param email
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月6日 下午4:37:17
	 */
	public boolean checkEmailCode(String code,String email){
		return new EmailDaoImpl().checkEmailCode(code, email, SmsBo.SMS_Invalid_Minute);
	}
	
	/**
	 * 
	 * @Title:        userEmailAuth 
	 * @Description:  TODO
	 * @param:        @param userId
	 * @param:        @param email
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月6日 下午5:33:24
	 */
	public static boolean userEmailAuth(String userId,String email){
		int authType = 1;//1:usual 2:education
		if(email.endsWith(".edu")){
			//education email
			authType = 2;
		}
		boolean flag = new UserDetailDaoImpl().emailAuth(userId, email,authType);
		return flag;
	}
}
