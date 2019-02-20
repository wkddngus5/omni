<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "bikeManage?requestType=20021";
%>
<%@ include file="../common/page_from.jspf"%>
<div class="pageHeader">
	<form onsubmit="return dwzSearch(this, 'dialog');"
		action="<%=actionUrl %>" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_id_title")%>：<input type="text"
						name="keyWords" value="${keyWords}" /></td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit"><%=rb.getString("common_search_title")%></button>
							</div>
						</div></li>
					<li><div class="button">
							<div class="buttonContent">
								<button type="button" multLookup="bikeVo"
									warn="<%=rb.getString("common_select_warn")%>">
									<%=rb.getString("common_select_title")%></button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="30"><input type="checkbox" class="checkboxCtrl"
					group="bikeVo" /></th>
				<th width="120"><%=rb.getString("common_id_title")%></th>
				<th width="120">IMEI</th>
				<th width="120"><%=rb.getString("bike_gps_time")%></th>
				<th width="120"><%=rb.getString("bike_gps_lng")%></th>
				<th width="120"><%=rb.getString("bike_gps_lat")%></th>
				<th width="120"><%=rb.getString("bike_heart_time")%></th>
				<th width="120"><%=rb.getString("common_select_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${bikeList}" varStatus="status" var="bike">
				<tr target="id_bike" rel="${bike.bid}">
					<td><input type="checkbox" name="bikeVo"
						value="{id:'${bike.bid}', number:'${bike.number}'}" /></td>
					<td>${bike.number}</td>
					<td>${bike.imei}</td>
					<td>${bike.gTime}</td>
					<td>${bike.gLng}</td>
					<td>${bike.gLat}</td>
					<td>${bike.heartTime}</td>
					<td><a class="btnSelect"
						href="javascript:$.bringBack({id:'${bike.bid}', number:'${bike.number}'})"
						title="<%=rb.getString("common_select_title") %>"><%=rb.getString("common_select_title")%></a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num_dialog.jspf"%>
</div>