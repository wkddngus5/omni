/**
 * FileName:     IBleBikeDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月16日 下午3:18:55
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.BleBikeVo;

 /**
 * @ClassName:     IBleBikeDao
 * @Description:蓝牙单车业务接口定义类/bluetooth bike business protocol definition
 * @author:    Albert
 * @date:        2017年6月16日 下午3:18:55
 *
 */
public interface IBleBikeDao {

	/**
	 * 添加蓝牙单车/add bluetooth bike
	 * @Title:        addBleBike 
	 * @Description:  TODO
	 * @param:        @param bikeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月16日 下午3:26:55
	 */
	boolean addBleBike(BleBikeVo bikeVo);
	
	/**
	 * 更新蓝牙信息/update bluetooth information
	 * @Title:        updateBleBike 
	 * @Description:  TODO
	 * @param:        @param bikeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月24日 下午1:47:10
	 */
	boolean updateBleBike(BleBikeVo bikeVo);
	
	/**
	 * 获取蓝牙单车/obtain bluetooth bike
	 * @Title:        getBleBike 
	 * @Description:  TODO
	 * @param:        @param number
	 * @param:        @return    
	 * @return:       BleBikeVo    
	 * @author        Albert
	 * @Date          2017年6月16日 下午3:27:45
	 */
	BleBikeVo getBleBike(String number);
	
}
