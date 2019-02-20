<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "areaManage?requestType=80001";
	boolean export = false;
	String[] areaType = rb.getString("bike_area_type").split(",");//get area type
	int type = ValueUtil.getInt(request.getAttribute("type"));
%>
<%@ include file="../common/page_from.jspf"%>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_id_title")%>：<input type="text"
						name="keyWords" value="${keyWords}" /></td>
					<td><%=rb.getString("common_type_title")%>: <select name="type">
							<%
								for (int i = 0; i < areaType.length; i++) {
							%>
							<option value=<%=i%> <%if (type == i) {%> selected="selected"
								<%}%>><%=areaType[i]%></option>
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
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("common_note_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("common_operating_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${areaList}" varStatus="status" var="area">
				<tr target="id_item" rel="${area.id}">
					<td>${area.id}</td>
					<td>${area.name}</td>
					<td>${area.note}</td>
					<td>${area.typeStr}</td>
					<td><a
						href="areaManage?requestType=80004&type=1&id=${area.id}"
						target="_blank"><%=rb.getString("bike_show_in_map_title")%></a> /
						<a href="areaManage?requestType=80002&id=${area.id}"
						target="_blank"><%=rb.getString("common_update_title")%></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>