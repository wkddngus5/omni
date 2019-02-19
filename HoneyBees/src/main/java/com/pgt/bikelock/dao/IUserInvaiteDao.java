/**
 * FileName:     IUserInvaiteDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月5日 下午3:40:24
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月5日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.InvaiteVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     IUserInvaiteDao
 * @Description:好友邀请接口定义/friend invite interface definition
 * @author:    Albert
 * @date:        2017年4月5日 下午3:40:24
 *
 */
public interface IUserInvaiteDao {
	
	/**
	 * 
	 * @Title:        shipExist 
	 * @Description:  判定邀请关系是否已存在/judge invite relations whether exsit
	 * @param:        @param userId
	 * @param:        @param invaiteUserId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月5日 下午3:41:16
	 */
	boolean shipExist(String userId,String invaiteUserId);
	
	/**
	 * 
	 * @Title:        addInvaiteShip 
	 * @Description:  增加邀请关系/add invite relations
	 * @param:        @param userId
	 * @param:        @param invaiteUserId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月5日 下午1:49:25
	 */
	boolean addShip(String userId,String invaiteUserId);
	
	/**
	 * 
	 * @Title:        getInvaiteList 
	 * @Description:  获取好友邀请列表/Get User Invaite List 
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<InvaiteVo>    
	 * @author        Albert
	 * @Date          2017年11月7日 下午8:27:03
	 */
	List<InvaiteVo> getInvaiteList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getInvaiteCount 
	 * @Description:  TODO
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年11月7日 下午8:36:49
	 */
	int getInvaiteCount(RequestListVo requestVo);
}
