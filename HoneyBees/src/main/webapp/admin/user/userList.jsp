<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] statusType = rb.getString("user_auth_status_value").split(
			",");//get status type
	int authStatus = ValueUtil.getInt(request.getAttribute("status"));
	String actionUrl = "userManage?requestType=30001";
	boolean export = true;
%>

<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">

	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>&funcId=${funcId}"
		method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("user_keywords_tips") %>" /></td>


					<td><%=rb.getString("user_register_date_title")%>： <input
						type="text" name="startTime" class="date" value="${startTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> -
						<input type="text" name="endTime" class="date" value="${endTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /></td>

					<td><%=rb.getString("common_status_title")%>: <select
						name="status">
							<%
								for (int i = 0; i < statusType.length; i++) {
							%>
							<option value=<%=i%> <%if (authStatus == i) {%>
								selected="selected" <%}%>><%=statusType[i]%></option>
							<%
								}
							%>
					</select></td>

				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit"><%=rb.getString("common_search_title")%></button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="80"><%=rb.getString("common_row_title")%></th>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("user_phone_title")%></th>
				<th width="120"><%=rb.getString("user_email_title")%></th>
				<th width="120" orderField="ride_count" class="${orderDirection}"><%=rb.getString("bike_use_count")%></th>
				<th width="120"><%=rb.getString("user_area_title")%></th>
				<th width="120" orderField="money" class="${orderDirection}"><%=rb.getString("user_money_title")%></th>
				<th width="120" orderField="gift_money" class="${orderDirection}"><%=rb.getString("user_gift_money_title")%></th>
				<th width="120"><%=rb.getString("user_auth_status_title")%></th>
				<th width="120"><%=rb.getString("user_login_date_title")%></th>
				<th width="120"><%=rb.getString("user_register_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" varStatus="status" var="user">
				<tr target="id_item" rel="${user.uid}">
					<td>${status.count}</td>
					<td>${user.uid}</td>
					<td>${user.userVo.nickName}</td>
					<td>${user.userVo.phone}</td>
					<td>${user.email}</td>
					<td>${user.rideCount}</td>
					<td>${user.userVo.cityName}</td>
					<td>${user.userVo.money}</td>
					<td>${user.userVo.giftMoney}</td>
					<td>${user.userVo.authStatusStr}</td>
					<td>${user.userVo.login_date}</td>
					<td>${user.userVo.register_date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>