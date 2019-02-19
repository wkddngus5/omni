/**
 * FileName:     UserCreditScoreVo.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月12日 下午4:42:50/4:42:50 pm, May 12, 2017
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/<initializing>
 */
package com.pgt.bikelock.vo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.LanguageUtil;
import com.pgt.bikelock.utils.ValueUtil;

 /**
 * @ClassName:     UserCreditScoreVo
 * @Description:用户信用积分记录实体/User credit score record entity
 * @author:    Albert
 * @date:        2017年5月12日 下午4:42:50/4:42:50 pm, May 12, 2017
 *
 */
public class CreditScoreVo {
	String id;
	String uid;
	UserVo userVo;
	String rule_name;//规则名称/rule name
	int rule_type;//1:加分 2:减分 3:设为0/1: score increasing 2:score reducing 3: set as 0
	int data_type;
	int count;
	String date;
	
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
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
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
	 * @return the userVo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	/**
	 * @param userVo the userVo to set
	 */
	public void setUserVo(UserVo userVo) {
		this.userVo = userVo;
	}
	/**
	 * @return the rule_name
	 */
	public String getRule_name() {
		return rule_name;
	}
	/**
	 * @param rule_name the rule_name to set
	 */
	public void setRule_name(int type) {
		
		String[] scoreType;
		if(this.rule_type == 1){
			scoreType = LanguageUtil.getDefaultValue("credit_add_score_type").split(",");
		}else{
			scoreType = LanguageUtil.getDefaultValue("credit_dec_score_type").split(",");
		}
		if(type > scoreType.length || type == 0){
			this.rule_name = "unknow";
		}else{
			this.rule_name = scoreType[type-1];
		}
		
	}
	/**
	 * @return the rule_type
	 */
	public int getRule_type() {
		return rule_type;
	}
	/**
	 * @param rule_type the rule_type to set
	 */
	public void setRule_type(int rule_type) {
		this.rule_type = rule_type;

	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	
	
	/**
	 * @return the data_type
	 */
	public int getData_type() {
		return data_type;
	}
	/**
	 * @param data_type the data_type to set
	 */
	public void setData_type(int data_type) {
		this.data_type = data_type;
	}
	public CreditScoreVo(){

	}
	
	/**
	 * 积分详情构造（新增）/score details structure (added)
	 * @param userId 用户ID/user ID
	 * @param dealType 操作类型 1：加分 2：减分/operating type 1: score increasing 2:score reducing
	 * @param scoreType 积分类型：和操作类型索引对应/score type:corresponding to the operating type index
	 */
	public CreditScoreVo(String userId,int dealType,int scoreType){
		this.uid = userId;
		String[] scoreValue;
		if(dealType == 1){
			scoreValue = OthersSource.getSourceString("credit_add_score_value").split(",");
		}else{
			scoreValue = OthersSource.getSourceString("credit_dec_score_value").split(",");
		}
		int value = ValueUtil.getInt(scoreValue[scoreType-1]);
		this.rule_type = dealType;
		this.data_type = scoreType;
		if(value == 0){
			this.rule_type = 0;
		}
		this.count = value;
	}

	public CreditScoreVo(ResultSet rst) throws SQLException{
		this.id = rst.getString("id");
		this.userVo = new UserVo(rst);
		this.date = rst.getString("date");
		this.rule_type = rst.getInt("rule_type");
		this.data_type = rst.getInt("data_type");
		setRule_name(this.data_type);
		this.count = rst.getInt("count");
	}
}
