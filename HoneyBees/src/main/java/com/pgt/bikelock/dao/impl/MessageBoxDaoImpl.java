/**
 * FileName:     MessageBoxDaoImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年7月20日 下午6:01:00
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年7月20日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.IMessageBoxDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.MessageBoxVo;
import com.pgt.bikelock.vo.RequestListVo;

/**
 * @ClassName:     MessageBoxDaoImpl
 * @Description:信箱业务接口实现/mailbox business protocol achieve
 * @author:    Albert
 * @date:        2017年7月20日 下午6:01:00
 *
 */
public class MessageBoxDaoImpl implements IMessageBoxDao {

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#addMessage(com.pgt.bikelock.vo.MessageBoxVo)
	 */
	public boolean addMessage(MessageBoxVo messageVo) {
		// TODO Auto-generated method stub
		String[] userIds = messageVo.getUser_id().split(",");
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_message_box (from_type,admin_id,user_id,message_type,title,content,reply,date,update_date) values  ";
		for (int i = 0; i < userIds.length; i++) {
			if(i!=0){
				sql = sql +",";
			}
			sql = sql +" (?,?,?,?,?,?,?,now(),now())";
		}
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 			
			for (int i = 0; i < userIds.length; i++) {
				int index = i*7;
				pstmt.setString(1+index,messageVo.getFrom_type());
				pstmt.setString(2+index,messageVo.getAdmin_id());
				pstmt.setString(3+index, userIds[i]);
				pstmt.setString(4+index, messageVo.getMessage_type());
				pstmt.setString(5+index, messageVo.getTitle());
				pstmt.setString(6+index, messageVo.getContent());
				pstmt.setInt(7+index, messageVo.getReply());
			}
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#addReplyMessage(com.pgt.bikelock.vo.MessageBoxVo)
	 */
	public boolean addReplyMessage(MessageBoxVo messageVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert t_message_box_reply(from_type,msgbox_id,admin_id,user_id,message_type,content,date) values (?,?,?,?,?,?,now()) ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,messageVo.getFrom_type());
			pstmt.setString(2,messageVo.getMsgbox_id());
			pstmt.setString(3, messageVo.getAdmin_id());
			pstmt.setString(4, messageVo.getUser_id());
			pstmt.setString(5, messageVo.getMessage_type());
			pstmt.setString(6, messageVo.getContent());
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#updateMessageIsReply(java.lang.String)
	 */
	public boolean updateMessageIsRead(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_message_box set isread = 1 where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,id);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#updateMessageStatus(java.lang.String)
	 */
	public boolean updateMessageStatus(String id,int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_message_box set status = ? where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,status);
			pstmt.setString(2,id);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#updateAllMessageIsRead(java.lang.String)
	 */
	public boolean updateAllMessageIsRead(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_message_box set isread = 1 where user_id = ? and isread = 0";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,userId);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#updateAllReplyIsRead(java.lang.String)
	 */
	public boolean updateAllReplyIsRead(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_message_box_reply set isread = 1 where user_id = ?  and isread = 0";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,userId);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#updateMessageIsWaitForReply(java.lang.String)
	 */
	public boolean updateMessageStatus(String id,int replay,int isRead) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_message_box set reply = ?,isread = ?,update_date = now() where id = ? ";

		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, replay);
			pstmt.setInt(2, isRead);
			pstmt.setString(3,id);
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getMessageList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<MessageBoxVo> getMessageList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		List<MessageBoxVo> messageList = new ArrayList<MessageBoxVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_message_box where 1=1";
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (user_phone like '%"+requestVo.getKeyWords()+"%'"
					+ " or admin_name like '%"+requestVo.getKeyWords()+"%'"
					+ " or title like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%')";
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				messageList.add(new MessageBoxVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return messageList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getWaitReplyMessageList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<MessageBoxVo> getWaitReplyMessageList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		List<MessageBoxVo> messageList = new ArrayList<MessageBoxVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_message_box where reply = 0" ;
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (user_phone like '%"+requestVo.getKeyWords()+"%'"
					+ " or admin_name like '%"+requestVo.getKeyWords()+"%'"
					+ " or title like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%')";
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				messageList.add(new MessageBoxVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return messageList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getReplyMessageList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<MessageBoxVo> getReplyMessageList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		List<MessageBoxVo> messageList = new ArrayList<MessageBoxVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_message_box_reply where 1 = 1" ;
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (user_phone like '%"+requestVo.getKeyWords()+"%'"
					+ " or admin_name like '%"+requestVo.getKeyWords()+"%'"
					+ " or title like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%'"
					+ " or reply_content like '%"+requestVo.getKeyWords()+"%')";
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				messageList.add(new MessageBoxVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return messageList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getWaitReplyMessageCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getWaitReplyMessageCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT * from t_message_box where reply = 0";

		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (user_phone like '%"+requestVo.getKeyWords()+"%'"
					+ " or admin_name like '%"+requestVo.getKeyWords()+"%'"
					+ " or title like '%"+requestVo.getKeyWords()+"%'"
					+ "or content like '%"+requestVo.getKeyWords()+"%'"
					+ " or reply_content like '%"+requestVo.getKeyWords()+"%')";
		}

		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs= pstmt.executeQuery();
			if(rs.next()) count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getMessageList(java.lang.String, int)
	 */
	public List<MessageBoxVo> getMessageList(String userId, int startPage) {
		// TODO Auto-generated method stub
		List<MessageBoxVo> messageList = new ArrayList<MessageBoxVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_message_box where user_id = ? and status != -1 order by id desc LIMIT ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, startPage);
			pstmt.setInt(3, BaseDao.pageSize);

			rs = pstmt.executeQuery();
			while(rs.next()){
				messageList.add(new MessageBoxVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return messageList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getReplyMessageList(java.lang.String, int)
	 */
	public List<MessageBoxVo> getReplyMessageList(String messageId,
			int startPage) {
		// TODO Auto-generated method stub
		List<MessageBoxVo> messageList = new ArrayList<MessageBoxVo>();

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_message_box_reply where msgbox_id = ? order by id ";
		if(startPage > 0){
			sql += " LIMIT ?,?";
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, messageId);

			if(startPage > 0){
				pstmt.setInt(2, startPage);
				pstmt.setInt(3, BaseDao.pageSize);
			}
			rs = pstmt.executeQuery();
			while(rs.next()){
				messageList.add(new MessageBoxVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return messageList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getMessageInfo(java.lang.String)
	 */
	public MessageBoxVo getMessageInfo(String id) {
		// TODO Auto-generated method stub
		MessageBoxVo messageVo = null;

		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from view_message_box where id = ? ";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			while(rs.next()){
				messageVo = new MessageBoxVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return messageVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getNoReadMessageCount(java.lang.String)
	 */
	public int getNoReadMessageCount(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT count(*) as num  from t_message_box where isread = 0 and user_id = ?";


		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs= pstmt.executeQuery();
			if(rs.next()) count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IMessageBoxDao#getNoReadReplyCount(java.lang.String)
	 */
	public int getNoReadReplyCount(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT count(*) as num from t_message_box_reply where isread = 0 and user_id = ?";


		PreparedStatement pstmt = null;
		int count = 0;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs= pstmt.executeQuery();
			if(rs.next()) count = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count;
	}
}
