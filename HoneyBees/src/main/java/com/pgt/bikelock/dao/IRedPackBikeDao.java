/**
 * FileName:     IBikeRedPackDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 下午5:05:07
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.math.BigDecimal;
import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.RedPackBikeVo;

 /**
 * @ClassName:     IBikeRedPackDao
 * @Description:单车红包接口定义/bike red envelopes interface definition
 * @author:    Albert
 * @date:        2017年4月26日 下午5:05:07
 *
 */
public interface IRedPackBikeDao {
	/**
	 * 
	 * @Title:        getRedPackBikeList 
	 * @Description:  红包单车列表获取/obtain bike list with red envelopes
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<BikeRedPackVo>    
	 * @author        Albert
	 * @Date          2017年4月26日 下午5:25:02
	 */
	List<RedPackBikeVo> getRedPackBikeList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        addRedPackBike 
	 * @Description:  添加红包单车/add red envelopes bike
	 * @param:        @param bikeVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月26日 下午5:28:49
	 */
	String addRedPackBike(String bikeId[],RedPackBikeVo bikeVo);
	
	/**
	 * 
	 * @Title:        deleteRedPackBike 
	 * @Description:  删除红包单车/delete red envelopes bike
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月26日 下午5:29:26
	 */
	boolean deleteRedPackBike(String id);
	
	/**
	 * 
	 * @Title:        getNoUseRedPackBike 
	 * @Description:  获取未领红包单车详情/obtain red envelopes details
	 * @param:        @param bikeId
	 * @param:        @return    
	 * @return:       RedPackBikeVo    
	 * @author        Albert
	 * @Date          2017年4月27日 下午7:58:48
	 */
	RedPackBikeVo getNoUseRedPackBike(String bikeId);
	
	/**
	 * 
	 * @Title:        getRedPackInfo 
	 * @Description:  获取红包信息/obtain red envelopes details
	 * @param:        @param packId
	 * @param:        @return    
	 * @return:       RedPackBikeVo    
	 * @author        Albert
	 * @Date          2017年4月27日 下午9:54:41
	 */
	RedPackBikeVo getRedPackInfo(String packId);
	
	/**
	 * 
	 * @Title:        setRedPack 
	 * @Description:  生成红包/generate red envelopes
	 * @param:        @param userId
	 * @param:        @param amount
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月27日 下午9:26:33
	 */
	boolean createRedPack(String userId,String packId,BigDecimal amount);
	
	/**
	 * 
	 * @Title:        getUserRedPackList 
	 * @Description:  获取用户红包列表/obtain user red envelopes list
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       List<RedPackBikeVo>    
	 * @author        Albert
	 * @Date          2017年5月18日 下午3:07:09
	 */
	List<RedPackBikeVo> getUserRedPackList(String userId,int startPage);
}
