/**
 * FileName:     IViolationDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月16日 下午6:39:58
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月16日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import com.pgt.bikelock.vo.ViolationVo;

 /**
 * @ClassName:     IViolationDao
 * @Description:违规接口定义/violate interface definition
 * @author:    Albert
 * @date:        2017年5月16日 下午6:39:58
 *
 */
public interface IViolationDao {
	/**
	 * 
	 * @Title:        addViolation 
	 * @Description:  添加违规/add violate
	 * @param:        @param violation
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月16日 下午6:40:46
	 */
	boolean addViolation(ViolationVo violation);
}
