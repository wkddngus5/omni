/**
 * FileName:     ISmsDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月12日 上午11:42:23
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.SmsVo;

 /**
 * @ClassName:     ISmsDao
 * @Description:短信业务接口定义/short message interface definition
 * @author:    Albert
 * @date:        2017年4月12日 上午11:42:23
 *
 */
public interface ISmsDao {
	
	public static final String table_name = "t_sms";
	
	/**
	 * 
	 * @Title:        addSms 
	 * @Description:  添加短信/add short message
	 * @param:        @param sms
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月12日 上午11:43:44
	 */
	boolean addSms(SmsVo sms);
	
	/**
	 * 
	 * @Title:        checkSmsCode 
	 * @Description:  校验短信验证码/verify short message code
	 * @param:        @param code
	 * @param:        @param phone
	 * @param:		  @param validMinutes 有效时长（分钟）/validity time(minitues)
	 * @param:        @return    
	 * @return:       boolean：无效或已过期 1：有效 /invalid or expired 1:effective
	 * @author        Albert
	 * @Date          2017年4月12日 上午11:45:47
	 */
	boolean checkSmsCode(String code,String phone,int validMinutes);
	
	/**
	 * 
	 * @Title:        getPhoneCountInDay 
	 * @Description:  TODO
	 * @param:        @param phone
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年11月7日 上午10:40:14
	 */
	int getPhoneCountInDay(String phone);
	
	/**
	 * 
	 * @Title:        getSmsList 
	 * @Description:  获取短信列表/Get sms list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<SmsVo>    
	 * @author        Albert
	 * @Date          2017年11月23日 上午11:58:35
	 */
	List<SmsVo> getSmsList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getSmsCount 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年11月23日 下午2:08:48
	 */
	int getSmsCount(RequestListVo requestVo);
}
