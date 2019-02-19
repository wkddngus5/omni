/**
 * FileName:     IBikeErrorDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 上午11:43:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.BikeErrorVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     IBikeErrorDao
 * @Description:单车故障接口定义/bike erro protocol definition
 * @author:    Albert
 * @date:        2017年4月11日 上午11:43:21
 *
 */
public interface IBikeErrorDao {
	String TABLE_NAME="t_bike_error";
	
	/**
	 * 
	 * @Title:        addBikeError 
	 * @Description:  增加故障信息/add erro information
	 * @param:        @param errorVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月11日 上午11:45:10
	 */
	boolean addBikeError(BikeErrorVo errorVo);
	
	/**
	 * 
	 * @Title:        getBikeErrorList 
	 * @Description:  获取故障列表/obtain erro list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<BikeErrorVo>    
	 * @author        Albert
	 * @Date          2017年4月11日 上午11:45:28
	 */
	List<BikeErrorVo> getBikeErrorList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getBikeErrorList 
	 * @Description:  get user report list
	 * @param:        @param userId
	 * @param:        @param startPage
	 * @param:        @return    
	 * @return:       List<BikeErrorVo>    
	 * @author        Albert
	 * @Date          2017年10月16日 上午11:39:43
	 */
	List<BikeErrorVo> getBikeErrorList(String userId,int startPage);
	
	/**
	 * 
	 * @Title:        getBikeError 
	 * @Description:  故障详情获取/erro details obtain
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       BikeErrorVo    
	 * @author        Albert
	 * @Date          2017年5月15日 下午3:26:15
	 */
	BikeErrorVo getBikeError(String id);
	
	/**
	 * 
	 * @Title:        getCount 
	 * @Description:  获取数量/obtain quantity
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年4月11日 下午2:08:15
	 */
	int getCount(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getWaitForDealCount 
	 * @Description:  获取待处理报告总数/obtain process report total number
	 * @param:        @param number
	 * @param:        @return    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年9月15日 下午1:35:54
	 */
	List<String> getWaitForDealCount(String number,boolean groupByUser);
	
	/**
	 * 
	 * @Title:        updateBikeErrorStatus 
	 * @Description:  更新故障状态/update error status
	 * @param:        @param errorVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月11日 上午11:45:39
	 */
	boolean updateBikeErrorStatus(BikeErrorVo errorVo);
	
	
	/**
	 * 
	 * @Title:        batchDealReport 
	 * @Description:  批量更新报告状态/update report status
	 * @param:        @param ids
	 * @param:        @param status
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月30日 下午4:55:00
	 */
	boolean batchDealReport(String ids,int status);
	
	/**
	 * 
	 * @Title:        deleteBikeError 
	 * @Description:  删除故障记录/delete error record
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月11日 上午11:45:51
	 */
	boolean deleteBikeError(String id);
	
	/**
	 * 
	 * @Title:        checkErrorIsWaitForApply 
	 * @Description:  判定故障是否正在等待审核/judge error whether waiting checking
	 * @param:        @param uid
	 * @param:        @param bnumber
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月27日 上午11:01:08
	 */
	boolean checkErrorIsWaitForApply(String uid,String bnumber,int type);
}
