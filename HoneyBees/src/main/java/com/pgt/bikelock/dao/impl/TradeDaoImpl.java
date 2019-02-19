/**
 * FileName:     TradeDaolImpl.java
 * @Description: TODO
 * All rights Reserved, Designed By OMNI.COM  
 * Copyright:    Copyright(C) 2012-2017
 * Company       OMNI
 * @author:    Albert
 * @version    V1.0 
 * Createdate:         2017-3-28 上午10:07:46
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2017-3-28       CQCN         1.0             1.0
 * Why & What is modified: <初始化>/initialization
 */
package com.pgt.bikelock.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.dao.ITradeDao;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeUseVo;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.TradeVo;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.admin.StatisticsVo;

/**
 * @ClassName:     TradeDaolImpl
 * @Description:交易订单接口实现类/transaction order protocol achieve
 * @author:    Albert
 * @date:        2017-3-28 上午10:07:46
 *
 */
public class TradeDaoImpl implements ITradeDao{

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#addTrade(com.pgt.bikelock.vo.TradeVo)
	 */
	public boolean addBikeTrade(TradeVo tradeVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_trade (record_id,amount,status,type,way,uid,date,out_pay_id,city_id,account_pay_amount,gift_pay_amount)"
				+ " VALUES(?,?,?,1,?,?,now(),?,?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1,tradeVo.getRecordId());
			pstmt.setBigDecimal(2, tradeVo.getAmount());
			pstmt.setInt(3, tradeVo.getStatus());
			pstmt.setInt(4,tradeVo.getWay());
			pstmt.setString(5, tradeVo.getUid());
			pstmt.setString(6, tradeVo.getOut_pay_id());
			pstmt.setInt(7, tradeVo.getCityId());
			pstmt.setBigDecimal(8, tradeVo.getAccountPayAmount());
			pstmt.setBigDecimal(9, tradeVo.getGiftPayAmount());
			if(pstmt.executeUpdate()>0){
				flag =  true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag;  
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#addDepositOrRecharge(java.lang.String, java.math.BigDecimal, int)
	 */
	public String addTrade(TradeVo tradeVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO t_trade (record_id,amount,balance,type,way,uid,date,status,out_trade_no,city_id) "
				+ "VALUES(?,?,?,?,?,?,now(),?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String tradeId = null;
		try {
			pstmt = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS);

			pstmt.setString(1, tradeVo.getRecordId());
			pstmt.setBigDecimal(2, tradeVo.getAmount());
			pstmt.setBigDecimal(3, tradeVo.getAmount());
			pstmt.setInt(4, tradeVo.getType());
			pstmt.setInt(5, tradeVo.getWay());
			pstmt.setString(6, tradeVo.getUid());
			pstmt.setInt(7,tradeVo.getStatus());
			pstmt.setString(8,tradeVo.getOut_trade_no());
			pstmt.setInt(9, tradeVo.getCityId());
			if(pstmt.executeUpdate()>0){
				rs= pstmt.getGeneratedKeys();
				if(rs.next()){
					tradeId = rs.getString(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return tradeId;  
	}


	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getFinalTrade(java.lang.String)
	 */
	public TradeVo getNotifyTradeDetail(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_trade_bike  where notify = 0 and uid = ? " ;
		TradeVo  tradeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);

			rs = pstmt.executeQuery();
			while(rs.next()){
				tradeVo = new TradeVo(rs);
				tradeVo.setBikeUseVo(new BikeUseVo(rs,false));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return tradeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getBikeUseTradeDetail(java.lang.String)
	 */
	public TradeVo getBikeUseTradeDetail(String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_trade_bike  where id = ? " ;
		TradeVo  tradeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			while(rs.next()){
				tradeVo = new TradeVo(rs);
				tradeVo.setBikeUseVo(new BikeUseVo(rs,false));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return tradeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getBikeUseTradeDetail(long)
	 */
	@Override
	public TradeVo getBikeUseTradeDetail(long useDate) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_trade_bike  where use_date = ? " ;
		TradeVo  tradeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setLong(1, useDate);

			rs = pstmt.executeQuery();
			while(rs.next()){
				tradeVo = new TradeVo(rs);
				tradeVo.setBikeUseVo(new BikeUseVo(rs,false));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return tradeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getNoPayTrade(java.lang.String)
	 */
	public TradeVo getNoPayTrade(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT id,record_id,uid,amount,status,type from t_trade  where uid =? and status = 0 and type =1 ";
		TradeVo trade = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				trade = new TradeVo();
				trade.setId(rs.getString("id"));
				trade.setRecordId(rs.getString("record_id"));
				trade.setUid(rs.getString("uid"));
				trade.setAmount(rs.getBigDecimal("amount"));
				trade.setType(rs.getInt("type"));
				trade.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return trade; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getTradeInfo(java.lang.String)
	 */
	public TradeVo getTradeInfo(String tradeId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_trade  where id =?";
		TradeVo trade = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tradeId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				trade = new TradeVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return trade; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getTradeDetailInfo(java.lang.String)
	 */
	@Override
	public TradeVo getTradeDetailInfo(String tradeId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_trade  LEFT JOIN t_user  ON t_trade.uid = t_user.id"
				+ " where t_trade.id = ?";

		TradeVo trade = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, tradeId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				trade = new TradeVo(rs);
				trade.setUserVo(new UserVo(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return trade; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getFinalDepositTradeId(java.lang.String)
	 */
	public TradeVo getFinalDepositTradeInfo(String uid){
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * from t_trade  where uid = ? and type =3 and status = 1 order by id desc LIMIT 0,1";
		TradeVo tradeVo = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, uid);
			rs = pstmt.executeQuery();
			if(rs.next()){
				tradeVo = new TradeVo(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return tradeVo; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#updateNotifySuccess(java.lang.String)
	 */
	public boolean updateNotifySuccess(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_trade set notify = 1 WHERE uid=? and status=1 ";

		PreparedStatement pstmt = null; 
		boolean flag = false; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			if(pstmt.executeUpdate()>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#updatePayWay(java.lang.String, int)
	 */
	public boolean updatePayWay(String tradeId, int way) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_trade set way = ? WHERE id=? ";

		PreparedStatement pstmt = null; 
		boolean flag = false; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, way);
			pstmt.setString(2, tradeId);
			if(pstmt.executeUpdate()>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}




	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#updateTradeSuccess(int)
	 */
	public boolean updateTradeSuccess(String trainId,String out_trade_no) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_trade set status=1 ,notify = 1,out_trade_no = ? WHERE id=?";

		PreparedStatement pstmt = null; 
		boolean flag = false; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, out_trade_no);
			pstmt.setString(2, trainId);
			if(pstmt.executeUpdate()>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getTradeList(java.lang.String)
	 */
	public List<TradeVo> getBikeTradeList(String userId,int startPage) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  view_trade_bike  where  uid = ? order by id desc limit ?,?" ;
		List<TradeVo>  list = new ArrayList<TradeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			pstmt.setInt(2, startPage);
			pstmt.setInt(3, BaseDao.pageSize);

			rs = pstmt.executeQuery();
			while(rs.next()){
				TradeVo tradeVo = new TradeVo(rs);
				tradeVo.setBikeUseVo(new BikeUseVo(rs,false));
				list.add(tradeVo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getTradeList(java.lang.String)
	 */
	public List<TradeVo> getTradeList(String userId,int startPage,int tradeType) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT * FROM  t_trade  where  uid = ?" ;
		if(tradeType == 1){
			sql += " and (type =2 or type = 5)";
		}
		sql += "  order by id desc limit ?,?";
		List<TradeVo>  list = new ArrayList<TradeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setString(1, userId);
			pstmt.setInt(2, startPage);
			pstmt.setInt(3, BaseDao.pageSize);


			rs = pstmt.executeQuery();
			while(rs.next()){
				TradeVo tradeVo = new TradeVo(rs);
				list.add(tradeVo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}



	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getTradeList(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<TradeVo> getTradeList(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT t.*,t_user.phone,t_user.city_id FROM  t_trade t LEFT JOIN t_user  ON t.uid = t_user.id"
				+ " where (phone like '%"+ requestVo.getKeyWords()+"%' "
				+ " or t.id like '%"+ requestVo.getKeyWords()+"%' "
				+ " or out_trade_no like '%"+ requestVo.getKeyWords()+"%')";

		if(requestVo.getType() > 0){
			sql += " and type ="+requestVo.getType();
		}
		if(requestVo.getStatus() > 0){
			sql += " and status ="+ (ValueUtil.getInt(requestVo.getStatus())-1)+"";
		}
		if(requestVo.getWay() > 0){
			sql += " and way ="+requestVo.getWay();
		}

		if(requestVo.getStartTime() != null){
			sql += " and date >= '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date <= '"+requestVo.getEndTime()+"'";
		}
		if(requestVo.getCityId() > 0){
			sql += " and t.city_id = "+requestVo.getCityId();
		}
		sql += " order by id "+requestVo.getOrderDirection()+" LIMIT ?,?" ;

		List<TradeVo>  list = new ArrayList<TradeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				TradeVo tradeVo = new TradeVo(rs);
				tradeVo.setUserVo(new UserVo(rs));
				list.add(tradeVo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  t_trade t LEFT JOIN t_user  ON t.uid = t_user.id"
				+ " where (phone like '%"+ requestVo.getKeyWords()+"%' "
				+ " or t.id like '%"+ requestVo.getKeyWords()+"%' "
				+ " or out_trade_no like '%"+ requestVo.getKeyWords()+"%')";

		if(requestVo.getType() > 0){
			sql += " and type ="+requestVo.getType();
		}
		if(requestVo.getStatus() > 0){
			sql += " and status ="+ (ValueUtil.getInt(requestVo.getStatus())-1)+"";
		}
		if(requestVo.getWay() > 0){
			sql += " and way ="+requestVo.getWay();
		}

		if(requestVo.getStartTime() != null){
			sql += " and date >= '"+requestVo.getStartTime()+"'";
		}

		if(requestVo.getEndTime() != null){
			sql += " and date <= '"+requestVo.getEndTime()+"'";
		}
		if(requestVo.getCityId() > 0){
			sql += " and t.city_id = "+requestVo.getCityId();
		}


		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 

			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt("num");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getTypeCount(int)
	 */
	public int getTypeCount(int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num FROM  t_trade  where type = ? and status = 1" ;
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql); 
			pstmt.setInt(1, type);
			rs = pstmt.executeQuery();
			if(rs.next()){
				count = rs.getInt("num");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return count; 
	}

	/**
	 * 报表统计
	 * @Title:        getStatisticsList 
	 * @Description:  TODO
	 * @param:        @param tableName
	 * @param:        @param dateType 时间类型 1：日 2：周 3：月 4：年
	 * @param:        @return    
	 * @return:       List<StatisticsVo>    
	 * @author        Albert
	 * @Date          2017年6月30日 下午3:56:43
	 */
	public List<StatisticsVo> getStatisticsList(int dateType,int payType,String date) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		//获取可用类型元素
		String sql="";
		if(date == null || StringUtils.isEmpty(date)){
			date = "CURDATE()";
		}else{
			date = "'"+date+"'";
		}
		switch (dateType) {
		case 1://日
			sql = "select min(date) as min_time,max(date) as max_time," +
					"CONVERT(date_format(date,'%k'),SIGNED) as title from t_trade where date_format("+date+",'%Y-%m')=date_format(date,'%Y-%m') " +
					"and day("+date+") = date_format(date,'%e')";
			break;
		case 2://周
			sql = "select min(date) as min_time,max(date) as max_time," +
					"date_format(date,'%w') as title from t_trade where date_format(date,'%Y') = YEAR("+date+")  " +
					"and WEEKOFYEAR("+date+") = WEEKOFYEAR(date)";
			break;
		case 3://月
			sql = "select min(date) as min_time,max(date) as max_time," +
					"CONVERT(date_format(date,'%e'),SIGNED) as title from t_trade where date_format(date,'%Y') = YEAR("+date+")  " +
					"and MONTH("+date+") = MONTH(date)";
			break;
		case 4://年
			sql = "select min(date) as min_time,max(date) as max_time," +
					"CONVERT(date_format(date,'%c'),SIGNED) as title from t_trade where date_format(date,'%Y') = YEAR("+date+") ";
			break;
		default://日
			sql = "select min(date) as min_time,max(date) as max_time," +
					"CONVERT(date_format(date,'%k'),SIGNED) as title from t_trade where date_format("+date+",'%Y-%m')=date_format(date,'%Y-%m') " +
					"and day("+date+") = date_format(date,'%e') ";
			break;
		}
		if(payType != 0){
			sql += " and type ="+payType;
		}
		sql += " group by title ORDER  by title asc";
		List<String> typeValueList = new ArrayList<String>();
		List<String> typeMinTimeValueList = new ArrayList<String>();
		List<String> typeMaxTimeValueList = new ArrayList<String>();
		List<StatisticsVo> recordList = new ArrayList<StatisticsVo>();
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
			//遍历统计元素
			for (int i=0;i<typeValueList.size();i++) {
				sql = "select count(*) as value from t_trade   where date >= ? and date <= ?";
				if(payType != 0){
					sql += " and type ="+payType;
				}
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

	public List<StatisticsVo> getPayWayStatisticsList(int dateType,int payType) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		//获取可用类型元素/gain available type element
		String sql="";
		switch (dateType) {
		case 1://日
			sql = "select count(*) as value,way as title from t_trade where date_format(CURDATE(),'%Y-%m')=date_format(date,'%Y-%m') " +
					"and day(CURDATE()) = date_format(date,'%e')";
			break;
		case 2://周
			sql = "select count(*) as value,way as title from t_trade where date_format(date,'%Y') = YEAR(CURDATE())  " +
					"and WEEKOFYEAR(CURDATE()) = WEEKOFYEAR(date)";
			break;
		case 3://月
			sql = "select count(*) as value,way as title  from t_trade where date_format(date,'%Y') = YEAR(CURDATE())  " +
					"and MONTH(CURDATE()) = MONTH(date)";
			break;
		case 4://年
			sql = "select count(*) as value,way as title from t_trade where date_format(date,'%Y') = YEAR(CURDATE()) ";
			break;
		default://日
			sql = "select count(*) as value,way as title from t_trade where date_format(CURDATE(),'%Y-%m')=date_format(date,'%Y-%m') " +
					"and day(CURDATE()) = date_format(date,'%e') ";
			break;
		}
		if(payType != 0){
			sql += " and type ="+payType;
		}
		sql += " group by title";
		List<StatisticsVo> recordList = new ArrayList<StatisticsVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				StatisticsVo record = new StatisticsVo(rs);
				record.setTitle(TradeVo.getWayStr(ValueUtil.getInt(record.getTitle())));
				recordList.add(record);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}

		return recordList; 
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#updateRideFree(java.lang.String)
	 */
	public boolean updateTradeInfo(String tradeId,BigDecimal amount,int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_trade set status = ?,amount = ? WHERE id= ? ";

		PreparedStatement pstmt = null; 
		boolean flag = false; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setBigDecimal(2, amount);
			pstmt.setString(3, tradeId);
			if(pstmt.executeUpdate()>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#updateTradeBalance(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public boolean updateTradeBalance(String tradeId, BigDecimal amount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_trade set balance = ? WHERE id= ? ";

		PreparedStatement pstmt = null; 
		boolean flag = false; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, amount);
			pstmt.setString(2, tradeId);
			if(pstmt.executeUpdate()>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#updateTradePayAmount(java.lang.String, java.math.BigDecimal, java.math.BigDecimal)
	 */
	@Override
	public boolean updateTradePayAmount(String tradeId,
			BigDecimal accountAmount, BigDecimal giftAmount) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="update t_trade set account_pay_amount = ?,gift_pay_amount = ? WHERE id= ? ";

		PreparedStatement pstmt = null; 
		boolean flag = false; 
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setBigDecimal(1, accountAmount);
			pstmt.setBigDecimal(2, giftAmount);			
			pstmt.setString(3, tradeId);
			if(pstmt.executeUpdate()>0){
				flag=true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getUserTradeList(java.lang.String, int, int)
	 */
	@Override
	public List<TradeVo> getUserTradeList(String userId, int type, int status) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT t_trade.*,t_user.phone,t_user.city_id FROM  t_trade  LEFT JOIN t_user "
				+ " ON t_trade.uid = t_user.id where uid = ? and t_trade.type = ? and t_trade.status = ?";

		List<TradeVo>  list = new ArrayList<TradeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, type);
			pstmt.setInt(3, status);
			rs = pstmt.executeQuery();
			while(rs.next()){
				TradeVo tradeVo = new TradeVo(rs);
				tradeVo.setUserVo(new UserVo(rs));
				list.add(tradeVo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#getUserRechargeSuccessTradeList(java.lang.String)
	 */
	@Override
	public List<TradeVo> getUserRechargeSuccessTradeList(String userId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT t_trade.*,t_user.phone,t_user.city_id FROM  t_trade  LEFT JOIN t_user "
				+ " ON t_trade.uid = t_user.id where uid = ? and t_trade.type = 2  and (t_trade.status = 1 or t_trade.status = 3)";

		List<TradeVo>  list = new ArrayList<TradeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs = pstmt.executeQuery();
			while(rs.next()){
				TradeVo tradeVo = new TradeVo(rs);
				tradeVo.setUserVo(new UserVo(rs));
				list.add(tradeVo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list; 
	}
	
			/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.ITradeDao#checkRideRecordExist(java.lang.String)
	 */
	@Override
	public boolean checkRideRecordExist(String rideId) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from t_trade where record_id = ? ";

		List<TradeVo>  list = new ArrayList<TradeVo>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, rideId);
			rs = pstmt.executeQuery();
			if(rs.next() && rs.getInt("num") > 0){
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return false; 
	}
}
