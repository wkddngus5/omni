<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "consumeManage?requestType=40009";
	boolean export = false;
%>
<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="80"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("consume_deposit_title")%></th>
				<th width="200"><%=rb.getString("consume_deposit_return_deal_title")%></th>
				<th width="200"><%=rb.getString("consume_deposit_return_automatic_refund")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${confList}" varStatus="status" var="conf">
				<tr target="id_item" rel="${conf.id}">
					<td>${conf.id}</td>
					<td>${conf.amount}</td>
					<td>${conf.return_min_day}-${conf.return_max_day}</td>
					<td>${conf.automaticRefundStr}</td>
					<td>${conf.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>