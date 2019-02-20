/**
 * FileName:     SstatisticsVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年6月30日 下午3:27:30/3:27:30 pm, June 30, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年6月30日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo.admin;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     SstatisticsVo
 * @Description:统计报表实体/statistical report entity
 * @author:    Albert
 * @date:        2017年6月30日 下午3:27:30/3:27:30 pm, June 30, 2017
 *
 */
public class StatisticsVo {
	String title;
	long value;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the value
	 */
	public long getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(long value) {
		this.value = value;
	}
	
	public StatisticsVo(){
		this.title = "0";
		this.value = 0;
	}
	
	public StatisticsVo(ResultSet rst) throws SQLException{
		this.title = BaseDao.getString(rst, "title");
		this.value = BaseDao.getLong(rst, "value");
	}
}
