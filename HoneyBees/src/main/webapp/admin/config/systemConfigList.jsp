<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "configManage?requestType=60013";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("system_config_key")%></th>
				<th width="120"><%=rb.getString("common_note_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${configList}" varStatus="status" var="configVo">
				<tr target="id_item" rel="${configVo.id}">
					<td>${configVo.id}</td>
					<td>${configVo.name}</td>
					<td>${configVo.config}</td>
					<td>${configVo.key}</td>
					<td>${configVo.note}</td>
					<td>${configVo.date}</td>					
					
				</tr>
			</c:forEach>
		</tbody>
	</table>

</div>
