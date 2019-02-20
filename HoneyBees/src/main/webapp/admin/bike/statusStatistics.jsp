<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "bikeManage?requestType=20041";
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form id="searchForm" onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("bike_statics_idle_day")%>：<input
						type="number" name="idleDay" value="${idleDay}"
						alt="<%=rb.getString("bike_statics_idle_day") %>" /></td>

				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button id="searchSubmit" type="submit"><%=rb.getString("common_search_title")%></button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
		<input type="hidden" name="tagIds" id="tagIds" />
	</form>
</div>
<div class="pageContent" layoutH="300">
	<div class="panel collapse" minH="60" defH="200">
		<h1><%=rb.getString("bike_status_tips_title")%></h1>
		<div style="font-size: 20px;">
			${bike_status_tips}
		</div>
	</div>
</div>



