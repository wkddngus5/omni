/**
 * FileName:     IBikeTypeDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月6日 上午10:53:52
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月6日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;
import com.pgt.bikelock.vo.BikeTypeVo;

 /**
 * @ClassName:     IBikeTypeDao
 * @Description:单车类型接口定义/bike type protocol definition
 * @author:    Albert
 * @date:        2017年4月6日 上午10:53:52
 *
 */
public interface IBikeTypeDao {

	/**
	 * 
	 * @Title:        getTypeInfo 
	 * @Description:  获取类型详情/obtain type details
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       BikeTypeVo    
	 * @author        Albert
	 * @Date          2017年4月6日 上午10:54:42
	 */
	BikeTypeVo getTypeInfo(String id);
	
	/**
	 * 
	 * @Title:        getTypeList 
	 * @Description:  获取价格类型/obtain price type
	 * @param:        @return    
	 * @return:       List<BikeTypeVo>    
	 * @author        Albert
	 * @Date          2017年4月5日 上午11:08:32
	 */
	List<BikeTypeVo> getTypeList(int industryId,int cityId);
	
	/**
	 * 
	 * @Title:        updateTypeInfo 
	 * @Description:  更新类型设置/update type set up
	 * @param:        @param typeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月7日 下午7:37:14
	 */
	boolean updateTypeInfo(BikeTypeVo typeVo);
	
	/**
	 * 
	 * @Title:        addTypeInfo 
	 * @Description:  add type
	 * @param:        @param typeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年10月25日 下午4:17:01
	 */
	boolean addTypeInfo(BikeTypeVo typeVo,int industryId);
	
	/**
	 * 
	 * @Title:        checkTypeExist 
	 * @Description:  Check price type if exist
	 * @param:        @param typeVo
	 * @param:        @return    
	 * @return:       BikeTypeVo    
	 * @author        Albert
	 * @Date          2017年10月25日 下午5:26:07
	 */
	BikeTypeVo checkTypeExist(BikeTypeVo typeVo,int industryId);
}
