<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" import="java.text.MessageFormat"%>


<%
	ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
	String actionUrl = "bikeManage?requestType=20056";
	boolean export = false;
%>
<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader" >
	<form onsubmit="return navTabSearch(this);"
		action="<%=actionUrl%>&funcId=${funcId}" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("common_keywords_title")%>：<input
						type="text" name="keyWords" value="${keyWords}"
						alt="<%=rb.getString("common_keywords_title") %>" /></td>
					<td>
					<td>
					Status:
					<select name="status">
					<option value="0" <c:if test="${status == 0}">selected="selected"</c:if>>All</option>
					<option value="1" <c:if test="${status == 1}">selected="selected"</c:if>>Active</option>
					<option value="2" <c:if test="${status == 2}">selected="selected"</c:if>>Canceled</option>
					</select>
					</td>
					<td>
					Available
					<select name="type">
					<option value="0" <c:if test="${type == 0}">selected="selected"</c:if>>All</option>
					<option value="1" <c:if test="${type == 1}">selected="selected"</c:if>>Available</option>
					<option value="2" <c:if test="${type == 2}">selected="selected"</c:if>>Expire</option>
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

				<th width="80"><%=rb.getString("common_id_title") %></th>
				<th width="80"><%=rb.getString("bike_manage_membership_plans_id") %></th>
				<th width="80">Plan title</th>
				<th width="80">Plan description</th>
				<th width="80"><%=rb.getString("user_title")%></th>

				<th width="80" orderField="start_time" class="${orderDirection}">
					<%=rb.getString("common_date_start_title")%>	
				</th>

				<th width="80" orderField="through_time" class="${orderDirection}">
					<%=rb.getString("common_date_end_title")%>	
				</th>
				<th>Available</th>
				<th>Stripe Id</th>

				<th width="80"><%=rb.getString("common_status_title")%></th>

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userMembershipList}" varStatus="status" var="userMembership">
				<tr target="id_item" rel="${userMembership.id}">

					<td>${userMembership.id}</td>
					<td>${userMembership.plan.id}</td>
					<td>${userMembership.plan.title}</td>
					<td>${userMembership.plan.description}</td>
					<td>${userMembership.user.phone==null?userMembership.userId:userMembership.user.phone}</td>
					<td><fmt:formatDate value="${userMembership.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
					<td><fmt:formatDate value="${userMembership.throughTime}" pattern="yyyy-MM-dd HH:mm"/></td>
					<td>
					<c:if test="${userMembership.available == 1}"> Available </c:if>
						<c:if test="${userMembership.available == 0}"> Expire </c:if>
					</td>
					<td>${userMembership.stripeId}</td>

					<td>
						<c:if test="${userMembership.canceled == true}"> Canceled </c:if>
						<c:if test="${userMembership.canceled == false}"> Active </c:if>
					</td>
				   
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num.jspf"%>
</div>