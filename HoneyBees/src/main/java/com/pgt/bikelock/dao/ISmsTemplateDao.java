/**
 * FileName:     ISmsTemplateDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月12日 下午12:15:59
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialziation
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.SmsTemplateVo;

 /**
 * @ClassName:     ISmsTemplateDao
 * @Description:短信模板业务接口定义/short message template business interface definition
 * @author:    Albert
 * @date:        2017年4月12日 下午12:15:59
 *
 */
public interface ISmsTemplateDao {
	
	public static final String table_name = "t_sms_template";
	
	/**
	 * 
	 * @Title:        getTemplate 
	 * @Description:  获取模板/obtain template
	 * @param:        @param industryId
	 * @param:        @param type
	 * @param:        @return    
	 * @return:       SmsTemplateVo    
	 * @author        Albert
	 * @Date          2017年4月12日 下午12:16:58
	 */
	SmsTemplateVo getTemplate(String industryId,int type);
	
	/**
	 * 
	 * @Title:        getTemplate 
	 * @Description:  获取模板/obtain template
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       SmsTemplateVo    
	 * @author        Albert
	 * @Date          2017年11月23日 下午4:45:07
	 */
	SmsTemplateVo getTemplate(String id);
	
	/**
	 * 
	 * @Title:        getTemplateList 
	 * @Description:  获取模板列表/get template list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<SmsTemplateVo>    
	 * @author        Albert
	 * @Date          2017年11月23日 下午4:08:44
	 */
	List<SmsTemplateVo> getTemplateList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        updateTemplate 
	 * @Description:  更新模板信息/update template information
	 * @param:        @param templateVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月23日 下午5:12:02
	 */
	boolean updateTemplate(SmsTemplateVo templateVo);
	
	/**
	 * 
	 * @Title:        addTemplate 
	 * @Description:  添加模板信息/add template information
	 * @param:        @param templateVo
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年11月23日 下午5:14:49
	 */
	String addTemplate(SmsTemplateVo templateVo);
	
}
