/**
 * FileName:     IUserDevice.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年3月30日 上午11:41:45
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年3月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialziation
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.UserDeviceVo;

 /**
 * @ClassName:     IUserDevice
 * @Description:用户设备接口定义/user device interface definition
 * @author:    Albert
 * @date:        2017年3月30日 上午11:41:45
 *
 */
public interface IUserDeviceDao {


	/**
	 * 
	 * @Title:        addDevice 
	 * @Description:  增加设备信息/add device information
	 * @param:        @param deviceVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月1日 下午1:32:56
	 */
	boolean addDevice(UserDeviceVo deviceVo);
	
	
	/**
	 * 
	 * @Title:        updateDevice 
	 * @Description:  修改设备信息/modify device information
	 * @param:        @param deviceVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月1日 下午1:33:05
	 */
	boolean updateDevice(UserDeviceVo deviceVo);
	
	
	/**
	 * 
	 * @Title:        updateRequestToken 
	 * @Description:  更新请求令牌/update request token
	 * @param:        @param uid
	 * @param:        @param token
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2018年2月25日 下午5:50:42
	 */
	boolean updateRequestToken(String uid,String token);
	
	/**
	 * 
	 * @Title:        getDeviceInfo 
	 * @Description:  获取设备信息/obtain device information
	 * @param:        @param uid
	 * @param:        @return    
	 * @return:       UserDeviceVo    
	 * @author        Albert
	 * @Date          2017年9月8日 下午2:45:50
	 */
	UserDeviceVo getDeviceInfo(String uid);
	
	/**
	 * 
	 * @Title:        getDeviceTokenList 
	 * @Description:  TODO
	 * @param:        @param type
	 * @param:        @return    
	 * @return:       List<String>    
	 * @author        Albert
	 * @Date          2017年12月25日 下午7:02:01
	 */
	List<String> getDeviceTokenList(int type);
}
