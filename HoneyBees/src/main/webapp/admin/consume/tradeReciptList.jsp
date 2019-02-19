<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
String[] statusType = rb.getString("common_status_value").split(",");//get status type
String actionUrl = "consumeManage?requestType=40003";
boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("user_phone_title") %>：<input type="text"
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
				<th width="80"><%=rb.getString("common_id_title") %></th>
				<th width="120"><%=rb.getString("user_id_title") %></th>
				<th width="120"><%=rb.getString("consume_id_title") %></th>
				<th width="120"><%=rb.getString("user_firstname_title") %></th>
				<th width="120"><%=rb.getString("user_lastname_title") %></th>
				<th width="120"><%=rb.getString("user_phone_title") %></th>
				<th width="120"><%=rb.getString("user_address_title") %></th>
				<th width="120"><%=rb.getString("user_zipcode_title") %></th>
				<th width="120"><%=rb.getString("user_country_title") %></th>
				<th width="120"><%=rb.getString("common_status_title") %></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${reciptList}" varStatus="status" var="recipt">
				<tr target="id_item" rel="${recipt.id}">
					<td>${recipt.id}</td>
					<td>${recipt.uid}</td>
					<td>${recipt.trade_ids}</td>
					<td>${recipt.firstname}</td>
					<td>${recipt.lastname}</td>
					<td>${recipt.phone}</td>
					<td>${recipt.address}</td>
					<td>${recipt.zip_code}</td>
					<td>${recipt.country}</td>
					<td>
					<c:if test="${recipt.status==0}">
							<%= statusType[0] %>
						</c:if> <c:if test="${recipt.status==1}">
							<%= statusType[1] %>
						</c:if>
				</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
<%@ include file="../common/page_num.jspf"%>
</div>