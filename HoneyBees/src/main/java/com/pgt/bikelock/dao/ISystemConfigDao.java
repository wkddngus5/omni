/**
 * FileName:     ISystemConfigDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月30日 下午6:41:07
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.admin.SystemConfigVo;

 /**
 * @ClassName:     ISystemConfigDao
 * @Description:系统设置接口类/system configuration interface
 * @author:    Albert
 * @date:        2017年11月30日 下午6:41:07
 *
 */
public interface ISystemConfigDao {

	/**
	 * 
	 * @Title:        addConfig 
	 * @Description:  TODO
	 * @param:        @param configVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月30日 下午6:45:53
	 */
	boolean addConfig(SystemConfigVo configVo);
	
	/**
	 * 
	 * @Title:        updateConfig 
	 * @Description:  TODO
	 * @param:        @param configVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月30日 下午6:46:07
	 */
	boolean updateConfig(SystemConfigVo configVo);
	
	/**
	 * 
	 * @Title:        getConfig 
	 * @Description:  TODO
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       SystemConfigVo    
	 * @author        Albert
	 * @Date          2017年11月30日 下午6:51:17
	 */
	SystemConfigVo getConfig(int id);
	
	/**
	 * 
	 * @Title:        getConfig 
	 * @Description:  TODO
	 * @param:        @param key
	 * @param:        @return    
	 * @return:       SystemConfigVo    
	 * @author        Albert
	 * @Date          2018年5月18日 上午10:39:23
	 */
	SystemConfigVo getConfig(String key);
	
	/**
	 * 
	 * @Title:        getConfigList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<SystemConfigVo>    
	 * @author        Albert
	 * @Date          2018年5月18日 上午10:47:09
	 */
	List<SystemConfigVo> getConfigList(boolean checkKey);
}
