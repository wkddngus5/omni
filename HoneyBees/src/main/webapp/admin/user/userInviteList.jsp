<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "userManage?requestType=30008";
	boolean export = true;
%>

<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">

	<form onsubmit="return navTabSearch(this);" action="<%=actionUrl%>&funcId=${funcId}"
		method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("user_keywords_tips") %>" /></td>


					<td><%=rb.getString("common_date_title")%>： <input
						type="text" name="startTime" class="date" value="${startTime}"
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
				
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("user_phone_title")%></th>
				<th width="120"><%=rb.getString("invite_user_phone")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${inviteList}" varStatus="status" var="invite">
				<tr target="id_item" rel="${invite.id}">
					<td>${invite.id}</td>
					<td><a href="userManage?requestType=30006&id=${invite.uid}" target="navTab">${invite.phone==null?invite.uid:invite.phone}</a></td>
					<td><a href="userManage?requestType=30006&id=${invite.i_uid}" target="navTab">${invite.iphone==null?invite.i_uid:invite.iphone}</a></td>
					<td>${invite.date}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>