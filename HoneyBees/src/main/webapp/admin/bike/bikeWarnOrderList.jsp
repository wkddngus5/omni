<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String actionUrl = "bikeManage?requestType=20038";
	boolean export = true;
	String bikeTips = (String) session
			.getAttribute("bike_order_warn_status_tips");
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form id="searchForm" onsubmit="return navTabSearch(this);"
		  action="<%=actionUrl%>&type=${errorType}&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
							type="text" name="keyWords" value="${keyWords}"
							alt="<%=rb.getString("bike_keywords_tips") %>" /></td>

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
							<button type="submit" id="searchSubmit"><%=rb.getString("common_search_title")%></button>
						</div>
					</div></li>
				</ul>
			</div>
		</div>
		<input type="hidden" name="tagIds" id="tagIds" />
	</form>
</div>
<div class="pageContent">
	<%@ include file="../common/page_func.jspf"%>
	<div class="panel" defH="400">
		<table class="table" width="100%" height="80%" layoutH="138"
			   layoutH="138">
			<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
									  class="checkboxCtrl"></th>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("bike_number")%></th>
				<th width="120">IMEI</th>
				<th width="120"><%=rb.getString("common_content_title")%></th>
				<th width="120"><%=rb.getString("common_date_title")%></th>
				<th width="120"><%=rb.getString("common_operating_title")%></th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${orderList}" varStatus="status" var="bikeOrder">
				<tr target="id_item" rel="${bikeOrder.id}">
					<td><input name="ids" value="${bikeOrder.id}" type="checkbox"></td>
					<td>${bikeOrder.id}</td>
					<td><a
							href="bikeManage?requestType=20043&id=${bikeOrder.bid}"
							target="navTab">${bikeOrder.number}</a></td>
					<td>${bikeOrder.imei}</td>
					<td>${bikeOrder.content}</td>
					<td>${bikeOrder.date}</td>
					<td><a
							href="bikeManage?requestType=20040&id=${bikeOrder.id}&imei=${bikeOrder.imei}"
							target="_blank"><%=rb.getString("bike_show_in_map_title")%></a></td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	<%@ include file="../common/page_num.jspf"%>
	<div class="panel collapse" minH="30" defH="50">
		<h1><%=rb.getString("bike_status_tips_title")%></h1>
		<div>
			<h1><%=bikeTips%></h1>
		</div>
	</div>
</div>


<script type="text/javascript">
    function showTagImeisOrder(ids) {
        this.searchForm.tagIds.value = ids;
        this.searchSubmit.click();
    }
</script>
