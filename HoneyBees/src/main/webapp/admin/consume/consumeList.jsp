<%@page import="com.pgt.bikelock.servlet.PayServlet"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%@ page language="java" import="com.pgt.bikelock.resource.OthersSource"%>

<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] payStatusTitle = rb.getString(
			"consume_order_pay_status_value").split(",");//get pay status type
	int payStatus = ValueUtil.getInt(request.getAttribute("status"));
	String[] payTypeTitle = rb
			.getString("consume_order_pay_type_value").split(",");
	int payType = ValueUtil.getInt(request.getAttribute("type"));
	String[] payWayTitle;
	if (OthersSource.getSourceString("consume_order_pay_way_value") != null) {
		payWayTitle = OthersSource.getSourceString(
				"consume_order_pay_way_value").split(",");
	} else {
		payWayTitle = rb.getString("consume_order_pay_way_value")
				.split(",");
	}
	int payWay = ValueUtil.getInt(request.getAttribute("way"));

	String actionUrl = "consumeManage?requestType=40001";
	boolean export = true;
%>

<%@ include file="../common/page_from.jspf"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("consume_keywords_tips") %>" /></td>
					<td><%=rb.getString("common_type_title")%>: <select
						name="type">
							<%
								for (int i = 0; i < payTypeTitle.length; i++) {
							%>
							<option value=<%=i%> <%if (payType == i) {%> selected="selected"
								<%}%>><%=payTypeTitle[i]%></option>
							<%
								}
							%>
					</select></td>

					<td><%=rb.getString("consume_order_pay_way_title")%>: <select
						name="way">
							<%
								for (int i = 0; i < payWayTitle.length; i++) {
							%>
							<option value=<%=i%> <%if (payWay == i) {%> selected="selected"
								<%}%>><%=payWayTitle[i]%></option>
							<%
								}
							%>
					</select></td>

					<td><%=rb.getString("common_status_title")%>: <select
						name="status">
							<%
								for (int i = 0; i < payStatusTitle.length; i++) {
							%>
							<option value=<%=i%> <%if (payStatus == i) {%>
								selected="selected" <%}%>><%=payStatusTitle[i]%></option>
							<%
								}
							%>
					</select></td>


					<td><%=rb.getString("common_date_title")%>： <input type="text"
						name="startTime" class="date" value="${startTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /> -
						<input type="text" name="endTime" class="date" value="${endTime}"
						dateFmt="yyyy-MM-dd HH:mm" readonly="true" class="required" /></td>

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
				<th width="80" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("user_title")%></th>
				<th width="120"><%=rb.getString("consume_order_amount_title")%></th>
				<th width="120"><%=rb.getString("consume_order_account_pay_amount")%></th>
				<th width="120"><%=rb.getString("consume_order_gift_pay_amount")%></th>
				<th width="120"><%=rb.getString("user_money_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("consume_order_pay_way_title")%></th>
				<th width="120"><%=rb.getString("common_status_title")%></th>
				<th width="120"><%=rb.getString("consume_order_pay_out_id")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${tradeList}" varStatus="status" var="consume">
				<tr target="id_item" rel="${consume.id}">
					<td>${consume.id}</td>
					<td><a href="userManage?requestType=30006&id=${consume.uid}"
						target="navTab">${consume.userVo.phone==null?consume.uid:consume.userVo.phone}</a></td>
					<td>${consume.amount}</td>
					<td>${consume.accountPayAmount}</td>
					<td>${consume.giftPayAmount}</td>
					<td>${consume.balance}</td>
					<td>${consume.typeStr}</td>
					<td>${consume.wayStr}</td>
					<td>${consume.statusStr}</td>
					<td>${consume.out_trade_no}</td>
					<td>${consume.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>