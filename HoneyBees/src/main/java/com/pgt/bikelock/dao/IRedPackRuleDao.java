/**
 * FileName:     IRedPackageRuleDao.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 下午5:04:02
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.RedPackRuleVo;

 /**
 * @ClassName:     IRedPackageRuleDao
 * @Description:红包规则接口定义/red envelope rules interface definition
 * @author:    Albert
 * @date:        2017年4月26日 下午5:04:02
 *
 */
public interface IRedPackRuleDao {
	
	String TABLE_NAME="t_red_package_rule";
	String VIEW_NAME="view_redpack_bike";
	String VIEW_RULE_NAME = "view_redpack_rule";
	
	/**
	 * 
	 * @Title:        getRuleList 
	 * @Description:  获取规则列表/obtain rule list
	 * @param:        @return    
	 * @return:       List<RedPackageRuleVo>    
	 * @author        Albert
	 * @Date          2017年4月26日 下午5:05:55
	 */
	List<RedPackRuleVo> getRuleList(RequestListVo requestVo);
	
	/**
	 * 
	 * @Title:        getRule 
	 * @Description:  获取规则详情/otain rule list
	 * @param:        @return    
	 * @return:       RedPackageRuleVo    
	 * @author        Albert
	 * @Date          2017年4月26日 下午5:06:36
	 */
	RedPackRuleVo getRule(String id);
	
	/**
	 * 
	 * @Title:        getRuleByBikeId 
	 * @Description:  获取规则详情(单车ID)/obtain rule detail(bike id)
	 * @param:        @param bid
	 * @param:        @return    
	 * @return:       RedPackRuleVo    
	 * @author        Albert
	 * @Date          2017年9月7日 下午1:45:47
	 */
	RedPackRuleVo getRuleByBikeId(String bid);
	
	/**
	 * 
	 * @Title:        updateRule 
	 * @Description:  修改规则/modify rule
	 * @param:        @param ruleVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月26日 下午5:07:20
	 */
	boolean updateRule(RedPackRuleVo ruleVo);
	
	/**
	 * 
	 * @Title:        addRule 
	 * @Description:  添加规则/add rule
	 * @param:        @param ruleVo
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月27日 上午10:15:29
	 */
	String addRule(RedPackRuleVo ruleVo);
	
	/**
	 * 
	 * @Title:        deleteRule 
	 * @Description:  删除规则/delete rule
	 * @param:        @param id
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月27日 上午10:27:00
	 */
	boolean deleteRule(String id);
}
