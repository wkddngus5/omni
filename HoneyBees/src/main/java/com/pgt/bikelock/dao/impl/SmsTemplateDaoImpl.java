/**
 * FileName:     SmsTemplateDaoImpl.java
 * @Description: TODO
* All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017年4月12日 下午12:17:29
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017年4月12日       CQCN         1.0             1.0
 * Why & What is modified: <初始化>
 */
package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.ISmsTemplateDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.SmsTemplateVo;

 /**
 * @ClassName:     SmsTemplateDaoImpl
 * @Description:短信模板业务接口实现/sms template business protocol achieve
 * @author:    Albert
 * @date:        2017年4月12日 下午12:17:29
 *
 */
public class SmsTemplateDaoImpl implements ISmsTemplateDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsTemplateDao#getTemplate(java.lang.String, int)
	 */
	public SmsTemplateVo getTemplate(String industryId, int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_sms_template  where industry_id = ? and type = ? " ;
		SmsTemplateVo templateVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, industryId);
			pstmt.setInt(2,type);
			rs = pstmt.executeQuery();
			if(rs.next()){
				templateVo = new SmsTemplateVo(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return templateVo; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsTemplateDao#getTemplate(java.lang.String)
	 */
	@Override
	public SmsTemplateVo getTemplate(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_sms_template  where id = ?" ;
		SmsTemplateVo templateVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				templateVo = new SmsTemplateVo(rs);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return templateVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsTemplateDao#getTemplateList(com.pgt.bikelock.vo.RequestListVo)
	 */
	@Override
	public List<SmsTemplateVo> getTemplateList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_sms_template where 1=1";
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and template like '%"+requestVo.getKeyWords()+"%'";
		}
		if(requestVo.getType() > 0){
			sql += " and type="+(requestVo.getType()-1);
		}
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >= "+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <= "+requestVo.getEndTime();
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		List<SmsTemplateVo> templateList = new ArrayList<SmsTemplateVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				templateList.add(new SmsTemplateVo(rs));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return templateList; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsTemplateDao#updateTemplate(com.pgt.bikelock.vo.SmsTemplateVo)
	 */
	@Override
	public boolean updateTemplate(SmsTemplateVo templateVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_sms_template set type=?,template= ?,date = now(),title = ? WHERE id= ?";
		 
		PreparedStatement pstmt = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,templateVo.getType());
			pstmt.setString(2,templateVo.getTemplate());
			pstmt.setString(3,templateVo.getTitle());
			pstmt.setString(4,templateVo.getId());
			 
			if(pstmt.executeUpdate()>0) flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ISmsTemplateDao#addTemplate(com.pgt.bikelock.vo.SmsTemplateVo)
	 */
	@Override
	public String addTemplate(SmsTemplateVo templateVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="insert into t_sms_template (type,template,industry_id,title,date) values (?,?,?,?,now())";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String tempId = "";
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1,templateVo.getType());
			pstmt.setString(2,templateVo.getTemplate());
			pstmt.setString(3, templateVo.getIndustry_id());
			pstmt.setString(4, templateVo.getTitle());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					tempId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return tempId;  
	}
	
}
