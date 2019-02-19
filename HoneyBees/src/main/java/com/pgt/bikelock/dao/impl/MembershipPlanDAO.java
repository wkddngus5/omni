
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

import org.apache.poi.util.StringUtil;

import com.alipay.api.internal.util.StringUtils;
import com.pgt.bikelock.vo.MembershipPlanVO;
import com.pgt.bikelock.vo.RequestListVo;
import com.pgt.bikelock.vo.UserMembershipVO;
import com.pgt.bikelock.vo.UserVo;
import com.pgt.bikelock.utils.DataSourceUtil;
import com.pgt.bikelock.utils.ValueUtil;



public class MembershipPlanDAO {


	public MembershipPlanVO updateMembershipPlanStripe (MembershipPlanVO plan, String stripeId) {

		if (plan == null || stripeId ==  null) {
			return null;
		}

		String sql = "UPDATE t_membership_plan SET stripe_id = ? WHERE id = ?";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;


		try {

			statement = connection.prepareStatement(sql);

			statement.setString(1, stripeId);
			statement.setInt(2, ValueUtil.getInt(plan.getId()));

			boolean success = statement.executeUpdate() > 0;

			if (!success) {
				return null;
			}

			plan.setStripeId(stripeId);

			return plan;

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return null;

	}


	public boolean updateMembershipPlan (String planId, String title, String description, boolean active,boolean isEdu) {

		if (planId == null || title ==  null || description == null) {
			return false;
		}

		String sql = "UPDATE t_membership_plan SET title = ?, description = ?, active = ?,is_education = ? WHERE id = ?";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;


		try {

			statement = connection.prepareStatement(sql);

			statement.setString(1, title);
			statement.setString(2, description);
			statement.setInt(3, active? 1: 0);
			statement.setInt(4, isEdu?1:0);
			statement.setString(5, planId);

			boolean success = statement.executeUpdate() > 0;
			return success;

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return false;

	}


	public MembershipPlanVO insertMembershipPlan (MembershipPlanVO plan) {

		if (plan == null) {
			return null;
		}

		String sql = "INSERT t_membership_plan (" +
			"city_id," +
			"`interval`," +
			"interval_count," +
			"plan_price," +
			"ride_unit," +
			"ride_free_unit_count," +
			"title," +
			"description," +
			"is_renewable," +
			"is_education" +
		") values (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

		Connection connection = DataSourceUtil.getConnection();
		List<UserMembershipVO> list = new ArrayList<UserMembershipVO>();
		PreparedStatement statement = null;
		ResultSet results = null;


		try {

			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			statement.setInt(1, ValueUtil.getInt(plan.getCityId()));
			statement.setInt(2, ValueUtil.getInt(plan.getInterval()));
			statement.setInt(3, ValueUtil.getInt(plan.getIntervalCount()));
			statement.setBigDecimal(4, plan.getPlanPrice());
			statement.setInt(5, plan.getRideUnit());
			statement.setInt(6, plan.getRideFreeUnitCount());
			statement.setString(7, plan.getTitle());
			statement.setString(8, plan.getDescription());
			statement.setInt(9, plan.getIsRenewable()? 1: 0);
			statement.setInt(10, plan.isEducation()?1:0);

			boolean success = statement.executeUpdate() > 0;
			if (!success) {
				return null;
			}

			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				plan.setId(rs.getInt(1) + "");
			}
			else {
				return null;
			}

			plan.setActive(true);

			return plan;

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


	public List<MembershipPlanVO> getMembershipPlans () {
		return getMembershipPlans(null);
	}


	public List<MembershipPlanVO> getMembershipPlans(RequestListVo requestVo) {
		String sql = "SELECT * FROM t_membership_plan where 1 =1 ";
		if(requestVo != null && requestVo.getCityId() != 0){
			sql += "  and city_id = "+requestVo.getCityId();
		}		
		if(requestVo != null && !StringUtils.isEmpty(requestVo.getKeyWords())){
			sql+= " and (title like '%"+requestVo.getKeyWords()+"%' "
					+ "or description like '%"+requestVo.getKeyWords()+"%' "
							+ "or stripe_id like '%"+requestVo.getKeyWords()+"%')";
		}
		if(requestVo != null){
			if(requestVo.getWay() > 0){
				sql += " and is_renewable = "+(requestVo.getWay()-1);
			}
			if(requestVo.getType() > 0){
				sql += " and is_education = "+(requestVo.getType()-1);
			}
			if(requestVo.getStatus() > 0){
				sql += " and active = "+(requestVo.getStatus()-1);
			}
			sql += " limit ?,?";
		}
		Connection connection = DataSourceUtil.getConnection();
		List<MembershipPlanVO> list = new ArrayList<MembershipPlanVO>();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			if(requestVo != null){
				statement.setInt(1,requestVo.getStartPage());
				statement.setInt(2,requestVo.getPageSize());
			}

			
			results = statement.executeQuery();

			while (results.next()) {
				MembershipPlanVO plan = new MembershipPlanVO(results);
				list.add(plan);
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

	
	public int getMembershipPlansCount(RequestListVo requestVo) {
		String sql = "SELECT count(*) FROM t_membership_plan where 1 =1 ";
		if(requestVo != null && requestVo.getCityId() != 0){
			sql += "  and city_id = "+requestVo.getCityId();
		}		
		if(requestVo != null && !StringUtils.isEmpty(requestVo.getKeyWords())){
			sql+= " and (title like '%"+requestVo.getKeyWords()+"%' "
					+ "or description like '%"+requestVo.getKeyWords()+"%' "
							+ "or stripe_id like '%"+requestVo.getKeyWords()+"%')";
		}
		if(requestVo != null){
			if(requestVo.getWay() > 0){
				sql += " and is_renewable = "+(requestVo.getWay()-1);
			}
			if(requestVo.getType() > 0){
				sql += " and is_education = "+(requestVo.getType()-1);
			}
			if(requestVo.getStatus() > 0){
				sql += " and active = "+(requestVo.getStatus()-1);
			}
			
		}
		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			
			
			results = statement.executeQuery();

			if(results.next()){
				return results.getInt(1);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return 0;

	}

	public List<MembershipPlanVO> getActiveMembershipPlansForCity (String cityId,boolean isEducation) {

		boolean queryAll =  cityId == null || cityId.equals("0");

		String sql = queryAll
			? "SELECT * FROM t_membership_plan WHERE active = 1 and is_education = ?"
			: "SELECT * FROM t_membership_plan WHERE city_id IN (?, 0) AND active = 1  and is_education = ?";

		Connection connection = DataSourceUtil.getConnection();
		List<MembershipPlanVO> list = new ArrayList<MembershipPlanVO>();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			if (!queryAll) {
				statement.setInt(1, ValueUtil.getInt(cityId));
				statement.setInt(2, isEducation ? 1:0);
			}else{
				statement.setInt(1, isEducation ? 1:0);
			}
			results = statement.executeQuery();

			while (results.next()) {
				MembershipPlanVO plan = new MembershipPlanVO(results);
				list.add(plan);
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


	public MembershipPlanVO getMembershipPlan (String planId) {

		String sql = "SELECT * FROM t_membership_plan WHERE id = ?";

		Connection connection = DataSourceUtil.getConnection();
		MembershipPlanVO plan = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setInt(1, ValueUtil.getInt(planId));
			results = statement.executeQuery();

			while (results.next()) {
				plan = new MembershipPlanVO(results);
				break;
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return plan;

	}


	public MembershipPlanVO getActiveMembershipPlan (String planId) {


		String sql = "SELECT * FROM t_membership_plan WHERE id = ? AND active = 1";

		Connection connection = DataSourceUtil.getConnection();
		MembershipPlanVO plan = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setInt(1, ValueUtil.getInt(planId));
			results = statement.executeQuery();

			while (results.next()) {
				plan = new MembershipPlanVO(results);
				break;
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return plan;

	}


	public int insertUserMembershipForUser (UserVo user, MembershipPlanVO plan, String stripeId) {

		if (user == null || plan == null) {
			return -1;
		}
		return insertUserMembershipForUser(user.getuId(), plan.getId(), stripeId);

	}


	public int insertUserMembershipForUser (String userId, String planId, String stripeId) { 


		MembershipPlanVO plan = getActiveMembershipPlan(planId);
		if (plan == null) {
			return -1;
		}

		String sql = "INSERT t_user_membership " +
			"(user_id, membership_plan_id, stripe_id, `interval`, interval_count) " + 
			"values (?, ?, ?, ?, ?)";

		Connection connection = DataSourceUtil.getConnection();
		List<UserMembershipVO> list = new ArrayList<UserMembershipVO>();
		PreparedStatement statement = null;
		ResultSet results = null;
		boolean success = false;

		try {

			statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, ValueUtil.getInt(userId));
			statement.setInt(2, ValueUtil.getInt(planId));
			statement.setString(3, stripeId);
			statement.setInt(4, plan.getInterval());
			statement.setInt(5, plan.getIntervalCount());
			success = statement.executeUpdate() > 0;

			if (success) {
				ResultSet rs = statement.getGeneratedKeys();
				rs.next();
				return rs.getInt(1);
			}

			return -1;

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

		return -1;

	}


	public boolean cancelImmediateUserMembershipForUser(UserVo user, UserMembershipVO userPlan)
	{

		if (user == null || userPlan == null) {
			return false;
		}
		return cancelImmediateUserMembershipForUser(user.getuId(), userPlan.getId());

	}


	public boolean cancelImmediateUserMembershipForUser(String userId, String userPlanId)
	{

		UserMembershipVO userMembership = getUserMembership(userPlanId);
		if (userMembership == null) {
			return false;
		}

		String sql = "UPDATE t_user_membership " +
			"SET canceled = 1, canceled_time = NOW(), through_time = NOW() " +
			"WHERE id = ? && user_id = ?";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;
		boolean success = false;

		try {

			statement = connection.prepareStatement(sql);

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			statement.setInt(1, ValueUtil.getInt(userPlanId));
			statement.setInt(2, ValueUtil.getInt(userId));
			success = statement.executeUpdate() > 0;

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return success;

	}


	public boolean cancelUserMembershipForUser (
		UserVo user, UserMembershipVO userPlan, Date throughTime)
	{

		if (user == null || userPlan == null) {
			return false;
		}
		return cancelUserMembershipForUser(user.getuId(), userPlan.getId(), throughTime);

	}


	public boolean cancelUserMembershipForUser (
		String userId, String userPlanId, Date throughTime)
	{

		UserMembershipVO userMembership = getUserMembership(userPlanId);
		if (userMembership == null) {
			return false;
		}

		String sql = "UPDATE t_user_membership " +
			"SET canceled = 1, canceled_time = NOW(), through_time = ? " +
			"WHERE id = ? && user_id = ?";

		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;
		boolean success = false;

		try {

			statement = connection.prepareStatement(sql);

			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			statement.setString(1, sdf.format(throughTime.getTime()));

			statement.setInt(2, ValueUtil.getInt(userPlanId));
			statement.setInt(3, ValueUtil.getInt(userId));
			success = statement.executeUpdate() > 0;

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return success;

	}


	public UserMembershipVO getUserMembership (String userPlanId) {

		String sql = "SELECT * FROM t_user_membership WHERE id = ?";

		Connection connection = DataSourceUtil.getConnection();
		UserMembershipVO userPlan = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setInt(1, ValueUtil.getInt(userPlanId));

			results = statement.executeQuery();

			while (results.next()) {
				userPlan = new UserMembershipVO(results);
				break;
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return userPlan;

	}

	public UserMembershipVO getUserMembershipWithStripeId (String stripeId) {

		String sql = "SELECT * FROM t_user_membership WHERE stripe_id = ?";

		Connection connection = DataSourceUtil.getConnection();
		UserMembershipVO userPlan = null;
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setString(1, stripeId);
			
			results = statement.executeQuery();

			while (results.next()) {
				userPlan = new UserMembershipVO(results);
				break;
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return userPlan;

	}


	public UserMembershipVO getActiveUserMembershipsForUser (UserVo user) {

		if (user == null) {
			return null;
		}
		return getActiveUserMembershipsForUser(user.getuId());

	}


	public UserMembershipVO getActiveUserMembershipsForUser (String userId) {

		List<UserMembershipVO> list = getUserMembershipsForUser(userId, true);
		UserMembershipVO membership = null;
		try {
			membership = list.get(0);
		}
		catch (java.lang.IndexOutOfBoundsException e) {
			// just return null
		}
		return membership;

	}


	public List<UserMembershipVO> getUserMembershipsForUser (UserVo user) {

		if (user == null) {
			return new ArrayList<UserMembershipVO>();
		}
		return getUserMembershipsForUser(user.getuId());

	}


	public List<UserMembershipVO> getUserMembershipsForUser (String userId) {
		return getUserMembershipsForUser(userId, false);
	}


	public List<UserMembershipVO> getUserMembershipsForUser (String userId, boolean onlyActive) {

		String sql = "SELECT " +
				"m.id AS \"m.id\"," +
				"m.city_id AS \"m.city_id\"," +
				"m.interval AS \"m.interval\"," +
				"m.interval_count AS \"m.interval_count\"," +
				"m.plan_price AS \"m.plan_price\"," +
				"m.ride_unit AS \"m.ride_unit\"," +
				"m.ride_free_unit_count AS \"m.ride_free_unit_count\"," +
				"m.title AS \"m.title\"," +
				"m.description AS \"m.description\"," +
				"m.active AS \"m.active\"," +
				"m.stripe_id AS \"m.stripe_id\", " +
				"m.is_renewable AS \"m.is_renewable\", " +
				"m.is_education AS \"m.is_education\", " +

				"um.id AS \"um.id\"," +
				"um.user_id AS \"um.user_id\"," +
				"um.membership_plan_id AS \"um.membership_plan_id\"," +
				"um.interval AS \"um.interval\", " +
				"um.interval_count AS \"um.interval_count\", " +
				"um.canceled AS \"um.canceled\"," +
				"um.start_time AS \"um.start_time\", " +
				"um.canceled_time AS \"um.canceled_time\", " +
				"um.through_time AS \"um.through_time\", " +
				"um.stripe_id AS \"um.stripe_id\" " +

			"FROM t_user_membership AS um " +
			"JOIN t_membership_plan AS m ON um.membership_plan_id = m.id " +
			"WHERE um.user_id = ? ";

		if (onlyActive) {
			sql = sql + " AND (um.through_time IS NULL OR um.through_time > NOW()) ORDER BY start_time DESC ";
		}

		Connection connection = DataSourceUtil.getConnection();
		List<UserMembershipVO> list = new ArrayList<UserMembershipVO>();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setInt(1, ValueUtil.getInt(userId));

			results = statement.executeQuery();

			while (results.next()) {

				UserMembershipVO userPlan = new UserMembershipVO(results, "um.");
				MembershipPlanVO plan = new MembershipPlanVO(results, "m.");
				userPlan.setPlan(plan);
				
				list.add(userPlan);

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


	public List<UserMembershipVO> getUserMemberships (RequestListVo requestVo) {

		String sql = "SELECT " +

				"m.id AS \"m.id\"," +
				"m.city_id AS \"m.city_id\"," +
				"m.interval AS \"m.interval\"," +
				"m.interval_count AS \"m.interval_count\"," +
				"m.plan_price AS \"m.plan_price\"," +
				"m.ride_unit AS \"m.ride_unit\"," +
				"m.ride_free_unit_count AS \"m.ride_free_unit_count\"," +
				"m.title AS \"m.title\"," +
				"m.description AS \"m.description\"," +
				"m.active AS \"m.active\"," +
				"m.stripe_id AS \"m.stripe_id\", " +
				"m.is_renewable AS \"m.is_renewable\", " +
				"m.is_education AS \"m.is_education\", " +

				"um.id AS \"um.id\"," +
				"um.user_id AS \"um.user_id\"," +
				"um.membership_plan_id AS \"um.membership_plan_id\"," +
				"um.interval AS \"um.interval\", " +
				"um.interval_count AS \"um.interval_count\", " +
				"um.canceled AS \"um.canceled\"," +
				"um.start_time AS \"um.start_time\", " +
				"um.canceled_time AS \"um.canceled_time\", " +
				"um.through_time AS \"um.through_time\", " +
				"um.stripe_id AS \"um.stripe_id\", " +
				
				"u.id AS \"u.id\"," +
				"u.phone AS \"u.phone\"" +

			"FROM t_user_membership AS um " +
			"JOIN t_membership_plan AS m ON um.membership_plan_id = m.id " +
			"JOIN t_user AS u ON um.user_id = u.id where 1=1";

		if (requestVo.getCityId() > 0) {
			sql = sql + " and u.city_id = "+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (title like '%"+requestVo.getKeyWords()+"%' or description like '%"+requestVo.getKeyWords()+"%'"
					+ " or um.stripe_id like '%"+requestVo.getKeyWords()+"%' or phone like '%"+requestVo.getKeyWords()+"%')";
		}
		if(requestVo.getStatus() > 0){
			sql += " and um.canceled = "+(requestVo.getStatus()-1);
		}
		if(requestVo.getType() == 1){
			//available
			sql += " and um.through_time > now()";
		}else if(requestVo.getType() == 2){
			//expire
			sql += " and um.through_time <= now()";
		}
		sql+= " limit ?,?";

		Connection connection = DataSourceUtil.getConnection();
		List<UserMembershipVO> list = new ArrayList<UserMembershipVO>();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);
			statement.setInt(1,requestVo.getStartPage());
			statement.setInt(2,requestVo.getPageSize());

			results = statement.executeQuery();

			while (results.next()) {

				UserMembershipVO userPlan = new UserMembershipVO(results, "um.");
				if(userPlan.getThroughTime() == null || userPlan.getThroughTime().after(new Date())){
					userPlan.setAvailable(1);
				}				
				
				MembershipPlanVO plan = new MembershipPlanVO(results, "m.");

				UserVo user = new UserVo();
				user.setuId(results.getString("u.id"));
				user.setPhone(results.getString("u.phone"));

				userPlan.setPlan(plan);
				userPlan.setUser(user);
				
				list.add(userPlan);

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
	
	public int getUserMembershipsCount(RequestListVo requestVo) {

		String sql = "SELECT count(*) FROM t_user_membership AS um " +
			"JOIN t_membership_plan AS m ON um.membership_plan_id = m.id " +
			"JOIN t_user AS u ON um.user_id = u.id where 1=1 ";

		if (requestVo.getCityId() > 0) {
			sql = sql + " and u.city_id = "+requestVo.getCityId();
		}
		if(!StringUtils.isEmpty(requestVo.getKeyWords())){
			sql += " and (title like '%"+requestVo.getKeyWords()+"%' or description like '%"+requestVo.getKeyWords()+"%'"
					+ " or um.stripe_id like '%"+requestVo.getKeyWords()+"%' or phone like '%"+requestVo.getKeyWords()+"%')";
		}
		if(requestVo.getStatus() > 0){
			sql += " and um.canceled = "+(requestVo.getStatus()-1);
		}
		if(requestVo.getType() == 1){
			//available
			sql += " and um.through_time > now()";
		}else if(requestVo.getType() == 2){
			//expire
			sql += " and um.through_time <= now()";
		}
		Connection connection = DataSourceUtil.getConnection();
		PreparedStatement statement = null;
		ResultSet results = null;

		try {

			statement = connection.prepareStatement(sql);

			results = statement.executeQuery();

			if(results.next()){
				return results.getInt(1);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			DataSourceUtil.close(results, statement, connection);
		}

		return 0;

	}

}