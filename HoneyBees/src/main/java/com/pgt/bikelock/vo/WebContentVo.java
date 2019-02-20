/**
 * FileName:     AgremmentVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月10日 下午7:16:14/7:16:14 pm, April 10, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月10日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.pgt.bikelock.dao.impl.BaseDao;

 /**
 * @ClassName:     AgremmentVo
 * @Description:用户协议实体/user agreement entity
 * @author:    Albert
 * @date:        2017年4月10日 下午7:16:14/7:16:14 pm, April 10, 2017
 *
 */
public class WebContentVo {
	String id;
	String industry_id;
	int type;//协议类型：1 用户协议 2：充值协议 3:不能开锁 4：举报违停 5：押金说明 ,6红包攻略,7积分规则,8隐私条款 9 how to sigin/agreement type 1: user agreement 2: recharge agreement 3: unlocked 4: report vialation 5: deposit description 6: red packet strategy 7: score rule 8: private policy 9: how to sigin
	String content;
	String date;
	String title;//标题（扩展内容）/title (extended content)
	/**显示属性**//**display attribute**/
	String typeStr;
	
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the industry_id
	 */
	public String getIndustry_id() {
		return industry_id;
	}
	/**
	 * @param industry_id the industry_id to set
	 */
	public void setIndustry_id(String industry_id) {
		this.industry_id = industry_id;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	
	
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
	public WebContentVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.industry_id = rst.getString("industry_id");
		this.type = rst.getInt("type");
		this.content = rst.getString("content");
		this.date = rst.getString("date");
		//this.title = BaseDao.getString(rst, "title");
	}
	
	public WebContentVo(String[] parms,HttpServletRequest req){
		this.id = req.getParameter(parms[0]);
		this.content= req.getParameter(parms[1]);
	}
}
