/**
 * FileName:     BikeCallBackServlet.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年10月19日 下午4:23:25
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年10月19日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.text.Bidi;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pgt.bikelock.bo.BikeBo;
import com.pgt.bikelock.bo.HoldBO;
import com.pgt.bikelock.dao.IBikeDao;
import com.pgt.bikelock.dao.IBikeUseDao;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.dao.impl.BikeUseDaoImpl;
import com.pgt.bikelock.filter.ParamsFilter;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.utils.ZoneDate;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.BikeVo;

 /**
 * @ClassName:     BikeCallBackServlet
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年10月19日 下午4:23:25
 *
 */
public class BikeCallBackServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IBikeDao bikeDao ;
	private IBikeUseDao bikeUseDao ;
	
	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		dealRequest(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		dealRequest(request, response);
	}
	
	
	/**
	 * 
	 * @Title:        dealRequest 
	 * @Description:  Deal lock call back request
	 * @param:        @param request
	 * @param:        @param response    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月19日 下午4:59:43
	 */
	private void dealRequest(HttpServletRequest request, HttpServletResponse response){

		JSONObject dataObject = JSONObject.parseObject(request.getParameter("data"));

		int requestType = dataObject.getIntValue("type");
		String token = dataObject.getString("token");
		long imei = dataObject.getLongValue("imei");
		int status = dataObject.getIntValue("status");
		
		System.out.println(request.getParameter("token"));
		if(!OthersSource.LOCK_SERVER_REQUEST_TOKEN.equals(token)){
			setCode(response, ParamsFilter.HTTP_RESULT_TOKEN_ERROR);
			return;
		}

		if(requestType == 0 || imei == 0){
			setCode(response, HTTP_RESULT_DATA_NULL_ERROR);
			return;
		}
		
		System.out.println("call back type:"+requestType);
		
		if(bikeDao == null){
			bikeDao = new BikeDaoImpl();
			bikeUseDao = new BikeUseDaoImpl();
		}

		
		if(requestType == 1000){//heart
			finishBikeUseAtHeart(imei, status);
		}else if(requestType == 1001){//lock/unlock
			int uid = ValueUtil.getInt(request.getParameter("uid"));
			long timeStamp = ValueUtil.getLong(request.getParameter("timestamp"));
			int runTime = ValueUtil.getInt(request.getParameter("runTime"));
			if(status == 0){//lock
				lockBike(imei, uid, timeStamp, runTime);
			}else if(status == 1){//unlock
				unlockBike(imei);
			}
		}
	}
	
	
	/**
	 * 
	 * @Title:        finishBikeUseAtHeart 
	 * @Description:  Finish order with heart
	 * @param:        @param imei
	 * @param:        @param lockStatus    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月19日 下午5:00:01
	 */
	private void finishBikeUseAtHeart(long imei, int lockStatus){
		BikeVo bike = bikeDao.getBikeInfoWithImei(imei+"");
		if(bike != null && bike.getStatus() == lockStatus){
			if(lockStatus == 0){

				//开始骑行/begin riding
				if (bikeUseDao.startUseBikeSuccess(bike.getBid())) {
					BikeUseVo bikeUseVo = bikeUseDao.getUseIngDetailInfo(imei+"",null);
					new HoldBO().payActiveHoldWhenStartRide(bikeUseVo);
				}
				
			}else if(lockStatus == 1){
				//结算骑行/caculate riding
				BikeUseVo bikeUseVo = bikeUseDao.getUseIngDetailInfo(imei+"",null);
				if(bikeUseVo != null){
					if(bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_USING && 
							(
//									bikeUseVo.getBikeVo().getStatus()==BikeVo.LOCK_ON || 
							bikeUseVo.getOut_area() == 1)){
						// 当前 单车正在被租赁,且锁是打开的或者越界状态	/present bike is renting, and lock is open or transboundary status
						new BikeBo().dealBikeOrder(bikeUseVo,true);
					}
				}

			}
		}
	}
	
	/**
	 * 
	 * @Title:        unlockBike 
	 * @Description:  Unlock Bike(Start ride)
	 * @param:        @param imei    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月19日 下午5:11:25
	 */
	private void unlockBike(long imei){
		BikeVo bike = bikeDao.getBikeInfoWithImei(imei+"");
		if(bike != null){
			// 设置开锁成功/set up open lock success
			if(!bikeUseDao.startUseBikeSuccess(bike.getBid())){
				//未能正常开始骑行时，手动标记开关/not begin riding normally, manual sign switch
				bikeDao.updateLockStatus(bike.getNumber(),1);
			}
			else {

				BikeUseVo bikeUseVo = bikeUseDao.getUseIngDetailInfo(imei+"",null);
				new HoldBO().payActiveHoldWhenStartRide(bikeUseVo);

			}
		}else{
			System.out.println("bike not exist in system;time:"+new ZoneDate().toLocaleString());
		}
	}

	/**
	 * 
	 * @Title:        lockBike 
	 * @Description:  Unlock Bike(Finish ride)
	 * @param:        @param imei
	 * @param:        @param uid
	 * @param:        @param timestamp
	 * @param:        @param runTime    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年10月19日 下午5:11:44
	 */
	private void lockBike(long imei, int uid, long timestamp, int runTime){
		BikeUseVo bikeUseVo = null;
		if(uid > 0 && timestamp > 0){
			bikeUseVo = bikeUseDao.getUseIngDetailInfo(uid,timestamp);
		}else{
			bikeUseVo = bikeUseDao.getUseIngDetailInfo(imei+"",null);
		}
		if(bikeUseVo == null){
			//直接修改锁状态为关闭/direct modify lock status as closed
			new BikeDaoImpl().updateLockStatusWithImei(imei, 0,false);
			return ;
		}
		System.out.println("lock "+bikeUseVo.getBikeVo().getNumber());
		if(bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_USING  || bikeUseVo.getBikeVo().getUseStatus()==BikeUseVo.STATUS_UNLOCK_ING
//				&& (bikeUseVo.getBikeVo().getStatus()==BikeVo.LOCK_ON || bikeUseVo.getBikeVo().getStatus()==BikeVo.LOCK_ERROR)
				){
			// 当前 单车正在被租赁,且锁是打开的/present bike is renting, and lock is open
			bikeUseVo.setEndTime(runTime);
			new BikeBo().dealBikeOrder(bikeUseVo,true);
		}else{
			System.out.println("omni bike is not in use or not lock"+";time:"+new ZoneDate().toLocaleString());
		}
	}
}
