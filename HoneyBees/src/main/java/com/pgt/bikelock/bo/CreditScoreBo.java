/**
 * FileName:     CreditScoreBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年5月12日 下午6:27:53
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年5月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.math.BigDecimal;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.ICreditScoreDao;
import com.pgt.bikelock.dao.impl.CreditScoreDaoImpl;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.CreditScoreVo;

/**
 * @ClassName:     CreditScoreBo
 * @Description:信用积分业务处理类/credit integral business proceesing class
 * @author:    Albert
 * @date:        2017年5月12日 下午6:27:53
 *
 */
public class CreditScoreBo {
	 

	/**
	 * 
	 * @Title:        getUserCreditPrice 
	 * @Description:  用户信用价格设置/user credit price set up
	 * @param:        @param userCredit
	 * @param:        @return    
	 * @return:       BigDecimal    
	 * @author        Albert
	 * @Date          2017年5月12日 下午6:43:20
	 */
	public static BigDecimal getUserCreditPrice(int userCredit){
		BigDecimal amount = null;
		//无信用价格相关设置/no credit price set up
		if(!haveCreditCheck()){
			return null;
		}
		int minScore = ValueUtil.getInt(OthersSource.getSourceString("credit_score_min"));
		if(userCredit < minScore){
			amount = new BigDecimal(OthersSource.getSourceString("credit_min_score_unit_price"));
		}

		return amount;
	}

	/**
	 * 
	 * @Title:        haveCreditCheck 
	 * @Description:  判定有无信用价格相关设置/judge whether have credit price set up
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月13日 上午10:39:27
	 */
	public static boolean haveCreditCheck(){
		if(!StringUtils.isEmpty(OthersSource.getSourceString("credit_score_min")) &&
				!StringUtils.isEmpty(OthersSource.getSourceString("credit_min_score_unit_price"))){
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Title:        addScoreRecord 
	 * @Description:  新增积分记录 / add credit score for user
	 * @param:        @param userId
	 * @param:        @param dealType 操作类型 1：加分 2：减分/operate type 1.add score 2:reduce score
	 * @param:        @param scoreType 积分类型：和操作类型索引对应/integral type:meet operate type index
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月15日 下午3:01:59
	 */
	public static boolean addScoreRecord(String userId,int dealType,int scoreType){
		ICreditScoreDao scoreDao = new CreditScoreDaoImpl();
		
		CreditScoreVo scoreVo = new CreditScoreVo(userId, dealType, scoreType);
		boolean result = scoreDao.addUserCreditRecord(scoreVo);
		return result;
	}
}
