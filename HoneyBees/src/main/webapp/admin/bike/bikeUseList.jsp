<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%
	ResourceBundle rb = ((ResourceBundle) session
			.getAttribute("languageRD"));
	String[] violationType = rb.getString("credit_dec_score_type")
			.split(",");//get dec score type
	String[] statusType = rb.getString("bike_use_status_option").split(
			",");//get status type
	int type = Integer
			.parseInt(request.getAttribute("type").toString());

	int way = Integer.parseInt(request.getAttribute("way").toString());
	int extendType1 = Integer.parseInt(request.getAttribute("extendType1")
			.toString());
	String actionUrl = "bikeManage?requestType=20022";
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
								for (int i = 0; i < statusType.length; i++) {
							%>
							<option value=<%=i%> <%if (type == i) {%> selected="selected"
								<%}%>><%=statusType[i]%></option>
							<%
								}
							%>
					</select></td>

					<td>Unlock Way:<select name="way">
							<option value="0" <%if (way == 0) {%> selected="selected" <%}%>>All</option>
							<option value="1" <%if (way == 1) {%> selected="selected" <%}%>>GPRS</option>
							<option value="2" <%if (way == 2) {%> selected="selected" <%}%>>BLE</option>
					</select>
					</td>
					
						<td>Lock Way:<select name="extendType1">
							<option value="0" <%if (extendType1 == 0) {%> selected="selected" <%}%>>All</option>
							<option value="1" <%if (extendType1 == 1) {%> selected="selected" <%}%>>GPRS</option>
							<option value="2" <%if (extendType1 == 2) {%> selected="selected" <%}%>>BLE</option>
							<option value="3" <%if (extendType1 == 3) {%> selected="selected" <%}%>>Admin</option>
					</select>
					</td>

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
	<table class="table" width="100%" layoutH="138" asc="asc" desc="desc">
		<thead>
			<tr>
				<th width="120" orderField="id" class="${orderDirection}"><%=rb.getString("common_id_title")%></th>
				<th width="120"><%=rb.getString("bike_number")%></th>
				<th width="120">Lock Status</th>
				<th width="120"><%=rb.getString("user_title")%></th>
				<th width="120" orderField="start_time" class="${orderDirection}"><%=rb.getString("common_date_start_title")%></th>
				<th width="120" orderField="end_time" class="${orderDirection}"><%=rb.getString("common_date_end_title")%></th>
				<th width="120" orderField="duration" class="${orderDirection}"><%=rb.getString("common_time_title")%></th>
				<th width="120" orderField="amount" class="${orderDirection}"><%=rb.getString("consume_order_amount_title")%></th>
				<th width="120"><%=rb.getString("common_status_title")%></th>
				<th width="80">Unlock Way</th>
				<th width="80">Lock Way</th>
				<th width="120"><%=rb.getString("violation_title")%></th>
				<th width="120"><%=rb.getString("common_operating_title")%></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${bikeUseList}" varStatus="status" var="bikeUse">
				<tr target="id_item" rel="${bikeUse.id}">
					<td>${bikeUse.id}</td>
					<td><a href="bikeManage?requestType=20043&id=${bikeUse.bid}"
						target="navTab">${bikeUse.bikeVo.number}</a></td>
					<td>${bikeUse.bikeVo.statusStr }</td>
					<td><a href="userManage?requestType=30006&id=${bikeUse.uid}"
						target="navTab">${bikeUse.userVo.phone == null?bikeUse.userVo.detailVo.email:bikeUse.userVo.phone}</a></td>
					<td>${bikeUse.startTime}</td>
					<td>${bikeUse.endTime}</td>
					<td>${bikeUse.duration}<c:if test="${bikeUse.oldDuration != 0}">[${bikeUse.oldDuration}]</c:if></td>
					<td>${(bikeUse.rideAmount == '0.00' && bikeUse.amount != '0.00') ? bikeUse.amount:bikeUse.rideAmount}</td>
					<td><c:if test="${bikeUse.status == 1}"><%=statusType[1]%></c:if>
						<c:if test="${bikeUse.status == 2}"><%=statusType[2]%></c:if> <c:if
							test="${bikeUse.status == 3}"><%=statusType[3]%></c:if>
							
						<c:if test="${bikeUse.out_area == 1}"><label style="color:red;">[Out area]</label></c:if>		
						<c:if test="${bikeUse.out_area == 2}"><label style="color:red;">[Prohibited parking]</label></c:if>		
					</td>

					<td><c:if test="${bikeUse.openWay == 0}">GPRS</c:if> <c:if
							test="${bikeUse.openWay == 1}">BLE</c:if></td>
					<td>
					<c:if test="${bikeUse.status == 3}">
					<c:if test="${bikeUse.closeWay == 0}">GPRS</c:if> <c:if
							test="${bikeUse.closeWay == 1}">BLE</c:if> <c:if
							test="${bikeUse.closeWay == 2}">Admin
							<c:if test="${bikeUse.adminName != null && bikeUse.adminName != ''}">[${bikeUse.adminName}]</c:if>
							</c:if>
					</c:if>
					
					</td>
					<td><c:if test="${bikeUse.violationType == 1}"><%=violationType[0]%></c:if>
						<c:if test="${bikeUse.violationType == 2}"><%=violationType[1]%></c:if>
						<c:if test="${bikeUse.violationType == 3}"><%=violationType[2]%></c:if>
						<c:if test="${bikeUse.violationType == 4}"><%=violationType[3]%></c:if>
					</td>
					<td><c:if test="${bikeUse.status == 1}">
							<a href="bikeManage?requestType=20027&id=${bikeUse.id}"
								target="ajaxTodo"
								title="<%=rb.getString("common_delete_confirm_title") %>"><span><%=rb.getString("common_delete_title")%></span></a>
						</c:if> <c:if test="${bikeUse.status == 2}">
							<a href="bikeManage?requestType=20028&id=${bikeUse.id}"
								target="dialog"
								title="<%=rb.getString("bike_use_finish_confirm") %>"><span><%=rb.getString("bike_use_finish_title")%></span></a>
						</c:if> <a href="bikeManage?requestType=20024&id=${bikeUse.id}"
						target="_blank"><%=rb.getString("bike_use_rule")%></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>
