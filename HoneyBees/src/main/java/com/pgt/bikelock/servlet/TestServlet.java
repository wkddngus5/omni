package com.pgt.bikelock.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.OMGVMCID;

import com.pgt.bike.lock.lib.entity.Command;
import com.pgt.bike.lock.lib.tcp.SessionMap;
import com.pgt.bike.lock.lib.tcp.TCPService;
import com.pgt.bike.lock.lib.utils.CommandUtil;
import com.pgt.bikelock.dao.impl.BikeDaoImpl;
import com.pgt.bikelock.servlet.admin.BaseManage; 
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;

public class TestServlet extends BaseManage {

	short power;
	 
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	 
	}

 
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		switch (getRequestType(req)) {
		case 20100:
			testUnlockBike(req,resp);
			break;
		case 20101:
			testShutdownBike(req,resp);
			break;
		case 20102:
			testAddBike(req,resp);
			break;
		case 20103:
			testUpdateNo(req,resp);
			break;
		case 20104:
			testGetIccid(req,resp);
			break;
		case 20105:
			testGetMac(req, resp);
			break;
		}
	}
	
	

	
	/**
	 * 生产时测试 开锁/production test unlock
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void testUnlockBike(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		//20100
		resp.setHeader("Charset","UTF-8");  
		resp.setContentType("text/html;charset=UTF-8");
		
		String imei = req.getParameter("imei");
		System.out.println("testUnlockBike test unlock IMEI="+imei);
		boolean isExist = hasBike(imei);
		if(!isExist){
			System.out.println("testUnlockBike no device!");
			PrintWriter out = resp.getWriter();
			
			out.write("数据库中没有找到设备");
			out.flush();
			out.close();
			return ;
		}
		
		byte[] order= CommandUtil.getBGMLockCommand(Command.CODE,imei,
				Command.LOCK_ON,0, TimeUtil.getCurrentLongTime());
		int sendStatus= TCPService.sendOrder(ValueUtil.getLong(imei), order);
		System.out.println("testUnlockBike unlock order sended");
		String content="未发送";
		if(sendStatus==SessionMap.STATUS_SEND_SESSION_NULL){
			content="未连接服务器";
		}else if(sendStatus==SessionMap.STATUS_SEND_SUCCESSFULLY){
			content="已发送开锁指令,"+power;
		}else{
			content="开锁指令发送失败";
		}
		PrintWriter out = resp.getWriter();
		out.write(content);
		out.flush();
		out.close();
	}
	
	
	/**
	 * 生产时测试 关机/production test open lock
	 * @param req
	 * @param resp
	 * @throws IOException 
	 */
	private void testShutdownBike(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		//20101
		resp.setHeader("Charset","UTF-8");  
		resp.setContentType("text/html;charset=UTF-8");
		
		String imei = req.getParameter("imei");
		System.out.println("testShutdownBike test shut down IMEI="+imei);
		boolean isExist = hasBike(imei);
		if(!isExist){
			System.out.println("testShutdownBike  no device!");
			PrintWriter out = resp.getWriter();
			
			out.write("数据库中没有找到设备");
			out.flush();
			out.close();
			return ;
		}
 
		byte[] order = CommandUtil.getSleepCommand(Command.CODE_OM, imei);;
		int sendStatus=TCPService.sendOrder(ValueUtil.getLong(imei), order);
		System.out.println("testShutdownBike shut down order sended");
		String content="未发送";
		if(sendStatus==SessionMap.STATUS_SEND_SESSION_NULL){
			content="未连接到服务器";
		}else if(sendStatus==SessionMap.STATUS_SEND_SUCCESSFULLY){
			content="已发送关机指令,"+power;
			//标记设备待激活/sign device wait to activate
			new BikeDaoImpl().updateBikeErrorStatusWithImei(ValueUtil.getLong(imei),4);
		}else{
			content="关机指令发送失败";
		}
		PrintWriter out = resp.getWriter();
		out.write(content);
		out.flush();
		out.close();
	}

	
	private void testAddBike(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setHeader("Charset","UTF-8");  
		resp.setContentType("text/html;charset=UTF-8");
		// 20102
		// 插入单车/insert bike
		String[] parms = new String[]{"number","imei","typeId","cityId"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String imei = req.getParameter(parms[1]);
		boolean isExist = hasBike(imei);
		if(isExist){
			System.out.println("testAddBike device has exist!");
			PrintWriter out = resp.getWriter();
			out.write("已保存至数据库");
			out.flush();
			out.close();
			return;
		}
		String number = req.getParameter(parms[0]);
		boolean flag = addBike(number,imei);
		PrintWriter out = resp.getWriter();
		if(flag){
			// 保存插入数据库成功/keep insert database success
			out.write("插入成功");
		}else{
			// 插入数据库失败/insert database failure
			out.write("插入失败");
		}
		out.flush();
		out.close();
	}
	
	private void testUpdateNo(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		// 20103
		resp.setHeader("Charset","UTF-8");  
		resp.setContentType("text/html;charset=UTF-8");
		String[] parms = new String[]{"number","imei"};
		if(!checkRequstParams(req, resp, parms)){
			return;
		}
		String imei = req.getParameter(parms[1]);
 
 
		boolean exist = hasBike(imei);
		if(exist == true){
 
			// 不等于空，数据库中已经存在该主板/not equal empty,database already exist board
			// 更新编号/update item number
			String number = req.getParameter(parms[0]);
			boolean flag = updateNumber(imei, number);
			PrintWriter out = resp.getWriter();
			if(flag){
				out.write("关联编号成功");
			}else{
				out.write("关联编号失败");
			}
			out.flush();
			out.close();
		}else{
			//
			PrintWriter out = resp.getWriter();
			out.write("没有找到设备");
			out.flush();
			out.close();
		}
	}
	
	private void testGetIccid(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		//20104
		resp.setHeader("Charset","UTF-8");  
		resp.setContentType("text/html;charset=UTF-8");
		String imei = req.getParameter("imei");
		
		byte[] order = CommandUtil.getIccidCommand(Command.CODE_OM, imei);
		int sendStatus= TCPService.sendOrder(ValueUtil.getLong(imei), order);
		String content="未发送";
		if(sendStatus==SessionMap.STATUS_SEND_SESSION_NULL){
			content="未连接服务器";
		}else if(sendStatus==SessionMap.STATUS_SEND_SUCCESSFULLY){
			content="已发送获取ICCID指令,"+power;
		}else{
			content="获取ICCID指令发送失败";
		}
		System.out.println("test get ICCID  sended");
		PrintWriter out = resp.getWriter();
		out.write(content);
		out.flush();
		out.close();
	}
	
	/**
	 * 20105
	 * @Title:        testGetMac 
	 * @Description:  获取MAC地址/get mac address
	 * @param:        @param req
	 * @param:        @param resp
	 * @param:        @throws IOException    
	 * @return:       void    
	 * @author        Albert
	 * @Date          2017年7月24日 下午12:21:55
	 */
	private void testGetMac(HttpServletRequest req, HttpServletResponse resp) throws IOException{

		resp.setHeader("Charset","UTF-8");  
		String imei = req.getParameter("imei");
		boolean exist = hasBike(imei);
		if(exist == true){
 
			byte[] order = CommandUtil.getMacCommand(Command.CODE_OM, imei);
			int sendStatus= TCPService.sendOrder(ValueUtil.getLong(imei), order);
			String content="未发送";
			if(sendStatus==SessionMap.STATUS_SEND_SESSION_NULL){
				content="未连接服务器";
			}else if(sendStatus==SessionMap.STATUS_SEND_SUCCESSFULLY){
				content="已发送获取MAC指令,"+power;
			}else{
				content="获取MAC指令发送失败";
			}
			PrintWriter out = resp.getWriter();
			out.write(content);
			out.flush();
			out.close();
		}else{
			//
			PrintWriter out = resp.getWriter();
			out.write("没有找到设备");
			out.flush();
			out.close();
		}
	}
	
	private boolean updateNumber(String imei,String number){
		Connection conn = DataSourceUtil.getConnection(); 
		String sql="update t_bike set   number=?  WHERE imei= ? ";
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, number);
			pstmt.setString(2, imei);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
		
	}
	
	private boolean addBike(String number,String imei){
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_bike (number,imei,type_id,city_id,add_date) values (?,?,?,?,now()) ";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, number);
			pstmt.setString(2,imei);
			pstmt.setString(3, "1");
			pstmt.setString(4, "1");
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/**
	 * 
	 * @param imei  
	 * @return  true 表示存在，false 不存在/show exist, false not exist
	 */
	private boolean hasBike(String imei){
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_bike where imei = ? " ;
		boolean  flag = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, imei);
			rs = pstmt.executeQuery();
			if(rs.next()){
				flag=true;
				power = rs.getShort("power");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag; 
	}


		
}
