<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.vo.WebContentVo"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "configManage?requestType=60003";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>
<%-- <div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>"
		method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_name_title")%>：<input type="text"
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
				</ul>
			</div>
		</div>
	</form>
</div> --%>
<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl"></th>
				<th width="80"><%=rb.getString("common_row_title")%></th>
				<th width="120">IP</th>
				<th width="120"><%=rb.getString("common_date_start_title")%></th>
				<th width="120"><%=rb.getString("common_date_end_title")%></th>
				<th width="120"><%=rb.getString("common_count_title")%></th>
				<th width="120"><%=rb.getString("common_status_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${clientList}" varStatus="status" var="client">
				<tr target="id_item" rel="${client.ip}">
					<td><input name="ids" value="${client.ip}" type="checkbox"></td>
					<td>${status.count}</td>
					<td>${client.ip}</td>
					<td>${client.firstDate}</td>
					<td>${client.lastDate}</td>
					<td>${client.count}</td>
					<td>${client.statusStr}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%-- <%@ include file="../common/page_num.jspf"%> --%>

</div>