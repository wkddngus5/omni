/**
 * FileName:     ICityDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月7日 下午5:04:01
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月7日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.CityVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     ICityDao
 * @Description:城市接口定义类/city interface definition type
 * @author:    Albert
 * @date:        2017年4月7日 下午5:04:01
 *
 */
public interface ICityDao {
	
	static String TABLE_NAME = "t_city";
	static String CLOUMN_ID = "id";
	static String CLOUMN_NAME = "name";
	
	/**
	 * 
	 * @Title:        getCityLlist 
	 * @Description:  获取城市列表/obtain city list
	 * @param:        @return    
	 * @return:       List<CityVo>    
	 * @author        Albert
	 * @Date          2017年4月7日 下午5:04:34
	 */
	List<CityVo> getCityLlist(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getNearCitylist 
	 * @Description:  TODO
	 * @param:        @param location
	 * @param:        @return    
	 * @return:       List<CityVo>    
	 * @author        Albert
	 * @Date          2018年8月27日 上午11:58:18
	 */
	List<CityVo> getNearCitylist(LatLng location);
	
	/**
	 * 
	 * @Title:        getCityInfo 
	 * @Description:  城市详情/city information
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       CityVo    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:39:05
	 */
	CityVo getCityInfo(String id);
	
	/**
	 * 
	 * @Title:        addCity 
	 * @Description:  添加城市/add city
	 * @param:        @param cityVo
	 * @param:        @return    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:33:34
	 */
	String addCity(CityVo cityVo);
	
	/**
	 * 
	 * @Title:        updateCity 
	 * @Description:  修改城市/modify city
	 * @param:        @param cityVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月6日 下午5:33:59
	 */
	boolean updateCity(CityVo cityVo);
	
}
