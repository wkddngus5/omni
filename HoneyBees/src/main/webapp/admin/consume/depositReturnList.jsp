<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] statusType = rb.getString("common_status_value")
			.split(",");//get status type
	int dealStatus = ValueUtil.getInt(request.getAttribute("status"));
	String actionUrl = "consumeManage?requestType=40006";
	boolean export = true;
%>

<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>&funcId=${funcId}"
		method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input type="text"
						name="keyWords" value="${keyWords}"  alt="<%=rb.getString("common_keywords_title")%>"/></td>
					<td><%=rb.getString("common_status_title")%>: <select
						name="status">
							<%
								for (int i = 0; i < statusType.length; i++) {
							%>
							<option value=<%=i%> <%if (dealStatus == i) {%>
								selected="selected" <%}%>><%=statusType[i]%></option>
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
			<th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
				<th width="80"  orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("user_phone_title")%></th>
				<th width="120"><%=rb.getString("common_status_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				<th width="120"><%=rb.getString("consume_order_pay_way_title")%></th>
				<th width="120"><%=rb.getString("consume_order_pay_out_id")%></th>
				<th width="120"><%=rb.getString("consume_order_pay_date")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${returnList}" varStatus="status" var="deposit">
				<tr target="id_item" rel="${deposit.id}">
				<td><input name="ids" value="${deposit.id}" type="checkbox"></td>
					<td>${deposit.id}</td>
					<td><a href="userManage?requestType=30006&id=${deposit.uid}" target="navTab">${deposit.userVo.phone}</a></td>
					<td>${deposit.statusStr}</td>
					<td>${deposit.date}</td>
					<td>${deposit.tradeVo.wayStr}</td>
					<td>${deposit.tradeVo.out_trade_no}</td>
					<td>${deposit.tradeVo.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>