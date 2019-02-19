package com.pgt.bikelock.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alipay.api.internal.util.StringUtils;
import com.amazonaws.RequestClientOptions.Marker;
import com.pgt.bikelock.dao.IBikeOrderRecord;
import com.pgt.bikelock.resource.OthersSource;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.TimeUtil;
import com.pgt.bikelock.utils.ValueUtil;
import com.pgt.bikelock.vo.BikeVo;
import com.pgt.bikelock.vo.LatLng;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.admin.BikeOrderRecord;
import com.pgt.bikelock.vo.admin.StatisticsVo;



public class BikeOrderRecordDaoImpl implements IBikeOrderRecord{


	public boolean insert(long imei, int orderId, long time, String content) {

		//判定是否需要保存指令/judge whether need keep command
//		if("1".equals(OthersSource.getSourceString("save_lock_order"))){
		Connection conn = DataSourceUtil.getConnection();
		String sql="INSERT INTO "+TABLE_NAME+"("
				+COLUMN_ORDER_ID+","
				+COLUMN_IMEI+","
				+COLUMN_TIME+","
				+COLUMN_CONTENT+") VALUES(?,?,?,?)";

		boolean flag = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,orderId);
			pstmt.setLong(2,imei);
			pstmt.setLong(3,time);
			pstmt.setString(4,content);
			if(pstmt.executeUpdate()>0){
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(pstmt, conn);
		}
		return flag;
		/*}else{
			return true;
		}*/


	}

	public BikeOrderRecord findLastByImei(long imei,int orderId) {
		Connection conn = DataSourceUtil.getConnection();
		BikeOrderRecord  orderRecord =null;
		String sql="SELECT * FROM "+TABLE_NAME+" WHERE imei=? and order_id=? ORDER BY id DESC";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, imei);
			pstmt.setInt(2, orderId);
			rs = pstmt.executeQuery();
			if(rs.next()){
				orderRecord=new BikeOrderRecord();
				orderRecord.setContent(rs.getString("content"));
				orderRecord.setId(rs.getInt("id"));
				orderRecord.setImei(rs.getLong("imei"));
				orderRecord.setOrderId(rs.getInt("order_id"));
				orderRecord.setTime(rs.getLong("time"));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return orderRecord;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeOrderRecord#haveRecordInMinutes(int, long)
	 */
	public boolean haveRecordInMinutes(int seconds, long imei) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		boolean flag = false;
		String sql="SELECT count(*) as num FROM "+TABLE_NAME+" WHERE imei=?  and content not like '%BLE%' and time >= UNIX_TIMESTAMP() - ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, imei);
			pstmt.setInt(2, seconds);
			rs = pstmt.executeQuery();
			if(rs.next()){
				flag = rs.getInt("num") > 0 ?true:false;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return flag;
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
	public List<StatisticsVo> getStatisticsList(String imei,int dateType,int dataType) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		//获取可用类型元素/get available type element
		String sql="";
		switch (dateType) {
			case 1://日/day
				sql = "select min(time) as min_time,max(time) as max_time," +
						"CONVERT(date_format(FROM_UNIXTIME(time),'%k'),SIGNED) as title from t_bike_order_record where date_format(CURDATE(),'%Y-%m')=date_format(FROM_UNIXTIME(time),'%Y-%m') " +
						"and day(CURDATE()) = date_format(FROM_UNIXTIME(time),'%e')";
				break;
			case 2://周/week
				sql = "select min(time) as min_time,max(time) as max_time," +
						"CONVERT(date_format(FROM_UNIXTIME(time),'%w'),SIGNED) as title from t_bike_order_record where date_format(FROM_UNIXTIME(time),'%Y') = YEAR(CURDATE())  " +
						"and WEEKOFYEAR(CURDATE()) = WEEKOFYEAR(FROM_UNIXTIME(time))";
				break;
			case 3://月/month
				sql = "select min(time) as min_time,max(time) as max_time," +
						"CONVERT(date_format(FROM_UNIXTIME(time),'%e'),SIGNED) as title from t_bike_order_record where date_format(FROM_UNIXTIME(time),'%Y') = YEAR(CURDATE())  " +
						"and MONTH(CURDATE()) = MONTH(FROM_UNIXTIME(time))";
				break;
			case 4://年/year
				sql = "select min(time) as min_time,max(time) as max_time," +
						"CONVERT(date_format(FROM_UNIXTIME(time),'%c'),SIGNED) as title from t_bike_order_record where date_format(FROM_UNIXTIME(time),'%Y') = YEAR(CURDATE()) ";
				break;
			default://日/day
				sql = "select min(time) as min_time,max(time) as max_time," +
						"CONVERT(date_format(FROM_UNIXTIME(time),'%k'),SIGNED) as title from t_bike_order_record where date_format(CURDATE(),'%Y-%m')=date_format(FROM_UNIXTIME(time),'%Y-%m') " +
						"and day(CURDATE()) = date_format(FROM_UNIXTIME(time),'%e') ";
				break;
		}
		if(dataType != 0){
			sql += " and order_id ="+dataType;
		}
		sql += " and imei = ? group by title ORDER  by title asc";
		List<String> typeValueList = new ArrayList<String>();
		List<String> typeMinTimeValueList = new ArrayList<String>();
		List<String> typeMaxTimeValueList = new ArrayList<String>();
		List<StatisticsVo> recordList = new ArrayList<StatisticsVo>();
		recordList.add(new StatisticsVo());
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, imei);
			rs = pstmt.executeQuery();
			while(rs.next()){
				String value = rs.getString("title");
				if(value != null){
					typeValueList.add(value);
					typeMinTimeValueList.add(rs.getString("min_time"));
					typeMaxTimeValueList.add(rs.getString("max_time"));
				}

			}
			//遍历统计元素/check Statistical elements
			for (int i=0;i<typeValueList.size();i++) {
				sql = "select count(*) as value from t_bike_order_record  where time >= ? and time <= ? and imei = ?";
				if(dataType != 0){
					sql += " and order_id ="+dataType;
				}
				pstmt = conn.prepareStatement(sql);
				String typeValue = typeValueList.get(i);
				pstmt.setString(1, typeMinTimeValueList.get(i));
				pstmt.setString(2, typeMaxTimeValueList.get(i));
				pstmt.setString(3, imei);
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

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeOrderRecord#getOrderRecord(com.pgt.bikelock.vo.RequestListVo)
	 */
	public List<BikeOrderRecord> getOrderRecord(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT r.*,b.number,b.id as bid from t_bike_order_record r left join t_bike b" +
				" on r.imei = b.imei where (r.imei like '%"+requestVo.getKeyWords()+"%' " +
				"or b.number like '%"+requestVo.getKeyWords()+"%' or r.content like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getType() != 0){
	/*		if(requestVo.getType() == 11){
				//报警指令时，同时显示定位指令/alarming command, show position command
				sql += " and (order_id = 11 or order_id = 5)";
			}else{
				sql += " and order_id = "+requestVo.getType();
			}*/
			sql += " and order_id = "+requestVo.getType();

		}
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and time >= "+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and time <= "+requestVo.getEndTime();
		}

		if(requestVo.getCityId() != 0){
			sql += " and b.city_id =" + requestVo.getCityId();
		}

		if(!StringUtils.isEmpty(requestVo.getTagIds())){
			sql += " and r.imei in ("+requestVo.getTagIds()+") order by r.imei desc LIMIT ?,?";
		}else{
			sql += " order by r.id "+requestVo.getOrderDirection()+" LIMIT ?,?";
		}

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeOrderRecord>  list = new ArrayList<BikeOrderRecord>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,requestVo.getStartPage());
			pstmt.setInt(2,requestVo.getPageSize());
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeOrderRecord record = new BikeOrderRecord(rs);
				record.setDate(TimeUtil.formaStrDate(record.getTime()+""));
				list.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeOrderRecord#getLastOrderRecord(int, int)
	 */
	@Override
	public List<BikeOrderRecord> getLastOrderRecord(int type) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT max(time),imei from t_bike_order_record where order_id = ?  group BY imei";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeOrderRecord>  list = new ArrayList<BikeOrderRecord>();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, type);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeOrderRecord record = new BikeOrderRecord(rs);
				list.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeOrderRecord#getWarnOrderRecordGroup()
	 */
	public List<BikeOrderRecord> getWarnOrderRecordGroup() {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT COUNT(*) as count,imei from t_bike_order_record where order_id = 11  GROUP BY imei";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BikeOrderRecord>  list = new ArrayList<BikeOrderRecord>();
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				BikeOrderRecord record = new BikeOrderRecord(rs);
				list.add(record);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see com.pgt.bikelock.dao.IBikeOrderRecord#getOrderRecordCount(com.pgt.bikelock.vo.RequestListVo)
	 */
	public int getOrderRecordCount(RequestListVo requestVo) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT count(*) as num from t_bike_order_record r left join t_bike b" +
				" on r.imei = b.imei where (r.imei like '%"+requestVo.getKeyWords()+"%' " +
				"or b.number like '%"+requestVo.getKeyWords()+"%' or r.content like '%"+requestVo.getKeyWords()+"%')";
		if(requestVo.getType() != 0){
			/*if(requestVo.getType() == 11){
				//报警指令时，同时显示定位指令/alarm command, show position command meanwhile
				sql += " and (order_id = 11 or order_id = 5)";
			}else{
				sql += " and order_id = "+requestVo.getType();
			}*/
			sql += " and order_id = "+requestVo.getType();
		}
		if(!StringUtils.isEmpty(requestVo.getStartTime())){
			sql += " and time >= "+requestVo.getStartTime();
		}
		if(!StringUtils.isEmpty(requestVo.getEndTime())){
			sql += " and time <= "+requestVo.getEndTime();
		}
		if(!StringUtils.isEmpty(requestVo.getTagIds())){
			sql += " and r.imei in ("+requestVo.getTagIds()+")";
		}
		if(requestVo.getCityId() != 0){
			sql += " and b.city_id =" + requestVo.getCityId();
		}
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
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
	 * @see com.pgt.bikelock.dao.IBikeOrderRecord#getAlarmLocation(java.lang.String, java.lang.String)
	 */
	@Override
	public LatLng getAlarmLocation(long imei, String id) {
		// TODO Auto-generated method stub
		Connection conn = DataSourceUtil.getConnection();
		String sql="SELECT content FROM "+TABLE_NAME+" WHERE imei = ? and id > ? and order_id = 5 ORDER BY id asc LIMIT 0,1";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String content = "";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, imei);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				content = rs.getString("content");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DataSourceUtil.close(rs,pstmt, conn);
		}
		LatLng location = null;
		if(!StringUtils.isEmpty(content)){
			try {
				location = new LatLng(
						ValueUtil
								.getDouble(content.split("lat=")[1].split(",")[0]),
						ValueUtil.getDouble(content.split("lng=")[1]));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return location;
	}

}
