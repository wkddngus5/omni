<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>


<%
	ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));

	String[] unitType = rb.getString("membership_plan_unit_value").split(",");
	pageContext.setAttribute("unitType", unitType);

	String[] rideUnitType = rb.getString("membership_plan_free_unit_value").split(",");
	pageContext.setAttribute("rideUnitType", rideUnitType);

	String actionUrl = "bikeManage?requestType=20055";
	boolean export = false;
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
					<td>
					Is Renewable:
					<select name="way">
					<option value="0"  <c:if test="${way == 0}">selected="selected"</c:if>>All</option>
					<option value="1" <c:if test="${way == 1}">selected="selected"</c:if>>Not Renewable</option>
					<option value="2" <c:if test="${way == 2}">selected="selected"</c:if>>Renewable</option>
					</select>
					</td>
					<td>
					Is Education:
					<select name="type">
					<option value="0" <c:if test="${type == 0}">selected="selected"</c:if>>All</option>
					<option value="1" <c:if test="${type == 1}">selected="selected"</c:if>>Not Education</option>
					<option value="2" <c:if test="${type == 2}">selected="selected"</c:if>>Education</option>
					</select>
					</td>
					<td>
					Status:
					<select name="status">
					<option value="0" <c:if test="${status == 0}">selected="selected"</c:if>>All</option>
					<option value="1" <c:if test="${status == 1}">selected="selected"</c:if>>Canceled</option>
					<option value="2" <c:if test="${status == 2}">selected="selected"</c:if>>Active</option>
					</select>
					</td>
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

				<th width="60"><%=rb.getString("common_id_title") %></th>
				<th width="60"><%=rb.getString("common_title")%></th>
				<th width="80"><%=rb.getString("membership_plan_unit")%></th>
				<th width="80"><%=rb.getString("membership_plan_unit_count")%></th>
				<th width="60"><%=rb.getString("membership_plan_unit_price")%></th>
				<th width="80"><%=rb.getString("membership_plan_free_unit")%></th>
				<th width="80"><%=rb.getString("membership_plan_free_unit_count")%></th>
				<th width="240"><%=rb.getString("membership_plan_description")%></th>
				<th width="100">Is Renewable</th>
				<th width="100">Is Education</th>
				<th width="60"><%=rb.getString("common_status_title")%></th>



			</tr>
		</thead>
		<tbody>
			<c:forEach items="${membershipPlanList}" varStatus="status"
				var="plan">
				<tr target="id_item" rel="${plan.id}">

					<td>${plan.id}</td>
					<td>${plan.title}</td>
					<td>${unitType[plan.interval]}</td>
					<td>${plan.intervalCount}</td>
					<td>${plan.planPrice}</td>
					<td>${rideUnitType[plan.rideUnit]}</td>
					<td>${plan.rideFreeUnitCount}</td>
					<td>${plan.description}</td>



					<td><c:if test="${plan.isRenewable == true}"> Renewable </c:if>
						<c:if test="${plan.isRenewable == false}"> Not Renewable </c:if></td>

					<td><c:if test="${plan.education == true}"> Education </c:if>
						<c:if test="${plan.education == false}"> Not  Education  </c:if></td>

					<td><c:if test="${plan.active == true}"> Active </c:if> <c:if
							test="${plan.active == false}"> Canceled </c:if></td>



				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>