<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] versionType = rb.getString("setting_version_type").split(
			",");
	int type = ValueUtil.getInt(request.getAttribute("type"));

	String actionUrl = "configManage?requestType=60011";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>"
		method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_name_title")%>：<input type="text"
						name="keyWords" value="${keyWords}" /></td>
					<td><%=rb.getString("common_type_title")%>: <select
						name="type">
							<%
								for (int i = 0; i < versionType.length; i++) {
							%>
							<option value=<%=i%> <%if (type == i) {%> selected="selected"
								<%}%>><%=versionType[i]%></option>
							<%
								}
							%>
					</select></td>
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
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("setting_version_name")%></th>
				<th width="120"><%=rb.getString("setting_version_code")%></th>
				<th width="120"><%=rb.getString("setting_version_file")%></th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${versionList}" varStatus="status" var="version">
				<tr target="id_item" rel="${version.id}">
					<td>${version.id}</td>
					<td>${version.typeStr}</td>
					<td>${version.version_name}</td>
					<td>${version.version_code}</td>
					<td>${version.url}</td>
					<td>${version.content}</td>
					<td>${version.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>

</div>