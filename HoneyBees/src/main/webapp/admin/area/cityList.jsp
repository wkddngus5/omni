<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "areaManage?requestType=80006";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>"
		method="post">
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
				<th width="120"  orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("common_note_title")%></th>
				<th width="120"><%=rb.getString("common_operating_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${cityList}" varStatus="status" var="city">
				<tr target="id_item" rel="${city.id}">
					<td>${city.id}</td>
					<td>${city.name}</td>
					<td>${city.note}</td>
					<td>
					<a href="areaManage?requestType=80004&type=2&id=${city.id}"
							target="_blank"><%=rb.getString("bike_show_in_map_title")%></a> / 
					<a href="areaManage?requestType=80007&id=${city.id}"
							target="_blank"><%=rb.getString("common_update_title")%></a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>