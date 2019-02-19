/**
 * FileName:     BikeServlet.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月26日 下午3:41:17
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月26日       CQCN         1.0             1.0
 * 2017年8月25日       CQCN         1.0.1           三合一适配
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.servlet.lock;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.ValueUtil;




/**
 * @ClassName:     BikeServlet
 * @Description:TODO
 * @author:    Albert
 * @date:        2017年4月26日 下午3:41:17
 *
 */
public class LockServlet extends HttpServlet {
	private int type;//请求类型
	private String imei;//设备IMEI号
	private int sendOrder;//是否成功发送指令
	private String[] imeis;//设备IMEI号(多个)
	private List<Map<String, String>> resultMap;
	
	//2017年8月25日15:19:09
	private String timestamp;//开锁时间（3合一）
	private String[] timestamps;//开锁时间集合“，”分割（3合一）
	private String userId;//开锁用户ID（选填）
	private String[] userIds;//开锁用户ID集合“，”分割（选填）

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String RETURN_PARAMS_ERROR = "params error";
	
	public static final int HTTP_RESULT_TOKEN_ERROR = -2;
	public static final int HTTP_RESULT_FAIL = 0;
	public static final int HTTP_RESULT_SUCCESS = 1;
	public static final int HTTP_RESULT_LOCK_DISCONNECT = 2;
	
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
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(!getRequestParams(req,resp)){
			return;
		}
		switch (type) {
		case 1000:
			setCallBackHost(req, resp);
			break;
		case 1001:
			unLock(req, resp);
			break;
		case 1002:
			getLocation(req, resp);
			break;
		case 1003:
			getInfo(req, resp);
			break;
		case 1004:
			getVersion(req, resp);
			break;
		case 1005:
			find(req, resp);
			break;
		case 1006:
			showDown(req, resp);
			break;
		case 1007:
			getMac(req, resp);
			break;
		case 1008:
			checkLockConnected(req, resp);
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(req, resp);
	}
	
	/**
	 * 获取请求参数
	 * @param req
	 * @return
	 */
	protected boolean getRequestParams(HttpServletRequest req,HttpServletResponse resp) {
		type = 0;
		sendOrder = 0;
		
		imei = null;
		imeis = null;
		timestamp = null;
		timestamps = null;
		userId = null;
		userIds = null;

		
		if(OthersSource.LOCK_SERVER_REQUEST_TOKEN != null ){
			if(null == req.getParameter("token") || "".equals(req.getParameter("token").trim())){
				returnData(resp, HTTP_RESULT_FAIL, RETURN_PARAMS_ERROR);
				return false;
			}else if(!OthersSource.LOCK_SERVER_REQUEST_TOKEN.equals(req.getParameter("token"))){
				returnData(resp, HTTP_RESULT_TOKEN_ERROR, "permission error");
				return false;
			}

		}
		

		type =  ValueUtil.getInt(req.getParameter("type"));
		if(type == 0){
			returnData(resp, HTTP_RESULT_FAIL, RETURN_PARAMS_ERROR);
			return false;
		}

		if(null != req.getParameter("imei") && !"".equals(req.getParameter("imei").trim())){
			String temp =  req.getParameter("imei");

			resultMap = null;
			if(temp.contains(",")){
				//多个
				imeis = temp.split(",");
				
				resultMap = new ArrayList<Map<String, String>>();
			}else{
				imei = temp;
			}
			
			String userIdsTemp =  req.getParameter("uid");
			if(null != userIdsTemp && !"".equals(userIdsTemp.trim())
					&& userIdsTemp.contains(",")){
				userIds = userIdsTemp.split(",");
			}else{
				userId = req.getParameter("uid");
				if(userId != null &&  ValueUtil.getInt(userId) == 0){
					returnData(resp, HTTP_RESULT_FAIL, RETURN_PARAMS_ERROR);
					return false;
				}
				
			}
			
			String timestampTemp =  req.getParameter("timestamp");
			if(null != timestampTemp && !"".equals(timestampTemp.trim())
					&& timestampTemp.contains(",")){
				timestamps = timestampTemp.split(",");
			}else{
				timestamp = req.getParameter("timestamp");
				if(timestamp == null || ValueUtil.getLong(timestamp) == 0){
					returnData(resp, HTTP_RESULT_FAIL, RETURN_PARAMS_ERROR);
					return false;
				}
			}
			
		}
		
		return true;
	}

	/**
	 * 
	 * @Title:        returnData 
	 * @Description:  返回参数
	 * @param:        @param resp
	 * @param:        @param code
	 * @param:        @param message    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 下午3:49:46
	 */
	protected void returnData(HttpServletResponse resp,int code,String message) {
		PrintWriter out;
		try {
			JSONObject dataMap= new JSONObject();
			dataMap.put("code", code);
			dataMap.put("msg", message);
			if( ValueUtil.getLong(timestamp) != 0){
				dataMap.put("timestamp", timestamp);
			}
			if(ValueUtil.getInt(userId) != 0){
				dataMap.put("uid", userId);
			}
			out = resp.getWriter();
			out.write(dataMap.toString());
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 返回结果
	 * @Title:        returnData 
	 * @Description:  TODO
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年6月20日 下午5:27:16
	 */
	protected void returnData(HttpServletResponse resp) {
		if(resultMap != null){
			PrintWriter out;
			try {
				JSONObject dataMap= new JSONObject();
				dataMap.put("code", 1);
				dataMap.put("msg", "success");
				dataMap.put("data", resultMap);
				out = resp.getWriter();
				out.write(dataMap.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			if(sendOrder == 1){
				returnData(resp,HTTP_RESULT_SUCCESS, "send order success");
			}else{
				returnData(resp,HTTP_RESULT_LOCK_DISCONNECT, "bike not connect to server");
			}
		}

	}
	
	/**
	 * 1000
	 * @Title:        setCallBackHost 
	 * @Description:  回调地址设置
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月25日 下午5:02:32
	 */
	public void setCallBackHost(HttpServletRequest req, HttpServletResponse resp){
		String callBackUrl = req.getParameter("callBackUrl");
		if(callBackUrl == null || callBackUrl.trim().equals(callBackUrl)){
			returnData(resp, 0, RETURN_PARAMS_ERROR);
			return;
		}
//		MyTcpCallBack.setCallBackHost(callBackUrl);
		returnData(resp, 1, "update callback url success");
	}
	


	/**
	 * 1001
	 * @Title:        openBike 
	 * @Description:  开锁
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 下午3:52:24
	 */
	public void unLock(HttpServletRequest req, HttpServletResponse resp){
		
		if(imeis != null){
			for (int i = 0; i < imeis.length; i++) {
				long imeiT = ValueUtil.getLong(imeis[i]);
				int userIdT = 0;
				if(userIds != null){
					userIdT = ValueUtil.getInt(userIds[i]);
				}
				long timestampT = 0;
				if(timestamps != null){
					timestampT = ValueUtil.getLong(timestamps[i]);
				}

				byte[] order= CommandUtil.getBGMLockCommand(Command.CODE,imeiT+"",
						Command.LOCK_ON,userIdT, timestampT);
				sendOrder = TCPService.sendOrder(imeiT, order);
				Map<String, String> result = new HashMap<String, String>();
				result.put("imei",imeiT+"");
				if(userIdT != 0){
					result.put("uid",userIdT+"");
				}
			
				result.put("status",sendOrder+"");
				result.put("timestamp",timestampT+"");
				resultMap.add(result);
			}
		}else{
			byte[] order= CommandUtil.getBGMLockCommand(Command.CODE,imei,
					Command.LOCK_ON,ValueUtil.getInt(userId), ValueUtil.getLong(timestamp));
			sendOrder = TCPService.sendOrder(Long.parseLong(imei), order);
		}
	
		returnData(resp);
	}

	/**
	 * 1002
	 * @Title:        getLocation 
	 * @Description:  位置获取
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 下午3:54:39
	 */
	public void getLocation(HttpServletRequest req, HttpServletResponse resp){

		if(imeis != null){
			for (String imeiT : imeis) {
				byte[] order = CommandUtil.getLocationCommand(Command.CODE_GM, imeiT);
				sendOrder = TCPService.sendOrder(Long.parseLong(imeiT), order);
				Map<String, String> result = new HashMap<String, String>();
				result.put("imei",imeiT);
				result.put("status",sendOrder+"");
				resultMap.add(result);
			}
		}else{
			byte[] order = CommandUtil.getLocationCommand(Command.CODE_GM, imei);
			sendOrder = TCPService.sendOrder(Long.parseLong(imei), order);
		}
		returnData(resp);
	}

	/**
	 * 1003
	 * @Title:        getInfo 
	 * @Description:  电量获取
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 下午3:54:54
	 */
	public void getInfo(HttpServletRequest req, HttpServletResponse resp){
		
		if(imeis != null){
			for (String imeiT : imeis) {
				byte[] order = CommandUtil.getInfoCommand(Command.CODE_OM, imeiT);
				sendOrder = TCPService.sendOrder(Long.parseLong(imeiT), order);
				Map<String, String> result = new HashMap<String, String>();
				result.put("imei",imeiT);
				result.put("status",sendOrder+"");
				resultMap.add(result);
			}
		}else{
			byte[] order = CommandUtil.getInfoCommand(Command.CODE_OM, imei);
			sendOrder = TCPService.sendOrder(Long.parseLong(imei), order);
		}
		returnData(resp);
	}

	/**
	 * 1004
	 * @Title:        getVersion 
	 * @Description:  版本
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 下午3:55:14
	 */
	public void getVersion(HttpServletRequest req, HttpServletResponse resp){
		
		if(imeis != null){
			for (String imeiT : imeis) {
				byte[] order = CommandUtil.getVersionCommand(Command.CODE_OM, imeiT);
				sendOrder = TCPService.sendOrder(Long.parseLong(imeiT), order);
				Map<String, String> result = new HashMap<String, String>();
				result.put("imei",imeiT);
				result.put("status",sendOrder+"");
				resultMap.add(result);
			}
		}else{
			byte[] order = CommandUtil.getVersionCommand(Command.CODE_OM, imei);
			sendOrder = TCPService.sendOrder(Long.parseLong(imei), order);
		}
		returnData(resp);
	}

	/**
	 * 1005
	 * @Title:        find 
	 * @Description:  找车
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年4月26日 下午3:55:25
	 */
	public void find(HttpServletRequest req, HttpServletResponse resp){
	
		if(imeis != null){
			for (String imeiT : imeis) {
				byte[] order = CommandUtil.getFindBikeCommand(Command.CODE_OM, imeiT, 3, 1);
				sendOrder = TCPService.sendOrder(Long.parseLong(imeiT), order);
				Map<String, String> result = new HashMap<String, String>();
				result.put("imei",imeiT);
				result.put("status",sendOrder+"");
				resultMap.add(result);
			}
		}else{
			byte[] order = CommandUtil.getFindBikeCommand(Command.CODE_OM, imei, 3, 1);
			sendOrder = TCPService.sendOrder(Long.parseLong(imei), order);
		}
		returnData(resp);
	}

	/**
	 * 1006
	 * @Title:        showDown 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年5月5日 下午8:54:33
	 */
	public void showDown(HttpServletRequest req, HttpServletResponse resp){
		
		if(imeis != null){
			for (String imeiT : imeis) {
				byte[] order = CommandUtil.getSleepCommand(Command.CODE_OM, imeiT);
				sendOrder = TCPService.sendOrder(Long.parseLong(imeiT), order);
				Map<String, String> result = new HashMap<String, String>();
				result.put("imei",imeiT);
				result.put("status",sendOrder+"");
				resultMap.add(result);
			}
		}else{
			byte[] order = CommandUtil.getSleepCommand(Command.CODE_OM, imei);
			sendOrder = TCPService.sendOrder(Long.parseLong(imei), order);
		}
		returnData(resp);
	}
	
	/**
	 * 1007
	 * @Title:        getMac 
	 * @Description:  TODO
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年8月25日 下午4:24:28
	 */
	public void getMac(HttpServletRequest req, HttpServletResponse resp){
		
		if(imeis != null){
			for (String imeiT : imeis) {
				byte[] order = CommandUtil.getMacCommand(Command.CODE_OM, imeiT);
				sendOrder = TCPService.sendOrder(Long.parseLong(imeiT), order);
				Map<String, String> result = new HashMap<String, String>();
				result.put("imei",imeiT);
				result.put("status",sendOrder+"");
				resultMap.add(result);
			}
		}else{
			byte[] order = CommandUtil.getMacCommand(Command.CODE_OM, imei);
			sendOrder = TCPService.sendOrder(Long.parseLong(imei), order);
		}
		returnData(resp);
	}
	
	
	/**
	 * 1008
	 * @Title:        checkLockConnected 
	 * @Description:  验证锁是否连接/check lock connect
	 * @param:        @param req
	 * @param:        @param resp    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2018年3月20日 下午3:10:53
	 */
	private void checkLockConnected(HttpServletRequest req, HttpServletResponse resp){
		if(TCPService.getIoSession(ValueUtil.getLong(imei)) == null){
			returnData(resp, HTTP_RESULT_SUCCESS, "connected");
		}else{
			returnData(resp, HTTP_RESULT_LOCK_DISCONNECT, "disconnecte");
		}
		
	}

}
