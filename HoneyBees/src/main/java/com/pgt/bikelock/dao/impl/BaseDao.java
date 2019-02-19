package com.pgt.bikelock.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;


public class BaseDao {
	
	public static final int pageSize = 60;//分页大小/paging size

	/**
	 * 获取所有的数据条数/obtain all date
	 * @param tableName 表名/datasheet name
	 * @param keyWords 关键字/keyword
	 * @param keyParms 匹配关键字段/match keyword
	 * @return
	 */
	public static int getCount(String tableName,RequestListVo requestVo,String[] keyParms) {
		Connection conn = DataSourceUtil.getConnection();
		String sql ="SELECT COUNT(*) AS total  FROM "+tableName+" where 1=1";
		for (int i = 0; i < keyParms.length; i++) {
			if(i == 0){
				sql += " and ("+keyParms[i]+" like '%"+requestVo.getKeyWords()+"%'";
			}else{
				sql += " or "+keyParms[i]+" like '%"+requestVo.getKeyWords()+"%' ";
			}
			if(i ==  keyParms.length-1){
				sql += ") ";
			}
		}
		if(requestVo.getCityId() > 0){
			sql += " and city_id ="+requestVo.getCityId();
		}
		if(requestVo.getType() > 0){
			sql += " and type ="+requestVo.getType();
		}
	
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and date >"+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and date <="+requestVo.getEndTime();
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
	

	/***
	 * 删除记录/delete record
	 * @param tableName
	 * @param recordId
	 * @return
	 */
	public static boolean deleteRecord(String tableName,String recordId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="delete from "+tableName+" where id in ("+recordId+")" ;               
		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;  
	}
	
	/**
	 * 
	 * @Title:        isExistColumn 
	 * @Description:  判定列是否存在/judge whether exsist
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return    
	 * @return:       boolean    
	 * @author        Albert
	 * @Date          2017年5月2日 上午9:49:21
	 */
	public static boolean isExistColumn(ResultSet rs, String columnName) {  
	    try {  
	        if (rs.findColumn(columnName) > 0 ) {  
	            return true;  
	        }   
	    }  
	    catch (SQLException e) {  
	        return false;  
	    }  
	      
	    return false;  
	}
	
	/**
	 * 
	 * @Title:        getString 
	 * @Description:  字符串获取/get the string
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       String    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:46:00
	 */
	public static String getString(ResultSet rst,String columnName) throws SQLException{
		try {
			if (!isExistColumn(rst, columnName)) {
				return "";
			}
			return rst.getString(columnName);
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
	}
	
	/**
	 * 
	 * @Title:        getDouble 
	 * @Description:  浮点型获取/floating point acquisition
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       Double    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:47:10
	 */
	public static Double getDouble(ResultSet rst,String columnName) throws SQLException{
		try {
			if (!isExistColumn(rst, columnName)) {
				return 0.0;
			}
			return rst.getDouble(columnName);
		} catch (Exception e) {
			// TODO: handle exception
			return 0.0;
		}
	}
	
	/**
	 * 
	 * @Title:        getInt 
	 * @Description:  整型获取/integer acquisition
	 * @param:        @param rs
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       int    
	 * @author        Albert
	 * @Date          2017年5月16日 上午10:47:56
	 */
	public static int getInt(ResultSet rst,String columnName) throws SQLException{
		try {
			if (!isExistColumn(rst, columnName)) {
				return 0;
			}
			return rst.getInt(columnName);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	/**
	 * 
	 * @Title:        getLong 
	 * @Description:  长整型获取/long integer get
	 * @param:        @param rst
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       long    
	 * @author        Albert
	 * @Date          2017年6月30日 下午3:55:28
	 */
	public static long getLong(ResultSet rst,String columnName) throws SQLException{
		try {
			if (!isExistColumn(rst, columnName)) {
				return 0;
			}
			return rst.getLong(columnName);
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}
	
	/**
	 * 
	 * @Title:        getBigDecimal 
	 * @Description:  金额获取/get amount
	 * @param:        @param rst
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       BigDecimal    
	 * @author        Albert
	 * @Date          2017年5月16日 上午11:00:54
	 */
	public static BigDecimal getBigDecimal(ResultSet rst,String columnName) throws SQLException{
		try {
			if (!isExistColumn(rst, columnName)) {
				return null;
			}
			return rst.getBigDecimal(columnName);
		} catch (Exception e) {
			// TODO: handle exception
			return new BigDecimal(0);
		}
	}
	
	/**
	 * 
	 * @Title:        getTimestamp 
	 * @Description:  TODO
	 * @param:        @param rst
	 * @param:        @param columnName
	 * @param:        @return
	 * @param:        @throws SQLException    
	 * @return:       Timestamp    
	 * @author        Albert
	 * @Date          2018年3月31日 下午6:03:06
	 */
	public static Timestamp getTimestamp(ResultSet rst,String columnName) throws SQLException{
		try {
			if (!isExistColumn(rst, columnName)) {
				return null;
			}
			if(rst.getTimestamp(columnName) == null){
				return new Timestamp(TimeUtil.getCurrentLongTime());
			}
			return rst.getTimestamp(columnName);
		} catch (Exception e) {
			// TODO: handle exception
			return new Timestamp(TimeUtil.getCurrentLongTime());
		}
	}
	
	/**
	 * 报表统计
	 * @Title:        getStatisticsList 
	 * @Description:  TODO
	 * @param:        @param tableName
	 * @param:        @param dateType 时间类型 1：日 2：周 3：月 4：年 time type:1. day 2:week 3:month 4: year
	 * @param:        @return    
	 * @return:       List<StatisticsVo>    
	 * @author        Albert
	 * @Date          2017年6月30日 下午3:56:43
	 */
	public static List<StatisticsVo> getStatisticsList(String tableName,String cloumnName,int dateType,String date) {
		// TODO Auto-generated method stub
		if(cloumnName == null){
			cloumnName = "date";
		}
		Connection conn = DataSourceUtil.getConnection();
		//获取可用类型元素/get available type element
		String sql="";
		if(date == null || StringUtils.isEmpty(date)){
			date = "CURDATE()";
		}else{
			date = "'"+date+"'";
		}
		switch (dateType) {
		case 1://日/day
			sql = "select min("+cloumnName+") as min_time,max("+cloumnName+") as max_time," +
					"CONVERT(date_format("+cloumnName+",'%k'),SIGNED) as title from "+tableName+" "
							+ "where date_format("+date+",'%Y-%m')=date_format("+cloumnName+",'%Y-%m') " +
					"and day("+date+") = date_format("+cloumnName+",'%e')";
			break;
		case 2://周/week
			sql = "select min("+cloumnName+") as min_time,max("+cloumnName+") as max_time," +
					"CONVERT(date_format("+cloumnName+",'%w'),SIGNED) as title from "+tableName+" where date_format("+cloumnName+",'%Y') = YEAR("+date+")  " +
					"and WEEKOFYEAR("+date+") = WEEKOFYEAR("+cloumnName+")";
			break;
		case 3://月/month
			sql = "select min("+cloumnName+") as min_time,max("+cloumnName+") as max_time," +
					"CONVERT(date_format("+cloumnName+",'%e'),SIGNED) as title from "+tableName+" where date_format("+cloumnName+",'%Y') = YEAR("+date+")  " +
					"and MONTH("+date+") = MONTH("+cloumnName+")";
			break;
		case 4://年/year
			sql = "select min("+cloumnName+") as min_time,max("+cloumnName+") as max_time," +
					"CONVERT(date_format("+cloumnName+",'%c'),SIGNED) as title from "+tableName+" where date_format("+cloumnName+",'%Y') = YEAR("+date+") ";
			break;
		default://日/day
			sql = "select min("+cloumnName+") as min_time,max("+cloumnName+") as max_time," +
					"CONVERT(date_format("+cloumnName+",'%k'),SIGNED) as title from "+tableName+" where date_format("+date+",'%Y-%m')=date_format("+cloumnName+",'%Y-%m') " +
					"and day("+date+") = date_format("+cloumnName+",'%e') ";
			break;
		}
		sql += " group by title ORDER  by title asc";
		List<String> typeValueList = new ArrayList<String>();
		List<String> typeMinTimeValueList = new ArrayList<String>();
		List<String> typeMaxTimeValueList = new ArrayList<String>();
		List<StatisticsVo> recordList = new ArrayList<StatisticsVo>();
		recordList.add(new StatisticsVo());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String value = rs.getString("title");
				if(value != null){
					typeValueList.add(value);
					typeMinTimeValueList.add(rs.getString("min_time"));
					typeMaxTimeValueList.add(rs.getString("max_time"));
				}

			}
			//遍历统计元素/watch caculate element
			for (int i=0;i<typeValueList.size();i++) {
				sql = "select count(*) as value from "+tableName+
						"  where "+cloumnName+" >= ? and "+cloumnName+" <= ?";
				pstmt = conn.prepareStatement(sql);
				String typeValue = typeValueList.get(i);
				pstmt.setString(1, typeMinTimeValueList.get(i));
				pstmt.setString(2, typeMaxTimeValueList.get(i));
				rs = pstmt.executeQuery();
				while(rs.next()){
					StatisticsVo record = new StatisticsVo(rs);
					record.setTitle(typeValue);
					recordList.add(record);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return recordList; 
	}
	
}
