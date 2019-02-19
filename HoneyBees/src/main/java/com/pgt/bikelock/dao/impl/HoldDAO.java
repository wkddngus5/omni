package com.pgt.bikelock.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.vo.HoldVO;
import com.pgt.bikelock.dao.impl.UserDaoImpl;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.ValueUtil;



public class HoldDAO {


	public List<HoldVO> getHoldsForUser (UserVo user) {

		if (user == null) {
			return new ArrayList<HoldVO>();
		}
		return getHoldsForUser(user.getuId());

	}


	public List<HoldVO> getHoldsForUser (String userId) {

		if (userId == null) {
			return new ArrayList<HoldVO>();
		}

		String sql = "SELECT h.*, t.amount FROM t_bike_hold h JOIN t_trade t ON t.id = h.trade_id WHERE user_id = ?";

		Connection connection = DataSourceUtil.getConnection();
		List<HoldVO> list = new ArrayList<HoldVO>();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setString(1, userId);

			results = statement.executeQuery();

			while (results.next()) {

				HoldVO hold = new HoldVO(results);
				hold.setTradeAmount(results.getBigDecimal("amount"));
				list.add(hold);

			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return list;

	}


	public HoldVO getHold (String holdId) {

		if (holdId == null) {
			return null;
		}

		String sql = "SELECT * FROM t_bike_hold WHERE id = ?";

		Connection connection = DataSourceUtil.getConnection();
		HoldVO hold = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setInt(1, ValueUtil.getInt(holdId));

			results = statement.executeQuery();

			if (results.next()) {
				hold = new HoldVO(results);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}


		return hold;

	}


	public HoldVO getLastHoldForUser (UserVo user) {

		if (user == null) {
			return null;
		}
		return getLastHoldForUser(user.getuId());

	}


	public HoldVO getLastHoldForUser (String userId) {

		if (userId == null) {
			return null;
		}

		String sql = "SELECT * FROM t_bike_hold WHERE user_id = ? ORDER BY start_time DESC LIMIT 1";

		Connection connection = DataSourceUtil.getConnection();
		HoldVO activeHold = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setString(1, userId);

			results = statement.executeQuery();

			if (results.next()) {
				activeHold = new HoldVO(results);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return activeHold;

	}


	public String insertHold (HoldVO hold) {

		if (hold == null) {
			return null;
		}

		String sql = "INSERT t_bike_hold " +
			"(prev_use_id, prev_bike_id, user_id, start_time, expiration_time, group_id) " + 
			"values (?, ?, ?, ?, ?, ?)";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;
		boolean success = false;

		try {

			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			java.text.SimpleDateFormat dateFormater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			statement.setInt   (1, ValueUtil.getInt(hold.getPrevUseId()));
			statement.setInt   (2, ValueUtil.getInt(hold.getPrevBikeId()));
			statement.setInt   (3, ValueUtil.getInt(hold.getUserId()));
			statement.setString(4, dateFormater.format(hold.getStartTime()));
			statement.setString(5, dateFormater.format(hold.getExpirationTime()));
			statement.setString(6, hold.getGroupId());

			success = statement.executeUpdate() > 0;

			if (success) {
				ResultSet rs = statement.getGeneratedKeys();
				rs.next();
				return rs.getString(1);
			}

			return null;

		}
		catch (SQLIntegrityConstraintViolationException e) {
			// record already exists
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return null;

	}


	public int closeHold (HoldVO hold) {

		if (hold == null) {
			return -1;
		}
		return closeHold(hold.getId(), hold.getNextUseId(), hold.getExpirationTime(), hold.getTradeId());

	}


	public int closeHold (String holdId, String nextUseId, Date expirationTime, String tradeId) {

		if (holdId == null || expirationTime == null || tradeId == null) {
			return -1;
		}

		String sql = nextUseId != null
			? "UPDATE t_bike_hold SET expiration_time = ?, trade_id = ?, next_use_id = ? WHERE id = ?"
			: "UPDATE t_bike_hold SET expiration_time = ?, trade_id = ? WHERE id = ?";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;

		try {

			statement = connection.prepareStatement(sql);
			java.text.SimpleDateFormat dateFormater = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			statement.setString(1, dateFormater.format(expirationTime));
			statement.setInt(2, ValueUtil.getInt(tradeId));
			
			if (nextUseId != null) {
				statement.setInt(3, ValueUtil.getInt(nextUseId));
				statement.setInt(4, ValueUtil.getInt(holdId));
			}
			else {
				statement.setInt(3, ValueUtil.getInt(holdId));
			}

			return statement.executeUpdate();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(null, statement, connection);
		}

		return -2;


	}


	public HoldVO getActiveHoldForUserId (String userId) {

		if (userId == null) {
			return null;
		}

		String sql = "SELECT * FROM t_bike_hold " +
			"WHERE expiration_time >= NOW() " + 
				"&& start_time <= NOW() " +
				"&& trade_id IS NULL " +
				"&& user_id = ?";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;
		HoldVO hold = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setString(1, userId);
			results = statement.executeQuery();

			if (results.next()) {
				hold = new HoldVO(results);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return hold;

	}


	public int getActiveHoldsCountWithBikeId (String bikeId) {

		if (bikeId == null) {
			return 0;
		}

		String sql = "SELECT COUNT(*) AS rowcount " +
			"FROM t_bike_hold " +
			"WHERE expiration_time >= NOW() " + 
				"&& start_time <= NOW() " +
				"&& trade_id IS NULL " +
				"&& prev_bike_id = ?";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;
		int activeHoldCount = 0;

		try {

			statement = connection.prepareStatement(sql);
			statement.setString(1, bikeId);
			results = statement.executeQuery();

			if (results.next()) {
				activeHoldCount = results.getInt("rowcount");
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return activeHoldCount;

	}


	public List<HoldVO> getExpiredHolds () {

		String sql = "SELECT * FROM t_bike_hold WHERE expiration_time < NOW() && trade_id IS NULL";

		Connection connection = DataSourceUtil.getConnection();
		List<HoldVO> list = new ArrayList<HoldVO>();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			results = statement.executeQuery();

			while (results.next()) {
				HoldVO hold = new HoldVO(results);
				list.add(hold);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return list;

	}

}