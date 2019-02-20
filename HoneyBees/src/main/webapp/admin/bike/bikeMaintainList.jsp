<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] maintainType = rb.getString("bike_maintain_type").split(
			",");//get maintain type
	int type = Integer
			.parseInt(request.getAttribute("type").toString());
	String[] statusType = rb.getString("common_status_value")
			.split(",");//get status type

	int status = Integer.parseInt(request.getAttribute("status")
			.toString());
	String actionUrl = "bikeManage?requestType=20032";
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

					<td><%=rb.getString("common_type_title")%>: <select
						name="type">
							<%
								for (int i = 0; i < maintainType.length; i++) {
							%>
							<option value=<%=i%> <%if (type == i) {%> selected="selected"
								<%}%>><%=maintainType[i]%></option>
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
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("bike_number")%></th>
				<th width="120"><%=rb.getString("nickname_title")%></th>
				<th width="120"><%=rb.getString("common_status_title")%></th>
				<th width="120"><%=rb.getString("common_type_title")%></th>
				<th width="120"><%=rb.getString("common_note_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				<th width="120"><%=rb.getString("common_deal_data_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${maintainList}" varStatus="status"
				var="maintainVo">
				<tr target="id_item" rel="${maintainVo.id}">
					<td>${maintainVo.id}</td>
					<td><a href="bikeManage?requestType=20043&id=${maintainVo.bid}"
						target="navTab">${maintainVo.number}</a></td>
					<td>${maintainVo.nickname}</td>
					<td>${maintainVo.statusStr}</td>
					<td>${maintainVo.typeStr}</td>
					<td>${maintainVo.note}</td>
					<td>${maintainVo.date}</td>
					<td>${maintainVo.deal_date}</td>

				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>
