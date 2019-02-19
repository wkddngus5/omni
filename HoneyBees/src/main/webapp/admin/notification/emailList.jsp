<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] statusValue = rb.getString("common_yes_no_value")
			.split(",");
	int status = Integer
			.parseInt(request.getAttribute("status").toString());
	String actionUrl = "notifyManage?requestType=11010";
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
						alt="<%=rb.getString("common_keywords_title") %>" /></td>
					<td><%=rb.getString("common_status_title")%>:
						<select name="status">
							<%
								for (int i = 0; i < statusValue.length; i++) {
							%>
							<option value=<%=i%> <%if (status == i) {%> selected="selected"
								<%}%>><%=statusValue[i]%></option>
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
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl"></th>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("notification_management_email_sender")%></th>
				<th width="120"><%=rb.getString("notification_management_email_receiver")%></th>
				<th width="120"><%=rb.getString("notification_management_email_subject")%></th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				<th width="120"><%=rb.getString("common_success_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${emailList}" varStatus="status" var="emailVo">
				<tr target="id_item" rel="${emailVo.id}">
					<td><input name="ids" value="${emailVo.id}" type="checkbox"></td>
					<td>${emailVo.id}</td>
					<td>${emailVo.sender}</td>
					<td>${emailVo.receiver}</td>
					<td>${emailVo.subject}</td>
					<td>${emailVo.content}</td>
					<td>${emailVo.date}</td>
					<td>${emailVo.statusStr}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>
