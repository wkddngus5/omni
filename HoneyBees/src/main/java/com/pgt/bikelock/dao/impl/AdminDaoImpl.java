package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pgt.bikelock.dao.IAdminDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.AdminVo;



public class AdminDaoImpl implements IAdminDao {
	
	public AdminVo find(String userName, String password) {
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT * FROM t_admin WHERE user_name=? AND password =?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		AdminVo user = null;
		try {
			if(conn!=null){
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,userName);
				pstmt.setString(2,password);
				rs = pstmt.executeQuery();
				if(rs.next()){
					user = new AdminVo(rs);
				}
				rs.close();
				pstmt.close();
				if(user != null){
					sql = "update t_admin set login_date = now() where id = ?";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1,user.getId());
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs, pstmt, conn);
		}
		return user;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.rui.han.dao.IAdminDao#getAdminInfo(int)
	 */
	public AdminVo getAdminInfo(int userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "select * from t_admin where id = ?";
		PreparedStatement pst = null;
		ResultSet rst = null;
		AdminVo adminVo = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setInt(1, userId);
			rst = pst.executeQuery();
			if(rst.next()) {
				adminVo = new AdminVo(rst);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rst, pst, conn);
		}
		return adminVo;
	}

	public AdminVo getAdminInfo(String userName){
		Connection conn = DataSourceUtil.getConnection();
		String sql = "select * from t_admin where user_name = ?";
		PreparedStatement pst = null;
		ResultSet rst = null;
		AdminVo adminVo = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, userName);
			rst = pst.executeQuery();
			if(rst.next()) {
				adminVo = new AdminVo(rst);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rst, pst, conn);
		}
		return adminVo;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAdminDao#updatePassword(java.lang.String, java.lang.String)
	 */
	public boolean updatePassword(String userId,String oldPassword,String mewPassword) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_admin set password=?   WHERE id= ? and password = ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,mewPassword);
			pstmt.setString(2,userId);
			pstmt.setString(3,oldPassword);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IAdminDao#updateNickname(java.lang.String, java.lang.String)
	 */
	public boolean updateNickname(String userId, String nickname) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_admin set nickname=?   WHERE id= ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1,nickname);
			pstmt.setString(2,userId);
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}

	
	/* (non-Javadoc)
	 * @see com.pgt.rui.han.dao.IAdminDao#updateAdminInfo(com.pgt.rui.han.vo.AdminVo)
	 */
	public boolean updateAdminInfo(AdminVo admin) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "update t_admin set user_name = ?,password = ?,nickname = ?,city_id = ?,"
				+ "phone = ?,email = ?,is_admin = ? where id = ?";
		PreparedStatement pst = null;
		boolean flag = false;
		try {
			pst = conn.prepareStatement(sql);
	
			pst.setString(1, admin.getUsername());
			pst.setString(2, admin.getPassword());
			pst.setString(3, admin.getNickname());
			pst.setInt(4, admin.getCityId());
			pst.setString(5, admin.getPhone());
			pst.setString(6, admin.getEmail());
			pst.setInt(7, admin.getIsAdmin());
			pst.setInt(8,admin.getId());
			
			if(pst.executeUpdate() > 0){
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pst, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.rui.han.dao.IAdminDao#getAdminList()
	 */
	public List<AdminVo> getAdminList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_admin" ;
		if(requestVo.getCityId() != 0){
			sql += " where city_id ="+ requestVo.getCityId();
		}
		List<AdminVo>  list= new ArrayList<AdminVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			rs = pstmt.executeQuery();
			while(rs.next()){
				AdminVo group = new AdminVo(rs);
				list.add(group);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}



	/* (non-Javadoc)
	 * @see com.pgt.rui.han.dao.IAdminDao#addAdmin(com.pgt.rui.han.vo.AdminVo)
	 */
	public String addAdmin(AdminVo admin) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql = "insert into t_admin (user_name,password,nickname,register_date,city_id,"
				+ "phone,email,is_admin) values (?,?,?,now(),?,?,?,?)";
		PreparedStatement pst = null;
		ResultSet rs = null;
		String userId = null;
		try {
			pst = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
	
			pst.setString(1, admin.getUsername());
			pst.setString(2, admin.getPassword());
			pst.setString(3, admin.getNickname());
			pst.setInt(4, admin.getCityId());
			pst.setString(5, admin.getPhone());
			pst.setString(6, admin.getEmail());
			pst.setInt(7, admin.getIsAdmin());
			if(pst.executeUpdate()>0){
				rs= pst.getGeneratedKeys();
				if(rs.next()){
					userId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pst, conn);
		}
		return userId;
	}

}
