<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
String actionUrl = "redpackManage?requestType=70001";
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form onsubmit="return dwzSearch(this, 'dialog');" action="<%=actionUrl %>" method="post">
	<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td>
					<%=rb.getString("common_name_title") %>：<input type="text"    name="keyWords" value="${keyWords}"   />
				</td>
			</tr>
		</table>
		<div class="subBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit"><%=rb.getString("common_search_title") %></button></div></div></li>
				
			</ul>
		</div>
	</div>
	</form>
</div>
<div class="pageContent">
	<table class="table" width="100%" layoutH="138">
	    <thead>
			<tr>
				<th width="80"><%=rb.getString("common_id_title") %></th>
				<th width="80"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("redpack_rule_free_use_time") %></th>
				<th width="120"><%=rb.getString("redpack_rule_least_use_time") %></th>
				<th width="120"><%=rb.getString("redpack_rule_max_amount") %></th>
				<th width="120"><%=rb.getString("common_date_start_title") %></th>
				<th width="120"><%=rb.getString("common_date_end_title") %></th>
				<th width="120"><%=rb.getString("common_date_title") %></th>
				<th width="120"><%=rb.getString("common_select_title") %></th>
			</tr>
		</thead>
		<tbody>
		    <c:forEach items="${ruleList}" varStatus="status" var="rule">
		        <tr target="id_rule" rel="${rule.id}">
		            <td>${rule.id}</td>
		            <td>${rule.name}</td>
		            <td>${rule.free_use_time}</td>    
		            <td>${rule.least_use_time}</td>
		            <td>${rule.max_amount}</td>
		            <td>${rule.start_time}</td>
		            <td>${rule.end_time}</td>
		            <td>${rule.date}</td>
		            <td>
					<a class="btnSelect" href="javascript:$.bringBack({id:'${rule.id}',start:'${rule.start_time}',end:'${rule.end_time}'})" title="<%=rb.getString("common_select_title") %>"><%=rb.getString("common_select_title") %></a>
					</td>
		        </tr>
		    </c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num_dialog.jspf"%>
</div>