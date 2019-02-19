<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
String actionUrl = "purviewManage?requestType=101";
boolean export = true;
%>

<%@ include file="../common/page_from.jspf"%>

<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="80"><%= rb.getString("common_id_title") %></th>
				<th width="120"><%= rb.getString("common_name_title") %></th>
				<th width="120"><%= rb.getString("common_note_title") %></th>
				<th width="120"><%= rb.getString("common_date_title") %></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${groupList}" varStatus="status" var="group">
				<!-- target 在删除和修改的时候用到，选择项的ID -->
				<tr target="id_item" rel="${group.id}">
					<td>${group.id}</td>
					<td>${group.name}</td>
					<td>${group.note}</td>
					<td>${group.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>