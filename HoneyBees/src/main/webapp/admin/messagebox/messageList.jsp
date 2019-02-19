<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "messageManage?requestType=90001";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>&funcId=${funcId}"
		method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input type="text"
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
				<th width="120"><%=rb.getString("user_phone_title")%></th>
				<th width="120"><%=rb.getString("message_admin_title")%></th>
				<th width="120"><%=rb.getString("common_title")%></th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				<th width="120"><%=rb.getString("common_update_data_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${messageList}" varStatus="status" var="message">
				<tr target="id_item" rel="${message.id}">
					<td>${message.id}</td>
					<td><a href="userManage?requestType=30006&id=${message.user_id}" target="navTab">
					${message.userPhone==null?message.user_id:message.userPhone}</a></td>
					<td>${message.adminName}</td>
					<td>${message.title}</td>
					<td>${message.content}</td>
					<td>${message.date}</td>
					<td>${message.update_date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>