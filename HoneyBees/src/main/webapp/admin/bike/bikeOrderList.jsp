<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] recordype = rb.getString("bike_order_record_type_value")
			.split(",");//get dec score type
	int type = Integer
			.parseInt(request.getAttribute("type").toString());
	String actionUrl = "bikeManage?requestType=20037";
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
						alt="<%=rb.getString("bike_keywords_tips") %>" /></td>
					<td><%=rb.getString("common_type_title")%>: <select
						name="type">
							<%
								for (int i = 0; i < recordype.length; i++) {
							%>
							<option value=<%=i%> <%if (type == i) {%> selected="selected"
								<%}%>><%=recordype[i]%></option>
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
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl"></th>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("bike_number")%></th>
				<th width="120">IMEI</th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orderList}" varStatus="status" var="bikeOrder">
				<tr target="id_item" rel="${bikeOrder.id}">
					<td><input name="ids" value="${bikeOrder.id}" type="checkbox"></td>
					<td>${bikeOrder.id}</td>
					<td><a href="bikeManage?requestType=20043&id=${bikeOrder.bid}"
						target="navTab">${bikeOrder.number}</a></td>
					<td>${bikeOrder.imei}</td>
					<td>${bikeOrder.typeStr}</td>
					<td>${bikeOrder.content}</td>
					<td>${bikeOrder.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>
