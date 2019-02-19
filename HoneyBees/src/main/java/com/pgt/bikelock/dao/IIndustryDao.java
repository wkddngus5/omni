/**
 * FileName:     IIndustryDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 上午9:41:19
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.IndustryVo;

 /**
 * @ClassName:     IIndustryDao
 * @Description:产业相关接口定义/industry related interface definition
 * @author:    Albert
 * @date:        2017年4月6日 上午9:41:19
 *
 */
public interface IIndustryDao {
	/**
	 * 
	 * @Title:        getIndustryInfo 
	 * @Description:  获取产业设置信息/obtain industry set up information
	 * @param:        @param industryId
	 * @param:        @return    
	 * @return:       IndustryVo    
	 * @author        Albert
	 * @Date          2017年4月6日 上午9:41:56
	 */
	IndustryVo getIndustryInfo(String industryId);
	
	/**
	 * 
	 * @Title:        updateIndustryInfo 
	 * @Description:  更新产业信息/update industry information
	 * @param:        @param industryVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月7日 下午6:30:02
	 */
	boolean updateIndustryInfo(IndustryVo industryVo);
}
