/**
 * FileName:     IImageDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月11日 下午3:20:14
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月11日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.sql.Connection;
import java.util.List;

import com.pgt.bikelock.vo.ImageVo;

 /**
 * @ClassName:     IImageDao
 * @Description:图片接口定义类/picture interface definition type
 * @author:    Albert
 * @date:        2017年4月11日 下午3:20:14
 *
 */
public interface IImageDao {
	/**
	 * 
	 * @Title:        addImages 
	 * @Description:  增加多张图片/add multi picutre
	 * @param:        @param list
	 * @param:        @return    
	 * @return:       String 图片Ids,逗号分割 /picture ids,comma divide
	 * @author        Albert
	 * @Date          2017年4月11日 下午3:29:04
	 */
	String addImages(List<ImageVo> list);
	
	/**
	 * 
	 * @Title:        getImages 
	 * @Description:  获取图片(多个)/get photo(multi)
	 * @param:        @param ids
	 * @param:        @param conn
	 * @param:        @return    
	 * @return:       List<ImageVo>    
	 * @author        Albert
	 * @Date          2017年4月11日 下午4:09:33
	 */
	List<ImageVo> getImages(String ids,Connection conn);
	
	/**
	 * 
	 * @Title:        getImage 
	 * @Description:  获取图片(单个)/get photo(one)
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       ImageVo   
	 * @author        Albert
	 * @Date          2017年5月10日 下午7:47:48
	 */
	ImageVo getImage(String id);
	
	/**
	 * 
	 * @Title:        deleteImages 
	 * @Description:  删除图片/delete photo
	 * @param:        @param ids
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月10日 下午7:51:21
	 */
	boolean deleteImages(String ids);
}
