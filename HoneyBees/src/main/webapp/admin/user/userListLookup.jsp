<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.text.MessageFormat"%>
<%@ page language="java" import="com.pgt.bikelock.utils.ValueUtil"%>
<%
ResourceBundle rb= ((ResourceBundle)session.getAttribute("languageRD"));
String[] statusType = rb.getString("user_auth_status_value").split(",");//get status type
int authStatus = ValueUtil.getInt(request.getAttribute("status"));
String actionUrl = "userManage?requestType=30002";
%>

<%@ include file="../common/page_from.jspf"%>

<div class="pageHeader">
	<form onsubmit="return dwzSearch(this, 'dialog');"
		action="<%=actionUrl%>" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td><%=rb.getString("user_search_title") %>：<input type="text"
						name="keyWords" value="${keyWords}" /></td>
					<td><%=rb.getString("common_status_title")%>: <select
						name="status">
							<%
								for (int i = 0; i < statusType.length; i++) {
							%>
							<option value=<%=i%> <%if (authStatus == i) {%>
								selected="selected" <%}%>><%=statusType[i]%></option>
							<%
								}
							%>
					</select></td>
				</tr>

			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit"><%=rb.getString("common_search_title") %></button>
							</div>
						</div></li>

					<c:if test="${only == false}">
						<li><div class="button">
								<div class="buttonContent">
									<button type="button" multLookup="userVo"
										warn="<%=rb.getString("common_select_warn")%>">
										<%=rb.getString("common_select_title")%></button>
								</div>
							</div></li>
					</c:if>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">

	<table class="table" width="100%" layoutH="138" targetType="dialog">
		<thead>
			<tr>
				<c:if test="${only == false}">
					<th width="30"><input type="checkbox" class="checkboxCtrl"
						group="userVo" /></th>
				</c:if>
				<th width="80"><%=rb.getString("common_row_title") %></th>
				<th width="120"><%=rb.getString("common_id_title") %></th>
				<th width="120"><%=rb.getString("common_name_title") %></th>
				<th width="120"><%=rb.getString("user_phone_title") %></th>
				<th width="120"><%=rb.getString("user_email_title") %></th>
				<th width="120"><%=rb.getString("user_money_title") %></th>
				<th width="120"><%=rb.getString("user_auth_status_title") %></th>
				<th width="120"><%=rb.getString("common_select_title") %></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" varStatus="status" var="user">
				<tr target="id_user" rel="${user.uid}">
					<c:if test="${only == false}">
						<td><input type="checkbox" name="userVo"
							value="{id:'${user.uid}', name:'${user.userVo.nickName}'
							,phone:'${user.userVo.phoneCode}${user.userVo.phone}',email:'${user.email}'}" /></td>
					</c:if>
					<td>${status.count}</td>
					<td>${user.uid}</td>
					<td>${user.userVo.nickName}</td>
					<td>${user.userVo.phone}</td>
					<td>${user.email}</td>
					<td>${user.userVo.money}</td>
					<td>${user.userVo.authStatusStr}</td>
					<td><a class="btnSelect"
						href="javascript:$.bringBack({id:'${user.uid}', name:'${user.userVo.nickName}
						',phone:'${user.userVo.phoneCode}${user.userVo.phone}',email:'${user.email}'})"
						title="<%=rb.getString("common_select_title") %>"><%=rb.getString("common_select_title") %></a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<%@ include file="../common/page_num_dialog.jspf"%>
</div>