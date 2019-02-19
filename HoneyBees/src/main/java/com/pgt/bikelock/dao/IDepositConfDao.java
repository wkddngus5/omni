/**
 * FileName:     IDepositConfDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月3日 下午2:14:25
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月3日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;
import com.pgt.bikelock.vo.DepositConfVo;
import com.pgt.bikelock.vo.RequestListVo;


 /**
 * @ClassName:     IDepositConfDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年7月3日 下午2:14:25
 *
 */
public interface IDepositConfDao {
	
	static final String table_name = "t_deposit_conf";

	/**
	 * 
	 * @Title:        getDepositConfList 
	 * @Description:  押金配置列表获取/deposit configuration list obtain
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<DepositConfVo>    
	 * @author        Albert
	 * @Date          2017年7月3日 下午2:16:28
	 */
	List<DepositConfVo> getDepositConfList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getDepositConf 
	 * @Description:  押金配置详情获取/deposit configuration details obtain
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       DepositConfVo    
	 * @author        Albert
	 * @Date          2017年7月3日 下午2:18:35
	 */
	DepositConfVo getDepositConf(int id);
	
	/**
	 * 
	 * @Title:        getDepositConfWithCityId 
	 * @Description:  当前城市押金配置详情获取/local city deposit configuration details obtain
	 * @param:        @param cityId
	 * @param:        @return    
	 * @return:       DepositConfVo    
	 * @author        Albert
	 * @Date          2017年7月3日 下午2:18:58
	 */
	DepositConfVo getDepositConfWithCityId(int cityId);
	
	/**
	 * 
	 * @Title:        updateDepositConf 
	 * @Description:  修改押金配置/modify deposit configuration
	 * @param:        @param confVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月3日 下午2:20:01
	 */
	boolean updateDepositConf(DepositConfVo confVo);
	
	/**
	 * 
	 * @Title:        addDepositConf 
	 * @Description:  新增押金配置/add deposit configuration
	 * @param:        @param confVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年7月3日 下午2:20:23
	 */
	boolean addDepositConf(DepositConfVo confVo);
	
}
