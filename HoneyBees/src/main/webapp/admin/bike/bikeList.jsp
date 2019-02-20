<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] statusType = rb.getString("bike_status_value").split(",");//get status type
	int status = ValueUtil.getInt(request.getAttribute("status"));

	String[] errorType = rb.getString("bike_lock_error_value").split(
			",");//get error type
	int error = ValueUtil.getInt(request.getAttribute("type"));
	String[] lockTypes = rb.getString("bike_lock_Type_values").split(
			",");
	int lockType = ValueUtil.getInt(request.getAttribute("extendType1"));
	String actionUrl = "bikeManage?requestType=20001";
	boolean export = true;
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form id="searchForm" onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("bike_keywords_tips") %>" /></td>
					<td><%=rb.getString("common_status_title")%>: <select
						name="status">
							<%
								for (int i = 0; i < statusType.length; i++) {
							%>
							<option value=<%=i%> <%if (status == i) {%> selected="selected"
								<%}%>><%=statusType[i]%></option>
							<%
								}
							%>
					</select></td>
					
					<td><%=rb.getString("bike_lock_type_title")%>: <select
						name="extendType1">
							<%
								for (int i = 0; i < lockTypes.length; i++) {
							%>
							<option value=<%=i%> <%if (lockType == i) {%> selected="selected"
								<%}%>><%=lockTypes[i]%></option>
							<%
								}
							%>
					</select></td>

					<td><%=rb.getString("bike_lock_error_title")%>: <select
						name="type">
							<%
								for (int i = 0; i < errorType.length; i++) {
							%>
							<option value=<%=i%> <%if (error == i) {%> selected="selected"
								<%}%>><%=errorType[i]%></option>
							<%
								}
							%>
					</select></td>
					<td>
					online:
					<select name="way">
						<option value="0" <c:if test="${way == 0}">selected="selected"</c:if>>All</option>
						<option value="1" <c:if test="${way == 1}">selected="selected"</c:if>>online</option>
						<option value="2" <c:if test="${way == 2}">selected="selected"</c:if>>offline</option>
					</select>
					</td>

				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button id="searchSubmit" type="submit"><%=rb.getString("common_search_title")%></button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
		<input type="hidden" name="tagIds" id="tagIds" />
	</form>
</div>
<div class="pageContent" layoutH="90">

	<%@ include file="../common/page_func.jspf"%>
	<div class="panel" defH="600">
		<table class="table" width="100%" height="80%" layoutH="138">
			<thead>
				<tr>
					<th width="120">ID</th>
					<th width="120" orderField="number" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
					<th width="120">IMEI</th>
					<th width="120">MAC</th>
					<th width="120"><%=rb.getString("common_operating_title")%></th>
					<th width="120"><%=rb.getString("bike_power")%></th>
					<th width="120"><%=rb.getString("bike_heart_time")%></th>
					<th width="120"><%=rb.getString("common_status_title")%></th>
					<th width="120" orderField="ride_count" class="${orderDirection}"><%=rb.getString("bike_use_count")%></th>
					<th width="120" orderField="last_ride_date"
						class="${orderDirection}"><%=rb.getString("bike_last_ride_time")%></th>
					<th width="120"><%=rb.getString("bike_gps_time")%></th>
					<th width="120"><%=rb.getString("bike_gps_lng")%>,<%=rb.getString("bike_gps_lat")%></th>
					
					<th width="120"><%=rb.getString("bike_lock_type_title")%></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${bikeList}" varStatus="status" var="bike">
					<tr target="id_item" rel="${bike.bid}">
						<td>${bike.bid}</td>
						<td><a href="bikeManage?requestType=20043&id=${bike.bid}"
							   target="navTab">${bike.number}</a></td>
						<td>${bike.imei}</td>
						<td>${bike.mac}</td>
						<td><a href="bikeManage?requestType=20020&id=${bike.bid}"
							target="_blank"><%=rb.getString("bike_show_in_map_title")%></a></td>
						<td>${bike.powerPercent}%</td>
						<td>${bike.heartTime}</td>
						<td>${bike.statusStr}</td>
						<td>${bike.rideCount}</td>
						<td>${bike.lastRideDate}</td>
						<td>${bike.gTime}</td>
						<td>${bike.gLng},${bike.gLat}</td>

						
						<td>${bike.bikeTypeStr}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<%@ include file="../common/page_num.jspf"%>
</div>

