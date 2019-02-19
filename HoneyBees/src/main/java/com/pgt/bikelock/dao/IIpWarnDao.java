/**
 * FileName:     IIpWarnDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年11月8日 下午3:49:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年11月8日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao;

 /**
 * @ClassName:     IIpWarnDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年11月8日 下午3:49:57
 *
 */
public interface IIpWarnDao {
	/**
	 * 
	 * @Title:        addWarn 
	 * @Description:  TODO
	 * @param:        @param ip
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年11月8日 下午3:50:46
	 */
	boolean addWarn(String ip,String userId);
}
