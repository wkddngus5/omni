/**
 * FileName:     IAppVersionDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月27日 下午3:46:11
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月27日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.AppVersionVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     IAppVersionDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月27日 下午3:46:11
 *
 */
public interface IAppVersionDao {
	
	final String TABLE_NAME = "t_app_version";
	final String CLOUMN_NAME = "version_name";
	final String CLOUMN_CODE = "version_code";
	final String CLOUMN_URL = "url";
	final String CLOUMN_CONTENT = "content";

	/**
	 * 
	 * @Title:        getTopVersion 
	 * @Description:  最新版本信息/final version information
	 * @param:        @param versionCode
	 * @param:        @return    
	 * @return:       AppVersionVo    
	 * @author        Albert
	 * @Date          2017年5月27日 下午3:47:01
	 */
	AppVersionVo getTopVersion(int versionCode);
	
	/**
	 * 
	 * @Title:        getVersionList 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<AppVersionVo>    
	 * @author        Albert
	 * @Date          2017年12月25日 上午10:46:42
	 */
	List<AppVersionVo> getVersionList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getVersionCount 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年12月25日 下午6:32:50
	 */
	int getVersionCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        addVersion 
	 * @Description:  TODO
	 * @param:        @param versionVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年12月25日 下午1:54:03
	 */
	String addVersion(AppVersionVo versionVo);
}
