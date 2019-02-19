/**
 * FileName:     DepositReturnBo.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月15日 下午3:11:31
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月15日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.bo;

import java.math.BigDecimal;

import com.alipay.api.AlipayUploadRequest;
import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IDepositConfDao;
import com.pgt.bikelock.dao.IUserDao;
import com.pgt.bikelock.dao.impl.DepositConfDaoImpl;
import com.pgt.bikelock.dao.impl.DepositDaoImpl;
import com.pgt.bikelock.dao.impl.IndustryDaoImpl;
import com.pgt.bikelock.dao.impl.TradeDaoImpl;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.utils.pay.AcquiroPayUtil;
import com.pgt.bikelock.utils.pay.AliPayUtil;
import com.pgt.bikelock.utils.pay.AnetPayUtil;
import com.pgt.bikelock.utils.pay.PayPalUtil;
import com.pgt.bikelock.utils.pay.PayuUtil;
import com.pgt.bikelock.utils.pay.StripPayUtil;
import com.pgt.bikelock.utils.pay.WechatPayUtil;
import com.pgt.bikelock.vo.DepositConfVo;
import com.pgt.bikelock.vo.DepositReturnVo;
import com.pgt.bikelock.vo.IndustryVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserVo;

/**
 * @ClassName:     DepositBo
 * @Description:押金业务控制类/deposit business controller
 * @author:    Albert
 * @date:        2017年4月15日 下午3:11:31
 *
 */
public class DepositBo {

	/**
	 * 
	 * @Title:        depositAuth 
	 * @Description:  押金认证/deposit certifications
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月15日 下午5:57:33
	 */
	public boolean depositAuth(UserVo userVo){
		IUserDao userDao = new UserDaoImpl();
		boolean flag = false;
		if(userVo == null){
			return false;
		}

		if(userVo.getAuthStatus() == 6){//重新缴付押金/renew pay deposit
			IndustryVo industryVo = new IndustryDaoImpl().getIndustryInfo(userVo.getIndustryId());
			flag = userDao.updateAuthDeposit(userVo.getuId(),industryVo.getRegister_auth_num());
		}else{//首次缴付（需验证前置认证）/first time pay(need verify)
			flag = userDao.updateAuthStatusCheckFront(userVo.getuId(), 3);
			if(flag){
				//创建注册优惠券(creat register coupon
				CouponBo.crateRegisterCoupon(userVo.getuId(),userVo.getCityId());
			}
		}

		return flag;
	}


	/**
	 * 
	 * @Title:        returnDeposit 
	 * @Description:  退还押金/refund deposit
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       int 0:申请失败/apply failure 1：申请成功/apply success   2：未缴付押金/not pay deposit  3：已在申请中/applying
	 * @author        Albert
	 * @Date          2017年4月15日 下午3:25:03
	 */
	public int returnDeposit(String userId,String cardNumber,String expireDate) {
		// TODO Auto-generated method stub
		UserVo userVo = new UserDaoImpl().getUserWithId(userId,true);
		if(userVo == null){
			return 0;
		}
		if(userVo.getAuthStatus() < 3 || userVo.getAuthStatus() == 6){//校验押金状态/verify deposit status
			return 2;
		}
		if(userVo.getAuthStatus() == 5){//已在申请中/applying
			return 3;
		}
		TradeVo tradeVo = new TradeDaoImpl().getFinalDepositTradeInfo(userId);
		if(tradeVo == null || StringUtils.isEmpty(tradeVo.getId())){//校验押金订单/verify deposit order
			return 2;
		}

		//提交申请/forward apply
		String returnId= new DepositDaoImpl().returnDeposit(userId,tradeVo.getId());
		if(returnId != null){
			boolean flag = new UserDaoImpl().updateAuthDeposit(userId, 5);

			if(flag){
				//验证是否自动退款/verify whether auto-refund
				DepositConfVo configVo = new DepositConfDaoImpl().getDepositConfWithCityId(userVo.getCityId());
				if( configVo != null && configVo.getAutomatic_refund() == 1){
					PayBo.refundOrder(returnId, tradeVo, 0,tradeVo.getAmount(),cardNumber,expireDate);
				}

			}
			return flag?1:0;
		}
		return 0;
	}

	/**
	 * 
	 * @Title:        dealDepositReturn 
	 * @Description:  处理押金退还/tackle deposit refund
	 * @param:        @param recordId
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月15日 下午3:42:53
	 */
	public boolean dealDepositReturn(String recordId,int status) {
		DepositReturnVo retrurnVo = new DepositDaoImpl().getDepositReturnInfo(recordId);
		TradeVo tradeVo = new TradeDaoImpl().getTradeInfo(retrurnVo.getTrade_id());
		boolean flag = PayBo.refundOrder(recordId, tradeVo, status,tradeVo.getAmount(),"","");
		return flag;
	}

	

	/**
	 * 
	 * @Title:        updateDepositReturn 
	 * @Description:  更新押金退还状态/update deposit refund status
	 * @param:        @param recordId
	 * @param:        @param status
	 * @param:        @param out_refund_no
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年8月22日 下午4:51:37
	 */
	public  boolean updateDepositReturn(String recordId,int status,String out_refund_no) {
		boolean flag = new DepositDaoImpl().updateDepositReturn(recordId, status,out_refund_no);
		return flag;
	}

	/**
	 * 
	 * @Title:        getDepositConfig 
	 * @Description:  押金配置/deposit configuration
	 * @param:        @param cityId
	 * @param:        @return    
	 * @return:       DepositConfVo    
	 * @author        Albert
	 * @Date          2017年7月3日 下午5:25:43
	 */
	public DepositConfVo getDepositConfig(int cityId){
		IDepositConfDao configDao = new DepositConfDaoImpl();
		//优先获取当前城市押金金额，如果当前没有则获取所有的/obtain exexiting city deposit ammount, if no then obtain
		DepositConfVo configVo = configDao.getDepositConfWithCityId(cityId);
		if(configVo == null){
			configVo = configDao.getDepositConfWithCityId(0);
		}
		return configVo;
	}

	/**
	 * 
	 * @Title:        getDepositAmount 
	 * @Description:  押金额度获取/deposit amount obtain
	 * @param:        @param cityId
	 * @param:        @return    
	 * @return:       BigDecimal    
	 * @author        Albert
	 * @Date          2017年7月3日 下午5:14:19
	 */
	public BigDecimal getDepositAmount(int cityId){
		DepositConfVo configVo = getDepositConfig(cityId);
		if(getDepositConfig(cityId) != null){
			return configVo.getAmount();
		}
		return new BigDecimal(0);
	}


}
