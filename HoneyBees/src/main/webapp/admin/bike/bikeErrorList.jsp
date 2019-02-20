<%@page import="org.apache.struts2.jasper.tagplugins.jstl.core.ForEach"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] reportType = rb.getString("bike_report_type_value").split(
			",");//get report type
	String[] statusType = rb.getString(
			"bike_report_review_result_value").split(",");//get status type
	int status = ValueUtil.getInt(request.getAttribute("status"));
	int type = Integer
			.parseInt(request.getAttribute("type").toString());

	String actionUrl = "bikeManage?requestType=20017";
	boolean export = true;
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("bike_error_keywords_tips") %>" /></td>
					<td><%=rb.getString("common_type_title")%>: <select
						name="type">
							<%
								for (int i = 0; i < reportType.length; i++) {
							%>
							<option value=<%=i%> <%if (type == i) {%> selected="selected"
								<%}%>><%=reportType[i]%></option>
							<%
								}
							%>
					</select></td>
					<td><%=rb.getString("common_status_title")%>: <select
						name="status">
							<%
								for (int i = 0; i < statusType.length; i++) {
							%>
							<option value=<%=i%> <%if (status == i) {%> selected="selected"
								<%}%>><%=statusType[i]%></option>
							<%
								}
							%>
					</select></td>
					<td><%=rb.getString("common_date_title")%>： <input type="text"
						name="startTime" class="date" value="${startTime}"
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
	<table class="table" width="100%" layoutH="138"  asc="asc" desc="desc">
		<thead>
			<tr>
			<th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
				<th width="80" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("bike_number")%></th>
				<th width="120"><%=rb.getString("user_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("common_image_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				<th width="120"><%=rb.getString("common_status_title")%></th>
				<th width="120"><%=rb.getString("common_note_title")%></th>
				<th width="120"><%=rb.getString("common_operating_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${errorList}" varStatus="status" var="error">
				<tr target="id_item" rel="${error.id}">
				<td><input name="ids" value="${error.id}" type="checkbox"></td>
					<td>${error.id}</td>
					<td><a href="bikeManage?requestType=20043&id=${error.bid}"
						target="navTab">${error.bnumber}</a></td>
					<td><a href="userManage?requestType=30006&id=${error.uid}" target="navTab">
					${error.userVo.phone == null?error.uid:error.userVo.phone}</a></td>

					<td>${error.typeStr}<c:if test="${error.errorTypeStr != null}">(${error.errorTypeStr})</c:if></td>
					<td>${error.content}</td>

					<td><c:forEach items="${error.listImage}" varStatus="status"
							var="image">
							<a href="${image.path}" target="view_window"><%=rb.getString("common_image_title")%>[${status.count}]</a>&nbsp;&nbsp;
		             </c:forEach></td>


					<td>${error.date}</td>
					<td><c:if test="${error.status==0}">
							<%=statusType[1]%>
						</c:if> <c:if test="${error.status==1}">
							<%=statusType[2]%>
						</c:if> <c:if test="${error.status==2}">
							<%=statusType[3]%>
						</c:if> <c:if test="${error.status==3}">
							<%=statusType[4]%>
						</c:if> <c:if test="${error.status==4}">
							<%=statusType[5]%>
						</c:if></td>
					<td>${error.review_note}</td>
					<td><a href="bikeManage?requestType=20036&lat=${error.lat}&lng=${error.lng}&title=${error.bnumber}" target="_blank"><%=rb.getString("bike_show_in_map_title")%></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>