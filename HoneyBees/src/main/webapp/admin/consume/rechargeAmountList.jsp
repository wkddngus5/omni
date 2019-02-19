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
				<th width="120"><%=rb.getString("consume_recharge_amount_title")%></th>
				<th width="120"><%=rb.getString("consume_gift_amount_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${amountList}" varStatus="status" var="amount">
				<tr target="id_item" rel="${amount.id}">
					<td>${amount.id}</td>
					<td>${amount.amount}</td>
					<td>${amount.gift}</td>
					<td>${amount.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>