<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] typeValue = rb.getString("notification_management_template_type_values")
			.split(",");
	String[] statusValue = rb.getString("common_yes_no_value")
			.split(",");
	int type = Integer
			.parseInt(request.getAttribute("type").toString());
	String actionUrl = "notifyManage?requestType=11002";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>
<%--< div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("common_keywords_title") %>" /></td>
					<td><%=rb.getString("common_type_title")%>:
						<select name="status">
							<%
								for (int i = 0; i < typeValue.length; i++) {
							%>
							<option value=<%=i%> <%if (type == i) {%> selected="selected"
								<%}%>><%=typeValue[i]%></option>
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
</div> --%>
<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl"></th>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("notification_management_config_sms_notification")%></th>
				<th width="120"><%=rb.getString("notification_management_config_email_notification")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				<c:if test="${lookup == 1}"><th width="80"><%=rb.getString("common_select_title")%></th></c:if>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${configList}" varStatus="status" var="configVo">
				<tr target="id_item" rel="${configVo.id}">
					<td><input name="ids" value="${configVo.id}" type="checkbox"></td>
					<td>${configVo.id}</td>
					<td>${configVo.typeStr}</td>
					<td>${configVo.template}</td>
					<td>
					<c:if test="${configVo.sms == 0}"><%=rb.getString("common_no_title")%></c:if>
					<c:if test="${configVo.sms == 1}"><%=rb.getString("common_yes_title")%></c:if>
					 </td>
					<td>
					<c:if test="${configVo.email == 0}"><%=rb.getString("common_no_title")%></c:if>
					<c:if test="${configVo.email == 1}"><%=rb.getString("common_yes_title")%></c:if>
					</td>
					<td>${configVo.date}</td>
					<c:if test="${lookup == 1}">
					<td><a class="btnSelect"
						href="javascript:$.bringBack({template:'${templateVo.template}'})"
						title="Select">Select</a></td>
					</c:if>
					
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%-- <%@ include file="../common/page_num.jspf"%> --%>
</div>
