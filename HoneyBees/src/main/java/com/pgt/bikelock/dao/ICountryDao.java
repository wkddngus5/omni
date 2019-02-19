/**
 * FileName:     ICountryDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月14日 下午3:26:38
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月14日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.CountryVo;

 /**
 * @ClassName:     ICountryDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月14日 下午3:26:38
 *
 */
public interface ICountryDao {
	/**
	 * 
	 * @Title:        getCountryList 
	 * @Description:  获取国家列表/obtain country list
	 * @param:        @return    
	 * @return:       List<CountryVo>    
	 * @author        Albert
	 * @Date          2017年4月14日 下午3:27:19
	 */
	List<CountryVo> getCountryList();
	
	/**
	 * 
	 * @Title:        getCountry 
	 * @Description:  TODO
	 * @param:        @param phoneCode
	 * @param:        @return    
	 * @return:       CountryVo    
	 * @author        Albert
	 * @Date          2017年11月11日 下午6:41:15
	 */
	CountryVo getCountry(int phoneCode);
}
