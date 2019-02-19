/**
 * FileName:     INewsDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月9日 下午8:46:49
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月9日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.NewsVo;
import com.pgt.bikelock.vo.RequestListVo;

 /**
 * @ClassName:     INewsDao
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年5月9日 下午8:46:49
 *
 */
public interface INewsDao {
	
	static String TABLE_NAME = "t_news";
	String COLUMN_TITLE="title";
	String COLUMN_CONTENT="content";
	
	/**
	 * 
	 * @Title:        getNewsList 
	 * @Description:  获取资讯列表/obtain information list
	 * @param:        @param requestVo
	 * @param:        @return    
	 * @return:       List<NewsVo>    
	 * @author        Albert
	 * @Date          2017年5月9日 下午8:47:15
	 */
	List<NewsVo> getNewsList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getNews 
	 * @Description:  获取资讯详情/obtain information details
	 * @param:        @param newsId
	 * @param:        @return    
	 * @return:       NewsVo    
	 * @author        Albert
	 * @Date          2017年5月10日 下午3:02:43
	 */
	NewsVo getNews(String newsId);
	
	/**
	 * 
	 * @Title:        getTopNews 
	 * @Description:  最新资讯获取/obtain latest information
	 * @param:        @return    
	 * @return:       NewsVo    
	 * @author        Albert
	 * @Date          2017年5月10日 下午6:08:11
	 */
	NewsVo getTopNews(int cityId);
	
	/**
	 * 
	 * @Title:        addNews 
	 * @Description:  添加资讯/add information
	 * @param:        @param newsVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月10日 下午2:50:07
	 */
	boolean addNews(NewsVo newsVo);
	
	/**
	 * 
	 * @Title:        updateNews 
	 * @Description:  修改资讯/modify information
	 * @param:        @param newsVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月10日 下午2:50:26
	 */
	boolean updateNews(NewsVo newsVo);
	

}
