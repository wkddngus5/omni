<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
String actionUrl = "redpackManage?requestType=70006";
boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_name_title") %>：<input type="text"
						name="keyWords" value="${keyWords}" /></td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit"><%=rb.getString("common_search_title") %></button>
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
				<th width="80"  orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title") %></th>
				<th width="80"><%=rb.getString("redpack_rule_title") %></th>
				<th width="120"><%=rb.getString("bike_number") %></th>
				<th width="120"><%=rb.getString("common_date_start_title") %></th>
				<th width="120"><%=rb.getString("common_date_end_title") %></th>
				<th width="120"><%=rb.getString("common_date_title") %></th>
				<th width="120"><%=rb.getString("consume_order_amount_title") %></th>
				<th width="120"><%=rb.getString("user_phone_title") %></th>
				<th width="120"><%=rb.getString("redpack_bike_user_date") %></th>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${bikeList}" varStatus="status" var="bike">
				<tr target="id_item" rel="${bike.id}">
					<td>${bike.id}</td>
					<td>${bike.rule_id}</td>
					<td><a href="bikeManage?requestType=20043&id=${bike.bid}"
						target="navTab">${bike.number}</a></td>
					<td>${bike.start_time}</td>
					<td>${bike.end_time}</td>
					<td>${bike.date}</td>
					<td>${bike.amount}</td>
					<td><a href="userManage?requestType=30006&id=${bike.uid}" target="navTab">${bike.phone}</a></td>
					<td>${bike.user_date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>