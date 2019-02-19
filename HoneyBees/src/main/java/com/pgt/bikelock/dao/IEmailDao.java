/**
 * FileName:     IEmailDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月29日 下午3:26:51
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月29日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.EmailVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     IEmailDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月29日 下午3:26:51
 *
 */
public interface IEmailDao {

	public static final String table_name = "t_email";
	
	/**
	 * 
	 * @Title:        addEmail 
	 * @Description:  TODO
	 * @param:        @param email
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月29日 下午3:28:09
	 */
	boolean addEmail(EmailVo email);

	
	/**
	 * 
	 * @Title:        getEmailList 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<EmailVo>    
	 * @author        Albert
	 * @Date          2017年11月29日 下午3:28:33
	 */
	List<EmailVo> getEmailList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getEmailCount 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年11月29日 下午4:13:39
	 */
	int getEmailCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        checkEmailCode 
	 * @Description:  TODO
	 * @param:        @param code
	 * @param:        @param email
	 * @param:        @param validMinutes
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年9月6日 下午4:31:14
	 */
	boolean checkEmailCode(String code, String email,int validMinutes);
}
