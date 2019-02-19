<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="com.pgt.bikelock.vo.WebContentVo"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] webType = rb.getString("setting_webcontent_type_value")
			.split(",");//get web type
	String actionUrl = "configManage?requestType=60003";
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
				<th width="80"  orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_name_title")%></th>
				<th width="120"><%=rb.getString("common_image_title")%></th>
				<th width="120"><%=rb.getString("common_date_start_title")%></th>
				<th width="120"><%=rb.getString("common_date_end_title")%></th>
				<th width="120"><%=rb.getString("common_add_date_title")%></th>
				<th width="120"><%=rb.getString("common_preview_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${newsList}" varStatus="status" var="news">
				<tr target="id_item" rel="${news.id}">
					<td>${news.id}</td>
					<td>${news.title}</td>
					<td><c:if test="${news.imageVo.path != null}">
							<a href="${news.imageVo.path}" target="view_window"><%=rb.getString("common_image_title")%></a>
						</c:if></td>
					<td>${news.start_time}</td>
					<td>${news.end_time}</td>
					<td>${news.date}</td>
					<td><a
						href="<%=basePath%>other?requestType=50004&newsId=${news.id}"
						target="dialog" mask="true"><%=rb.getString("common_preview_title")%></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>

</div>