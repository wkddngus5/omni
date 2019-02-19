package com.pgt.bikelock.dao;

import java.util.List;

import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.AdminVo;

public interface IAdminDao {
	String TABLE_NAME="t_admin";
	/**自增ID int(11) */
	String COLUMN_ID="id";
	/**用户ID varchar(20) */
	String COLUMN_USER_ID="user_name";
	/**昵称 varchar(11) */
	String COLUMN_NICKNAME="nickname";
	/**密码 varchar(20) */
	String COLUMN_PASSWORD="password";
	
	/**
	 * 在数据库中搜索用户对象/search user object in database
	 * @param userName 账号/account
	 * @param password 密码/password
	 * @return 用户对象/user object
	 */
	AdminVo find(String userName,String password);
	
	/**
	 * 
	 * @Title:        getAdminInfo 
	 * @Description:  获取管理员资料/get administrator data
	 * @param:        @param userId
	 * @param:        @return    
	 * @return:       AdminVo    
	 * @author        Albert
	 * @Date          2017年6月5日 下午5:54:09
	 */
	AdminVo getAdminInfo(int userId);
	
	/**
	 * 
	 * @Title:        getAdminInfo 
	 * @Description:  获取管理员资料/get administrator data
	 * @param:        @param userName
	 * @param:        @return    
	 * @return:       AdminVo    
	 * @author        Albert
	 * @Date          2017年7月15日 下午2:35:34
	 */
	AdminVo getAdminInfo(String userName);
	
	/**
	 * 
	 * @Title:        updatePassword 
	 * @Description:  修改密码/modify password
	 * @param:        @param userId
	 * @param:        @param oldPassword
	 * @param:        @param mewPassword
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月18日 下午10:04:28
	 */
	boolean updatePassword(String userId,String oldPassword,String mewPassword);
	
	/**
	 * 
	 * @Title:        updateNickname 
	 * @Description:  修改昵称/modify nickname
	 * @param:        @param userId
	 * @param:        @param nickname
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年4月18日 下午10:01:28
	 */
	boolean updateNickname(String userId,String nickname);


	/**
	 * 
	 * @Title:        addAdmin 
	 * @Description:  增加管理员资料/add administrator information
	 * @param:        @param admin
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月5日 下午5:56:25
	 */
	String addAdmin(AdminVo admin);
	
	/**
	 * 
	 * @Title:        updateAdminInfo 
	 * @Description:  修改管理员资料/modify administrator information
	 * @param:        @param admin
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年6月5日 下午5:56:43
	 */
	boolean updateAdminInfo(AdminVo admin);
	
	/**
	 * 管理员列表
	 * @Title:        getAdminList 
	 * @Description:  TODO
	 * @param:        @return    
	 * @return:       List<AdminVo>    
	 * @author        Albert
	 * @Date          2017年6月4日 上午12:07:13
	 */
	List<AdminVo> getAdminList(RequestListVo requestVo);
	
}
